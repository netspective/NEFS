/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Aye Thu
 */

package com.netspective.sparx.panel;

import com.netspective.commons.metric.CountMetric;
import com.netspective.commons.metric.Metric;
import com.netspective.commons.metric.MetricsProducer;
import com.netspective.sparx.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class for saving all the panel editors defined in the project
 *
 */
public class PanelEditors implements MetricsProducer
{
    private Project project;
    private List panels = new ArrayList();
    private Map byName = new TreeMap();
    private Map byNameSpace = new TreeMap();
    private Set nameSpaceNames = new TreeSet();

    public PanelEditors(Project project)
    {
        this.project = project;
    }

    /**
     * Gets the project associated with the panels
     *
     * @return  Project
     */
    public Project getProject()
    {
        return project;
    }

    /**
     * Adds a panel editor
     *
     * @param panel
     */
    public void add(ReportPanelEditor panel)
    {
        panels.add(panel);
        byName.put(panel.getNameForMapKey(), panel);
		if (null != panel.getNameSpace())
        {
            String nameSpaceId = panel.getNameSpace().getNameSpaceId();
            if (!byNameSpace.containsKey(nameSpaceId))
                byNameSpace.put(nameSpaceId, new ArrayList());
            ((ArrayList) byNameSpace.get(nameSpaceId)).add(panel);
            nameSpaceNames.add(panel.getNameSpace().getNameSpaceId());
        }
        //  add the panels' defined query to the project's list of queries
        //getProject().getQueries().add(panel.getQuery());

    }

    /**
     * Gets a panel editor by its index
     *
     * @param i
     * @return
     */
    public ReportPanelEditor get(int i)
    {
        return (ReportPanelEditor) panels.get(i);
    }

    /**
     * Gets a panel editor by its name
     *
     * @param name
     * @return
     */
    public ReportPanelEditor get(String name)
    {
        return (ReportPanelEditor) byName.get(ReportPanelEditor.translateNameForMapKey(name));
    }

    /**
     * Gets a subset of panels belonging to one name space
     * @param nameSpace
     * @return
     */
    public List getByNameSpace(String nameSpace)
    {
        if (byNameSpace.containsKey(nameSpace))
            return (ArrayList) byNameSpace.get(nameSpace);
        return null;
    }

    /**
     * Gets all the panel editors as a list
     *
     * @return      a list containing panel editors
     */
    public List getPanelEditors()
    {
        return panels;
    }

    /**
     * Gets all the names of the panel editors
     *
     * @return      a set containing all the names of the panel editors
     */
    public Set getNames()
    {
        return byName.keySet();
    }

    /**
     * Gets all the namespaces of the panel editors (panel editor names are of the "pkg.name" format so
     * this mehtod returns all the 'pkg' names)
     *
     * @return
     */
    public Set getNameSpaceNames()
    {
        return nameSpaceNames;
    }

    /**
     * Gets the total of all the panel editors
     *
     * @return
     */
    public int size()
    {
        return panels.size();
    }

    /**
     * Generates various metrics associated with the dialogs
     * @param parent
     */
    public void produceMetrics(Metric parent)
    {
        CountMetric tpMetric = parent.addCountMetric("Total Packages");
        tpMetric.setSum(nameSpaceNames.size());
        CountMetric tdMetric = parent.addCountMetric("Total Panel Editors");
        tdMetric.setSum(panels.size());

    }

}