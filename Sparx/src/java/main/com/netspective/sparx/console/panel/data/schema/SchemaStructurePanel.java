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
 * $Id: SchemaStructurePanel.java,v 1.1 2003-04-13 02:37:06 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel.data.schema;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.axiom.schema.Schemas;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.BasicSchema;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.schema.table.type.EnumerationTable;

public class SchemaStructurePanel extends AbstractHtmlTabularReportPanel
{
    private static final Log log = LogFactory.getLog(SchemaStructurePanel.class);
    public static final String REQPARAMNAME_SHOW_DETAIL_TABLE = "schema-table";
    private static final HtmlTabularReport structureReport = new BasicHtmlTabularReport();
    private static final ValueSource noSelectedRowMsgSource = new StaticValueSource("Please select a table.");
    private static final GeneralColumn schemaTableColumn = new GeneralColumn();

    static
    {
        schemaTableColumn.setHeading(new StaticValueSource("Table"));
        schemaTableColumn.setCommand("redirect,detail?"+ REQPARAMNAME_SHOW_DETAIL_TABLE +"=%{1}");
        structureReport.addColumn(schemaTableColumn);

        GeneralColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("XML Node Name"));
        structureReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Columns"));
        structureReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Indexes"));
        structureReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Cached Rows"));
        structureReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Dependencies"));
        structureReport.addColumn(column);
    }

    protected class StructureRow
    {
        private int level;
        private List ancestors;
        private String heading;
        private Schema.TableTreeNode tableTreeNode;
        private EnumerationTable enumTable;

        protected StructureRow(int level, String heading)
        {
            this.level = level;
            this.heading = heading;
        }

        protected StructureRow(int level, Schema.TableTreeNode tableTreeNode, List ancestors)
        {
            this.level = level;
            this.ancestors = ancestors;
            this.tableTreeNode = tableTreeNode;
        }

        protected StructureRow(int level, EnumerationTable enumTable)
        {
            this.level = level;
            this.enumTable = enumTable;
        }
    }

    private static Map rowsCache = new HashMap();

    public void addStructurRow(List rows, int level, Schema.TableTreeNode treeNode, List ancestors)
    {
        StructureRow activeStructureRow = new StructureRow(level, treeNode, ancestors);
        rows.add(activeStructureRow);
        List children = treeNode.getChildren();
        if(children != null)
        {
            for(int c = 0; c < children.size(); c++)
            {
                List childAncestors = new ArrayList();
                childAncestors.add(activeStructureRow);
                childAncestors.addAll(ancestors);
                addStructurRow(rows, level+1, (Schema.TableTreeNode) children.get(c), childAncestors);
            }
        }
    }

    public List createStructureRows(Schemas schemas)
    {
        List rows = (List) rowsCache.get(schemas);
        if(rows != null)
            return rows;

        rows = new ArrayList();
        for(int i = 0; i < schemas.size(); i++)
        {
            Schema schema = schemas.get(i);
            Schema.TableTree tree = schema.getStructure();

            StructureRow schemaRow = new StructureRow(0, "Schema: '" + schema.getName() + "'");
            rows.add(schemaRow);

            StructureRow appTablesRow = new StructureRow(1, "Application Tables");
            rows.add(appTablesRow);

            List children = tree.getChildren();
            for(int c = 0; c < children.size(); c++)
            {
                List ancestors = new ArrayList();
                ancestors.add(appTablesRow);
                addStructurRow(rows, 2, (Schema.TableTreeNode) children.get(c), ancestors);
            }

            StructureRow enumTablesRow = new StructureRow(1, "Enumeration Tables");
            rows.add(enumTablesRow);

            Set sortedEnumTables = new TreeSet(BasicSchema.TABLE_COMPARATOR);
            Tables tables = schema.getTables();
            for(int c = 0; c < tables.size(); c++)
            {
                Table table = tables.get(c);
                if(table instanceof EnumerationTable)
                    sortedEnumTables.add(table);
            }
            for(Iterator iter = sortedEnumTables.iterator(); iter.hasNext(); )
            {
                rows.add(new StructureRow(2, (EnumerationTable) iter.next()));
            }
        }

        rowsCache.put(schemas, rows);
        return rows;
    }

    public boolean affectsNavigationContext(NavigationContext nc)
    {
        return true;
    }

    public TabularReportDataSource createDataSource(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        return new StructureDataSource(vc, createStructureRows(nc.getSqlManager().getSchemas()), null);
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return structureReport;
    }

    protected class StructureDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow;
        protected StructureRow activeRow;
        protected StructureRow selectedRow;
        protected List rows;
        protected TabularReportDataSource.Hierarchy hierarchy = new ActiveHierarchy();

        protected class ActiveHierarchy implements TabularReportDataSource.Hierarchy
        {
            public int getColumn()
            {
                return 0;
            }

            public int getLevel()
            {
                return activeRow.level;
            }

            public int getParentRow()
            {
                return row-1;
            }
        }

        public boolean isHierarchical()
        {
            return true;
        }

        public TabularReportDataSource.Hierarchy getActiveHierarchy()
        {
            return hierarchy;
        }

        public StructureDataSource(HtmlTabularReportValueContext vc, List rows, StructureRow selectedRow)
        {
            super(vc);
            this.rows = rows;
            this.selectedRow = selectedRow;
            lastRow = rows.size() - 1;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    if(activeRow.tableTreeNode != null)
                        return activeRow.tableTreeNode.getTable().getName();
                    else if(activeRow.enumTable != null)
                        return activeRow.enumTable.getName();
                    else
                        return activeRow.heading;

                case 1:
                    if(activeRow.tableTreeNode != null)
                        return activeRow.tableTreeNode.getTable().getXmlNodeName();
                    else if(activeRow.enumTable != null)
                        return activeRow.enumTable.getXmlNodeName();

                case 2:
                    if(activeRow.tableTreeNode != null)
                        return new Integer(activeRow.tableTreeNode.getTable().getColumns().size());
                    else if(activeRow.enumTable != null)
                        return new Integer(activeRow.enumTable.getColumns().size());

                case 3:
                    if(activeRow.tableTreeNode != null)
                        return new Integer(activeRow.tableTreeNode.getTable().getIndexes().size());
                    else if(activeRow.enumTable != null)
                        return new Integer(activeRow.enumTable.getIndexes().size());

                case 4:
                    Rows rows = null;
                    if(activeRow.tableTreeNode != null)
                        rows = activeRow.tableTreeNode.getTable().getData();
                    else if(activeRow.enumTable != null)
                        rows = activeRow.enumTable.getData();
                    return rows != null ? new Integer(rows.size()) : null;

                default:
                    return null;
            }
        }

        public boolean next()
        {
            if(row < lastRow)
            {
                row++;
                activeRow = (StructureRow) rows.get(row);
                return true;
            }

            return false;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }

        public boolean isActiveRowSelected()
        {
            return activeRow == selectedRow;
        }
    }
}
