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
 * $Id: AbstractPanel.java,v 1.5 2003-12-13 17:33:32 shahid.shah Exp $
 */

package com.netspective.sparx.panel;

import java.io.IOException;
import java.io.Writer;
import java.io.StringReader;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.value.HttpServletValueContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.io.InputSourceLocator;
import com.netspective.commons.text.TextUtils;

public abstract class AbstractPanel implements HtmlPanel, TemplateConsumer
{
    public static final String PANELTYPE_TEMPLATE_NAMESPACE = HtmlPanel.class.getName();
    public static final String PANELTYPE_ATTRNAME_TYPE = "type";
    public static final String[] PANELTYPE_ATTRNAMES_SET_BEFORE_CONSUMING = null;
    public static final PanelTypeTemplateConsumerDefn templateConsumer = new PanelTypeTemplateConsumerDefn();

    static
    {
        TemplateCatalog.registerConsumerDefnForClass(templateConsumer, AbstractPanel.class, true, true);
    }

    protected static class PanelTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public PanelTypeTemplateConsumerDefn()
        {
            super(PANELTYPE_TEMPLATE_NAMESPACE, PANELTYPE_ATTRNAME_TYPE, PANELTYPE_ATTRNAMES_SET_BEFORE_CONSUMING);
        }
    }

    private static int panelNumber = 0;
    private int height = -1;
    private int width = -1;
    protected HtmlPanelFrame frame;
    protected HtmlPanelBanner banner;
    private String identifier = "AbstractPanel_" + getNextPanelNumber();
    private InputSourceLocator inputSourceLocator;
    private boolean allowViewSource;

    synchronized static private final int getNextPanelNumber()
    {
        return ++panelNumber;
    }

    public AbstractPanel()
    {
        banner = createBanner();
        frame = createFrame();
    }

    public InputSourceLocator getInputSourceLocator()
    {
        return inputSourceLocator;
    }

    public void setInputSourceLocator(InputSourceLocator inputSourceLocator)
    {
        this.inputSourceLocator = inputSourceLocator;
    }

    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        return templateConsumer;
    }

    public void registerTemplateConsumption(Template template)
    {
    }

    public String getPanelIdentifier()
    {
        return identifier;
    }

    public void setPanelIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public HtmlPanelFrame getFrame()
    {
        return frame;
    }

    public void setFrame(HtmlPanelFrame rf)
    {
        frame = rf;
    }

    public HtmlPanelBanner getBanner()
    {
        return banner;
    }

    public void setBanner(HtmlPanelBanner value)
    {
        banner = value;
    }

    public HtmlPanelFrame createFrame()
    {
        if(frame == null)
            frame = new HtmlPanelFrame();
        return frame;
    }

    public HtmlPanelBanner createBanner()
    {
        if(banner == null)
            banner = new HtmlPanelBanner();
        return banner;
    }

    public HtmlPanels getChildren()
    {
        return null;
    }

    public boolean affectsNavigationContext(NavigationContext nc)
    {
        return false;
    }

    public boolean isAllowViewSource(HttpServletValueContext vc)
    {
        return allowViewSource;
    }

    public void setAllowViewSource(boolean allowViewSource)
    {
        this.allowViewSource = allowViewSource;
    }

    public static void renderXdmObjectViewSource(Writer writer, NavigationContext nc, String objectName, Class cls, String objectId, InputSourceLocator isl) throws IOException
    {
        Theme theme = nc.getActiveTheme();
        String xmlSourceImg = theme.getResourceUrl("/images/xml-source.gif");

        writer.write("<table class='view-xml-source'>\n");
        writer.write("  <tr id='view-src-"+ objectId +"-cmd-show'><td class='cmd-view'><img src='"+ xmlSourceImg +"' border=0> <a href=\"javascript:ViewXmlSource('"+ objectId +"')\">View "+ objectName +"</a></td></tr>\n");
        writer.write("  <tr id='view-src-"+ objectId +"-cmd-hide' style='display:none'><td class='cmd-hide'><img src='"+ xmlSourceImg +"' border=0> <a href=\"javascript:ViewXmlSource('"+ objectId +"')\">Hide "+ objectName +"</a></td></tr>\n");
        writer.write("  <tr id='view-src-"+ objectId +"-location' style='display:none'><td class='location'>XML Location: <b>"+ nc.getConsoleFileBrowserLink(isl.getInputSourceTracker().getIdentifier(), true) + " " + isl.getLineNumbersText() + "</b>");
        writer.write("      <br>Java Class Instantiated: <b><code>"+ nc.getClassSourceHtml(cls, false) + "</code></b></td></tr>\n");
        writer.write("      </td></tr>\n");
        writer.write("  <tr id='view-src-"+ objectId +"-content' style='display:none'><td class='content'>\n");
        HtmlSyntaxHighlightPanel.emitHtml("xml", new StringReader(TextUtils.getUnindentedText(isl.getSourceText())), writer);
        writer.write("  </td></tr>\n");
        writer.write("</table>\n");
    }

    public static void renderPanelViewSource(Writer writer, NavigationContext nc, HtmlPanel panel) throws IOException
    {
        if(panel.isAllowViewSource(nc))
            renderXdmObjectViewSource(writer, nc, "Panel XDM Code", panel.getClass(), panel.getPanelIdentifier(), panel.getInputSourceLocator());
    }

    public void renderViewSource(Writer writer, NavigationContext nc) throws IOException
    {
        renderPanelViewSource(writer, nc, this);
    }
}
