package com.netspective.sparx.theme.basic;

import com.netspective.commons.io.InheritableFileResources;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.navigate.NavigationSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.panel.HtmlPanelSkin;
import com.netspective.commons.report.tabular.TabularReportSkin;

public class AbstractTheme implements Theme
{
    private String name;
    private ValueSource path;
    private InheritableFileResources resources;
    private boolean defaultTheme;
    private NavigationSkin navigationSkin;
    private HtmlPanelSkin panelSkin;
    private BasicHtmlTabularReportPanelSkin defaultReportSkin = new BasicHtmlTabularReportPanelSkin(this, "panel/output", false);

    public AbstractTheme()
    {
        navigationSkin = constructNavigationSkin();
        panelSkin = constructPanelSkin();
    }

    protected NavigationSkin constructNavigationSkin()
    {
        return null;
    }

    protected HtmlPanelSkin constructPanelSkin()
    {
        return new BasicHtmlPanelSkin(this, "panel/output", false);
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

    public NavigationSkin getNavigationSkin()
    {
        return navigationSkin;
    }

    public HtmlPanelSkin getPanelSkin()
    {
        return panelSkin;
    }

    public HtmlTabularReportSkin getReportSkin()
    {
        return defaultReportSkin;
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
