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
 * $Id: ValueSourcesPage.java,v 1.1 2003-03-24 13:28:00 shahid.shah Exp $
 */

package com.netspective.sparx.console.page;

import java.io.Writer;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;

import javax.servlet.ServletException;

import com.netspective.sparx.console.ConsoleServletPage;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.StandardReport;
import com.netspective.sparx.report.Report;
import com.netspective.sparx.report.ReportColumn;
import com.netspective.sparx.report.ReportSkin;
import com.netspective.sparx.report.ReportContext;
import com.netspective.sparx.report.column.GeneralColumn;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.text.TextUtils;

public class ValueSourcesPage extends ConsoleServletPage
{
    public static final Report valueSourcesReport = new StandardReport();

    static
    {
        GeneralColumn identifiers = new GeneralColumn();
        identifiers.setHeading(new StaticValueSource("Identifier(s)"));
        identifiers.setColIndexInArray(0);
        identifiers.setWordWrap(false);

        GeneralColumn usage = new GeneralColumn();
        usage.setHeading(new StaticValueSource("Usage"));
        usage.setColIndexInArray(1);

        valueSourcesReport.getFrame().setHeading(new StaticValueSource("Value Sources"));
        valueSourcesReport.initialize(new ReportColumn[] {
                identifiers,
                usage,
            });
    }

    public void handlePageBody(Writer writer, NavigationContext nc) throws ServletException, IOException
    {
        ValueSources factory = ValueSources.getInstance();
        Set valueSourcesClasses = factory.getValueSourceClassesSet();

        Object[][] rows = new Object[valueSourcesClasses.size()][];
        int rowNum = 0;
        for(Iterator i = valueSourcesClasses.iterator(); i.hasNext(); )
        {
            Object[] row = new Object[valueSourcesReport.getColumns().size()];
            rows[rowNum] = row;

            Class vsClass = (Class) i.next();
            try
            {
                String[] identifiers = factory.getValueSourceIdentifiers(vsClass);
                ValueSourceDocumentation doc = factory.getValueSourceDocumentation(vsClass);

                row[0] = TextUtils.join(identifiers, ", ");
                if(doc != null)
                {
                    String usage = (identifiers.length > 1 ? "<i>id</i>" : identifiers[0]) + ":" + doc.getUsageHtml();

                    row[1] = "<font color=green>" + usage + "</font><br>" +
                            doc.getDescription() + "<br>Class: " + vsClass.getName();
                }
                else
                {
                    row[1] = "No documentation available in " + vsClass.getName();
                }
            }
            catch (Exception e)
            {
                throw new ServletException(e);
            }

            rowNum++;
        }

        ReportSkin skin = nc.getActiveTheme().getReportSkin();
        ReportContext rc = new ReportContext(nc.getServletContext(), nc.getServlet(), nc.getRequest(), nc.getResponse(), valueSourcesReport, skin);
        rc.produceReport(writer, rows);
    }
}