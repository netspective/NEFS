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
 * $Id: QueryParameters.java,v 1.2 2003-04-09 16:57:37 shahid.shah Exp $
 */

package com.netspective.axiom.sql;

import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.xdm.XmlDataModelSchema;

public class QueryParameters
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    public class ValueApplyContext
    {
        private int activeParamNum;

        public ValueApplyContext()
        {
            activeParamNum = 0;
        }

        public int getActiveParamNum()
        {
            return activeParamNum;
        }

        public int getNextParamNum()
        {
            return ++activeParamNum;
        }
    }

    public class ValueRetrieveContext
    {
        private List bindValues = new ArrayList();
        private List bindTypes = new ArrayList();

        public ValueRetrieveContext()
        {
            bindValues = new ArrayList();
            bindTypes = new ArrayList();
        }

        public void addBindValue(Object object, int type)
        {
            bindValues.add(object);
            bindTypes.add(new Integer(type));
        }

        public Object[] getBindValues()
        {
            return bindValues.toArray();
        }

        public Integer[] getBindTypes()
        {
            return (Integer[]) bindTypes.toArray(new Integer[bindTypes.size()]);
        }
    }

    private Query query;
    private List params = new ArrayList();

    public QueryParameters(Query parent)
    {
        setQuery(parent);
    }

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(Query query)
    {
        this.query = query;
    }

    public QueryParameter createParam()
    {
        return new QueryParameter(this);
    }

    public void addParam(QueryParameter param)
    {
        params.add(param);
        param.setIndex(params.size());
    }

    /**
     * Retrieve the bind parameters that would be applied to a prepared statement (useful for debugging).
     * @param cc The connection context
     * @return An object that holds all the bind parameters and associated SQL types
     * @throws SQLException
     */
    public ValueRetrieveContext retrieve(ConnectionContext cc) throws SQLException
    {
        ValueRetrieveContext vrc = new ValueRetrieveContext();

        if(params.size() == 0)
            return vrc;

        int paramsCount = params.size();
        for(int i = 0; i < paramsCount; i++)
            ((QueryParameter) params.get(i)).retrieve(vrc, cc);

        return vrc;
    }

    /**
     * Apply the parameters in this list to the given prepared statement.
     * @param cc The connection context
     * @param stmt The prepared statement
     * @return The index of the last parameter applied
     * @throws SQLException
     */
    public int apply(ConnectionContext cc, PreparedStatement stmt) throws SQLException
    {
        ValueApplyContext vac = new ValueApplyContext();

        if(params.size() == 0)
            return 0;

        int paramsCount = params.size();
        for(int i = 0; i < paramsCount; i++)
        {
            ((QueryParameter) params.get(i)).apply(vac, cc, stmt);
        }

        return vac.getActiveParamNum();
    }

    public QueryParameter get(int index)
    {
        return (QueryParameter) params.get(index);
    }

    public int size()
    {
        return params.size();
    }
}
