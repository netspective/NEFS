package com.netspective.sparx.theme.basic;

import com.netspective.commons.io.InheritableFileResources;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.navigate.NavigationSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.commons.report.tabular.TabularReportSkin;

/**
 * The Theme class is used to save theme related information such as name, style, and location.
 *
 * @author Aye Thu
 * Created on Feb 16, 2003 10:32:51 PM
 */
public class AbstractTheme implements Theme
{
    private String name;
    private ValueSource path;
    private InheritableFileResources resources;
    private boolean defaultTheme;
    private NavigationSkin navigationSkin = new BasicTabbedNavigationSkin(this);
    private BasicHtmlTabularReportPanelSkin defaultReportSkin = new BasicHtmlTabularReportPanelSkin(this, "panel/output", false);

    public AbstractTheme()
    {
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
