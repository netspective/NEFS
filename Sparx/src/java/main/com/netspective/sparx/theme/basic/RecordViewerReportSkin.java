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

import java.io.Writer;
import java.io.IOException;

/**
 * @author aye
 * $Id: RecordViewerReportSkin.java,v 1.2 2003-07-11 14:37:46 aye.thu Exp $
 */
public class RecordViewerReportSkin extends BasicHtmlTabularReportPanelSkin
{
    public RecordViewerReportSkin(Theme theme, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    /**
     * This method is overidden because a record viewer does not show a banner (only the record editor does)
     */
    //public ReportBanner getReportBanner(ReportContext rc)
    //{
    //    return null;
    //}

    /**
     * Produces additional html in the heading of the report. The skin will not display any actions associated with
     * the panel and will instead only display the record add report action item.
     * @param writer
     * @param vc
     * @param frame
     * @throws IOException
     */
    public void produceHeadingExtras(Writer writer, HtmlPanelValueContext vc, HtmlPanelFrame frame) throws IOException
    {

        HtmlPanelActions actions = frame.getActions();

        if(actions != null && actions.size() > 0)
        {
            Theme theme = ((HtmlTabularReportValueContext) vc).getActiveTheme();
            String imgPath = ((HtmlTabularReportValueContext) vc).getThemeImagesRootUrl(theme) + "/" + panelResourcesPrefix;

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
                HtmlPanelAction item = actions.get(i);
                String itemUrl = "TODO"; //item.getParameters();
                String itemCaption = item.getCaption().getTextValue(vc);
                ValueSource itemIcon = item.getIcon();
                if (itemIcon != null)
                {
                    // icon for this item is defined so use the passed in image INSTEAD of using the CSS based background image
                    itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-item\" width=\"18\"></td>");
                    colCount++;
                }
                else
                {
                    itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-item\" width=\"18\"><img src=\"" + imgPath + "/login/spacer.gif\" width=\"18\" height=\"19\"></td>");
                    colCount++;
                }
                itemBuffer.append("            <td class=\""+ panelClassNamePrefix +"-frame-action-box\">" +
                        "<a class=\""+ panelClassNamePrefix +"-frame-action\" href=\""+ itemUrl + "\">&nbsp;" + itemCaption + "&nbsp;</a></td>");
                colCount++;
            }

            if (itemBuffer.length() > 0)
                writer.write(itemBuffer.toString());
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
