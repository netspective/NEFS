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
 * $Id: ConsoleServlet.java,v 1.15 2003-08-08 01:58:44 shahid.shah Exp $
 */

package com.netspective.sparx.console;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationControllerServlet;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.Project;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.form.security.LoginDialog;
import com.netspective.sparx.form.security.LoginDialogContext;
import com.netspective.sparx.form.listener.DialogValidateListener;
import com.netspective.sparx.form.DialogValidationContext;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.Themes;
import com.netspective.commons.security.Crypt;
import com.netspective.commons.security.AuthenticatedUser;

public class ConsoleServlet extends NavigationControllerServlet
{
    public static final String CONSOLE_ID = "console";
    public static final String REQATTRNAME_INCONSOLE = "in-console";
    public static final Boolean REQATTRVALUE_INCONSOLE = new Boolean(true);
    public static final String INITPARAMNAME_LOGIN_USER_ID = "login-user-id";
    public static final String INITPARAMNAME_LOGIN_USER_PASSWORD_UNENCRYPTED = "login-password";
    public static final String INITPARAMNAME_LOGIN_USER_PASSWORD_ENCRYPTED = "login-password-encrypted";

    private HttpLoginManager loginManager;
    private String loginUserId;
    private String loginPasswordEncrypted;

    protected class LoginValidator implements DialogValidateListener
    {
        public void validateDialog(DialogValidationContext dvc)
        {
            LoginDialogContext ldc = (LoginDialogContext) dvc.getDialogContext();
            LoginDialog loginDialog = (LoginDialog) ldc.getDialog();

            if(loginUserId == null)
            {
                loginDialog.getUserIdField().invalidate(ldc, "No '"+ INITPARAMNAME_LOGIN_USER_ID +"' servlet init parameter provided.");
                return;
            }

            if(loginPasswordEncrypted == null)
            {
                loginDialog.getPasswordField().invalidate(ldc, "No '"+ INITPARAMNAME_LOGIN_USER_PASSWORD_UNENCRYPTED +"' or '"+ INITPARAMNAME_LOGIN_USER_PASSWORD_ENCRYPTED +"' servlet init parameter provided.");
                return;
            }

            DialogContext.DialogFieldStates states = ldc.getFieldStates();
            String userId = states.getState(loginDialog.getUserIdFieldName()).getValue().getTextValue();
            if(! loginUserId.equals(userId))
            {
                loginDialog.getUserIdField().invalidate(ldc, "Invalid user name.");
                return;
            }

            String providedPassword = states.getState(loginDialog.getPasswordFieldName()).getValue().getTextValue();
            String encryptedProvidedPassword =
                    ldc.hasEncryptedPassword() ? providedPassword :
                        Crypt.crypt(AuthenticatedUser.PASSWORD_ENCRYPTION_SALT, providedPassword);
            if(! encryptedProvidedPassword.equals(loginPasswordEncrypted))
                loginDialog.getPasswordField().invalidate(ldc, "Invalid password.");
        }
    }

    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);

        loginUserId = servletConfig.getInitParameter(INITPARAMNAME_LOGIN_USER_ID);
        loginPasswordEncrypted = servletConfig.getInitParameter(INITPARAMNAME_LOGIN_USER_PASSWORD_ENCRYPTED);
        if(loginPasswordEncrypted == null)
        {
            String loginPasswordUnencrypted = servletConfig.getInitParameter(INITPARAMNAME_LOGIN_USER_PASSWORD_UNENCRYPTED);
            if(loginPasswordUnencrypted != null)
                loginPasswordEncrypted = Crypt.crypt(AuthenticatedUser.PASSWORD_ENCRYPTION_SALT, loginPasswordUnencrypted);
        }
    }

    protected Theme getTheme()
    {
        return Themes.getInstance().getTheme(CONSOLE_ID);
    }

    protected NavigationTree getNavigationTree(Project project)
    {
        return project.getConsoleNavigationTree();
    }

    protected HttpLoginManager getLoginManager(Project project)
    {
        if(loginManager == null)
        {
            loginManager = project.getLoginManagers().getLoginManager("console");
            loginManager.getLoginDialog().addListener(new LoginValidator());
        }
        return loginManager;
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        long startTime = System.currentTimeMillis();
        httpServletRequest.setAttribute(REQATTRNAME_INCONSOLE, REQATTRVALUE_INCONSOLE);

        NavigationContext nc = createNavigationContext(httpServletRequest, httpServletResponse);
        checkForLogout(nc);
        if(nc.isRedirectToAlternateChildRequired())
        {
            httpServletResponse.sendRedirect(nc.getActivePage().getUrl(nc));
            return;
        }

        renderPage(nc);

        long renderTime = System.currentTimeMillis() - startTime;
        httpServletResponse.getWriter().write("Render time: " + renderTime + " milliseconds");
    }
}
