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
package com.netspective.axiom.schema.diagram;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.table.BasicTable;

public class FocusedTableSchemaDiagramFilter extends DefaultSchemaDiagramFilter
{
    private String tableName;

    public FocusedTableSchemaDiagramFilter(String tableName)
    {
        this.tableName = BasicTable.translateTableNameForMapKey(tableName);
        setShowLookupForeignKeyTypeEdges(true);
    }

    public boolean isDescendant(GraphvizSchemaDiagramGenerator generator, Table checkTable, Table compareAgainst)
    {
        final Tables children = checkTable.getChildTables();
        for(int i = 0; i < children.size(); i++)
        {
            Table table = children.get(i);
            if(table == compareAgainst)
                return true;

            if(isDescendant(generator, table, compareAgainst))
                return true;
        }

        return false;
    }

    public boolean isReference(GraphvizSchemaDiagramGenerator generator, Table checkTable, Table compareAgainst)
    {
        final Columns foreignKeyColumns = checkTable.getForeignKeyColumns();
        for(int i = 0; i < foreignKeyColumns.size(); i++)
        {
            Column column = foreignKeyColumns.get(i);
            if(includeColumnInDiagram(generator, column))
            {
                ForeignKey foreignKey = column.getForeignKey();
                if(foreignKey.getReferencedColumns().getFirst().getTable() == compareAgainst)
                    return true;
            }
        }

        Tables children = checkTable.getChildTables();
        for(int i = 0; i < children.size(); i++)
        {
            Table table = children.get(i);
            if(isReference(generator, table, compareAgainst))
                return true;
        }

        return false;
    }

    public boolean includeTableInDiagram(GraphvizSchemaDiagramGenerator generator, Table table)
    {
        if(!super.includeTableInDiagram(generator, table))
            return false;

        final Table focusedTable = generator.getSchema().getTables().getByName(tableName);
        if(table == focusedTable)
            return true;

        if(isDescendant(generator, focusedTable, table))
            return true;

        if(isReference(generator, focusedTable, table))
            return true;

        return false;
    }

    public GraphvizSchemaDiagramTableNodeGenerator getTableNodeGenerator(GraphvizSchemaDiagramGenerator generator, Table table)
    {
        return super.getTableNodeGenerator(generator, table);
    }
}
