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
 * $Id: ConnectionContext.java,v 1.4 2003-08-31 22:40:12 shahid.shah Exp $
 */

package com.netspective.axiom;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;

import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.axiom.value.DatabasePolicyValueContext;
import com.netspective.axiom.connection.ConnectionContextNotClosedException;
import com.netspective.commons.value.ValueContext;

public interface ConnectionContext extends DatabasePolicyValueContext
{
    /**
     * Is this a shared connection across a user's session?
     * @return True if this connection is bound to a user's session or false if it belongs to the application
     */
    public boolean isBoundToSession();

    /**
     * Retrieve the DatabaseValueContext instance that created this object.
     */
    public DatabaseConnValueContext getDatabaseValueContext();

    /**
     * Retrieve the connection instance associated with this context. If the method is being called the first time for
     * any given instance it will create the connection and then return it. If the method is being called the second
     * time or later, then the previously created connection is returned (allowing connection sharing).
     */
    public Connection getConnection() throws NamingException, SQLException;

    /**
     * Retrieve the data source identifier associated with this connection.
     */
    public String getDataSourceId();

    /**
     * Close the connection context and free up any associated resources
     */
    public void close() throws SQLException;

    /**
     * Called when the connection context should close the connection context and free up any associated resources
     * but is being done so implicitly by the system (such as a session timing out) instead of explicity via an API.
     */
    public void timeOut() throws SQLException;

    public void commitAndClose() throws SQLException;

    public void rollbackAndClose() throws SQLException;

    /**
     * When a connection is created, an exception for that connection not being closed is created and stored
     * (for stack trace purposes). If the connection is properly closed, the exception is not important. If, however,
     * the connection context remains open (such as at the end of an application, end of a session, etc) the exception
     * is available to be thrown and it will have the stack trace of the original code where the connection was created.
     * @return
     */
    public ConnectionContextNotClosedException getContextNotClosedException();

    /**
     * This method is useful in error handlers at application closing or other times when this connection should be
     * considered a connection leak and log message should be presented.
     * @param log The log to use to send the error to
     * @param message If null, a default message will appear before the stack trace of the originating cc opener is displayed
     */
    public void rollbackAndCloseAndLogAsConnectionLeak(Log log, String message);
}
