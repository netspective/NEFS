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
 * $Id: TemplateElement.java,v 1.1 2003-03-13 18:33:14 shahid.shah Exp $
 */

package com.netspective.commons.xml.template;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.netspective.commons.text.ExpressionText;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xml.ContentHandlerNodeStackEntry;

public class TemplateElement extends TemplateNode
{
    private TemplateContentHandler defnContentHandler;
    private String defnSourceSystemId;
    private int defnSourceLineNumber;
    private String url;
    private String localName;
    private String qName;
    private Attributes attributes;
    private boolean defnFinalized;
    private boolean[] attrHasExpression;
    private int attrExpressionsCount;
    private List children = new ArrayList();

    public TemplateElement(TemplateContentHandler handler, String url, String localName, String qName, Attributes attributes)
    {
        this.defnContentHandler = handler;
        this.defnSourceSystemId = handler.getDocumentLocator().getSystemId();
        this.defnSourceLineNumber = handler.getDocumentLocator().getLineNumber();
        this.attributes = new AttributesImpl(attributes);
        this.localName = localName;
        this.qName = qName;
        this.url = url;
        this.defnFinalized = false;
        this.attrExpressionsCount = 0;
        this.attrHasExpression = null;
    }

    public TemplateContentHandler getDefnContentHandler()
    {
        return defnContentHandler;
    }

    public String getDefnSourceLocation()
    {
        return defnSourceSystemId + " line " + defnSourceLineNumber;
    }

    public String getElementName()
    {
        return qName;
    }

    public void addChild(TemplateNode child)
    {
        children.add(child);
    }

    protected boolean isAllowReplaceExpressions()
    {
        String exprsFlag = attributes.getValue(defnContentHandler.getNodeIdentifiers().getTemplateReplaceExprsAttrName());
        return exprsFlag != null && exprsFlag.length() > 0 ? TextUtils.toBoolean(exprsFlag) : true;
    }

    protected void apply(TemplateApplyContext ac) throws SAXException
    {
        TemplateContentHandler contentHandler = ac.getContentHandler();

        if(! contentHandler.isInIgnoreNode() && ac.isAllowReplaceExpressions() && isAllowReplaceExpressions())
        {
            if(! defnFinalized)
                finalizeDefinition(ac);

            if(attrExpressionsCount > 0)
            {
                ac.pushActiveTemplate(this, true);
                contentHandler.startTemplateElement(ac, url, localName, qName, attributes);
                ac.popActiveTemplate();
            }
            else
                contentHandler.startElement(url, localName, qName, attributes);

            for(int i = 0; i < children.size(); i++)
                ((TemplateNode) children.get(i)).apply(ac);

            contentHandler.endElement(url, localName, qName);
        }
        else
        {
            contentHandler.startElement(url, localName, qName, attributes);
            for(int i = 0; i < children.size(); i++)
                ((TemplateNode) children.get(i)).apply(ac);
            contentHandler.endElement(url, localName, qName);
        }
    }

    protected void finalizeDefinition(TemplateApplyContext ac) throws SAXException
    {
        defnFinalized = true;
        attrExpressionsCount = 0;
        attrHasExpression = new boolean[attributes.getLength()];
        for(int i = 0; i < attributes.getLength(); i++)
        {
            String attrValue = attributes.getValue(i);
            if(attrValue.indexOf(ExpressionText.EXPRESSION_REPLACEMENT_PREFIX) != -1)
            {
                attrHasExpression[i] = true;
                attrExpressionsCount++;
            }
        }
    }

    public Attributes replaceAttributeExpressions(TemplateApplyContext ac) throws SAXException
    {
        Stack nodeStack = ac.getContentHandler().getNodeStack();
        ContentHandlerNodeStackEntry activeEntry = (ContentHandlerNodeStackEntry) nodeStack.peek();
        return activeEntry.evaluateTemplateAttrExpressions(ac, getAttrHasExpression());
    }

    public Attributes getAttributes()
    {
        return attributes;
    }

    public List getChildren()
    {
        return children;
    }

    public boolean defnFinalized()
    {
        return defnFinalized;
    }

    public int getAttrExpressionsCount()
    {
        return attrExpressionsCount;
    }

    public boolean[] getAttrHasExpression()
    {
        return attrHasExpression;
    }
}

