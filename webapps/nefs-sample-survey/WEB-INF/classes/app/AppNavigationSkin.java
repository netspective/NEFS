/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Netspective", "Sparx", and "Junxion" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Netspective where possible. We suggest using
 *    the "powered by" button or creating a "powered by" link to
 *    http://www.netspective.com for each application using this code.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: AppNavigationSkin.java,v 1.5 2003-08-31 15:25:28 shahid.shah Exp $
 */

package app;

import java.io.Writer;
import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationSkin;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.basic.AbstractThemeSkin;
import com.netspective.sparx.theme.basic.BasicTabbedNavigationSkin;
import com.netspective.sparx.command.DialogCommand;
import com.netspective.sparx.ProductRelease;
import com.netspective.commons.security.AuthenticatedUser;

public class AppNavigationSkin extends AbstractThemeSkin implements NavigationSkin
{
    public AppNavigationSkin(Theme theme, String name)
    {
        super(theme, name);
    }

    public NavigationContext createContext(javax.servlet.jsp.PageContext jspPageContext, NavigationTree tree, String navTreeId)
    {
        return new NavigationContext(tree,
                jspPageContext.getServletContext(),
                (Servlet) jspPageContext.getPage(),
                jspPageContext.getRequest(),
                jspPageContext.getResponse(),
                this, navTreeId);
    }

    public NavigationContext createContext(ServletContext context, HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, NavigationTree tree, String navTreeId)
    {
        return new NavigationContext(tree, context, servlet, request, response, this, navTreeId);
    }

