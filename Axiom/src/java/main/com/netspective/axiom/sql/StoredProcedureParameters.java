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
 * $Id: StoredProcedureParameters.java,v 1.5 2003-11-11 23:08:37 aye.thu Exp $
 */

package com.netspective.axiom.sql;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.axiom.ConnectionContext;

import javax.naming.NamingException;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * List class for keeping track of the stored procedure parameters
 */
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
    private List inParams = new ArrayList();
    private List outParams = new ArrayList();
    private int resultsetParamIndex = -1;

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
     * Adds a stored procedure parameter to the parameter list and if the param is a result set, the
     * position of the param in the list is saved.
     * @param param
     */
    public void addParam(StoredProcedureParameter param)
    {
        params.add(param);
        param.setIndex(params.size());
        int inoutType = param.getType().getValueIndex();
        if (inoutType == StoredProcedureParameter.Type.OUT ||
            inoutType == StoredProcedureParameter.Type.IN_OUT)
            outParams.add(param);
        if (inoutType == StoredProcedureParameter.Type.IN ||
            inoutType == StoredProcedureParameter.Type.IN_OUT)
            inParams.add(param);

    }

    /**
     * Gets the index of an out parameter which is a result set
     * @return
     */
    public int getResultSetPrameterIndex()
    {
        return resultsetParamIndex;
    }

    /**
     * Gets the result set out parameter
     * @return Null if there is no result set out parameter
     */
    public StoredProcedureParameter getResultSetParameter()
    {
        for (int i=0; i < outParams.size(); i++)
        {
            StoredProcedureParameter spp = (StoredProcedureParameter)outParams.get(i);
            if (spp.getSqlIdentifierType().equals(QueryParameterType.RESULTSET_IDENTIFIER))
                return spp;
        }
        return null;
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
    public int apply(ConnectionContext cc, CallableStatement stmt) throws SQLException, NamingException
    {
        StoredProcedureParameters.ValueApplyContext vac = new StoredProcedureParameters.ValueApplyContext();

        if(params.size() == 0)
            return 0;

        int paramsCount = params.size();
        for(int i = 0; i < paramsCount; i++)
        {
            ((StoredProcedureParameter) params.get(i)).apply(vac, cc, stmt);
        }
        /*
        for (int i = 0; i < inParams.size(); i++)
        {
            ((StoredProcedureParameter) inParams.get(i)).apply(vac, cc, stmt);

        }
        for (int i = 0; i < outParams.size(); i++)
        {
            ((StoredProcedureParameter) outParams.get(i)).apply(vac, cc, stmt);

        }
        */
        return vac.getActiveParamNum();
    }

    /**
     * Retrieve the values of the parameters in this list which are OUT parameters
     */
    public void extract(ConnectionContext cc, CallableStatement stmt) throws SQLException
    {
        int paramsCount = params.size();
        for(int i = 0; i < paramsCount; i++)
        {
            StoredProcedureParameter spp = (StoredProcedureParameter) params.get(i);
            if (spp.getType().getValueIndex() == StoredProcedureParameter.Type.OUT ||
                spp.getType().getValueIndex() == StoredProcedureParameter.Type.IN_OUT)
            {
                spp.extract(cc, stmt);
            }
        }
    }

    /**
     * Gets the stored procedure parameter by index
     * @param index
     * @return
     */
    public StoredProcedureParameter get(int index)
    {
        return (StoredProcedureParameter) params.get(index);
    }

    /**
     * Gets the total number of parameters
     * @return
     */
    public int size()
    {
        return params.size();
    }

    public StoredProcedureParameter[] getIns()
    {
        return getByType(StoredProcedureParameter.Type.IN);
    }

    public StoredProcedureParameter[] getOuts()
    {
        return getByType(StoredProcedureParameter.Type.OUT);
    }

    public StoredProcedureParameter[] getInOuts()
    {
        return getByType(StoredProcedureParameter.Type.IN_OUT);
    }

    /**
     * Gets an array of stored procedures by their type. In/out parameters will satisfy
     * both in and out criterias.
     * @param type
     * @return
     */
    public StoredProcedureParameter[] getByType(int type)
    {
        ArrayList list = new ArrayList();
        int paramsCount = params.size();
        if (type == StoredProcedureParameter.Type.IN)
        {
            // this will get all IN parameters and also IN/OUT params
            for(int i = 0; i < paramsCount; i++)
            {
                StoredProcedureParameter spParameter = (StoredProcedureParameter) params.get(i);
                if (spParameter.getType().getValueIndex() == StoredProcedureParameter.Type.IN ||
                    spParameter.getType().getValueIndex() == StoredProcedureParameter.Type.IN_OUT)
                {
                    list.add(spParameter);
                }
            }
        }
        else if (type == StoredProcedureParameter.Type.OUT)
        {
            // this will get all OUT parameters and also IN/OUT params
            for(int i = 0; i < paramsCount; i++)
            {
                StoredProcedureParameter spParameter = (StoredProcedureParameter) params.get(i);
                if (spParameter.getType().getValueIndex() == StoredProcedureParameter.Type.OUT ||
                    spParameter.getType().getValueIndex() == StoredProcedureParameter.Type.IN_OUT)
                {
                    list.add(spParameter);
                }
            }
        }
        else if (type == StoredProcedureParameter.Type.IN_OUT)
        {
            // this will get only IN/OUT params
            for(int i = 0; i < paramsCount; i++)
            {
                StoredProcedureParameter spParameter = (StoredProcedureParameter) params.get(i);
                if (spParameter.getType().getValueIndex() == StoredProcedureParameter.Type.IN_OUT)
                {
                    list.add(spParameter);
                }
            }
        }
        else
            return null;

        return (StoredProcedureParameter[]) list.toArray();

    }

}
