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
 * $Id: SchemaTableColumnsRefByPanel.java,v 1.3 2003-05-30 23:11:32 shahid.shah Exp $
 */

import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;

import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.console.panel.data.schema.SchemaTableColumnsPanel;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.column.ColumnsCollection;

public class SchemaTableColumnsRefByPanel extends SchemaTableColumnsPanel
{
    private static final HtmlTabularReport fKeysReport = new BasicHtmlTabularReport();

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
        column.setHeading(new StaticValueSource("Referenced By"));
        fKeysReport.addColumn(column);
    }

    public SchemaTableColumnsRefByPanel()
    {
        getFrame().setHeading(new StaticValueSource("Referenced By"));
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return fKeysReport;
    }

    public ColumnsDataSource createColumnsDataSource(NavigationContext nc, Table table)
    {
        Columns depColumns = new ColumnsCollection();
        for(int i = 0; i < table.getColumns().size(); i++)
        {
            Column col = table.getColumns().get(i);
            if(col.getDependentForeignKeys() != null)
                depColumns.add(col);
        }

        return new ColumnsFKeysDataSource(nc, depColumns);
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
                    Set dependents = column.getDependentForeignKeys();
                    String[] depNames = new String[dependents.size()];
                    int dn = 0;
                    for(Iterator i = column.getDependentForeignKeys().iterator(); i.hasNext(); )
                    {
                        ForeignKey fKey = (ForeignKey) i.next();
                        depNames[dn] = fKey.getSourceColumns().getFirst().getQualifiedName();
                        dn++;
                    }
                    Arrays.sort(depNames);

                    StringBuffer sb = new StringBuffer();
                    for(int i = 0; i < depNames.length; i++)
                    {
                        sb.append(depNames[i]);
                        sb.append("<br>");
                    }
                    return sb.toString();

                default:
                    return null;
            }
        }
    }
}
