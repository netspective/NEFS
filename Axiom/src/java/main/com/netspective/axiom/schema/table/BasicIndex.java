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
package com.netspective.axiom.schema.table;

import java.util.StringTokenizer;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Index;
import com.netspective.axiom.schema.IndexColumns;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.column.IndexColumnsCollection;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;

public class BasicIndex implements Index, TemplateConsumer
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();
    public static final String ATTRNAME_TYPE = "type";
    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[]{"name"};

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[]{"row"});
    }

    protected class IndexTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public IndexTypeTemplateConsumerDefn()
        {
            super(getTable().getSchema().getIndexTypesTemplatesNameSpaceId(), ATTRNAME_TYPE, ATTRNAMES_SET_BEFORE_CONSUMING);
        }
    }

    private Table table;
    private String indexName;
    private String columnNames;
    private IndexColumns indexColumns;
    private boolean unique;
    private IndexSqlDataDefns sqlDataDefns = new IndexSqlDataDefns(this);
    private TemplateConsumerDefn templateConsumer;

    public BasicIndex(Table table)
    {
        this.table = table;
    }

    public BasicIndex(Column column)
    {
        this.table = column.getTable();
        this.indexColumns = new IndexColumnsCollection();
        this.indexColumns.add(column);
    }

    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        if(templateConsumer == null)
            templateConsumer = new IndexTypeTemplateConsumerDefn();
        return templateConsumer;
    }

    public void registerTemplateConsumption(Template template)
    {
    }

    public IndexColumns getColumns()
    {
        if(indexColumns == null)
        {
            if(table == null)
                throw new RuntimeException("A table is required for indexing columns.");

            Columns tableColumns = table.getColumns();
            this.indexColumns = new IndexColumnsCollection();

            StringTokenizer st = new StringTokenizer(columnNames, ",");
            while(st.hasMoreTokens())
            {
                String columnName = st.nextToken().trim();
                Column column = tableColumns.getByName(columnName);
                if(column == null)
                    throw new RuntimeException("Column '" + columnName + "' not found in table '" + table.getName() + "' in index " + this + ".");

                indexColumns.add(column);
            }
        }

        return indexColumns;
    }

    public void setColumns(String columnNames)
    {
        this.columnNames = columnNames;
        this.indexColumns = null;
    }

    public String getName()
    {
        if(indexName == null)
        {
            if(indexColumns.size() > 1)
                throw new RuntimeException("Indexes with more than one column must provide a name.");

            StringBuffer name = new StringBuffer(table.getAbbrev());
            name.append("_");
            name.append(indexColumns.getSole().getName());
            setName(name.toString());
        }

        return indexName;
    }

    public void setName(String indexName)
    {
        this.indexName = indexName;
    }

    public Table getTable()
    {
        return table;
    }

    public String getType()
    {
        return isUnique() ? "unique" : "";
    }

    public boolean isUnique()
    {
        return unique;
    }

    public void setUnique(boolean unique)
    {
        this.unique = unique;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public IndexSqlDataDefns getSqlDataDefns()
    {
        return sqlDataDefns;
    }

    public IndexSqlDataDefns createSqlDdl()
    {
        return sqlDataDefns;
    }

    public void addSqlDdl(IndexSqlDataDefns sqlDataDefn)
    {
        // do nothing -- we have the instance already created, but the XML data model will call this anyway
    }
}
