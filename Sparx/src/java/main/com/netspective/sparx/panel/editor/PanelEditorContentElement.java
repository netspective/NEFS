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
 * @author Aye Thu
 */

/**
 * $Id: PanelEditorContentElement.java,v 1.3 2004-03-14 00:54:32 aye.thu Exp $
 */

package com.netspective.sparx.panel.editor;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.HtmlLayoutPanel;
import com.netspective.sparx.panel.HtmlPanel;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Base content element for a panel editor
 *
 */
public class PanelEditorContentElement extends HtmlLayoutPanel
{
    public static final int HIGHLIGHT_ACTIVE_ITEM = HtmlPanel.START_CUSTOM * 2;

    public static final XdmBitmaskedFlagsAttribute.FlagDefn[] FLAG_DEFNS = new XdmBitmaskedFlagsAttribute.FlagDefn[]
    {
        new XdmBitmaskedFlagsAttribute.FlagDefn(Flags.ACCESS_PRIVATE, "HIGHLIGHT_ACTIVE_ITEM", HIGHLIGHT_ACTIVE_ITEM)

    };

    public class Flags extends  XdmBitmaskedFlagsAttribute
    {
        public XdmBitmaskedFlagsAttribute.FlagDefn[] getFlagsDefns()
        {
            return FLAG_DEFNS;
        }
    }

    public static final String PANELTYPE_TEMPLATE_NAMESPACE = PanelEditorContentElement.class.getName();
    public static final String PANELTYPE_ATTRNAME_TYPE = "type";
    public static final String[] PANELTYPE_ATTRNAMES_SET_BEFORE_CONSUMING = new String[] { "name" };
    public static final PanelEditorContentElementTypeTemplateConsumerDefn elementTypeTemplateConsumer =
            new PanelEditorContentElementTypeTemplateConsumerDefn();

    static
    {
        TemplateCatalog.registerConsumerDefnForClass(elementTypeTemplateConsumer, PanelEditorContentElement.class, true, true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
    }

    protected static class PanelEditorContentElementTypeTemplateConsumerDefn extends TemplateConsumerDefn
    {
        public PanelEditorContentElementTypeTemplateConsumerDefn()
        {
            super(PANELTYPE_TEMPLATE_NAMESPACE, PANELTYPE_ATTRNAME_TYPE, PANELTYPE_ATTRNAMES_SET_BEFORE_CONSUMING);
        }

        public String getNameSpaceId()
        {
            return PanelEditorContentElement.class.getName();
        }
    }

    private String name;
    private String caption;
    private String description;
    private PanelEditor parent;
    private Flags flags = new Flags();
    /* flag to indicate if the record actions for this panel have been prepared or not */
    private boolean initialized = false;
    private List elementTypes = new ArrayList();

    public PanelEditorContentElement()
    {
        
    }

    public TemplateConsumerDefn getTemplateConsumerDefn()
    {
        return elementTypeTemplateConsumer;    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void registerTemplateConsumption(Template template)
    {
        elementTypes.add(template.getTemplateName());
    }

    public Flags getFlags()
    {
        return flags;
    }

    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

    public String getCaption()
    {
        return caption;
    }

    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public PanelEditor getParent()
    {
        return parent;
    }

    /**
     * Sets the parent panel editor
     *
     * @param parent
     */
    public void setParent(PanelEditor parent)
    {
        this.parent = parent;
    }

    public String getName()
    {
        return (name == null ? getParent().getName() : name);
    }

    /**
     * Sets the name for the content item
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    /**
     * Adds a description for the panel editor content item
     *
     * @param desc
     */
    public void addDescription(String desc)
    {
        this.description = desc;
    }

    /**
     *
     * @param writer
     * @param state
     */
    public void renderDisplayContent(Writer writer, NavigationContext nc, PanelEditorState state) throws IOException
    {

    }

    public void renderEditorContent(Writer writer, NavigationContext nc, PanelEditorState state) throws IOException
    {

    }

    public void initialize()
    {

    }

    public String appendElementInfoToActionUrl(String url, int actionMode)
    {
        url = url + "," +  getName();
        return url;
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }

    public void prepareElement(PanelEditorState state)
    {

    }
}