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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.column.ColumnsCollection;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.console.panel.presentation.dialogs.DialogFieldsValidationPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;

public class SchemaTableColumnsValidationsPanel extends SchemaTableColumnsPanel
{
    private static final Log log = LogFactory.getLog(SchemaTableColumnsValidationsPanel.class);
    public static final String REQPARAMNAME_SHOW_DETAIL_COLUMN = "schema-table-column";
    private static final HtmlTabularReport columnsDalReport = new BasicHtmlTabularReport();
    private static final GeneralColumn schemaTableColumn = new GeneralColumn();

    static
    {
        GeneralColumn column = new GeneralColumn();
        columnsDalReport.addColumn(column);

        schemaTableColumn.setHeading(new StaticValueSource("Column"));
        schemaTableColumn.setRedirect(new RedirectValueSource("detail?" + REQPARAMNAME_SHOW_DETAIL_COLUMN + "=%{0}"));
        columnsDalReport.addColumn(schemaTableColumn);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Domain"));
        columnsDalReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Validations"));
        columnsDalReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Validators"));
        columnsDalReport.addColumn(column);
    }

    public SchemaTableColumnsValidationsPanel()
    {
        getFrame().setHeading(new StaticValueSource("Validations"));
    }

    public ColumnsDataSource createColumnsDataSource(NavigationContext nc, Table table)
    {
        Columns valColumns = new ColumnsCollection();
        for (int i = 0; i < table.getColumns().size(); i++)
        {
            Column col = table.getColumns().get(i);
            if (col.getValidationRules() != null && col.getValidationRules().size() > 0)
                valColumns.add(col);
        }

        return new ColumnsValidationsDataSource(nc, valColumns);
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return columnsDalReport;
    }

    protected class ColumnsValidationsDataSource extends ColumnsDataSource
    {
        public ColumnsValidationsDataSource(NavigationContext nc, Columns columns)
        {
            super(nc, columns);
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            Column column = columns.get(row);

            switch (columnIndex)
            {
                case 0:
                case 1:
                case 2:
                    return super.getActiveRowColumnData(columnIndex, flags);

                case 3:
                    return new Integer(column.getValidationRules().size());

                case 4:
                    return DialogFieldsValidationPanel.getValidationsHtml((HtmlTabularReportValueContext) reportValueContext, column.getValidationRules());

                default:
                    return null;
            }
        }
    }
}
