/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.axiom.value;

import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.ConnectionProvider;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.connection.AutoCommitConnectionContext;
import com.netspective.axiom.connection.TransactionConnectionContext;
import com.netspective.commons.acl.AccessControlListsManager;
import com.netspective.commons.config.ConfigurationsManager;
import com.netspective.commons.script.ScriptsManager;
import com.netspective.commons.value.DefaultValueContext;

public class BasicDatabaseConnValueContext extends DefaultValueContext implements DatabaseConnValueContext
{
    private static final Log log = LogFactory.getLog(BasicDatabaseConnValueContext.class);

    private ConnectionProvider provider;

    protected String defaultDataSource = DatabaseConnValueContext.DATASRCID_DEFAULT_DATA_SOURCE;

    public ConnectionContext getConnection(String dataSourceId, boolean transaction) throws NamingException, SQLException
    {
        ConnectionContext result = null;
        if(transaction)
            result = new TransactionConnectionContext(dataSourceId, this);
        else
            result = new AutoCommitConnectionContext(dataSourceId, this);

        if(log.isTraceEnabled())
            log.trace("Obtained " + result + " for data source '" + result.getDataSourceId() + "'.");

        return result;
    }

    public ConnectionContext getConnection(String dataSourceId, boolean transaction, boolean throwRuntimeException)
    {
        try
        {
            return getConnection(dataSourceId, transaction);
        }
        catch(NamingException e)
        {
            if(throwRuntimeException)
                throw new NestableRuntimeException(e);
            else
            {
                log.error("Error getting connection", e);
                return null;
            }
        }
        catch(SQLException e)
        {
            if(throwRuntimeException)
                throw new NestableRuntimeException(e);
            else
            {
                log.error("Error getting connection", e);
                return null;
            }
        }
    }


    public ConnectionProvider getConnectionProvider()
    {
        return provider == null ? getSqlManager().getConnectionProvider() : provider;
    }

    public void returnConnection(ConnectionContext cc) throws SQLException
    {
        if(log.isTraceEnabled())
            log.trace("Returned " + cc + " for data source '" + cc.getDataSourceId() + "'.");
        cc.close();
    }

    public void setConnectionProvider(ConnectionProvider provider)
    {
        this.provider = provider;
    }

    public String translateDataSourceId(String dataSourceId)
    {
        return dataSourceId;
    }

    public ScriptsManager getScriptsManager()
    {
        return getSqlManager();
    }

    public AccessControlListsManager getAccessControlListsManager()
    {
        return getSqlManager();
    }

    public ConfigurationsManager getConfigurationsManager()
    {
        return getSqlManager();
    }

    public SqlManager getSqlManager()
    {
        return null;
    }

    /**
     * Sets the default data source to a user-defined string.
     *
     * @param defaultDataSource A string representing the new data source
     */
    public void setDefaultDataSource(String defaultDataSource)
    {
        this.defaultDataSource = defaultDataSource;
    }

    /**
     * Retrieves the default data source
     *
     * @return A string representing the default data source
     */
    public String getDefaultDataSource()
    {
        return null == defaultDataSource ? DatabaseConnValueContext.DATASRCID_DEFAULT_DATA_SOURCE : defaultDataSource;
    }
}
