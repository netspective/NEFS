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

package com.netspective.sparx.panel;

import com.netspective.axiom.sql.Query;
import com.netspective.sparx.sql.QueryDefinition;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationConditionalAction;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.Project;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumerDefn;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RecordEditorPanel extends AbstractHtmlTabularReportPanel implements TemplateConsumer
{
    public static final String DEFAULT_REPORT_SKIN = "";
    public static final String DEFAULT_QUERY_NAME = "records-query";

    public static final String ATTRNAME_TYPE = "record-editor-panel";
    public static final String[] ATTRNAMES_SET_BEFORE_CONSUMING = new String[] { "name", "dialogs" };
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
    /* list of dialogs associated with this panel for record creation/edition/deletion */
    private List dialogList = new ArrayList();
    /* default dialog to use for record editing */
    private Dialog defaultDialog;
    /* the package that the panel belongs to */
    private RecordEditorPanelsPackage nameSpace;
    /* name of the panel */
    private String name;


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
     * Sets the list of dialog names available for the record editor panel
     *
     * @param dialogs   comma separated string of dialog names
     */
    public void setDialogs(String dialogs)
    {
        StringTokenizer tokenizer = new StringTokenizer(dialogs);
        while (tokenizer.hasMoreTokens())
        {
            Dialog dialog = this.project.getDialog(tokenizer.nextToken());
            if (dialog != null)
                dialogList.add(dialog);
        }
    }

    /**
     * Gets the list of dialogs declared for record managing
     *
     * @return  a list of dialogs
     */
    public List getDialogList()
    {
        return dialogList;
    }

    /**
     * Adds a dialog to the list of dialogs
     *
     * @param dialog
     */
    public void addDialog(Dialog dialog)
    {
        dialogList.add(dialog);
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
     *
     *
     * @return
     */
    public Query createQuery()
    {
        return new com.netspective.sparx.sql.Query(project);
    }

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
}