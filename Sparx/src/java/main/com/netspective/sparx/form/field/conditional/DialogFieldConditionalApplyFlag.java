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
package com.netspective.sparx.form.field.conditional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.acl.PermissionNotFoundException;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.console.ConsoleServlet;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldConditionalAction;
import com.netspective.sparx.form.field.DialogFieldFlags;
import com.netspective.sparx.form.field.type.SelectField;
import com.netspective.sparx.form.field.type.TextField;
import com.netspective.sparx.value.source.DialogFieldValueSource;

public class DialogFieldConditionalApplyFlag extends DialogFieldConditionalAction
{
    private static final Log log = LogFactory.getLog(DialogFieldConditionalApplyFlag.class);

    private boolean clear;
    private DialogFieldFlags flags;
    private DialogPerspectives perspective = new DialogPerspectives();
    private String[] hasPermissions;
    private String[] lackPermissions;
    private ValueSource valueSource;
    private ValueSource conditionalValueSource;

    private ValueSource hasValue;
    private ValueSource value;
    private ValueSource isTrue;

    public DialogFieldConditionalApplyFlag()
    {
        super();
    }

    public DialogFieldConditionalApplyFlag(DialogField sourceField)
    {
        super(sourceField);
    }

    public void setSourceField(DialogField value)
    {
        super.setSourceField(value);
        flags = value.createFlags();
    }

    public DialogFieldFlags createFlags()
    {
        return flags;
    }

    public DialogFieldFlags getFlags()
    {
        return flags;
    }

    public void setFlags(DialogFieldFlags flags)
    {
        this.flags = flags;
    }

    public boolean isClear()
    {
        return clear;
    }

    public void setClear(boolean clear)
    {
        this.clear = clear;
    }

    /**
     * Gets the javascipt expression. This method will only be used when  {@link #setHasValue} has been
     * configured with a dialog field value source.
     *
     * @param vc value context
     *
     * @return a javscript expression if this condition effects client side behavior
     */
    public String getExpression(ValueContext vc)
    {
        StringBuffer buffer = new StringBuffer();
        DialogField partnerField = getPartnerField();
        if(partnerField != null)
        {
            if(partnerField instanceof TextField)
            {
                if(value == null)
                    buffer.append("control.value != ''");
                else
                    buffer.append("control.value == '" + value.getTextValue(vc) + "'");
            }
            else if(partnerField instanceof SelectField)
            {
                SelectField select = (SelectField) partnerField;
                int style = select.getStyle().getValueIndex();
                if(style == SelectField.Style.RADIO)
                {
                    if(value == null)
                        buffer.append("control.value != ''");
                    else
                        buffer.append("control.value == '" + value.getTextValue(vc) + "'");
                }
                else if(style == SelectField.Style.LIST)
                {
                    if(value == null)
                        buffer.append("control.options[control.selectedIndex] != ''");
                    else
                        buffer.append("control.options[control.selectedIndex] == '" + value.getTextValue(vc) + "'");
                }
                else if(style == SelectField.Style.MULTICHECK)
                {
                    if(value == null)
                        buffer.append("control.value != ''");
                    else
                        buffer.append("checkedCheckedValue(control," + value + "')");

                }
            }
        }
        return buffer.toString();
    }


    /**
     * Checks to see if a partner field needs to be declared.
     *
     * @return Always returns FALSE
     */
    public boolean isPartnerRequired()
    {
        return false;
    }

    public ValueSource getValue()
    {
        return value;
    }

    /**
     * Sub-condition that is related to the {@link DialogFieldConditionalApplyFlag#setHasValue} condition
     */
    public void setValue(ValueSource value)
    {
        this.value = value;
    }

    public ValueSource getHasValue()
    {
        return hasValue;
    }

    /**
     * Condition used to check if the value source contains a value or not but when the associated subcondition
     * {@link DialogFieldConditionalApplyFlag#setHasValue} is also defined, the value in the value source MUST BE the same as
     * whats defined in the 'value'.
     * For example:
     * <pre>&lt;conditional ... has-value="field:id" value="123"/&gt;</pre>
     * <p/>
     * This means that the condition is deemed to be true if the field called 'id' contains a value of 123.
     */
    public void setHasValue(ValueSource hasValue)
    {
        this.hasValue = hasValue;
        if(hasValue instanceof DialogFieldValueSource)
            setPartnerFieldName(((DialogFieldValueSource) hasValue).getFieldName());
    }

    public ValueSource getIsTrue()
    {
        return isTrue;
    }

