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
 */


package com.netspective.axiom.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.commons.text.ExpressionText;
import com.netspective.commons.text.ValueSourceOrJavaExpressionText;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XmlDataModelSchema;

/**
 * Class for handling stored procedure calls
 *
 * @author Aye Thu
 * @version $Id: StoredProcedure.java,v 1.12 2004-06-18 22:29:33 shahid.shah Exp $
 */
public class StoredProcedure
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();
    public static final String LISTPARAM_PREFIX = "param-list:";
    public static long queryNumber = 0;

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] { "sql-dynamic", "sql-text-has-expressions" });
        XML_DATA_MODEL_SCHEMA_OPTIONS.setPcDataHandlerMethodName("appendSqlText");
    }

    private class StoredProcedureSqlExpressionText extends ValueSourceOrJavaExpressionText
    {
        public StoredProcedureSqlExpressionText(String staticExpr, Map vars)
        {
            super(staticExpr, vars);
        }

        public StoredProcedureSqlExpressionText(String staticExpr)
        {
            super(staticExpr);
        }

        protected String getReplacement(ValueContext vc, String entireText, String replaceToken)
        {
            if(replaceToken.startsWith(LISTPARAM_PREFIX)) // format is param:#
            {
                StringBuffer sb = new StringBuffer();
                try
                {
                    int paramNum = Integer.parseInt(replaceToken.substring(LISTPARAM_PREFIX.length()));
                    if(paramNum >= 0 && paramNum < parameters.size())
                    {
                        StoredProcedureParameter param = parameters.get(paramNum);
                        if(!param.isListType())
                            throw new RuntimeException("Stored Procedure '"+ getNameForMapKey() +
                                    "': only list parameters may be specified here (param '" + paramNum + "')");

                        ValueSource source = param.getValue();
                        String[] values = source.getTextValues(vc);

                        for(int q = 0; q < values.length; q++)
                        {
                            if(q > 0)
                                sb.append(", ");
                            sb.append("?");
                        }
                    }
                    else
                        throw new RuntimeException("Stored Procedure '"+ getQualifiedName() +"': parameter '" + paramNum + "' does not exist");
                }
                catch(Exception e)
                {
                    throw new NestableRuntimeException(e);
                }

                return sb.toString();
            }
            else
                return super.getReplacement(vc, entireText, replaceToken);
        }
    }

    private class StoredProcedureDbmsSqlTexts extends DbmsSqlTexts
    {
        public StoredProcedureDbmsSqlTexts()
        {
            super(StoredProcedure.this, "stored-procedure");
        }

        public ExpressionText createExpr(String sql)
        {
            return new StoredProcedureSqlExpressionText(sql, createVarsMap());
        }
    }

    /* log */
    private Log log = LogFactory.getLog(StoredProcedure.class);
    /* the name space to which this stored procedure call belongs to */
    private StoredProceduresNameSpace nameSpace;
    /* the name of the stored procedure call defined in the XML */
    private String spName;
    /* bind parameters defined for the stored procedure call */
    private StoredProcedureParameters parameters;
    /* the datasource associated with the stored procedure call */
    private ValueSource dataSourceId;
    /* execution log associated witht the stored procedure call */
    private QueryExecutionLog execLog = new QueryExecutionLog();
    private StoredProcedure.StoredProcedureDbmsSqlTexts sqlTexts = new StoredProcedure.StoredProcedureDbmsSqlTexts();

    /* the name of the procedure/function in the database. This is OPTIONAL */
    private String procedureName;

    public StoredProcedure()
    {
        queryNumber++;
        setName(this.getClass().getName() + "-" + queryNumber);
    }

    public StoredProcedure(StoredProceduresNameSpace nameSpace)
    {
        queryNumber++;
        setNameSpace(nameSpace);
        setName(this.getClass().getName() + "-" + queryNumber);
    }

    public StoredProceduresNameSpace getNameSpace()
    {
        return nameSpace;
    }

    /**
     * Gets the actual name of the stored procedure defined in the database
     * @return
     */
    public String getProcedureName()
    {
        return procedureName;
    }

    /**
     * Sets the actual name of the stored procedure defined in the database. This name
     * is used to dynamically investigate in/out types of the parameters of the stored
     * procedure.
     * @param name
     */
    public void setProcedureName(String name)
    {
        procedureName = name;
    }

    public void setNameSpace(StoredProceduresNameSpace pkg)
    {
        this.nameSpace = pkg;
    }

    public String getNameForMapKey()
    {
        return translateNameForMapKey(getQualifiedName());
    }

    public static String translateNameForMapKey(String name)
    {
       return name != null ? name.toUpperCase() : null;
    }

    public Log getLog()
    {
       return log;
    }

    public QueryExecutionLog getExecLog()
    {
        return execLog;
    }

    public String getQualifiedName()
    {
        return nameSpace != null ? nameSpace.getNameSpaceId() + "." + spName : spName;
    }

    public String getName()
    {
        return spName;
    }

    public void setName(String name)
    {
        this.spName = name;
        log = LogFactory.getLog(getClass().getName() + "." + this.getQualifiedName());
    }

    /**
     * Gets the IN/OUT parameters registered for this stored procedure call
     * @return
     */
    public StoredProcedureParameters getParams()
    {
        return parameters;
    }

    public StoredProcedureParameters createParams()
    {
        return new StoredProcedureParameters(this);
    }

    public void addParams(StoredProcedureParameters params)
    {
        this.parameters = params;
    }

    /**
     * Gets a parameter by its index
     * @param index
     * @return
     */
    public StoredProcedureParameter getParam(int index)
    {
        return  parameters.get(index);
    }

    public ValueSource getDataSrc()
    {
        return dataSourceId;
    }

    public void setDataSrc(ValueSource dataSourceId)
    {
        this.dataSourceId = dataSourceId;
    }

    public DbmsSqlText createSql()
    {
        return sqlTexts.create();
    }

    public void addSql(DbmsSqlText text)
    {
        sqlTexts.add(text);
    }

    public void appendSqlText(String sql)
    {
        DbmsSqlText sqlText = sqlTexts.getByDbms(DatabasePolicies.DBPOLICY_ANSI);
        if(sqlText == null)
            setSqlText(sql);
        else
            sqlText.addText(sql);
    }

    protected void setSqlText(String sql)
    {
        DbmsSqlText text = sqlTexts.create();
        text.setSql(sql);
        sqlTexts.add(text);
    }

    public StoredProcedureDbmsSqlTexts getSqlTexts()
    {
        return sqlTexts;
    }

    public String getSqlText(ConnectionContext cc) throws NamingException, SQLException
    {
        DbmsSqlText sqlText = sqlTexts.getByDbmsOrAnsi(cc.getDatabasePolicy());
        return sqlText != null ? sqlText.getSql(cc) : null;
    }

    /**
     * Gets the stored procedure's metadata information from the database. This will search
     * all available catalogs and schemas. This method will ONLY return the metadata of the
     * stored procedure only when the <i>procedure-name</i> attribute is set in the XML declaration.
     * @param cc
     * @throws NamingException
     * @throws SQLException
     */
    public String getMetaData(ConnectionContext cc) throws NamingException, SQLException
    {
        // TODO : Using this metadata, we can determine what variables are in and out so that the developer doesn't even have to set it in XML
        // but currently the procedure-name attribute isn't required but the 'type' attribute is required. If we go the
        // metadata route we need to change some handling to accept setting the 'type' and if it's not set, we can use
        // the metadata to get the param type
        StringBuffer sb = new StringBuffer();
        if (procedureName != null && procedureName.length() > 0)
        {
            // Get DatabaseMetaData
            Connection connection = cc.getConnection();
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet rs = dbmd.getProcedureColumns(null, null, procedureName, "%");
            // Printout table data
            while(rs.next())
            {
                // Get procedure metadata
                String dbProcedureCatalog   = rs.getString(1);
                String dbProcedureSchema    = rs.getString(2);
                String dbProcedureName      = rs.getString(3);
                String dbColumnName         = rs.getString(4);
                short  dbColumnReturn       = rs.getShort(5);
                String dbColumnReturnTypeName = rs.getString(7);
                int    dbColumnPrecision      = rs.getInt(8);
                int    dbColumnByteLength     = rs.getInt(9);
                short  dbColumnScale          = rs.getShort(10);
                short  dbColumnRadix          = rs.getShort(11);
                String dbColumnRemarks        = rs.getString(13);
                // Interpret the return type (readable for humans)
                String procReturn;
                switch(dbColumnReturn)
                {
                    case DatabaseMetaData.procedureColumnIn:
                        procReturn = "In";
                        break;
                    case DatabaseMetaData.procedureColumnOut:
                        procReturn = "Out";
                        break;
                    case DatabaseMetaData.procedureColumnInOut:
                        procReturn = "In/Out";
                        break;
                    case DatabaseMetaData.procedureColumnReturn:
                        procReturn = "return value";
                        break;
                    case DatabaseMetaData.procedureColumnResult:
                        procReturn = "return ResultSet";
                    default:
                        procReturn = "Unknown";
                }
                // Printout
                sb.append("Procedure: " + dbProcedureCatalog + "." + dbProcedureSchema
                                 + "." + dbProcedureName);
                sb.append("   ColumnName [ColumnType(ColumnPrecision)]: " + dbColumnName
                                 + " [" + dbColumnReturnTypeName + "(" + dbColumnPrecision + ")]");
                sb.append("   ColumnReturns: " + procReturn + "(" + dbColumnReturnTypeName + ")");
                sb.append("   Radix: " + dbColumnRadix + ", Scale: " + dbColumnScale);
                sb.append("   Remarks: " + dbColumnRemarks);
            }
            rs.close();
            connection.close();
        }
        return sb.toString();
    }

    public String createExceptionMessage(ConnectionContext cc, int[] overrideIndexes, Object[] overrideValues) throws NamingException, SQLException
    {
        StringBuffer text = new StringBuffer();

        text.append("Stored Procedure id = ");
        text.append(getQualifiedName());
        text.append("\n");
        text.append(getSqlText(cc));
        text.append("\n");
        if(overrideIndexes != null)
        {
            text.append("\nBind Parameters (overridden in method):\n");
            for(int i = 0; i < overrideIndexes.length; i++)
            {
                text.append("["+ overrideIndexes[i]  +"] ");
                text.append(overrideValues[i]);
                if(overrideValues[i] != null)
                    text.append(" ("+ overrideValues[i].getClass().getName() +")");
                text.append("\n");
            }
        }
        else if(parameters != null)
        {
            text.append("\nBind Parameters (in query):\n");
            for(int i = 0; i < parameters.size(); i++)
                (parameters.get(i)).appendBindText(text, cc, "\n");
            text.append("\n");
        }
        return text.toString();
    }

    /**
     * Appends tracing messages to the executions log
     * @param cc
     * @param overrideIndexes       parameter indexes to override
     * @param overrideValues        parameter override values
     * @throws NamingException
     * @throws SQLException
     */
    public void trace(ConnectionContext cc, int[] overrideIndexes, Object[] overrideValues) throws NamingException, SQLException
    {
        StringBuffer traceMsg = new StringBuffer();
        traceMsg.append(QueryExecutionLogEntry.class.getName() + " '"+ getQualifiedName() +"' at "+
                cc.getContextLocation() +"\n");
        traceMsg.append(getSqlText(cc));
        if(overrideIndexes != null)
        {
            for(int i = 0; i < overrideIndexes.length; i++)
            {
                traceMsg.append("["+ overrideIndexes[i] +"] ");
                traceMsg.append(overrideValues[i]);
                if(overrideValues[i] != null)
                    traceMsg.append(" (" + overrideValues[i].getClass().getName() + ")");
                traceMsg.append("\n");
            }
        }
        else
        {
            StoredProcedureParameters params = getParams();
            if(params != null)
            {
                for(int i = 0; i < params.size(); i++)
                    (params.get(i)).appendBindText(traceMsg, cc, "\n");
            }
        }
        log.trace(traceMsg);
    }

    /**
     * Executes the stored procedure without any statistical logging
     * @param cc                    Connection context
     * @param overrideIndexes       parameter indexes to override
     * @param overrideValues        parameter override values
     * @throws NamingException
     * @throws SQLException
     */
    protected QueryResultSet executeAndIgnoreStatistics(ConnectionContext cc, int[] overrideIndexes, Object[] overrideValues,
                                                        boolean scrollable)
            throws NamingException, SQLException
    {
        if(log.isTraceEnabled()) trace(cc, overrideIndexes, overrideValues);

        Connection conn = null;
        CallableStatement stmt = null;
        boolean closeConnection = true;
        try
        {
            getMetaData(cc);
            conn = cc.getConnection();
            String sql = StringUtils.strip(getSqlText(cc));
            if (scrollable)
                stmt = conn.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            else
                stmt = conn.prepareCall(sql);

            if(parameters != null)
            {
                parameters.apply(cc, stmt, overrideIndexes, overrideValues);
                stmt.execute();
                parameters.extract(cc, stmt);
                StoredProcedureParameter rsParameter = parameters.getResultSetParameter();
                if (rsParameter != null)
                {
                    closeConnection = false;
                    return (QueryResultSet) rsParameter.getExtractedValue(cc.getDatabaseValueContext());
                }
                else
                    return null;
            }
            else
            {
                stmt.execute();
                return null;
            }
        }
        catch(SQLException e)
        {
            log.error(createExceptionMessage(cc, overrideIndexes, overrideValues), e);
            throw e;
        }
    }


    /**
     * NOTE: When using the batch update facility, a CallableStatement object can call only stored
     * procedures that take input parameters or no parameters at all. Further, the stored procedure
     * must return an update count. The CallableStatement.executeBatch method
     * (inherited from PreparedStatement) will throw a BatchUpdateException if the stored procedure
     * returns anything other than an update count or takes OUT or INOUT parameters.
     * @param cc
     * @throws SQLException
     * @throws NamingException
     */
    protected int[] batchExecute(ConnectionContext cc) throws SQLException, NamingException
    {
        // TODO: This method NEEDS to be tested!
        Connection conn;
        CallableStatement stmt;

        conn = cc.getConnection();
        String sql = StringUtils.strip(getSqlText(cc));

        stmt = conn.prepareCall(sql);
        // TODO: parameters must do addBatch() calles!!!
        if(parameters != null)
            parameters.apply(cc, stmt);

        return stmt.executeBatch();

    }

    /**
     * Executes the stored procedure and records different statistics such as database connection times,
     * parameetr binding times, and procedure execution times.
     * @param cc
     * @param overrideIndexes       parameter indexes to override
     * @param overrideValues        parameter override values
     * @param scrollable
     * @throws NamingException
     * @throws SQLException
     */
    protected QueryResultSet executeAndRecordStatistics(ConnectionContext cc, int[] overrideIndexes, Object[] overrideValues,
                                                        boolean scrollable)
            throws NamingException, SQLException
    {
        if(log.isTraceEnabled()) trace(cc, overrideIndexes, overrideValues);
        QueryExecutionLogEntry logEntry = execLog.createNewEntry(cc, this.getQualifiedName());
        Connection conn = null;
        CallableStatement stmt = null;
        boolean closeConnection = true;
        try
        {
            logEntry.registerGetConnectionBegin();
            conn = cc.getConnection();
            logEntry.registerGetConnectionEnd(conn);
            String sql = StringUtils.strip(getSqlText(cc));
            if (scrollable)
                stmt = conn.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            else
                stmt = conn.prepareCall(sql);

            logEntry.registerBindParamsBegin();

            if(parameters != null)
            {
                parameters.apply(cc, stmt, overrideIndexes, overrideValues);
                logEntry.registerBindParamsEnd();

                logEntry.registerExecSqlBegin();
                stmt.execute();
                logEntry.registerExecSqlEndSuccess();
                parameters.extract(cc, stmt);
                StoredProcedureParameter rsParameter = parameters.getResultSetParameter();
                if (rsParameter != null)
                {
                    closeConnection = false;
                    Value val = rsParameter.getValue().getValue(cc.getDatabaseValueContext());
                    return (QueryResultSet) val.getValue();
                }
                else
                    return null;
            }
            else
            {
                logEntry.registerExecSqlBegin();
                stmt.execute();
                logEntry.registerExecSqlEndSuccess();
                return null;
            }
        }
        catch(SQLException e)
        {
            logEntry.registerExecSqlEndFailed();
            log.error(createExceptionMessage(cc, overrideIndexes, overrideValues), e);
            throw e;
        }
    }

    /**
     * Executes the stored procedure  without any statistical logging
     * @param dbvc
     * @param overrideIndexes       parameter indexes to override
     * @param overrideValues        parameter override values
     * @throws SQLException
     * @throws NamingException
     */
    public QueryResultSet executeAndIgnoreStatistics(DatabaseConnValueContext dbvc, int[] overrideIndexes, Object[] overrideValues,
                                                     boolean scrollable) throws SQLException, NamingException
    {
        String dataSrcIdText = dataSourceId == null ? null : dataSourceId.getTextValue(dbvc);
        return executeAndIgnoreStatistics(
                    dataSrcIdText != null ? dbvc.getConnection(dataSrcIdText, false) : dbvc.getConnection(dbvc.getDefaultDataSource(), false),
                    overrideIndexes, overrideValues, scrollable);
    }

    /**
     * Executes the stored procedure and records different statistics such as database connection times,
     * parameetr binding times, and procedure execution times.
     * @param dbvc
     * @param overrideIndexes       parameter indexes to override
     * @param overrideValues        parameter override values
     * @param scrollable
     * @throws SQLException
     * @throws NamingException
     */
    protected QueryResultSet executeAndRecordStatistics(DatabaseConnValueContext dbvc, int[] overrideIndexes, Object[] overrideValues,
                                                        boolean scrollable) throws SQLException, NamingException
    {
        String dataSrcIdText = dataSourceId != null ? dataSourceId.getTextValue(dbvc) : null;
        return executeAndRecordStatistics(
                    dataSrcIdText != null ? dbvc.getConnection(dataSrcIdText, false) : dbvc.getConnection(dbvc.getDefaultDataSource(), false),
                    overrideIndexes, overrideValues, scrollable);
    }

    /**
     * Executes the stored procedure
     * @param dbvc                  database connection value context
     * @param overrideIndexes       parameter indexes to override
     * @param overrideValues        parameter override values
     * @param scrollable            whether or not the report is pageable (NOT SUPPORTED YET)
     * @throws NamingException
     * @throws SQLException
     */
    public QueryResultSet execute(DatabaseConnValueContext dbvc, int[] overrideIndexes, Object[] overrideValues,
                                  boolean scrollable) throws NamingException, SQLException
    {
        if (log.isInfoEnabled())
            return executeAndRecordStatistics(dbvc, overrideIndexes, overrideValues, scrollable);
        else
            return executeAndIgnoreStatistics(dbvc, overrideIndexes, overrideValues, scrollable);
    }

    /**
     * Executes the stored procedure
     * @param cc                    Connection context
     * @param overrideIndexes       parameter indexes to override
     * @param overrideValues        parameter override values
     * @param scrollable            whether or not the report is pageable (NOT SUPPORTED YET)
     * @throws NamingException
     * @throws SQLException
     */
    public QueryResultSet execute(ConnectionContext cc, int[] overrideIndexes, Object[] overrideValues,
                                  boolean scrollable) throws NamingException, SQLException
    {
        if (log.isInfoEnabled())
            return executeAndRecordStatistics(cc, overrideIndexes, overrideValues, scrollable);
        else
            return executeAndIgnoreStatistics(cc, overrideIndexes, overrideValues, scrollable);
    }

}
