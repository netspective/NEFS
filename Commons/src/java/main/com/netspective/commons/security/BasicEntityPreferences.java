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
 * $Id: BasicEntityPreferences.java,v 1.1 2004-08-08 22:53:32 shahid.shah Exp $
 */

package com.netspective.commons.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.xdm.XmlDataModelSchema;

public class BasicEntityPreferences implements EntityPreferences, MutableEntityPreferences, java.io.Serializable
{
    private static final Log log = LogFactory.getLog(BasicEntityPreferences.class);
    private Map preferences = new PreferencesMap();
    private XmlDataModelSchema schema = XmlDataModelSchema.getSchema(getClass());

    public EntityPreference getPreference(String key)
    {
        return (EntityPreference) preferences.get(key);
    }

    public String getPreferenceValue(String key, String defaultValue)
    {
        EntityPreference result = getPreference(key);
        return result != null ? result.getPreferenceValue() : defaultValue;
    }

    public Map getPreferences()
    {
        return preferences;
    }

    public EntityPreference createPreference()
    {
        return new BasicEntityPreference();
    }

    public void addPreference(EntityPreference preference)
    {
        preferences.put(preference.getPreferenceKey(), preference);
    }

    protected class PreferencesMap implements Map, java.io.Serializable
    {
        private Map wrappedMap = new HashMap();

        public PreferencesMap()
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
         * When adding a EntityPreference to the map, give the opportunity for the EntityPreferences object to define
         * setXXX(yyy) where XXX is the preference key and yyy is the preference value. It uses XDM functionality to
         * define mutators with specific preference names at java getter/setters. For example, if a preference is named
         * "max-login-attempts" and it's value is an integer "5" then EntityPreferences.setMaxLoginAttempts(5) will be
         * called. If a method is available, it will be used as type-specific call.
         * @param key The preference key
         * @param value The EntityPreference instance
         * @return
         */
        public Object put(Object key, Object value)
        {
            EntityPreference pref = (EntityPreference) value;
            try
            {
                schema.assignInstanceValue(BasicEntityPreferences.this, pref.getPreferenceKey(), pref.getPreferenceValue(), false);
            }
            catch (Exception e)
            {
                log.error(e);
            }
            return wrappedMap.put(key, value);
        }
    }
}
