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
 * $Id: AbstractContentHandler.java,v 1.2 2003-03-23 04:44:03 shahid.shah Exp $
 */

package com.netspective.commons.xml;

import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.Locator;

import com.netspective.commons.xml.template.TemplateContentHandler;
import com.netspective.commons.xml.template.TemplateCatalog;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateElement;
import com.netspective.commons.xml.template.TemplateText;
import com.netspective.commons.io.Resource;
import com.netspective.commons.io.FileTracker;

public abstract class AbstractContentHandler implements TemplateContentHandler
{
    private static final Log log = LogFactory.getLog(AbstractContentHandler.class);

    protected static final TemplateCatalog templateCatalog = new TemplateCatalog();

    private NodeIdentifiers nodeIdentifiers;
    private ParseContext parseContext;
    private Stack nodeStack = new Stack(); // stack that manages the non-ignored, non-template definition nodes
    private Stack ignoreStack = new Stack(); // stack that manages the nodes that should be ignored (will be empty if not inside an ignorable element)
    private Stack templateDefnStack = new Stack(); // stack that manages nodes for template definitions (will be empty if no template is being defined)
    private StringBuffer characters = new StringBuffer();

    private boolean inInclude; // set to true when processing inside an include element

    public AbstractContentHandler(ParseContext pc, String xmlNameSpace)
    {
        this.parseContext = pc;
        this.nodeIdentifiers = new NodeIdentifiers(xmlNameSpace);
    }

    public NodeIdentifiers getNodeIdentifiers()
    {
        return nodeIdentifiers;
    }

    public void setNodeIdentifiers(NodeIdentifiers nodeIdentifiers)
    {
        this.nodeIdentifiers = nodeIdentifiers;
    }

    public Stack getIgnoreStack()
    {
        return ignoreStack;
    }

    public Stack getTemplateDefnStack()
    {
        return templateDefnStack;
    }

    public boolean isInInclude()
    {
        return inInclude;
    }

    public void setInInclude(boolean inInclude)
    {
        this.inInclude = inInclude;
    }

    public boolean isInIgnoreNode()
    {
        return ! ignoreStack.isEmpty();
    }

    public String getStateText()
    {
        return null;
    }

    public TemplateCatalog getTemplatCatalog()
    {
        return templateCatalog;
    }

    public ContentHandlerNodeStackEntry getActiveNodeEntry()
    {
        return (ContentHandlerNodeStackEntry) getNodeStack().peek();
    }

    public Stack getNodeStack()
    {
        return nodeStack;
    }

    public ParseContext getParseContext()
    {
        return parseContext;
    }

    public void setParseContext(ParseContext parseContext)
    {
        this.parseContext = parseContext;
    }

    public Object evaluateHandlerExpression(String exprText) throws ContentHandlerException
    {
        try
        {
            Expression expression = ExpressionFactory.createExpression(exprText);
            JexlContext context = JexlHelper.createContext();
            Map vars = new HashMap();
            vars.put("handler", this);
            vars.put("parseContext", parseContext);
            vars.put("activeNode", nodeStack.peek());
            vars.put("nodeStack", nodeStack);
            return expression.evaluate(context);
        }
        catch (Exception e)
        {
            if(e instanceof ContentHandlerException)
                throw (ContentHandlerException) e;
            else
                throw new ContentHandlerException(parseContext, e);
        }
    }

    public void includeInputSource(ContentHandlerNodeStackEntry activeEntry, Attributes attrs) throws ContentHandlerException, ParserConfigurationException, SAXException, IOException, TransformProcessingInstructionEncounteredException
    {
        String resourceName = attrs.getValue(NodeIdentifiers.ATTRNAME_INCLUDE_RESOURCE);
        String templateName = attrs.getValue(NodeIdentifiers.ATTRNAME_INCLUDE_TEMPLATE);
        if (resourceName != null && resourceName.length() > 0)
        {
            Resource resource = null;
            Object relativeTo = activeEntry.getResourceIncludeRelativeTo();
            String relativeToExpr = attrs.getValue(NodeIdentifiers.ATTRNAME_INCLUDE_RESOURCE_RELATIVE_TO);
            if(relativeToExpr != null)
                relativeTo = evaluateHandlerExpression(relativeToExpr);

            if(relativeTo instanceof Class)
                resource = new Resource((Class) relativeTo, resourceName);
            else if(relativeTo instanceof ClassLoader)
                resource = new Resource((ClassLoader) relativeTo, resourceName);
            else if(relativeTo instanceof String)
            {
                try
                {
                    resource = new Resource(Class.forName((String) relativeTo), resourceName);
                }
                catch (ClassNotFoundException e)
                {
                    throw new ContentHandlerException(parseContext, "The result of '"+ NodeIdentifiers.ATTRNAME_INCLUDE_RESOURCE_RELATIVE_TO +"' attribute expression '"+ relativeTo +"' in <"+ nodeIdentifiers.getIncludeElementName()  +"> ("+ relativeToExpr +") must be either a Class or a ClassLoader.");
                }
            }
            else if(relativeToExpr != null)
                throw new ContentHandlerException(parseContext, "The result of '"+ NodeIdentifiers.ATTRNAME_INCLUDE_RESOURCE_RELATIVE_TO +"' attribute expression '"+ relativeTo +"' in <"+ nodeIdentifiers.getIncludeElementName()  +"> ("+ relativeToExpr +") must be either a Class or a ClassLoader.");
            else
                resource = new Resource(activeEntry.getClass(), resourceName);

            ParseContext includePC = activeEntry.parseInclude(parseContext, resource);
            parseContext.getErrors().addAll(includePC.getErrors());
        }
        else if(templateName != null && templateName.length() > 0)
        {
            Template template = templateCatalog.getTemplate(nodeIdentifiers.getGenericTemplateProducer(), templateName);
            if(template == null)
                throw new SAXParseException("Generic template '"+ templateName +"' was not found in the active document.", parseContext.getLocator());
            template.applyChildren(template.createApplyContext(this, nodeIdentifiers.getIncludeElementName(), attrs));
        }
        else
        {
            if (parseContext.getInputFileTracker() == null)
                throw new RuntimeException("Include files not allowed unless a source file exists.");

            String fileName = attrs.getValue(NodeIdentifiers.ATTRNAME_INCLUDE_FILE);
            FileTracker includeFile = new FileTracker();
            includeFile.setFile(parseContext.resolveFile(fileName));
            includeFile.setParent(parseContext.getInputFileTracker());

            ParseContext includePC = activeEntry.parseInclude(parseContext, includeFile.getFile());
            parseContext.getErrors().addAll(includePC.getErrors());
        }
    }

