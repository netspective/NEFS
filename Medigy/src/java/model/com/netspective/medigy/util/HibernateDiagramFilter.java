package com.netspective.medigy.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;

import com.netspective.medigy.reference.ReferenceEntity;
import com.netspective.tool.graphviz.GraphvizDiagramEdge;
import com.netspective.tool.graphviz.GraphvizDiagramNode;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramGenerator;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramGeneratorFilter;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramReferenceTableNodeGenerator;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramTableNodeGenerator;
import com.netspective.tool.hibernate.document.diagram.HibernateDiagramTableStructureNodeGenerator;

public class HibernateDiagramFilter implements HibernateDiagramGeneratorFilter
{
    private Set commonColumns = new HashSet();
    private boolean hideCommonColumns;
    private boolean showReferenceData;
    private final HibernateDiagramTableNodeGenerator tableDataNodeGenerator = new HibernateDiagramReferenceTableNodeGenerator("enum");
    private final HibernateDiagramTableNodeGenerator tableStructureNodeGenerator = new HibernateDiagramTableStructureNodeGenerator("app");

    public HibernateDiagramFilter(boolean hideCommonColumns, boolean showReferenceData)
    {
        this.hideCommonColumns = hideCommonColumns;
        this.showReferenceData = showReferenceData;
        commonColumns.add("version");
        commonColumns.add("create_timestamp");
        commonColumns.add("update_timestamp");
        commonColumns.add("record_status");
        commonColumns.add("create_session_id");
        commonColumns.add("update_session_id");
    }

    public Set getCommonColumns()
    {
        return commonColumns;
    }

    public void formatForeignKeyEdge(final HibernateDiagramGenerator generator, final ForeignKey foreignKey, final GraphvizDiagramEdge edge)
    {
        for (Iterator colls = generator.getConfiguration().getCollectionMappings(); colls.hasNext();)
        {
            final Collection coll = (Collection) colls.next();
            if (coll.isOneToMany())
            {
                // for parents, we put the crow arrow pointing to us (the source becomes the parent, not the child -- this way it will look like a tree)
                if (foreignKey.getReferencedTable() == coll.getOwner().getTable() && foreignKey.getTable() == coll.getCollectionTable())
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
        if (showReferenceData)
            return ReferenceEntity.class.isAssignableFrom(pclass.getMappedClass())
                    ? tableDataNodeGenerator : tableStructureNodeGenerator;
        else
            return tableStructureNodeGenerator;
    }

    public boolean includeClassInDiagram(final HibernateDiagramGenerator generator, final PersistentClass pclass)
    {
        return true;
    }

    public boolean includeColumnInDiagram(final HibernateDiagramGenerator generator, final Column column)
    {
        if (!hideCommonColumns)
            return true;

        return !commonColumns.contains(column.getName());
    }

    public boolean includeForeignKeyEdgeInDiagram(final HibernateDiagramGenerator generator, final ForeignKey foreignKey)
    {
        return true;
    }
}
