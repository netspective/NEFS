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
 * $Id: ResultSetUtils.java,v 1.2 2003-08-19 16:08:33 shahid.shah Exp $
 */

package com.netspective.axiom.sql;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

import org.apache.commons.discovery.tools.DiscoverSingleton;

public class ResultSetUtils
{
    private static final ResultSetUtils INSTANCE = (ResultSetUtils) DiscoverSingleton.find(ResultSetUtils.class, ResultSetUtils.class.getName());

    public static ResultSetUtils getInstance()
    {
        return INSTANCE;
    }

    public ResultSetUtils()
    {
    }

    /**
     * Given a ResultSet, return a Map of all the column names in the ResultSet
     * in lowercase as the key and the index of the column as the value.
     */
    public Map getColumnNamesIndexMap(ResultSet rs) throws SQLException
    {
        Map map = new HashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colsCount = rsmd.getColumnCount();
        for (int i = 1; i <= colsCount; i++)
        {
            map.put(rsmd.getColumnName(i).toLowerCase(), new Integer(i));
        }
        return map;
    }

    public Object getResultSetSingleColumn(ResultSet rs) throws SQLException
    {
        if(rs.next())
            return rs.getObject(1);
        else
            return null;
    }

    public Object[] getResultSetSingleRowArray(ResultSet rs) throws SQLException
    {
        if(rs.next())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colsCount = rsmd.getColumnCount();
            Object[] result = new Object[colsCount];
            for(int i = 1; i <= colsCount; i++)
            {
                result[i - 1] = rs.getObject(i);
            }
            return result;
        }
        else
            return null;
    }

    public Map getResultSetSingleRowAsMap(ResultSet rs) throws SQLException
    {
        Map result = new HashMap();
        if(rs.next())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colsCount = rsmd.getColumnCount();
            for(int i = 1; i <= colsCount; i++)
            {
                result.put(rsmd.getColumnName(i).toLowerCase(), rs.getObject(i));
            }
            return result;
        }
        else
            return null;
    }

    public Map[] getResultSetRowsAsMapArray(ResultSet rs) throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        int colsCount = rsmd.getColumnCount();
        String[] columnNames = new String[colsCount];
        for(int c = 1; c <= colsCount; c++)
        {
            columnNames[c - 1] = rsmd.getColumnName(c).toLowerCase();
        }

        ArrayList result = new ArrayList();
        while(rs.next())
        {
            Map rsMap = new HashMap();
            for(int i = 1; i <= colsCount; i++)
            {
                rsMap.put(columnNames[i - 1], rs.getObject(i));
            }
            result.add(rsMap);
        }

        if(result.size() > 0)
            return (Map[]) result.toArray(new Map[result.size()]);
        else
            return null;
    }

    public Object[] getResultSetSingleRowAsArray(ResultSet rs) throws SQLException
    {
        if(rs.next())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colsCount = rsmd.getColumnCount();
            Object[] result = new Object[colsCount];
            for(int i = 1; i <= colsCount; i++)
            {
                result[i - 1] = rs.getObject(i);
            }
            return result;
        }
        else
            return null;
    }

    public String[] getResultSetSingleRowAsStrings(ResultSet rs) throws SQLException
    {
        if(rs.next())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colsCount = rsmd.getColumnCount();
            String[] result = new String[colsCount];
            for(int i = 1; i <= colsCount; i++)
            {
                result[i - 1] = rs.getString(i);
            }
            return result;
        }
        else
            return null;
    }

    public Object[][] getResultSetRowsAsMatrix(ResultSet rs) throws SQLException
    {
        ArrayList result = new ArrayList();
        while(rs.next())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colsCount = rsmd.getColumnCount();
            Object[] row = new Object[colsCount];
            for(int i = 1; i <= colsCount; i++)
            {
                row[i - 1] = rs.getObject(i);
            }
            result.add(row);
        }

        if(result.size() > 0)
            return (Object[][]) result.toArray(new Object[result.size()][]);
        else
            return null;
    }

    public String[] getResultSetRowsAsStrings(ResultSet rs) throws SQLException
    {
        ArrayList result = new ArrayList();
        while(rs.next())
        {
            result.add(rs.getString(1));
        }

        if(result.size() > 0)
            return (String[]) result.toArray(new String[result.size()]);
        else
            return null;
    }
}
