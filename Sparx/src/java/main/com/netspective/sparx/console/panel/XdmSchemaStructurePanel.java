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
 * $Id: XdmSchemaStructurePanel.java,v 1.1 2003-03-31 20:16:55 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.report.tabular.*;
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
import com.netspective.sparx.report.ReportHttpServletValueContext;
import com.netspective.sparx.report.HtmlTabularReportSkin;

public class XdmSchemaStructurePanel extends AbstractHtmlTabularReportPanel
{
    private static final Log log = LogFactory.getLog(XdmSchemaStructurePanel.class);
    public static final String REQPARAMNAME_SHOW_CLASS_DETAIL = "xdm-class";
    private static final TabularReport structureReport = new BasicTabularReport();
    private static final TabularReport detailReport = new BasicTabularReport();

    static
    {
        structureReport.getFrame().setHeading(new StaticValueSource("XDM Structure"));

        GeneralColumn elementName = new GeneralColumn();
        elementName.setHeading(new StaticValueSource("Element"));
        elementName.setWordWrap(false);
        elementName.setUrl(new StaticValueSource("detail?"+ REQPARAMNAME_SHOW_CLASS_DETAIL +"=%{1}"));
        structureReport.addColumn(elementName);

        GeneralColumn xdmClass = new GeneralColumn();
        xdmClass.setHeading(new StaticValueSource("Class"));
        xdmClass.setWordWrap(false);
        structureReport.addColumn(xdmClass);

        GeneralColumn isText = new GeneralColumn();
        isText.setHeading(new StaticValueSource("Text"));
        isText.setWordWrap(false);
        isText.setAlign(new TabularReportColumn.AlignStyle(TabularReportColumn.ALIGN_CENTER));
        structureReport.addColumn(isText);

        GeneralColumn isRecursive = new GeneralColumn();
        isRecursive.setHeading(new StaticValueSource("Recursive"));
        isRecursive.setWordWrap(false);
        isRecursive.setAlign(new TabularReportColumn.AlignStyle(TabularReportColumn.ALIGN_CENTER));
        structureReport.addColumn(isRecursive);

        detailReport.getFrame().setHeading(new StaticValueSource("XDM Detail"));

        elementName = new GeneralColumn();
        elementName.setHeading(new StaticValueSource("Element"));
        elementName.setWordWrap(false);
        detailReport.addColumn(elementName);

        xdmClass = new GeneralColumn();
        xdmClass.setHeading(new StaticValueSource("Type"));
        xdmClass.setWordWrap(false);
        detailReport.addColumn(xdmClass);
    }

    protected class StructureRow
    {
        private int level;
        private List ancestors;
        private XmlDataModelSchema schema;
        private TemplateProducer templateProducer;
        private String elementName;
        private boolean recursive;

        protected StructureRow(int level, String elementName, List ancestors, XmlDataModelSchema schema, boolean recursive)
        {
            this.elementName = elementName;
            this.level = level;
            this.ancestors = ancestors;
            this.recursive = recursive;
            this.schema = schema;
        }

        protected StructureRow(int level, String elementName, List ancestors, TemplateProducer templateProducer, boolean recursive)
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
    private XdmSchemaStructurePanelViewEnumeratedAttribute view = new XdmSchemaStructurePanelViewEnumeratedAttribute(0);

    public void addStructureRow(List rows, int level, List ancestors, Object parent, String name)
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

