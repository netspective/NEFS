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
package com.netspective.sparx.console.panel.data.schema;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.sql.DbmsSqlTexts;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.theme.Theme;

public class SchemaTableColumnsPanel extends AbstractHtmlTabularReportPanel
{
    private static final Log log = LogFactory.getLog(SchemaTableColumnsPanel.class);
    public static final String REQPARAMNAME_SHOW_DETAIL_TABLE = "schema-table";
    private static final HtmlTabularReport columnsReport = new BasicHtmlTabularReport();
    private static final String ATTRID_ACTIVE_TABLE = "ACTIVE_TABLE";

    static
    {
        GeneralColumn column = new GeneralColumn();
        columnsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("SQL Name"));
        columnsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Domain"));
        columnsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("XML Name"));
        columnsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("SQL Defn"));
        columnsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("SQL Default"));
        columnsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("References"));
        columnsReport.addColumn(column);
    }

    public SchemaTableColumnsPanel()
    {
        getFrame().setHeading(new StaticValueSource("Overview"));
    }

    public ColumnsDataSource createColumnsDataSource(NavigationContext nc, Table table)
    {
        return new ColumnsDataSource(nc, table.getColumns());
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        List rows = SchemaTablesPanel.createStructureRows(nc.getSqlManager().getSchemas());
        SchemaTablesPanel.StructureRow selectedRow = SchemaTablesPanel.getSelectedStructureRow(nc, rows);

        if(selectedRow == null)
            return new SimpleMessageDataSource(SchemaTablesPanel.noTableSelected);
        else
        {
            final Table activeTable = selectedRow.tableTreeNode != null
                                      ? selectedRow.tableTreeNode.getTable() : selectedRow.enumTable;
            nc.setAttribute(ATTRID_ACTIVE_TABLE, activeTable);
            return createColumnsDataSource(nc, activeTable);
        }
    }

    public void renderBeforeReport(Writer writer, NavigationContext nc, Theme theme, TabularReportDataSource ds) throws IOException
    {
        Table table = (Table) nc.getAttribute(ATTRID_ACTIVE_TABLE);
        if(table != null && table.getDescription() != null)
            writer.write("<div class='textbox'>" + table.getDescription() + "</div>");
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return columnsReport;
    }

    protected class ColumnsDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected NavigationContext nc;
        protected int row = -1;
        protected int lastRow;
        protected Columns columns;

        public ColumnsDataSource(NavigationContext nc, Columns columns)
        {
            super();
            this.nc = nc;
            this.columns = columns;
            lastRow = columns.size() - 1;
        }

        public String getSqlTexts(DbmsSqlTexts sqlTexts)
        {
            Set dbmsIds = sqlTexts.getAvailableDbmsIds();
            if(dbmsIds.size() == 1)
            {
                for(Iterator dbmsIdIter = dbmsIds.iterator(); dbmsIdIter.hasNext();)
                {
                    String dbmsId = (String) dbmsIdIter.next();
                    return sqlTexts.getByDbmsId(dbmsId).getSql(reportValueContext);
                }
            }

            StringBuffer allSql = new StringBuffer();
            for(Iterator dbmsIdIter = dbmsIds.iterator(); dbmsIdIter.hasNext();)
            {
                if(allSql.length() > 0)
                    allSql.append("<br>");

                String dbmsId = (String) dbmsIdIter.next();
                allSql.append(dbmsId);
                allSql.append(": ");
                allSql.append(sqlTexts.getByDbmsId(dbmsId).getSql(reportValueContext));
            }
            return allSql.toString();
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            Column column = columns.get(row);

            switch(columnIndex)
            {
                case 0:
                    Theme theme = ((HtmlTabularReportValueContext) reportValueContext).getActiveTheme();
                    StringBuffer content = new StringBuffer();
                    if(column.isPrimaryKey())
                        content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/primary-key.gif") + "\" title=\"Primary key\"> ");
                    if(column.isUnique())
                        content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/value-unique.gif") + "\" title=\"Values must be unique\"> ");
                    if(column.isRequiredByApp())
                        content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/value-required.gif") + "\" title=\"Value is required\"> ");
                    if(column.isRequiredByDbms())
                        content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/value-required-dbms.gif") + "\" title=\"Value is required (but only in the DBMS)\"> ");
                    if(column.isIndexed())
                        content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/indexed.gif") + "\" title=\"Column is indexed\"> ");
                    if(column.getForeignKey() != null)
                    {
                        boolean isLogical = column.getForeignKey().isLogical();
                        switch(column.getForeignKey().getType())
                        {
                            case ForeignKey.FKEYTYPE_PARENT:
                                content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/parent-ref-key.gif") + "\" title=\"Child-key reference" + (isLogical
                                                                                                                                                             ? " (logical)"
                                                                                                                                                             : "") + "\"> ");
                                break;

                            case ForeignKey.FKEYTYPE_SELF:
                                content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/self-ref-key.gif") + "\" title=\"Self reference" + (isLogical
                                                                                                                                                      ? " (logical)"
                                                                                                                                                      : "") + "\"> ");
                                break;

                            default:
                                content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/foreign-key.gif") + "\" title=\"Foreign key reference" + (isLogical
                                                                                                                                                            ? " (logical)"
                                                                                                                                                            : "") + "\"> ");
                        }
                    }
                    if(column.getDependentForeignKeys() != null && column.getDependentForeignKeys().size() > 0)
                        content.append("<img src=\"" + theme.getResourceUrl("/images/dbdd/foreign-key-elsewhere.gif") + "\" title=\"Referenced as a foreign key elsewhere\"> ");
                    return content.toString();

                case 1:
                    return column.isPrimaryKey() ? ("<b>" + column.getName() + "</b>") : column.getName();

                case 2:
                    List dataTypes = column.getDataTypeNames();
                    return dataTypes.size() > 0 ? dataTypes.get(0) : null;

                case 3:
                    return column.getXmlNodeName();

                case 4:
                    return getSqlTexts(column.getSqlDdl().getSqlDefns());

                case 5:
                    return getSqlTexts(column.getSqlDdl().getDefaultSqlExprValues());

                case 6:
                    ForeignKey fKey = column.getForeignKey();
                    if(fKey == null) return null;
                    Table fKeyTable = fKey.getReferencedColumns().getFirst().getTable();
                    return "<a href=\"?" + REQPARAMNAME_SHOW_DETAIL_TABLE + "=" +
                           fKeyTable.getSchema().getName() + "." +
                           fKeyTable.getName() + "\">" + fKey.getReference().getReference() +
                           "</a>";

                default:
                    return null;
            }
        }

        public int getTotalRows()
        {
            return columns.size();
        }

        public boolean hasMoreRows()
        {
            return row < lastRow;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            row = rowNum;
        }

        public boolean next()
        {
            if(!hasMoreRows())
                return false;

            setActiveRow(row + 1);
            return true;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }
    }
}
