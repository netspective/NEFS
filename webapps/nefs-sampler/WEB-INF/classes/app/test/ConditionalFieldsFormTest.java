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
 * @version $Id: ConditionalFieldsFormTest.java,v 1.1 2004-01-05 05:35:09 aye.thu Exp $
 */

package app.test;

import org.xml.sax.SAXException;

import java.net.URL;
import java.io.IOException;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebLink;


/**
 * Tests the dialog with the conditional fields
 */
public class ConditionalFieldsFormTest extends FormInputTest
{
    private WebForm form;

    protected void setUp() throws Exception
    {
        super.setUp();
        // append the request param 'id' for value source population testing
        formsInputLink.click();
        WebResponse response = wc.getCurrentPage().getLinkWith("Conditionals").click();
        form = response.getForms()[0];
    }

    public void testForm() throws IOException, SAXException
    {
        // verify that the text field is not required
        WebResponse response = form.submit();
        //  verify the first row header names (if the submit was successful)
        String[][] fieldStates = response.getTableWithID(DIALOG_CONTEXT_DEBUG_PANEL).asText();
        assertEquals("Field", fieldStates[0][0]);
        assertEquals("Type", fieldStates[0][1]);
        assertEquals("Control Id", fieldStates[0][2]);
        assertEquals("Flags", fieldStates[0][3]);
        assertEquals("Value", fieldStates[0][4]);


        // verify that the text field is now required
        URL url = wc.getCurrentPage().getURL();
        String urlStr = url.getHost() + ":" + url.getPort() + "/" + wc.getCurrentPage().getLinkWith("Conditionals").getURLString() + "?id=12345";
        response = wc.getResponse("http://" + urlStr);
        form = response.getForms()[0];
        response = form.submit();
        WebLink errorlink = response.getLinkWith("Text Field is required.");
        assertNotNull(errorlink);
    }

}
