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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netspective.axiom.sql.dynamic.comparison.BinaryOpComparison;
import com.netspective.axiom.sql.dynamic.comparison.ContainsComparison;
import com.netspective.axiom.sql.dynamic.comparison.ContainsComparisonIgnoreCase;
import com.netspective.axiom.sql.dynamic.comparison.DateComparison;
import com.netspective.axiom.sql.dynamic.comparison.DynamicComparison;
import com.netspective.axiom.sql.dynamic.comparison.EndsWithComparison;
import com.netspective.axiom.sql.dynamic.comparison.EndsWithComparisonIgnoreCase;
import com.netspective.axiom.sql.dynamic.comparison.InComparison;
import com.netspective.axiom.sql.dynamic.comparison.InLiteralComparison;
import com.netspective.axiom.sql.dynamic.comparison.IsDefinedComparison;
import com.netspective.axiom.sql.dynamic.comparison.LikeExpressionComparison;
import com.netspective.axiom.sql.dynamic.comparison.StartsWithComparison;
import com.netspective.axiom.sql.dynamic.comparison.StartsWithComparisonIgnoreCase;
import com.netspective.commons.text.TextUtils;
import com.netspective.commons.value.ValueSources;

public class SqlComparisonFactory
{
    private static List comparisonsList = new ArrayList();
    private static Map comparisonsMap = new HashMap();
    private static Map comparisonsListByGroup = new HashMap();

    static
    {
        registerComparison(new BinaryOpComparison("equals", "equals", "general", "="), new String[]{"is", "="});
        registerComparison(new BinaryOpComparison("not-equals", "does not equal", "general", "!="), new String[]{
            "is-not", "!="
        });
        registerComparison(new LikeExpressionComparison(), null);
        registerComparison(new StartsWithComparison(), null);
        registerComparison(new StartsWithComparisonIgnoreCase(), null);
        registerComparison(new ContainsComparison(), null);
        registerComparison(new ContainsComparisonIgnoreCase(), null);
        registerComparison(new EndsWithComparison(), null);
        registerComparison(new EndsWithComparisonIgnoreCase(), null);
        registerComparison(new InComparison(), null);
        registerComparison(new InLiteralComparison(), null);
        registerComparison(new IsDefinedComparison(), null);
        registerComparison(new BinaryOpComparison("greater-than", "greater than", "general", ">"), new String[]{
            "gt", ">"
        });
        registerComparison(new BinaryOpComparison("greater-than-equal", "greater than or equal to", "general", ">="), new String[]{
            "gte", ">="
        });
        registerComparison(new BinaryOpComparison("less-than", "less than", "general", "<"), new String[]{"lt", "<"});
        registerComparison(new BinaryOpComparison("less-than-equal", "less than or equal to", "general", "<="), new String[]{
            "lte", "<="
        });
        registerComparison(new DateComparison("lte-date", "<="), new String[]{"lte-date", "less-than-equal-date"});
        registerComparison(new DateComparison("lt-date", "<"), new String[]{"lt-date", "less-than-date"});
        registerComparison(new DateComparison("gte-date", ">="), new String[]{"gte-date", "greater-than-equal-date"});
        registerComparison(new DateComparison("gt-date", ">"), new String[]{"gt-date", "greater-than-date"});
    }

    static public void registerComparison(SqlComparison comp, String[] aliases)
    {
        comparisonsList.add(comp);
        comparisonsMap.put(comp.getName(), comp);

        List groupList = (List) comparisonsListByGroup.get(comp.getGroupName());
        if(groupList == null)
        {
            groupList = new ArrayList();
            comparisonsListByGroup.put(comp.getGroupName(), groupList);
        }
        groupList.add(comp);

        if(aliases != null)
        {
            for(int i = 0; i < aliases.length; i++)
                comparisonsMap.put(aliases[i], comp);
        }
    }

    public static String getDynamicComparisonValueSourceSpec(String name)
    {
        if(name.startsWith(DynamicComparison.DYNAMIC_ID))
        {
            String[] items = TextUtils.getInstance().split(name, ",", true);
            if(items.length == 2)
                return items[1];
            else
                return null;
        }
        else
            return null;
    }

    public static SqlComparison getComparison(String name)
    {
        String dynamicIdValueSource = getDynamicComparisonValueSourceSpec(name);
        if(dynamicIdValueSource != null)
            return new DynamicComparison("private",
                                         ValueSources.getInstance().getValueSource(dynamicIdValueSource, ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION));
        else
            return (SqlComparison) comparisonsMap.get(name);
    }

    public static String[] getComparisonIdentifiers()
    {
        return (String[]) comparisonsMap.keySet().toArray(new String[comparisonsMap.size()]);
    }

    public static List getComparisonsList()
    {
        return comparisonsList;
    }

    public static List getComparisonsList(String groupNames)
    {
        if(groupNames == null || groupNames.length() == 0)
            return new ArrayList();

        List result = (List) comparisonsListByGroup.get(groupNames);
        if(result != null)
            return result;

        result = new ArrayList();

        String[] groups = TextUtils.getInstance().split(groupNames, ",", true);
        for(int i = 0; i < groups.length; i++)
        {
            List list = (List) comparisonsListByGroup.get(groups[i]);
            result.addAll(list);
        }

        // store it for future use
        comparisonsListByGroup.put(groupNames, result);
        return result;
    }

    public static Map getComparisonsListByGroup()
    {
        return comparisonsListByGroup;
    }
}