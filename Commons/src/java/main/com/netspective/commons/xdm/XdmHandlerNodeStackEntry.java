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
 * $Id: XdmHandlerNodeStackEntry.java,v 1.1 2003-03-13 18:33:13 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import java.io.File;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.netspective.commons.xml.template.TemplateApplyContext;
import com.netspective.commons.xml.template.TemplateExpressionText;
import com.netspective.commons.xml.ContentHandlerNodeStackEntry;
import com.netspective.commons.xml.ParseContext;
import com.netspective.commons.xml.ContentHandlerException;
import com.netspective.commons.io.Resource;

public class XdmHandlerNodeStackEntry implements ContentHandlerNodeStackEntry
{
    private String elementName;
    private Attributes attributes;
    private Object instance;
    private XmlDataModelSchema schema;
    private XmlDataModelSchema.Options options;
    private TemplateApplyContext activeApplyContext;

    public XdmHandlerNodeStackEntry(String elementName, Attributes attrs, Object instance)
    {
        this.elementName = elementName;
        this.attributes = attrs;
        this.instance = instance;
        this.schema = XmlDataModelSchema.getSchema(instance.getClass());
        this.options = this.schema.getOptions();
    }

    public Object getResourceIncludeRelativeTo()
    {
        return instance.getClass().getClassLoader();
    }

    public ParseContext parseInclude(ParseContext parentPC, File srcFile) throws ContentHandlerException
    {
        try
        {
            XdmParseContext includePC = new XdmParseContext(srcFile);
            includePC.parse(getInstance());
            return includePC;
        }
        catch(Exception e)
        {
            if (e instanceof ContentHandlerException)
                throw (ContentHandlerException) e;
            else
                throw new ContentHandlerException(parentPC, e);
        }
    }

    public ParseContext parseInclude(ParseContext parentPC, Resource resource) throws ContentHandlerException
    {
        try
        {
            XdmParseContext includePC = new XdmParseContext(resource);
            includePC.parse(getInstance());
            return includePC;
        }
        catch(Exception e)
        {
            if (e instanceof ContentHandlerException)
                throw (ContentHandlerException) e;
            else
                throw new ContentHandlerException(parentPC, e);
        }
    }

    public void fillCreateApplyContextExpressionsVars(Map vars)
    {
        vars.put("ownerSchema", getSchema());
        vars.put("owner", getInstance());
    }

    public String evaluateTemplateTextExpressions(TemplateApplyContext ac, String text)
    {
        TemplateExpressionText tet = new TemplateExpressionText();
        Stack nodeStack = ac.getContentHandler().getNodeStack();
        XdmHandlerNodeStackEntry activeEntry = (XdmHandlerNodeStackEntry) nodeStack.peek();

        Map vars = ac.getExprContext().getVars();
        vars.put("thisEntry", activeEntry);
        vars.put("thisSchema", activeEntry.getSchema());
        vars.put("this", activeEntry.getInstance());

        return tet.getFinalText(ac, text);
    }

    public Attributes evaluateTemplateAttrExpressions(TemplateApplyContext ac, boolean[] attrHasExpression) throws SAXException
    {
        TemplateExpressionText tet = new TemplateExpressionText();
        Stack nodeStack = ac.getContentHandler().getNodeStack();
        XdmHandlerNodeStackEntry activeEntry = (XdmHandlerNodeStackEntry) nodeStack.peek();

        Map vars = ac.getExprContext().getVars();
        vars.put("thisEntry", activeEntry);
        vars.put("thisSchema", activeEntry.getSchema());
        vars.put("this", activeEntry.getInstance());

        AttributesImpl attrs = new AttributesImpl(attributes);
        for(int i = 0; i < attrs.getLength(); i++)
        {
            if(attrHasExpression[i])
                attrs.setValue(i, tet.getFinalText(ac, attrs.getValue(i)));
        }
        return attrs;
    }

    public Attributes getAttributes()
    {
        return attributes;
    }

    public String getElementName()
    {
        return elementName;
    }

    public Object getInstance()
    {
        return instance;
    }

    public void setInstance(Object instance)
    {
        this.instance = instance;
    }

    public XmlDataModelSchema.Options getOptions()
    {
        return options;
    }

    public void setOptions(XmlDataModelSchema.Options options)
    {
        this.options = options;
    }

    public XmlDataModelSchema getSchema()
    {
        return schema;
    }

    public void setSchema(XmlDataModelSchema schema)
    {
        this.schema = schema;
    }

    public TemplateApplyContext getActiveApplyContext()
    {
        return activeApplyContext;
    }

    public void setActiveApplyContext(TemplateApplyContext activeApplyContext)
    {
        this.activeApplyContext = activeApplyContext;
    }
}

