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
 * $Id: XmlDataModelSchema.java,v 1.15 2003-05-17 17:51:04 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.exception.UnsupportedAttributeException;
import com.netspective.commons.xdm.exception.UnsupportedElementException;
import com.netspective.commons.xdm.exception.UnsupportedTextException;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.io.InputSourceTracker;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.command.Command;
import com.netspective.commons.command.Commands;
import com.netspective.commons.command.CommandNotFoundException;

/**
 * This class is used to introspect existing classes and allow parsing of XML
 * and unmarshalling the XML elements into an exact replica of a Java object
 * model. This is very useful when an XML structure needs to be unmarshalled
 * into a set of java classes (called a DataModel) that mimics the XML.
 * This class's original code (and indeed the very idea) was taken from the
 * Jakarta Ant project but this version is very generic (whereas the Ant
 * code was specific to TaskDefs). The DataModelSchema is most appropriate
 * for cases where unmarshalling of XML into a structured Java object model
 * is critical; this class does not (yet) handle the marshalling of Java into
 * XML using any particular rules (though it wouldn't be hard to add that
 * capability).
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:snshah@netspective.com">Shahid N. Shah</a>
 */
public class XmlDataModelSchema
{
    public static final String XMLDATAMODEL_SCHEMA_OPTIONS_FIELD_NAME = "XML_DATA_MODEL_SCHEMA_OPTIONS";
    public static final String ADD_TEXT_DEFAULT_METHOD_NAME = "addText";

    public interface CustomElementCreator
    {
        public Object createCustomDataModelElement(XdmParseContext pc, XmlDataModelSchema schema, Object parent, String elementName, String alternateClassName)
                throws DataModelException, InvocationTargetException, IllegalAccessException, InstantiationException;
    }

    public interface CustomElementStorer
    {
        public void storeCustomDataModelElement(XdmParseContext pc, XmlDataModelSchema schema, Object parent, Object child, String elementName)
                throws DataModelException, InvocationTargetException, IllegalAccessException, InstantiationException;
    }

    public interface CustomElementAttributeSetter
    {
        public void setCustomDataModelElementAttribute(XdmParseContext pc, XmlDataModelSchema schema, Object parent, String attrName, String attrValue)
                throws DataModelException, InvocationTargetException, IllegalAccessException, DataModelException;
    }

    public interface ConstructionFinalizeListener
    {
        public void finalizeConstruction() throws DataModelException;
    }

    public interface InputSourceTrackerListener
    {
        public void setInputSourceTracker(InputSourceTracker tracker);
    }

    public static class Options
    {
        private String pcDataHandlerMethodName = ADD_TEXT_DEFAULT_METHOD_NAME;
        private boolean generateAutoAliases = true;
        private boolean ignorePcData;
        private Map aliases = new HashMap();
        private Set ignoreNestedElements = new HashSet();
        private Set ignoreAttributes = new HashSet();

        public Options()
        {
        }

        public Options(Options copy)
        {
            pcDataHandlerMethodName = copy.pcDataHandlerMethodName;
            generateAutoAliases = copy.generateAutoAliases;
            ignorePcData = copy.ignorePcData;
            aliases.putAll(copy.aliases);
            ignoreNestedElements.addAll(copy.ignoreNestedElements);
            ignoreAttributes.addAll(copy.ignoreAttributes);
        }

        public void setPropertyNames(PropertyNames propertyNames)
        {
            String[] allNames = propertyNames.getAllNames();
            for(int i = 0; i < allNames.length; i++)
            {
                String alias = allNames[i];
                if(ignoreAttributes.contains(alias))
                    ignoreAttributes.addAll(propertyNames.getAliases());
                if(ignoreNestedElements.contains(alias))
                    ignoreNestedElements.addAll(propertyNames.getAliases());
            }
        }

        public boolean isGenerateAutoAliases()
        {
            return generateAutoAliases;
        }

        public boolean ignoreAttribute(String attrName)
        {
            return ignoreAttributes.contains(attrName);
        }

        public boolean ignoreNestedElement(String elementName)
        {
            return ignoreNestedElements.contains(elementName);
        }

        public Set getIgnoreAttributes()
        {
            return ignoreAttributes;
        }

