/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package app.test;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

public class NumberFieldsFormTest extends FormInputTest
{
    private WebForm form;

    protected void setUp() throws Exception
    {
        super.setUp();
        formsInputLink.click();
        WebResponse response = wc.getCurrentPage().getLinkWith("Numbers").click();
        form = response.getForms()[0];
    }

    /**
     * Verify that the
     */
    public void testForm() throws IOException, SAXException
    {
        // verify all the default values
        assertEquals("$-123456789.45", form.getParameterValue("_dc.currencyField1"));
        assertEquals("-$123456789.4", form.getParameterValue("_dc.currencyField2"));
        assertEquals("$123456789", form.getParameterValue("_dc.currencyField3"));
        assertEquals("800-123-4567", form.getParameterValue("_dc.phoneField1"));
        assertEquals("(800) 123-4567", form.getParameterValue("_dc.phoneField2"));
        assertEquals("12345", form.getParameterValue("_dc.zipField"));
        assertEquals("999-99-9999", form.getParameterValue("_dc.ssnField1"));
        assertEquals("999-99-9999", form.getParameterValue("_dc.ssnField2"));

        form.setParameter("_dc.integerField", "12345");
        form.setParameter("_dc.floatField", "12345");
        form.setParameter("_dc.currencyField1", "$-123.45");
        form.setParameter("_dc.currencyField2", "$-123.4");
        form.setParameter("_dc.currencyField3", "$123456789.00");
        WebResponse response = form.submit();

        // currencyField3 allows no decimals
        WebLink errorlink = response.getLinkWith("Currency values must have the format $xxx.xx for positive " +
                                                 "values and -$xxx.xx for negative values. (decimals = 0)");
        assertNotNull(errorlink);
        // crrencyField2 allows one decimal and negative sign before the symbol
        errorlink = response.getLinkWith("Currency values must have the format $xxx.xx for positive " +
                                         "values and -$xxx.xx for negative values. (decimals = 1)");
        assertNotNull(errorlink);

        form.setParameter("_dc.currencyField2", "-$123.4");
        form.setParameter("_dc.currencyField3", "$123456789");
        response = form.submit();

        //  verify the first row header names
        String[][] fieldStates = response.getTableWithID(DIALOG_CONTEXT_DEBUG_PANEL).asText();
        assertEquals("Field", fieldStates[0][0]);
        assertEquals("Type", fieldStates[0][1]);
        assertEquals("Control Id", fieldStates[0][2]);
        assertEquals("Flags", fieldStates[0][3]);
        assertEquals("Value", fieldStates[0][4]);

        // verify the first column to make sure all fields were submitted
        // NOTE: There is a space (&nbps;) after the field names
        assertEquals("integer_field ", fieldStates[1][0]);
        assertEquals("float_field ", fieldStates[2][0]);
        assertEquals("currency_field1 ", fieldStates[3][0]);
        assertEquals("currency_field2 ", fieldStates[4][0]);
        assertEquals("currency_field3 ", fieldStates[5][0]);
        assertEquals("phone_field1 ", fieldStates[6][0]);
        assertEquals("phone_field2 ", fieldStates[7][0]);
        assertEquals("zip_field ", fieldStates[8][0]);
        assertEquals("ssn_field1 ", fieldStates[9][0]);
        assertEquals("ssn_field2 ", fieldStates[10][0]);
        // verify the fourth column to make sure all the values were submitted
        assertEquals("12345 ", fieldStates[1][4]);
        assertEquals("12345.0 ", fieldStates[2][4]);
        assertEquals("-123.45 ", fieldStates[3][4]);
        assertEquals("-123.4 ", fieldStates[4][4]);
        assertEquals("123456789 ", fieldStates[5][4]);
        assertEquals("8001234567 ", fieldStates[6][4]);
        assertEquals("8001234567 ", fieldStates[7][4]);
        assertEquals("12345 ", fieldStates[8][4]);
        assertEquals("999999999 ", fieldStates[9][4]);
        assertEquals("999-99-9999 ", fieldStates[10][4]);

    }
}
