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
 * $Id: Property.java,v 1.1 2003-03-13 18:33:10 shahid.shah Exp $
 */

package com.netspective.commons.config;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.netspective.commons.value.ValueContext;
import com.netspective.commons.text.ValueSourceExpressionText;
import com.netspective.commons.text.ExpressionText;
import com.netspective.commons.xdm.XmlDataModelSchema;

public class Property
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setPcDataHandlerMethodName("appendPropertyValueText");

    public class PropertyTextExpression extends ValueSourceExpressionText
    {
        protected int dynamicReplacementsCount;

        public PropertyTextExpression()
        {
            dynamicReplacementsCount = 0;
        }

        protected String getReplacement(ValueContext vc, String entireText, String replaceToken)
        {
            Property property = getOwner().findProperty(replaceToken);
            if(property != null)
            {
                PropertyTextExpression subExpr = new PropertyTextExpression();
                String result = subExpr.getFinalText(vc, property.getValue(vc));
                if(subExpr.dynamicReplacementsCount == 0)
                    property.setFinalValue(result);
                else
                    dynamicReplacementsCount += subExpr.dynamicReplacementsCount;

                return result;
            }
            else if(replaceToken.startsWith("/"))
            {
                String[] items = StringUtils.split(replaceToken, "/");
                Configuration alternateConfig = owner.getManager().getConfiguration(items[0]);
                if(alternateConfig != null)
                {
                    property = alternateConfig.findProperty(items[1]);
                    if(property != null)
                    {
                        PropertyTextExpression subExpr = new PropertyTextExpression();
                        String result = subExpr.getFinalText(vc, property.getValue(vc));
                        if(subExpr.dynamicReplacementsCount == 0)
                            property.setFinalValue(result);
                        else
                            dynamicReplacementsCount += subExpr.dynamicReplacementsCount;

                        return result;
                    }
                    else
                        return getOriginalReplacement(items[1]);
                }
                else
                    return getOriginalReplacement("Configuration '"+ items[1] +"' not found");
            }
            else
                return super.getReplacement(vc, entireText, replaceToken);
        }
    }

    protected Configuration owner;
    protected Property parent;
    protected String name;
    protected String value;
    protected String description;
    protected List childrenList;
    protected Map childrenMap;
    protected boolean dynamic;

    public Property()
    {

    }

    public Property(Configuration owner, Property parent)
    {
        setOwner(owner);
        setParent(parent);
    }

    protected void setFinalValue(String value)
    {
        this.value = value;
        setDynamic(false);
    }

    protected void setDynamicValue(String value)
    {
        this.value = value;
        setDynamic(true);
    }

    public List getChildrenList()
    {
        return childrenList;
    }

    public Map getChildrenMap()
    {
        return childrenMap;
    }

    public Configuration getOwner()
    {
        return owner;
    }

    protected void setOwner(Configuration owner)
    {
        this.owner = owner;
    }

    public Property getParent()
    {
        return parent;
    }

    protected void setParent(Property parent)
    {
        this.parent = parent;
    }

    public String getName()
    {
        return name;
    }

    static public String getNameForMapKey(String name)
    {
        return name.toUpperCase();
    }

    public String getNameForMapKey()
    {
        return name != null ? getNameForMapKey(name) : null;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setValue(String expr)
    {
        if(expr.indexOf(ExpressionText.EXPRESSION_REPLACEMENT_PREFIX) >= 0)
            setDynamicValue(expr);
        else
            setFinalValue(expr);
    }

    public void appendPropertyValueText(String text)
    {
        setValue(value != null ? value += text : text);
    }

    protected String getValue()
    {
        return value;
    }

    protected String getValue(ValueContext vc)
    {
        if(! isDynamic()) return value;

        PropertyTextExpression expr = new PropertyTextExpression();
        return expr.getFinalText(vc, value);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isDynamic()
    {
        return dynamic;
    }

    protected void setDynamic(boolean dynamic)
    {
        this.dynamic = dynamic;
    }

    public void addProperty(Property property)
    {
        if(property.getOwner() == null)
            property.setOwner(getOwner());
        setParent(this);

        if(childrenList == null)
        {
            childrenList = new ArrayList();
            childrenMap = new HashMap();
        }

        childrenList.add(property);
        if(property.getName() != null)
            childrenMap.put(property.getName(), property);

        property.getOwner().registerProperty(property);
    }

    public Property getProperty(int index)
    {
        return (Property) childrenList.get(index);
    }

    public Property getProperty(String name)
    {
        return (Property) childrenMap.get(name);
    }
}