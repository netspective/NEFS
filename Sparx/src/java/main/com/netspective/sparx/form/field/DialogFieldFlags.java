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
 * @author
 */

/**
 * $Id: DialogFieldFlags.java,v 1.2 2003-09-09 05:24:17 aye.thu Exp $
 */
package com.netspective.sparx.form.field;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.sparx.form.DialogContext;

/**
 */
public class DialogFieldFlags extends XdmBitmaskedFlagsAttribute
{
    // all these values are also defined in dialog.js (make sure they are always in sync)
    public static final int REQUIRED = 1;
    public static final int PRIMARY_KEY = REQUIRED * 2;
    public static final int UNAVAILABLE = PRIMARY_KEY * 2;
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
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "REQUIRED", DialogFieldFlags.REQUIRED),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "PRIMARY_KEY", DialogFieldFlags.PRIMARY_KEY),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "UNAVAILABLE", DialogFieldFlags.UNAVAILABLE),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "READ_ONLY", DialogFieldFlags.READ_ONLY),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "INITIAL_FOCUS", DialogFieldFlags.INITIAL_FOCUS),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "PERSIST", DialogFieldFlags.PERSIST),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "CREATE_ADJACENT_AREA", DialogFieldFlags.CREATE_ADJACENT_AREA),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "SHOW_CAPTION_AS_CHILD", DialogFieldFlags.SHOW_CAPTION_AS_CHILD),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "HIDDEN", DialogFieldFlags.INPUT_HIDDEN),
        new FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "HAS_CONDITIONAL_DATA", DialogFieldFlags.HAS_CONDITIONAL_DATA),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "COLUMN_BREAK_BEFORE", DialogFieldFlags.COLUMN_BREAK_BEFORE),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "COLUMN_BREAK_AFTER", DialogFieldFlags.COLUMN_BREAK_AFTER),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "BROWSER_READONLY", DialogFieldFlags.BROWSER_READONLY),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "IDENTIFIER", DialogFieldFlags.IDENTIFIER),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "READONLY_HIDDEN_UNLESS_HAS_DATA", DialogFieldFlags.READONLY_HIDDEN_UNLESS_HAS_DATA),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "READONLY_UNAVAILABLE_UNLESS_HAS_DATA", DialogFieldFlags.READONLY_UNAVAILABLE_UNLESS_HAS_DATA),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "DOUBLE_ENTRY", DialogFieldFlags.DOUBLE_ENTRY),
        new FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "SCANNABLE", DialogFieldFlags.SCANNABLE),
        new FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "AUTO_BLUR", DialogFieldFlags.AUTO_BLUR),
        new FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "SUBMIT_ONBLUR", DialogFieldFlags.SUBMIT_ONBLUR),
        new FlagDefn(DialogFieldFlags.ACCESS_XDM, "CREATE_ADJACENT_AREA_HIDDEN", DialogFieldFlags.CREATE_ADJACENT_AREA_HIDDEN),
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

    /**
     * Sets the dialog field this flag is associated with
     * @param field
     */
    public void setField(DialogField field)
    {
        this.field = field;
    }

    /**
     * Gets the dialog field this flag is associated with
     * @return dialog field object
     */
    public DialogField getField()
    {
        return field;
    }

    /**
     * Clears a flag
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
        else if (state != null && state.getField().carryFlag(flag))
        {
            DialogContext.DialogFieldStates fieldStates = state.getDialogContext().getFieldStates();
            DialogFields children = state.getField().getChildren();
            if (children != null)
            {
                for (int i=0; i < children.size(); i++)
                {
                    fieldStates.getState(children.get(i)).getStateFlags().clearFlag(flag);
                }
            }
        }
    }

    /**
     * Sets a flag
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
        else if (state != null && state.getField().carryFlag(flag))
        {
            DialogContext.DialogFieldStates fieldStates = state.getDialogContext().getFieldStates();
            DialogFields children = state.getField().getChildren();
            if (children != null)
            {
                for (int i=0; i < children.size(); i++)
                {
                    fieldStates.getState(children.get(i)).getStateFlags().setFlag(flag);
                }
            }
        }
    }

}
