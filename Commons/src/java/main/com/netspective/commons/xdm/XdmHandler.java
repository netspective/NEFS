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
 * $Id: XdmHandler.java,v 1.2 2003-04-23 15:41:52 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import java.util.List;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.exception.DataModelSyntaxException;
import com.netspective.commons.xml.template.TemplateApplyContext;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateConsumer;
import com.netspective.commons.xml.AbstractContentHandler;
import com.netspective.commons.xml.NodeIdentifiers;

public class XdmHandler extends AbstractContentHandler
{
    private static final Log log = LogFactory.getLog(XdmHandler.class);

    private XdmHandlerNodeStackEntry inAttrSetterTextEntry; // set to true when an element name is same as an attribute name so the element text becomes the attribute value
    private boolean unmarshallingTemplate; // set to true when a template is being unmarshalled at the end of its definition phase

    public XdmHandler(XdmParseContext pc, Object parent)
    {
        super(pc, "xdm");

        getNodeStack().push(new XdmHandlerNodeStackEntry(null, null, parent));
    }

    public String getStateText()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[Active Locator: " + getDocumentLocator().getSystemId() + " line " + getDocumentLocator().getLineNumber());
        sb.append(", NodeStack: " + getNodeStack().size());
        sb.append(" ");
        for(Iterator i = getNodeStack().iterator(); i.hasNext(); )
        {
            XdmHandlerNodeStackEntry entry = (XdmHandlerNodeStackEntry) i.next();
            sb.append(entry.getElementName() + " ");
            sb.append(entry.getInstance());
            if(i.hasNext()) sb.append(" / ");
        }
        sb.append(", Ignore stack size: " + getIgnoreStack().size());
        sb.append(", Template stack size: " + getTemplateDefnStack().size());
        sb.append(", In include: " + isInInclude());
        sb.append(", In attr setter text entry: " + inAttrSetterTextEntry);
        sb.append(", Unmarshalling template: " + unmarshallingTemplate);
        sb.append("]");

        return sb.toString();
    }

    public void text(String text) throws SAXException
    {
        if(handleDefaultText(text))
            return;

        XdmHandlerNodeStackEntry activeEntry = (XdmHandlerNodeStackEntry) getNodeStack().peek();
        try
        {
            if(inAttrSetterTextEntry != null)
            {
                if(inAttrSetterTextEntry.getAttributes().getLength() > 0)
                {
                    Exception e = new DataModelSyntaxException(((XdmParseContext) getParseContext()), "<"+ inAttrSetterTextEntry.getElementName() +"> under <"+ activeEntry.getElementName() +"> should not have attributes, they will be ignored.");
                    if(getParseContext().isThrowErrorException())
                        throw new SAXException(e);
                    else
                        getParseContext().addError(e);
                }
                activeEntry.getSchema().setAttribute(((XdmParseContext) getParseContext()), activeEntry.getInstance(), inAttrSetterTextEntry.getElementName(), text, false);
                return;
            }

            activeEntry.getSchema().addText(((XdmParseContext) getParseContext()), activeEntry.getInstance(), text);
        }
        catch (DataModelException exc)
        {
            throw new SAXException(exc.getMessage() + " " + getStateText(), exc);
        }
    }

    private boolean handleTemplateStartElement(XdmHandlerNodeStackEntry activeEntry, String url, String localName, String qName, Attributes attributes) throws SAXException
    {
        if(defaultHandleTemplateStartElement(url, localName, qName, attributes))
            return true;

        String elementName = qName.toLowerCase();

        if(activeEntry.getInstance() instanceof TemplateProducerParent)
        {
            TemplateProducers templateProducers = ((TemplateProducerParent) activeEntry.getInstance()).getTemplateProducers();
            TemplateProducer tp = templateProducers.get(elementName);
            if (tp != null)
            {
                String templateName = tp.getTemplateName(url, localName, qName, attributes);
                Template template = new Template(templateName, this, templateCatalog, tp, url, localName, qName, attributes);
                templateCatalog.registerTemplate(tp, templateName, template);
                getTemplateDefnStack().push(template);
                return true;
            }
        }

        return false;
    }

