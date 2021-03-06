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
package com.netspective.sparx.console.panel.data.sql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.netspective.axiom.SqlManager;
import com.netspective.axiom.sql.Queries;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryExecutionLog;
import com.netspective.commons.io.InputSourceLocator;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;

public class StaticQueriesCatalogPanel extends AbstractHtmlTabularReportPanel
{
    public static final HtmlTabularReport catalogReport = new BasicHtmlTabularReport();
    private static final TabularReportColumn queryIdColumn = new GeneralColumn();

    static
    {
        queryIdColumn.setHeading(new StaticValueSource("Query"));
        queryIdColumn.setRedirect(new RedirectValueSource("detail?" + QueryDbmsSqlTextsPanel.REQPARAMNAME_QUERY + "=%{1}"));
        catalogReport.addColumn(queryIdColumn);

        // this is here just so that it will be available as part of the URL (it's hidden)
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Name"));
        column.getFlags().setFlag(TabularReportColumn.Flags.HIDDEN);
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Params"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Executed"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Avg"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Max"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Conn"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Bind"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("SQL"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Fail"));
        catalogReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Source"));
        catalogReport.addColumn(column);
    }

    public StaticQueriesCatalogPanel()
    {
        getFrame().setHeading(new StaticValueSource("Available Static Queries"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new CatalogDataSource(nc, nc.getSqlManager(), nc.getHttpRequest().getParameter(QueryDbmsSqlTextsPanel.REQPARAMNAME_QUERY));
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return catalogReport;
    }

    public class CatalogDataSource extends AbstractHtmlTabularReportDataSource
    {
        private NavigationContext nc;
        private Queries queries;
        private Query activeRowQuery;
        private String selectedQueryName;
        private String activeNameSpace;
        private List rows = new ArrayList();
        private int activeRow = -1;
        private int lastRow;
        private TabularReportDataSource.Hierarchy hierarchy = new ActiveHierarchy();

        protected class ActiveHierarchy implements TabularReportDataSource.Hierarchy
        {
            public int getColumn()
            {
                return 0;
            }

            public int getLevel()
            {
                return activeRowQuery != null ? 1 : 0;
            }

            public int getParentRow()
            {
                return -1; //TODO: need to implement this
            }
        }

        public CatalogDataSource(NavigationContext nc, SqlManager sqlManager, String selectedQueryName)
        {
            super();
            this.nc = nc;
            queries = sqlManager.getQueries();
            this.selectedQueryName = selectedQueryName;

            //TODO: this does not account for queries that are not contained within a namespace
            Set sortedNamesSpaces = new TreeSet(queries.getNameSpaceNames());
            for(Iterator nsi = sortedNamesSpaces.iterator(); nsi.hasNext();)
            {
                String nameSpaceId = (String) nsi.next();
                Set sortedQueryNamesInNameSpace = new TreeSet();

                for(int i = 0; i < queries.size(); i++)
                {
                    Query query = queries.get(i);
                    if(nameSpaceId.equals(query.getNameSpace().getNameSpaceId()))
                    {
                        sortedQueryNamesInNameSpace.add(query.getQualifiedName());
                    }
                }

                rows.add(nameSpaceId);
                rows.addAll(sortedQueryNamesInNameSpace);
            }

            lastRow = rows.size() - 1;
        }

        public boolean isHierarchical()
        {
            return true;
        }

        public boolean isActiveRowSelected()
        {
            if(activeRowQuery == null)
                return false;

            return activeRowQuery.getQualifiedName().equals(selectedQueryName);
        }

        public TabularReportDataSource.Hierarchy getActiveHierarchy()
        {
            return hierarchy;
        }

        public int getActiveRowNumber()
        {
            return activeRow;
        }

        public int getTotalRows()
        {
            return rows.size();
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
            String itemName = (String) rows.get(activeRow);
            activeRowQuery = queries.get(itemName);
            if(activeRowQuery == null)
                activeNameSpace = itemName;
            else
                activeNameSpace = null;
        }

        public boolean next()
        {
            if(!hasMoreRows())
                return false;

            setActiveRow(activeRow + 1);
            return true;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            if(activeNameSpace != null)
            {
                switch(columnIndex)
                {
                    case 0:
                        return activeNameSpace;

                    default:
                        return null;
                }
            }

            switch(columnIndex)
            {
                case 0:
                    return reportValueContext.getSkin().constructRedirect(reportValueContext, queryIdColumn.getRedirect(), activeRowQuery.getName(), activeRowQuery.getQualifiedName(), null);

                case 1:
                    return activeRowQuery.getQualifiedName();

                case 2:
                    return activeRowQuery.getParams() != null ? new Integer(activeRowQuery.getParams().size()) : null;

                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    QueryExecutionLog execLog = activeRowQuery.getExecLog();
                    QueryExecutionLog.QueryExecutionStatistics stats = execLog.getStatistics();

                    switch(columnIndex)
                    {
                        case 3:
                            return stats.totalExecutions > 0 ? new Integer(stats.totalExecutions) : null;
                        case 4:
                            return stats.maxTotalExecTime > 0 ? new Long(stats.maxTotalExecTime) : null;
                        case 5:
                            return stats.averageTotalExecTime > 0 ? new Long(stats.averageTotalExecTime) : null;
                        case 6:
                            return stats.averageConnectionEstablishTime > 0
                                   ? new Long(stats.averageConnectionEstablishTime) : null;
                        case 7:
                            return stats.averageBindParamsTime > 0 ? new Long(stats.averageBindParamsTime) : null;
                        case 8:
                            return stats.averageBindParamsTime > 0 ? new Long(stats.averageSqlExecTime) : null;
                        case 9:
                            return stats.averageBindParamsTime > 0 ? new Integer(stats.totalFailed) : null;
                    }

                case 10:
                    final InputSourceLocator inputSourceLocator = activeRowQuery.getInputSourceLocator();
                    if(inputSourceLocator != null)
                        return "<code>" + nc.getConsoleFileBrowserLink(inputSourceLocator.getInputSourceTracker().getIdentifier(), true) + inputSourceLocator.getLineNumbersText() + "</code>";
                    else
                        return "&nbsp;";

                default:
                    return null;
            }
        }

    }
}

