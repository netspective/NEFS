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
package com.netspective.sparx.form;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;

/**
 * Class representing all the flags that can be set on a dialog
 */
public class DialogFlags extends XdmBitmaskedFlagsAttribute
{
    // NOTE: when adding new flags, make sure to create them before the
    // last CUSTOM_START entry. This is because QueryBuilderDialog
    // extends this class and has additional flags that is based on the value
    // of CUSTOM_START.

    // retain all the values coming from request parameters
    public static final int RETAIN_ALL_REQUEST_PARAMS = 1;
    // hide hints defined to read only fields
    public static final int HIDE_READONLY_HINTS = RETAIN_ALL_REQUEST_PARAMS * 2;
    // encrypt multipart form data
    public static final int ENCTYPE_MULTIPART_FORMDATA = HIDE_READONLY_HINTS * 2;
    // hide the heading of the dialog when dialog is in execution mode
    public static final int HIDE_HEADING_IN_EXEC_MODE = ENCTYPE_MULTIPART_FORMDATA * 2;
    // hide read only fields unless they have values in them
    public static final int READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA = HIDE_HEADING_IN_EXEC_MODE * 2;
    // include read only fields in the dialog onlny when they have data
    public static final int READONLY_FIELDS_UNAVAILABLE_UNLESS_HAVE_DATA = READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA * 2;
    // completely disable client side validation
    public static final int DISABLE_CLIENT_VALIDATION = READONLY_FIELDS_UNAVAILABLE_UNLESS_HAVE_DATA * 2;
    // treat the enter (return) key as the tab key
    public static final int TRANSLATE_ENTER_KEY_TO_TAB_KEY = DISABLE_CLIENT_VALIDATION * 2;
    // shows a message if no data has been changed at submission of the dialog
    public static final int SHOW_DATA_CHANGED_MESSAGE_ON_LEAVE = TRANSLATE_ENTER_KEY_TO_TAB_KEY * 2;
    // disables all the javascript keypress handlers
    public static final int DISABLE_CLIENT_KEYPRESS_FILTERS = SHOW_DATA_CHANGED_MESSAGE_ON_LEAVE * 2;
    // hides the field hints until the focus is on the field
    public static final int HIDE_HINTS_UNTIL_FOCUS = DISABLE_CLIENT_KEYPRESS_FILTERS * 2;
    // save the initial state of the dialog
    public static final int RETAIN_INITIAL_STATE = HIDE_HINTS_UNTIL_FOCUS * 2;
    // disable the auto-execution capability for this dialog (force input always)
    public static final int DISABLE_AUTO_EXECUTE = RETAIN_INITIAL_STATE * 2;
    // allow the dialog to execute multiple times (using back button)
    public static final int ALLOW_MULTIPLE_EXECUTES = DISABLE_AUTO_EXECUTE * 2;
    // allow pending data in the dialog
    public static final int ALLOW_PENDING_DATA = ALLOW_MULTIPLE_EXECUTES * 2;
    // when generating dialog context beans (form beans) for dialogs, generate it for this particular dialog
    public static final int GENERATE_DCB = ALLOW_PENDING_DATA * 2;
    // allow the dialog to execute when the cancel button is pressed
    public static final int ALLOW_EXECUTE_WITH_CANCEL_BUTTON = GENERATE_DCB * 2;
    // allow the dialog to execute when the cancel button is pressed
    public static final int DISABLE_ACTIVITY_ANNOUNCEMENT = ALLOW_EXECUTE_WITH_CANCEL_BUTTON * 2;
    // close the window after the dialog has successfully executed
    public static final int CLOSE_PAGE_AFTER_EXECUTE = DISABLE_ACTIVITY_ANNOUNCEMENT * 2;
    // custom start
    public static final int CUSTOM_START = CLOSE_PAGE_AFTER_EXECUTE * 2;

