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
 * @author Shahid N. Shah
 */

/**
 * $Id: AnsiDatabasePolicy.java,v 1.1 2003-03-13 18:25:39 shahid.shah Exp $
 */

package com.netspective.axiom.policy;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.security.NoSuchAlgorithmException;
import java.net.UnknownHostException;
import java.io.Writer;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.DatabasePolicy;
import com.netspective.axiom.DatabasePolicies;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Indexes;
import com.netspective.axiom.schema.Index;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.ColumnValue;
import com.netspective.axiom.schema.GeneratedValueColumn;
import com.netspective.axiom.schema.PrimaryKeyColumns;
import com.netspective.axiom.sql.DbmsSqlText;
import com.netspective.axiom.sql.QueryExecutionLog;
import com.netspective.axiom.sql.QueryExecutionLogEntry;
import com.netspective.axiom.sql.dynamic.QueryDefnSelectStmtGenerator;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.schema.column.SqlDataDefns;
import com.netspective.axiom.schema.column.BasicColumn;
import com.netspective.axiom.schema.table.TablesCollection;
import com.netspective.axiom.schema.table.IndexesCollection;
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.column.type.GuidColumn;
import com.netspective.axiom.value.DatabasePolicyValueContext;
import com.netspective.commons.text.GloballyUniqueIdentifier;
import com.netspective.commons.text.JavaExpressionText;
import com.netspective.commons.text.TextUtils;

public class AnsiDatabasePolicy implements DatabasePolicy
{
    private static final Log log = LogFactory.getLog(AnsiDatabasePolicy.class);

    /* --------------------------------------------------------------------------------------------------------------*/

    protected class AnsiSqlDdlFormats implements SqlDdlFormats
    {
        private String scriptStatementTerminator;
        private String createTableClauseFormat;
        private String dropTableStatementFormat;
        private String createIndexStatementFormat;
        private String dropIndexStatementFormat;
        private String createSequenceStatementFormat;
        private String dropSequenceStatementFormat;
        private String fkeyConstraintAlterTableStatementFormat;
        private String fkeyConstraintTableClauseFormat;
        private boolean createPrimaryKeyIndex;
        private boolean createParentKeyIndex;

        public AnsiSqlDdlFormats()
        {
            setScriptStatementTerminator(";");
            setCreateTableClauseFormat("CREATE TABLE ${table.name}");
            setDropTableStatementFormat("DROP TABLE ${table.name}");
            setCreateIndexStatementFormat("CREATE ${index.type} INDEX ${index.name} on ${index.table.name} (${index.columns.getNamesDelimited(', ')})");
            setDropIndexStatementFormat("DROP INDEX ${index.name}");
            setCreateSequenceStatementFormat("CREATE SEQUENCE ${column.sequenceName} increment 1 start 1");
            setDropSequenceStatementFormat("DROP SEQUENCE ${column.sequenceName}");
            setFkeyConstraintTableClauseFormat("CONSTRAINT ${fkey.constraintName} FOREIGN KEY (${fkey.sourceColumn.name}) REFERENCES ${fkey.referencedColumn.table.name} (${fkey.referencedColumn.name}) ON DELETE CASCADE");
            setFkeyConstraintAlterTableStatementFormat("ALTER TABLE ${fkey.sourceColumn.table.name} ADD " + getFkeyConstraintTableClauseFormat());
            setCreatePrimaryKeyIndex(true);
            setCreateParentKeyIndex(true);
        }

        public String getCreateTableClauseFormat()
        {
            return createTableClauseFormat;
        }

        public void setCreateTableClauseFormat(String createTableClauseFormat)
        {
            this.createTableClauseFormat = createTableClauseFormat;
        }

        public String getDropTableStatementFormat()
        {
            return dropTableStatementFormat;
        }

        public String getCreateIndexStatementFormat()
        {
            return createIndexStatementFormat;
        }

        public String getDropIndexStatementFormat()
        {
            return dropIndexStatementFormat;
        }

        public void setCreateIndexStatementFormat(String createIndexStatementFormat)
        {
            this.createIndexStatementFormat = createIndexStatementFormat;
        }

        public void setDropIndexStatementFormat(String dropIndexStatementFormat)
        {
            this.dropIndexStatementFormat = dropIndexStatementFormat;
        }

