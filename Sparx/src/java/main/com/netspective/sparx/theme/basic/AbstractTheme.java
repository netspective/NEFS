package com.netspective.sparx.theme.basic;

import java.util.Map;
import java.util.TreeMap;

import com.netspective.commons.io.InheritableFileResources;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.navigate.NavigationSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.panel.HtmlPanelSkin;
import com.netspective.sparx.form.DialogSkin;

public class AbstractTheme implements Theme
{
    private String name;
    private ValueSource path;
    private InheritableFileResources resources;
    private boolean defaultTheme;
    private Map navigationSkins = new TreeMap();
    private Map tabularReportSkins = new TreeMap();
    private Map panelSkins = new TreeMap();
    private Map dialogSkins = new TreeMap();
    private NavigationSkin defaultNavigationSkin;
    private HtmlPanelSkin tabbedPanelSkin;
    private HtmlPanelSkin templatePanelSkin;
    private BasicHtmlTabularReportPanelSkin defaultReportSkin = new BasicHtmlTabularReportPanelSkin(this, "panel-output", "panel/output", false);
    private StandardDialogSkin defaultDialogSkin = new StandardDialogSkin(this, "panel-input", "panel/input", false);
    private LoginDialogSkin defaulLoginDialogSkin = new LoginDialogSkin(this, "panel-input", "panel/input", false);

    public AbstractTheme()
    {
        defaultNavigationSkin = constructDefaultNavigationSkin();
        tabbedPanelSkin = constructTabbedPanelSkin();
        templatePanelSkin = constructTabbedPanelSkin();
    }

    protected NavigationSkin constructDefaultNavigationSkin()
    {
        return null;
    }

    protected HtmlPanelSkin constructTabbedPanelSkin()
    {
        return new BasicHtmlPanelSkin(this, "panel-output", "panel/output", false);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ValueSource getResourcesPath()
    {
        return path;
    }

    public void setResourcesPath(ValueSource path)
    {
        this.path = path;
    }

    public InheritableFileResources getResources(ValueContext vc)
    {
        if(resources == null)
        {
            resources = new InheritableFileResources();
            resources.setRootPath(path);
            resources.discover(vc);
        }

        return resources;
    }

    public NavigationSkin getDefaultNavigationSkin()
    {
        return defaultNavigationSkin;
    }

    public NavigationSkin getNavigationSkin(String name)
    {
        return (NavigationSkin) navigationSkins.get(name);
    }

    public HtmlTabularReportSkin createReportSkin()
    {
        return defaultReportSkin;
    }

    public DialogSkin createDialogSkin()
    {
        return defaultDialogSkin;
    }

    public HtmlPanelSkin getTabbedPanelSkin()
    {
        return tabbedPanelSkin;
    }

    public HtmlPanelSkin getTemplatePanelSkin()
    {
        return templatePanelSkin;
    }

    public HtmlPanelSkin getTemplateSkin(String name)
    {
        return (HtmlPanelSkin) panelSkins.get(name);
    }

    public HtmlTabularReportSkin getDefaultReportSkin()
    {
        return defaultReportSkin;
    }

    public HtmlTabularReportSkin getReportSkin(String name)
    {
        return (HtmlTabularReportSkin) tabularReportSkins.get(name);
    }

    public LoginDialogSkin getLoginDialogSkin()
    {
        return defaulLoginDialogSkin;
    }

    public DialogSkin getDefaultDialogSkin()
    {
        return defaultDialogSkin;
    }

    public DialogSkin getDialogSkin(String name)
    {
        return (DialogSkin) dialogSkins.get(name);
    }

    public void addDialogSkin(DialogSkin skin)
    {
        skin.setTheme(this);
        dialogSkins.put(skin.getName(), skin);
    }

    public void addNavigationSkin(NavigationSkin skin)
    {
        skin.setTheme(this);
        navigationSkins.put(skin.getName(), skin);
    }

    public void addPanelSkin(HtmlPanelSkin skin)
    {
        skin.setTheme(this);
        panelSkins.put(skin.getName(), skin);
    }

    public void addReportSkin(HtmlTabularReportSkin skin)
    {
        skin.setTheme(this);
        tabularReportSkins.put(skin.getName(), skin);

    }

    public Map getDialogSkins()
    {
        return dialogSkins;
    }

    public Map getNavigationSkins()
    {
        return navigationSkins;
    }

    public Map getPanelSkins()
    {
        return panelSkins;
    }

    public Map getReportSkins()
    {
        return tabularReportSkins;
    }

    public boolean isDefault()
    {
        return defaultTheme;
    }

    public void setDefault(boolean defaultTheme)
    {
        this.defaultTheme = defaultTheme;
    }
}
