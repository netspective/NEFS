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
 * $Id: SqlComparisonFactory.java,v 1.3 2004-03-19 03:12:20 shahid.shah Exp $
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
import com.netspective.axiom.sql.dynamic.comparison.EndsWithComparison;
import com.netspective.axiom.sql.dynamic.comparison.InComparison;
import com.netspective.axiom.sql.dynamic.comparison.IsDefinedComparison;
import com.netspective.axiom.sql.dynamic.comparison.StartsWithComparison;
import com.netspective.axiom.sql.dynamic.comparison.StartsWithComparisonIgnoreCase;
import com.netspective.axiom.sql.dynamic.comparison.EndsWithComparisonIgnoreCase;

public class SqlComparisonFactory
{
    static List comparisonsList = new ArrayList();
    static Map comparisonsMap = new HashMap();

    static
    {
        registerComparison(new BinaryOpComparison("equals", "equals", "general", "="), new String[]{"is", "="});
        registerComparison(new BinaryOpComparison("not-equals", "does not equal", "general", "!="), new String[]{"is-not", "!="});
        registerComparison(new StartsWithComparison(), null);
        registerComparison(new StartsWithComparisonIgnoreCase(), null);
        registerComparison(new ContainsComparison(), null);
        registerComparison(new ContainsComparisonIgnoreCase(), null);
        registerComparison(new EndsWithComparison(), null);
        registerComparison(new EndsWithComparisonIgnoreCase(), null);
        registerComparison(new InComparison(), null);
        registerComparison(new IsDefinedComparison(), null);
        registerComparison(new BinaryOpComparison("greater-than", "greater than", "general", ">"), new String[]{"gt", ">"});
        registerComparison(new BinaryOpComparison("greater-than-equal", "greater than or equal to", "general", ">="), new String[]{"gte", ">="});
        registerComparison(new BinaryOpComparison("less-than", "less than", "general", "<"), new String[]{"lt", "<"});
        registerComparison(new BinaryOpComparison("less-than-equal", "less than or equal to", "general", "<="), new String[]{"lte", "<="});
        registerComparison(new DateComparison("lte-date", "<="), new String[]{"lte-date", "less-than-equal-date"});
        registerComparison(new DateComparison("lt-date", "<"), new String[]{"lt-date", "less-than-date"});
        registerComparison(new DateComparison("gte-date", ">="), new String[]{"gte-date", "greater-than-equal-date"});
        registerComparison(new DateComparison("gt-date", ">"), new String[]{"gt-date", "greater-than-date"});
    }

    static public void registerComparison(SqlComparison comp, String[] aliases)
    {
        comparisonsList.add(comp);
        comparisonsMap.put(comp.getName(), comp);

        if(aliases != null)
        {
            for(int i = 0; i < aliases.length; i++)
                comparisonsMap.put(aliases[i], comp);
        }
    }

    public static SqlComparison getComparison(String name)
    {
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
}