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
package com.netspective.axiom;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.NamingException;

import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.connection.JndiConnectionProvider;
import com.netspective.axiom.schema.BasicSchema;
import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Schemas;
import com.netspective.axiom.schema.SchemasCollection;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.table.type.EnumerationTable;
import com.netspective.axiom.schema.table.type.EnumerationTableRow;
import com.netspective.axiom.schema.table.type.EnumerationTableRows;
import com.netspective.axiom.sql.Queries;
import com.netspective.axiom.sql.QueriesNameSpace;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.sql.ResultSetUtils;
import com.netspective.axiom.sql.StoredProcedure;
import com.netspective.axiom.sql.StoredProcedures;
import com.netspective.axiom.sql.StoredProceduresNameSpace;
import com.netspective.axiom.sql.collection.QueriesCollection;
import com.netspective.axiom.sql.collection.QueriesPackage;
import com.netspective.axiom.sql.collection.QueryDefinitionsCollection;
import com.netspective.axiom.sql.collection.StoredProceduresCollection;
import com.netspective.axiom.sql.collection.StoredProceduresPackage;
import com.netspective.axiom.sql.dynamic.QueryDefinition;
import com.netspective.axiom.sql.dynamic.QueryDefinitions;
import com.netspective.commons.metric.CountMetric;
import com.netspective.commons.metric.Metric;
import com.netspective.commons.metric.MetricsGroup;
import com.netspective.commons.metric.MetricsProducer;
import com.netspective.commons.product.NetspectiveComponent;
import com.netspective.commons.xdm.DefaultXdmComponentItems;
import com.netspective.commons.xdm.XdmIdentifierConstantsGenerator;
import com.netspective.commons.xdm.XmlDataModelSchema;

