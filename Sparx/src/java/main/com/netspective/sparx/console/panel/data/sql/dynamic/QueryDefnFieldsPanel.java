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
 * $Id: QueryDefnFieldsPanel.java,v 1.1 2003-04-13 02:37:06 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel.data.sql.dynamic;

import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.console.panel.data.sql.dynamic.QueryDefnDetailPanel;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.axiom.sql.dynamic.QueryDefnFields;
import com.netspective.axiom.sql.dynamic.QueryDefnField;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;

public class QueryDefnFieldsPanel extends QueryDefnDetailPanel
{
    public static final HtmlTabularReport queryDefnFieldsReport = new BasicHtmlTabularReport();
    protected static final ValueSource noFields = new StaticValueSource("Query definition has no fields.");

    static
    {
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Alias"));
        queryDefnFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Caption"));
        queryDefnFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Join"));
        queryDefnFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Column"));
        queryDefnFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Column Expr"));
        queryDefnFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Where Expr"));
        queryDefnFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Order by Expr"));
        queryDefnFieldsReport.addColumn(column);
    }

    public QueryDefnFieldsPanel()
    {
        getFrame().setHeading(new StaticValueSource("Fields"));
    }

    public boolean affectsNavigationContext(NavigationContext nc)
    {
        return true;
    }

    public TabularReportDataSource createDataSource(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        QueryDefnDetailPanel.SelectedQueryDefinition selectedQueryDefn = getSelectedQueryDefn(vc);
        if(selectedQueryDefn.getDataSource() != null)
            return selectedQueryDefn.getDataSource();
        else
        {
            nc.setPageHeading(selectedQueryDefn.getPageHeading());
            return new QueryDefnJoinsDataSource(vc, selectedQueryDefn);
        }
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return queryDefnFieldsReport;
    }

    public class QueryDefnJoinsDataSource extends AbstractHtmlTabularReportDataSource
    {
        private QueryDefnFields queryDefnFields;
        private int activeRow = -1;
        private int lastRow;

        public QueryDefnJoinsDataSource(HtmlTabularReportValueContext vc, QueryDefnDetailPanel.SelectedQueryDefinition selectedQueryDefn)
        {
            super(vc);
            queryDefnFields = selectedQueryDefn.getQueryDefn().getFields();
            if(queryDefnFields != null)
                lastRow = queryDefnFields.size() - 1;
            else
                lastRow = -1;
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
                return true;
            }

            return false;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            QueryDefnField queryDefnField = queryDefnFields.get(activeRow);

            try
            {
                switch(columnIndex)
                {
                    case 0:
                        return queryDefnField.getName();

                    case 1:
                        return queryDefnField.getCaption();

                    case 2:
                        return queryDefnField.getJoin() != null ? queryDefnField.getJoin().getName() : null;

                    case 3:
                        return queryDefnField.getColumn();

                    case 4:
                        return queryDefnField.getColumnExpr();

                    case 5:
                        return queryDefnField.getWhereExpr();

                    case 6:
                        return queryDefnField.getOrderByExpr();

                    default:
                        return null;
                }
            }
            catch (QueryDefinitionException e)
            {
                return e.getMessage();
            }
        }

        public ValueSource getNoDataFoundMessage()
        {
            return noFields;
        }
    }
}
