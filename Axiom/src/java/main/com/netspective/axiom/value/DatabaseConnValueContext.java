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
 * $Id: DatabaseConnValueContext.java,v 1.3 2003-04-10 13:04:48 shahbaz.javeed Exp $
 */

package com.netspective.axiom.value;

import java.sql.SQLException;

import javax.naming.NamingException;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.ConnectionProvider;
import com.netspective.axiom.SqlManager;
import com.netspective.commons.value.ValueContext;

public interface DatabaseConnValueContext extends ValueContext
{
    public static final String DATASRCID_DEFAULT_DATA_SOURCE = new String();

    /**
     * Given a data source identifier, translate the name to a suitable name for
     * the given ValueContext. For example, if the dataSourceId is null, return
     * the <i>default</i> dataSource. If any per-user or per-request dataSourceId
     * name changes are needed, override this method.
     */
    public String translateDataSourceId(String dataSourceId);

    /**
     * Obtains a connection (with appropriate pooling) for the given dataSourceId. The returnConnection method should
     * be called when the connection is no longer needed.
     * @param dataSourceId the data source identifier to use -- it is passed through the translateDataSourceId() method before use
     * @param transaction true if this will be the start of a transaction (multiple SQL statements) or false for auto-commit
     */
    public ConnectionContext getConnection(String dataSourceId, boolean transaction) throws NamingException, SQLException;

    /**
     * Returns a connection to the pool for the given dataSourceId. If the connection was retrieved for transaction
     * processing (shared connection across multiple SQL statements) then it is the responsiblity of the caller to
     * call rollback() or commit() before returning the connection.
     * @param cc the connection context instance for connection no longer being used
     */
    public void returnConnection(ConnectionContext cc) throws SQLException;

    /**
     * Retrieves the connection provider that should be used for obtaining connections from the database.
     * @return the ConnectionProvider instance that should be used for connections
     */
    public ConnectionProvider getConnectionProvider();

    /**
     * Specifies the connection provider that this value context should use for retrieving connections. If no
     * connection provider is specified, a default one will be used.
     * @param provider
     */
    public void setConnectionProvider(ConnectionProvider provider);

    /**
     * Retrieves the default SQL manager
     */
    public SqlManager getSqlManager();

	/**
	 * Sets the default data source to a user-defined string.
	 * @param defaultDataSource A string representing the new data source
	 */
	public void setDefaultDataSource(String defaultDataSource);

	/**
	 * Retrieves the default data source
	 * @return A string representing the default data source
	 */
	public String getDefaultDataSource();
}
