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
 * $Id: RecordEditorPanel.java,v 1.3 2004-03-02 07:36:06 aye.thu Exp $
 */

package com.netspective.sparx.panel;

import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.sparx.Project;
import com.netspective.sparx.command.RecordEditorCommand;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.sql.Query;
import com.netspective.sparx.sql.QueryDefinition;
import com.netspective.sparx.theme.Theme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;

public class RecordEditorPanel extends AbstractHtmlTabularReportPanel implements TemplateConsumer
{
    private static final Log log = LogFactory.getLog(RecordEditorPanel.class);

    public static final String PANEL_RECORD_EDIT_ACTION     = "Edit";
    public static final String PANEL_RECORD_ADD_ACTION      = "Add";
    public static final String PANEL_RECORD_MANAGE_ACTION  = "Add/Edit";
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
    /* default name assigned to the query defined in the editor panel */
    public static final String DEFAULT_QUERY_NAME = "records-query";

    public static final String ATTRNAME_TYPE = "record-editor-panel";
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();
    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[] { "name", "dialog" };
    private static RecordEditorPanel.RecordEditorTemplateConsumerDefn recordEditorPanelConsumer = new RecordEditorPanel.RecordEditorTemplateConsumerDefn();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        TemplateCatalog.registerConsumerDefnForClass(recordEditorPanelConsumer, RecordEditorPanel.class, true, true);
    }

    protected static class RecordEditorTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public RecordEditorTemplateConsumerDefn()
        {
            super(null, ATTRNAME_TYPE, ATTRNAMES_SET_BEFORE_CONSUMING);
        }

        public String getNameSpaceId()
        {
            return RecordEditorPanel.class.getName();
        }
    }


    private Project project;
    /* query associated with this panel */
    private Query query;
    /* query definition associated with this panel */
    private QueryDefinition queryDef;
    /* default dialog to use for record editing */
    private Dialog defaultDialog;
    /* the package that the panel belongs to */
    private RecordEditorPanelsPackage nameSpace;
    /* name of the panel */
    private String name;
    /* flag to indicate if the record actions for this panel have been prepared or not */
    private boolean actionsPrepared = false;
    /* indicates what column index of the query is used as the primary key */
    private int pkColumnIndex = 0;
    /* indicates what request parameter is required for this panel */
    private String requireRequestParam = null;

    public RecordEditorPanel(Project project)
    {
        this.project = project;
        // assign the default report skin to use
        setReportSkin(DEFAULT_EDITOR_SKIN);
    }

    /**
     *
     */
    public RecordEditorPanel(Project project, RecordEditorPanelsPackage pkg)
    {
        this(project);
        setNameSpace(pkg);
    }

    public int getPkColumnIndex()
    {
        return pkColumnIndex;
    }

    public void setPkColumnIndex(int pkColumnIndex)
    {
        this.pkColumnIndex = pkColumnIndex;
    }

    public String getRequireRequestParam()
    {
        return requireRequestParam;
    }

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
    public RecordEditorPanelsPackage getNameSpace()
    {
        return nameSpace;
    }

    /**
     * Sets the namespace (package name)
     *
     * @param nameSpace     package name
     */
    public void setNameSpace(RecordEditorPanelsPackage nameSpace)
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
     * Set the dialog name available for the record editor panel
     *
     * @param dialog
     */
    public void setDialog(String dialog)
    {
        defaultDialog = this.project.getDialog(dialog);
        if (defaultDialog == null)
            log.error("Unknown dialog '" + dialog + "'.");
    }

    public Dialog getDialog()
    {
        return defaultDialog;
    }

    /**
     * Gets the query defined for the display mode of the record manager
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
        //this.project.getQueries().add(query);
    }

    /**
     * Gets the query definition defined for the display mode of the record manager
     *
     * @return  query definition object
     */
    public QueryDefinition getQueryDef()
    {
        return queryDef;
    }

    /**
     * Sets the query definition defined for the display mode of the record manager
     *
     * @param queryDef
     */
    public void setQueryDef(QueryDefinition queryDef)
    {
        this.queryDef = queryDef;
    }

    public String getNameForMapKey()
    {
        return translateNameForMapKey(getQualifiedName());
    }

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
        String existingUrlParams = nc.getHttpRequest().getQueryString();
        if (existingUrlParams != null)
            url = url + existingUrlParams + "&";
        if (mode == EDIT_RECORD_DISPLAY_MODE)
             url = url + RecordEditorCommand.RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",edit,${" + pkColumnIndex + "}";
        else if (mode == DELETE_RECORD_DISPLAY_MODE)
            url = url + RecordEditorCommand.RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",delete,${" + pkColumnIndex + "}";
        else if (mode == ADD_RECORD_DISPLAY_MODE)
            url = url + RecordEditorCommand.RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",add";
        else if (mode == MANAGE_RECORDS_DISPLAY_MODE)
            url = url + RecordEditorCommand.RECORD_EDITOR_COMMAND_REQUEST_PARAM_NAME + "=" + this.getQualifiedName() + ",manage";
        else if (mode == DEFAULT_DISPLAY_MODE)
            url = "";
        return url;
    }

    /**
     *
     * @param nc
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

        createPanelBannerActions(nc);
        if (report.getActions() == null)
        {
            HtmlReportActions actions = new HtmlReportActions();
            HtmlReportAction editAction = actions.createAction();
            editAction.setTitle(new StaticValueSource(PANEL_RECORD_EDIT_ACTION));
            editAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, EDIT_RECORD_DISPLAY_MODE)));
            editAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_EDIT));

            HtmlReportAction deleteAction = actions.createAction();
            deleteAction.setTitle(new StaticValueSource(PANEL_RECORD_DELETE_ACTION));
            deleteAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, DELETE_RECORD_DISPLAY_MODE)));
            deleteAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_DELETE));

            actions.addAction(editAction);
            actions.addAction(deleteAction);
            report.addActions(actions);
        }
        qrp.setReportSkin(DEFAULT_EDITOR_SKIN);
        actionsPrepared = true;
    }

    /**
     *
     * @param nc
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

        HtmlPanelAction manageAction = banner.createAction();
        //addAction.setIcon(ValueSources.getInstance().createValueSourceOrStatic("servlet-context-uri:/resources/sparx/theme/sampler/images/panel/output/record-editor-add-edit.gif"));
        manageAction.setCaption(new StaticValueSource(PANEL_RECORD_MANAGE_ACTION));
        manageAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, MANAGE_RECORDS_DISPLAY_MODE)));
        actions.add(manageAction);

        HtmlPanelAction addAction = banner.createAction();
        //addAction.setIcon(ValueSources.getInstance().createValueSourceOrStatic("servlet-context-uri:/resources/sparx/theme/sampler/images/panel/output/record-editor-add-edit.gif"));
        addAction.setCaption(new StaticValueSource(PANEL_RECORD_ADD_ACTION));
        addAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, ADD_RECORD_DISPLAY_MODE)));
        actions.add(addAction);

        HtmlPanelAction doneAction = banner.createAction();
        //doneAction.setIcon(ValueSources.getInstance().createValueSourceOrStatic("servlet-context-uri:/resources/sparx/theme/sampler/images/panel/output/record-editor-done.gif"));
        doneAction.setCaption(new StaticValueSource(PANEL_RECORD_DONE_ACTION));
        doneAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, DEFAULT_DISPLAY_MODE)));
        actions.add(doneAction);

        banner.setActions(actions);
    }

    /**
     *
     * @param nc
     * @param vc
     */
    public void preparePanelActionStates(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        HtmlPanelActionStates actionStates = vc.getPanelActionStates();
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        HtmlPanelActions actions = qrp.getBanner().getActions();

        int mode = ((Integer) nc.getAttribute(DISPLAY_MODE_CONTEXT_ATTRIBUTE)).intValue();
        if (mode == DEFAULT_DISPLAY_MODE)
        {
            actionStates.getState(PANEL_RECORD_DONE_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
            actionStates.getState(PANEL_RECORD_ADD_ACTION).getStateFlags().setFlag(HtmlPanelAction.Flags.HIDDEN);
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
     * Check to see if all the actions needed for the editor panel have been ADDED. The
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
     * Renders the record editor panel
     *
     * @param nc
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
        HtmlTabularReportValueContext context = qrp.createContext(nc, theme);
        context.setPanelRenderFlags(flags);

        // process the context to calculate the states of the panel actions
        preparePanelActionStates(nc, context);
        qrp.render(writer, context, qrp.createDataSource(nc));
    }


}