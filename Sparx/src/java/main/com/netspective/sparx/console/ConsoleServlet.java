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
 * $Id: ConsoleServlet.java,v 1.3 2003-03-29 13:00:56 shahid.shah Exp $
 */

package com.netspective.sparx.console;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationSkin;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.ApplicationManagerComponent;
import com.netspective.sparx.ApplicationManager;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.Themes;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.lang.ClassPath;

public class ConsoleServlet extends HttpServlet
{
    protected ApplicationManager getPresentationManager() throws ServletException
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

            return pmComponent.getManager();
        }
        catch(Exception e)
        {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        ApplicationManager pm = getPresentationManager();

        Theme defaultTheme = Themes.getInstance().getDefaultTheme();
        ConsoleNavigationTree tree = pm.getConsoleNavigationTree();

        if(tree == null)
            throw new ServletException("Navigation tree 'ace' not found. Available: " + pm.getNavigationTrees());

        String activePageId = httpServletRequest.getPathInfo();
        if(activePageId == null)
            activePageId = "/";

        NavigationSkin skin = defaultTheme.getNavigationSkin();
        NavigationContext nc = skin.createContext(getServletContext(), this, httpServletRequest, httpServletResponse,
                                        tree, activePageId);
        nc.setConsoleMode(true);

        NavigationPage activePage = nc.getActivePage();
        Writer writer = httpServletResponse.getWriter();

        if(activePage != null)
            activePage.handlePage(writer, nc);
        else
        {
            skin.renderPageMetaData(writer, nc);
            skin.renderPageHeader(writer, nc);
            writer.write("No page located for path '"+ activePageId +"'.");
            if(nc.isDevelopmentEnvironment())
            {
                writer.write("<pre>\n");
                writer.write(tree.toString());
                writer.write("</pre>\n");
            }
            skin.renderPageFooter(writer, nc);
        }
    }

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
        doGet(httpServletRequest, httpServletResponse);
    }
}
