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
 * $Id: QueryDialog.java,v 1.10 2003-10-19 17:05:31 shahid.shah Exp $
 */

package com.netspective.sparx.form.sql;

import java.io.Writer;
import java.io.IOException;

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
import com.netspective.sparx.form.field.DialogFieldFlags;
import com.netspective.sparx.form.field.type.TextField;
import com.netspective.sparx.form.field.type.IntegerField;
import com.netspective.sparx.form.field.type.DataSourceNavigatorButtonsField;
import com.netspective.sparx.form.field.type.ReportSelectedItemsField;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollState;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollStates;
import com.netspective.sparx.console.panel.data.sql.QueryDetailPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.sql.Query;
import com.netspective.sparx.panel.QueryReportPanel;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.Project;
import com.netspective.axiom.sql.QueryParameter;
import com.netspective.axiom.sql.QueryParameters;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.commons.value.source.StaticValueSource;

/**
 * Class for handling query report actions such as navigation and selection
 */
public class QueryDialog extends Dialog
{
    public static final String PARAMNAME_ROWS_PER_PAGE  = ".report_rows_per_page";
    public static final String PARAMNAME_SELECTABLE     = ".report_selectable";
    public static final String PARAMNAME_REPORT_PANEL   = ".report_panel";
    public static final String PARAMNAME_REPORT_SKIN    = ".report_skin";
    public static final String PARAMNAME_QUERY          = ".query";


    private static final Log log = LogFactory.getLog(QueryDialog.class);
    public static final String DEFAULT_ROWS_PER_PAGE_FIELD_NAME = "rows-per-page";
    public static final String DEFAULT_ROWS_PER_PAGE_FIELD_CAPTION = "Rows per page";
    public static final String DEFAULT_ROWS_PER_PAGE_FIELD_VALUE = "10";
    public static final String REPORT_ACTION_FIELD_NAME =  Dialog.PARAMNAME_CONTROLPREFIX +  "report_action";

    //  declare all the possible report actions
    public static final int EXECUTE_SELECT_ACTION   = 0;  // this flag will never be used on the server side
    public static final int EXECUTE_RS_NEXT_ACTION  = 1;
    public static final int EXECUTE_RS_PREV_ACTION  = 2;
    public static final int EXECUTE_RS_FIRST_ACTION = 3;
    public static final int EXECUTE_RS_DONE_ACTION  = 4;
    public static final int EXECUTE_RS_LAST_ACTION  = 5;

    private Query query;
    private HtmlTabularReport report;
    private HtmlTabularReportSkin reportSkin;
    private String[] urlFormats;
    private QueryReportPanel reportPanel;

    public QueryDialog(Project project)
    {
        super(project);
        setDialogContextClass(QueryDialogContext.class);
        createReportActionField();
        createNavigatorField();
        createRowsPerPageField();
        createSelectedItemsField();
    }

