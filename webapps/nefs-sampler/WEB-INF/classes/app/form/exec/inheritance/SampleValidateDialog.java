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
package app.form.exec.inheritance;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import com.netspective.sparx.Project;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.field.DialogField;

import auto.dcb.form.exec.inheritance.ValidateContext;

/**
 * Demonstrate how to take multiple pieces of data in a dialog and attach custom java logic for extra cross-field
 * validation of data before execution. When an error is found, we add error messages and let Sparx do the error
 * handling and error correction before executing the dialog.
 */
public class SampleValidateDialog extends Dialog
{
    public SampleValidateDialog(Project project)
    {
        super(project);
    }

    public SampleValidateDialog(Project project, DialogsPackage pkg)
    {
        super(project, pkg);
    }

    /**
     * This method is called each time the dialog will be displayed to the user and fields need their values
     * populated. By default Sparx will do almost all value populating but sometimes you may want to populate something
     * programmitically.
     */
    public void populateValues(DialogContext dc, int formatType)
    {
        // make sure default data population occurs
        super.populateValues(dc, formatType);

        // only put data into the fields if we're going to display the data for the first time
        // because if it's not the initial entry it means that there's an error on the screen and the user's
        // data (that they entered) should be placed into the fields
        if(dc.getDialogState().isInitialEntry() && formatType == DialogField.DISPLAY_FORMAT)
        {
            // We're using the auto-generated dialog context bean (DCB) which is a type-safe wrapper around the
            // general-purpose DialogContext. To generate the DCB, you:
            //   1) add generate-dcb="yes" flag to <dialog> tag
            //   2) go to Console -> Project -> Ant Build page and run 'generate-dialog-context-beans' target

            ValidateContext validateContext = new ValidateContext(dc);
            Date birthDate = validateContext.getBirthDate().getDateValue();
            if(birthDate == null)
            {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 25); // assume we're 25 years old
                validateContext.getBirthDate().setValue(cal.getTime());
            }
        }
    }

    /**
     * This method is called right before execute to validate the dialog. If you need to perform any validation
     * that the fields can't perform on their own (such as cross-field validation) then you should perform it here.
     *
     * @param dc The DialogContext containing the field values and states.
     */
    public boolean isValid(DialogContext dc)
    {
        // if any of the default validation failed, leave now
        if(!super.isValid(dc))
            return false;

        // We're using the auto-generated dialog context bean (DCB) which is a type-safe wrapper around the
        // general-purpose DialogContext. To generate the DCB, you:
        //   1) add generate-dcb="yes" flag to <dialog> tag
        //   2) go to Console -> Project -> Ant Build page and run 'generate-dialog-context-beans' target

        ValidateContext validateContext = new ValidateContext(dc);
        Date birthDate = validateContext.getBirthDate().getDateValue();
        int age = validateContext.getAge().getIntValue();

        // find out the current year and year of the birth of the person who entered data to validate
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(birthDate);
        int yearOfBirth = cal.get(Calendar.YEAR);

        // now that we have our values, we're just going to validate that it's correct
        if(currentYear - yearOfBirth != age)
        {
            // we will add an error message at the dialog level
            dc.getValidationContext().addError("The Age and Birth Date fields do not match.");

            // now we'll add messages to each field as well
            validateContext.getBirthDateField().invalidate(dc, "If you want the age to be " + age + " then birth date should be in year " + (currentYear - age));
            validateContext.getAgeField().invalidate(dc, "If you want the birth year to be " + yearOfBirth + " then age should be " + (currentYear - yearOfBirth));

            // we return false so the dialog does not go into execute mode until we have valid data
            return false;
        }

        return true;
    }

    /**
     * The dialog execute method is called as soon as all data is entered and validated. This method is guaranteed to
     * only be called when all fields' data is valid.
     *
     * @param writer The writer where HTML and other output goes -- this is most likely the same as the response stream
     *               but sometimes may be some other writer (if output is being redirected to a file or something).
     * @param dc     The {@see DialogContext} contains all the current user's field values, flags, and state-related data.
     */
    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        writer.write("Congratulations <b>" + dc.getFieldStates().getState("full_name").getValue().getTextValue() + "</b>! ");
        writer.write("It seems that your birthdate and ages match correctly.");
        writer.write("<p>");
        writer.write("This code demonstrated how you can take two three pieces of data, have Sparx individually validate " +
                     "the general fields such as text, date, and integer but add your own custom validator some other logic by " +
                     "overriding the isValid() method of the com.netspective.sparx.form.Dialog class.<p>");

        String relativePath = "/WEB-INF/classes/app/form/exec/inheritance/SampleValidateDialog.java";
        writer.write("You may review the code at " + dc.getConsoleFileBrowserLinkShowAlt(dc.getServlet().getServletConfig().getServletContext().getRealPath(relativePath), relativePath));
        writer.write("<p>");
        writer.write("<a href=\"" + dc.getNavigationContext().getActivePage().getName() + "\">Clear the dialog</a>");
    }
}
