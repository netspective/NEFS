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
package com.netspective.sparx.console.panel.data;

import com.netspective.axiom.ConnectionProvider;
import com.netspective.axiom.ConnectionProviderEntries;
import com.netspective.axiom.ConnectionProviderEntry;
import com.netspective.axiom.ConnectionProviderEntryStatistics;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;

public class DataSourcesCatalogPanel extends AbstractHtmlTabularReportPanel
{
    public static final String REQPARAMNAME_DATA_SOURCE = "selected-data-source";
    public static final HtmlTabularReport catalogReport = new BasicHtmlTabularReport();

    static
    {
        TabularReportColumn column = catalogReport.createColumn();
        column.setHeading(new StaticValueSource("Identifier"));
        column.setRedirect(new RedirectValueSource("explorer?" + REQPARAMNAME_DATA_SOURCE + "=%{0}"));
        catalogReport.addColumn(column);

        column = catalogReport.createColumn();
        column.setHeading(new StaticValueSource("Default"));
        catalogReport.addColumn(column);

        column = catalogReport.createColumn();
        column.setHeading(new StaticValueSource("Properties"));
        catalogReport.addColumn(column);
    }

    public class ConnectionProviderValueSource extends AbstractValueSource
    {
        public PresentationValue getPresentationValue(ValueContext vc)
        {
            return new PresentationValue(getValue(vc));
        }

        public Value getValue(ValueContext vc)
        {
            ConnectionProvider cp = ((DatabaseConnValueContext) vc).getConnectionProvider();
            Class underlyingImplementationClass = cp.getUnderlyingImplementationClass();
            return new GenericValue("Connection Provider: " + cp.getClass().getName() + "<br>" + "Underlying Implementation: " + (underlyingImplementationClass != null
                    ? underlyingImplementationClass.getName() : "Unavailable"));
        }

        public boolean hasValue(ValueContext vc)
        {
            return true;
        }
    }

    public DataSourcesCatalogPanel()
    {
        getFrame().setHeading(new StaticValueSource("Available Data Sources"));
        getBanner().setContent(new ConnectionProviderValueSource());
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new DataSourcesCatalogDataSource();
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return catalogReport;
    }

    public class DataSourcesCatalogDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow = -1;
        protected ConnectionProviderEntry[] entries;

        public DataSourcesCatalogDataSource()
        {
            super();
        }

        public void setReportValueContext(TabularReportValueContext reportValueContext)
        {
            super.setReportValueContext(reportValueContext);
            ConnectionProviderEntries cpe = ((HtmlTabularReportValueContext) reportValueContext).getConnectionProvider().getDataSourceEntries(reportValueContext);
            entries = (ConnectionProviderEntry[]) cpe.values().toArray(new ConnectionProviderEntry[cpe.size()]);
            lastRow = entries.length - 1;
        }

        public int getTotalRows()
        {
            return entries.length;
        }

        public boolean hasMoreRows()
        {
            return row < lastRow;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            this.row = rowNum;
        }

        public boolean next()
        {
            if (!hasMoreRows())
                return false;

            setActiveRow(row + 1);
            return true;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            ConnectionProviderEntry entry = entries[row];

            switch (columnIndex)
            {
                case 0:
                    if ((flags & TabularReportColumn.GETDATAFLAG_FOR_URL) != 0)
                        return entry.getDataSourceId();
                    else
                        return reportValueContext.getSkin().constructRedirect(reportValueContext, reportValueContext.getReport().getColumn(0).getRedirect(), entry.getDataSourceId(), entry.getDataSourceId(), null);

                case 1:
                    return ((HtmlTabularReportValueContext) reportValueContext).getDefaultDataSource().equals(entry.getDataSourceId()) ?
                            "Yes" : null;

                case 2:
                    if (!entry.isValid()) return entry.getException().getMessage();
                    ConnectionProviderEntryStatistics stats = entry.getStatistics();
                    StringBuffer sb = new StringBuffer();
                    sb.append("<table>" +
                            "<tr><td align=right class=property_name>Database:</td><td class=property_value><b>" + entry.getDatabaseProductName() + " Version " + entry.getDatabaseProductVersion() + "</b></td></tr>" +
                            "<tr><td align=right class=property_name>Driver:</td><td class=property_value>" + entry.getDriverName() + " Version " + entry.getDriverVersion() + "</td></tr>" +
                            "<tr><td align=right class=property_name>URL:</td><td class=property_value>" + entry.getURL() + "</td></tr>" +
                            "<tr><td align=right class=property_name>User:</td><td class=property_value>" + entry.getUserName() + "</td></tr>" +
                            "<tr><td align=right class=property_name>ResultSet Type:</td><td class=property_value>" + entry.getResultSetType() + "</td></tr>" +
                            "<tr><td align=right class=property_name>Database Policy:</td><td class=property_value>" + entry.getDatabasePolicyClassName() + "</td></tr>" +
                            "</table>");

                    if (stats == null)
                        sb.append("<b>No connection statistics provider class registered for connection pool.</b>");
                    else
                    {
                        sb.append("<br>Statistics");
                        sb.append("<table>" +
                                "<tr><td align=right class=property_name>Provider:</td><td class=property_value>" + reportValueContext.getSkin().constructClassRef(stats.getClass()) + "</td></tr>" +
                                "<tr><td align=right class=property_name>Active Connections:</td><td class=property_value>" + stats.getActiveConnections() + "</td></tr>" +
                                "<tr><td align=right class=property_name>Max Connections:</td><td class=property_value>" + stats.getMaxConnections() + "</td></tr>" +
                                "<tr><td align=right class=property_name>Total Connections:</td><td class=property_value>" + stats.getTotalConnections() + "</td></tr>" +
                                "</table>");
                    }
                    return sb.toString();

                default:
                    return "Invalid column: " + columnIndex;
            }
        }
    }
}
