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
 * $Id: AntTargetsDocumentationPanel.java,v 1.1 2003-07-08 02:30:09 shahid.shah Exp $
 */

package com.netspective.sparx.console.form;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;
import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;

import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.report.tabular.BasicHtmlTabularReport;
import com.netspective.sparx.report.tabular.AbstractHtmlTabularReportDataSource;
import com.netspective.sparx.panel.AbstractHtmlTabularReportPanel;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.column.GeneralColumn;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.value.ValueSource;

public class AntTargetsDocumentationPanel extends AbstractHtmlTabularReportPanel
{
    public static final HtmlTabularReport targetsReport = new BasicHtmlTabularReport();
    private static final ValueSource noTargets = new StaticValueSource("No targets found in Ant project.");

    static
    {
        TabularReportColumn column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Target"));
        targetsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Description"));
        targetsReport.addColumn(column);

        column = new GeneralColumn();
        column.setHeading(new StaticValueSource("Dependencies"));
        targetsReport.addColumn(column);
    }

    public AntTargetsDocumentationPanel()
    {
        getFrame().setHeading(new StaticValueSource("Ant Build Targets"));
    }

    public TabularReportDataSource createDataSource(NavigationContext nc)
    {
        return new AntTargetsDocumentationDataSource(nc);
    }

    public HtmlTabularReport getReport(NavigationContext nc)
    {
        return targetsReport;
    }

    protected class AntTargetsDocumentationDataSource extends AbstractHtmlTabularReportDataSource
    {
        protected AntBuildDialog antBuildDialog;
        protected Project antProject;
        protected List targets = new ArrayList();
        protected Target activeTarget;
        protected int activeRowIndex = -1;
        protected int lastRowIndex;

        public AntTargetsDocumentationDataSource(NavigationContext nc)
        {
            super();
            antBuildDialog = (AntBuildDialog) nc.getDialogsManager().getDialog("console.ant-build");
            if(antBuildDialog == null)
                throw new RuntimeException("Unable to find console.ant-build dialog!");

            File projectFile = new File(antBuildDialog.getActiveAntProjectFileValueSource().getTextValue(nc));
            if(!projectFile.exists())
            {
                lastRowIndex = activeRowIndex;
                return;
            }

            antProject = AntBuildDialog.getConfiguredProject(projectFile);
            Set sortedTargetNames = new TreeSet(antProject.getTargets().keySet());
            for(Iterator i = sortedTargetNames.iterator(); i.hasNext(); )
            {
                String targetName = (String) i.next();
                if(! antBuildDialog.isShowPrivateTargets() && antBuildDialog.isPrivateTargetName(targetName))
                    continue;
                targets.add(antProject.getTargets().get(targetName));
            }

            lastRowIndex = targets.size() - 1;
        }

        public boolean isHierarchical()
        {
            return false;
        }

        public boolean isActiveTargetDefault()
        {
            return activeTarget.getName().equals(antProject.getDefaultTarget());
        }

        public Object getActiveRowColumnData(int columnIndex, int flags)
        {
            switch(columnIndex)
            {
                case 0:
                    return "<code>" + (isActiveTargetDefault() ? ("<b>" + activeTarget.getName() + "</b>") : activeTarget.getName()) + "</code>";

                case 1:
                    return activeTarget.getDescription();

                case 2:
                    StringBuffer dep = new StringBuffer();
                    boolean first = true;
                    for(Enumeration e = activeTarget.getDependencies(); e.hasMoreElements(); )
                    {
                        String targetName = e.nextElement().toString();
                        if(! antBuildDialog.isShowPrivateTargets() && antBuildDialog.isPrivateTargetName(targetName))
                            continue;
                        if(! first)
                            dep.append(", ");
                        dep.append(targetName);
                        first = false;
                    }
                    return dep.toString();

                default:
                    return null;
            }
        }

        public int getTotalRows()
        {
            return targets.size();
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
            activeTarget = (Target) targets.get(activeRowIndex);
        }

        public boolean next()
        {
            if(! hasMoreRows())
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
            return noTargets;
        }
    }
}
