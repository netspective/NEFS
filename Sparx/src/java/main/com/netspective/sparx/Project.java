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
 * $Id: Project.java,v 1.41 2003-11-24 05:27:58 aye.thu Exp $
 */

package com.netspective.sparx;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.netspective.axiom.SqlManager;
import com.netspective.axiom.ConnectionProviderEntryStatistics;
import com.netspective.axiom.connection.BasicConnectionProviderEntry;
import com.netspective.axiom.sql.QueriesNameSpace;
import com.netspective.axiom.sql.StoredProceduresNameSpace;
import com.netspective.axiom.sql.dynamic.QueryDefinition;
import com.netspective.sparx.navigate.NavigationTreesManager;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.navigate.NavigationTrees;
import com.netspective.sparx.navigate.NavigationConditionalAction;
import com.netspective.sparx.navigate.NavigationPageBodyHandler;
import com.netspective.sparx.navigate.NavigationPath;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.Themes;
import com.netspective.sparx.theme.basic.AbstractTheme;
import com.netspective.sparx.console.ConsoleManager;
import com.netspective.sparx.console.ConsoleNavigationTree;
import com.netspective.sparx.console.ConsoleServlet;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollStates;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollStatesManager;
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
import com.netspective.sparx.sql.StoredProceduresPackage;
import com.netspective.sparx.template.freemarker.FreeMarkerConfigurationAdapters;
import com.netspective.sparx.template.freemarker.FreeMarkerConfigurationAdapter;
import com.netspective.sparx.ant.AntProjects;
import com.netspective.sparx.ant.AntProject;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.security.HttpLoginManagers;
import com.netspective.sparx.security.LoginManagersManager;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateContentHandler;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XdmIdentifierConstantsGenerator;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.lang.ClassPath;
import com.netspective.commons.product.NetspectiveComponent;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.metric.Metric;
import com.netspective.commons.metric.MetricsGroup;
import com.netspective.commons.metric.CountMetric;
import com.netspective.commons.metric.FileTypeMetric;
import com.netspective.commons.metric.AverageMetric;
import com.netspective.commons.command.Commands;

/**
 * A container for all components such dialogs, fields, validation rules, conditional processing, static SQL statements,
 * dynamic queries, database schemas, schema data declarations, access control lists, and configuration files.
 * There is only one instance of a Project for each Servlet Context (application) and the components contained by the
 * Project are cached for use by all users of the application. Instead of each user having a copy of each dialog,
 * SQL statement, schema, and other components, all users (requests) of the Servlet reuse the same instances.
 */

public class Project extends SqlManager implements NavigationTreesManager, ConsoleManager, DialogsManager, XmlDataModelSchema.ConstructionFinalizeListener, LoginManagersManager
{
    public static final String TEMPLATEELEMNAME_PANEL_TYPE = "panel-type";
    public static final String TEMPLATEELEMNAME_DIALOG_TYPE = "dialog-type";
    public static final String TEMPLATEELEMNAME_DIALOG_EXECUTE_HANDLER = "dialog-execute-handler";
    public static final String TEMPLATEELEMNAME_PAGE_BODY_HANDLER = "page-body-handler";
    public static final String TEMPLATEELEMNAME_DIALOG_FIELD_TYPE = "dialog-field-type";
    public static final String TEMPLATEELEMNAME_DIALOG_FIELD_CONDITIONAL_ACTION_TYPE = "dialog-field-conditional-action";
    public static final String TEMPLATEELEMNAME_NAVIGATION_PAGE_CONDITIONAL_ACTION_TYPE = "navigation-page-conditional-action";

    private static final Log log = LogFactory.getLog(Project.class);
    private static final PanelTypeTemplate PANEL_TYPES = new PanelTypeTemplate();
    private static final DialogTypeTemplate DIALOG_TYPES = new DialogTypeTemplate();
    private static final DialogExecuteHandlerTemplate DIALOG_EXECUTE_HANDLERS = new DialogExecuteHandlerTemplate();
    private static final NavigationPageBodyHandlerTemplate PAGE_BODY_HANDLERS = new NavigationPageBodyHandlerTemplate();
    private static final DialogFieldTypeTemplate FIELD_TYPES = new DialogFieldTypeTemplate();
    private static final DialogFieldConditionalActionTemplate FIELD_CONDITIONAL_ACTIONS = new DialogFieldConditionalActionTemplate();
    private static final NavigationConditionalActionTemplate PAGE_CONDITIONAL_ACTIONS = new NavigationConditionalActionTemplate();

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

