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
 * $Id: AbstractValue.java,v 1.12 2004-04-12 22:36:07 shahid.shah Exp $
 */

package com.netspective.commons.value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.exception.ValueException;

public abstract class AbstractValue implements Value
{
    public static final String BLANK_STRING = "";
    private int listType;
    private Object value;

    public AbstractValue()
    {
    }

    public AbstractValue(int listType)
    {
        this.listType = listType;
    }

    public Class getValueHolderClass()
    {
        return Object.class;
    }

    public Class getBindParamValueHolderClass()
    {
        return getValueHolderClass();
    }

    public boolean hasValue()
    {
        return value != null;
    }

    public boolean isListValue()
    {
        return listType != VALUELISTTYPE_NONE;
    }

    public int getListValueType()
    {
        return listType;
    }

    public Object getValueForSqlBindParam()
    {
        return value;
    }

    public String getTextValue()
    {
        switch(listType)
        {
            case VALUELISTTYPE_NONE:
                return value != null ? value.toString() : null;

            case VALUELISTTYPE_STRINGARRAY:
                return value != null ? ((String[]) value)[0] : null;

            case VALUELISTTYPE_LIST:
                if(value != null)
                {
                    List valueAsList = (List) value;
                    Object v = valueAsList.size() > 0 ? valueAsList.get(0) : null;
                    return v != null ? v.toString() : null;
                }
                return null;

            default:
                return null;
        }
    }

    public boolean getBooleanValue()
    {
        Object result = getValue();
        if(result instanceof Boolean)
            return ((Boolean) result).booleanValue();

        if(result != null)
            return TextUtils.toBoolean(result.toString());

        return false;
    }

    public int getIntValue()
    {
        return Integer.parseInt(getTextValue());
    }

    public long getLongValue()
    {
        return Long.parseLong(getTextValue());
    }

    public double getDoubleValue()
    {
        return Double.parseDouble(getTextValue());
    }

    public String getTextValueOrBlank()
    {
        String value = getTextValue();
        return value == null ? BLANK_STRING : value;
    }

    public String getTextValueOrDefault(String defaultText)
    {
        String value = getTextValue();
        return value == null ? defaultText : value;
    }

    public Object getValue()
    {
        return value;
    }

    public void appendText(String text)
    {
        String existing = getTextValue();
        if(existing != null)
            setTextValue(existing + text);
        else
            setTextValue(text);
    }

    public void setValue(Object value) throws ValueException
    {
        this.value = value;
    }

    /**
     * Performs a copy by reference, not a copy by value so be careful
     * @param value
     * @throws ValueException
     */
    public void copyValueByReference(Value value) throws ValueException
    {
        setValue(value.getValue());
    }

    public void setValue(String[] value)
    {
        listType = VALUELISTTYPE_STRINGARRAY;
        setValue((Object) value);
    }

    public void setValue(List value)
    {
        listType = VALUELISTTYPE_LIST;
        setValue((Object) value);
    }

    public void setValueFromSqlResultSet(ResultSet rs, int rowNum, int colIndex) throws SQLException, ValueException
    {
        setValue(rs.getObject(colIndex));
    }

    public void setTextValue(String value) throws ValueException
    {
        setValue(value);
    }

    public String[] getTextValues()
    {
        switch(listType)
        {
            case VALUELISTTYPE_NONE:
                String text = getTextValue();
                if(text != null)
                    return new String[] { text };
                else
                    return null;

            case VALUELISTTYPE_STRINGARRAY:
                return (String[]) getValue();

            case VALUELISTTYPE_LIST:
                List list = (List) getValue();
                if(list == null)
                    return null;
                String[] array = new String[list.size()];
                for(int i = 0; i < list.size(); i++)
                {
                    Object item = list.get(i);
                    array[i] = item == null ? null : item.toString();
                }
                return array;

            default:
                return null;
        }
    }

    public List getListValue()
    {
        switch(listType)
        {
            case VALUELISTTYPE_NONE:
                String text = getTextValue();
                if(text != null)
                {
                    List list = new ArrayList();
                    list.add(text);
                    return list;
                }
                else
                    return null;

            case VALUELISTTYPE_STRINGARRAY:
                String[] array = (String[]) getValue();
                if(array == null)
                    return null;
                List list = new ArrayList();
                for(int i = 0; i < array.length; i++)
                    list.add(array[i]);
                return list;

            case VALUELISTTYPE_LIST:
                return (List) getValue();

            default:
                return null;
        }
    }

