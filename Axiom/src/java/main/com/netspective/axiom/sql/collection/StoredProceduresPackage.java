package com.netspective.axiom.sql.collection;

import com.netspective.axiom.sql.*;
import com.netspective.commons.xdm.XmlDataModelSchema;


public class StoredProceduresPackage implements StoredProceduresNameSpace
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] { "container" });
        XML_DATA_MODEL_SCHEMA_OPTIONS.addAliases("name-space-id", new String[] { "package" });
    }

    private StoredProcedures container;
    private String packageName;

    public StoredProceduresPackage(StoredProcedures queries)
    {
        setContainer(queries);
    }

    public StoredProcedures getContainer()
    {
        return container;
    }

    public void setContainer(StoredProcedures container)
    {
        this.container = container;
    }

    public String getNameSpaceId()
    {
        return packageName;
    }

    public void setNameSpaceId(String identifier)
    {
        this.packageName = identifier;
    }

    public StoredProcedure createStoredProcedure()
    {
        return new StoredProcedure(this);
    }

    public void addStoredProcedure(StoredProcedure query)
    {
        container.add(query);
    }
}
