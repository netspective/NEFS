package app.test;

import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebForm;

import java.io.IOException;

import org.xml.sax.SAXException;


public class BooleanFieldsFormTest  extends FormInputTest
{
    private WebForm form;

    protected void setUp() throws Exception
    {
        super.setUp();
        formsInputLink.click();
        WebResponse response = wc.getCurrentPage().getLinkWith("Boolean").click();
        form = response.getForms()[0];
    }

    public void testForm() throws IOException, SAXException
    {

        // verify all the default values
        //assertEquals("0", form.getParameterValue("_dc.boolFieldRadio0"));
        // this returns a NULL if the default value is false for some reason
        assertEquals("1", form.getParameterValue("_dc.boolFieldAlone"));
        assertEquals("0", form.getParameterValue("_dc.boolFieldCombo"));


        form.setCheckbox("_dc.boolFieldAlone", false);
        WebResponse response = form.submit();

        //  verify the first row header names
        String[][] fieldStates = response.getTableWithID("DlgCntxt_Debug_States_content").asText();
        assertEquals("Field", fieldStates[0][0]);
        assertEquals("Type", fieldStates[0][1]);
        assertEquals("Control Id", fieldStates[0][2]);
        assertEquals("Flags", fieldStates[0][3]);
        assertEquals("Value", fieldStates[0][4]);

        // verify the first column to make sure all fields were submitted
        // NOTE: There is a space (&nbps;) after the field names
        assertEquals("bool_field_radio ", fieldStates[1][0]);
        assertEquals("bool_field_alone ", fieldStates[2][0]);
        assertEquals("bool_field_combo ", fieldStates[3][0]);
        // verify the fourth column to make sure all the values were submitted
        assertEquals("0 ", fieldStates[1][4]);
        assertEquals("0 ", fieldStates[2][4]);
        assertEquals("0 ", fieldStates[3][4]);

    }
}
