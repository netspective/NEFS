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
 * $Id: RecordEditorPanel.java,v 1.2 2004-03-01 18:50:04 aye.thu Exp $
 */

package com.netspective.sparx.panel;

import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.sparx.Project;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlReportAction;
import com.netspective.sparx.report.tabular.HtmlReportActions;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
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

    public static final String DEFAULT_REPORT_SKIN = "";
    public static final String DEFAULT_QUERY_NAME = "records-query";

    public static final String ATTRNAME_TYPE = "record-editor-panel";
    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[] { "name", "dialog" };
    private static RecordEditorPanel.RecordEditorTemplateConsumerDefn recordEditorPanelConsumer = new RecordEditorPanel.RecordEditorTemplateConsumerDefn();

    static
    {
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
    /* */
    private boolean actionsPrepared = false;

    public RecordEditorPanel(Project project)
    {
        this.project = project;
        // assign the default report skin to use
        setReportSkin(DEFAULT_REPORT_SKIN);
    }

    /**
     *
     */
    public RecordEditorPanel(Project project, RecordEditorPanelsPackage pkg)
    {
        this(project);
        setNameSpace(pkg);
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
     * @param actionType    type of record action
     * @return              url string used to construct a redirect value source
     */
    public String generateRecordActionUrl(NavigationContext nc, int actionType)
    {
        String url = "?";
        String existingUrlParams = nc.getHttpRequest().getQueryString();
        if (existingUrlParams != null)
            url = url + existingUrlParams + "&";
        if (actionType == HtmlReportAction.Type.RECORD_EDIT)
             url = url + "cmd=record-editor-panel," + this.getQualifiedName() + ",edit,${0}";
        else if (actionType == HtmlReportAction.Type.RECORD_DELETE)
            url = url + "cmd=record-editor-panel," + this.getQualifiedName() + ",delete,${0}";

        return url;
    }

    /**
     *
     * @param nc
     */
    public void prepareRecordActionUrls(NavigationContext nc)
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
        if (report.getActions() == null)
        {
            HtmlReportActions actions = new HtmlReportActions();
            HtmlReportAction editAction = actions.createAction();
            editAction.setTitle(new StaticValueSource("Edit"));
            editAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, HtmlReportAction.Type.RECORD_EDIT)));
            editAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_EDIT));

            HtmlReportAction deleteAction = actions.createAction();
            deleteAction.setTitle(new StaticValueSource("Delete"));
            deleteAction.setRedirect(new RedirectValueSource(generateRecordActionUrl(nc, HtmlReportAction.Type.RECORD_DELETE)));
            deleteAction.setType(new HtmlReportAction.Type(HtmlReportAction.Type.RECORD_DELETE));
            actions.addAction(editAction);
            actions.addAction(deleteAction);
            report.addActions(actions);
        }
        qrp.setReportSkin("record-editor");
    }

    /**
     * Renders the record editor panel
     *
     * @param nc
     */
    public void render(Writer writer, NavigationContext nc, Theme theme, int flags) throws IOException
    {
        // handle the basic display mode first
        com.netspective.sparx.sql.Query query = (com.netspective.sparx.sql.Query) getQuery();
        if(query == null)
        {
            throw new RuntimeException("Query not found in "+ this +".");
        }
        if (!actionsPrepared)
            prepareRecordActionUrls(nc);
        QueryReportPanel qrp = getQuery().getPresentation().getDefaultPanel();
        qrp.render(writer, nc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);
    }


}