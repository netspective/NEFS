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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.StringUtils;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.text.ExpressionText;
import com.netspective.commons.text.ValueSourceOrJavaExpressionText;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.value.DatabaseConnValueContext;

import javax.naming.NamingException;
import java.util.Map;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 * Class for handling stored procedure calls
 *
 * @author Aye Thu
 * @version $Id: StoredProcedure.java,v 1.5 2003-11-06 00:04:01 aye.thu Exp $
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


    private Log log = LogFactory.getLog(StoredProcedure.class);
    private StoredProceduresNameSpace nameSpace;
    private String spName;
    private StoredProcedureParameters parameters;
    private ValueSource dataSourceId;
    private QueryExecutionLog execLog = new QueryExecutionLog();
    private StoredProcedure.StoredProcedureDbmsSqlTexts sqlTexts = new StoredProcedure.StoredProcedureDbmsSqlTexts();


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

    public String createExceptionMessage(ConnectionContext cc, Object[] overrideParams) throws NamingException, SQLException
    {
        StringBuffer text = new StringBuffer();

        text.append("Stored Procedure id = ");
        text.append(getQualifiedName());
        text.append("\n");
        text.append(getSqlText(cc));
        text.append("\n");
        if(overrideParams != null)
        {
            text.append("\nBind Parameters (overridden in method):\n");
            for(int i = 0; i < overrideParams.length; i++)
            {
                text.append("["+ (i + 1) +"] ");
                text.append(overrideParams[i] + " ("+ overrideParams[i].getClass().getName() +")");
            }
            text.append("\n");
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
     * @param overrideParams
     * @throws NamingException
     * @throws SQLException
     */
    public void trace(ConnectionContext cc, Object[] overrideParams) throws NamingException, SQLException
    {
        StringBuffer traceMsg = new StringBuffer();
        traceMsg.append(QueryExecutionLogEntry.class.getName() + " '"+ getQualifiedName() +"' at "+
                cc.getContextLocation() +"\n");
        traceMsg.append(getSqlText(cc));
        if(overrideParams != null)
        {
            for(int i = 0; i < overrideParams.length; i++)
            {
                traceMsg.append("["+ i +"] ");
                traceMsg.append(overrideParams[i]);
                if(overrideParams[i] != null)
                    traceMsg.append(" (" + overrideParams[i].getClass().getName() + ")");
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
     * @param cc                Connection context
     * @param overrideParams    Parameter values overriding the ones defined in XMLFlag indicating whether or not to close the DB connection after execution
     * @throws NamingException
     * @throws SQLException
     */
    protected void executeAndIgnoreStatistics(ConnectionContext cc, Object[] overrideParams, boolean scrollable)
            throws NamingException, SQLException
    {
        if(log.isTraceEnabled()) trace(cc, overrideParams);

        Connection conn = null;
        CallableStatement stmt = null;
        try
        {
            conn = cc.getConnection();
            String sql = StringUtils.strip(getSqlText(cc));
            if (scrollable)
                stmt = conn.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            else
                stmt = conn.prepareCall(sql);

            if(overrideParams != null)
            {
                for(int i = 0; i < overrideParams.length; i++)
                    stmt.setObject(i + 1, overrideParams[i]);
            }

            if(parameters != null)
                parameters.apply(cc, stmt);
            stmt.execute();
            parameters.extract(cc, stmt);
        }
        catch(SQLException e)
        {
            log.error(createExceptionMessage(cc, overrideParams), e);
            throw e;
        }
        finally
        {
            // TODO: we should close the connection if the OUT parameters don't include result sets
            /*
            if (conn != null && closeConnection)
            {
                stmt.close();
                conn.close();
            }
            */
        }
    }

    /**
     * Executes a stored procedure that returns a result set
     * @param cc
     * @param overrideParams
     * @param scrollable
     * @return
     * @throws SQLException
     * @throws NamingException
     */
    protected QueryResultSet executeQueryAndIgnoreStatistics(ConnectionContext cc, Object[] overrideParams, boolean scrollable)
        throws SQLException, NamingException
    {
        if(log.isTraceEnabled()) trace(cc, overrideParams);

        Connection conn = null;
        CallableStatement stmt = null;
        try
        {
            conn = cc.getConnection();
            String sql = StringUtils.strip(getSqlText(cc));
            if (scrollable)
                stmt = conn.prepareCall(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            else
                stmt = conn.prepareCall(sql);

            if(overrideParams != null)
            {
                for(int i = 0; i < overrideParams.length; i++)
                    stmt.setObject(i + 1, overrideParams[i]);
            }

            if(parameters != null)
                parameters.apply(cc, stmt);
            ResultSet rs = stmt.executeQuery();
            parameters.extract(cc, stmt);
            return new QueryResultSet(this, cc, rs);
        }
        catch(SQLException e)
        {
            log.error(createExceptionMessage(cc, overrideParams), e);
            throw e;
        }

    }

    /**
     * Executes a stored procedure that returns a result set
     * @param cc
     * @param overrideParams
     * @param scrollable
     * @return
     * @throws SQLException
     * @throws NamingException
     */
    protected QueryResultSet executeQueryAndRecordStatistics(ConnectionContext cc, Object[] overrideParams, boolean scrollable)
        throws SQLException, NamingException
    {
        if(log.isTraceEnabled()) trace(cc, overrideParams);
        QueryExecutionLogEntry logEntry = execLog.createNewEntry(cc, this.getQualifiedName());
        Connection conn = null;
        CallableStatement stmt = null;
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
            if(overrideParams != null)
            {
                for(int i = 0; i < overrideParams.length; i++)
                    stmt.setObject(i + 1, overrideParams[i]);
            }

            if(parameters != null)
                parameters.apply(cc, stmt);
            logEntry.registerBindParamsEnd();
            logEntry.registerExecSqlBegin();
            ResultSet rs = stmt.executeQuery();
            logEntry.registerExecSqlEndSuccess();
            parameters.extract(cc, stmt);
            return new QueryResultSet(this, cc, rs);
        }
        catch(SQLException e)
        {
            logEntry.registerExecSqlEndFailed();
            log.error(createExceptionMessage(cc, overrideParams), e);
            throw e;
        }
    }

    /**
     * Executes a stored procedure that returns a result set
     * @param dbvc
     * @param overrideParams
     * @param scrollable
     * @return
     * @throws SQLException
     * @throws NamingException
     */
    protected QueryResultSet executeQueryAndIgnoreStatistics(DatabaseConnValueContext dbvc, Object[] overrideParams, boolean scrollable) throws SQLException, NamingException
    {
        String dataSrcIdText = dataSourceId != null ? dataSourceId.getTextValue(dbvc) : null;
        return executeQuery(
                    dataSrcIdText != null ? dbvc.getConnection(dataSrcIdText, false) : dbvc.getConnection(dbvc.getDefaultDataSource(), false),
                    overrideParams, scrollable);
    }

    /**
     * Executes the stored procedure and records different statistics such as database connection times,
     * parameetr binding times, and procedure execution times.
     * @param dbvc
     * @param overrideParams
     * @param scrollable
     * @throws SQLException
     * @throws NamingException
     */
    protected QueryResultSet executeQueryAndRecordStatistics(DatabaseConnValueContext dbvc, Object[] overrideParams, boolean scrollable) throws SQLException, NamingException
    {
        String dataSrcIdText = dataSourceId != null ? dataSourceId.getTextValue(dbvc) : null;
        return executeQueryAndRecordStatistics(
                    dataSrcIdText != null ? dbvc.getConnection(dataSrcIdText, false) : dbvc.getConnection(dbvc.getDefaultDataSource(), false),
                    overrideParams, scrollable);
    }


    /**
     * Executes a stored procedure that returns a result set. This should be used only when
     * the stored procedure is known to return a result set not as an OUT parameter.
     * @param cc
     * @param overrideParams
     * @param scrollable
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    public QueryResultSet executeQuery(ConnectionContext cc, Object[] overrideParams, boolean scrollable)
            throws NamingException, SQLException
    {
        if (log.isInfoEnabled())
            return executeQueryAndRecordStatistics(cc, overrideParams, scrollable);
        else
            return executeQueryAndIgnoreStatistics(cc, overrideParams, scrollable);
    }

    /**
     * Executes a stored procedure that returns a result set. This should be used only when
     * the stored procedure is known to return a result set not as an OUT parameter.
     * @param dbvc
     * @param overrideParams
     * @param scrollable
     * @return
     * @throws NamingException
     * @throws SQLException
     */
    public QueryResultSet executeQuery(DatabaseConnValueContext dbvc, Object[] overrideParams, boolean scrollable)
            throws NamingException, SQLException
    {
        if (log.isInfoEnabled())
            return executeQueryAndRecordStatistics(dbvc, overrideParams, scrollable);
        else
            return executeQueryAndIgnoreStatistics(dbvc, overrideParams, scrollable);
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
        Connection conn = null;
        CallableStatement stmt = null;

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
     * @param overrideParams
     * @param scrollable
     * @throws NamingException
     * @throws SQLException
     */
    protected void executeAndRecordStatistics(ConnectionContext cc, Object[] overrideParams, boolean scrollable)
            throws NamingException, SQLException
    {
        if(log.isTraceEnabled()) trace(cc, overrideParams);
        QueryExecutionLogEntry logEntry = execLog.createNewEntry(cc, this.getQualifiedName());
        Connection conn = null;
        CallableStatement stmt = null;
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
            if(overrideParams != null)
            {
                for(int i = 0; i < overrideParams.length; i++)
                    stmt.setObject(i + 1, overrideParams[i]);
            }
            if(parameters != null)
                parameters.apply(cc, stmt);
            logEntry.registerBindParamsEnd();

            logEntry.registerExecSqlBegin();
            stmt.execute();
            logEntry.registerExecSqlEndSuccess();
            parameters.extract(cc, stmt);
        }
        catch(SQLException e)
        {
            logEntry.registerExecSqlEndFailed();
            log.error(createExceptionMessage(cc, overrideParams), e);
            throw e;
        }
        finally
        {
            // TODO: we should close the connection if the OUT parameters don't include result sets
            /*
            if (conn != null && closeConnection)
            {
                stmt.close();
                conn.close();
            }
            */
        }
    }

    /**
     * Executes the stored procedure  without any statistical logging
     * @param dbvc
     * @param overrideParams
     * @throws SQLException
     * @throws NamingException
     */
    public void executeAndIgnoreStatistics(DatabaseConnValueContext dbvc, Object[] overrideParams, boolean scrollable) throws SQLException, NamingException
    {
        String dataSrcIdText = dataSourceId == null ? null : dataSourceId.getTextValue(dbvc);
        executeAndIgnoreStatistics(
                    dataSrcIdText != null ? dbvc.getConnection(dataSrcIdText, false) : dbvc.getConnection(dbvc.getDefaultDataSource(), false),
                    overrideParams, scrollable);
    }

    /**
     * Executes the stored procedure and records different statistics such as database connection times,
     * parameetr binding times, and procedure execution times.
     * @param dbvc
     * @param overrideParams
     * @param scrollable
     * @throws SQLException
     * @throws NamingException
     */
    protected void executeAndRecordStatistics(DatabaseConnValueContext dbvc, Object[] overrideParams, boolean scrollable) throws SQLException, NamingException
    {
        String dataSrcIdText = dataSourceId != null ? dataSourceId.getTextValue(dbvc) : null;
        executeAndRecordStatistics(
                    dataSrcIdText != null ? dbvc.getConnection(dataSrcIdText, false) : dbvc.getConnection(dbvc.getDefaultDataSource(), false),
                    overrideParams, scrollable);
    }

    /**
     * Executes the stored procedure
     * @param dbvc
     * @param overrideParams
     * @param scrollable
     * @throws NamingException
     * @throws SQLException
     */
    public void execute(DatabaseConnValueContext dbvc, Object[] overrideParams, boolean scrollable) throws NamingException, SQLException
    {
        if (log.isInfoEnabled())
            executeAndRecordStatistics(dbvc, overrideParams, scrollable);
        else
            executeAndIgnoreStatistics(dbvc, overrideParams, scrollable);
    }

    /**
     * Executes the stored procedure
     * @param cc                Connection context
     * @param overrideParams    parameters values to override the ones defined in XML
     * @param scrollable        whether or not
     * @throws NamingException
     * @throws SQLException
     */
    public void execute(ConnectionContext cc, Object[] overrideParams, boolean scrollable) throws NamingException, SQLException
    {
        if (log.isInfoEnabled())
            executeAndRecordStatistics(cc, overrideParams, scrollable);
        else
            executeAndIgnoreStatistics(cc, overrideParams, scrollable);
    }

}
