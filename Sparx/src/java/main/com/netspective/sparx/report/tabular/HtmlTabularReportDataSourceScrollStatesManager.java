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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

import com.netspective.commons.report.tabular.TabularReportDataSourceScrollState;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.value.HttpServletValueContext;

/**
 * An implementation of HtmlTabularReportDataSourceScrollStates that stores the scroll states in a Map and provides
 * for a periodic check and retirement of inactive scroll states.
 */
public class HtmlTabularReportDataSourceScrollStatesManager implements HtmlTabularReportDataSourceScrollStates
{
    private static final String ATTRNAME_ACTIVE_SCROLL_STATE = "active-scroll-state";
    private static final long DEFAULT_TIMEOUT_CHECK_DELAY = 3 * 60 * 1000; // 3 minutes
    private static final Set allScrollStateManagers = new HashSet();

    public static Set getAllScrollStateManagers()
    {
        return allScrollStateManagers;
    }

    public static void closeAllScrollStates(Log log)
    {
        for (Iterator i = allScrollStateManagers.iterator(); i.hasNext();)
        {
            HtmlTabularReportDataSourceScrollStatesManager states = (HtmlTabularReportDataSourceScrollStatesManager) i.next();
            int count = states.closeAll();
            log.debug("Closed " + count + " scroll states from " + states);
        }
    }

    private Map scrollStates = Collections.synchronizedMap(new HashMap());
    private long timeoutDuration = DEFAULT_TIMEOUT_CHECK_DELAY;

    public HtmlTabularReportDataSourceScrollStatesManager()
    {
        allScrollStateManagers.add(this);
    }

    public long getTimeoutDuration()
    {
        return timeoutDuration;
    }

    public void setTimeoutDuration(long timeoutDuration)
    {
        this.timeoutDuration = timeoutDuration;
    }

    public Map getScrollStates()
    {
        return scrollStates;
    }

    public HtmlTabularReportDataSourceScrollState getScrollStateByDialogTransactionId(DialogContext dc)
    {
        return getScrollStateByDialogTransactionId(dc, dc.getDialogState().getIdentifier());
    }

    public HtmlTabularReportDataSourceScrollState getScrollStateByDialogTransactionId(HttpServletValueContext vc, String id)
    {
        HtmlTabularReportDataSourceScrollState state =
                (HtmlTabularReportDataSourceScrollState) scrollStates.get(vc.getHttpRequest().getSession().getId() + "." + id);

        // since set the data source to auto close after a period of time, make sure the data source didn't close itself
        if (state != null && !state.isClosed())
            return state;
        else
            return null;
    }

    public HtmlTabularReportDataSourceScrollState getActiveScrollState(HttpServletValueContext vc)
    {
        HtmlTabularReportDataSourceScrollState state = (HtmlTabularReportDataSourceScrollState) scrollStates.get(vc.getHttpRequest().getSession().getId() + "." + ATTRNAME_ACTIVE_SCROLL_STATE);

        // since set the data source to auto close after a period of time, make sure the data source didn't close itself
        if (state != null && !state.isClosed())
            return state;
        else
            return null;
    }

    public void setActiveScrollState(DialogContext dc, TabularReportDataSourceScrollState state)
    {
        state.getDataSource().setAutoClose(timeoutDuration);

        final String sessionId = dc.getHttpRequest().getSession().getId();
        scrollStates.put(sessionId + "." + state.getIdentifier(), state);
        scrollStates.put(sessionId + "." + ATTRNAME_ACTIVE_SCROLL_STATE, state);
    }

    public void removeActiveState(HttpServletValueContext vc, TabularReportDataSourceScrollState state)
    {
        final String sessionId = vc.getHttpRequest().getSession().getId();
        scrollStates.remove(sessionId + "." + state.getIdentifier());
        scrollStates.remove(sessionId + "." + ATTRNAME_ACTIVE_SCROLL_STATE);
        state.close();
    }

    public void removeActiveState(HttpServletValueContext vc)
    {
        TabularReportDataSourceScrollState state = getActiveScrollState(vc);
        if (state != null)
            removeActiveState(vc, state);
    }

    public int closeAll()
    {
        Set statesRetired = new HashSet();

        for (Iterator i = scrollStates.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry entry = (Map.Entry) i.next();
            HtmlTabularReportDataSourceScrollState state = (HtmlTabularReportDataSourceScrollState) entry.getValue();

            if (!statesRetired.contains(state))
            {
                statesRetired.add(state);
                state.close();
            }
        }

        scrollStates.clear();
        return statesRetired.size();
    }
}
