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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.commons.lang.ValueBeanIndexedResultSetAssignable;

public class QueryUtils
{
    private static final Log log = LogFactory.getLog(QueryUtils.class);
    private static final QueryUtils INSTANCE = (QueryUtils) DiscoverSingleton.find(QueryUtils.class, QueryUtils.class.getName());
    private static final String[] EMPTY_TEXT_ARRAY = new String[0];
    private static final Map EMPTY_MAP = new HashMap();
    private static final Map[] EMPTY_MAP_ARRAY = new Map[0];

    public static QueryUtils getInstance()
    {
        return INSTANCE;
    }

    public QueryUtils()
    {
    }

    public Object getResultSetRowAsValueBean(final Query query, final DatabaseConnValueContext dbvc, final Object[] params)
    {
        if(query == null)
            throw new RuntimeException("The query should not be NULL.");

        if(dbvc == null)
            throw new RuntimeException("The database value context should not  be NULL.");

        if(query.getValueBean() == null)
            throw new RuntimeException("Query " + query.getQualifiedName() + " does not have a value-bean set.");

        QueryResultSet qrs = null;
        try
        {
            qrs = query.execute(dbvc, params, false);
            Object value = query.constructValueBean();
            ResultSet rs = qrs.getResultSet();
            if(value instanceof ValueBeanIndexedResultSetAssignable)
            {
                if(rs.next())
                    ((ValueBeanIndexedResultSetAssignable) value).setFieldsToColumnsByIndex(rs, query.getValueBeanTranslator());
            }
            else
                log.error("Don't know how to assign values to the bean since ValueBeanIndexedResultSetAssignable is not implemented.");
            return value;
        }
        catch(SQLException e)
        {
            log.error(e);
            return null;
        }
        catch(NamingException e)
        {
            log.error(e);
            return null;
        }
        catch(IllegalAccessException e)
        {
            log.error(e);
            return null;
        }
        catch(InstantiationException e)
        {
            log.error(e);
            return null;
        }
        finally
        {
            try
            {
                if(qrs != null) qrs.close(true);
            }
            catch(SQLException e)
            {
                log.error(e);
            }
        }
    }

    /**
     * Run the query provided and return the first column from each row as a String array.
     *
     * @param query  The query that should be executed
     * @param dbvc   The database (mainly data source) that should be used for the query
     * @param params The parameters to use
     *
     * @return String[] of length greater than zero if records found, NULL if an error occurs or a String[] array of 0
     *         if no values are found but no exception was encountered.
     */
    public String[] getResultSetRowsAsStrings(final Query query, final DatabaseConnValueContext dbvc, final Object[] params)
    {
        if(query == null)
            throw new RuntimeException("The query should not be NULL.");

        if(dbvc == null)
            throw new RuntimeException("The database value context should not  be NULL.");

        QueryResultSet qrs = null;
        try
        {
            qrs = query.execute(dbvc, params, false);
            String[] result = ResultSetUtils.getInstance().getResultSetRowsAsStrings(qrs.getResultSet());
            return result == null ? EMPTY_TEXT_ARRAY : result;
        }
        catch(SQLException e)
        {
            log.error(e);
            return null;
        }
        catch(NamingException e)
        {
            log.error(e);
            return null;
        }
        finally
        {
            try
            {
                if(qrs != null) qrs.close(true);
            }
            catch(SQLException e)
            {
                log.error(e);
            }
        }
    }

    /**
     * Run the query provided and return the first row's columns as a map (column name is the key, value is the map).
     *
     * @param query  The query that should be executed
     * @param dbvc   The database (mainly data source) that should be used for the query
     * @param params The parameters to use
     *
     * @return Map of length greater than zero if records found, NULL if an error occurs or a Map of 0 length
     *         if no values are found but no exception was encountered.
     */
    public Map getResultSetSingleRowAsMap(final Query query, final DatabaseConnValueContext dbvc, final Object[] params)
    {
        if(query == null)
            throw new RuntimeException("The query should not be NULL.");

        if(dbvc == null)
            throw new RuntimeException("The database value context should not  be NULL.");

        QueryResultSet qrs = null;
        try
        {
            qrs = query.execute(dbvc, params, false);
            Map result = ResultSetUtils.getInstance().getResultSetSingleRowAsMap(qrs.getResultSet());
            return result == null ? EMPTY_MAP : result;
        }
        catch(SQLException e)
        {
            log.error(e);
            return null;
        }
        catch(NamingException e)
        {
            log.error(e);
            return null;
        }
        finally
        {
            try
            {
                if(qrs != null) qrs.close(true);
            }
            catch(SQLException e)
            {
                log.error(e);
            }
        }
    }

    /**
     * Run the query provided and return the first row's first column value.
     *
     * @param query  The query that should be executed
     * @param dbvc   The database (mainly data source) that should be used for the query
     * @param params The parameters to use
     *
     * @return Map of length greater than zero if records found, NULL if an error occurs or a Map of 0 length
     *         if no values are found but no exception was encountered.
     */
    public Object getResultSetSingleColumn(final Query query, final DatabaseConnValueContext dbvc, final Object[] params)
    {
        if(query == null)
            throw new RuntimeException("The query should not be NULL.");

        if(dbvc == null)
            throw new RuntimeException("The database value context should not  be NULL.");

        QueryResultSet qrs = null;
        try
        {
            qrs = query.execute(dbvc, params, false);
            return ResultSetUtils.getInstance().getResultSetSingleColumn(qrs.getResultSet());
        }
        catch(SQLException e)
        {
            log.error(e);
            return null;
        }
        catch(NamingException e)
        {
            log.error(e);
            return null;
        }
        finally
        {
            try
            {
                if(qrs != null) qrs.close(true);
            }
            catch(SQLException e)
            {
                log.error(e);
            }
        }
    }

    /**
     * Run the query provided and return the all the rows as a map array
     *
     * @param query  The query that should be executed
     * @param dbvc   The database (mainly data source) that should be used for the query
     * @param params The parameters to use
     *
     * @return Map of length greater than zero if records found, NULL if an error occurs or a Map of 0 length
     *         if no values are found but no exception was encountered.
     */
    public Map[] getResultSetRowsAsMapArray(final Query query, final DatabaseConnValueContext dbvc, final Object[] params)
    {
        if(query == null)
            throw new RuntimeException("The query should not be NULL.");

        if(dbvc == null)
            throw new RuntimeException("The database value context should not  be NULL.");

        QueryResultSet qrs = null;
        try
        {
            qrs = query.execute(dbvc, params, false);
            Map[] result = ResultSetUtils.getInstance().getResultSetRowsAsMapArray(qrs.getResultSet());
            return result == null ? EMPTY_MAP_ARRAY : result;
        }
        catch(SQLException e)
        {
            log.error(e);
            return null;
        }
        catch(NamingException e)
        {
            log.error(e);
            return null;
        }
        finally
        {
            try
            {
                if(qrs != null) qrs.close(true);
            }
            catch(SQLException e)
            {
                log.error(e);
            }
        }
    }
}
