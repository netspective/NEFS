package com.netspective.medigy.util;

import java.util.Iterator;

import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.OneToMany;

import com.netspective.tool.hibernate.document.diagram.HibernateDiagramGeneratorFilter;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramGenerator;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramTableNodeGenerator;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramReferenceTableNodeGenerator;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramTableStructureNodeGenerator;
import com.netspective.tool.graphviz.GraphvizDiagramEdge;
import com.netspective.tool.graphviz.GraphvizDiagramNode;
import com.netspective.medigy.reference.ReferenceEntity;
import com.netspective.medigy.model.session.Session;

public class HibernateDiagramFilter implements HibernateDiagramGeneratorFilter
{
    private final HibernateDiagramTableNodeGenerator tableDataNodeGenerator = new HibernateDiagramReferenceTableNodeGenerator("enum");
    private final HibernateDiagramTableNodeGenerator tableStructureNodeGenerator = new HibernateDiagramTableStructureNodeGenerator("app");

    public HibernateDiagramFilter()
    {
    }

    public void formatForeignKeyEdge(final HibernateDiagramGenerator generator, final ForeignKey foreignKey, final GraphvizDiagramEdge edge)
    {
        for(Iterator colls = generator.getConfiguration().getCollectionMappings(); colls.hasNext(); )
        {
            final Collection coll = (Collection) colls.next();
            if(coll.isOneToMany())
            {
                // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
                if(foreignKey.getReferencedTable() == coll.getOwner().getTable() && foreignKey.getTable() == coll.getCollectionTable())
                    edge.setArrowHead("crow");
            }
        }
    }

    public void formatTableNode(final HibernateDiagramGenerator generator, final PersistentClass pclass, final GraphvizDiagramNode node)
    {
    }

    public String getName()
    {
        return "MEDIGY";
    }

    public HibernateDiagramTableNodeGenerator getTableNodeGenerator(final HibernateDiagramGenerator generator,
                                                                    final PersistentClass pclass)
    {
        return ReferenceEntity.class.isAssignableFrom(pclass.getMappedClass()) ? tableDataNodeGenerator : tableStructureNodeGenerator;
    }

    public boolean includeClassInDiagram(final HibernateDiagramGenerator generator, final PersistentClass pclass)
    {
        return true;
    }

    public boolean includeColumnInDiagram(final HibernateDiagramGenerator generator, final Column column)
    {
        return true;
    }

    public boolean includeForeignKeyEdgeInDiagram(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        // too many pointers to this table will make the diagram unreadable.
        if(Session.class.isAssignableFrom(generator.getClassForTable(foreignKey.getReferencedTable()).getMappedClass()))
            return false;

        return true;
    }
}
