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
 * $Id: HtmlLayoutPanel.java,v 1.6 2003-04-21 20:05:17 shahid.shah Exp $
 */

package com.netspective.sparx.panel;

import java.io.Writer;
import java.io.IOException;

import com.netspective.sparx.navigate.NavigationContext;

public class HtmlLayoutPanel implements HtmlPanel
{
    private static int panelNumber = 0;
    private int height = -1, width = -1;
    private HtmlPanels children = new BasicHtmlPanels();
    private HtmlPanelFrame frame;
    private HtmlPanelBanner banner;
    private String identifier = "HtmlLayoutPanel_" + getNextPanelNumber();

    synchronized static private final int getNextPanelNumber()
    {
        return ++panelNumber;
    }

    public HtmlLayoutPanel()
    {
        frame = createFrame();
        banner = createBanner();
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public HtmlPanelFrame getFrame()
    {
        return frame;
    }

    public void setFrame(HtmlPanelFrame rf)
    {
        frame = rf;
    }

    public HtmlPanelBanner getBanner()
    {
        return banner;
    }

    public void setBanner(HtmlPanelBanner value)
    {
        banner = value;
    }

    public HtmlLayoutPanel createPanels()
    {
        return new HtmlLayoutPanel();
    }

    public void addPanels(HtmlLayoutPanel panel)
    {
        children.add(panel);
    }

    public HtmlPanelFrame createFrame()
    {
        if(frame == null)
            frame = new HtmlPanelFrame();
        return frame;
    }

    public HtmlPanelBanner createBanner()
    {
        if(banner == null)
            banner = new HtmlPanelBanner();
        return banner;
    }

    public boolean affectsNavigationContext(NavigationContext nc)
    {
        for(int i = 0; i < children.size(); i++)
        {
            if(children.get(i).affectsNavigationContext(nc))
                return true;
        }

        return false;
    }

    public int getStyle()
    {
        return children.getStyle();
    }

    public void setStyle(HtmlPanelsStyleEnumeratedAttribute style)
    {
        children.setStyle(style);
    }

    public void addPanel(HtmlPanel panel)
    {
        children.add(panel);
    }

    public HtmlPanels getChildren()
    {
        return children;
    }

    public void render(Writer writer, NavigationContext nc) throws IOException
    {
        if(getStyle() == HtmlPanelsStyleEnumeratedAttribute.VERTICAL)
        {
            for(int i = 0; i < children.size(); i++)
            {
                writer.write("<div style='padding-bottom: 6'>");
                children.get(i).render(writer, nc);
                writer.write("</div>");
            }
        }
        else
        {
            writer.write("<table cellspacing=0 cellpadding=3><tr valign=top>");
            for(int i = 0; i < children.size(); i++)
            {
                writer.write("<td>");
                children.get(i).render(writer, nc);
                writer.write("</td>");
            }
            writer.write("</tr></table>");
        }
    }

    public void render(Writer writer, NavigationContext nc, HtmlPanelSkin skin) throws IOException
    {
        if(getStyle() == HtmlPanelsStyleEnumeratedAttribute.VERTICAL)
        {
            for(int i = 0; i < children.size(); i++)
            {
                writer.write("<div style='padding-bottom: 6'>");
                children.get(i).render(writer, nc, skin);
                writer.write("</div>");
            }
        }
        else
        {
            writer.write("<table cellspacing=0 cellpadding=3><tr valign=top>");
            for(int i = 0; i < children.size(); i++)
            {
                writer.write("<td>");
                children.get(i).render(writer, nc, skin);
                writer.write("</td>");
            }
            writer.write("</tr></table>");
        }
    }
}
