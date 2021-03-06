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
package com.netspective.sparx.panel.editor;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.HtmlLayoutPanel;
import com.netspective.sparx.panel.HtmlPanel;

/**
 * Base content element for a panel editor
 */
public class PanelEditorContentElement extends HtmlLayoutPanel
{
    public static final int HIGHLIGHT_ACTIVE_ITEM = HtmlPanel.START_CUSTOM * 2;

    public static final XdmBitmaskedFlagsAttribute.FlagDefn[] FLAG_DEFNS = new XdmBitmaskedFlagsAttribute.FlagDefn[]
    {
        new XdmBitmaskedFlagsAttribute.FlagDefn(Flags.ACCESS_PRIVATE, "HIGHLIGHT_ACTIVE_ITEM", HIGHLIGHT_ACTIVE_ITEM)

    };

    public class Flags extends XdmBitmaskedFlagsAttribute
    {
        public XdmBitmaskedFlagsAttribute.FlagDefn[] getFlagsDefns()
        {
            return FLAG_DEFNS;
        }
    }

    public static final String PANELTYPE_TEMPLATE_NAMESPACE = PanelEditorContentElement.class.getName();
    public static final String PANELTYPE_ATTRNAME_TYPE = "type";
    public static final String[] PANELTYPE_ATTRNAMES_SET_BEFORE_CONSUMING = new String[]{"name"};
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

    /**
     * Child class to record the state of the child element of a panel editor. This state object is populated by the child
     * element to inform the parent panel editor of its state.
     */
    public class PanelEditorContentState
    {
        private boolean emptyContent;
        private String elementName;

        public PanelEditorContentState(String elementName)
        {
            this.elementName = elementName;
        }

        public boolean isEmptyContent()
        {
            return emptyContent;
        }

        public void setEmptyContent(boolean emptyContent)
        {
            this.emptyContent = emptyContent;
        }

        public String getElementName()
        {
            return elementName;
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
        return elementTypeTemplateConsumer;
    }

    public void registerTemplateConsumption(Template template)
    {
        elementTypes.add(template.getTemplateName());
    }

    /**
     * Constructs a new state object
     */
    public PanelEditorContentState constructStateObject()
    {
        return new PanelEditorContentState(getName());
    }

    /**
     * Gets the flags
     */
    public Flags getFlags()
    {
        return flags;
    }

    /**
     * Sets the flags
     *
     * @param flags Flags set for the content element
     */
    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

    /**
     * Gets the caption for the content element. Mainly used for displaying ADD action items.
     */
    public String getCaption()
    {
        return caption;
    }

    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    /**
     * Gets the parent panel editor.
     *
     * @return Panel editor parent
     */
    public PanelEditor getParent()
    {
        return parent;
    }

    /**
     * Sets the parent panel editor
     */
    public void setParent(PanelEditor parent)
    {
        this.parent = parent;
    }

    /**
     * Gets the name of the content element
     */
    public String getName()
    {
        return (name == null ? getParent().getName() : name);
    }

    /**
     * Sets the name for the content element
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the description for the content element. The description is used only for documentation purposes.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Adds a description for the panel editor content item
     */
    public void addDescription(String desc)
    {
        this.description = desc;
    }

    /**
     * @param writer
     * @param state
     */
    public void renderDisplayContent(Writer writer, NavigationContext nc, PanelEditorState state) throws IOException
    {

    }

    public void renderEditorContent(Writer writer, NavigationContext nc, PanelEditorState state) throws IOException
    {


    }

    public void addStateToParentState(PanelEditorState state)
    {

    }

    /**
     * Initialize the content element.
     */
    public void initialize()
    {
        setInitialized(true);
    }

    /**
     * Adds information specific to the content element using a comma separated token. This information can only
     * be "understood" by the content element itself.
     *
     * @param url        URL generated by the parent panel editor
     * @param actionMode the current mode of the panel editor
     */
    public String appendElementInfoToActionUrl(String url, int actionMode)
    {
        url = url + "," + getName();
        return url;
    }

    /**
     * Checks to see if the content element has been initilized or not.
     *
     * @return True if the element has been initialized
     */
    public boolean isInitialized()
    {
        return initialized;
    }

    /**
     * Sets the initialized flag for the content element
     */
    public void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }

    /**
     * Prepares the content element for request specific execution
     *
     * @param state current state of the panel editor for the request
     */
    public void prepareElement(PanelEditorState state)
    {

    }
}