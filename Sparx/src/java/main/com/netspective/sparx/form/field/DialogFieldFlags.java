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
package com.netspective.sparx.form.field;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;

/**
 * The <code>DialogFieldFlags</code> object specifies all the flags defined for
 * a dialog's fields.
 */
public class DialogFieldFlags extends XdmBitmaskedFlagsAttribute
{
    // all these values are also defined in dialog.js (make sure they are always in sync)
    public static final int REQUIRED = 1;
    public static final int PRIMARY_KEY = REQUIRED * 2;
    public static final int PRIMARY_KEY_GENERATED = PRIMARY_KEY * 2;
    public static final int UNAVAILABLE = PRIMARY_KEY_GENERATED * 2;
    public static final int READ_ONLY = UNAVAILABLE * 2;
    public static final int INITIAL_FOCUS = READ_ONLY * 2;
    public static final int PERSIST = INITIAL_FOCUS * 2;
    public static final int CREATE_ADJACENT_AREA = PERSIST * 2;
    public static final int SHOW_CAPTION_AS_CHILD = CREATE_ADJACENT_AREA * 2;
    public static final int INPUT_HIDDEN = SHOW_CAPTION_AS_CHILD * 2;
    public static final int HAS_CONDITIONAL_DATA = INPUT_HIDDEN * 2;
    public static final int COLUMN_BREAK_BEFORE = HAS_CONDITIONAL_DATA * 2;
    public static final int COLUMN_BREAK_AFTER = COLUMN_BREAK_BEFORE * 2;
    public static final int BROWSER_READONLY = COLUMN_BREAK_AFTER * 2;
    public static final int IDENTIFIER = BROWSER_READONLY * 2;
    public static final int READONLY_HIDDEN_UNLESS_HAS_DATA = IDENTIFIER * 2;
    public static final int READONLY_UNAVAILABLE_UNLESS_HAS_DATA = READONLY_HIDDEN_UNLESS_HAS_DATA * 2;
    public static final int DOUBLE_ENTRY = READONLY_UNAVAILABLE_UNLESS_HAS_DATA * 2;
    public static final int SCANNABLE = DOUBLE_ENTRY * 2;
    public static final int AUTO_BLUR = SCANNABLE * 2;
    public static final int SUBMIT_ONBLUR = AUTO_BLUR * 2;
    public static final int CREATE_ADJACENT_AREA_HIDDEN = SUBMIT_ONBLUR * 2;
    public static final int START_CUSTOM = CREATE_ADJACENT_AREA_HIDDEN * 2; // all DialogField "children" will use this

