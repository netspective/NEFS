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
package com.netspective.commons.report.tabular;

import java.util.HashMap;
import java.util.Map;

import com.netspective.commons.value.ValueSource;

public class TabularReportColumnConditionalApplyFlag implements TabularReportColumnConditionalState
{
    private boolean clearFlag;
    private int reportColumnFlag;
    private String[] hasPermissions;
    private String[] lackPermissions;
    private ValueSource valueSource;

    public static Map flagValueMap = new HashMap();

    static
    {
    }

    public TabularReportColumnConditionalApplyFlag()
    {
        super();
        this.hasPermissions = null;
        this.lackPermissions = null;
    }

    public TabularReportColumnConditionalApplyFlag(int fieldFlag)
    {
        this();
        setReportColumnFlag(fieldFlag);
    }

    public int getReportColumnFlag()
    {
        return reportColumnFlag;
    }

    public void setReportColumnFlag(int reportColumnFlag)
    {
        this.reportColumnFlag = reportColumnFlag;
    }

    public boolean isClearFlag()
    {
        return clearFlag;
    }

    public void setClearFlag(boolean clearFlag)
    {
        this.clearFlag = clearFlag;
    }

    //TODO: conversion from Sparx 2.x required
    /*
    public boolean importFromXml(ReportColumn column, Element elem, int conditionalItem)
    {
        String flagName = elem.getAttribute("flag");
        Long flagValue = (Long) flagValueMap.get(flagName);
        if(flagValue == null)
            throw new RuntimeException("ReportColumn Conditional " + conditionalItem + " has has an invalid 'flag' attribute (" + flagName + ").");

        setReportColumnFlag(flagValue.intValue());

        clearFlag = elem.getAttribute("clear").equals("yes");

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

        return true;
    }
    */

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

    public void makeStateChanges(com.netspective.commons.report.tabular.TabularReportValueContext rc, TabularReportColumnState state)
    {
    }

    //TODO: Conversion from Sparx 2.x required
    /*
    public void makeStateChanges(ReportContext rc, ReportContext.ColumnState state)
    {
        // keep checking things until the status is set to false -- if it's false, we're going to just leave
        // and not do anything

        boolean status = true;

        if(status && (this.hasPermissions != null || this.lackPermissions != null))
        {
            String env = (String) rc.getSession().getAttribute(Dialog.ENV_PARAMNAME);
            if(env != null && env.equals("ace"))
            {
                // if the dialog is being run in ACE, don't allow conditionals to be executed since
                // conditionals can contain permission checking which is dependent upon the application
                state.setOutputFormat("Conditionals using permission checking are not allowed to run in ACE since " +
                        "they are dependent on the application's security settings.");
                return;
            }

            HttpServletRequest request = (HttpServletRequest) rc.getRequest();
            AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute(LoginDialog.DEFAULT_ATTRNAME_USERINFO);
            AccessControlList acl = AccessControlListFactory.getACL(rc.getServletContext());
            if(this.hasPermissions != null)
                status = user.hasAnyPermission(acl, this.hasPermissions);
            if(status && this.lackPermissions != null)
                status = !(user.hasAnyPermission(acl, this.lackPermissions));
        }

        if(status && valueSource != null)
        {
            Object value = valueSource.getObjectValue(rc);
            if(value instanceof Boolean)
                status = ((Boolean) value).booleanValue();
            else
                status = value != null;
        }

        if(status && clearFlag)
            state.clearFlag(reportColumnFlag);
        else if(status)
            state.setFlag(reportColumnFlag);
    }
    */
}