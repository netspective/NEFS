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
 * $Id: XmlDataModelSchemaStructurePanel.java,v 1.3 2003-03-28 04:12:53 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.AbstractHtmlTabularReportPanel;

public class XmlDataModelSchemaStructurePanel extends AbstractHtmlTabularReportPanel
{
    private static final Log log = LogFactory.getLog(XmlDataModelSchemaStructurePanel.class);
    private static final TabularReport report = new BasicTabularReport();

    static
    {
        report.getFrame().setHeading(new StaticValueSource("XDM Components Usage"));

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

    private XmlDataModel dataModel;

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
        if(dataModel == null)
            dataModel = nc.getApplicationManagerComponent();
        return new StructureDataSource();
    }

    public TabularReport getReport()
    {
        return report;
    }

    protected class StructureDataSource implements TabularReportDataSource
    {
        protected int row = -1;
        protected List rows = new ArrayList();
        protected int lastRow;
        protected Row activeRow;
        protected Hierarchy hierarchy = new ActiveHierarchy();
        protected Set visitedElements = new HashSet();
        protected Set visitedTemplateProducers = new HashSet();

        protected class Row
        {
            private int level;
            private XmlDataModelSchema parentSchema;
            private XmlDataModelSchema schema;
            private String templateProducerElemName;
            private String elementName;
            private boolean recursive;

            public Row(int level, String elementName, XmlDataModelSchema parentSchema, XmlDataModelSchema schema, boolean recursive)
            {
                this.elementName = elementName;
                this.level = level;
                this.parentSchema = parentSchema;
                this.recursive = recursive;
                this.schema = schema;
            }

            public Row(int level, String elementName, XmlDataModelSchema parentSchema, String templateProducerElemName, boolean recursive)
            {
                this.elementName = elementName;
                this.level = level;
                this.parentSchema = parentSchema;
                this.recursive = recursive;
                this.templateProducerElemName = templateProducerElemName;
            }
        }

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

        public StructureDataSource()
        {
            addRow(-1, XmlDataModelSchema.getSchema(dataModel.getClass()), dataModel.getClass(), null);
            lastRow = rows.size() - 1;
        }

        public void addRow(int level, XmlDataModelSchema parentSchema, Class activeClass, String name)
        {
            XmlDataModelSchema schema = XmlDataModelSchema.getSchema(activeClass);
            XmlDataModelSchema.PropertyNames propNames = (XmlDataModelSchema.PropertyNames) parentSchema.getPropertyNames().get(name);

            boolean recursive = visitedElements.contains(schema);

            if(name != null)
                rows.add(new Row(level, propNames.getPrimaryName(), parentSchema, schema, recursive));

            if(recursive)
                return;

            visitedElements.add(schema);

            List children = new ArrayList();
            Map schemaPropertyNames = schema.getPropertyNames();

            Iterator iterator = schema.getNestedElements().keySet().iterator();
            while (iterator.hasNext())
            {
                String nestedName = (String) iterator.next();
                if(schema.getOptions().ignoreNestedElement(nestedName) || ! ((XmlDataModelSchema.PropertyNames) schemaPropertyNames.get(nestedName)).isPrimaryName(nestedName))
                    continue;

                children.add(nestedName);
            }

            if(children.size() > 0)
            {
                String[] nested = (String[]) children.toArray(new String[children.size()]);
                Arrays.sort(nested);

                for(int i = 0; i < nested.length; i++)
                {
                    String nestedName = nested[i];
                    try
                    {
                        addRow(level+1, schema, schema.getElementType(nestedName), nestedName);
                    }
                    catch(DataModelException e)
                    {
                        log.error(e);
                    }
                }
            }
        }

        public Object getActiveRowColumnData(TabularReportValueContext vc, int columnIndex)
        {
            switch(columnIndex)
            {
                case 0:
                    return activeRow.elementName;

                case 1:
                    if(activeRow.templateProducerElemName != null)
                        return "Template producer";
                    else
                        return activeRow.schema.getBean().getName();

                case 2:
                    if(activeRow.templateProducerElemName == null)
                        return activeRow.schema.supportsCharacters() ? "Yes" : "&nbsp;";
                    else
                        return "&nbsp;";

                case 3:
                    return activeRow.recursive ? "Yes" : "&nbsp;";

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
