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
import java.util.StringTokenizer;

import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.axiom.sql.dynamic.exception.QueryDefnJoinNotFoundException;

/**
 * Class for handling the join conditions defined within a query-defn using &lt;join&gt; tag.
 * Join tag is used to let the query definition know of the list of tables and joins.
 * This is necessary to be able to get all the fields that are a part of the query
 * definition.  In the context of the final SQL statement that Sparx generates,
 * the join tags become a part of the where clause to signify the relationships
 * between tables (if any exists).
 */
public class QueryDefnJoin
{
    private QueryDefinition owner;
    private String name;
    private String tableName;
    private String fromClauseExpr;
    private String condition;
    private boolean autoInclude;
    private String implyJoinsStr;
    private QueryDefnJoin[] implyJoins;

    public QueryDefnJoin(QueryDefinition owner)
    {
        this.owner = owner;
    }

    public String getName()
    {
        return name;
    }

    public String getTable()
    {
        return tableName == null ? name : tableName;
    }

    public String getCondition()
    {
        return condition;
    }

    public String getFromExpr()
    {
        String tableName = getTable();
        return fromClauseExpr != null ? fromClauseExpr : (tableName.equals(name) ? tableName : (tableName + " " + name));
    }

    public QueryDefnJoin[] getImpliedJoins() throws QueryDefinitionException
    {
        if(implyJoinsStr != null && implyJoins == null)
        {
            StringTokenizer st = new StringTokenizer(implyJoinsStr, ",");
            List implyJoinsList = new ArrayList();
            while(st.hasMoreTokens())
            {
                String join = st.nextToken();
                QueryDefnJoin joinDefn = owner.getJoins().get(join);
                if(joinDefn == null)
                    throw new QueryDefnJoinNotFoundException(owner, join, "implied join '" + join + "' not found in join '" + getName() + "'");
                implyJoinsList.add(joinDefn);
            }
            implyJoins = (QueryDefnJoin[]) implyJoinsList.toArray(new QueryDefnJoin[implyJoinsList.size()]);
        }
        return implyJoins;
    }

    public boolean shouldAutoInclude()
    {
        return autoInclude;
    }

    /**
     * Sets the name to be used to uniquely identify a join.
     *
     * @param name unique name of the join
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the schema table name to be used in this join.
     *
     * @param tableName schema table name
     */
    public void setTable(String tableName)
    {
        this.tableName = tableName;
    }

    public void setFromExpr(String fromClauseExpr)
    {
        this.fromClauseExpr = fromClauseExpr;
    }

    /**
     * Defines the join condition.
     *
     * @param condition string containing the join condition
     */
    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    public void setAutoInclude(boolean autoInclude)
    {
        this.autoInclude = autoInclude;
    }

    public void setImplyJoin(String implyJoinsStr)
    {
        this.implyJoinsStr = implyJoinsStr;
    }

    public void setImplyJoins(QueryDefnJoin[] implyJoins)
    {
        this.implyJoins = implyJoins;
    }
}