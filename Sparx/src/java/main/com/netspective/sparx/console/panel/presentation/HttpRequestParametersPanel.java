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
package com.netspective.sparx.console.panel.presentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;

public class HttpRequestParametersPanel extends AbstractHtmlTabularReportPanel
{
    public static final HtmlTabularReport paramsReport = new BasicHtmlTabularReport();
    private static final ValueSource noParams = new StaticValueSource("No request parameters found.");

    static
    {
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Parameter"));
        paramsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Value"));
        paramsReport.addColumn(column);
    }

    public HttpRequestParametersPanel()
    {
        getFrame().setHeading(new StaticValueSource("Request Parameters"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new HttpRequestParametersDataSource(nc.getHttpRequest());
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return paramsReport;
    }

    protected class HttpRequestParametersDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected class ParameterRow
        {
            private ParameterRow parentRow;
            private String name;
            private String value;
            private int level;

            public ParameterRow(int level, String name, String value, ParameterRow parentRow)
            {
                this.level = level;
                this.name = name;
                this.value = value;
                this.parentRow = parentRow;
            }
        }

        protected class ParameterRows
        {
            protected List rows = new ArrayList();

            public ParameterRows()
            {
                List paramNamesList = new ArrayList();
                for(Enumeration e = request.getParameterNames(); e.hasMoreElements();)
                    paramNamesList.add(e.nextElement());

                String[] paramNames = (String[]) paramNamesList.toArray(new String[paramNamesList.size()]);
                Arrays.sort(paramNames);

                for(int i = 0; i < paramNames.length; i++)
                {
                    String name = paramNames[i];
                    String[] values = request.getParameterValues(name);
                    add(0, name, values);
                }
            }

            public void add(int level, String name, String[] values)
            {
                if(values.length > 1)
                {
                    ParameterRow activeRow = new ParameterRow(level, name, null, null);
                    rows.add(activeRow);
                    for(int i = 0; i < values.length; i++)
                        rows.add(new ParameterRow(level + 1, name + "[" + i + "]", values[i], activeRow));
                }
                else if(values.length == 1)
                    rows.add(new ParameterRow(level, name, values[0], null));
            }

            public ParameterRow get(int i)
            {
                return (ParameterRow) rows.get(i);
            }

            public int indexOf(ParameterRow row)
            {
                return rows.indexOf(row);
            }

            public int size()
            {
                return rows.size();
            }
        }

        protected class ActiveHierarchy implements TabularReportDataSource.Hierarchy
        {
            public int getColumn()
            {
                return 0;
            }

            public int getLevel()
            {
                return activeRow.level;
            }

            public int getParentRow()
            {
                return activeRow.parentRow != null ? paramRows.indexOf(activeRow.parentRow) : -1;
            }
        }

        protected int activeRowIndex = -1;
        protected int lastRowIndex;
        protected HttpServletRequest request;
        protected ParameterRow activeRow;
        protected ParameterRows paramRows;
        protected TabularReportDataSource.Hierarchy hierarchy = new ActiveHierarchy();

        public boolean isHierarchical()
        {
            return true;
        }

        public TabularReportDataSource.Hierarchy getActiveHierarchy()
        {
            return hierarchy;
        }

        public HttpRequestParametersDataSource(HttpServletRequest request)
        {
            super();
            this.request = request;
            paramRows = new ParameterRows();
            lastRowIndex = paramRows.size() - 1;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    return activeRow.name;

                case 1:
                    return activeRow.value;

                default:
                    return null;
            }
        }

        public int getTotalRows()
        {
            return paramRows.size();
        }

        public boolean hasMoreRows()
        {
            return activeRowIndex < lastRowIndex;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            activeRowIndex = rowNum;
            activeRow = paramRows.get(activeRowIndex);
        }

        public boolean next()
        {
            if(!hasMoreRows())
                return false;

            setActiveRow(activeRowIndex + 1);
            return true;
        }

        public int getActiveRowNumber()
        {
            return activeRowIndex + 1;
        }

        public ValueSource getNoDataFoundMessage()
        {
            return noParams;
        }
    }
}
