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
package com.netspective.axiom.schema.table;

import java.lang.reflect.InvocationTargetException;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.PrimaryKeyColumnValues;
import com.netspective.axiom.schema.PrimaryKeyColumns;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Table;
import com.netspective.commons.validate.ValidationContext;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.exception.DataModelException;

public class BasicRow implements Row, XmlDataModelSchema.CustomElementAttributeSetter
{
    protected Table rowTable;
    protected ColumnValues values;

    public BasicRow(Table table)
    {
        setTable(table);
    }

    public Table getTable()
    {
        return rowTable;
    }

    public void setTable(Table table)
    {
        rowTable = table;
        values = table.getColumns().constructValuesInstance();
    }

    public ColumnValues getColumnValues()
    {
        return values;
    }

    public PrimaryKeyColumnValues getPrimaryKeyValues()
    {
        PrimaryKeyColumns primaryKeyColumns = rowTable.getPrimaryKeyColumns();
        PrimaryKeyColumnValues pkValues = primaryKeyColumns.constructPrimaryKeyValuesInstance();
        pkValues.copyValuesUsingColumnInstances(values);
        return pkValues;
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    public ValidationContext getValidationResult(ValidationContext vc)
    {
        return values.getValidationResult(vc);
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    public void beforeInsert(ConnectionContext cc, int flags, ColumnValues columnValues)
    {
    }

    public void beforeUpdate(ConnectionContext cc, int flags, ColumnValues columnValues)
    {
    }

    public void beforeDelete(ConnectionContext cc, int flags, ColumnValues columnValues)
    {
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    public void afterInsert(ConnectionContext cc, int flags, ColumnValues columnValues)
    {
    }

    public void afterUpdate(ConnectionContext cc, int flags, ColumnValues columnValues)
    {
    }

    public void afterDelete(ConnectionContext cc, int flags, ColumnValues columnValues)
    {
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    public void setCustomDataModelElementAttribute(XdmParseContext pc, XmlDataModelSchema schema, Object parent, String attrName, String attrValue)
            throws DataModelException, InvocationTargetException, IllegalAccessException, DataModelException
    {
        Column column = rowTable.getColumns().getByNameOrXmlNodeName(attrName);
        if(column != null)
            values.getByColumn(column).setTextValue(attrValue);
        else
            schema.setAttribute(pc, parent, attrName, attrValue, true);
    }

    public String toString()
    {
        Columns columns = rowTable.getColumns();

        StringBuffer str = new StringBuffer();
        str.append("Class = ");
        str.append(this.getClass().getName());
        str.append(", Table = ");
        str.append(rowTable.getName());
        str.append(", Primary Key = ");
        str.append(getPrimaryKeyValues());
        str.append("\n");
        for(int columnIndex = 0; columnIndex < columns.size(); columnIndex++)
        {
            Column column = columns.get(columnIndex);
            ColumnValue value = values.getByColumn(column);
            Object valueForBindParam = value.getValueForSqlBindParam();
            str.append(columnIndex + ") " + value.toString() + " (" + value.getClass().getName() + ")");
            if(valueForBindParam != null)
            {
                if(!value.getClass().getName().equals(valueForBindParam.getClass().getName()))
                    str.append("[BIND AS " + valueForBindParam.getClass().getName() + "]");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public boolean equals(Object o)
    {

        BasicRow rowObject;

        if(o == null)
            return false;

        try
        {
            rowObject = (BasicRow) o;
        }
        catch(ClassCastException cce)
        {
            return false;
        }

        return this.getColumnValues().equals(rowObject.getColumnValues());
    }
}
