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
 * $Id: XdmComponentsPanel.java,v 1.11 2003-04-05 14:14:59 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel;

import java.util.Iterator;
import java.util.Map;
import java.util.List;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.report.tabular.TabularReportException;
import com.netspective.commons.report.tabular.AbstractTabularReportDataSource;
import com.netspective.sparx.report.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.xdm.XdmComponent;
import com.netspective.commons.io.InputSourceTracker;
import com.netspective.commons.io.FileTracker;

public class XdmComponentsPanel extends AbstractHtmlTabularReportPanel
{
    private static final HtmlTabularReport report = new BasicHtmlTabularReport();

    static
    {
        NumericColumn index = new NumericColumn();
        report.addColumn(index);

        GeneralColumn xdmClass = new GeneralColumn();
        xdmClass.setHeading(new StaticValueSource("XDM Component"));
        report.addColumn(xdmClass);

        GeneralColumn xdmSystemId = new GeneralColumn();
        xdmSystemId.setHeading(new StaticValueSource("Source"));
        xdmSystemId.setColIndex(2);
        report.addColumn(xdmSystemId);
    }

    public XdmComponentsPanel()
    {
        getFrame().setHeading(new StaticValueSource("XDM Components Usage"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new UsageReportDataSource();
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return report;
    }

    public class UsageReportDataSource extends AbstractTabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow = XdmComponentFactory.getComponentsBySystemId().size() - 1;
        protected Map componentsBySystemId = XdmComponentFactory.getComponentsBySystemId();
        protected Iterator iterator = componentsBySystemId.entrySet().iterator();
        protected String systemId;
        protected XdmComponent component;

        public String getHtml(FileTracker ft)
        {
            StringBuffer src = new StringBuffer();
            src.append(ft.getFile().getAbsolutePath());
            List preProcs = ft.getPreProcessors();
            if(preProcs != null && preProcs.size() > 0)
            {
                src.append("<ul>");
                for(int i = 0; i < preProcs.size(); i++)
                    src.append("<li>"+ getHtml((FileTracker) preProcs.get(i)) +" (pre-processors)</li>");
                src.append("</ul>");
            }

            List dependencies = ft.getIncludes();
            if(dependencies != null && dependencies.size() > 0)
            {
                src.append("<ul>");
                for(int i = 0; i < dependencies.size(); i++)
                    src.append("<li>"+ getHtml((FileTracker) dependencies.get(i)) +"</li>");
                src.append("</ul>");
            }

            return src.toString();
        }

        public String getErrorsHtml(List errors)
        {
            StringBuffer src = new StringBuffer();
            if(errors.size() > 0)
            {
                src.append("<ul>");
                for(int i = 0; i < errors.size(); i++)
                    src.append("<li>"+ errors.get(i) +"</li>");
                src.append("</ul>");
            }

            return src.toString();
        }

        public Object getActiveRowColumnData(TabularReportValueContext vc, int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    return new Integer(getActiveRowNumber());

                case 1:
                    return vc.getSkin().constructClassRef(component.getClass());

                case 2:
                    StringBuffer sb = new StringBuffer();
                    InputSourceTracker ist = component.getInputSource();
                    if(ist instanceof FileTracker)
                        sb.append(getHtml((FileTracker) ist));
                    else
                        sb.append(ist.getIdentifier() + " (Dependencies: " + ist.getDependenciesCount() + ")");
                    sb.append(getErrorsHtml(component.getErrors()));
                    return sb.toString();

                default:
                    return "Invalid column: " + columnIndex;
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
                Map.Entry entry = (Map.Entry) iterator.next();
                systemId = (String) entry.getKey();
                component = (XdmComponent) entry.getValue();
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