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
 * $Id: HtmlPanelFrame.java,v 1.4 2003-07-11 05:30:32 aye.thu Exp $
 */

package com.netspective.sparx.panel;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.sparx.panel.HtmlPanelAction;
import com.netspective.sparx.panel.HtmlPanelActions;

public class HtmlPanelFrame
{
    public static class Flags extends XdmBitmaskedFlagsAttribute
    {
        public static final int HAS_HEADING = 1;
        public static final int HAS_HEADING_EXTRA = HAS_HEADING * 2;
        public static final int HAS_FOOTING = HAS_HEADING_EXTRA * 2;
        public static final int IS_SELECTABLE = HAS_FOOTING * 2;
        public static final int ALLOW_COLLAPSE = IS_SELECTABLE * 2;
        public static final int IS_COLLAPSED = ALLOW_COLLAPSE * 2;
        public static final int HIDE_HEADING = IS_COLLAPSED * 2;


        public static final XdmBitmaskedFlagsAttribute.FlagDefn[] FLAGDEFNS = new XdmBitmaskedFlagsAttribute.FlagDefn[]
        {
            new XdmBitmaskedFlagsAttribute.FlagDefn(ACCESS_PRIVATE, "HAS_HEADING", HAS_HEADING),
            new XdmBitmaskedFlagsAttribute.FlagDefn(ACCESS_PRIVATE, "HAS_HEADING_EXTRA", HAS_HEADING_EXTRA),
            new XdmBitmaskedFlagsAttribute.FlagDefn(ACCESS_PRIVATE, "HAS_FOOTING", HAS_FOOTING),
            new XdmBitmaskedFlagsAttribute.FlagDefn(ACCESS_XDM, "IS_SELECTABLE", IS_SELECTABLE),
            new XdmBitmaskedFlagsAttribute.FlagDefn(ACCESS_XDM, "ALLOW_COLLAPSE", ALLOW_COLLAPSE),
            new XdmBitmaskedFlagsAttribute.FlagDefn(ACCESS_XDM, "IS_COLLAPSED", IS_COLLAPSED),
            new XdmBitmaskedFlagsAttribute.FlagDefn(ACCESS_XDM, "HIDE_HEADING", HIDE_HEADING)
        };

        public XdmBitmaskedFlagsAttribute.FlagDefn[] getFlagsDefns()
        {
            return FLAGDEFNS;
        }
    }

    private ValueSource heading;
    private ValueSource footing;
    private ValueSource allowSelect;
    private Flags flags = new Flags();
    private HtmlPanelActions actions = new HtmlPanelActions();

    public HtmlPanelFrame()
    {
    }

    public Flags getFlags()
    {
        return flags;
    }

    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

    public ValueSource getAllowSelect()
    {
        return allowSelect;
    }

    public void setAllowSelect(ValueSource vs)
    {
        allowSelect = vs;
        flags.updateFlag(Flags.IS_SELECTABLE, allowSelect != null);
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
        flags.updateFlag(Flags.HAS_HEADING, heading != null);
    }

    /**
     * Checks to see if the frame heading should be hidden
     * @return
     */
    public boolean hideHeading()
    {
        return flags.flagIsSet(Flags.HIDE_HEADING);
    }

    /**
     * Set or clear the hide status of the frame heading
     * @param hide
     */
    public void setHideHeading(boolean hide)
    {
         flags.updateFlag(Flags.HIDE_HEADING, hide);
    }

    public ValueSource getFooting()
    {
        return footing;
    }

    public void setFooting(ValueSource footing)
    {
        this.footing = footing;
        flags.updateFlag(Flags.HAS_FOOTING, footing != null);
    }

    public HtmlPanelActions getActions()
    {
        return actions;
    }

    public HtmlPanelAction createAction()
    {
        return new HtmlPanelAction();
    }

    public void addAction(HtmlPanelAction item)
    {
        actions.add(item);
    }
}