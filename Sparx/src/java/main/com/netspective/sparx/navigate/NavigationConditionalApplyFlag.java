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

 * $Id: NavigationConditionalApplyFlag.java,v 1.1 2003-08-11 07:12:44 aye.thu Exp $
 */
package com.netspective.sparx.navigate;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.acl.PermissionNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class NavigationConditionalApplyFlag extends NavigationConditionalAction
{
    private String[] hasPermissions;
    private String[] lackPermissions;
    private ValueSource hasValue = ValueSource.NULL_VALUE_SOURCE;
    private ValueSource isTrue = ValueSource.NULL_VALUE_SOURCE;
    private String dataCommand;
    private NavigationPage.Flags flags = createFlags();

    public NavigationConditionalApplyFlag()
    {
        super();
    }

    public NavigationConditionalApplyFlag(NavigationPath path)
    {
        super(path);
        //flags = path.createFlags();
    }

    /**
     * Sets the has-permissions attribute values
     * @param permissionsStr
     */
    public void setHasPermissions(String permissionsStr)
    {
        if(permissionsStr != null && permissionsStr.length() > 0)
        {
            List permsList = new ArrayList();
            StringTokenizer st = new StringTokenizer(permissionsStr, ",");
            while(st.hasMoreTokens())
                permsList.add(st.nextToken());
            hasPermissions = (String[]) permsList.toArray(new String[permsList.size()]);
        }
    }

    /**
     * Gets the has-permissions attribute values
     * @return  an array of permission strings
     */
    public String[] getHasPermissions()
    {
        return hasPermissions;
    }

    /**
     * Gets the lack-permissions attribute values
     * @return an array of permission strings
     */
    public String[] getLackPermissions()
    {
        return lackPermissions;
    }

    /**
     * Sets the lack-permissions attribute value
     * @param lackPermissionsStr
     */
    public void setLackPermissions(String lackPermissionsStr)
    {
        if(lackPermissionsStr != null && lackPermissionsStr.length() > 0)
        {
            List permsList = new ArrayList();
            StringTokenizer st = new StringTokenizer(lackPermissionsStr, ",");
            while(st.hasMoreTokens())
                permsList.add(st.nextToken());
            lackPermissions = (String[]) permsList.toArray(new String[permsList.size()]);
        }
    }

    public NavigationPage.Flags createFlags()
    {
        return new NavigationPage().new Flags();
    }

    public NavigationPage.Flags getFlags()
    {
        return flags;
    }

    public void setFlags(NavigationPage.Flags flags)
    {
        this.flags.copy(flags);
    }

    /**
     * Sets the has-value attribute value
     * @param value
     */
    public void setHasValue(ValueSource value)
    {
        hasValue = value;
    }

    /**
     * Gets the has-value attribute value
     * @return
     */
    public ValueSource getHasValue()
    {
        return hasValue;
    }

    /**
     * Gets the is-true attribute value
     * @return
     */
    public ValueSource getIsTrue()
    {
        return isTrue;
    }

    /**
     * Sets the is-true attribute value
     * @param aTrue
     */
    public void setIsTrue(ValueSource aTrue)
    {
        isTrue = aTrue;
    }

    /**
     * Gets the data command to check for
     * @return
     */
    public String getDataCommand()
    {
        return dataCommand;
    }

    /**
     * Sets the data command to check for
     * @param dataCmd
     */
    public void setDataCommand(String dataCmd)
    {
        dataCommand = dataCmd;
    }

    /**
     *
     * @param nc
     */
    public void applyFlags(NavigationContext nc)
    {
        boolean status = true;

        // the keep checking things until the status is set to false -- if it's false, we're going to just leave
        // and not do anything
        //  TODO: How do we get a hold of the dataCmd for the current request?  When we figure out, then need to update next lines.
        /*
        if(status && dataCommand != DialogContext.)
            status = nc.matchesDataCmdCondition(dataCmd);
        */
        boolean hasPermissionFlg = true;
        boolean lackPermissionFlg = false;

        // handle any configured permissions
        if(status && (this.hasPermissions != null || this.lackPermissions != null))
        {
            HttpServletRequest request = (HttpServletRequest) nc.getRequest();
            AuthenticatedUser authUser = (AuthenticatedUser) request.getSession().getAttribute("authenticated-user");
            try
            {
                if(this.hasPermissions != null)
                    hasPermissionFlg = authUser.hasAnyPermission(nc.getProject(), this.hasPermissions);
                if(this.lackPermissions != null)
                    lackPermissionFlg = authUser.hasAnyPermission(nc.getProject(), this.lackPermissions);
            }
            catch (PermissionNotFoundException e)
            {
                e.printStackTrace();
            }

            // set 'status' to true only if the user lacks certain permissions and
            // has certain permissions
            if(lackPermissionFlg == false && hasPermissionFlg == true)
                status = true;
            else
                status = false;
        }

        // handle any configured  'value' checks
        if(status && isTrue != ValueSource.NULL_VALUE_SOURCE)
        {
            // handle the is-true boolean check
            Object value = isTrue.getValue(nc).getValue();
            if(value instanceof Boolean)
                status = ((Boolean) value).booleanValue();
            else
                status = value != null;
        }
        if (status && hasValue != ValueSource.NULL_VALUE_SOURCE)
        {
            String textVal = hasValue.getValue(nc).getTextValue();
            status = textVal != null && textVal.length() > 0;
            System.out.println(status);
        }

        /*
        if(status && clearFlag)
        {
            nc.clearFlag(getPath().getId(), navigationPathFlag);
        }
        else if(status)
        {
            nc.setFlag(getPath().getId(), navigationPathFlag);
        }
        */
        if(status)
        {
            nc.getState(this.getPath()).getFlags().setFlag(flags.getFlags());
        }
    }

}
