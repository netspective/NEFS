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
package com.netspective.commons.attr;

import java.util.Map;

public interface MutableAttributes extends Attributes
{
    public interface AttributeMutationObserver
    {
        public void observeAttributeAdd(Attributes attributes, Attribute attribute);

        public void observeAttributeChange(Attributes attributes, Attribute attribute);

        public void observeAttributeRemove(Attributes attributes, Attribute attribute);
    }

    public void addMutationObserver(AttributeMutationObserver observer);

    public Attribute addAttribute(final Attribute attribute);

    public void removeAttribute(final Attribute attribute);

    /**
     * Create an auto-managed attribute
     *
     * @param key   The preference key
     * @param value The text value of the preference; will be parsed and checked for string, integer, etc
     *
     * @return The newly created Preference object
     */
    public Attribute createAttribute(final String key, final String value);

    /**
     * Using a Map with various attributes such as 'preferenceKey', 'preferenceValue', and potentially others, construct
     * a preference object plus the map keys and using Java reflection set the keys using setKey(value) format. This
     * allows a preference object to be created using XDM.
     * <p/>
     * The following keys are recognized:
     * 'attributeClass' : if present, the value of this key will be the fully-qualified name of the Preference class that will be instantiated
     * 'attributeName'  : required
     * 'attributeValue' : required in most cases, but may be null if attribute value should return null
     *
     * @param attrMap The key/value pairs that should be assigned to the new attribute object
     *
     * @return The newly created Preference object
     */
    public Attribute createAttribute(final Map attrMap);

    /**
     * Ascertain whether observeAttribute*() should be called
     *
     * @return True if observeAttribute*() should be called, false if they should not be called
     */
    public boolean isObserving();

    /**
     * Specify whether observeAttribute*() should be called
     *
     * @param observing True if observeAttribute*() should be called, false if they should not be called
     */
    public void setObserving(boolean observing);

    /**
     * Any time a preference is added, this method is called
     */
    public void observeAttributeAdd(Attribute attribute);

    /**
     * Any time a preference is changed this method is called
     */
    public void observeAttributeChange(Attribute attribute);

    /**
     * Any time a preference is removed, this method is called
     */
    public void observeAttributeRemove(Attribute attribute);
}