    /**
     * Condition used to check if the value source contains a boolean value of TRUE.
     */
    public void setIsTrue(ValueSource aTrue)
    {
        isTrue = aTrue;
    }
    /*
    public boolean importFromXml(DialogField sourceField, Element elem, int conditionalItem)
    {
        if(!super.importFromXml(sourceField, elem, conditionalItem))
            return false;

        String flagName = elem.getAttribute("flag");
        Integer fieldFlagValue = (Integer) dialogFieldNameValueMap.get(flagName);
        //if(fieldFlagValue == null)
        //    sourceField.addErrorMessage("Conditional " + conditionalItem + " has has an invalid 'flag' attribute (" + flagName + ").");
        //else
        if(fieldFlagValue != null)
            setDialogFieldFlag(fieldFlagValue.intValue());

        clear = elem.getAttribute("clear").equals("yes");

        String dataCmdStr = elem.getAttribute("data-cmd");
        if(dataCmdStr.length() > 0)
        {
            setDataCmd(dataCmdStr);
            if(dataCmd == DialogContext.DATA_CMD_NONE)
            {
                sourceField.addErrorMessage("Conditional " + conditionalItem + " has has an invalid 'data-cmd' attribute (" + dataCmdStr + ").");
                return false;
            }
        }

        String permissionsStr = elem.getAttribute("has-permission");
        if(permissionsStr.length() > 0)
        {
            List permsList = new ArrayList();
            StringTokenizer st = new StringTokenizer(permissionsStr, ",");
            while(st.hasMoreTokens())
                permsList.add(st.nextToken());
            this.setHasPermissions((String[]) permsList.toArray(new String[permsList.size()]));
        }

        permissionsStr = elem.getAttribute("lack-permission");
        if(permissionsStr.length() > 0)
        {
            List permsList = new ArrayList();
            StringTokenizer st = new StringTokenizer(permissionsStr, ",");
            while(st.hasMoreTokens())
                permsList.add(st.nextToken());
            this.setLackPermissions((String[]) permsList.toArray(new String[permsList.size()]));
        }

        String valueAvailStr = elem.getAttribute("has-value");
        if(valueAvailStr.length() == 0)
            valueAvailStr = elem.getAttribute("is-true");

        if(valueAvailStr.length() > 0)
            valueSource = ValueSourceFactory.getSingleOrStaticValueSource(valueAvailStr);

        // if the action is "set-value" then look for the value to set the field to
        String valueStr = elem.getAttribute("value");
        if (valueStr.length() > 0)
            this.conditionalValueSource = ValueSourceFactory.getSingleOrStaticValueSource(valueStr);
        return true;
    }
    */

    public DialogPerspectives getPerspective()
    {
        return perspective;
    }

    public void setPerspective(DialogPerspectives perspective)
    {
        this.perspective.copy(perspective);
    }

    public String[] getHasPermissions()
    {
        return hasPermissions;
    }

    public void setHasPermissions(String permissions)
    {
        this.hasPermissions = TextUtils.getInstance().split(permissions, ",", true);
    }

    public String[] getLackPermissions()
    {
        return lackPermissions;
    }

    public void setLackPermissions(String lackPermissions)
    {
        this.lackPermissions = TextUtils.getInstance().split(lackPermissions, ",", true);
    }

    public void applyFlags(DialogContext dc)
    {
        boolean status = true;
        // Need to do this to set the flags
        //flags.setValue(flag.getValue().toUpperCase().replace('-', '_'));

        // the keep checking things until the status is set to false -- if it's false, we're going to just leave
        // and not do anything

        if(status && perspective.getFlags() != 0)
            status = dc.matchesPerspective((int) perspective.getFlags());

        boolean hasPermissionFlg = true;
        boolean lackPermissionFlg = false;
        if(status && (this.hasPermissions != null || this.lackPermissions != null))
        {
            if(dc.getServlet() instanceof ConsoleServlet)
            {
                // if the dialog is being run in the console, don't allow conditionals to be executed since
                // conditionals can contain permission checking which is dependent upon the application
                getSourceField().invalidate(dc,
                                            "Conditionals using permission checking are not allowed to run in ACE since " +
                                            "they are dependent on the application's security settings.");
                return;
            }

            AuthenticatedUser user = dc.getAuthenticatedUser();
            try
            {
                if(this.hasPermissions != null)
                    hasPermissionFlg = user.hasAnyPermission(dc.getAccessControlListsManager(), this.hasPermissions);
                if(this.lackPermissions != null)
                    lackPermissionFlg = user.hasAnyPermission(dc.getAccessControlListsManager(), this.lackPermissions);
            }
            catch(PermissionNotFoundException e)
            {
                log.error("Permission error", e);
            }

            // set 'status' to true only if the user lacks certain permissions and
            // has certain permissions
            if(lackPermissionFlg == false && hasPermissionFlg == true)
                status = true;
            else
                status = false;
        }

        // check the hasValue attribute
        if(status && hasValue != null)
        {
            Value hasValueItem = hasValue.getValue(dc);
            if(value == null)
                status = hasValueItem.getTextValue() != null && hasValueItem.getTextValue().trim().length() > 0
                         ? true : false;
            else
            {
                String valueCheck = value.getTextValue(dc);
                if(valueCheck != null && valueCheck.equals(hasValueItem.getTextValue()))
                    status = true;
                else
                    status = false;
            }
        }
        // check the isTrue attribute
        if(status && isTrue != null)
        {
            Value value = isTrue.getValue(dc);
            if(value.getValue() instanceof Boolean)
            {
                boolean isTrueValue = ((Boolean) value.getValue()).booleanValue();
                if(isTrueValue)
                    status = true;
                else
                    status = false;
            }
            else
            {
                status = false;
            }
        }

        if(status && clear)
        {
            dc.getFieldStates().clearStateFlag(getSourceField(), flags.getFlags());
        }
        else if(status)
        {
            dc.getFieldStates().setStateFlag(getSourceField(), flags.getFlags());
        }

        if(status && conditionalValueSource != null)
            dc.getFieldStates().getState(getSourceField()).getValue().copyValueByReference(valueSource.getValue(dc));
    }
}