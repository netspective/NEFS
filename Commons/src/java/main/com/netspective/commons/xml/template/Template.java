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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import org.apache.commons.jexl.JexlContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.netspective.commons.io.InputSourceLocator;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xml.ContentHandlerNodeStackEntry;
import com.netspective.commons.xml.NodeIdentifiers;

public class Template extends TemplateElement
{
    private InputSourceLocator inputSourceLocator;
    private String templateName;
    private TemplateCatalog templateCatalog;
    private TemplateProducer templateProducer;
    private boolean calculatedInheritedTemplates;
    private Template[] inheritedTemplates;
    private Map templateParamsDeclarations = new HashMap();
    public static final String EXPRVARNAME_PARAMS = "params";

    public class Parameter
    {
        private String name;
        private String defaultValue;
        private String copyAttribute;
        private boolean required;

        public Parameter(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public boolean isRequired()
        {
            return required;
        }

        public void setRequired(boolean required)
        {
            this.required = required;
        }

        public boolean hasDefaultValue()
        {
            return defaultValue != null;
        }

        public String getDefaultValue()
        {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue)
        {
            this.defaultValue = defaultValue;
        }

        public boolean hasCopyAttribute()
        {
            return copyAttribute != null;
        }

        public String getCopyAttribute()
        {
            return copyAttribute;
        }

        public void setCopyAttribute(String copyAttribute)
        {
            this.copyAttribute = copyAttribute;
        }
    }

