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
 * $Id: StoredProcedureParameter.java,v 1.2 2003-10-31 03:35:12 aye.thu Exp $
 */
package com.netspective.axiom.sql;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.axiom.ConnectionContext;

import java.sql.Types;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.Map;
import java.util.HashMap;

/**
 * Class representing an in or out parameter of a callable statement object
 */
public class StoredProcedureParameter implements XmlDataModelSchema.ConstructionFinalizeListener
{
    public static class Type extends XdmEnumeratedAttribute
    {
        public static final int IN = 0;
        public static final int OUT = 1;
        public static final int IN_AND_OUT = 2;

        public static final String[] VALUES = new String[] { "in", "out", "in-out" };

        public Type()
        {
            super();
        }

        public Type(int index)
        {
            super(index);
        }

        public String[] getValues()
        {
            return VALUES;
        }
    }

    private StoredProcedureParameters parent;
    private String name;
    private ValueSource value;
    private int sqlType = Types.VARCHAR;
    private Class javaType = String.class;
    private int index;
    private Type type;

    public StoredProcedureParameter(StoredProcedureParameters parent)
    {
        setParent(parent);
    }

    public int getSqlTypeCode()
    {
        return sqlType;
    }

    public void setSqlTypeCode(int type)
    {
        this.sqlType = type;
    }

    public void setSqlType(QueryParameterTypeEnumeratedAttribute sqlType)
    {
        String paramTypeName = sqlType.getValue();
        if(paramTypeName != null)
        {
            QueryParameterType type = QueryParameterType.get(paramTypeName);
            if(type == null)
                throw new RuntimeException("param type '" + paramTypeName + "' is invalid for param '"+
                        getName() +"' in stored procedure '" + parent.getProcedure().getQualifiedName() + "'");
            setSqlTypeCode(type.getJdbcType());
            setJavaType(type.getJavaClass());
        }
    }

    /**
     * Gets the type of parameter (IN, OUT, or IN/OUT)
     * @return
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Sets the type of parameter ( in or Out)
     * @param type
     */
    public void setType(Type type)
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
        int paramNum = vac.getNextParamNum();
        Value sv = value.getValue(cc);
        if (getType().getValueIndex() == Type.IN)
        {
            switch (sqlType)
            {
                case Types.VARCHAR:
                    stmt.setObject(paramNum, value.getTextValue(cc));
                    break;
                case Types.INTEGER:
                    stmt.setInt(paramNum, sv.getIntValue());
                    break;
                case Types.DOUBLE:
                    stmt.setDouble(paramNum, sv.getDoubleValue());
                    break;
                case Types.ARRAY:
                    String[] textValues = value.getTextValues(cc);
                    for(int q = 0; q < textValues.length; q++)
                    {
                        paramNum = vac.getNextParamNum();
                        stmt.setObject(paramNum, textValues[q]);
                    }
                    break;
                default:
                    // TODO: Need to handle all the types??
                    break;
            }
        }
        else if (getType().getValueIndex() == Type.OUT)
        {
            // sqlType MUST be of java.sqlTypes value always!
            stmt.registerOutParameter(paramNum, sqlType);
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
        // TODO: This needs to be tested.. no checking for stored procedure situation yet
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
            if (value != null && type.getValueIndex() == Type.IN)
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

    public boolean isListType()
    {
        return sqlType == Types.ARRAY;
    }

    /**
     * Appends a list of bind parameters and their respective information used for debugging to the buffer
     * @param text
     * @param vc
     * @param terminator
     */
    public void appendBindText(StringBuffer text, ValueContext vc, String terminator)
    {
        text.append("["+ index +"]");
        if(sqlType != Types.ARRAY)
        {
            Object ov = value.getValue(vc);
            text.append(value.getSpecification().getSpecificationText());
            text.append(" = ");
            text.append(((Value)ov).getValueForSqlBindParam());
            text.append(" (java: ");
            text.append(ov != null ? ov.getClass().getName() : "<NULL>");
            text.append(", sql: ");
            text.append(sqlType);
            text.append(", ");
            text.append(QueryParameterType.get(sqlType));
            text.append(")");
        }
        else
        {
            text.append(value.getSpecification().getSpecificationText());
            text.append(" = ");

            String[] textValues = value.getTextValues(vc);
            if(value != null)
            {
                for(int v = 0; v < textValues.length; v++)
                {
                    if(v > 0)
                        text.append(", ");
                    text.append("'" + textValues[v] + "'");
                }
            }
            else
            {
                text.append("null");
            }

            text.append(" (text list)");
        }
        if(terminator != null) text.append(terminator);
    }
}
