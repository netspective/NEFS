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
 * $Id: QueryDefnAnsiSelectStmtGenerator.java,v 1.5 2003-11-22 04:52:20 roque.hernandez Exp $
 */

package com.netspective.axiom.policy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.axiom.sql.dynamic.*;
import com.netspective.axiom.DatabasePolicy;

class QueryDefnAnsiSelectStmtGenerator implements QueryDefnSelectStmtGenerator
{
    private DatabasePolicy policy;
    private QueryDefinition queryDefn;
    private QueryDefnSelect select;
    private Set joins = new HashSet();
    private List selectClause = new ArrayList();
    private List fromClause = new ArrayList();
    private List fromClauseComments = new ArrayList();
    private List whereJoinClause = new ArrayList();
    private List bindParams = new ArrayList();

    protected QueryDefnAnsiSelectStmtGenerator(DatabasePolicy policy, QueryDefnSelect select)
    {
        this.policy = policy;
        this.queryDefn = select.getQueryDefn();
        this.select = select;
    }

    public DatabasePolicy getPolicy()
    {
        return policy;
    }

    public QueryDefnSelect getQuerySelect()
    {
        return select;
    }

    public List getBindParams()
    {
        return bindParams;
    }

    public void addJoin(QueryDefnField field) throws QueryDefinitionException
    {
        QueryDefnJoin join = field.getJoin();
        this.addJoin(join, false, null);
    }

    public void addJoin(QueryDefnJoin join, boolean autoInc, QueryDefnJoin impliedBy) throws QueryDefinitionException
    {
        if(join == null || joins.contains(join))
            return;

        fromClause.add(join.getFromExpr());
        if(autoInc || impliedBy != null)
        {
            StringBuffer comments = new StringBuffer();
            comments.append("/* ");
            if(autoInc) comments.append("auto-included for join definition '"+ join.getName() +"'");
            if(impliedBy != null)
            {
                if(autoInc) comments.append(", ");
                comments.append("implied by join definition '"+ impliedBy.getName() +"'");
            }
            comments.append(" */");
            fromClauseComments.add(comments.toString());
        }
        else
            fromClauseComments.add(null);

        String whereCriteria = join.getCondition();
        if(whereCriteria != null)
            whereJoinClause.add(whereCriteria);
        joins.add(join);

        QueryDefnJoin[] impliedJoins = join.getImpliedJoins();
        if(impliedJoins != null && impliedJoins.length > 0)
        {
            for(int i = 0; i < impliedJoins.length; i++)
                addJoin(impliedJoins[i], autoInc, join);
        }
    }

    public void addParam(ValueSource bindParam)
    {
        bindParams.add(bindParam);
    }

