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

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.naming.NamingException;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.value.DatabasePolicyValueContext;
import com.netspective.commons.diagram.GraphvizDiagramEdge;
import com.netspective.commons.diagram.GraphvizDiagramGenerator;
import com.netspective.commons.diagram.GraphvizDiagramNode;

public class GraphvizSchemaDiagramGenerator
{
    protected static final String COLUMN_PORT_NAME_CONSTRAINT_SUFFIX = "_CONSTR";

    /**
     * The schema we'll be generating the diagram for
     */
    private final Schema schema;

    /**
     * The database policy for which we're generating the schema generator; null to generate db-independent ERD
     */
    private final DatabasePolicyValueContext valueContext;

    /**
     * Provides the rules to determine which particular tables should be in the diagram
     */
    private final GraphvizSchemaDiagramGeneratorFilter schemaDiagramFilter;

    /**
     * The generator that we will use to add the nodes, edges, etc
     */
    private GraphvizDiagramGenerator graphvizDiagramGenerator;

    public GraphvizSchemaDiagramGenerator(DatabasePolicyValueContext valueContext, Schema schema,
                                          GraphvizDiagramGenerator graphvizDiagramGenerator, GraphvizSchemaDiagramGeneratorFilter schemaDiagramFilter)
    {
        this.valueContext = valueContext;
        this.schema = schema;
        this.graphvizDiagramGenerator = graphvizDiagramGenerator;
        this.schemaDiagramFilter = schemaDiagramFilter;
    }

    public void generate() throws SQLException, NamingException
    {
        final GraphvizDiagramGenerator gdg = getGraphvizDiagramGenerator();
        final GraphvizSchemaDiagramGeneratorFilter filter = getSchemaDiagramFilter();

        final Set referenceColumnsInIncludedTables = new HashSet();

        final Tables tables = schema.getTables();
        for(int i = 0; i < tables.size(); i++)
        {
            final Table table = tables.get(i);
            if(filter.includeTableInDiagram(this, table))
            {
                final Columns foreignKeyColumns = table.getForeignKeyColumns();
                for(int j = 0; j < foreignKeyColumns.size(); j++)
                    referenceColumnsInIncludedTables.add(foreignKeyColumns.get(j));

                final GraphvizSchemaDiagramTableNodeGenerator nodeGenerator = filter.getTableNodeGenerator(this, table);
                final GraphvizDiagramNode node = nodeGenerator.generateTableNode(this, filter, table);
                gdg.addNode(node);
            }
        }

        for(Iterator i = referenceColumnsInIncludedTables.iterator(); i.hasNext();)
        {
            final Column foreignKeyColumn = (Column) i.next();
            final ForeignKey foreignKey = foreignKeyColumn.getForeignKey();
            final Column firstSourceColumn = foreignKey.getSourceColumns().getFirst();
            final Column firstReferenceColumn = foreignKey.getReference().getColumns().getFirst();
            final GraphvizSchemaDiagramTableNodeGenerator sourceTableNodeGenerator = filter.getTableNodeGenerator(this, firstSourceColumn.getTable());
            final GraphvizSchemaDiagramTableNodeGenerator refTableNodeGenerator = filter.getTableNodeGenerator(this, firstReferenceColumn.getTable());

            // make sure both sides have a table.column visible otherwise we won't draw an edge
            if(filter.includeTableInDiagram(this, firstSourceColumn.getTable()) &&
               filter.includeTableInDiagram(this, firstReferenceColumn.getTable()) &&
               filter.includeColumnInDiagram(this, firstSourceColumn) &&
               filter.includeColumnInDiagram(this, firstReferenceColumn) &&
               filter.includeForeignKeyEdgeInDiagram(this, foreignKey))
            {
                GraphvizDiagramEdge edge = new GraphvizDiagramEdge(gdg, sourceTableNodeGenerator.getEdgeSourceElementAndPort(this, foreignKey),
                                                                   refTableNodeGenerator.getEdgeDestElementAndPort(this, foreignKey));
                filter.formatForeignKeyEdge(this, foreignKey, edge);
                gdg.addEdge(edge);
            }
        }
    }

    /*-- Accessors and Mutators for access to private fields --------------------------------------------------------*/

    public GraphvizDiagramGenerator getGraphvizDiagramGenerator()
    {
        return graphvizDiagramGenerator;
    }

    public Schema getSchema()
    {
        return schema;
    }

    public GraphvizSchemaDiagramGeneratorFilter getSchemaDiagramFilter()
    {
        return schemaDiagramFilter;
    }

    public DatabasePolicyValueContext getValueContext()
    {
        return valueContext;
    }
}
