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
package com.netspective.axiom.sql;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.xdm.XdmParseContext;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.exception.DataModelException;

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
    /* configured value for this parameter */
    private ValueSource value;
    private QueryParameterType paramType;
    private int index;
    private Type type;
    /* declares whether or not the bind column is allowed to have a SQL NULL */
    private boolean allowNull = false;
    /* declares the name of the array type (e.g create or replace type NUM_ARRAY as table of number;) */
    private String typeName;


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
     * Gets the special TYPE defined in database
     *
     * @return
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * Sets the special TYPE defined in database
     * (e.g create or replace type NUM_ARRAY as table of number;)
     *
     * IMPORTANT: PLSQL types are known to exactly PLSQL and PLSQL alone. SQL types -- stored in the data dictionary --
     * are visible to all, usable by all. you must use a SQL type via create type.
     *
     * @param typeName
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
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
     * Apply the IN/OUT parameters of the callable statement object
     *
     * @param vac               the context in which the apply action is being performed
     * @param stmt              the statement object
     * @throws SQLException
     */
    public void apply(StoredProcedureParameters.ValueApplyContext vac, ConnectionContext cc, CallableStatement stmt) throws SQLException
    {
        int paramNum = vac.getNextParamNum();
        int jdbcType = getSqlType().getJdbcType();
        if (getType().getValueIndex() == Type.IN || getType().getValueIndex() == Type.IN_OUT)
        {
            String text = value.getTextValue(cc);
            switch (jdbcType)
            {
                case Types.VARCHAR:
                    // user override value if it exists
                    if (vac.hasOverrideValues() && vac.hasActiveParamOverrideValue())
                        text = (String) vac.getActiveParamOverrideValue();
                    if (allowNull && text == null)
                        stmt.setNull(paramNum, Types.VARCHAR);
                    else if (text != null)
                        stmt.setObject(paramNum, text);
                    else
                        log.warn("Parameter '" + getName() + "' was not bound. Value = " + text);
                    break;

                case Types.CHAR:
                    if (vac.hasOverrideValues() && vac.hasActiveParamOverrideValue())
                        text = (String) vac.getActiveParamOverrideValue();
                    if (allowNull && text == null)
                        stmt.setNull(paramNum, Types.CHAR);
                    else if (text != null)
                        stmt.setObject(paramNum, text.substring(0,1));
                    else
                        log.warn("Parameter '" + getName() + "' was not bound. Value = " + text);
                    break;

                case Types.INTEGER:
                    if (vac.hasOverrideValues() && vac.hasActiveParamOverrideValue())
                        text = vac.getActiveParamOverrideValue() != null ? vac.getActiveParamOverrideValue().toString() : null;
                    if (allowNull && text == null)
                        stmt.setNull(paramNum, Types.INTEGER);
                    else if (text != null)
                        stmt.setInt(paramNum, Integer.parseInt(text));
                    else
                        log.warn("Parameter '" + getName() + "' was not bound. Value = " + text);

                    break;

                case Types.DOUBLE:
                    if (vac.hasOverrideValues() && vac.hasActiveParamOverrideValue())
                        text = vac.getActiveParamOverrideValue() != null ? vac.getActiveParamOverrideValue().toString() : null;
                    if (allowNull && text == null)
                        stmt.setNull(paramNum, Types.DOUBLE);
                    else if (text != null)
                        stmt.setDouble(paramNum, Double.parseDouble(text));
                    else
                        log.warn("Parameter '" + getName() + "' was not bound. Value = " + text);

                    break;

                case Types.ARRAY:
                    // Arrays are quite tricky. Right now, this is supporting String arrays only.
                    // TODO: Support integer and float arrays also
                    String[] textValues = value.getTextValues(cc);
                    if (vac.hasOverrideValues() && vac.hasActiveParamOverrideValue())
                        textValues = (String[]) vac.getActiveParamOverrideValue();
                    applyInArrayValue(cc, stmt, paramNum, textValues);
                    break;
                default:
                    log.warn("Unknown JDBC type for parameter '" + getName() + "' (index=" +
                            paramNum + ") of stored procedure '" + parent.getProcedure() + "'.");
                    break;
            }
        }
        if (getType().getValueIndex() == Type.OUT || getType().getValueIndex() == Type.IN_OUT)
        {
            String identifier = getSqlType().getIdentifier();
            // result sets are returned differently for different vendors
            if (identifier.equals(QueryParameterType.RESULTSET_IDENTIFIER))
                stmt.registerOutParameter(paramNum, getVendorSpecificResultSetType(cc));
            else
                stmt.registerOutParameter(paramNum, jdbcType);
        }
    }

    /**
     *  Applies an array object as an IN parameter
     *
     * @param cc        Connection context
     * @param stmt      Callable statement object
     * @param paramNum  the index
     * @param array     the java array object
     */
    private void applyInArrayValue(ConnectionContext cc, CallableStatement stmt, int paramNum, Object[] array)
    {
        // TODO: This is the initial implementation. NOT TESTED YET. 01/13/2004 AT
        try
        {
            if (isOracleDriver(cc))
            {
                // doing the following ORACLE specific calls
                // oracle.sql.ArrayDescriptor descriptor =  oracle.sql.ArrayDescriptor.createDescriptor( "NUM_ARRAY", conn );
                // oracle.sql.ARRAY array_to_pass = new oracle.sql.ARRAY( descriptor, conn, javaArrray );
                // stmt.setARRAY( paramNum, array_to_pass );

                // create the array descriptor
                Class arrayDescriptorClass = Class.forName("oracle.sql.ArrayDescriptor");
                Method createDescriptor = arrayDescriptorClass.getMethod("createDescriptor", new Class[] {String.class, Connection.class});
                Object descriptor = createDescriptor.invoke(null, new Object[] {getTypeName(), cc.getConnection()});

                // create the array to pass to the database
                Class arrayClass = Class.forName("oracle.sql.ARRAY");
                Constructor arrayConstructor = arrayClass.getConstructor(new Class[] {arrayDescriptorClass, Connection.class,
                                                                                      getSqlType().getJavaClass()});
                arrayConstructor.newInstance(new Object[] {descriptor, cc.getConnection(), array});

                Class oracleStmt = Class.forName("oracle.jdbc.OracleCallableStatement");
                Method setArrayMethod = oracleStmt.getMethod("setARRAY", new Class[] {int.class, arrayClass});
                setArrayMethod.invoke(stmt, new Object[] {new Integer(paramNum), array});
            }
        }
        catch(Exception e)
        {
            log.error("Failed to apply the ARRAY IN parameter at index "+ paramNum + " in stored procedure " +
                    "'" + parent.getProcedure() + "'.");
        }
    }

    /**
     * Checks to see if the connection context is connected to an Oracle database
     * @param cc
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    private boolean isOracleDriver(ConnectionContext cc) throws NamingException, SQLException
    {
        String driverName = cc.getConnection().getMetaData().getDriverName();
        return (driverName.indexOf("Oracle") != -1) ? true : false;
    }

    /**
     * Database vendors implement the process of returning result set from a stored procedure
     * differently. This method will try to detect the database vendor through the driver name and
     * register the correct out parameter type specific to the vendor.
     * If it isn't able to guess the database from the drive name, it will return <code>Types.OTHER</code>
     * as the result set type.
     * @param cc  connection context object
     * @return  the JDBC type code defined by <code>java.sql.Types</code>.
     */
    public int getVendorSpecificResultSetType(ConnectionContext cc)
    {
        // set the default type
        int sqlType = Types.OTHER;
        try
        {
            if (isOracleDriver(cc))
            {
                // ORACLE DRIVER
                Class oClass = Class.forName("oracle.jdbc.driver.OracleTypes");
                sqlType = oClass.getField("CURSOR").getInt(null);
            }
        }
        catch (Exception e)
        {
            log.error("Failed to get the column type for the result set cursor for stored procedure" +
                    "'" + parent.getProcedure() + "'.");
        }
        return sqlType;
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
     * Gets the extracted value
     * @param vc the value context
     * @return
     */
    public Object getExtractedValue(ValueContext vc)
    {
       return value.getValue(vc).getValue();
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

        // result sets are special
        if (identifier.equals(QueryParameterType.RESULTSET_IDENTIFIER))
        {
            ResultSet rs = (ResultSet) stmt.getObject(index);
            QueryResultSet qrs = new QueryResultSet(getParent().getProcedure(), cc, rs);
            value.getValue(cc).setValue(qrs);
            return;
        }

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
                value.getValue(cc).setValue(stmt.getObject(index));
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