    public String generateSql(ValueContext vc) throws QueryDefinitionException
    {
        QueryDefnFields showFields = select.getDisplayFields();
        int showFieldsCount = showFields.size();
        if(showFieldsCount > 0)
        {
            for(int sf = 0; sf < showFieldsCount; sf++)
            {
                QueryDefnField field = showFields.get(sf);
                String selClauseAndLabel = field.getSelectClauseExprAndLabel();
                if(selClauseAndLabel != null)
                    selectClause.add(field.getSelectClauseExprAndLabel());
                addJoin(field);
            }
        }
        else
            selectClause.add("*");

        QueryDefnConditions allSelectConditions = select.getConditions();
        QueryDefnConditions usedSelectConditions = allSelectConditions.getUsedConditions(this, vc);

        // add join tables which have the auto-include flag set and their respective conditions to the
        // from and where clause lists. If the join is already in the 'joins' list, no need to add it in.
        List autoIncJoinList = this.queryDefn.getJoins().getAutoIncludeJoins();
        for(Iterator it = autoIncJoinList.iterator(); it.hasNext();)
        {
            this.addJoin((QueryDefnJoin) it.next(), true, null);
        }

        StringBuffer sql = new StringBuffer();

        int selectCount = selectClause.size();
        int selectLast = selectCount - 1;
        sql.append("select ");
        if(select.distinctRowsOnly())
            sql.append("distinct \n");
        else
            sql.append("\n");
        for(int sc = 0; sc < selectCount; sc++)
        {
            sql.append("  " + selectClause.get(sc));
            if(sc != selectLast)
                sql.append(", ");
            sql.append("\n");
        }

        int fromCount = fromClause.size();
        int fromLast = fromCount - 1;
        sql.append("from \n");
        for(int fc = 0; fc < fromCount; fc++)
        {
            sql.append("  " + getSchemaPrefix() + fromClause.get(fc));
            if(fc != fromLast)
                sql.append(",");
            String comments = (String) fromClauseComments.get(fc);
            if(comments != null)
            {
                sql.append(" ");
                sql.append(comments);
            }
            sql.append("\n");
        }

        StringBuffer whereClauseSql = new StringBuffer();

        boolean haveJoinWheres = false;
        int whereCount = whereJoinClause.size();
        int whereLast = whereCount - 1;
        if(whereCount > 0)
        {
            whereClauseSql.append("where\n  (\n");
            for(int wc = 0; wc < whereCount; wc++)
            {
                whereClauseSql.append("  " + whereJoinClause.get(wc));
                if(wc != whereLast)
                    whereClauseSql.append(" and ");
                whereClauseSql.append("\n");
            }
            whereClauseSql.append("  )");
            haveJoinWheres = true;
        }

        boolean haveCondWheres = false;
        int usedCondsCount = usedSelectConditions.size();
        if(usedCondsCount > 0)
        {
            String conditionSql = usedSelectConditions.createSql(this, vc, usedSelectConditions);
            if (conditionSql != null && conditionSql.length() > 0)
            {
                if (haveJoinWheres)
                {
                    whereClauseSql.append(" and (\n");
                }
                else
                {
                    whereClauseSql.append("where\n  (\n");
                }
                whereClauseSql.append(conditionSql + "  )\n");
            }
            haveCondWheres = true;
        }

        QueryDefnSqlWhereExpressions whereExprs = select.getWhereExprs();
        if(whereExprs != null && whereExprs.size() > 0)
        {
            boolean first = false;
            if(!haveJoinWheres && !haveCondWheres)
            {
                whereClauseSql.append("where\n  (\n");
                first = true;
            }

            int whereExprsCount = whereExprs.size();
            for(int we = 0; we < whereExprsCount; we++)
            {
                QueryDefnSqlWhereExpression expr = whereExprs.get(we);
                if(first)
                    first = false;
                else
                    whereClauseSql.append(expr.getConnectorSql());

                whereClauseSql.append(" (");
                whereClauseSql.append(expr.getWhereCondExpr(this, vc));
                whereClauseSql.append("  )\n");
            }
        }

        // save this because some callers might need just the where clause
        sql.append(whereClauseSql);

        QueryDefnFields groupBys = select.getGroupByFields();
        int groupBysCount = groupBys.size();
        if(groupBysCount > 0)
        {
            int groupByLast = groupBysCount - 1;
            sql.append("group by\n");
            for(int gb = 0; gb < groupBysCount; gb++)
            {
                QueryDefnField field = groupBys.get(gb);
                sql.append("  " + field.getQualifiedColName());
                if(gb != groupByLast)
                {
                    sql.append(", ");
                }
                sql.append("\n");
            }

        }

        QueryDefnSortFieldReferences orderBys = select.getOrderByFieldRefs();
        int orderBysCount = orderBys.size();
        int orderBysLast = orderBysCount - 1;
        if(orderBysCount > 0)
        {
            sql.append("order by\n");
            for(int ob = 0; ob < orderBysCount; ob++)
            {
                QueryDefnSortFieldReference sortRef = orderBys.get(ob);
                QueryDefinition.QueryFieldSortInfo[] fields = sortRef.getFields(vc);
                int lastField = fields.length - 1;
                for(int i = 0; i < fields.length; i++)
                {
                    QueryDefinition.QueryFieldSortInfo fieldSortInfo = fields[i];

                    sql.append("  " + fieldSortInfo.getField().getOrderByExpr());
                    if(fieldSortInfo.isDescending())
                        sql.append(" desc");

                    if(i != lastField)
                    {
                        sql.append(", ");
                        sql.append("\n");
                    }
                }

                if(ob != orderBysLast)
                    sql.append(", ");
                sql.append("\n");
            }
        }

        return sql.toString();
    }

    private String getSchemaPrefix()
    {
        if ( policy.isPrefixTableNamesWithSchemaName() && (select instanceof TableQueryDefnSelect) ) {
            return ((TableQueryDefnSelect)select).getOwner().getSchema().getName() + ".";
        } else
            return "";
    }
}
