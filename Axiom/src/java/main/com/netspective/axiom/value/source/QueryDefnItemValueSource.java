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
package com.netspective.axiom.value.source;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.SqlManager;
import com.netspective.axiom.SqlManagerComponent;
import com.netspective.axiom.sql.dynamic.QueryDefinition;
import com.netspective.axiom.value.DatabaseConnValueContext;
import com.netspective.commons.io.Resource;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.commons.xdm.exception.DataModelException;

public abstract class QueryDefnItemValueSource extends AbstractValueSource
{
    private static final Log log = LogFactory.getLog(QueryDefnItemValueSource.class);

    private ValueSource dataSourceId;
    private QueryDefinition queryDefn;
    private ValueSource queryDefnSourceId;
    private String queryDefnId;

    public QueryDefnItemValueSource()
    {
    }

    public QueryDefnItemValueSource(QueryDefinition queryDefn)
    {
        setQueryDefn(queryDefn);
    }

    public QueryDefinition getQueryDefn()
    {
        return queryDefn;
    }

    public void setQueryDefn(QueryDefinition queryDefn)
    {
        this.queryDefn = queryDefn;
    }

    public ValueSource getDataSourceId()
    {
        return dataSourceId;
    }

    public void setDataSourceId(ValueSource value)
    {
        dataSourceId = value;
    }

    public String getQueryDefnId()
    {
        return queryDefnId;
    }

    public ValueSource getQueryDefnSourceId()
    {
        return queryDefnSourceId;
    }

    public SqlManager getSqlManager(DatabaseConnValueContext dbcvc) throws DataModelException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, NoSuchMethodException
    {
        SqlManager result = null;
        if(queryDefnSourceId != null)
        {
            SqlManagerComponent component = null;

            String sourceIdText = queryDefnSourceId.getTextValue(dbcvc);
            if(sourceIdText == null)
                throw new RuntimeException("sourceId returned null text value in " + this);

            if(sourceIdText.startsWith("r "))
            {
                String resourceId = sourceIdText.substring(2);
                component = (SqlManagerComponent) XdmComponentFactory.get(SqlManagerComponent.class, new Resource(QueryDefnItemValueSource.class, resourceId), XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);
            }
            else
                component = (SqlManagerComponent) XdmComponentFactory.get(SqlManagerComponent.class, new File(sourceIdText), XdmComponentFactory.XDMCOMPFLAGS_DEFAULT);
            result = component.getManager();
        }
        else
            result = dbcvc.getSqlManager();

        return result;
    }

    public QueryDefinition getQueryDefn(DatabaseConnValueContext dbcvc)
    {
        if(queryDefn != null)
            return queryDefn;

        SqlManager sqlManager = null;
        try
        {
            sqlManager = getSqlManager(dbcvc);
        }
        catch(Exception e)
        {
            log.error("Unable to retrieve Sql Manager", e);
            throw new NestableRuntimeException(e);
        }

        QueryDefinition result = sqlManager.getQueryDefinition(queryDefnId, true);
        if(result == null)
            throw new RuntimeException("QueryDefinition '" + queryDefnId + "' could not be located in SQL Manager " + sqlManager);

        return result;
    }

    /**
     * Assigns the source of the query. The format is 'query-source/query-defn-id@data-source-id'. Where the only required
     * item is the query-id. Query-source may be either a static value or a value source and may resolve to either a
     * resource id or a file name. If a resource id is required, use 'r resourceId' (prefix 'r ' in front of the value
     * to indicate it's a resource). The Query id is always a static text item and data-source-id may be a value source,
     * null, or a static text string.
     */
    public void setSource(String params)
    {
        int dataSrcIdDelim = params.indexOf('@');
        if(dataSrcIdDelim != -1)
        {
            String srcParams = params.substring(0, dataSrcIdDelim);
            int querySrcIdDelim = srcParams.lastIndexOf('/');
            if(querySrcIdDelim != -1)
            {
                queryDefnSourceId = ValueSources.getInstance().getValueSourceOrStatic(srcParams.substring(0, querySrcIdDelim));
                queryDefnId = srcParams.substring(querySrcIdDelim + 1);
            }
            else
                queryDefnId = srcParams;

            setDataSourceId(ValueSources.getInstance().getValueSourceOrStatic(params.substring(dataSrcIdDelim + 1)));
        }
        else
            dataSourceId = null;
    }

    public void initialize(StringTokenizer params)
    {
        if(params.hasMoreTokens())
            setSource(params.nextToken().trim());
    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);
        initialize(new StringTokenizer(spec.getParams(), ","));
    }

    public static class QueryDefnSourceParameter extends ValueSourceDocumentation.Parameter
    {
        public QueryDefnSourceParameter()
        {
            super("query-defn-source", true, "The format is 'query-defn-source/query-defn-id@data-source-id'. Where the only required " +
                                             "item is the query-defn-id. Query-defn-source may be either a static value or a value source and may resolve to either a " +
                                             "resource id or a file name. If a resource id is required, use 'r resourceId' (prefix 'r ' in front of the value " +
                                             "to indicate it's a resource). The query-defn-id is always a static text item and data-source-id may be a value source, " +
                                             "null, or a static text string.");
        }
    }
}
