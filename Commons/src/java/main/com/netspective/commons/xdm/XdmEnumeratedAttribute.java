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
 * $Id: XdmEnumeratedAttribute.java,v 1.1 2003-03-13 18:33:12 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.exception.UnsupportedAttributeValueException;
import com.netspective.commons.xdm.XdmParseContext;

/**
 * Helper class for attributes that can only take one of a fixed list
 * of values.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public abstract class XdmEnumeratedAttribute
{
    private static final int UNKNOWN_VALUE_INDEX = -1;
    protected int valueIndex = UNKNOWN_VALUE_INDEX;
    protected String value;

    /**
     * This is the only method a subclass needs to implement.
     *
     * @return an array holding all possible values of the enumeration.
     */
    public abstract String[] getValues();

    public XdmEnumeratedAttribute()
    {
    }

    public XdmEnumeratedAttribute(int valueIndex)
    {
        this.value = getValues()[valueIndex];
        this.valueIndex = valueIndex;
    }

    public void setValue(String value)
    {
        int indexFound = getValueIndex(value);
        if (indexFound == UNKNOWN_VALUE_INDEX)
            throw new RuntimeException("Invalid "+ this.getClass().getName() +" value: " + value);
        else
        {
            this.value = value;
            this.valueIndex = indexFound;
        }
    }

    public void setValue(XdmParseContext pc, Object element, String attribute, String value) throws DataModelException
    {
        int indexFound = getValueIndex(value);
        if (indexFound == UNKNOWN_VALUE_INDEX)
        {
            UnsupportedAttributeValueException e = new UnsupportedAttributeValueException(pc, this, element, attribute, value);
            pc.addError(e);
            if(pc.isThrowErrorException())
                throw e;
        }
        else
        {
            this.value = value;
            this.valueIndex = indexFound;
        }
    }

    /**
     * Is this value included in the enumeration?
     */
    public final int getValueIndex(String value)
    {
        String[] values = getValues();
        if (values == null || value == null)
            return UNKNOWN_VALUE_INDEX;

        for (int i = 0; i < values.length; i++)
        {
            if (value.equals(values[i]))
            {
                return i;
            }
        }
        return UNKNOWN_VALUE_INDEX;
    }

    /**
     * Is this value included in the enumeration?
     */
    public final boolean containsValue(String value)
    {
        String[] values = getValues();
        if (values == null || value == null)
        {
            return false;
        }

        for (int i = 0; i < values.length; i++)
        {
            if (value.equals(values[i]))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the value.
     */
    public final String getValue()
    {
        return value;
    }

    public final int getValueIndex()
    {
        return valueIndex;
    }
}
