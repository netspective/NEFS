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
package com.netspective.commons.xml.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.netspective.commons.xml.NodeIdentifiers;

public class TemplateConsumerDefn implements Cloneable
{
    private String nameSpaceId; // the namespace that identifies where the template definition will be retrieved from (NEVER use directly, always use getter)
    private String templateRefAttrName; // the name of the attribute of the element that indicates which template(s) to consume
    private String[] attrNamesToSetBeforeConsuming; // the names of the attributes to set before applying any templates
    private Set attrNamesToSetBeforeConsumingSet = new HashSet(); // the names of the attributes to set before applying any templates

    public TemplateConsumerDefn(String nameSpaceId, String templateRefAttrName, String[] setAttribsFirst)
    {
        this.nameSpaceId = nameSpaceId;
        this.templateRefAttrName = templateRefAttrName;
        this.attrNamesToSetBeforeConsuming = setAttribsFirst;
        if (setAttribsFirst != null)
        {
            for (int i = 0; i < setAttribsFirst.length; i++)
                attrNamesToSetBeforeConsumingSet.add(setAttribsFirst[i]);
        }
    }

    public boolean isTemplateAttribute(String attrName)
    {
        return attrName.equalsIgnoreCase(templateRefAttrName);
    }

    public String[] getAttrNamesToApplyBeforeConsuming()
    {
        return attrNamesToSetBeforeConsuming;
    }

    public String getTemplateRefAttrName()
    {
        return templateRefAttrName;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public String getNameSpaceId()
    {
        return nameSpaceId;
    }

    public String getAlternateClassName(TemplateContentHandler contentHandler, List templates, String elementName, Attributes attributes) throws SAXException
    {
        String alternateClassName = attributes.getValue(NodeIdentifiers.ATTRNAME_ALTERNATE_CLASS_NAME);
        if (alternateClassName != null)
            return alternateClassName;

        if (templates != null)
        {
            for (int i = 0; i < templates.size(); i++)
            {
                Template template = (Template) templates.get(i);
                String tmplAlternateClassName = template.getAlternateClassName();
                if (tmplAlternateClassName != null)
                    alternateClassName = tmplAlternateClassName;
            }
        }

        return alternateClassName;
    }

    public List getTemplatesToApply(TemplateContentHandler contentHandler, String elementName, Attributes attributes) throws SAXException
    {
        List templates = null;

        String templateNames = attributes.getValue(getTemplateRefAttrName());
        if (templateNames != null && templateNames.length() > 0)
        {
            Map consumerTemplates = contentHandler.getTemplatCatalog().getConsumerTemplates(this);
            if (consumerTemplates == null)
                throw new SAXException("Element '" + elementName + "' has no templates in namespace '" + getNameSpaceId() + "'.");

            templates = new ArrayList();
            StringTokenizer st = new StringTokenizer(templateNames, ",");
            while (st.hasMoreTokens())
            {
                String templateName = st.nextToken().trim();
                Template template = (Template) consumerTemplates.get(templateName);
                if (template == null)
                    throw new SAXException("Template '" + templateName + "' for element '" + elementName + "' in namespace '" + getNameSpaceId() + "'. was not found in the active document. Available: " + consumerTemplates.keySet());
                templates.add(template);
            }
        }

        return templates;
    }

    public Map consume(TemplateContentHandler contentHandler, List templates, String elementName, Attributes attributes) throws SAXException
    {
        Map paramValues = new HashMap();
        for (int i = 0; i < templates.size(); i++)
        {
            Template template = (Template) templates.get(i);
            TemplateApplyContext tac = template.createApplyContext(contentHandler, elementName, attributes);
            template.applyChildren(tac);
            paramValues.putAll(tac.getTemplateParamValues());
        }
        return paramValues;
    }
}

