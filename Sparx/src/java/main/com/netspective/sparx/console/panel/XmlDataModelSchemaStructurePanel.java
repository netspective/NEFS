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
 * $Id: XmlDataModelSchemaStructurePanel.java,v 1.1 2003-03-27 22:22:56 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel;

import com.netspective.sparx.report.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.report.tabular.*;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xml.template.TemplateProducers;
import com.netspective.commons.xml.template.TemplateProducerParent;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

        GeneralColumn xdmSystemId = new GeneralColumn();
        xdmSystemId.setHeading(new StaticValueSource("Text"));
        xdmSystemId.setWordWrap(false);
        report.addColumn(xdmSystemId);
    }

    private Class xdmClass;

    public Class getXdmClass()
    {
        return xdmClass;
    }

    public void setXdmClass(Class xdmClass)
    {
        this.xdmClass = xdmClass;
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
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
        protected Object[] activeRow;
        protected Set visitedElements = new HashSet();

        public StructureDataSource()
        {
            addRow(-1, XmlDataModelSchema.getSchema(xdmClass), xdmClass, null);
            lastRow = rows.size() - 1;
        }

        public void addRow(int level, XmlDataModelSchema parentSchema, Class cls, String name)
        {
            TemplateProducers templateProducers = null;
            if(parentSchema instanceof TemplateProducerParent)
                templateProducers = ((TemplateProducerParent) parentSchema).getTemplateProducers();

            if (visitedElements.contains(name))
                return;

            visitedElements.add(name);

            XmlDataModelSchema schema = XmlDataModelSchema.getSchema(cls);

            if(name != null)
                rows.add(new Object[] { new Integer(level), parentSchema, schema, name });

            Iterator iterator = schema.getNestedElements().keySet().iterator();
            while (iterator.hasNext())
            {
                String nestedName = (String) iterator.next();
                if(schema.getOptions().ignoreNestedElement(nestedName))
                    continue;
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

        public Object getData(TabularReportValueContext vc, int columnIndex)
        {
            switch(columnIndex)
            {
                case 0:
                    int level = ((Integer) activeRow[0]).intValue();
                    StringBuffer indent = new StringBuffer();
                    for(int i = 0; i < level; i++)
                        indent.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                    indent.append((String) activeRow[3]);
                    return indent.toString();

                case 1:
                    return ((XmlDataModelSchema) activeRow[2]).getBean().getName();

                default:
                    return ((XmlDataModelSchema) activeRow[2]).supportsCharacters() ? "Yes" : "&nbsp;";
            }
        }

        public Object getData(TabularReportValueContext vc, String columnName)
        {
            throw new TabularReportException("getData(vc, columnName) is not suppored");
        }

        public boolean next()
        {
            if(row < lastRow)
            {
                row++;
                activeRow = (Object[]) rows.get(row);
                return true;
            }

            return false;
        }

        public int getRow()
        {
            return row + 1;
        }
    }

}
