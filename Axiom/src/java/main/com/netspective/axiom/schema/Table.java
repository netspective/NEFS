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
 * $Id: Table.java,v 1.12 2003-12-04 09:06:45 roque.hernandez Exp $
 */

package com.netspective.axiom.schema;

import java.sql.SQLException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.naming.NamingException;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.schema.table.TableQueryDefinition;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
import com.netspective.axiom.sql.QueryExecutionLog;
import com.netspective.axiom.sql.QueriesNameSpace;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateConsumer;

public interface Table extends QueriesNameSpace, TemplateConsumer
{
    /**
     * Returns the schema that owns this table.
     */
    public Schema getSchema();

    /**
     * Sets the schema that owns this table.
     */
    public void setSchema(Schema value);

    /**
     * Called at the end of the construction phase when all tables, columns, etc are defined and foreign-key and other
     * placeholders should be replaced by appropriate columns.
     */
    public void finishConstruction();

    /**
     * Is this table considered an "application" or "system" table? A application table is usually any non-reference
     * and non-enumeration table.
     */
    public boolean isApplicationTable();

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Get the query definition that this table is managing.
     */
    public TableQueryDefinition getQueryDefinition();

    public QueryDefnSelect getAccessorByColumnEquality(Column column);
    public QueryDefnSelect getAccessorByColumnsEquality(Columns columns);
    public QueryDefnSelect getAccessorByIndexEquality(Index index);
    public QueryDefnSelect getAccessorByPrimaryKeyEquality();

    public QueryDefnSelect createAccessor() throws QueryDefinitionException;
    public void addAccessor(QueryDefnSelect accessor) throws QueryDefinitionException;

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Returns the name of the table as it appears in the database.
     */
    public String getName();

    /**
     * Returns the name of the table suitable for use as a key in a Map for
     * runtime lookup purposes.
     */
    public String getNameForMapKey();

    /**
     * Sets the name of the table as it appears in the database.
     */
    public void setName(String value);

    /**
     * Returns the name of the table suitable for use as an XML node/element.
     */
    public String getXmlNodeName();

    /**
     * Sets the name of the table suitable for use as an XML node/element.
     */
    public void setXmlNodeName(String value);

    /**
     * Returns an abbreviated form of the table name.
     */
    public String getAbbrev();

    /**
     * Sets the abbreviated form of the table name.
     */
    public void setAbbrev(String abbrev);

    /**
     * Returns a column name suitable for displaying to the user. If no caption was set, this
     * method uses some basic rules to translate the column name to the friendly form of the column name.
     */
    public String getCaption();

    /**
     * Sets the friendly form of the column name suitable for displaying to the user.
     * @param caption The caption to show the end user in place of the column name.
     */
    public void setCaption(String caption);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Returns the description of this table.
     */
    public String getDescription();

    /**
     * Sets the description of this table.
     */
    public void setDescription(String value);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Factory method to create a default column instance.
     * @return
     */
    public Column createColumn();

