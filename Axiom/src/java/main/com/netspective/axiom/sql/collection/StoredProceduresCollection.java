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
 * @author Aye Thu
 */

/**
 * $Id: StoredProceduresCollection.java,v 1.4 2003-11-19 05:27:31 aye.thu Exp $
 */

package com.netspective.axiom.sql.collection;

import com.netspective.axiom.sql.StoredProcedures;
import com.netspective.axiom.sql.StoredProcedure;
import com.netspective.axiom.SqlManager;
import com.netspective.commons.metric.Metric;
import com.netspective.commons.metric.CountMetric;
import com.netspective.commons.metric.AverageMetric;

import java.util.*;


public class StoredProceduresCollection implements StoredProcedures
{
    private SqlManager sqlManager;
    private List storedProcs = new ArrayList();
    private Map byName = new HashMap();
    private Set nameSpaceNames = new HashSet();

    public StoredProceduresCollection()
    {

    }

    public SqlManager getSqlManager()
    {
        return sqlManager;
    }

    public void setSqlManager(SqlManager sqlManager)
    {
        this.sqlManager = sqlManager;
    }

    public void add(StoredProcedure sp)
    {
        storedProcs.add(sp);
        byName.put(sp.getNameForMapKey(), sp);

		//TODO: Modify this to also use a method similar to getNameForMapKey() for case-insensitive namespaces
		if (null != sp.getNameSpace())
            nameSpaceNames.add(sp.getNameSpace().getNameSpaceId());
    }

    public StoredProcedure get(int i)
    {
        return (StoredProcedure) storedProcs.get(i);
    }

    public StoredProcedure get(String name)
    {
        return (StoredProcedure) byName.get(StoredProcedure.translateNameForMapKey(name));
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
        return storedProcs.size();
    }

    /**
     * Generates various metrics associated with stored procedures
     * @param parent
     */
    public void produceMetrics(Metric parent)
    {
        CountMetric qdMetric = parent.addCountMetric("Total Stored Procedures");
        qdMetric.setSum(storedProcs.size());
        AverageMetric avgFieldCountMetric = qdMetric.addAverageMetric("Avg Parameters Per Stored Procedure");
        CountMetric fieldCountMetric = qdMetric.addCountMetric("Total Parameters");

        for (int i=0; i < storedProcs.size(); i++)
        {
            int paramCount = ((StoredProcedure)storedProcs.get(i)).getParams().size();
            avgFieldCountMetric.incrementAverage(paramCount);
            fieldCountMetric.incrementCount(paramCount);
        }
    }
}
