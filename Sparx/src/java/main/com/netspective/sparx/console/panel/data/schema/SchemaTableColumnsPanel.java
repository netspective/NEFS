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
 * $Id: SchemaTableColumnsPanel.java,v 1.2 2003-04-23 15:42:15 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel.data.schema;

import java.util.List;
import java.util.Set;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.text.TextUtils;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.sql.DbmsSqlTexts;

public class SchemaTableColumnsPanel extends AbstractHtmlTabularReportPanel
{
    private static final Log log = LogFactory.getLog(SchemaTableColumnsPanel.class);
    public static final String REQPARAMNAME_SHOW_DETAIL_TABLE = "schema-table";
    private static final HtmlTabularReport columnsReport = new BasicHtmlTabularReport();
    private static final GeneralColumn schemaTableColumn = new GeneralColumn();

    static
    {
        schemaTableColumn.setHeading(new StaticValueSource("Column"));
        schemaTableColumn.setCommand("redirect,detail?"+ REQPARAMNAME_SHOW_DETAIL_TABLE +"=%{1}");
        columnsReport.addColumn(schemaTableColumn);

        GeneralColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Domain"));
        columnsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Class"));
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
        getFrame().setHeading(new StaticValueSource("Table Columns"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        List rows = SchemaStructurePanel.createStructureRows(nc.getSqlManager().getSchemas());
        SchemaStructurePanel.StructureRow selectedRow = SchemaStructurePanel.getSelectedStructureRow(nc, rows);

        if(selectedRow == null)
            return new SimpleMessageDataSource(vc, SchemaStructurePanel.noTableSelected);
        else
            return new ColumnsDataSource(vc, selectedRow.tableTreeNode != null ? selectedRow.tableTreeNode.getTable() : selectedRow.enumTable);
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return columnsReport;
    }

    protected class ColumnsDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow;
        protected Table table;

        public ColumnsDataSource(HtmlTabularReportValueContext vc, Table table)
        {
            super(vc);
            this.table = table;
            lastRow = table.getColumns().size() - 1;
        }

        public String getSqlTexts(DbmsSqlTexts sqlTexts)
        {
            Set dbmsIds = sqlTexts.getAvailableDbmsIds();
            if(dbmsIds.size() == 1)
            {
                for(Iterator dbmsIdIter = dbmsIds.iterator(); dbmsIdIter.hasNext(); )
                {
                    String dbmsId = (String) dbmsIdIter.next();
                    return sqlTexts.getByDbmsId(dbmsId).getSql(reportValueContext);
                }
            }

            StringBuffer allSql = new StringBuffer();
            for(Iterator dbmsIdIter = dbmsIds.iterator(); dbmsIdIter.hasNext(); )
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
            Column column = table.getColumns().get(row);

            switch(columnIndex)
            {
                case 0:
                    return column.getName();

                case 1:
                    List dataTypes = column.getDataTypeNames();
                    return dataTypes.size() > 0 ? dataTypes.get(0) : null;

                case 2:
                    return reportValueContext.getSkin().constructClassRef(column.getClass());

                case 3:
                    return getSqlTexts(column.getSqlDdl().getSqlDefns());

                case 4:
                    return getSqlTexts(column.getSqlDdl().getDefaultSqlExprValues());

                case 5:
                    ForeignKey fKey = column.getForeignKey();
                    return fKey == null ? null : fKey.getReference().getReference();

                default:
                    return null;
            }
        }

        public boolean next()
        {
            if(row < lastRow)
            {
                row++;
                return true;
            }

            return false;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }
    }
}
