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
 * @author Shahid N. Shah
 */

/**
 * $Id: TabularReportFrame.java,v 1.1 2003-03-25 20:59:54 shahid.shah Exp $
 */

package com.netspective.commons.report.tabular;

import java.util.ArrayList;

import com.netspective.commons.value.ValueSource;

public class TabularReportFrame
{
    public final static long RPTFRAMEFLAG_HAS_HEADING = 1;
    public final static long RPTFRAMEFLAG_HAS_HEADINGEXTRA = RPTFRAMEFLAG_HAS_HEADING * 2;
    public final static long RPTFRAMEFLAG_HAS_FOOTING = RPTFRAMEFLAG_HAS_HEADINGEXTRA * 2;
    public final static long RPTFRAMEFLAG_HAS_ADD = RPTFRAMEFLAG_HAS_FOOTING * 2;
    public final static long RPTFRAMEFLAG_HAS_EDIT = RPTFRAMEFLAG_HAS_ADD * 2;
    public final static long RPTFRAMEFLAG_HAS_DELETE = RPTFRAMEFLAG_HAS_EDIT * 2;
    public static final long RPTFRAMEFLAG_IS_SELECTABLE = RPTFRAMEFLAG_HAS_DELETE * 2;
    public static final long RPTFRAMEFLAG_ALLOW_COLLAPSE = RPTFRAMEFLAG_IS_SELECTABLE * 2;
    public static final long RPTFRAMEFLAG_IS_COLLAPSED = RPTFRAMEFLAG_ALLOW_COLLAPSE * 2;

    private ValueSource heading;
    private ValueSource headingExtra;
    private ValueSource footing;
    private ValueSource allowSelect;
    private ValueSource recordAddCaption;
    private ValueSource recordAddUrl;
    private ValueSource recordEditUrl;
    private ValueSource recordDeleteUrl;
    private long flags;
    private ArrayList items;

    public TabularReportFrame()
    {
        heading = null;
        footing = null;
    }

    public ValueSource getAllowSelect()
    {
        return allowSelect;
    }

    public void setAllowSelect(ValueSource vs)
    {
        allowSelect = vs;
        applyFlag(RPTFRAMEFLAG_IS_SELECTABLE, allowSelect != null);
    }

    public final boolean flagIsSet(long flag)
    {
        return (flags & flag) == 0 ? false : true;
    }

    public long getFlags()
    {
        return flags;
    }

    public void setFlags(long flags)
    {
        this.flags = flags;
    }

    public final void setFlag(long flag)
    {
        flags |= flag;
    }

    public final void clearFlag(long flag)
    {
        flags &= ~flag;
    }

    public final void applyFlag(long flag, boolean apply)
    {
        if (apply)
            flags |= flag;
        else
            flags &= ~flag;
    }

    public boolean hasHeadingOrFooting()
    {
        return heading != null || footing != null;
    }

    public ValueSource getHeading()
    {
        return heading;
    }

    public void setHeading(ValueSource vs)
    {
        heading = vs;
        applyFlag(RPTFRAMEFLAG_HAS_HEADING, heading != null);
    }

    public ValueSource getHeadingExtra()
    {
        return headingExtra;
    }

    public void setHeadingExtra(ValueSource headingExtra)
    {
        this.headingExtra = headingExtra;
        applyFlag(RPTFRAMEFLAG_HAS_HEADINGEXTRA, headingExtra != null);
    }

    public ValueSource getFooting()
    {
        return footing;
    }

    public void setFooting(ValueSource footing)
    {
        this.footing = footing;
        applyFlag(RPTFRAMEFLAG_HAS_FOOTING, footing != null);
    }

    public ArrayList getItems()
    {
        return items;
    }

    public Item getItem(int n)
    {
        return (Item) items.get(n);
    }

    public void addItem(Item item)
    {
        if (items == null) items = new ArrayList();
        items.add(item);
    }

    public ValueSource getRecordAddCaption()
    {
        return recordAddCaption;
    }

    public void setRecordAddCaption(ValueSource recordItemName)
    {
        recordAddCaption = recordItemName;
        applyFlag(RPTFRAMEFLAG_HAS_ADD, recordAddCaption != null);
    }

    public ValueSource getRecordAddUrl()
    {
        return recordAddUrl;
    }

    public void setRecordAddUrl(ValueSource recordAddUrl)
    {
        this.recordAddUrl = recordAddUrl;
        applyFlag(RPTFRAMEFLAG_HAS_ADD, this.recordAddUrl != null);
    }

    public ValueSource getRecordDeleteUrl()
    {
        return recordDeleteUrl;
    }

    public void setRecordDeleteUrl(ValueSource recordDeleteUrl)
    {
        this.recordDeleteUrl = recordDeleteUrl;
        applyFlag(RPTFRAMEFLAG_HAS_DELETE, this.recordDeleteUrl != null);
    }

    public ValueSource getRecordEditUrl()
    {
        return recordEditUrl;
    }

    public void setRecordEditUrl(ValueSource recordEditUrl)
    {
        this.recordEditUrl = recordEditUrl;
        applyFlag(RPTFRAMEFLAG_HAS_EDIT, this.recordEditUrl != null);
    }

    public Item createAction()
    {
        return new Item();
    }

    public void addAction(Item item)
    {
        items.add(item);
    }

    static public class Item
    {
        private ValueSource icon;
        private ValueSource caption;
        private ValueSource url;

        public ValueSource getCaption()
        {
            return caption;
        }

        public void setCaption(ValueSource caption)
        {
            this.caption = caption;
        }

        public ValueSource getIcon()
        {
            return icon;
        }

        public void setIcon(ValueSource icon)
        {
            this.icon = icon;
        }

        public ValueSource getUrl()
        {
            return url;
        }

        public void setUrl(ValueSource url)
        {
            this.url = url;
        }
    }
}