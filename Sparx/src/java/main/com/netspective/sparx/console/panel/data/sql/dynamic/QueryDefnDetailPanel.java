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

import com.netspective.axiom.SqlManager;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.sql.dynamic.QueryDefinition;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.console.panel.data.schema.SchemaTableColumnsPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;

abstract public class QueryDefnDetailPanel extends AbstractHtmlTabularReportPanel
{
    public static final String REQPARAMNAME_QUERY_DEFN_SOURCE = "query-defn-source";
    public static final String REQPARAMNAME_QUERY_DEFN = "selected-query-defn-id";
    private static final ValueSource noQueryDefnParamAvailSource = new StaticValueSource("No '" + REQPARAMNAME_QUERY_DEFN + "' parameter provided.");

    protected class SelectedQueryDefinition
    {
        private String queryDefnSource;
        private String queryDefnName;
        private String pageHeading;
        private QueryDefinition queryDefn;
        private TabularReportDataSource dataSource;

        public SelectedQueryDefinition(SqlManager sqlManager, String queryDefnSource, String queryDefnName)
        {
            this.queryDefnSource = queryDefnSource;
            this.queryDefnName = queryDefnName;

            if (queryDefnName == null)
            {
                dataSource = new AbstractHtmlTabularReportPanel.SimpleMessageDataSource(noQueryDefnParamAvailSource);
                return;
            }

            if (queryDefnSource == null || "dynamic".equalsIgnoreCase(queryDefnSource))
            {
                queryDefn = sqlManager.getQueryDefns().get(queryDefnName);
                if (queryDefn == null)
                    dataSource = new AbstractHtmlTabularReportPanel.SimpleMessageDataSource("Custom query definition '" + queryDefnName + "' not found.");
                else
                    pageHeading = "Custom Dynamic Query: " + queryDefn.getName();
            }
            else if (queryDefnSource.startsWith("schema"))
            {
                String[] querySourceParams = TextUtils.getInstance().split(queryDefnSource, ",", true);
                Schema schema = sqlManager.getSchema(querySourceParams[1]);
                if (schema != null)
                {
                    Table table = schema.getTables().getByName(queryDefnName);
                    if (table != null)
                    {
                        queryDefn = table.getQueryDefinition();
                        pageHeading = "Schema Dynamic Query: " + querySourceParams[1] + "." + queryDefnName;
                    }
                    else
                        dataSource = new AbstractHtmlTabularReportPanel.SimpleMessageDataSource("Table '" + querySourceParams[1] + "." + queryDefnName + "' not found.");
                }
                else
                    dataSource = new AbstractHtmlTabularReportPanel.SimpleMessageDataSource("Schema '" + querySourceParams[1] + "' not found. Available: " + sqlManager.getSchemas().getNames());
            }
            else
                dataSource = new AbstractHtmlTabularReportPanel.SimpleMessageDataSource("Unknown query source.");
        }

        public TabularReportDataSource getDataSource()
        {
            return dataSource;
        }

        public QueryDefinition getQueryDefn()
        {
            return queryDefn;
        }

        public String getQueryDefnName()
        {
            return queryDefnName;
        }

        public String getQueryDefnSource()
        {
            return queryDefnSource;
        }

        public String getPageHeading()
        {
            return pageHeading;
        }
    }

    public SelectedQueryDefinition getSelectedQueryDefn(NavigationContext nc)
    {
        String schemaTable = nc.getHttpRequest().getParameter(SchemaTableColumnsPanel.REQPARAMNAME_SHOW_DETAIL_TABLE);
        if (schemaTable != null)
        {
            String[] items = TextUtils.getInstance().split(schemaTable, ".", false);
            return new SelectedQueryDefinition(nc.getSqlManager(), "schema," + items[0], items[1]);
        }
        else
            return new SelectedQueryDefinition(nc.getSqlManager(),
                    nc.getHttpRequest().getParameter(REQPARAMNAME_QUERY_DEFN_SOURCE),
                    nc.getHttpRequest().getParameter(REQPARAMNAME_QUERY_DEFN));
    }
}
