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

import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.Dialogs;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.DialogFields;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;

public abstract class DialogDetailPanel extends AbstractHtmlTabularReportPanel
{
    public static final String REQPARAMNAME_DIALOG = "selected-dialog-id";
    private static final ValueSource noDialogParamAvailSource = new StaticValueSource("No '" + REQPARAMNAME_DIALOG + "' parameter provided.");
    protected static final ValueSource noFields = new StaticValueSource("Dialog has no fields.");

    protected class SelectedDialog
    {
        private String dialogName;
        private Dialog dialog;
        private TabularReportDataSource dataSource;

        public SelectedDialog(Dialogs dialogs, String dialogName)
        {
            this.dialogName = dialogName;

            if (dialogName == null)
            {
                dataSource = new AbstractHtmlTabularReportPanel.SimpleMessageDataSource(noDialogParamAvailSource);
                return;
            }

            dialog = dialogs.get(dialogName);
            if (dialog == null)
                dataSource = new AbstractHtmlTabularReportPanel.SimpleMessageDataSource("Dialog '" + dialogName + "' not found. Available: " + dialogs.getNames());
        }

        public TabularReportDataSource getDataSource()
        {
            return dataSource;
        }

        public void setDataSource(TabularReportDataSource dataSource)
        {
            this.dataSource = dataSource;
        }

        public Dialog getDialog()
        {
            return dialog;
        }

        public void setDialog(Dialog dialog)
        {
            this.dialog = dialog;
        }

        public String getDialogName()
        {
            return dialogName;
        }

        public void setDialogName(String dialogName)
        {
            this.dialogName = dialogName;
        }

        private List createAttribute(String name, Object value)
        {
            List result = new ArrayList();
            result.add(name);
            result.add(value);
            return result;
        }

        public List getDialogAttributes(HtmlTabularReportValueContext rc)
        {
            List result = new ArrayList();
            result.add(createAttribute("Name", dialog.getQualifiedName()));
            result.add(createAttribute("Html Form Name", dialog.getHtmlFormName()));
            result.add(createAttribute("Dialog Class", rc.getSkin().constructClassRef(dialog.getClass())));
            result.add(createAttribute("Dialog Context Class", rc.getSkin().constructClassRef(dialog.getDialogContextClass())));
            result.add(createAttribute("Dialog Director Class", rc.getSkin().constructClassRef(dialog.getDirector().getClass())));
            result.add(createAttribute("Heading", dialog.getHtmlFormName()));
            result.add(createAttribute("Number of Fields", new Integer(dialog.getFields().totalSize())));
            result.add(createAttribute("Loop Display", dialog.getLoop().getValue()));
            result.add(createAttribute("Retain Request Params", dialog.getRetainParams()));
            result.add(createAttribute("Dialog Flags", dialog.getDialogFlags().getFlagsText()));
            result.add(createAttribute("Debug Flags", dialog.getDebugFlags().getFlagsText()));
            return result;
        }
    }

    protected class FieldRow
    {
        protected int level;
        protected FieldRow parentRow;
        protected FieldRows ancestors;
        protected String heading;
        protected DialogField field;

        protected FieldRow(int level, FieldRow parentRow, String heading)
        {
            this.level = level;
            this.parentRow = parentRow;
            this.heading = heading;
        }

        protected FieldRow(int level, DialogField field, FieldRows ancestors)
        {
            this.level = level;
            this.parentRow = ancestors.size() > 0 ? ancestors.get(0) : null;
            this.ancestors = ancestors;
            this.field = field;
        }

        public FieldRow getParentRow()
        {
            return parentRow;
        }

        public DialogField getField()
        {
            if (field != null)
                return field;

            return null;
        }
    }

    protected class FieldRows
    {
        protected List rows = new ArrayList();

        public FieldRows()
        {
        }

        public FieldRows(Dialog dialog)
        {
            DialogFields children = dialog.getFields();
            FieldRows ancestors = new FieldRows();
            for (int c = 0; c < children.size(); c++)
                add(0, children.get(c), ancestors);
        }

        public void add(int level, DialogField field, FieldRows ancestors)
        {
            FieldRow activeRow = new FieldRow(level, field, ancestors);
            rows.add(activeRow);
            DialogFields children = field.getChildren();
            if (children != null)
            {
                for (int c = 0; c < children.size(); c++)
                {
                    FieldRows childAncestors = new FieldRows();
                    childAncestors.rows.add(activeRow);
                    childAncestors.rows.addAll(ancestors.rows);
                    add(level + 1, children.get(c), childAncestors);
                }
            }
        }

        public FieldRow get(int i)
        {
            return (FieldRow) rows.get(i);
        }

        public int indexOf(FieldRow row)
        {
            return rows.indexOf(row);
        }

        public int size()
        {
            return rows.size();
        }
    }

    public SelectedDialog getSelectedDialog(NavigationContext nc)
    {
        return new SelectedDialog(nc.getProject().getDialogs(), nc.getHttpRequest().getParameter(REQPARAMNAME_DIALOG));
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

        public DialogFieldsDataSource(DialogDetailPanel.SelectedDialog selectedDialog)
        {
            super();
            fieldRows = new DialogDetailPanel.FieldRows(selectedDialog.getDialog());
            lastRowIndex = fieldRows.size() - 1;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            DialogField activeField = activeRow.getField();

            switch (columnIndex)
            {
                case 0:
                    if (activeField != null)
                        return activeField.getQualifiedName();
                    else
                        return activeRow.heading;

                case 1:
                    if (activeField != null)
                        return activeField.getFieldTypes().size() > 0 ? activeField.getFieldTypes().get(0) : null;

                case 2:
                    if (activeField != null)
                        return activeField.getHtmlFormControlId();

                case 3:
                    if (activeField != null)
                        return activeField.getCaption() != ValueSource.NULL_VALUE_SOURCE ?
                                activeField.getCaption().getSpecification() :
                                null;

                case 4:
                    if (activeField != null)
                        return activeField.getFlags().getFlagsText();

                case 5:
                    if (activeField != null)
                        return activeField.getDefault() != null && activeField.getDefault() != ValueSource.NULL_VALUE_SOURCE ?
                                activeField.getDefault().getSpecification() :
                                null;

                case 6:
                    if (activeField != null)
                        return activeField.getHint() != null && activeField.getHint() != ValueSource.NULL_VALUE_SOURCE ?
                                activeField.getHint().getSpecification() :
                                null;

                default:
                    return null;
            }
        }

        public int getTotalRows()
        {
            return fieldRows.size();
        }

        public boolean hasMoreRows()
        {
            return activeRowIndex < lastRowIndex;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            activeRowIndex = rowNum;
            activeRow = fieldRows.get(activeRowIndex);
        }

        public boolean next()
        {
            if (!hasMoreRows())
                return false;

            setActiveRow(activeRowIndex + 1);
            return true;
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
