package com.netspective.axiom.sql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.netspective.commons.value.ValueSource;

/**
 * Class for handling stored procedure calls
 */
public class StoredProcedure
{
    public static long queryNumber = 0;

    private Log log = LogFactory.getLog(StoredProcedure.class);
    private StoredProceduresNameSpace nameSpace;
    private String queryName;
    private StoredProcedureParameters parameters;
    private ValueSource dataSourceId;


    public StoredProcedure()
    {
        queryNumber++;
        setName(this.getClass().getName() + "-" + queryNumber);
    }

    public StoredProcedure(StoredProceduresNameSpace nameSpace)
    {
        queryNumber++;
        setNameSpace(nameSpace);
        setName(this.getClass().getName() + "-" + queryNumber);
    }

    public StoredProceduresNameSpace getNameSpace()
    {
        return nameSpace;
    }

    public void setNameSpace(StoredProceduresNameSpace pkg)
    {
        this.nameSpace = pkg;
    }

    public String getNameForMapKey()
    {
        return translateNameForMapKey(getQualifiedName());
    }

    public static String translateNameForMapKey(String name)
    {
       return name != null ? name.toUpperCase() : null;
    }

    public Log getLog()
    {
       return log;
    }

    public String getQualifiedName()
    {
        return nameSpace != null ? nameSpace.getNameSpaceId() + "." + queryName : queryName;
    }

    public String getName()
    {
        return queryName;
    }

    public void setName(String name)
    {
        this.queryName = name;
        log = LogFactory.getLog(getClass().getName() + "." + this.getQualifiedName());
    }

    public StoredProcedureParameters getParams()
    {
        return parameters;
    }

    public StoredProcedureParameters createParams()
    {
        return new StoredProcedureParameters(this);
    }

    public void addParams(StoredProcedureParameters params)
    {
        this.parameters = params;
    }

    public ValueSource getDataSrc()
    {
        return dataSourceId;
    }

    public void setDataSrc(ValueSource dataSourceId)
    {
        this.dataSourceId = dataSourceId;
    }

}
