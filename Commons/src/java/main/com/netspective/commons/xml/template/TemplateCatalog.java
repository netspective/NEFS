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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.netspective.commons.validate.ValidationUtils;

public class TemplateCatalog
{
    private static Map templateDataByClassName = new HashMap();

    public static class TemplateConsumerData
    {
        private String nameSpaceId;
        private String templateRefAttrName;
        private TemplateConsumerDefn consumerDefn;

        public TemplateConsumerData(TemplateConsumerDefn consumerDefn)
        {
            this.consumerDefn = consumerDefn;
        }

        public TemplateConsumerData(String nameSpaceId, String templateRefAttrName)
        {
            this.nameSpaceId = nameSpaceId;
            this.templateRefAttrName = templateRefAttrName;
        }

        public TemplateConsumerDefn getConsumerDefn()
        {
            return consumerDefn;
        }

        public String getNameSpaceId()
        {
            return consumerDefn != null ? consumerDefn.getNameSpaceId() : nameSpaceId;
        }

        public String getTemplateRefAttrName()
        {
            return consumerDefn != null ? consumerDefn.getTemplateRefAttrName() : templateRefAttrName;
        }

        public Map getTemplates(TemplateCatalog templateCatalog)
        {
            if (consumerDefn != null)
                return templateCatalog.getConsumerTemplates(consumerDefn);

            Map templates = (Map) templateCatalog.getTemplatesByNameSpace().get(nameSpaceId);
            if (templates != null)
                return templates;

            String regEx = "/" + nameSpaceId + "/";

            // no templates found, see if we can match any using a regular expression
            for (Iterator i = templateCatalog.getTemplatesByNameSpace().entrySet().iterator(); i.hasNext();)
            {
                Map.Entry entry = (Map.Entry) i.next();
                String searchNameSpaceId = (String) entry.getKey();

                if (ValidationUtils.matchRegexp(searchNameSpaceId, regEx))
                    return (Map) entry.getValue();
            }

            return null;
        }
    }

    protected static void storeClassData(TemplateConsumerData data, Class cls, boolean interfaces, boolean ancestors)
    {
        templateDataByClassName.put(cls.getName(), data);
        if (interfaces)
        {
            for (int i = 0; i < cls.getInterfaces().length; i++)
            {
                templateDataByClassName.put(cls.getInterfaces()[i].getName(), data);
            }
        }
        if (ancestors)
        {
            Class superClass = cls.getSuperclass();
            while (superClass != null && superClass != Object.class)
            {
                templateDataByClassName.put(superClass.getName(), data);
                superClass = superClass.getSuperclass();
            }
        }
    }

    public static void registerConsumerDefnForClass(TemplateConsumerDefn consumerDefn, Class cls, boolean interfaces, boolean ancestors)
    {
        storeClassData(new TemplateConsumerData(consumerDefn), cls, interfaces, ancestors);
    }

    public static void registerNamespaceForClass(String nameSpaceId, String templateNameAttrName, Class cls, boolean interfaces, boolean ancestors)
    {
        storeClassData(new TemplateConsumerData(nameSpaceId, templateNameAttrName), cls, interfaces, ancestors);
    }

    /**
     * Each item's key in the map is a template producer/consumer namespace identifier. Each item's value is another
     * map which holds a catalog of all the templates for that particular producer/consumer by name.
     */
    private Map templatesByNameSpace = new HashMap();

    public Map getTemplatesByNameSpace()
    {
        return templatesByNameSpace;
    }

    /**
     * Get the sorted list of all namespaces
     *
     * @return a Set with that returns the names in alphbetical order
     */
    public Set getTemplateNameSpaceIds()
    {
        return new TreeSet(templatesByNameSpace.keySet());
    }

    /**
     * Get the sorted list of all template names for a particular namespace
     *
     * @param nameSpaceId
     *
     * @return
     */
    public Set getTemplateNames(String nameSpaceId)
    {
        Map templates = (Map) templatesByNameSpace.get(nameSpaceId);
        return new TreeSet(templates.keySet());
    }

    protected Map getProducerCatalog(TemplateProducer producer)
    {
        return (Map) templatesByNameSpace.get(producer.getNameSpaceId());
    }

    protected Map getConsumerCatalog(TemplateConsumerDefn consumer)
    {
        return (Map) templatesByNameSpace.get(consumer.getNameSpaceId());
    }

    public Map getProducerTemplates(TemplateProducer producer)
    {
        Map result = getProducerCatalog(producer);
        if (result == null)
        {
            result = new HashMap();
            templatesByNameSpace.put(producer.getNameSpaceId(), result);
        }
        return result;
    }

    public Map getConsumerTemplates(TemplateConsumerDefn consumer)
    {
        return getConsumerCatalog(consumer);
    }

    public TemplateConsumerData getTemplateConsumerDataForClass(Class cls)
    {
        return (TemplateConsumerData) templateDataByClassName.get(cls.getName());
    }

    public Template getTemplate(TemplateProducer producer, String templateName)
    {
        Map producerCatalog = getProducerCatalog(producer);
        if (producerCatalog != null)
            return (Template) producerCatalog.get(templateName);
        else
            return null;
    }

    public void registerTemplate(TemplateProducer producer, String templateName, Template template)
    {
        Map producerCatalog = getProducerTemplates(producer);
        producerCatalog.put(templateName, template);
        producer.addInstance(templateName, template);
    }
}
