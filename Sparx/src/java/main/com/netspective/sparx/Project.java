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
 * $Id: Project.java,v 1.16 2003-08-11 07:14:16 aye.thu Exp $
 */

package com.netspective.sparx;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.netspective.axiom.SqlManager;
import com.netspective.axiom.ConnectionProviderEntryStatistics;
import com.netspective.axiom.connection.BasicConnectionProviderEntry;
import com.netspective.axiom.sql.QueriesNameSpace;
import com.netspective.axiom.sql.dynamic.QueryDefinition;
import com.netspective.sparx.navigate.NavigationTreesManager;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.navigate.NavigationTrees;
import com.netspective.sparx.navigate.NavigationConditionalAction;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.Themes;
import com.netspective.sparx.theme.basic.AbstractTheme;
import com.netspective.sparx.console.ConsoleManager;
import com.netspective.sparx.console.ConsoleNavigationTree;
import com.netspective.sparx.console.ConsoleServlet;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.Dialogs;
import com.netspective.sparx.form.DialogsManager;
import com.netspective.sparx.form.handler.DialogExecuteHandler;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldConditionalAction;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.sql.QueriesPackage;
import com.netspective.sparx.template.freemarker.FreeMarkerConfigurationAdapters;
import com.netspective.sparx.template.freemarker.FreeMarkerConfigurationAdapter;
import com.netspective.sparx.ant.AntProjects;
import com.netspective.sparx.ant.AntProject;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.security.HttpLoginManagers;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateContentHandler;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XdmIdentifierConstantsGenerator;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.lang.ClassPath;

/**
 * A container for all components such dialogs, fields, validation rules, conditional processing, static SQL statements,
 * dynamic queries, database schemas, schema data declarations, access control lists, and configuration files.
 * There is only one instance of a Project for each Servlet Context (application) and the components contained by the
 * Project are cached for use by all users of the application. Instead of each user having a copy of each dialog,
 * SQL statement, schema, and other components, all users (requests) of the Servlet reuse the same instances.
 */

public class Project extends SqlManager implements NavigationTreesManager, ConsoleManager, DialogsManager, XmlDataModelSchema.ConstructionFinalizeListener
{
    public static final String TEMPLATEELEMNAME_PANEL_TYPE = "panel-type";
    public static final String TEMPLATEELEMNAME_DIALOG_TYPE = "dialog-type";
    public static final String TEMPLATEELEMNAME_DIALOG_EXECUTE_HANDLER = "dialog-execute-handler";
    public static final String TEMPLATEELEMNAME_DIALOG_FIELD_TYPE = "dialog-field-type";
    public static final String TEMPLATEELEMNAME_DIALOG_FIELD_CONDITIONAL_ACTION_TYPE = "dialog-field-conditional-action";
    public static final String TEMPLATEELEMNAME_NAVIGATION_PAGE_CONDITIONAL_ACTION_TYPE = "navigation-page-conditional-action";

    private static final Log log = LogFactory.getLog(Project.class);
    private static final PanelTypeTemplate PANEL_TYPES = new PanelTypeTemplate();
    private static final DialogTypeTemplate DIALOG_TYPES = new DialogTypeTemplate();
    private static final DialogExecuteHandlerTemplate DIALOG_EXECUTE_HANDLERS = new DialogExecuteHandlerTemplate();
    private static final DialogFieldTypeTemplate FIELD_TYPES = new DialogFieldTypeTemplate();
    private static final DialogFieldConditionalActionTemplate CONDITIONAL_ACTIONS = new DialogFieldConditionalActionTemplate();
    private static final NavigationConditionalActionTemplate NAVIGATION_PAGE_CONDITIONAL_ACTIONS = new NavigationConditionalActionTemplate();

    protected static class PanelTypeTemplate extends TemplateProducer
    {
        public PanelTypeTemplate()
        {
            super(HtmlPanel.class.getName(), TEMPLATEELEMNAME_PANEL_TYPE, "name", "extends", true, false);
        }
    }

    protected static class DialogTypeTemplate extends TemplateProducer
    {
        public DialogTypeTemplate()
        {
            super(Dialog.class.getName(), TEMPLATEELEMNAME_DIALOG_TYPE, "name", "extends", true, false);
        }
    }

    protected static class DialogExecuteHandlerTemplate extends TemplateProducer
    {
        public DialogExecuteHandlerTemplate()
        {
            super(DialogExecuteHandler.class.getName(), TEMPLATEELEMNAME_DIALOG_EXECUTE_HANDLER, "name", "extends", true, false);
        }
    }

