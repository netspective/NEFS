package com.netspective.axiom.sql.dynamic.comparison;

import java.sql.Types;

import com.netspective.axiom.sql.JdbcTypesEnumeratedAttribute;
import com.netspective.axiom.sql.dynamic.QueryDefnCondition;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.sql.dynamic.QueryDefnSelectStmtGenerator;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSource;

public class InLiteralComparison extends BinaryOpComparison
{
    public interface LiteralProducer
    {
        public String getLiteral(final String value);
    }

    public InLiteralComparison()
    {
        super("in-literal", "in-literal", "string");
    }

    public String getWhereCondExpr(final ValueContext vc, final QueryDefnSelect select, final QueryDefnSelectStmtGenerator statement, final QueryDefnCondition cond) throws QueryDefinitionException
    {
        select.setAlwaysDirty(true);
        int bindCount = 0;

        ValueSource vs = cond.getValue();
        Value value = vs.getValue(vc);
        if(value == null)
            return null;

        final JdbcTypesEnumeratedAttribute jdbcTypeEnum = cond.getBindJdbcType();
        final int jdbcType = jdbcTypeEnum == null ? Types.NULL : jdbcTypeEnum.getJdbcValue();
        final LiteralProducer lp;
        if(jdbcType == Types.INTEGER)
            lp = new LiteralProducer() {
                public String getLiteral(final String value)
                {
                    return value;
                }
            };
         else
            lp = new LiteralProducer() {
                public String getLiteral(final String value)
                {
                    return TextUtils.getInstance().createLiteral(value, "'", "'", "''", false, true, null);
                }
            };


        final StringBuffer retString = new StringBuffer(cond.getField().getWhereExpr() + " in (");

        if(value.isListValue())
        {
            String[] values = value.getTextValues();
            if(values == null || values.length == 0)
                return null;
            bindCount = values.length;
            for(int i = 0; i < bindCount; i++)
            {
                if(i != 0)
                    retString.append(", ");
                retString.append(lp.getLiteral(values[i]));
            }
        }
        else
        {
            bindCount = 1;
            retString.append(TextUtils.getInstance().createLiteral(vs.getTextValue(vc), "'", "'", "''", false, true, null));
        }

        retString.append(")");
        return retString.toString();
    }
}