        public void setDropTableStatementFormat(String dropTableStatementFormat)
        {
            this.dropTableStatementFormat = dropTableStatementFormat;
        }

        public String getCreateSequenceStatementFormat()
        {
            return createSequenceStatementFormat;
        }

        public void setCreateSequenceStatementFormat(String createSequenceStatementFormat)
        {
            this.createSequenceStatementFormat = createSequenceStatementFormat;
        }

        public String getDropSequenceStatementFormat()
        {
            return dropSequenceStatementFormat;
        }

        public void setDropSequenceStatementFormat(String dropSequenceStatementFormat)
        {
            this.dropSequenceStatementFormat = dropSequenceStatementFormat;
        }

        public String getfkeyConstraintAlterTableStatementFormat()
        {
            return fkeyConstraintAlterTableStatementFormat;
        }

        public void setFkeyConstraintAlterTableStatementFormat(String fKeyConstraintAlterTableStatementFormat)
        {
            this.fkeyConstraintAlterTableStatementFormat = fKeyConstraintAlterTableStatementFormat;
        }

        public String getFkeyConstraintTableClauseFormat()
        {
            return fkeyConstraintTableClauseFormat;
        }

        public void setFkeyConstraintTableClauseFormat(String fKeyConstraintTableClauseFormat)
        {
            this.fkeyConstraintTableClauseFormat = fKeyConstraintTableClauseFormat;
        }

        public String getScriptStatementTerminator()
        {
            return scriptStatementTerminator;
        }

        public void setScriptStatementTerminator(String scriptStatementTerminator)
        {
            this.scriptStatementTerminator = scriptStatementTerminator;
        }

        public boolean isCreatePrimaryKeyIndex()
        {
            return createPrimaryKeyIndex;
        }

        public void setCreatePrimaryKeyIndex(boolean createPrimaryKeyIndex)
        {
            this.createPrimaryKeyIndex = createPrimaryKeyIndex;
        }

        public boolean isCreateParentKeyIndex()
        {
            return createParentKeyIndex;
        }

        public void setCreateParentKeyIndex(boolean createParentKeyIndex)
        {
            this.createParentKeyIndex = createParentKeyIndex;
        }
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    private SqlDdlFormats ddlFormats = new AnsiSqlDdlFormats();

    public SqlDdlFormats getDdlFormats()
    {
        return ddlFormats;
    }

    public boolean supportsSequences()
    {
        return false;
    }

    public boolean supportsForeignKeyConstraints()
    {
        return true;
    }

    public String getDbmsIdentifier()
    {
        return DatabasePolicies.DBMSID_DEFAULT;
    }

