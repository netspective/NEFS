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
package com.netspective.commons.validate.rule;

import com.netspective.commons.validate.ValidationContext;
import com.netspective.commons.validate.ValidationUtils;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueSource;

public class IntegerValueValidationRule extends BasicValidationRule
{
    private String invalidRangeMessage = "{0} must be between {1} and {2}.";
    private String invalidMultipleMessage = "{0} must be a multiple of {1}.";
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;
    private int multipleOf = 0;

    public IntegerValueValidationRule()
    {
        super();
    }

    public IntegerValueValidationRule(ValueSource caption)
    {
        super(caption);
    }

    public int getMin()
    {
        return min;
    }

    public void setMin(int min)
    {
        this.min = min;
    }

    public int getMax()
    {
        return max;
    }

    public void setMax(int max)
    {
        this.max = max;
    }

    public int getMultipleOf()
    {
        return multipleOf;
    }

    public void setMultipleOf(int multipleOf)
    {
        this.multipleOf = multipleOf;
    }

    public String getInvalidRangeMessage()
    {
        return invalidRangeMessage;
    }

    public void setInvalidRangeMessage(String invalidRangeMessage)
    {
        this.invalidRangeMessage = invalidRangeMessage;
    }

    public String getInvalidMultipleMessage()
    {
        return invalidMultipleMessage;
    }

    public void setInvalidMultipleMessage(String invalidMultipleMessage)
    {
        this.invalidMultipleMessage = invalidMultipleMessage;
    }

    public boolean isValid(ValidationContext vc, Value value)
    {
        if (!isValidType(vc, value, Integer.class))
            return false;

        Integer intValue = (Integer) value.getValue();
        if (intValue != null && !ValidationUtils.isInRange(intValue.intValue(), min, max))
        {
            vc.addValidationError(value, getInvalidRangeMessage(),
                    new Object[]{getValueCaption(vc), new Integer(min), new Integer(max)});
            return false;
        }
        // only check for multiple-of when the value is not zero
        if (intValue != null && multipleOf != 0 && !ValidationUtils.isMultipleOf(intValue.intValue(), multipleOf))
        {
            vc.addValidationError(value, getInvalidMultipleMessage(),
                    new Object[]{getValueCaption(vc), new Integer(multipleOf)});
            return false;
        }

        return true;
    }
}
