package com.netspective.sparx.theme.basic;

import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.panel.HtmlPanelActions;
import com.netspective.sparx.command.RedirectCommand;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.command.Command;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;

/**
 * @author aye
 * $Id: SelectableHtmlTabularReportPanelSkin.java,v 1.2 2003-08-22 03:33:44 shahid.shah Exp $
 */
public class SelectableHtmlTabularReportPanelSkin  extends BasicHtmlTabularReportPanelSkin
{
    private boolean highlightRow = false;

    public SelectableHtmlTabularReportPanelSkin()
    {
        super();
    }

    public SelectableHtmlTabularReportPanelSkin(Theme theme, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    public void produceHeadingExtras(Writer writer, HtmlPanelValueContext vc, HtmlPanelFrame frame) throws IOException
    {
        super.produceHeadingExtras(writer, vc, frame);

        HtmlTabularReportValueContext rc = ((HtmlTabularReportValueContext)vc);
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        HtmlPanelActions frameActions = frame.getActions();
        if (actions != null)
        {
            HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_SELECT));
            if (reportAction != null)
            {
                Theme theme = rc.getActiveTheme();
                Command command = reportAction.getCommand(vc);
                if (frameActions.size() > 0)
                    writer.write("            <td bgcolor=\"white\"><img src=\"" + theme.getImageResourceUrl(panelResourcesPrefix + "/spacer.gif") + "\" width=\"5\" height=\"5\"></td>");
                writer.write("            <td class=\""+ panelClassNamePrefix +"-frame-action-item\" width=\"18\"><img src=\"" + theme.getImageResourceUrl(panelResourcesPrefix + "/spacer.gif") + "\" width=\"18\" height=\"19\"></td>");
                if (command instanceof RedirectCommand)
                {
                     writer.write("            <td class=\""+ panelClassNamePrefix +"-frame-action-box\">" +
                        "<a class=\""+ panelClassNamePrefix +"-frame-action\" href=\""+ ((RedirectCommand) command).getLocation().getTextValue(rc)  +
                        "\">&nbsp;" + reportAction.getCaption().getTextValue(vc) + "&nbsp;</a></td>");
                }
            }
        }
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
                panelAction.setCommand(reportAction.getCommand());
                frame.addAction(panelAction);
            }
        }*/
        super.render(writer, rc, ds);
    }

    protected int getRowDecoratorPrependColsCount(HtmlTabularReportValueContext rc)
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
        {
            // no actions are defined in the report so return 0
            return 0;
        }
        HtmlReportAction reportAction = actions.get(HtmlReportAction.Type.getValue(HtmlReportAction.Type.RECORD_SELECT));
        if (reportAction != null)
            return 1;
        else
            return 0;
    }

    /**
     * Produces html to prepend to the data row
     * @param writer
     * @param rc
     * @param isOddRow
     * @throws IOException
     */
    public void produceDataRowDecoratorPrepend(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds, boolean isOddRow) throws IOException
    {
        BasicHtmlTabularReport report = (BasicHtmlTabularReport)rc.getReport();
        HtmlReportActions actions = report.getActions();
        if (actions == null)
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
        if (selectedValues != null)
        {
            for (int i = 0; i < selectedValues.length; i++)
            {
                //System.out.println(selectedValues[i] + " " + rowData[0]);
                if (selectedValues[i].equalsIgnoreCase(rowData.toString()))
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
