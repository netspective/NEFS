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
 * $Id: XmlDataModelSchemaStructurePanel.java,v 1.4 2003-03-29 13:00:56 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.report.tabular.BasicTabularReport;
import com.netspective.commons.report.tabular.TabularReport;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportException;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XmlDataModel;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.exception.UnsupportedElementException;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.AbstractHtmlTabularReportPanel;

public class XmlDataModelSchemaStructurePanel extends AbstractHtmlTabularReportPanel
{
    private static final Log log = LogFactory.getLog(XmlDataModelSchemaStructurePanel.class);
    private static final TabularReport report = new BasicTabularReport();

    static
    {
        report.getFrame().setHeading(new StaticValueSource("XDM Structure"));

        GeneralColumn elementName = new GeneralColumn();
        elementName.setHeading(new StaticValueSource("Element"));
        elementName.setWordWrap(false);
        report.addColumn(elementName);

        GeneralColumn xdmClass = new GeneralColumn();
        xdmClass.setHeading(new StaticValueSource("Class"));
        xdmClass.setWordWrap(false);
        report.addColumn(xdmClass);

        GeneralColumn isText = new GeneralColumn();
        isText.setHeading(new StaticValueSource("Text"));
        isText.setWordWrap(false);
        isText.setAlign(new TabularReportColumn.AlignStyle(TabularReportColumn.ALIGN_CENTER));
        report.addColumn(isText);

        GeneralColumn isRecursive = new GeneralColumn();
        isRecursive.setHeading(new StaticValueSource("Recursive"));
        isRecursive.setWordWrap(false);
        isRecursive.setAlign(new TabularReportColumn.AlignStyle(TabularReportColumn.ALIGN_CENTER));
        report.addColumn(isRecursive);
    }

    protected class Row
    {
        private int level;
        private List ancestors;
        private XmlDataModelSchema schema;
        private TemplateProducer templateProducer;
        private String elementName;
        private boolean recursive;

        protected Row(int level, String elementName, List ancestors, XmlDataModelSchema schema, boolean recursive)
        {
            this.elementName = elementName;
            this.level = level;
            this.ancestors = ancestors;
            this.recursive = recursive;
            this.schema = schema;
        }

        protected Row(int level, String elementName, List ancestors, TemplateProducer templateProducer, boolean recursive)
        {
            this.elementName = elementName;
            this.level = level;
            this.ancestors = ancestors;
            this.recursive = recursive;
            this.templateProducer = templateProducer;
        }
    }

    private XmlDataModel dataModel;
    private static Map dataModelRows = new HashMap();

    public void addRow(List rows, int level, List ancestors, Object parent, String name)
    {
        XmlDataModelSchema parentSchema = (XmlDataModelSchema) ancestors.get(0);
        XmlDataModelSchema.PropertyNames propNames = (XmlDataModelSchema.PropertyNames) parentSchema.getPropertyNames().get(name);

        boolean recursive = false;
        XmlDataModelSchema schema = null;
        Object childInstance = null;

        if(name != null)
        {
            try
            {
                childInstance = parentSchema.createElement(null, null, parent, name, true);
                if(childInstance != null)
                    schema = XmlDataModelSchema.getSchema(childInstance.getClass());
                else
                {
                    log.error("Unable to create child for " + name);
                    return;
                }

            }
            catch (DataModelException e)
            {
                log.error(e);
                return;
            }
            catch (UnsupportedElementException e)
            {
                log.error(e);
                return;
            }

            recursive = ancestors.contains(schema);

            rows.add(new Row(level, propNames.getPrimaryName(), ancestors, schema, recursive));
        }
        else
        {
            schema = XmlDataModelSchema.getSchema(parent.getClass());
            childInstance = parent;
        }

        if(recursive)
            return;

        List childElemNames = new ArrayList();
        Map schemaPropertyNames = schema.getPropertyNames();

        Iterator iterator = schema.getNestedElements().keySet().iterator();
        while (iterator.hasNext())
        {
            String nestedName = (String) iterator.next();
            if(schema.getOptions().ignoreNestedElement(nestedName) || ! ((XmlDataModelSchema.PropertyNames) schemaPropertyNames.get(nestedName)).isPrimaryName(nestedName))
                continue;

            childElemNames.add(nestedName);
        }

        TemplateProducers templateProducers = null;
        if(childInstance instanceof TemplateProducerParent)
        {
            templateProducers = ((TemplateProducerParent) childInstance).getTemplateProducers();
            for(int i = 0; i < templateProducers.size(); i++)
            {
                TemplateProducer templateProducer = templateProducers.get(i);
                childElemNames.add(templateProducer.getElementName());
            }
        }

        if(childElemNames.size() > 0)
        {
            String[] nested = (String[]) childElemNames.toArray(new String[childElemNames.size()]);
            Arrays.sort(nested);

            List childAncestors = new ArrayList();
            childAncestors.add(schema);
            childAncestors.addAll(ancestors);

            for(int i = 0; i < nested.length; i++)
            {
                String nestedName = nested[i];
                if(templateProducers != null && templateProducers.get(nestedName) != null)
                    rows.add(new Row(level+1, nestedName, ancestors, templateProducers.get(nestedName), false));
                else
                    addRow(rows, level+1, childAncestors, childInstance, nestedName);
            }
        }
    }

