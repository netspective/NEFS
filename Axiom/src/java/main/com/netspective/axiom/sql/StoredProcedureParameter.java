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
 * $Id: StoredProcedureParameter.java,v 1.1 2003-10-29 23:00:12 aye.thu Exp $
 */
package com.netspective.axiom.sql;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.Value;
import com.netspective.axiom.ConnectionContext;

import java.sql.Types;
import java.sql.SQLException;
import java.sql.CallableStatement;

/**
 * Class representing an in or out parameter of a callable statement object
 */
public class StoredProcedureParameter implements XmlDataModelSchema.ConstructionFinalizeListener
{
    public static final int OUT_PARAMETER = 0;
    public static final int IN_PARAMETER = 1;

    private StoredProcedureParameters parent;
    private String name;
    private ValueSource value;
    private int sqlType = Types.VARCHAR;
    private Class javaType = String.class;
    private int index;
    private int type;

    public StoredProcedureParameter(StoredProcedureParameters parent)
    {
        setParent(parent);
    }

    /**
     * Gets the type of parameter (in or out)
     * @return
     */
    public int getType()
    {
        return type;
    }

    /**
     * Sets the type of parameter ( in or Out)
     * @param type
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     *
     * @param pc
     * @param element
     * @param elementName
     * @throws DataModelException
     */
    public void finalizeConstruction(XdmParseContext pc, Object element, String elementName) throws DataModelException
    {
        if(value == null)
        {
            RuntimeException e = new RuntimeException(QueryParameter.class.getName() + " '" +
                    this.parent.getProcedure().getQualifiedName() + "' has no 'value' or 'values' attribute.");
            this.parent.getProcedure().getLog().error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Apply the in and out parameters to the callable statement object
     * @param vac
     * @param cc
     * @param stmt
     * @throws SQLException
     */
    public void apply(StoredProcedureParameters.ValueApplyContext vac, ConnectionContext cc, CallableStatement stmt) throws SQLException
    {
        if(sqlType != Types.ARRAY)
        {
            int paramNum = vac.getNextParamNum();
            if(sqlType == Types.VARCHAR)
            {
                if (getType() == IN_PARAMETER)
                    stmt.setObject(paramNum, value.getTextValue(cc));
                else if (getType() == OUT_PARAMETER)
                    stmt.registerOutParameter(paramNum, Types.VARCHAR);
            }
            else
            {

                switch(sqlType)
                {
                    case Types.INTEGER:
                        if (getType() == IN_PARAMETER)
                        {
                            Value sv = value.getValue(cc);
                            stmt.setInt(paramNum, sv.getIntValue());
                        }
                        else if (getType() == OUT_PARAMETER)
                            stmt.registerOutParameter(paramNum, Types.INTEGER);
                        break;

                    case Types.DOUBLE:
                        if (getType() == IN_PARAMETER)
                        {
                            Value sv = value.getValue(cc);
                            stmt.setDouble(paramNum, sv.getDoubleValue());
                        }
                        else if (getType() == OUT_PARAMETER)
                            stmt.registerOutParameter(paramNum, Types.DOUBLE);
                        break;
                }
            }
        }
        else
        {
            // this IN parameter is actually an array (OUT parameter won't have a value associated
            // with it.
            if (value != null && getType() == IN_PARAMETER)
            {
                String[] textValues = value.getTextValues(cc);
                for(int q = 0; q < textValues.length; q++)
                {
                    int paramNum = vac.getNextParamNum();
                    stmt.setObject(paramNum, textValues[q]);
                }
            }
        }
    }
    /**
     *
     * @param vrc
     * @param cc
     * @throws SQLException
     */
    public void retrieve(StoredProcedureParameters.ValueRetrieveContext vrc, ConnectionContext cc) throws SQLException
    {
        if(sqlType != Types.ARRAY)
        {
            if(sqlType == Types.VARCHAR)
            {
                vrc.addInOutValue(value != null ? value.getTextValue(cc) : null, sqlType, type);
            }
            else
            {
                switch(sqlType)
                {
                    case Types.INTEGER:
                        vrc.addInOutValue(value != null ? new Integer(value.getValue(cc).getIntValue()) : null, sqlType, type);
                        break;

                    case Types.DOUBLE:
                        vrc.addInOutValue(value != null ? new Double(value.getValue(cc).getDoubleValue()) : null, sqlType, type);
                        break;
                }
            }
        }
        else
        {
            if (value != null && type == IN_PARAMETER)
            {
                String[] textValues = value.getTextValues(cc);
                for(int q = 0; q < textValues.length; q++)
                    vrc.addInOutValue(textValues[q], Types.VARCHAR, type);
            }
        }
    }


    /**
     * Gets teh index of this parameter
     * @return
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index of this parameter
     * @param index
     */
    public void setIndex(int index)
    {
        this.index = index;
        if(name == null)
            setName("param-" + index);
    }

    /**
     * Gts the name of this parameter
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this parameter
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the parent collection object
     * @return
     */
    public StoredProcedureParameters getParent()
    {
        return parent;
    }

    /**
     * Sets the parent collection object
     * @param parent
     */
    public void setParent(StoredProcedureParameters parent)
    {
        this.parent = parent;
    }

    /**
     * Gets the equivalent Java type
     * @return
     */
    public Class getJavaType()
    {
        return javaType;
    }

    /**
     * Sets the equivalent Java type
     * @param javaType
     */
    public void setJavaType(Class javaType)
    {
        this.javaType = javaType;
    }

    /**
     * Gets the dynamic value for this parameter
     * @return
     */
    public ValueSource getValue()
    {
        return value;
    }

    /**
     * Sets the dynamic value for this parameter
     * @param value
     */
    public void setValue(ValueSource value)
    {
        this.value = value;
    }

}
