/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 * @author Shahid N. Shah
 */

/**
 * $Id: SampleValidateDialog.java,v 1.1 2003-12-11 17:37:49 shahid.shah Exp $
 */

package app.form.exec.inheritance;

import java.io.Writer;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

import auto.dcb.form.exec.inheritance.ValidateContext;

import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.Project;

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
     * @param dc The DialogContext containing the field values and states.
     * @return
     */
    public boolean isValid(DialogContext dc)
    {
        // if any of the default validation failed, leave now
        if(! super.isValid(dc))
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
            validateContext.getBirthDateField().invalidate(dc, "If you want the age to be "+ age +" then birth date should be in year " + (currentYear - age));
            validateContext.getAgeField().invalidate(dc, "If you want the birth year to be "+ yearOfBirth +" then age should be " + (currentYear - yearOfBirth));

            // we return false so the dialog does not go into execute mode until we have valid data
            return false;
        }

        return true;
    }

    /**
     * The dialog execute method is called as soon as all data is entered and validated. This method is guaranteed to
     * only be called when all fields' data is valid.
     * @param writer The writer where HTML and other output goes -- this is most likely the same as the response stream
     *               but sometimes may be some other writer (if output is being redirected to a file or something).
     * @param dc The {@see DialogContext} contains all the current user's field values, flags, and state-related data.
     * @throws IOException
     * @throws DialogExecuteException
     */
    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        writer.write("Congratulations <b>" + dc.getFieldStates().getState("full_name").getValue().getTextValue() + "</b>! ");
        writer.write("It seems that your birthdate and ages match correctly.");
        writer.write("<p>");
        writer.write("This code demonstrated how you can take two three pieces of data, have Sparx individually validate " +
                     "the general fields such as text, date, and integer but add your own custom validator some other logic by " +
                     "overriding the isValid() method of the com.netspective.sparx.form.Dialog class.<p>");

        String relativePath = "/WEB-INF/classes/app/inheritance/SampleValidateDialog.java";
        writer.write("You may review the code at " + dc.getConsoleFileBrowserLinkShowAlt(dc.getServlet().getServletConfig().getServletContext().getRealPath(relativePath), relativePath));
        writer.write("<p>");
        writer.write("<a href=\""+ dc.getNavigationContext().getActivePage().getName() +"\">Clear the dialog</a>");
    }
}
