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
 * $Id: DialogFieldValueSource.java,v 1.5 2004-07-26 15:27:31 shahid.shah Exp $
 */

package com.netspective.sparx.value.source;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletRequest;

import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.AbstractValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.sparx.value.ServletValueContext;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.Dialog;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.value.DatabaseConnValueContext;

public class DialogFieldValueSource extends AbstractValueSource
{
    public static final String[] IDENTIFIERS = new String[] { "field", "dialog-field" };
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation(
            "Provides access to a specific field of a dialog.",
            new ValueSourceDocumentation.Parameter[]
            {
                new ValueSourceDocumentation.Parameter("field-name", true, "The name of the field.")
            }
    );

    private String fieldName;
    private ServletRequestParameterValueSource servletRequestParameterValueSource;

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static ValueSourceDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);
        fieldName = spec.getParams();
        servletRequestParameterValueSource = new ServletRequestParameterValueSource();
        servletRequestParameterValueSource.initialize(new ValueSourceSpecification(Dialog.PARAMNAME_CONTROLPREFIX + fieldName));
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        return new PresentationValue(getValue(vc));
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public Value getValue(final ValueContext vc)
    {
        if(vc instanceof DialogContext)
        {
            return ((DialogContext) vc).getFieldStates().getState(fieldName).getValue();
        }
        else
        {
            if (vc instanceof ConnectionContext)
            {
                DatabaseConnValueContext databaseValueContext = ((ConnectionContext) vc).getDatabaseValueContext();
                if (databaseValueContext instanceof DialogContext)
                    return ((DialogContext) databaseValueContext).getFieldStates().getState(fieldName).getValue();
                else
                {
                    ServletValueContext svc = (ServletValueContext) databaseValueContext;
                    ServletRequest request =  svc.getRequest();
                    DialogContext dc = (DialogContext) request.getAttribute(DialogContext.DIALOG_CONTEXT_ATTR_NAME);
                    if(dc != null)
                        return dc.getFieldStates().getState(fieldName).getValue();
                    else
                        return servletRequestParameterValueSource.getValue(vc);
                }
            }
            else
            {
               ServletRequest request = ((ServletValueContext) vc).getRequest();
                DialogContext dc = (DialogContext) request.getAttribute(DialogContext.DIALOG_CONTEXT_ATTR_NAME);
                if(dc != null)
                    return dc.getFieldStates().getState(fieldName).getValue();
                else
                    return servletRequestParameterValueSource.getValue(vc);
            }

        }

    }

    public boolean hasValue(ValueContext vc)
    {
        Value value = getValue(vc);
        return value.getTextValue() != null;
    }
}