package com.netspective.sparx.console.panel.data.sql;

import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.axiom.sql.*;
import com.netspective.axiom.SqlManager;

import java.util.*;

public class StoredProceduresCatalogPanel  extends AbstractHtmlTabularReportPanel
{
    public static final HtmlTabularReport catalogReport = new BasicHtmlTabularReport();
    private static final TabularReportColumn spIdColumn = new GeneralColumn();

    static
    {
        spIdColumn.setHeading(new StaticValueSource("Stored Procedure"));
        spIdColumn.setRedirect(new RedirectValueSource("detail?"+ QueryDbmsSqlTextsPanel.REQPARAMNAME_QUERY +"=%{1}"));
        catalogReport.addColumn(spIdColumn);

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
    }

    public StoredProceduresCatalogPanel()
    {
        getFrame().setHeading(new StaticValueSource("Available Stored Procedures"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new StoredProceduresCatalogPanel.CatalogDataSource(nc.getSqlManager(), nc.getHttpRequest().getParameter(QueryDbmsSqlTextsPanel.REQPARAMNAME_QUERY));
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return catalogReport;
    }

    public class CatalogDataSource extends AbstractHtmlTabularReportDataSource
    {
        private StoredProcedures storedProcs;
        private StoredProcedure activeRowStoredProc;
        private String selectedStoredProcName;
        private String activeNameSpace;
        private List rows = new ArrayList();
        private int activeRow = -1;
        private int lastRow;
        private TabularReportDataSource.Hierarchy hierarchy = new StoredProceduresCatalogPanel.CatalogDataSource.ActiveHierarchy();

        protected class ActiveHierarchy implements TabularReportDataSource.Hierarchy
        {
            public int getColumn()
            {
                return 0;
            }

            public int getLevel()
            {
                return activeRowStoredProc != null ? 1 : 0;
            }

            public int getParentRow()
            {
                return -1; //TODO: need to implement this
            }
        }

        public CatalogDataSource(SqlManager sqlManager, String selectedSPName)
        {
            super();
            storedProcs = sqlManager.getStoredProcedures();
            this.selectedStoredProcName = selectedSPName;

            //TODO: this does not account for storedProcs that are not contained within a namespace
            Set sortedNamesSpaces = new TreeSet(storedProcs.getNameSpaceNames());
            for(Iterator nsi = sortedNamesSpaces.iterator(); nsi.hasNext(); )
            {
                String nameSpaceId = (String) nsi.next();
                Set sortedQueryNamesInNameSpace = new TreeSet();

                for(int i = 0; i < storedProcs.size(); i++)
                {
                    StoredProcedure sp = storedProcs.get(i);
                    if(nameSpaceId.equals(sp.getNameSpace().getNameSpaceId()))
                    {
                        sortedQueryNamesInNameSpace.add(sp.getQualifiedName());
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
            if(activeRowStoredProc == null)
                return false;

            return activeRowStoredProc.getQualifiedName().equals(selectedStoredProcName);
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
            activeRowStoredProc = storedProcs.get(itemName);
            if(activeRowStoredProc == null)
                activeNameSpace = itemName;
            else
                activeNameSpace = null;
        }

        public boolean next()
        {
            if(! hasMoreRows())
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
                    return reportValueContext.getSkin().constructRedirect(reportValueContext, spIdColumn.getRedirect(),
                            activeRowStoredProc.getName(), activeRowStoredProc.getQualifiedName(), null);

                case 1:
                    return activeRowStoredProc.getQualifiedName();

                case 2:
                    return activeRowStoredProc.getParams() != null ? new Integer(activeRowStoredProc.getParams().size()) : null;

                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    QueryExecutionLog execLog = activeRowStoredProc.getExecLog();
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
                            return stats.averageConnectionEstablishTime > 0 ? new Long(stats.averageConnectionEstablishTime) : null;
                        case 7:
                            return stats.averageBindParamsTime > 0 ? new Long(stats.averageBindParamsTime) : null;
                        case 8:
                            return stats.averageBindParamsTime > 0 ? new Long(stats.averageSqlExecTime) : null;
                        case 9:
                            return stats.averageBindParamsTime > 0 ? new Integer(stats.totalFailed) : null;
                    }

                default:
                    return null;
            }
        }

    }
}
