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
package com.netspective.sparx.navigate.query;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.sparx.navigate.DefaultScrollableRowState;
import com.netspective.sparx.navigate.ScrollableRowsState;

public class DefaultQueryResultsNavigatorState implements QueryResultsNavigatorState, Serializable, HttpSessionBindingListener
{
    private static final Log log = LogFactory.getLog(DefaultQueryResultsNavigatorState.class);

    protected class AutoCloseTask extends TimerTask
    {
        public void run()
        {
            if(log.isDebugEnabled())
                log.debug("Automatically closing " + this + " after " + autoCloseInactivityDuration + " milliseconds of inactivity.");
            close();
        }
    }

    private final String executionIdentifer;
    private final ResultSet resultSet;
    private final int columnCount;
    private final QueryResultSet queryResultSet;
    private final ScrollableRowsState scrollState;
    private final long creationTime = System.currentTimeMillis();

    private boolean valid = true;
    private long lastAccessed = System.currentTimeMillis();
    private long autoCloseInactivityDuration = 0;
    private Timer autoCloseTimer;

    public DefaultQueryResultsNavigatorState(String executionId, QueryResultSet queryResultSet, int maxRowsPerPage, long autoCloseInactivityDuration) throws SQLException
    {
        this.executionIdentifer = executionId;
        this.resultSet = queryResultSet.getResultSet();
        this.columnCount = resultSet.getMetaData().getColumnCount();
        this.queryResultSet = queryResultSet;
        this.scrollState = new DefaultScrollableRowState(getTotalRowsInResultSet(), maxRowsPerPage, 10);
        setAutoCloseInactivityDuration(autoCloseInactivityDuration);
    }

    public String getExecutionIdentifer()
    {
        return executionIdentifer;
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

    protected int getTotalRowsInResultSet()
    {
        final int totalRows;
        try
        {
            // get the current row number
            int currentRow = resultSet.getRow();
            resultSet.last();
            totalRows = resultSet.getRow();
            if(currentRow == 0)
            // there is no current row so THE DEFAULT BEHAVIOR is to send the cursor to before the first row
                resultSet.beforeFirst();
            else
            // set the cusor back to the original row
                resultSet.absolute(currentRow);
        }
        catch(SQLException e)
        {
            log.error("Unable to get total rows", e);
            throw new NestableRuntimeException(e);
        }

        return totalRows;
    }

    public QueryResultSet getQueryResultSet()
    {
        recordActivity();
        return queryResultSet;
    }

    public ScrollableRowsState getScrollState()
    {
        recordActivity();
        return scrollState;
    }

    public Object[][] getActivePageColumnValues() throws SQLException
    {
        recordActivity();
        final int startRow = scrollState.getScrollActivePageStartRow();
        final int endRow = scrollState.getScrollActivePageEndRow();
        Object[][] dataMatrix = new Object[endRow - startRow][columnCount];
        resultSet.absolute(startRow + 1);
        for(int i = startRow; i < endRow; i++)
        {
            Object[] row = dataMatrix[i - startRow];
            for(int j = 0; j < columnCount; j++)
                row[j] = resultSet.getObject(j + 1);
            resultSet.next();
        }
        return dataMatrix;
    }

    public Object[][] getActivePageColumnValuesByColNames(String[] columnNames) throws SQLException
    {
        recordActivity();
        final int startRow = scrollState.getScrollActivePageStartRow();
        final int endRow = scrollState.getScrollActivePageEndRow();
        Object[][] dataMatrix = new Object[endRow - startRow][columnNames.length];
        resultSet.absolute(startRow + 1);
        for(int i = startRow; i < endRow; i++)
        {
            Object[] row = dataMatrix[i - startRow];
            for(int j = 0; j < columnNames.length; j++)
                row[j] = resultSet.getObject(columnNames[j]);
            resultSet.next();
        }
        return dataMatrix;
    }

    public Object[][] getActivePageColumnValuesByColNumbers(int[] columnNumbers) throws SQLException
    {
        recordActivity();
        final int startRow = scrollState.getScrollActivePageStartRow();
        final int endRow = scrollState.getScrollActivePageEndRow();
        Object[][] dataMatrix = new Object[endRow - startRow][columnNumbers.length];
        resultSet.absolute(startRow + 1);
        for(int i = startRow; i < endRow; i++)
        {
            resultSet.next();
            Object[] row = dataMatrix[i - startRow];
            for(int j = 0; j < columnNumbers.length; j++)
                row[j] = resultSet.getObject(columnNumbers[j]);
            resultSet.next();
        }
        return dataMatrix;
    }

    public void close()
    {
        if(!valid)
            return;

        if(log.isDebugEnabled())
            log.debug("Closing " + this + ".");

        if(autoCloseTimer != null)
        {
            autoCloseTimer.cancel();
            autoCloseTimer = null;
        }
        try
        {
            valid = false;
            queryResultSet.close(true);
        }
        catch(SQLException e)
        {
            log.error("Unable to close query result set while unbinding from session.");
        }
    }

    public void valueBound(HttpSessionBindingEvent event)
    {
        // do nothing
    }

    public void valueUnbound(HttpSessionBindingEvent event)
    {
        close();
    }
}
