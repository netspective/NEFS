/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.commons.xdm;

import java.util.Set;

import com.netspective.commons.Product;
import com.netspective.commons.acl.AccessControlList;
import com.netspective.commons.acl.AccessControlLists;
import com.netspective.commons.acl.AccessControlListsManager;
import com.netspective.commons.acl.Permission;
import com.netspective.commons.acl.PermissionNotFoundException;
import com.netspective.commons.acl.Role;
import com.netspective.commons.acl.RoleNotFoundException;
import com.netspective.commons.activity.Activity;
import com.netspective.commons.activity.ActivityManager;
import com.netspective.commons.activity.ActivityObserver;
import com.netspective.commons.activity.ActivityObservers;
import com.netspective.commons.activity.basic.ActivityObserversList;
import com.netspective.commons.activity.basic.DefaultActivityObserver;
import com.netspective.commons.command.Command;
import com.netspective.commons.command.Commands;
import com.netspective.commons.config.Configuration;
import com.netspective.commons.config.Configurations;
import com.netspective.commons.config.ConfigurationsManager;
import com.netspective.commons.config.SystemProperty;
import com.netspective.commons.product.NetspectiveComponent;
import com.netspective.commons.report.Report;
import com.netspective.commons.report.Reports;
import com.netspective.commons.report.ReportsManager;
import com.netspective.commons.report.tabular.AbstractTabularReport;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.calc.ColumnDataCalculator;
import com.netspective.commons.report.tabular.calc.TabularReportCalcs;
import com.netspective.commons.script.BeanScriptPackage;
import com.netspective.commons.script.BeanScripts;
import com.netspective.commons.script.Script;
import com.netspective.commons.script.ScriptEngine;
import com.netspective.commons.script.ScriptsManager;
import com.netspective.commons.script.ScriptsNameSpace;
import com.netspective.commons.template.TemplateProcessor;
import com.netspective.commons.template.TemplateProcessorTypeTemplateConsumer;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateProducers;

