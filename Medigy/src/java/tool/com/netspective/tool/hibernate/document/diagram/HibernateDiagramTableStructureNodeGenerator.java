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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.naming.NamingException;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.OneToMany;

import com.netspective.tool.graphviz.GraphvizDiagramNode;
import com.netspective.medigy.reference.ReferenceEntity;

public class HibernateDiagramTableStructureNodeGenerator implements HibernateDiagramTableNodeGenerator
{
    private static final String COLUMN_PORT_NAME_CONSTRAINT_SUFFIX = "_CONSTR";

    private String name;
    private boolean showDataTypes = true;
    private boolean showConstraints = true;

    private String entityTableAttrs = "BORDER=\"1\" CELLSPACING=\"0\" CELLBORDER=\"0\"";
    private String tableNameBgColor = "lightgoldenrodyellow";

    public HibernateDiagramTableStructureNodeGenerator(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getColumnDefinitionRow(final HibernateDiagramGenerator generator,
                                         final Column column,
                                         final boolean isPK,
                                         final boolean isFK,
                                         final String indent) throws SQLException, NamingException
    {
        final StringBuffer result = new StringBuffer(indent + "<TR>\n");
        result.append(indent + indent + "<TD ALIGN=\"LEFT\" PORT=\"" + column.getName() + "\">" + column.getName() + "</TD>\n");

        if(showDataTypes)
            result.append(indent + indent + "<TD ALIGN=\"LEFT\">"+ column.getSqlType(generator.getDialect(), generator.getMapping()) +"</TD>\n");

        if(showConstraints)
        {

            List constraints = new ArrayList();
            if(isPK)
                constraints.add("PK");
            if(column.isUnique())
                constraints.add("U");
            if(column.isFormula())
                constraints.add("F");
            if(! column.isNullable())
                constraints.add("R");
            if(isFK)
                constraints.add("FK");

/*
            TODO: see if we can figure out whether a foreign key is a parent/child key or a "lookup" key (Axiom notions)
            final ForeignKey foreignKey = column.getForeignKey();
            if(foreignKey != null)
            {
                final Table refTable = foreignKey.getReference().getColumns().getFirst().getTable();
                switch(foreignKey.getType())
                {
                    case ForeignKey.FKEYTYPE_PARENT:
                        constraints.add("FK: Parent");
                        break;

                    case ForeignKey.FKEYTYPE_LOOKUP:
                        constraints.add("FK: " + refTable.getName());
                        break;

                    case ForeignKey.FKEYTYPE_SELF:
                        constraints.add("FK: Self");
                        break;
                }
            }
*/

            if(constraints.size() > 0)
                result.append(indent + indent + "<TD ALIGN=\"LEFT\" PORT=\"" + column.getName() + COLUMN_PORT_NAME_CONSTRAINT_SUFFIX + "\">" + constraints + "</TD>\n");
            else
                result.append(indent + indent + "<TD ALIGN=\"LEFT\"> </TD>\n");
        }

        result.append(indent + "</TR>\n");
        return result.toString();
    }

    public GraphvizDiagramNode generateTableNode(final HibernateDiagramGenerator generator,
                                                 final HibernateDiagramGeneratorFilter filter,
                                                 final PersistentClass pclass)
    {
        final Table table = pclass.getTable();
        final StringBuffer primaryKeyRows = new StringBuffer();
        final StringBuffer parentKeyRows = new StringBuffer();
        final StringBuffer columnRows = new StringBuffer();

        final PrimaryKey primaryKeyColumns = table.getPrimaryKey();
        final String indent = "        ";
        int hidden = 0;

        for(final Iterator columns = table.getColumnIterator(); columns.hasNext(); )
        {
            final Column column = (Column) columns.next();

            if(filter.includeColumnInDiagram(generator, column))
            {
                try
                {
                    if(primaryKeyColumns.containsColumn(column))
                        primaryKeyRows.append(getColumnDefinitionRow(generator, column, true, false, indent) + "\n");
                    //else if(parentRefColumns.contains(column))
                    //    parentKeyRows.append(getColumnDefinitionRow(generator, column, false, indent) + "\n");
                    else
                    {
                        boolean isFK = false;
                        for(Iterator fkIterator = table.getForeignKeyIterator(); fkIterator.hasNext(); )
                        {
                            final ForeignKey fKey = (ForeignKey) fkIterator.next();
                            if(fKey.containsColumn(column))
                            {
                                isFK = true;
                                break;
                            }
                        }
                        columnRows.append(getColumnDefinitionRow(generator, column, false, isFK, indent) + "\n");
                    }
                }
                catch(SQLException e)
                {
                    throw new HibernateDiagramGeneratorException(e);
                }
                catch(NamingException e)
                {
                    throw new HibernateDiagramGeneratorException(e);
                }
            }
            else
                hidden++;
        }

        int colSpan = 1;
        if(showDataTypes) colSpan++;
        if(showConstraints) colSpan++;

        StringBuffer tableNodeLabel = new StringBuffer("<<TABLE " + entityTableAttrs + ">\n");
        tableNodeLabel.append("        <TR><TD COLSPAN=\"" + colSpan + "\" BGCOLOR=\"" + tableNameBgColor + "\">" + table.getName() + "</TD></TR>\n");
        if(primaryKeyRows.length() > 0)
            tableNodeLabel.append(primaryKeyRows);
        if(parentKeyRows.length() > 0)
            tableNodeLabel.append(parentKeyRows);
        tableNodeLabel.append(columnRows);
        if(hidden > 0)
            tableNodeLabel.append("        <TR><TD COLSPAN=\"" + colSpan + "\">(" + hidden + " columns not shown)</TD></TR>\n");
        tableNodeLabel.append("    </TABLE>>");

        GraphvizDiagramNode result = new GraphvizDiagramNode(generator.getGraphvizDiagramGenerator(), table.getName());
        result.setLabel(tableNodeLabel.toString());
        result.setShape("plaintext");
        result.setFontName("Courier");

        return result;
    }

    public String getEdgeSourceElementAndPort(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        for(Iterator colls = generator.getConfiguration().getCollectionMappings(); colls.hasNext(); )
        {
            final Collection coll = (Collection) colls.next();
            if(coll.isOneToMany())
            {
                // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
                //System.out.println(coll.getOwner().getTable().getName() + " -> " + coll.getCollectionTable().getName());
                if(foreignKey.getReferencedTable() == coll.getOwner().getTable() && foreignKey.getTable() == coll.getCollectionTable())
                    return foreignKey.getReferencedTable().getName();
            }
        }

        return foreignKey.getTable().getName() + ":" + (showConstraints
                                                               ? (foreignKey.getColumn(0).getName() + COLUMN_PORT_NAME_CONSTRAINT_SUFFIX)
                                                               : foreignKey.getColumn(0).getName());

    }

    public String getEdgeDestElementAndPort(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        for(Iterator colls = generator.getConfiguration().getCollectionMappings(); colls.hasNext(); )
        {
            final Collection coll = (Collection) colls.next();
            if(coll.isOneToMany())
            {
                // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
                //System.out.println(coll.getOwner().getTable().getName() + " -> " + coll.getCollectionTable().getName());
                if(foreignKey.getReferencedTable() == coll.getOwner().getTable() && foreignKey.getTable() == coll.getCollectionTable())
                    return foreignKey.getTable().getName();
            }
        }

        return foreignKey.getReferencedTable().getName() + ":" + foreignKey.getReferencedTable().getPrimaryKey().getColumn(0).getName();
    }

    public boolean isShowConstraints()
    {
        return showConstraints;
    }

    public void setShowConstraints(final boolean showConstraints)
    {
        this.showConstraints = showConstraints;
    }

    public boolean isShowDataTypes()
    {
        return showDataTypes;
    }

    public void setShowDataTypes(final boolean showDataTypes)
    {
        this.showDataTypes = showDataTypes;
    }
}