    public void startTemplateElement(TemplateApplyContext tac, String url, String localName, String qName, Attributes attributes) throws SAXException
    {
        XdmHandlerNodeStackEntry entry = (XdmHandlerNodeStackEntry) getActiveNodeEntry();
        entry.setActiveApplyContext(tac);
        startElement(url, localName, qName, attributes);
        entry.setActiveApplyContext(null);
    }

    public void registerTemplateConsumption(Template template)
    {
        XdmHandlerNodeStackEntry entry = (XdmHandlerNodeStackEntry) getActiveNodeEntry();
        if(entry.getInstance() instanceof TemplateConsumer)
            ((TemplateConsumer) entry.getInstance()).registerTemplateConsumption(template);
    }

    public void startElement(String url, String localName, String qName, Attributes attributes) throws SAXException
    {
        //System.out.println(getStackDepthPrefix() + qName + " " + getAttributeNames(attributes));

        if(handleDefaultStartElement(url, localName, qName, attributes))
            return;

        String elementName = qName.toLowerCase();
        XdmHandlerNodeStackEntry activeEntry = (XdmHandlerNodeStackEntry) getNodeStack().peek();

        if(!unmarshallingTemplate && handleTemplateStartElement(activeEntry, url, localName, qName, attributes))
            return;

        if (elementName.equals(getNodeIdentifiers().getIncludeElementName()))
        {
            try
            {
                includeInputSource(activeEntry, attributes);
                setInInclude(true);
                return;
            }
            catch (Exception e)
            {
                throw new SAXException(e);
            }
        }

        if (activeEntry.getInstance() instanceof XmlDataModelSchema.InputSourceTrackerListener)
        {
            try
            {
                ((XmlDataModelSchema.InputSourceTrackerListener) activeEntry.getInstance()).setInputSourceTracker(getParseContext().getInputFileTracker());
            }
            catch (Exception e)
            {
                throw new SAXException(e.getMessage() + " " + getStateText(), e);
            }
        }

        if (! getIgnoreStack().isEmpty() || activeEntry.getOptions().ignoreNestedElement(elementName))
        {
            getIgnoreStack().push(elementName);
        }
        else
        {
            Object childInstance = null;
            XmlDataModelSchema activeSchema = activeEntry.getSchema();
            TemplateConsumerDefn templateConsumer = null;
            List templatesToConsume = null;

            try
            {
                String alternateClassName = attributes.getValue(NodeIdentifiers.ATTRNAME_ALTERNATE_CLASS_NAME);
                childInstance = activeSchema.createElement(((XdmParseContext) getParseContext()), alternateClassName, activeEntry.getInstance(), elementName, false);

                boolean inAttrSetterText = childInstance == activeEntry.getInstance();

                if(childInstance != null && ! inAttrSetterText)
                {
                    // see if we have any templates that need to be applied
                    if(childInstance instanceof TemplateConsumer)
                    {
                        // check to see if we don't have our own alternate class but one of our the templates we're about to apply want to override our class
                        templateConsumer = ((TemplateConsumer) childInstance).getTemplateConsumerDefn();
                        templatesToConsume = templateConsumer.getTemplatesToApply(this, elementName, attributes);
                        if(alternateClassName == null)
                        {
                            alternateClassName = templateConsumer.getAlternateClassName(this, templatesToConsume, elementName, attributes);
                            if(alternateClassName != null)
                                childInstance = activeSchema.createElement(((XdmParseContext) getParseContext()), alternateClassName, activeEntry.getInstance(), elementName, false);
                        }
                    }
                }

                if (childInstance == null)
                {
                    getIgnoreStack().push(elementName);
                    return;
                }

                if(inAttrSetterText)
                {
                    inAttrSetterTextEntry = new XdmHandlerNodeStackEntry(elementName, attributes, childInstance);
                    return;
                }
            }
            catch (DataModelException e)
            {
                e.printStackTrace();
                throw new SAXException(e.getMessage() + " " + getStateText(), e);
            }

            if(childInstance != null)
            {
                XdmHandlerNodeStackEntry childEntry = null;
                try
                {
                    childEntry = new XdmHandlerNodeStackEntry(elementName, attributes, childInstance);
                    getNodeStack().push(childEntry);

                    // if we're already inside a template, then check to see if we have any attributes that need their
                    // expressions replaced
                    TemplateApplyContext activeApplyTemplateContext = activeEntry.getActiveApplyContext();
                    if(activeApplyTemplateContext != null)
                    {
                        TemplateApplyContext.StackEntry atcEntry = activeApplyTemplateContext.getActiveEntry();
                        if(atcEntry != null && atcEntry.isReplacementExprsFoundInAttrs())
                            attributes = atcEntry.getActiveTemplate().replaceAttributeExpressions(activeApplyTemplateContext);
                    }

                    // if the current element indicates the start of a template, apply it now
                    if(templateConsumer != null && templatesToConsume != null)
                    {
                        String[] attrNamesToSet = templateConsumer.getAttrNamesToApplyBeforeConsuming();
                        if(attrNamesToSet != null)
                        {
                            for(int i = 0; i < attrNamesToSet.length; i++)
                            {
                                String attrValue = attributes.getValue(attrNamesToSet[i]);
                                String lowerCaseAttrName = attrNamesToSet[i].toLowerCase();
                                childEntry.getSchema().setAttribute(((XdmParseContext) getParseContext()), childEntry.getInstance(), lowerCaseAttrName, attrValue, false);
                            }
                        }

                        templateConsumer.consume(this, templatesToConsume, elementName, attributes);
                    }

                    for (int i = 0; i < attributes.getLength(); i++)
                    {
                        String origAttrName = attributes.getQName(i);
                        String lowerCaseAttrName = origAttrName.toLowerCase();
                        if (! childEntry.getOptions().ignoreAttribute(lowerCaseAttrName) && !lowerCaseAttrName.equals(NodeIdentifiers.ATTRNAME_ALTERNATE_CLASS_NAME) &&
                            !(getNodeStack().size() < 3 && lowerCaseAttrName.startsWith(NodeIdentifiers.ATTRNAMEPREFIX_NAMESPACE_BINDING)) &&
                            !(templateConsumer != null && templateConsumer.isTemplateAttribute(origAttrName)) &&
                            !(origAttrName.startsWith(getNodeIdentifiers().getXmlNameSpacePrefix())))

                            childEntry.getSchema().setAttribute(((XdmParseContext) getParseContext()), childEntry.getInstance(), lowerCaseAttrName, attributes.getValue(i), false);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    throw new SAXException(e.getMessage() + " " + getStateText(), e);
                }

                try
                {
                    activeEntry.getSchema().storeElement(((XdmParseContext) getParseContext()), activeEntry.getInstance(), childEntry.getInstance(), elementName, false);
                }
                catch (DataModelException e)
                {
                    e.printStackTrace();
                    throw new SAXException(e.getMessage() + " " + getStateText(), e);
                }
            }
        }
    }

    public void endDocument() throws SAXException
    {
    }

    public void endElement(String url, String localName, String qName) throws SAXException
    {
        if(handleDefaultEndElement(url, localName, qName))
            return;

        if(inAttrSetterTextEntry != null)
        {
            inAttrSetterTextEntry = null;
            return;
        }

        if (!getTemplateDefnStack().isEmpty())
        {
            Object templateNode = getTemplateDefnStack().pop();

            // if, after popping the stack, the stack is empty it means we're at the root template definition element
            if(getTemplateDefnStack().isEmpty())
            {
                // now that we have finished "recording" the template, see if we're supposed to treat this template
                // as an object/instance also -- if we are, immediately apply the template at the active entry level
                Template template = (Template) templateNode;
                if(template.getTemplateProducer().isUnmarshallContents())
                {
                    unmarshallingTemplate = true;
                    XdmHandlerNodeStackEntry activeEntry = (XdmHandlerNodeStackEntry) getNodeStack().peek();
                    template.applySelfAndChildren(template.createApplyContext(this, activeEntry.getElementName(), activeEntry.getAttributes()));
                    unmarshallingTemplate = false;
                }
            }
        }
        else if (!getIgnoreStack().isEmpty())
            getIgnoreStack().pop();
        else
        {
            try
            {
                XdmHandlerNodeStackEntry activeEntry = (XdmHandlerNodeStackEntry) getNodeStack().pop();
                if (activeEntry != null)
                    activeEntry.getSchema().finalizeElementConstruction(((XdmParseContext) getParseContext()), activeEntry.getInstance(), activeEntry.getElementName());
            }
            catch (DataModelException e)
            {
                throw new SAXException(e.getMessage() + " " + getStateText(), e);
            }
        }
    }
}
