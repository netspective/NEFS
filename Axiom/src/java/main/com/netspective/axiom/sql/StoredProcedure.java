package com.netspective.axiom.sql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.exception.NestableRuntimeException;
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


    // TODO: All methods after this needs to be worked on
    public QueryResultSet execute(DatabaseConnValueContext dbvc, Object[] overrideParams) throws NamingException, SQLException
    {
        return log.isInfoEnabled() ? executeAndRecordStatistics(dbvc, overrideParams) : executeAndIgnoreStatistics(dbvc, overrideParams);
    }

    protected QueryResultSet executeAndIgnoreStatistics(ConnectionContext cc, Object[] overrideParams) throws NamingException, SQLException
    {
        //if(log.isTraceEnabled()) trace(cc, overrideParams);
        try
        {
            Connection conn = cc.getConnection();
            CallableStatement stmt = null;
            String sql = getSqlText(cc);
            stmt = conn.prepareCall(sql);

            if(overrideParams != null)
            {
                for(int i = 0; i < overrideParams.length; i++)
                    stmt.setObject(i + 1, overrideParams[i]);
            }
            else if(parameters != null)
                parameters.apply(cc, stmt);

            boolean executeStmtResult = stmt.execute();


            //return new QueryResultSet(this, cc, executeStmtResult, stmt.getResultSet());
        }
        catch(SQLException e)
        {
            log.error(createExceptionMessage(cc, overrideParams), e);
            throw e;
        }
        return null;
    }

    protected QueryResultSet executeAndIgnoreStatistics(DatabaseConnValueContext dbvc, Object[] overrideParams) throws SQLException, NamingException
    {
        String dataSrcIdText = dataSourceId == null ? null : dataSourceId.getTextValue(dbvc);
        return executeAndIgnoreStatistics(
                    dataSrcIdText != null ? dbvc.getConnection(dataSrcIdText, false) : dbvc.getConnection(dbvc.getDefaultDataSource(), false),
                    overrideParams);
    }

    private QueryResultSet executeAndRecordStatistics(DatabaseConnValueContext dbvc, Object[] overrideParams)
    {
        return null;
    }

}
