
package com.netspective.axiom.sql.collection;

import com.netspective.axiom.sql.StoredProcedures;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.StoredProcedure;
import com.netspective.axiom.SqlManager;

import java.util.*;


public class StoredProceduresCollection implements StoredProcedures
{
    private SqlManager sqlManager;
    private List queries = new ArrayList();
    private Map byName = new HashMap();
    private Set nameSpaceNames = new HashSet();

    public StoredProceduresCollection()
    {

    }

    public SqlManager getSqlManager()
    {
        return sqlManager;
    }

    public void setSqlManager(SqlManager sqlManager)
    {
        this.sqlManager = sqlManager;
    }

    public void add(StoredProcedure query)
    {
        queries.add(query);
        byName.put(query.getNameForMapKey(), query);

		//TODO: Modify this to also use a method similar to getNameForMapKey() for case-insensitive namespaces
		if (null != query.getNameSpace())
            nameSpaceNames.add(query.getNameSpace().getNameSpaceId());
    }

    public StoredProcedure get(int i)
    {
        return (StoredProcedure) queries.get(i);
    }

    public StoredProcedure get(String name)
    {
        return (StoredProcedure) byName.get(StoredProcedure.translateNameForMapKey(name));
    }

    public Set getNames()
    {
        return byName.keySet();
    }

    public Set getNameSpaceNames()
    {
        return nameSpaceNames;
    }

    public int size()
    {
        return queries.size();
    }
}
