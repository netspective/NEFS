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
 * $Id: Column.java,v 1.11 2004-06-10 19:57:45 shahid.shah Exp $
 */

package com.netspective.axiom.schema;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netspective.axiom.schema.column.ColumnQueryDefnField;
import com.netspective.axiom.schema.column.RequirementEnumeratedAttribute;
import com.netspective.axiom.schema.column.SqlDataDefns;
import com.netspective.axiom.schema.table.TableQueryDefinition;
import com.netspective.commons.validate.ValidationRules;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateElement;
import com.netspective.commons.xml.template.TemplateProducer;

/**
 * Class for handling the column of database table.  Provides functionality for
 * declaring database table columns, their validation rules and foreign key dependencies.
 */
public interface Column extends TemplateConsumer
{
    /**
     * Constructs a column value object instance for storage of this column's data.
     */
    public ColumnValue constructValueInstance();

    /**
     * Returns the validation rules that this column contains.
     */
    public ValidationRules getValidationRules();

    /**
     * Constructs a column value object instance for storage of this column's data.
     */
    public ColumnQueryDefnField createQueryDefnField(TableQueryDefinition owner);

    /**
     * Called at the end of the construction phase when all tables, columns, etc are defined and foreign-key and other
     * placeholders should be replaced by appropriate columns.
     */
    public void finishConstruction();

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Returns the name of the column as it appears in the database.
     */
    public String getName();

    /**
     * Return the name of this column with the column name quoted (if necessary) for output in SQL
     *
     * @return 
     */
    public String getSqlName();

    /**
     * Returns name of the column prefixed by the name of the table.
     */
    public String getQualifiedName();

    /**
     * Returns name of the column prefixed by the name of the table.
     */
    public String getSqlQualifiedName();

    /**
     * Returns the name of the column suitable for use as a key in a Map for
     * runtime lookup purposes.
     */
    public String getNameForMapKey();

    /**
     * Ascertain whether or not the column's name should be quoted when referenced in SQL. This is so that if the
     * column name is not a valid SQL identifier (like starts with a number or something) it can be properly generated
     * in SQL.
     *
     * @return
     */
    public boolean isQuoteNameInSql();

    /**
     * Returns the name of the column suitable for use as an XML node name (abc_def becomes abc-def)
     */
    public String getXmlNodeName();

    /**
     * Returns an abbreviated form of the column name.
     */
    public String getAbbrev();

    /**
     * Sets the name of the column as it appears in the database
     *
     * @param value column name
     */
    public void setName(String value);

    /**
     * Sets the name of the column the name of the column suitable for use as an XML node name.
     *
     * @param value name of column suitable for use as an XML node name
     */
    public void setXmlNodeName(String value);

    /**
     * Sets the abbreviated form of the column name.
     *
     * @param abbrev abbreviated form of column name
     */
    public void setAbbrev(String abbrev);

    /**
     * Returns a column name suitable for displaying to the user. If no caption was set, this
     * method uses some basic rules to translate the column name to the friendly form of the column name.
     */
    public String getCaption();

    /**
     * Sets the friendly form of the column name suitable for displaying to the user.
     *
     * @param caption caption to show the end user in place of the column name.
     */
    public void setCaption(String caption);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Returns the index of the column in the row (as an array)
     */
    public int getIndexInRow();

    /**
     * Sets the index of the column in the row (as an array).
     *
     * @param value the index of the column in the row
     */
    public void setIndexInRow(int value);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Return the description of this column.
     */
    public String getDescr();

    /**
     * Sets the description of this column.
     *
     * @param value description for this column
     */
    public void setDescr(String value);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Returns the schema that owns this column.
     */
    public Schema getSchema();

    /**
     * Sets the schema that owns this column.
     */
    void setSchema(Schema owner);

    /**
     * Returns the table that owns this column.
     */
    public Table getTable();

    /**
     * Sets the table that owns this column.
     */
    public void setTable(Table value);

    /**
     * Gets the data types assigned to this column.
     */
    public List getDataTypeNames();

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * If this column is sequenced in the database (assuming the database supports sequences),
     * then return the sequence name or return null if the column is not sequenced.
     */
    public String getSequenceName();

