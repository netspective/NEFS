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
package com.netspective.sparx.panel;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

import com.netspective.commons.io.InputSourceLocator;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.value.HttpServletValueContext;

/**
 * AbstractPanel class is the base class of all panel classes that need to implement the HtmlPanel interface. This class
 * contains all the basic settings that all panels should have such as banners, frames, width and height. Also the
 * unique identifier for each panel is generated in this class.
 */
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

    /* a sequence number assigned to the panel with respect to other panels in a page */
    private static int panelNumber = 0;
    /* the height of the panel */
    private int height = -1;
    /* the width of the panel */
    private int width = -1;
    /* frame object of the panel */
    protected HtmlPanelFrame frame;
    /* banner object of the panel */
    protected HtmlPanelBanner banner;
    /* the identifier for the panel used as the html id */
    private String identifier = "AbstractPanel_" + getNextPanelNumber();
    private InputSourceLocator inputSourceLocator;
    /* flag for indicating whether  or not the XML declaration of the panel should be shown on the page */
    private boolean allowViewSource;

    /**
     * Gets the panel number to generate the unique identifier for the panel
     *
     * @return a unique panel number
     */
    synchronized static private final int getNextPanelNumber()
    {
        return ++panelNumber;
    }

    /**
     * Sole constructor.
     */
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

    /**
     * Gets the panel identifier name
     *
     * @return Unique panel identifier name used as the html ID
     */
    public String getPanelIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the panel identifier name
     *
     * @param identifier Unique panel identifier name
     */
    public void setPanelIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Gets the height of the panel
     *
     * @return panel height in pixels
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Sets the height of the panel
     *
     * @param height height in pixels
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     * Gets the width of the panel
     *
     * @return width in pixels
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Sets the width of the panel
     *
     * @param width width in pixels
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * Gets the frame asociated with the panel
     *
     * @return frame
     */
    public HtmlPanelFrame getFrame()
    {
        return frame;
    }

    /**
     * Sets the frame for the panel
     *
     * @param rf frame
     */
    public void setFrame(HtmlPanelFrame rf)
    {
        frame = rf;
    }

    /**
     * Gets the banner associated with the panel
     *
     * @return banner
     */
    public HtmlPanelBanner getBanner()
    {
        return banner;
    }

    /**
     * Sets the banner associated with the panel
     *
     * @param value banner
     */
    public void setBanner(HtmlPanelBanner value)
    {
        banner = value;
    }

    /**
     * Creates a new frame object if it doesn't exist already. If frame already exists, the method returns the
     * existing frame.
     *
     * @return frame associated with the panel
     */
    public HtmlPanelFrame createFrame()
    {
        if (frame == null)
            frame = new HtmlPanelFrame();
        return frame;
    }

    /**
     * Creates a new banner if it doesn't exist already. If banner already exists, the method returns the existing banner
     *
     * @return banner
     */
    public HtmlPanelBanner createBanner()
    {
        if (banner == null)
            banner = new HtmlPanelBanner();
        return banner;
    }

    /**
     * Gets the children panels
     *
     * @return panel children
     */
    public HtmlPanels getChildren()
    {
        return null;
    }

    /**
     * Checks to see if the panel effects the navigation context
     *
     * @param nc Navigation context
     *
     * @return False
     */
    public boolean affectsNavigationContext(NavigationContext nc)
    {
        return false;
    }

    /**
     * Checks to see if the source XML for this panel should be shown
     *
     * @param vc servlet value context
     *
     * @return true if the source is allowed to be viewed
     */
    public boolean isAllowViewSource(HttpServletValueContext vc)
    {
        return allowViewSource;
    }

    /**
     * Sets the flag for indicating if the source of this panel should be allowed to view
     *
     * @param allowViewSource view flag
     */
    public void setAllowViewSource(boolean allowViewSource)
    {
        this.allowViewSource = allowViewSource;
    }

    /**
     * Writes the HTML for displaying the source XML of this panel
     *
     * @param writer     the writer object to write the HTML to
     * @param nc         the current navigation context
     * @param objectName
     * @param cls
     * @param objectId
     * @param isl
     *
     * @throws IOException
     */
    public static void renderXdmObjectViewSource(Writer writer, NavigationContext nc, String objectName, Class cls, String objectId, InputSourceLocator isl) throws IOException
    {
        Theme theme = nc.getActiveTheme();
        String xmlSourceImg = theme.getResourceUrl("/images/xml-source.gif");

        writer.write("<table class='view-xml-source'>\n");
        writer.write("  <tr id='view-src-" + objectId + "-cmd-show'><td class='cmd-view'><img src='" + xmlSourceImg + "' border=0> <a href=\"javascript:ViewXmlSource('" + objectId + "')\">View " + objectName + "</a></td></tr>\n");
        writer.write("  <tr id='view-src-" + objectId + "-cmd-hide' style='display:none'><td class='cmd-hide'><img src='" + xmlSourceImg + "' border=0> <a href=\"javascript:ViewXmlSource('" + objectId + "')\">Hide " + objectName + "</a></td></tr>\n");
        writer.write("  <tr id='view-src-" + objectId + "-location' style='display:none'><td class='location'>XML Location: <b>" + nc.getConsoleFileBrowserLink(isl.getInputSourceTracker().getIdentifier(), true) + " " + isl.getLineNumbersText() + "</b>");
        writer.write("      <br>Java Class Instantiated: <b><code>" + nc.getClassSourceHtml(cls, false) + "</code></b></td></tr>\n");
        writer.write("      </td></tr>\n");
        writer.write("  <tr id='view-src-" + objectId + "-content' style='display:none'><td class='content'>\n");
        HtmlSyntaxHighlightPanel.emitHtml("xml", new StringReader(TextUtils.getInstance().getUnindentedText(isl.getSourceText())), writer);
        writer.write("  </td></tr>\n");
        writer.write("</table>\n");
    }

    /**
     * Writes the XML source of the panel when the allow view source flag is set to true
     *
     * @param writer the write object to write the HTML to
     * @param nc     the navigation context
     * @param panel  the panel
     *
     * @throws IOException
     */
    public static void renderPanelViewSource(Writer writer, NavigationContext nc, HtmlPanel panel) throws IOException
    {
        if (panel.isAllowViewSource(nc))
            renderXdmObjectViewSource(writer, nc, "Panel XDM Code", panel.getClass(), panel.getPanelIdentifier(), panel.getInputSourceLocator());
    }

    /**
     * Calls the static method {@link AbstractPanel#renderPanelViewSource(Writer, NavigationContext, HtmlPanel)}
     *
     * @param writer the write object to write the HTML to
     * @param nc     the navigation context
     *
     * @throws IOException
     * @see AbstractPanel#renderPanelViewSource(Writer, NavigationContext, HtmlPanel);
     */
    public void renderViewSource(Writer writer, NavigationContext nc) throws IOException
    {
        renderPanelViewSource(writer, nc, this);
    }
}
