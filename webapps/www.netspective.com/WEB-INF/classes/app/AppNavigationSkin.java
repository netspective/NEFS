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
 * $Id: AppNavigationSkin.java,v 1.1 2003-11-24 03:17:33 shahid.shah Exp $
 */

package app;

import java.io.Writer;
import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationSkin;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.basic.AbstractThemeSkin;
import com.netspective.sparx.util.HttpUtils;

public class AppNavigationSkin extends AbstractThemeSkin implements NavigationSkin
{
    public AppNavigationSkin(Theme theme, String name)
    {
        super(theme, name);
    }

    public NavigationContext createContext(javax.servlet.jsp.PageContext jspPageContext, NavigationTree tree, String navTreeId)
    {
        return new NavigationContext(tree,
                (Servlet) jspPageContext.getPage(),
                jspPageContext.getRequest(),
                jspPageContext.getResponse(),
                this, navTreeId);
    }

    public NavigationContext createContext(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, NavigationTree tree, String navTreeId)
    {
        return new NavigationContext(tree, servlet, request, response, this, navTreeId);
    }

    public void renderPageMetaData(Writer writer, NavigationContext nc) throws IOException
    {
        // we don't render anything 'cause FreeMarker takes care of everything
    }

    public void renderMenus(Writer writer, NavigationContext nc, List children, boolean root) throws IOException
    {
        writer.write("<table class='menu'>");
        for(int i = 0; i < children.size(); i++)
        {
            NavigationPage activePage = (NavigationPage) children.get(i);
            if(((NavigationPage.Flags) activePage.getFlags()).isHidden())
                continue;

            boolean activePathSelected = activePage == nc.getActivePage();
            boolean activePathLeaf = activePage.getChildrenList().size() == 0;
            final String className =
                    root ?  (activePathSelected ? "menu-root-active" : "menu-root") :
                        activePathLeaf ?
                            (activePathSelected ? "menu-leaf-active" : "menu-leaf") :
                            (activePathSelected ? "menu-active" : "menu");

            String checkmark = "<img src='"+ nc.getRootUrl() +"/resources/check-box-empty.gif'>";
            if(activePage.getChildrenList().size() == 0)
            {
                    checkmark = "<img src='"+ nc.getRootUrl() +"/resources/check-box-unchecked.gif'>";
            }
            writer.write("<tr valign=top class='"+ className +"'><td class='"+ className +"'>"+checkmark+"</td><td class='"+ className +"'>");
            if(((NavigationPage.Flags) activePage.getFlags()).isRejectFocus())
                writer.write(activePage.getCaption(nc));
            else
            {
                writer.write("<a href='"+ activePage.getUrl(nc) +"' class='"+ className +"'>");
                writer.write(activePage.getCaption(nc));
                writer.write("</a>");
            }
            renderMenus(writer, nc, activePage.getChildrenList(), false);
            writer.write("</td></tr>");
        }
        writer.write("</table>");
    }

    public void renderPageHeader(Writer writer, NavigationContext nc) throws IOException
    {
        // in case any errors or messages need to appear, they'll show up at the top of our app
        HttpUtils.renderDevelopmentEnvironmentHeader(writer, nc);

        // otherwise, we don't render anything 'cause FreeMarker takes care of everything
    }

    public void renderPageFooter(Writer writer, NavigationContext nc) throws IOException
    {
        // we don't render anything 'cause FreeMarker takes care of everything
    }
}