public class SqlManager extends DefaultXdmComponentItems implements MetricsProducer
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final Log log = LogFactory.getLog(SqlManager.class);
    private static final ThreadLocal THREAD_SQL_MANAGER = new ThreadLocal();

    public static final ConnectionProvider DEFAULT_CONN_PROVIDER = (ConnectionProvider) DiscoverSingleton.find(ConnectionProvider.class, JndiConnectionProvider.class.getName());

    public static final String PREFIX_CUSTOM = "custom.";
    public static final String PREFIX_SCHEMA = "schema.";

    static
    {
        NetspectiveComponent.getInstance().registerProduct(com.netspective.axiom.ProductRelease.PRODUCT_RELEASE);
    }

    public static final SqlManager getThreadSqlManager()
    {
        return (SqlManager) THREAD_SQL_MANAGER.get();
    }

    public static final Schema getThreadDefaultSchema()
    {
        SqlManager sqlManager = getThreadSqlManager();
        return sqlManager != null ? sqlManager.getSchemas().getDefault() : null;
    }

    public static final void setThreadSqlManager(SqlManager sqlManager)
    {
        THREAD_SQL_MANAGER.set(sqlManager);
    }

    protected QueriesNameSpace activeNameSpace;
    private ConnectionProvider provider;
    private Map providers = new HashMap();
    private QueriesNameSpace temporaryQueriesNameSpace;
    private Queries queries = constructQueries();
    private StoredProcedures storedProcedures = constructStoredProcedures();
    protected StoredProceduresNameSpace activeSPNameSpace;
    private QueryDefinitions queryDefns = constructQueryDefinitions();
    private Schemas schemas = constructSchemas();

    public SqlManager()
    {
        temporaryQueriesNameSpace = new QueriesPackage(queries);
        temporaryQueriesNameSpace.setNameSpaceId("temporary");
        addConnectionProvider(DEFAULT_CONN_PROVIDER);
    }

    public QueriesNameSpace getTemporaryQueriesNameSpace()
    {
        return temporaryQueriesNameSpace;
    }

    protected StoredProcedures constructStoredProcedures()
    {
        StoredProceduresCollection storedProceduresCollection = new StoredProceduresCollection();
        storedProceduresCollection.setSqlManager(this);
        return storedProceduresCollection;
    }

    protected Queries constructQueries()
    {
        QueriesCollection queriesCollection = new QueriesCollection();
        queriesCollection.setSqlManager(this);

        return queriesCollection;
    }

    protected QueryDefinitions constructQueryDefinitions()
    {
        return new QueryDefinitionsCollection();
    }

    protected Schemas constructSchemas()
    {
        return new SchemasCollection();
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public StoredProcedures getStoredProcedures()
    {
        return storedProcedures;
    }

    public StoredProcedure getStoredProcedure(final String name)
    {
        StoredProcedure sp = null;
        String actualName = "";
        if(name != null && name.length() > 0)
        {
            actualName = StoredProcedure.translateNameForMapKey(name);
            sp = storedProcedures.get(actualName);
        }
        if(sp == null && log.isDebugEnabled())
        {
            log.debug("Unable to find stored procedure object '" + name + "' as '" + actualName + "'. Available: " + storedProcedures);
            return null;
        }
        return sp;
    }

    public Queries getQueries()
    {
        return queries;
    }

    public Query getQuery(final String name)
    {
        Query query = null;
        String actualName = "";
        if(name != null && name.length() > 0)
        {
            actualName = Query.translateNameForMapKey(name);
            query = queries.get(actualName);
        }
        if(query == null && log.isDebugEnabled())
        {
            log.debug("Unable to find query object '" + name + "' as '" + actualName + "'. Available: " + queries);
            return null;
        }
        return query;
    }

    public Object[][] executeQueryAndGetMatrix(ConnectionContext cc, String queryName, Object[] bindParams, boolean closeConnection) throws NamingException, SQLException
    {
        Object[][] ret = null;
        Query query = getQuery(queryName);
        if(query != null)
        {
            QueryResultSet queryResultSet = query.execute(cc, bindParams, false);
            ret = ResultSetUtils.getInstance().getResultSetRowsAsMatrix(queryResultSet.getResultSet());
            queryResultSet.close(closeConnection);
            cc.close();
        }
        return ret;
    }

    public Map[] executeQueryAndGetMapArray(ConnectionContext cc, String queryName, Object[] bindParams, boolean closeConnection) throws NamingException, SQLException
    {
        Map[] ret = null;
        Query query = getQuery(queryName);
        if(query != null)
        {
            QueryResultSet queryResultSet = query.execute(cc, bindParams, false);
            ret = ResultSetUtils.getInstance().getResultSetRowsAsMapArray(queryResultSet.getResultSet());
            queryResultSet.close(closeConnection);
            cc.close();
        }
        return ret;
    }

    public Query constructQuery() // not called "create" because we don't want XDM to create tag at this level but we still need the method available
    {
        return new Query();
    }

    public void appendQuery(Query query) // not called "add" because we don't want XDM to create tag at this level but we still need the method available
    {
        queries.add(query);
    }

    public StoredProceduresNameSpace createStoredProcedures()
    {
        activeSPNameSpace = new StoredProceduresPackage(getStoredProcedures());
        return activeSPNameSpace;
    }

    public QueriesNameSpace createQueries()
    {
        activeNameSpace = new QueriesPackage(getQueries());
        return activeNameSpace;
    }

    public void addQueries(QueriesNameSpace pkg)
    {
        activeNameSpace = null;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public QueryDefinitions getQueryDefns()
    {
        return queryDefns;
    }

    public QueryDefinition getQueryDefinition(final String name)
    {
        String actualName = QueryDefinition.translateNameForMapKey(name);
        QueryDefinition queryDefn = queryDefns.get(actualName);

        if(queryDefn == null && log.isDebugEnabled())
        {
            log.debug("Unable to find query definition object '" + name + "' as '" + actualName + "'. Available: " + queryDefns);
            return null;
        }
        return queryDefn;
    }

    public QueryDefinition getQueryDefinition(final String name, boolean checkSchemas)
    {
        if(name == null)
            throw new RuntimeException("name is NULL");

        if(name.startsWith(PREFIX_CUSTOM))
            return getQueryDefinition(name.substring(PREFIX_CUSTOM.length()));
        else if(name.startsWith(PREFIX_SCHEMA))
        {
            StringTokenizer st = new StringTokenizer(name, ".");
            String schemaPrefix = st.nextToken();
            String schemaName = st.nextToken();
            Schema schema = getSchema(schemaName);
            if(schema != null && st.hasMoreTokens())
            {
                String tableName = st.nextToken();
                Table table = schema.getTables().getByName(tableName);
                if(table != null)
                    return table.getQueryDefinition();
                else
                {
                    log.debug("Unable to find table '" + tableName + "' from '" + name + "' in getQueryDefinition(). Available: " + queryDefns);
                    return null;
                }
            }
            else
            {
                log.debug("Unable to find schema '" + schemaName + "' from '" + name + "' in getQueryDefinition(). Available: " + queryDefns);
                return null;
            }
        }
        else
            return getQueryDefinition(name);
    }

    public QueryDefinition createQueryDefn()
    {
        return new QueryDefinition();
    }

    public void addQueryDefn(QueryDefinition queryDefn)
    {
        queryDefns.add(queryDefn);
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public Schemas getSchemas()
    {
        return schemas;
    }

    public Schema getSchema(final String name)
    {
        String actualName = BasicSchema.translateNameForMapKey(name);
        Schema schema = schemas.getByName(actualName);

        if(schema == null && log.isDebugEnabled())
        {
            log.debug("Unable to find schema object '" + name + "' as '" + actualName + "'. Available: " + schemas);
            return null;
        }
        return schema;
    }

    public Schema createSchema()
    {
        return new BasicSchema(this);
    }

    public void addSchema(Schema schema)
    {
        schemas.add(schema);
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public void addDatabasePolicy(DatabasePolicy policy)
    {
        DatabasePolicies.getInstance().registerDatabasePolicy(policy);
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public void addConnectionProvider(ConnectionProvider provider)
    {
        this.providers.put(provider.getConnectionProviderName(), provider);
        this.provider = provider;
    }

    public ConnectionProvider getConnectionProvider()
    {
        return provider;
    }

    public void setDefaultConnectionProviderName(String name)
    {
        this.provider = (ConnectionProvider) providers.get(name);
        if(this.provider == null)
            throw new RuntimeException("No connection provider named '" + name + "' was found. Available: " + providers.keySet());
    }

    /* ------------------------------------------------------------------------------------------------------------- */
    /**
     * Produces metrics associated with database related entities such as static queries and stored procedures
     */
    public void produceMetrics(Metric parent)
    {
        MetricsGroup databaseGroup = parent.addGroupMetric("Data Management");
        getQueries().produceMetrics(databaseGroup);
        getQueryDefns().produceMetrics(databaseGroup);
        getStoredProcedures().produceMetrics(databaseGroup);

        CountMetric dsMetric = databaseGroup.addCountMetric("Total data sources");
        dsMetric.setSum(getConnectionProvider().getAvailableDataSources().size());

        databaseGroup.addValueMetric("Schemas", Integer.toString(schemas.size()));
        databaseGroup.addValueMetric("Database Policies", Integer.toString(DatabasePolicies.getInstance().size()));
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    protected class SqlManagerIdentifierConstantsGenerator
    {
        public static final String DELIM = ".";
        private String rootPackage = "sql";
        private String queryPackage = "sql.query";
        private String storedProcPackage = "sql.stored-proc";
        private String queryDefnPackage = "sql.query-defn";
        private String schemaPackage = "sql.schema";

        public SqlManagerIdentifierConstantsGenerator()
        {
        }

        public SqlManagerIdentifierConstantsGenerator(String root, String queries, String queryDefns, String storedProcs, String schemas)
        {
            this.rootPackage = root;
            this.queryPackage = queries;
            this.queryDefnPackage = queryDefns;
            this.schemaPackage = schemas;
            this.storedProcPackage = storedProcs;
        }

        public String getStoredProcedurePackage(StoredProcedure sp)
        {
            return this.storedProcPackage + DELIM + sp.getQualifiedName();
        }

        public String getQueryPackage(Query query)
        {
            return this.queryPackage + DELIM + query.getQualifiedName();
        }

        public void setStoredProcedurePackage(String storedProcs)
        {
            this.storedProcPackage = storedProcs;
        }

        public void setQueryPackage(String queries)
        {
            this.queryPackage = queries;
        }

        public String getQueryDefnPackage(QueryDefinition queryDefn)
        {
            return this.queryDefnPackage + DELIM + queryDefn.getName();
        }

        public void setQueryDefnPackage(String queryDefnPackage)
        {
            this.queryDefnPackage = queryDefnPackage;
        }

        public String getRootPackage()
        {
            return rootPackage;
        }

        public void setRootPackage(String rootPackage)
        {
            this.rootPackage = rootPackage;
        }

        public String getSchemaPackage(Schema schema)
        {
            return schemaPackage + DELIM + schema.getName();
        }

        public String getSchemaPackage(Table table)
        {
            if(table instanceof EnumerationTable)
                return getSchemaPackage(table.getSchema()) + DELIM + "enum" + DELIM + table.getName();
            else
                return getSchemaPackage(table.getSchema()) + DELIM + "table" + DELIM + table.getName();
        }

        public String getSchemaPackage(Column column)
        {
            return getSchemaPackage(column.getTable()) + DELIM + column.getName();
        }

        public void setSchemaPackage(String schemaPackage)
        {
            this.schemaPackage = schemaPackage;
        }

        public void defineConstants(Map constants, Queries queries)
        {
            for(int i = 0; i < queries.size(); i++)
            {
                Query query = queries.get(i);
                constants.put(getQueryPackage(query), query.getQualifiedName());
            }
        }

        public void defineConstants(Map constants, StoredProcedures storedProcs)
        {
            for(int i = 0; i < storedProcs.size(); i++)
            {
                StoredProcedure sp = storedProcs.get(i);
                constants.put(getStoredProcedurePackage(sp), sp.getQualifiedName());
            }
        }

        public void defineConstants(Map constants, QueryDefinitions queryDefns)
        {
            for(int i = 0; i < queryDefns.size(); i++)
            {
                QueryDefinition queryDefn = queryDefns.get(i);
                constants.put(getQueryDefnPackage(queryDefn), queryDefn.getName());
            }
        }

        public void defineConstants(Map constants, Schemas schemas)
        {
            for(int i = 0; i < schemas.size(); i++)
            {
                Schema schema = schemas.get(i);
                constants.put(getSchemaPackage(schema), new Integer(i));

                for(int t = 0; t < schema.getTables().size(); t++)
                {
                    Table table = schema.getTables().get(t);
                    String tablePkg = getSchemaPackage(table);

                    if(table instanceof EnumerationTable)
                    {
                        EnumerationTableRows rows = ((EnumerationTable) table).getEnums();
                        if(rows != null && rows.size() > 0)
                        {
                            constants.put(tablePkg, new Integer(t));
                            for(int r = 0; r < rows.size(); r++)
                            {
                                EnumerationTableRow row = (EnumerationTableRow) rows.getRow(r);
                                constants.put(tablePkg + DELIM + row.getJavaConstant(), row.getIdAsInteger());
                            }
                        }
                    }
                    else
                    {
                        constants.put(tablePkg, new Integer(t));
                        for(int c = 0; c < table.getColumns().size(); c++)
                        {
                            Column column = table.getColumns().get(c);
                            constants.put(getSchemaPackage(column), new Integer(column.getIndexInRow()));
                        }
                    }
                }
            }
        }

        public Map createConstants()
        {
            Map constants = new HashMap();
            defineConstants(constants, queries);
            defineConstants(constants, storedProcedures);
            defineConstants(constants, queryDefns);
            defineConstants(constants, queryDefns);
            defineConstants(constants, schemas);
            return constants;
        }
    }

    protected SqlManagerIdentifierConstantsGenerator getSqlIdentifiersConstantsDecls()
    {
        return new SqlManagerIdentifierConstantsGenerator();
    }

    public void generateIdentifiersConstants(File rootPath, String rootPkgAndClassName) throws IOException
    {
        XdmIdentifierConstantsGenerator xicg =
                new XdmIdentifierConstantsGenerator(rootPath,
                                                    rootPkgAndClassName,
                                                    getSqlIdentifiersConstantsDecls().createConstants());
        xicg.generateCode();
    }
}
