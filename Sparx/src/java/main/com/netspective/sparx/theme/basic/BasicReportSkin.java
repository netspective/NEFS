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

/**
 * $Id: BasicReportSkin.java,v 1.6 2003-04-02 22:53:52 shahid.shah Exp $
 */

package com.netspective.sparx.theme.basic;

import java.io.IOException;
import java.io.Writer;

import com.netspective.sparx.report.tabular.TabularReportFrame;
import com.netspective.sparx.report.tabular.TabularReportBanner;
import com.netspective.commons.report.tabular.TabularReportColumns;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.report.tabular.TabularReportColumnState;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.sparx.report.tabular.TabularReportAction;
import com.netspective.sparx.report.tabular.TabularReportActions;
import com.netspective.sparx.report.tabular.TabularReport;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.report.ReportHttpServletValueContext;
import com.netspective.sparx.report.HtmlTabularReportSkin;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.lang.ClassPath;
import com.netspective.commons.command.Command;
import com.netspective.commons.command.CommandNotFoundException;

public class BasicReportSkin extends AbstractThemeSkin implements HtmlTabularReportSkin
{
    static public final int HTMLFLAG_SHOW_BANNER = 1;
    static public final int HTMLFLAG_SHOW_HEAD_ROW = HTMLFLAG_SHOW_BANNER * 2;
    static public final int HTMLFLAG_SHOW_FOOT_ROW = HTMLFLAG_SHOW_HEAD_ROW * 2;
    static public final int HTMLFLAG_ADD_ROW_SEPARATORS = HTMLFLAG_SHOW_FOOT_ROW * 2;
    static public final int HTMLFLAG_FULL_WIDTH = HTMLFLAG_ADD_ROW_SEPARATORS * 2;
    static public final int HTMLFLAG_STARTCUSTOM = HTMLFLAG_FULL_WIDTH * 2;

    private String panelStyle;
    protected int flags;

    public BasicReportSkin(Theme theme, String panelStyle, boolean fullWidth)
    {
        super(theme);
        setPanelStyle(panelStyle);
        setFlag(HTMLFLAG_SHOW_BANNER | HTMLFLAG_SHOW_HEAD_ROW | HTMLFLAG_SHOW_FOOT_ROW | HTMLFLAG_ADD_ROW_SEPARATORS);
        if(fullWidth)
            setFlag(HTMLFLAG_FULL_WIDTH);
    }

    public String constructClassRef(Class cls)
    {
        return "<span title=\""+ ClassPath.getClassFileName(cls) +"\">" + cls.getName() + "</span>";
    }

    public String getBlankValue()
    {
        return "&nbsp;";
    }

    public String getPanelStyle()
    {
        return panelStyle;
    }

    public void setPanelStyle(String panelStyle)
    {
        this.panelStyle = panelStyle;
    }

    public String getFileExtension()
    {
        return ".html";
    }

    public final long getFlags()
    {
        return flags;
    }

    public final boolean flagIsSet(long flag)
    {
        return (flags & flag) == 0 ? false : true;
    }

    public final void setFlag(long flag)
    {
        flags |= flag;
    }

    public final void clearFlag(long flag)
    {
        flags &= ~flag;
    }

    public final void updateFlag(long flag, boolean set)
    {
        if(set) flags |= flag; else flags &= ~flag;
    }

