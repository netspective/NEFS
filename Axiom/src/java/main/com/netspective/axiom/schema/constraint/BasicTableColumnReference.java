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
 * $Id: BasicTableColumnReference.java,v 1.4 2004-06-21 04:28:59 shahid.shah Exp $
 */

package com.netspective.axiom.schema.constraint;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.TableColumnsReference;

public class BasicTableColumnReference implements TableColumnsReference
{
    private Schema schema;
    private String reference;
    private String tableName;
    private String columnName;

    public static final String createReference(Column column)
    {
        return column.getTable().getName() + QUALIFIED_TABLENAME_COLUMNNAME_SEPARATOR + column.getName();
    }

    public static final String createSqlReference(Column column)
    {
        return column.getTable().getSqlName() + QUALIFIED_TABLENAME_COLUMNNAME_SEPARATOR + column.getSqlName();
    }

    public BasicTableColumnReference(Schema schema, String reference)
    {
        this.schema = schema;
        this.reference = reference;

        int delimPos = reference.indexOf(QUALIFIED_TABLENAME_COLUMNNAME_SEPARATOR);
        if (delimPos == -1)
        {
            tableName = reference;
            columnName = null;
        }
        else
        {
            tableName = reference.substring(0, delimPos);
            columnName = reference.substring(delimPos + 1);
        }
    }

    public String getColumnNames()
    {
        return columnName;
    }

    public String getReference()
    {
        if (hasColumnNames())
            return reference;

        Table table = schema.getTables().getByName(getTableName());
        if (table == null)
            return null;

        return table.getName() + "." + table.getPrimaryKeyColumns().getOnlyNames(",");
    }

    public String getTableName()
    {
        return tableName;
    }

    public boolean hasColumnNames()
    {
        return columnName != null;
    }

    public Columns getColumns()
    {
        Table table = schema.getTables().getByName(getTableName());
        if (table == null)
            return null;

        return hasColumnNames() ?
                table.getColumns().getByNames(getColumnNames(), ",") :
                table.getPrimaryKeyColumns();
    }

    public String toString()
    {
        return reference;
    }
}
