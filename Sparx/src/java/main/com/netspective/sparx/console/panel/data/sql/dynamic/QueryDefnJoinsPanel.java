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
package com.netspective.sparx.console.panel.data.sql.dynamic;

import com.netspective.axiom.sql.dynamic.QueryDefnJoin;
import com.netspective.axiom.sql.dynamic.QueryDefnJoins;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;

public class QueryDefnJoinsPanel extends QueryDefnDetailPanel
{
    public static final HtmlTabularReport queryDefnJoinsReport = new BasicHtmlTabularReport();
    protected static final ValueSource noJoins = new StaticValueSource("Query definition has no joins.");

    static
    {
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Alias"));
        queryDefnJoinsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Table"));
        queryDefnJoinsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Condition"));
        queryDefnJoinsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Auto-inc"));
        queryDefnJoinsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Implies"));
        queryDefnJoinsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Weight"));
        queryDefnJoinsReport.addColumn(column);
    }

    public QueryDefnJoinsPanel()
    {
        getFrame().setHeading(new StaticValueSource("Joins"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        QueryDefnDetailPanel.SelectedQueryDefinition selectedQueryDefn = getSelectedQueryDefn(nc);
        if (selectedQueryDefn.getDataSource() != null)
            return selectedQueryDefn.getDataSource();
        else
            return new QueryDefnJoinsDataSource(selectedQueryDefn);
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return queryDefnJoinsReport;
    }

    public class QueryDefnJoinsDataSource extends AbstractHtmlTabularReportDataSource
    {
        private QueryDefnJoins queryDefnJoins;
        private int activeRow = -1;
        private int lastRow;

        public QueryDefnJoinsDataSource(QueryDefnDetailPanel.SelectedQueryDefinition selectedQueryDefn)
        {
            super();
            queryDefnJoins = selectedQueryDefn.getQueryDefn().getJoins();
            if (queryDefnJoins != null)
                lastRow = queryDefnJoins.size() - 1;
            else
                lastRow = -1;
        }

        public int getActiveRowNumber()
        {
            return activeRow;
        }

        public int getTotalRows()
        {
            return queryDefnJoins.size();
        }

        public boolean hasMoreRows()
        {
            return activeRow < lastRow;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            activeRow = rowNum;
        }

        public boolean next()
        {
            if (!hasMoreRows())
                return false;

            setActiveRow(activeRow + 1);
            return true;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            QueryDefnJoin queryDefnJoin = queryDefnJoins.get(activeRow);

            switch (columnIndex)
            {
                case 0:
                    return queryDefnJoin.getName();

                case 1:
                    return queryDefnJoin.getTable();

                case 2:
                    return queryDefnJoin.getCondition();

                case 3:
                    return queryDefnJoin.shouldAutoInclude() ? "Yes" : "No";

                case 4:
                    try
                    {
                        QueryDefnJoin[] implied = queryDefnJoin.getImpliedJoins();
                        if (implied != null)
                        {
                            StringBuffer impliedJoins = new StringBuffer();
                            for (int i = 0; i < implied.length; i++)
                            {
                                if (i > 0)
                                    impliedJoins.append(", ");
                                impliedJoins.append(implied[i]);
                            }
                            return impliedJoins.toString();
                        }
                    }
                    catch (QueryDefinitionException e)
                    {
                        return e.getMessage();
                    }

                default:
                    return null;
            }
        }

        public ValueSource getNoDataFoundMessage()
        {
            return noJoins;
        }
    }
}
