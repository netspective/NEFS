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