    protected static class DialogFieldTypeTemplate extends TemplateProducer
    {
        public DialogFieldTypeTemplate()
        {
            super(DialogField.class.getName(), TEMPLATEELEMNAME_DIALOG_FIELD_TYPE, "name", "extends", true, false);
        }
    }

    protected static class DialogFieldConditionalActionTemplate extends TemplateProducer
    {
        public DialogFieldConditionalActionTemplate()
        {
            super(DialogFieldConditionalAction.class.getName(), TEMPLATEELEMNAME_DIALOG_FIELD_CONDITIONAL_ACTION_TYPE, "name", "extends", true, false);
        }
    }

    protected static class NavigationConditionalActionTemplate extends TemplateProducer
    {
        public NavigationConditionalActionTemplate()
        {
            super(NavigationConditionalAction.class.getName(), TEMPLATEELEMNAME_NAVIGATION_PAGE_CONDITIONAL_ACTION_TYPE, "name", "extends", true, false);
        }
    }

    static
    {
        templateProducers.add(PANEL_TYPES);
        templateProducers.add(DIALOG_TYPES);
        templateProducers.add(DIALOG_EXECUTE_HANDLERS);
        templateProducers.add(FIELD_TYPES);
        templateProducers.add(CONDITIONAL_ACTIONS);
        templateProducers.add(NAVIGATION_PAGE_CONDITIONAL_ACTIONS);
    }

    private NavigationTrees navigationTrees = new NavigationTrees();
    private Dialogs dialogs = new Dialogs();
    private DialogsPackage activeDialogsNameSpace;
    private AntProjects antProjects = new AntProjects();
    private HttpLoginManagers loginManagers = new HttpLoginManagers();

