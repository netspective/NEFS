/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: DialogFlags.java,v 1.4 2003-05-11 17:52:25 shahid.shah Exp $
 */

package com.netspective.sparx.form;

import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;

public class DialogFlags extends XdmBitmaskedFlagsAttribute
{
    // NOTE: when adding new flags, make sure to create them before the
    // last DLGFLAG_CUSTOM_START entry. This is because QueryBuilderDialog
    // extends this class and has additional flags that is based on the value
    // of DLGFLAG_CUSTOM_START.
    public static final int CONTENTS_FINALIZED = 1;
    public static final int RETAIN_ALL_REQUEST_PARAMS = CONTENTS_FINALIZED * 2;
    public static final int HIDE_READONLY_HINTS = RETAIN_ALL_REQUEST_PARAMS * 2;
    public static final int ENCTYPE_MULTIPART_FORMDATA = HIDE_READONLY_HINTS * 2;
    public static final int HIDE_HEADING_IN_EXEC_MODE = ENCTYPE_MULTIPART_FORMDATA * 2;
    public static final int READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA = HIDE_HEADING_IN_EXEC_MODE * 2;
    public static final int READONLY_FIELDS_UNAVAILABLE_UNLESS_HAVE_DATA = READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA * 2;
    public static final int DISABLE_CLIENT_VALIDATION = READONLY_FIELDS_UNAVAILABLE_UNLESS_HAVE_DATA * 2;
    public static final int TRANSLATE_ENTER_KEY_TO_TAB_KEY = DISABLE_CLIENT_VALIDATION * 2;
    public static final int SHOW_DATA_CHANGED_MESSAGE_ON_LEAVE = TRANSLATE_ENTER_KEY_TO_TAB_KEY * 2;
    public static final int DISABLE_CLIENT_KEYPRESS_FILTERS = SHOW_DATA_CHANGED_MESSAGE_ON_LEAVE * 2;
    public static final int CUSTOM_START = DISABLE_CLIENT_KEYPRESS_FILTERS * 2;

    public static final FlagDefn[] FLAG_DEFNS = new FlagDefn[]
    {
        new FlagDefn(DialogFlags.ACCESS_PRIVATE, "CONTENTS_FINALIZED", CONTENTS_FINALIZED),
        new FlagDefn(DialogFlags.ACCESS_PRIVATE, "RETAIN_ALL_REQUEST_PARAMS", RETAIN_ALL_REQUEST_PARAMS),
        new FlagDefn(DialogFlags.ACCESS_XDM, "HIDE_READONLY_HINTS", HIDE_READONLY_HINTS),
        new FlagDefn(DialogFlags.ACCESS_PRIVATE, "ENCTYPE_MULTIPART_FORMDATA", ENCTYPE_MULTIPART_FORMDATA),
        new FlagDefn(DialogFlags.ACCESS_PRIVATE, "HIDE_HEADING_IN_EXEC_MODE", HIDE_HEADING_IN_EXEC_MODE),
        new FlagDefn(DialogFlags.ACCESS_XDM, "READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA", READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA),
        new FlagDefn(DialogFlags.ACCESS_XDM, "READONLY_FIELDS_UNAVAILABLE_UNLESS_HAVE_DATA", READONLY_FIELDS_UNAVAILABLE_UNLESS_HAVE_DATA),
        new FlagDefn(DialogFlags.ACCESS_XDM, "DISABLE_CLIENT_VALIDATION", DISABLE_CLIENT_VALIDATION),
        new FlagDefn(DialogFlags.ACCESS_XDM, "TRANSLATE_ENTER_KEY_TO_TAB_KEY", TRANSLATE_ENTER_KEY_TO_TAB_KEY),
        new FlagDefn(DialogFlags.ACCESS_XDM, "SHOW_DATA_CHANGED_MESSAGE_ON_LEAVE", SHOW_DATA_CHANGED_MESSAGE_ON_LEAVE),
        new FlagDefn(DialogFlags.ACCESS_XDM, "DISABLE_CLIENT_KEYPRESS_FILTERS", DISABLE_CLIENT_KEYPRESS_FILTERS)
    };

    public DialogFlags()
    {
    }

    public FlagDefn[] getFlagsDefns()
    {
        return FLAG_DEFNS;
    }

    public void clearFlag(long flag)
    {
        super.clearFlag(flag);
        //TODO: ??if((flag & (REJECT_FOCUS | HIDDEN)) != 0)
        //    clearFlagRecursively(flag);
    }

    public void setFlag(long flag)
    {
        super.setFlag(flag);
        //TODO: ??if((flag & (REJECT_FOCUS | HIDDEN)) != 0)
        //    setFlagRecursively(flag);
    }
}

