/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 * @author Shahid N. Shah
 */

/**
 * $Id: ReverseEngineerSchemaAction.java,v 1.1 2004-04-28 17:04:32 shahid.shah Exp $
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

    /*****************************************************************************************************************
     ** Setters that correspond to the dialog field names (they will be called automatically by the ActionDialog)    *
     *****************************************************************************************************************/

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
        catch (Exception e)
        {
            log.error(e);
            throw new NestableRuntimeException(e);
        }
    }
}
