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
package com.netspective.sparx.panel;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.RedirectValueSource;
import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.sparx.form.field.DialogFieldFlags;

/**
 * Class responsible for handling the action associated with a panel.
 */
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

    /**
     * Sets the caption to be displayed for the action.
     *
     * @param caption value source object containing the caption for panel action
     */
    public void setCaption(ValueSource caption)
    {
        this.caption = caption;
    }

    public ValueSource getHint()
    {
        return hint;
    }

    /**
     * Hint to be displayed for the panel action.
     *
     * @param hint value source object containing the hint
     */
    public void setHint(ValueSource hint)
    {
        this.hint = hint;
    }

    public ValueSource getIcon()
    {
        return icon;
    }

    /**
     * The icon to be displayed for the panel action link(s).
     *
     * @param icon value source pointing to the icon for this panel action link(s)
     */
    public void setIcon(ValueSource icon)
    {
        this.icon = icon;
    }

    public RedirectValueSource getRedirect()
    {
        return redirect;
    }

    /**
     * Sets the URI to redirect to when this action is selected by the user.
     *
     * @param redirect value soruce object containing the URI of the page (within
     *                 application context) to redirect to
     */
    public void setRedirect(RedirectValueSource redirect)
    {
        this.redirect = redirect;
    }

    public HtmlPanelActions.Style getStyle()
    {
        return children.getStyle();
    }

    /**
     * Sets the style in which to arrange the action link(s).
     *
     * @param style style object containing the style description for the panel
     *              action link(s).
     */
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
     *
     * @param vc The dialog context which is the state of the dialog
     */
    public State constructStateInstance(HtmlPanelValueContext vc)
    {
        return new State(vc, this);
    }

    /**
     * Create flags for the panel object
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
     */
    public void setFlags(Flags flags)
    {
        this.flags.copy(flags);
    }

}
