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

import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogPerspectives;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFieldFlags;
import com.netspective.sparx.form.field.conditional.DialogFieldConditionalApplyFlag;
import com.netspective.sparx.form.handler.DialogNextActionProvider;

public class DirectorNextActionsSelectField extends SelectField implements DialogNextActionProvider
{
    public static final String DEFAULT_NAME = "director_next_actions";

    public static final Flags.FlagDefn[] NEXT_ACTION_FIELD_FLAG_DEFNS = new Flags.FlagDefn[SelectField.SELECT_FIELD_FLAG_DEFNS.length + 1];

    static
    {
        for(int i = 0; i < SelectField.SELECT_FIELD_FLAG_DEFNS.length; i++)
            NEXT_ACTION_FIELD_FLAG_DEFNS[i] = SelectField.SELECT_FIELD_FLAG_DEFNS[i];
        NEXT_ACTION_FIELD_FLAG_DEFNS[SelectField.SELECT_FIELD_FLAG_DEFNS.length + 0] = new Flags.FlagDefn(Flags.ACCESS_XDM, "DISPLAY_SINGLE_ACTION", Flags.DISPLAY_SINGLE_ACTION);
    }

    public class Flags extends SelectField.Flags
    {
        public static final int DISPLAY_SINGLE_ACTION = SelectField.Flags.START_CUSTOM;
        public static final int START_CUSTOM = DISPLAY_SINGLE_ACTION * 2;

        public Flags()
        {
        }

        public Flags(State dfs)
        {
            super(dfs);
        }

        public FlagDefn[] getFlagsDefns()
        {
            return NEXT_ACTION_FIELD_FLAG_DEFNS;
        }
    }

    private DialogPerspectives perspective = new DialogPerspectives();

    public DirectorNextActionsSelectField()
    {
        super();
        getFlags().setFlag(Flags.PERSIST);
    }

    public DialogFieldFlags createFlags()
    {
        return new Flags();
    }

    public void setParent(DialogField newParent)
    {
        super.setParent(newParent);
        if(getName() == null)
            setName(DEFAULT_NAME);
    }

    /**
     * The next actions field is a SelectField which has a caption and a value. The caption is displayed to the
     * user and the value is a URL which indicates where they want to go next. The URL can be either a String or
     * a ValueSource that can dynamically compute the next location based on the current location.
     */
    public String getDialogNextActionUrl(DialogContext dc, String defaultUrl)
    {
        String value = dc.getRequest().getParameter(getHtmlFormControlId());
        if(value == null)
            return defaultUrl;
        ValueSource svs = ValueSources.getInstance().getValueSourceOrStatic(value);
        if(svs == null)
            return defaultUrl;
        String url = svs.getTextValue(dc);
        if(url.equals("-"))
            return defaultUrl;
        return url;
    }

    public void makeStateChanges(DialogContext dc, int stage)
    {
        super.makeStateChanges(dc, stage);
        if(stage != DialogContext.STATECALCSTAGE_AFTER_VALIDATION)
            return;

        ValueSource choices = getChoices();
        PresentationValue pValue = choices != null ? choices.getPresentationValue(dc) : null;
        PresentationValue.Items items = pValue != null ? pValue.getItems() : null;

        int listSize = items != null ? items.size() : 0;
        if(listSize == 0)
            return;

        // if there's only a single item but we don't want to display "one item only" then get the value but hide the field
        if(listSize == 1 && !getFlags().flagIsSet(Flags.DISPLAY_SINGLE_ACTION))
        {
            SelectFieldState state = (SelectFieldState) dc.getFieldStates().getState(this);
            state.getValue().setTextValue(((PresentationValue.Items.Item) items.get(0)).getValue());
            state.getStateFlags().setFlag(Flags.INPUT_HIDDEN);
        }
    }

    public DialogPerspectives getPerspective()
    {
        return perspective;
    }

    public void setPerspective(DialogPerspectives perspective)
    {
        this.perspective.copy(perspective);
        if(this.perspective.getFlags() != DialogPerspectives.NONE)
        {
            getFlags().setFlag(Flags.INPUT_HIDDEN);
            DialogFieldConditionalApplyFlag dataCmdAction = new DialogFieldConditionalApplyFlag(this);
            dataCmdAction.getFlags().setFlag(Flags.INPUT_HIDDEN);
            dataCmdAction.setClear(true);
            dataCmdAction.setPerspective(this.perspective);
            addConditional(dataCmdAction);
        }
    }
}
