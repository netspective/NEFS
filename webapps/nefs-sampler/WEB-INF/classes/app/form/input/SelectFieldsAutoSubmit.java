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
package app.form.input;

import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.sparx.Project;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.field.DialogFieldStates;
import com.netspective.sparx.form.field.DialogFieldValue;
import com.netspective.sparx.form.field.type.SelectField;

public class SelectFieldsAutoSubmit extends Dialog
{
    public SelectFieldsAutoSubmit(Project project)
    {
        super(project);
    }

    public SelectFieldsAutoSubmit(Project project, DialogsPackage pkg)
    {
        super(project, pkg);
    }

    public void finalizeContents()
    {
        super.finalizeContents();

        // we are creating a dynamic value source so that the partner can be filled in using the selected value of the
        // main field (if this was a "normal" value source we would just assign it in the XML 'choices' attribute)
        SelectField related = (SelectField) getFields().getByName("sel_field_related");
        related.setChoices(new PartnerValueSource());
    }

    public boolean isValid(DialogContext dc)
    {
        if(!super.isValid(dc))
            return false;

        // if our related field is not populated yet, be sure set the dialog as invalid so that we don't execute yet
        DialogFieldStates dfs = dc.getFieldStates();
        if(!dfs.getState("sel_field_related").hasRequiredValue())
            return false;

        // if we have a value in the partner then we're ok.
        return true;
    }

    protected class PartnerValueSource extends AbstractValueSource
    {
        public PartnerValueSource()
        {
            super();
        }

        public boolean hasValue(ValueContext vc)
        {
            return true;
        }

        public Value getValue(ValueContext vc)
        {
            return getPresentationValue(vc);
        }

        public PresentationValue getPresentationValue(ValueContext vc)
        {
            DialogContext dc = (DialogContext) vc;
            DialogFieldStates dfs = dc.getFieldStates();
            DialogFieldValue mainValue = dfs.getState("sel_field_main").getValue();

            PresentationValue result = new PresentationValue();
            if(mainValue.hasValue())
            {
                String mainTextValue = mainValue.getTextValue();
                PresentationValue.Items items = result.createItems();
                for(int i = 0; i < 5; i++)
                    items.addItem("Choice " + mainTextValue + " " + i, mainTextValue + i);
            }

            return result;
        }
    }

}
