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
package com.netspective.sparx.form.field.type;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldFlags;

public class SocialSecurityField extends TextField
{
    public static final String VALIDATE_PATTERN = "^([\\d]{3})[-]?([\\d]{2})[-]?([\\d]{4})$";
    public static final String DISPLAY_SUBSTITUTION_PATTERN = "s/" + VALIDATE_PATTERN + "/$1-$2-$3/g";
    public static final String SUBMIT_SUBSTITUTION_PATTERN = "s/" + VALIDATE_PATTERN + "/$1$2$3/g";

    public static final Flags.FlagDefn[] SSN_FIELD_FLAG_DEFNS = new Flags.FlagDefn[TextField.TEXT_FIELD_FLAG_DEFNS.length + 1];

    static
    {
        for(int i = 0; i < TextField.TEXT_FIELD_FLAG_DEFNS.length; i++)
            SSN_FIELD_FLAG_DEFNS[i] = TextField.TEXT_FIELD_FLAG_DEFNS[i];
        SSN_FIELD_FLAG_DEFNS[TextField.TEXT_FIELD_FLAG_DEFNS.length + 0] = new Flags.FlagDefn(Flags.ACCESS_XDM, "STRIP_DASHES", Flags.STRIP_DASHES);
    }

    public class Flags extends TextField.Flags
    {
        public static final int STRIP_DASHES = TextField.Flags.START_CUSTOM;
        public static final int START_CUSTOM = STRIP_DASHES * 2;

        public Flags()
        {
            setFlag(STRIP_DASHES);
        }

        public Flags(State dfs)
        {
            super(dfs);
            setFlag(STRIP_DASHES);
        }

        public FlagDefn[] getFlagsDefns()
        {
            return SSN_FIELD_FLAG_DEFNS;
        }
    }

    public class State extends TextFieldState
    {
        public State(DialogContext dc, DialogField field)
        {
            super(dc, field);
        }

    }

    public SocialSecurityField()
    {
        super();
        setRegExpr(VALIDATE_PATTERN);
        setInvalidRegExMessage("The SSN must be of the format 999-99-9999 or 999999999.");
        setDisplayPattern(DISPLAY_SUBSTITUTION_PATTERN);
    }

    public DialogField.State constructStateInstance(DialogContext dc)
    {
        return new State(dc, this);
    }

    public Class getStateClass()
    {
        return SocialSecurityField.State.class;
    }

    public DialogFieldFlags createFlags()
    {
        return new Flags();
    }

    public String getSubmitPattern()
    {
        return getFlags().flagIsSet(Flags.STRIP_DASHES) ? SUBMIT_SUBSTITUTION_PATTERN : null;
    }
}