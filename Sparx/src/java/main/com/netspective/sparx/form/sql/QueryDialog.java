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
 * @author Shahid N. Shah
 */

/**
 * $Id: QueryDialog.java,v 1.5 2003-07-11 17:40:19 aye.thu Exp $
 */

package com.netspective.sparx.form.sql;

import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogSkin;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogFlags;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.form.field.type.TextField;
import com.netspective.sparx.form.field.type.IntegerField;
import com.netspective.sparx.form.field.type.DataSourceNavigatorButtonsField;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportDestination;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollStates;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollState;
import com.netspective.sparx.report.tabular.destination.HtmlTabularReportBrowserDestination;
import com.netspective.sparx.console.panel.data.sql.QueryDetailPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.sql.Query;
import com.netspective.sparx.command.HttpServletCommand;
import com.netspective.sparx.panel.QueryReportPanel;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.theme.Theme;
import com.netspective.axiom.sql.QueryParameter;
import com.netspective.axiom.sql.QueryParameters;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.command.Commands;
import com.netspective.commons.command.CommandNotFoundException;
import com.netspective.commons.command.CommandException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

public class QueryDialog extends Dialog
{
    private static final Log log = LogFactory.getLog(QueryDialog.class);
    public static final String DEFAULT_ROWS_PER_PAGE_FIELD_NAME = "rows-per-page";
    public static final String DEFAULT_ROWS_PER_PAGE_FIELD_CAPTION = "Rows per page";
    public static final String DEFAULT_ROWS_PER_PAGE_FIELD_VALUE = "10";

    private Query query;
    private HtmlTabularReport report;
    private HtmlTabularReportSkin reportSkin;
    private String[] urlFormats;
    private int rowsPerPage;

    public QueryDialog()
    {
        super();
        createNavigatorField();
        createRowsPerPageField();
    }

    public QueryDialog(DialogsPackage pkg)
    {
        super(pkg);
        createNavigatorField();
        createRowsPerPageField();
    }

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(Query query)
    {
        this.query = query;
    }

    /**
     * Create the result set navigator buttons
     */
    public void createNavigatorField()
    {
        addField(new DataSourceNavigatorButtonsField());
    }

    public void createParamFields()
    {
        QueryParameters params = query.getParams();
        if(params != null)
        {
            for(int i = 0; i < query.getParams().size(); i++)
            {
                QueryParameter param = query.getParams().get(i);
                DialogField field = new TextField();
                field.setName("param_" + i);
                field.setCaption(new StaticValueSource(param.getName() != null ? param.getName() : ("Parameter " + i)));
                field.setDefault(param.getValue());
                addField(field);
            }
        }
    }

    /**
     * Creates the rows-per-page dialog field. By default, the field is hidden.
     */
    private void createRowsPerPageField()
    {
        DialogField field = new IntegerField();
        field.setName(DEFAULT_ROWS_PER_PAGE_FIELD_NAME);
        field.setCaption(new StaticValueSource(DEFAULT_ROWS_PER_PAGE_FIELD_CAPTION));
        field.setDefault(new StaticValueSource(DEFAULT_ROWS_PER_PAGE_FIELD_VALUE));
        // by default, hide the field
        field.getFlags().setFlag(DialogField.Flags.INPUT_HIDDEN);
        addField(field);
        showRowsPerPageField();
    }

    /**
     * Shows the rows per page field which is hidden by default
     */
    public void showRowsPerPageField()
    {
        getFields().getByQualifiedName(DEFAULT_ROWS_PER_PAGE_FIELD_NAME).getFlags().clearFlag(DialogField.Flags.INPUT_HIDDEN);
    }

    public DialogContext createContext(NavigationContext nc, DialogSkin skin)
    {
        DialogContext dc = super.createContext(nc, skin);
        dc.getRequest().setAttribute(QueryDetailPanel.REQPARAMNAME_QUERY, query.getQualifiedName());
        dc.getDialog().getDialogFlags().setFlag(DialogFlags.HIDE_HEADING_IN_EXEC_MODE);
        return dc;
    }

    public HtmlTabularReport getReport()
    {
        return report;
    }

    public void setReport(HtmlTabularReport report)
    {
        this.report = report;
    }

    public HtmlTabularReportSkin getReportSkin()
    {
        return reportSkin;
    }

    public void setReportSkin(HtmlTabularReportSkin reportSkin)
    {
        this.reportSkin = reportSkin;
    }

    public int getRowsPerPage()
    {
        return rowsPerPage;
    }