    protected static class NavigationPageBodyHandlerTemplate extends TemplateProducer
    {
        public NavigationPageBodyHandlerTemplate()
        {
            super(NavigationPageBodyHandler.class.getName(), TEMPLATEELEMNAME_PAGE_BODY_HANDLER, "name", "extends", true, false);
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
        NetspectiveComponent.getInstance().registerProduct(com.netspective.sparx.ProductRelease.PRODUCT_RELEASE);
        templateProducers.add(PANEL_TYPES);
        templateProducers.add(DIALOG_TYPES);
        templateProducers.add(DIALOG_EXECUTE_HANDLERS);
        templateProducers.add(PAGE_BODY_HANDLERS);
        templateProducers.add(FIELD_TYPES);
        templateProducers.add(FIELD_CONDITIONAL_ACTIONS);
        templateProducers.add(PAGE_CONDITIONAL_ACTIONS);

        // since we're subclassing these items, tell XDM about it so that auto documentation works on subclasses items instead of parent class items
        XML_DATA_MODEL_SCHEMA_OPTIONS.addSubclassedItemClass("query", com.netspective.sparx.sql.Query.class);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addSubclassedItemClass("queries", com.netspective.sparx.sql.QueriesPackage.class);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addSubclassedItemClass("stored-procedure", com.netspective.sparx.sql.StoredProcedure.class);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addSubclassedItemClass("stored-procedures", com.netspective.sparx.sql.StoredProceduresPackage.class);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addSubclassedItemClass("tabular-report", com.netspective.sparx.report.tabular.HtmlTabularReport.class);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addSubclassedItemClass("query-defn", com.netspective.sparx.sql.QueryDefinition.class);
    }

    private List lifecycleListeners = new ArrayList();
    private NavigationTrees navigationTrees = new NavigationTrees(this);
    private Dialogs dialogs = new Dialogs(this);
    private DialogsPackage activeDialogsNameSpace;
    private AntProjects antProjects = new AntProjects();
    private HttpLoginManagers loginManagers = new HttpLoginManagers();
    private Themes themes = new Themes();
    private ValueSource defaultDataSource;
    private HtmlTabularReportDataSourceScrollStates scrollStates = new HtmlTabularReportDataSourceScrollStatesManager();
    private Set countLinesInFileExtn = new HashSet();
    private boolean ignoreCaseInFileExtn = true;