    public void renderPageMetaData(Writer writer, NavigationContext nc) throws IOException
    {
        NavigationPage activePage = nc.getActivePage();

        writer.write("<!-- Application Header Begins -->\n");
        writer.write("<html>\n");
        writer.write("<head>\n");
        writer.write("<title>" + (activePage != null ? activePage.getTitle(nc) : "") + "</title>\n");

        Theme theme = getTheme();

        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/images/favicon.ico") + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/css/general.css") + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + nc.getRootUrl() + "/resources/navigation.css" + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/css/panel-input.css") + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/css/panel-output.css") + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/css/panel-content-dialog.css") + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/css/panel-content-report.css") + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/css/panel-content-text.css") + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/css/syntax-highlight.css") + "\" type=\"text/css\">\n");
        writer.write("	<link rel=\"stylesheet\" href=\"" + theme.getResourceUrl("/calendar-0.9.2/calendar-win2k-1.css") + "\" type=\"text/css\">\n");
        writer.write("  <script src=\"" + theme.getResourceUrl("/scripts/panel.js") + "\" language=\"JavaScript1.1\"></script>\n");
        writer.write("  <script src=\"" + theme.getResourceUrl("/scripts/dialog.js") + "\" language=\"JavaScript1.2\"></script>\n");

        writer.write("</head>\n");
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
                AuthenticatedRespondent user = (AuthenticatedRespondent) nc.getAuthenticatedUser();
                if(user != null && user.visitedPage(nc, activePage))
                    checkmark = "<img src='"+ nc.getRootUrl() +"/resources/check-box-checked.gif'>";
                else
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
        BasicTabbedNavigationSkin.renderDevelopmentEnvironmentHeader(writer, nc);

        if (((NavigationPage.Flags) nc.getActiveState().getFlags()).isPopup())
            return;

        writer.write("<body leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>\n");
        writer.write("<!-- BEGIN MASTHEAD -->\n");
        writer.write("<table width='100%' height='50' border='0' cellpadding='0' cellspacing='0' background='"+ nc.getRootUrl() +"/resources/top-back.gif'>\n");
        writer.write("  <tr>\n");
        writer.write("    <td><div align='center'><img src='"+ nc.getRootUrl() +"/resources/masthead.gif' width='520' height='50' alt='Risk Management'></div></td>\n");
        writer.write("    <td><div align='right'><img src='"+ nc.getRootUrl() +"/resources/marsh-logo.gif' width='126' height='50' alt='Marsh'></div></td>\n");
        writer.write("  </tr>\n");
        writer.write("</table>\n");
        writer.write("<!-- END MASTHEAD -->\n");

        writer.write("<!-- BEGIN CONTENT TABLE -->\n");
        writer.write("<table width='100%' border='0' cellpadding='0' cellspacing='0' height=95%>\n");
        writer.write("  <tr valign='top'>\n");
        writer.write("    <td width='5%' background='"+ nc.getRootUrl() +"/resources/collage.gif'>&nbsp;</td>\n");
        writer.write("    <td width='20%' bgcolor='#FFFFFF'>\n");

        writer.write("<!-- BEGIN NAVIGATION AREA -->\n");
        renderMenus(writer, nc, nc.getOwnerTree().getRoot().getChildrenList(), true);
        writer.write("<!-- END NAVIGATION AREA -->\n");

        writer.write("	</td>\n");
        writer.write("    <td width='2%' background='"+ nc.getRootUrl() +"/resources/shade.gif'>&nbsp;</td>\n");
        writer.write("    <td width='71%'>\n");

        AuthenticatedUser user = nc.getAuthenticatedUser();
        writer.write("<!-- BEGIN CONTENT AREA -->\n");
        writer.write("	<p align=right>PIN: "+ (user != null ? user.getUserId() : "NONE") +" [<a href='"+ nc.getRootUrl()  +"?_logout=1'>Logout</a>]</p>\n");

        String heading = nc.getActivePage().getHeading(nc);
        if(! "-".equals(heading))
            writer.write("<h1>"+ heading +"</h1>");
    }

    public void renderPageFooter(Writer writer, NavigationContext nc) throws IOException
    {
        if (((NavigationPage.Flags) nc.getActiveState().getFlags()).isPopup())
            return;

        NavigationPage activePage = nc.getActivePage();
        boolean isDialogPage = activePage.getCommand() instanceof DialogCommand;
        if(!isDialogPage)
        {
            NavigationPage nextPage = activePage.getNextPath();
            if(nextPage != null)
                writer.write("<p align=right><a href='"+ nextPage.getUrl(nc) +"'>Continue to next page ></a></p>");
        }

        writer.write("   <p><p><table width=100%><tr><td valign=bottom align=center class=\"page_content\"><table cellpadding=3>");
        writer.write("        <tr>");
        writer.write("                <td align=center class=\"copyright_footer\" colspan=2> ");
        writer.write("                        &copy; 2003 Marsh USA Inc. and Maden Technologies, Inc.");
        writer.write("                </td> ");
        writer.write("        </tr><tr>");
        writer.write("                <td align=center class=\"powered_by_sparx_footer\"> ");
/*
        writer.write("                        <a target=\"netspective\" href=\"http://www.netspective.com/\"> ");
        writer.write("                        <img border=\"0\" alt=\"Powered by Netspective Sparx\" src=\"" + sparxResourcesUrl + "/images/powered-by-sparx-sm.gif\">");
        writer.write("                        </a>");
*/
        writer.write("                </td><td class=\"powered_by_sparx_footer\"><a href='" + nc.getRootUrl() + "/console'>Powered by " + ProductRelease.PRODUCT_RELEASE.getProductBuild() + "</a>.</td>");
        writer.write("   </tr></table>");

        writer.write("<!-- END CONTENT AREA -->\n");
        writer.write("</td>\n");
        writer.write("<td width='2%'>&nbsp;</td>\n");
        writer.write("  </tr>\n");
        writer.write("</table>\n");
        writer.write("<!-- END CONTENT TABLE -->\n");

        writer.write("</body>\n");
        writer.write("</html>\n");

        // for dialogs we want the visited page to be set only if the dialog executes properly
        if(! isDialogPage)
        {
            AuthenticatedRespondent user = (AuthenticatedRespondent) nc.getAuthenticatedUser();
            if(user != null)
                user.setVisitedPage(nc, activePage);
        }
    }
}
