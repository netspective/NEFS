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
 * $Id: QueryDefnCommand.java,v 1.3 2003-07-03 22:35:16 aye.thu Exp $
 */

package com.netspective.sparx.command;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.sql.QueryBuilderDialog;
import com.netspective.sparx.form.sql.QuerySelectDialog;
import com.netspective.sparx.sql.QueryDefinition;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.axiom.SqlManager;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueryDefnCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(QueryDefnCommand.class);
    public static final int UNLIMITED_ROWS = Integer.MAX_VALUE;
    public static final String[] IDENTIFIERS = new String[] { "query-defn" };

    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Displays results of a Query definition.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("query-defn-name", true, "The fully qualified name of the Query Definition."),
                new CommandDocumentation.Parameter("query-defn-select-id", false, "The name of the query definition select or '-' for the standard query builder dialog."),
                new CommandDocumentation.Parameter("query-defn-select-dialog-id", false, "The name of a specific query definition select dialog or '-' for the standard query builder dialog."),
                new CommandDocumentation.Parameter("report-id", false, "The name of a specific report element in the query definition select or '-' for the default report-id."),
                new CommandDocumentation.Parameter("rows-per-page", false, "-", "The number of rows per page to display ('-' means single page, any other number means a pageable report."),
                new QueryCommand.SkinParameter(),
                new CommandDocumentation.Parameter("url-formats", false, "The url-formats parameter is one or more "+
                                                   "semicolon-separated URL formats that may override those within a report."),
            }
    );
    public static final int MAXIMUM_CONDITIONS = 5;

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    private String schemaName;
    private String queryDefnName;
    private String queryDefnSelectName;
    private String queryDefnSelectDialogName;
    private int rowsPerPage;
    private String reportSkinName;
    private String reportId;
    private String[] urlFormats;

    public void setParameters(StringTokenizer params)
    {
        queryDefnName = params.nextToken();

        if(params.hasMoreTokens())
        {
            queryDefnSelectName = params.nextToken();
            if(queryDefnSelectName.length() == 0 || queryDefnSelectName.equals(PARAMVALUE_DEFAULT))
                queryDefnSelectName = null;
        }
        else
            queryDefnSelectName = null;

        if(params.hasMoreTokens())
        {
            queryDefnSelectDialogName = params.nextToken();
            if(queryDefnSelectDialogName.length() == 0 || queryDefnSelectDialogName.equals(PARAMVALUE_DEFAULT))
                queryDefnSelectDialogName = null;
        }
        else
            queryDefnSelectDialogName = null;

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
    }

    public String getQueryDefnName()
    {
        return queryDefnName;
    }

    public String getReportSkinName()
    {
        return reportSkinName;
    }

    public void setQueryDefnName(String queryDefnName)
    {
        this.queryDefnName = queryDefnName;
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
        StringBuffer sb = new StringBuffer(queryDefnName);
        sb.append(delim);
        sb.append(queryDefnSelectName != null ? queryDefnSelectName : PARAMVALUE_DEFAULT);
        sb.append(delim);
        sb.append(queryDefnSelectDialogName != null ? queryDefnSelectDialogName : PARAMVALUE_DEFAULT);
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

    public QueryBuilderDialog createQueryDefnDialog(Writer writer, SqlManager sqlManager, Theme theme) throws IOException
    {
        // get the registered query definition
        com.netspective.sparx.sql.QueryDefinition queryDefn = (QueryDefinition) sqlManager.getQueryDefinition(queryDefnName, true);
        if(queryDefn == null)
        {
            writer.write("Query definition " + queryDefnName + " not found.");
            return null;
        }
        QueryBuilderDialog result = null;
        if (queryDefnSelectDialogName != null && !queryDefnSelectDialogName.equals(PARAMVALUE_DEFAULT))
        {
            result = queryDefn.getPresentation().getSelectDialog(queryDefnSelectDialogName);
            if (result == null)
            {
                writer.write("Query select dialog '" + queryDefnSelectDialogName + "' not found.");
                return null;
            }
            result.setName(queryDefnSelectDialogName);
            result.setQueryDefn(queryDefn);
        }
        else
        {
            result = queryDefn.createQueryBuilderDialog();
        }
        return result;
        // TODO:
        //if(queryDefnSelectDialogName.equals(PARAMVALUE_DEFAULT))
        //    return ((com.netspective.sparx.sql.Query) query).getPresentation().getDefaultDialog();
        //else
        //    return ((com.netspective.sparx.sql.Query) query).getPresentation().getDialog(queryDefnSelectDialogName);
    }

    public void handleCommand(Writer writer, DialogContext dc, boolean unitTest) throws CommandException, IOException
    {
        SqlManager sqlManager = dc.getSqlManager();
        Theme theme = dc.getActiveTheme();
        QueryBuilderDialog dialog = createQueryDefnDialog(writer, sqlManager, theme);
        if(dialog != null)
            dialog.render(writer, dc.getNavigationContext(), theme, HtmlPanel.RENDERFLAGS_DEFAULT);
    }

    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        SqlManager sqlManager = nc.getSqlManager();
        Theme theme = nc.getActiveTheme();
        QueryBuilderDialog dialog = createQueryDefnDialog(writer, sqlManager, theme);
        if(dialog != null)
            dialog.render(writer, nc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);
    }
}
