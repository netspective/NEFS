/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: AbstractConnectionContext.java,v 1.1 2003-03-13 18:25:39 shahid.shah Exp $
 */

package com.netspective.axiom.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.DatabasePolicy;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.value.DatabaseConnValueContext;

public abstract class AbstractConnectionContext implements ConnectionContext
{
    private static final Log log = LogFactory.getLog(AbstractConnectionContext.class);

    private DatabaseConnValueContext dbvc;
    private DatabasePolicy dbPolicy;
    private String dataSourceId;
    private Connection connection;

    public AbstractConnectionContext(String dataSourceId, DatabaseConnValueContext dbvc)
    {
        this.dataSourceId = dataSourceId;
        this.dbvc = dbvc;
    }

    public void initializeConnection(Connection conn) throws SQLException
    {
    }

    public Connection getConnection() throws NamingException, SQLException
    {
        if(connection == null)
        {
            connection = dbvc.getConnectionProvider().getConnection(dataSourceId);
            if(log.isTraceEnabled())
                log.trace("Obtained " + connection + " for data source '"+ getDataSourceId() +"'.");
            initializeConnection(connection);
        }
        return connection;
    }

    public DatabasePolicy getDatabasePolicy() throws NamingException, SQLException
    {
        if(dbPolicy == null)
            dbPolicy = DatabasePolicies.getInstance().getDatabasePolicy(getConnection());
        return dbPolicy;
    }

    public DatabaseConnValueContext getDatabaseValueContext()
    {
        return dbvc;
    }

    public String getDataSourceId()
    {
        return dataSourceId;
    }

    public void close() throws SQLException
    {
        if(connection != null)
        {
            if(log.isTraceEnabled())
                log.trace("Closed " + connection + " for data source '"+ getDataSourceId() +"'.");
            connection.close();
            connection = null;
        }
    }

    public void commitAndClose() throws SQLException
    {
        if(connection != null)
        {
            connection.commit();
            close();
        }
    }

    public void rollbackAndClose() throws SQLException
    {
        if(connection != null)
        {
            connection.rollback();
            close();
        }
    }

    /***************************************************************/
    /* --- delegate all other calls to the parent value context -- */
    /***************************************************************/

    public Object getAttribute(String attributeId)
    {
        return dbvc.getAttribute(attributeId);
    }

    public Object getContextLocation()
    {
        return dbvc.getContextLocation();
    }

    public boolean inMaintenanceMode()
    {
        return dbvc.inMaintenanceMode();
    }

    public boolean isAntBuildEnvironment()
    {
        return dbvc.isAntBuildEnvironment();
    }

    public boolean isDemonstrationEnvironment()
    {
        return dbvc.isDemonstrationEnvironment();
    }

    public boolean isDevelopmentEnvironment()
    {
        return dbvc.isDevelopmentEnvironment();
    }

    public boolean isTrainingEnvironment()
    {
        return dbvc.isTrainingEnvironment();
    }

    public boolean isProductionEnvironment()
    {
        return dbvc.isProductionEnvironment();
    }

    public boolean isTestEnvironment()
    {
        return dbvc.isTestEnvironment();
    }

    public void removeAttribute(String attributeId)
    {
        dbvc.removeAttribute(attributeId);
    }

    public void setAttribute(String attributeId, Object attributeValue)
    {
        dbvc.setAttribute(attributeId, attributeValue);
    }

    public void setContextLocation(Object locator)
    {
        dbvc.setContextLocation(locator);
    }
}
