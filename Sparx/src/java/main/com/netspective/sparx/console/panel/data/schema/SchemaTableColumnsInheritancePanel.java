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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Table;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;

public class SchemaTableColumnsInheritancePanel extends SchemaTableColumnsPanel
{
    private static final Log log = LogFactory.getLog(SchemaTableColumnsInheritancePanel.class);
    public static final String REQPARAMNAME_SHOW_DETAIL_COLUMN = "schema-table-column";
    private static final HtmlTabularReport columnsInhReport = new BasicHtmlTabularReport();
    private static final GeneralColumn schemaTableColumn = new GeneralColumn();

    static
    {
        GeneralColumn column = new GeneralColumn();
        columnsInhReport.addColumn(column);

        schemaTableColumn.setHeading(new StaticValueSource("Column"));
        schemaTableColumn.setRedirect(new RedirectValueSource("detail?" + REQPARAMNAME_SHOW_DETAIL_COLUMN + "=%{0}"));
        columnsInhReport.addColumn(schemaTableColumn);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Domain"));
        columnsInhReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Inheritance"));
        columnsInhReport.addColumn(column);
    }

    public SchemaTableColumnsInheritancePanel()
    {
        getFrame().setHeading(new StaticValueSource("Data Type Inheritance"));
    }

    public ColumnsDataSource createColumnsDataSource(NavigationContext nc, Table table)
    {
        return new ColumnsDescrsDataSource(nc, table.getColumns());
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return columnsInhReport;
    }

    protected class ColumnsDescrsDataSource extends ColumnsDataSource
    {
        public ColumnsDescrsDataSource(NavigationContext nc, Columns columns)
        {
            super(nc, columns);
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            Column column = columns.get(row);

            switch(columnIndex)
            {
                case 0:
                case 1:
                case 2:
                    return super.getActiveRowColumnData(columnIndex, flags);

                case 3:
                    List dataTypes = column.getDataTypeNames();
                    if(dataTypes.size() > 1)
                    {
                        StringBuffer inh = new StringBuffer();
                        for(int i = 0; i < dataTypes.size(); i++)
                        {
                            if(i > 0)
                                inh.append(" -> ");
                            inh.append(dataTypes.get(i).toString());
                        }
                        return inh.toString();
                    }
                    else
                        return null;

                default:
                    return null;
            }
        }
    }
}
