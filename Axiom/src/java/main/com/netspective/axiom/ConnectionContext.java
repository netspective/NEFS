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
 * $Id: ConnectionContext.java,v 1.3 2003-08-17 00:00:05 shahid.shah Exp $
 */

package com.netspective.axiom;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.axiom.value.DatabasePolicyValueContext;
import com.netspective.axiom.connection.ConnectionContextNotClosedException;
import com.netspective.commons.value.ValueContext;

public interface ConnectionContext extends DatabasePolicyValueContext
{
    public final static int OWNERSHIP_DEFAULT            = 0;
    public final static int OWNERSHIP_AUTHENTICATED_USER = 1;

    /**
     * Retrieve the DatabaseValueContext instance that created this object.
     */
    public DatabaseConnValueContext getDatabaseValueContext();

    public int getOwnership();

    public void setOwnership(int ownership);

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
}
