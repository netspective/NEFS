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

import com.netspective.commons.command.CommandNotFoundException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.HtmlPanelAction;
import com.netspective.sparx.panel.HtmlPanelActionStates;
import com.netspective.sparx.panel.HtmlPanelActions;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.theme.Theme;

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

    public void renderFrameBegin(Writer writer, HtmlPanelValueContext vc) throws IOException
    {
        HtmlPanel panel = vc.getPanel();
        HtmlPanelFrame frame = panel.getFrame();

        Theme theme = getTheme();

        writer.write("<table id=\"" + panel.getPanelIdentifier() + "_frame\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" nowrap ");
        if (flags.flagIsSet(BasicHtmlPanelSkin.Flags.FULL_WIDTH))
            writer.write("width='100%' ");
        writer.write(">\n");

        if (frame.hasHeadingOrFooting())
        {
            String heading = null;
            ValueSource hvs = frame.getHeading();
            if (hvs != null)
                heading = hvs.getValue(vc).getTextValue();
            if (heading != null && !frame.isHideHeading(vc))
            {
                writer.write("<tr>\n");
                writer.write("    <td class=\"" + panelClassNamePrefix + "\">\n");
                writer.write("    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" nowrap>\n");
                writer.write("        <tr>\n");
                if (frame.getFlags().flagIsSet(HtmlPanelFrame.Flags.ALLOW_COLLAPSE))
                {
                    if (vc.isMinimized())
                        writer.write("            <td id=\"" + panel.getPanelIdentifier() + "_action\" class=\"" + panelClassNamePrefix + "-frame-heading-action-expand\" align=\"left\" valign=\"middle\" nowrap width=\"17\" onclick=\"ALL_PANELS.togglePanelExpandCollapse('" + panel.getPanelIdentifier() + "')\">" +
                                "<!-- <img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/spacer.gif") + "\" alt=\"\" height=\"5\" width=\"17\" border=\"0\">--></td>");
                    else
                        writer.write("            <td id=\"" + panel.getPanelIdentifier() + "_action\" class=\"" + panelClassNamePrefix + "-frame-heading-action-collapse\"   align=\"left\" valign=\"middle\" nowrap width=\"17\" onclick=\"ALL_PANELS.togglePanelExpandCollapse('" + panel.getPanelIdentifier() + "')\">" +
                                "<!-- <img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/spacer.gif") + "\" alt=\"\" height=\"5\" width=\"17\" border=\"0\"> --></td>");

                    writer.write("<script>ALL_PANELS.getPanel(\"" + panel.getPanelIdentifier() + "\").minimized = " + (vc.isMinimized()
                            ? "true" : "false") + "</script>");
                }
                else
                {
                    writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-heading-action-left-blank\" align=\"left\" valign=\"middle\" nowrap width=\"17\">" +
                            "<!-- <img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/spacer.gif") + "\" alt=\"\" height=\"5\" width=\"17\" border=\"0\">--></td>\n");
                }
                writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-heading\" align=\"left\" valign=\"middle\" nowrap>" + heading +
                        "</td>\n");
                writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-heading-action-right-blank\" align=\"center\" valign=\"middle\" nowrap width=\"17\">" +
                        "<!-- <img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/spacer.gif") + "\" alt=\"\" height=\"5\" width=\"17\" border=\"0\">--></td>\n");
                writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-mid\" align=\"right\" valign=\"top\" nowrap width=\"100%\">" +
                        "<!-- <img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/spacer.gif") + "\" alt=\"\" height=\"5\" width=\"100%\" border=\"0\">--></td>\n");
                //writer.write("            <td class=\""+ panelClassNamePrefix +"-frame-end-cap\" align=\"right\" valign=\"top\" nowrap width=\"2\"></td>\n");
                produceHeadingExtras(writer, vc, frame);
                writer.write("        </tr>\n");
                writer.write("    </table>\n");
                writer.write("    </td>\n");
                writer.write("</tr>\n");
            }
            else
            {
                writer.write("<tr>\n");
                writer.write("    <td class=\"" + panelClassNamePrefix + "\">\n");
                writer.write("    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" nowrap>\n");
                writer.write("        <tr>\n");
                writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-begin-cap\" align=\"left\" valign=\"top\" nowrap width=\"2\"></td>\n");
                writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-mid\" align=\"right\" valign=\"top\" nowrap width=\"100%\"></td>\n");
                writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-end-cap\" align=\"right\" valign=\"top\" nowrap width=\"2\"></td>\n");
                writer.write("        </tr>\n");
                writer.write("    </table>\n");
                writer.write("    </td>\n");
                writer.write("</tr>\n");
            }
        }

        renderBanner(writer, vc);

        int height = panel.getHeight();
        int width = panel.getWidth();
        if (height > 0)
            writer.write("<tr id=\"" + panel.getPanelIdentifier() + "_content\">\n     <td class=\"" + panelClassNamePrefix + "-content\"><div class='" + contentDivClass + "' style=\"width: " + width + "; height: " + height + "; overflow: auto;\">\n");
        else
            writer.write("<tr id=\"" + panel.getPanelIdentifier() + "_content\">\n     <td class=\"" + panelClassNamePrefix + "-content\"><div class='" + contentDivClass + "' style=\"width: " + width + ";\">\n");
    }

    public void produceHeadingExtras(Writer writer, HtmlPanelValueContext vc, HtmlPanelFrame frame) throws IOException
    {
        HtmlPanelActions actions = frame.getActions();
        HtmlPanelActionStates actionStates = vc.getPanelActionStates();
        if (actions != null && actions.size() > 0)
        {
            Theme theme = getTheme();
            int displayedItems = 0;

            // create a temporary string buffer for the HTML of the heading action items
            StringBuffer itemBuffer = new StringBuffer();
            for (int i = 0; i < actions.size(); i++)
            {
                HtmlPanelAction item = actions.get(i);
                HtmlPanelAction.State state = actionStates.getState(item);
                if (state != null && state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                    continue;

                if (displayedItems == 0)
                {
                    itemBuffer.append("            <td class=\"" + panelClassNamePrefix + "-frame-mid\"><img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/login/spacer.gif") + "\" width=\"25\" height=\"5\"></td>");
                    displayedItems++;
                }

                RedirectValueSource redirect = item.getRedirect();

                String itemUrl = redirect.getUrl(vc);
                // NOTE: This is a fix to process any remaining value sources in the URL
                ValueSource vs = ValueSources.getInstance().createValueSourceOrStatic("simple-expr:" + itemUrl);
                itemUrl = vs.getTextValue(vc);
                String itemCaption = item.getCaption().getTextValue(vc);
                itemBuffer.append("            <td class=\"" + panelClassNamePrefix + "-frame-action-item\">" +
                        "<a  href=\"" + itemUrl + "\">&nbsp;" + itemCaption + "&nbsp;</a></td>");
                displayedItems++;
            }
            if (itemBuffer.length() > 0)
                writer.write(itemBuffer.toString());
            writer.write("            <td class=\"" + panelClassNamePrefix + "-frame-end-cap\" align=\"right\" valign=\"top\" nowrap width=\"2\">" +
                    "<img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/login/spacer.gif") + "\" width=\"2\" height=\"5\"></td>\n");

        }
    }


    protected void produceBannerActionsHtml(Writer writer, HtmlPanelValueContext rc, HtmlPanelActions actions) throws IOException, CommandNotFoundException
    {
        int actionsCount = actions.size();
        if (actionsCount == 0) return;

        HtmlPanelActionStates actionStates = rc.getPanelActionStates();

        for (int i = 0; i < actionsCount; i++)
        {
            HtmlPanelAction action = actions.get(i);
            HtmlPanelAction.State state = actionStates.getState(action);
            if (state != null && state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                continue;

            ValueSource itemCaption = action.getCaption();
            String itemUrl = action.getRedirect().getUrl(rc);
            // NOTE: This is a fix to process any remaining value sources in the URL
            ValueSource vs = ValueSources.getInstance().createValueSourceOrStatic("simple-expr:" + itemUrl);
            itemUrl = vs.getTextValue(rc);
            String caption = itemCaption != null ? itemCaption.getValue(rc).getTextValue() : "item" + i;
            RedirectValueSource itemRedirect = action.getRedirect();

            // Instead of using the caption use the icons/images as the labels
            if (itemRedirect == null)
            {
                writer.write(caption);
            }
            else
            {
                String hint = action.getHint() != null ? action.getHint().getValue(rc).getTextValue() : "";
                writer.write("<a href=\"" + itemUrl + "\" title=\"" + hint + "\">" + caption + "</a>");
            }
        }
    }

    public void produceDataRowDecoratorPrepend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, String[] rowData, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) rc.getReport();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report
            return;
        }
        HtmlReportAction[] editReportActions = actions.getByType(HtmlReportAction.Type.RECORD_EDIT);
        if (editReportActions != null && editReportActions.length > 0)
        {
            ValueSource redirect = editReportActions[0].getRedirect();
            Theme theme = getTheme();

            String label = "<img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/panel-editor-action-edit.gif") + "\" " +
                    "alt=\"\" height=\"7\" width=\"7\" border=\"0\">";
            String editRecordUrl = this.constructRedirect(rc, redirect, label, null, null);
            editRecordUrl = report.replaceOutputPatterns(rc, ds, editRecordUrl);
            writer.write("<td " + (isOddRow ? "class=\"report-column-even\"" : "class=\"report-column-odd\"") + " width=\"10\">");
            writer.write(editRecordUrl);
            writer.write("</td>");
        }
    }


    public void produceDataRowDecoratorAppend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, String[] rowData, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) rc.getReport();
        HtmlReportActions actions = report.getActions();
        HtmlPanelActionStates actionStates = rc.getPanelActionStates();
        if (actions == null)
        {
            // no actions are defined in the report
            return;
        }
        HtmlReportAction[] deleteReportActions = actions.getByType(HtmlReportAction.Type.RECORD_DELETE);
        if (deleteReportActions != null && deleteReportActions.length > 0)
        {
            HtmlPanelAction.State state = actionStates.getState(deleteReportActions[0]);
            if (state != null && state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                return;
            ValueSource redirect = deleteReportActions[0].getRedirect();
            Theme theme = getTheme();

            String label = "<img src=\"" + theme.getResourceUrl("/images/" + panelResourcesPrefix + "/panel-editor-action-delete.gif") + "\" alt=\"\" height=\"7\" width=\"7\" border=\"0\">";
            String deleteRecordUrl = this.constructRedirect(rc, redirect, label, null, null);
            deleteRecordUrl = report.replaceOutputPatterns(rc, ds, deleteRecordUrl);

            writer.write("<td " + (isOddRow ? "class=\"report-column-even\"" : "class=\"report-column-odd\"") + " width=\"10\">");
            writer.write(deleteRecordUrl);
            writer.write("</td>");

        }
    }

    /**
     * Gets the additional number of columns to append after the data columns
     *
     * @param rc
     *
     * @return
     */
    protected int getRowDecoratorAppendColsCount(HtmlTabularReportValueContext rc)
    {
        int cols = super.getRowDecoratorAppendColsCount(rc);
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) rc.getReport();
        HtmlPanelActionStates actionStates = rc.getPanelActionStates();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report so return 0
            return cols;
        }
        HtmlReportAction[] deleteReportActions = actions.getByType(HtmlReportAction.Type.RECORD_DELETE);
        if (deleteReportActions != null && deleteReportActions.length > 0)
        {
            HtmlPanelAction.State state = actionStates.getState(deleteReportActions[0]);
            if (state != null && state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                return cols;
            else
                return cols + 1;
        }
        else
            return cols;

    }

    /**
     * Gets the additional number of columns to prepend to the data
     *
     * @param rc
     *
     * @return
     */
    protected int getRowDecoratorPrependColsCount(HtmlTabularReportValueContext rc)
    {
        int cols = super.getRowDecoratorPrependColsCount(rc);
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) rc.getReport();
        HtmlPanelActionStates actionStates = rc.getPanelActionStates();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report so return 0
            return cols;
        }
        HtmlReportAction[] editReportActions = actions.getByType(HtmlReportAction.Type.RECORD_EDIT);
        if (editReportActions != null && editReportActions.length > 0)
        {
            HtmlPanelAction.State state = actionStates.getState(editReportActions[0]);
            if (state != null && state.getStateFlags().flagIsSet(HtmlPanelAction.Flags.HIDDEN))
                return cols;
            else
                return cols + 1;
        }
        else
            return cols;
    }


}