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

import java.util.List;

import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.schema.Table;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;

public class SchemaTableDataPanel extends AbstractHtmlTabularReportPanel
{
    public SchemaTableDataPanel()
    {
        getFrame().setHeading(new StaticValueSource("Table Static Data"));
    }

    public HtmlTabularReport createDataReport(Table table)
    {
        HtmlTabularReport dataReport = new BasicHtmlTabularReport();
        Columns columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++)
        {
            GeneralColumn reportColumn = new GeneralColumn();
            reportColumn.setHeading(new StaticValueSource(columns.get(i).getName()));
            dataReport.addColumn(reportColumn);
        }
        return dataReport;
    }

    public TabularReportDataSource createDataSource(HtmlTabularReportValueContext vc, Table table)
    {
        return new TableDataSource(table.getData());
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        List rows = SchemaTablesPanel.createStructureRows(nc.getSqlManager().getSchemas());
        SchemaTablesPanel.StructureRow selectedRow = SchemaTablesPanel.getSelectedStructureRow(nc, rows);

        if (selectedRow == null)
            return new SimpleMessageDataSource(SchemaTablesPanel.noTableSelected);
        else
        {
            Table table = selectedRow.getTable();
            if (table == null)
                return new SimpleMessageDataSource(SchemaTablesPanel.noTableSelected);
            else
            {
                Rows data = table.getData();
                if (data == null)
                    return new SimpleMessageDataSource("Table has no static data");
                else
                    return new TableDataSource(data);
            }
        }
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        List rows = SchemaTablesPanel.createStructureRows(nc.getSqlManager().getSchemas());
        SchemaTablesPanel.StructureRow selectedRow = SchemaTablesPanel.getSelectedStructureRow(nc, rows);

        if (selectedRow == null)
            return new BasicHtmlTabularReport();
        else
        {
            Table table = selectedRow.getTable();
            if (table != null && table.getData() != null)
                return createDataReport(table);
            else
                return new BasicHtmlTabularReport();
        }
    }

    protected class TableDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow;
        protected Rows tableData;

        public TableDataSource(Rows tableData)
        {
            super();
            this.tableData = tableData;
            this.lastRow = tableData.size() - 1;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            Row dataRow = tableData.getRow(row);
            return dataRow.getColumnValues().getByColumnIndex(columnIndex).getTextValueOrBlank();
        }

        public int getTotalRows()
        {
            return tableData.size();
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
            if (!hasMoreRows())
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
