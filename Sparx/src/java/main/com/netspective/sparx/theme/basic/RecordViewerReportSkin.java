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
 * @author Aye Thu
 */

package com.netspective.sparx.theme.basic;

import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.HtmlPanelActions;
import com.netspective.sparx.panel.HtmlPanelAction;
import com.netspective.commons.command.Command;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.report.tabular.TabularReportDataSource;

import java.io.Writer;
import java.io.IOException;

/**
 * Class for producing a html report that allows adding and editing of data
 *
 * $Id: RecordViewerReportSkin.java,v 1.3 2003-07-12 02:20:33 aye.thu Exp $
 */
public class RecordViewerReportSkin extends BasicHtmlTabularReportPanelSkin
{
    public RecordViewerReportSkin()
    {
        super();
    }

    public RecordViewerReportSkin(Theme theme, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    /**
     * Renders the html report
     * @param writer
     * @param rc
     * @param ds
     * @throws IOException
     */
    public void render(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        // if ADD record action is defined, we want to display an "Add" action item in the frame but the frame
        // doesn't know about the report contained within. So, create a frame action item for the "Add" and fool
        // the frame into drawing it out.
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_ADD));
        if (reportAction != null)
        {
            HtmlPanelFrame frame = ((HtmlTabularReportValueContext) rc).getPanel().getFrame();
            HtmlPanelAction panelAction = frame.createAction();
            panelAction.setCaption(reportAction.getCaption());
            panelAction.setCommand(reportAction.getCommand());
            frame.addAction(panelAction);
        }

        super.render(writer, rc, ds);
    }

    /**
     *
     * @param writer
     * @param rc
     * @param ds
     * @param isOddRow
     * @throws IOException
     */
    public void produceDataRowDecoratorPrepend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_EDIT));
        if (reportAction != null)
        {
            Command actionCommand = reportAction.getCommand(rc);
            Theme theme = rc.getActiveTheme();
            String imgPath = rc.getThemeImagesRootUrl(theme) + "/" + panelResourcesPrefix;

            String label = "<img src=\"" + imgPath + "/content-action-edit.gif\" " +
                "alt=\"\" height=\"10\" width=\"10\" border=\"0\">";
            String editRecordUrl = this.constructRedirect(rc, actionCommand, label, null, null);

            writer.write("<td " + (isOddRow ? "class=\"report\"" : "class=\"report-alternative\"") + " width=\"10\">");
            writer.write(editRecordUrl);
            writer.write("</td>");
        }
    }

    /**
     * Gets the additional number of columns to prepend to the data
     * @param rc
     * @return
     */
    protected int getRowDecoratorPrependColsCount(HtmlTabularReportValueContext rc)
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        for (int i=0; actions != null && i < actions.size(); i++)
        {
            HtmlReportAction action = actions.get(i);
            if (action.getType().getValueIndex() == HtmlReportAction.Type.RECORD_EDIT)
            {
                return 1;
            }
        }
        return 0;
    }
}
