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
package com.netspective.axiom.sql;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.value.ValueContext;

public class QueryExecutionLog extends ArrayList
{
    private static final Log log = LogFactory.getLog(QueryExecutionLog.class);

    public final class QueryExecutionStatistics
    {
        public int resetAfterCount;
        public int totalExecutions;
        public int totalFailed;

        public long averageTotalExecTime;
        public long maxTotalExecTime;
        public long sumTotalExecTime;

        public long averageConnectionEstablishTime;
        public long maxConnectionEstablishTime;
        public long sumConnectionEstablishTime;

        public long averageBindParamsTime;
        public long maxBindParamsTime;
        public long sumBindParamsTime;

        public long averageSqlExecTime;
        public long maxSqlExecTime;
        public long sumSqlExecTime;

        public QueryExecutionStatistics()
        {
        }
    }

    /* resetLogAfterCount
		 value 0 means never reset
		 value > 0 means reset after this many entries
	*/
    private int resetLogAfterCount = -1;

    public QueryExecutionLog()
    {
    }

    public void setResetLogAfterCount(int resetLogAfterCount)
    {
        this.resetLogAfterCount = resetLogAfterCount;
    }

    public int getResetLogAfterCount()
    {
        return resetLogAfterCount;
    }

    public QueryExecutionLogEntry createNewEntry(ValueContext vc, String identifier) throws SQLException
    {
        if(resetLogAfterCount > 0 && size() >= resetLogAfterCount)
            clear();

        String locator = vc.getContextLocation() != null ? vc.getContextLocation().toString() : "<no locator>";
        QueryExecutionLogEntry result = new QueryExecutionLogEntry(identifier, locator);
        add(result);
        return result;
    }

    public QueryExecutionStatistics getStatistics()
    {
        QueryExecutionStatistics stats = new QueryExecutionStatistics();

        int items = size();
        int failed = 0;
        int successful = 0;

        for(int i = 0; i < items; i++)
        {
            QueryExecutionLogEntry entry = (QueryExecutionLogEntry) get(i);
            if(!entry.wasSuccessful())
            {
                failed++;
                continue;
            }

            long totalTime = entry.getTotalExecutionTime();
            stats.sumTotalExecTime += totalTime;
            if(totalTime > stats.maxTotalExecTime)
                stats.maxTotalExecTime = totalTime;

            long connTime = entry.getConnectionEstablishTime();
            stats.sumConnectionEstablishTime += connTime;
            if(connTime > stats.maxConnectionEstablishTime)
                stats.maxConnectionEstablishTime = connTime;

            long bindParamsTime = entry.getBindParamsBindTime();
            stats.sumBindParamsTime += bindParamsTime;
            if(bindParamsTime > stats.maxBindParamsTime)
                stats.maxBindParamsTime = bindParamsTime;

            long sqlExecTime = entry.getSqlExecTime();
            stats.sumSqlExecTime += sqlExecTime;
            if(sqlExecTime > stats.maxSqlExecTime)
                stats.maxSqlExecTime = sqlExecTime;

            successful++;
        }

        stats.resetAfterCount = resetLogAfterCount;
        stats.totalExecutions = items;
        stats.totalFailed = failed;

        if(successful == 0)
            return stats;

        stats.averageTotalExecTime = stats.sumTotalExecTime / successful;
        stats.averageConnectionEstablishTime = stats.sumConnectionEstablishTime / successful;
        stats.averageBindParamsTime = stats.sumBindParamsTime / successful;
        stats.averageSqlExecTime = stats.sumSqlExecTime / successful;

        return stats;
    }
}