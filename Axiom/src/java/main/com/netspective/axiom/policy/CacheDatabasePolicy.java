package com.netspective.axiom.policy;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Nov 2, 2003
 * Time: 2:12:55 PM
 * To change this template use Options | File Templates.
 */
public class CacheDatabasePolicy
{
     public static final String DBMSID_CACHE_SQL = "Cache";
    public String getDbmsIdentifier()
    {
        return DBMSID_CACHE_SQL;
    }

    public String[] getDbmsIdentifiers()
    {
        return new String[] { getDbmsIdentifier(), "Cache" };
    }
}
