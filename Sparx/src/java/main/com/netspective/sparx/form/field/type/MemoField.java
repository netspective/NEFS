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
 * $Id: MemoField.java,v 1.3 2003-08-01 21:29:40 aye.thu Exp $
 */

package com.netspective.sparx.form.field.type;

import java.io.IOException;
import java.io.Writer;

import com.netspective.sparx.form.DialogContext;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.text.TextUtils;

public class MemoField extends TextField
{
    public static class Wrap extends XdmEnumeratedAttribute
    {
        public static final int SOFT = 0;
        public static final int HARD = 1;

        public static final String[] VALUES = new String[] { "soft", "hard" };

        public Wrap()
        {
        }

        public Wrap(int valueIndex)
        {
            super(valueIndex);
        }

        public String[] getValues()
        {
            return VALUES;
        }
    }

    protected int rows = 3, cols = 40;
    protected Wrap wordWrap = new Wrap(Wrap.SOFT);

    public MemoField()
    {
        super();
        setMaxLength(4096);
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int newRows)
    {
        rows = newRows;
    }

    public int getCols()
    {
        return cols;
    }

    public void setCols(int newCols)
    {
        cols = newCols;
    }

    public Wrap getWordWrap()
    {
        return wordWrap;
    }

    public void setWordWrap(Wrap wordWrap)
    {
        this.wordWrap = wordWrap;
    }

    public void renderControlHtml(Writer writer, DialogContext dc) throws IOException
    {
        if(isInputHidden(dc))
        {
            writer.write(getHiddenControlHtml(dc));
            return;
        }

        TextFieldState state = (TextFieldState) dc.getFieldStates().getState(this);
        String value = state.getValue().getTextValue();
        String id = getHtmlFormControlId();

        if(isReadOnly(dc))
        {
            String valueStr = value != null ? TextUtils.escapeHTML(value) : "";
            writer.write("<input type='hidden' name='" + id + "' value=\"" + valueStr + "\">" + valueStr);
        }
        else
        {
            writer.write(
                    "<textarea maxlength=\"" + getMaxLength() + "\" id=\"" + id + "\" name=\"" + id + "\" rows=\"" + rows + "\" cols=\"" + cols + "\" wrap=\"" +
                    wordWrap.getValue() + "\"" + (isRequired(dc) ? " class=\"" + dc.getSkin().getControlAreaRequiredStyleClass()+ "\" " : " ") +
                    dc.getSkin().getDefaultControlAttrs() +
                    ">" + (value != null ? TextUtils.escapeHTML(value) : "") + "</textarea>");
        }
    }

    /**
     *
     */
    public String getCustomJavaScriptDefn(DialogContext dc)
    {
        return (super.getCustomJavaScriptDefn(dc) + "field.maxLength = " + getMaxLength() + ";\n");
    }
}
