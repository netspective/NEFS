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
package com.netspective.sparx.navigate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.metric.AverageMetric;
import com.netspective.commons.metric.CountMetric;
import com.netspective.commons.metric.Metric;
import com.netspective.commons.metric.MetricsProducer;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.Project;

public class NavigationTrees implements MetricsProducer
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(NavigationTrees.class);

    private Project project;
    private NavigationTree defaultTree;
    private Map trees = new HashMap();

    public NavigationTrees(Project project)
    {
        this.project = project;
    }

    public Map getTrees()
    {
        return trees;
    }

    public NavigationTree getNavigationTree(final String name)
    {
        NavigationTree tree = (NavigationTree) trees.get(name);

        if(tree == null && log.isDebugEnabled())
        {
            log.debug("Unable to find NavigationTree '" + name + "'. Available: " + trees);
            return null;
        }

        return tree;
    }

    public NavigationTree createNavigationTree()
    {
        return new NavigationTree(project);
    }

    public NavigationTree createNavigationTree(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        if(NavigationTree.class.isAssignableFrom(cls))
        {
            Constructor c = cls.getConstructor(new Class[]{Project.class});
            return (NavigationTree) c.newInstance(new Object[]{project});
        }
        else
            throw new RuntimeException("Don't know what to do with with class: " + cls);
    }

    public void addNavigationTree(NavigationTree tree)
    {
        if(tree.isDefaultTree() || tree.getName() == null)
            defaultTree = tree;

        trees.put(tree.getName(), tree);
    }

    /**
     * Ê
     * Sets the tree to be the default tree.
     *
     * @param name Tree name
     */
    public void setDefaultTree(String name)
    {
        if(defaultTree != null)
        {
            if(defaultTree.getName() == null)
                throw new RuntimeException("Failed to change the default navigation tree because current default tree doesn" +
                                           "not have a name associated with it.");
            defaultTree.setDefault(false);
        }
        NavigationTree tree = getNavigationTree(name);
        tree.setDefault(true);
        defaultTree = tree;
    }

    public NavigationTree getDefaultTree()
    {
        return defaultTree;
    }

    public int size()
    {
        return trees.size();
    }

    public String toString()
    {
        return trees.toString();
    }

    /**
     * Generates various metrics related to navigation trees
     */
    public void produceMetrics(Metric parent)
    {
        CountMetric treesMetric = parent.addCountMetric("Total Navigation Trees");
        // don't count the console tree
        treesMetric.setSum(trees.size() > 1 ? trees.size() - 1 : 0);
        AverageMetric avgLevelMetric = treesMetric.addAverageMetric("Avg Depth Per Tree");
        AverageMetric avgPageMetric = treesMetric.addAverageMetric("Avg Pages Per Tree");
        CountMetric totalPagesMetric = treesMetric.addCountMetric("Total Pages");
        Iterator itr = trees.values().iterator();

        while(itr.hasNext())
        {
            NavigationTree navigationTree = (NavigationTree) itr.next();
            // exclude the CONSOLE navigation tree from the metrics
            if(!navigationTree.getName().equals("console"))
            {
                avgPageMetric.incrementAverage(navigationTree.size());
                totalPagesMetric.incrementCount(navigationTree.size());
                avgLevelMetric.incrementAverage(navigationTree.getMaxLevel());
            }
        }
    }
}
