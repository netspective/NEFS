/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.sparx.console.panel.presentation.dialogs;

import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReport;

public class DialogContextFieldStatesClassesPanel extends DialogDetailPanel
{
    public static final HtmlTabularReport stateClassesReport = new BasicHtmlTabularReport();

    static
    {
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Field"));
        stateClassesReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Type"));
        stateClassesReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Control Id"));
        stateClassesReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("State Class"));
        stateClassesReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Value Class"));
        stateClassesReport.addColumn(column);
    }

    protected class DialogContextSelectedDialog extends SelectedDialog
    {
        private DialogContext dialogContext;

        public DialogContextSelectedDialog(DialogContext dc)
        {
            super(dc.getProject().getDialogs(), null);
            setDialog(dc.getDialog());
            setDialogName(dc.getDialog().getName());
            setDataSource(new DialogContextFieldStatesPanelDataSource(this));
            setDialogContext(dc);
        }

        public DialogContext getDialogContext()
        {
            return dialogContext;
        }

        public void setDialogContext(DialogContext dialogContext)
        {
            this.dialogContext = dialogContext;
        }
    }

    public DialogContextFieldStatesClassesPanel()
    {
        getFrame().setHeading(new StaticValueSource("Field State Classes"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        DialogDetailPanel.SelectedDialog selectedDialog = new DialogContextSelectedDialog(nc.getDialogContext());
        return selectedDialog.getDataSource();
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return stateClassesReport;
    }

    protected class DialogContextFieldStatesPanelDataSource extends DialogFieldsDataSource
    {
        private DialogContextSelectedDialog selectedDialog;

        public DialogContextFieldStatesPanelDataSource(DialogContextSelectedDialog selectedDialog)
        {
            super(selectedDialog);
            this.selectedDialog = selectedDialog;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            DialogField activeField = activeRow.getField();
            DialogField.State activeFieldState = selectedDialog.getDialogContext().getFieldStates().getState(activeField);

            switch(columnIndex)
            {
                case 0:
                case 1:
                case 2:
                    return super.getActiveRowColumnData(columnIndex, flags);

                case 3:
                    if(activeFieldState != null)
                        return reportValueContext.getSkin().constructClassRef(activeFieldState.getClass());

                case 4:
                    if(activeFieldState != null)
                        return reportValueContext.getSkin().constructClassRef(activeFieldState.getValue().getClass());

                default:
                    return null;
            }
        }
    }
}