    /**
     * Sets the rows per page for the report
     * @param rowsPerPage
     */
    public void setRowsPerPage(int rowsPerPage)
    {
        this.rowsPerPage = rowsPerPage;
        getFields().getByQualifiedName(DEFAULT_ROWS_PER_PAGE_FIELD_NAME).setDefault(new StaticValueSource(Integer.toString(rowsPerPage)));
    }

    public String[] getUrlFormats()
    {
        return urlFormats;
    }

    public void setUrlFormats(String[] urlFormats)
    {
        this.urlFormats = urlFormats;
    }

    public void renderReport(Writer writer, DialogContext dc, HtmlTabularReportSkin reportSkin) throws IOException, QueryDefinitionException
    {
        QueryReportPanel reportPanel = null;

        HtmlTabularReportDataSourceScrollStates scrollStatesManager = HtmlTabularReportDataSourceScrollStates.getInstance();
        HtmlTabularReportDataSourceScrollState scrollStateById = scrollStatesManager.getScrollStateByDialogTransactionId(dc);

        /*
            If the state is not found, then we have not executed at all yet;
            if the state is found and it's the initial execution then it means
            that the user has pressed the "back" button -- which means we
            should reset the state management.
         */
        if(scrollStateById == null || (scrollStateById != null && dc.isInitialExecute()))
        {
            // if our transaction does not have a scroll state, but there is an active scroll state available, then it
            // means that we need to close the previous one and remove the attribute so that the connection can be
            // closed and returned to the pool
            HtmlTabularReportDataSourceScrollState activeScrollState = scrollStatesManager.getActiveScrollState(dc);

            if(activeScrollState != null && ! getDialogFlags().flagIsSet(QueryBuilderDialogFlags.ALLOW_MULTIPLE_SCROLL_STATES))
                scrollStatesManager.removeActiveState(dc, activeScrollState);

            reportPanel = new QueryReportPanel();
            reportPanel.setQuery(query);
            reportPanel.setScrollable(true);
            reportPanel.setScrollRowsPerPage(this.getRowsPerPage());
        }
        else
        {
            reportPanel = (QueryReportPanel) scrollStateById.getPanel();
        }
        reportPanel.render(writer, dc, dc.getActiveTheme(), HtmlPanel.RENDERFLAGS_DEFAULT);
        //destination.render(reportPanel, reportSkin);
    }

    /**
     * Initiates state changes to each individual field of the dialog
     * @param dc
     * @param stage
     */
    public void makeStateChanges(DialogContext dc, int stage)
    {

        DialogContext.DialogFieldStates states = dc.getFieldStates();

        if(dc.inExecuteMode() && stage == DialogContext.STATECALCSTAGE_FINAL)
        {

            DialogFields fields = getFields();
            for(int i = 0; i < fields.size(); i++)
            {
                DialogField field = fields.get(i);
                field.makeStateChanges(dc, stage);
                states.getState(field).getStateFlags().setFlag(DialogField.Flags.INPUT_HIDDEN);
            }

            // Hide the dialog director(OK/Cancel)
            states.getState(getDirector()).getStateFlags().setFlag(DialogField.Flags.UNAVAILABLE);
            // display the Navigation buttons
            states.getState("ds_nav_buttons").getStateFlags().clearFlag(DialogField.Flags.UNAVAILABLE);
        }
        else
        {
            // if the dialog isnt in execute mode, do not display the result navigation buttons
            states.getState("ds_nav_buttons").getStateFlags().setFlag(DialogField.Flags.UNAVAILABLE);
        }
    }


    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        try
        {
            DialogField.State state = dc.getFieldStates().getState(DEFAULT_ROWS_PER_PAGE_FIELD_NAME);
            rowsPerPage =  state.getValue().getIntValue();

            renderReport(writer, dc, reportSkin);
        }
        catch (Exception e)
        {
            log.error("Exception while trying to render report", e);
            throw new DialogExecuteException(e);
        }
        /*
        try
        {
            HttpServletCommand command = (HttpServletCommand) Commands.getInstance().getCommand("query," + query.getQualifiedName());
            command.handleCommand(writer, dc, false);
        }
        catch (CommandNotFoundException e)
        {
            log.error("Unable to find query command -- this should never happen.", e);
            throw new DialogExecuteException(e);
        }
        catch (CommandException e)
        {
            log.error("Error executing query command.", e);
            throw new DialogExecuteException(e);
        }
        */
    }
}
