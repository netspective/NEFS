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
 * $Id: TemplateProducer.java,v 1.4 2003-06-23 16:44:55 shahid.shah Exp $
 */

package com.netspective.commons.xml.template;

import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class defines elements that can be produce templates. Elements declared as templates are defined the
 * same ways a <xdm:template> element except they can have any element name and can specify an attribute which will
 * specify the template name. Then, later, elements can define consumers of the particular template as well. Unlike
 * <xdm:template> tags, each elementName will have it's own templates namespace.
 */
public class TemplateProducer
{
    private String nameSpaceId; // the namespace that identifies where the template definition will be stored
    private String elementName; // the name of the element that will define a template
    private String templateNameAttrName; // the name of the attribute of the element that specifies the template name
    private String templateInhAttrName; // the name of the attribute of the element that specifies what other templates to inherit
    private boolean unmarshallContents; // true to treat this producer as both a template and a instance/object of the data model, false to only treat as template and not unmarshall to actual data
    private boolean isStatic; // true if the producer is not instance-dependent
    private List instances = new ArrayList(); // the templates defined for this producer (in order that they were defined)
    private Map instancesMap = new TreeMap(); // the templates defined for this producer (indexed by the sorted name attribute)

    public TemplateProducer(String nameSpaceId, String elementName, String templateNameAttrName, String templateInhAttrName, boolean isStatic, boolean unmarshallContents)
    {
        this.elementName = elementName;
        this.nameSpaceId = nameSpaceId;
        this.templateNameAttrName = templateNameAttrName;
        this.templateInhAttrName = templateInhAttrName;
        this.isStatic = isStatic;
        this.unmarshallContents = unmarshallContents;
    }

    public String getTemplateName(String url, String localName, String qName, Attributes attributes) throws SAXException
    {
        String templateName = attributes.getValue(getTemplateNameAttrName());
        if(templateName == null || templateName.length() == 0)
            throw new SAXException("Template must have a '"+ getTemplateNameAttrName() +"' attribute in <"+ elementName +"> ");
        return templateName;
    }

    public String getElementName()
    {
        return elementName;
    }

    public String getNameSpaceId()
    {
        return nameSpaceId;
    }

    public String getTemplateNameAttrName()
    {
        return templateNameAttrName;
    }

    public String getTemplateInhAttrName()
    {
        return templateInhAttrName;
    }

    public boolean isStatic()
    {
        return isStatic;
    }

    public boolean isUnmarshallContents()
    {
        return unmarshallContents;
    }

    public List getInstances()
    {
        return instances;
    }

    public Map getInstancesMap()
    {
        return instancesMap;
    }

    public void addInstance(String templateName, Template template)
    {
        instances.add(template);
        instancesMap.put(templateName, template);
    }
}

