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

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.discovery.tools.DiscoverSingleton;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.exception.DataModelException;

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
     * Create a text array that contains the headings of the columns in the given result set.
     *
     * @param resultSet                  The result set that we want to create column headers for
     * @param preferColumnLabelForHeader True if we want to use the label (if available) for a column or use it's name if unavailable or false
     *
     * @return The headings
     */
    public String[] getColumnHeadings(ResultSet resultSet, boolean preferColumnLabelForHeader) throws SQLException
    {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsCount = rsmd.getColumnCount();

        String[] header = new String[columnsCount];
        if(preferColumnLabelForHeader)
        {
            for(int i = 1; i < columnsCount; i++)
            {
                String label = rsmd.getColumnLabel(i);
                if(label != null && label.length() > 0)
                    header[i - 1] = label;
                else
                    header[i - 1] = rsmd.getColumnName(i);
            }
        }
        else
        {
            for(int i = 1; i < columnsCount; i++)
                header[i - 1] = rsmd.getColumnName(i);
        }

        return header;
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
        for(int i = 1; i <= colsCount; i++)
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
        return getResultSetSingleRowAsMap(rs, false);
    }

    public Map getResultSetSingleRowAsMap(ResultSet rs, boolean useLabelAsKey) throws SQLException
    {
        Map result = new HashMap();
        if(rs.next())
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colsCount = rsmd.getColumnCount();
            for(int i = 1; i <= colsCount; i++)
            {
                result.put(useLabelAsKey ? rsmd.getColumnLabel(i).toLowerCase() : rsmd.getColumnName(i).toLowerCase(), rs.getObject(i));
            }
            return result;
        }
        else
            return null;
    }

    public Map[] getResultSetRowsAsMapArray(ResultSet rs) throws SQLException
    {
        return getResultSetRowsAsMapArray(rs, false);
    }

    public Map[] getResultSetRowsAsMapArray(ResultSet rs, boolean useLabelAsKey) throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        int colsCount = rsmd.getColumnCount();
        String[] columnNames = new String[colsCount];
        for(int c = 1; c <= colsCount; c++)
        {
            columnNames[c - 1] = useLabelAsKey
                                 ? rsmd.getColumnLabel(c).toLowerCase() : rsmd.getColumnName(c).toLowerCase();
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

    public Set getResultSetRowsFirstColumnAsSet(ResultSet rs) throws SQLException
    {
        Set result = new HashSet();
        while(rs.next())
            result.add(rs.getObject(1));
        return result;
    }

    /**
     * Given a ResultSet, assign the current row's values using appropriate accessor methods of the
     * instance object (using Java reflection).
     *
     * @param rs              The ResultSet to assign
     * @param instance        The object who's mutator methods should be matched
     * @param columnKeys      The names of the columns (or labels) that should be assigned to the mutators of the instance object.
     *                        This may be '*' (for all columns) or a comma-separated list of names/labels. The parameter names
     *                        may optionally be followed by an '=' to indicate a default value for the column. Column
     *                        names may optionally be terminated with an '!' to indicate that they are required (an exception
     *                        is thrown if the parameter is unavailable. For example, "a,b!,c" would mean that parameter
     *                        'a', 'b' and 'c' should be assigned using setA(), setB() and setC() if available but an
     *                        exception should be thrown if 'b' is not available as a column name/label.
     * @param useLabelsAsKeys true if the keys provided are column labels and not column names
     *
     * @return The Map created using getResultSetSingleRowAsMap(rs) since this method requires the Map to be created anyway
     */
    public Map assignColumnValuesToInstance(ResultSet rs, Object instance, String columnKeys, boolean useLabelsAsKeys) throws IllegalAccessException, InvocationTargetException, DataModelException, SQLException
    {
        XmlDataModelSchema schema = XmlDataModelSchema.getSchema(instance.getClass());
        Map result = getResultSetSingleRowAsMap(rs, useLabelsAsKeys);
        schema.assignMapValues(instance, result, columnKeys);
        return result;
    }

    /**
     * Given a class, loop through the result set and return the result as a set of typed Java objects. The values are
     * filled using reflection. This method simply loops through the result set and calls type.newInstance() and then
     * calls assignColumnValuesToInstance() for each row.
     *
     * @param list            The list to add the typed java objects into
     * @param rs              The result set to retrieve
     * @param type            The class that each row should represent
     * @param columnKeys      The names of the columns (or labels) that should be assigned to the mutators of the instance object.
     *                        This may be '*' (for all columns) or a comma-separated list of names/labels. The parameter names
     *                        may optionally be followed by an '=' to indicate a default value for the column. Column
     *                        names may optionally be terminated with an '!' to indicate that they are required (an exception
     *                        is thrown if the parameter is unavailable. For example, "a,b!,c" would mean that parameter
     *                        'a', 'b' and 'c' should be assigned using setA(), setB() and setC() if available but an
     *                        exception should be thrown if 'b' is not available as a column name/label.
     * @param useLabelsAsKeys true if the keys provided are column labels and not column names
     */
    public void getResultSetRowsAsTypedObjects(List list, ResultSet rs, Class type, String columnKeys, boolean useLabelsAsKeys) throws IllegalAccessException, InvocationTargetException, DataModelException, SQLException, InstantiationException
    {
        XmlDataModelSchema schema = XmlDataModelSchema.getSchema(type);

        while(rs.next())
        {
            Object row = type.newInstance();
            Map result = getResultSetSingleRowAsMap(rs, useLabelsAsKeys);
            schema.assignMapValues(row, result, columnKeys);
            list.add(row);
        }
    }
}
