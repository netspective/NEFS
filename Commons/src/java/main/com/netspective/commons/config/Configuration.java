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
 * $Id: Configuration.java,v 1.1 2003-03-13 18:33:10 shahid.shah Exp $
 */

package com.netspective.commons.config;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;

import com.netspective.commons.value.ValueContext;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XmlDataModelSchema;

public class Configuration extends Property implements XmlDataModelSchema.CustomElementCreator, XmlDataModelSchema.CustomElementStorer
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    protected ConfigurationsManager manager;
    private Map allProperties = new HashMap();

    public Configuration()
    {
    }

    public ConfigurationsManager getManager()
    {
        return manager;
    }

    public void setManager(ConfigurationsManager manager)
    {
        this.manager = manager;
    }

    public Property createProperty()
    {
        Property result = new Property(this, this);
        return result;
    }

    public Object createCustomDataModelElement(XdmParseContext pc, XmlDataModelSchema schema, Object parent, String elementName, String alternateClassName)
            throws InvocationTargetException, IllegalAccessException, InstantiationException
    {
        /* all custom elements are treated as properties with the element name as the property name */
        Property property = createProperty();
        property.setName(elementName);
        return property;
    }

    public void storeCustomDataModelElement(XdmParseContext pc, XmlDataModelSchema schema, Object parent, Object child, String elementName)
            throws InvocationTargetException, IllegalAccessException, InstantiationException
    {
        /* just add the property that was created in createCustomElement */
        addProperty((Property) child);
    }

    public Map getAllProperties()
    {
        return allProperties;
    }

    public void setAllProperties(Map allProperties)
    {
        this.allProperties = allProperties;
    }

    public void registerProperty(Property property)
    {
        if(property.getName() != null)
            allProperties.put(property.getName(), property);
    }

    public String getTextValue(ValueContext vc, String propertyName, String defaultValue)
    {
        Property property = (Property) allProperties.get(propertyName);
        if(property == null)
            return defaultValue;
        else
            return property.getValue(vc);
    }

    public String getTextValue(ValueContext vc, String propertyName) throws PropertyNotFoundException
    {
        Property property = (Property) allProperties.get(propertyName);
        if(property == null)
            throw new PropertyNotFoundException(this, propertyName);
        else
            return property.getValue(vc);
    }

    public String getExpression(String propertyName) throws PropertyNotFoundException
    {
        Property property = (Property) allProperties.get(propertyName);
        if(property == null)
            throw new PropertyNotFoundException(this, propertyName);
        else
            return property.getValue();
    }

    public Property findProperty(String name)
    {
        return (Property) allProperties.get(name);
    }

    public void dumpProperties()
    {
        Set set = new TreeSet(allProperties.keySet());
        for(Iterator i = set.iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            Property property = findProperty(key);

            System.out.print(key);
            System.out.print(" = ");
            System.out.println(property.getValue(null));
        }
    }
}