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
 * $Id: TextField.java,v 1.3 2003-05-09 01:22:20 shahid.shah Exp $
 */

package com.netspective.sparx.form.field.type;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.commons.validate.ValidationRules;
import com.netspective.commons.validate.rule.TextValueValidationRule;
import com.netspective.commons.text.TextUtils;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogContextMemberInfo;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.field.DialogField;

import org.apache.oro.text.perl.MalformedPerl5PatternException;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;

public class TextField extends DialogField
{
    private static final Log log = LogFactory.getLog(TextField.class);
    public static Perl5Util perlUtil = new Perl5Util();

    public static final Flags.FlagDefn[] TEXT_FIELD_FLAG_DEFNS = new Flags.FlagDefn[DialogField.FLAG_DEFNS.length + 4];
    static
    {
        for(int i = 0; i < DialogField.FLAG_DEFNS.length; i++)
            TEXT_FIELD_FLAG_DEFNS[i] = DialogField.FLAG_DEFNS[i];
        TEXT_FIELD_FLAG_DEFNS[DialogField.FLAG_DEFNS.length + 0] = new XdmBitmaskedFlagsAttribute.FlagDefn(Flags.ACCESS_XDM, "MASK_ENTRY", Flags.MASKENTRY);
        TEXT_FIELD_FLAG_DEFNS[DialogField.FLAG_DEFNS.length + 1] = new XdmBitmaskedFlagsAttribute.FlagDefn(Flags.ACCESS_XDM, "UPPERCASE", Flags.UPPERCASE);
        TEXT_FIELD_FLAG_DEFNS[DialogField.FLAG_DEFNS.length + 2] = new XdmBitmaskedFlagsAttribute.FlagDefn(Flags.ACCESS_XDM, "LOWERCASE", Flags.LOWERCASE);
        TEXT_FIELD_FLAG_DEFNS[DialogField.FLAG_DEFNS.length + 3] = new XdmBitmaskedFlagsAttribute.FlagDefn(Flags.ACCESS_XDM, "TRIM", Flags.TRIM);
    }

    public class Flags extends DialogField.Flags
    {
        public static final int MASKENTRY = DialogField.Flags.START_CUSTOM;
        public static final int UPPERCASE = MASKENTRY * 2;
        public static final int LOWERCASE = UPPERCASE * 2;
        public static final int TRIM = LOWERCASE * 2;
        public static final int START_CUSTOM = TRIM * 2;

        public Flags()
        {
        }

        public FlagDefn[] getFlagsDefns()
        {
            return TEXT_FIELD_FLAG_DEFNS;
        }
    }

    private int size = 32;
    private String displayPattern;
    private String formatPattern;
    private TextValueValidationRule textValidationRule;

    public TextField(Dialog owner)
    {
        super(owner);
        initialize();
    }

    public TextField(DialogField parent)
    {
        super(parent);
        initialize();
    }

    public void initialize()
    {
        textValidationRule = new TextValueValidationRule();
        textValidationRule.setMinLength(0);
        textValidationRule.setMaxLength(255);
    }

    public DialogField.Flags createFlags()
    {
        return new Flags();
    }

    public final int getSize()
    {
        return size;
    }

    public void setSize(int value)
    {
        size = value;
    }

    public String getDisplayPattern()
    {
        return displayPattern;
    }

    public void setDisplayPattern(String displayPattern)
    {
        this.displayPattern = displayPattern;
    }

    public String getFormatPattern()
    {
        return formatPattern;
    }

    public void setFormatPattern(String formatPattern)
    {
        this.formatPattern = formatPattern;
    }

    public void addValidation(ValidationRules rules)
    {
        super.addValidation(rules);

        // hang onto the text validation rule since we're going to need it for javascript definition and rendering
        for(int i = 0; i < rules.size(); i++)
        {
            if(rules.get(i) instanceof TextValueValidationRule)
            {
                textValidationRule = (TextValueValidationRule) rules.get(i);
                return;
            }
        }

        textValidationRule = new TextValueValidationRule();
        textValidationRule.setCaption(getCaption());
        textValidationRule.setMinLength(0);
        textValidationRule.setMaxLength(0);
    }

    /**
     * Format the dialog field value for every dialog stage(display/validation stages) but not
     * after successful validation(submit stage).
     *
     * @param   value field value
     * @return String formatted text
     */
    public String formatDisplayValue(String value)
    {
        if(value == null) return null;

        long flags = getFlags().getFlags();
        if((flags & Flags.UPPERCASE) != 0) value = value.toUpperCase();
        if((flags & Flags.LOWERCASE) != 0) value = value.toLowerCase();
        if((flags & Flags.TRIM) != 0) value = value.trim();

        if(this.displayPattern != null)
        {
            synchronized(perlUtil)
            {
                try
                {
                    value = perlUtil.substitute(displayPattern, value);
                }
                catch(MalformedPerl5PatternException e)
                {
                    value = e.toString();
                    log.error(e);
                }
            }
        }
        return value;
    }

    /**
     * Format the dialog field value after successful validation.
     *
     * @param   value field value
     * @return String formatted text
     */
    public String formatSubmitValue(String value)
    {
        if(value == null) return null;

        long flags = getFlags().getFlags();
        if((flags & Flags.UPPERCASE) != 0) value = value.toUpperCase();
        if((flags & Flags.LOWERCASE) != 0) value = value.toLowerCase();
        if((flags & Flags.TRIM) != 0) value = value.trim();

        if(this.formatPattern != null)
        {
            synchronized(perlUtil)
            {
                try
                {
                    value = perlUtil.substitute(formatPattern, value);
                }
                catch(MalformedPerl5PatternException e)
                {
                    value = e.toString();
                    log.error(e);
                }
            }
        }
        return value;
    }

