package com.netspective.axiom.sql.dynamic.comparison;

import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.dynamic.QueryDefnSelectStmtGenerator;
import com.netspective.axiom.sql.dynamic.QueryDefnCondition;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;

/**
 * SQL Comparison class for letting the user explicitly define what the LIKE expression should be. This is a more
 * flexible comparison than the specific LIKE comparisons such as {@link ContainsComparison} and
 * {@link EndsWithComparison}. By default, this comparison is not case sensitive.
 */
public class LikeExpressionComparison extends BinaryOpComparison
{
    public LikeExpressionComparison()
    {
        super("like", "like", "string");
    }

    public LikeExpressionComparison(String name, String caption, String group)
    {
        super(name, caption, group);
    }

    public LikeExpressionComparison(String name, String caption, String group, String sqlExpr)
    {
        super(name, caption, group, sqlExpr);
    }

    public String getWhereCondExpr(ValueContext vc, QueryDefnSelect select, QueryDefnSelectStmtGenerator statement, QueryDefnCondition cond) throws QueryDefinitionException
    {
        ValueSource bindParam = null;
        String textValue = cond.getValue().getTextValue(vc);
        if (textValue == null || textValue.trim().length() == 0)
        {
            bindParam = new StaticValueSource("%");
        }
        else
        {
            bindParam = cond.getValue();
        }
        statement.addParam(bindParam);
        String retString = "";
        String bindExpression = cond.getBindExpr();
        if (bindExpression != null && bindExpression.length() > 0)
        {
            retString = cond.getField().getWhereExpr() + " like UPPER(" + bindExpression + ")";
        }
        else
            retString = cond.getField().getWhereExpr() + " like UPPER(?)";
        return retString;

    }
}

