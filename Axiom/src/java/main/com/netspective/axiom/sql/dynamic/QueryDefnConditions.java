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
package com.netspective.axiom.sql.dynamic;

import java.util.ArrayList;
import java.util.List;

import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.commons.value.ValueContext;

public class QueryDefnConditions
{
    private List list = new ArrayList();
    private QueryDefnCondition parentCondition;
    private boolean haveAnyDynamicConditions;

    public QueryDefnConditions()
    {
    }

    public QueryDefnConditions(QueryDefnCondition parent)
    {
        parentCondition = parent;
    }

    public QueryDefnCondition getParentCondition()
    {
        return parentCondition;
    }

    public int size()
    {
        return list.size();
    }

    public QueryDefnCondition get(int i)
    {
        return (QueryDefnCondition) list.get(i);
    }

    public void copy(QueryDefnConditions conds)
    {
        for(int i = 0; i < conds.size(); i++)
            add(conds.get(i));
    }

    /**
     * Return true if any of the conditions in this list are dynamic -- that they should be removed if their
     * bind value happens to be null;
     */
    public boolean hasAnyDynamicConditions()
    {
        return haveAnyDynamicConditions;
    }

    /**
     * Insert a new condition into the list
     *
     * @param condition the condition to insert
     */
    public void add(QueryDefnCondition condition)
    {
        list.add(condition);
        if(!haveAnyDynamicConditions && condition.removeIfValueIsNull())
            haveAnyDynamicConditions = true;
    }

    public void add(QueryDefnConditions conditions)
    {
        list.add(conditions);
    }

    public void registerDynamicConditions()
    {
        for(int i = 0; i < list.size(); i++)
            if(!haveAnyDynamicConditions && ((QueryDefnCondition) list.get(i)).removeIfValueIsNull())
                haveAnyDynamicConditions = true;
    }

    /**
     * Return the list of query conditions that were "used" or not removed because the condition is specified as
     * removeIfValueIsNull() and value of the bind parameter of the condition was null. While we are checking for
     * used conditions, we will use the QueryCondition.keepCondition method which will automatically process
     * child (nested) conditions. Also, while processing we will call the SelectStmtGenerator.addJoin method to
     * add the joins for each of the fields we're going to put into the used conditions list.
     *
     * @param stmtGen the active SelectStmtGenerator
     * @param vc      the active ValueContext
     */
    public QueryDefnConditions getUsedConditions(QueryDefnSelectStmtGenerator stmtGen, ValueContext vc) throws QueryDefinitionException
    {
        // if we don't have any dynamic conditions, all the conditions will be used :)
        if(!haveAnyDynamicConditions)
            return this;

        // if we get to here, it means only some of the query conditions will be used
        // we we need to keep track of them
        QueryDefnConditions usedConditions = new QueryDefnConditions(parentCondition);

        int allCondsCount = list.size();
        for(int c = 0; c < allCondsCount; c++)
        {
            QueryDefnCondition cond = (QueryDefnCondition) list.get(c);
            cond.useCondition(stmtGen, vc, usedConditions);
        }

        return usedConditions;
    }

    /**
     * Create the SQL string for the list of Query conditions
     *
     * @return String
     */
    public String createSql(QueryDefnSelectStmtGenerator stmtGen, ValueContext vc, QueryDefnConditions usedConditions) throws QueryDefinitionException
    {
        StringBuffer sb = new StringBuffer();
        QueryDefnSelect select = stmtGen.getQuerySelect();
        int usedCondsCount = usedConditions.list.size();
        int condsUsedLast = usedCondsCount - 1;

        for(int c = 0; c < usedCondsCount; c++)
        {
            boolean conditionAdded = false;
            Object condObj = usedConditions.list.get(c);
            if(condObj instanceof QueryDefnConditions)
            {
                String sql = createSql(stmtGen, vc, (QueryDefnConditions) condObj);
                if(sql != null && sql.length() > 0)
                {
                    sb.append("(" + sql + ")");
                    conditionAdded = true;
                }
                if(c != condsUsedLast)
                    sb.append(parentCondition.getConnectorSql());
            }
            else
            {
                // single query condition. not a list.
                QueryDefnCondition cond = (QueryDefnCondition) usedConditions.list.get(c);
                if(!cond.isJoinOnly())
                {
                    // create and add the where condition string only if this condition has a valid comparison
                    // (meaning the Select Condition was not inluded only for the sake of including the Join Condition of the table)
                    sb.append(" (" + cond.getWhereCondExpr(vc, select, stmtGen) + ")");
                    conditionAdded = true;
                }
                if(c != condsUsedLast && !((QueryDefnCondition) usedConditions.list.get(c + 1)).isJoinOnly())
                    sb.append(cond.getConnectorSql());
            }
            if(conditionAdded)
                sb.append("\n");
        }
        return sb.toString();
    }
}
