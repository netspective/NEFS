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
 * $Id: BasicAttributes.java,v 1.2 2004-08-14 21:16:32 shahid.shah Exp $
 */

package com.netspective.commons.attr;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.xdm.XmlDataModelSchema;

public class BasicAttributes implements Attributes, MutableAttributes, java.io.Serializable
{
    private static final Log log = LogFactory.getLog(BasicAttributes.class);
    private static final String MAPKEY_ATTR_CLASS = "attribute-class";
    private static final String MAPKEY_ATTR_NAME = "attribute-name";
    private static final String MAPKEY_ATTR_VALUE = "attribute-value";

    private Map attributes = new AttributesMap();
    private XmlDataModelSchema schema = XmlDataModelSchema.getSchema(getClass());
    private boolean observing = true;
    private List observers = new ArrayList();
    private boolean serializableRequired;

    public BasicAttributes()
    {
    }

    public BasicAttributes(boolean serializableRequired)
    {
        this.serializableRequired = serializableRequired;
    }

    public boolean isSerializableRequired()
    {
        return serializableRequired;
    }

    public void setSerializableRequired(boolean serializableRequired)
    {
        this.serializableRequired = serializableRequired;
    }

    public void addMutationObserver(AttributeMutationObserver observer)
    {
        observers.add(observer);
    }

    public Attribute getAttribute(String name)
    {
        return (Attribute) attributes.get(name);
    }

    public Collection getAttributes(String name)
    {
        return (Collection) attributes.get(name);
    }

    public boolean isAttributeList(String name)
    {
        return attributes.get(name) instanceof Collection;
    }

    public String getAttributeValue(String name, String defaultValue)
    {
        try
        {
            TextAttribute result = (TextAttribute) getAttribute(name);
            return result != null ? result.getAttributeValue() : defaultValue;
        }
        catch (ClassCastException e)
        {
            return defaultValue;
        }
    }

    public int getAttributeValue(String name, int defaultValue)
    {
        try
        {
            IntegerAttribute result = (IntegerAttribute) getAttribute(name);
            return result != null ? result.getAttributeValue() : defaultValue;
        }
        catch (ClassCastException e)
        {
            return defaultValue;
        }
    }

    public Map getAttributesMap()
    {
        return attributes;
    }

    public Attribute createAttribute(String key, String value)
    {
        try
        {
            int intValue = Integer.parseInt(value);
            return new BasicIntegerAttribute(this, key, intValue);
        }
        catch (NumberFormatException e)
        {
            return new BasicTextAttribute(this, key, value);
        }
    }

    public synchronized Attribute createAttribute(final Map attrMap)
    {
        Map attrMapCopy = new HashMap(attrMap);

        Object cls = attrMapCopy.get(MAPKEY_ATTR_CLASS);
        Object attrName = attrMapCopy.get(MAPKEY_ATTR_NAME);
        Object attrValue = attrMapCopy.get(MAPKEY_ATTR_VALUE);
        String textValue = attrValue != null ? attrValue.toString() : null;

        if(attrName == null)
        {
            log.error("Unable to create attribute. No attr name provided as '" + MAPKEY_ATTR_NAME + "' key: " + attrMap);
            return null;
        }

        Attribute result = null;
        final String keyText = attrName.toString();
        if(cls != null && cls.toString().length() > 0)
        {
            try
            {
                Class prefClass = Class.forName(cls.toString());
                attrMapCopy.remove(MAPKEY_ATTR_CLASS);

                // find a constructor that can take an owner, a key, and text value
                Constructor c = prefClass.getConstructor(new Class[] { Attributes.class, String.class, String.class });
                if(c == null)
                {
                    // find a constructor that can take an owner, a key
                    c = prefClass.getConstructor(new Class[] { Attributes.class, String.class });
                    if(c == null)
                    {
                        c = prefClass.getConstructor(new Class[] { Attributes.class });
                        if(c != null)
                            result = (Attribute) c.newInstance(new Object[] { this });
                        else
                            result = new ExceptionAttribute(this, keyText, textValue, new RuntimeException("No valid constructor found for Preference " + prefClass));
                    }
                    else
                    {
                        result = (Attribute) c.newInstance(new Object[] { this, attrName });
                        attrMapCopy.remove(MAPKEY_ATTR_NAME); // key is set, don't let XDM set it again below
                    }
                }
                else
                {
                    result = (Attribute) c.newInstance(new Object[] { this, attrName, attrValue });
                    attrMapCopy.remove(MAPKEY_ATTR_NAME);  // key is set, don't let XDM set it again below
                    attrMapCopy.remove(MAPKEY_ATTR_VALUE);  // value is set, don't let XDM set it again below
                }
            }
            catch (ClassNotFoundException e)
            {
                log.error("Unable to find Preference " + cls, e);
                result = new ExceptionAttribute(this, keyText, textValue, e);
            }
            catch (Exception e)
            {
                log.error("Unable to create Preference " + cls, e);
                result = new ExceptionAttribute(this, keyText, textValue, e);
            }
        }
        else
            result = new BasicTextAttribute(this, keyText, textValue);

        try
        {
            XmlDataModelSchema schema = XmlDataModelSchema.getSchema(result.getClass());
            schema.assignMapValues(result, attrMapCopy, "*");
        }
        catch (Exception e)
        {
            log.error("Error assigning Preference " + cls, e);
            result = new ExceptionAttribute(this, keyText, textValue, e);
        }

        return result;
    }

