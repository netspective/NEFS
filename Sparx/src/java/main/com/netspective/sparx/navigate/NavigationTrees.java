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
 * $Id: NavigationTrees.java,v 1.5 2003-10-19 17:05:32 shahid.shah Exp $
 */

package com.netspective.sparx.navigate;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.Project;

public class NavigationTrees
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
            log.debug("Unable to find NavigationTree '"+ name +"'. Available: " + trees);
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
            Constructor c = cls.getConstructor(new Class[] { Project.class });
            return (NavigationTree) c.newInstance(new Object[] { project });
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
}
