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
 * $Id: DialogFieldsPanel.java,v 1.1 2003-05-06 14:52:14 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel.presentation.dialogs;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.console.panel.presentation.dialogs.DialogDetailPanel;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.value.ValueSource;

public class DialogFieldsPanel extends DialogDetailPanel
{
    public static final HtmlTabularReport dialogFieldsReport = new BasicHtmlTabularReport();
    protected static final ValueSource noFields = new StaticValueSource("Dialog has no parameters.");

    static
    {
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Name"));
        dialogFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Control Id"));
        dialogFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Caption"));
        dialogFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Flags"));
        dialogFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Default"));
        dialogFieldsReport.addColumn(column);
    }

    public DialogFieldsPanel()
    {
        getFrame().setHeading(new StaticValueSource("Dialog Fields"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        DialogDetailPanel.SelectedDialog selectedDialog = getSelectedDialog(vc);
        if(selectedDialog.getDataSource() != null)
            return selectedDialog.getDataSource();
        else
            return new DialogFieldsDataSource(vc, selectedDialog);
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return dialogFieldsReport;
    }

    protected class DialogFieldsDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected int activeRowIndex = -1;
        protected int lastRowIndex;
        protected DialogDetailPanel.FieldRow activeRow;
        protected DialogDetailPanel.FieldRows fieldRows;
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
                return activeRow.getParentRow() != null ? fieldRows.indexOf(activeRow.getParentRow()) : -1;
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

        public DialogFieldsDataSource(HtmlTabularReportValueContext vc, DialogDetailPanel.SelectedDialog selectedDialog)
        {
            super(vc);
            fieldRows = new DialogDetailPanel.FieldRows(selectedDialog.getDialog());
            lastRowIndex = fieldRows.size() - 1;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            DialogField activeField = activeRow.getField();

            switch(columnIndex)
            {
                case 0:
                    if(activeField != null)
                        return activeField.getQualifiedName();
                    else
                        return activeRow.heading;

                case 1:
                    if(activeField != null)
                        return activeField.getHtmlFormControlId();

                case 2:
                    if(activeField != null)
                        return activeField.getCaption() != ValueSource.NULL_VALUE_SOURCE ?
                                activeField.getCaption().getSpecification() :
                                null;

                case 3:
                    if(activeField != null)
                        return activeField.getFlags().getFlagsText();

                case 4:
                    if(activeField != null)
                        return activeField.getDefault() != null && activeField.getDefault() != ValueSource.NULL_VALUE_SOURCE ?
                                activeField.getDefault().getSpecification() :
                                null;

                default:
                    return null;
            }
        }

        public boolean next()
        {
            if(activeRowIndex < lastRowIndex)
            {
                activeRowIndex++;
                activeRow = fieldRows.get(activeRowIndex);
                return true;
            }

            return false;
        }

        public int getActiveRowNumber()
        {
            return activeRowIndex + 1;
        }

        public ValueSource getNoDataFoundMessage()
        {
            return noFields;
        }
    }
}
