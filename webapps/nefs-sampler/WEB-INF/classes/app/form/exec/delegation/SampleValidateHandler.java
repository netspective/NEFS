/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 * @author Shahid N. Shah
 */

/**
 * $Id: SampleValidateHandler.java,v 1.1 2003-12-11 17:37:49 shahid.shah Exp $
 */

package app.form.exec.delegation;

import java.io.Writer;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

import auto.dcb.form.exec.delegation.ValidateContext;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.DialogValidationContext;
import com.netspective.sparx.form.handler.DialogExecuteHandler;
import com.netspective.sparx.form.listener.DialogValidateListener;
import com.netspective.sparx.form.listener.DialogPopulateListener;
import com.netspective.sparx.form.field.DialogField;

public class SampleValidateHandler implements DialogValidateListener, DialogPopulateListener, DialogExecuteHandler
{
    public void populateDialogValues(DialogContext dc, int formatType)
    {
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

    public void validateDialog(DialogValidationContext dvc)
    {
        // We're using the auto-generated dialog context bean (DCB) which is a type-safe wrapper around the
        // general-purpose DialogContext. To generate the DCB, you:
        //   1) add generate-dcb="yes" flag to <dialog> tag
        //   2) go to Console -> Project -> Ant Build page and run 'generate-dialog-context-beans' target

        ValidateContext validateContext = new ValidateContext(dvc.getDialogContext());
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
            dvc.addError("The Age and Birth Date fields do not match.");

            // now we'll add messages to each field as well
            validateContext.getBirthDateField().invalidate(dvc.getDialogContext(), "If you want the age to be "+ age +" then birth date should be in year " + (currentYear - age));
            validateContext.getAgeField().invalidate(dvc.getDialogContext(), "If you want the birth year to be "+ yearOfBirth +" then age should be " + (currentYear - yearOfBirth));
        }

        // we don't return true or false -- we just add errors to the DialogValidationContext which will
        // indicate that there are validation errors so that Sparx knows when to stop
    }

    public void executeDialog(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        writer.write("Congratulations <b>" + dc.getFieldStates().getState("full_name").getValue().getTextValue() + "</b>! ");
        writer.write("It seems that your birthdate and ages match correctly.");
        writer.write("<p>");
        writer.write("This code demonstrated how you can take two three pieces of data, have Sparx individually validate " +
                     "the general fields such as text, date, and integer but add your own custom validator some other logic by " +
                     "creating listeners for various events such as population, validation, and execution.<p>");

        String relativePath = "/WEB-INF/classes/app/delegation/SampleValidateHandler.java";
        writer.write("You may review the code at " + dc.getConsoleFileBrowserLinkShowAlt(dc.getServlet().getServletConfig().getServletContext().getRealPath(relativePath), relativePath));
        writer.write("<p>");
        writer.write("<a href=\""+ dc.getNavigationContext().getActivePage().getName() +"\">Clear the dialog</a>");
    }

}
