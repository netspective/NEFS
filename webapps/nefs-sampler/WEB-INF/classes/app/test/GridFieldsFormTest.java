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
import com.meterware.httpunit.WebResponse;


public class GridFieldsFormTest extends FormInputTest
{
    private WebForm form;

    public void testForm() throws IOException, SAXException
    {
        // verify all the default values
        assertEquals("1", form.getParameterValue("_dc.grid01_row01_integerField01"));
        assertEquals("2", form.getParameterValue("_dc.grid01_row01_integerField02"));
        assertEquals("3", form.getParameterValue("_dc.grid01_row01_integerField03"));
        assertEquals("4", form.getParameterValue("_dc.grid01_row01_integerField04"));
        assertEquals("default", form.getParameterValue("_dc.composite01_textField03"));
        assertEquals("default", form.getParameterValue("_dc.composite01_textField02"));
        assertEquals("default", form.getParameterValue("_dc.composite01_textField01"));

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
        assertEquals("grid-01.row-01.integer_field_01 ", fieldStates[4][0]);
        assertEquals("grid-01.row-01.integer_field_02 ", fieldStates[5][0]);
        assertEquals("grid-01.row-01.integer_field_03 ", fieldStates[6][0]);
        assertEquals("grid-01.row-01.integer_field_04 ", fieldStates[7][0]);
        assertEquals("composite-01 ", fieldStates[16][0]);
        assertEquals("composite-01.text_field_01 ", fieldStates[17][0]);
        assertEquals("composite-01.text_field_02 ", fieldStates[18][0]);
        assertEquals("composite-01.text_field_03 ", fieldStates[19][0]);

        assertEquals("1 ", fieldStates[4][4]);
        assertEquals("2 ", fieldStates[5][4]);
        assertEquals("3 ", fieldStates[6][4]);
        assertEquals("4 ", fieldStates[7][4]);
        assertEquals("default ", fieldStates[17][4]);
        assertEquals("default ", fieldStates[18][4]);
        assertEquals("default ", fieldStates[19][4]);

    }

    protected void setUp() throws Exception
    {
        super.setUp();
        formsInputLink.click();
        WebResponse response = wc.getCurrentPage().getLinkWith("Grids").click();
        form = response.getForms()[0];
    }
}