    public static final FlagDefn[] FLAG_DEFNS = new FlagDefn[]
    {
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "REQUIRED", DialogFieldFlags.REQUIRED, "Specifies whether the field is required or not. If this attribute is set, code is automatically generated to enforce this validation rule."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "PRIMARY_KEY", DialogFieldFlags.PRIMARY_KEY, "Specifies whether or not to treat the contents of this field as a primary key for a database table. When this attribute is set to true, then the field acts differently depending upon the Dialog's Data Perspective. If it's in add mode (data needs to be inserted), the field allows data entry by the end user. If the dialog is in edit or delete mode, the field's value becomes read-only (not able to be changed)."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "PRIMARY_KEY_GENERATED", DialogFieldFlags.PRIMARY_KEY_GENERATED, "Specifies whether or not to treat the contents of this field as an auto-generated primary key for a database table. When this attribute is set to true, the field acts differently depending upon the Dialog's Data Perspective. In add mode (data needs to be inserted), the field is made invisible to the end user as the value of the field is generated by the application. In edit or delete mode, the field's value becomes read-only (not able to be changed) but is made visible to the end user."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "UNAVAILABLE", DialogFieldFlags.UNAVAILABLE, "Specifies whether or not the field is available in the form. As an unavailable field, there is no value available to the programmer nor is there a caption/label, or control available to the end user. Setting a field as an unavailable field is almost like commenting out the field because most dialog skins will not process invisible fields."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "READ_ONLY", DialogFieldFlags.READ_ONLY, "If set, the field's value becomes a static text string (will not generate a real HTML control)."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "INITIAL_FOCUS", DialogFieldFlags.INITIAL_FOCUS, "Specifies whether to set the initial focus of the dialog to this field. The last field to have this value set to yes (in creation order) will have the focus when the dialog is first displayed."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "PERSIST", DialogFieldFlags.PERSIST, "Specifies whether or not to automatically remember the last contents of this field (as a browser cookie) for the next time the user loads the dialog. This can be used in place of the default attribute when the user's last input value should be used as the default for the field."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "CREATE_ADJACENT_AREA", DialogFieldFlags.CREATE_ADJACENT_AREA, "If set, creates a dynamic HTML block next to the current field so that data can be filled in.  It creates a quasi-control that can be filled in from a popup. The name of the quasi-control is xx_adjacent, where xx is the name of this field."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "SHOW_CAPTION_AS_CHILD", DialogFieldFlags.SHOW_CAPTION_AS_CHILD, "Specifies the behavior of the field's caption if it is being placed into a composite or grid field."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "HIDDEN", DialogFieldFlags.INPUT_HIDDEN, "Specifies whether the field is hidden or not. As a hidden field, the value of the field is still available to the programmer, but there is no field caption/label or input control available to the end user."),
        new FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "HAS_CONDITIONAL_DATA", DialogFieldFlags.HAS_CONDITIONAL_DATA),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "COLUMN_BREAK_BEFORE", DialogFieldFlags.COLUMN_BREAK_BEFORE, "Specifies whether a column break should be included before this field. The actual behavior of this attribute is determined by a skin, but typically if this field is a primary (top-level) field, the column break will create a dialog with multiple columns. If this field is a secondary field (child of composite), a simple line break will be inserted between the composite siblings."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "COLUMN_BREAK_AFTER", DialogFieldFlags.COLUMN_BREAK_AFTER, "Specifies whether a column break should be included after this field. The actual behavior of this attribute is determined by a skin, but typically if this field is a primary (top-level) field, the column break will create a dialog with multiple columns. If this field is a secondary field (child of composite), a simple line break will be inserted between the composite siblings."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "BROWSER_READONLY", DialogFieldFlags.BROWSER_READONLY, "If set, the field is treated as a non-editable control instead of a static text block in HTML."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "IDENTIFIER", DialogFieldFlags.IDENTIFIER, "Specifies whether or not to treat the contents of this field as an identifier. An identifier is a field whose values may only contain uppercase letters, numbers, and an underscore."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "READONLY_HIDDEN_UNLESS_HAS_DATA", DialogFieldFlags.READONLY_HIDDEN_UNLESS_HAS_DATA, "Specifies whether or not to make a read-only field hidden when there is no data available in that field."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "READONLY_UNAVAILABLE_UNLESS_HAS_DATA", DialogFieldFlags.READONLY_UNAVAILABLE_UNLESS_HAS_DATA, "Specifies whether or not to make a read-only field unavailable when there is no data available in that field."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "DOUBLE_ENTRY", DialogFieldFlags.DOUBLE_ENTRY, "If set, it requires the data to be entered twice before it is accepted. There is still a single field, but data entry happens twice and is matched at the client side before data is validated."),
        new FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "SCANNABLE", DialogFieldFlags.SCANNABLE),
        new FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "AUTO_BLUR", DialogFieldFlags.AUTO_BLUR, "Specifies whether or not to automatically leave a field after a particular number of characters."),
        new FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "SUBMIT_ONBLUR", DialogFieldFlags.SUBMIT_ONBLUR, "Specifies whether or not to submit the form when leaving the focus for this field."),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "CREATE_ADJACENT_AREA_HIDDEN", DialogFieldFlags.CREATE_ADJACENT_AREA_HIDDEN, "Performs same action as create-adjacent-area but hides the area so the value is available to the browser and server processing but not visible to the end user."),
    };

    private DialogField.State state = null;
    private DialogField field = null;

    public DialogFieldFlags()
    {
        super();
    }

    public DialogFieldFlags(int flags)
    {
        super(flags);
    }

    public DialogFieldFlags(DialogField field, int flags)
    {
        super(flags);
        this.field = field;
    }

    public DialogFieldFlags(DialogField.State state, int flags)
    {
        super(flags);
        this.state = state;
    }

    public DialogFieldFlags(DialogField df)
    {
        field = df;
    }

    public DialogFieldFlags(DialogField.State dfs)
    {
        state = dfs;
    }

    public FlagDefn[] getFlagsDefns()
    {
        return FLAG_DEFNS;
    }

    public DialogField.State getState()
    {
        return state;
    }

    public void setState(DialogField.State state)
    {
        this.state = state;
    }

    /**
     * Sets the dialog field this flag is associated with.
     *
     * @param field
     */
    public void setField(DialogField field)
    {
        this.field = field;
    }

    /**
     * Gets the dialog field this flag is associated with.
     *
     * @return dialog field object
     */
    public DialogField getField()
    {
        return field;
    }

    /**
     * Clears a flag. If this flag object belongs to a field and the flag is one of the carry over flags,
     * then all children fields will have their corresponding flags cleared.
     *
     * @param flag
     */
    public void clearFlag(long flag)
    {
        super.clearFlag(flag);
        // check to see if the flag object is related to the state or the field itself
        if (field != null && field.carryFlag(flag))
        {
            // check to see if the flag should be carried to the children
            if (field.getChildren() != null)
                field.getChildren().clearFlags(flag);
        }
    }

    /**
     * Sets a flag. If this flag object belongs to a field and the flag is one of the carry over flags,
     * then all children fields will have their corresponding flags set.
     *
     * @param flag
     */
    public void setFlag(long flag)
    {
        super.setFlag(flag);
        // check to see if the flag object is related to the state or the field itself
        if (field != null && field.carryFlag(flag))
        {
            // check to see if the flag should be carried to the children
            if (field.getChildren() != null)
                field.getChildren().setFlags(flag);
        }
    }

}
