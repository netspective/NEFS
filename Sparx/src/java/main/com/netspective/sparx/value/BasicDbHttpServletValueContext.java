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
 * $Id: BasicDbHttpServletValueContext.java,v 1.8 2003-05-06 17:18:19 shahid.shah Exp $
 */

package com.netspective.sparx.value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.netspective.axiom.value.BasicDatabaseConnValueContext;
import com.netspective.axiom.SqlManager;
import com.netspective.sparx.ApplicationManager;
import com.netspective.sparx.ApplicationManagerComponent;
import com.netspective.sparx.form.DialogsManager;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.Themes;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.config.ConfigurationsManager;
import com.netspective.commons.acl.AccessControlListsManager;
import com.netspective.commons.text.TextUtils;

public class BasicDbHttpServletValueContext extends BasicDatabaseConnValueContext
                                      implements ServletValueContext, HttpServletValueContext,
                                                 DatabaseServletValueContext, DatabaseHttpServletValueContext
{
    public static final String INITPARAMNAME_RUNTIME_ENVIRONMENT = "netspective-runtime-environment";
    public static final String INITPARAMNAME_RUNTIME_ENVIRONMENT_MODE = "netspective-runtime-environment-mode";
    public static final String REQATTRNAME_ACTIVE_THEME = "sparx-active-theme";

    private ServletContext context;
    private Servlet servlet;
    private ServletRequest request;
    private ServletResponse response;
    private boolean isPopup;
    private boolean isPrint;
    private String rootUrl;

    public BasicDbHttpServletValueContext()
    {
    }

    public BasicDbHttpServletValueContext(ServletContext context, Servlet servlet, ServletRequest request, ServletResponse response)
    {
        initialize(context, servlet, request, response);
    }

    public void initialize(ServletContext context, Servlet servlet, ServletRequest request, ServletResponse response)
    {
        contextNum++;
        this.context = context;
        this.request = request;
        this.response = response;
        this.servlet = servlet;
        rootUrl = ((HttpServletRequest) request).getContextPath();
    }

    public Object getContextLocation()
    {
        return getHttpRequest().getRequestURI();
    }

    public Object getAttribute(String attributeId)
    {
        return request.getAttribute(attributeId);
    }

    public void setAttribute(String attributeId, Object attributeValue)
    {
        request.setAttribute(attributeId, attributeValue);
    }

    public HttpServletRequest getHttpRequest()
    {
        return (HttpServletRequest) request;
    }

    public HttpServletResponse getHttpResponse()
    {
        return (HttpServletResponse) response;
    }

    public HttpServlet getHttpServlet()
    {
        return (HttpServlet) servlet;
    }

    public ServletRequest getRequest()
    {
        return request;
    }

    public ServletResponse getResponse()
    {
        return response;
    }

    public Servlet getServlet()
    {
        return servlet;
    }

    public ServletContext getServletContext()
    {
        return context;
    }

    public boolean isInMaintenanceMode()
    {
        return "maintenance".equalsIgnoreCase(context.getInitParameter(INITPARAMNAME_RUNTIME_ENVIRONMENT_MODE));
    }

    public boolean isDemonstrationEnvironment()
    {
        return "demonstration".equalsIgnoreCase(context.getInitParameter(INITPARAMNAME_RUNTIME_ENVIRONMENT));
    }

    public boolean isDevelopmentEnvironment()
    {
        String runtimeEnv = context.getInitParameter(INITPARAMNAME_RUNTIME_ENVIRONMENT);
        return runtimeEnv == null || "development".equalsIgnoreCase(context.getInitParameter(INITPARAMNAME_RUNTIME_ENVIRONMENT));
    }

    public boolean isProductionEnvironment()
    {
        return "production".equalsIgnoreCase(context.getInitParameter(INITPARAMNAME_RUNTIME_ENVIRONMENT));
    }

    public boolean isTestEnvironment()
    {
        return "testing".equalsIgnoreCase(context.getInitParameter(INITPARAMNAME_RUNTIME_ENVIRONMENT));
    }

    public boolean isTrainingEnvironment()
    {
        return "training".equalsIgnoreCase(context.getInitParameter(INITPARAMNAME_RUNTIME_ENVIRONMENT));
    }

    public boolean isPopup()
    {
        return isPopup;
    }

    public void setIsPopup(boolean popup)
    {
        this.isPopup = popup;
    }

    public boolean isPrint()
    {
        return isPrint;
    }

    public void setIsPrint(boolean print)
    {
        this.isPrint = print;
    }

    public AuthenticatedUser getAuthenticatedUser()
    {
        return (AuthenticatedUser) getHttpRequest().getSession(true).getAttribute("authenticated-user");
    }

    public void setAuthenticatedUser(AuthenticatedUser user)
    {
        getHttpRequest().getSession(true).setAttribute("authenticated-user", user);
    }

    public ConfigurationsManager getConfigurationsManager()
    {
        return getApplicationManager();
    }

    public AccessControlListsManager getAccessControlListsManager()
    {
        return getApplicationManager();
    }

    public SqlManager getSqlManager()
    {
        return getApplicationManager();
    }

    public DialogsManager getDialogsManager()
    {
        return getApplicationManager();
    }

    public ApplicationManagerComponent getApplicationManagerComponent()
    {
        try
        {
            // never store the PresentationManagerComponent instance since it may change if it needs to be reloaded
            // (always use the factory get() method)
            ApplicationManagerComponent pmComponent =
                (ApplicationManagerComponent) XdmComponentFactory.get(
                        ApplicationManagerComponent.class,
                        getServletContext().getRealPath("/WEB-INF/sparx/components.xml"),
                        XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);

            for(int i = 0; i < pmComponent.getErrors().size(); i++)
                System.out.println(pmComponent.getErrors().get(i));

            return pmComponent;
        }
        catch(Exception e)
        {
            throw new NestableRuntimeException(e);
        }
    }

    public ApplicationManager getApplicationManager()
    {
        return getApplicationManagerComponent().getManager();
    }

    public String getApplicationName()
    {
        String servletContextName = context.getServletContextName();

        if (servletContextName != null && servletContextName.length() > 1)
        {
            return TextUtils.sqlIdentifierToText(context.getServletContextName().substring(1), true);
        }
        else
        {
            return null;
        }
    }

    public final String getRootUrl()
    {
        return rootUrl;
    }

    public final String getThemeResourcesRootUrl(Theme theme)
    {
        return rootUrl + "/sparx/theme/" + theme.getName();
    }

    public final String getThemeImagesRootUrl(Theme theme)
    {
        return rootUrl + "/sparx/theme/" + theme.getName() + "/images";
    }

    public final String getServletRootUrl()
    {
        return rootUrl + "/" + getHttpRequest().getServletPath();
    }

    public Theme getActiveTheme()
    {
        return (Theme) request.getAttribute(REQATTRNAME_ACTIVE_THEME);
    }
}
