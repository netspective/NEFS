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
 * $Id: AnsiSqlDdlFormats.java,v 1.3 2004-08-09 22:29:21 shahid.shah Exp $
 */

package com.netspective.axiom.policy.ddl;

import java.util.HashMap;
import java.util.Map;

import com.netspective.axiom.policy.SqlDdlFormats;
import com.netspective.commons.text.TextUtils;

public class AnsiSqlDdlFormats implements SqlDdlFormats
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
    private String tableCommentClauseFormat;
    private String columnCommentClauseFormat;
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
        setFkeyConstraintTableClauseFormat("CONSTRAINT ${fkey.constraintName} FOREIGN KEY (${fkey.sourceColumns.getOnlyNames(', ')}) REFERENCES ${fkey.referencedColumns.first.table.name} (${fkey.referencedColumns.getOnlyNames(', ')}) ON DELETE CASCADE");
        setFkeyConstraintAlterTableStatementFormat("ALTER TABLE ${fkey.sourceColumns.first.table.name} ADD " + getFkeyConstraintTableClauseFormat());
        setCreatePrimaryKeyIndex(true);
        setCreateParentKeyIndex(true);
    }

    public Map createJavaExpressionVars()
    {
        Map result = new HashMap();
        result.put("textUtils", TextUtils.getInstance());
        return result;
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

    public String getTableCommentClauseFormat()
    {
        return tableCommentClauseFormat;
    }

    public void setTableCommentClauseFormat(String tableCommentClauseFormat)
    {
        this.tableCommentClauseFormat = tableCommentClauseFormat;
    }

    public String getColumnCommentClauseFormat()
    {
        return columnCommentClauseFormat;
    }

    public void setColumnCommentClauseFormat(String columnCommentClauseFormat)
    {
        this.columnCommentClauseFormat = columnCommentClauseFormat;
    }
}
