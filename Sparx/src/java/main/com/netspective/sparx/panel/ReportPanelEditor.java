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
 * @author Aye Thu
 */

/**
 * $Id: ReportPanelEditor.java,v 1.1 2004-03-05 20:17:11 aye.thu Exp $
 */

package com.netspective.sparx.panel;

import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.Project;
import com.netspective.sparx.command.DialogCommand;
import com.netspective.sparx.command.PanelEditorCommand;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogState;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.sql.Query;
import com.netspective.sparx.sql.QueryDefinition;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.util.HttpUtils;

import java.io.IOException;
import java.io.Writer;

public class ReportPanelEditor extends AbstractPanelEditor
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] {  });
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[] {  });
    }

    /* default name assigned to the query defined in the panel editor */
    public static final String DEFAULT_QUERY_NAME = "panel-editor-query";

    /* query definition associated with this panel */
    private QueryDefinition queryDef;
    /* layout panel to use to display the panel editor */
    private HtmlLayoutPanel layoutPanel = new HtmlLayoutPanel();

    public ReportPanelEditor(Project project)
    {
        super(project);
    }

    /**
     *
     */
    public ReportPanelEditor(Project project, PanelEditorsPackage pkg)
    {
        super(project, pkg);
    }

    /**
     * Gets the associated layout panel
     *
     * @return      layout panel
     */
    public HtmlLayoutPanel getLayoutPanel()
    {
        return layoutPanel;
    }

    /**
     * Sets the associated layout panel
     *
     * @param layoutPanel
     */
    public void setLayoutPanel(HtmlLayoutPanel layoutPanel)
    {
        this.layoutPanel = layoutPanel;
    }

    /**
     * Gets the column index to use as the primary key when a record is to be edited
     *
     * @return      report column index
     */
    public int getPkColumnIndex()
    {
        return pkColumnIndex;
    }

    /**
     * Sets the column index to use as the primary key when a record is to be edited
     *
     * @param pkColumnIndex
     */
    public void setPkColumnIndex(int pkColumnIndex)
    {
        this.pkColumnIndex = pkColumnIndex;
    }

    /**
     * Creates the source of the data to use to populate the panel
     *
     * @param   nc      Current navigation context
     * @return          data source for the panel
     */
    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return null;
    }

    /**
     * Gets the report
     *
     * @param nc
     * @return
     */
    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return null;
    }

    /**
     * Sets the query defined for the display mode of the record manager
     *
     * @param query     query name
     */
    public void setQuery(Query query)
    {
        this.query = query;
    }

    /**
     * Creates a query object
     *
     * @return          query
     */
    public Query createQuery()
    {
        return new com.netspective.sparx.sql.Query(project);
    }

    /**
     * Adds a query object
     *
     * @param query     query
     */
    public void addQuery(Query query)
    {
        this.query = query;
        this.query.setName(DEFAULT_QUERY_NAME);
        this.query.setNameSpace(getNameSpace().getQueriesNameSpace());
        this.project.getQueries().add(query);
    }

    /**
     * Gets the query definition defined for the default display mode of the panel editor
     *
     * @return  query definition object
     */
    public QueryDefinition getQueryDef()
    {
        return queryDef;
    }

    /**
     * Sets the query definition defined for the default display mode of the panel editor
     *
     * @param queryDef
     */
    public void setQueryDef(QueryDefinition queryDef)
    {
        this.queryDef = queryDef;
    }

     /**
     * Creates all the panel actions for the  panel editor. This method SHOULD only be called once to populate the
     * panel editor.
     *
     * @param nc    current navigation context
     */
    public void createPanelActions(NavigationContext nc)
    {
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        if (qrp == null)
        {
            qrp = new QueryReportPanel();
            qrp.setQuery(getQuery());
            qrp.setFrame(new HtmlPanelFrame());
            qrp.setDefault(true);
            getQuery().getPresentation().setDefaultPanel(qrp);
        }
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) qrp.getReport();
        if (report == null)
        {
            report = new BasicHtmlTabularReport();
            qrp.addReport(report);
        }
        // HIDE THE HEADING
        qrp.getReport().getFlags().setFlag(TabularReport.Flags.HIDE_HEADING);
        qrp.setReportSkin(DEFAULT_EDITOR_SKIN);
        super.createPanelActions(nc);
    }

    public void createPanelContentActions(NavigationContext nc)
    {
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) qrp.getReport();
        HtmlReportActions actions = new HtmlReportActions();
        HtmlReportAction editAction = actions.createAction();
        editAction.setCaption(new StaticValueSource(PANEL_RECORD_EDIT_ACTION));
        editAction.setHint(new StaticValueSource(PANEL_RECORD_EDIT_ACTION));
        editAction.setRedirect(new RedirectValueSource(generatePanelActionUrl(nc, EDIT_CONTENT_DISPLAY_MODE)));
        editAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_EDIT));

        HtmlReportAction deleteAction = actions.createAction();
        deleteAction.setCaption(new StaticValueSource(PANEL_CONTENT_DELETE_ACTION));
        deleteAction.setHint(new StaticValueSource(PANEL_CONTENT_DELETE_ACTION));
        deleteAction.setRedirect(new RedirectValueSource(generatePanelActionUrl(nc, DELETE_CONTENT_DISPLAY_MODE)));
        deleteAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_DELETE));

        actions.addAction(editAction);
        actions.addAction(deleteAction);
        report.addActions(actions);
    }

    public void createPanelFrameActions(NavigationContext nc)
    {
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        HtmlPanelFrame frame = qrp.getFrame();
        HtmlPanelActions actions = new HtmlPanelActions();
        HtmlPanelAction manageAction = frame.createAction();
        manageAction.setCaption(new StaticValueSource(PANEL_CONTENT_MANAGE_ACTION));
        manageAction.setRedirect(new RedirectValueSource(generatePanelActionUrl(nc, MANAGE_CONTENT_DISPLAY_MODE)));
        actions.add(manageAction);

        HtmlPanelAction addAction = frame.createAction();
        addAction.setCaption(new StaticValueSource(PANEL_CONTENT_ADD_ACTION));
        addAction.setRedirect(new RedirectValueSource(generatePanelActionUrl(nc, ADD_CONTENT_DISPLAY_MODE)));
        actions.add(addAction);

        HtmlPanelAction doneAction = frame.createAction();
        doneAction.setCaption(new StaticValueSource(PANEL_RECORD_DONE_ACTION));
        doneAction.setRedirect(new RedirectValueSource(generatePanelActionUrl(nc, DEFAULT_DISPLAY_MODE)));
        actions.add(doneAction);

        frame.setActions(actions);
    }

    public void createPanelBannerActions(NavigationContext nc)
    {
        // Calculate what to display in the banner
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        if (qrp.getBanner() == null)
        {
            qrp.setBanner(new HtmlPanelBanner());
        }
        HtmlPanelBanner banner = qrp.getBanner();
        HtmlPanelActions actions = new HtmlPanelActions();
        HtmlPanelAction addAction = frame.createAction();
        addAction.setCaption(new StaticValueSource(PANEL_CONTENT_ADD_ACTION));
        addAction.setRedirect(new RedirectValueSource(generatePanelActionUrl(nc, ADD_CONTENT_DISPLAY_MODE)));
        actions.add(addAction);
        banner.setActions(actions);
    }

    /**
     * Generates the URL string for the  panel editor's associated actions
     *
     * @param actionMode    the mode  for which the URL is being calculated
     * @return              url string (containing context sensitive elements) used to construct a redirect value source
     */
    public String generatePanelActionUrl(NavigationContext nc, int actionMode)
    {
        String url = "?";
        String currentUrl = nc.getActivePage().getUrl(nc);

        if (actionMode == EDIT_CONTENT_DISPLAY_MODE)
        {
            url = url + PanelEditorCommand.PANEL_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() +
                    ",edit,${" + pkColumnIndex + "},${request-attr:" + PREV_MODE_CONTEXT_ATTRIBUTE + "}";
        }
        else if (actionMode == DELETE_CONTENT_DISPLAY_MODE)
        {
            url = url + PanelEditorCommand.PANEL_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() +
                    ",delete,${" + pkColumnIndex + "},${request-attr:" + PREV_MODE_CONTEXT_ATTRIBUTE + "}";
        }
        else if (actionMode == ADD_CONTENT_DISPLAY_MODE)
        {
            url = url + PanelEditorCommand.PANEL_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() +
                    ",add,,${request-attr:" + PREV_MODE_CONTEXT_ATTRIBUTE + "}";
        }
        else if (actionMode == MANAGE_CONTENT_DISPLAY_MODE)
            url = url + PanelEditorCommand.PANEL_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",manage";
        else if (actionMode == DEFAULT_DISPLAY_MODE)
            url = currentUrl;

        ValueSource retainParamsVS = nc.getActivePage().getRetainParams();
        if(retainParamsVS != null)
            url = HttpUtils.appendParams(nc.getHttpRequest(), url, retainParamsVS.getTextValue(nc));
        return url;
    }

    /**
     * Renders the panel editor
     *
     * @param writer    writer to render the output to
     * @param nc        current navigation context
     * @param theme     current theme
     * @param flags
     * @throws IOException
     */
    public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
    {
        com.netspective.sparx.sql.Query query = (com.netspective.sparx.sql.Query) getQuery();
        if(query == null)
        {
            throw new RuntimeException("Query not found in "+ this +".");
        }
        if (getRequireRequestParam() != null)
        {
            if (nc.getHttpRequest().getParameter(getRequireRequestParam()) == null)
                throw new RuntimeException("Record editor panel '" + getQualifiedName() + "' requires the request " +
                        "parameter '" + getRequireRequestParam() + "'.");
        }
        String requestedMode = (String) nc.getRequest().getAttribute(CURRENT_MODE_CONTEXT_ATTRIBUTE);
        String requestedKey = (String) nc.getRequest().getAttribute(POPULATE_KEY_CONTEXT_ATTRIBUTE);
        String prevMode = (String) nc.getRequest().getAttribute(PREV_MODE_CONTEXT_ATTRIBUTE);

        // add all the required panel actions
        if (!isActionsPrepared())
            createPanelActions(nc);
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        // NOTE: This scrollable setting is used or else the connection won't be opened with 'scrollablity'  even
        // if the database supports it.
        qrp.setScrollable(true);

        // get the requested mode, key, and previous modes from the request
        int mode = validatePanelEditorMode(requestedMode, requestedKey);
        if (mode == UNKNOWN_MODE)
        {
            log.error("Unexpected mode encountered for the record editor panel '" + getName() + "'.");
            throw new RuntimeException("Unexpected mode encountered for the record editor panel '" + getName() + "'.");
        }

        HtmlTabularReportSkin skin = null;
        if (mode == ReportPanelEditor.MANAGE_CONTENT_DISPLAY_MODE)
            skin = theme.getReportSkin("panel-editor-compressed");
        else
            skin = theme.getReportSkin("panel-editor");

        HtmlTabularReportValueContext context = qrp.createContext(nc, skin);
        context.setPanelRenderFlags(flags);

        TabularReportDataSource dataRoot = qrp.createDataSource(nc);
        int totalRows = dataRoot.getTotalRows();
        // process the context to calculate the states of the panel actions
        preparePanelActionStates(nc, context, totalRows, mode);

        writer.write("<table border=\"0\" cellspacing=\"0\" class=\"panel-editor-table\">");
        writer.write("<tr valign=\"top\">");
        if (mode == ReportPanelEditor.ADD_CONTENT_DISPLAY_MODE || mode == ReportPanelEditor.EDIT_CONTENT_DISPLAY_MODE ||
            mode == ReportPanelEditor.DELETE_CONTENT_DISPLAY_MODE)
        {
            // set the dialog perspective using the requested mode.
            // IMPORTANT: The dialog perspective strings are exactly the same as the panel editor's add/edit/delete modes.
            nc.getRequest().setAttribute(DialogState.PARAMNAME_PERSPECTIVE, requestedMode);
            // record action was defined so we need to display the requested display mode
            DialogContext dc = dialog.createContext(nc, theme.getDefaultDialogSkin());
            dc.addRetainRequestParams(DialogCommand.DIALOG_COMMAND_RETAIN_PARAMS);

            dialog.prepareContext(dc);

            writer.write("<td class=\"panel-editor-dialog\">");
            try
            {
                dialog.render(writer, dc, true);
            }
            catch (DialogExecuteException dee)
            {
                log.error("Failed to render dialog in panel editor '" + getQualifiedName() + "'.");
                throw new RuntimeException("Failed to render dialog in panel editor '" + getQualifiedName() + "'.");
            }
            writer.write("</td>");
        }
        writer.write("<td class=\"panel-editor-report\">");
        qrp.render(writer, context, dataRoot);
        writer.write("</td>");

        writer.write("</tr></table>");
    }

    public void render(Writer writer, DialogContext dc, Theme theme, int flags) throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}