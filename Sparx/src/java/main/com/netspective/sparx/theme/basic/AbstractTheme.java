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
 * $Id: AbstractTheme.java,v 1.15 2003-08-22 03:33:44 shahid.shah Exp $
 */

package com.netspective.sparx.theme.basic;

import java.util.Map;
import java.util.TreeMap;
import java.io.IOException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.navigate.NavigationSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.panel.HtmlPanelSkin;
import com.netspective.sparx.form.DialogSkin;
import com.netspective.sparx.util.WebResourceLocator;
import com.netspective.sparx.util.WebResource;

public class AbstractTheme implements Theme
{
    private static final Log log = LogFactory.getLog(AbstractTheme.class);

    private String name;
    private WebResourceLocator resourceLocator;
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

    public WebResourceLocator getResourceLocator()
    {
        return resourceLocator;
    }

    public void setWebResourceLocator(WebResourceLocator locator)
    {
        this.resourceLocator = locator;
    }

    public String getResourceUrl(final String relativeUrl)
    {
        StringBuffer themeRelativeUrlBuf = new StringBuffer("theme/");
        themeRelativeUrlBuf.append(name);
        if(! relativeUrl.startsWith("/"))
            themeRelativeUrlBuf.append('/');
        themeRelativeUrlBuf.append(relativeUrl);
        String themeRelativeUrl = themeRelativeUrlBuf.toString();

        if(resourceLocator == null)
        {
            System.err.println("No resource locator set for theme " + this);
            return themeRelativeUrl;
        }

        try
        {
            WebResource resource = resourceLocator.findWebResource(themeRelativeUrl);
            if(resource != null)
                return resource.getUrl();
        }
        catch (IOException e)
        {
            log.error("Unable to retrieve resource information for URL " + themeRelativeUrl, e);
            return themeRelativeUrl;
        }

        if(log.isWarnEnabled()) log.warn("Resource '"+ themeRelativeUrl +"' not located in resource locator " + resourceLocator);
        return themeRelativeUrl;
    }

    public String getImageResourceUrl(final String relativeUrl)
    {
        StringBuffer imageRelativeUrlBuf = new StringBuffer("/images");
        if(! relativeUrl.startsWith("/"))
            imageRelativeUrlBuf.append('/');
        imageRelativeUrlBuf.append(relativeUrl);
        return getResourceUrl(imageRelativeUrlBuf.toString());
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