    public static final FlagDefn[] FLAG_DEFNS = new FlagDefn[]
    {
        new FlagDefn(DialogFlags.ACCESS_PRIVATE, "RETAIN_ALL_REQUEST_PARAMS", RETAIN_ALL_REQUEST_PARAMS, "If set, all the request parameters are retained for use in multiple invocations of this dialog during more than one request/response cycle."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "HIDE_READONLY_HINTS", HIDE_READONLY_HINTS, "If set, all the hints of fields in this dialog are hidden in read-only mode."),
        new FlagDefn(DialogFlags.ACCESS_PRIVATE, "ENCTYPE_MULTIPART_FORMDATA", ENCTYPE_MULTIPART_FORMDATA),
        new FlagDefn(DialogFlags.ACCESS_PRIVATE, "HIDE_HEADING_IN_EXEC_MODE", HIDE_HEADING_IN_EXEC_MODE, "If set, the heading for this dialog is hidden in dialog execution mode."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA", READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA, "If set, the read-only fields in this dialog are hidden when the field does not have a value. Field values are still available but the field is not visible."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "READONLY_FIELDS_UNAVAILABLE_UNLESS_HAVE_DATA", READONLY_FIELDS_UNAVAILABLE_UNLESS_HAVE_DATA, "If set, the read-only fields in this dialog are made invislble when the field does not have a value. Field is not visible nor does it have any value associated with it."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "DISABLE_CLIENT_VALIDATION", DISABLE_CLIENT_VALIDATION, "If set, the client-side validation Javascripts associated with this dialog's fields are not executed on form submission."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "TRANSLATE_ENTER_KEY_TO_TAB_KEY", TRANSLATE_ENTER_KEY_TO_TAB_KEY, "If set, the enter key acts the same as the tab key (move to next control)."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "SHOW_DATA_CHANGED_MESSAGE_ON_LEAVE", SHOW_DATA_CHANGED_MESSAGE_ON_LEAVE, "If set, a pop up message is shown when the user changes data on the form but leaves the form without pressing submit."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "DISABLE_CLIENT_KEYPRESS_FILTERS", DISABLE_CLIENT_KEYPRESS_FILTERS, "If set, it disables the built-in client-side keypress filters. Sparx has built-in capabilities to not allow certain keypressed in specific controls. For example, integer and float fields do not allow text so alphabetic keys are disabled."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "HIDE_HINTS_UNTIL_FOCUS", HIDE_HINTS_UNTIL_FOCUS, "If set, it hides the field hints until the control receives focus.  The default behavior is to show hints with the controls regardless of the key focus."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "RETAIN_INITIAL_STATE", RETAIN_INITIAL_STATE, "If set, the initial data values of this dialog are tracked (as they were in the form before the user made any changes). Used when there is a need to get access to the original data and compare it to the final data for doing partial inserts or for doing something only if a value has changed."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "DISABLE_AUTO_EXECUTE", DISABLE_AUTO_EXECUTE, "If set, the auto-execution capability for this dialog is disabled.  This means input is always needed for dialog execution."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "ALLOW_MULTIPLE_EXECUTES", ALLOW_MULTIPLE_EXECUTES, "If set, the dialog is allowed to execute multiple times (using back button). Otherwise, an error message is displayed everytime this dialog is executed using back button."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "ALLOW_PENDING_DATA", ALLOW_PENDING_DATA, "If set, data is allowed to be entered in the dialog fields but server side validation is not performed. Client-side validation is performed normally."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "GENERATE_DCB", GENERATE_DCB, "If set, a dialog context bean is automatically generated for this dialog, whenever Ant build is run using generate-dialog-context-beans target."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "ALLOW_EXECUTE_WITH_CANCEL_BUTTON", ALLOW_EXECUTE_WITH_CANCEL_BUTTON, "If set, the cancel button will cause the form to be submitted and the dialog's execute method will be called. The default is to just send a JavaScript 'history.back()' event."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "DISABLE_ACTIVITY_ANNOUNCEMENT", DISABLE_ACTIVITY_ANNOUNCEMENT, "If set, the dialog will NOT announce events to the activity manager so that observers are preventing from acting upon the activity."),
        new FlagDefn(DialogFlags.ACCESS_XDM, "CLOSE_PAGE_AFTER_EXECUTE", CLOSE_PAGE_AFTER_EXECUTE, "If set, the page will close itself after the dialog has successfully executed. This is useful for popup windows.")
    };

    public DialogFlags()
    {
    }

    public FlagDefn[] getFlagsDefns()
    {
        return FLAG_DEFNS;
    }

    /**
     * Clear the flag
     */
    public void clearFlag(long flag)
    {
        super.clearFlag(flag);
        //TODO: ??if((flag & (REJECT_FOCUS | HIDDEN)) != 0)
        //    clearFlagRecursively(flag);
    }

    /**
     * Sets the flag
     */
    public void setFlag(long flag)
    {
        super.setFlag(flag);
        //TODO: ??if((flag & (REJECT_FOCUS | HIDDEN)) != 0)
        //    setFlagRecursively(flag);
    }
}

