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
package com.netspective.commons.value;

import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.commons.value.source.StaticValueSource;

public interface ValueSource
{
    public static final ValueSource NULL_VALUE_SOURCE = new StaticValueSource();

    /**
     * Returns the complete set of tokens used to initialize a particular instance of a value source.
     */
    public ValueSourceSpecification getSpecification();

    /**
     * Initialize the value source from a command string.
     *
     * @param srcTokens The actual value source tokens used to construct the class and pass along parameters
     */
    public void initialize(ValueSourceSpecification srcTokens) throws ValueSourceInitializeException;

    /**
     * Determines whether or not a value is present in this value source.
     *
     * @param vc The context under which determination is made.
     *
     * @return True if there is a value available, false if there's no value available.
     */
    public boolean hasValue(ValueContext vc);

    /**
     * Returns the value associate with this value source. The value may
     * be either a single value or a list of values. This method is called
     * when the caller needs the value for usage in a non-presentation
     * environment (for and algorithm or calculation or persistence). For
     * presentation to an end user via a user interface, use the
     * getPresentationValue() method.
     */
    public Value getValue(ValueContext vc);

    /**
     * Returns the value associate with this value source. The value may
     * be either a single value or a list of values. This method is called
     * when the caller needs the value for presentation to an end user
     * via a user interface like a combo box or select list. The main
     * difference between getValue() and getPresentationValue() is that
     * the value returned may contain more than one entry per item. In the
     * case of usage within a select item list, the first entry per item
     * will be what the user should be shown and the second entry per item
     * will be what should be stored in the database.
     */
    public PresentationValue getPresentationValue(ValueContext vc);

    /**
     * Returns the caption associated with a particular presentation value.
     * Uses the getPresentationValue(ValueContext) method to obtain the captions
     * and values, then searches for the value, and returns the caption matching
     * the given value. Returns null if not found.
     */
    public PresentationValue.Items.Item getPresentationItem(ValueContext vc, String value);

    /**
     * Simple wrapper method that basically does and optimized version of the following:
     * getValue(vc) != null ? getValue().getTextValue(vc) : null.
     */
    public String getTextValue(ValueContext vc);

    /**
     * Simple wrapper method that basically does and optimized version of the following:
     * getValue(vc) != null ? getValue().getTextValueOrBlank(vc) : null.
     */
    public String getTextValueOrBlank(ValueContext vc);

    /**
     * Simple wrapper method that basically does and optimized version of the following:
     * getValue() != null ? getValue().getTextValues(vc) : null.
     */
    public String[] getTextValues(ValueContext vc);
}
