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
 * $Id: SchemaTableDataPanel.java,v 1.2 2003-04-28 16:01:39 shahid.shah Exp $
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
import com.netspective.commons.report.Report;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Indexes;
import com.netspective.axiom.schema.Index;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.sql.DbmsSqlTexts;

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
        for(int i = 0; i < columns.size(); i++)
        {
            GeneralColumn reportColumn = new GeneralColumn();
            reportColumn.setHeading(new StaticValueSource(columns.get(i).getName()));
            dataReport.addColumn(reportColumn);
        }
        return dataReport;
    }

    public TabularReportDataSource createDataSource(HtmlTabularReportValueContext vc, Table table)
    {
        return new TableDataSource(vc, table.getData());
    }

    public TabularReportDataSource createDataSource(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        List rows = SchemaTablesPanel.createStructureRows(nc.getSqlManager().getSchemas());
        SchemaTablesPanel.StructureRow selectedRow = SchemaTablesPanel.getSelectedStructureRow(nc, rows);

        if(selectedRow == null)
            return new SimpleMessageDataSource(vc, SchemaTablesPanel.noTableSelected);
        else
        {
            Table table = selectedRow.getTable();
            if(table == null)
                return new SimpleMessageDataSource(vc, SchemaTablesPanel.noTableSelected);
            else
            {
                Rows data = table.getData();
                if(data == null)
                    return new SimpleMessageDataSource(vc, "Table has no static data");
                else
                    return new TableDataSource(vc, data);
            }
        }
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        List rows = SchemaTablesPanel.createStructureRows(nc.getSqlManager().getSchemas());
        SchemaTablesPanel.StructureRow selectedRow = SchemaTablesPanel.getSelectedStructureRow(nc, rows);

        if(selectedRow == null)
            return new BasicHtmlTabularReport();
        else
        {
            Table table = selectedRow.getTable();
            if(table != null && table.getData() != null)
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

        public TableDataSource(HtmlTabularReportValueContext vc, Rows tableData)
        {
            super(vc);
            this.tableData = tableData;
            this.lastRow = tableData.size() - 1;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            Row dataRow = tableData.getRow(row);
            return dataRow.getColumnValues().getByColumnIndex(columnIndex).getTextValueOrBlank();
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
