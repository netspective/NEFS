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
 * $Id: XdmSchemaStructurePanel.java,v 1.5 2003-09-10 04:02:18 aye.thu Exp $
 */

package com.netspective.sparx.console.panel.framework;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportColumnState;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.value.source.HttpServletRedirectValueSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XmlDataModel;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xdm.exception.UnsupportedElementException;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.text.TextUtils;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;

public class XdmSchemaStructurePanel extends AbstractHtmlTabularReportPanel
{
    private static final Log log = LogFactory.getLog(XdmSchemaStructurePanel.class);
    public static final String REQPARAMNAME_SHOW_CLASS_DETAIL = "xdm-class";
    private static final HtmlTabularReport structureReport = new BasicHtmlTabularReport();
    private static final HtmlTabularReport detailReport = new BasicHtmlTabularReport();
    private static final ValueSource noSelectedRowMsgSource = new StaticValueSource("Please select an element.");
    private static final GeneralColumn elementTagIdColumn = new GeneralColumn();

    static
    {
        elementTagIdColumn.setHeading(new StaticValueSource("Element"));
        elementTagIdColumn.setRedirect(new HttpServletRedirectValueSource("detail?"+ REQPARAMNAME_SHOW_CLASS_DETAIL +"=%{1}"));
        structureReport.addColumn(elementTagIdColumn);

        GeneralColumn xdmClass = new GeneralColumn();
        xdmClass.setHeading(new StaticValueSource("Class"));
        structureReport.addColumn(xdmClass);

        GeneralColumn isText = new GeneralColumn();
        isText.setHeading(new StaticValueSource("Text"));
        isText.setAlign(new TabularReportColumn.AlignStyle(TabularReportColumn.ALIGN_CENTER));
        structureReport.addColumn(isText);

        GeneralColumn isRecursive = new GeneralColumn();
        isRecursive.setHeading(new StaticValueSource("Recursive"));
        isRecursive.setAlign(new TabularReportColumn.AlignStyle(TabularReportColumn.ALIGN_CENTER));
        structureReport.addColumn(isRecursive);

        GeneralColumn elementName = new GeneralColumn();
        elementName.setHeading(new StaticValueSource("Element"));
        detailReport.addColumn(elementName);

        xdmClass = new GeneralColumn();
        xdmClass.setHeading(new StaticValueSource("Type"));
        detailReport.addColumn(xdmClass);

        GeneralColumn xdmChoices = new GeneralColumn();
        xdmChoices.setHeading(new StaticValueSource("Choices"));
        detailReport.addColumn(xdmChoices);
    }

    protected class StructureRow
    {
        private StructureRow parentRow;
        private int level;
        private List ancestors;
        private XmlDataModelSchema schema;
        private Object instance;
        private TemplateProducer templateProducer;
        private Class interfaceClass;
        private String elementName;
        private boolean recursive;

        protected StructureRow(int level, String elementName, StructureRow parentRow, List ancestors, XmlDataModelSchema schema, Object instance, boolean recursive)
        {
            this.parentRow = parentRow;
            this.elementName = elementName;
            this.level = level;
            this.ancestors = ancestors;
            this.recursive = recursive;
            this.schema = schema;
            this.instance = instance;
        }

        protected StructureRow(int level, String elementName, StructureRow parentRow, List ancestors, TemplateProducer templateProducer, boolean recursive)
        {
            this.parentRow = parentRow;
            this.elementName = elementName;
            this.level = level;
            this.ancestors = ancestors;
            this.recursive = recursive;
            this.templateProducer = templateProducer;
        }

        protected StructureRow(int level, String elementName, StructureRow parentRow, List ancestors, Class interfaceClass, boolean recursive)
        {
            this.parentRow = parentRow;
            this.elementName = elementName;
            this.level = level;
            this.ancestors = ancestors;
            this.recursive = recursive;
            this.interfaceClass = interfaceClass;
        }

