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
 * $Id: AbstractConnectionContext.java,v 1.18 2004-04-27 04:05:00 shahid.shah Exp $
 */

package com.netspective.axiom.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.DatabasePolicy;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.commons.RuntimeEnvironmentFlags;
import com.netspective.commons.acl.AccessControlListsManager;
import com.netspective.commons.config.ConfigurationsManager;
import com.netspective.commons.script.ScriptsManager;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.text.TextUtils;

public abstract class AbstractConnectionContext implements ConnectionContext
{
    private static final Log log = LogFactory.getLog(AbstractConnectionContext.class);
    private static final Set connectionContextsWithOpenConnections = Collections.synchronizedSet(new HashSet());

    public static final Set getConnectionContextsWithOpenConnections()
    {
        return connectionContextsWithOpenConnections;
    }

    private DatabaseConnValueContext dbvc;
    private DatabasePolicy dbPolicy;
    private String dataSourceId;
    private Connection connection;
    private long creationTime;
    private ConnectionContextNotClosedException contextNotClosedException;

    public AbstractConnectionContext(String dataSourceId, DatabaseConnValueContext dbvc)
    {
        this.creationTime = System.currentTimeMillis();
        this.dataSourceId = dataSourceId;
        this.dbvc = dbvc;
    }

    public void initializeConnection(Connection conn) throws SQLException
    {
    }

    public boolean isBoundToSession()
    {
        return false;
    }

    public Connection getConnection() throws NamingException, SQLException
    {
        if(connection == null)
        {
            connection = dbvc.getConnectionProvider().getConnection(this, getDataSourceId());

            // hang on to the point where the connection was created so that we can throw this if the connection is
            // not properly closed
            this.contextNotClosedException = new ConnectionContextNotClosedException(this);
            connectionContextsWithOpenConnections.add(this);

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
        return dataSourceId == null ? dbvc.getDefaultDataSource() : dataSourceId;
    }

    public void close() throws SQLException
    {
        if(connection != null)
        {
            if(log.isTraceEnabled())
                log.trace("Closed " + connection + " for data source '"+ getDataSourceId() +"'.");
            connection.close();
            connection = null;
            connectionContextsWithOpenConnections.remove(this);
        }
    }

    public void timeOut() throws SQLException
    {
        if(log.isTraceEnabled())
            log.trace("Connection " + connection + " for data source '"+ getDataSourceId() +"' timed-out.");
        rollbackAndClose();
    }

    public void commitAndClose() throws SQLException
    {
        if(connection != null)
        {
            try
            {
                connection.commit();
            }
            finally
            {
                close();
            }
        }
    }

    public void rollbackAndClose() throws SQLException
    {
        if(connection != null)
        {
            try
            {
                connection.rollback();
            }
            finally
            {
                close();
            }
        }
    }

    public ConnectionContextNotClosedException getContextNotClosedException()
    {
        return contextNotClosedException;
    }

    public String getConnectionOpenStackStrace()
    {
        if(contextNotClosedException == null)
            return null;

        return TextUtils.getStackTrace(contextNotClosedException);
    }

    public void rollbackAndCloseAndLogAsConnectionLeak(Log log, String message)
    {
        if(message == null)
            message = "** CONNECTION LEAK DETECTED ** Connection for DataSource '"+ getDataSourceId() +"' not closed -- rolling back and forcing close now.\n";

        if(log.isErrorEnabled())
            log.error(message, getContextNotClosedException());
        else
        {
            System.err.println(message);
            getContextNotClosedException().printStackTrace(System.err);
        }
        try
        {
            rollbackAndClose();
        }
        catch (SQLException e)
        {
            log.error("Unable to close leaking connection", e);
        }
    }

    /*-------------------------------------------------------------
     delegate all other calls to the parent value context
     --------------------------------------------------------------*/

    public long getCreationTime()
    {
        return creationTime;
    }

    public Date getCreationDate()
    {
        return new Date(creationTime);
    }

    public AccessControlListsManager getAccessControlListsManager()
    {
        return dbvc.getAccessControlListsManager();
    }

    public ConfigurationsManager getConfigurationsManager()
    {
        return dbvc.getConfigurationsManager();
    }

    public AuthenticatedUser getAuthenticatedUser()
    {
        return dbvc.getAuthenticatedUser();
    }

    public SqlManager getSqlManager()
    {
        return dbvc.getSqlManager();
    }

    public Object getAttribute(String attributeId)
    {
        return dbvc.getAttribute(attributeId);
    }

    public Object getContextLocation()
    {
        return dbvc.getContextLocation();
    }

    public RuntimeEnvironmentFlags getRuntimeEnvironmentFlags()
    {
        return dbvc.getRuntimeEnvironmentFlags();
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

    public boolean isConditionalExpressionTrue(String expr, Map vars)
    {
        return dbvc.isConditionalExpressionTrue(expr, vars);
    }

    public Object evaluateExpression(String expr, Map vars)
    {
        return dbvc.evaluateExpression(expr, vars);
    }

    public ScriptsManager getScriptsManager()
    {
        return dbvc.getScriptsManager();
    }
}
