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
 * $Id: DialogsCatalogPanel.java,v 1.5 2003-05-30 23:11:33 shahid.shah Exp $
 */

package com.netspective.sparx.console.panel.presentation.dialogs;

import java.util.TreeSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.form.Dialogs;
import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogsManager;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.RuntimeEnvironmentFlags;

public abstract class DialogsCatalogPanel extends AbstractHtmlTabularReportPanel
{
    public DialogsCatalogPanel()
    {
        getFrame().setHeading(new StaticValueSource("Available Dialogs"));
    }

    public class DialogsCatalogDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected Dialogs dialogs;
        protected Dialog activeRowDialog;
        protected String selectedDialogName;
        protected String activeNameSpace;
        protected List rows = new ArrayList();
        protected int activeRow = -1;
        protected int lastRow;
        protected TabularReportDataSource.Hierarchy hierarchy = new ActiveHierarchy();

        protected class ActiveHierarchy implements TabularReportDataSource.Hierarchy
        {
            public int getColumn()
            {
                return 0;
            }

            public int getLevel()
            {
                return activeRowDialog != null ? 1 : 0;
            }

            public int getParentRow()
            {
                return -1; //TODO: need to implement this
            }
        }

        public DialogsCatalogDataSource(NavigationContext nc, String selectedDialogName)
        {
            super();
            dialogs = nc.getApplicationManager().getDialogs();
            this.selectedDialogName = selectedDialogName;
            boolean doingFrameworkDeveploment = nc.getEnvironmentFlags().flagIsSet(RuntimeEnvironmentFlags.FRAMEWORK_DEVELOPMENT);

            //TODO: this does not account for dialogs that are not contained within a namespace
            Set sortedNamesSpaces = new TreeSet(dialogs.getNameSpaceNames());
            for(Iterator nsi = sortedNamesSpaces.iterator(); nsi.hasNext(); )
            {
                String nameSpaceId = (String) nsi.next();
                if(nameSpaceId.startsWith("console") && ! doingFrameworkDeveploment)
                    continue;

                Set sortedDialogNamesInNameSpace = new TreeSet();

                for(int i = 0; i < dialogs.size(); i++)
                {
                    Dialog dialog = dialogs.get(i);
                    if(dialog.getNameSpace() != null && nameSpaceId.equals(dialog.getNameSpace().getNameSpaceId()))
                    {
                        sortedDialogNamesInNameSpace.add(dialog.getQualifiedName());
                    }
                }

                rows.add(nameSpaceId);
                rows.addAll(sortedDialogNamesInNameSpace);
            }

            lastRow = rows.size() - 1;
        }

        public boolean isHierarchical()
        {
            return true;
        }

        public boolean isActiveRowSelected()
        {
            if(activeRowDialog == null)
                return false;

            return activeRowDialog.getQualifiedName().equals(selectedDialogName);
        }

        public TabularReportDataSource.Hierarchy getActiveHierarchy()
        {
            return hierarchy;
        }

        public int getActiveRowNumber()
        {
            return activeRow;
        }

        public int getTotalRows()
        {
            return rows.size();
        }

        public boolean hasMoreRows()
        {
            return activeRow < lastRow;
        }

        public boolean isScrollable()
        {
            return true;
        }

        public void setActiveRow(int rowNum)
        {
            activeRow = rowNum;
            String itemName = (String) rows.get(activeRow);
            activeRowDialog = dialogs.get(itemName);
            if(activeRowDialog == null)
                activeNameSpace = itemName;
            else
                activeNameSpace = null;
        }

        public boolean next()
        {
            if(! hasMoreRows())
                return false;

            setActiveRow(activeRow + 1);
            return true;
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            if(activeNameSpace != null)
            {
                switch(columnIndex)
                {
                    case 0:
                        return activeNameSpace;

                    default:
                        return null;
                }
            }

            switch(columnIndex)
            {
                case 0:
                    return reportValueContext.getSkin().constructRedirect(reportValueContext, reportValueContext.getReport().getColumn(0).getCommand(), activeRowDialog.getName(), activeRowDialog.getQualifiedName(), null);

                case 1:
                    return activeRowDialog.getQualifiedName();

                case 2:
                    return activeRowDialog.getHtmlFormName();

                default:
                    return null;
            }
        }

    }
}

