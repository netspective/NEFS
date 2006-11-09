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
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netspective.sparx.theme.ThemeSkin;

public interface NavigationSkin extends ThemeSkin
{
    /**
     * Create a context that can be used to render this navigation skin.
     *
     * @param navTreeId The active page that will be rendered.
     *
     * @return NavigationContext
     */
    public NavigationContext createContext(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, NavigationTree tree, String navTreeId);

    /**
     * Render the meta data like <html>, <head>, <script> etc.
     *
     * @param writer The output destination
     * @param nc     The current navigation context
     */
    public void renderPageMetaData(Writer writer, NavigationContext nc) throws IOException;

    /**
     * Render the information contained in the page header (anything before the body)
     */
    public void renderPageHeader(Writer writer, NavigationContext nc) throws IOException;

    /**
     * Render all the navigation after to the body (the footer)
     *
     * @param writer The output writer
     * @param nc     The current navigation context
     */
    public void renderPageFooter(Writer writer, NavigationContext nc) throws IOException;

    public interface Resource
    {
        public void render(final Writer writer, final NavigationContext nc) throws IOException;
    }

    public class StyleSheet implements Resource
    {
        private String href;
        private HtmlResourceScopeAttribute scope = new HtmlResourceScopeAttribute(HtmlResourceScopeAttribute.THEME);

        public StyleSheet()
        {
        }

        public String getHref()
        {
            return href;
        }

        public void setHref(String href)
        {
            this.href = href;
        }

        public HtmlResourceScopeAttribute getScope()
        {
            return scope;
        }

        public void setScope(HtmlResourceScopeAttribute scope)
        {
            this.scope = scope;
        }

        public void render(final Writer writer, final NavigationContext nc) throws IOException
        {
            switch(scope.getValueIndex())
            {
                case HtmlResourceScopeAttribute.THEME:
                    writer.write("	<link rel=\"stylesheet\" href=\"" + nc.getThemeResourceUrl(href) + "\" type=\"text/css\">\n");
                    break;

                case HtmlResourceScopeAttribute.APP:
                    writer.write("	<link rel=\"stylesheet\" href=\"" + nc.getRootUrl() + href + "\" type=\"text/css\">\n");
                    break;

                case HtmlResourceScopeAttribute.CUSTOM:
                    writer.write("	<link rel=\"stylesheet\" href=\"" + href + "\" type=\"text/css\">\n");
            }
        }
    }

    public class Script implements Resource
    {
        private String src;
        private HtmlResourceScopeAttribute scope = new HtmlResourceScopeAttribute(HtmlResourceScopeAttribute.THEME);

        public Script()
        {
        }

        public String getSrc()
        {
            return src;
        }

        public void setSrc(final String source)
        {
            this.src = source;
        }

        public HtmlResourceScopeAttribute getScope()
        {
            return scope;
        }

        public void setScope(HtmlResourceScopeAttribute scope)
        {
            this.scope = scope;
        }

        public void render(final Writer writer, final NavigationContext nc) throws IOException
        {
            switch(scope.getValueIndex())
            {
                case HtmlResourceScopeAttribute.THEME:
                    writer.write("  <script src=\"" + nc.getThemeResourceUrl(src) + "\" language=\"JavaScript1.2\"></script>\n");
                    break;

                case HtmlResourceScopeAttribute.APP:
                    writer.write("  <script src=\"" + nc.getRootUrl() + src + "\" language=\"JavaScript1.2\"></script>\n");
                    break;

                case HtmlResourceScopeAttribute.CUSTOM:
                    writer.write("  <script src=\"" + src + "\" language=\"JavaScript1.1\"></script>\n");
            }
        }
    }

    public class Resources
    {
        private List resources = new ArrayList();

        public Resources()
        {
        }

        public StyleSheet createStyleSheet()
        {
            return new StyleSheet();
        }

        public void addStyleSheet(final StyleSheet script)
        {
            resources.add(script);
        }

        public Script createScript()
        {
            return new Script();
        }

        public void addScript(final Script script)
        {
            resources.add(script);
        }

        public List getResources()
        {
            return resources;
        }

        public void registerThemeScript(final String src)
        {
            final Script script = createScript();
            script.setSrc(src);
            addScript(script);
        }

        public void registerThemeStyleSheet(final String href)
        {
            final StyleSheet sheet = createStyleSheet();
            sheet.setHref(href);
            addStyleSheet(sheet);
        }

        public void registerAppScript(final String src)
        {
            final Script script = createScript();
            script.setSrc(src);
            script.setScope(new HtmlResourceScopeAttribute(HtmlResourceScopeAttribute.APP));
            addScript(script);
        }

        public void registerAppStyleSheet(final String href)
        {
            final StyleSheet sheet = createStyleSheet();
            sheet.setHref(href);
            sheet.setScope(new HtmlResourceScopeAttribute(HtmlResourceScopeAttribute.APP));
            addStyleSheet(sheet);
        }
    }

}
