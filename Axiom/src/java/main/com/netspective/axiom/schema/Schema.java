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
package com.netspective.axiom.schema;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.netspective.axiom.SqlManager;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
import com.netspective.axiom.sql.Queries;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.dynamic.QueryDefinitions;
import com.netspective.commons.xdm.XmlDataModelSchema;

/**
 * Interface for declaring and handlin database schema.  Provides functionality
 * for creation and maintenance of table structures within the schema.  The schema
 * descriptor supports completely automatic generation of SQL DML (insert/updates/removes),
 * SQL DDL (create tables, objects, etc) and XML import/export. Schema tags support
 * full relational integrity and may actually be used to define meta data to support
 * Java-based relational integrity for existing and legacy databases that may not
 * be built with fully relational integrity.
 */
public interface Schema extends XmlDataModelSchema.InputSourceLocatorListener
{
    public String getName();

    public String getNameForMapKey();

    public String getXmlNodeName();

    public String getPresentationTemplatesNameSpaceId();

    public String getDataTypesTemplatesNameSpaceId();

    public String getTableTypesTemplatesNameSpaceId();

    public String getIndexTypesTemplatesNameSpaceId();

    /**
     * Sets the name used to uniquely identify this schema.
     *
     * @param name schema name
     */
    public void setName(String name);

    /**
     * Sets name of this schema suitable for use as an XML node/element.
     */
    public void setXmlNodeName(String xmlNodeName);

    public SqlManager getSqlManager();

    public void setSqlManager(SqlManager manager);

    /**
     * Factory method to construct a new table instance in this schema.
     */
    public Table createTable();

    /**
     * Factory method to register a table instance.
     */
    public void addTable(Table table);

    /**
     * Returns all tables registered in the schema
     */
    public Tables getTables();

    /**
     * Factory method to construct a new view instance in this schema.
     */
    public Query createView();

    /**
     * Factory method to register a view instance.
     */
    public void addView(Query view);

    /**
     * Returns all views registered in the schema
     */
    public Queries getViews();

    /**
     * Returns all tables registered in the schema that are designed to store application data. This would include
     * all tables except enumeration and reference tables.
     */
    public Tables getApplicationTables();

    /**
     * Return the query definitions collection comprising all the query definitions generated by tables.
     */
    public QueryDefinitions getQueryDefinitions();

    /**
     * Obtains the table structure of this schema -- while tables already understand parent/child relationships,
     * because tables can have multiple parents dealing with the relationships within tables becomes cumbersome. The
     * TableTree structure returned by this method is far more convenient because a single tree with all parent/child
     * and ancestor relationships are returned and appropriate references to the actual underlying tables are made
     * available.
     */
    public TableTree getStructure();

    /**
     * Using the structure of this schema, generate a data access layer (lightweight data access objects) that wraps
     * the tables, columns, and foreign keys in the schema.
     *
     * @param rootDir             The physical location of the .java class files
     * @param dalClassPackageName The package name of the main data access layer class
     * @param dalClassName        The class name of the main data access layer class
     *
     * @return The generator class used to generate the data access layer
     */
    public DataAccessLayerGenerator generateDataAccessLayer(File rootDir, String dalClassPackageName, String dalClassName) throws IOException;

    public interface TableTreeNode
    {
        /**
         * Return the table for which structural information was gathered
         */
        public Table getTable();

        /**
         * Return the parent node for this node
         */
        public TableTreeNode getParentNode();

        /**
         * Get all the children for this node -- each item in the list is a TableTreeNode
         */
        public List getChildren();

        /**
         * Get all the ancestors for this node -- each item in the list is a TableTreeNode
         */
        public List getAncestors();

        /**
         * Returns a string with the names of all the ancestor tables delimited by the given delimiter.
         */
        public String getAncestorTableNames(String delimiter);

        /**
         * Returns a the foreign key reference if this tree node is connected to its parent by a ParentForeignKey. If
         * the relationship with the parent is only a hierarchical relationship (meaning no real column is connecting
         * the data) then this method will return null.
         */
        public ParentForeignKey getParentForeignKey();

        /**
         * Returns true if this node has any children.
         */
        public boolean hasChildren();

        /**
         * Returns true if this node has any children have have any children.
         */
        public boolean hasGrandchildren();
    }

    public interface TableTree
    {
        /**
         * Return the schema for which structural information was gathered
         */
        public Schema getSchema();

        /**
         * Get all the children for the tree -- each item in the list is a TableTreeNode that comprise the "main"
         * tables (entry points) for the hierarchy.
         */
        public List getChildren();

        /**
         * Generate the DataAccessLayer for this tree
         */
        public void generateDataAccessLayer(DataAccessLayerGenerator generator);
    }
}
