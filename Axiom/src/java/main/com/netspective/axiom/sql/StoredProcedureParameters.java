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
 * $Id: StoredProcedureParameters.java,v 1.2 2003-10-31 03:35:12 aye.thu Exp $
 */

package com.netspective.axiom.sql;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.axiom.ConnectionContext;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class StoredProcedureParameters
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
        // ArrayList allows NULL items
        private List inOutValues = new ArrayList();
        private List valueTypes = new ArrayList();
        private List inOutTypes = new ArrayList();

        public ValueRetrieveContext()
        {
            inOutValues = new ArrayList();
            valueTypes = new ArrayList();
            inOutTypes = new ArrayList();
        }

        public void addInOutValue(Object object, int type, StoredProcedureParameter.Type inOutType)
        {
            inOutValues.add(object);
            valueTypes.add(new Integer(type));
            inOutTypes.add(inOutType);
        }

        public Object[] getInOutValues()
        {
            return inOutValues.toArray();
        }

        public Integer[] getValueTypes()
        {
            return (Integer[]) valueTypes.toArray(new Integer[valueTypes.size()]);
        }

        public StoredProcedureParameter.Type[] getInOutTypes()
        {
            return (StoredProcedureParameter.Type[]) inOutTypes.toArray();
        }
    }

    private StoredProcedure procedure;
    private List params = new ArrayList();

    public StoredProcedureParameters(StoredProcedure parent)
    {
        setProcedure(parent);
    }

    /**
     * Gets the procedure object associated with this object
     * @return
     */
    public StoredProcedure getProcedure()
    {
        return procedure;
    }

    /**
     * Sets the procedure object associated with this object
     * @param procedure
     */
    public void setProcedure(StoredProcedure procedure)
    {
        this.procedure = procedure;
    }

    /**
     * Creates a stored procedure parameter
     * @return
     */
    public StoredProcedureParameter createParam()
    {
        return new StoredProcedureParameter(this);
    }

    /**
     * Adds a stored procedure parameter
     * @param param
     */
    public void addParam(StoredProcedureParameter param)
    {
        params.add(param);
        param.setIndex(params.size());
    }

    /**
     * Retrieve the bind parameters that would be applied to a prepared statement (useful for debugging).
     * @param cc The connection context
     * @return An object that holds all the bind parameters and associated SQL types
     * @throws java.sql.SQLException
     */
    public StoredProcedureParameters.ValueRetrieveContext retrieve(ConnectionContext cc) throws SQLException
    {
        StoredProcedureParameters.ValueRetrieveContext vrc = new StoredProcedureParameters.ValueRetrieveContext();

        if(params.size() == 0)
            return vrc;

        int paramsCount = params.size();
        for(int i = 0; i < paramsCount; i++)
            ((StoredProcedureParameter) params.get(i)).retrieve(vrc, cc);

        return vrc;
    }

    /**
     * Apply the parameters in this list to the given prepared statement.
     * @param cc The connection context
     * @param stmt The prepared statement
     * @return The index of the last parameter applied
     * @throws SQLException
     */
    public int apply(ConnectionContext cc, CallableStatement stmt) throws SQLException
    {
        StoredProcedureParameters.ValueApplyContext vac = new StoredProcedureParameters.ValueApplyContext();

        if(params.size() == 0)
            return 0;

        int paramsCount = params.size();
        for(int i = 0; i < paramsCount; i++)
        {
            ((StoredProcedureParameter) params.get(i)).apply(vac, cc, stmt);
        }

        return vac.getActiveParamNum();
    }

    public StoredProcedureParameter get(int index)
    {
        return (StoredProcedureParameter) params.get(index);
    }

    public int size()
    {
        return params.size();
    }

}
