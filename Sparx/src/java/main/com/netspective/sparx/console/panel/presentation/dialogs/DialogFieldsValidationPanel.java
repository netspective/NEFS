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
 * $Id: DialogFieldsValidationPanel.java,v 1.3 2003-05-30 23:11:33 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel.presentation.dialogs;

import java.util.Map;
import java.util.Arrays;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.console.panel.presentation.dialogs.DialogDetailPanel;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.report.tabular.column.NumericColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.validate.ValidationRules;
import com.netspective.commons.validate.ValidationRule;
import com.netspective.commons.xdm.XmlDataModelSchema;

public class DialogFieldsValidationPanel extends DialogDetailPanel
{
    public static final HtmlTabularReport dialogFieldsReport = new BasicHtmlTabularReport();

    static
    {
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Name"));
        dialogFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Type"));
        dialogFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Control Id"));
        dialogFieldsReport.addColumn(column);

        column = new NumericColumn();
        column.setHeading(new StaticValueSource("Validations"));
        dialogFieldsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Validators"));
        dialogFieldsReport.addColumn(column);
    }

    public DialogFieldsValidationPanel()
    {
        getFrame().setHeading(new StaticValueSource("Fields Validations"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        DialogDetailPanel.SelectedDialog selectedDialog = getSelectedDialog(nc);
        if(selectedDialog.getDataSource() != null)
            return selectedDialog.getDataSource();
        else
            return new DialogFieldsValidationPanelDataSource(selectedDialog);
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return dialogFieldsReport;
    }

    public static final String getValidationsHtml(HtmlTabularReportValueContext vc, ValidationRules rules)
    {
        StringBuffer text = new StringBuffer("<table>");
        for(int i = 0; i < rules.size(); i++)
        {
            ValidationRule rule = rules.get(i);
            text.append("<tr><td colspan=3 class=report-column-even>");
            text.append(vc.getSkin().constructClassRef(rule.getClass()));
            text.append("</td></tr>");
            if(! rule.getName().equals(rule.getClass().getName()))
            {
                text.append("<tr>");
                text.append("<td class=report-column-even>&nbsp;&nbsp;</td>");
                text.append("<td class=report-column-even>Name</td>");
                text.append("<td class=report-column-even>"+ rule.getName() +"</td>");
                text.append("</tr>");
            }
            XmlDataModelSchema schema = XmlDataModelSchema.getSchema(rule.getClass());
            Map attributeAccessors = schema.getAttributeAccessors();
            Object[] attrNames = attributeAccessors.keySet().toArray();
            Arrays.sort(attrNames);

            Map propertyNames = schema.getPropertyNames();
            for(int an = 0; an < attrNames.length; an++)
            {
                String attrName = (String) attrNames[an];
                if(attrName.equals("name") || attrName.equals("class"))
                    continue;

                XmlDataModelSchema.PropertyNames propNames = (XmlDataModelSchema.PropertyNames) propertyNames.get(attrName);
                if(propNames != null && propNames.isPrimaryName(attrName))
                {
                    XmlDataModelSchema.AttributeAccessor accessor = (XmlDataModelSchema.AttributeAccessor) attributeAccessors.get(attrName);
                    if(accessor != null)
                    {
                        text.append("<tr>");
                        text.append("<td class=report-column-even>&nbsp;&nbsp;</td>");
                        text.append("<td class=report-column-even>" + propNames.getPrimaryName() + "</td>");
                        try
                        {
                            text.append("<td class=report-column-even>" + accessor.get(null, rule) + "</td>");
                        }
                        catch (Exception e)
                        {
                            text.append("<td class=report-column-even>" + e.toString() + "</td>");
                        }
                        text.append("</tr>");
                    }
                }
            }
        }
        text.append("</table>");
        return text.toString();

    }

    protected class DialogFieldsValidationPanelDataSource extends DialogFieldsDataSource
    {
        public DialogFieldsValidationPanelDataSource(SelectedDialog selectedDialog)
        {
            super(selectedDialog);
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            DialogField activeField = activeRow.getField();

            switch(columnIndex)
            {
                case 0:
                case 1:
                case 2:
                    return super.getActiveRowColumnData(columnIndex, flags);

                case 3:
                    if(activeField != null)
                        return new Integer(activeField.getValidationRules().size());

                case 4:
                    if(activeField != null)
                        return getValidationsHtml((HtmlTabularReportValueContext) reportValueContext, activeField.getValidationRules());

                default:
                    return null;
            }
        }
    }
}
