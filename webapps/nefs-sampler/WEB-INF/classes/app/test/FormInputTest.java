package app.test;

import junit.framework.TestCase;
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
        pageAfterLogin = wc.getResponse("http://localhost:8099/nefs-sampler");

        // check all the page entries
        homeLink = pageAfterLogin.getLinkWith("Home");
        formsInputLink = pageAfterLogin.getLinkWith("Forms Input");
        formsExecuteLink = pageAfterLogin.getLinkWith("Forms Execution");
        playLink = pageAfterLogin.getLinkWith("Play");
        sitemapLink = pageAfterLogin.getLinkWith("Sitemap");
        consoleLink = pageAfterLogin.getLinkWith("Console");
        sampleAppsLink = pageAfterLogin.getLinkWith("Sample Apps Home");
    }

    /**
     * Tests to make sure all the first level menu items exist
     * @throws Exception
     */
    public void testTopLevelLinks() throws Exception
    {
        assertNotNull(homeLink);
        assertNotNull(formsInputLink);
        assertNotNull(formsExecuteLink);
        assertNotNull(playLink);
        assertNotNull(sitemapLink);
        assertNotNull(consoleLink);
        assertNotNull(sampleAppsLink);
    }

    /**
     * Tests to make sure all the menus for the Forms Input page exist
     * @throws IOException
     * @throws SAXException
     */
    public void testFormsInputPage() throws IOException, SAXException
    {
        formsInputLink.click();
        WebResponse  response = wc.getCurrentPage();
        assertNotNull(response.getLinkWith("Text"));
        assertNotNull(response.getLinkWith("Numbers"));
        assertNotNull(response.getLinkWith("Boolean"));
        assertNotNull(response.getLinkWith("Selection"));
        assertNotNull(response.getLinkWith("Grids"));
        assertNotNull(response.getLinkWith("Conditionals"));
        assertNotNull(response.getLinkWith("Popups"));
        assertNotNull(response.getLinkWith("Date/Time"));
        assertNotNull(response.getLinkWith("Advanced"));
    }

    /**
     * verify that all the menus exist in the Forms Execute page
     * @throws IOException
     * @throws SAXException
     */
    public void testForm() throws IOException, SAXException
    {
        formsInputLink.click();
        WebResponse  response = wc.getCurrentPage();
        assertNotNull(response.getLinkWith("Director"));
        assertNotNull(response.getLinkWith("Templates"));
        assertNotNull(response.getLinkWith("Handlers"));
        assertNotNull(response.getLinkWith("Inheritance"));
        assertNotNull(response.getLinkWith("Delegation"));
    }



}
