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
 * $Id: XdmBitmaskedFlagsAttribute.java,v 1.3 2003-04-03 14:07:25 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.exception.UnsupportedBitmaskedFlagsAttributeValueException;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.text.TextUtils;

/**
 * Helper class for attributes that can take multiple flags from a set of a flags.
 */
public abstract class XdmBitmaskedFlagsAttribute
{
    protected int flags = 0;
    public static final String FLAG_DELIMITER = "|";

    public static class FlagDefn
    {
        private String name;
        private int mask;

        public FlagDefn(String name, int mask)
        {
            this.name = name;
            this.mask = mask;
        }

        public int getMask()
        {
            return mask;
        }

        public void setMask(int mask)
        {
            this.mask = mask;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

    /**
     * This is the only method a subclass needs to implement.
     * @return an array holding all possible values of the flags.
     */
    public abstract FlagDefn[] getFlagsDefns();

    public XdmBitmaskedFlagsAttribute()
    {
    }

    public XdmBitmaskedFlagsAttribute(int flags)
    {
        setFlag(flags);
    }

    public void setValue(String value)
    {
        this.flags = 0;

        String[] flagNames = TextUtils.split(value, FLAG_DELIMITER, true);
        FlagDefn[] flagDefns = getFlagsDefns();
        for(int i = 0; i < flagNames.length; i++)
        {
            String flagName = flagNames[i];
            boolean found = false;
            for(int j = 0; j < flagDefns.length; j++)
            {
                FlagDefn flagDefn = flagDefns[j];
                if(flagDefn.name.equalsIgnoreCase(flagName))
                {
                    found = true;
                    this.flags |= flagDefns[j].mask;
                }
            }

            if(! found)
                throw new RuntimeException("Invalid "+ this.getClass().getName() +" value: " + value + " (flag '"+ flagName +"' not found)");
        }
    }

    public void setValue(XdmParseContext pc, Object element, String attribute, String value) throws DataModelException
    {
        try
        {
            setValue(value);
        }
        catch (RuntimeException e)
        {
            UnsupportedBitmaskedFlagsAttributeValueException bfae = new UnsupportedBitmaskedFlagsAttributeValueException(pc, this, element, attribute, value);
            pc.addError(bfae);
            if(pc.isThrowErrorException())
                throw e;
        }
    }

    public final long getFlags()
    {
        return flags;
    }

    public final boolean flagIsSet(long flag)
    {
        return (flags & flag) == 0 ? false : true;
    }

    public final void setFlag(long flag)
    {
        flags |= flag;
    }

    public final void clearFlag(long flag)
    {
        flags &= ~flag;
    }

    public final void updateFlag(long flag, boolean set)
    {
        if(set) flags |= flag; else flags &= ~flag;
    }

    public final void copy(XdmBitmaskedFlagsAttribute flags)
    {
        FlagDefn[] flagDefns = flags.getFlagsDefns();
        for(int i = 0; i < flagDefns.length; i++)
        {
            int copyMask = flagDefns[i].mask;
            updateFlag(copyMask, flags.flagIsSet(copyMask));
        }
    }

    public String[] getFlagNames()
    {
        FlagDefn[] flagDefns = getFlagsDefns();
        String[] result = new String[flagDefns.length];
        for(int i = 0; i < flagDefns.length; i++)
            result[i] = flagDefns[i].name;
        return result;
    }

    public String getFlagsText()
    {
        StringBuffer text = new StringBuffer();

        FlagDefn[] flagDefns = getFlagsDefns();
        for(int i = 0; i < flagDefns.length; i++)
        {
            if((flags & flagDefns[i].mask) != 0)
            {
                if(text.length() > 0)
                    text.append(" " + FLAG_DELIMITER + " ");
                text.append(flagDefns[i].name);
            }
        }

        return text.toString();
    }
}
