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
 * $Id: BasicDbHttpServletValueContext.java,v 1.34 2003-08-31 23:08:34 shahid.shah Exp $
 */

package com.netspective.sparx.value;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.template.Configuration;

import com.netspective.axiom.value.BasicDatabaseConnValueContext;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.ConnectionContext;
import com.netspective.sparx.Project;
import com.netspective.sparx.ProjectComponent;
import com.netspective.sparx.ProjectManager;
import com.netspective.sparx.connection.HttpSessionBindableTransactionConnectionContext;
import com.netspective.sparx.connection.HttpSessionBindableAutoCommitConnectionContext;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.template.freemarker.FreeMarkerConfigurationAdapters;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationControllerServlet;
import com.netspective.sparx.navigate.NavigationControllerServletOptions;
import com.netspective.sparx.form.DialogsManager;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.config.ConfigurationsManager;
import com.netspective.commons.acl.AccessControlListsManager;
import com.netspective.commons.RuntimeEnvironmentFlags;
import com.netspective.commons.RuntimeEnvironment;

public class BasicDbHttpServletValueContext extends BasicDatabaseConnValueContext
                                      implements ServletValueContext, HttpServletValueContext,
                                                 DatabaseServletValueContext, DatabaseHttpServletValueContext
{
    private static final Log log = LogFactory.getLog(BasicDbHttpServletValueContext.class);

    public static final String CONTEXTATTRNAME_FREEMARKER_CONFIG = "freemarker-config";
    public static final String INITPARAMNAME_DEFAULT_DATA_SRC_ID = "com.netspective.sparx.DEFAULT_DATA_SOURCE";
    public static final String REQATTRNAME_ACTIVE_THEME = "sparx-active-theme";
    public static final String REQATTRNAME_ACTIVE_LOGIN_MANAGER = "sparx-active-login-manager";
    public static final String REQATTRNAME_SHARED_CONN_CONTEXT = "sparx-shared-cc.";
    public static final String SESSATTRNAME_SHARED_CONN_CONTEXT = "sparx-shared-cc.";

    private NavigationContext navigationContext;
    private DialogContext dialogContext;
    private Servlet servlet;
    private ServletRequest request;
    private ServletResponse response;
    private String rootUrl;

    public BasicDbHttpServletValueContext()
    {
    }

    public BasicDbHttpServletValueContext(Servlet servlet, ServletRequest request, ServletResponse response)
    {
        initialize(servlet, request, response);
    }

    public void initialize(Servlet servlet, ServletRequest request, ServletResponse response)
    {
        contextNum++;
        this.request = request;
        this.response = response;
        this.servlet = servlet;
        rootUrl = ((HttpServletRequest) request).getContextPath();
    }

    public void initialize(NavigationContext nc)
    {
        initialize(nc.getServlet(), nc.getRequest(), nc.getResponse());
        setNavigationContext(nc);
    }

    /**
     * Override the parent get connection to provide connection contexts that may be stored in HTTP sessions. If they
     * are stored in HTTP sessions, they will be automatically closed when the session unbinding event occurs. This
     * method allows connection sharing to take place as well -- if a connection is available in either the session or
     * the request then it will be "reused" and a new ConnectionContext will not be created.
     * @param dataSourceId
     * @param transaction
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    public ConnectionContext getConnection(String dataSourceId, boolean transaction) throws NamingException, SQLException
    {
        ConnectionContext result = null;

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        result = (ConnectionContext) httpRequest.getSession().getAttribute(SESSATTRNAME_SHARED_CONN_CONTEXT + dataSourceId);
        if(result != null)
        {
            if(log.isTraceEnabled())
                log.trace("Reusing shared session cc " + result + " for data source '"+ result.getDataSourceId() +"'.");
            return result;
        }

        result = (ConnectionContext) httpRequest.getAttribute(REQATTRNAME_SHARED_CONN_CONTEXT + dataSourceId);
        if(result != null)
        {
            if(log.isTraceEnabled())
                log.trace("Reusing shared request cc " + result + " for data source '"+ result.getDataSourceId() +"'.");
            return result;
        }

        if(transaction)
            result = new HttpSessionBindableTransactionConnectionContext(dataSourceId, this);
        else
            result = new HttpSessionBindableAutoCommitConnectionContext(dataSourceId, this);

        if(log.isTraceEnabled())
            log.trace("Obtained " + result + " for data source '"+ result.getDataSourceId() +"'.");

        return result;
    }

    public String getDefaultDataSource()
    {
        String dataSourceId = super.getDefaultDataSource();
        if(dataSourceId != null && dataSourceId.length() > 0)
            return dataSourceId;

        dataSourceId = ((NavigationControllerServlet) servlet).getServletOptions().getDefaultDataSourceId();
        if(dataSourceId == null)
            dataSourceId = getProject().getDefaultDataSource();

        if(dataSourceId == null)
            throw new RuntimeException("No default data source available. Provide one using '"+ NavigationControllerServletOptions.INITPARAMNAME_SERVLET_OPTIONS +"' servlet context init parameter or in project.xml using 'default-data-source' tag.");

        return dataSourceId;
    }

    public NavigationContext getNavigationContext()
    {
        return navigationContext;
    }

    public void setNavigationContext(NavigationContext navigationContext)
    {
        this.navigationContext = navigationContext;
    }

    public DialogContext getDialogContext()
    {
        return dialogContext;
    }

    public void setDialogContext(DialogContext dialogContext)
    {
        this.dialogContext = dialogContext;
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

    public AuthenticatedUser getAuthenticatedUser()
    {
        HttpLoginManager loginManager = getActiveLoginManager();
        return loginManager != null ? loginManager.getAuthenticatedUser(this) : null;
    }

    public ConfigurationsManager getConfigurationsManager()
    {
        return getProject();
    }

    public AccessControlListsManager getAccessControlListsManager()
    {
        return getProject();
    }

    public SqlManager getSqlManager()
    {
        return getProject();
    }

    public DialogsManager getDialogsManager()
    {
        return getProject();
    }

    public RuntimeEnvironmentFlags getRuntimeEnvironmentFlags()
    {
        return ((RuntimeEnvironment) getServlet()).getRuntimeEnvironmentFlags();
    }

    public ProjectComponent getProjectComponent()
    {
        return ((ProjectManager) getServlet()).getProjectComponent();
    }

    public Project getProject()
    {
        return getProjectComponent().getProject();
    }

    public final String getSparxResourceUrl(String resource)
    {
        return rootUrl + "/sparx/" + resource;
    }

    public final String getRootUrl()
    {
        return rootUrl;
    }

    public final String getServletRootUrl()
    {
        return rootUrl + "/" + getHttpRequest().getServletPath();
    }

    public Theme getActiveTheme()
    {
        return (Theme) request.getAttribute(REQATTRNAME_ACTIVE_THEME);
    }

    public HttpLoginManager getActiveLoginManager()
    {
        return (HttpLoginManager) request.getAttribute(REQATTRNAME_ACTIVE_LOGIN_MANAGER);
    }

    public Configuration getFreeMarkerConfiguration()
    {
        final ServletContext servletContext = getHttpServlet().getServletContext();
        Configuration result = (Configuration) servletContext.getAttribute(CONTEXTATTRNAME_FREEMARKER_CONFIG);
        if(result == null)
        {
            result = FreeMarkerConfigurationAdapters.getInstance().constructWebAppConfiguration(this);
            servletContext.setAttribute(CONTEXTATTRNAME_FREEMARKER_CONFIG, result);
        }
        return result;
    }
}
