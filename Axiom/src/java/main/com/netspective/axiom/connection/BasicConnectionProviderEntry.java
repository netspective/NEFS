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
package com.netspective.axiom.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionProviderEntry;
import com.netspective.axiom.ConnectionProviderEntryStatistics;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.DatabasePolicy;

public class BasicConnectionProviderEntry extends HashMap implements ConnectionProviderEntry
{
    private static final Log log = LogFactory.getLog(BasicConnectionProviderEntry.class);
    private static final Map STATISTICS_PROVIDERS = new HashMap();

    public static final String KEYNAME_DRIVER_NAME = "Driver Name";
    public static final String KEYNAME_DRIVER_VERSION = "Driver Version";
    public static final String KEYNAME_DATABASE_POLICY_CLASSNAME = "Database Policy Class";
    public static final String KEYNAME_DATABASE_POLICY_DBMSID = "Database Policy DBMS Identifer";
    public static final String KEYNAME_DATABASE_PRODUCT_NAME = "Database Product";
    public static final String KEYNAME_DATABASE_PRODUCT_VERSION = "Database Product Version";
    public static final String KEYNAME_URL = "URL";
    public static final String KEYNAME_USER_NAME = "Username";
    public static final String KEYNAME_RESULTSET_TYPE = "ResultSet Type";

    private boolean valid;
    private Throwable exception;
    private String dataSourceId;
    private DataSource dataSource;

    public void init(String dataSourceId, Connection conn)
    {
        this.dataSourceId = dataSourceId;

        try
        {
            try
            {
                DatabasePolicy policy = DatabasePolicies.getInstance().getDatabasePolicy(conn);
                put(KEYNAME_DATABASE_POLICY_CLASSNAME, policy.getClass().getName());
                put(KEYNAME_DATABASE_POLICY_DBMSID, policy.getDbmsIdentifier());
            }
            catch(Exception dpe)
            {
                put(KEYNAME_DATABASE_POLICY_CLASSNAME, dpe.toString());
            }

            DatabaseMetaData dbmd = conn.getMetaData();

            put(KEYNAME_DRIVER_NAME, dbmd.getDriverName());
            put(KEYNAME_DATABASE_PRODUCT_NAME, dbmd.getDatabaseProductName());
            put(KEYNAME_DATABASE_PRODUCT_VERSION, dbmd.getDatabaseProductVersion());
            put(KEYNAME_DRIVER_VERSION, dbmd.getDriverVersion());
            put(KEYNAME_URL, dbmd.getURL());
            put(KEYNAME_USER_NAME, dbmd.getUserName());

            String resultSetType = "unknown";
            if(dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE))
                resultSetType = "scrollable (insensitive)";
            else if(dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE))
                resultSetType = "scrollable (sensitive)";
            else if(dbmd.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY))
                resultSetType = "non-scrollabe (forward only)";
            put(KEYNAME_RESULTSET_TYPE, resultSetType);

            valid = true;
        }
        catch(Exception e)
        {
            exception = e;
        }
        finally
        {
            try
            {
                conn.close();
            }
            catch(SQLException e)
            {
                log.error("SQL Exception while closing connection", e);
            }
        }
    }

    public void init(String dataSourceId, DataSource dataSource)
    {
        try
        {
            this.dataSource = dataSource;
            init(dataSourceId, dataSource.getConnection());
        }
        catch(SQLException e)
        {
            this.dataSourceId = dataSourceId;
            exception = e;
            log.error("SQL Exception while obtaining connection", e);
        }
    }

    public static void registerStatisticsProvider(ConnectionProviderEntryStatistics stats)
    {
        STATISTICS_PROVIDERS.put(stats.getImplementationClassName(), stats.getClass());
    }

    public Throwable getException()
    {
        return exception;
    }

    public boolean isValid()
    {
        return valid;
    }

    public String getDatabasePolicyClassName()
    {
        return (String) get(KEYNAME_DATABASE_POLICY_CLASSNAME);
    }

    public String getDatabaseProductName()
    {
        return (String) get(KEYNAME_DATABASE_PRODUCT_NAME);
    }

    public String getDatabaseProductVersion()
    {
        return (String) get(KEYNAME_DATABASE_PRODUCT_VERSION);
    }

    public String getDataSourceId()
    {
        return dataSourceId;
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public String getDriverName()
    {
        return (String) get(KEYNAME_DRIVER_NAME);
    }

    public String getDriverVersion()
    {
        return (String) get(KEYNAME_DRIVER_VERSION);
    }

    public String getResultSetType()
    {
        return (String) get(KEYNAME_RESULTSET_TYPE);
    }

    public String getURL()
    {
        return (String) get(KEYNAME_URL);
    }

    public String getUserName()
    {
        return (String) get(KEYNAME_USER_NAME);
    }

    public ConnectionProviderEntryStatistics getStatistics()
    {
        if(dataSource == null)
            return null;

        Class statsClass = (Class) STATISTICS_PROVIDERS.get(dataSource.getClass().getName());
        if(statsClass == null)
        {
            log.error("Unable to find a statistics provider for class " + dataSource.getClass().getName());
            return null;
        }

        try
        {
            ConnectionProviderEntryStatistics stats = (ConnectionProviderEntryStatistics) statsClass.newInstance();
            stats.setConnectionProviderEntry(this);
            return stats;
        }
        catch(Exception e)
        {
            log.error("Error instantiating statistics provider ", e);
            return null;
        }
    }
}
