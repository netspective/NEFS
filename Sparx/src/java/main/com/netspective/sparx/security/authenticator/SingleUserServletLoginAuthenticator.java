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
 * $Id: SingleUserServletLoginAuthenticator.java,v 1.1 2003-08-14 17:59:18 shahid.shah Exp $
 */

package com.netspective.sparx.security.authenticator;

import com.netspective.commons.security.Crypt;
import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.sparx.security.LoginAuthenticator;
import com.netspective.sparx.security.LoginDialog;
import com.netspective.sparx.security.LoginDialogContext;

import javax.servlet.ServletConfig;

/**
 * Implements a basic login authenticator that assumes a single user has access to the entire servlet and the user
 * information is stored in servlet init parameters.
 *
 * The following parameters must be defined as init parameters in the servlet:
 *   'login-user-id' - the single valid user id
 *   'login-password' OR 'login-password-encrypted' - the login-password param, if provided, assumes the password is
 *       plain text. If an encrypted password is provided using the login-password-encrypted param, the assumption is
 *       that the Commons Crypt class is used for encryption. You may run run
 *            "java com.netspective.commons.security.Crypt NC <password>"
 *       to generate an encrypted password. If both an encrypted and unencrypted form is provided, the encrypted
 *       version takes precedence.
 */
public class SingleUserServletLoginAuthenticator implements LoginAuthenticator
{
    public static final String INITPARAMNAME_LOGIN_USER_ID = SingleUserServletLoginAuthenticator.class.getName() + ".LOGIN_USER_ID";
    public static final String INITPARAMNAME_LOGIN_USER_PASSWORD_UNENCRYPTED = SingleUserServletLoginAuthenticator.class.getName() + ".LOGIN_PASSWORD_PLAIN_TEXT";
    public static final String INITPARAMNAME_LOGIN_USER_PASSWORD_ENCRYPTED = SingleUserServletLoginAuthenticator.class.getName() + ".LOGIN_PASSWORD_ENCRYPTED";

    public boolean isUserValid(LoginDialog loginDialog, LoginDialogContext loginDialogContext)
    {
        ServletConfig servletConfig = loginDialogContext.getServlet().getServletConfig();
        String loginUserId = servletConfig.getInitParameter(INITPARAMNAME_LOGIN_USER_ID);
        String loginPasswordEncrypted = servletConfig.getInitParameter(INITPARAMNAME_LOGIN_USER_PASSWORD_ENCRYPTED);
        if(loginPasswordEncrypted == null)
        {
            String loginPasswordUnencrypted = servletConfig.getInitParameter(INITPARAMNAME_LOGIN_USER_PASSWORD_UNENCRYPTED);
            if(loginPasswordUnencrypted != null)
                loginPasswordEncrypted = Crypt.crypt(AuthenticatedUser.PASSWORD_ENCRYPTION_SALT, loginPasswordUnencrypted);
        }

        if(loginUserId == null)
        {
            loginDialog.getUserIdField().invalidate(loginDialogContext, "No '"+ INITPARAMNAME_LOGIN_USER_ID +"' servlet init parameter provided.");
            return false;
        }

        if(loginPasswordEncrypted == null)
        {
            loginDialog.getPasswordField().invalidate(loginDialogContext, "No '"+ INITPARAMNAME_LOGIN_USER_PASSWORD_UNENCRYPTED +"' or '"+ INITPARAMNAME_LOGIN_USER_PASSWORD_ENCRYPTED +"' servlet init parameter provided.");
            return false;
        }

        if(! loginUserId.equals(loginDialogContext.getUserIdInput()))
            return false;

        if(! loginPasswordEncrypted.equals(loginDialogContext.getPasswordInput(! loginDialogContext.hasEncryptedPassword())))
            return false;

        return true;
    }
}
