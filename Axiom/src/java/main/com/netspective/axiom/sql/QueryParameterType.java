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
 * $Id: QueryParameterType.java,v 1.5 2003-11-10 23:02:02 aye.thu Exp $
 */

package com.netspective.axiom.sql;

import java.util.Map;
import java.util.HashMap;
import java.sql.Types;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Blob;
import java.sql.ResultSet;

/**
 * Class for holding the relationship mappings between JDBC SQL data types and Java
 * data types.
 */
public class QueryParameterType
{
    public static final String STRING_ARRAY_IDENTIFIER    = "strings";
    public static final String INTEGER_IDENTIFIER   = "integer";
    public static final String DOUBLE_IDENTIFIER    = "double";
    public static final String VARCHAR_IDENTIFIER   = "varchar";
    public static final String REAL_IDENTIFIER      = "real";
    public static final String FLOAT_IDENTIFIER     = "float";
    public static final String CLOB_IDENTIFIER      = "clob";
    public static final String DECIMAL_IDENTIFIER   = "decimal";
    public static final String DATE_IDENTIFIER      = "date";
    public static final String BLOB_IDENTIFIER      = "blob";
    public static final String SMALLINT_IDENTIFIER  = "smallint";
    public static final String BIGINT_IDENTIFIER    = "bigint";
    public static final String RESULTSET_IDENTIFIER = "resultset";

    public static final QueryParameterType TEXT = new QueryParameterType("text", Types.VARCHAR, String.class);
    private static final Map typesMapByIdentifier = new HashMap();
    private static final Map typesMapByJdbcType = new HashMap();
    private static String[] typeIdentifiers;

    private String identifier;
    private int jdbcType;
    private Class javaClass;

    /**
     * Static initialization of the mappings between the JDBC types and Java data types
     */
    static
    {
        add(STRING_ARRAY_IDENTIFIER, Types.ARRAY, String[].class);
        add(INTEGER_IDENTIFIER, Types.INTEGER, Integer.class);
        add(DOUBLE_IDENTIFIER, Types.DOUBLE, Double.class);
        add(VARCHAR_IDENTIFIER, Types.VARCHAR, String.class);
        add(TEXT);

        // added to support stored procedures
        add(REAL_IDENTIFIER, Types.REAL, Float.class);
        add(FLOAT_IDENTIFIER, Types.FLOAT, Float.class);
        //add("char", Types.CHAR, char.class);
        add(CLOB_IDENTIFIER, Types.CLOB, Clob.class);
        add(DECIMAL_IDENTIFIER, Types.DECIMAL, Double.class);
        add(DATE_IDENTIFIER, Types.DATE, Date.class);
        add(BLOB_IDENTIFIER, Types.BLOB, Blob.class);
        add(SMALLINT_IDENTIFIER, Types.SMALLINT, Byte.class);
        add(BIGINT_IDENTIFIER, Types.BIGINT, Long.class);
        add(RESULTSET_IDENTIFIER, Types.OTHER, ResultSet.class);
    }

    /**
     * Add a new query parameter type to the static map holding all the
     * identifiers
     * @param type
     */
    public final static void add(QueryParameterType type)
    {
        typesMapByIdentifier.put(type.getIdentifier(), type);
        typesMapByJdbcType.put(new Integer(type.getJdbcType()), type);
        typeIdentifiers = (String[]) typesMapByIdentifier.keySet().toArray(new String[typesMapByIdentifier.size()]);
    }

    /**
     * Add a new query parameter type to the static map holding all the
     * identifiers
     * @param identifier
     * @param jdbcType
     * @param javaClass
     */
    public final static void add(String identifier, int jdbcType, Class javaClass)
    {
        add(new QueryParameterType(identifier, jdbcType, javaClass));
    }

    /**
     * Gets a query parameter type by its identifier which is the java data type name
     * (not the JDBC data type)
     * @param identifier
     * @return
     */
    public final static QueryParameterType get(String identifier)
    {
        return (QueryParameterType) typesMapByIdentifier.get(identifier);
    }

    /**
     * Gets the query parameter type by its JDBC data type (java.sql.Types)
     * @param jdbcType
     * @return
     */
    public final static QueryParameterType get(int jdbcType)
    {
        return (QueryParameterType) typesMapByJdbcType.get(new Integer(jdbcType));
    }

    /**
     * Gets all the java data type names
     * @return
     */
    public final static String[] getTypeIdentifiers()
    {
        return typeIdentifiers;
    }

    /**
     * Creates a new QueryParameterType with the java data type identifier string,
     * the JDBC Sql type, and the actual java data class.
     * @param identifier
     * @param jdbcType
     * @param javaClass
     */
    public QueryParameterType(String identifier, int jdbcType, Class javaClass)
    {
        this.identifier = identifier;
        this.javaClass = javaClass;
        this.jdbcType = jdbcType;
    }

    /**
     * Gets the java data type identifier string for this parameter
     * @return
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     * Gets the java data type class for this parameter
     * @return
     */
    public Class getJavaClass()
    {
        return javaClass;
    }

    /**
     * Gets the JDBC Sql type for this parameter
     * @return
     */
    public int getJdbcType()
    {
        return jdbcType;
    }
}

