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
package com.netspective.commons.metric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Metric
{
    public static final int METRIC_TYPE_GROUP = 0;
    public static final int METRIC_TYPE_SIMPLE_SUM = 1;
    public static final int METRIC_TYPE_AVERAGE = 2;
    public static final int METRIC_TYPE_LAST = 3; /* for extension classes */

    public static final int METRICFLAG_SORT_CHILDREN = 1;
    public static final int METRICFLAG_SHOW_PCT_OF_PARENT = METRICFLAG_SORT_CHILDREN * 2;
    public static final int METRICFLAG_SUM_CHILDREN = METRICFLAG_SHOW_PCT_OF_PARENT * 2;

    private Metrics owner;
    private Metric parent;
    private int level;
    private String name;
    private List children;
    private Map childMap;
    private long flags;

    public Metric(int level, Metric parent, String name)
    {
        this.level = level;
        setParent(parent);
        this.name = name;
    }

    protected void setOwner(Metrics owner)
    {
        this.owner = owner;
    }

    public int getLevel()
    {
        return level;
    }

    public Metrics getOwner()
    {
        return owner;
    }

    public String getName()
    {
        return name;
    }

    public Metric getParent()
    {
        return parent;
    }

    public void setParent(Metric metric)
    {
        parent = metric;
        if(parent != null) metric.setOwner(parent.getOwner());
    }

    public abstract String getFormattedValue();

    public final long getFlags()
    {
        return flags;
    }

    public final boolean flagIsSet(long flag)
    {
        return (flags & flag) == 0 ? false : true;
    }

    public final void setFlag(long flag)
    {
        setFlag(flag, false);
    }

    public final void clearFlag(long flag)
    {
        clearFlag(flag, false);
    }

    public final void setFlag(long flag, boolean includeChildren)
    {
        flags |= flag;
        if(includeChildren && children != null)
        {
            Iterator i = children.iterator();
            while(i.hasNext())
            {
                ((Metric) i.next()).setFlag(flag);
            }
        }
    }

    public final void clearFlag(long flag, boolean includeChildren)
    {
        flags &= ~flag;
        if(includeChildren && children != null)
        {
            Iterator i = children.iterator();
            while(i.hasNext())
            {
                ((Metric) i.next()).clearFlag(flag);
            }
        }
    }

    public List getChildren()
    {
        return children;
    }

    protected void addChild(Metric metric)
    {
        metric.setParent(this);
        if(children == null)
        {
            children = new ArrayList();
            childMap = new HashMap();
        }
        children.add(metric);
        childMap.put(metric.getName(), metric);
    }

    public MetricsGroup addGroupMetric(String name)
    {
        MetricsGroup metric = (MetricsGroup) getChild(name);
        if(metric != null)
            return metric;

        metric = new MetricsGroup(this, name);
        addChild(metric);
        return metric;
    }

    public ValueMetric addValueMetric(String name, String value)
    {
        ValueMetric metric = (ValueMetric) getChild(name);
        if(metric != null)
            return metric;

        metric = new ValueMetric(this, name, value);
        addChild(metric);
        return metric;
    }

    public CountMetric addCountMetric(String name)
    {
        CountMetric metric = (CountMetric) getChild(name);
        if(metric != null)
            return metric;

        metric = new CountMetric(this, name);
        addChild(metric);
        return metric;
    }

    public AverageMetric addAverageMetric(String name)
    {
        AverageMetric metric = (AverageMetric) getChild(name);
        if(metric != null)
            return metric;

        metric = new AverageMetric(this, name);
        addChild(metric);
        return metric;
    }

    public FileTypeMetric addFileTypeMetric(String name, boolean isCode)
    {
        FileTypeMetric metric = (FileTypeMetric) getChild(name);
        if(metric != null)
            return metric;

        metric = new FileTypeMetric(this, name, isCode);
        addChild(metric);
        return metric;
    }

    public Metric getChild(String name)
    {
        if(childMap == null)
            return null;

        return (Metric) childMap.get(name);
    }

    public int getAncestorCount()
    {
        int count = 0;
        Metric parent = getParent();
        while(parent != null)
        {
            count++;
            parent = parent.getParent();
        }
        return count;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < getAncestorCount(); i++)
            sb.append("  ");

        sb.append(getName());

        String fv = getFormattedValue();
        if(fv != null)
        {
            sb.append(": ");
            sb.append(fv);
        }
        sb.append("\n");

        if(children != null)
        {
            for(int i = 0; i < children.size(); i++)
            {
                Metric childMetric = (Metric) children.get(i);
                sb.append(childMetric);
            }
        }
        return sb.toString();
    }
}