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
 * $Id: PresentationValue.java,v 1.3 2003-07-05 19:19:44 shahid.shah Exp $
 */

package com.netspective.commons.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.netspective.commons.value.exception.ValueException;

public class PresentationValue implements Value
{
    public class Items extends ArrayList
    {
        public class Item
        {
            private String caption;
            private String value;
            private int flags;
            private Object custom;

            public Item()
            {
            }

            public String getCaption()
            {
                return caption != null ? caption : value;
            }

            public void setCaption(String caption)
            {
                this.caption = caption;
            }

            public Object getCustom()
            {
                return custom;
            }

            public void setCustom(Object custom)
            {
                this.custom = custom;
            }

            public int getFlags()
            {
                return flags;
            }

            public void setFlags(int flags)
            {
                this.flags = flags;
            }

            public String getValue()
            {
                return value;
            }

            public void setValue(String value)
            {
                if(this.value != null)
                    valueMap.remove(this.value);
                this.value = value;
                valueMap.put(value, this);
            }

            public String toString()
            {
                return getCaption();
            }
        }

        private Map valueMap = new HashMap();

        public void addItem(String value)
        {
            Item item = new Item();
            item.setValue(value);
            add(item);
        }

        public void addItem(String caption, String value)
        {
            Item item = new Item();
            item.setCaption(caption);
            item.setValue(value);
            add(item);
        }

        public Item addItem()
        {
            Item item = new Item();
            add(item);
            return item;
        }

        public Item getItem(int index)
        {
            return (Item) get(index);
        }

        public Item getItemWithValue(String value)
        {
            return (Item) valueMap.get(value);
        }
    }

    private Value value = new GenericValue();

    public PresentationValue()
    {
    }

    public PresentationValue(Value value)
    {
        this.value = value;
    }

    public Items createItems()
    {
        Items items = new Items();
        value.setValue(items);
        return items;
    }

    public Items getItems()
    {
        if(value.isListValue())
            return (Items) value.getValue();
        else
        {
            String textValue = value.getTextValue();
            Items items = createItems();
            items.addItem(textValue);
            return items;
        }
    }

    /*---- DELEGATED WRAPPERS ---*/

    public void appendText(String text) throws ValueException
    {
        value.appendText(text);
    }

    public Class getBindParamValueHolderClass()
    {
        return value.getBindParamValueHolderClass();
    }

    public double getDoubleValue()
    {
        return value.getDoubleValue();
    }

    public int getIntValue()
    {
        return value.getIntValue();
    }

    public List getListValue()
    {
        return value.getListValue();
    }

    public int getListValueType()
    {
        return value.getListValueType();
    }

    public String getTextValue()
    {
        return value.getTextValue();
    }

    public String getTextValueOrBlank()
    {
        return value.getTextValueOrBlank();
    }

    public String getTextValueOrDefault(String defaultText)
    {
        return value.getTextValueOrDefault(defaultText);
    }

    public String[] getTextValues()
    {
        return value.getTextValues();
    }

    public Object getValue()
    {
        return value.getValue();
    }

    public Object getValueForSqlBindParam()
    {
        return value.getValueForSqlBindParam();
    }

    public Class getValueHolderClass()
    {
        return value.getValueHolderClass();
    }

    public boolean hasValue()
    {
        return value.hasValue();
    }

    public boolean isListValue()
    {
        return value.isListValue();
    }

    public void setTextValue(String value) throws ValueException
    {
        this.value.setTextValue(value);
    }

    public void setValue(Object value) throws ValueException
    {
        this.value.setValue(value);
    }

    public void setValue(List value)
    {
        this.value.setValue(value);
    }

    public void setValue(String[] value)
    {
        this.value.setValue(value);
    }

    public void setValueFromSqlResultSet(ResultSet rs, int rowNum, int colIndex) throws SQLException, ValueException
    {
        value.setValueFromSqlResultSet(rs, rowNum, colIndex);
    }
}
