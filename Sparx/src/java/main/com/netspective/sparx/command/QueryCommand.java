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
 * $Id: QueryCommand.java,v 1.9 2003-08-01 05:43:45 aye.thu Exp $
 */

package com.netspective.sparx.command;

import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.DialogDebugFlags;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.sql.QueryDialogContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.panel.HtmlTabularReportPanel;
import com.netspective.sparx.panel.QueryReportPanel;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.axiom.SqlManager;
import com.netspective.sparx.sql.Query;
import com.netspective.sparx.report.tabular.HtmlTabularReport;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

public class QueryCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(QueryCommand.class);
    public static final int UNLIMITED_ROWS = Integer.MAX_VALUE;
    public static final String[] IDENTIFIERS = new String[] { "query" };

    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Displays results of a SQL statement and optionally allows a dialog to be executed along with it.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("query-name", true, "The fully qualified name of the statement (package-name.statement-name)."),
                new CommandDocumentation.Parameter("query-dialog-id", false, "The name of a specific query dialog or '-' for no query dialog or 'DEFAULT' for default query dialog."),
                new CommandDocumentation.Parameter("report-id", false, "The name of a specific report element in the query declaration or '-' for the default report-id."),
                new CommandDocumentation.Parameter("rows-per-page", false, "-", "The number of rows per page to display ('-' means single page, any other number means a pageable report."),
                new SkinParameter(),
                new CommandDocumentation.Parameter("url-formats", false, "The url-formats parameter is one or more "+
                                                   "semicolon-separated URL formats that may override those within a report."),

                // the following are the same as the ones in DialogComponentCommand so be sure to make them look the same
                new CommandDocumentation.Parameter("additional-dialog-name", true, "The fully qualified name of the dialog (package-name.dialog-name)"),
                new CommandDocumentation.Parameter("additional-dialog-perspective", false, new DialogPerspectives(), null, "The dialog perspective to send to DialogContext."),
                new DialogCommand.SkinParameter(),
                new CommandDocumentation.Parameter("additional-dialog-debug-flags", false, new DialogDebugFlags(), null, "The debug flags.")
            }
    );

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    private String queryName;
    private String queryDialogName;
    private int rowsPerPage;
    private String reportSkinName;
    private String reportId;
    private String[] urlFormats;
    private DialogCommand additionalDialogCommand;

    public void setParameters(StringTokenizer params)
    {
        queryName = params.nextToken();

        if(params.hasMoreTokens())
        {
            queryDialogName = params.nextToken();
            if(queryDialogName.length() == 0 || queryDialogName.equals(PARAMVALUE_DEFAULT))
                queryDialogName = null;
        }
        else
            queryDialogName = null;

        if(params.hasMoreTokens())
        {
            reportId = params.nextToken();
            if(reportId.length() == 0 || reportId.equals(PARAMVALUE_DEFAULT))
                reportId = null;
        }
        else
            reportId = null;

        if(params.hasMoreTokens())
        {
            String rowsPerPageStr = params.nextToken();
            if(rowsPerPageStr.length() == 0 || rowsPerPageStr.equals(PARAMVALUE_DEFAULT))
                rowsPerPage = UNLIMITED_ROWS;
            else
                rowsPerPage = Integer.parseInt(rowsPerPageStr);
        }
        else
            rowsPerPage = UNLIMITED_ROWS;

        if(params.hasMoreTokens())
        {
            reportSkinName = params.nextToken();
            if(reportSkinName.length() == 0 || reportSkinName.equals(PARAMVALUE_DEFAULT))
                reportSkinName = null;
        }
        else
            reportSkinName = null;

        if(params.hasMoreTokens())
        {
            String urlFormatsStr = params.nextToken();
            if(urlFormatsStr.length() == 0 || urlFormatsStr.equals(PARAMVALUE_DEFAULT))
                setUrlFormats(null);
            else
            {
                StringTokenizer urlFmtTokenizer = new StringTokenizer(urlFormatsStr, ";");
                List urlFormatsList = new ArrayList();
                while(urlFmtTokenizer.hasMoreTokens())
                {
                    String urlFormat = urlFmtTokenizer.nextToken();
                    if(urlFormat.length() == 0 || urlFormat.equals(PARAMVALUE_DEFAULT))
                        urlFormatsList.add(null);
                    else
                        urlFormatsList.add(urlFormat);
                }
                setUrlFormats((String[]) urlFormatsList.toArray(new String[urlFormatsList.size()]));
            }
        }
        else
            setUrlFormats(null);

        if(params.hasMoreTokens())
        {
            additionalDialogCommand = new DialogCommand();
            additionalDialogCommand.setParameters(params);
        }
    }

    public String getQueryName()
    {
        return queryName;
    }

    public String getReportSkinName()
    {
        return reportSkinName;
    }

    public void setQueryName(String queryName)
    {
        this.queryName = queryName;
    }

    public void setReportSkinName(String reportSkinName)
    {
        this.reportSkinName = reportSkinName;
    }

    public String[] getUrlFormats()
    {
        return urlFormats;
    }

    public void setUrlFormats(String[] urlFormats)
    {
        this.urlFormats = urlFormats;
    }

    public String getParameters()
    {
        String delim = getParametersDelimiter();
        StringBuffer sb = new StringBuffer(queryName);
        sb.append(delim);
        sb.append(queryDialogName != null ? queryDialogName : PARAMVALUE_DEFAULT);
        sb.append(delim);
        sb.append(reportId != null ? reportId : PARAMVALUE_DEFAULT);
        sb.append(delim);
        sb.append(rowsPerPage != UNLIMITED_ROWS ? Integer.toString(rowsPerPage) : PARAMVALUE_DEFAULT);
        sb.append(delim);
        sb.append(reportSkinName != null ? reportSkinName : PARAMVALUE_DEFAULT);
        sb.append(delim);
        if(urlFormats != null)
        {
            for(int i = 0; i < urlFormats.length; i++)
            {
                if(i > 0) sb.append(";");
                sb.append(urlFormats[i]);
            }
        }
        else
            sb.append(PARAMVALUE_DEFAULT);
        if(additionalDialogCommand != null)
        {
            sb.append(delim);
            sb.append(additionalDialogCommand.getParameters());
        }
        return sb.toString();
    }

    /**
     * Gets the query dialog associated with the query
     * @param writer
     * @param sqlManager
     * @param theme
     * @return
     * @throws IOException
     */
    public com.netspective.sparx.form.sql.QueryDialog createQueryDialog(Writer writer, SqlManager sqlManager, Theme theme) throws IOException
    {
        Query query = (com.netspective.sparx.sql.Query)sqlManager.getQuery(queryName);
        if(query == null)
        {
            writer.write("Query " + queryName + " not found.");
            return null;
        }

        if(queryDialogName == null || queryDialogName.equals("default"))
            return ((com.netspective.sparx.sql.Query) query).getPresentation().getDefaultDialog();
        else
            return ((com.netspective.sparx.sql.Query) query).getPresentation().getDialog(queryDialogName);
    }

    /**
     * Gets the report panel associated with the query
     * @param writer
     * @param sqlManager
     * @param theme
     * @return
     * @throws IOException
     */
    public QueryReportPanel createQueryReportPanel(Writer writer, SqlManager sqlManager, Theme theme) throws IOException
    {
        Query query = (com.netspective.sparx.sql.Query)sqlManager.getQuery(queryName);
        if(query == null)
        {
            writer.write("Query " + queryName + " not found.");
            return null;
        }
        QueryReportPanel result = null;
        if (reportId != null)
        {
            result = query.getPresentation().getPanel(reportId);
        }

        if (result == null)
        {
            result = query.getPresentation().getDefaultPanel();
        }
        result.setReportSkin(reportSkinName);

        return result;
    }

    public void handleCommand(Writer writer, DialogContext dc, boolean unitTest) throws CommandException, IOException
    {
        SqlManager sqlManager = dc.getSqlManager();
        Theme theme = dc.getActiveTheme();

        if(additionalDialogCommand != null)
            writer.write("<table><tr valign='top'><td>");

        if(queryDialogName != null)
        {
            com.netspective.sparx.form.sql.QueryDialog queryDialog = createQueryDialog(writer, sqlManager, theme);
            if(queryDialog != null)
                queryDialog.render(writer, dc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);
        }
        else
        {
            HtmlTabularReportPanel panel = createQueryReportPanel(writer, sqlManager, theme);
            if(panel != null)
                panel.render(writer, dc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);
        }

        if(additionalDialogCommand != null)
        {
            writer.write("</td><td>");
            additionalDialogCommand.handleCommand(writer, dc, unitTest);
            writer.write("</td></tr></td></table>");
        }
    }

    /**
     * Handles the "query" command based on navigation context
     * @param writer
     * @param nc
     * @param unitTest
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        SqlManager sqlManager = nc.getSqlManager();
        Theme theme = nc.getActiveTheme();

        if(additionalDialogCommand != null)
            writer.write("<table><tr valign='top'><td>");

        boolean autoExecute = false;
        // NOTE: if query dialog name was not specified then the report will be auto-executed without
        // displaying a dialog.
        if (queryDialogName == null && (rowsPerPage > 0 && rowsPerPage < UNLIMITED_ROWS))
        {
            // if rows per page was specified without a dialog name, use the default dialog to handle the report
            queryDialogName = "default";
            autoExecute = true;
        }

        // before executing the query dialog, a query report panel MUST be assigned to it
        if (queryDialogName != null)
        {
            // a non-default  query dialog name was specified or the default one was specified (explicitly or implied)
            com.netspective.sparx.form.sql.QueryDialog queryDialog = createQueryDialog(writer, sqlManager, theme);
            if(queryDialog != null)
            {
                if (autoExecute)
                    ((HttpServletRequest)nc.getRequest()).setAttribute(Dialog.PARAMNAME_AUTOEXECUTE, "yes");

                QueryReportPanel panel = createQueryReportPanel(writer, sqlManager, theme);
                // create the context for which the dialog can run in
                QueryDialogContext qdc = (QueryDialogContext)queryDialog.createContext(nc, theme.getDefaultDialogSkin());
                if (rowsPerPage < UNLIMITED_ROWS && rowsPerPage > 0)
                    qdc.setRowsPerPage(rowsPerPage);
                qdc.setReportPanel(panel);
                queryDialog.render(writer, qdc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);
            }
        }
        else
        {
            QueryReportPanel panel = createQueryReportPanel(writer, sqlManager, theme);
            TabularReport.Flags flags = panel.getReport(nc).getFlags();
            boolean isSelectable = flags != null? flags.flagIsSet(HtmlTabularReport.Flags.SELECTABLE) : false;
            if (isSelectable)
            {
                ((HttpServletRequest)nc.getRequest()).setAttribute(Dialog.PARAMNAME_AUTOEXECUTE, "yes");
                com.netspective.sparx.form.sql.QueryDialog queryDialog = createQueryDialog(writer, sqlManager, theme);
                QueryDialogContext qdc = (QueryDialogContext)queryDialog.createContext(nc, theme.getDefaultDialogSkin());
                qdc.setReportPanel(panel);
                queryDialog.render(writer, qdc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);
            }
            else
            {
                // simple non-pageable, non-selectable report
                panel.render(writer, nc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);
            }
        }

        if(additionalDialogCommand != null)
        {
            writer.write("</td><td>");
            additionalDialogCommand.handleCommand(writer, nc, unitTest);
            writer.write("</td></tr></td></table>");
        }
    }

    public static class SkinParameter extends CommandDocumentation.Parameter
    {
        public SkinParameter()
        {
            super("report-skin-name", false, "The name of a ReportSkin implementation registered in the SkinFactory.");
        }

        public String[] getEnums()
        {
            //TODO: return (String[]) SkinFactory.getInstance().getDialogSkins().keySet().toArray(new String[SkinFactory.getInstance().getNavigationSkins().keySet().size()]);
            return null;
        }
    }
}