    /**
     * Set the sequence name of this column.
     */
    public void setSequenceName(String value);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Returns the class that should be used to create a ForeignKey to this column. For instance, if a TextColumn is
     * referenced as a foreign key, it will return a TextColumn as a referencee because the two types are the same;
     * however, if an AutoIncColumn is being referenced then it returns a LongIntegerColumn as its referencee class
     * because the two types are slightly different (for obvious reasons).
     */
    public Class getForeignKeyReferenceeClass();

    /**
     * Sets the class that should be used to create a ForeignKey to this column.
     *
     * @see Column#setForeignKeyReferenceeClass
     */
    public void setForeignKeyReferenceeClass(Class cls);

    /**
     * Returns the @link ForeignKey object for this column or null if the column is not a
     * foreign key reference.
     */
    public ForeignKey getForeignKey();

    /**
     * Sets this column's foreign key
     */
    public void setLookupRef(String reference);

    /**
     * Sets this column's foreign key as a parent-ref foreign key
     */
    public void setParentRef(String reference);

    /**
     * Sets this column's foreign key as a self-ref foreign key.
     */
    public void setSelfRef(String reference);

    /**
     * Removes foreign key dependency for this column.
     *
     * @param fKey The foreign key from another table that references this column
     */
    public void removeForeignKeyDependency(ForeignKey fKey);

    /**
     * Registers foreign key dependency for this column.
     *
     * @param fKey The foreign key from another table that references this column
     */
    public void registerForeignKeyDependency(ForeignKey fKey);

    /**
     * Returns the list of foreign keys that are dependent upon this column.
     */
    public Set getDependentForeignKeys();

    /**
     * Assign a already-created foreign key to this column
     */
    void setForeignKey(ForeignKey foreignKey);


    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Returns the size of this column if the column is a text or string column.
     */
    public int getSize();

    /**
     * Sets the size of this column.
     *
     * @param value column size
     */
    public void setSize(int value);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Factory method to construct a new child table instance for this column
     */
    public Table createTable();

    /**
     * Factory method to register a child table instance for this column
     */
    public void addTable(Table table);

    /**
     * Returns all child tables registered for this column
     */
    public Tables getColumnTables();

    /* ------------------------------------------------------------------------------------------------------------- */

    public boolean isIndexed();

    public void setIndexed(boolean flag);

    public boolean isPrimaryKey();

    /**
     * Sets this column as the primary key for the table.
     *
     * @param flag If <code>true</code>, the column is set as the primary key for the table.
     */
    public void setPrimaryKey(boolean flag);

    public boolean isUnique();

    /**
     * Sets whether or not this column represents unique field.
     *
     * @param flag If <code>true</code>, this column is set as a unique field.
     */
    public void setUnique(boolean flag);

    /**
     * Indicates whether or not the columns value is populated by the underlying database
     * and data insertion from the application is not necessary.
     *
     * @return True if database is handling the insertion of value
     */
    public boolean isInsertManagedByDbms();

    public void setInsertManagedByDbms(boolean flag);

    /**
     * Indicates whether or not the column's value is updated by the underlying database
     * and data update from the application is not necessary.
     *
     * @return True if database is handling the update of the column
     */
    public boolean isUpdateManagedByDbms();

    public void setUpdateManagedByDbms(boolean flag);

    public boolean isRequiredByApp();

    public boolean isRequiredByDbms();

    public void setRequired(RequirementEnumeratedAttribute requirement);

    public boolean isAllowAddToTable();

    public void setAllowAddToTable(boolean flag);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Return the SQL definition that can be used to create this column for the
     * database ID specified in the dbms parameter.
     */
    public SqlDataDefns getSqlDdl();

    /**
     * Factory method to create a SqlExpression object that will hold a DBMS-specific SQL definition
     */
    public SqlDataDefns createSqlDdl();

    /**
     * Add a SQL definition expression to the expressions in this list
     */
    public void addSqlDdl(SqlDataDefns sqlDataDefn);

    /* ------------------------------------------------------------------------------------------------------------- */

    /**
     * Given a bind value, provide the string text that can be used to place into SQL text
     */
    public String formatSqlLiteral(Object value);

    /**
     * Return the embedded presentation template
     */
    public TemplateProducer getPresentation();

    /**
     * Find all the presentation templates defined in this column and place copies of them into the given table dialog
     * template.
     *
     * @param dialogTemplate The table dialog template
     * @param jexlVars       Replacement variables for interpolating template variable replacements
     */
    public void addSchemaRecordEditorDialogTemplates(TemplateElement dialogTemplate, Map jexlVars);
}
