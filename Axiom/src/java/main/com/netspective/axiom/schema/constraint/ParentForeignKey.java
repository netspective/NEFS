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
 * $Id: ParentForeignKey.java,v 1.2 2003-03-18 22:32:43 shahid.shah Exp $
 */

package com.netspective.axiom.schema.constraint;

import java.sql.SQLException;
import javax.naming.NamingException;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.TableColumnsReference;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.constraint.BasicForeignKey;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.QueryResultSet;

public class ParentForeignKey extends BasicForeignKey
{
    public ParentForeignKey(Column source, TableColumnsReference reference)
    {
        super(source, reference);
    }

    public short getType()
    {
        return ForeignKey.FKEYTYPE_PARENT;
    }

    public void fillChildValuesFromParentConnector(ColumnValues childValues, ColumnValues parentValues)
    {
        Columns srcColumns = getSourceColumns();
        Columns refColumns = getReferencedColumns();

        for(int i = 0; i < srcColumns.size(); i++)
        {
            ColumnValue parentValue = parentValues.getByColumn(refColumns.get(i));
            ColumnValue childValue = childValues.getByColumn(srcColumns.get(i));

            childValue.setValue(parentValue);
        }
    }

    public Rows getChildRowsByParentValues(ConnectionContext cc, ColumnValues parentValues) throws NamingException, SQLException
    {
        Columns srcColumns = getSourceColumns();
        Columns refColumns = getReferencedColumns();
        Table srcTable = srcColumns.getFirst().getTable();

        QueryDefnSelect accessor = srcTable.getAccessorByColumnsEquality(srcColumns);
        if(accessor == null)
            throw new SQLException("Unable to accessor for selecting source columns " + srcColumns);

        Object[] bindValues = parentValues.getByColumns(refColumns).getValuesForSqlBindParams();
        if(bindValues == null)
            throw new SQLException("No bind value provided for source columns " + srcColumns);

        Rows resultRows = srcTable.createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null)
            resultRows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return resultRows;
    }

    public Rows getChildRowsByParentRow(ConnectionContext cc, Row row) throws NamingException, SQLException
    {
        if(row.getTable() != getReferencedColumns().getFirst().getTable())
            throw new SQLException("Row value is from table '"+ row.getTable().getName() +"' while referenced column is from table '" + getReferencedColumns().getFirst().getTable().getName() + "'.");
        return getChildRowsByParentValues(cc, row.getColumnValues());
    }

}
