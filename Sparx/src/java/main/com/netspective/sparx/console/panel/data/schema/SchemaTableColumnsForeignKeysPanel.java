package com.netspective.sparx.console.panel.data.schema;

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
 * $Id: SchemaTableColumnsForeignKeysPanel.java,v 1.4 2003-05-30 23:11:32 shahid.shah Exp $
 */

import java.io.StringWriter;
import java.io.IOException;

import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.console.panel.data.schema.SchemaTableColumnsPanel;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.table.type.EnumerationTable;

public class SchemaTableColumnsForeignKeysPanel extends SchemaTableColumnsPanel
{
    private static final HtmlTabularReport fKeysReport = new BasicHtmlTabularReport();
    private static final SchemaTableDataPanel dataPanel = new SchemaTableDataPanel();

    static
    {
        GeneralColumn column = new GeneralColumn();
        fKeysReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("SQL Name"));
        fKeysReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Domain"));
        fKeysReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("References"));
        fKeysReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Ref Type"));
        fKeysReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Ref Static Data"));
        fKeysReport.addColumn(column);
    }

    public SchemaTableColumnsForeignKeysPanel()
    {
        getFrame().setHeading(new StaticValueSource("Foreign Keys"));
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return fKeysReport;
    }

    public ColumnsDataSource createColumnsDataSource(NavigationContext nc, Table table)
    {
        return new ColumnsFKeysDataSource(nc, table.getForeignKeyColumns());
    }

    protected class ColumnsFKeysDataSource extends ColumnsDataSource
    {
        public ColumnsFKeysDataSource(NavigationContext nc, Columns columns)
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
                    ForeignKey fKey = column.getForeignKey();
                    if(fKey == null) return null;
                    Table fKeyTable = fKey.getReferencedColumns().getFirst().getTable();
                    return "<a href=\"?"+ REQPARAMNAME_SHOW_DETAIL_TABLE +"="+
                                fKeyTable.getSchema().getName() + "." +
                                fKeyTable.getName() +"\">" + fKey.getReference().getReference() +
                            "</a>";

                case 4:
                    fKey = column.getForeignKey();
                    if(fKey == null) return null;
                    switch(fKey.getType())
                    {
                        case ForeignKey.FKEYTYPE_LOOKUP:
                            return "Lookup";

                        case ForeignKey.FKEYTYPE_PARENT:
                            return "Parent";

                        case ForeignKey.FKEYTYPE_SELF:
                            return "Self";

                        default:
                            return null;
                    }

                case 5:
                    fKey = column.getForeignKey();
                    if(fKey == null) return null;
                    fKeyTable = fKey.getReferencedColumns().getFirst().getTable();

                    if(fKeyTable instanceof EnumerationTable && fKeyTable.getData() != null && fKeyTable.getData().size() > 0)
                    {
                        HtmlTabularReportValueContext thisVC = (HtmlTabularReportValueContext) reportValueContext;
                        HtmlTabularReportValueContext dataVC = new HtmlTabularReportValueContext(
                                thisVC.getServletContext(), thisVC.getServlet(),
                                thisVC.getRequest(), thisVC.getResponse(), dataPanel, dataPanel.createDataReport(fKeyTable),
                                thisVC.getSkin()
                                );
                        StringWriter sw = new StringWriter();
                        try
                        {
                            dataVC.setPanelRenderFlags(HtmlPanel.RENDERFLAG_NOFRAME);
                            TabularReportDataSource ds = dataPanel.createDataSource(dataVC, fKeyTable);
                            dataVC.produceReport(sw, ds);
                            ds.close();
                        }
                        catch (IOException e)
                        {
                            return e.toString();
                        }

                        return sw.toString();
                    }

                default:
                    return null;
            }
        }
    }
}
