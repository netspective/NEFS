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
package com.netspective.tool.hibernate.document.diagram;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;

import com.netspective.tool.graphviz.GraphvizDiagramEdge;
import com.netspective.tool.graphviz.GraphvizDiagramNode;

public class DefaultHibernateDiagramFilter implements HibernateDiagramGeneratorFilter
{
    private String name;
    private Set ignoreClassNamesAndPatterns = new HashSet();
    private Set ignoreTableNamesAndPatterns = new HashSet();
    private Set ignoreColumnNamesAndPatterns = new HashSet();
    private boolean showParentForeignKeyTypeEdges = true;
    private boolean showLookupForeignKeyTypeEdges = false;
    private boolean showSelfRefForeignKeyTypeEdges = true;
    private final HibernateDiagramTableNodeGenerator tableDataNodeGenerator = new HibernateDiagramReferenceTableNodeGenerator("enum");
    private final HibernateDiagramTableNodeGenerator tableStructureNodeGenerator = new HibernateDiagramTableStructureNodeGenerator("app");

    public DefaultHibernateDiagramFilter()
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

    public void setShowLookupForeignKeyTypeEdges(final boolean showLookupForeignKeyTypeEdges)
    {
        this.showLookupForeignKeyTypeEdges = showLookupForeignKeyTypeEdges;
    }

    public boolean isShowParentForeignKeyTypeEdges()
    {
        return showParentForeignKeyTypeEdges;
    }

    public void setShowParentForeignKeyTypeEdges(final boolean showParentForeignKeyTypeEdges)
    {
        this.showParentForeignKeyTypeEdges = showParentForeignKeyTypeEdges;
    }

    public boolean isShowSelfRefForeignKeyTypeEdges()
    {
        return showSelfRefForeignKeyTypeEdges;
    }

    public void setShowSelfRefForeignKeyTypeEdges(final boolean showSelfRefForeignKeyTypeEdges)
    {
        this.showSelfRefForeignKeyTypeEdges = showSelfRefForeignKeyTypeEdges;
    }

    protected boolean isIgnoreColumnName(final Column column)
    {
        for(Iterator i = ignoreClassNamesAndPatterns.iterator(); i.hasNext();)
        {
            String pattern = (String) i.next();
            if(pattern.startsWith("/"))
            {
                if(column.getName().matches(pattern.substring(1, pattern.length()-2)))
                    return true;
            }
            else if(column.getName().equalsIgnoreCase(pattern))
                return true;
        }

        return false;
    }

    protected boolean isIgnoreClassName(final PersistentClass pclass)
    {
        for(Iterator i = ignoreTableNamesAndPatterns.iterator(); i.hasNext();)
        {
            String pattern = (String) i.next();
            if(pattern.startsWith("/"))
            {
                if(pclass.getClassName().matches(pattern.substring(1, pattern.length()-2)))
                    return true;
            }
            else if(pclass.getClassName().equalsIgnoreCase(pattern))
                return true;
        }

        return false;
    }

    protected boolean isIgnoreTableName(final Table table)
    {
        for(Iterator i = ignoreTableNamesAndPatterns.iterator(); i.hasNext();)
        {
            String pattern = (String) i.next();
            if(pattern.startsWith("/"))
            {
                if(table.getName().matches(pattern.substring(1, pattern.length()-2)))
                    return true;
            }
            else if(table.getName().equalsIgnoreCase(pattern))
                return true;
        }

        return false;
    }

    public boolean includeColumnInDiagram(HibernateDiagramGenerator generator, Column column)
    {
        return isIgnoreColumnName(column) ? false : true;
    }

    public HibernateDiagramTableNodeGenerator getTableNodeGenerator(final HibernateDiagramGenerator generator, final PersistentClass pclass)
    {
        // TODO: should treat enum/type tables differently but we don't yet
        // return (table instanceof EnumerationTable) ? tableDataNodeGenerator : tableStructureNodeGenerator;
        return tableStructureNodeGenerator;
    }

    public boolean includeClassInDiagram(final HibernateDiagramGenerator generator, final PersistentClass pclass)
    {
        return isIgnoreClassName(pclass) || isIgnoreTableName(pclass.getTable()) ? false : true;
    }

    public boolean includeForeignKeyEdgeInDiagram(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
/*
        TODO: in hibernate we don't distinguish a foreign key type yet :-(
        switch(foreignKey.getType())
        {
            case ForeignKey.FKEYTYPE_PARENT:
                return showParentForeignKeyTypeEdges;

            case ForeignKey.FKEYTYPE_LOOKUP:
                return showLookupForeignKeyTypeEdges;

            case ForeignKey.FKEYTYPE_SELF:
                return showSelfRefForeignKeyTypeEdges;
        }
*/
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

    public void setIgnoreColumnNamesAndPatterns(final String commaSeparatedListOfNamesAndPatterns)
    {
        final String[] items = commaSeparatedListOfNamesAndPatterns.split(",");
        for(int i = 0; i < items.length; i++)
            ignoreColumnNamesAndPatterns.add(items[i]);
    }

    public void setIgnoreTableNamesAndPatterns(final String commaSeparatedListOfNamesAndPatterns)
    {
        final String[] items = commaSeparatedListOfNamesAndPatterns.split(",");
        for(int i = 0; i < items.length; i++)
            ignoreTableNamesAndPatterns.add(items[i]);
    }

    public void setIgnoreClassNamesAndPatterns(final String commaSeparatedListOfNamesAndPatterns)
    {
        final String[] items = commaSeparatedListOfNamesAndPatterns.split(",");
        for(int i = 0; i < items.length; i++)
            ignoreClassNamesAndPatterns.add(items[i]);
    }

    public void formatTableNode(final HibernateDiagramGenerator generator, final PersistentClass pclass, final GraphvizDiagramNode node)
    {

    }

    public void formatForeignKeyEdge(final HibernateDiagramGenerator generator, final ForeignKey foreignKey, final GraphvizDiagramEdge edge)
    {
/*
        TODO: we don't know foreign key types (parent/child, etc)
        switch(foreignKey.getType())
        {
            case ForeignKey.FKEYTYPE_PARENT:
                edge.setArrowHead("crow");
                break;
        }
*/
    }

}
