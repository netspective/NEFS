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
 * $Id: StoredProcedureParameter.java,v 1.7 2003-11-10 23:02:02 aye.thu Exp $
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
import java.sql.Clob;
import java.sql.Array;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class representing an in or out parameter of a callable statement object
 */
public class StoredProcedureParameter implements XmlDataModelSchema.ConstructionFinalizeListener
{
    public static class Type extends XdmEnumeratedAttribute
    {
        public static final int IN = 0;
        public static final int OUT = 1;
        public static final int IN_OUT = 2;

        public static final String IN_NAME = "in";
        public static final String OUT_NAME = "out";
        public static final String IN_OUT_NAME = "in-out";

        public static final String[] VALUES = new String[] { IN_NAME, OUT_NAME, IN_OUT_NAME };

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

    private Log log = LogFactory.getLog(StoredProcedureParameter.class);
    private StoredProcedureParameters parent;
    private String name;
    private ValueSource value;
    //private int sqlType = Types.VARCHAR;
    //private Class javaType = String.class;
    private QueryParameterType paramType;
    private int index;
    private Type type;

    public StoredProcedureParameter(StoredProcedureParameters parent)
    {
        setParent(parent);
    }

    /**
     * Sets the type of value the parameter will contain
     * @param sqlType
     */
    public void setSqlType(QueryParameterTypeEnumeratedAttribute sqlType)
    {
        String paramTypeName = sqlType.getValue();
        if(paramTypeName != null)
        {
            paramType = QueryParameterType.get(paramTypeName);
            if(paramType == null)
                throw new RuntimeException("param type '" + paramTypeName + "' is invalid for param '"+
                        getName() +"' in stored procedure '" + parent.getProcedure().getQualifiedName() + "'");
        }
    }

    /**
     * Gets the parameter's value type object
     * @return
     */
    public QueryParameterType getSqlType()
    {
        return paramType;
    }

    /**
     * Gets the parameters java data type name
     * @return
     */
    public String getSqlIdentifierType()
    {
        return (paramType != null ? paramType.getIdentifier(): null);
    }

    /**
     * Gets the parameter's JDBC data type
     * @return
     */
    public int getSqlJdbcType()
    {
        return (paramType != null ? paramType.getJdbcType() : -999999);
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
        // The In, out, or in/out type MUST be defined
        if(type == null)
        {
            RuntimeException e = new RuntimeException("Parameter '" + getName() +
                    "'  for stored procedure '" +
                    this.parent.getProcedure().getQualifiedName() + "' has no 'type' attribute.");
            this.parent.getProcedure().getLog().error(e.getMessage(), e);
            throw e;
        }
        if(value == null)
        {
            RuntimeException e = new RuntimeException("Parameter '" + getName() +
                    "'  for stored procedure '" +
                    this.parent.getProcedure().getQualifiedName() + "' has no 'value' or 'values' attribute.");
            this.parent.getProcedure().getLog().error(e.getMessage(), e);
            throw e;
        }
        // NOTE: The Sql Type MUST be defined, if it is not it will default to VARCHAR!
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
        int jdbcType = getSqlType().getJdbcType();
        if (getType().getValueIndex() == Type.IN || getType().getValueIndex() == Type.IN_OUT)
        {
            switch (jdbcType)
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
                    log.warn("Unknown JDBC type for parameter '" + getName() + "' (index=" +
                            paramNum + ") of stored procedure '" + parent.getProcedure() + "'.");
                    break;
            }
        }
        if (getType().getValueIndex() == Type.OUT || getType().getValueIndex() == Type.IN_OUT)
        {
            // jdbcType MUST be of java.sql.Types value always!
            System.out.println("OUT>> " + paramNum + " " + jdbcType);
            stmt.registerOutParameter(paramNum, jdbcType);
        }
    }

    /**
     * Checks to see if this parameter is available as an Output type
     * @return
     */
    public boolean isOutType()
    {
        return (type.getValueIndex() == Type.OUT || type.getValueIndex() == Type.IN_OUT) ? true: false;
    }


