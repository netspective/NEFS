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

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationControllerServlet;

public class SessionAttributeServiceHandler implements ClientServiceRequestHandler
{
    public static final String REQUEST_IDENTIFIER = "sendSessionAttribute";
    private static final String REQ_HEADER_COMMAND = NavigationControllerServlet.CLIENT_SERVICE_REQUEST_HEADER_NAME + "-" + REQUEST_IDENTIFIER + "-command";
    private static final String REQ_HEADER_VAR_NAME = NavigationControllerServlet.CLIENT_SERVICE_REQUEST_HEADER_NAME + "-" + REQUEST_IDENTIFIER + "-varName";
    private static final String REQ_HEADER_VAR_VALUE = NavigationControllerServlet.CLIENT_SERVICE_REQUEST_HEADER_NAME + "-" + REQUEST_IDENTIFIER + "-varValue";

    public String getClientServiceRequestIdentifier()
    {
        return REQUEST_IDENTIFIER;
    }

    public boolean isNavigationContextRequiredForClientService()
    {
        return false;
    }

    protected void appendSessionAttribute(HttpSession session, String varName, String varValue)
    {
        Object existingValue = session.getAttribute(varName);
        if(existingValue != null)
        {
            if(existingValue instanceof Collection)
                ((Collection) existingValue).add(varValue);
            else
            {
                Collection c = new ArrayList();
                c.add(existingValue);
                c.add(varValue);
                session.setAttribute(varName, c);
            }
        }
        else
            session.setAttribute(varName, varValue);
    }

    protected void setSessionAttribute(HttpSession session, String varName, String varValue)
    {
        session.setAttribute(varName, varValue);
    }

    protected void removeSessionAttribute(HttpSession session, String varName, String varValue)
    {
        session.removeAttribute(varName);
    }

    public void handleClientServiceRequest(NavigationContext nc, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ClientServiceRequestHandlerException
    {
        httpServletResponse.setContentLength(0);

        String command = httpServletRequest.getHeader(REQ_HEADER_COMMAND);
        String varName = httpServletRequest.getHeader(REQ_HEADER_VAR_NAME);
        String varValue = httpServletRequest.getHeader(REQ_HEADER_VAR_VALUE);

        if(command != null && varName != null && varValue != null)
        {
            HttpSession session = httpServletRequest.getSession();
            if(command.equals("set"))
                setSessionAttribute(session, varName, varValue);
            else if(command.equals("append"))
                appendSessionAttribute(session, varName, varValue);
            else if(command.equals("remove"))
                session.removeAttribute(varName);
        }
    }

    public boolean isAllowedToServiceClient(NavigationContext nc)
    {
        return true;  
    }
}
