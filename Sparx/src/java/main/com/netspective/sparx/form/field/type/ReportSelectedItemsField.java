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
package com.netspective.sparx.form.field.type;

import java.io.IOException;
import java.io.Writer;

import com.netspective.commons.value.PresentationValue;
import com.netspective.sparx.form.DialogContext;

public class ReportSelectedItemsField extends SelectField
{
    public ReportSelectedItemsField()
    {
        setStyle(new Style(Style.MULTILIST));
    }

    public void renderControlHtml(Writer writer, DialogContext dc) throws IOException
    {
        // the 'choices' for this special SELECT field comes from the request param only (not in XDM)

        SelectFieldState state = (SelectFieldState) dc.getFieldStates().getState(this);
        SelectFieldState.SelectFieldValue sfValue = (SelectFieldState.SelectFieldValue) state.getValue();
        String[] values = sfValue.getTextValues();

        PresentationValue pValue = new PresentationValue();
        PresentationValue.Items choices = pValue.createItems();
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                choices.addItem(values[i]);
            }
        }
        sfValue.calcSelections(choices);

        boolean readOnly = isReadOnly(dc);
        String id = getHtmlFormControlId();
        String defaultControlAttrs = dc.getSkin().getDefaultControlAttrs();

        StringBuffer options = new StringBuffer();

        // this field is always hidden no matter what the flags are set to
        for (int i = 0; i < choices.size(); i++)
        {
            PresentationValue.Items.Item choice = choices.getItem(i);
            boolean selected = (choice.getFlags() & PRESENTATIONITEMFLAG_IS_SELECTED) != 0;
            options.append("    <option value=\"" + choice.getValue() + "\" " + (selected ? "selected" : "") + ">" + choice.getCaption() + "</option>\n");
        }
        writer.write("<select name='" + id + "' size='" + getSize() + "' multiple='yes' " + defaultControlAttrs +
                (isInputHidden(dc) ? " style=\"display:none;\"" : "") +
                ">\n" + options + "</select>\n");

    }

}
