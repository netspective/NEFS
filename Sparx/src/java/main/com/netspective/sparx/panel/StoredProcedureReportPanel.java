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
 */

package com.netspective.sparx.panel;

import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.sql.QueryResultSetDataSource;
import com.netspective.sparx.sql.StoredProcedure;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.axiom.sql.QueryResultSet;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author  Aye Thu
 * @version $Id: StoredProcedureReportPanel.java,v 1.3 2003-11-26 17:31:42 shahid.shah Exp $
 */
public class StoredProcedureReportPanel extends AbstractHtmlTabularReportPanel
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(StoredProcedureReportPanel.class);
    private static final ValueSource NO_DATA_MSG = new StaticValueSource("No data in stored procedure results.");

    private HtmlTabularReport report;
    private String[] urlFormats;
    private boolean defaultPanel;
    private StoredProcedure parentProcedure;

    /**
     * Creates a new basic html tabulare report
     * @return
     */
    public HtmlTabularReport createReport()
    {
        return new BasicHtmlTabularReport();
    }

    /**
     * Sets the report for this panel
     * @param report
     */
    public void addReport(HtmlTabularReport report)
    {
        this.report = report;
    }

    public HtmlTabularReport getReport()
    {
        return report;
    }

    /**
     * Sets the stored procedure associated with this report panel
     * @param sp
     */
    public void setStoredProcedure(StoredProcedure sp)
    {
        parentProcedure = sp;
    }

    /**
     * Gets the stored procedure associated with this report panel
     * @return StoredProcedure object
     */
    public StoredProcedure getStoredProcedure()
    {
        return parentProcedure;
    }

    /**
     * Indicates whether or not this panel is the defaul report panel for the stored procedure
     * @return True if this is the default report panel
     */
    public boolean isDefaultPanel()
    {
        return defaultPanel;
    }

    /**
     * Sets the current report panel to be the default one
     * @param defaultPanel
     */
    public void setDefault(boolean defaultPanel)
    {
        this.defaultPanel = defaultPanel;
    }

    /**
     * Gets the name of the repot panel
     * @return
     */
    public String getName()
    {
        return getPanelIdentifier();
    }

    /**
     * Sets the name of the report panel
     * @param name
     */
    public void setName(String name)
    {
        setPanelIdentifier(name);
    }

    protected String getCachedResultSetAttributeId()
    {
        return "SPRS-" + this.hashCode();
    }

    /**
     * Executes the query and assigns the result set to a new report data source object
     * @param nc
     * @return
     */
    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        try
        {
            QueryResultSet resultSet = (QueryResultSet) nc.getAttribute(getCachedResultSetAttributeId());
            if(resultSet == null)
            {
                if (isScrollable())
                    resultSet = parentProcedure.execute(nc, null, true);
                else
                    resultSet = parentProcedure.execute(nc, null, false);
            }
            QueryResultSetDataSource qrsds = new QueryResultSetDataSource(NO_DATA_MSG);
            if (resultSet != null)
            {
                qrsds.setQueryResultSet(resultSet);
            }
            return qrsds;

        }
        catch (Exception e)
        {
            log.error("Unable to create data source", e);
            throw new NestableRuntimeException(e);
        }
    }

    /**
     *
     * @param nc
     * @return
     */
    public HtmlTabularReport getReport(NavigationContext nc)
    {
        HtmlTabularReport activeReport = getReport();
        if(activeReport == null)
        {
            // if the report is null, we need to create it by running the query and getting the meta data
            activeReport = new BasicHtmlTabularReport();
            try
            {
                QueryResultSet resultSet = null;
                if (isScrollable())
                    resultSet = parentProcedure.execute(nc, null, true);
                else
                    resultSet = parentProcedure.execute(nc, null, false);

                resultSet.fillReportFromMetaData(activeReport);
                nc.setAttribute(getCachedResultSetAttributeId(), resultSet); // store the result set so we don't run it again
            }
            catch (Exception e)
            {
                log.error("Unable to create report for query ", e);
                throw new NestableRuntimeException(e);
            }
            this.report = activeReport;
        }

        return activeReport;
    }
}
