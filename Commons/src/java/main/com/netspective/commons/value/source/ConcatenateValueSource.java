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
package com.netspective.commons.value.source;

import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSourceSpecification;

/**
 * Value source class for appending and prepending strings to the value
 * of another value source.
 */
public class ConcatenateValueSource extends AbstractValueSource
{
    public final static String[] IDENTIFIERS = new String[]{"concat"};
    private ValueSource valueSource;
    private String appendText;
    private String prependText;

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public ConcatenateValueSource(String prependText, ValueSource valueSource, String appendText)
    {
        this.appendText = appendText;
        this.prependText = prependText;
        this.valueSource = valueSource;
    }

    /**
     * Gets the value source specification. This actually returns the specification of
     * the value source to which strings are being appended/prepended. There is no
     * specification defined for the ConcatenateValueSource since it cannot be created
     * using ValueSourceSpecification currently.
     *
     * @return
     */
    public ValueSourceSpecification getSpecification()
    {
        return valueSource.getSpecification();
    }

    public Value getValue(ValueContext vc)
    {
        Value value = valueSource.getValue(vc);
        String text = value.getTextValue();
        if (appendText == null && prependText == null)
            return value;

        StringBuffer result = new StringBuffer();
        if (prependText != null)
            result.append(prependText);
        result.append(text);
        if (appendText != null)
            result.append(appendText);

        return new GenericValue(result.toString());
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        return new PresentationValue(getValue(vc));
    }

    public boolean hasValue(ValueContext vc)
    {
        return valueSource.hasValue(vc);
    }
}
