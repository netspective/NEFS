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

package com.netspective.sparx.panel.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.netspective.commons.metric.CountMetric;
import com.netspective.commons.metric.Metric;
import com.netspective.commons.metric.MetricsProducer;
import com.netspective.sparx.Project;

/**
 * Class for saving all the panel editors defined in the project
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
     * @return Project
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
    public void add(PanelEditor panel)
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
    }

    /**
     * Gets a panel editor by its index
     *
     * @param i
     *
     * @return
     */
    public PanelEditor get(int i)
    {
        return (PanelEditor) panels.get(i);
    }

    /**
     * Gets a panel editor by its name
     *
     * @param name
     *
     * @return
     */
    public PanelEditor get(String name)
    {
        return (PanelEditor) byName.get(PanelEditor.translateNameForMapKey(name));
    }

    /**
     * Gets a subset of panels belonging to one name space
     *
     * @param nameSpace
     *
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
     * @return a list containing panel editors
     */
    public List getPanelEditors()
    {
        return panels;
    }

    /**
     * Gets all the names of the panel editors
     *
     * @return a set containing all the names of the panel editors
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
     *
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