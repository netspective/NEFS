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
 * $Id: SchemaTableAncestorsPanel.java,v 1.3 2003-04-23 15:42:15 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel.data.schema;

import java.util.List;
import java.util.ArrayList;

import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.panel.HtmlPanelFrame;
import com.netspective.sparx.value.ServletValueContext;
import com.netspective.axiom.schema.Table;

public class SchemaTableAncestorsPanel extends SchemaStructurePanel
{
    public static final String REQATTRNAME_SELECTED_ROW = "selected-row";

    public class TableDescriptionValueSource extends AbstractValueSource
    {
        private Value emptyValue = new GenericValue("");

        public Value getPresentationValue(ValueContext vc)
        {
            StructureRow selectedRow = (StructureRow) ((ServletValueContext) vc).getRequest().getAttribute(REQATTRNAME_SELECTED_ROW);

            if(selectedRow == null)
                return emptyValue;
            else
            {
                Table table = selectedRow.getTable();
                if(table != null)
                    return new GenericValue(table.getDescription());
                else
                    return emptyValue;
            }
        }

        public Value getValue(ValueContext vc)
        {
            return getPresentationValue(vc);
        }

        public boolean hasValue(ValueContext vc)
        {
            return true;
        }
    }

    public SchemaTableAncestorsPanel()
    {
        getFrame().setHeading(new StaticValueSource("Table Hierarchy"));
        getBanner().setContent(new TableDescriptionValueSource());
        getFrame().getFlags().setFlag(HtmlPanelFrame.Flags.ALLOW_COLLAPSE);
    }

    public TabularReportDataSource createDataSource(NavigationContext nc, HtmlTabularReportValueContext vc)
    {
        List rows = createStructureRows(nc.getSqlManager().getSchemas());
        StructureRow selectedRow = getSelectedStructureRow(nc, rows);
        vc.getRequest().setAttribute(REQATTRNAME_SELECTED_ROW, selectedRow);

        if(selectedRow == null)
            return new SimpleMessageDataSource(vc, noTableSelected);
        else
            return new AncestorsDataSource(vc, rows, selectedRow);
    }

    protected class AncestorsDataSource extends StructureDataSource
    {
        protected StructureRow selectedRow;

        public AncestorsDataSource(HtmlTabularReportValueContext vc, List structureRows, StructureRow selectedRow)
        {
            super(vc, structureRows, selectedRow);
            this.selectedRow = selectedRow;
            this.rows = new ArrayList();

            for(int i = 0; i < structureRows.size(); i++)
            {
                StructureRow checkRow = (StructureRow) structureRows.get(i);
                if(checkRow == selectedRow || selectedRow.ancestors.contains(checkRow) || (checkRow.ancestors != null && checkRow.ancestors.contains(selectedRow)))
                    rows.add(structureRows.get(i));
            }

            lastRow = this.rows.size() - 1;
        }

        public ValueSource getNoDataFoundMessage()
        {
            return noTableSelected;
        }
    }
}