        public Set getIgnoreNestedElements()
        {
            return ignoreNestedElements;
        }

        public boolean isIgnorePcData()
        {
            return ignorePcData;
        }

        public String getPcDataHandlerMethodName()
        {
            return pcDataHandlerMethodName;
        }

        public Options setIgnoreAttributes(Set ignoreAttributes)
        {
            this.ignoreAttributes = ignoreAttributes;
            return this;
        }

        public Options setIgnoreNestedElements(Set ignoreNestedElements)
        {
            this.ignoreNestedElements = ignoreNestedElements;
            return this;
        }

        public Options addIgnoreAttributes(String[] ignoreAttributesList)
        {
            for(int i = 0; i < ignoreAttributesList.length; i++)
                ignoreAttributes.add(ignoreAttributesList[i]);
            return this;
        }

        public Options addIgnoreNestedElements(String[] ignoreElementsList)
        {
            for(int i = 0; i < ignoreElementsList.length; i++)
                ignoreNestedElements.add(ignoreElementsList[i]);
            return this;
        }

        public Options setIgnorePcData(boolean ignorePcData)
        {
            this.ignorePcData = ignorePcData;
            return this;
        }

        public Options setPcDataHandlerMethodName(String pcDataHandlerMethodName)
        {
            this.pcDataHandlerMethodName = pcDataHandlerMethodName;
            return this;
        }

        public void setGenerateAutoAliases(boolean generateAutoAliases)
        {
            this.generateAutoAliases = generateAutoAliases;
        }

        public Map getAliases()
        {
            return aliases;
        }

        public void addAliases(String primaryName, String[] alternateNames)
        {
	        if (null == alternateNames)
		        return;

            Set altNames = new HashSet();
            for(int i = 0; i < alternateNames.length; i++)
                altNames.add(alternateNames[i]);

            Set set = (Set) aliases.get(primaryName);
            if(set == null)
                aliases.put(primaryName, altNames);
            else
                set.addAll(altNames);
        }

        public void addAliases(String primaryName, Set alternateNames)
        {
	        if (null == alternateNames)
	            return;

            Set set = (Set) aliases.get(primaryName);
            if(set == null)
                aliases.put(primaryName, alternateNames);
            else
                set.addAll(alternateNames);
        }
    }

    /**
     * Holds options parameters for this schema
     */
    private Options options;

    /**
     * holds the names of the properties (key is a name, value is the PropertyName object associated with it)
     */
    private Map propertyNames;

    /**
     * holds the types of the attributes that could be set.
     */
    private Map attributeTypes;

    /**
     * holds the attribute setter anonymous classes.
     */
    private Map attributeSetters;

    /**
     * holds the attribute setter methods
     */
    private Map attributeSetterMethods;

    /**
     * holds the classes that have accessors
     */
    private Map attributeAccessors;

    /**
     * holds the classes that can manage bitmasked flags
     */
    private Map flagsAttributeAccessors;

    /**
     * Holds the types of nested elements that could be created.
     */
    private Map nestedTypes;

    /**
     * Holds methods to create nested elements.
     */
    private Map nestedCreators;

    /**
     * Holds methods to create nested elements.
     */
    private Map nestedAltClassNameCreators;

    /**
     * Holds methods to create nested elements.
     */
    private Map nestedAdders;

    /**
     * Holds methods to store configured or custom nested elements.
     */
    private Map nestedStorers;

    /**
     * The method to add PCDATA text.
     */
    private Method addText = null;

    /**
     * The Class that's been introspected.
     */
    private Class bean;

    /**
     * instances we've already created
     */
    private static Map schemas = new HashMap();

    public static Map getSchemas()
    {
        return schemas;
    }

    /**
     * Factory method for schema objects.
     */
    public synchronized static XmlDataModelSchema getSchema(Class c)
    {
        XmlDataModelSchema schema = (XmlDataModelSchema) schemas.get(c);
        if (schema == null)
        {
            schema = new XmlDataModelSchema(c);
            schemas.put(c, schema);
        }
        return schema;
    }

    public Options getOptions()
    {
        return options;
    }

    public Map getPropertyNames()
    {
        return propertyNames;
    }

