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
 * $Id: DatabasePolicy.java,v 1.5 2004-06-11 11:40:11 shahid.shah Exp $
 */

package com.netspective.axiom;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.netspective.axiom.policy.SqlDdlFormats;
import com.netspective.axiom.policy.SqlDdlGenerator;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.column.type.GuidColumn;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.dynamic.QueryDefnSelectStmtGenerator;

public interface DatabasePolicy
{
    /**
     * This flag should be passed to the DML (insert/update/remove) methods if, in addition to generating the SQL, the
     * statement should be executed.
     */
    public static final int DMLFLAG_EXECUTE = 1;

    /**
     * This flag should be passed to the DML (insert/update/remove) methods if bind parameters should be used for all
     * the DML values instead of SQL injection.
     */
    public static final int DMLFLAG_USE_BIND_PARAMS = DMLFLAG_EXECUTE * 2;

    /**
     * Returns a string identifier that can be used in a database schema to specify dbms-specific SQL that might need
     * to be used. This ID should be the same as the ID used for the DDL generator and is considered the "primary id".
     */
    public String getDbmsIdentifier();

    /**
     * Returns a string array that lists all the possible identifiers that may be used to retrieve this policy from a
     * factory. This will include at least the primary id and any aliases (such as the JDBC database product names).
     */
    public String[] getDbmsIdentifiers();

    /**
     * When an auto increment value is needed in a SQL insert, this method is called before the execution of the insert
     * statement. This gives the DatabasePolicy to go get a sequence value or do whatever is
     * necessary in order to fill in the auto inc value into the columnNames, columnValues (which are the DML items
     * being filled out by the DmlTask object. In case sequences are not used, this method will just do nothing and
     * wait for the handleAutoIncPostDmlExecute to be called.
     *
     * @param cc     The database connection to use
     * @param column The column
     *
     * @return The auto inc value generated by the database or null if not known yet
     */
    public Object handleAutoIncPreDmlInsertExecute(ConnectionContext cc, AutoIncColumn column) throws SQLException;

    /**
     * When an auto increment value is needed in a SQL DML, this method is called after the execution of the DML
     * (insert, update, or delete). This gives the DatabasePolicy to go get a sequence value or do whatever is
     * necessary in order to return the auto inc value.
     *
     * @param cc                 The database connection to use
     * @param column             The column that is the auto inc column
     * @param autoIncColumnValue The value of the auto inc column returned by handleAutoIncPreDmlExecute method
     *
     * @return The auto inc value generated by the database or null if not known yet
     */
    public Object handleAutoIncPostDmlInsertExecute(ConnectionContext cc, AutoIncColumn column, Object autoIncColumnValue) throws SQLException;

    /**
     * Gets the current value of an auto increment column.
     *
     * @param cc     The database connection to use
     * @param column The column that is the auto inc column
     *
     * @return The auto inc value generated by the database or null if not known yet
     */
    public Object getAutoIncCurrentValue(ConnectionContext cc, AutoIncColumn column) throws SQLException;

    /**
     * Returns a boolean value whether to retain autoinc columns in any
     * insert DMLs.  Required because SQL Server does not allow
     * including the column in either case, generating an exception if it is
     */
    public boolean retainAutoIncColInInsertDml();

    /**
     * Returns a boolean value whether to retain autoinc columns in any
     * update DMLs.  Required because SQL Server does not allow
     * including the column in either case, generating an exception if it is
     */
    public boolean retainAutoIncColInUpdateDml();

    /**
     * When a globally unique ID (GUID) is needed in a SQL insert, this method is called before the execution of the insert
     * statement. This gives the DatabasePolicy a chance to create a unique ID value or do whatever is
     * necessary in order to fill in the GUID value into the columnNames, columnValues (which are the DML items
     * being filled out by the DmlTask object.
     *
     * @param cc     The database connection to use
     * @param column The column that is the guid column
     *
     * @return The GUID value generated or null if not known yet
     */
    public Object handleGUIDPreDmlInsertExecute(ConnectionContext cc, GuidColumn column) throws SQLException;

    /**
     * When a globally unique ID (GUID) is needed in a SQL DML, this method is called after the execution of the DML
     * (insert, update, or delete).
     *
     * @param cc              The database connection to use
     * @param column          The column that is the guid column
     * @param GUIDColumnValue The value of the column returned by handleAutoIncPreDmlExecute method
     *
     * @return The auto inc value generated by the database or null if not known yet
     */
    public Object handleGUIDPostDmlInsertExecute(ConnectionContext cc, GuidColumn column, Object GUIDColumnValue) throws SQLException;

    /**
     * Returns a boolean value whether to retain GUIDs in any
     * insert DMLs.
     */
    public boolean retainGUIDColInInsertDml();

    /**
     * Returns a boolean value whether to retain GUIDs in any
     * update DMLs.
     */
    public boolean retainGUIDColInUpdateDml();

    /**
     * Reverse engineer the catalog from the given connection into a valid XML file that may be processed by Axiom.
     *
     * @param writer  The XML destination
     * @param conn    The connection
     * @param catalog The catalog to reverse engineer
     */
    public void reverseEngineer(Writer writer, Connection conn, String catalog, String schemaPattern) throws IOException, SQLException;

