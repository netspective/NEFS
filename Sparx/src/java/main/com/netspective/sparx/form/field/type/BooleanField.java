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
 * $Id: BooleanField.java,v 1.1 2003-05-13 02:13:39 shahid.shah Exp $
 */

package com.netspective.sparx.form.field.type;

import java.io.IOException;
import java.io.Writer;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogContextMemberInfo;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldValue;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.exception.ValueException;
import com.netspective.commons.text.TextUtils;

public class BooleanField extends DialogField
{
    public class BooleanFieldState extends State
    {
        public class BooleanFieldValue extends BasicStateValue
        {
            public Class getValueHolderClass()
            {
                return Boolean.class;
            }

            public void setTextValue(String value) throws ValueException
            {
                setValue(value == null ? null : (new Boolean(TextUtils.toBoolean(value))));
            }
        }

        public BooleanFieldState(DialogContext dc)
        {
            super(dc);
        }

        public DialogFieldValue constructValueInstance()
        {
            return new BooleanFieldValue();
        }
    }

    private BooleanFieldStyle style = new BooleanFieldStyle(BooleanFieldStyle.RADIO);
    private BooleanFieldChoices choices;
    private ValueSource trueText;
    private ValueSource falseText;
    private ValueSource noneText;

    public BooleanField(Dialog owner)
    {
        super(owner);
    }

    public BooleanField(DialogField parent)
    {
        super(parent);
    }

    public void initialize()
    {
        super.initialize();
        setChoices(new BooleanFieldChoices(BooleanFieldChoices.YESNO));
    }

    public DialogField.State constructStateInstance(DialogContext dc)
    {
        return new BooleanFieldState(dc);
    }

    public BooleanFieldChoices getChoices()
    {
        return choices;
    }

    public void setChoices(BooleanFieldChoices choices)
    {
        this.choices = choices;
        int choicesTextIndex = choices.getValueIndex() * 2;
        setFalseText(BooleanFieldChoices.TEXT_CHOICES[choicesTextIndex]);
        setTrueText(BooleanFieldChoices.TEXT_CHOICES[choicesTextIndex+1]);
    }

    public ValueSource getFalseText()
    {
        return falseText;
    }

    public void setFalseText(ValueSource falseText)
    {
        this.falseText = falseText;
    }

    public ValueSource getNoneText()
    {
        return noneText;
    }

    public void setNoneText(ValueSource noneText)
    {
        this.noneText = noneText;
    }


    public BooleanFieldStyle getStyle()
    {
        return style;
    }

    public void setStyle(BooleanFieldStyle style)
    {
        this.style = style;
    }

    public ValueSource getTrueText()
    {
        return trueText;
    }

    public void setTrueText(ValueSource trueText)
    {
        this.trueText = trueText;
    }

