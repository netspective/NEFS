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
 * @author Aye Thu
 */

/**
 * $Id: PanelEditorSkin.java,v 1.1 2004-03-03 08:10:09 aye.thu Exp $
 */

package com.netspective.sparx.theme.basic;

import com.netspective.commons.command.CommandNotFoundException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.sparx.panel.HtmlPanelAction;
import com.netspective.sparx.panel.HtmlPanelActionStates;
import com.netspective.sparx.panel.HtmlPanelActions;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSource;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.basic.RecordEditorReportSkin;

import java.io.IOException;
import java.io.Writer;

public class PanelEditorSkin extends RecordEditorReportSkin
{
    public PanelEditorSkin()
    {
        super();
        setPanelClassNamePrefix("panel-editor");
        setPanelResourcesPrefix("panel/output");
    }

    public PanelEditorSkin(Theme theme, String name, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, name, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    public void produceHeadingExtras(Writer writer, HtmlPanelValueContext vc, HtmlPanelFrame frame) throws IOException
    {
        HtmlPanelActions actions = frame.getActions();
        HtmlPanelActionStates actionStates = vc.getPanelActionStates();
        if(actions != null && actions.size() > 0)
        {
            Theme theme = getTheme();
            int displayedItems = 0;

            // create a temporary string buffer for the HTML of the heading action items
            StringBuffer itemBuffer = new StringBuffer();
            for (int i=0; i < actions.size(); i++)
            {
                HtmlPanelAction item = actions.get(i);
               HtmlPanelAction.State state = actionStates.getState(item);
               if (state != null &&  state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                   continue;

                if (displayedItems == 0)
                {
                    itemBuffer.append("            <td bgcolor=\"white\"><img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/login/spacer.gif") + "\" width=\"5\" height=\"5\"></td>");
                    displayedItems++;
                }

                RedirectValueSource redirect = item.getRedirect();
                String itemUrl = redirect.getUrl(vc);
                String itemCaption = item.getCaption().getTextValue(vc);
                ValueSource itemIcon = item.getIcon();
                /*
                if (itemIcon != null)
                {
                    // icon for this item is defined so use the passed in image INSTEAD of using the CSS based background image
                    itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-item\" width=\"18\"></td>");
                    displayedItems++;
                }
                else
                {
                    itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-item\" width=\"18\"><img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/spacer.gif") + "\" width=\"18\" height=\"19\"></td>");
                    displayedItems++;
                }
                */
                itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-box\">" +
                        "<a class=\""+ panelClassNamePrefix +"-frame-action\" href=\""+ itemUrl + "\">&nbsp;" + itemCaption + "&nbsp;</a></td>");
                displayedItems++;
            }

            if (itemBuffer.length() > 0)
                writer.write(itemBuffer.toString());
        }
    }


    protected void produceBannerActionsHtml(Writer writer, HtmlPanelValueContext rc, HtmlPanelActions actions) throws IOException, CommandNotFoundException
    {
        int actionsCount = actions.size();
        if(actionsCount == 0) return;

        HtmlPanelActionStates actionStates = rc.getPanelActionStates();

        for(int i = 0; i < actionsCount; i++)
        {
            HtmlPanelAction action = actions.get(i);
            HtmlPanelAction.State state = actionStates.getState(action);
            if (state != null &&  state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                continue;

            ValueSource itemCaption = action.getCaption();
            ValueSource itemIcon = action.getIcon();
            String caption = "";
            RedirectValueSource itemRedirect = action.getRedirect();

            // Instead of using the caption use the icons/images as the labels
            if (itemRedirect == null)
            {
                caption = itemCaption != null ? itemCaption.getValue(rc).getTextValue() : "item" + i;
            }
            else
            {
                String hint = action.getHint() != null ? action.getHint().getValue(rc).getTextValue() : "";
                String label = itemCaption != null ? itemCaption.getValue(rc).getTextValue() : "item" + i;
                caption = constructRedirect(rc, itemRedirect, label, hint, null);
            }
            //if(i > 0)
            //    writer.write(", ");
            // if(itemIcon != null)
            // writer.write("<img src='" + itemIcon.getValue(rc).getTextValue() + "'>");
            writer.write(caption);
        }
    }

    public void produceDataRowDecoratorAppend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, String[] rowData, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        HtmlPanelActionStates actionStates = rc.getPanelActionStates();
        if (actions == null)
        {
            // no actions are defined in the report
            return;
        }
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_DELETE));
        if (reportAction != null)
        {
            HtmlPanelAction.State state = actionStates.getState(reportAction);
            if (state != null &&  state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                return;
            ValueSource redirect = reportAction.getRedirect();
            Theme theme = getTheme();

            String label = "<img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/content-action-delete.gif") + "\" alt=\"\" height=\"10\" width=\"10\" border=\"0\">";
            String deleteRecordUrl = this.constructRedirect(rc, redirect, label, null, null);
            deleteRecordUrl = report.replaceOutputPatterns(rc, ds, deleteRecordUrl);

            writer.write("<td " + (isOddRow ? "class=\"report-column-even\"" : "class=\"report-column-odd\"") + " width=\"10\">");
            writer.write(deleteRecordUrl);
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
        HtmlPanelActionStates actionStates = rc.getPanelActionStates();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report so return 0
            return cols;
        }
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_DELETE));
        if (reportAction != null)
        {
            HtmlPanelAction.State state = actionStates.getState(reportAction);
            if (state != null &&  state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                return cols;
            else
                return cols+1;
        }
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
        HtmlPanelActionStates actionStates = rc.getPanelActionStates();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report so return 0
            return cols;
        }
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_EDIT));
        if (reportAction != null)
        {
            HtmlPanelAction.State state = actionStates.getState(reportAction);
            if (state != null &&  state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                return cols;
            else
                return cols+1;
        }
        else
            return cols;
    }


}