    /**
     * Reverse engineer the catalog from the given connection into a valid XML file that may be processed by Axiom.
     *
     * @param output  The XML destination
     * @param conn    The connection
     * @param catalog The catalog to reverse engineer
     */
    public void reverseEngineer(File output, Connection conn, String catalog, String schemaPattern) throws IOException, SQLException;

    /**
     * When generating any SQL with bind parameters, should comments about the bind information (param num, etc) be placed
     * in the SQL? Some databases get syntax errors when doing that.
     */
    boolean isPlaceBindComments();

    /**
     * Specificy whether should comments about the bind information (param num, etc) be placed in the SQL? Some databases get syntax errors
     * when doing that.
     */
    void setPlaceBindComments(boolean placeBindComments);

    /**
     * Insert values into a database managed by this policy.
     *
     * @param cc           The connection context (required only if flags has DMLFLAG_EXECUTE set, null otherwise)
     * @param flags        One or more DMLFLAG_* flags
     * @param columnValues The values that should be inserted
     * @param rowListener  The row listener that should receive insert event (triggers)
     *
     * @return The insert SQL that either was executed (assuming DMLFLAG_EXECUTE was set) or the SQL that should be executed
     */
    public String insertValues(ConnectionContext cc, int flags, ColumnValues columnValues, RowInsertListener rowListener) throws NamingException, SQLException;

    /**
     * Update values into a database managed by this policy.
     *
     * @param cc                  The connection context (required only if flags has DMLFLAG_EXECUTE set, null otherwise)
     * @param flags               One or more DMLFLAG_* flags
     * @param columnValues        The values that should be updated
     * @param rowListener         The row listener that should receive update event (triggers)
     * @param whereCond           The where condition for which records to update
     * @param whereCondBindParams The bind parameters for the where condition, if any
     *
     * @return The update SQL that either was executed (assuming DMLFLAG_EXECUTE was set) or the SQL that should be executed
     */
    public String updateValues(ConnectionContext cc, int flags, ColumnValues columnValues, RowUpdateListener rowListener, String whereCond, Object[] whereCondBindParams) throws NamingException, SQLException;

    /**
     * Delete values from a database managed by this policy.
     *
     * @param cc                  The connection context (required only if flags has DMLFLAG_EXECUTE set, null otherwise)
     * @param flags               One or more DMLFLAG_* flags
     * @param columnValues        The values that should be deleted (may be null)
     * @param rowListener         The row listener that should receive delete event (triggers)
     * @param whereCond           The where condition for which records to delete
     * @param whereCondBindParams The bind parameters for the where condition, if any
     *
     * @return The delete SQL that either was executed (assuming DMLFLAG_EXECUTE was set) or the SQL that should be executed
     */
    public String deleteValues(ConnectionContext cc, int flags, ColumnValues columnValues, RowDeleteListener rowListener, String whereCond, Object[] whereCondBindParams) throws NamingException, SQLException;

    /**
     * Return the select statement generator for the given query definition select instance.
     *
     * @param queryDefnSelect
     */
    public QueryDefnSelectStmtGenerator createSelectStatementGenerator(QueryDefnSelect queryDefnSelect);

    public boolean isPrefixTableNamesWithSchemaName();

    public void setPrefixTableNamesWithSchemaName(boolean prefix);

    public SqlDdlFormats getDdlFormats();

    public SqlDdlGenerator getDdlGenerator();

    public boolean supportsSequences();

    public boolean supportsForeignKeyConstraints();

    public interface RowInsertListener
    {
        public void beforeInsert(ConnectionContext cc, int flags, ColumnValues columnValues) throws SQLException;

        public void afterInsert(ConnectionContext cc, int flags, ColumnValues columnValues) throws SQLException;
    }

    public interface ColumnInsertListener
    {
        public void beforeInsert(ConnectionContext cc, int flags, ColumnValue columnValue, ColumnValues columnValues) throws SQLException;

        public void afterInsert(ConnectionContext cc, int flags, ColumnValue columnValue, ColumnValues columnValues) throws SQLException;
    }

    public interface RowUpdateListener
    {
        public void beforeUpdate(ConnectionContext cc, int flags, ColumnValues columnValues) throws SQLException;

        public void afterUpdate(ConnectionContext cc, int flags, ColumnValues columnValues) throws SQLException;
    }

    public interface ColumnUpdateListener
    {
        public void beforeUpdate(ConnectionContext cc, int flags, ColumnValue columnValue, ColumnValues columnValues) throws SQLException;

        public void afterUpdate(ConnectionContext cc, int flags, ColumnValue columnValue, ColumnValues columnValues) throws SQLException;
    }

    public interface RowDeleteListener
    {
        public void beforeDelete(ConnectionContext cc, int flags, ColumnValues columnValues) throws SQLException;

        public void afterDelete(ConnectionContext cc, int flags, ColumnValues columnValues) throws SQLException;
    }

    public interface ColumnDeleteListener
    {
        public void beforeDelete(ConnectionContext cc, int flags, ColumnValue columnValue, ColumnValues columnValues) throws SQLException;

        public void afterDelete(ConnectionContext cc, int flags, ColumnValue columnValue, ColumnValues columnValues) throws SQLException;
    }
}
