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
 * $Id: PanelEditor.java,v 1.2 2004-03-03 08:22:37 aye.thu Exp $
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
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.sql.Query;
import com.netspective.sparx.sql.QueryDefinition;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;

public class PanelEditor extends AbstractPanel
{
    private static final Log log = LogFactory.getLog(PanelEditor.class);

    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] {  });
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[] {  });
    }

    public static final String PANEL_RECORD_EDIT_ACTION     = "Edit";
    public static final String PANEL_RECORD_ADD_ACTION      = "Add";
    public static final String PANEL_RECORD_MANAGE_ACTION   = "Manage";
    public static final String PANEL_RECORD_DONE_ACTION     = "Done";
    public static final String PANEL_RECORD_DELETE_ACTION   = "Delete";

    // the following are all the possible modes that the editor panel can be in
    public static final int UNKNOWN_MODE                = -1;
    public static final int DEFAULT_DISPLAY_MODE        = 1;    /* default display report mode */
    public static final int EDIT_RECORD_DISPLAY_MODE    = 2;    /* editing a record mode (dialog and report) */
    public static final int DELETE_RECORD_DISPLAY_MODE  = 3;    /* deleting a record mode (dialog and report) */
    public static final int ADD_RECORD_DISPLAY_MODE     = 4;    /* add a record mode (dialog and report) */
    public static final int MANAGE_RECORDS_DISPLAY_MODE = 5;    /* managing records mode (report only but different from default) */

    /* the display mode is passed to the panel using this attribute in the navigation context */
    public static final String DISPLAY_MODE_CONTEXT_ATTRIBUTE = "record-editor-mode";
    /* default skin to use to display query report panel */
    public static final String DEFAULT_EDITOR_SKIN = "panel-editor";
    /* default name assigned to the query defined in the panel editor */
    public static final String DEFAULT_QUERY_NAME = "panel-editor-query";
    /* default name assigned to the dialog defined in the panel editor */
    public static final String DEFAULT_DIALOG_NAME = "panel-editor-dialog";

    /* associated project */
    private Project project;
    /* query associated with this panel */
    private Query query;
    /* query definition associated with this panel */
    private QueryDefinition queryDef;
    /* default dialog to use for panel content editing */
    private Dialog dialog;
    /* the package that the panel belongs to */
    private PanelEditorsPackage nameSpace;
    /* name of the panel */
    private String name;
    /* flag to indicate if the record actions for this panel have been prepared or not */
    private boolean actionsPrepared = false;
    /* indicates what column index of the query is used as the primary key */
    private int pkColumnIndex = 0;
    /* indicates what request parameter is required for this panel */
    private String requireRequestParam = null;
    /* layout panel to use to display the panel editor */
    private HtmlLayoutPanel layoutPanel = new HtmlLayoutPanel();

    public PanelEditor(Project project)
    {
        this.project = project;
    }

    /**
     *
     */
    public PanelEditor(Project project, PanelEditorsPackage pkg)
    {
        this(project);
        setNameSpace(pkg);
    }

    /**
     * Gets the associated project
     *
     * @return  associated project
     */
    public Project getProject()
    {
        return project;
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
     * Gets the request parameter that is required for this panel editor
     *
     * @return      request parameter name
     */
    public String getRequireRequestParam()
    {
        return requireRequestParam;
    }

    /**
     * Sets the request parameter that is required for this panel editor
     *
     * @param requireRequestParam
     */
    public void setRequireRequestParam(String requireRequestParam)
    {
        this.requireRequestParam = requireRequestParam;
    }

    /**
     * Gets the name of the panel
     *
     * @return  name of the panel
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the panel
     *
     * @param name      name of the panel
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public String getQualifiedName()
    {
        return nameSpace != null ? nameSpace.getNameSpaceId() + "." + name : name;
    }

    /**
     * Gets the namespace (package name)
     *
     * @return      namespace
     */
    public PanelEditorsPackage getNameSpace()
    {
        return nameSpace;
    }

    /**
     * Sets the namespace (package name)
     *
     * @param nameSpace     package name
     */
    public void setNameSpace(PanelEditorsPackage nameSpace)
    {
        this.nameSpace = nameSpace;
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
     * Set the dialog ID associated with the panel editor
     *
     * @param dialog
     */
    public void setDialogRef(String dialog)
    {
        this.dialog = this.project.getDialog(dialog);
        if (this.dialog == null)
            log.error("Unknown dialog '" + dialog + "'.");
    }

    /**
     * Gets the dialog associated with the panel editor
     *
     * @return
     */
    public Dialog getDialog()
    {
        return dialog;
    }

    /**
     * Adds a dialog to the panel editor.
     *
     * @param dialog
     */
    public void addDialog(Dialog dialog)
    {
        this.dialog = dialog;
        if (this.dialog.getName() == null)
            this.dialog.setName(DEFAULT_DIALOG_NAME);
        this.dialog.setNameSpace(getNameSpace().getDialogsNameSpace());
    }

    /**
     * Creates a dialog instance
     *
     * @return
     */
    public Dialog createDialog()
    {
        return new com.netspective.sparx.form.Dialog(project);
    }

    /**
     * Gets the query defined for the default display mode of the panel editor
     *
     * @return   query name (NULL if query definition is defined instead)
     */
    public Query getQuery()
    {
        return query;
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
     * Gets the name to use when saving this panel editor in a collection map
     *
     * @return
     */
    public String getNameForMapKey()
    {
        return translateNameForMapKey(getQualifiedName());
    }

    /**
     * Translate name of the panel editor to a key name suitable for use in a map
     *
     * @param name  name of the panel editor
     * @return      translated key name
     */
    public static final String translateNameForMapKey(String name)
    {
        return name != null ? name.toUpperCase() : null;
    }

    /**
     * Generates the URL string for the associated actions
     *
     * @param mode    type of record action
     * @return              url string used to construct a redirect value source
     */
    public String generateRecordActionUrl(NavigationContext nc, int mode)
    {
        String url = "?";
        String currentUrl = nc.getActivePage().getUrl(nc);


        if (mode == EDIT_RECORD_DISPLAY_MODE)
             url = url + PanelEditorCommand.RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",edit,${" + pkColumnIndex + "}";
        else if (mode == DELETE_RECORD_DISPLAY_MODE)
            url = url + PanelEditorCommand.RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",delete,${" + pkColumnIndex + "}";
        else if (mode == ADD_RECORD_DISPLAY_MODE)
            url = url + PanelEditorCommand.RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",add";
        else if (mode == MANAGE_RECORDS_DISPLAY_MODE)
            url = url + PanelEditorCommand.RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",manage";
        else if (mode == DEFAULT_DISPLAY_MODE)
            url = currentUrl;

        ValueSource retainParamsVS = nc.getActivePage().getRetainParams();
        if(retainParamsVS != null)
            url = HttpUtils.appendParams(nc.getHttpRequest(), url, retainParamsVS.getTextValue(nc));
        return url;
    }

    /**
     * Creates all the panel actions for the  panel editor. This method SHOULD only be called once to populate the
     * panel editor.
     *
     * @param nc    current navigation context
     */
    protected void createPanelActions(NavigationContext nc)
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
        //createPanelBannerActions(nc);
        createPanelFrameActions(nc);
        createPanelRecordActions(nc);
        qrp.setReportSkin(DEFAULT_EDITOR_SKIN);
        actionsPrepared = true;
    }

    protected void createPanelRecordActions(NavigationContext nc)
    {
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        BasicHtmlTabularReport report = (BasicHtmlTabularReport) qrp.getReport();
        HtmlReportActions actions = new HtmlReportActions();
        HtmlReportAction editAction = actions.createAction();
        editAction.setCaption(new StaticValueSource(PANEL_RECORD_EDIT_ACTION));
        editAction.setHint(new StaticValueSource(PANEL_RECORD_EDIT_ACTION));
        editAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, EDIT_RECORD_DISPLAY_MODE)));
        editAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_EDIT));

        HtmlReportAction deleteAction = actions.createAction();
        deleteAction.setCaption(new StaticValueSource(PANEL_RECORD_DELETE_ACTION));
        deleteAction.setHint(new StaticValueSource(PANEL_RECORD_DELETE_ACTION));
        deleteAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, DELETE_RECORD_DISPLAY_MODE)));
        deleteAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_DELETE));

        actions.addAction(editAction);
        actions.addAction(deleteAction);
        report.addActions(actions);
    }

    protected void createPanelFrameActions(NavigationContext nc)
    {
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        HtmlPanelFrame frame = qrp.getFrame();
        HtmlPanelActions actions = new HtmlPanelActions();
        HtmlPanelAction manageAction = frame.createAction();
        manageAction.setCaption(new StaticValueSource(PANEL_RECORD_MANAGE_ACTION));
        manageAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, MANAGE_RECORDS_DISPLAY_MODE)));
        actions.add(manageAction);

        HtmlPanelAction addAction = frame.createAction();
        addAction.setCaption(new StaticValueSource(PANEL_RECORD_ADD_ACTION));
        addAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, ADD_RECORD_DISPLAY_MODE)));
        actions.add(addAction);

        HtmlPanelAction doneAction = frame.createAction();
        doneAction.setCaption(new StaticValueSource(PANEL_RECORD_DONE_ACTION));
        doneAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, DEFAULT_DISPLAY_MODE)));
        actions.add(doneAction);

        frame.setActions(actions);
    }

    /**
     * Creates all the panel actions for the banner
     *
     * @param nc    current navigation context
     */
    protected void createPanelBannerActions(NavigationContext nc)
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
        addAction.setCaption(new StaticValueSource(PANEL_RECORD_ADD_ACTION));
        addAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, ADD_RECORD_DISPLAY_MODE)));
        actions.add(addAction);
        banner.setActions(actions);
    }

    /**
     * Calculate and process the state of the all the panel actions based on current context
     *
     * @param nc    current navigation context
     * @param vc    current report panel context
     */
    public void preparePanelActionStates(NavigationContext nc, HtmlTabularReportValueContext vc, int panelRecordCount)
    {
        HtmlPanelActionStates actionStates = vc.getPanelActionStates();
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        HtmlPanelActions bannerActions = qrp.getBanner().getActions();
        HtmlPanelActions frameActions = qrp.getFrame().getActions();
        HtmlReportActions recordActions = ((BasicHtmlTabularReport)qrp.getReport()).getActions();

        int mode = ((Integer) nc.getAttribute(DISPLAY_MODE_CONTEXT_ATTRIBUTE)).intValue();
        if (mode == DEFAULT_DISPLAY_MODE)
        {
            actionStates.getState(PANEL_RECORD_DONE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            if (panelRecordCount > 0)
                actionStates.getState(PANEL_RECORD_ADD_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            actionStates.getState(PANEL_RECORD_DELETE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            if (panelRecordCount <= 0)
                actionStates.getState(PANEL_RECORD_MANAGE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
        }
        else if (mode == ADD_RECORD_DISPLAY_MODE || mode == EDIT_RECORD_DISPLAY_MODE || mode == DELETE_RECORD_DISPLAY_MODE)
        {
            actionStates.getState(PANEL_RECORD_DONE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            actionStates.getState(PANEL_RECORD_MANAGE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            actionStates.getState(PANEL_RECORD_ADD_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
        }
        else if (mode == MANAGE_RECORDS_DISPLAY_MODE)
        {
            actionStates.getState(PANEL_RECORD_MANAGE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
        }
    }

    /**
     * Check to see if all the actions needed for the  panel edito have been ADDED. The
     * adding of the actions need to be done only once and this flag is set
     * once the addition is done.
     *
     * @return  True if all required actions have been added
     */
    public boolean isActionsPrepared()
    {
        return actionsPrepared;
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
        // add all the required panel actions
        if (!isActionsPrepared())
            createPanelActions(nc);
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        // NOTE: This scrollable setting is used or else the connection won't be opened with 'scrollablity'  even
        // if the database supports it.
        qrp.setScrollable(true);

        HtmlTabularReportValueContext context = qrp.createContext(nc, theme);
        context.setPanelRenderFlags(flags);

        TabularReportDataSource dataRoot = qrp.createDataSource(nc);
        int totalRows = dataRoot.getTotalRows();
        // process the context to calculate the states of the panel actions
        preparePanelActionStates(nc, context, totalRows);

        writer.write("<table border=\"0\" cellspacing=\"0\" class=\"panel-editor-table\">");
        writer.write("<tr valign=\"top\">");
        int mode = ((Integer) nc.getAttribute(DISPLAY_MODE_CONTEXT_ATTRIBUTE)).intValue();
        if (mode == PanelEditor.ADD_RECORD_DISPLAY_MODE || mode == PanelEditor.EDIT_RECORD_DISPLAY_MODE ||
            mode == PanelEditor.DELETE_RECORD_DISPLAY_MODE)
        {
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