    public List createRows(XmlDataModel dataModel)
    {
        List rows = (List) dataModelRows.get(dataModel);
        if(rows != null)
            return rows;

        rows = new ArrayList();
        List ancestors = new ArrayList();
        ancestors.add(XmlDataModelSchema.getSchema(dataModel.getClass()));
        addRow(rows, -1, ancestors, dataModel, null);

        dataModelRows.put(dataModel, rows);
        return rows;
    }

    public XmlDataModel getDataModel()
    {
        return dataModel;
    }

    public void setDataModel(XmlDataModel dataModel)
    {
        this.dataModel = dataModel;
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new StructureDataSource(createRows(dataModel == null ? nc.getApplicationManagerComponent() : dataModel));
    }

    public TabularReport getReport()
    {
        return report;
    }

    protected class StructureDataSource implements TabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow;
        protected Row activeRow;
        protected List rows;
        protected Hierarchy hierarchy = new ActiveHierarchy();

        protected class ActiveHierarchy implements Hierarchy
        {
            public int getColumn()
            {
                return 0;
            }

            public int getLevel()
            {
                return activeRow.level;
            }

            public int getParentRow()
            {
                return row-1;
            }
        }

        public boolean isHierarchical()
        {
            return true;
        }

        public TabularReportDataSource.Hierarchy getActiveHiearchy()
        {
            return hierarchy;
        }

        public StructureDataSource(List rows)
        {
            this.rows = rows;
            lastRow = rows.size() - 1;
        }

        public Object getActiveRowColumnData(TabularReportValueContext vc, int columnIndex)
        {
            switch(columnIndex)
            {
                case 0:
                    return activeRow.elementName;

                case 1:
                    if(activeRow.templateProducer != null)
                    {
                        if(activeRow.templateProducer.isStatic())
                            return "Template producer (namespace '" + activeRow.templateProducer.getNameSpaceId() + "')";
                        else
                            return "Template producer (dynamic namespace: "+ vc.getSkin().constructClassRef(activeRow.templateProducer.getClass()) +")";
                    }
                    else
                        return vc.getSkin().constructClassRef(activeRow.schema.getBean());

                case 2:
                    if(activeRow.templateProducer == null)
                        return activeRow.schema.supportsCharacters() ? "Yes" : vc.getSkin().getBlankValue();
                    else
                        return vc.getSkin().getBlankValue();

                case 3:
                    return activeRow.recursive ? "Yes" : vc.getSkin().getBlankValue();

                default:
                    return "Unknown column " + columnIndex;
            }
        }

        public Object getActiveRowColumnData(TabularReportValueContext vc, String columnName)
        {
            throw new TabularReportException("getActiveRowColumnData(vc, columnName) is not suppored");
        }

        public boolean next()
        {
            if(row < lastRow)
            {
                row++;
                activeRow = (Row) rows.get(row);
                return true;
            }

            return false;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }
    }

}
