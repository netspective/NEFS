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

import com.netspective.commons.security.AuthenticatedUserInitializationException;
import com.netspective.commons.security.MutableAuthenticatedUser;

public interface LoginAuthenticator
{
    /**
     * Called once a user has entered data into a login dialog and that data is ready to be validated to
     * see if the user is a valid user in the system. The method has access to the loginDialogContext so
     * adding validation errors to the login dialog (for messages, etc) is allowed.
     *
     * @param loginManager       The login manager for which the authentication is being performed
     * @param loginDialogContext The value object that contains the user's inputs
     *
     * @return True if the user is valid or false if the user is not a valid user
     */
    public boolean isUserValid(HttpLoginManager loginManager, LoginDialogContext loginDialogContext);

    /**
     * Once a user is considered valid, create the user instance that will hold the user's information,
     * credentials, etc.
     *
     * @param loginManager
     * @param loginDialogContext
     *
     * @return
     */
    public MutableAuthenticatedUser constructAuthenticatedUser(HttpLoginManager loginManager, LoginDialogContext loginDialogContext);

    /**
     * Assign the authenticated user's access control roles, permissions, and do other user initialization.
     *
     * @param ldc  The LoginDialogContext that was constructed by the LoginDialog.
     * @param user The authenticated user that was constructed using constructAuthenticatedUser()
     */
    public void initAuthenticatedUser(HttpLoginManager loginManager, LoginDialogContext ldc, MutableAuthenticatedUser user) throws AuthenticatedUserInitializationException;
}
