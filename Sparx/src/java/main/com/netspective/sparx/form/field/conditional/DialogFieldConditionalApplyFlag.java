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
 * $Id: DialogFieldConditionalApplyFlag.java,v 1.5 2003-06-09 06:47:06 aye.thu Exp $
 */

package com.netspective.sparx.form.field.conditional;

import javax.servlet.http.HttpServletRequest;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.Value;
import com.netspective.commons.RuntimeEnvironmentFlags;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.sparx.form.field.DialogFieldConditionalAction;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogDataCommands;

public class DialogFieldConditionalApplyFlag extends DialogFieldConditionalAction
{
    private boolean clear;
    private DialogField.Flags flags;
    private DialogDataCommands dataCmd = new DialogDataCommands();
    private String[] hasPermissions;
    private String[] lackPermissions;
    private ValueSource valueSource;
    private ValueSource conditionalValueSource;
    private Flag flag = new Flag();
    private ValueSource hasValue;
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

    /**
     * Inner class for defining all the flags available for dialog field conditionals. This class
     * directly reads from DialogField.Flags to construct all the available flags.
     */
    public static class Flag extends XdmEnumeratedAttribute
    {
        public Flag()
        {
        }

        public String[] getValues()
        {
            DialogField.Flags.FlagDefn[] flagDefns =  DialogField.FLAG_DEFNS;
            String[] values = new String[DialogField.FLAG_DEFNS.length];
            for (int i = 0; i < values.length; i++)
            {
                values[i] = DialogField.FLAG_DEFNS[i].getName().toLowerCase().replace('_', '-');
            }
            return values;
        }
    }

    public Flag getFlag()
    {
        return flag;
    }

    public void setFlag(Flag flag)
    {
        this.flag = flag;
    }

    public DialogField.Flags getFlags()
    {
        return flags;
    }

    public void setFlags(DialogField.Flags flags)
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

    public boolean isPartnerRequired()
    {
        return false;
    }

    public ValueSource getHasValue()
    {
        return hasValue;
    }

    public void setHasValue(ValueSource hasValue)
    {
        this.hasValue = hasValue;
    }

    public ValueSource getIsTrue()
    {
        return isTrue;
    }

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

    public DialogDataCommands getDataCmd()
    {
        return dataCmd;
    }

    public void setDataCmd(DialogDataCommands dataCmd)
    {
        this.dataCmd.copy(dataCmd);
    }

    public String[] getHasPermissions()
    {
        return hasPermissions;
    }

    public void setHasPermissions(String[] permissions)
    {
        this.hasPermissions = permissions;
    }

    public String[] getLackPermissions()
    {
        return lackPermissions;
    }

    public void setLackPermissions(String[] lackPermissions)
    {
        this.lackPermissions = lackPermissions;
    }

    public void applyFlags(DialogContext dc)
    {
        boolean status = true;
        // Need to do this to set the flags
        flags.setValue(flag.getValue().toUpperCase().replace('-', '_'));

        // the keep checking things until the status is set to false -- if it's false, we're going to just leave
        // and not do anything

        if(status && dataCmd.getFlags() != 0)
            status = dc.matchesDataCmdCondition((int) dataCmd.getFlags());

        boolean hasPermissionFlg = true;
        boolean lackPermissionFlg = false;
        if(status && (this.hasPermissions != null || this.lackPermissions != null))
        {
            if(dc.getEnvironmentFlags().flagIsSet(RuntimeEnvironmentFlags.CONSOLE_MODE))
            {
                // if the dialog is being run in ACE, don't allow conditionals to be executed since
                // conditionals can contain permission checking which is dependent upon the application
                getSourceField().invalidate(dc,
                        "Conditionals using permission checking are not allowed to run in ACE since " +
                        "they are dependent on the application's security settings.");
                return;
            }

            HttpServletRequest request = (HttpServletRequest) dc.getRequest();
            //TODO: AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute(LoginDialog.DEFAULT_ATTRNAME_USERINFO);
            /*
            if(this.hasPermissions != null)
                hasPermissionFlg = user.hasAnyPermission(dc.getAccessControlListsManager(), this.hasPermissions);
            if(this.lackPermissions != null)
                lackPermissionFlg = user.hasAnyPermission(dc.getAccessControlListsManager(), this.lackPermissions);
            */

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
            Value value = hasValue.getValue(dc);
            status = value.getTextValue() != null ? true : false;
        }
        // check the isTrue attribute
        if(status && isTrue != null)
        {
            Value value = isTrue.getValue(dc);
            if (value.getValue() instanceof Boolean)
            {
                boolean isTrueValue = ((Boolean)value.getValue()).booleanValue();
                if (isTrueValue)
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

        if (status && conditionalValueSource != null)
            dc.getFieldStates().getState(getSourceField()).getValue().setValue(valueSource.getValue(dc));
    }
}