    public QueryDialog(Project project, DialogsPackage pkg)
    {
        super(project, pkg);
        setDialogContextClass(QueryDialogContext.class);
        createReportActionField();
        createNavigatorField();
        createRowsPerPageField();
        createSelectedItemsField();
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
     * This is the field whose value indicates what time of action should be performed when the dialog is executed
     * Client side script should set this field to the appropriate value before submitting the form.
     */
    public void createReportActionField()
    {
        DialogField field = new IntegerField();
        field.setName(REPORT_ACTION_FIELD_NAME);
        field.getFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
        addField(field);
    }

    /**
     *  Creates the selected item field. By default, the field is hidden.
     */
    public void createSelectedItemsField()
    {
        ReportSelectedItemsField selectedItemsField = new ReportSelectedItemsField();
        selectedItemsField.setName("selected_item_list");
        selectedItemsField.setSize(5);
        selectedItemsField.getFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
        selectedItemsField.getFlags().setFlag(DialogFieldFlags.INPUT_HIDDEN);
        addField(selectedItemsField);
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
        field.getFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
        addField(field);
    }

    /**
     * Shows the rows per page field which is hidden by default
     */
    public void showRowsPerPageField()
    {
        getFields().getByQualifiedName(DEFAULT_ROWS_PER_PAGE_FIELD_NAME).getFlags().clearFlag(DialogFieldFlags.INPUT_HIDDEN);
    }

    /**
     * Creates the dialog context associated with the dialog
     * @param nc
     * @param skin
     * @return
     */
    public DialogContext createContext(NavigationContext nc, DialogSkin skin)
    {
        DialogContext dc = super.createContext(nc, skin);
        // saves  the query associated with this dialog  in the request object
        dc.getRequest().setAttribute(QueryDetailPanel.REQPARAMNAME_QUERY, query.getQualifiedName());
        dc.getDialog().getDialogFlags().setFlag(DialogFlags.HIDE_HEADING_IN_EXEC_MODE);
        return dc;
    }

    /**
     * Populate the dialog with field values.
     * This should be called everytime the dialog is loaded except when it is ready for
     * execution (validated already)
     */
    public void populateValues(DialogContext dc, int formatType)
    {
        super.populateValues(dc, formatType);
    }

    public void render(Writer writer, DialogContext dc, Theme theme, int flags) throws IOException
    {
        try
        {
            render(writer, dc, false);
        }
        catch (DialogExecuteException e)
        {
            log.error("Dialog execute error", e);
            e.printStackTrace();
            writer.write(e.toString());
        }
    }

    /**
     * Gets the report panel associated with the report
     * @return
     */
    public QueryReportPanel getReportPanel()
    {
        return reportPanel;
    }

    /**
     * Sets the report panel associated with the report
     * @param reportPanel
     */
    public void setReportPanel(QueryReportPanel reportPanel)
    {
        this.reportPanel = reportPanel;
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

    public String[] getUrlFormats()
    {
        return urlFormats;
    }

    public void setUrlFormats(String[] urlFormats)
    {
        this.urlFormats = urlFormats;
    }

    /**
     * Gets the name of the dialog field containing the report rows per page value
     * @return
     */
    public String getRowsPerPageParamName()
    {
        return PARAMNAME_DIALOGPREFIX + getHtmlFormName() + PARAMNAME_ROWS_PER_PAGE;
    }

    /**
     * Gets the name of the dialog field containing the selectable report flag
     * @return
     */
    public String getSelectableParamName()
    {
        return PARAMNAME_DIALOGPREFIX + getHtmlFormName() + PARAMNAME_SELECTABLE;
    }

    /**
     * Gets the name of the dialog field containing the report panel name
     * @return
     */
    public String getReportPanelParamName()
    {
        return PARAMNAME_DIALOGPREFIX + getHtmlFormName() + PARAMNAME_REPORT_PANEL;
    }
    /**
     * Renders the report of the dialog query
     * @param writer
     * @param dc
     * @param reportSkin
     * @throws IOException
     * @throws QueryDefinitionException
     */
    public void renderReport(Writer writer, DialogContext dc, HtmlTabularReportSkin reportSkin) throws IOException, QueryDefinitionException
    {
        QueryReportPanel reportPanel = null;
        QueryDialogContext qdc = (QueryDialogContext)dc;
        if (qdc.getRowsPerPage() > 0)
        {
            HtmlTabularReportDataSourceScrollStates scrollStatesManager = dc.getProject().getScrollStates();
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
                reportPanel = qdc.getReportPanel();
                reportPanel.setScrollRowsPerPage(qdc.getRowsPerPage());
                reportPanel.setScrollable(true);
            }
            else
            {
                reportPanel = (QueryReportPanel) scrollStateById.getPanel();
            }
            reportPanel.render(writer, dc, dc.getActiveTheme(), HtmlPanel.RENDERFLAGS_DEFAULT);
        }
        else
        {
            // no navigation actions are allowed meaning this is a non-scrollable static report
            reportPanel = qdc.getReportPanel();
            reportPanel.render(writer, dc, dc.getActiveTheme(), HtmlPanel.RENDERFLAGS_DEFAULT);
        }
        //destination.render(reportPanel, reportSkin);
    }

    /**
     * Initiates state changes to each individual field of the dialog
     * @param dc
     * @param stage
     */
    public void makeStateChanges(DialogContext dc, int stage)
    {
        QueryDialogContext qdc = (QueryDialogContext) dc;
        DialogContext.DialogFieldStates states = dc.getFieldStates();
        if(dc.inExecuteMode() && stage == DialogContext.STATECALCSTAGE_FINAL)
        {
            //DialogField.State state = dc.getFieldStates().getState(DEFAULT_ROWS_PER_PAGE_FIELD_NAME);
            //rowsPerPage =  state.getValue().getIntValue();

            // hide all the dialog fields
            DialogFields fields = getFields();
            for(int i = 0; i < fields.size(); i++)
            {
                DialogField field = fields.get(i);
                field.makeStateChanges(dc, stage);
                states.getState(field).getStateFlags().setFlag(DialogFieldFlags.INPUT_HIDDEN);
            }

            // Hide the dialog director(OK/Cancel)
            states.getState(getDirector()).getStateFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
            states.getState("selected_item_list").getStateFlags().clearFlag(DialogFieldFlags.UNAVAILABLE);
            // display the Navigation buttons
            if (qdc.getRowsPerPage() > 0)
            {
                states.getState("ds_nav_buttons").getStateFlags().clearFlag(DialogFieldFlags.UNAVAILABLE);
            }
            else
            {
                states.getState("ds_nav_buttons").getStateFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
            }
        }
        else
        {
            states.getState("selected_item_list").getStateFlags().clearFlag(DialogFieldFlags.UNAVAILABLE);
            // if the dialog isnt in execute mode, do not display the result navigation buttons
            if (qdc.getRowsPerPage() > 0)
            {
                states.getState(DEFAULT_ROWS_PER_PAGE_FIELD_NAME).getStateFlags().clearFlag(DialogFieldFlags.UNAVAILABLE);
                states.getState("ds_nav_buttons").getStateFlags().setFlag(DialogFieldFlags.UNAVAILABLE);
            }
        }
    }

    /**
     * Executes the query associated with the dialog and displays the report
     * @param writer
     * @param dc
     * @throws IOException
     * @throws DialogExecuteException
     */
    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        try
        {
            renderReport(writer, dc, reportSkin);
        }
        catch (Exception e)
        {
            log.error("Exception while trying to render report", e);
            e.printStackTrace();
            throw new DialogExecuteException(e);
        }

    }

}
