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
 * $Id: SelectFieldChoicesValueSource.java,v 1.2 2003-11-26 17:31:42 shahid.shah Exp $
 */

package com.netspective.sparx.form.field.type;

import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.value.Value;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.source.AbstractValueSource;

public class SelectFieldChoicesValueSource extends AbstractValueSource
{
    public class Items extends ArrayList
    {
        public class Item
        {
            private ValueSource caption = ValueSource.NULL_VALUE_SOURCE;
            private ValueSource value = ValueSource.NULL_VALUE_SOURCE;

            public Item()
            {
            }

            public ValueSource getCaption()
            {
                return caption;
            }

            public void setCaption(ValueSource caption)
            {
                this.caption = caption;
            }

            public ValueSource getValue()
            {
                return value;
            }

            public void setValue(ValueSource value)
            {
                this.value = value;
            }

            public void addText(String text)
            {
                setCaption(ValueSources.getInstance().getValueSourceOrStatic(text));
            }
        }

        private List items = new ArrayList();

        public Item createItem()
        {
            return new Item();
        }

        public void addItem(Item item)
        {
            add(item);
        }

        public Item getItem(int index)
        {
            return (Item) get(index);
        }

        public List getItems()
        {
            return items;
        }

        // required for XDM because XmlDataModelSchema.Options().setIgnorePcData(true) cannot be used in inner class
        public void addText(String text)
        {
            // do nothing, we're ignoring the text
        }
    }

    private Items items = new Items();

    public SelectFieldChoicesValueSource()
    {
    }

    public Items createItems()
    {
        return items;
    }

    public void addItems(Items items)
    {
        // do nothing, just here as placeholder for XDM
    }

    public Items getItems()
    {
        return items;
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        PresentationValue pValue = new PresentationValue();
        PresentationValue.Items choices = pValue.createItems();
        for (int i=0; i < items.size(); i++)
        {
            Items.Item item = items.getItem(i);
            choices.addItem(item.getCaption().getTextValue(vc), item.getValue().getTextValue(vc));
        }
        return pValue;
    }

    public String[] getTextValues(ValueContext vc)
    {
        String[] values = new String[items.size()];
        for (int i=0; i < items.size(); i++)
        {
            Items.Item item = items.getItem(i);
            values[i] = item.getValue().getTextValue(vc);
        }
        return values;
    }

    public Value getValue(ValueContext vc)
    {
        return getPresentationValue(vc);
    }

    public boolean hasValue(ValueContext vc)
    {
        return items.size() > 0;
    }
}