    public String[] getDbmsIdentifiers()
    {
        return new String[] { getDbmsIdentifier() };
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
                if (rs.next())
                    value = rs.getObject(1);
            }
            finally
            {
                if (rs != null) rs.close();
            }
        }
        catch (NamingException e)
        {
            throw new SQLException(e.toString() + " [" + sql + "]");
        }
        catch (SQLException e)
        {
            throw new SQLException(e.toString() + " [" + sql + "]");
        }
        finally
        {
            if (stmt != null) stmt.close();
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

    public Object handleGUIDPreDmlInsertExecute(ConnectionContext cc, GuidColumn column) throws SQLException
    {
        try
        {
            return GloballyUniqueIdentifier.getRandomGUID(false);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new SQLException(e.toString());
        }
        catch (UnknownHostException e)
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

        writer.write("<?xml version=\"1.0\"?>\n\n");
        writer.write("<!-- Reverse engineered by Axiom\n");
        writer.write("     driver: " + dbmd.getDriverName() + "\n");
        writer.write("     driver-version: " + dbmd.getDriverVersion() + "\n");
        writer.write("     product: " + dbmd.getDatabaseProductName() + "\n");
        writer.write("     product-version: " + dbmd.getDatabaseProductVersion() + "\n");

        writer.write("     available catalogs:");
        ResultSet rs = dbmd.getCatalogs();
        while(rs.next())
        {
            writer.write(" "+rs.getObject(1).toString());
        }
        rs.close();
        writer.write("\n");

        writer.write("     available schemas:");
        rs = dbmd.getSchemas();
        while(rs.next())
        {
            writer.write(" "+rs.getObject(1).toString());
        }
        rs.close();
        writer.write("\n");
        writer.write("-->\n\n");

        writer.write("<component xmlns:xdm=\"http://www.netspective.org/Framework/Commons/XMLDataModel\">\n");
        writer.write("    <xdm:include resource=\"com/netspective/axiom/conf/axiom.xml\"/>\n");
        writer.write("    <schema name=\""+ catalog + "." + schemaPattern +"\">\n");

        Map dbmdTypeInfoByName = new HashMap();
        Map dbmdTypeInfoByJdbcType = new HashMap();
        ResultSet typesRS = dbmd.getTypeInfo();
        while (typesRS.next())
        {
            int colCount = typesRS.getMetaData().getColumnCount();
            Object[] typeInfo = new Object[colCount];
            for (int i = 1; i <= colCount; i++)
                typeInfo[i - 1] = typesRS.getObject(i);
            dbmdTypeInfoByName.put(typesRS.getString(1), typeInfo);
            dbmdTypeInfoByJdbcType.put(new Integer(typesRS.getInt(2)), typeInfo);
        }
        typesRS.close();

        ResultSet tables = dbmd.getTables(catalog, schemaPattern, null, new String[]{"TABLE"});
        while (tables.next())
        {
            String tableNameOrig = tables.getString(3);
            String tableName = TextUtils.fixupTableNameCase(tableNameOrig);

            writer.write("        <table name=\""+ tableName + "\">\n");

            Map primaryKeys = new HashMap();
            try
            {
                ResultSet pkRS = dbmd.getPrimaryKeys(null, null, tableNameOrig);
                while (pkRS.next())
                {
                    primaryKeys.put(pkRS.getString(4), pkRS.getString(5));
                }
                pkRS.close();
            }
            catch (Exception e)
            {
                // driver may not support this function
            }

            Map fKeys = new HashMap();
            try
            {
                ResultSet fkRS = dbmd.getImportedKeys(null, null, tableNameOrig);
                while (fkRS.next())
                {
                    fKeys.put(fkRS.getString(8), TextUtils.fixupTableNameCase(fkRS.getString(3)) + "." + fkRS.getString(4).toLowerCase());
                }
                fkRS.close();
            }
            catch (Exception e)
            {
                // driver may not support this function
            }

            // we keep track of processed columns so we don't duplicate them in the XML
            Set processedColsMap = new HashSet();
            ResultSet columns = dbmd.getColumns(null, null, tableNameOrig, null);
            while (columns.next())
            {
                String columnNameOrig = columns.getString(4);
                if (processedColsMap.contains(columnNameOrig))
                    continue;
                processedColsMap.add(columnNameOrig);

                String columnName = columnNameOrig.toLowerCase();

                writer.write("            <column name=\""+ columnName + "\"");
                try
                {
                    if (fKeys.containsKey(columnNameOrig))
                        writer.write(" lookup-ref=\""+ fKeys.get(columnNameOrig) +"\"");
                    else
                    {
                        short jdbcType = columns.getShort(5);
                        String dataType = (String) dataTypesMap.get(new Integer(jdbcType));
                        if (dataType == null) dataType = Short.toString(jdbcType);
                        writer.write(" type=\""+ dataType +"\"");
                    }

                    if (primaryKeys.containsKey(columnNameOrig))
                        writer.write(" primary-key=\"yes\"");

                    if (columns.getString(18).equals("NO"))
                        writer.write(" required=\"yes\"");

                    String defaultValue = columns.getString(13);
                    if(defaultValue != null)
                        writer.write(" default=\""+ defaultValue +"\"");

                    String remarks = columns.getString(12);
                    if(remarks != null)
                        writer.write(" descr=\""+ remarks +"\"");

                }
                catch (Exception e)
                {
                }

                writer.write("/>\n");
            }
            columns.close();

            writer.write("        </table>\n");
        }
        tables.close();

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

                if(bindNum  > 1)
                    result.append(", ");

                result.append(" ["+ i + ", " + bindNum +"] ");
                result.append(bindValue);
                result.append(" ("+ bindValues[i].getClass() +")");

                bindNum++;
            }
        }

