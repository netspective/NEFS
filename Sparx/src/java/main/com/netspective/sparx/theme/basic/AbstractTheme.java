package com.netspective.sparx.theme.basic;

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
    private NavigationSkin navigationSkin;
    private HtmlPanelSkin tabbedPanelSkin;
    private HtmlPanelSkin templatePanelSkin;
    private BasicHtmlTabularReportPanelSkin defaultReportSkin = new BasicHtmlTabularReportPanelSkin(this, "panel-output", "panel/output", false);
    private StandardDialogSkin defaultDialogSkin = new StandardDialogSkin(this, "panel-input", "panel/input", false);

    public AbstractTheme()
    {
        navigationSkin = constructNavigationSkin();
        tabbedPanelSkin = constructPanelSkin();
        templatePanelSkin = constructPanelSkin();
    }

    protected NavigationSkin constructNavigationSkin()
    {
        return null;
    }

    protected HtmlPanelSkin constructPanelSkin()
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

    public NavigationSkin getNavigationSkin()
    {
        return navigationSkin;
    }

    public HtmlPanelSkin getTabbedPanelSkin()
    {
        return tabbedPanelSkin;
    }

    public HtmlPanelSkin getTemplatePanelSkin()
    {
        return templatePanelSkin;
    }

    public HtmlTabularReportSkin getReportSkin()
    {
        return defaultReportSkin;
    }

    public HtmlTabularReportSkin getReportSkin(String name)
    {
        //TODO: fix this
        return defaultReportSkin;
    }

    public DialogSkin getDialogSkin()
    {
        return defaultDialogSkin;
    }

    public DialogSkin getDialogSkin(String name)
    {
        return defaultDialogSkin;
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
