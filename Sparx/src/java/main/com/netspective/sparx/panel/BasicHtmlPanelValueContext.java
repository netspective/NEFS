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
 * $Id: BasicHtmlPanelValueContext.java,v 1.5 2004-03-12 06:53:14 aye.thu Exp $
 */

package com.netspective.sparx.panel;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.value.BasicDbHttpServletValueContext;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class BasicHtmlPanelValueContext extends BasicDbHttpServletValueContext implements HtmlPanelValueContext
{
    private HtmlPanel panel;
    private int panelRenderFlags;
    private HtmlPanelActionStates panelActionStates = new HtmlPanelActionStates(this);

    public BasicHtmlPanelValueContext(NavigationContext nc, HtmlPanel panel)
    {
        this(nc.getServlet(), nc.getRequest(), nc.getResponse(), panel);
        setNavigationContext(nc);
    }

    public BasicHtmlPanelValueContext(Servlet servlet, ServletRequest request, ServletResponse response, HtmlPanel panel)
    {
        super(servlet, request, response);
        this.panel = panel;

        HtmlPanelActions bannerActions = panel.getBanner().getActions();
        HtmlPanelActions frameActions = panel.getFrame().getActions();
        for (int k = 0; k < bannerActions.size(); k++)
        {
            HtmlPanelAction.State state = bannerActions.get(k).constructStateInstance(this);
            panelActionStates.addState(state);
        }
        for (int j = 0; j < frameActions.size(); j++)
        {
            HtmlPanelAction.State state = frameActions.get(j).constructStateInstance(this);
            panelActionStates.addState(state);
        }
    }

    public HtmlPanel getPanel()
    {
        return panel;
    }

    public boolean isMinimized()
    {
        return false;
    }

    public int getPanelRenderFlags()
    {
        return panelRenderFlags;
    }

    public void setPanelRenderFlags(int panelRenderFlags)
    {
        this.panelRenderFlags = panelRenderFlags;
    }

    public HtmlPanelActionStates getPanelActionStates()
    {
        return panelActionStates;
    }
}
