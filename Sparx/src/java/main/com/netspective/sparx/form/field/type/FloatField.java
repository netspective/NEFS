/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: FloatField.java,v 1.3 2003-07-08 20:15:06 shahid.shah Exp $
 */

package com.netspective.sparx.form.field.type;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.field.type.TextField;
import com.netspective.sparx.form.field.DialogFieldValue;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldValidations;
import com.netspective.commons.value.exception.ValueException;
import com.netspective.commons.validate.rule.FloatValueValidationRule;

public class FloatField extends TextField
{
    public class FloatFieldState extends TextFieldState
    {
        public class FloatFieldValue extends TextFieldValue
        {
            public Class getValueHolderClass()
            {
                return Float.class;
            }

            public void setTextValue(String value) throws ValueException
            {
                if(value == null || value.length() == 0)
                {
                    setValue((Float) null);
                    return;
                }

                try
                {
                    setValue(new Float(Float.parseFloat(value)));
                }
                catch (NumberFormatException e)
                {
                    setInvalidText(value);
                    invalidate(getDialogContext(), getErrorCaption().getTextValue(getDialogContext()) + " requires a value in float format ("+ e.getMessage() +").");
                }
            }

            public float getFloatValue()
            {
                Float floatValue = (Float) getValue();
                if(floatValue != null)
                    return floatValue.floatValue();
                else
                    return (float) 0.0;
            }

            public Float getFloatObject()
            {
                return (Float) getValue();
            }

            public void setFloatValue(float value)
            {
                setValue(new Float(value));
            }
        }

        public FloatFieldState(DialogContext dc)
        {
            super(dc);
        }

        public DialogFieldValue constructValueInstance()
        {
            return new FloatFieldValue();
        }
    }

    private FloatValueValidationRule floatValidationRule;

	public FloatField()
	{
		super();
		setSize(10);
	}

    public DialogField.State constructStateInstance(DialogContext dc)
    {
        return new FloatFieldState(dc);
    }

    public Class getStateClass()
    {
        return FloatFieldState.class;
    }

    public Class getStateValueClass()
    {
        return FloatFieldState.FloatFieldValue.class;
    }

    public DialogFieldValidations constructValidationRules()
    {
        DialogFieldValidations rules = super.constructValidationRules();
        floatValidationRule = new FloatValueValidationRule();
        floatValidationRule.setMin(Float.MIN_VALUE);
        floatValidationRule.setMax(Float.MAX_VALUE);
        rules.addRule(floatValidationRule);
        return rules;
    }

    public void setInvalidRangeMessage(String invalidRangeMessage)
    {
        floatValidationRule.setInvalidRangeMessage(invalidRangeMessage);
    }

    public void setInvalidTypeMessage(String invalidTypeMessage)
    {
        floatValidationRule.setInvalidTypeMessage(invalidTypeMessage);
    }

    public void setMax(float max)
    {
        floatValidationRule.setMax(max);
    }

    public void setMin(float min)
    {
        floatValidationRule.setMin(min);
    }

    public void addValidation(DialogFieldValidations rules)
    {
        super.addValidation(rules);

        // hang onto the float validation rule since we're going to need it for javascript definition and rendering
        boolean found = false;
        for(int i = 0; i < rules.size(); i++)
        {
            if(rules.get(i) instanceof FloatValueValidationRule)
            {
                if(found)
                    throw new RuntimeException("Multiple float validation rules not allowed.");

                floatValidationRule = (FloatValueValidationRule) rules.get(i);
                found = true;
            }
        }
    }

	public String getCustomJavaScriptDefn(DialogContext dc)
	{
		StringBuffer buf = new StringBuffer(super.getCustomJavaScriptDefn(dc));

		if(floatValidationRule.getMin() != Float.MIN_VALUE)
			buf.append("field.minValue = " + floatValidationRule.getMin() + ";\n");
		if(floatValidationRule.getMax() != Float.MAX_VALUE)
			buf.append("field.maxValue = " + floatValidationRule.getMax() + ";\n");

		return buf.toString();
	}
}
