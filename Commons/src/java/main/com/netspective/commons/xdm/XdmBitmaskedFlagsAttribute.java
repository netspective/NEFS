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
 * $Id: XdmBitmaskedFlagsAttribute.java,v 1.5 2003-04-04 17:02:21 shahid.shah Exp $
 */

package com.netspective.commons.xdm;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.exception.UnsupportedBitmaskedFlagsAttributeValueException;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.text.TextUtils;

/**
 * Helper class for attributes that can take multiple flags from a set of a flags.
 */
public abstract class XdmBitmaskedFlagsAttribute implements Cloneable
{
    private static final Log log = LogFactory.getLog(XdmBitmaskedFlagsAttribute.class);

    public static final int ACCESS_XDM = 1;      // available via XML
    public static final int ACCESS_PRIVATE = 2;  // available only to Java

    protected int flags = 0;
    protected Map flagDefnsByName;
    protected Map flagSetterXmlNodeNames;
    public static final String FLAG_DELIMITER = "|";

    public static class FlagDefn
    {
        private int access;
        private String name;
        private int mask;

        public FlagDefn(int access, String name, int mask)
        {
            this.access = access;
            this.name = name;
            this.mask = mask;
        }

        public int getAccess()
        {
            return access;
        }

        public void setAccess(int access)
        {
            this.access = access;
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

    public Map getFlagDefnsByName()
    {
        if(flagDefnsByName == null)
        {
            flagDefnsByName = new HashMap();
            flagSetterXmlNodeNames = new HashMap();

            FlagDefn[] flagDefns = getFlagsDefns();
            for(int i = 0; i < flagDefns.length; i++)
            {
                FlagDefn flagDefn = flagDefns[i];
                if(flagDefns == null)
                    throw new RuntimeException("Flags "+ i +" in " + this.getClass().getName() + " is null");

                String flagName = flagDefn.getName();
                String javaId = TextUtils.xmlTextToJavaIdentifier(flagName.toLowerCase(), false);
                String xmlNodeName = TextUtils.javaIdentifierToXmlNodeName(javaId).toLowerCase();

                flagDefnsByName.put(flagName, flagDefns);
                flagDefnsByName.put(javaId, flagDefn);
                flagDefnsByName.put(xmlNodeName, flagDefn);

                if(flagDefn.access == ACCESS_XDM)
                    flagSetterXmlNodeNames.put(xmlNodeName, flagDefn);
            }
        }

        return flagDefnsByName;
    }

    public Map getFlagSetterXmlNodeNames()
    {
        if(flagSetterXmlNodeNames == null)
            getFlagDefnsByName();

        return flagSetterXmlNodeNames;
    }

    public XdmBitmaskedFlagsAttribute()
    {
    }

    public XdmBitmaskedFlagsAttribute cloneFlags()
    {
        try
        {
            return (XdmBitmaskedFlagsAttribute) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            log.error(e);
            return null;
        }
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

    public void setFlag(long flag)
    {
        flags |= flag;
    }

    public void clearFlag(long flag)
    {
        flags &= ~flag;
    }

    public final void updateFlag(long flag, boolean set)
    {
        if(set) setFlag(flag); else clearFlag(flags);
    }

    public final boolean updateFlag(String flagName, boolean set)
    {
        FlagDefn flagDefn = (FlagDefn) getFlagDefnsByName().get(flagName);
        if(flagDefn == null)
            return false;
        else
        {
            updateFlag(flagDefn.getMask(), set);
            return true;
        }
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
        if(flagDefns == null)
            return new String[] { this.getClass().getName() + ".getFlagsDefns() is NULL" };

        String[] result = new String[flagDefns.length];
        for(int i = 0; i < flagDefns.length; i++)
            result[i] = flagDefns[i] != null ? flagDefns[i].getName() : (this.getClass().getName() + ".getFlagsDefns()["+i +"] is NULL");
        return result;
    }

    public String[] getFlagNamesWithXdmAccess()
    {
        FlagDefn[] flagDefns = getFlagsDefns();
        if(flagDefns == null)
            return new String[] { this.getClass().getName() + ".getFlagsDefns() is NULL" };

        List result = new ArrayList();
        for(int i = 0; i < flagDefns.length; i++)
        {
            if(flagDefns[i] != null && flagDefns[i].getAccess() != ACCESS_XDM)
                continue;
            result.add(flagDefns[i] != null ? flagDefns[i].getName() : (this.getClass().getName() + ".getFlagsDefns()["+i +"] is NULL"));
        }
        return (String[]) result.toArray(new String[result.size()]);
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
                text.append(flagDefns[i].getName());
            }
        }

        return text.toString();
    }
}
