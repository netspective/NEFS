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
 * @version $Id: TextFieldsFormTest.java,v 1.3 2004-01-04 04:40:27 aye.thu Exp $
 */
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
        response = wc.getResponse("http://" + urlStr);
        form = response.getForms()[0];
    }

    /**
     * Verify that the
     * @throws IOException
     * @throws SAXException
     */
    public void testForm() throws IOException, SAXException
    {
        // the following fields' default values are set by values from request parameters
        assertEquals("12345", form.getParameterValue("_dc.textFieldHidden"));
        assertEquals("12345", form.getParameterValue("_dc.staticField1"));
        assertEquals( "conditional Required Text", form.getParameterValue( "_dc.textFieldConditionallyRequired" ) );

        form.setParameter("_dc.textFieldRequired", "12345");
        form.setParameter("_dc.textField", "12345");
        form.setParameter("_dc.emailField", "test@netspective.com");
        form.setParameter("_dc.maskedField", "password");
        WebResponse response = form.submit();

        //  verify the first row header names
        String[][] fieldStates = response.getTableWithID(DIALOG_CONTEXT_DEBUG_PANEL).asText();
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
