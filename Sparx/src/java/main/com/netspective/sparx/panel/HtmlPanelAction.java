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
 * $Id: HtmlPanelAction.java,v 1.3 2004-03-02 07:36:44 aye.thu Exp $
 */

package com.netspective.sparx.panel;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.sparx.form.field.DialogFieldFlags;

public class HtmlPanelAction
{
    public static final Flags.FlagDefn[] FLAG_DEFNS = new Flags.FlagDefn[]
    {
        new Flags.FlagDefn(DialogFieldFlags.ACCESS_PRIVATE, "HIDDEN", HtmlPanelAction.Flags.HIDDEN),
    };

    public class Flags extends XdmBitmaskedFlagsAttribute
    {
        public static final int HIDDEN = 1;
        private State state = null;

        public Flags()
        {

        }

        public Flags(int flags)
        {
            super(flags);
        }

        public FlagDefn[] getFlagsDefns()
        {
            return FLAG_DEFNS;
        }

        public State getState()
        {
            return state;
        }

        public void setState(State state)
        {
            this.state = state;
        }
    }

    public class State
    {
        private Flags stateFlags;
        private HtmlPanelValueContext panelContext;
        private HtmlPanelAction action;

        public State(HtmlPanelValueContext vc, HtmlPanelAction action)
        {
            this.panelContext = vc;
            this.action = action;
            this.stateFlags = action.createFlags();
            this.stateFlags.setState(this);
            initialize(vc);
        }

        private void initialize(HtmlPanelValueContext vc)
        {
            stateFlags.copy(getFlags());
        }

        public HtmlPanelAction getPanelAction()
        {
            return action;
        }

        /**
         * Gets the state flags associated with the action
         * @return
         */
        public HtmlPanelAction.Flags getStateFlags()
        {
            return stateFlags;
        }

    }

    private ValueSource icon;
    private ValueSource caption;
    private ValueSource hint;
    private RedirectValueSource redirect;
    private HtmlPanelActions children = new HtmlPanelActions();
    private Flags flags = createFlags();

    public ValueSource getCaption()
    {
        return caption;
    }

    public void setCaption(ValueSource caption)
    {
        this.caption = caption;
    }

    public ValueSource getHint()
    {
        return hint;
    }

    public void setHint(ValueSource hint)
    {
        this.hint = hint;
    }

    public ValueSource getIcon()
    {
        return icon;
    }

    public void setIcon(ValueSource icon)
    {
        this.icon = icon;
    }

    public RedirectValueSource getRedirect()
    {
        return redirect;
    }

    public void setRedirect(RedirectValueSource redirect)
    {
        this.redirect = redirect;
    }

    public HtmlPanelActions.Style getStyle()
    {
        return children.getStyle();
    }

    public void setStyle(HtmlPanelActions.Style style)
    {
        children.setStyle(style);
    }

    public HtmlPanelActions getChildren()
    {
        return children;
    }

    public void setChildren(HtmlPanelActions children)
    {
        this.children = children;
    }

    public HtmlPanelAction createAction()
    {
        return new HtmlPanelAction();
    }

    public void addAction(HtmlPanelAction item)
    {
        children.add(item);
    }

    /**
     * Creates a new state object for this panel action
     * @param vc  The dialog context which is the state of the dialog
     * @return
     */
    public State constructStateInstance(HtmlPanelValueContext vc)
    {
        return new State(vc, this);
    }

    /**
     * Create flags for the panel object
     * @return
     */
    public HtmlPanelAction.Flags createFlags()
    {
        return new Flags();
    }

    public Flags getFlags()
    {
        return flags;
    }

    /**
     * Sets the flags for the panel action
     * @param flags
     */
    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

}
