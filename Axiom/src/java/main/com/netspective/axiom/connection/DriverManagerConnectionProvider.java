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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionProvider;
import com.netspective.axiom.ConnectionProviderEntries;
import com.netspective.axiom.ConnectionProviderEntry;
import com.netspective.commons.value.ValueContext;

public class DriverManagerConnectionProvider implements ConnectionProvider
{
    public static final DriverManagerConnectionProvider PROVIDER = new DriverManagerConnectionProvider();
    private static final Log log = LogFactory.getLog(DriverManagerConnectionProvider.class);

    public class DataSourceInfo
    {
        private String driverName;
        private String connUrl;
        private String connUser;
        private String connPassword;

        public DataSourceInfo()
        {
        }

        public DataSourceInfo(String driverName, String connUrl, String connUser, String connPassword)
        {
            this.driverName = driverName;
            this.connUrl = connUrl;
            this.connUser = connUser;
            this.connPassword = connPassword;
        }

        public String getDriverName()
        {
            return driverName;
        }

        public void setDriverName(String driverName)
        {
            this.driverName = driverName;
        }

        public String getConnUrl()
        {
            return connUrl;
        }

        public void setConnUrl(String connUrl)
        {
            this.connUrl = connUrl;
        }

        public String getConnUser()
        {
            return connUser;
        }

        public void setConnUser(String connUser)
        {
            this.connUser = connUser;
        }

        public String getConnPassword()
        {
            return connPassword;
        }

        public void setConnPassword(String connPassword)
        {
            this.connPassword = connPassword;
        }

        public boolean setInfo(List dsInfoList)
        {
            if(dsInfoList.size() < 2)
                return false;

            setDriverName(dsInfoList.get(0).toString());
            setConnUrl(dsInfoList.get(1).toString());
            if(dsInfoList.size() > 2)
                setConnUser(dsInfoList.get(2).toString());
            if(dsInfoList.size() > 3)
                setConnPassword(dsInfoList.get(3).toString());

            return true;
        }

        public boolean setInfo(Map dsInfoMap, String keyPrefix)
        {
            String driverName = dsInfoMap.get(keyPrefix != null
                                              ? keyPrefix + "jdbc-driver-class" : "jdbc-driver-class").toString();
            String connUrl = dsInfoMap.get(keyPrefix != null
                                           ? keyPrefix + "jdbc-connection-url" : "jdbc-connection-url").toString();
            String connUser = dsInfoMap.get(keyPrefix != null
                                            ? keyPrefix + "jdbc-connection-user" : "jdbc-connection-user").toString();
            String connPassword = dsInfoMap.get(keyPrefix != null
                                                ? keyPrefix + "jdbc-connection-password" : "jdbc-connection-password").toString();

            if(driverName == null || connUrl == null)
                return false;

            setDriverName(driverName);
            setConnUrl(connUrl);

            if(connUser != null)
                setConnUser(connUser);

            if(connPassword != null)
                setConnUser(connPassword);

            return true;
        }

        public boolean setInfo(String[] dsInfoArray)
        {
            if(dsInfoArray.length < 2)
                return false;

            setDriverName(dsInfoArray[0]);
            setConnUrl(dsInfoArray[1]);

            if(dsInfoArray.length > 2)
                setConnUser(dsInfoArray[2]);
            if(dsInfoArray.length > 3)
                setConnPassword(dsInfoArray[3]);

            return true;
        }

        public String toString()
        {
            StringBuffer sb = new StringBuffer();
            sb.append("[" + getClass().getName() + ": " + getDriverName() + ", " + getConnUrl() + ", " + getConnUser() + ", " + getConnPassword() + "]");
            return sb.toString();
        }
    }

    private Map dataSources = new HashMap();
    private String name = "driver-manager";

    public String getConnectionProviderName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Class getUnderlyingImplementationClass()
    {
        return DataSourceInfo.class;
    }

    public DataSourceInfo createDataSource()
    {
        return new DataSourceInfo();
    }

    public DataSourceInfo getDataSourceInfo(String dataSourceId)
    {
        return (DataSourceInfo) dataSources.get(dataSourceId);
    }

    public void addDataSourceInfo(String dataSourceId, String driverName, String connUrl, String connUser, String connPassword)
    {
        addDataSourceInfo(dataSourceId, new DataSourceInfo(driverName, connUrl, connUser, connPassword));
    }

    public void addDataSourceInfo(String dataSourceId, DataSourceInfo dataSourceInfo)
    {
        dataSources.put(dataSourceId, dataSourceInfo);
    }

    public Connection getConnection(ValueContext vc, String dataSourceId) throws NamingException, SQLException
    {
        DataSourceInfo dsInfo = getDataSourceInfo(dataSourceId);
        if(dsInfo != null)
        {
            try
            {
                Class.forName(dsInfo.getDriverName());
            }
            catch(ClassNotFoundException cnfe)
            {
                throw new NamingException("Driver '" + dsInfo.getDriverName() + "' not found for dataSourceId '" + dataSourceId + "'");
            }
            return DriverManager.getConnection(dsInfo.getConnUrl(), dsInfo.getConnUser(), dsInfo.getConnPassword());
        }
        else
            throw new NamingException("Information for DataSource '" + dataSourceId + "' not found.");
    }

    public DataSource getDataSource(ValueContext vc, String dataSourceId) throws NamingException, SQLException
    {
        throw new RuntimeException("Not implemented -- please use getConnection() instead.");
    }

    public Set getAvailableDataSources()
    {
        return dataSources.keySet();
    }

    public ConnectionProviderEntries getDataSourceEntries(ValueContext vc)
    {
        ConnectionProviderEntries entries = new BasicConnectionProviderEntries();
        Set available = getAvailableDataSources();
        for(Iterator i = available.iterator(); i.hasNext();)
        {
            ConnectionProviderEntry entry = getDataSourceEntry(vc, (String) i.next());
            if(entry != null)
                entries.add(entry);
        }
        return entries;
    }

    public ConnectionProviderEntry getDataSourceEntry(ValueContext vc, String dataSourceId)
    {
        DataSourceInfo dsInfo = getDataSourceInfo(dataSourceId);
        if(dsInfo != null)
        {
            try
            {
                Class.forName(dsInfo.getDriverName());
            }
            catch(ClassNotFoundException cnfe)
            {
                log.debug(DriverManagerConnectionProvider.class.getName() + ".getDataSourceEntry('" + dataSourceId + "')", cnfe);
                return null;
            }

            try
            {
                Connection conn = DriverManager.getConnection(dsInfo.getConnUrl(), dsInfo.getConnUser(), dsInfo.getConnPassword());
                BasicConnectionProviderEntry entry = new BasicConnectionProviderEntry();
                entry.init(dataSourceId, conn); // the connection will be closed in the init method
                return entry;
            }
            catch(SQLException e)
            {
                log.debug(DriverManagerConnectionProvider.class.getName() + ".getDataSourceEntry('" + dataSourceId + "')", e);
                return null;
            }
        }
        else
            return null;
    }
}