    public Map getAttributeSetters()
    {
        return attributeSetters;
    }

    public Map getAttributeAccessors()
    {
        return attributeAccessors;
    }

    public Map getAttributeSetterMethods()
    {
        return attributeSetterMethods;
    }

    public Map getFlagsAttributeAccessors()
    {
        return flagsAttributeAccessors;
    }

    public Map getAttributeTypes()
    {
        return attributeTypes;
    }

    public Map getNestedTypes()
    {
        return nestedTypes;
    }

    public Map getNestedStorers()
    {
        return nestedStorers;
    }

    public Map getNestedCreators()
    {
        return nestedCreators;
    }

    public Map getNestedAdders()
    {
        return nestedAdders;
    }

    public Class getBean()
    {
        return bean;
    }

    private static Object getStaticFieldValue(final Class bean, final String name)
    {
        try
        {
            Field field = bean.getField(name);
            return field.get(null);
        }
        catch (NoSuchFieldException e)
        {
            return null;
        }
        catch (SecurityException e)
        {
            return null;
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
        catch (IllegalAccessException e)
        {
            return null;
        }
    }

    private XmlDataModelSchema(final Class bean)
    {
        propertyNames = new HashMap();
        attributeTypes = new HashMap();
        attributeSetters = new HashMap();
        attributeSetterMethods = new HashMap();
        attributeAccessors = new HashMap();
        flagsAttributeAccessors = new HashMap();
        nestedTypes = new HashMap();
        nestedCreators = new HashMap();
        nestedAltClassNameCreators = new HashMap();
        nestedAdders = new HashMap();
        nestedStorers = new HashMap();

        this.bean = bean;

        Options customOptions = (Options) getStaticFieldValue(bean, XMLDATAMODEL_SCHEMA_OPTIONS_FIELD_NAME);
        options = customOptions != null ? customOptions : new Options();

        Method[] methods = bean.getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            final Method m = methods[i];
            final String name = m.getName();
            Class returnType = m.getReturnType();
            Class[] args = m.getParameterTypes();

            if (name.equals(options.pcDataHandlerMethodName)
                    && java.lang.Void.TYPE.equals(returnType)
                    && args.length == 1
                    && java.lang.String.class.equals(args[0]))
            {
                addText = methods[i];
            }
            else if (name.startsWith("get") && args.length == 0)
            {
                String[] propNames = getPropertyNames(name, "get");
                for(int pn = 0; pn < propNames.length; pn++)
                {
                    if(propNames[pn].length() == 0)
                        continue;

                    AttributeAccessor aa = createAttributeAccessor(m, propNames[pn], returnType);
                    if (aa != null)
                        attributeAccessors.put(propNames[pn], aa);
                }
            }
            else if (name.startsWith("set")
                    && java.lang.Void.TYPE.equals(returnType)
                    && args.length == 1
                    && !args[0].isArray())
            {
                String[] propNames = getPropertyNames(name, "set");
                for(int pn = 0; pn < propNames.length; pn++)
                {
                    if(propNames[pn].length() == 0)
                        continue;

                    attributeSetterMethods.put(propNames[pn], m);
                    AttributeSetter as = createAttributeSetter(m, propNames[pn], args[0]);
                    if (as != null)
                    {
                        attributeTypes.put(propNames[pn], args[0]);
                        attributeSetters.put(propNames[pn], as);
                    }
                }
            }
            else if (name.startsWith("create")
                    && !returnType.isArray()
                    && !returnType.isPrimitive()
                    && (args.length == 0))
            {
                // prevent infinite recursion for nested recursive elements
                if(! returnType.getClass().equals(bean.getClass()))
                    getSchema(returnType);
                String[] propNames = getPropertyNames(name, "create");
                for(int pn = 0; pn < propNames.length; pn++)
                {
                    if(propNames[pn].length() == 0)
                        continue;

                    nestedTypes.put(propNames[pn], returnType);
                    nestedCreators.put(propNames[pn], new NestedCreator()
                    {
                        public Object create(Object parent) throws InvocationTargetException, IllegalAccessException
                        {
                            return m.invoke(parent, new Object[]{});
                        }
                    });
                }
            }
            else if (name.startsWith("create")
                    && !returnType.isArray()
                    && !returnType.isPrimitive()
                    && (args.length == 1 && args[0] == Class.class))
            {
                // prevent infinite recursion for nested recursive elements
                if(! returnType.getClass().equals(bean.getClass()))
                    getSchema(returnType);
                String[] propNames = getPropertyNames(name, "create");
                for(int pn = 0; pn < propNames.length; pn++)
                {
                    if(propNames[pn].length() == 0)
                        continue;

                    nestedTypes.put(propNames[pn], returnType);
                    nestedAltClassNameCreators.put(propNames[pn], new NestedAltClassCreator()
                    {
                        public Object create(Object parent, Class cls) throws InvocationTargetException, IllegalAccessException
                        {
                            return m.invoke(parent, new Object[]{ cls });
                        }
                    });
                }
            }
            else if (name.startsWith("add")
                    && java.lang.Void.TYPE.equals(returnType)
                    && args.length == 1
                    && !java.lang.String.class.equals(args[0])
                    && !args[0].isArray()
                    && !args[0].isPrimitive())
            {
                String[] propNames = getPropertyNames(name, "add");
                try
                {
                    final Constructor c = args[0].getConstructor(new Class[]{});
                    for(int pn = 0; pn < propNames.length; pn++)
                    {
                        if(propNames[pn].length() == 0)
                            continue;

                        if(!nestedCreators.containsKey(propNames[pn]))
                        {
                            nestedTypes.put(propNames[pn], args[0]);
                            nestedCreators.put(propNames[pn], new NestedCreator()
                            {
                                public boolean allowAlternateClass() { return false; }

                                public Object create(Object parent)
                                        throws InvocationTargetException, IllegalAccessException, InstantiationException
                                {
                                    return c.newInstance(new Object[]{});
                                }

                                public Object create(Object parent, Class cls)
                                        throws InvocationTargetException, IllegalAccessException, InstantiationException
                                {
                                    return c.newInstance(new Object[]{ cls });
                                }
                            });
                        }
                    }
                }
                catch (NoSuchMethodException nse)
                {
                }
                // prevent infinite recursion for nested recursive elements
                if(! args[0].getClass().equals(bean.getClass()))
                    getSchema(args[0]);
                for(int pn = 0; pn < propNames.length; pn++)
                {
                    if(propNames[pn].length() == 0)
                        continue;

                    nestedStorers.put(propNames[pn], new NestedStorer()
                    {
                        public void store(Object parent, Object child)
                                throws InvocationTargetException, IllegalAccessException, InstantiationException
                        {
                            m.invoke(parent, new Object[]{child});
                        }
                    });
                }
            }
        }
    }

