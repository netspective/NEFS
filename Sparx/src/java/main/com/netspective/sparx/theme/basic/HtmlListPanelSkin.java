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
package com.netspective.sparx.theme.basic;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.netspective.sparx.command.CommandListItem;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.value.HttpServletValueContext;

/**
 * Class for displaying a html panel containing a list of items which
 * are hyperlinks.
 */
public class HtmlListPanelSkin extends BasicHtmlPanelSkin
{
    public HtmlListPanelSkin()
    {
        super();
    }

    public HtmlListPanelSkin(Theme theme, String name, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, name, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
    }

    /**
     * Renders the panel with the presentation item list
     *
     * @param writer
     * @param nc
     * @param itemList
     *
     * @throws IOException
     */
    public void renderHtml(Writer writer, HtmlPanelValueContext nc, List itemList) throws IOException
    {
        renderPanelRegistration(writer, nc);

        int panelRenderFlags = nc.getPanelRenderFlags();
        if ((panelRenderFlags & HtmlPanel.RENDERFLAG_NOFRAME) == 0)
        {
            renderFrameBegin(writer, nc);
            writer.write("\t<table class=\"report\" width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">\n");
        }
        else
        {
            writer.write("\t<table id=\"" + ((HtmlPanelValueContext) nc).getPanel().getPanelIdentifier() +
                    "_content\" class=\"report_no_frame\" width=\"100%\" border=\"0\" cellspacing=\"2\" " +
                    "cellpadding=\"0\">\n");
        }
        writer.write("\t\t<tr><td>\n");
        if (itemList != null && itemList.size() > 0)
        {
            writer.write("\t\t\t<ul>\n");
            for (int i = 0; i < itemList.size(); i++)
            {
                Object item = itemList.get(i);
                if (item instanceof CommandListItem)
                {
                    writer.write("\t\t\t\t<li><a href=\"" + ((CommandListItem) item).getUrl((HttpServletValueContext) nc) + "\">" +
                            ((CommandListItem) item).getCaption().getTextValue(nc) + "</a>");
                    if (((CommandListItem) item).getDescription() != null)
                        writer.write(":&nbsp;" + ((CommandListItem) item).getDescription().getTextValue(nc));
                    writer.write("</li>\n");
                }
                else if (item instanceof String)
                {
                    writer.write("\t\t\t\t<li><a href=\"" + item + "\">" + item + "</a></li>\n");
                }
            }
            writer.write("\t\t\t</ul>\n");
        }
        else
        {
            writer.write("The item list is empty.");
        }
        writer.write("\t\t</td></tr>\n");

        writer.write("\t</table>\n");
        if ((panelRenderFlags & HtmlPanel.RENDERFLAG_NOFRAME) == 0)
            renderFrameEnd(writer, (HtmlPanelValueContext) nc);

    }

}
