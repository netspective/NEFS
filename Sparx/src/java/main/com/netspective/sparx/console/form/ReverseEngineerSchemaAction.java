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
package com.netspective.sparx.console.form;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionContext;

public class ReverseEngineerSchemaAction
{
    private static final Log log = LogFactory.getLog(ReverseEngineerSchemaAction.class);

    private static final int DATASRCINFO_DRIVER_CLASS = 0;
    private static final int DATASRCINFO_CONN_URL = 1;
    private static final int DATASRCINFO_CONN_USER = 2;
    private static final int DATASRCINFO_CONN_PASSWORD = 3;

    private String dataSourceName;
    private String[] dataSourceInfo = new String[4];
    private String catalogName;
    private String schemaPattern;
    private File destination;
    private ConnectionContext connectionContext;

    public ReverseEngineerSchemaAction()
    {
    }

    /**
     * **************************************************************************************************************
     * * Setters that correspond to the dialog field names (they will be called automatically by the ActionDialog)    *
     * ***************************************************************************************************************
     */

    public void setCatalogName(String catalogName)
    {
        this.catalogName = catalogName;
    }

    public void setDataSourceName(String dataSourceName)
    {
        this.dataSourceName = dataSourceName;
    }

    public void setDestination(File destination)
    {
        this.destination = destination;
    }

    public void setJdbcDriverName(String jdbcDriverName)
    {
        dataSourceInfo[DATASRCINFO_DRIVER_CLASS] = jdbcDriverName;
    }

    public void setJdbcConnectionUrl(String jdbcDriverUrl)
    {
        dataSourceInfo[DATASRCINFO_CONN_URL] = jdbcDriverUrl;
    }

    public void setJdbcConnectionPassword(String jdbcPassword)
    {
        dataSourceInfo[DATASRCINFO_CONN_USER] = jdbcPassword;
    }

    public void setJdbcConnectionUser(String jdbcUser)
    {
        dataSourceInfo[DATASRCINFO_CONN_PASSWORD] = jdbcUser;
    }

    public void setSchemaPattern(String schemaPattern)
    {
        this.schemaPattern = schemaPattern;
    }

    /*****************************************************************************************************************
     ** Callback methods provided to ActionDialog so it can fill in items we need                                    *
     *****************************************************************************************************************/

    /**
     * Callback for action class that provides it a data source name. If this method returns a non-null value then
     * the framework will attempt to locate this data source name via JNDI (or other connection provider) and call
     * the setConnection() method appropriately.
     *
     * @return A value source specification or static string that supplies a valid data source name
     */
    public String getDataSourceName()
    {
        if(dataSourceName != null && dataSourceName.trim().length() == 0)
            return null;

        return dataSourceName;
    }

    /**
     * This is a callback that provides dynamic datasource information for the case where we do not have a defined
     * data source but we do know the driver class, connection url, user name, and password. If the framework finds
     * the return value of getDataSourceName() is null, it will attempt to use this method to get information to open
     * a connection.
     *
     * @return A string array supplying the data source information for creating a dynamic connection
     */
    public String[] getDataSourceInfo()
    {
        return dataSourceInfo;
    }

    /**
     * Using the information from either the getDataSourceName() or getDataSourceInfo() methods (whichever returns a
     * non-null value) the framework will open a connection and assign it to us via this method. We don't have to worry
     * about closing it since the connection is already in transaction mode and will be closed for us at the end of the
     * execute.
     *
     * @param connectionContext The connection context automatically opened for us by the framework
     */
    public void setConnectionContext(ConnectionContext connectionContext)
    {
        this.connectionContext = connectionContext;
    }

    /**
     * The dialog execute method is called as soon as all data is entered and validated.
     * This method is guaranteed to only be called when all fields' data is valid. We
     * are not returning a value because we want the container to automatically manage
     * the next action (in our case, stay where its at to show our message).
     */
    public void execute(Writer writer) throws IOException
    {
        try
        {
            connectionContext.getDatabasePolicy().reverseEngineer(destination, connectionContext.getConnection(), catalogName, schemaPattern);
            writer.write(destination + " created.");
        }
        catch(Exception e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }
    }
}
