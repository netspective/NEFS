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
 * $Id: SchemaRecordEditorDialogContext.java,v 1.5 2004-04-12 18:06:13 shahid.shah Exp $
 */

package com.netspective.sparx.form.schema;

import com.netspective.sparx.form.DialogContext;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.PrimaryKeyColumnValues;
import com.netspective.axiom.ConnectionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * Dialog context container class for the SchemaRecordEditorDialog
 */
public class SchemaRecordEditorDialogContext extends DialogContext
{
    private static final Log log = LogFactory.getLog(SchemaRecordEditorDialogContext.class);

    private ConnectionContext activeConnectionContext;
    private List rowsAdded = new ArrayList();
    private List rowsUpdated = new ArrayList();
    private List rowsDeleted = new ArrayList();

    public ConnectionContext getActiveConnectionContext()
    {
        return activeConnectionContext;
    }

    public void setActiveConnectionContext(ConnectionContext activeConnectionContext)
    {
        this.activeConnectionContext = activeConnectionContext;
    }

    /**
     * Gets a List object of recently added rows
     * @return
     */
    public List getRowsAdded()
    {
        return rowsAdded;
    }

    /**
     * Gets a List object of recently updated rows
     * @return
     */
    public List getRowsUpdated()
    {
        return rowsUpdated;
    }

    /**
     * Gets a List object of recently deleted rows
     * @return
     */
    public List getRowsDeleted()
    {
        return rowsDeleted;
    }

    /**
     * Gets the primary key values of the row that was added
     * @param rowNum Row number
     * @return
     */
    public ColumnValues getAddedRowPrimaryKeyValues(int rowNum)
    {
        Row row = (Row)rowsAdded.get(rowNum);
        return row.getPrimaryKeyValues();
    }

    /**
     * Gets the primary key value of the row that was recently added. If there are more than
     * one primary key, the first primary key column is used.
     * @param rowNum
     * @return A NULL object is returned if there are not primary keys
     */
    public ColumnValue getAddedRowPrimaryKeyValue(int rowNum)
    {
        Row row = (Row)rowsAdded.get(rowNum);
        PrimaryKeyColumnValues primaryKeyValues = row.getPrimaryKeyValues();
        ColumnValue val =  primaryKeyValues != null ? primaryKeyValues.getByColumnIndex(0) : null;
        return val;
    }
}
