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
 * $Id: QueryExecutionLogEntry.java,v 1.1 2003-03-13 18:25:43 shahid.shah Exp $
 */

package com.netspective.axiom.sql;

import java.util.Date;
import java.sql.SQLException;

import org.apache.commons.logging.Log;

import com.netspective.commons.value.ValueContext;

public class QueryExecutionLogEntry
{
    public static final String FIELD_SEPARATOR = ";";

    private String identifier;
    private boolean successful;
    private String source;
    private Date entryDate;
    private long initTime;
    private long getConnStartTime;
    private long getConnEndTime;
    private long bindParamsStartTime;
    private long bindParamsEndTime;
    private long execSqlStartTime;
    private long execSqlEndTime;

    public QueryExecutionLogEntry(String identifier, String source) throws SQLException
    {
        this.identifier = identifier;
        this.source = source;
        entryDate = new Date();
        initTime = System.currentTimeMillis();
    }

    public String getSource()
    {
        return source;
    }

    public boolean wasSuccessful()
    {
        return successful;
    }

    public void registerGetConnectionBegin()
    {
        getConnStartTime = System.currentTimeMillis();
    }

    public void registerGetConnectionEnd(java.sql.Connection conn)
    {
        getConnEndTime = System.currentTimeMillis();
    }

    public void registerBindParamsBegin()
    {
        bindParamsStartTime = System.currentTimeMillis();
    }

    public void registerBindParamsEnd()
    {
        bindParamsEndTime = System.currentTimeMillis();
    }

    public void registerExecSqlBegin()
    {
        execSqlStartTime = System.currentTimeMillis();
    }

    public void registerExecSqlEndSuccess()
    {
        execSqlEndTime = System.currentTimeMillis();
        successful = true;
    }

    public void registerExecSqlEndFailed()
    {
        execSqlEndTime = System.currentTimeMillis();
    }

    public void finalize(ValueContext vc, Log log)
    {
        if(!log.isInfoEnabled())
            return;

        StringBuffer info = new StringBuffer();
        info.append(identifier);
        info.append(FIELD_SEPARATOR);
        info.append(successful ? 1 : 0);
        info.append(FIELD_SEPARATOR);
        if(successful)
        {
            info.append(getConnEndTime - getConnStartTime);
            info.append(FIELD_SEPARATOR);
            info.append(bindParamsEndTime - bindParamsStartTime);
            info.append(FIELD_SEPARATOR);
            info.append(execSqlEndTime - execSqlStartTime);
            info.append(FIELD_SEPARATOR);
            info.append(execSqlEndTime - initTime);
            info.append(FIELD_SEPARATOR);
        }
        else
        {
            info.append(-1);
            info.append(FIELD_SEPARATOR);
            info.append(-1);
            info.append(FIELD_SEPARATOR);
            info.append(-1);
            info.append(FIELD_SEPARATOR);
            info.append(-1);
            info.append(FIELD_SEPARATOR);
        }
        info.append(source);

        log.info(info.toString());
    }

    public Date getEntryDate()
    {
        return entryDate;
    }

    public long getInitTime()
    {
        return initTime;
    }

    public long getTotalExecutionTime()
    {
        return execSqlEndTime - initTime;
    }

    public long getConnectionEstablishTime()
    {
        return getConnEndTime - getConnStartTime;
    }

    public long getBindParamsBindTime()
    {
        return bindParamsEndTime - bindParamsStartTime;
    }

    public long getSqlExecTime()
    {
        return execSqlEndTime - execSqlStartTime;
    }
}