    public Project()
    {
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public void finalizeConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        // schemas generated dynamic templates so lets "run" them now before we're done with the rest of the project
        TemplateContentHandler handler = (TemplateContentHandler) pc.getParser().getContentHandler();
        try
        {
            handler.executeDynamicTemplates();
        }
        catch (SAXException e)
        {
            throw new DataModelException(pc, e);
        }
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public DialogFieldConditionalActionTemplate getConditionalActions()
    {
        return CONDITIONAL_ACTIONS;
    }

    public DialogTypeTemplate getDialogTypes()
    {
        return DIALOG_TYPES;
    }

    public static DialogExecuteHandlerTemplate getDialogExecuteHandlers()
    {
        return DIALOG_EXECUTE_HANDLERS;
    }

    public DialogFieldTypeTemplate getFieldTypes()
    {
        return FIELD_TYPES;
    }

    public PanelTypeTemplate getPanelTypes()
    {
        return PANEL_TYPES;
    }

    public NavigationConditionalActionTemplate getNavigationPageConditionalActions()
    {
        return NAVIGATION_PAGE_CONDITIONAL_ACTIONS;
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public QueriesNameSpace createQueries()
    {
        activeNameSpace = new QueriesPackage(getQueries());
        return activeNameSpace;
    }

    public com.netspective.axiom.sql.Query createQuery()
    {
        return new com.netspective.sparx.sql.Query();
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public Theme createRegisterTheme()
    {
        return new AbstractTheme();
    }

    public void addRegisterTheme(Theme theme)
    {
        Themes.getInstance().registerTheme(theme);
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public HttpLoginManager createLoginManager()
    {
        return new HttpLoginManager();
    }

    public void addLoginManager(HttpLoginManager loginManager)
    {
        loginManagers.addLoginManager(loginManager);
    }

    public HttpLoginManagers getLoginManagers()
    {
        return loginManagers;
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public AntProject createRegisterAntProject()
    {
        return new AntProject();
    }

    public void addRegisterAntProject(AntProject antProject)
    {
        antProjects.add(antProject);
    }

    public AntProjects getAntProjects()
    {
        return antProjects;
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public void addRegisterConnectionProviderEntryStatistics(ConnectionProviderEntryStatistics stats)
    {
        BasicConnectionProviderEntry.registerStatisticsProvider(stats);
    }

    public void addRegisterClassPathProvider(ClassPath.ClassPathProvider provider)
    {
        ClassPath.registerClassPathProvider(provider);
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public TabularReport createTabularReport()
    {
        return new BasicHtmlTabularReport();
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public ConsoleNavigationTree getConsoleNavigationTree()
    {
        return (ConsoleNavigationTree) getNavigationTree(ConsoleServlet.CONSOLE_ID);
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public void addNavigationTree(NavigationTree tree)
    {
        navigationTrees.addNavigationTree(tree);
    }

    public NavigationTree createNavigationTree()
    {
        return navigationTrees.createNavigationTree();
    }

    public NavigationTree getDefaultNavigationTree()
    {
        return navigationTrees.getDefaultTree();
    }

    public NavigationTree getNavigationTree(String name)
    {
        return navigationTrees.getNavigationTree(name);
    }

    public NavigationTrees getNavigationTrees()
    {
        return navigationTrees;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public Dialogs getDialogs()
    {
        return dialogs;
    }

    public Dialog getDialog(final String name)
    {
        String actualName = Dialog.translateNameForMapKey(name);
        Dialog dialog = dialogs.get(actualName);

        if(dialog == null && log.isDebugEnabled())
        {
            log.debug("Unable to find dialog '"+ name +"' as '"+ actualName +"'. Available: " + dialogs);
            return null;
        }
        return dialog;
    }

    public Dialog createDialog()
    {
        return new Dialog();
    }

    public void addDialog(Dialog query)
    {
        dialogs.add(query);
    }

    public DialogsPackage createDialogs()
    {
        activeDialogsNameSpace = new DialogsPackage(getDialogs());
        return activeDialogsNameSpace;
    }

    public void addDialogs(DialogsPackage pkg)
    {
        activeDialogsNameSpace = null;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public com.netspective.axiom.sql.dynamic.QueryDefinition createQueryDefn()
    {
        return new com.netspective.sparx.sql.QueryDefinition();
    }

    public QueryDefinition getQueryDefinition(String name)
    {
        return super.getQueryDefinition(name);
    }
    /* ------------------------------------------------------------------------------------------------------------- */

    public FreeMarkerConfigurationAdapter getFreemarkerConfiguration(String name)
    {
        return FreeMarkerConfigurationAdapters.getInstance().getConfiguration(name);
    }

    public FreeMarkerConfigurationAdapter createRegisterFreemarkerConfiguration()
    {
        return FreeMarkerConfigurationAdapters.getInstance().createConfigurationAdapter();
    }

    public void addRegisterFreemarkerConfiguration(FreeMarkerConfigurationAdapter config)
    {
        FreeMarkerConfigurationAdapters.getInstance().addConfiguration(config);
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    protected class PresentationIdentifierConstantsGenerator
    {
        public static final String DELIM = ".";
        private String rootPackage = "pres";
        private String formPackage = "pres.form";

        public PresentationIdentifierConstantsGenerator()
        {
        }

        public PresentationIdentifierConstantsGenerator(String root, String formPackage)
        {
            this.rootPackage = root;
            this.formPackage = formPackage;
        }

        public String getFormPackage(Dialog dialog)
        {
            return this.formPackage + DELIM + dialog.getQualifiedName();
        }

        public String getFormPackage(DialogField dialogField)
        {
            return getFormPackage(dialogField.getOwner()) + DELIM + dialogField.getQualifiedName();
        }

        public void setFormPackage(String queries)
        {
            this.formPackage = queries;
        }

        public String getRootPackage()
        {
            return rootPackage;
        }

        public void setRootPackage(String rootPackage)
        {
            this.rootPackage = rootPackage;
        }

        public void defineConstants(Map constants, Dialogs dialogs)
        {
            for(int i = 0; i < dialogs.size(); i++)
            {
                Dialog dialog = dialogs.get(i);
                if(dialog.getQualifiedName().startsWith(ConsoleServlet.CONSOLE_ID))
                    continue;

                constants.put(getFormPackage(dialog), dialog.getQualifiedName());
                DialogFields fields = dialog.getFields();
                for(int j = 0; j < fields.size(); j++)
                {
                    DialogField field = fields.get(j);
                    if(field.getQualifiedName() != null)
                        constants.put(getFormPackage(field), field.getQualifiedName());
                }
            }
        }

        public Map createConstants()
        {
            Map constants = new HashMap();
            defineConstants(constants, dialogs);
            return constants;
        }
    }

    protected PresentationIdentifierConstantsGenerator getPresentationIdentifiersConstantsDecls()
    {
        return new PresentationIdentifierConstantsGenerator();
    }

    public void generateIdentifiersConstants(File rootPath, String rootPkgAndClassName) throws IOException
    {
        super.generateIdentifiersConstants(rootPath, rootPkgAndClassName);

        XdmIdentifierConstantsGenerator xicg =
                new XdmIdentifierConstantsGenerator(rootPath,
                                                    rootPkgAndClassName,
                                                    getPresentationIdentifiersConstantsDecls().createConstants());
        xicg.generateCode();
    }
}
