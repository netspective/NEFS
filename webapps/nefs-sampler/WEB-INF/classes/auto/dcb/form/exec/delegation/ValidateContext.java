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

package auto.dcb.form.exec.delegation;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.field.DialogFieldFlags;
import com.netspective.sparx.form.field.DialogFieldStates;

public class ValidateContext
{
    public static final String DIALOG_ID = "form.exec.delegation.validate";
    private DialogContext dialogContext;
    private DialogFieldStates fieldStates;

    public ValidateContext(DialogContext dc)
    {
        this.dialogContext = dc;
        this.fieldStates = dc.getFieldStates();
    }

    public DialogContext getDialogContext() { return dialogContext; }

    public com.netspective.sparx.form.field.type.TextField.TextFieldState getFullNameState()
    { return (com.netspective.sparx.form.field.type.TextField.TextFieldState) fieldStates.getState("full_name"); }

    public com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue getFullName()
    {
        return (com.netspective.sparx.form.field.type.TextField.TextFieldState.TextFieldValue) getFullNameState().getValue();
    }

    public DialogFieldFlags getFullNameStateFlags() { return getFullNameState().getStateFlags(); }

    public String getFullNamePrivateRequestParam()
    { return dialogContext.getRequest().getParameter("_dc.full_name"); }

    public String getFullNamePublicRequestParam() { return dialogContext.getRequest().getParameter("full_name"); }

    public com.netspective.sparx.form.field.type.TextField getFullNameField()
    { return (com.netspective.sparx.form.field.type.TextField) getFullNameState().getField(); }

    public com.netspective.sparx.form.field.type.DateTimeField.DateTimeFieldState getBirthDateState()
    {
        return (com.netspective.sparx.form.field.type.DateTimeField.DateTimeFieldState) fieldStates.getState("birth_date");
    }

    public com.netspective.sparx.form.field.type.DateTimeField.DateTimeFieldState.DateTimeFieldValue getBirthDate()
    {
        return (com.netspective.sparx.form.field.type.DateTimeField.DateTimeFieldState.DateTimeFieldValue) getBirthDateState().getValue();
    }

    public DialogFieldFlags getBirthDateStateFlags() { return getBirthDateState().getStateFlags(); }

    public String getBirthDatePrivateRequestParam()
    { return dialogContext.getRequest().getParameter("_dc.birth_date"); }

    public String getBirthDatePublicRequestParam() { return dialogContext.getRequest().getParameter("birth_date"); }

    public com.netspective.sparx.form.field.type.DateTimeField getBirthDateField()
    { return (com.netspective.sparx.form.field.type.DateTimeField) getBirthDateState().getField(); }

    public com.netspective.sparx.form.field.type.IntegerField.IntegerFieldState getAgeState()
    { return (com.netspective.sparx.form.field.type.IntegerField.IntegerFieldState) fieldStates.getState("age"); }

    public com.netspective.sparx.form.field.type.IntegerField.IntegerFieldState.IntegerFieldValue getAge()
    {
        return (com.netspective.sparx.form.field.type.IntegerField.IntegerFieldState.IntegerFieldValue) getAgeState().getValue();
    }

    public DialogFieldFlags getAgeStateFlags() { return getAgeState().getStateFlags(); }

    public String getAgePrivateRequestParam() { return dialogContext.getRequest().getParameter("_dc.age"); }

    public String getAgePublicRequestParam() { return dialogContext.getRequest().getParameter("age"); }

    public com.netspective.sparx.form.field.type.IntegerField getAgeField()
    { return (com.netspective.sparx.form.field.type.IntegerField) getAgeState().getField(); }

}
