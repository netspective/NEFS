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
 * @author Aye Thu
 */

/**
 * @version $Id: FormInputTest.java,v 1.3 2004-01-04 04:40:27 aye.thu Exp $
 */

package app.test;

import junit.framework.TestCase;
import junit.framework.Assert;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.netspective.sparx.form.Dialog;

import java.io.IOException;

import org.xml.sax.SAXException;

public class FormInputTest extends TestCase
{
    public static final String DIALOG_CONTEXT_DEBUG_PANEL = "DlgCntxt_Debug_States_content";

    static
    {
        HttpUnitOptions.setScriptingEnabled(false);
    }

    protected WebConversation wc;
    protected WebResponse response;
    private WebResponse pageAfterLogin;
    protected WebLink homeLink, formsInputLink, formsExecuteLink, playLink, sitemapLink, consoleLink, sampleAppsLink;

    /**
     * Start a new web conversation, get the console login page, enter the login data and get the first reponse page
     * after logging in.
     * @throws Exception
     */
    protected void setUp() throws Exception
    {
        wc = new WebConversation();
        response = wc.getResponse("http://localhost:8099/nefs-sampler");

        // check all the page entries
        homeLink = response.getLinkWith("Home");
        formsInputLink = response.getLinkWith("Forms Input");
        formsExecuteLink = response.getLinkWith("Forms Execution");
        playLink = response.getLinkWith("Play");
        sitemapLink = response.getLinkWith("Sitemap");
        consoleLink = response.getLinkWith("Console");
        sampleAppsLink = response.getLinkWith("Sample Apps Home");
    }

    /**
     * Checks to see if there is a link with the text in the current response
     * @param linkText
     * @throws SAXException
     */
    public void assertLinkPresentWithText(String linkText) throws SAXException
    {
        Assert.assertNotNull("Link with text [" + linkText + "] not found in response.",
                response.getLinkWith(linkText));
    }


    /**
     * Verify that all the menu items are being displayed
     * @throws IOException
     * @throws SAXException
     */
    public void testForm() throws IOException, SAXException
    {
        assertNotNull(homeLink);
        assertNotNull(formsInputLink);
        assertNotNull(formsExecuteLink);
        assertNotNull(playLink);
        assertNotNull(sitemapLink);
        assertNotNull(consoleLink);
        assertNotNull(sampleAppsLink);

        formsInputLink.click();
        response = wc.getCurrentPage();
        assertLinkPresentWithText("Text");
        assertLinkPresentWithText("Numbers");
        assertLinkPresentWithText("Boolean");
        assertLinkPresentWithText("Selection");
        assertLinkPresentWithText("Grids");
        assertLinkPresentWithText("Conditionals");
        assertLinkPresentWithText("Popups");
        assertLinkPresentWithText("Date/Time");
        assertLinkPresentWithText("Advanced");

        formsExecuteLink.click();
        response = wc.getCurrentPage();
        assertLinkPresentWithText("Director");
        assertLinkPresentWithText("Templates");
        assertLinkPresentWithText("Handlers");
        assertLinkPresentWithText("Inheritance");
        assertLinkPresentWithText("Delegation");
    }



}