    public Template(String templateName, TemplateContentHandler handler, InputSourceLocator inputSourceLocator, TemplateCatalog catalog, TemplateProducer producer, String url, String localName, String qName, Attributes attributes) throws SAXException
    {
        super(handler, url, localName, qName, attributes);
        this.inputSourceLocator = inputSourceLocator;
        this.templateName = templateName;
        this.templateCatalog = catalog;
        this.templateProducer = producer;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public InputSourceLocator getInputSourceLocator()
    {
        return inputSourceLocator;
    }

    public void declareParameter(TemplateContentHandler contentHandler, String url, String localName, String qName, Attributes attributes) throws SAXException
    {
        String paramName = attributes.getValue(NodeIdentifiers.ATTRNAME_TEMPLATE_PARAM_DECL_NAME);
        if(paramName == null || paramName.length() == 0)
            throw new SAXParseException("Template parameter has no '" + NodeIdentifiers.ATTRNAME_TEMPLATE_PARAM_DECL_NAME + "' attribute.", contentHandler.getDocumentLocator());

        Parameter param = new Parameter(paramName);
        templateParamsDeclarations.put(paramName, param);

        param.setDefaultValue(attributes.getValue(NodeIdentifiers.ATTRNAME_TEMPLATE_PARAM_DECL_DEFAULT));

        String paramRequiredStr = attributes.getValue(NodeIdentifiers.ATTRNAME_TEMPLATE_PARAM_DECL_REQUIRED);
        param.setRequired(paramRequiredStr != null && paramRequiredStr.length() > 0
                          ? TextUtils.getInstance().toBoolean(paramRequiredStr) : false);

        param.setCopyAttribute(attributes.getValue(NodeIdentifiers.ATTRNAME_TEMPLATE_PARAM_DECL_COPYATTR));
    }

    public Template[] getInheritedTemplates() throws SAXException
    {
        if(!calculatedInheritedTemplates)
        {
            Map myTemplatesMap = templateCatalog.getProducerTemplates(templateProducer);
            if(myTemplatesMap == null)
                throw new SAXException("Unable to locate templates catalog for namespace '" + templateProducer.getNameSpaceId() + "'");

            if(templateProducer.getTemplateInhAttrName() != null)
            {
                String inheritTemplatesNames = getAttributes().getValue(templateProducer.getTemplateInhAttrName());
                if(inheritTemplatesNames != null && inheritTemplatesNames.length() > 0)
                {
                    List inherited = new ArrayList();
                    StringTokenizer st = new StringTokenizer(inheritTemplatesNames, ",");
                    while(st.hasMoreTokens())
                    {
                        String templateName = st.nextToken().trim();

                        Template template = (Template) myTemplatesMap.get(templateName);
                        if(template == null)
                            throw new SAXException("Inherited template '" + templateName + "' for template '" + templateProducer.getElementName() + "' (namespace '" + templateProducer.getNameSpaceId() + "') was not found in the active document. Available: " + myTemplatesMap.keySet() + " at " + getDefnSourceLocation());
                        inherited.add(template);
                    }
                    inheritedTemplates = (Template[]) inherited.toArray(new Template[inherited.size()]);
                }
                else
                    inheritedTemplates = null;
            }
            else
                inheritedTemplates = null;

            calculatedInheritedTemplates = true;
        }

        return inheritedTemplates;
    }

    public TemplateProducer getTemplateProducer()
    {
        return templateProducer;
    }

    public Map getTemplateParamsDeclarations() throws SAXException
    {
        return templateParamsDeclarations;
    }

    public String getAlternateClassName() throws SAXException
    {
        // if this template has an alternate class name, return it immediately
        String alternateClassName = getAttributes().getValue(NodeIdentifiers.ATTRNAME_ALTERNATE_CLASS_NAME);
        if(alternateClassName != null)
            return alternateClassName;

        // no alternate class name provided in the main template, check inheritance tree
        Template[] inheritedTemplates = getInheritedTemplates();
        if(inheritedTemplates != null)
        {
            for(int i = 0; i < inheritedTemplates.length; i++)
            {
                String inhAlternateClassName = inheritedTemplates[i].getAlternateClassName();
                if(inhAlternateClassName != null)
                    alternateClassName = inhAlternateClassName;
            }
        }

        return alternateClassName;
    }

    public void applyChildren(TemplateApplyContext ac) throws SAXException
    {
        if(!defnFinalized())
            finalizeDefinition(ac);

        TemplateContentHandler contentHandler = ac.getContentHandler();
        contentHandler.registerTemplateConsumption(this);

        Template[] inheritedTemplates = getInheritedTemplates();
        if(inheritedTemplates != null)
        {
            for(int i = 0; i < inheritedTemplates.length; i++)
                inheritedTemplates[i].applyChildren(ac);
        }

        List children = getChildren();
        for(int i = 0; i < children.size(); i++)
        {
            ((TemplateNode) children.get(i)).apply(ac);
        }
    }

    public void applySelfAndChildren(TemplateApplyContext ac) throws SAXException
    {
        if(!defnFinalized())
            finalizeDefinition(ac);

        TemplateContentHandler contentHandler = ac.getContentHandler();
        contentHandler.registerTemplateConsumption(this);

        Template[] inheritedTemplates = getInheritedTemplates();
        if(inheritedTemplates != null)
        {
            for(int i = 0; i < inheritedTemplates.length; i++)
                inheritedTemplates[i].applyChildren(ac);
        }

        super.apply(ac);
    }

    public TemplateApplyContext createApplyContext(TemplateContentHandler contentHandler, String elementName, Attributes attributes) throws SAXException
    {
        String exprsFlag = attributes.getValue(contentHandler.getNodeIdentifiers().getTemplateReplaceExprsAttrName());
        boolean allowReplaceExpressions = exprsFlag != null && exprsFlag.length() > 0
                                          ? TextUtils.getInstance().toBoolean(exprsFlag) : true;
        if(!allowReplaceExpressions)
            return new TemplateApplyContext(contentHandler);

        Map vars = new HashMap();

        JexlContext jc = org.apache.commons.jexl.JexlHelper.createContext();
        Stack nodeStack = contentHandler.getNodeStack();
        ContentHandlerNodeStackEntry activeEntry = (ContentHandlerNodeStackEntry) nodeStack.peek();

        vars.put("nodeStack", nodeStack);
        vars.put("ownerEntry", activeEntry);

        activeEntry.fillCreateApplyContextExpressionsVars(vars);

        Map templateParamsValues = allowReplaceExpressions
                                   ? getTemplateParamsValues(contentHandler.getNodeIdentifiers(), attributes) : null;
        if(templateParamsValues != null) vars.put(EXPRVARNAME_PARAMS, templateParamsValues);

        jc.setVars(vars);

        return new TemplateApplyContext(contentHandler, jc, templateParamsValues);
    }

    public void fillTemplateParamsRequiredAndDefaultValues(Set requiredParamNames, Map templateParamsValues, Attributes attributesFromCaller) throws SAXException
    {
        Template[] inheritedTemplates = getInheritedTemplates();

        if(inheritedTemplates != null)
        {
            for(int i = 0; i < inheritedTemplates.length; i++)
            {
                Template template = inheritedTemplates[i];
                template.fillTemplateParamsRequiredAndDefaultValues(requiredParamNames, templateParamsValues, attributesFromCaller);
            }
        }

        Map templateParametersDecls = getTemplateParamsDeclarations();
        if(templateParametersDecls.size() > 0)
        {
            for(Iterator i = templateParametersDecls.entrySet().iterator(); i.hasNext();)
            {
                Map.Entry entry = (Map.Entry) i.next();
                Parameter paramDefn = (Parameter) entry.getValue();
                if(paramDefn.isRequired())
                    requiredParamNames.add(paramDefn);

                if(paramDefn.hasDefaultValue())
                    templateParamsValues.put(paramDefn.getName(), paramDefn.getDefaultValue());

                if(paramDefn.hasCopyAttribute())
                {
                    // if we have attributes to copy, it may be a comma-separated list; we will find the first one
                    // with a value and set it as the parameter value
                    StringTokenizer st = new StringTokenizer(paramDefn.getCopyAttribute(), ",");
                    while(st.hasMoreTokens())
                    {
                        String attrName = st.nextToken().trim();
                        String attrValue = attributesFromCaller.getValue(attrName);
                        if(attrValue != null)
                        {
                            templateParamsValues.put(paramDefn.getName(), attrValue);
                            break;
                        }
                    }
                }
            }
        }
    }

    public Map getTemplateParamsValues(NodeIdentifiers nodeIdentifiers, Attributes attributesFromCaller) throws SAXException
    {
        Set requiredParams = new HashSet();
        Map templateParamsValues = new HashMap();

        fillTemplateParamsRequiredAndDefaultValues(requiredParams, templateParamsValues, attributesFromCaller);

        for(int i = 0; i < attributesFromCaller.getLength(); i++)
        {
            String attrName = attributesFromCaller.getQName(i);
            if(attrName.startsWith(nodeIdentifiers.getTemplateParamAttrPrefix()))
                templateParamsValues.put(attrName.substring(nodeIdentifiers.getTemplateParamAttrPrefix().length()), attributesFromCaller.getValue(i));
        }

        // validate that all required parameters are available
        for(Iterator i = requiredParams.iterator(); i.hasNext();)
        {
            Parameter param = (Parameter) i.next();
            String paramValue = (String) templateParamsValues.get(param.getName());

            if(paramValue == null)
                throw new SAXException("Required param '" + param.getName() + "' not found. Available: " + templateParamsValues);
        }

        return templateParamsValues;
    }
}

