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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.oro.text.perl.Perl5Util;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.column.BasicColumn;
import com.netspective.axiom.schema.table.BasicTable;
import com.netspective.axiom.schema.table.type.EnumerationTable;
import com.netspective.commons.diagram.GraphvizDiagramEdge;
import com.netspective.commons.text.TextUtils;

public class DefaultSchemaDiagramFilter implements GraphvizSchemaDiagramGeneratorFilter
{
    protected Perl5Util perlUtil = new Perl5Util();

    private String name;
    private Set ignoreTableNamesAndPatterns = new HashSet();
    private Set ignoreColumnNamesAndPatterns = new HashSet();
    private boolean showParentForeignKeyTypeEdges = true;
    private boolean showLookupForeignKeyTypeEdges = false;
    private boolean showSelfRefForeignKeyTypeEdges = true;
    private GraphvizSchemaDiagramTableNodeGenerator tableDataNodeGenerator = new SchemaDiagramTableDataNodeGenerator("enum");
    private GraphvizSchemaDiagramTableNodeGenerator tableStructureNodeGenerator = new SchemaDiagramTableStructureNodeGenerator("app");

    public DefaultSchemaDiagramFilter()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isShowLookupForeignKeyTypeEdges()
    {
        return showLookupForeignKeyTypeEdges;
    }

    public void setShowLookupForeignKeyTypeEdges(boolean showLookupForeignKeyTypeEdges)
    {
        this.showLookupForeignKeyTypeEdges = showLookupForeignKeyTypeEdges;
    }

    public boolean isShowParentForeignKeyTypeEdges()
    {
        return showParentForeignKeyTypeEdges;
    }

    public void setShowParentForeignKeyTypeEdges(boolean showParentForeignKeyTypeEdges)
    {
        this.showParentForeignKeyTypeEdges = showParentForeignKeyTypeEdges;
    }

    public boolean isShowSelfRefForeignKeyTypeEdges()
    {
        return showSelfRefForeignKeyTypeEdges;
    }

    public void setShowSelfRefForeignKeyTypeEdges(boolean showSelfRefForeignKeyTypeEdges)
    {
        this.showSelfRefForeignKeyTypeEdges = showSelfRefForeignKeyTypeEdges;
    }

    protected boolean isIgnoreColumnName(Column column)
    {
        for(Iterator i = ignoreColumnNamesAndPatterns.iterator(); i.hasNext();)
        {
            String pattern = (String) i.next();
            if(pattern.startsWith("/"))
            {
                if(perlUtil.match(pattern, column.getName()))
                    return true;
            }
            else if(BasicColumn.translateColumnNameForMapKey(pattern).equals(column.getNameForMapKey()))
                return true;
        }

        return false;
    }

    protected boolean isIgnoreTableName(Table table)
    {
        for(Iterator i = ignoreTableNamesAndPatterns.iterator(); i.hasNext();)
        {
            String pattern = (String) i.next();
            if(pattern.startsWith("/"))
            {
                if(perlUtil.match(pattern, table.getName()))
                    return true;
            }
            else if(BasicTable.translateTableNameForMapKey(pattern).equals(table.getNameForMapKey()))
                return true;
        }

        return false;
    }

    public boolean includeColumnInDiagram(GraphvizSchemaDiagramGenerator generator, Column column)
    {
        return isIgnoreColumnName(column) ? false : true;
    }

    public GraphvizSchemaDiagramTableNodeGenerator getTableNodeGenerator(GraphvizSchemaDiagramGenerator generator, Table table)
    {
        return (table instanceof EnumerationTable) ? tableDataNodeGenerator : tableStructureNodeGenerator;
    }

    public boolean includeTableInDiagram(GraphvizSchemaDiagramGenerator generator, Table table)
    {
        return isIgnoreTableName(table) ? false : true;
    }

    public boolean includeForeignKeyEdgeInDiagram(GraphvizSchemaDiagramGenerator generator, ForeignKey foreignKey)
    {
        switch(foreignKey.getType())
        {
            case ForeignKey.FKEYTYPE_PARENT:
                return showParentForeignKeyTypeEdges;

            case ForeignKey.FKEYTYPE_LOOKUP:
                return showLookupForeignKeyTypeEdges;

            case ForeignKey.FKEYTYPE_SELF:
                return showSelfRefForeignKeyTypeEdges;
        }
        return true;
    }

    public Set getIgnoreColumnNamesAndPatterns()
    {
        return ignoreColumnNamesAndPatterns;
    }

    public Set getIgnoreTableNamesAndPatterns()
    {
        return ignoreTableNamesAndPatterns;
    }

    public void setIgnoreColumnNamesAndPatterns(String commaSeparatedListOfNamesAndPatterns)
    {
        String[] items = TextUtils.getInstance().split(commaSeparatedListOfNamesAndPatterns, ",", true);
        for(int i = 0; i < items.length; i++)
            ignoreColumnNamesAndPatterns.add(items[i]);
    }

    public void setIgnoreTableNamesAndPatterns(String commaSeparatedListOfNamesAndPatterns)
    {
        String[] items = TextUtils.getInstance().split(commaSeparatedListOfNamesAndPatterns, ",", true);
        for(int i = 0; i < items.length; i++)
            ignoreTableNamesAndPatterns.add(items[i]);
    }

    public void formatForeignKeyEdge(GraphvizSchemaDiagramGenerator generator, ForeignKey foreignKey, GraphvizDiagramEdge edge)
    {
        switch(foreignKey.getType())
        {
            case ForeignKey.FKEYTYPE_PARENT:
                edge.setArrowHead("crow");
                break;
        }
    }

}
