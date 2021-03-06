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
package com.netspective.axiom.sql;

import com.netspective.axiom.DatabasePolicies;
import com.netspective.commons.text.ExpressionText;
import com.netspective.commons.value.ValueContext;

/**
 * Contains a SQL text block that is specific to a particular DBMS. The SQL text block may contain expressions that
 * resolve to either value sources or Java expressions.
 */
public class DbmsSqlText
{
    private DbmsSqlTexts owner;
    private String dbms = DatabasePolicies.DBMSID_DEFAULT;
    private String role = null;
    private ExpressionText sqlText;
    private QueryParameters parameters;

    public DbmsSqlText(DbmsSqlTexts owner)
    {
        this.owner = owner;
    }

    public void addText(String text)
    {
        if(sqlText == null)
            setSql(text);
        else
            setSql(sqlText.getStaticExpr() + text);
    }

    public String getDbms()
    {
        return dbms;
    }

    public void setDbms(DatabasePolicies.DatabasePolicyEnumeratedAttribute dbms)
    {
        this.dbms = dbms.getValue();
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getSql()
    {
        return sqlText.getStaticExpr();
    }

    public String getSql(ValueContext vc)
    {
        return sqlText.getFinalText(vc);
    }

    public void setSql(String sqlText)
    {
        this.sqlText = owner.createExpr(this, sqlText);
    }

    public String toString()
    {
        return "dbms '" + dbms + "' (" + sqlText.getStaticExpr() + ") params (" + parameters + ")";
    }

    public QueryParameters getParams()
    {
        return parameters;
    }

    public QueryParameters createParams()
    {
        return new QueryParameters(this);
    }

    public void addParams(QueryParameters params)
    {
        this.parameters = params;
    }

    public Query getOwnerQuery()
    {
        if(owner.getOwner() instanceof Query)
            return (Query) owner.getOwner();
        else
            throw new RuntimeException(this + " is not owned by a Query.");
    }
}
