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
 * $Id: TextValueValidationRule.java,v 1.5 2004-03-18 14:54:10 shahid.shah Exp $
 */

package com.netspective.commons.validate.rule;

import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Matcher;

import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.validate.ValidationContext;
import com.netspective.commons.validate.ValidationUtils;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;

public class TextValueValidationRule extends BasicValidationRule implements XmlDataModelSchema.ConstructionFinalizeListener
{
    private String invalidLengthMessage = "{0} ({1}) must have a length between {2} and {3}.";
    private String invalidRegExMessage = "{0} ({1}) must match the format {2}.";
    private int minLength = 0;
    private int maxLength = Integer.MAX_VALUE;
    private String regExpr;
    private Pattern regExprPattern;
    private boolean regExprPatternValid;
    private Perl5Matcher regExprMatcher;

    public TextValueValidationRule()
    {
    }

    public void finalizeConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        if(regExpr != null && ! regExprPatternValid)
            pc.addError("Reg-expr '"+ regExpr +"' is not valid at " + pc.getLocator().getSystemId() + " line "+ pc.getLocator().getLineNumber());
    }

    public TextValueValidationRule(ValueSource caption)
    {
        super(caption);
    }

    public String getInvalidLengthMessage()
    {
        return invalidLengthMessage;
    }

    public void setInvalidLengthMessage(String invalidLengthMessage)
    {
        this.invalidLengthMessage = invalidLengthMessage;
    }

    public String getInvalidRegExMessage()
    {
        return invalidRegExMessage;
    }

    public void setInvalidRegExMessage(String invalidRegExMessage)
    {
        this.invalidRegExMessage = invalidRegExMessage;
    }

    public int getMinLength()
    {
        return minLength;
    }

    public void setMinLength(int minLength)
    {
        this.minLength = minLength;
    }

    public int getMaxLength()
    {
        return maxLength;
    }

    public void setMaxLength(int maxLength)
    {
        this.maxLength = maxLength;
    }

    public String getRegExpr()
    {
        return regExpr;
    }

    public void setRegExpr(String regExpr)
    {
        this.regExpr = regExpr;

        if(regExpr != null)
        {
            regExprMatcher = new Perl5Matcher();
            Perl5Compiler p5c = new Perl5Compiler();
            try
            {
                regExprPattern = p5c.compile(regExpr);
                regExprPatternValid = true;
            }
            catch (MalformedPatternException e)
            {
                regExprPattern = null;
                regExprPatternValid = false;
            }
        }
        else
        {
            regExprMatcher = null;
            regExprPatternValid = true;
            regExprPattern = null;
        }
    }

    public boolean isValid(ValidationContext vc, Value value)
    {
        String text = value.getTextValue();
        if(text == null || text.length() == 0)
            return true;

        if(!ValidationUtils.isInRange(text.length(), minLength, maxLength))
        {
            vc.addValidationError(value, getInvalidLengthMessage(),
                                new Object[] { getValueCaption(vc), text, new Integer(minLength), new Integer(maxLength) });
            return false;
        }

        if(regExprPatternValid && !regExprMatcher.matches(text, regExprPattern))
        {
            vc.addValidationError(value, getInvalidRegExMessage(), new Object[] { getValueCaption(vc), text, regExpr });
            return false;
        }

        return true;
    }
}
