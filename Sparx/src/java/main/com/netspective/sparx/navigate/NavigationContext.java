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
 * $Id: NavigationContext.java,v 1.24 2003-12-22 13:10:59 shahid.shah Exp $
 */

package com.netspective.sparx.navigate;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.oro.text.perl.Perl5Util;

import com.netspective.sparx.value.BasicDbHttpServletValueContext;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.handler.DialogNextActionProvider;
import com.netspective.commons.text.TextUtils;

public class NavigationContext extends BasicDbHttpServletValueContext
{
    private NavigationTree ownerTree;
    private NavigationPage activePage;
    private boolean activePageValid;
    private boolean redirectRequired;
    private boolean missingRequiredReqParams;
    private NavigationSkin skin;
    private NavigationTree.FindResults activePathFindResults;
    private String pageTitle;
    private String pageHeading;
    private String pageSubheading;
    private Map navigationStates = new HashMap();
    private int maxLevel = 0;
    private NavigationErrorPage errorPage;
    private Throwable errorPageException;
    private Class matchedErrorPageExceptionClass;

    public NavigationContext(NavigationTree ownerTree, Servlet aServlet, ServletRequest aRequest, ServletResponse aResponse, NavigationSkin skin, String activePathId)
    {
        super(aServlet, aRequest, aResponse);

        this.ownerTree = ownerTree;
        this.skin = skin;
        activePathFindResults = ownerTree.findPath(activePathId);
        activePage = (NavigationPage) activePathFindResults.getMatchedPath();
        maxLevel = ownerTree.getMaxLevel();

        NavigationPage firstDescendantWithBody = findFirstMemberWithBody(activePage);

        if(firstDescendantWithBody != null)
        {
            if(firstDescendantWithBody != activePage || (activePathId == null || activePathId.equals("/")))
                redirectRequired = true;
            activePage = firstDescendantWithBody;
        }

        if(activePage != null)
        {
            activePageValid = activePage.isValid(this);
            if(activePageValid)
                activePage.makeStateChanges(this);

            if(activePage.getRedirect() != null)
                redirectRequired = true;
        }
    }

    public boolean isActivePageValid()
    {
        return activePageValid;
    }

    public boolean isRedirectRequired()
    {
        return redirectRequired;
    }

    public void setMissingRequiredReqParam(String name)
    {
        this.missingRequiredReqParams = true;
    }

    public boolean isMissingRequiredReqParams()
    {
        return missingRequiredReqParams;
    }

    public NavigationErrorPage getErrorPage()
    {
        return errorPage;
    }

    public Throwable getErrorPageException()
    {
        return errorPageException;
    }

    public String getErrorPageExceptionStackStrace()
    {
        if(errorPageException == null)
            return null;

        if(errorPageException instanceof ServletException)
        {
            Throwable rootCause = ((ServletException) errorPageException).getRootCause();
            if(rootCause != null)
                return TextUtils.getStackTrace(rootCause);
        }

        return TextUtils.getStackTrace(errorPageException);
    }

    public String getErrorPageExceptionStackStraceHtml()
    {
        String text = getErrorPageExceptionStackStrace();
        Perl5Util perlUtil = new Perl5Util();
        text = perlUtil.substitute("s/\n/<br>/g", text);
        return perlUtil.substitute("s/\t/&nbsp;&nbsp;&nbsp;&nbsp;/g", text);
    }

    public Class getMatchedErrorPageExceptionClass()
    {
        return matchedErrorPageExceptionClass;
    }

    public void setErrorPageException(NavigationErrorPage errorPage, Throwable errorPageException, Class matchedExceptionClass)
    {
        this.errorPage = errorPage;
        this.errorPageException = errorPageException;
        this.matchedErrorPageExceptionClass = matchedExceptionClass;
    }

    public NavigationPage findFirstMemberWithBody(NavigationPage parent)
    {
        if(parent == null || (parent != null && parent.getBodyType().getValueIndex() != NavigationPageBodyType.NONE))
            return parent;

        NavigationPage defNavigationPage = (NavigationPage) parent.getDefaultChild();
        if(defNavigationPage == null)
            defNavigationPage = parent.getFirstFocusableChild();
        return findFirstMemberWithBody(defNavigationPage);
    }

    public NavigationTree.FindResults getActivePathFindResults()
    {
        return activePathFindResults;
    }

    public DialogNextActionProvider getDialogNextActionProvider()
    {
        return activePage.getDialogNextActionProvider();
    }

    public NavigationPage getActivePage()
    {
        return activePage;
    }

    public NavigationTree getOwnerTree()
    {
        return ownerTree;
    }

    public NavigationSkin getSkin()
    {
        return skin;
    }

    public int getMaxLevel()
    {
        return maxLevel;
    }

    public Map getNavigationStates()
    {
        return navigationStates;
    }

    public String getPageHeading()
    {
        if(errorPage != null)
            return errorPage.getHeading(this);

        if(pageHeading != null)
            return pageHeading;

        return activePage.getHeading(this);
    }

    public String getPageSubheading()
    {
        if(errorPage != null)
            return errorPage.getSubHeading(this);

        if(pageSubheading != null)
            return pageSubheading;

        return activePage.getSubHeading(this);
    }

    public String getPageTitle()
    {
        if(errorPage != null)
            return errorPage.getTitle(this);

        if(pageTitle != null)
            return pageTitle;

        if(pageHeading != null)
            return pageHeading;

        return activePage.getTitle(this);
    }

    public void setPageHeading(String pageHeading)
    {
        this.pageHeading = pageHeading;
    }

    public void setPageSubheading(String pageSubheading)
    {
        this.pageSubheading = pageSubheading;
    }

    public void setPageTitle(String pageTitle)
    {
        this.pageTitle = pageTitle;
    }

    public void setNavigationStates(Map navigationStates)
    {
        this.navigationStates = navigationStates;
    }

    public NavigationPath.State getActiveState()
    {
        return getState(activePage);
    }

    public NavigationPath.State getState(NavigationPath path)
    {
        NavigationPath.State state = (NavigationPath.State) navigationStates.get(path.getQualifiedName());
        if (state == null)
        {
            state = path.constructState();
            navigationStates.put(path.getQualifiedName(), state);
        }
        return state;
    }

    public NavigationContext getNavigationContext()
    {
        return this;
    }

    public FileSystemContext getFileSystemContext(File rootPath, String rootCaption)
    {
        return new FileSystemContext(getRootUrl(), rootPath, rootCaption, activePathFindResults.getUnmatchedPath(0));
    }

    public FileSystemContext getProjectFileSystemContext()
    {
        return getFileSystemContext(new File(getServlet().getServletConfig().getServletContext().getRealPath("/")), "Project");
    }
}