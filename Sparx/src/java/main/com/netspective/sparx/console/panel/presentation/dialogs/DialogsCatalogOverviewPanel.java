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
 * $Id: DialogsCatalogOverviewPanel.java,v 1.7 2003-09-10 04:02:18 aye.thu Exp $
 */

package com.netspective.sparx.console.panel.presentation.dialogs;

import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.console.panel.presentation.dialogs.DialogDetailPanel;
import com.netspective.sparx.form.DialogFlags;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.value.source.HttpServletRedirectValueSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.text.TextUtils;

public class DialogsCatalogOverviewPanel extends DialogsCatalogPanel
{
    public static final HtmlTabularReport catalogReport = new BasicHtmlTabularReport();
    private static final TabularReportColumn dialogIdColumn = new GeneralColumn();

    static
    {
        dialogIdColumn.setHeading(new StaticValueSource("Dialog"));
        dialogIdColumn.setRedirect(new HttpServletRedirectValueSource("unit-test?"+ DialogDetailPanel.REQPARAMNAME_DIALOG +"=%{1}"));
        catalogReport.addColumn(dialogIdColumn);

        // this is here just so that it will be available as part of the URL (it's hidden)
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Name"));
        column.getFlags().setFlag(TabularReportColumn.Flags.HIDDEN);
        catalogReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("HTML Form Name"));
        catalogReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Heading"));
        catalogReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Fields"));
        catalogReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Loop"));
        catalogReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Retain Params"));
        catalogReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Dialog Flags"));
        catalogReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Debug Flags"));
        catalogReport.addColumn(column);
    }

    public DialogsCatalogOverviewPanel()
    {
        getFrame().setHeading(new StaticValueSource("Overview"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new CatalogDataSource(nc, nc.getHttpRequest().getParameter(DialogDetailPanel.REQPARAMNAME_DIALOG));
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return catalogReport;
    }

    public class CatalogDataSource extends DialogsCatalogDataSource
    {
        public CatalogDataSource(NavigationContext nc, String selectedDialogName)
        {
            super(nc, selectedDialogName);
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            if(activeNameSpace != null)
                return super.getActiveRowColumnData(columnIndex, flags);

            switch(columnIndex)
            {
                case 0:
                case 1:
                case 2:
                    return super.getActiveRowColumnData(columnIndex, flags);

                case 3:
                    return activeRowDialog.getFrame().getHeading() != null && activeRowDialog.getFrame().getHeading() != ValueSource.NULL_VALUE_SOURCE ?
                            activeRowDialog.getFrame().getHeading().getSpecification() :
                            null;

                case 4:
                    return activeRowDialog.getFields() != null ? new Integer(activeRowDialog.getFields().totalSize()) : null;

                case 5:
                    return activeRowDialog.getLoop().getValue();

                case 6:
                    return activeRowDialog.getDialogFlags().flagIsSet(DialogFlags.RETAIN_ALL_REQUEST_PARAMS) ?
                            "ALL" :
                            (activeRowDialog.getRetainParams() != null ? TextUtils.join(activeRowDialog.getRetainParams(), ", ") : null);

                case 7:
                    return activeRowDialog.getDialogFlags().getFlagsText();

                case 8:
                    return activeRowDialog.getDebugFlags().getFlagsText();

                default:
                    return null;
            }
        }
    }
}