        public boolean isConcreteClass()
        {
            return templateProducer == null && interfaceClass == null;
        }
    }

    private XmlDataModel dataModel;
    private static Map dataModelRows = new HashMap();
    private XdmSchemaStructurePanelViewEnumeratedAttribute view = new XdmSchemaStructurePanelViewEnumeratedAttribute(0);

    public void addStructureRow(List rows, int level, StructureRow parentRow, List ancestors, Object parent, String name)
    {
        XmlDataModelSchema parentSchema = (XmlDataModelSchema) ancestors.get(0);
        XmlDataModelSchema.PropertyNames propNames = (XmlDataModelSchema.PropertyNames) parentSchema.getPropertyNames().get(name);

        boolean recursive = false;
        XmlDataModelSchema schema = null;
        Object childInstance = null;

        StructureRow childRow = null;
        if(name != null)
        {
            try
            {
                childInstance = parentSchema.createElement(null, null, parent, name, true);
                if(childInstance != null)
                    schema = XmlDataModelSchema.getSchema(childInstance.getClass());
                else
                {
                    Class interfaceClass = parentSchema.getElementType(name);
                    if(interfaceClass == null)
                        interfaceClass = parentSchema.getAttributeType(name);
                    rows.add(new StructureRow(level, propNames.getPrimaryName(), parentRow, ancestors, interfaceClass, false));
                    log.warn("Unable to create child for " + name + ", must be an interface instead of a concrete class: " + interfaceClass);
                    return;
                }

            }
            catch (DataModelException e)
            {
                log.error("Error adding structureRow", e);
                return;
            }
            catch (UnsupportedElementException e)
            {
                log.error("Error adding structureRow", e);
                return;
            }

            recursive = ancestors.contains(schema);

            // flags have "createXXX" methods but they are not really nestable elements
            if(! XdmBitmaskedFlagsAttribute.class.isAssignableFrom(childInstance.getClass()))
            {
                childRow = new StructureRow(level, propNames.getPrimaryName(), parentRow, ancestors, schema, childInstance, recursive);
                rows.add(childRow);
            }
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
                    rows.add(new StructureRow(level+1, nestedName, childRow, ancestors, templateProducers.get(nestedName), false));
                else
                    addStructureRow(rows, level+1, childRow, childAncestors, childInstance, nestedName);
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
        addStructureRow(rows, -1, null, ancestors, dataModel, null);

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
        switch(view.getValueIndex())
        {
            case XdmSchemaStructurePanelViewEnumeratedAttribute.TREE:
                getFrame().setHeading(new StaticValueSource("All Elements"));
                break;

            case XdmSchemaStructurePanelViewEnumeratedAttribute.DETAIL_ANCESTORS:
                getFrame().setHeading(new StaticValueSource("Ancestors"));
                break;

            case XdmSchemaStructurePanelViewEnumeratedAttribute.DETAIL:
                getFrame().setHeading(new StaticValueSource("Children"));
                break;
        }
    }

    public XmlDataModel getDataModel()
    {
        return dataModel;
    }

    public void setDataModel(XmlDataModel dataModel)
    {
        this.dataModel = dataModel;
    }

    public boolean affectsNavigationContext(NavigationContext nc)
    {
        return true;
    }

    public HtmlTabularReportValueContext createContext(NavigationContext nc, HtmlTabularReportSkin skin)
    {
        HtmlTabularReportValueContext vc = new HtmlTabularReportValueContext(nc.getServlet(), nc.getRequest(), nc.getResponse(), this, getReport(nc), skin);
        if(view.getValueIndex() == XdmSchemaStructurePanelViewEnumeratedAttribute.TREE && vc.getReport() == structureReport)
        {
            TabularReportColumnState[] states = vc.getStates();
            states[1].getFlags().setFlag(TabularReportColumn.Flags.HIDDEN);
            states[3].getFlags().setFlag(TabularReportColumn.Flags.HIDDEN);
        }

        return vc;
    }

    public StructureRow getSelectedStructureRow(NavigationContext nc, List structureRows)
    {
        String selectedClass = nc.getRequest().getParameter(REQPARAMNAME_SHOW_CLASS_DETAIL);
        if(selectedClass == null)
            return null;

        for(int i = 0; i < structureRows.size(); i++)
        {
            StructureRow structureRow = (StructureRow) structureRows.get(i);
            if(structureRow.schema != null && structureRow.schema.getBean().getName().equals(selectedClass))
            {
                nc.setPageHeading(structureRow.schema.getBean().getName());
                return structureRow;
            }
        }

        return null;
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        List structureRows = createStructureRows(dataModel == null ? nc.getProjectComponent() : dataModel);
        StructureRow selectedRow = getSelectedStructureRow(nc, structureRows);

        switch(view.getValueIndex())
        {
            case XdmSchemaStructurePanelViewEnumeratedAttribute.STRUCTURE:
                return new StructureDataSource(structureRows, selectedRow);

            case XdmSchemaStructurePanelViewEnumeratedAttribute.TREE:
                return new StructureDataSource(structureRows, selectedRow);

            case XdmSchemaStructurePanelViewEnumeratedAttribute.DETAIL_ANCESTORS:
            case XdmSchemaStructurePanelViewEnumeratedAttribute.DETAIL:
                if(selectedRow != null)
                {
                    try
                    {
                        if(view.getValueIndex() == XdmSchemaStructurePanelViewEnumeratedAttribute.DETAIL_ANCESTORS)
                            return new DetailAncestorsDataSource(structureRows, selectedRow);
                        else
                            return new DetailDataSource(structureRows, selectedRow);
                    }
                    catch (DataModelException e)
                    {
                        log.error("Error creating data source", e);
                        return new SimpleMessageDataSource(noSelectedRowMsgSource);

                    }
                }
                return new SimpleMessageDataSource(noSelectedRowMsgSource);

            default:
                return null;
        }
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return view.getValueIndex() == XdmSchemaStructurePanelViewEnumeratedAttribute.DETAIL ? detailReport : structureReport;
    }

    protected class StructureDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow;
        protected StructureRow activeRow;
        protected StructureRow selectedRow;
        protected List rows;
        protected TabularReportDataSource.Hierarchy hierarchy = new ActiveHierarchy();

        protected class ActiveHierarchy implements TabularReportDataSource.Hierarchy
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
                return activeRow.parentRow != null ? rows.indexOf(activeRow.parentRow) : -1;
            }
        }

        public boolean isHierarchical()
        {
            return true;
        }

        public TabularReportDataSource.Hierarchy getActiveHierarchy()
        {
            return hierarchy;
        }

        public StructureDataSource(List rows, StructureRow selectedRow)
        {
            super();
            this.rows = rows;
            this.selectedRow = selectedRow;
            lastRow = rows.size() - 1;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    if(activeRow.isConcreteClass())
                        return reportValueContext.getSkin().constructRedirect(reportValueContext, elementTagIdColumn.getRedirect(), activeRow.elementName, null, null);
                    else
                        return activeRow.elementName;

                case 1:
                    if((flags & TabularReportColumn.GETDATAFLAG_FOR_URL) != 0)
                    {
                        if(activeRow.templateProducer != null)
                            return activeRow.templateProducer.getClass().getName();
                        else if(activeRow.interfaceClass != null)
                            return activeRow.interfaceClass.getName();
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
                                return "Template producer (dynamic namespace: "+ reportValueContext.getSkin().constructClassRef(activeRow.templateProducer.getClass()) +")";
                        }
                        else if(activeRow.interfaceClass != null)
                            return reportValueContext.getSkin().constructClassRef(activeRow.interfaceClass);
                        else
                            return reportValueContext.getSkin().constructClassRef(activeRow.schema.getBean());
                    }

                case 2:
                    if(activeRow.isConcreteClass())
                        return activeRow.schema.supportsCharacters() ? "Yes" : reportValueContext.getSkin().getBlankValue();
                    else
                        return reportValueContext.getSkin().getBlankValue();

                case 3:
                    return activeRow.recursive ? "Yes" : reportValueContext.getSkin().getBlankValue();

                default:
                    return "Unknown column " + columnIndex;
            }
        }

        public int getTotalRows()
        {
            return rows.size();
        }

        public boolean hasMoreRows()
        {
            return row < lastRow;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            row = rowNum;
            activeRow = (StructureRow) rows.get(row);
        }

        public boolean next()
        {
            if(! hasMoreRows())
                return false;

            setActiveRow(row+1);
            return true;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }

        public boolean isActiveRowSelected()
        {
            return activeRow == selectedRow;
        }
    }

    protected class DetailAncestorsDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow;
        protected StructureRow activeRow;
        protected StructureRow selectedRow;
        protected List ancestorRows;
        protected TabularReportDataSource.Hierarchy hierarchy = new ActiveHierarchy();

        protected class ActiveHierarchy implements TabularReportDataSource.Hierarchy
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
                return activeRow.parentRow != null ? ancestorRows.indexOf(activeRow.parentRow) : -1;
            }
        }

        public boolean isHierarchical()
        {
            return true;
        }

        public TabularReportDataSource.Hierarchy getActiveHierarchy()
        {
            return hierarchy;
        }

        public DetailAncestorsDataSource(List structureRows, StructureRow selectedRow)
        {
            super();
            this.selectedRow = selectedRow;
            this.ancestorRows = new ArrayList();

            for(int i = 0; i < structureRows.size(); i++)
            {
                StructureRow checkRow = (StructureRow) structureRows.get(i);
                if(checkRow == selectedRow ||
                   (selectedRow.ancestors.contains(checkRow.schema) && checkRow.level < selectedRow.level))
                    ancestorRows.add(structureRows.get(i));
            }

            lastRow = this.ancestorRows.size() - 1;
        }

        public ValueSource getNoDataFoundMessage()
        {
            return noSelectedRowMsgSource;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    if(activeRow.isConcreteClass())
                        return reportValueContext.getSkin().constructRedirect(reportValueContext, elementTagIdColumn.getRedirect(), activeRow.elementName, null, null);
                    else
                        return activeRow.elementName;

                case 1:
                    if((flags & TabularReportColumn.GETDATAFLAG_FOR_URL) != 0)
                    {
                        if(activeRow.templateProducer != null)
                            return activeRow.templateProducer.getClass().getName();
                        else if(activeRow.interfaceClass != null)
                            return activeRow.interfaceClass.getName();
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
                                return "Template producer (dynamic namespace: "+ reportValueContext.getSkin().constructClassRef(activeRow.templateProducer.getClass()) +")";
                        }
                        else if(activeRow.interfaceClass != null)
                            return reportValueContext.getSkin().constructClassRef(activeRow.interfaceClass);
                        else
                            return reportValueContext.getSkin().constructClassRef(activeRow.schema.getBean());
                    }

                case 2:
                    if(activeRow.isConcreteClass())
                        return activeRow.schema.supportsCharacters() ? "Yes" : reportValueContext.getSkin().getBlankValue();
                    else
                        return reportValueContext.getSkin().getBlankValue();

                case 3:
                    return activeRow.recursive ? "Yes" : reportValueContext.getSkin().getBlankValue();

                default:
                    return "Unknown column " + columnIndex;
            }
        }

        public int getTotalRows()
        {
            return ancestorRows.size();
        }

        public boolean hasMoreRows()
        {
            return row < lastRow;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            row = rowNum;
            activeRow = (StructureRow) ancestorRows.get(row);
        }

        public boolean next()
        {
            if(! hasMoreRows())
                return false;

            setActiveRow(row+1);
            return true;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }

        public boolean isActiveRowSelected()
        {
            return activeRow == selectedRow;
        }
    }

    protected class DetailDataSource extends AbstractHtmlTabularReportDataSource
    {
        private class DetailRow
        {
            private StructureRow childElement;
            private String attrName;
            private XdmBitmaskedFlagsAttribute bfa;
            private XdmBitmaskedFlagsAttribute.FlagDefn flagDefn;
            private Class attrType;

            public DetailRow(StructureRow childElement)
            {
                this.childElement = childElement;
            }

            public DetailRow(String attrName) throws DataModelException
            {
                this.attrName = attrName;
                attrType = selectedRow.schema.getAttributeType(attrName);
            }

            public DetailRow(String attrName, XdmBitmaskedFlagsAttribute bfa, XdmBitmaskedFlagsAttribute.FlagDefn flagDefn)
            {
                this.attrName = attrName;
                this.bfa = bfa;
                this.flagDefn = flagDefn;
            }
        }

        private Map childPropertyNames;
        private StructureRow selectedRow;
        private int activeRowNum = -1;
        private int lastRowNum;
        private DetailRow activeRow;
        private List detailRows = new ArrayList();

        public DetailDataSource(List structureRows, StructureRow selectedRow) throws DataModelException
        {
            super();
            this.selectedRow = selectedRow;

            if(selectedRow.schema != null)
            {
                childPropertyNames = selectedRow.schema.getPropertyNames();

                Map flagSetters = new HashMap();
                if(selectedRow.instance != null)
                {
                    for(Iterator i = selectedRow.schema.getFlagsAttributeAccessors().entrySet().iterator(); i.hasNext(); )
                    {
                        Map.Entry entry = (Map.Entry) i.next();
                        XmlDataModelSchema.AttributeAccessor accessor = (XmlDataModelSchema.AttributeAccessor) entry.getValue();
                        Object returnVal = null;
                        try
                        {
                            returnVal = accessor.get(null, selectedRow.instance);
                        }
                        catch (Exception e)
                        {
                            throw new DataModelException(null, e);
                        }

                        if(returnVal instanceof XdmBitmaskedFlagsAttribute)
                        {
                            XdmBitmaskedFlagsAttribute bfa = (XdmBitmaskedFlagsAttribute) returnVal;
                            Map xmlNodeNames = bfa.getFlagSetterXmlNodeNames();
                            for(Iterator xmliter = xmlNodeNames.keySet().iterator(); xmliter.hasNext(); )
                            {
                                String xmlNodeName = (String) xmliter.next();
                                if(! childPropertyNames.containsKey(xmlNodeName))
                                    flagSetters.put(xmlNodeName, new Object[] { bfa, xmlNodeNames.get(xmlNodeName) });
                            }
                        }
                    }
                }

                Set sortedChildPropertyNames = new TreeSet(selectedRow.schema.getAttributes());
                sortedChildPropertyNames.addAll(flagSetters.keySet());
                Iterator iterator = sortedChildPropertyNames.iterator();
                while (iterator.hasNext())
                {
                    String attrName = (String) iterator.next();

                    if(flagSetters.containsKey(attrName))
                    {
                        Object[] flagSetterInfo = (Object[]) flagSetters.get(attrName);
                        detailRows.add(new DetailRow(attrName, (XdmBitmaskedFlagsAttribute) flagSetterInfo[0], (XdmBitmaskedFlagsAttribute.FlagDefn) flagSetterInfo[1]));
                        continue;
                    }

                    if(selectedRow.schema.getOptions().ignoreAttribute(attrName))
                        continue;

                    XmlDataModelSchema.PropertyNames attrNames = (XmlDataModelSchema.PropertyNames) childPropertyNames.get(attrName);
                    if(attrNames != null && ! attrNames.isPrimaryName(attrName))
                        continue;

                    detailRows.add(new DetailRow(attrName));
                }

                // find all the structure rows that have this structureRow as the first ancestor (meaning we're their parent)
                Set addedChildElems = new HashSet();
                for(int i = 0; i < structureRows.size(); i++)
                {
                    StructureRow checkChildRow = (StructureRow) structureRows.get(i);
                    if(checkChildRow.schema != null)
                    {
                        if(checkChildRow.ancestors.get(0) == selectedRow.schema)
                        {
                            if(!addedChildElems.contains(checkChildRow.schema))
                            {
                                addedChildElems.add(checkChildRow.schema);
                                detailRows.add(new DetailRow(checkChildRow));
                            }
                        }
                    }
                }
            }

            activeRowNum = -1;
            lastRowNum = detailRows.size() - 1;
        }

        public ValueSource getNoDataFoundMessage()
        {
            return noSelectedRowMsgSource;
        }

        public int getTotalRows()
        {
            return detailRows.size();
        }

        public boolean hasMoreRows()
        {
            return activeRowNum < lastRowNum;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            activeRowNum = rowNum;
            activeRow = (DetailRow) detailRows.get(activeRowNum);
        }

        public boolean next()
        {
            if(! hasMoreRows())
                return false;

            setActiveRow(activeRowNum+1);
            return true;
        }

        public int getActiveRowNumber()
        {
            return activeRowNum;
        }

        public boolean isActiveRowSelected()
        {
            return activeRow.childElement == selectedRow;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0 :
                    if(activeRow.childElement != null)
                        return "<a href=\"?xdm-class="+ activeRow.childElement.schema.getBean().getName() +"\">&lt;" + activeRow.childElement.elementName + "&gt;</a>";
                    else
                        return activeRow.attrName;

                case 1:
                    if(activeRow.childElement != null)
                        return reportValueContext.getSkin().constructClassRef(activeRow.childElement.schema.getBean());
                    else if(activeRow.bfa != null)
                        return "<span title=\"alias for " + activeRow.flagDefn.getName() + " (" + activeRow.bfa.getClass() +")\">boolean (dynamic)</span>";
                    else
                        return reportValueContext.getSkin().constructClassRef(activeRow.attrType);

                case 2:
                    if(activeRow.attrType != null)
                    {
                        if(XdmBitmaskedFlagsAttribute.class.isAssignableFrom(activeRow.attrType))
                        {
                            XdmBitmaskedFlagsAttribute bfa = null;
                            try
                            {
                                XmlDataModelSchema.NestedCreator creator = (XmlDataModelSchema.NestedCreator) selectedRow.schema.getNestedCreators().get(activeRow.attrName);
                                if(creator != null)
                                    bfa = (XdmBitmaskedFlagsAttribute) creator.create(selectedRow.instance);
                                else
                                    bfa = (XdmBitmaskedFlagsAttribute) activeRow.attrType.newInstance();
                            }
                            catch (Exception e)
                            {
                                log.error("Error retrieving flags data", e);
                                return e.toString();
                            }

                            return TextUtils.join(bfa.getFlagNamesWithXdmAccess(), " | ");
                        }
                        else if(XdmEnumeratedAttribute.class.isAssignableFrom(activeRow.attrType))
                        {
                            XdmEnumeratedAttribute ea = null;
                            try
                            {
                                ea = (XdmEnumeratedAttribute) activeRow.attrType.newInstance();
                            }
                            catch (Exception e)
                            {
                                log.error("Error retrieving enumeration data", e);
                                return e.toString();
                            }

                            return TextUtils.join(ea.getValues(), ", ");
                        }
                        else if(Boolean.class.isAssignableFrom(activeRow.attrType) || (activeRow.attrType == boolean.class))
                            return TextUtils.join(TextUtils.getBooleanChoices(), ", ");
                        else
                            return reportValueContext.getSkin().getBlankValue();
                    }
                    else if(activeRow.bfa != null)
                        return TextUtils.join(TextUtils.getBooleanChoices(), ", ");
                    else
                        return reportValueContext.getSkin().getBlankValue();

                default:
                    return "Unknown column " + columnIndex;
            }
        }
    }
}