    public Project()
    {
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public List getLifecycleListeners()
    {
        return lifecycleListeners;
    }

    public void addListener(EventListener listener)
    {
        if(listener instanceof ProjectLifecyleListener)
            lifecycleListeners.add(listener);
        else
            log.error("Unknown listener type: " + listener.getClass());
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

    public DialogTypeTemplate getDialogTypes()
    {
        return DIALOG_TYPES;
    }

    public DialogExecuteHandlerTemplate getDialogExecuteHandlers()
    {
        return DIALOG_EXECUTE_HANDLERS;
    }

    public NavigationPageBodyHandlerTemplate getPageBodyHandlers()
    {
        return PAGE_BODY_HANDLERS;
    }

    public DialogFieldTypeTemplate getFieldTypes()
    {
        return FIELD_TYPES;
    }

    public PanelTypeTemplate getPanelTypes()
    {
        return PANEL_TYPES;
    }

    public DialogFieldConditionalActionTemplate getFieldConditionalActions()
    {
        return FIELD_CONDITIONAL_ACTIONS;
    }

    public NavigationConditionalActionTemplate getPageConditionalActions()
    {
        return PAGE_CONDITIONAL_ACTIONS;
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public QueriesNameSpace createQueries()
    {
        activeNameSpace = new QueriesPackage(this, getQueries());
        return activeNameSpace;
    }

    public com.netspective.axiom.sql.Query constructQuery() // not called "create" because we don't want XDM to create tag at this level but we still need the method available
    {
        return new com.netspective.sparx.sql.Query(this);
    }

    public StoredProceduresNameSpace createStoredProcedures()
    {
        activeSPNameSpace = new StoredProceduresPackage(this, getStoredProcedures());
        return activeSPNameSpace;
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public Theme createTheme()
    {
        return new AbstractTheme();
    }

    public Themes getThemes()
    {
        return themes;
    }

    public void addTheme(Theme theme)
    {
        getThemes().registerTheme(theme);
    }

    public void setDefaultTheme(String defaultTheme)
    {
        getThemes().setDefaultTheme(defaultTheme);
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public HttpLoginManager createLoginManager()
    {
        return new HttpLoginManager(this);
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

    public AntProject createAntProject()
    {
        return new AntProject(this);
    }

    public void addAntProject(AntProject antProject)
    {
        antProjects.add(antProject);
    }

    public AntProjects getAntProjects()
    {
        return antProjects;
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public void addConnectionProviderEntryStatistics(ConnectionProviderEntryStatistics stats)
    {
        BasicConnectionProviderEntry.registerStatisticsProvider(stats);
    }

    public void addClassPathProvider(ClassPath.ClassPathProvider provider)
    {
        ClassPath.registerClassPathProvider(provider);
    }

    /* ------------------------------------------------------------------------------------------------------------ */

    public TabularReport createTabularReport()
    {
        return new BasicHtmlTabularReport();
    }

    public HtmlTabularReportDataSourceScrollStates getScrollStates()
    {
        return scrollStates;
    }

    public void addScrollStates(HtmlTabularReportDataSourceScrollStates scrollStates)
    {
        this.scrollStates = scrollStates;
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

    public NavigationTree createNavigationTree(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        return navigationTrees.createNavigationTree(cls);
    }

    public NavigationTree getDefaultNavigationTree()
    {
        return navigationTrees.getDefaultTree();
    }

    /**
     * Sets the default navigation tree for the project
     * @param name
     */
    public void setDefaultNavigationTree(String name)
    {
        navigationTrees.setDefaultTree(name);
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

    public ValueSource getDefaultDataSource()
    {
        return defaultDataSource;
    }

    public void setDefaultDataSource(ValueSource defaultDataSource)
    {
        this.defaultDataSource = defaultDataSource;
    }

    public com.netspective.axiom.sql.dynamic.QueryDefinition createQueryDefn()
    {
        return new com.netspective.sparx.sql.QueryDefinition(this);
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

    public FreeMarkerConfigurationAdapter createFreemarkerConfiguration()
    {
        return FreeMarkerConfigurationAdapters.getInstance().createConfigurationAdapter();
    }

    public void addFreemarkerConfiguration(FreeMarkerConfigurationAdapter config)
    {
        FreeMarkerConfigurationAdapters.getInstance().addConfiguration(config);
    }

    /* ------------------------------------------------------------------------------------------------------------- */
    /**
     * Generates the metrics for the different components defined within the project
     * @param parent
     */
    public void produceMetrics(Metric parent)
    {
        super.produceMetrics(parent);
        MetricsGroup mg = parent.addGroupMetric("Presentation");
        getDialogs().produceMetrics(mg);
        getThemes().produceMetrics(mg);
        getNavigationTrees().produceMetrics(mg);

        // include metrics about the value sources being used in the project
        ValueSources.getInstance().produceMetrics(parent);
        // include metrics about the commands being used in the project
        Commands.getInstance().produceMetrics(parent);

        parent.addValueMetric("Lifecycle Listeners", Integer.toString(lifecycleListeners.size()));
        parent.addValueMetric("Ant Projects", Integer.toString(antProjects.size()));
        parent.addValueMetric("Login Managers", Integer.toString(loginManagers.size()));
    }

    /**
     * Creates the various metric associated with the project files
     * @param parentMetric
     * @param path
     */
    public void createFileSystemMetrics(Metric parentMetric, File path)
    {
        Metric fsMetrics = parentMetric.addGroupMetric("Application Files");
        Metric dirMetrics = fsMetrics.addGroupMetric("Folders");
        Metric allFileMetrics = fsMetrics.addGroupMetric("Files");
        allFileMetrics.setFlag(Metric.METRICFLAG_SUM_CHILDREN);
        allFileMetrics.setFlag(Metric.METRICFLAG_SORT_CHILDREN);

        FileTypeMetric codeFileMetrics = parentMetric.addFileTypeMetric("Code Files", true);
        codeFileMetrics.setFlag(Metric.METRICFLAG_SUM_CHILDREN);
        codeFileMetrics.setFlag(Metric.METRICFLAG_SORT_CHILDREN);

        FileTypeMetric appFileMetrics = parentMetric.addFileTypeMetric("App Files", false);
        appFileMetrics.setFlag(Metric.METRICFLAG_SUM_CHILDREN);
        appFileMetrics.setFlag(Metric.METRICFLAG_SORT_CHILDREN);

        calcFileSystemMetrics(path, 1, dirMetrics, allFileMetrics, codeFileMetrics, appFileMetrics);
    }

    public void calcFileSystemMetrics(File path, int depth, Metric dirMetrics, Metric allFileMetrics, FileTypeMetric codeFileMetrics, FileTypeMetric appFileMetrics)
    {
        CountMetric totalDirsMetric = dirMetrics.addCountMetric("Total folders");
        AverageMetric avgEntriesMetric = dirMetrics.addAverageMetric("Average entries per folder");
        AverageMetric avgDepthMetric = dirMetrics.addAverageMetric("Average Depth");
        avgDepthMetric.incrementAverage(depth);

        File[] entries = path.listFiles();
        for(int i = 0; i < entries.length; i++)
        {
            File entry = entries[i];
            if(entry.isDirectory())
            {
                totalDirsMetric.incrementCount();
                File[] childEntries = entry.listFiles();
                avgEntriesMetric.incrementAverage(childEntries.length);
                calcFileSystemMetrics(entry, depth + 1, dirMetrics, allFileMetrics, codeFileMetrics, appFileMetrics);
            }
            else
            {
                String entryCaption = entry.getName();
                String entryExtension = "(no extension)";
                int extnIndex = entryCaption.lastIndexOf('.');
                if(extnIndex > -1)
                    entryExtension = entryCaption.substring(extnIndex);
                if(ignoreCaseInFileExtn)
                    entryExtension = entryExtension.toLowerCase();

                CountMetric fileMetric = allFileMetrics.addCountMetric(entryExtension);
                //fileMetric.setFlag(Metric.METRICFLAG_SHOW_PCT_OF_PARENT);
                fileMetric.incrementCount();

                if(countLinesInFileExtn.contains(entryExtension))
                {
                    FileTypeMetric ftMetric = (FileTypeMetric) codeFileMetrics.getChild(entryExtension);
                    if(ftMetric == null)
                        ftMetric = codeFileMetrics.addFileTypeMetric(entryExtension, true);
                    ftMetric.incrementCount(entry);
                }
                else
                {
                    FileTypeMetric ftMetric = (FileTypeMetric) appFileMetrics.getChild(entryExtension);
                    if(ftMetric == null)
                        ftMetric = appFileMetrics.addFileTypeMetric(entryExtension, false);
                    ftMetric.incrementCount(entry);
                }
            }
        }
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    protected class PresentationIdentifierConstantsGenerator
    {
        public static final String DELIM = ".";
        public static final char DELIM_CH = '.';
        private String rootPackage = "pres";
        private String formPackage = "pres.form";
        private String navigationPackage = "pres.navigation";

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

        public String getNavigationPackage(NavigationTree tree)
        {
            return this.navigationPackage + DELIM + tree.getName();
        }

        public String getNavigationPackage(NavigationPath path)
        {
            return getNavigationPackage(path.getOwner()) + path.getQualifiedName().replace('/', DELIM_CH);
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

        public void defineConstants(Map constants, NavigationPath navigationPath)
        {
            List children = navigationPath.getChildrenList();
            for(int i = 0; i < children.size(); i++)
            {
                NavigationPath path = (NavigationPath) children.get(i);
                if(path.getQualifiedName() != null)
                    constants.put(getNavigationPackage(path), path.getQualifiedName());
                defineConstants(constants, path);
            }
        }

        public void defineConstants(Map constants, NavigationTrees navigationTrees)
        {
            for(Iterator i = navigationTrees.getTrees().values().iterator(); i.hasNext(); )
            {
                NavigationTree tree = (NavigationTree) i.next();
                if(tree.getName().startsWith(ConsoleServlet.CONSOLE_ID))
                    continue;

                constants.put(getNavigationPackage(tree), tree.getName());
                defineConstants(constants, tree.getRoot());
            }
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
            defineConstants(constants, navigationTrees);
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