public class DefaultXdmComponentItems implements TemplateProducerParent, ConfigurationsManager, AccessControlListsManager, ReportsManager, ScriptsManager, ActivityManager
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    protected static final TemplateProducers templateProducers = new TemplateProducers();

    public static final String TEMPLATEELEMNAME_TEMPLATE_PROCESSOR_TYPE = "template-processor-type";
    public static final String TEMPLATEELEMNAME_TABULAR_REPORT_COLUMN_TYPE = "tabular-report-column-type";

    private static final TemplateProcessorTypeTemplate TEMPLATE_PROCESSOR_TYPES = new TemplateProcessorTypeTemplate();
    private static final TabularReportColumnTypeTemplate TABULAR_REPORT_COLUMN_TYPES = new TabularReportColumnTypeTemplate();

    protected static class TemplateProcessorTypeTemplate extends TemplateProducer
    {
        public TemplateProcessorTypeTemplate()
        {
            super(TemplateProcessor.class.getName(), TEMPLATEELEMNAME_TEMPLATE_PROCESSOR_TYPE, "name", "extends", true, false);
        }
    }

    protected static class TabularReportColumnTypeTemplate extends TemplateProducer
    {
        public TabularReportColumnTypeTemplate()
        {
            super(TabularReportColumn.class.getName(), TEMPLATEELEMNAME_TABULAR_REPORT_COLUMN_TYPE, "name", "extends", true, false);
        }
    }

    static
    {
        NetspectiveComponent.getInstance().registerProduct(com.netspective.commons.ProductRelease.PRODUCT_RELEASE);
        TemplateCatalog.registerConsumerDefnForClass(TemplateProcessorTypeTemplateConsumer.INSTANCE, TemplateProcessor.class, true, true);
        templateProducers.add(TABULAR_REPORT_COLUMN_TYPES);
        templateProducers.add(TEMPLATE_PROCESSOR_TYPES);
    }

    public TemplateProducers getTemplateProducers()
    {
        return templateProducers;
    }

    private Product product = new XdmProduct();
    private AccessControlLists aclsManager = new AccessControlLists();
    private Configurations configsManager = new Configurations();
    private Reports reportsManager = new Reports();
    private BeanScriptPackage activeScriptsNameSpace;
    private BeanScripts scripts = new BeanScripts();
    private ActivityObservers activityObservers = new ActivityObserversList(this);

    public static TabularReportColumnTypeTemplate getTabularReportColumnTypes()
    {
        return TABULAR_REPORT_COLUMN_TYPES;
    }

    public static TemplateProcessorTypeTemplate getTemplateProcessorTypes()
    {
        return TEMPLATE_PROCESSOR_TYPES;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public Product getProduct()
    {
        return product;
    }

    public Product createProduct()
    {
        return product;
    }

    public void addProduct(Product product)
    {
        this.product = product;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public void addValueSource(ValueSource vs)
    {
        ValueSources.getInstance().registerValueSource(vs.getClass());
    }

    public void addCommand(Command command)
    {
        Commands.getInstance().registerCommand(command.getClass());
    }

    public void addTabularReportCalcType(ColumnDataCalculator calc)
    {
        TabularReportCalcs.getInstance().registerColumnDataCalc(calc.getClass());
    }

    public SystemProperty createSystemProperty()
    {
        // the system property is "registered" automatically in the construction finalization listener
        return new SystemProperty();
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public Configurations getConfigurations()
    {
        return configsManager;
    }

    public void addConfiguration(Configuration config)
    {
        getConfigurations().addConfiguration(config);
    }

    public Configuration getDefaultConfiguration()
    {
        return getConfigurations().getConfiguration();
    }

    public Configuration getConfiguration(final String name)
    {
        return getConfigurations().getConfiguration(name);
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public AccessControlLists getAccessControlLists()
    {
        return aclsManager;
    }

    public AccessControlList createAccessControlList()
    {
        return getAccessControlLists().createAccessControlList();
    }

    public void addAccessControlList(AccessControlList acl)
    {
        getAccessControlLists().addAccessControlList(acl);
    }

    public AccessControlList getDefaultAccessControlList()
    {
        return getAccessControlLists().getAccessControlList();
    }

    public AccessControlList getAccessControlList(final String name)
    {
        return getAccessControlLists().getAccessControlList(name);
    }

    public Permission getPermission(String name) throws PermissionNotFoundException
    {
        return getAccessControlLists().getPermission(name);
    }

    public Role getRole(String name) throws RoleNotFoundException
    {
        return getAccessControlLists().getRole(name);
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public ScriptEngine createScriptEngine()
    {
        return new ScriptEngine();
    }

    public void addScriptEngine(ScriptEngine scriptEngine)
    {
        scriptEngine.register();
    }

    public ScriptsNameSpace createScripts()
    {
        activeScriptsNameSpace = new BeanScriptPackage(scripts);
        return activeScriptsNameSpace;
    }

    public void addScripts(ScriptsNameSpace pkg)
    {
        activeScriptsNameSpace = null;
    }

    public Script getScript(String id)
    {
        return scripts.get(id);
    }

    public Set getScriptNames()
    {
        return scripts.getNames();
    }

    public Script createScript()
    {
        if (activeScriptsNameSpace != null)
            return activeScriptsNameSpace.createScript();
        else
            return scripts.createBeanScript();
    }

    public void addScript(Script beanScript)
    {
        scripts.add(beanScript);
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public ActivityObserver createActivityObserver()
    {
        return new DefaultActivityObserver();
    }

    public void addActivityObserver(ActivityObserver activityObserver)
    {
        activityObservers.addActivityObserver(activityObserver);
    }

    public void broadcastActivity(Activity activity)
    {
        activityObservers.observeActivity(activity);
    }

    public ActivityObservers getActivityObservers()
    {
        return activityObservers;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public Reports getReports()
    {
        return reportsManager;
    }

    public TabularReport createTabularReport()
    {
        return new AbstractTabularReport();
    }

    public void addTabularReport(TabularReport report)
    {
        getReports().add(report);
    }

    public Report getReport(String name)
    {
        return getReports().get(name);
    }
}