    public int size()
    {
        switch(listType)
        {
            case VALUELISTTYPE_NONE:
                return hasValue() ? 1 : 0;

            case VALUELISTTYPE_STRINGARRAY:
                String[] array = (String[]) getValue();
                return array == null ? 0 : array.length;

            case VALUELISTTYPE_LIST:
                List list = (List) getValue();
                return list == null ? 0 : list.size();

            default:
                return 0;
        }
    }

    public boolean equals(Object o){

        AbstractValue valueObject;

        if (o == null)
            return false;

        try
        {
            valueObject = (AbstractValue) o;
        }
        catch (ClassCastException e)
        {
            return false;
        }

        if (this.getValueHolderClass() != valueObject.getValueHolderClass())
            return false;

        if ( this.value == null && valueObject.value == null)
            return true;

        if ( (this.value == null && valueObject.value != null) || (this.value != null && valueObject.value == null) )
            return false;

        if (this.getListValueType() != valueObject.getListValueType())
            return false;

        if (valueObject.getListValueType() == Value.VALUELISTTYPE_NONE)
        {
            return value.equals(valueObject.value);
        }
        else if (valueObject.getListValueType() == Value.VALUELISTTYPE_LIST)
        {
            List thisList = this.getListValue();
            List valueObjectList = valueObject.getListValue();
            for (int i = 0; i < thisList.size(); i++)
            {
                if (!thisList.get(i).equals(valueObjectList.get(i)))
                    return false;
            }
        }
        else if (valueObject.getListValueType() == Value.VALUELISTTYPE_STRINGARRAY)
        {
            String[] thisList = this.getTextValues();
            String[] valueObjectList = valueObject.getTextValues();
            for (int i = 0; i < thisList.length; i++)
            {
                if (!thisList[i].equals(valueObjectList[i]))
                    return false;
            }
        }

        return true;
    }

    public void importFromXml(Element parent)
    {
        String valueType = parent.getAttribute("value-type");
        if(valueType.equals("strings"))
        {
            NodeList valuesNodesList = parent.getElementsByTagName("values");
            if(valuesNodesList.getLength() > 0)
            {
                NodeList valueNodesList = ((Element) valuesNodesList.item(0)).getElementsByTagName("value");
                int valuesCount = valueNodesList.getLength();
                if(valuesCount > 0)
                {
                    String[] values = new String[valuesCount];
                    for(int i = 0; i < valuesCount; i++)
                    {
                        Element valueElem = (Element) valueNodesList.item(i);
                        if(valueElem.getChildNodes().getLength() > 0)
                            values[i] = valueElem.getFirstChild().getNodeValue();
                    }
                    setValue(values);
                }
            }
        }
        else
        {
            NodeList valueList = parent.getElementsByTagName("value");
            if(valueList.getLength() > 0)
            {
                Element valueElem = (Element) valueList.item(0);
                if(valueElem.getChildNodes().getLength() > 0)
                    setTextValue(valueElem.getFirstChild().getNodeValue());
            }
        }
    }

    public void exportToXml(Element parent)
    {
        Document doc = parent.getOwnerDocument();
        if(isListValue())
        {
            parent.setAttribute("value-type", "strings");
            Element valuesElem = doc.createElement("values");
            String[] values = getTextValues();
            if(values != null)
            {
                for(int i = 0; i < values.length; i++)
                {
                    Element valueElem = doc.createElement("value");
                    valueElem.appendChild(doc.createTextNode(values[i]));
                    valuesElem.appendChild(valueElem);
                }
            }
            parent.appendChild(valuesElem);
        }
        else
        {
            String value = getTextValue();
            if(value != null && value.length() > 0)
            {
                parent.setAttribute("value-type", "string");
                Element valueElem = doc.createElement("value");
                valueElem.appendChild(doc.createTextNode(value));
                parent.appendChild(valueElem);
            }
        }
    }

    public String toString()
    {
        return super.toString() + ": " + getTextValue();
    }
}
