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
 * @version $Id: AppTest.java,v 1.1 2004-04-12 21:08:16 shahid.shah Exp $
 */

import java.io.IOException;
import java.util.Random;

import org.xml.sax.SAXException;

import com.meterware.httpunit.HTMLElementPredicate;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import junit.framework.Assert;
import junit.framework.TestCase;

public class AppTest extends TestCase
{
    private static final String ROOT_URL = "http://localhost:8099/nefs-sample-books";

    static
    {
        HttpUnitOptions.setScriptingEnabled(false);
    }

    private WebConversation webConversation;
    private WebResponse activeResponse;
    private WebLink homeLink, addBookLink, editBookLink, deleteBookLink, searchBooksLink, consoleLink, sampleAppsLink;

    /**
     * Start a new web conversation, get the console login page, enter the login data and get the first reponse page
     * after logging in.
     * @throws Exception
     */
    protected void setUp() throws Exception
    {
        webConversation = new WebConversation();
        webConversation.getClientProperties().setAutoRedirect(true);
        setActiveResponse(webConversation.getResponse(ROOT_URL));
    }

    protected String createAbsoluteUrl(String relativeUrl)
    {
        return ROOT_URL + relativeUrl;
    }

    protected WebConversation getWebConversation()
    {
        return webConversation;
    }

    protected WebResponse getActiveResponse()
    {
        return activeResponse;
    }

    protected void setActiveResponse(WebResponse response) throws SAXException
    {
        this.activeResponse = response;

        // check all the page entries
        homeLink = response.getLinkWith("Home");
        addBookLink = response.getLinkWith("Add Book");
        editBookLink = response.getLinkWith("Edit Book");
        deleteBookLink = response.getLinkWith("Delete Book");
        searchBooksLink = response.getLinkWith("Search Books");
        consoleLink = response.getLinkWith("Console");
        sampleAppsLink = response.getLinkWith("Sample Apps Home");

        assertNotNull(homeLink);
        assertNotNull(addBookLink);
        assertNotNull(editBookLink);
        assertNotNull(deleteBookLink);
        assertNotNull(searchBooksLink);
        assertNotNull(consoleLink);
        assertNotNull(sampleAppsLink);
    }

    /**
     * Checks to see if there is a link with the text in the current activeResponse
     * @param linkText
     * @throws org.xml.sax.SAXException
     */
    protected void assertLinkWithTextPresentInActiveResponse(String linkText) throws SAXException
    {
        Assert.assertNotNull("Link with text [" + linkText + "] not found in activeResponse.",
        getActiveResponse().getLinkWith(linkText));
    }

    protected void assertLinkWithTextMissingInActiveResponse(String linkText) throws SAXException
    {
        Assert.assertNull("Link with text [" + linkText + "] found in activeResponse.",
        getActiveResponse().getLinkWith(linkText));
    }

    protected void submitDialog(WebForm form) throws IOException, SAXException
    {
        setActiveResponse(form.submit());
    }

    protected WebLink getAddBookLink()
    {
        return addBookLink;
    }

    protected WebLink getConsoleLink()
    {
        return consoleLink;
    }

    protected WebLink getDeleteBookLink()
    {
        return deleteBookLink;
    }

    protected WebLink getEditBookLink()
    {
        return editBookLink;
    }

    protected WebLink getHomeLink()
    {
        return homeLink;
    }

    protected WebLink getSampleAppsLink()
    {
        return sampleAppsLink;
    }

    protected WebLink getSearchBooksLink()
    {
        return searchBooksLink;
    }

    protected void runHomePageContentsTest() throws IOException, SAXException
    {
        // there should be at least 100 book links
        for(int i = 1; i <= 100; i++)
        {
            assertLinkWithTextPresentInActiveResponse("BOOK_" + i);
        }
    }

    public void testHomePage() throws IOException, SAXException
    {
        runHomePageContentsTest();
    }

    public WebTable getDataTable() throws SAXException
    {
        // now try and see if the right data got in there so find the table with all the rows
        WebTable result = getActiveResponse().getFirstMatchingTable(new HTMLElementPredicate()
        {
            public boolean matchesCriteria(Object htmlElement, Object criteria)
            {
                WebTable table = (WebTable) htmlElement;
                return table.getColumnCount() > 1 && table.getCellAsText(0, 1).trim().equals("ID");
            }
        }, null);

        assertNotNull(result);
        return result;
    }

    public int getRowWithColumnText(WebTable dataTable, int column, String columnText)
    {
        for(int row = 1; row < dataTable.getRowCount(); row++) // start with row 1 since row 0 is the heading row
        {
            if(dataTable.getCellAsText(row, column).trim().equals(columnText))
                return row;
        }

        return -1;
    }

    public void testSingleAddEditDelete() throws IOException, SAXException
    {
        final int nameColumnInDataTable = 2;
        final int authorColumnInDataTable = 3;

        final String testId = "AUTO_001";
        final String testName = "Auto Name 001";
        final String editedTestName = "Auto Name x01";
        final String testAuthor = "Auto Author 001";
        final int testGenre = new Random().nextInt(6); // valid values are 0 to 5
        final String testISBN = "AUTO_001";

        //-------------------
        // Add a test record
        //-------------------
        getAddBookLink().click();
        setActiveResponse(getWebConversation().getCurrentPage());

        WebForm form = getActiveResponse().getForms()[0];

        form.setParameter("_dc.id", testId);
        form.setParameter("_dc.name", testName);
        form.setParameter("_dc.author", testAuthor);
        form.setParameter("_dc.genre", Integer.toString(testGenre));
        form.setParameter("_dc.isbn", testISBN);

        submitDialog(form);
        runHomePageContentsTest();

        // Make sure what we created got added
        assertLinkWithTextPresentInActiveResponse(testId);
        WebTable dataTable = getDataTable();

        int dataRow = getRowWithColumnText(dataTable, nameColumnInDataTable, testName);
        assertTrue("Unable to find row with " + testName + " in data table", dataRow > 0);
        assertEquals(testAuthor, dataTable.getCellAsText(dataRow, authorColumnInDataTable).trim());

        //-------------------------------
        // Edit the record that we added
        //-------------------------------
        setActiveResponse(webConversation.getResponse(createAbsoluteUrl("/edit?id=" + testId)));

        form = getActiveResponse().getForms()[0];
        form.setParameter("_dc.name", editedTestName);

        submitDialog(form);
        runHomePageContentsTest();

        // Make sure we get back to the home page with newly edited name
        assertLinkWithTextPresentInActiveResponse(testId);
        dataTable = getDataTable();
        dataRow = getRowWithColumnText(dataTable, nameColumnInDataTable, editedTestName);
        assertTrue("Unable to find row with " + testName + " in data table", dataRow > 0);
        assertEquals(testAuthor, dataTable.getCellAsText(dataRow, authorColumnInDataTable).trim());

        //---------------------------------
        // Delete the record that we added
        //---------------------------------
        setActiveResponse(webConversation.getResponse(createAbsoluteUrl("/delete?id=" + testId)));

        form = getActiveResponse().getForms()[0];
        submitDialog(form);
        runHomePageContentsTest();

        // Make sure the record is gone
        assertLinkWithTextMissingInActiveResponse(testId);
        dataTable = getDataTable();
        dataRow = getRowWithColumnText(dataTable, nameColumnInDataTable, editedTestName);
        assertTrue("The record " + testId + " should not be in data table", dataRow == -1);
    }

}