            rows.add(new StructureRow(level, propNames.getPrimaryName(), ancestors, schema, recursive));
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
                    rows.add(new StructureRow(level+1, nestedName, ancestors, templateProducers.get(nestedName), false));
                else
                    addStructureRow(rows, level+1, childAncestors, childInstance, nestedName);
            }
        }
    }

    public List createStructureRows(XmlDataModel dataModel)
    {
        List rows = (List) dataModelRows.get(dataModel);
        if(rows != null)
            return rows;

        rows = new ArrayList();
        List ancestors = new ArrayList();
        ancestors.add(XmlDataModelSchema.getSchema(dataModel.getClass()));
        addStructureRow(rows, -1, ancestors, dataModel, null);

        dataModelRows.put(dataModel, rows);
        return rows;
    }

    public XdmSchemaStructurePanelViewEnumeratedAttribute getView()
    {
        return view;
    }

    public void setView(XdmSchemaStructurePanelViewEnumeratedAttribute view)
    {
        this.view = view;
    }

    public XmlDataModel getDataModel()
    {
        return dataModel;
    }

    public void setDataModel(XmlDataModel dataModel)
    {
        this.dataModel = dataModel;
    }

    public ReportHttpServletValueContext createContext(NavigationContext nc, HtmlTabularReportSkin skin)
    {
        ReportHttpServletValueContext vc = new ReportHttpServletValueContext(nc.getServletContext(), nc.getServlet(), nc.getRequest(), nc.getResponse(), getReport(nc), skin);
        if(view.getValueIndex() == XdmSchemaStructurePanelViewEnumeratedAttribute.TREE && vc.getReport() == structureReport)
        {
            TabularReportColumnState[] states = vc.getStates();
            states[1].setFlag(TabularReportColumn.COLFLAG_HIDDEN);
            states[2].setFlag(TabularReportColumn.COLFLAG_HIDDEN);
            states[3].setFlag(TabularReportColumn.COLFLAG_HIDDEN);
        }
        return vc;
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        List structureRows = createStructureRows(dataModel == null ? nc.getApplicationManagerComponent() : dataModel);
        if(view.getValueIndex() == XdmSchemaStructurePanelViewEnumeratedAttribute.DETAIL)
        {
            String showClassDetail = nc.getRequest().getParameter(REQPARAMNAME_SHOW_CLASS_DETAIL);
            for(int i = 0; i < structureRows.size(); i++)
            {
                try
                {
                    StructureRow structureRow = (StructureRow) structureRows.get(i);
                    if(structureRow.schema != null && structureRow.schema.getBean().getName().equals(showClassDetail))
                        return new DetailDataSource(structureRows, structureRow);
                }
                catch (DataModelException e)
                {
                    log.error(e);
                    return new DetailNotFoundDataSource();
                }
            }
            return new DetailNotFoundDataSource();
        }
        else
            return new StructureDataSource(structureRows);
    }

    public TabularReport getReport(NavigationContext nc)
    {
        return view.getValueIndex() == XdmSchemaStructurePanelViewEnumeratedAttribute.DETAIL ? detailReport : structureReport;
    }

    protected class StructureDataSource extends AbstractTabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow;
        protected StructureRow activeRow;
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

        public Object getActiveRowColumnData(TabularReportValueContext vc, int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    return activeRow.elementName;

                case 1:
                    if((flags & TabularReportColumn.GETDATAFLAG_FOR_URL) != 0)
                    {
                        if(activeRow.templateProducer != null)
                            return activeRow.templateProducer.getClass().getName();
                        else
                            return activeRow.schema.getBean().getName();
                    }
                    else
                    {
                        if(activeRow.templateProducer != null)
                        {
                            if(activeRow.templateProducer.isStatic())
                                return "Template producer (namespace '" + activeRow.templateProducer.getNameSpaceId() + "')";
                            else
                                return "Template producer (dynamic namespace: "+ vc.getSkin().constructClassRef(activeRow.templateProducer.getClass()) +")";
                        }
                        else
                            return vc.getSkin().constructClassRef(activeRow.schema.getBean());
                    }

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

        public boolean next()
        {
            if(row < lastRow)
            {
                row++;
                activeRow = (StructureRow) rows.get(row);
                return true;
            }

            return false;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }
    }

    protected class DetailNotFoundDataSource extends AbstractTabularReportDataSource
    {
    }

    protected class DetailDataSource extends AbstractTabularReportDataSource
    {
        private class DetailRow
        {
            private StructureRow childElement;
            private String attrName;
            private Class attrType;

            public DetailRow(StructureRow childElement)
            {
                this.childElement = childElement;
            }

            public DetailRow(String attrName) throws DataModelException
            {
                this.attrName = attrName;
                attrType = structureRow.schema.getAttributeType(attrName);
            }
        }

        private List structureRows;
        private Map childPropertyNames;
        private StructureRow structureRow;
        private int activeRowNum = -1;
        private int lastRowNum;
        private DetailRow activeRow;
        private List detailRows = new ArrayList();

        public DetailDataSource(List structureRows, StructureRow structureRow) throws DataModelException
        {
            this.structureRows = structureRows;
            this.structureRow = structureRow;

            if(structureRow.schema != null)
            {
                // find all the structure rows that have this structureRow as the first ancestor (meaning we're their parent)
                for(int i = 0; i < structureRows.size(); i++)
                {
                    StructureRow checkChildRow = (StructureRow) structureRows.get(i);
                    if(checkChildRow.schema != null)
                    {
                        if(checkChildRow.ancestors.get(0) == structureRow.schema)
                            detailRows.add(new DetailRow(checkChildRow));
                    }
                }

                childPropertyNames = structureRow.schema.getPropertyNames();
                Iterator iterator = new TreeSet(structureRow.schema.getAttributes()).iterator();
                while (iterator.hasNext())
                {
                    String attrName = (String) iterator.next();
                    if(structureRow.schema.getOptions().ignoreAttribute(attrName))
                        continue;

                    XmlDataModelSchema.PropertyNames attrNames = (XmlDataModelSchema.PropertyNames) childPropertyNames.get(attrName);
                    if(! attrNames.isPrimaryName(attrName))
                        continue;

                    detailRows.add(new DetailRow(attrName));
                }

            }

            activeRowNum = -1;
            lastRowNum = detailRows.size() - 1;
        }

        public boolean next()
        {
            if(activeRowNum < lastRowNum)
            {
                activeRowNum++;
                activeRow = (DetailRow) detailRows.get(activeRowNum);
                return true;
            }

            return false;
        }

        public int getActiveRowNumber()
        {
            return activeRowNum;
        }

        public Object getActiveRowColumnData(TabularReportValueContext vc, int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0 :
                    if(activeRow.childElement != null)
                        return "&lt;" + activeRow.childElement.elementName + "&gt;";
                    else
                        return activeRow.attrName;

                case 1:
                    if(activeRow.childElement != null)
                        return vc.getSkin().constructClassRef(activeRow.childElement.schema.getBean());
                    else
                        return vc.getSkin().constructClassRef(activeRow.attrType);

                default:
                    return "Unknown column " + columnIndex;
            }
        }
    }
}
