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
 * $Id: AbstractHtmlTabularReportPanel.java,v 1.4 2003-04-24 02:26:21 shahid.shah Exp $
 */

package com.netspective.sparx.panel;

import java.io.Writer;
import java.io.IOException;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.HtmlPanelSkin;
import com.netspective.sparx.panel.HtmlPanels;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.panel.HtmlPanelBanner;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.theme.Theme;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;

public abstract class AbstractHtmlTabularReportPanel implements HtmlTabularReportPanel
{
    private static int panelNumber = 0;
    private int height = -1, width = -1;
    private HtmlPanelFrame frame;
    private HtmlPanelBanner banner;
    private String identifier = "AbstractHtmlTabularReportPanel_" + getNextPanelNumber();

    synchronized static private final int getNextPanelNumber()
    {
        return ++panelNumber;
    }

    public AbstractHtmlTabularReportPanel()
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

    public HtmlPanels getChildren()
    {
        return null;
    }

    public boolean affectsNavigationContext(NavigationContext nc)
    {
        return false;
    }

    public HtmlTabularReportValueContext createContext(NavigationContext nc, HtmlTabularReportSkin skin)
    {
        return new HtmlTabularReportValueContext(nc.getServletContext(), nc.getServlet(), nc.getRequest(), nc.getResponse(), this, getReport(nc), skin);
    }

    public void render(Writer writer, NavigationContext nc, Theme theme) throws IOException
    {
        HtmlTabularReportValueContext vc = createContext(nc, theme.getReportSkin());
        vc.produceReport(writer, createDataSource(nc, vc));
    }

    public class SimpleMessageDataSource extends AbstractHtmlTabularReportDataSource
    {
        private ValueSource message;

        public SimpleMessageDataSource(HtmlTabularReportValueContext vc, String message)
        {
            super(vc);
            this.message = new StaticValueSource(message);
        }

        public SimpleMessageDataSource(HtmlTabularReportValueContext vc, ValueSource message)
        {
            super(vc);
            this.message = message;
        }

        public ValueSource getNoDataFoundMessage()
        {
            return message;
        }
    }
}