    /**
     * Sets the named attribute.
     */
    public void setAttribute(XdmParseContext pc, Object element, String attributeName, String value, boolean withinCustom) throws DataModelException, UnsupportedAttributeException
    {
        boolean hasCustom = element instanceof CustomElementAttributeSetter;
        AttributeSetter as = (AttributeSetter) attributeSetters.get(attributeName);
        if (as == null && (withinCustom || ! hasCustom))
        {
            // see if we're trying to set a named flag
            for(Iterator i = flagsAttributeAccessors.entrySet().iterator(); i.hasNext(); )
            {
                Map.Entry entry = (Map.Entry) i.next();
                AttributeAccessor accessor = (AttributeAccessor) entry.getValue();
                Object returnVal = null;
                try
                {
                    returnVal = accessor.get(pc, element);
                }
                catch (Exception e)
                {
                    throw new DataModelException(pc, e);
                }

                if(returnVal instanceof XdmBitmaskedFlagsAttribute)
                {
                    XdmBitmaskedFlagsAttribute bfa = (XdmBitmaskedFlagsAttribute) returnVal;
                    if(bfa.updateFlag(attributeName, TextUtils.toBoolean(value)))
                        return;
                }
            }

            UnsupportedAttributeException e = new UnsupportedAttributeException(pc, element, attributeName);
            pc.addError(e);
            if(pc.isThrowErrorException())
                throw e;
            else
                return;
        }
        try
        {
            if (as == null && (!withinCustom && hasCustom))
                ((CustomElementAttributeSetter) element).setCustomDataModelElementAttribute(pc, this, element, attributeName, value);
            else
                as.set(pc, element, value);
        }
        catch (IllegalAccessException ie)
        {
            throw new DataModelException(pc, ie);
        }
        catch (InvocationTargetException ite)
        {
            Throwable t = ite.getTargetException();
            if (t instanceof DataModelException)
            {
                throw (DataModelException) t;
            }
            throw new DataModelException(pc, t);
        }
        catch (NumberFormatException nfe)
        {
            throw new DataModelException(pc, nfe);
        }
    }

