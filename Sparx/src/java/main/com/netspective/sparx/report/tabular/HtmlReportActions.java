package com.netspective.sparx.report.tabular;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.navigate.NavigationContext;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Class for html report actions
 *
 * $Id: HtmlReportActions.java,v 1.4 2004-06-23 15:15:52 aye.thu Exp $
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
     *
     * @return
     */
    public HtmlReportAction createAction()
    {
        return new HtmlReportAction();
    }

    /**
     * Creates a specific report action
     *
     * @param type
     * @return
     */
    public HtmlReportAction createAction(int type)
    {
        HtmlReportAction action = new HtmlReportAction();
        action.setType(new HtmlReportAction.Type(type));
        return action;
    }

    /**
     * Gets a report action by index
     *
     * @param index
     * @return
     */
    public HtmlReportAction getByIndex(int index)
    {
        return (HtmlReportAction) actions.get(index);
    }

    /**
     * Gets report actions based on their type
     *
     * @param type  requested report action type
     * @return      an array of report actions of the requested type
     */
    public HtmlReportAction[] getByType(int type)
    {
        List newList = new ArrayList();
        int listSize = actions.size();
        for (int i=0; i < listSize; i++)
        {
            HtmlReportAction action = (HtmlReportAction) actions.get(i);
            if (action.getType().getValueIndex() == type)
                newList.add(action);
        }

        int newListSize = newList.size();
        HtmlReportAction[] actionList = new HtmlReportAction[newListSize];
        for (int j=0; j < newListSize; j++)
        {
            actionList[j] = (HtmlReportAction) newList.get(j);
        }
        return actionList;
    }

    /**
     * Gets all the actions as an array
     *
     * @return
     */
    public HtmlReportAction[] getAll()
    {
        HtmlReportAction[] actionList = new HtmlReportAction[actions.size()];
        for (int i=0; i < actionList.length; i++)
        {
            actionList[i] = (HtmlReportAction) actions.get(i);
        }
        return actionList;
    }


    /**
     * Adds a report action using its type as the index
     * @param action
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
