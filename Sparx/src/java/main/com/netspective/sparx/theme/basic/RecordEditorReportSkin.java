/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

package com.netspective.sparx.theme.basic;

import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.commons.command.Command;

import java.io.Writer;
import java.io.IOException;

/**
 * @author aye
 * $Id: RecordEditorReportSkin.java,v 1.10 2003-08-30 16:41:29 shahid.shah Exp $
 */
public class RecordEditorReportSkin extends BasicHtmlTabularReportPanelSkin
{
    public RecordEditorReportSkin()
    {
        super();
    }

    public RecordEditorReportSkin(Theme theme, String name, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, name, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    public void produceDataRowDecoratorAppend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, String[] rowData, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report
            return;
        }
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_DELETE));

        if (reportAction != null)
        {
            Command actionCommand = reportAction.getCommand(rc);
            Theme theme = getTheme();

            String label = "<img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/content-action-delete.gif") + "\" alt=\"\" height=\"10\" width=\"10\" border=\"0\">";
            String deleteRecordUrl = this.constructRedirect(rc, actionCommand, label, null, null);
            deleteRecordUrl = report.replaceOutputPatterns(rc, ds, deleteRecordUrl);

            writer.write("<td " + (isOddRow ? "class=\"report-column-even\"" : "class=\"report-column-odd\"") + " width=\"10\">");
            writer.write(deleteRecordUrl);
            writer.write("</td>");

        }
    }

    /**
     *
     * @param writer
     * @param rc
     * @param ds
     * @param isOddRow
     * @throws IOException
     */
    public void produceDataRowDecoratorPrepend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, String[] rowData, boolean isOddRow) throws IOException
    {
        super.produceDataRowDecoratorPrepend(writer, rc, ds, rowData, isOddRow);
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report
            return;
        }
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_EDIT));
        if (reportAction != null)
        {
            Command actionCommand = reportAction.getCommand(rc);
            Theme theme = getTheme();

            String label = "<img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/content-action-edit.gif") + "\" " +
                "alt=\"\" height=\"10\" width=\"10\" border=\"0\">";
            String editRecordUrl = this.constructRedirect(rc, actionCommand, label, null, null);
            editRecordUrl = report.replaceOutputPatterns(rc, ds, editRecordUrl);
            writer.write("<td " + (isOddRow ? "class=\"report-column-even\"" : "class=\"report-column-odd\"") + " width=\"10\">");
            writer.write(editRecordUrl);
            writer.write("</td>");
        }
    }

    /**
     * Gets the additional number of columns to append after the data columns
     * @param rc
     * @return
     */
    protected int getRowDecoratorAppendColsCount(HtmlTabularReportValueContext rc)
    {
        int cols = super.getRowDecoratorAppendColsCount(rc);
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report so return 0
            return cols;
        }
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_DELETE));
        if (reportAction != null)
            return cols+1;
        else
            return cols;

    }

    /**
     * Gets the additional number of columns to prepend to the data
     * @param rc
     * @return
     */
    protected int getRowDecoratorPrependColsCount(HtmlTabularReportValueContext rc)
    {
        int cols = super.getRowDecoratorPrependColsCount(rc);
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report so return 0
            return cols;
        }
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_EDIT));
        if (reportAction != null)
            return cols+1;
        else
            return cols;
    }

}
