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
 * $Id: BasicReportSkin.java,v 1.1 2003-03-24 13:28:02 shahid.shah Exp $
 */

package com.netspective.sparx.theme.basic;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.netspective.sparx.report.ReportSkin;
import com.netspective.sparx.report.ReportFrame;
import com.netspective.sparx.report.ReportContext;
import com.netspective.sparx.report.ReportBanner;
import com.netspective.sparx.report.StandardReport;
import com.netspective.sparx.report.ReportColumnsList;
import com.netspective.sparx.report.ReportColumn;
import com.netspective.sparx.report.Report;
import com.netspective.sparx.theme.Theme;
import com.netspective.commons.value.ValueSource;

public class BasicReportSkin extends AbstractThemeSkin implements ReportSkin
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

    public void produceHeadingExtras(Writer writer, ReportContext rc, ReportFrame frame) throws IOException
    {
        ArrayList items = frame.getItems();

        if(items != null && items.size() > 0)
        {
            Theme theme = rc.getActiveTheme();
            String imgPath = rc.getThemeImagesRootUrl(theme) + "/" + panelStyle;

            int colCount = 0;

            // create a temporary string buffer for the HTML of the heading action items
            StringBuffer itemBuffer = new StringBuffer();
            for (int i=0; items != null && i < items.size(); i++)
            {
                if (i != 0)
                {
                    itemBuffer.append("            <td bgcolor=\"white\"><img src=\"" + imgPath + "/login/spacer.gif\" width=\"5\" height=\"5\"></td>");
                    colCount++;
                }
                ReportFrame.Item item = (ReportFrame.Item) items.get(i);
                ValueSource itemUrl = item.getUrl();
                ValueSource itemCaption = item.getCaption();
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
                        "<a class=\"panel-frame-action-output\" href=\""+ itemUrl.getValue(rc) + "\">&nbsp;" +
                        itemCaption.getValue(rc) + "&nbsp;</a></td>");
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

    public ReportBanner getReportBanner(ReportContext rc)
    {
        return rc.getReport().getBanner();
    }

    public ReportFrame getReportFrame(ReportContext rc)
    {
        return rc.getReport().getFrame();
    }

    /**
     * Produce the report
     * @param writer
     * @param rc
     * @param rs
     * @param data
     * @throws SQLException
     * @throws IOException
     */
    public void produceReport(Writer writer, ReportContext rc, ResultSet rs, Object[][] data) throws SQLException, IOException
    {
        ReportFrame frame = getReportFrame(rc);
        ReportBanner banner = getReportBanner(rc);

        Theme theme = rc.getActiveTheme();
        String imgPath = rc.getThemeImagesRootUrl(theme) + "/" + panelStyle;

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
                if (frame.allowCollapse())
                {
                    if (frame.isMinimized())
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

            if(banner != null)
            {
                produceBannerRow(writer, rc);
            }
        }

        writer.write("<tr>\n" +
                "    <td class=\"panel-content-output\">\n");
        writer.write("    <table class=\"report\" width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">\n");
        int startDataRow = 0;
        if(flagIsSet(HTMLFLAG_SHOW_HEAD_ROW) && !rc.getReport().flagIsSet(StandardReport.REPORTFLAG_HIDE_HEADING))
        {
            if(!rc.getReport().flagIsSet(StandardReport.REPORTFLAG_FIRST_DATA_ROW_HAS_HEADINGS))
            {
                produceHeadingRow(writer, rc, (Object[]) null);
            }
            else
            {
                if(rs != null)
                    produceHeadingRow(writer, rc, rs);
                else if(data.length > 0)
                {
                    produceHeadingRow(writer, rc, data[0]);
                    startDataRow = 1;
                }
            }
        }
        if(rs != null)
        {
            produceDataRows(writer, rc, rs);
        }
        else
        {
            produceDataRows(writer, rc, data, startDataRow);
        }

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
                writer.write("    <td class=\"panel-banner-footer-output\">" + fvs.getValue(rc) + "</td>\n");
                writer.write("</tr>\n");
            }
        }

        writer.write("</table>\n");
    }

    /**
     * Displays the report banner html. Utilizes the THEME
     * @param writer
     * @param rc
     * @throws IOException
     */
    private void produceBannerRow(Writer writer, ReportContext rc) throws IOException
    {
        ReportBanner banner = getReportBanner(rc);
        if (banner == null)
            return;

        writer.write("<tr><td class=\"panel-banner-output\">\n");
        ReportBanner.Items items = banner.getItems();
        int style = items.getStyle();
        if (style == ReportBanner.Items.LAYOUTSTYLE_HORIZONTAL)
        {
            for (int i=0; items != null && i < items.size(); i++)
            {
                ReportBanner.Item item = (ReportBanner.Item) items.get(i);
                ValueSource itemUrl = item.getUrl();
                ValueSource itemCaption = item.getCaption();
                ValueSource itemIcon = item.getIcon();
                String caption = itemCaption != null ? (itemUrl != null ? ("<a href='" + itemUrl.getValue(rc) + "'>" +
                        itemCaption.getValue(rc) + "</a>") : itemCaption.getValue(rc).getTextValue()) : null;
                if(i > 0)
                    writer.write(", ");
                if(itemIcon != null)
                    writer.write("<img src='" + itemIcon.getValue(rc) + "'>");
                writer.write(caption);
            }
        }
        else
        {
            writer.write("<table border=0 cellspacing=0>");
            for(int i = 0; items != null && i < items.size(); i++)
            {
                ReportBanner.Item item = (ReportBanner.Item) items.get(i);
                ValueSource itemUrl = item.getUrl();
                ValueSource itemCaption = item.getCaption();
                ValueSource itemIcon = item.getIcon();
                ReportBanner.Items childItems = item.getChildItems();
                String caption = itemCaption != null ? (itemUrl != null ? ("<a href='" + itemUrl.getValue(rc) + "'>" + itemCaption.getValue(rc) + "</a>") : itemCaption.getValue(rc).getTextValue()) : null;

                writer.write("<tr><td>");
                writer.write(itemIcon != null ? "<img src='" + itemIcon.getValue(rc) + "'>" : "-");
                writer.write("</td>");
                writer.write("<td>");
                if(caption != null)
                    writer.write(caption);
                if(childItems != null)
                    childItems.produceHtml(writer, rc);
                writer.write("</td>");
                writer.write("</tr>");
            }
            writer.write("</table>");
        }
        writer.write("</td></tr>\n");
    }

    public void produceReport(Writer writer, ReportContext rc, ResultSet rs) throws SQLException, IOException
    {
        produceReport(writer, rc, rs, null);
    }

    public void produceReport(Writer writer, ReportContext rc, Object[][] data) throws IOException
    {
        try
        {
            produceReport(writer, rc, null, data);
        }
        catch(SQLException e)
        {
            throw new RuntimeException("This should never happen.");
        }
    }

    private int getTableColumnsCount(ReportContext rc)
    {
        return (rc.getVisibleColsCount() * 2) +
               (getRowDecoratorPrependColsCount(rc) * 2) +
               (getRowDecoratorAppendColsCount(rc) * 2) +
               + 1; // each column has "spacer" in between, first column as spacer before too
    }

    public void produceHeadingRowDecoratorPrepend(Writer writer, ReportContext rc) throws IOException
    {
    }

    public void produceHeadingRowDecoratorAppend(Writer writer, ReportContext rc) throws IOException
    {
    }

    public void produceDataRowDecoratorPrepend(Writer writer, ReportContext rc, int rowNum, Object[] rowData, boolean isOddRow) throws IOException
    {
    }

    public void produceDataRowDecoratorAppend(Writer writer, ReportContext rc, int rowNum, Object[] rowData, boolean isOddRow) throws IOException
    {
    }

    public void produceHeadingRow(Writer writer, ReportContext rc, Object[] headings) throws IOException
    {
        ReportColumnsList columns = rc.getColumns();
        ReportContext.ColumnState[] states = rc.getStates();
        int dataColsCount = columns.size();

        Theme theme = rc.getActiveTheme();
        String imgPath = rc.getThemeImagesRootUrl(theme) + "/" + panelStyle;

        String sortAscImgTag = " <img src=\""+ imgPath + "/column-sort-ascending.gif\" border=0>";
        String sortDescImgTag = " <img src=\""+ imgPath + "/column-sort-descending.gif\" border=0>";

        writer.write("<tr>");
        produceHeadingRowDecoratorPrepend(writer, rc);

        if(headings == null)
        {
            for(int i = 0; i < dataColsCount; i++)
            {
                ReportColumn rcd = columns.getColumn(i);
                ReportContext.ColumnState rcs = rc.getState(i);
                if(states[i].isHidden())
                    continue;

                String colHeading = rcd.getHeading().getValue(rc).getTextValue();
                ValueSource headingAnchorAttrs = rcd.getHeadingAnchorAttrs();
                if(headingAnchorAttrs != null)
                    colHeading = "<a " + headingAnchorAttrs.getValue(rc) + ">" + colHeading + "</a>";
                if(rcs.flagIsSet(ReportColumn.COLFLAG_SORTED_ASCENDING))
                    colHeading += sortAscImgTag;
                if(rcs.flagIsSet(ReportColumn.COLFLAG_SORTED_DESCENDING))
                    colHeading += sortDescImgTag;
                writer.write("        <td class=\"report-field\" nowrap>" + colHeading  + "</td>");
            }
        }
        else
        {
            for(int i = 0; i < dataColsCount; i++)
            {
                ReportColumn rcd = columns.getColumn(i);
                ReportContext.ColumnState rcs = rc.getState(i);
                if(states[i].isHidden())
                    continue;

                Object heading = headings[rcd.getColIndexInArray()];
                if(heading != null)
                {
                    String colHeading = heading.toString();
                    ValueSource headingAnchorAttrs = rcd.getHeadingAnchorAttrs();
                    if(headingAnchorAttrs != null)
                        colHeading = "<a " + headingAnchorAttrs.getValue(rc) + ">" + colHeading + "</a>";
                    if(rcs.flagIsSet(ReportColumn.COLFLAG_SORTED_ASCENDING))
                        colHeading += sortAscImgTag;
                    if(rcs.flagIsSet(ReportColumn.COLFLAG_SORTED_DESCENDING))
                        colHeading += sortDescImgTag;

                    writer.write("        <td class=\"report-field\" nowrap>" + colHeading  + "</td>");
                }
                else
                    writer.write("        <td class=\"report-field\" nowrap>&nbsp;&nbsp;</td>");
            }
        }

        produceHeadingRowDecoratorAppend(writer, rc);

        writer.write("</tr>");
        /*
        if(flagIsSet(HTMLFLAG_ADD_ROW_SEPARATORS))
            writer.write("</tr><tr><td colspan='" + tableColsCount + "'><img src='" + rowSepImgSrc + "' height='2' width='100%'></td></tr>");
        */
    }

    public void produceHeadingRow(Writer writer, ReportContext rc, ResultSet rs) throws IOException, SQLException
    {
        ReportColumnsList columns = rc.getColumns();
        ReportContext.ColumnState[] states = rc.getStates();
        int dataColsCount = columns.size();

        if(!rs.next()) return;

        Theme theme = rc.getActiveTheme();
        String imgPath = rc.getThemeImagesRootUrl(theme) + "/" + panelStyle;

        String sortAscImgTag = " <img src=\""+ imgPath + "/column-sort-ascending.gif\" border=0>";
        String sortDescImgTag = " <img src=\""+ imgPath + "/column-sort-descending.gif\" border=0>";

        writer.write("<tr>");
        produceHeadingRowDecoratorPrepend(writer, rc);

        for(int i = 0; i < dataColsCount; i++)
        {
            ReportColumn rcd = columns.getColumn(i);
            ReportContext.ColumnState rcs = rc.getState(i);
            if(states[i].isHidden())
                continue;

            Object heading = rs.getString(rcd.getColIndexInResultSet());
            if(heading != null)
            {
                String colHeading = heading.toString();
                ValueSource headingAnchorAttrs = rcd.getHeadingAnchorAttrs();
                if(headingAnchorAttrs != null)
                    colHeading = "<a " + headingAnchorAttrs.getValue(rc) + ">" + colHeading + "</a>";
                if(rcs.flagIsSet(ReportColumn.COLFLAG_SORTED_ASCENDING))
                    colHeading += sortAscImgTag;
                if(rcs.flagIsSet(ReportColumn.COLFLAG_SORTED_DESCENDING))
                    colHeading += sortDescImgTag;

                writer.write("        <td class=\"report-field\" nowrap>" + colHeading  + "</td>");
            }
            else
                writer.write("        <td class=\"report-field\" nowrap>&nbsp;&nbsp;</td>");
        }

        produceHeadingRowDecoratorAppend(writer, rc);

        /*
        if(flagIsSet(HTMLFLAG_ADD_ROW_SEPARATORS))
            writer.write("</tr><tr><td colspan='" + tableColsCount + "'><img src='" + rowSepImgSrc + "' height='2' width='100%'></td></tr>");
        else
            writer.write("</tr>");
            */
    }

    /*
      This method and the next one (produceDataRows with Object[][] data) are almost
      identical except for their data sources (ResultSet vs. Object[][]). Be sure to
      modify that method when this method changes, too
    */

    public void produceDataRows(Writer writer, ReportContext rc, ResultSet rs) throws SQLException, IOException
    {
        Report defn = rc.getReport();
        ReportColumnsList columns = rc.getColumns();
        ReportContext.ColumnState[] states = rc.getStates();

        int rowsWritten = 0;
        int dataColsCount = columns.size();
        int tableColsCount = getTableColumnsCount(rc);

        //TODO: Sparx 2.x conversion required
        //ResultSetScrollState scrollState = rc.getScrollState();
        //boolean paging = scrollState != null;

        ResultSetMetaData rsmd = rs.getMetaData();
        int resultSetColsCount = rsmd.getColumnCount();
        boolean isOddRow = false;

        while(rs.next())
        {
            // the reason why we need to copy the objects here is that
            // most JDBC drivers will only let data be ready one time; calling
            // the resultSet.getXXX methods more than once is problematic
            //
            Object[] rowData = new Object[resultSetColsCount];
            for(int i = 1; i <= resultSetColsCount; i++)
                rowData[i - 1] = rs.getObject(i);

            isOddRow = ! isOddRow;
            int rowNum = rs.getRow();

            writer.write("<tr>");
            produceDataRowDecoratorPrepend(writer, rc, rowNum, rowData, isOddRow);

            for(int i = 0; i < dataColsCount; i++)
            {

                ReportColumn column = columns.getColumn(i);
                ReportContext.ColumnState state = states[i];

                if(state.isHidden())
                    continue;

                String data =
                        state.flagIsSet(ReportColumn.COLFLAG_HASOUTPUTPATTERN) ?
                        state.getOutputFormat() :
                        column.getFormattedData(rc, rowNum, rowData, true);

                String singleRow = "<td " + (isOddRow ? "class=\"report\"" : "class=\"report-alternative\"") + ">" +
                        (state.flagIsSet(ReportColumn.COLFLAG_WRAPURL) ? "<a href=\"" + state.getUrl() + "\" " + state.getUrlAnchorAttrs() + ">" +
                        data + "</a>" : data) +
                        "&nbsp;</td>";
                writer.write(defn.replaceOutputPatterns(rc, rowNum, rowData, singleRow));
            }

            produceDataRowDecoratorAppend(writer, rc, rowNum, rowData, isOddRow);

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

    /*
      This method and the previous one (produceDataRows with ResultSet) are almost
      identical except for their data sources (Object[][] vs. ResultSet). Be sure to
      modify that method when this method changes, too.
    */

    public void produceDataRows(Writer writer, ReportContext rc, Object[][] data, int startDataRow) throws IOException
    {
        Report defn = rc.getReport();
        ReportColumnsList columns = rc.getColumns();
        ReportContext.ColumnState[] states = rc.getStates();

        int rowsWritten = 0;
        int dataColsCount = columns.size();
        int tableColsCount = getTableColumnsCount(rc);

        //TODO: Sparx 2.x conversion required
        //ResultSetScrollState scrollState = rc.getScrollState();
        //boolean paging = scrollState != null;
        boolean isOddRow = false;

        for(int row = startDataRow; row < data.length; row++)
        {
            Object[] rowData = data[row];
            isOddRow = ! isOddRow;
            int rowNum = row - startDataRow;

            writer.write("<tr>");

            produceDataRowDecoratorPrepend(writer, rc, rowNum, rowData, isOddRow);

            for(int i = 0; i < dataColsCount; i++)
            {
                ReportColumn column = columns.getColumn(i);
                ReportContext.ColumnState state = states[i];

                if(state.isHidden())
                    continue;

                String colData =
                        state.flagIsSet(ReportColumn.COLFLAG_HASOUTPUTPATTERN) ?
                        state.getOutputFormat() :
                        column.getFormattedData(rc, rowNum, rowData, true);

                String singleRow = "<td " + (isOddRow ? "class=\"report\"" : "class=\"report-alternative\"") + ">" +
                        (state.flagIsSet(ReportColumn.COLFLAG_WRAPURL) ? "<a href=\"" + state.getUrl() + "\" " + state.getUrlAnchorAttrs() + ">" + colData + "</a>" : colData) +
                        "</td>";
                /*
                String singleRow = "<td align='" + ALIGN_ATTRS[column.getAlignStyle()] + "'>"+ dataTagsBegin +"<font " + dataFontAttrs + ">" +
                        (state.flagIsSet(ReportColumn.COLFLAG_WRAPURL) ? "<a href='" + state.getUrl() + "'" + state.getUrlAnchorAttrs() + ">" + colData + "</a>" : colData) +
                        "</font>"+ dataTagsEnd +"</td><td><font " + dataFontAttrs + ">&nbsp;&nbsp;</td>";
                */
                writer.write(defn.replaceOutputPatterns(rc, rowNum, rowData, singleRow));
            }

            produceDataRowDecoratorAppend(writer, rc, rowNum, rowData, isOddRow);

            writer.write("</tr>");
            rowsWritten++;
            //if(paging && rc.endOfPage())
            //    break;
        }

        if(rowsWritten == 0)
        {
            //writer.write("</tr><tr><td colspan='" + tableColsCount + "'><font " + dataFontAttrs + ">No data found.</font></td></tr>");
            writer.write("<tr><td class=\"report-summary\" colspan='" + tableColsCount + "'>No data found.</td></tr>");
            //if(paging)
            //    scrollState.setNoMoreRows();
        }
        //else if(paging)
        //{
        //    scrollState.accumulateRowsProcessed(rowsWritten);
        //    if(rowsWritten < scrollState.getRowsPerPage())
        //        scrollState.setNoMoreRows();
        //}
    }

    public void produceFootRow(Writer writer, ReportContext rc) throws SQLException, IOException
    {
        int calcsCount = rc.getCalcsCount();
        if(calcsCount == 0)
            return;

        ReportContext.ColumnState[] states = rc.getStates();
        ReportColumnsList columns = rc.getColumns();
        int dataColsCount = columns.size();

        writer.write("<tr>");
        for(int i = 0; i < dataColsCount; i++)
        {
            ReportColumn column = columns.getColumn(i);
            if(states[i].isHidden())
                continue;

            writer.write("<td class=\"report-summary\"" + column.getFormattedData(rc, states[i].getCalc()) + "</td>");
        }
        writer.write("</tr>");
    }

    /*
    public String getFrameHdTableAttrs()
    {
        return frameHdTableAttrs;
    }

    public void setFrameHdTableAttrs(String frameHdTableAttrs)
    {
        this.frameHdTableAttrs = frameHdTableAttrs;
    }

    public String getFrameHdRowSpacerAttrs()
    {
        return frameHdRowSpacerAttrs;
    }

    public void setFrameHdRowSpacerAttrs(String frameHdRowSpacerAttrs)
    {
        this.frameHdRowSpacerAttrs = frameHdRowSpacerAttrs;
    }

    public String getFrameHdCellAttrs()
    {
        return frameHdCellAttrs;
    }

    public void setFrameHdCellAttrs(String frameHdCellAttrs)
    {
        this.frameHdCellAttrs = frameHdCellAttrs;
    }

    public String getFrameHdInfoCellAttrs()
    {
        return frameHdInfoCellAttrs;
    }

    public void setFrameHdInfoCellAttrs(String frameHdInfoCellAttrs)
    {
        this.frameHdInfoCellAttrs = frameHdInfoCellAttrs;
    }

    public ValueSource getFrameHdTabImgSrcValueSource()
    {
        return frameHdTabImgSrcValueSource;
    }

    public void setFrameHdTabImgSrcValueSource(ValueSource frameHdTabImgSrcValueSource)
    {
        this.frameHdTabImgSrcValueSource = frameHdTabImgSrcValueSource;
    }

    public ValueSource getFrameHdSpacerImgSrcValueSource()
    {
        return frameHdSpacerImgSrcValueSource;
    }

    public void setFrameHdSpacerImgSrcValueSource(ValueSource frameHdSpacerImgSrcValueSource)
    {
        this.frameHdSpacerImgSrcValueSource = frameHdSpacerImgSrcValueSource;
    }

    public String getDataHdRowAttrs()
    {
        return dataHdRowAttrs;
    }

    public void setDataHdRowAttrs(String dataHdRowAttrs)
    {
        this.dataHdRowAttrs = dataHdRowAttrs;
    }

    public String getDataHdCellAttrs()
    {
        return dataHdCellAttrs;
    }

    public void setDataHdCellAttrs(String dataHdCellAttrs)
    {
        this.dataHdCellAttrs = dataHdCellAttrs;
    }

    public String getDataEvenRowAttrs()
    {
        return dataEvenRowAttrs;
    }

    public void setDataEvenRowAttrs(String dataEvenRowAttrs)
    {
        this.dataEvenRowAttrs = dataEvenRowAttrs;
    }

    public String getDataOddRowAttrs()
    {
        return dataOddRowAttrs;
    }

    public void setDataOddRowAttrs(String dataOddRowAttrs)
    {
        this.dataOddRowAttrs = dataOddRowAttrs;
    }

    public String getDataFtRowAttrs()
    {
        return dataFtRowAttrs;
    }

    public void setDataFtRowAttrs(String dataFtRowAttrs)
    {
        this.dataFtRowAttrs = dataFtRowAttrs;
    }

    public void setFlags(int flags)
    {
        this.flags = flags;
    }

    public String getOuterTableAttrs()
    {
        return outerTableAttrs;
    }

    public void setOuterTableAttrs(String outerTableAttrs)
    {
        this.outerTableAttrs = outerTableAttrs;
    }

    public String getInnerTableAttrs()
    {
        return innerTableAttrs;
    }

    public void setInnerTableAttrs(String innerTableAttrs)
    {
        this.innerTableAttrs = innerTableAttrs;
    }

    public String getFrameHdRowAttrs()
    {
        return frameHdRowAttrs;
    }

    public void setFrameHdRowAttrs(String frameHdRowAttrs)
    {
        this.frameHdRowAttrs = frameHdRowAttrs;
    }

    public String getFrameHdFontAttrs()
    {
        return frameHdFontAttrs;
    }

    public void setFrameHdFontAttrs(String frameHdFontAttrs)
    {
        this.frameHdFontAttrs = frameHdFontAttrs;
    }

    public String getFrameFtRowAttrs()
    {
        return frameFtRowAttrs;
    }

    public void setFrameFtRowAttrs(String frameFtRowAttrs)
    {
        this.frameFtRowAttrs = frameFtRowAttrs;
    }

    public String getFrameFtFontAttrs()
    {
        return frameFtFontAttrs;
    }

    public void setFrameFtFontAttrs(String frameFtFontAttrs)
    {
        this.frameFtFontAttrs = frameFtFontAttrs;
    }

    public String getBannerRowAttrs()
    {
        return bannerRowAttrs;
    }

    public void setBannerRowAttrs(String bannerRowAttrs)
    {
        this.bannerRowAttrs = bannerRowAttrs;
    }

    public String getBannerItemFontAttrs()
    {
        return bannerItemFontAttrs;
    }

    public void setBannerItemFontAttrs(String bannerItemFontAttrs)
    {
        this.bannerItemFontAttrs = bannerItemFontAttrs;
    }

    public String getDataHdFontAttrs()
    {
        return dataHdFontAttrs;
    }

    public void setDataHdFontAttrs(String dataHdFontAttrs)
    {
        this.dataHdFontAttrs = dataHdFontAttrs;
    }

    public String getDataFontAttrs()
    {
        return dataFontAttrs;
    }

    public void setDataFontAttrs(String dataFontAttrs)
    {
        this.dataFontAttrs = dataFontAttrs;
    }

    public String getDataFtFontAttrs()
    {
        return dataFtFontAttrs;
    }

    public void setDataFtFontAttrs(String dataFtFontAttrs)
    {
        this.dataFtFontAttrs = dataFtFontAttrs;
    }

    public String getRowSepImgSrc()
    {
        return rowSepImgSrc;
    }

    public void setRowSepImgSrc(String rowSepImgSrc)
    {
        this.rowSepImgSrc = rowSepImgSrc;
    }

    public String getSortAscImgSrc()
    {
        return sortAscImgSrc;
    }

    public void setSortAscImgSrc(String sortAscImgSrc)
    {
        this.sortAscImgSrc = sortAscImgSrc;
    }

    public String getSortDescImgSrc()
    {
        return sortDescImgSrc;
    }

    public void setSortDescImgSrc(String sortDescImgSrc)
    {
        this.sortDescImgSrc = sortDescImgSrc;
    }
    */

    protected int getRowDecoratorPrependColsCount(ReportContext rc)
    {
        return 0;
    }

    protected int getRowDecoratorAppendColsCount(ReportContext rc)
    {
        return 0;
    }
}