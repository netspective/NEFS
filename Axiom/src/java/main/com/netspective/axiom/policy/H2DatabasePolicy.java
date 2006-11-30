package com.netspective.axiom.policy;

import java.sql.SQLException;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.axiom.policy.ddl.HsqlDbSqlDdlFormats;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.schema.column.type.AutoIncColumn;

public class H2DatabasePolicy extends AnsiDatabasePolicy
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    public H2DatabasePolicy()
    {
        setName("H2");
        setAliases("H2 Database Engine");
    }

    public SqlDdlFormats createDdlFormats()
    {
        return new HsqlDbSqlDdlFormats();
    }

    public boolean retainAutoIncColInInsertDml()
    {
        return false;
    }

    public boolean retainAutoIncColInUpdateDml()
    {
        return false;
    }

    public Object handleAutoIncPostDmlInsertExecute(ConnectionContext cc, AutoIncColumn column, Object autoIncColumnValue) throws SQLException
    {
        return executeAndGetSingleValue(cc, "CALL IDENTITY()");
    }
}
