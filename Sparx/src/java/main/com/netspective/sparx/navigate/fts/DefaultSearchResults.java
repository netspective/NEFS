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
package com.netspective.sparx.navigate.fts;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import com.netspective.sparx.navigate.DefaultScrollableRowState;
import com.netspective.sparx.navigate.ScrollableRowsState;

public class DefaultSearchResults implements Serializable, FullTextSearchResults
{
    private static final Log log = LogFactory.getLog(DefaultSearchResults.class);

    protected class AutoCloseTask extends TimerTask
    {
        public void run()
        {
            if(log.isDebugEnabled())
                log.debug("Automatically closing " + this + " after " + autoCloseInactivityDuration + " milliseconds of inactivity.");
            searchResultsManager.timeOut(DefaultSearchResults.this);
        }
    }

    private final FullTextSearchResultsManager searchResultsManager;
    private final FullTextSearchPage searchPage;
    private final SearchExpression expression;
    private final Query query;
    private final SearchHits searchHits;
    private final ScrollableRowsState scrollState;
    private final long creationTime = System.currentTimeMillis();

    private boolean valid = true;
    private long lastAccessed = System.currentTimeMillis();
    private long autoCloseInactivityDuration = 0;
    private Timer autoCloseTimer;

    public DefaultSearchResults(final FullTextSearchResultsManager searchResultsManager, final FullTextSearchPage searchPage, final SearchExpression expression,
                                final Query query, final SearchHits searchHits, final int rowsPerPage, final long autoCloseInactivityDuration)
    {
        this.searchResultsManager = searchResultsManager;
        this.searchPage = searchPage;
        this.expression = expression;
        this.query = query;
        this.searchHits = searchHits;
        this.scrollState = new DefaultScrollableRowState(searchHits.length(), rowsPerPage, 10);
        setAutoCloseInactivityDuration(autoCloseInactivityDuration);
    }

    public String[][] getActivePageHitValues(String[] fieldNames) throws IOException
    {
        recordActivity();
        final int startRow = scrollState.getScrollActivePageStartRow();
        final int endRow = scrollState.getScrollActivePageEndRow();
        String[][] hitsMatrix = new String[endRow - startRow][fieldNames.length];
        for(int i = startRow; i < endRow; i++)
        {
            Document doc = searchHits.getDoc(i);
            String[] row = hitsMatrix[i - startRow];
            for(int j = 0; j < fieldNames.length; j++)
            {
                row[j] = doc.get(fieldNames[j]);
            }
        }
        return hitsMatrix;
    }

    public String[][] getActivePageHitValues() throws IOException
    {
        recordActivity();
        return getActivePageHitValues(searchPage.getRenderer().getHitsMatrixFieldNames());
    }

    public ScrollableRowsState getScrollState()
    {
        recordActivity();
        return scrollState;
    }

    public SearchExpression getExpression()
    {
        recordActivity();
        return expression;
    }

    public FullTextSearchPage getSearchPage()
    {
        recordActivity();
        return searchPage;
    }

    public SearchHits getHits()
    {
        recordActivity();
        return searchHits;
    }

    public Query getQuery()
    {
        recordActivity();
        return query;
    }

    public long getCreationTime()
    {
        return creationTime;
    }

    public long getLastAccessed()
    {
        return lastAccessed;
    }

    public boolean isValid()
    {
        return valid;
    }

    public long getAutoCloseInactivityDuration()
    {
        return autoCloseInactivityDuration;
    }

    public void setAutoCloseInactivityDuration(long autoCloseInactivityDuration)
    {
        if(autoCloseTimer != null)
            autoCloseTimer.cancel();

        this.autoCloseInactivityDuration = autoCloseInactivityDuration;
        scheduleAutoCloseCheck();
    }

    protected void recordActivity()
    {
        lastAccessed = System.currentTimeMillis();
        if(autoCloseTimer != null)
        {
            if(log.isDebugEnabled())
                log.debug("Activity recorded in " + this + ", resetting to auto close in " + autoCloseInactivityDuration + " milliseconds.");
            scheduleAutoCloseCheck();
        }
    }

    protected void scheduleAutoCloseCheck()
    {
        if(log.isDebugEnabled())
            log.debug("Setting " + this + " to auto close in " + autoCloseInactivityDuration + " milliseconds.");

        if(autoCloseTimer != null)
        {
            autoCloseTimer.cancel();
            autoCloseTimer = null;
        }

        if(autoCloseInactivityDuration > 0)
        {
            autoCloseTimer = new Timer();
            autoCloseTimer.schedule(new AutoCloseTask(), autoCloseInactivityDuration);
        }
    }
}