        return result.toString();
    }

    protected void executeAndRecordStatistics(ConnectionContext cc, QueryExecutionLog qel, String identifer, String sql, Object[] bindValues, Object[] addlParams) throws NamingException, SQLException
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
            stmt.execute();
            logEntry.registerExecSqlEndSuccess();
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

    protected void executeAndIgnoreStatistics(ConnectionContext cc, String sql, Object[] bindValues, Object[] addlParams) throws NamingException, SQLException
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

            stmt.execute();
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

        for (int i = 0; i < columnsCount; i++)
        {
            ColumnValue value = columnValues.getByColumnIndex(i);
            Column column = value.getColumn();
            Object bindValue = value.getValueForSqlBindParam();

            if(column instanceof GeneratedValueColumn)
            {
                GeneratedValueColumn generator = (GeneratedValueColumn) column;
                generators[i] = generator;
                haveGenerators = true;

                if(! generator.retainValueInDml(cc)) continue;
                bindValue = generator.handlePreDmlExecute(cc);
            }

            if(execute && (column instanceof ColumnInsertListener))
            {
                ((ColumnInsertListener) column).beforeInsert(cc, flags, value, columnValues);
                colListeners[i] = (ColumnInsertListener) column;
                haveColListeners = true;
            }

            if (!isFirstColumn)
            {
                namesSql.append(", ");
                valuesSql.append(", ");
            }

            isFirstColumn = false;
            namesSql.append(column.getName());

            if(value.isSqlExpr())
                valuesSql.append(value.getSqlExprs().getByDbms(this).getSql(cc));
            else
            {
                if (bindValue == null)
                    valuesSql.append("NULL");
                else if((flags & DMLFLAG_USE_BIND_PARAMS) != 0)
                {
                    valuesSql.append("? /* "+ i +" */");
                    bindValues[i] = bindValue;
                }
                else
                    valuesSql.append(column.formatSqlLiteral(bindValue));
            }
        }

        Table table = columnValues.getByColumnIndex(0).getColumn().getTable();
        String sql = "insert into " + table.getName() + " (" + namesSql + ") values (" + valuesSql + ")";

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

        for (int i = 0; i < columnsCount; i++)
        {
            ColumnValue value = columnValues.getByColumnIndex(i);
            Column column = value.getColumn();
            Object bindValue = value.getValueForSqlBindParam();

            if(execute && (column instanceof ColumnUpdateListener))
            {
                ((ColumnUpdateListener) column).beforeUpdate(cc, flags, value, columnValues);
                colListeners[i] = (ColumnUpdateListener) column;
                haveColListeners = true;
            }

            if (i != 0)
                setsSql.append(", ");

            setsSql.append(column.getName());
            setsSql.append(" = ");

            if(value.isSqlExpr())
                setsSql.append(value.getSqlExprs().getByDbms(cc.getDatabasePolicy()).getSql(cc));
            else
            {
                if (bindValue == null)
                    setsSql.append("NULL");
                else if((flags & DMLFLAG_USE_BIND_PARAMS) != 0)
                {
                    setsSql.append("?");
                    bindValues[i] = bindValue;
                }
                else
                    setsSql.append(column.formatSqlLiteral(bindValue));
            }
        }

        Table table = columnValues.getByColumnIndex(0).getColumn().getTable();

        String sql = "update " + table.getName() + " set " + setsSql;
        if (whereCond != null)
            sql += " where " + whereCond;

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
            for (int i = 0; i < columnsCount; i++)
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

        String sql = "delete from " + table.getName();
        if (whereCond != null)
            sql += " where " + whereCond;

        if(execute)
        {
            if(rowListener != null)
                rowListener.beforeDelete(cc, flags, columnValues);

            if(log.isInfoEnabled())
                executeAndRecordStatistics(cc, table.getDmlExecutionLog(), table.getName() + ".delete()", sql, null, whereCondBindParams);
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

            if(rowListener != null)
                rowListener.afterDelete(cc, flags, columnValues);
        }

        return sql;
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    public QueryDefnSelectStmtGenerator createSelectStatementGenerator(QueryDefnSelect queryDefnSelect)
    {
        return new QueryDefnAnsiSelectStmtGenerator(this, queryDefnSelect);
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    public void generateSqlDdl(File output, DatabasePolicyValueContext vc, Schema schema, boolean dropFirst) throws IOException
    {
        Writer writer = new FileWriter(output);
        try
        {
            generateSqlDdl(writer, vc, schema, dropFirst);
        }
        finally
        {
            if(writer != null) writer.close();
        }
    }

    public void generateSqlDdl(Writer writer, DatabasePolicyValueContext vc, Schema schema, boolean dropFirst) throws IOException
    {
        SqlDdlGeneratorContext gc = new SqlDdlGeneratorContext(writer, vc, schema, dropFirst);
        renderSqlDdlSchemaScript(gc);
    }

    /* --------------------------------------------------------------------------------------------------------------*/

    public class SqlDdlGeneratorContext
    {
        private Writer writer;
        private DatabasePolicyValueContext valueContext;
        private Schema schema;
        private boolean dropObjectsFirst;
        private Set visitedTables = new HashSet();
        private Set delayedConstraints = new HashSet();

        public SqlDdlGeneratorContext(Writer writer, DatabasePolicyValueContext vc, Schema schema, boolean dropObjectsFirst)
        {
            this.writer = writer;
            this.valueContext = vc;
            this.schema = schema;
            this.dropObjectsFirst = dropObjectsFirst;
        }
    }

    public void renderSqlDdlSchemaScript(SqlDdlGeneratorContext gc) throws IOException
    {
        Writer writer = gc.writer;
        SqlDdlFormats ddlFormats = getDdlFormats();
        Tables tablesWithData = new TablesCollection();

        Tables tables = gc.schema.getTables();
        for(int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);

            if(i > 0)
                writer.write("\n");

            if(gc.dropObjectsFirst)
            {
                if(supportsSequences())
                    renderSqlDdlSequenceStatements(gc, table, true);

                renderSqlDdlIndexStatements(gc, table, true);

                if(renderSqlDdlTableStatement(gc, table, true))
                {
                    writer.write(ddlFormats.getScriptStatementTerminator());
                    writer.write("\n");
                }
            }

            if(supportsSequences())
                renderSqlDdlSequenceStatements(gc, table, false);

            if(renderSqlDdlTableStatement(gc, table, false))
            {
                writer.write(ddlFormats.getScriptStatementTerminator());
                writer.write("\n");
            }

            renderSqlDdlIndexStatements(gc, table, false);

            gc.visitedTables.add(table);

            if(table.getData() != null)
                tablesWithData.add(table);
        }

        if(gc.delayedConstraints.size() > 0)
        {
            writer.write("\n");
            for(Iterator iter = gc.delayedConstraints.iterator(); iter.hasNext(); )
            {
                ForeignKey fKey = (ForeignKey) iter.next();
                renderSqlDdlConstraintClause(gc, null, fKey);
                writer.write(ddlFormats.getScriptStatementTerminator());
                writer.write("\n");
            }
        }

        if(tablesWithData.size() > 0)
        {
            for(int i = 0; i < tablesWithData.size(); i++)
            {
                Table table = tablesWithData.get(i);
                Rows dataRows = table.getData();

                writer.write("\n");
                try
                {
                    for(int dr = 0; dr < dataRows.size(); dr++)
                    {
                        Row dataRow = dataRows.getRow(dr);
                        String sql = insertValues(null, 0, dataRow.getColumnValues(), null);
                        writer.write(sql);
                        writer.write(ddlFormats.getScriptStatementTerminator());
                        writer.write("\n");
                    }
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean renderSqlDdlSequenceStatements(SqlDdlGeneratorContext gc, Table table, boolean isDropSql) throws IOException
    {
        Writer writer = gc.writer;
        SqlDdlFormats ddlFormats = getDdlFormats();

        String format = isDropSql ? ddlFormats.getDropSequenceStatementFormat() : ddlFormats.getCreateSequenceStatementFormat();

        int seqCount = 0;
        Map vars = new HashMap();
        vars.put("table", table);

        JavaExpressionText jet = new JavaExpressionText(format, vars);

        try
        {
            Columns columns = table.getColumns();
            for(int i = 0; i < columns.size(); i++)
            {
                Column column = columns.get(i);
                if(column instanceof AutoIncColumn)
                {
                    jet.getJexlContext().getVars().put("column", column);
                    String clause = jet.getFinalText(gc.valueContext);

                    writer.write(clause);
                    writer.write(ddlFormats.getScriptStatementTerminator());
                    writer.write("\n");

                    seqCount++;
                }
            }
        }
        catch (Exception e)
        {
            log.error("Error in "+ AnsiDatabasePolicy.class +".renderSqlDdlSequenceStatements(): " + e.getMessage(), e);
            throw new IOException(e.toString());
        }

        return seqCount > 0;
    }

    public boolean renderSqlDdlIndexStatements(SqlDdlGeneratorContext gc, Table table, boolean isDropSql) throws IOException
    {
        Writer writer = gc.writer;
        SqlDdlFormats ddlFormats = getDdlFormats();

        String format = isDropSql ? ddlFormats.getDropIndexStatementFormat() : ddlFormats.getCreateIndexStatementFormat();

        Map vars = new HashMap();
        vars.put("table", table);

        JavaExpressionText jet = new JavaExpressionText(format, vars);

        Indexes tableIndexes = table.getIndexes();
        Indexes createIndexes = new IndexesCollection();
        createIndexes.merge(tableIndexes);

        if(ddlFormats.isCreatePrimaryKeyIndex())
        {
            PrimaryKeyColumns pkCols = table.getPrimaryKeyColumns();
            if(pkCols != null && pkCols.size() > 0)
            {
                Index index = table.createIndex();
                index.setName("PK_" + table.getName());
                index.setColumns(pkCols.getOnlyNames(","));
                createIndexes.add(index);
            }
        }

        if(ddlFormats.isCreateParentKeyIndex())
        {
            Columns prCols = table.getParentRefColumns();
            if(prCols != null && prCols.size() > 0)
            {
                for(int i = 0; i < prCols.size(); i++)
                {
                    Index index = table.createIndex();
                    index.setName("PR_" + table.getAbbrev() + "_" + prCols.get(i).getName());
                    index.setColumns(prCols.get(i).getName());
                    createIndexes.add(index);
                }
            }
        }

        try
        {
            for(int i = 0; i < createIndexes.size(); i++)
            {
                Index index = createIndexes.get(i);
                jet.getJexlContext().getVars().put("index", index);

                String statement = jet.getFinalText(gc.valueContext);

                writer.write(statement);
                writer.write(ddlFormats.getScriptStatementTerminator());
                writer.write("\n");
            }
        }
        catch (Exception e)
        {
            log.error("Error in "+ AnsiDatabasePolicy.class +".renderSqlDdlIndexStatements(): " + e.getMessage(), e);
            throw new IOException(e.toString());
        }

        return createIndexes.size() > 0;
    }

    public void renderSqlDdlConstraintClause(SqlDdlGeneratorContext gc, Table table, ForeignKey fkey) throws IOException
    {
        Writer writer = gc.writer;
        SqlDdlFormats ddlFormats = getDdlFormats();

        String format = table != null ? ddlFormats.getFkeyConstraintTableClauseFormat() : ddlFormats.getfkeyConstraintAlterTableStatementFormat();
        if(format == null)
            return;

        Map vars = new HashMap();
        vars.put("table", fkey);
        vars.put("fkey", fkey);

        JavaExpressionText jet = new JavaExpressionText(format, vars);
        writer.write(jet.getFinalText(gc.valueContext));
    }

    public boolean renderSqlDdlTableStatement(SqlDdlGeneratorContext gc, Table table, boolean isDropSql) throws IOException
    {
        if(table.getColumns().size() == 0)
            return false;

        Writer writer = gc.writer;
        SqlDdlFormats ddlFormats = getDdlFormats();

        String format = isDropSql ? ddlFormats.getDropTableStatementFormat() : ddlFormats.getCreateTableClauseFormat();
        if(format == null)
            return false;

        Map vars = new HashMap();
        vars.put("table", table);

        Set tableConstraints = new HashSet();
        Set tableDelayedConstraints = new HashSet();

        JavaExpressionText jet = new JavaExpressionText(format, vars);
        try
        {
            writer.write(jet.getFinalText(gc.valueContext));

            // the rest of the text from here on is for create table, not drop table
            if(isDropSql)
                return true;

            writer.write("\n");
            writer.write("(\n");

            final String indent = "    ";

            Columns columns = table.getColumns();
            int lastColumn = columns.size() - 1;
            for(int i = 0; i < columns.size(); i++)
            {
                Column column = columns.get(i);

                writer.write(indent);
                renderSqlDdlColumnCreateClause(gc, column);

                if(supportsForeignKeyConstraints())
                {
                    ForeignKey fKey = column.getForeignKey();
                    if(fKey != null)
                    {
                        /*
                          1) if visitedTables is not provided, we'll assume that we'll place the foreign key contraints inside the table
                          2) if visitedTables is provided, we'll assume that we'll only place the fkey constraints inside the table if the
                             table has already been defined; otherwise, we'll assume fkeys will be created later using "alter table"
                        */

                        if(gc.visitedTables.contains(fKey.getReferencedColumn().getTable()))
                            tableConstraints.add(fKey);
                        else
                        {
                            gc.delayedConstraints.add(fKey);
                            tableDelayedConstraints.add(fKey);
                        }
                    }
                }

                if(i < lastColumn)
                    writer.write(",");
                else if(i == lastColumn && tableConstraints.size() > 0)
                    writer.write(",");

                writer.write(" /* ");
                writer.write(TextUtils.getRelativeClassName(BasicColumn.class, column.getClass()));
                writer.write(" */");

                writer.write("\n");
            }

            if(tableConstraints.size() > 0)
            {
                writer.write("\n");

                int lastConstr = tableConstraints.size() - 1;
                int constrIndex = 0;
                for(Iterator constr = tableConstraints.iterator(); constr.hasNext(); )
                {
                    ForeignKey fKey = (ForeignKey) constr.next();

                    writer.write(indent);

                    renderSqlDdlConstraintClause(gc, table, fKey);
                    if(constrIndex < lastConstr)
                        writer.write(",");

                    writer.write("\n");
                    constrIndex++;
                }

                if(tableDelayedConstraints.size() > 0)
                {
                    writer.write("\n");

                    for(Iterator constr = tableDelayedConstraints.iterator(); constr.hasNext(); )
                    {
                        ForeignKey fKey = (ForeignKey) constr.next();

                        writer.write(indent);

                        writer.write("/* DELAYED: ");
                        renderSqlDdlConstraintClause(gc, table, fKey);
                        writer.write(" ("+ fKey.getReferencedColumn().getTable().getName() +" table not created yet) */");

                        writer.write("\n");
                        constrIndex++;
                    }
                }
            }

            writer.write(")");
        }
        catch (Exception e)
        {
            log.error("Error in "+ AnsiDatabasePolicy.class +".renderSqlDdlTableStatement(): " + e.getMessage(), e);
            throw new IOException(e.toString());
        }

        return true;
    }

    public void renderSqlDdlColumnCreateClause(SqlDdlGeneratorContext gc, Column column) throws IOException
    {
        Writer writer = gc.writer;

        SqlDataDefns sqlDataDefns = column.getSqlDdl();
        DbmsSqlText defineExpr = sqlDataDefns.getSqlDefns().getByDbmsOrAnsi(this);
        DbmsSqlText defaultExpr = sqlDataDefns.getDefaultSqlExprValues().getByDbmsOrAnsi(this);

        writer.write(column.getName());
        writer.write(" ");

        if(defineExpr != null)
            writer.write(defineExpr.getSql(gc.valueContext).toUpperCase());
        else
            writer.write("No definition found in column '"+ column +"' for policy '"+ getDbmsIdentifier() +"' or ANSI. Available: " + sqlDataDefns.getSqlDefns().getAvailableDbmsIds());

        if(defaultExpr != null)
        {
            writer.write(" DEFAULT ");
            writer.write(column.formatSqlLiteral(defaultExpr.getSql(gc.valueContext)));
        }

        if(column.isPrimaryKey())
            writer.write(" PRIMARY KEY");

        if(column.isRequiredByDbms())
            writer.write(" NOT NULL");
    }
}
