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
package com.netspective.axiom.policy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.DatabasePolicy;
import com.netspective.axiom.policy.ddl.AnsiSqlDdlFormats;
import com.netspective.axiom.policy.ddl.AnsiSqlDdlGenerator;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.GeneratedValueColumn;
import com.netspective.axiom.schema.RowDeleteType;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.column.type.GuidColumn;
import com.netspective.axiom.sql.DbmsSqlText;
import com.netspective.axiom.sql.DbmsSqlTexts;
import com.netspective.axiom.sql.QueryExecutionLog;
import com.netspective.axiom.sql.QueryExecutionLogEntry;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.dynamic.QueryDefnSelectStmtGenerator;
import com.netspective.commons.text.GloballyUniqueIdentifier;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.xdm.XmlDataModelSchema;

public class AnsiDatabasePolicy implements DatabasePolicy
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(AnsiDatabasePolicy.class);

    private String name;
    private String[] aliases;
    private SqlDdlFormats ddlFormats = createDdlFormats();
    private SqlDdlGenerator ddlGenerator = createDdlGenerator();
    private boolean prefixTableNamesWithSchemaName = false;
    private boolean placeBindComments = false;

    public AnsiDatabasePolicy()
    {
        setName(DatabasePolicies.DBMSID_DEFAULT);
        setAliases(DatabasePolicies.DBMSID_DEFAULT);
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    public boolean isPrefixTableNamesWithSchemaName()
    {
        return this.prefixTableNamesWithSchemaName;
    }

    public void setPrefixTableNamesWithSchemaName(boolean prefix)
    {
        this.prefixTableNamesWithSchemaName = prefix;
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    public SqlDdlFormats createDdlFormats()
    {
        return new AnsiSqlDdlFormats();
    }

    public void addDdlFormats(SqlDdlFormats ddlFormats)
    {
        this.ddlFormats = ddlFormats;
    }

    public SqlDdlFormats getDdlFormats()
    {
        return ddlFormats;
    }

    public SqlDdlGenerator createDdlGenerator()
    {
        return new AnsiSqlDdlGenerator();
    }

    public void addDdlGenerator(SqlDdlGenerator ddlGenerator)
    {
        this.ddlGenerator = ddlGenerator;
    }

    public SqlDdlGenerator getDdlGenerator()
    {
        return ddlGenerator;
    }

    public boolean supportsSequences()
    {
        return false;
    }

    public boolean supportsForeignKeyConstraints()
    {
        return true;
    }

    /**
     * Sets the unique ID and creates a single alias for this policy. Should be called before setting aliases since
     * once a name is set, a single aliase is created automatically.
     */
    public void setName(String name)
    {
        this.name = name.toLowerCase();
        setAliases(this.name);
    }

    public void setAliases(String aliases)
    {
        this.aliases = TextUtils.getInstance().split(aliases, ",", true);

        Set aliasesSet = new HashSet();
        for(int i = 0; i < this.aliases.length; i++)
        {
            String alias = this.aliases[i].toLowerCase();
            aliasesSet.add(alias);
        }

        if(!aliasesSet.contains(getDbmsIdentifier()))
            aliasesSet.add(getDbmsIdentifier());

        this.aliases = (String[]) aliasesSet.toArray(new String[aliasesSet.size()]);
    }

    public String getDbmsIdentifier()
    {
        return name;
    }

    public String[] getDbmsIdentifiers()
    {
        return aliases;
    }

    public Object executeAndGetSingleValue(ConnectionContext cc, String sql) throws SQLException
    {
        Object value = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = cc.getConnection().createStatement();
            try
            {
                rs = stmt.executeQuery(sql);
                if(rs.next())
                    value = rs.getObject(1);
            }
            finally
            {
                if(rs != null) rs.close();
            }
        }
        catch(NamingException e)
        {
            throw new SQLException(e.toString() + " [" + sql + "]");
        }
        catch(SQLException e)
        {
            throw new SQLException(e.toString() + " [" + sql + "]");
        }
        finally
        {
            if(stmt != null) stmt.close();
        }
        return value;
    }

    public Object handleAutoIncPreDmlInsertExecute(ConnectionContext cc, AutoIncColumn column) throws SQLException
    {
        return null;
    }

    public Object handleAutoIncPostDmlInsertExecute(ConnectionContext cc, AutoIncColumn column, Object autoIncColumnValue) throws SQLException
    {
        return autoIncColumnValue;
    }

    public Object getAutoIncCurrentValue(ConnectionContext cc, AutoIncColumn column) throws SQLException
    {
        return null;
    }

    public boolean retainAutoIncColInInsertDml()
    {
        return true;
    }

    public boolean retainAutoIncColInUpdateDml()
    {
        return true;
    }

    public Object handleGUIDPreDmlInsertExecute(ConnectionContext cc, GuidColumn column) throws SQLException
    {
        try
        {
            return GloballyUniqueIdentifier.getRandomGUID(false);
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new SQLException(e.toString());
        }
        catch(UnknownHostException e)
        {
            throw new SQLException(e.toString());
        }
    }

    public Object handleGUIDPostDmlInsertExecute(ConnectionContext cc, GuidColumn column, Object GUIDColumnValue) throws SQLException
    {
        return GUIDColumnValue;
    }

    public boolean retainGUIDColInInsertDml()
    {
        return true;
    }

    public boolean retainGUIDColInUpdateDml()
    {
        return true;
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    public Map prepareJdbcTypeInfoMap()
    {
        Map jdbcTypeInfoMap = new HashMap();

        jdbcTypeInfoMap.put(new Integer(java.sql.Types.VARCHAR), "text");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.BIGINT), "big-int");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.INTEGER), "integer");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.DECIMAL), "decimal");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.FLOAT), "float");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.DOUBLE), "double");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.DATE), "date");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.TIME), "time");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.TIMESTAMP), "time-stamp");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.BIT), "boolean");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.NUMERIC), "numeric");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.REAL), "double");
        jdbcTypeInfoMap.put(new Integer(java.sql.Types.TINYINT), "short-int");

        return jdbcTypeInfoMap;
    }

    public void reverseEngineer(Writer writer, Connection conn, String catalog, String schemaPattern) throws IOException, SQLException
    {
        Map dataTypesMap = prepareJdbcTypeInfoMap();
        DatabaseMetaData dbmd = conn.getMetaData();
        TextUtils textUtils = TextUtils.getInstance();

        writer.write("<?xml version=\"1.0\"?>\n\n");
        writer.write("<!-- Reverse engineered by Axiom\n");
        writer.write("     driver: " + dbmd.getDriverName() + "\n");
        writer.write("     driver-version: " + dbmd.getDriverVersion() + "\n");
        writer.write("     product: " + dbmd.getDatabaseProductName() + "\n");
        writer.write("     product-version: " + dbmd.getDatabaseProductVersion() + "\n");

        writer.write("     available catalogs:");
        ResultSet rs = null;
        try
        {
            rs = dbmd.getCatalogs();
            while(rs.next())
            {
                writer.write(" " + rs.getObject(1).toString());
            }
        }
        finally
        {
            if(rs != null) rs.close();
        }

        writer.write("\n");

        writer.write("     available schemas:");
        try
        {
            rs = dbmd.getSchemas();
            while(rs.next())
            {
                writer.write(" " + rs.getObject(1).toString());
            }
        }
        finally
        {
            if(rs != null) rs.close();
        }
        writer.write("\n");
        writer.write("-->\n\n");

        writer.write("<component xmlns:xdm=\"http://www.netspective.org/Framework/Commons/XMLDataModel\">\n");
        writer.write("    <xdm:include resource=\"com/netspective/axiom/conf/axiom.xml\"/>\n");
        writer.write("    <schema name=\"" + catalog + "." + schemaPattern + "\">\n");

        Map dbmdTypeInfoByName = new HashMap();
        Map dbmdTypeInfoByJdbcType = new HashMap();
        ResultSet typesRS = null;
        try
        {
            typesRS = dbmd.getTypeInfo();
            while(typesRS.next())
            {
                int colCount = typesRS.getMetaData().getColumnCount();
                Object[] typeInfo = new Object[colCount];
                for(int i = 1; i <= colCount; i++)
                    typeInfo[i - 1] = typesRS.getObject(i);
                dbmdTypeInfoByName.put(typesRS.getString(1), typeInfo);
                dbmdTypeInfoByJdbcType.put(new Integer(typesRS.getInt(2)), typeInfo);
            }
        }
        finally
        {
            if(typesRS != null) typesRS.close();
        }

        ResultSet tables = null;
        try
        {
            tables = dbmd.getTables(catalog, schemaPattern, null, new String[]{"TABLE"});
            while(tables.next())
            {
                String tableNameOrig = tables.getString(3);
                String tableName = textUtils.fixupTableNameCase(tableNameOrig);

                writer.write("        <table name=\"" + tableName + "\">\n");

                Map primaryKeys = new HashMap();
                ResultSet pkRS = null;
                try
                {
                    pkRS = dbmd.getPrimaryKeys(null, null, tableNameOrig);
                    while(pkRS.next())
                    {
                        primaryKeys.put(pkRS.getString(4), pkRS.getString(5));
                    }

                }
                catch(Exception e)
                {
                    // driver may not support this function
                }
                finally
                {
                    if(pkRS != null) pkRS.close();
                }

                Map fKeys = new HashMap();
                ResultSet fkRS = null;
                try
                {
                    fkRS = dbmd.getImportedKeys(null, null, tableNameOrig);
                    while(fkRS.next())
                    {
                        fKeys.put(fkRS.getString(8), textUtils.fixupTableNameCase(fkRS.getString(3)) + "." + fkRS.getString(4).toLowerCase());
                    }
                }
                catch(Exception e)
                {
                    // driver may not support this function
                }
                finally
                {
                    if(fkRS != null) fkRS.close();
                }

                // we keep track of processed columns so we don't duplicate them in the XML
                Set processedColsMap = new HashSet();
                ResultSet columns = null;
                try
                {
                    columns = dbmd.getColumns(null, null, tableNameOrig, null);
                    while(columns.next())
                    {
                        String columnNameOrig = columns.getString(4);
                        if(processedColsMap.contains(columnNameOrig))
                            continue;
                        processedColsMap.add(columnNameOrig);

                        String columnName = columnNameOrig.toLowerCase();

                        writer.write("            <column name=\"" + columnName + "\"");
                        try
                        {
                            if(fKeys.containsKey(columnNameOrig))
                                writer.write(" lookup-ref=\"" + fKeys.get(columnNameOrig) + "\"");
                            else
                            {
                                short jdbcType = columns.getShort(5);
                                String dataType = (String) dataTypesMap.get(new Integer(jdbcType));
                                if(dataType == null) dataType = Short.toString(jdbcType);
                                writer.write(" type=\"" + dataType + "\"");
                            }

                            if(primaryKeys.containsKey(columnNameOrig))
                                writer.write(" primary-key=\"yes\"");

                            if(columns.getString(18).equals("NO"))
                                writer.write(" required=\"yes\"");

                            String defaultValue = columns.getString(13);
                            if(defaultValue != null)
                                writer.write(" default=\"" + defaultValue + "\"");

                            String remarks = columns.getString(12);
                            if(remarks != null)
                                writer.write(" descr=\"" + remarks + "\"");

                        }
                        catch(Exception e)
                        {
                        }

                        writer.write("/>\n");
                    }
                }
                finally
                {
                    if(columns != null) columns.close();
                }


                writer.write("        </table>\n");
            }
        }
        finally
        {
            tables.close();
        }

        writer.write("    </schema>\n");
        writer.write("</component>");
    }

    public void reverseEngineer(File output, Connection conn, String catalog, String schemaPattern) throws IOException, SQLException
    {
        Writer writer = new FileWriter(output);
        try
        {
            reverseEngineer(writer, conn, catalog, schemaPattern);
        }
        finally
        {
            if(writer != null) writer.close();
        }
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    public boolean isPlaceBindComments()
    {
        return placeBindComments;
    }

    public void setPlaceBindComments(boolean placeBindComments)
    {
        this.placeBindComments = placeBindComments;
    }

    public static String getDmlDebugText(String sql, Object[] bindValues)
    {
        StringBuffer result = new StringBuffer();

        result.append(AnsiDatabasePolicy.class.getName());
        result.append("\n");
        result.append(sql);

        if(bindValues != null)
        {
            result.append("\nBIND");

            int bindNum = 1;
            for(int i = 0; i < bindValues.length; i++)
            {
                Object bindValue = bindValues[i];
                if(bindValue == null)
                    continue;

                if(bindNum > 1)
                    result.append(", ");

                result.append(" [" + i + ", " + bindNum + "] ");
                result.append(bindValue);
                result.append(" (" + bindValues[i].getClass() + ")");

                bindNum++;
            }
        }

        return result.toString();
    }

    protected int executeAndRecordStatistics(ConnectionContext cc, QueryExecutionLog qel, String identifer, String sql, Object[] bindValues, Object[] addlParams) throws NamingException, SQLException
    {
        if(log.isTraceEnabled())
            log.trace(getDmlDebugText(sql, bindValues));

        QueryExecutionLogEntry logEntry = qel.createNewEntry(cc, identifer);
        try
        {
            logEntry.registerGetConnectionBegin();
            Connection conn = cc.getConnection();
            logEntry.registerGetConnectionEnd(conn);
            PreparedStatement stmt = conn.prepareStatement(sql);

            logEntry.registerBindParamsBegin();
            int bindNum = 0;
            if(bindValues != null)
            {
                for(int i = 0; i < bindValues.length; i++)
                {
                    Object bindValue = bindValues[i];
                    if(bindValue != null)
                    {
                        bindNum++;
                        stmt.setObject(bindNum, bindValue);
                    }
                }
            }
            if(addlParams != null)
            {
                for(int i = 0; i < addlParams.length; i++)
                {
                    Object bindValue = addlParams[i];
                    if(bindValue != null)
                    {
                        bindNum++;
                        stmt.setObject(bindNum, bindValue);
                    }
                }
            }
            logEntry.registerBindParamsEnd();

            logEntry.registerExecSqlBegin();
            int result = stmt.executeUpdate();
            stmt.close();
            logEntry.registerExecSqlEndSuccess();
            return result;
        }
        catch(SQLException e)
        {
            logEntry.registerExecSqlEndFailed();
            log.error(getDmlDebugText(sql, bindValues), e);
            throw e;
        }
        finally
        {
            logEntry.finalize(cc, log);
        }
    }

    protected int executeAndIgnoreStatistics(ConnectionContext cc, String sql, Object[] bindValues, Object[] addlParams) throws NamingException, SQLException
    {
        if(log.isTraceEnabled())
            log.trace(getDmlDebugText(sql, bindValues));

        try
        {
            Connection conn = cc.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            int bindNum = 0;
            if(bindValues != null)
            {
                for(int i = 0; i < bindValues.length; i++)
                {
                    Object bindValue = bindValues[i];
                    if(bindValue != null)
                    {
                        bindNum++;
                        stmt.setObject(bindNum, bindValue);
                    }
                }
            }
            if(addlParams != null)
            {
                for(int i = 0; i < addlParams.length; i++)
                {
                    Object bindValue = addlParams[i];
                    if(bindValue != null)
                    {
                        bindNum++;
                        stmt.setObject(bindNum, bindValue);
                    }
                }
            }

            int result = stmt.executeUpdate();
            stmt.close();
            return result;
        }
        catch(SQLException e)
        {
            log.error(getDmlDebugText(sql, bindValues), e);
            throw e;
        }
    }

    public String insertValues(ConnectionContext cc, int flags, ColumnValues columnValues, RowInsertListener rowListener) throws NamingException, SQLException
    {
        int columnsCount = columnValues.size();
        boolean execute = (flags & DMLFLAG_EXECUTE) != 0;
        StringBuffer namesSql = new StringBuffer();
        StringBuffer valuesSql = new StringBuffer();

        if(execute && rowListener != null)
            rowListener.beforeInsert(cc, flags, columnValues);

        Object[] bindValues = ((flags & DMLFLAG_USE_BIND_PARAMS) != 0) ? new Object[columnsCount] : null;

        GeneratedValueColumn[] generators = new GeneratedValueColumn[columnsCount];
        ColumnInsertListener[] colListeners = new ColumnInsertListener[columnsCount];

        boolean haveGenerators = false;
        boolean haveColListeners = false;
        boolean isFirstColumn = true;
        boolean placeBindComments = isPlaceBindComments();

        for(int i = 0; i < columnsCount; i++)
        {
            ColumnValue value = columnValues.getByColumnIndex(i);
            Column column = value.getColumn();
            Object bindValue = value.getValueForSqlBindParam();

            if(execute && column.isInsertManagedByDbms())
                continue;

            if(execute && (column instanceof GeneratedValueColumn))
            {
                GeneratedValueColumn generator = (GeneratedValueColumn) column;
                generators[i] = generator;
                haveGenerators = true;

                if(!generator.retainValueInInsertDml(cc)) continue;
                bindValue = generator.handlePreDmlExecute(cc);
            }

            if(execute && (column instanceof ColumnInsertListener))
            {
                ((ColumnInsertListener) column).beforeInsert(cc, flags, value, columnValues);
                colListeners[i] = (ColumnInsertListener) column;
                haveColListeners = true;
                bindValue = value.getValueForSqlBindParam(); // in case the listener changed the column value
            }

            if(!isFirstColumn)
            {
                namesSql.append(", ");
                valuesSql.append(", ");
            }

            isFirstColumn = false;
            namesSql.append(column.getSqlName());

            if(value.isSqlExpr())
            {
                final DbmsSqlTexts sqlExprs = value.getSqlExprs();
                final DbmsSqlText sqlExprForDb = sqlExprs != null ? sqlExprs.getByDbmsOrAnsi(this) : null;
                if(sqlExprForDb != null)
                    valuesSql.append(sqlExprForDb.getSql(cc));
                else
                {
                    log.error("Column " + value.getColumn().getQualifiedName() + " is specifying a SQL Expression to insert but no expression is available for db '" + cc.getDatabasePolicy().getDbmsIdentifier() + "' or 'ansi'. Available: " + sqlExprs);
                    valuesSql.append("NULL");
                }
            }
            else
            {
                if(bindValue == null)
                    valuesSql.append("NULL");
                else if((flags & DMLFLAG_USE_BIND_PARAMS) != 0)
                {
                    valuesSql.append("?");
                    if(placeBindComments)
                        valuesSql.append(" /* \"+ i +\" */");
                    bindValues[i] = bindValue;
                }
                else
                    valuesSql.append(column.formatSqlLiteral(bindValue));
            }
        }

        final Table table = columnValues.getByColumnIndex(0).getColumn().getTable();
        final String tableName = resolveTableName(table);
        final String sql = "insert into " + tableName + " (" + namesSql + ") values (" + valuesSql + ")";

        if(execute)
        {
            if(log.isInfoEnabled())
                executeAndRecordStatistics(cc, table.getDmlExecutionLog(), table.getName() + ".insert()", sql, bindValues, null);
            else
                executeAndIgnoreStatistics(cc, sql, bindValues, null);

            if(haveGenerators || haveColListeners)
            {
                for(int i = 0; i < columnsCount; i++)
                {
                    if(generators[i] != null)
                    {
                        ColumnValue value = columnValues.getByColumnIndex(i);
                        Object postDmlValue = generators[i].handlePostDmlExecute(cc, bindValues[i]);
                        if(value != null)
                            value.setValue(postDmlValue);
                    }

                    if(colListeners[i] != null)
                    {
                        ColumnValue value = columnValues.getByColumnIndex(i);
                        colListeners[i].afterInsert(cc, flags, value, columnValues);
                    }
                }
            }

            if(rowListener != null)
                rowListener.afterInsert(cc, flags, columnValues);
        }

        return sql;
    }

    public String updateValues(ConnectionContext cc, int flags, ColumnValues columnValues, RowUpdateListener rowListener, String whereCond, Object[] whereCondBindParams) throws NamingException, SQLException
    {
        boolean execute = (flags & DMLFLAG_EXECUTE) != 0;

        if(execute && rowListener != null)
            rowListener.beforeUpdate(cc, flags, columnValues);

        int columnsCount = columnValues.size();
        StringBuffer setsSql = new StringBuffer();
        Object[] bindValues = new Object[columnsCount];
        ColumnUpdateListener[] colListeners = new ColumnUpdateListener[columnsCount];
        boolean haveColListeners = false;
        boolean isFirstColumn = true;
        boolean placeBindComments = isPlaceBindComments();

        for(int i = 0; i < columnsCount; i++)
        {
            ColumnValue value = columnValues.getByColumnIndex(i);
            Column column = value.getColumn();

            // primary keys should not be in the update SQL
            if(column.isPrimaryKey())
                continue;

            if(execute && column.isUpdateManagedByDbms())
                continue;

            if(execute && (column instanceof GeneratedValueColumn))
            {
                GeneratedValueColumn generator = (GeneratedValueColumn) column;
                if(!generator.retainValueInUpdateDml(cc)) continue;
            }

            if(execute && (column instanceof ColumnUpdateListener))
            {
                ((ColumnUpdateListener) column).beforeUpdate(cc, flags, value, columnValues);
                colListeners[i] = (ColumnUpdateListener) column;
                haveColListeners = true;
            }

            Object bindValue = value.getValueForSqlBindParam();

            if(!isFirstColumn)
                setsSql.append(", ");

            isFirstColumn = false;
            setsSql.append(column.getSqlName());
            setsSql.append(" = ");

            if(value.isSqlExpr())
            {
                final DbmsSqlTexts sqlExprs = value.getSqlExprs();
                final DbmsSqlText sqlExprForDb = sqlExprs != null ? sqlExprs.getByDbmsOrAnsi(this) : null;
                if(sqlExprForDb != null)
                    setsSql.append(sqlExprForDb.getSql(cc));
                else
                {
                    log.error("Column " + value.getColumn().getQualifiedName() + " is specifying a SQL Expression to update but no expression is available for db '" + cc.getDatabasePolicy().getDbmsIdentifier() + "' or 'ansi'. Available: " + sqlExprs);
                    setsSql.append("NULL");
                }

            }
            else
            {
                if(bindValue == null)
                    setsSql.append("NULL");
                else if((flags & DMLFLAG_USE_BIND_PARAMS) != 0)
                {
                    setsSql.append("?");
                    if(placeBindComments)
                        setsSql.append(" /* \"+ i +\" */");
                    bindValues[i] = bindValue;
                }
                else
                    setsSql.append(column.formatSqlLiteral(bindValue));
            }
        }

        final Table table = columnValues.getByColumnIndex(0).getColumn().getTable();
        final String tableName = resolveTableName(table);

        String sql = "update " + tableName + " set " + setsSql;
        if(whereCond != null)
        {
            if(!whereCond.startsWith("where"))
                sql += " where";
            sql += " " + whereCond;
        }

        if(execute)
        {
            if(log.isInfoEnabled())
                executeAndRecordStatistics(cc, table.getDmlExecutionLog(), table.getName() + ".update()", sql, bindValues, whereCondBindParams);
            else
                executeAndIgnoreStatistics(cc, sql, bindValues, whereCondBindParams);

            if(haveColListeners)
            {
                for(int i = 0; i < columnsCount; i++)
                {
                    if(colListeners[i] != null)
                    {
                        ColumnValue value = columnValues.getByColumnIndex(i);
                        colListeners[i].afterUpdate(cc, flags, value, columnValues);
                    }
                }
            }

            if(rowListener != null)
                rowListener.afterUpdate(cc, flags, columnValues);
        }

        return sql;
    }

    public String deleteValues(ConnectionContext cc, int flags, ColumnValues columnValues, RowDeleteListener rowListener, String whereCond, Object[] whereCondBindParams) throws NamingException, SQLException
    {
        boolean execute = (flags & DMLFLAG_EXECUTE) != 0;

        int columnsCount = columnValues.size();
        ColumnDeleteListener[] colListeners = new ColumnDeleteListener[columnsCount];
        boolean haveColListeners = false;

        if(execute)
        {
            for(int i = 0; i < columnsCount; i++)
            {
                ColumnValue value = columnValues.getByColumnIndex(i);
                Column column = value.getColumn();

                if(column instanceof ColumnDeleteListener)
                {
                    ((ColumnDeleteListener) column).beforeDelete(cc, flags, value, columnValues);
                    colListeners[i] = (ColumnDeleteListener) column;
                    haveColListeners = true;
                }
            }
        }

        Table table = columnValues.getByColumnIndex(0).getColumn().getTable();
        final RowDeleteType rowDeleteType = table.getRowDeleteType();
        final String tableName = resolveTableName(table);

        String sql = null;
        final String identifier;
        if(rowDeleteType.isLogicalDelete())
        {
            final String customSetClauseFormat = table.getLogicalDeleteUpdateSqlSetClauseFormat();
            final String setClauseFormat = customSetClauseFormat != null
                                           ? customSetClauseFormat
                                           : table.getSchema().getLogicalDeleteUpdateSqlSetClauseFormat();

            sql = "update " + tableName + " set " + setClauseFormat + ' ';
            if(whereCond != null)
            {
                if(!whereCond.startsWith("where"))
                    sql += " where";
                sql += " " + whereCond;
            }

            identifier = table.getName() + ".delete(logical)";
        }
        else
        {
            sql = "delete from " + tableName;
            if(whereCond != null)
            {
                if(!whereCond.startsWith("where"))
                    sql += " where";
                sql += " " + whereCond;
            }

            identifier = table.getName() + ".delete(physical)";
        }

        if(execute)
        {
            if(rowListener != null)
                rowListener.beforeDelete(cc, flags, columnValues);

            if(log.isInfoEnabled())
                executeAndRecordStatistics(cc, table.getDmlExecutionLog(), identifier, sql, null, whereCondBindParams);
            else
                executeAndIgnoreStatistics(cc, sql, null, whereCondBindParams);

            if(haveColListeners)
            {
                for(int i = 0; i < columnsCount; i++)
                {
                    if(colListeners[i] != null)
                    {
                        ColumnValue value = columnValues.getByColumnIndex(i);
                        colListeners[i].afterDelete(cc, flags, value, columnValues);
                    }
                }
            }
        }

        if(rowListener != null)
            rowListener.afterDelete(cc, flags, columnValues);

        return sql;
    }

    public String resolveTableName(Table table)
    {
        final String tableName = (isPrefixTableNamesWithSchemaName()
                                  ? table.getSchema().getName() + "." + table.getSqlName() : table.getSqlName());
        return tableName;
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    public QueryDefnSelectStmtGenerator createSelectStatementGenerator(QueryDefnSelect queryDefnSelect)
    {
        return new QueryDefnAnsiSelectStmtGenerator(this, queryDefnSelect);
    }
}
