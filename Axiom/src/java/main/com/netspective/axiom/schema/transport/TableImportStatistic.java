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
package com.netspective.axiom.schema.transport;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Locator;

import com.netspective.axiom.schema.PrimaryKeyColumnValues;

public class TableImportStatistic
{
    private String tableName;
    private long successfulRows;
    private long unsuccessfulRows;
    private List importErrors;
    private List idReferences;
    private long sqlTimeSpent;
    private long processingTimeSpent;

    public TableImportStatistic(String tableName)
    {
        this.tableName = tableName;
        this.importErrors = new ArrayList();
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public long getSuccessfulRows()
    {
        return successfulRows;
    }

    public void incSuccessfulRows()
    {
        this.successfulRows++;
    }

    public void incSuccessfulRows(long count)
    {
        this.successfulRows += count;
    }

    public void setSuccessfulRows(long successfulRows)
    {
        this.successfulRows = successfulRows;
    }

    public long getUnsuccessfulRows()
    {
        return unsuccessfulRows;
    }

    public void incUnsuccessfulRows()
    {
        this.unsuccessfulRows++;
    }

    public void incUnsuccessfulRows(long count)
    {
        this.unsuccessfulRows += count;
    }

    public void setUnsuccessfulRows(long unsuccessfulRows)
    {
        this.unsuccessfulRows = unsuccessfulRows;
    }

    public List getImportErrors()
    {
        return importErrors;
    }

    public List getIdReferences()
    {
        return idReferences;
    }

    public void addImportError(String message, Locator locator)
    {
        importErrors.add(message + " at " + locator.getSystemId() + " line " + locator.getColumnNumber() + " column " + locator.getColumnNumber());
    }

    public void setImportErrors(List importErrors)
    {
        this.importErrors = importErrors;
    }

    public void addIdReference(String id, PrimaryKeyColumnValues values)
    {
        if(idReferences == null)
            idReferences = new ArrayList();

        idReferences.add("IDREF '" + id + "' is '" + values + "'");
    }

    public void addProcessingTimeSpent(long startTime, long endTime)
    {
        processingTimeSpent += (endTime - startTime);
    }

    public void addSqlTimeSpent(long startTime, long endTime)
    {
        sqlTimeSpent += (endTime - startTime);
    }

    public long getSqlTimeSpent()
    {
        return sqlTimeSpent;
    }
}
