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
package com.netspective.sparx.theme.console;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.netspective.commons.value.ValueSources;
import com.netspective.sparx.console.ConsoleServletPage;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.template.freemarker.FreeMarkerTemplateProcessor;
import com.netspective.sparx.theme.Theme;

public class ThemesPage extends ConsoleServletPage
{
    public ThemesPage(NavigationTree owner)
    {
        super(owner);
    }

    public boolean isValid(NavigationContext nc)
    {
        if (!super.isValid(nc))
            return false;

        // check to see if all the themes are loaded
        List children = getChildrenList();
        Map themes = nc.getProject().getThemes().getThemesByName();

        if (children.size() != themes.size())
            synchronize(nc);

        return true;
    }

    public void synchronize(NavigationContext nc)
    {
        removeAllChildren();

        Map themes = nc.getProject().getThemes().getThemesByName();
        for (Iterator i = themes.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry entry = (Map.Entry) i.next();
            Theme theme = (Theme) entry.getValue();
            ThemePage page = new ThemePage(getOwner());
            String name = theme.getName();
            page.setTheme(theme);
            page.setName(name != null ? name : "default");
            if (name.equalsIgnoreCase("sampler"))
                page.setDefault(true);
            FreeMarkerTemplateProcessor templateProcessor = new FreeMarkerTemplateProcessor();
            templateProcessor.setConfig("console");
            templateProcessor.setSource(ValueSources.getInstance().getValueSource(ValueSources.createSpecification("console-page-content:../inspector.ftl"), ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION, true));
            page.addBody(templateProcessor);
            appendChild(page);
        }
    }

    public void handlePageBody(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        synchronize(nc);
        super.handlePageBody(writer, nc);
    }
}