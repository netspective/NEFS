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
 * $Id: ValueSourcesDocumentationPanel.java,v 1.4 2003-03-29 13:00:56 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel;

import java.util.Set;
import java.util.Iterator;

import com.netspective.sparx.report.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.report.tabular.*;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.text.TextUtils;

public class ValueSourcesDocumentationPanel extends AbstractHtmlTabularReportPanel
{
    public static final TabularReport documentationReport = new BasicTabularReport();
    static
    {
        documentationReport.getFrame().setHeading(new StaticValueSource("Value Sources Documentation"));
        documentationReport.getFrame().setFlag(TabularReportFrame.RPTFRAMEFLAG_ALLOW_COLLAPSE);

        TabularReportColumn identifiers = documentationReport.createColumn();
        identifiers.setHeading(new StaticValueSource("Identifier(s)"));
        identifiers.setColIndex(0);
        identifiers.setWordWrap(false);
        documentationReport.addColumn(identifiers);

        TabularReportColumn doc = documentationReport.createColumn();
        doc.setHeading(new StaticValueSource("Documentation"));
        documentationReport.addColumn(doc);
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new DocumentationReportDataSource();
    }

    public TabularReport getReport()
    {
        return documentationReport;
    }

    public static abstract class ReportDataSource implements TabularReportDataSource
    {
        protected int row = -1;
        protected int lastRow = ValueSources.getInstance().getValueSourceClassesSet().size() - 1;
        protected ValueSources factory = ValueSources.getInstance();
        protected Set valueSourceClassesSet = ValueSources.getInstance().getValueSourceClassesSet();
        protected Iterator vsIterator = valueSourceClassesSet.iterator();
        protected Class vsClass;
        protected String[] identifiers;

        public boolean isHierarchical()
        {
            return false;
        }

        public TabularReportDataSource.Hierarchy getActiveHiearchy()
        {
            return null;
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
                vsClass = (Class) vsIterator.next();
                identifiers = factory.getValueSourceIdentifiers(vsClass);
                return true;
            }

            return false;
        }

        public int getActiveRowNumber()
        {
            return row + 1;
        }
    }

    public static class DocumentationReportDataSource extends ReportDataSource
    {
        private ValueSourceDocumentation doc;

        public DocumentationReportDataSource()
        {
        }

        public Object getActiveRowColumnData(TabularReportValueContext vc, int columnIndex)
        {
            switch(columnIndex)
            {
                case 0:
                    return TextUtils.join(identifiers, ", ");

                case 1:
                    if(doc != null)
                    {
                        String usage = (identifiers.length > 1 ? "<i>id</i>" : identifiers[0]) + ":" + doc.getUsageHtml();
                        return "<font color=green>" + usage + "</font><br>" + doc.getDescription() + "<br><font color=#999999>" + vc.getSkin().constructClassRef(vsClass) + "</font>";
                    }
                    else
                        return "No documentation available in " + vc.getSkin().constructClassRef(vsClass) + ".";

                default:
                    return "Invalid column: " + columnIndex;
            }
        }

        public boolean next()
        {
            if(! super.next())
                return false;

            doc = factory.getValueSourceDocumentation(vsClass);
            return true;
        }
    }
}
