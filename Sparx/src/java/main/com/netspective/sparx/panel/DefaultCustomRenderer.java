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
 * $Id: DefaultCustomRenderer.java,v 1.3 2004-08-09 22:15:14 shahid.shah Exp $
 */

package com.netspective.sparx.panel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportColumnState;
import com.netspective.commons.report.tabular.TabularReportColumns;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.template.TemplateProcessor;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.sparx.panel.HtmlTabularReportPanel.CustomRenderer;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollState;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.sql.QueryResultSetDataSource;
import com.netspective.sparx.template.freemarker.FreeMarkerTemplateProcessor;

public class DefaultCustomRenderer implements CustomRenderer, XmlDataModelSchema.CustomElementAttributeSetter
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    protected interface DataPopulator
    {
        public void populateData(Map vars, final HtmlTabularReportValueContext rc, final TabularReportDataSource ds);
    }

    private CustomRendererDataType dataType;
    private String dataVarName = "data";
    private TemplateProcessor body;
    private boolean renderContainerTableTag = true;
    private boolean renderFrame = true;
    private Map templateVars;
    private DataPopulator dataPopulator;

    public DefaultCustomRenderer()
    {
        setDataType(new CustomRendererDataType(CustomRendererDataType.FORMATTED_MATRIX));
    }

    public CustomRendererDataType getDataType()
    {
        return dataType;
    }

    public void setDataType(CustomRendererDataType dataType)
    {
        this.dataType = dataType;
        switch (dataType.getValueIndex())
        {
            case CustomRendererDataType.QUERY_RESULT_SET:
                dataPopulator = new QueryResultSetPopulator();
                break;

            case CustomRendererDataType.SQL_RESULT_SET:
                dataPopulator = new SqlResultSetPopulator();
                break;

            case CustomRendererDataType.TABULAR_DATA_SOURCE:
                dataPopulator = new TabularDataSourceDataPopulator();
                break;

            case CustomRendererDataType.FORMATTED_MATRIX:
                dataPopulator = new FormattedMatrixDataPopulator();
                break;

            case CustomRendererDataType.RAW_MATRIX:
                dataPopulator = new RawMatrixDataPopulator();
                break;

            default:
                throw new RuntimeException("Unknown data type");
        }
    }

    public String getDataVarName()
    {
        return dataVarName;
    }

    public void setDataVarName(String dataVarName)
    {
        this.dataVarName = dataVarName;
    }

    public TemplateProcessor createBody()
    {
        return new FreeMarkerTemplateProcessor();
    }

    public void addBody(TemplateProcessor body)
    {
        this.body = body;
    }

    public TemplateProcessor getTemplateProcessor()
    {
        return body;
    }

    public Map getTemplateVars(final HtmlTabularReportValueContext rc, TabularReportDataSource ds)
    {
        final Map result = templateVars != null ? new HashMap(templateVars) : new HashMap();
        dataPopulator.populateData(result, rc, ds);
        return result;
    }

    public void setRenderContainerTableTag(boolean renderContainerTableTag)
    {
        this.renderContainerTableTag = renderContainerTableTag;
    }

    public boolean isRenderContainerTableTag()
    {
        return renderContainerTableTag;
    }

    public boolean isRenderFrame()
    {
        return renderFrame;
    }

    public void setRenderFrame(boolean renderFrame)
    {
        this.renderFrame = renderFrame;
    }

    public void setCustomDataModelElementAttribute(XdmParseContext pc, XmlDataModelSchema schema, Object parent, String attrName, String attrValue)
            throws DataModelException, InvocationTargetException, IllegalAccessException, DataModelException
    {
        // if we don't know something about an attribute, save it for the template (pass it in)
        if (templateVars == null)
            templateVars = new HashMap();

        templateVars.put(TextUtils.getInstance().xmlTextToJavaIdentifier(attrName, false), attrValue);
    }

    public class SqlResultSetPopulator implements DataPopulator
    {
        public void populateData(Map vars, final HtmlTabularReportValueContext rc, final TabularReportDataSource ds)
        {
            vars.put(dataVarName, ((QueryResultSetDataSource) ds).getQueryResultSet().getResultSet());
        }
    }

    public class QueryResultSetPopulator implements DataPopulator
    {
        public void populateData(Map vars, final HtmlTabularReportValueContext rc, final TabularReportDataSource ds)
        {
            vars.put(dataVarName, ((QueryResultSetDataSource) ds).getQueryResultSet());
        }
    }

    public class TabularDataSourceDataPopulator implements DataPopulator
    {
        public void populateData(Map vars, final HtmlTabularReportValueContext rc, final TabularReportDataSource ds)
        {
            vars.put(dataVarName, ds);
        }
    }

    public class RawMatrixDataPopulator implements DataPopulator
    {
        public void populateData(Map vars, final HtmlTabularReportValueContext rc, final TabularReportDataSource ds)
        {
            List list = new ArrayList();

            TabularReportColumns columns = rc.getColumns();

            int rowsWritten = 0;
            int dataColsCount = columns.size();

            boolean hierarchical = ds.isHierarchical();
            HtmlTabularReportDataSourceScrollState scrollState = (HtmlTabularReportDataSourceScrollState) rc.getScrollState();
            boolean paging = scrollState != null;

            while (ds.next())
            {
                // construct the HTML for the data columns
                Object[] rowData = new Object[dataColsCount];
                for (int i = 0; i < dataColsCount; i++)
                    rowData[i] = ds.getActiveRowColumnData(i, 0);

                if (hierarchical)
                    list.add(new Object[]{ds.getActiveHierarchy(), rowData});
                else
                    list.add(rowData);

                rowsWritten++;
                // check to see if this row should be the last
                if (paging && rowsWritten == scrollState.getRowsPerPage())
                    break;

            }

            if (rowsWritten > 0 && paging)
            {
                // record the number of rows written to the total number of rows already displayed
                scrollState.accumulateRowsProcessed(rowsWritten);
                // if the total number of rows written is less than the scroll state's number of rows per page setting,
                // this must be the last page
                if (rowsWritten < scrollState.getRowsPerPage())
                    scrollState.setNoMoreRows();
            }

            vars.put(dataVarName, list);
        }
    }

    public class FormattedMatrixDataPopulator implements DataPopulator
    {
        public void populateData(Map vars, final HtmlTabularReportValueContext rc, final TabularReportDataSource ds)
        {
            List list = new ArrayList();

            HtmlTabularReport defn = ((HtmlTabularReport) rc.getReport());
            TabularReportColumns columns = rc.getColumns();
            TabularReportColumnState[] states = rc.getStates();

            int rowsWritten = 0;
            int dataColsCount = columns.size();

            boolean hierarchical = ds.isHierarchical();
            HtmlTabularReportDataSourceScrollState scrollState = (HtmlTabularReportDataSourceScrollState) rc.getScrollState();
            boolean paging = scrollState != null;

            while (ds.next())
            {
                // construct the HTML for the data columns
                String[] rowData = new String[dataColsCount];
                for (int i = 0; i < dataColsCount; i++)
                {
                    TabularReportColumn column = columns.getColumn(i);
                    TabularReportColumnState state = states[i];

                    if (!state.isVisible())
                        continue;

                    String data =
                            state.getFlags().flagIsSet(TabularReportColumn.Flags.HAS_OUTPUT_PATTERN) ?
                            state.getOutputFormat() :
                            column.getFormattedData(rc, ds, TabularReportColumn.GETDATAFLAG_DO_CALC);
                    RedirectValueSource redirect = column.getRedirect();
                    if (data != null && redirect != null)
                    {
                        String newdata = rc.getSkin().constructRedirect(rc, redirect, data, null, null);
                        data = defn.replaceOutputPatterns(rc, ds, newdata);
                    }

                    rowData[i] = data;
                }

                if (hierarchical)
                    list.add(new Object[]{ds.getActiveHierarchy(), rowData});
                else
                    list.add(rowData);

                rowsWritten++;
                // check to see if this row should be the last
                if (paging && rowsWritten == scrollState.getRowsPerPage())
                    break;

            }

            if (rowsWritten > 0 && paging)
            {
                // record the number of rows written to the total number of rows already displayed
                scrollState.accumulateRowsProcessed(rowsWritten);
                // if the total number of rows written is less than the scroll state's number of rows per page setting,
                // this must be the last page
                if (rowsWritten < scrollState.getRowsPerPage())
                    scrollState.setNoMoreRows();
            }

            vars.put(dataVarName, list);
        }
    }
}