    /**
     * Factory method to construct a new column instance in this table.
     */
    public Column createColumn(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * Factory method to register a data-type instance.
     */
    public void addColumn(Column column);

    /**
     * Returns all data-types registered in the schema
     */
    public Columns getColumns();

    /**
     * Returns the columns that comprise the primary keys for this table
     */
    public PrimaryKeyColumns getPrimaryKeyColumns();

    /**
     * Returns the columns that are foreign keys of the given type.
     * @param fkeyType One of the constants defined in the ForeignKey class
     */
    public Columns getForeignKeyColumns(int fkeyType);

    /**
     * Returns all the columns that are foreign key references.
     * @return
     */
    public Columns getForeignKeyColumns();

    /**
     * Return the columns that refer to other tables as parents
     */
    public Columns getParentRefColumns();

    /* ------------------------------------------------------------------------------------------------------------- */

    public Column getParentColumn();
    public void setParentColumn(Column parentColumn);

    /**
     * Return true if this table is a child table.
     */
    public boolean isChildTable();

    /**
     * Return true if this table is a child table.
     */
    public Tables getParentTables();

    /**
     * Register the given table as a child table of this table
     */
    public void registerChildTable(Table table);

    /**
     * Remove the given table as a child table of this table
     */
    public void removeChildTable(Table table);

    /**
     * Returns all the child tables of this table
     */
    public Tables getChildTables();

    /**
     * Returns true if this table has any children
     */
    public boolean isParentTable();

    public TableHierarchyReference createHierarchy();
    public TableHierarchyReference getHierarchy();
    public void addHierarchy(com.netspective.axiom.schema.TableHierarchyReference hierarchy);

    /* ------------------------------------------------------------------------------------------------------------- */

    public Rows getData();

    /**
     * Create a single row object suitable for storing data for this table's columns
     */
    public Row createRow();

    /**
     * Create a single row object suitable for storing data for this table's columns with the value of the given
     * foreign key filled into the appropriate column.
     */
    public Row createRow(ParentForeignKey pfKey, Row parentRow);

    /**
     * Create a multiple rows object suitable for storing data for this table's columns
     */
    public Rows createRows();

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Register that the fKey is dependent upon this table.
     */
    public void registerForeignKeyDependency(ForeignKey fKey);

    /**
     * Register that the fKey is dependent upon this table.
     */
    public void removeForeignKeyDependency(ForeignKey fKey);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Retrieve the row identified by the given primary key
     * @param cc The active connection context
     * @param row The row in which to store the data
     * @return the row passed in
     */
    public Row getRowByPrimaryKeys(ConnectionContext cc, PrimaryKeyColumnValues values, Row row) throws NamingException, SQLException;

    /**
     * Retrieve the row identified by the given primary key
     * @param cc The active connection context
     * @param row The row in which to store the data
     * @return the row passed in
     */
    public Row getRowByPrimaryKeys(ConnectionContext cc, Object[] pkValues, Row row) throws NamingException, SQLException;

    /**
     * Using the primary key of the given row, go back to the database and reload the
     * data into the row.
     */
    public void refreshData(ConnectionContext cc, Row row) throws NamingException, SQLException;

    /**
     * Using the primary key of the given row, go to the database and load the
     * data into a temporary row and return true if the data in the database has
     * changed.
     */
    public boolean dataChangedInStorage(ConnectionContext cc, Row row) throws NamingException, SQLException;

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Get the DML execution log that this table is managing.
     */
    public QueryExecutionLog getDmlExecutionLog();

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Return all the indexes in this table.
     */
    public Indexes getIndexes();

    /**
     * Construct a new Index instance for populating with more than one column.
     */
    public Index createIndex();

    /**
     * Construct a new Index instance for populating with a single column.
     */
    public Index createIndex(Column column);

    /**
     * Add the given index to the list of indexes in this table.
     */
    public void addIndex(Index index);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Insert the given Row into the database.
     */
    public void insert(ConnectionContext cc, Row row) throws SQLException;

    /**
     * Update the given Row in the database using the whereCond and bind parameters.
     * @param whereCond The condition that is appened to the update statement like <code>" where " + whereCond</code>
     * @param whereCondBindParams Any optional list of bind parameters that should be bound to the whereCond
     */
    public void update(ConnectionContext cc, Row row, String whereCond, Object[] whereCondBindParams) throws SQLException;

    /**
     * Delete the given Row in the database using the whereCond and bind parameters.
     * @param whereCond The condition that is appened to the delete statement like <code>" where " + whereCond</code>
     * @param whereCondBindParams Any optional list of bind parameters that should be bound to the whereCond
     */
    public void delete(ConnectionContext cc, Row row, String whereCond, Object[] whereCondBindParams) throws SQLException;

    /**
     * Update the given Row in the database using the primary key of the row that is provided.
     */
    public void update(ConnectionContext cc, Row row) throws SQLException;

    /**
     * Delete the given Row in the database using the primary key of the row that is provided.
     */
    public void delete(ConnectionContext cc, Row row) throws SQLException;

    /**
     * Add callbacks for trigger events
     * @param trigger The class that should be called when trigger events occur
     */
    public void addTrigger(TableRowTrigger trigger);

    /**
     * Contruct a trigger object
     * @return
     */
    public TableRowTrigger createTrigger();

    /* ------------------------------------------------------------------------------------------------------------- */

    public Schema.TableTreeNode createTreeNode(Schema.TableTree owner, Schema.TableTreeNode parent, int level);

    /**
     * Gets the list of table types consumed by the table.
     */
    public List getTableTypes();

    public TemplateProducer getPresentation();

    public void addSchemaRecordEditorDialogTemplates(Template dialogsPackageTemplate);
}
