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
package com.netspective.sparx.theme.basic;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.sparx.panel.HtmlPanelActions;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.theme.Theme;

public class SelectableHtmlTabularReportPanelSkin extends BasicHtmlTabularReportPanelSkin
{
    private boolean highlightRow = false;

    public SelectableHtmlTabularReportPanelSkin()
    {
        super();
    }

    public SelectableHtmlTabularReportPanelSkin(Theme theme, String name, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, name, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    public void produceHeadingExtras(Writer writer, HtmlPanelValueContext vc, HtmlPanelFrame frame) throws IOException
    {
        super.produceHeadingExtras(writer, vc, frame);

        HtmlTabularReportValueContext rc = ((HtmlTabularReportValueContext) vc);
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) rc.getReport();
        HtmlReportActions actions = report.getActions();
        HtmlPanelActions frameActions = frame.getActions();
        if(actions != null)
        {
            HtmlReportAction[] selectReportActions = actions.getByType(HtmlReportAction.Type.RECORD_SELECT);
            if(selectReportActions != null && selectReportActions.length > 0)
            {
                Theme theme = rc.getActiveTheme();
                RedirectValueSource redirect = (RedirectValueSource) selectReportActions[0].getRedirect();
                if(frameActions.size() > 0)
                    writer.write("            <td bgcolor=\"white\"><img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/spacer.gif") + "\" width=\"5\" height=\"5\"></td>");
                writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-action-item\" width=\"18\"><img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/spacer.gif") + "\" width=\"18\" height=\"19\"></td>");
                if(redirect != null)
                {
                    writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-action-box\">" +
                                 "<a class=\"" + panelClassNamePrefix + "-frame-action\" href=\"" + redirect.getUrl(rc) +
                                 "\">&nbsp;" + selectReportActions[0].getCaption().getTextValue(vc) + "&nbsp;</a></td>");
                }
            }
        }
    }

    /**
     * Renders the html report
     */
    public void render(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        /*BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        if (actions != null)
        {
            // if ADD record action is defined, we want to display an "Add" action item in the frame but the frame
            // doesn't know about the report contained within. So, create a frame action item for the "Add" and fool
            // the frame into drawing it out.
            HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_SELECT));
            if (reportAction != null)
            {
                HtmlPanelFrame frame = ((HtmlTabularReportValueContext) rc).getPanel().getFrame();
                HtmlPanelAction panelAction = frame.createAction();
                panelAction.setCaption(reportAction.getCaption() != null ? reportAction.getCaption() : new StaticValueSource("Process Items"));
                panelAction.setRedirect(reportAction.getRedirect());
                frame.addAction(panelAction);
            }
        }*/
        super.render(writer, rc, ds);
    }

    protected int getRowDecoratorPrependColsCount(HtmlTabularReportValueContext rc)
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) rc.getReport();
        HtmlReportActions actions = report.getActions();
        if(actions == null)
        {
            // no actions are defined in the report so return 0
            return 0;
        }
        HtmlReportAction[] selectReportActions = actions.getByType(HtmlReportAction.Type.RECORD_SELECT);
        if(selectReportActions != null && selectReportActions.length > 0)
            return 1;
        else
            return 0;
    }

    /**
     * Produces html to prepend to the data row
     */
    public void produceDataRowDecoratorPrepend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) rc.getReport();
        HtmlReportActions actions = report.getActions();
        if(actions == null)
        {
            // no actions are defined in the report
            return;
        }

        String rowData = report.getColumn(0).getOutput();
        writer.write("<td " + (isOddRow ? "class=\"report\"" : "class=\"report-alternative\"") + " width=\"10\">");
        writer.write("<input type=\"checkbox\" value=\"" + rowData + "\" name=\"checkbox_" + rowData +
                     "\" title=\"Click here to select the row.\" ");
        HttpServletRequest request = (HttpServletRequest) rc.getRequest();
        String[] selectedValues = request.getParameterValues("_dc.selected_item_list");
        if(selectedValues != null)
        {
            for(int i = 0; i < selectedValues.length; i++)
            {
                //System.out.println(selectedValues[i] + " " + rowData[0]);
                if(selectedValues[i].equalsIgnoreCase(rowData.toString()))
                {
                    writer.write("checked");
                    highlightRow = true;
                }
            }
        }
        writer.write(" onClick=\"handleRowCheckEvent(this, 'selected_item_list', " + rowData + ")\">\n");
        writer.write("</td>");
    }
}
