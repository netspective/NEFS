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
package com.netspective.sparx.report.tabular;

import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.xdm.XmlDataModelSchema;

/**
 * Class for html report actions
 * <p/>
 * $Id: HtmlReportActions.java,v 1.6 2004-08-15 02:27:28 shahid.shah Exp $
 */
public class HtmlReportActions
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    private List actions = new ArrayList();

    public HtmlReportActions()
    {
    }

    /**
     * Creates a new generic html report action
     */
    public HtmlReportAction createAction()
    {
        return new HtmlReportAction();
    }

    /**
     * Creates a specific report action
     */
    public HtmlReportAction createAction(int type)
    {
        HtmlReportAction action = new HtmlReportAction();
        action.setType(new HtmlReportAction.Type(type));
        return action;
    }

    /**
     * Gets a report action by index
     */
    public HtmlReportAction getByIndex(int index)
    {
        return (HtmlReportAction) actions.get(index);
    }

    /**
     * Gets report actions based on their type
     *
     * @param type requested report action type
     *
     * @return an array of report actions of the requested type
     */
    public HtmlReportAction[] getByType(int type)
    {
        List newList = new ArrayList();
        int listSize = actions.size();
        for(int i = 0; i < listSize; i++)
        {
            HtmlReportAction action = (HtmlReportAction) actions.get(i);
            if(action.getType().getValueIndex() == type)
                newList.add(action);
        }

        int newListSize = newList.size();
        HtmlReportAction[] actionList = new HtmlReportAction[newListSize];
        for(int j = 0; j < newListSize; j++)
        {
            actionList[j] = (HtmlReportAction) newList.get(j);
        }
        return actionList;
    }

    /**
     * Gets all the actions as an array
     */
    public HtmlReportAction[] getAll()
    {
        HtmlReportAction[] actionList = new HtmlReportAction[actions.size()];
        for(int i = 0; i < actionList.length; i++)
        {
            actionList[i] = (HtmlReportAction) actions.get(i);
        }
        return actionList;
    }


    /**
     * Adds a report action using its type as the index
     */
    public void addAction(HtmlReportAction action)
    {
        actions.add(action);
    }

    public int size()
    {
        return actions.size();
    }
}