    public void renderControlHtml(Writer writer, DialogContext dc) throws IOException
    {
        if(isInputHidden(dc))
        {
            writer.write(getHiddenControlHtml(dc));
            return;
        }

        DialogField.DialogFieldState state = dc.getFieldStates().getState(this);
        Flags stateFlags = (Flags) state.getStateFlags();
        String textValue = state.getValue().getTextValue();

        if(textValue == null)
            textValue = "";
        else
            textValue = TextUtils.escapeHTML(textValue);

        String className = isRequired(dc) ? dc.getSkin().getControlAreaRequiredStyleClass() : dc.getSkin().getControlAreaStyleClass();

        String controlAreaStyle = dc.getSkin().getControlAreaStyleAttrs();
        if(isReadOnly(dc))
        {
            writer.write("<input type='hidden' name='" + getHtmlFormControlId() + "' value=\"" + textValue + "\"><span id='" + getQualifiedName() + "'>" + textValue + "</span>");
        }
        else if(isBrowserReadOnly(dc))
        {
            className = dc.getSkin().getControlAreaReadonlyStyleClass();
            writer.write("<input type=\"text\" name=\"" + getHtmlFormControlId() + "\" readonly value=\"" +
                    textValue + "\" maxlength=\"" + textValidationRule.getMaxLength() + "\" size=\"" + size + "\" " + controlAreaStyle +
                    " class=\"" + className + "\" " + dc.getSkin().getDefaultControlAttrs() + ">");
        }
        else if(!stateFlags.flagIsSet(Flags.MASKENTRY))
        {
            writer.write("<input type=\"text\" name=\"" + getHtmlFormControlId() + "\" value=\"" + textValue + "\" maxlength=\"" +
                    textValidationRule.getMaxLength() + "\" size=\"" + size + "\" " + controlAreaStyle + " class=\"" + className + "\" " +
                    dc.getSkin().getDefaultControlAttrs() + ">");
        }
        else
        {
            writer.write("<input type=\"password\" name=\"" + getHtmlFormControlId() + "\" value=\"" + textValue + "\" maxlength=\"" +
                    textValidationRule.getMaxLength() + "\" size=\"" + size + "\" " + controlAreaStyle + " class=\"" + className + "\" " +
                    dc.getSkin().getDefaultControlAttrs() + ">");
        }
    }

    /**
     *
     */
    public String getCustomJavaScriptDefn(DialogContext dc)
    {
        StringBuffer buf = new StringBuffer(super.getCustomJavaScriptDefn(dc));

        Flags flags = (Flags) getFlags();

        if(flags.flagIsSet(Flags.UPPERCASE))
            buf.append("field.uppercase = 'yes';\n");
        else
            buf.append("field.uppercase = 'no';\n");

        if(flags.flagIsSet(Flags.LOWERCASE))
            buf.append("field.lowercase = 'yes';\n");
        else
            buf.append("field.lowercase = 'no';\n");

        if(flags.flagIsSet(Flags.TRIM))
            buf.append("field.trim = 'yes';\n");
        else
            buf.append("field.trim = 'no';\n");

        if(textValidationRule != null)
        {
            if(textValidationRule.getRegExpr() != null)
            {
                buf.append("field.text_format_pattern = " + textValidationRule.getRegExpr() + ";\n");
                if(textValidationRule.getInvalidRegExMessage() != null)
                    buf.append("field.text_format_err_msg = \"" + textValidationRule.getInvalidRegExMessage() + "\";\n");
            }
        }

        return buf.toString();
    }

    /**
     * Produces Java code when a custom DialogContext is created
     */
    public DialogContextMemberInfo getDialogContextMemberInfo()
    {
        DialogContextMemberInfo mi = createDialogContextMemberInfo("String");
        String fieldName = mi.getFieldName();
        String memberName = mi.getMemberName();
        String dataType = mi.getDataType();

        mi.addJavaCode("\tpublic " + dataType + " get" + memberName + "() { return getValue(\"" + fieldName + "\"); }\n");
        mi.addJavaCode("\tpublic " + dataType + " get" + memberName + "(" + dataType + " defaultValue) { return getValue(\"" + fieldName + "\", defaultValue); }\n");
        mi.addJavaCode("\tpublic " + dataType + " get" + memberName + "OrBlank() { return getValue(\"" + fieldName + "\", \"\"); }\n");

        mi.addJavaCode("\tpublic String get" + memberName + "String() { return getValue(\"" + fieldName + "\"); }\n");
        mi.addJavaCode("\tpublic String get" + memberName + "String(String defaultValue) { return getValue(\"" + fieldName + "\", defaultValue); }\n");

        mi.addJavaCode("\tpublic Object get" + memberName + "Object() { return getValueAsObject(\"" + fieldName + "\"); }\n");
        mi.addJavaCode("\tpublic Object get" + memberName + "Object(Object defaultValue) { return getValueAsObject(\"" + fieldName + "\", defaultValue); }\n");

        mi.addJavaCode("\tpublic void set" + memberName + "(" + dataType + " value) { setValue(\"" + fieldName + "\", value); }\n");
        mi.addJavaCode("\tpublic void set" + memberName + "Object(" + dataType + " value) { setValue(\"" + fieldName + "\", (" + dataType + ") value); }\n");

        return mi;
    }
}
