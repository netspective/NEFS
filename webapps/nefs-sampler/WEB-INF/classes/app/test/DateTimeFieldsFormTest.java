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
 * @version $Id: DateTimeFieldsFormTest.java,v 1.1 2004-01-04 04:40:27 aye.thu Exp $
 */
package app.test;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;

public class DateTimeFieldsFormTest extends FormInputTest
{
    private WebForm form;

    protected void setUp() throws Exception
    {
        super.setUp();
        formsInputLink.click();
        WebResponse response = wc.getCurrentPage().getLinkWith("Date/Time").click();
        form = response.getForms()[0];
    }

    public void testForm() throws IOException, SAXException
    {
        // verify strictTodayDate's date

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String strictTodayDate = sdf.format(new Date());
        assertEquals(strictTodayDate, form.getParameterValue("_dc.dateFieldStrict"));

        // verify the current time field
        sdf = new SimpleDateFormat("HH:mm");
        String nowTime = sdf.format(new Date());
        assertEquals(nowTime, form.getParameterValue("_dc.timeField"));

        // verify nonStrictTomorrowDate's date
        Calendar calendar = GregorianCalendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, day + 1);
        sdf = new SimpleDateFormat("MM/dd/yyyy");
        String nonStrictTomorrowDate = sdf.format(calendar.getTime());
        assertEquals(nonStrictTomorrowDate, form.getParameterValue("_dc.dateFieldNonstrict"));

        // verify the date-time field
        sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        String presentDateTime = sdf.format(new Date());
        assertEquals(presentDateTime, form.getParameterValue("_dc.datetimeField"));

        // set the duration field with invalid relationship values
        form.setParameter("_dc.durationField_begin", "10/10/2003");
        form.setParameter("_dc.durationField_end", "10/09/2003");

        // verify that the error is shown
        WebResponse response = form.submit();
        WebLink errorlink = response.getLinkWith("Beginning value should be before ending value");
        assertNotNull(errorlink);

        // set the duration field with one child missing
        form.setParameter("_dc.durationField_begin", "");
        response = form.submit();
        errorlink = response.getLinkWith("Both beginning and ending values should be provided");
        assertNotNull(errorlink);

        // set the duration field with valid values
        form.setParameter("_dc.durationField_begin", "10/10/2003");
        form.setParameter("_dc.durationField_end", "10/11/2003");
        response = form.submit();

        String[][] fieldStates = response.getTableWithID(DIALOG_CONTEXT_DEBUG_PANEL).asText();
        assertEquals("Field", fieldStates[0][0]);
        assertEquals("Type", fieldStates[0][1]);
        assertEquals("Control Id", fieldStates[0][2]);
        assertEquals("Flags", fieldStates[0][3]);
        assertEquals("Value", fieldStates[0][4]);

        // verify the fourth column to make sure all the values were submitted
        assertEquals("10/10/2003", fieldStates[2][4].trim());
        assertEquals("10/11/2003", fieldStates[3][4].trim());
        assertEquals(strictTodayDate, fieldStates[4][4].trim());
        assertEquals(nonStrictTomorrowDate, fieldStates[5][4].trim());
        assertEquals(nowTime, fieldStates[6][4].trim());
        assertEquals(presentDateTime, fieldStates[7][4].trim());

    }
}