    public void produceHeadingExtras(Writer writer, TabularReportValueContext rc, TabularReportFrame frame) throws IOException
    {
        TabularReportActions actions = frame.getActions();

        if(actions != null && actions.size() > 0)
        {
            Theme theme = ((ReportHttpServletValueContext) rc).getActiveTheme();
            String imgPath = ((ReportHttpServletValueContext) rc).getThemeImagesRootUrl(theme) + "/" + panelStyle;

            int colCount = 0;

            // create a temporary string buffer for the HTML of the heading action items
            StringBuffer itemBuffer = new StringBuffer();
            for (int i=0; i < actions.size(); i++)
            {
                if (i != 0)
                {
                    itemBuffer.append("            <td bgcolor=\"white\"><img src=\"" + imgPath + "/login/spacer.gif\" width=\"5\" height=\"5\"></td>");
                    colCount++;
                }
                TabularReportAction item = actions.get(i);
                String itemUrl = "TODO"; //item.getParameters();
                String itemCaption = item.getCaption().getTextValue(rc);
                ValueSource itemIcon = item.getIcon();
                if (itemIcon != null)
                {
                    // icon for this item is defined so use the passed in image INSTEAD of using the CSS based background image
                    itemBuffer.append("            <td class=\"panel-frame-action-item-output\"><img src=\"" + itemIcon.getValue(rc) + "\" height=\"14\" width=\"17\" border=\"0\"></td>");
                    colCount++;
                }
                else
                {
                    itemBuffer.append("            <td class=\"panel-frame-action-item-output\" width=\"17\"><img src=\"" + imgPath +
                        "/panel/output/spacer.gif\" alt=\"\" height=\"14\" width=\"17\" border=\"0\"></td>");
                    colCount++;
                }
                itemBuffer.append("            <td class=\"panel-frame-action-box-output\">" +
                        "<a class=\"panel-frame-action-output\" href=\""+ itemUrl + "\">&nbsp;" + itemCaption + "&nbsp;</a></td>");
                colCount++;
            }

            writer.write("<td nowrap>\n");
            writer.write("    <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
            writer.write("        <tr>\n");
            writer.write("            <td bgcolor=\"white\" width=\"100%\" colspan=\"" + colCount + "\">" +
                    "<img src=\"" + imgPath + "/login/spacer.gif\" height=\"5\"></td>\n");
            writer.write("        </tr>\n");
            if (itemBuffer.length() > 0)
            {
                writer.write("        <tr>\n");
                writer.write(itemBuffer.toString());
                writer.write("        </tr>\n");
            }
            writer.write("        </tr>\n  ");
            writer.write("    </table>\n");
            writer.write("</td>\n");
        }
    }

    public void produceReport(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        TabularReportFrame frame = ((TabularReport) rc.getReport()).getFrame();

        Theme theme = ((ReportHttpServletValueContext) rc).getActiveTheme();
        String imgPath = ((ReportHttpServletValueContext) rc).getThemeImagesRootUrl(theme) + "/" + panelStyle;

        writer.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" nowrap ");
        if(flagIsSet(HTMLFLAG_FULL_WIDTH))
            writer.write("width='100%' ");
        writer.write(">\n");

        if(frame.hasHeadingOrFooting())
        {
            String heading = null;
            ValueSource hvs = frame.getHeading();
            if(hvs != null)
                heading = hvs.getValue(rc).getTextValue();

            if(heading != null)
            {
                writer.write("<tr>\n");
                writer.write("    <td class=\"panel-output\">\n");
                writer.write("    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" nowrap>\n");
                writer.write("        <tr>\n");
                if (frame.getFlags().flagIsSet(TabularReportFrame.Flags.ALLOW_COLLAPSE))
                {
                    if (rc.isMinimized())
                        writer.write("            <td class=\"panel-frame-heading-action-expand-output\"   align=\"left\" valign=\"middle\" nowrap width=\"17\">" +
                            "<img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"17\" border=\"0\"></td>");
                    else
                        writer.write("            <td class=\"panel-frame-heading-action-collapse-output\"   align=\"left\" valign=\"middle\" nowrap width=\"17\">" +
                            "<img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"17\" border=\"0\"></td>");
                }
                else
                {
                    writer.write("            <td class=\"panel-frame-heading-action-left-blank-output\" align=\"left\" valign=\"middle\" nowrap width=\"17\">" +
                        "<img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"17\" border=\"0\"></td>\n");
                }
                writer.write("            <td class=\"panel-frame-heading-output\" align=\"left\" valign=\"middle\" nowrap>" + heading +
                        "</td>\n");
                writer.write("            <td class=\"panel-frame-heading-action-right-blank-output\" align=\"center\" valign=\"middle\" nowrap width=\"17\">" +
                    "<img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"17\" border=\"0\"></td>\n");
                writer.write("            <td class=\"panel-frame-mid-output\" align=\"right\" valign=\"top\" nowrap width=\"100%\">" +
                    "<img src=\"" + imgPath + "/panel/output/spacer.gif\" alt=\"\" height=\"5\" width=\"100%\" border=\"0\"></td>\n");
                writer.write("            <td class=\"panel-frame-end-cap-output\" align=\"right\" valign=\"top\" nowrap width=\"2\"></td>\n");
                produceHeadingExtras(writer, rc, frame);
                writer.write("        </tr>\n");
                writer.write("    </table>\n");
                writer.write("    </td>\n");
                writer.write("</tr>\n");
            }
        }

        produceBannerRow(writer, rc);

        writer.write("<tr>\n" +
                "    <td class=\"panel-content-output\">\n");
        writer.write("    <table class=\"report\" width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">\n");
        if(flagIsSet(HTMLFLAG_SHOW_HEAD_ROW) && !rc.getReport().getFlags().flagIsSet(TabularReport.Flags.HIDE_HEADING))
        {
            if(!rc.getReport().getFlags().flagIsSet(TabularReport.Flags.FIRST_DATA_ROW_HAS_HEADINGS))
                produceHeadingRow(writer, rc);
            else
                produceHeadingRow(writer, rc, ds);
        }

        produceDataRows(writer, rc, ds);

        if(flagIsSet(HTMLFLAG_SHOW_FOOT_ROW) && rc.getCalcsCount() > 0)
            produceFootRow(writer, rc);
        writer.write("    </table>\n");
        writer.write("    </td>\n");
        writer.write("</tr>\n");
        if(frame.hasHeadingOrFooting())
        {
            ValueSource fvs = frame.getFooting();
            if(fvs != null)
            {
                writer.write("<tr>\n");
                writer.write("    <td class=\"panel-banner-footer-output\">" + fvs.getTextValue(rc) + "</td>\n");
                writer.write("</tr>\n");
            }
        }

        writer.write("</table>\n");
    }

    protected void producerBannerActionsHtml(Writer writer, TabularReportValueContext rc, TabularReportActions actions) throws IOException, CommandNotFoundException
    {
        int actionsCount = actions.size();
        if(actionsCount == 0) return;
        String bannerItemFontAttrs = "";

        //TODO: conversion from Sparx 2.x required below
        //ReportSkin skin = rc.getSkin();
        //if (skin instanceof com.netspective.sparx.xaf.skin.HtmlReportSkin)
        //    bannerItemFontAttrs = ((HtmlReportSkin) skin).getBannerItemFontAttrs();

        if(actions.getStyle().getValueIndex() == TabularReportActions.Style.HORIZONTAL)
        {
            for(int i = 0; i < actionsCount; i++)
            {
                TabularReportAction action = actions.get(i);
                Command itemCmd = action.getCommand(rc);
                ValueSource itemCaption = action.getCaption();
                ValueSource itemIcon = action.getIcon();
                String caption = itemCaption != null ? (itemCmd != null ? ("<a href='" + itemCmd.getParameters() + "'>" + itemCaption.getValue(rc) + "</a>") : itemCaption.getValue(rc).getTextValue()) : null;

                writer.write("<font " + bannerItemFontAttrs + ">");
                if(i > 0)
                    writer.write(", ");
                if(itemIcon != null)
                    writer.write("<img src='" + itemIcon.getValue(rc) + "'>");
                writer.write(caption);
                writer.write("</font>");
            }
        }
        else
        {
            writer.write("<table border=0 cellspacing=0>");
            for(int i = 0; i < actionsCount; i++)
            {
                TabularReportAction action = actions.get(i);
                Command itemCmd = action.getCommand(rc);
                ValueSource itemCaption = action.getCaption();
                ValueSource itemIcon = action.getIcon();
                TabularReportActions childItems = action.getChildren();
                String caption = itemCaption != null ? (itemCmd != null ? ("<a href='" + itemCmd.getParameters() + "'>" + itemCaption.getValue(rc) + "</a>") : itemCaption.getValue(rc).getTextValue()) : null;

                writer.write("<tr><td>");
                writer.write(itemIcon != null ? "<img src='" + itemIcon.getValue(rc) + "'>" : "-");
                writer.write("</td>");
                writer.write("<td><font " + bannerItemFontAttrs + ">");
                if(caption != null)
                    writer.write(caption);
                if(childItems != null && childItems.size() > 0)
                    producerBannerActionsHtml(writer, rc, childItems);
                writer.write("</font></td>");
                writer.write("</tr>");
            }
            writer.write("</table>");
        }
    }

    /**
     * Displays the report banner html. Utilizes the THEME
     * @param writer
     * @param rc
     * @throws IOException
     */
    protected void produceBannerRow(Writer writer, TabularReportValueContext rc) throws IOException
    {
        TabularReportBanner banner = ((TabularReport) rc.getReport()).getBanner();
        if (banner == null)
            return;

        TabularReportActions actions = banner.getActions();
        ValueSource content = banner.getContent();
        if((actions == null || (actions != null || actions.size() == 0) && content == null))
            return;

        writer.write("<tr><td class=\"panel-banner-output\">\n");
        if(content != null)
        {
            writer.write(content.getTextValue(rc));
        }
        else
        {
            try
            {
                producerBannerActionsHtml(writer, rc, actions);
            }
            catch (CommandNotFoundException e)
            {
                writer.write(e.toString());
            }
        }
        writer.write("</td></tr>\n");
    }

    private int getTableColumnsCount(TabularReportValueContext rc)
    {
        return (rc.getVisibleColsCount() * 2) +
               (getRowDecoratorPrependColsCount(rc) * 2) +
               (getRowDecoratorAppendColsCount(rc) * 2) +
               + 1; // each column has "spacer" in between, first column as spacer before too
    }

    public void produceHeadingRowDecoratorPrepend(Writer writer, TabularReportValueContext rc) throws IOException
    {
    }

    public void produceHeadingRowDecoratorAppend(Writer writer, TabularReportValueContext rc) throws IOException
    {
    }

    public void produceDataRowDecoratorPrepend(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds, boolean isOddRow) throws IOException
    {
    }

    public void produceDataRowDecoratorAppend(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds, boolean isOddRow) throws IOException
    {
    }

    public void produceHeadingRow(Writer writer, TabularReportValueContext rc) throws IOException
    {
        TabularReportColumns columns = rc.getColumns();
        TabularReportColumnState[] states = rc.getStates();
        int dataColsCount = columns.size();

        Theme theme = ((ReportHttpServletValueContext) rc).getActiveTheme();
        String imgPath = ((ReportHttpServletValueContext) rc).getThemeImagesRootUrl(theme) + "/" + panelStyle;

        String sortAscImgTag = " <img src=\""+ imgPath + "/column-sort-ascending.gif\" border=0>";
        String sortDescImgTag = " <img src=\""+ imgPath + "/column-sort-descending.gif\" border=0>";

        writer.write("<tr>");
        produceHeadingRowDecoratorPrepend(writer, rc);

        for(int i = 0; i < dataColsCount; i++)
        {
            TabularReportColumn rcd = columns.getColumn(i);
            TabularReportColumnState rcs = rc.getState(i);
            if(! states[i].isVisible())
                continue;

            String colHeading = rcs.getHeading();
            if(colHeading != null)
            {
                ValueSource headingAnchorAttrs = rcd.getHeadingAnchorAttrs();
                if(headingAnchorAttrs != null)
                    colHeading = "<a " + headingAnchorAttrs.getValue(rc) + ">" + colHeading + "</a>";
                if(rcs.flagIsSet(TabularReportColumn.COLFLAG_SORTED_ASCENDING))
                    colHeading += sortAscImgTag;
                if(rcs.flagIsSet(TabularReportColumn.COLFLAG_SORTED_DESCENDING))
                    colHeading += sortDescImgTag;

                writer.write("        <td class=\"report-field\" nowrap>" + colHeading  + "</td>");
            }
            else
                writer.write("        <td class=\"report-field\" nowrap>&nbsp;&nbsp;</td>");
        }

        produceHeadingRowDecoratorAppend(writer, rc);

        writer.write("</tr>");
    }

    public void produceHeadingRow(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        TabularReportColumns columns = rc.getColumns();
        TabularReportColumnState[] states = rc.getStates();
        int dataColsCount = columns.size();

        // get the first row (heading)
        if(!ds.next()) return;

        Theme theme = ((ReportHttpServletValueContext) rc).getActiveTheme();
        String imgPath = ((ReportHttpServletValueContext) rc).getThemeImagesRootUrl(theme) + "/" + panelStyle;

        String sortAscImgTag = " <img src=\""+ imgPath + "/column-sort-ascending.gif\" border=0>";
        String sortDescImgTag = " <img src=\""+ imgPath + "/column-sort-descending.gif\" border=0>";

        writer.write("<tr>");
        produceHeadingRowDecoratorPrepend(writer, rc);

        for(int i = 0; i < dataColsCount; i++)
        {
            TabularReportColumn rcd = columns.getColumn(i);
            TabularReportColumnState rcs = rc.getState(i);
            if(! states[i].isVisible())
                continue;

            Object heading = ds.getActiveRowColumnData(rc, rcd.getColIndex(), flags);
            if(heading != null)
            {
                String colHeading = heading.toString();
                ValueSource headingAnchorAttrs = rcd.getHeadingAnchorAttrs();
                if(headingAnchorAttrs != null)
                    colHeading = "<a " + headingAnchorAttrs.getValue(rc) + ">" + colHeading + "</a>";
                if(rcs.flagIsSet(TabularReportColumn.COLFLAG_SORTED_ASCENDING))
                    colHeading += sortAscImgTag;
                if(rcs.flagIsSet(TabularReportColumn.COLFLAG_SORTED_DESCENDING))
                    colHeading += sortDescImgTag;

                writer.write("        <td class=\"report-field\" nowrap>" + colHeading  + "</td>");
            }
            else
                writer.write("        <td class=\"report-field\" nowrap>&nbsp;&nbsp;</td>");
        }

        produceHeadingRowDecoratorAppend(writer, rc);
    }

    public void produceDataRows(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        TabularReport defn = ((TabularReport) rc.getReport());
        TabularReportColumns columns = rc.getColumns();
        TabularReportColumnState[] states = rc.getStates();

        int rowsWritten = 0;
        int dataColsCount = columns.size();
        int tableColsCount = getTableColumnsCount(rc);

        boolean hiearchical = ds.isHierarchical();

        //TODO: Sparx 2.x conversion required
        //ResultSetScrollState scrollState = rc.getScrollState();
        //boolean paging = scrollState != null;

        boolean isOddRow = false;
        while(ds.next())
        {
            isOddRow = ! isOddRow;

            writer.write("<tr>");
            produceDataRowDecoratorPrepend(writer, rc, ds, isOddRow);

            int hiearchyCol = 0;
            int activeLevel = 0;

            if(hiearchical)
            {
                TabularReportDataSource.Hierarchy activeHierarchy = ds.getActiveHiearchy();
                hiearchyCol = activeHierarchy.getColumn();
                activeLevel = activeHierarchy.getLevel();
            }

            for(int i = 0; i < dataColsCount; i++)
            {

                TabularReportColumn column = columns.getColumn(i);
                TabularReportColumnState state = states[i];

                if(! state.isVisible())
                    continue;

                String data =
                        state.flagIsSet(TabularReportColumn.COLFLAG_HASOUTPUTPATTERN) ?
                        state.getOutputFormat() :
                        column.getFormattedData(rc, ds, TabularReportColumn.GETDATAFLAG_DO_CALC);

                String style = state.getCssStyleAttrValue();
                if(hiearchical && (hiearchyCol == i) && activeLevel > 0)
                    style += "padding-left:" + (activeLevel * 15);

                String singleRow = "<td " + (isOddRow ? "class=\"report\"" : "class=\"report-alternative\"") + " style=\"" + style +  "\">" +
                        (state.flagIsSet(TabularReportColumn.COLFLAG_WRAPURL) ? "<a href=\"" + state.getUrl() + "\" " + state.getUrlAnchorAttrs() + ">" +
                        data + "</a>" : data) +
                        "&nbsp;</td>";

                writer.write(defn.replaceOutputPatterns(rc, ds, singleRow));
            }

            produceDataRowDecoratorAppend(writer, rc, ds, isOddRow);

            rowsWritten++;
            //TODO: Sparx 2.x conversion required
            //if(paging && rc.endOfPage())
            //    break;
        }

        if(rowsWritten == 0)
        {
            writer.write("<tr><td class=\"report-summary\" colspan='" + tableColsCount + "'>No data found.</td></tr>");
            //TODO: Sparx 2.x conversion required
            //if(paging)
            //    scrollState.setNoMoreRows();
        }
        //TODO: Sparx 2.x conversion required
        //else if(paging)
        //{
        //    scrollState.accumulateRowsProcessed(rowsWritten);
        //    if(rowsWritten < scrollState.getRowsPerPage())
        //        scrollState.setNoMoreRows();
        //}
    }

    public void produceFootRow(Writer writer, TabularReportValueContext rc) throws IOException
    {
        int calcsCount = rc.getCalcsCount();
        if(calcsCount == 0)
            return;

        TabularReportColumnState[] states = rc.getStates();
        TabularReportColumns columns = rc.getColumns();
        int dataColsCount = columns.size();

        writer.write("<tr>");
        for(int i = 0; i < dataColsCount; i++)
        {
            TabularReportColumn column = columns.getColumn(i);
            if(! states[i].isVisible())
                continue;

            String summary = column.getFormattedData(rc, states[i].getCalc());
            if(summary == null)
                summary = "&nbsp;";

            writer.write("<td class=\"report-summary\" style=\""+ states[i].getCssStyleAttrValue() +"\">" + summary + "</td>");
        }
        writer.write("</tr>");
    }

    protected int getRowDecoratorPrependColsCount(com.netspective.commons.report.tabular.TabularReportValueContext rc)
    {
        return 0;
    }

    protected int getRowDecoratorAppendColsCount(com.netspective.commons.report.tabular.TabularReportValueContext rc)
    {
        return 0;
    }
}