package com.netspective.sparx.report.tabular;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aye
 * $Id: HtmlReportActions.java,v 1.1 2003-07-11 14:39:56 aye.thu Exp $
 */
public class HtmlReportActions
{
    private List actions = new ArrayList();

    public HtmlReportActions()
    {
    }

    public List getActions()
    {
        return actions;
    }

    public void setActions(List actions)
    {
        this.actions = actions;
    }

    /**
     * Gets a report action by index
     * @param type
     * @return
     */
    public HtmlReportAction get(int type)
    {
        return (HtmlReportAction) actions.get(type);
    }

    /**
     * Adds a report action
     * @param action
     */
    public void add(HtmlReportAction action)
    {
        actions.add(action);
    }

    public int size()
    {
        return actions.size();
    }
}
