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
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
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
 * $Id: NavigationPage.java,v 1.4 2003-04-01 01:45:50 shahid.shah Exp $
 */

package com.netspective.sparx.navigate;

import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.value.HttpServletValueContext;
import com.netspective.sparx.panel.HtmlLayoutPanel;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class NavigationPage extends NavigationPath
{
    static public final long NAVGPAGEFLAG_REJECT_FOCUS = 1;
    static public final long NAVGPAGEFLAG_HAS_BODY = NAVGPAGEFLAG_REJECT_FOCUS * 2;
    static public final long NAVGPAGEFLAG_HIDDEN = NAVGPAGEFLAG_HAS_BODY * 2;
    static public final long NAVGPAGEFLAG_HAS_CONDITIONAL_ACTIONS = NAVGPAGEFLAG_HIDDEN * 2;
    static public final long NAVGPAGEFLAG_STARTCUSTOM = NAVGPAGEFLAG_HAS_CONDITIONAL_ACTIONS * 2;

    //TODO: still need to implement this old Sparx feature
    static private final String THIS_NAV_ID_REPLACE_EXPR = "{NAV_ID}";
    static private final String PARENT_NAV_ID_REPLACE_EXPR = "{PARENT_NAV_ID}";

    private ValueSource caption;
    private ValueSource title;
    private ValueSource heading;
    private ValueSource subHeading;
    private ValueSource url;
    private ValueSource retainParams;
    private HtmlLayoutPanel panel;

    /* --- XDM Callbacks --------------------------------------------------------------------------------------------*/

    public NavigationPage createPage()
    {
        return new NavigationPage();
    }

    public void addPage(NavigationPage page)
    {
        appendChild(page);
    }

    /* -------------------------------------------------------------------------------------------------------------*/

    public boolean isValid(NavigationContext nc)
    {
        return true;
    }

    /* -------------------------------------------------------------------------------------------------------------*/

    /**
     * Determines whether the NavigationPath is part of the active path.
     * @param  nc  A context primarily to obtain the Active NavigationPath.
     * @return  <code>true</code> if the NavigationPath object is:
     *              1. The Active NavigationPath.
     *              2. In the ancestor list of the Active NavigationPath.
     *              3. One of the Default Children.
     */
    public boolean isInActivePath(NavigationContext nc)
    {
        //get the current NavigationPath
        NavigationPath activePath = nc.getActivePage();

        if (getQualifiedName().equals(activePath.getQualifiedName())) return true;

        //get the parents and for each set the property of current to true
        List ancestors = activePath.getAncestorsList();
        for (int i = 0; i < ancestors.size(); i++)
        {
            NavigationPath checkPath = (NavigationPath) ancestors.get(i);
            if (getQualifiedName().equals(checkPath.getQualifiedName())) return true;
        }

        //get the default children if any and set the property of current to true
        Map childrenMap = activePath.getChildrenMap();
        List childrenList = activePath.getChildrenList();
        while (!childrenMap.isEmpty() && !childrenList.isEmpty())
        {
            NavigationPath defaultChildPath = (NavigationPath) childrenMap.get(activePath.getDefaultChild());
            if (defaultChildPath == null)
                defaultChildPath = (NavigationPath) childrenList.get(0);

            if (getQualifiedName().equals(defaultChildPath.getQualifiedName()))
                return true;

            childrenMap = defaultChildPath.getChildrenMap();
            childrenList = defaultChildPath.getChildrenList();
        }

        return false;
    }

    public void makeStateChanges(NavigationContext nc)
    {

    }

    /* -------------------------------------------------------------------------------------------------------------*/

    /**
     *  if we have children, get the first child that does not have focus rejected
     */
    public NavigationPage getFirstFocusableChild()
    {
        List childrenList = getChildrenList();
        if(childrenList.size() > 0)
        {
            for(int i = 0; i < childrenList.size(); i++)
            {
                NavigationPage child = (NavigationPage) childrenList.get(i);
                if(! child.flagIsSet(NAVGPAGEFLAG_REJECT_FOCUS | NAVGPAGEFLAG_HIDDEN))
                    return child;
                else
                    return child.getNextPath();
            }
        }

        return null;
    }

    /**
     * Return the next sibling that can be focused
     */
    public NavigationPage getNextFocusableSibling()
    {
        // if we get to here we either have no children or all our children don't allow focus
        NavigationPath parent = getParent();
        if(parent != null)
        {
            List siblings = parent.getChildrenList();
            int thisIndex = siblings.indexOf(this);
            if(thisIndex == -1)
                throw new RuntimeException("Unable to find " + this + " in siblings list.");

            // find the first sibling that allows focus
            for(int i = thisIndex + 1; i < siblings.size(); i++)
            {
                NavigationPage sibling = (NavigationPage) siblings.get(i);
                if(! sibling.flagIsSet(NAVGPAGEFLAG_REJECT_FOCUS | NAVGPAGEFLAG_HIDDEN))
                    return sibling;
                else
                    return sibling.getNextPath();
            }
        }

        return null;
    }

    /**
     * Return the "next" path (the one immediately following this one). This method will try to obtain the parent node
     * of the given NavigationPath and find itself in the parent's list (its siblings).
     */
    public NavigationPage getNextPath(boolean checkChildren)
    {
        NavigationPage parent = (NavigationPage) getParent();
        NavigationPage nextPath = checkChildren ? getFirstFocusableChild() : null;
        if(nextPath == null)
        {
            nextPath = getNextFocusableSibling();
            if(nextPath == null && parent != null)
                nextPath = parent.getNextPath(false);
        }
        return nextPath;
    }

    /**
     * Return the "next" path (the one immediately following this one). This method will try to obtain the parent node
     * of the given NavigationPath and find itself in the parent's list (its siblings).
     */
    public NavigationPage getNextPath()
    {
        return getNextPath(true);
    }

    /* -------------------------------------------------------------------------------------------------------------*/

    public ValueSource getCaption()
    {
        return caption;
    }

    public void setCaption(ValueSource caption)
    {
        this.caption = caption;
    }

    public ValueSource getHeading()
    {
        return heading;
    }

    public void setHeading(ValueSource heading)
    {
        this.heading = heading;
    }

    public ValueSource getSubHeading()
    {
        return subHeading;
    }

    public void setSubHeading(ValueSource subHeading)
    {
        this.subHeading = subHeading;
    }

    public ValueSource getTitle()
    {
        return title;
    }

    public void setTitle(ValueSource title)
    {
        this.title = title;
    }

    public ValueSource getUrl()
    {
        return url;
    }

    public void setUrl(ValueSource url)
    {
        this.url = url;
    }

    public String getCaption(ValueContext vc)
    {
        ValueSource vs = getCaption();
        if(vs == null)
            return getName();
        else
            return vs.getTextValue(vc);
    }

    public String getHeading(ValueContext vc)
    {
        ValueSource vs = getHeading();
        if(vs == null)
            return getCaption(vc);
        else
            return vs.getTextValue(vc);
    }

    public String getTitle(ValueContext vc)
    {
        ValueSource vs = getTitle();
        if(vs == null)
            return getHeading(vc);
        else
            return vs.getTextValue(vc);
    }

    public String getSubHeading(ValueContext vc)
    {
        ValueSource vs = getSubHeading();
        if(vs == null)
            return null;
        else
            return vs.getTextValue(vc);
    }

    public String getUrl(HttpServletValueContext vc)
    {
        ValueSource vs = getUrl();
        if(vs == null)
        {
            HttpServletRequest request = vc.getHttpRequest();
            return request.getContextPath() + request.getServletPath() + getQualifiedName();
        }
        else
            return vs.getTextValue(vc);
    }

    public ValueSource getRetainParams()
    {
        return retainParams;
    }

    public void setRetainParams(ValueSource retainParams)
    {
        this.retainParams = retainParams;
    }

    /* -------------------------------------------------------------------------------------------------------------*/

    public boolean isHidden()
    {
        return flagIsSet(NAVGPAGEFLAG_HIDDEN);
    }

    public void setHidden(boolean hidden)
    {
        if(hidden)
            setFlagRecursively(NAVGPAGEFLAG_HIDDEN);
        else
            clearFlagRecursively(NAVGPAGEFLAG_HIDDEN);
    }

    public boolean allowFocus()
    {
        return ! flagIsSet(NAVGPAGEFLAG_REJECT_FOCUS);
    }

    public void setAllowFocus(boolean allowFocus)
    {
        if(! allowFocus)
            setFlag(NAVGPAGEFLAG_REJECT_FOCUS);
        else
            clearFlag(NAVGPAGEFLAG_REJECT_FOCUS);
    }

    /* -------------------------------------------------------------------------------------------------------------*/

    public HtmlLayoutPanel createPanels()
    {
        panel = new HtmlLayoutPanel();
        setFlag(NAVGPAGEFLAG_HAS_BODY);
        return panel;
    }

    public boolean requireLogin(NavigationContext nc)
    {
        return true;
    }

    public boolean canHandlePage(NavigationContext nc)
    {
        return true;
    }

    public void handlePageMetaData(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        NavigationSkin skin = nc.getSkin();
        if(skin != null) skin.renderPageMetaData(writer, nc);
    }

    public void handlePageHeader(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        NavigationSkin skin = nc.getSkin();
        if(skin != null) skin.renderPageHeader(writer, nc);
    }

    public void handlePageBody(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        writer.write("Path '"+ nc.getActivePathFindResults().getSearchedForPath() +"' is a " + this.getClass().getName() + " class but has no body.");
    }

    public void handlePageFooter(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        NavigationSkin skin = nc.getSkin();
        if(skin != null) skin.renderPageFooter(writer, nc);
    }

    public void handlePage(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        //try
        //{
            handlePageMetaData(writer, nc);
            handlePageHeader(writer, nc);
        //if(!ComponentCommandFactory.handleDefaultBodyItem(nc.getServletContext(), nc.getServlet(), nc.getRequest(), nc.getResponse()))
            if(panel != null)
                panel.render(writer, nc);
            else
                handlePageBody(writer, nc);
            handlePageFooter(writer, nc);
        //}
        //catch (ComponentCommandException e)
        //{
        //    throw new NavigationPageException(e);
        //}
    }
}