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

package com.netspective.sparx.navigate.client;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.acl.PermissionNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract class for client service handlers that use the configured Access Control List of the project to determine
 * whether or not the service should handle the incoming client request. The {@link #isAllowedToServiceClient(com.netspective.sparx.navigate.NavigationContext)}
 * method should be invoked to do the check before calling the {@link #handleClientServiceRequest(com.netspective.sparx.navigate.NavigationContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * method.
 *
 */
public abstract class AccessControlledServiceHandler implements ClientServiceRequestHandler
{
    private static final Log log = LogFactory.getLog(AccessControlledServiceHandler.class);

    public abstract String getClientServiceRequestIdentifier();
    public abstract void handleClientServiceRequest(NavigationContext nc, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ClientServiceRequestHandlerException;

    private String[] permissions;

    public boolean isNavigationContextRequiredForClientService()
    {
        return true;
    }

    /**
     * Checks to see if the authenticated user ass
     *
     * @param nc    current navigation context
     * @return      true if the handler is allowed to service the request else false
     */
    public boolean isAllowedToServiceClient(NavigationContext nc)
    {
        if (getPermissions() == null)
        {
            // no permissions have been defined for the service handler so allow anyone  access
            return true;
        }

        AuthenticatedUser authenticatedUser = nc.getAuthenticatedUser();
        if (authenticatedUser != null)
        {
            try
            {
                if (authenticatedUser.hasAnyPermission(nc.getProject(), getPermissions()))
                    return true;
            }
            catch (PermissionNotFoundException e)
            {
                nc.setMissingRequiredPermissions(getPermissions());
                log.error("Unknown permission(s) defined for the client service: '" + getClientServiceRequestIdentifier() + "'.");
            }
        }
        else
        {
            log.error("Requesting client must be logged in.");
        }
        return false;
    }

    /**
     * Sets the permission(s) associated with the service handler.
     *
     * @param permissions   Permission string (multiple permissions must be separated with commas)
     */
    public void setPermissions(String permissions)
    {
        this.permissions = TextUtils.getInstance().split(permissions, ",", true);
    }

    /**
     * Gets the permission(s) associated with the service handler
     *
     * @return  an array of permissions
     */
    public String[] getPermissions()
    {
        return permissions;
    }
}
