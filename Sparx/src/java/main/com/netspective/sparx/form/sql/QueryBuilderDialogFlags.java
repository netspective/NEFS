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
 * $Id: QueryBuilderDialogFlags.java,v 1.2 2003-07-01 05:57:24 aye.thu Exp $
 */

package com.netspective.sparx.form.sql;

import com.netspective.sparx.form.DialogFlags;

public class QueryBuilderDialogFlags extends DialogFlags
{
    public static final int HIDE_OUTPUT_DESTS = CUSTOM_START;
    public static final int ALLOW_DEBUG = HIDE_OUTPUT_DESTS * 2;
    public static final int HIDE_CRITERIA = ALLOW_DEBUG * 2;
    public static final int ALWAYS_SHOW_DSNAV = HIDE_CRITERIA * 2;
    public static final int ALLOW_MULTIPLE_SCROLL_STATES = ALWAYS_SHOW_DSNAV * 2; // allow multiple query select scroll states to be active

    public static final FlagDefn[] QBD_FLAG_DEFNS = new FlagDefn[DialogFlags.FLAG_DEFNS.length + 5];
    static
    {
        for(int i = 0; i < DialogFlags.FLAG_DEFNS.length; i++)
            QBD_FLAG_DEFNS[i] = DialogFlags.FLAG_DEFNS[i];
        QBD_FLAG_DEFNS[DialogFlags.FLAG_DEFNS.length + 0] = new FlagDefn(ACCESS_XDM, "HIDE_OUTPUT_DESTS", HIDE_OUTPUT_DESTS);
        QBD_FLAG_DEFNS[DialogFlags.FLAG_DEFNS.length + 1] = new FlagDefn(ACCESS_XDM, "ALLOW_DEBUG", ALLOW_DEBUG);
        QBD_FLAG_DEFNS[DialogFlags.FLAG_DEFNS.length + 2] = new FlagDefn(ACCESS_XDM, "HIDE_CRITERIA", HIDE_CRITERIA);
        QBD_FLAG_DEFNS[DialogFlags.FLAG_DEFNS.length + 3] = new FlagDefn(ACCESS_XDM, "ALWAYS_SHOW_DSNAV", ALWAYS_SHOW_DSNAV);
        QBD_FLAG_DEFNS[DialogFlags.FLAG_DEFNS.length + 4] = new FlagDefn(ACCESS_XDM, "ALLOW_MULTIPLE_SCROLL_STATES", ALLOW_MULTIPLE_SCROLL_STATES);
    }

    public QueryBuilderDialogFlags()
    {
        setFlag(ALLOW_DEBUG | READONLY_FIELDS_HIDDEN_UNLESS_HAVE_DATA | ALWAYS_SHOW_DSNAV);
    }

    public FlagDefn[] getFlagsDefns()
    {
        return QBD_FLAG_DEFNS;
    }
}

