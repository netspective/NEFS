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
 */


package com.netspective.sparx.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.sql.QueryDialogContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.panel.StoredProcedureReportPanel;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.HtmlTabularReportPanel;
import com.netspective.sparx.sql.StoredProcedure;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.axiom.SqlManager;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.io.Writer;
import java.io.IOException;

/**
 * Command for executing a stored procedure and producing a report returned from the stored procedure as a result set
 * @author Aye Thu
 * @version $Id: StoredProcedureCommand.java,v 1.3 2003-11-16 15:18:04 shahid.shah Exp $
 */
public class StoredProcedureCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(QueryCommand.class);
    public static final int UNLIMITED_ROWS = Integer.MAX_VALUE;
    public static final String[] IDENTIFIERS = new String[] { "stored-procedure", "stored-proc" };

    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Displays the results of a result set returned from the execution of a stored procedure.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("stored-procedure-name", true, "The fully qualified name of the stored procedure (package-name.stored-procedure-name)."),

                new CommandDocumentation.Parameter("report-id", false, "The name of a specific report panel element in the stored procedure call declaration or '-' for the default report panel."),
                new CommandDocumentation.Parameter("rows-per-page", false, "-", "The number of rows per page to display ('-' means single page, any other number means a pageable report."),
                new SkinParameter(),
                new CommandDocumentation.Parameter("url-formats", false, "The url-formats parameter is one or more "+
                                                   "semicolon-separated URL formats that may override those within a report."),
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

    private String spName;
    private String spDialogName;
    private int rowsPerPage;
    private String reportSkinName;
    private String reportId;
    private String[] urlFormats;

    /**
     *
     * @param params
     */
    public void setParameters(StringTokenizer params)
    {
        spName = params.nextToken();

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
            {
                try
                {
                    rowsPerPage = Integer.parseInt(rowsPerPageStr);
                }
                catch (NumberFormatException e)
                {
                    log.error("Invalid rows per page value for stored procedure command", e);
                }
            }
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
                urlFormats = null;
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
                urlFormats = (String[]) urlFormatsList.toArray(new String[urlFormatsList.size()]);
            }
        }
        else
            urlFormats = null;

    }

    /**
     * Constructs a string containing the command paramaters of the stored procedure
     * @return
     */
    public String getParameters()
    {
        String delim = getParametersDelimiter();
        StringBuffer sb = new StringBuffer(spName);
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

        return sb.toString();
    }

    /**
     * Gets the query dialog associated with the stored procedure call
     * @param writer
     * @param sqlManager
     * @param theme
     * @return
     * @throws IOException
     */
    public com.netspective.sparx.form.sql.QueryDialog createQueryDialog(Writer writer, SqlManager sqlManager, Theme theme) throws IOException
    {
        StoredProcedure query = (com.netspective.sparx.sql.StoredProcedure)sqlManager.getStoredProcedure(spName);
        if(query == null)
        {
            writer.write("Stored procedure '" + spName + "' not found.");
            return null;
        }

        // TODO: Implement the stored procedure dialog
        /*
        if (spDialogName == null || spDialogName.equals("default"))
            return query.getPresentation().getDefaultDialog();
        else
            return query.getPresentation().getDialog(queryDialogName);
            */
        return null;
    }


    /**
     * Gets the report panel associated with the stored procedure call
     * @param writer
     * @param sqlManager
     * @param theme
     * @return
     * @throws java.io.IOException
     */
    public StoredProcedureReportPanel createReportPanel(Writer writer, SqlManager sqlManager, Theme theme) throws IOException
    {
        StoredProcedure storedProc = (com.netspective.sparx.sql.StoredProcedure) sqlManager.getStoredProcedure(spName);
        if(storedProc == null)
        {
            log.error("Stored procedure " + spName + " not found in "+ this +".");
            throw new RuntimeException("Stored procedure " + spName + " not found in "+ this +".");
        }
        StoredProcedureReportPanel result = null;
        if (reportId != null)
            result = storedProc.getPresentation().getPanel(reportId);

        if (result == null)
            result = storedProc.getPresentation().getDefaultPanel();
        result.setReportSkin(reportSkinName);
        return result;
    }

    /**
     * Handles the stored procedure call command executed from a dialog context
     * @param writer
     * @param dc
     * @param unitTest
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, DialogContext dc, boolean unitTest) throws CommandException, IOException
    {
        SqlManager sqlManager = dc.getSqlManager();
        Theme theme = dc.getActiveTheme();

        HtmlTabularReportPanel panel = createReportPanel(writer, sqlManager, theme);
        if(panel != null)
            panel.render(writer, dc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);

    }

    /**
     * Handles the stored procedure call command executed from a navigation context such as from a navigation page
     * @param writer            Writer object associated with the response buffer
     * @param nc                the navigation context in which the command was executed
     * @param unitTest          flag indicating if this execution is for a unit test
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        SqlManager sqlManager = nc.getSqlManager();
        Theme theme = nc.getActiveTheme();

        boolean autoExecute = false;
        // NOTE: if query dialog name was not specified then the report will be auto-executed without
        // displaying a dialog.
        if (spDialogName == null && (rowsPerPage > 0 && rowsPerPage < UNLIMITED_ROWS))
        {
            // if rows per page was specified without a dialog name, use the default dialog to handle the report
            spDialogName = "default";
            autoExecute = true;
        }
        // before executing the query dialog, a query report panel MUST be assigned to it
        if (spDialogName != null)
        {
            // a non-default  query dialog name was specified or the default one was specified (explicitly or implied)
            com.netspective.sparx.form.sql.QueryDialog queryDialog = createQueryDialog(writer, sqlManager, theme);
            if(queryDialog != null)
            {
                if (autoExecute)
                    nc.getRequest().setAttribute(Dialog.PARAMNAME_AUTOEXECUTE, "yes");

                StoredProcedureReportPanel panel = createReportPanel(writer, sqlManager, theme);
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
            StoredProcedureReportPanel panel = createReportPanel(writer, sqlManager, theme);
            TabularReport.Flags flags = panel.getReport(nc).getFlags();
            boolean isSelectable = flags != null? flags.flagIsSet(HtmlTabularReport.Flags.SELECTABLE) : false;
            if (isSelectable)
            {
                nc.getRequest().setAttribute(Dialog.PARAMNAME_AUTOEXECUTE, "yes");
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


    }

    /**
     * Gets the stored procedure name
     * @return
     */
    public String getStoredProcedureName()
    {
        return spName;
    }

    /**
     * Gets the report skin name
     * @return
     */
    public String getReportSkinName()
    {
        return reportSkinName;
    }

    /**
     * Gets the URL formats
     * @return
     */
    public String[] getUrlFormats()
    {
        return urlFormats;
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