    protected boolean handleDefaultText(String text) throws SAXException
    {
        if(! templateDefnStack.isEmpty())
        {
            TemplateElement activeTemplate = (TemplateElement) templateDefnStack.peek();
            activeTemplate.addChild(new TemplateText(activeTemplate, text));
            return true;
        }
        else if(nodeStack.isEmpty() || ! ignoreStack.isEmpty())
            return true;
        else
            return false;
    }

    public void characters(char[] buf, int start, int end) throws SAXException
    {
        characters.append(buf, start, end);
    }

    public void consumeCharacters() throws SAXException
    {
        if(characters.length() > 0)
        {
            text(characters.toString());
            characters = new StringBuffer();
        }
    }

    protected boolean defaultHandleTemplateStartElement(String url, String localName, String qName, Attributes attributes) throws SAXException
    {
        String elementName = qName.toLowerCase();
        if(! templateDefnStack.isEmpty())
        {
            // we're inside a template already so just grab the contents
            TemplateElement activeTemplate = (TemplateElement) templateDefnStack.peek();
            if(nodeIdentifiers.getTemplateParamDecl().equals(elementName))
            {
                if(activeTemplate instanceof Template)
                {
                    ((Template) activeTemplate).declareParameter(this, url, localName, qName, attributes);
                    templateDefnStack.push(activeTemplate);
                }
                else
                    throw new SAXParseException("<"+ nodeIdentifiers.getTemplateParamDecl() +"> not allowed here.", parseContext.getLocator());
            }
            else
            {
                TemplateElement childNode = new TemplateElement(this, url, localName, qName, attributes);
                activeTemplate.addChild(childNode);
                templateDefnStack.push(childNode);
            }
            return true;
        }
        else if (elementName.equals(nodeIdentifiers.getTemplateElementName()))
        {
            String templateName = attributes.getValue(NodeIdentifiers.ATTRNAME_GENERIC_TEMPLATE_NAME);
            if(templateName == null || templateName.length() == 0)
                throw new SAXParseException("Template must have a '"+ NodeIdentifiers.ATTRNAME_GENERIC_TEMPLATE_NAME +"' attribute in <"+ elementName +"> ", parseContext.getLocator());

            Template template = new Template(this, templateCatalog, nodeIdentifiers.getGenericTemplateProducer(), url, localName, qName, attributes);
            templateCatalog.registerTemplate(nodeIdentifiers.getGenericTemplateProducer(), templateName, template);
            templateDefnStack.push(template);
            return true;
        }
        else
            return false;
    }

    public String getStackDepthPrefix()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 1; i < nodeStack.size(); i++)
            sb.append("  ");
        return sb.toString();
    }

    public String getAttributeNames(Attributes attributes)
    {
        StringBuffer sb = new StringBuffer(" Attrs: [");
        for(int i = 0; i < attributes.getLength(); i++)
        {
            sb.append(attributes.getQName(i));
            sb.append("=");
            sb.append(attributes.getValue(i));
            sb.append(" ");
        }
        sb.append(attributes);
        sb.append("]");
        return sb.toString();
    }

    public void endDocument() throws SAXException
    {
    }

    public boolean handleDefaultStartElement(String url, String localName, String qName, Attributes attributes) throws SAXException
    {
        consumeCharacters();

        // containers are simply holders of other elements and should not be processed
        if(qName.equals(nodeIdentifiers.getContainerElementName()))
            return true;

        return false;
    }


    public boolean handleDefaultEndElement(String url, String localName, String qName) throws SAXException
    {
        consumeCharacters();

        // containers are simply holders of other elements and should not be processed
        if(qName.equals(nodeIdentifiers.getContainerElementName()))
            return true;

        if(inInclude)
        {
            inInclude = false;
            return true;
        }

        return false;
    }

    public void endPrefixMapping(String s) throws SAXException
    {
    }

    public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException
    {
    }

    public void processingInstruction(String target, String pi) throws SAXException
    {
        if (target.equals(parseContext.getTransformInstruction()))
        {
            if (parseContext.prepareTransformInstruction(pi))
                throw new SAXException(new TransformProcessingInstructionEncounteredException());
        }
    }

    public Locator getDocumentLocator()
    {
        return parseContext.getLocator();
    }

    public void setDocumentLocator(Locator locator)
    {
        parseContext.setLocator(locator);
    }

    public void skippedEntity(String s) throws SAXException
    {
    }

    public void startDocument() throws SAXException
    {
    }

    public void startPrefixMapping(String s, String s1) throws SAXException
    {
    }

}
