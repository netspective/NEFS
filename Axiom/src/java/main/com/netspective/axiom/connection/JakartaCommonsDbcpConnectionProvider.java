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
 * $Id: JakartaCommonsDbcpConnectionProvider.java,v 1.1 2003-09-02 17:06:56 roque.hernandez Exp $
 */

package com.netspective.axiom.connection;

import com.netspective.axiom.ConnectionProvider;
import com.netspective.axiom.ConnectionProviderEntries;
import com.netspective.axiom.ConnectionProviderEntry;
import com.netspective.commons.xdm.XmlDataModelSchema;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JakartaCommonsDbcpConnectionProvider implements ConnectionProvider
{
    private static final Log log = LogFactory.getLog(JakartaCommonsDbcpConnectionProvider.class);
    private Map dataSourcesInfo = new HashMap();
    private Map dataSources = new HashMap();

    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    public class DataSourceInfo
    {
        private String dataSourceId;
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

        public String getDataSourceId()
        {
            return dataSourceId;
        }

        public void setDataSourceId(String dataSourceId)
        {
            this.dataSourceId = dataSourceId;
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
    }

    public DataSourceInfo createDataSource(){
        return new DataSourceInfo();
    }

    public void addDataSource(DataSourceInfo dataSourceInfo)
    {
        dataSourcesInfo.put(dataSourceInfo.getDataSourceId(), dataSourceInfo);
    }

    protected DataSource createDataSource(String dataSourceId) throws NamingException {

        DataSourceInfo dataSourceInfo = (DataSourceInfo) dataSourcesInfo.get(dataSourceId);

        if (dataSourceInfo == null)
            throw new NamingException("Data Source: '" + dataSourceId + "' not defined as a data source for Jakarta Commons DBCP provider.");

        try
        {
            Class.forName(dataSourceInfo.driverName);
        }
        catch(ClassNotFoundException cnfe)
        {
            throw new NamingException("Driver '"+ dataSourceInfo.driverName + "' not found for dataSourceId '"+ dataSourceId +"'");
        }

        ObjectPool connectionPool = new GenericObjectPool(null);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dataSourceInfo.connUrl, dataSourceInfo.connUser, dataSourceInfo.connPassword);
        try
        {
            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
        }
        catch (Exception e)
        {
            //TODO: Need to figure out exactly what exception is thrown here.
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

        return dataSource;
    }

    public DataSource getDataSource(String dataSourceId) throws NamingException {

        DataSource dataSource = (DataSource) dataSources.get(dataSourceId);
        if (dataSource == null){
            dataSource = createDataSource(dataSourceId);
            dataSources.put(dataSourceId, dataSource);
        }
        return dataSource;
    }

    public final Connection getConnection(String dataSourceId) throws NamingException, SQLException
    {
        if(dataSourceId == null)
            throw new NamingException("dataSourceId is NULL in " + this.getClass().getName() + ".getConnection(String)");

        DataSource source = getDataSource(dataSourceId);

        if(source == null)
        {
            if(log.isDebugEnabled())
                log.debug("dataSourceId not found in "+ JakartaCommonsDbcpConnectionProvider.class.getName() + ".getConnection('" + dataSourceId + "'). Available: " + getAvailableDataSources());
            throw new NamingException("Data source '" + dataSourceId + "' not found in Jakarta Commons DBCP provider.");
        }

        return source.getConnection();
    }

    public ConnectionProviderEntry getDataSourceEntry(String dataSourceId)
    {
        try
        {
            DataSource source = getDataSource(dataSourceId);
            if(source == null)
                return null;

            return getDataSourceEntry(dataSourceId, source);
        }
        catch(Exception ex)
        {
            log.debug(JakartaCommonsDbcpConnectionProvider.class.getName() + ".getDataSourceEntry('"+ dataSourceId +"')", ex);
            return null;
        }
    }

    public ConnectionProviderEntry getDataSourceEntry(String dataSourceId, DataSource source) throws SQLException
    {
        BasicConnectionProviderEntry result = new BasicConnectionProviderEntry();
        result.init(dataSourceId, source);
        return result;
    }

    public Set getAvailableDataSources()
    {
        return dataSources.keySet();
    }

    public ConnectionProviderEntries getDataSourceEntries()
    {
        ConnectionProviderEntries entries = new BasicConnectionProviderEntries();
        Set available = getAvailableDataSources();
        for(Iterator i = available.iterator(); i.hasNext(); )
        {
            ConnectionProviderEntry entry = getDataSourceEntry((String) i.next());
            if(entry != null)
                entries.add(entry);
        }
        return entries;
    }

    public Class getUnderlyingImplementationClass()
    {
        return DataSourceInfo.class;
    }
}
