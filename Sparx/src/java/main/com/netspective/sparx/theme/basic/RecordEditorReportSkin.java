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
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.command.Command;

import java.io.Writer;
import java.io.IOException;

/**
 * @author aye
 * $Id: RecordEditorReportSkin.java,v 1.2 2003-07-11 14:37:46 aye.thu Exp $
 */
public class RecordEditorReportSkin extends BasicHtmlTabularReportPanelSkin
{
    public RecordEditorReportSkin(Theme theme, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    public void produceDataRowDecoratorAppend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        for (int i=0; actions != null && i < actions.size(); i++)
        {
            HtmlReportAction action = actions.get(i);
            if (action.getType().getValueIndex() == HtmlReportAction.Type.RECORD_DELETE)
            {
                Command actionCommand = action.getCommand(rc);
                Theme theme = rc.getActiveTheme();
                String imgPath = rc.getThemeImagesRootUrl(theme) + "/" + panelResourcesPrefix;

                String label = "<img src=\"" + imgPath + "/panel/output/content-action-delete.gif\" alt=\"\" height=\"10\" width=\"10\" border=\"0\">";
                String deleteRecordUrl = this.constructRedirect(rc, actionCommand, label, null, null);

                writer.write("<td " + (isOddRow ? "class=\"report\"" : "class=\"report-alternative\"") + " width=\"10\">");
                writer.write(deleteRecordUrl);
                writer.write("</td>");
                break;
            }
        }
    }

    public void produceDataRowDecoratorPrepend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        for (int i=0; actions != null && i < actions.size(); i++)
        {
            HtmlReportAction action = actions.get(i);
            if (action.getType().getValueIndex() == HtmlReportAction.Type.RECORD_EDIT)
            {
                Command actionCommand = action.getCommand(rc);
                Theme theme = rc.getActiveTheme();
                String imgPath = rc.getThemeImagesRootUrl(theme) + "/" + panelResourcesPrefix;

                String label = "<img src=\"" + imgPath + "/panel/output/content-action-edit.gif\" " +
                    "alt=\"\" height=\"10\" width=\"10\" border=\"0\">";
                String editRecordUrl = this.constructRedirect(rc, actionCommand, label, null, null);

                writer.write("<td " + (isOddRow ? "class=\"report\"" : "class=\"report-alternative\"") + " width=\"10\">");
                writer.write(editRecordUrl);
                writer.write("</td>");
                break;
            }
        }

    }



    protected int getRowDecoratorAppendColsCount(HtmlTabularReportValueContext rc)
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        for (int i=0; actions != null && i < actions.size(); i++)
        {
            HtmlReportAction action = actions.get(i);
            if (action.getType().getValueIndex() == HtmlReportAction.Type.RECORD_DELETE)
            {
                return 1;
            }
        }
        return 0;
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
