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
 * $Id: QueryReportPanel.java,v 1.4 2003-05-24 20:28:36 shahid.shah Exp $
 */

package com.netspective.sparx.panel;

import java.sql.SQLException;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;

public class QueryReportPanel extends AbstractHtmlTabularReportPanel
{
    private static final Log log = LogFactory.getLog(QueryReportPanel.class);
    private static final ValueSource NO_DATA_MSG = new StaticValueSource("No data in query results.");

    private Query query;
    private HtmlTabularReport report;
    private String[] urlFormats;
    private boolean defaultPanel;

    public String getName()
    {
        return getIdentifier();
    }

    public void setName(String name)
    {
        setIdentifier(name);
    }

    public boolean isDefaultPanel()
    {
        return defaultPanel;
    }

    public void setDefault(boolean defaultPanel)
    {
        this.defaultPanel = defaultPanel;
    }

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(Query query)
    {
        this.query = query;
    }

    public HtmlTabularReport getReport()
    {
        return report;
    }

    public HtmlTabularReport createReport()
    {
        return new BasicHtmlTabularReport();
    }

    public void addReport(HtmlTabularReport report)
    {
        this.report = report;
    }

    public String[] getUrlFormats()
    {
        return urlFormats;
    }

    public void setUrlFormats(String[] urlFormats)
    {
        this.urlFormats = urlFormats;
    }

    public TabularReportDataSource createDataSource(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        try
        {
            QueryResultSet resultSet = (QueryResultSet) nc.getAttribute("QRS-" + this.hashCode());
            if(resultSet == null)
                resultSet = query.execute(nc, null, false);
            QueryResultSetDataSource qrsds = new QueryResultSetDataSource(vc, NO_DATA_MSG);
            qrsds.setQueryResultSet(resultSet);
            return qrsds;
        }
        catch (Exception e)
        {
            log.error("Unable to create data source", e);
            throw new NestableRuntimeException(e);
        }
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        HtmlTabularReport report = getReport();
        if(report == null)
        {
            // if the report is null, we need to create it by running the query and getting the meta data
            report = new BasicHtmlTabularReport();
            try
            {
                QueryResultSet resultSet = query.execute(nc, null, false);
                resultSet.fillReportFromMetaData(report);
                nc.setAttribute("QRS-" + this.hashCode(), resultSet); // store the result set so we don't run it again
            }
            catch (Exception e)
            {
                log.error("Unable to create report", e);
                throw new NestableRuntimeException(e);
            }
        }

        return report;
    }

    public class QueryResultSetDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected int activeRowIndex = -1;
        protected QueryResultSet queryResultSet;
        protected ResultSet resultSet;
        protected boolean scrollable;
        protected ValueSource message;
        protected boolean calculatedTotalRows;
        protected int totalRows = TOTAL_ROWS_UNKNOWN;

        public QueryResultSetDataSource(HtmlTabularReportValueContext vc, ValueSource noDataMessage) throws SQLException
        {
            super(vc);
            this.message = noDataMessage;
        }

        public QueryResultSet getQueryResultSet()
        {
            return queryResultSet;
        }

        public void setQueryResultSet(QueryResultSet queryResultSet)
        {
            this.queryResultSet = queryResultSet;
            setResultSet(queryResultSet.getResultSet());
        }

        protected void setResultSet(ResultSet resultSet)
        {
            this.resultSet = queryResultSet.getResultSet();

            try
            {
                scrollable = resultSet.getType() != ResultSet.TYPE_FORWARD_ONLY ? true : false;
            }
            catch (SQLException e)
            {
                log.error("Unable to set result set", e);
                throw new NestableRuntimeException(e);
            }

            activeRowIndex = -1;
            calculatedTotalRows = false;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            try
            {
                return queryResultSet.getResultSet().getObject(columnIndex + 1);
            }
            catch (SQLException e)
            {
                log.error("Unable to retrieve column data", e);
                return e.toString();
            }
        }

        public int getTotalRows()
        {
            if(calculatedTotalRows)
                return totalRows;

            try
            {
                if(scrollable)
                {
                    resultSet.last();
                    totalRows = resultSet.getRow();
                    resultSet.first();
                }
            }
            catch (SQLException e)
            {
                log.error("Unable to get total rows", e);
                throw new NestableRuntimeException(e);
            }

            calculatedTotalRows = true;
            return totalRows;
        }

        public boolean hasMoreRows()
        {
            try
            {
                return ! resultSet.isAfterLast();
            }
            catch (SQLException e)
            {
                log.error("Unable to check if more rows are available", e);
                throw new NestableRuntimeException(e);
            }
        }

        public boolean isScrollable()
        {
            return scrollable;
        }

        public void setActiveRow(int rowNum)
        {
            try
            {
                if(scrollable)
                {
                    resultSet.absolute(rowNum);
                    resultSet.previous();
                }
                else
                {
                    queryResultSet.reExecute();
                    setResultSet(queryResultSet.getResultSet());

                    if(rowNum > 0)
                    {
                        int atRow = 0;
                        while(resultSet.next())
                        {
                            if(atRow >= rowNum)
                                break;

                            atRow++;
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                log.error("Unable to set active row", e);
                throw new NestableRuntimeException(e);
            }
        }

        public boolean next()
        {
            try
            {
                if(resultSet.next())
                {
                    activeRowIndex++;
                    return true;
                }
            }
            catch (SQLException e)
            {
                log.error("Unable to move to next row", e);
                throw new NestableRuntimeException(e);
            }

            return false;
        }

        public int getActiveRowNumber()
        {
            return activeRowIndex + 1;
        }

        public ValueSource getNoDataFoundMessage()
        {
            return message;
        }
    }

}
