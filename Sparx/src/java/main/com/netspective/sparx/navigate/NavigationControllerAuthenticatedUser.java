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
package com.netspective.sparx.navigate;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface NavigationControllerAuthenticatedUser
{
    /**
     * Specify whether the user has a special navigation tree (calculated via role or other special condition).
     *
     * @return True if the getUserNavigationTree() method should be called to retrieve the user-specific tree or false if
     *         all users share the same navigation tree.
     */
    public boolean hasUserSpecificNavigationTree();

    /**
     * Return the user's special navigation tree (perhaps based on role or other decisions). The actual navigation tree
     * instance will usually be retrieved from the ncServlet.getProject() method. Unless the method is dynamically
     * generating the navigation tree, the implementation should not cache the navigation tree because navigation trees
     * like all other Project components may be refreshed if the XML files that created them change in the file system.
     *
     * @return The navigation tree that the user should use or NULL if it has no special tree and the default one
     *         assigned by the NavigationController should be used.
     */
    public NavigationTree getUserSpecificNavigationTree(NavigationControllerServlet ncServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    /**
     * This method is called from a navigation controller if a user has recently logged in and needs to redirect to the
     * proper tree. Since the old (invalid) NavigationContext is available, it is passed and may be used to redirect a
     * user to the proper location. Once this method is called, all other processing for the current request stops. The
     * usual job for this method is to call nc.getHttpResponse().sendRedirect(something);
     *
     * @param nc The NavigationContext which led to the invalid tree being used
     */
    public void redirectToUserTree(NavigationContext nc) throws IOException;
}
