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
package com.netspective.commons.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netspective.commons.metric.CountMetric;
import com.netspective.commons.metric.Metric;

public class BeanScripts implements Scripts
{
    private List scripts = new ArrayList();
    private Map byName = new HashMap();
    private Set nameSpaceNames = new HashSet();

    public BeanScripts()
    {
    }

    public BeanScript createBeanScript()
    {
        return new BeanScript();
    }

    public void add(Script script)
    {
        if(script.getName() == null)
            throw new RuntimeException("Bean scripts must have a name");

        scripts.add(script);
        byName.put(script.getQualifiedName(), script);
        if(null != script.getNameSpace())
            nameSpaceNames.add(script.getNameSpace().getNameSpaceId());
    }

    public Script get(int i)
    {
        return (Script) scripts.get(i);
    }

    public Script get(String name)
    {
        return (Script) byName.get(name);
    }

    public Set getNames()
    {
        return byName.keySet();
    }

    public Set getNameSpaceNames()
    {
        return nameSpaceNames;
    }

    public int size()
    {
        return scripts.size();
    }

    public void produceMetrics(Metric parent)
    {
        CountMetric tpMetric = parent.addCountMetric("Total Script Packages");
        tpMetric.setSum(nameSpaceNames.size());
        CountMetric tdMetric = parent.addCountMetric("Total Scripts");
        tdMetric.setSum(scripts.size());
    }
}
