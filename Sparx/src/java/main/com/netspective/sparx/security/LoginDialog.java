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
package com.netspective.sparx.security;

import java.io.IOException;
import java.io.Writer;

import com.netspective.commons.security.MutableAuthenticatedUser;
import com.netspective.sparx.Project;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.field.DialogField;

public class LoginDialog extends Dialog
{
    public static final String DEFAULT_USERID_FIELD_NAME = "user-id";
    public static final String DEFAULT_PASSWORD_FIELD_NAME = "password";
    public static final String DEFAULT_REMEMBER_ID_FIELD_NAME = "remember";

    private HttpLoginManager loginManager;
    private String userIdFieldName = DEFAULT_USERID_FIELD_NAME;
    private String passwordFieldName = DEFAULT_PASSWORD_FIELD_NAME;
    private String rememberIdFieldName = DEFAULT_REMEMBER_ID_FIELD_NAME;

    public LoginDialog(HttpLoginManager loginManager)
    {
        super(loginManager.getProject());
        setLoginManager(loginManager);
        setDialogContextClass(LoginDialogContext.class);
        setName("login");
    }

    public LoginDialog(Project project, DialogsPackage pkg)
    {
        super(project, pkg);
    }

    public HttpLoginManager getLoginManager()
    {
        return loginManager;
    }

    public void setLoginManager(HttpLoginManager loginManager)
    {
        this.loginManager = loginManager;
    }

    public DialogField getUserIdField()
    {
        return getFields().getByName(getUserIdFieldName());
    }

    public String getUserIdFieldName()
    {
        return userIdFieldName;
    }

    public void setUserIdFieldName(String userIdFieldName)
    {
        this.userIdFieldName = userIdFieldName;
    }

    public DialogField getPasswordField()
    {
        return getFields().getByName(getPasswordFieldName());
    }

    public String getPasswordFieldName()
    {
        return passwordFieldName;
    }

    public void setPasswordFieldName(String passwordFieldName)
    {
        this.passwordFieldName = passwordFieldName;
    }

    public DialogField getRememberIdField()
    {
        return getFields().getByName(getRememberIdFieldName());
    }

    public String getRememberIdFieldName()
    {
        return rememberIdFieldName;
    }

    public void setRememberIdFieldName(String rememberIdFieldName)
    {
        this.rememberIdFieldName = rememberIdFieldName;
    }

    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        LoginDialogContext ldc = (LoginDialogContext) dc;
        HttpLoginManager loginManager = getLoginManager();
        MutableAuthenticatedUser user = loginManager.createAuthenticatedUser(ldc, ldc.getUserIdInput(),
                                                                             ldc.getPasswordInput(!ldc.hasEncryptedPassword()), ((LoginDialogContext) dc).hasRememberedValues());
        loginManager.login(dc, user, ldc.getRememberIdInput());
    }
}
