package app.test;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebForm;


public class TextFieldsFormTest extends FormInputTest
{
    private WebForm form;

    protected void setUp() throws Exception
    {
        super.setUp();
        // append the request param 'id' for value source population testing
        formsInputLink.click();
        URL url = wc.getCurrentPage().getURL();
        String urlStr = url.getHost() + ":" + url.getPort() + "/" + wc.getCurrentPage().getLinkWith("Text").getURLString() + "?id=12345";
        WebResponse  response =wc.getResponse("http://" + urlStr);
        //WebResponse  response = wc.getCurrentPage().getLinkWith("Text").click();
        form = response.getForms()[0];
    }

    /**
     * Verify the default values
     */
    public void testRequiredValues()
    {
        assertEquals( "conditional Required Text", form.getParameterValue( "_dc.textFieldConditionallyRequired" ) );
    }

    /**
     * Verify that fields whose default values that are dependent upon request params are populated correctly
     */
    public void testRequestParameterFieldPopulation() throws IOException, SAXException
    {

        String url = wc.getCurrentPage().getURL().toString() + "?id=123";
        form = wc.getResponse(url).getForms()[0];
        assertEquals("123", form.getParameterValue("_dc.textFieldHidden"));
        assertEquals("123", form.getParameterValue("_dc.staticField1"));
    }

    /**
     * Verify that the
     * @throws IOException
     * @throws SAXException
     */
    public void testTextFieldsFormInput() throws IOException, SAXException
    {
        // the following fields' default values are set by values from request parameters
        assertEquals("12345", form.getParameterValue("_dc.textFieldHidden"));
        assertEquals("12345", form.getParameterValue("_dc.staticField1"));

        form.setParameter("_dc.textFieldRequired", "12345");
        form.setParameter("_dc.textField", "12345");
        form.setParameter("_dc.emailField", "test@netspective.com");
        form.setParameter("_dc.maskedField", "password");
        WebResponse response = form.submit();

        //  verify the first row header names
        String[][] fieldStates = response.getTableWithID("AbstractPanel_3_content").asText();
        assertEquals("Field", fieldStates[0][0]);
        assertEquals("Type", fieldStates[0][1]);
        assertEquals("Control Id", fieldStates[0][2]);
        assertEquals("Flags", fieldStates[0][3]);
        assertEquals("Value", fieldStates[0][4]);

        // verify the first column to make sure all fields were submitted
        // NOTE: There is a space (&nbps;) after the field names
        assertEquals("text_field_required ", fieldStates[1][0]);
        assertEquals("text_field_hidden ", fieldStates[2][0]);
        assertEquals("static_field1 ", fieldStates[3][0]);
        assertEquals("static_field2 ", fieldStates[4][0]);
        assertEquals("text_field_conditionally_required ", fieldStates[5][0]);
        assertEquals("text_field ", fieldStates[6][0]);
        assertEquals("email_field ", fieldStates[7][0]);
        assertEquals("masked_field ", fieldStates[8][0]);

        // verify the fourth column to make sure all the values were submitted
        assertEquals("12345 ", fieldStates[1][4]);
        assertEquals("12345 ", fieldStates[2][4]);
        assertEquals("12345 ", fieldStates[3][4]);
        assertEquals("Static Field's Value ", fieldStates[4][4]);
        assertEquals("conditional Required Text ", fieldStates[5][4]);
        assertEquals("12345 ", fieldStates[6][4]);
        assertEquals("test@netspective.com ", fieldStates[7][4]);
        assertEquals("password ", fieldStates[8][4]);

    }
}
