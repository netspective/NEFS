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
 * $Id: BasicConnectionProviderEntry.java,v 1.1 2003-03-13 18:25:39 shahid.shah Exp $
 */

package com.netspective.axiom.connection;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.netspective.axiom.ConnectionProviderEntry;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.DatabasePolicy;

public class BasicConnectionProviderEntry extends HashMap implements ConnectionProviderEntry
{
    public static final String KEYNAME_DATA_SOURCE_ID = "DataSource Identifier";
    public static final String KEYNAME_DATA_SOURCE_CLASS = "DataSource Class";
    public static final String KEYNAME_DRIVER_NAME = "Driver Name";
    public static final String KEYNAME_DRIVER_VERSION = "Driver Version";
    public static final String KEYNAME_DATABASE_POLICY_CLASSNAME = "Database Policy Class";
    public static final String KEYNAME_DATABASE_POLICY_DBMSID = "Database Policy DBMS Identifer";
    public static final String KEYNAME_DATABASE_PRODUCT_NAME = "Database Product";
    public static final String KEYNAME_DATABASE_PRODUCT_VERSION = "Database Product Version";
    public static final String KEYNAME_URL = "URL";
    public static final String KEYNAME_USER_NAME = "Username";
    public static final String KEYNAME_RESULTSET_TYPE = "ResultSet Type";
    public static final String KEYNAME_EXCEPTION = "Exception";

    public BasicConnectionProviderEntry(String dataSourceId, DataSource dataSource, Connection conn)
    {
        super();
        init(dataSourceId, dataSource, conn);
    }

    public void init(String dataSourceId, DataSource dataSource, Connection conn)
    {
        put(KEYNAME_DATA_SOURCE_ID, dataSourceId);
        try
        {
            DatabaseMetaData dbmd = conn.getMetaData();

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

            put(KEYNAME_DRIVER_NAME, dbmd.getDriverName());
            put(KEYNAME_DATA_SOURCE_CLASS, dataSource != null ? dataSource.getClass().getName() : "<NULL>");
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
        }
        catch(Exception e)
        {
            put(KEYNAME_EXCEPTION, e.toString());
        }
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
        return (String) get(KEYNAME_DATA_SOURCE_ID);
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
}
