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
 * $Id: HtmlTabularReportDataSourceMapScrollStates.java,v 1.1 2003-09-14 05:39:58 shahid.shah Exp $
 */

package com.netspective.sparx.report.tabular;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import org.apache.commons.logging.Log;

import com.netspective.commons.report.tabular.TabularReportDataSourceScrollState;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.value.HttpServletValueContext;

/**
 * An implementation of HtmlTabularReportDataSourceScrollStates that stores the scroll states in a Map and provides
 * for a periodic check and retirement of inactive scroll states.
 */
public class HtmlTabularReportDataSourceMapScrollStates implements HtmlTabularReportDataSourceScrollStates
{
    private static final String ATTRNAME_ACTIVE_SCROLL_STATE = "active-scroll-state";
    private static final long DEFAULT_TIMEOUT_CHECK_DELAY = 5 * 60 * 1000; // 5 minutes
    private static final Set allScrollStateManagers = new HashSet();

    public static Set getAllScrollStateManagers()
    {
        return allScrollStateManagers;
    }

    public static void timeoutAllScrollStates(Log log)
    {
        for(Iterator i = allScrollStateManagers.iterator(); i.hasNext(); )
        {
            HtmlTabularReportDataSourceMapScrollStates states = (HtmlTabularReportDataSourceMapScrollStates) i.next();
            states.timeoutCheckTimer.cancel();
            int count = states.timouteAll();
            log.debug("Timed out " + count + " scroll states from " + states);
        }
    }

    private Map scrollStates = Collections.synchronizedMap(new HashMap());
    private long timeoutDuration;
    private long timeoutCheckDelay;
    private Timer timeoutCheckTimer;

    class RetireInactiveStatesTask extends TimerTask
    {
        public void run()
        {
            Set statesRetired = new HashSet();
            Set keysRetired = new HashSet();

            for(Iterator i = scrollStates.entrySet().iterator(); i.hasNext(); )
            {
                Map.Entry entry = (Map.Entry) i.next();
                HtmlTabularReportDataSourceScrollState state = (HtmlTabularReportDataSourceScrollState) entry.getValue();
                if(state.getInactivityTime() > timeoutDuration)
                {
                    keysRetired.add(entry.getKey());
                    if(! statesRetired.contains(state))
                    {
                        statesRetired.add(state);
                        state.timeOut();
                    }
                }
            }

            for(Iterator i = keysRetired.iterator(); i.hasNext(); )
                scrollStates.remove(i.next());
        }
    }

    public HtmlTabularReportDataSourceMapScrollStates()
    {
        setTimeoutCheckDelay(DEFAULT_TIMEOUT_CHECK_DELAY);
        allScrollStateManagers.add(this);
    }

    public long getTimeoutCheckDelay()
    {
        return timeoutCheckDelay;
    }

    public void setTimeoutCheckDelay(long timeoutCheckDelay)
    {
        this.timeoutCheckDelay = timeoutCheckDelay;
        if(timeoutCheckTimer != null)
            timeoutCheckTimer.cancel();
        timeoutCheckTimer = new Timer();
        timeoutCheckTimer.schedule(new RetireInactiveStatesTask(), timeoutCheckDelay);
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
        return (HtmlTabularReportDataSourceScrollState) scrollStates.get(dc.getHttpRequest().getSession().getId() + "." + dc.getTransactionId());
    }

    public HtmlTabularReportDataSourceScrollState getActiveScrollState(HttpServletValueContext vc)
    {
        return (HtmlTabularReportDataSourceScrollState) scrollStates.get(vc.getHttpRequest().getSession().getId() + "." + ATTRNAME_ACTIVE_SCROLL_STATE);
    }

    public void setActiveScrollState(DialogContext dc, TabularReportDataSourceScrollState state)
    {
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
        if(state != null)
            removeActiveState(vc, state);
    }

    public int timouteAll()
    {
        Set statesRetired = new HashSet();

        for(Iterator i = scrollStates.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) i.next();
            HtmlTabularReportDataSourceScrollState state = (HtmlTabularReportDataSourceScrollState) entry.getValue();

            if(! statesRetired.contains(state))
            {
                statesRetired.add(state);
                state.timeOut();
            }
        }

        scrollStates.clear();
        return statesRetired.size();
    }
}