    public void renderControlHtml(Writer writer, DialogContext dc) throws IOException
    {
        if(isInputHidden(dc))
        {
            writer.write(getHiddenControlHtml(dc));
            return;
        }

        String falseTextStr = falseText.getTextValue(dc);
        String trueTextStr = trueText.getTextValue(dc);
        String noneTextStr = noneText != null ? noneText.getTextValue(dc) : null;

        BooleanFieldState.BooleanFieldValue dfvalue = (BooleanFieldState.BooleanFieldValue) dc.getFieldStates().getState(this).getValue();
        Boolean bValue = (Boolean) dfvalue.getValue();
        boolean value = bValue == null ? false : bValue.booleanValue();
        String strValue = bValue == null ? "" : bValue.toString();
        String boolValueCaption = bValue == null ? "" : (value ? trueTextStr : falseTextStr);

        if(isReadOnly(dc))
        {
            if (this.noneText == null) {
                writer.write("<input type='hidden' name='" + getHtmlFormControlId() + "' value='" + (strValue != null ? strValue : "") + "'><span id='" + getQualifiedName() + "'>" + (value ? trueTextStr : falseTextStr) + "</span>");
            } else {
                writer.write("<input type='hidden' name='" + getHtmlFormControlId() + "' value='" +
                        (strValue != null ? strValue : "") + "'><span id='" + getQualifiedName() + "'>" +
                        boolValueCaption +
                        "</span>");
            }
            return;
        }

        String id = getHtmlFormControlId();
        String defaultControlAttrs = dc.getSkin().getDefaultControlAttrs();
        switch(style.getValueIndex())
        {
            case BooleanFieldStyle.RADIO:
                if (noneTextStr != null)
                {
                    String[] val = { "" , "" , "" };
                    setChecked (strValue, val);
                    writer.write(
                        "<nobr><input type='radio' name='" + id + "' id='" + id + "0' value='0' " + val[0] + defaultControlAttrs + "> <label for='" + id + "0'>" + falseTextStr + "</label></nobr> " +
                        "<nobr><input type='radio' name='" + id + "' id='" + id + "1' value='1' " + val[1] + defaultControlAttrs + "> <label for='" + id + "1'>" + trueTextStr + "</label></nobr> " +
                        "<nobr><input type='radio' name='" + id + "' id='" + id + "2' value='2' " + val[2] + defaultControlAttrs + "> <label for='" + id + "2'>" + noneTextStr + "</label></nobr>");
                }
                else
                {
                    writer.write(
                        "<nobr><input type='radio' name='" + id + "' id='" + id + "0' value='0' " + (value ? "" : "checked ") + defaultControlAttrs + "> <label for='" + id + "0'>" + falseTextStr + "</label></nobr> " +
                        "<nobr><input type='radio' name='" + id + "' id='" + id + "1' value='1' " + (value ? "checked " : "") + defaultControlAttrs + "> <label for='" + id + "1'>" + trueTextStr + "</label></nobr>");
                }
                break;

            case BooleanFieldStyle.CHECK:
                writer.write("<nobr><input type='checkbox' name='" + id + "' id='" + id + "' value='1' " + (value ? "checked " : "") + defaultControlAttrs + "> <label for='" + id + "'>" + super.getCaption().getTextValue(dc) + "</label></nobr>");
                break;

            case BooleanFieldStyle.CHECKALONE:
                writer.write("<input type='checkbox' name='" + id + "' value='1' " + (value ? "checked " : "") + defaultControlAttrs + "> ");
                break;

            case BooleanFieldStyle.COMBO:
                writer.write(
                        "<select name='" + id + "' " + defaultControlAttrs + ">" +
                        "<option " + (value ? "" : "selected") + " value='0'>" + falseTextStr + "</option>" +
                        "<option " + (value ? "selected" : "") + " value='1'>" + trueTextStr + "</option>" +
                        "</select>");
                break;

            default:
                writer.write("Unknown style " + style);
        }
    }

   /**
     * Produces Java code when a custom DialogContext is created
     */
    public DialogContextMemberInfo getDialogContextMemberInfo()
    {
        DialogContextMemberInfo mi = createDialogContextMemberInfo("boolean");
        String fieldName = mi.getFieldName();
        String memberName = mi.getMemberName();
        String dataType = mi.getDataType();

        mi.addJavaCode("\tpublic " + dataType + " get" + memberName + "() { Boolean o = (Boolean) getValueAsObject(\"" + fieldName + "\"); return o == null ? false : o.booleanValue(); }\n");
        mi.addJavaCode("\tpublic " + dataType + " get" + memberName + "(" + dataType + " defaultValue) { Boolean o = (Boolean) getValueAsObject(\"" + fieldName + "\"); return o == null ? defaultValue : o.booleanValue(); }\n");
        mi.addJavaCode("\tpublic void set" + memberName + "(" + dataType + " value) { setValue(\"" + fieldName + "\", value == true ? \"1\" : \"0\"); }\n");

        return mi;
    }

    private void setChecked (String strValue, String[] val)
    {
        int index;
        if (strValue != null)
        {
            try
            {
                index = Integer.parseInt (strValue);
                val[index] = " checked ";
            }
            catch (NumberFormatException e) { }
        }
    }
}
