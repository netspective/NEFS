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

public class DialogPerspectives extends XdmBitmaskedFlagsAttribute
{
    public static final int NONE = 0;
    public static final int ADD = 1;
    public static final int EDIT = ADD * 2;
    public static final int DELETE = EDIT * 2;
    public static final int CONFIRM = DELETE * 2;
    public static final int PRINT = CONFIRM * 2;
    public static final int LAST = PRINT * 2;

    public static final FlagDefn[] PERSPECTIVE_DEFNS = new FlagDefn[]
    {
        new FlagDefn(DialogFlags.ACCESS_XDM, "ADD", ADD,
                     "Indicates that the dialog should be processed for adding/inserting records.",
                     "Declare the ADD dialog data perspective (${xdmAttrDetailAliasComment})"),
        new FlagDefn(DialogFlags.ACCESS_XDM, "EDIT", EDIT, "Indicates that the dialog should be processed for " +
                                                           "editing/updating records. This perspective will automatically make any primary-keys read-only.",
                     "Declare the EDIT dialog data perspective (${xdmAttrDetailAliasComment})"),
        new FlagDefn(DialogFlags.ACCESS_XDM, "DELETE", DELETE, "Indicates that the dialog should be processed for " +
                                                               "deleting/removing records. This perspective will automatically make all fields read-only " +
                                                               "(for confirmation) and allow submission.",
                     "Declare the DELETE dialog data perspective (${xdmAttrDetailAliasComment})"),
        new FlagDefn(DialogFlags.ACCESS_XDM, "PRINT", PRINT, "Indicates that the dialog should be processed for " +
                                                             "printing data. All the items become read-only and a few tweaks are made so that the dialog looks more " +
                                                             "like a report than a form.",
                     "Declare the PRINT dialog data perspective (${xdmAttrDetailAliasComment})"),
        new FlagDefn(DialogFlags.ACCESS_XDM, "CONFIRM", CONFIRM, "Indicates that the dialog should be processed for " +
                                                                 "confirming data. This perspective will automatically make all fields read-only (for confirmation) but " +
                                                                 "does not infer a further action.",
                     "Declare the CONFIRM dialog data perspective (${xdmAttrDetailAliasComment})"),
    };

    public DialogPerspectives()
    {
        setFlagDelimiter(",");
    }

    public FlagDefn[] getFlagsDefns()
    {
        return PERSPECTIVE_DEFNS;
    }
}
