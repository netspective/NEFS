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
 * $Id: DynamicQueriesCatalogPanel.java,v 1.1 2003-04-07 01:03:52 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel.data;

import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;

import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.sql.dynamic.QueryDefinitions;
import com.netspective.axiom.sql.dynamic.QueryDefinition;

public class DynamicQueriesCatalogPanel extends AbstractHtmlTabularReportPanel
{
    public static final HtmlTabularReport catalogReport = new BasicHtmlTabularReport();
    private static final TabularReportColumn queryDefnIdColumn = new GeneralColumn();
    public static final String REQPARAMNAME_QUERY_DEFN = "query-defn";

    static
    {
        queryDefnIdColumn.setHeading(new StaticValueSource("Query Definition"));
        queryDefnIdColumn.setCommand("redirect,detail?"+ REQPARAMNAME_QUERY_DEFN +"=%{0}");
        catalogReport.addColumn(queryDefnIdColumn);

        GeneralColumn column = new NumericColumn();
        column.setHeading(new StaticValueSource("Fields"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Joins"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Selects"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Dialogs"));
        catalogReport.addColumn(column);
    }

    public DynamicQueriesCatalogPanel()
    {
        getFrame().setHeading(new StaticValueSource("Available Dynamic Queries"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        return new CatalogDataSource(vc, nc.getSqlManager());
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return catalogReport;
    }

    public class CatalogDataSource extends AbstractHtmlTabularReportDataSource
    {
        private QueryDefinitions queryDefns;
        private QueryDefinition activeRowQueryDefn;
        private List rows = new ArrayList();
        private int activeRow = -1;
        private int lastRow;

        public CatalogDataSource(HtmlTabularReportValueContext vc, SqlManager sqlManager)
        {
            super(vc);
            queryDefns = sqlManager.getQueryDefns();

            rows.addAll(new TreeSet(queryDefns.getNames()));

            lastRow = rows.size() - 1;
        }

        public int getActiveRowNumber()
        {
            return activeRow;
        }

        public boolean next()
        {
            if(activeRow < lastRow)
            {
                activeRow++;
                String itemName = (String) rows.get(activeRow);
                activeRowQueryDefn = queryDefns.get(itemName);
                return true;
            }

            return false;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    if((flags & TabularReportColumn.GETDATAFLAG_FOR_URL) != 0)
                        return activeRowQueryDefn.getName();
                    else
                        return reportValueContext.getSkin().constructRedirect(reportValueContext, queryDefnIdColumn.getCommand(), activeRowQueryDefn.getName(), null, null);

                case 1:
                    return new Integer(activeRowQueryDefn.getFields().size());

                case 2:
                    return new Integer(activeRowQueryDefn.getJoins().size());

                case 3:
                    return new Integer(activeRowQueryDefn.getSelects().size());

                default:
                    return null;
            }
        }

    }
}