    /**
     * Adds PCDATA areas.
     */
    public void addText(XdmParseContext pc, Object element, String text) throws DataModelException, UnsupportedTextException
    {
        if(options.ignorePcData) return;

        if (addText == null)
        {
            UnsupportedTextException e = new UnsupportedTextException(pc, element, text);
            pc.addError(e);
            if(pc.isThrowErrorException())
                throw e;
            else
                return;
        }
        try
        {
            addText.invoke(element, new String[]{text});
        }
        catch (IllegalAccessException ie)
        {
            // impossible as getMethods should only return public methods
            throw new DataModelException(pc, ie);
        }
        catch (InvocationTargetException ite)
        {
            Throwable t = ite.getTargetException();
            if (t instanceof DataModelException)
            {
                throw (DataModelException) t;
            }
            throw new DataModelException(pc, t);
        }
    }

    protected Object createElement(XdmParseContext pc, String alternateClassName, Object element, String elementName) throws DataModelException, UnsupportedElementException
    {
        try
        {
            if(alternateClassName != null)
            {
                Class cls = null;
                try
                {
                    cls = Class.forName(alternateClassName);
                }
                catch (ClassNotFoundException e)
                {
                    throw new DataModelException(pc, e);
                }

                NestedAltClassCreator nac = (NestedAltClassCreator) nestedAltClassNameCreators.get(elementName);
                return nac != null ? nac.create(element, cls) : cls.newInstance();
            }
            else
            {
                NestedCreator nc = (NestedCreator) nestedCreators.get(elementName);
                if(nc != null)
                    return nc.create(element);
            }
        }
        catch (IllegalAccessException ie)
        {
            // impossible as getMethods should only return public methods
            throw new DataModelException(pc, ie);
        }
        catch (InstantiationException ine)
        {
            // impossible as getMethods should only return public methods
            throw new DataModelException(pc, ine);
        }
        catch (InvocationTargetException ite)
        {
            Throwable t = ite.getTargetException();
            if (t instanceof DataModelException)
            {
                throw (DataModelException) t;
            }
            throw new DataModelException(pc, t);
        }

        // if the element is being defined as a sub-element but has an attribute of the same name, it's a convenience attribute setter
        AttributeSetter as = (AttributeSetter) attributeSetters.get(elementName);
        if(as != null)
            return element;
        else
        {
            UnsupportedElementException e = new UnsupportedElementException(this, pc, element, elementName);
            if(pc != null)
            {
                pc.addError(e);
                if(pc.isThrowErrorException())
                    throw e;
                else
                    return null;
            }
            else
                return null;
        }
    }

    /**
     * Creates a named nested element.
     */
    public Object createElement(XdmParseContext pc, String alternateClassName, Object element, String elementName, boolean withinCustom) throws DataModelException, UnsupportedElementException
    {
        //System.out.println("Creating: " + element.getClass().getName() + " " + elementName + " " + alternateClassName + " " + withinCustom);

        try
        {
            if(element instanceof CustomElementCreator && ! withinCustom)
                return ((CustomElementCreator) element).createCustomDataModelElement(pc, this, element, elementName, alternateClassName);
            else
                return createElement(pc, alternateClassName, element, elementName);
        }
        catch (IllegalAccessException ie)
        {
            // impossible as getMethods should only return public methods
            throw new DataModelException(pc, ie);
        }
        catch (InstantiationException ine)
        {
            // impossible as getMethods should only return public methods
            throw new DataModelException(pc, ine);
        }
        catch (InvocationTargetException ite)
        {
            Throwable t = ite.getTargetException();
            if (t instanceof DataModelException)
            {
                throw (DataModelException) t;
            }
            throw new DataModelException(pc, t);
        }
    }

