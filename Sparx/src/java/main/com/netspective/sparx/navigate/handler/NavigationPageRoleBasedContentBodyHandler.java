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
package com.netspective.sparx.navigate.handler;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.xml.template.Template;
import com.netspective.commons.xml.template.TemplateConsumerDefn;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationPageBodyHandler;

/**
 * Allows definition of a page body that could change based on the
 */

public class NavigationPageRoleBasedContentBodyHandler extends NavigationPageBodyDefaultHandler
{
    protected class RoleBasedContentBodyHandler implements NavigationPageBodyHandler
    {
        private String roleName;
        private NavigationPageBodyHandler handler;

        public String getRoleName()
        {
            return roleName;
        }

        public void setRoleName(String roleName)
        {
            this.roleName = roleName;
        }

        public NavigationPageBodyHandler createHandler()
        {
            return new NavigationPageBodyDefaultHandler();
        }

        public void addHandler(NavigationPageBodyHandler handler)
        {
            this.handler = handler;
        }

        public void handleNavigationPageBody(NavigationPage page, Writer writer, NavigationContext nc) throws ServletException, IOException
        {
            handler.handleNavigationPageBody(page, writer, nc);
        }

        public TemplateConsumerDefn getTemplateConsumerDefn()
        {
            return handler.getTemplateConsumerDefn();
        }

        public void registerTemplateConsumption(Template template)
        {
            handler.registerTemplateConsumption(template);
        }
    }

    private Map handlersByRole = new HashMap();

    public Map getHandlersByRole()
    {
        return handlersByRole;
    }

    public RoleBasedContentBodyHandler createRoleBody()
    {
        return new RoleBasedContentBodyHandler();
    }

    public void addRoleBody(RoleBasedContentBodyHandler handler)
    {
        handlersByRole.put(handler.getRoleName(), handler);
    }

    public void handleNavigationPageBody(NavigationPage page, Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        AuthenticatedUser user = nc.getAuthenticatedUser();
        if(user != null)
        {
            String[] roleNames = user.getUserRoleNames();
            if(roleNames != null)
            {
                for(int i = 0; i < roleNames.length; i++)
                {
                    String roleName = roleNames[i];
                    RoleBasedContentBodyHandler roleBasedContent = (RoleBasedContentBodyHandler) handlersByRole.get(roleName);
                    if(roleBasedContent != null)
                    {
                        roleBasedContent.handleNavigationPageBody(page, writer, nc);
                        return;
                    }
                }
            }
            else
                page.getLog().error("Unable to run " + this.getClass() + ".handleNavigationPageBody() because the authenticated user has no roles assigned.");
        }
        else
            page.getLog().error("Unable to run " + this.getClass() + ".handleNavigationPageBody() because there is no authenticated user.");
    }
}