    public boolean isObserving()
    {
        return observing;
    }

    public void setObserving(boolean observing)
    {
        this.observing = observing;
    }

    public Attribute addAttribute(Attribute attribute)
    {
        if (serializableRequired && !(attribute instanceof java.io.Serializable))
        {
            log.error("A Non-Serializable attribute "+  attribute.getClass() + " was added to " +
                      this.getClass() + " object. \n" +
                      "The process of serializing the session would fail if the server is configured for clustering or persistant sessions.\n" +
                      "Make sure any attribute added to the AuthenticatedUser object implements Serializable and that it either implements the \n" +
                      "serialization of its members or its members implement Serializable as well.");
        }

        if(attribute.isAllowMultiple())
        {
            Collection coll = (Collection) attributes.get(attribute.getAttributeName());
            if(coll == null)
            {
                coll = ((MutableAttribute) attribute).createMultiAttributeCollection();
                attributes.put(attribute.getAttributeName(), coll);
            }
            coll.add(attribute);
        }
        else
            attributes.put(attribute.getAttributeName(), attribute);

        observeAttributeAdd(attribute);
        return attribute;
    }

    public void removeAttribute(Attribute attribute)
    {
        if(attribute.isAllowMultiple())
        {
            Collection coll = (Collection) attributes.get(attribute.getAttributeName());
            if(coll != null)
                coll.remove(attribute);
        }
        else
            attributes.put(attribute.getAttributeName(), attribute);

        observeAttributeRemove(attribute);
    }

    public void observeAttributeAdd(Attribute attribute)
    {
        if(isObserving())
        {
            for(int i = 0; i < observers.size(); i++)
                ((AttributeMutationObserver) observers.get(i)).observeAttributeAdd(this, attribute);
        }
    }

    public void observeAttributeChange(Attribute attribute)
    {
        if(isObserving())
        {
            for(int i = 0; i < observers.size(); i++)
                ((AttributeMutationObserver) observers.get(i)).observeAttributeChange(this, attribute);
        }
    }

    public void observeAttributeRemove(Attribute attribute)
    {
        if(isObserving())
        {
            for(int i = 0; i < observers.size(); i++)
                ((AttributeMutationObserver) observers.get(i)).observeAttributeRemove(this, attribute);
        }
    }

    protected class AttributesMap implements Map, java.io.Serializable
    {
        private Map wrappedMap = new HashMap();

        public AttributesMap()
        {
        }

        public int hashCode()
        {
            return wrappedMap.hashCode();
        }

        public int size()
        {
            return wrappedMap.size();
        }

        public void clear()
        {
            wrappedMap.clear();
        }

        public boolean isEmpty()
        {
            return wrappedMap.isEmpty();
        }

        public boolean containsKey(Object key)
        {
            return wrappedMap.containsKey(key);
        }

        public boolean containsValue(Object value)
        {
            return wrappedMap.containsValue(value);
        }

        public boolean equals(Object o)
        {
            return wrappedMap.equals(o);
        }

        public Collection values()
        {
            return wrappedMap.values();
        }

        public void putAll(Map t)
        {
            wrappedMap.putAll(t);
        }

        public Set entrySet()
        {
            return wrappedMap.entrySet();
        }

        public Set keySet()
        {
            return wrappedMap.keySet();
        }

        public Object get(Object key)
        {
            return wrappedMap.get(key);
        }

        public Object remove(Object key)
        {
            return wrappedMap.remove(key);
        }

        /**
         * When adding a Attribute to the map, give the opportunity for the Attributes object to define
         * setXXX(yyy) where XXX is the attribute name and yyy is the attribute value. It uses XDM functionality to
         * define mutators with specific attribute names as java getter/setters. For example, if a preference is named
         * "max-login-attempts" and it's value is an integer "5" then Attributes.setMaxLoginAttempts(5) will be
         * called. If a method is available, it will be used as type-specific call.
         * @param key The preference key
         * @param value The EntityPreference instance
         * @return
         */
        public Object put(Object key, Object value)
        {
            // if value is a List, we're not going to assign it to a mutator
            if(value instanceof Attribute)
            {
                Attribute attr = (Attribute) value;
                try
                {
                    schema.assignInstanceValue(BasicAttributes.this, attr.getAttributeName(), attr.getAttributeTextValue(), false);
                }
                catch (Exception e)
                {
                    log.error(e);
                }
            }
            return wrappedMap.put(key, value);
        }
    }
}
