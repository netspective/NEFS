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
 * $Id: AnsiSqlDdlGenerator.java,v 1.2 2004-07-25 21:11:29 shahid.shah Exp $
 */

package com.netspective.axiom.policy.ddl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.DatabasePolicy;
import com.netspective.axiom.policy.AnsiDatabasePolicy;
import com.netspective.axiom.policy.SqlDdlFormats;
import com.netspective.axiom.policy.SqlDdlGenerator;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.Index;
import com.netspective.axiom.schema.Indexes;
import com.netspective.axiom.schema.PrimaryKeyColumns;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.column.BasicColumn;
import com.netspective.axiom.schema.column.SqlDataDefns;
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.table.IndexesCollection;
import com.netspective.axiom.schema.table.TablesCollection;
import com.netspective.axiom.sql.DbmsSqlText;
import com.netspective.axiom.value.DatabasePolicyValueContext;
import com.netspective.commons.text.JavaExpressionText;
import com.netspective.commons.text.TextUtils;

public class AnsiSqlDdlGenerator implements SqlDdlGenerator
{
    private static final Log log = LogFactory.getLog(AnsiSqlDdlGenerator.class);

    public AnsiSqlDdlGenerator()
    {
    }

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

    public void renderSqlDdlSchemaScript(SqlDdlGeneratorContext gc) throws IOException
    {
        DatabasePolicy policy = gc.getDatabasePolicy();
        Writer writer = gc.getWriter();
        SqlDdlFormats ddlFormats = gc.getSqlDdlFormats();
        Tables tablesWithData = new TablesCollection();

        Tables tables = gc.getSchema().getTables();
        for(int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);

            if(i > 0)
                writer.write("\n");

            if(gc.isDropObjectsFirst())
            {
                if(policy.supportsSequences())
                    renderSqlDdlSequenceStatements(gc, table, true);

                renderSqlDdlIndexStatements(gc, table, true);

                if(renderSqlDdlTableStatement(gc, table, true))
                {
                    writer.write(ddlFormats.getScriptStatementTerminator());
                    writer.write("\n");
                }
            }

            if(policy.supportsSequences())
                renderSqlDdlSequenceStatements(gc, table, false);

            if(renderSqlDdlTableStatement(gc, table, false))
            {
                writer.write(ddlFormats.getScriptStatementTerminator());
                writer.write("\n");
            }

            renderSqlDdlIndexStatements(gc, table, false);

            gc.getVisitedTables().add(table);

            if(table.getData() != null)
                tablesWithData.add(table);
        }

        if(gc.getDelayedConstraints().size() > 0)
        {
            writer.write("\n");
            for(Iterator iter = gc.getDelayedConstraints().iterator(); iter.hasNext(); )
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
                        String sql = policy.insertValues(null, 0, dataRow.getColumnValues(), null);
                        writer.write(sql);
                        writer.write(ddlFormats.getScriptStatementTerminator());
                        writer.write("\n");
                    }
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public boolean renderSqlDdlSequenceStatements(SqlDdlGeneratorContext gc, Table table, boolean isDropSql) throws IOException
    {
        Writer writer = gc.getWriter();
        SqlDdlFormats ddlFormats = gc.getSqlDdlFormats();

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
                    String clause = jet.getFinalText(gc.getValueContext());

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
        Writer writer = gc.getWriter();
        SqlDdlFormats ddlFormats = gc.getSqlDdlFormats();

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

                String statement = jet.getFinalText(gc.getValueContext());

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
        Writer writer = gc.getWriter();
        SqlDdlFormats ddlFormats = gc.getSqlDdlFormats();

        String format = table != null ? ddlFormats.getFkeyConstraintTableClauseFormat() : ddlFormats.getfkeyConstraintAlterTableStatementFormat();
        if(format == null)
            return;

        Map vars = new HashMap();
        vars.put("table", fkey);
        vars.put("fkey", fkey);

        JavaExpressionText jet = new JavaExpressionText(format, vars);
        writer.write(jet.getFinalText(gc.getValueContext()));
    }

    public boolean renderSqlDdlTableStatement(SqlDdlGeneratorContext gc, Table table, boolean isDropSql) throws IOException
    {
        if(table.getColumns().size() == 0)
            return false;

        Writer writer = gc.getWriter();
        SqlDdlFormats ddlFormats = gc.getSqlDdlFormats();

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
            writer.write(jet.getFinalText(gc.getValueContext()));

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

                if(gc.getDatabasePolicy().supportsForeignKeyConstraints())
                {
                    ForeignKey fKey = column.getForeignKey();
                    if(fKey != null && ! fKey.isLogical())
                    {
                        /*
                          1) if visitedTables is not provided, we'll assume that we'll place the foreign key contraints inside the table
                          2) if visitedTables is provided, we'll assume that we'll only place the fkey constraints inside the table if the
                             table has already been defined; otherwise, we'll assume fkeys will be created later using "alter table"
                        */

                        if(gc.getVisitedTables().contains(fKey.getReferencedColumns().getFirst().getTable()))
                            tableConstraints.add(fKey);
                        else
                        {
                            gc.getDelayedConstraints().add(fKey);
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
                        writer.write(" ("+ fKey.getReferencedColumns().getFirst().getTable().getName() +" table not created yet) */");

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
        Writer writer = gc.getWriter();
        DatabasePolicy policy = gc.getDatabasePolicy();

        SqlDataDefns sqlDataDefns = column.getSqlDdl();
        DbmsSqlText defineExpr = sqlDataDefns.getSqlDefns().getByDbmsOrAnsi(policy);
        DbmsSqlText defaultExpr = sqlDataDefns.getDefaultSqlExprValues().getByDbmsOrAnsi(policy);

        writer.write(column.getName());
        writer.write(" ");

        if(defineExpr != null)
            writer.write(defineExpr.getSql(gc.getValueContext()).toUpperCase());
        else
            writer.write("No definition found in column '"+ column +"' for policy '"+ policy.getDbmsIdentifier() +"' or ANSI. Available: " + sqlDataDefns.getSqlDefns().getAvailableDbmsIds());

        if(defaultExpr != null)
        {
            writer.write(" DEFAULT ");
            writer.write(defaultExpr.getSql(gc.getValueContext()));
        }

        if(column.isPrimaryKey())
            writer.write(" PRIMARY KEY");

        if(column.isRequiredByApp() || column.isRequiredByDbms())
            writer.write(" NOT NULL");
    }
}