    /**
     * Extract the OUT parameter values from the callable statment and
     * assign them to the value of the parameter.
     * @param cc
     * @param stmt
     * @throws SQLException
     */
    public void extract(ConnectionContext cc, CallableStatement stmt) throws SQLException
    {
        if (getType().getValueIndex() == StoredProcedureParameter.Type.IN)
            return;

        int index = this.getIndex();
        QueryParameterType paramType = getSqlType();
        int jdbcType = paramType.getJdbcType();
        String identifier = paramType.getIdentifier();

        switch (jdbcType)
        {
            case Types.VARCHAR:
                value.getValue(cc).setTextValue(stmt.getString(index));
                break;
            case Types.INTEGER:
                value.getValue(cc).setValue(new Integer(stmt.getInt(index)));
                break;
            case Types.DOUBLE:
                value.getValue(cc).setValue(new Double(stmt.getDouble(index)));
                break;
            case Types.CLOB:
                Clob clob = stmt.getClob(index);
                value.getValue(cc).setTextValue(clob.getSubString(1,(int) clob.length()));
                break;
            case java.sql.Types.ARRAY:
                Array array = stmt.getArray(index);
                value.getValue(cc).setValue(array);
                break;
            case java.sql.Types.BIGINT:
                long bigint = stmt.getLong(index);
                value.getValue(cc).setValue(new Long(bigint));
                break;
            case java.sql.Types.BINARY:
                value.getValue(cc).setTextValue(new String(stmt.getBytes(index)));
                break;
            case java.sql.Types.BIT:
                boolean bit = stmt.getBoolean(index);
                value.getValue(cc).setValue(new Boolean(bit));
            case java.sql.Types.BLOB:
                value.getValue(cc).setValue(stmt.getBlob(index));
                break;
            case java.sql.Types.CHAR:
                value.getValue(cc).setTextValue(stmt.getString(index));
                break;
            case java.sql.Types.DATE:
                value.getValue(cc).setValue(stmt.getDate(index));
                break;
            case java.sql.Types.DECIMAL:
                value.getValue(cc).setValue(stmt.getBigDecimal(index));
                break;
            case java.sql.Types.DISTINCT:
                value.getValue(cc).setValue(stmt.getObject(index));
                break;
            case java.sql.Types.FLOAT:
                value.getValue(cc).setValue(new Float(stmt.getFloat(index)));
                break;
            case java.sql.Types.JAVA_OBJECT:
                value.getValue(cc).setValue(stmt.getObject(index));
                break;
            case java.sql.Types.LONGVARBINARY:
                value.getValue(cc).setTextValue(new String(stmt.getBytes(index)));
                break;
            case java.sql.Types.LONGVARCHAR:
                value.getValue(cc).setTextValue(stmt.getString(index));
                break;
            //case java.sql.Types.NULL:
            //    value.getValue(cc).setValue(null);
            //    break;
            case java.sql.Types.NUMERIC:
                value.getValue(cc).setValue(stmt.getBigDecimal(index));
                break;
            case java.sql.Types.OTHER:
                if (identifier.equals(QueryParameterType.RESULTSET_IDENTIFIER))
                {
                    ResultSet rs = (ResultSet) stmt.getObject(index);
                    QueryResultSet qrs = new QueryResultSet(getParent().getProcedure(), cc, rs);
                    value.getValue(cc).setValue(qrs);
                }
                else
                {
                    value.getValue(cc).setValue(stmt.getObject(index));
                }
                break;
            case java.sql.Types.REAL:
                value.getValue(cc).setValue(new Float(stmt.getFloat(index)));
                break;
            //case java.sql.Types.REF:
            //    Ref ref = stmt.getRef(index);
            //    break;
            case java.sql.Types.SMALLINT:
                short sh = stmt.getShort(index);
                value.getValue(cc).setValue(new Short(sh));
                break;
            case java.sql.Types.STRUCT:
                value.getValue(cc).setValue(stmt.getObject(index));
                break;
            case java.sql.Types.TIME:
                value.getValue(cc).setValue(stmt.getTime(index));
                break;
            case java.sql.Types.TIMESTAMP:
                value.getValue(cc).setValue(stmt.getTimestamp(index));
                break;
            case java.sql.Types.TINYINT:
                byte b = stmt.getByte(index);
                value.getValue(cc).setValue(new Byte(b));
                break;
            case java.sql.Types.VARBINARY:
                 value.getValue(cc).setValue(stmt.getBytes(index));
                break;
            default:
                throw new RuntimeException("Unknown JDBC Type set for stored procedure parameter '" +
                        this.getName() + "'.");
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
        int jdbcType = getSqlType().getJdbcType();
        if(jdbcType != Types.ARRAY)
        {
            if(jdbcType == Types.VARCHAR)
            {
                vrc.addInOutValue(value != null ? value.getTextValue(cc) : null, jdbcType, type);
            }
            else
            {
                switch(jdbcType)
                {
                    case Types.INTEGER:
                        vrc.addInOutValue(value != null ? new Integer(value.getValue(cc).getIntValue()) : null, jdbcType, type);
                        break;

                    case Types.DOUBLE:
                        vrc.addInOutValue(value != null ? new Double(value.getValue(cc).getDoubleValue()) : null, jdbcType, type);
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
     * Gets the name of this parameter
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
        return paramType != null  && paramType.getJdbcType() == Types.ARRAY ? true : false;
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
        int jdbcType = getSqlType().getJdbcType();
        if(jdbcType != Types.ARRAY)
        {
            Object ov = value.getValue(vc);
            text.append(value.getSpecification().getSpecificationText());
            text.append(" = ");
            text.append(((Value)ov).getValueForSqlBindParam());
            text.append(" (java: ");
            text.append(ov != null ? ov.getClass().getName() : "<NULL>");
            text.append(", sql: ");
            text.append(jdbcType);
            text.append(", ");
            text.append(QueryParameterType.get(jdbcType));
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