    public void storeElement(XdmParseContext pc, Object element, Object child, String elementName, boolean withinCustom) throws DataModelException
    {
        if (elementName == null)
            return;

        NestedStorer ns = (NestedStorer) nestedStorers.get(elementName);
        try
        {
            if(ns == null && (!withinCustom && element instanceof CustomElementStorer))
                ((CustomElementStorer) element).storeCustomDataModelElement(pc, this, element, child, elementName);
            else if(ns != null)
                ns.store(element, child);
        }
        catch (IllegalAccessException ie)
        {
            // impossible as getMethods should only return public methods
            throw new DataModelException(pc, ie);
        }
        catch (InstantiationException ine)
        {
            // impossible as getMethods should only return public methods
            throw new DataModelException(pc, ine);
        }
        catch (InvocationTargetException ite)
        {
            Throwable t = ite.getTargetException();
            if (t instanceof DataModelException)
            {
                throw (DataModelException) t;
            }
            throw new DataModelException(pc, t);
        }
    }

    public void finalizeElementConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        if(element instanceof ConstructionFinalizeListener)
            ((ConstructionFinalizeListener) element).finalizeConstruction();
    }

    /**
     * returns the type of a named nested element.
     */
    public Class getElementType(String elementName)
            throws DataModelException
    {
        Class nt = (Class) nestedTypes.get(elementName);
        if (nt == null)
        {
            String msg = "Class " + bean.getName() + " doesn't support the nested \"" + elementName + "\" element.";
            throw new DataModelException(null, msg);
        }
        return nt;
    }

    /**
     * returns the type of a named attribute.
     */
    public Class getAttributeType(String attributeName)
            throws DataModelException
    {
        Class at = (Class) attributeTypes.get(attributeName);
        if (at == null)
        {
            String msg = "Class " + bean.getName() + " doesn't support the \"" + attributeName + "\" attribute.";
            throw new DataModelException(null, msg);
        }
        return at;
    }

    /**
     * Does the introspected class ignore PCDATA
     */
    public boolean isIgnoreText()
    {
        return options.ignorePcData;
    }

    /**
     * Does the introspected class support PCDATA?
     */
    public boolean supportsCharacters()
    {
        return !options.ignorePcData && addText != null;
    }

    /**
     * Does the introspected class ignore PCDATA
     */
    public boolean hasAddTextMethod()
    {
        return addText != null;
    }

    /**
     * Return all attribues supported by the introspected class.
     */
    public Set getAttributes()
    {
        return attributeSetters.keySet();
    }

    /**
     * Return all nested elements supported by the introspected class.
     */
    public Map getNestedElements()
    {
        return nestedTypes;
    }

    /**
     * Create a proper implementation of AttributeAccessor for the given
     * attribute type.
     */
    private AttributeAccessor createAttributeAccessor(final Method m, final String attrName, final Class arg)
    {
        AttributeAccessor result = new AttributeAccessor()
        {
            public Object get(XdmParseContext pc, Object parent)
                    throws InvocationTargetException, IllegalAccessException
            {
                return m.invoke(parent, null);
            }
        };

        if (XdmBitmaskedFlagsAttribute.class.isAssignableFrom(arg))
            flagsAttributeAccessors.put(attrName, result);

        return result;
    }

    /**
     * Create a proper implementation of AttributeSetter for the given
     * attribute type.
     */
    private AttributeSetter createAttributeSetter(final Method m, final String attrName, final Class arg)
    {
        // simplest case - setAttribute expects String
        if (java.lang.String.class.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new String[]{value});
                }
            };

            // now for the primitive types, use their wrappers
        }
        else if (java.lang.Character.class.equals(arg)
                || java.lang.Character.TYPE.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new Character[]{new Character(value.charAt(0))});
                }
            };
        }
        else if (java.lang.Byte.TYPE.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new Byte[]{new Byte(value)});
                }
            };
        }
        else if (java.lang.Short.TYPE.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new Short[]{new Short(value)});
                }
            };
        }
        else if (java.lang.Integer.TYPE.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new Integer[]{new Integer(value)});
                }
            };
        }
        else if (java.lang.Long.TYPE.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new Long[]{new Long(value)});
                }
            };
        }
        else if (java.lang.Float.TYPE.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new Float[]{new Float(value)});
                }
            };
        }
        else if (java.lang.Double.TYPE.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new Double[]{new Double(value)});
                }
            };
        }
        // boolean gets an extra treatment, because we have a nice method
        else if (java.lang.Boolean.class.equals(arg)
                || java.lang.Boolean.TYPE.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    m.invoke(parent, new Boolean[]{new Boolean(TextUtils.toBoolean(value))});
                }
            };
        }
        // Class doesn't have a String constructor but a decent factory method
        else if (java.lang.Class.class.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException, DataModelException
                {
                    try
                    {
                        m.invoke(parent, new Class[]{Class.forName(value)});
                    }
                    catch (ClassNotFoundException ce)
                    {
                        DataModelException dme = new DataModelException(pc, ce);
                        pc.addError(dme);
                        if(pc.isThrowErrorException())
                            throw dme;
                    }
                }
            };
        }
        else if (java.io.File.class.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    // resolve relative paths through DataModel
                    m.invoke(parent, new File[]{pc.resolveFile(value)});
                }
            };
        }
        else if (ValueSource.class.equals(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException
                {
                    ValueSource vs = ValueSources.getInstance().getValueSourceOrStatic(value);
                    if(vs == null)
                        pc.addWarning("Unable to create ValueSource for '"+ value +"' at " + pc.getLocator().getSystemId() +" line "+ pc.getLocator().getLineNumber());
                    m.invoke(parent, new ValueSource[]{ vs });
                }
            };
        }
        else if (Command.class.isAssignableFrom(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException, DataModelException
                {
                    try
                    {
                        m.invoke(parent, new Command[]{ Commands.getInstance().getCommand(value) });
                    }
                    catch (CommandNotFoundException e)
                    {
                        throw new DataModelException(pc, e);
                    }
                }
            };
        }
        else if (XdmEnumeratedAttribute.class.isAssignableFrom(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException, DataModelException
                {
                    try
                    {
                        XdmEnumeratedAttribute ea = (XdmEnumeratedAttribute) arg.newInstance();
                        ea.setValue(pc, parent, attrName, value);
                        m.invoke(parent, new XdmEnumeratedAttribute[]{ea});
                    }
                    catch (InstantiationException ie)
                    {
                        throw new DataModelException(pc, ie);
                    }
                }
            };
        }
        else if (XdmBitmaskedFlagsAttribute.class.isAssignableFrom(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException, DataModelException
                {
                    try
                    {
                        XdmBitmaskedFlagsAttribute bfa = null;
                        NestedCreator creator = (NestedCreator) nestedCreators.get(attrName);
                        if(creator != null)
                            bfa = (XdmBitmaskedFlagsAttribute) creator.create(parent);
                        else
                            bfa = (XdmBitmaskedFlagsAttribute) arg.newInstance();
                        bfa.setValue(pc, parent, attrName, value);
                        m.invoke(parent, new XdmBitmaskedFlagsAttribute[]{ bfa });
                    }
                    catch (InstantiationException ie)
                    {
                        throw new DataModelException(pc, ie);
                    }
                }
            };
        }
        else if (Locale.class.isAssignableFrom(arg))
        {
            return new AttributeSetter()
            {
                public void set(XdmParseContext pc, Object parent, String value)
                        throws InvocationTargetException, IllegalAccessException, DataModelException
                {
                    String[] items = TextUtils.split(value, ",", true);
                    switch(items.length)
                    {
                        case 1:
                            m.invoke(parent, new Locale[] { new Locale(items[0], "") });
                            break;

                        case 2:
                            m.invoke(parent, new Locale[] { new Locale(items[1], items[2]) });
                            break;

                        case 3:
                            m.invoke(parent, new Locale[] { new Locale(items[1], items[2], items[3]) });
                            break;

                        case 4:
                            throw new DataModelException(pc, "Too many items in Locale constructor.");
                    }
                }
            };
        }
        else
        {
            // worst case. look for a public String constructor and use it
            try
            {
                final Constructor c = arg.getConstructor(new Class[]{java.lang.String.class});
                return new AttributeSetter()
                {
                    public void set(XdmParseContext pc, Object parent, String value)
                            throws InvocationTargetException, IllegalAccessException, DataModelException
                    {
                        try
                        {
                            Object attribute = c.newInstance(new String[]{value});
                            m.invoke(parent, new Object[]{attribute});
                        }
                        catch (InstantiationException ie)
                        {
                            throw new DataModelException(pc, ie);
                        }
                    }
                };

            }
            catch (NoSuchMethodException nme)
            {
            }
        }

        return null;
    }

    public class PropertyNames
    {
        private String primaryName;
        private Set aliases = new HashSet();

        public PropertyNames(String methodName, String prefix)
        {
            int start = prefix.length();

            String nameMinusPrefix = methodName.substring(start);
            String xmlNodeName = TextUtils.javaIdentifierToXmlNodeName(nameMinusPrefix);

            primaryName = xmlNodeName.toLowerCase();
            aliases.add(primaryName);

            if(options.isGenerateAutoAliases() && ! xmlNodeName.equals(nameMinusPrefix))
                aliases.add(nameMinusPrefix.toLowerCase());

            // map all the property names to this object
            for(Iterator i = aliases.iterator(); i.hasNext(); )
            {
                String name = (String) i.next();
                propertyNames.put(name, this);
            }

            Map customAliasesMap = options.getAliases();
            String[] allNames = getAllNames();
            for(int i = 0; i < allNames.length; i++)
            {
                String name = allNames[i];
                Set customAliases = (Set) customAliasesMap.get(name);
                if(customAliases != null)
                    aliases.addAll(customAliases);
            }

            options.setPropertyNames(this);
        }

        public String getPrimaryName()
        {
            return primaryName;
        }

        public boolean isPrimaryName(String name)
        {
            return primaryName.equals(name);
        }

        public Set getAliases()
        {
            return aliases;
        }

        public String[] getAllNames()
        {
            return (String[]) aliases.toArray(new String[aliases.size()]);
        }

	    /**
	     * Returns a string representation of the object. In general, the
	     * <code>toString</code> method returns a string that
	     * "textually represents" this object. The result should
	     * be a concise but informative representation that is easy for a
	     * person to read.
	     * It is recommended that all subclasses override this method.
	     * <p>
	     * The <code>toString</code> method for class <code>Object</code>
	     * returns a string consisting of the name of the class of which the
	     * object is an instance, the at-sign character `<code>@</code>', and
	     * the unsigned hexadecimal representation of the hash code of the
	     * object. In other words, this method returns a string equal to the
	     * value of:
	     * <blockquote>
	     * <pre>
	     * getClass().getName() + '@' + Integer.toHexString(hashCode())
	     * </pre></blockquote>
	     *
	     * @return  a string representation of the object.
	     */
	    public String toString () {
		    String output = primaryName + ": [";
		    String[] aliases = getAllNames();

            for (int i = 0; i < aliases.length; i ++)
            {
	            if (0 != i)
		            output += ", ";

	            output += aliases[i];
	        }

		    output += "]";
		    return output;
	    }
    }

    private String[] getPropertyNames(String methodName, String prefix)
    {
        PropertyNames result = new PropertyNames(methodName, prefix);
        return result.getAllNames();
    }

    public interface NestedCreator
    {
        public Object create(Object parent)
                throws InvocationTargetException, IllegalAccessException, InstantiationException;
    }

    public interface NestedAltClassCreator
    {
        public Object create(Object parent, Class cls)
                throws InvocationTargetException, IllegalAccessException, InstantiationException;
    }

    public interface NestedStorer
    {
        public void store(Object parent, Object child)
                throws InvocationTargetException, IllegalAccessException, InstantiationException;
    }

    public interface AttributeSetter
    {
        public void set(XdmParseContext pc, Object parent, String value)
                throws InvocationTargetException, IllegalAccessException,
                DataModelException;
    }

    public interface AttributeAccessor
    {
        public Object get(XdmParseContext pc, Object parent)
                throws InvocationTargetException, IllegalAccessException,
                DataModelException;
    }
}
