package auto.dal.db.dao.org;

import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.sql.dynamic.QueryDefnSelects;
import com.netspective.axiom.schema.Table;
import javax.naming.NamingException;
import java.sql.SQLException;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.schema.PrimaryKeyColumnValues;
import com.netspective.axiom.schema.column.type.DateTimeColumn;
import com.netspective.axiom.schema.column.type.DateColumn;
import com.netspective.axiom.schema.column.type.GuidTextColumn;
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.axiom.schema.column.type.RecordStatusIdColumn;
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;
import com.netspective.axiom.schema.column.type.GuidColumn;
import com.netspective.axiom.schema.column.type.LongIntegerColumn;
import auto.dal.db.dao.OrgTable;

public final class OrgRoleDeclarationTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_CR_STAMP_EQUALITY = 2;
    public static final int ACCESSORID_BY_CR_SESS_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_REC_STAT_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_ROLE_NAME_ID_EQUALITY = 5;
    public static final int ACCESSORID_BY_ORG_ID_EQUALITY = 6;
    public static final int ACCESSORID_BY_ROLE_TYPE_ID_EQUALITY = 7;
    public static final int ACCESSORID_BY_ROLE_NAME_EQUALITY = 8;
    public static final int ACCESSORID_BY_INDEX_ORG_ROLE_DECLARATION_UNQ_EQUALITY = 9;
    public static final int COLINDEX_CR_STAMP = 0;
    public static final int COLINDEX_CR_SESS_ID = 1;
    public static final int COLINDEX_REC_STAT_ID = 2;
    public static final int COLINDEX_ROLE_NAME_ID = 3;
    public static final int COLINDEX_ORG_ID = 4;
    public static final int COLINDEX_ROLE_TYPE_ID = 5;
    public static final int COLINDEX_ROLE_NAME = 6;
    
    public OrgRoleDeclarationTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        crSessIdForeignKey = table.getColumns().get(COLINDEX_CR_SESS_ID).getForeignKey();
        recStatIdForeignKey = table.getColumns().get(COLINDEX_REC_STAT_ID).getForeignKey();
        orgIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_ORG_ID).getForeignKey();
        roleTypeIdForeignKey = table.getColumns().get(COLINDEX_ROLE_TYPE_ID).getForeignKey();
    }
    
    public final OrgRoleDeclarationTable.Record createChildLinkedByOrgId(OrgTable.Record parentRecord)
    {
        return new OrgRoleDeclarationTable.Record(table.createRow(orgIdForeignKey, parentRecord.getRow()));
    }
    
    public final OrgRoleDeclarationTable.Record createRecord()
    {
        return new OrgRoleDeclarationTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByCrSessIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_SESS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrStampEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIndexOrgRoleDeclarationUnqEquality()
    {
        return accessors.get(ACCESSORID_BY_INDEX_ORG_ROLE_DECLARATION_UNQ_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrgIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ORG_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRecStatIdEquality()
    {
        return accessors.get(ACCESSORID_BY_REC_STAT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRoleNameEquality()
    {
        return accessors.get(ACCESSORID_BY_ROLE_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRoleNameIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ROLE_NAME_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRoleTypeIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ROLE_TYPE_ID_EQUALITY);
    }
    
    public final OrgRoleDeclarationTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final GuidTextColumn getCrSessIdColumn()
    {
        return (GuidTextColumn)table.getColumns().get(COLINDEX_CR_SESS_ID);
    }
    
    public final ForeignKey getCrSessIdForeignKey()
    {
        return crSessIdForeignKey;
    }
    
    public final DateTimeColumn getCrStampColumn()
    {
        return (DateTimeColumn)table.getColumns().get(COLINDEX_CR_STAMP);
    }
    
    public final LongIntegerColumn getOrgIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_ORG_ID);
    }
    
    public final ParentForeignKey getOrgIdForeignKey()
    {
        return orgIdForeignKey;
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: Org_Role_Declaration.org_id;
     *  Referenced Columns: Org.org_id
     */
    public final OrgRoleDeclarationTable.Records getParentRecordsByOrgId(OrgTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, orgIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final RecordStatusIdColumn getRecStatIdColumn()
    {
        return (RecordStatusIdColumn)table.getColumns().get(COLINDEX_REC_STAT_ID);
    }
    
    public final ForeignKey getRecStatIdForeignKey()
    {
        return recStatIdForeignKey;
    }
    
    public final OrgRoleDeclarationTable.Record getRecord(Row row)
    {
        return new OrgRoleDeclarationTable.Record(row);
    }
    
    public final OrgRoleDeclarationTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String roleNameId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { roleNameId }, null);
        Record result = row != null ? new OrgRoleDeclarationTable.Record(row) : null;
        return result;
    }
    
    public final OrgRoleDeclarationTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new OrgRoleDeclarationTable.Record(row) : null;
        return result;
    }
    
    public final TextColumn getRoleNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ROLE_NAME);
    }
    
    public final GuidColumn getRoleNameIdColumn()
    {
        return (GuidColumn)table.getColumns().get(COLINDEX_ROLE_NAME_ID);
    }
    
    public final EnumerationIdRefColumn getRoleTypeIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_ROLE_TYPE_ID);
    }
    
    public final ForeignKey getRoleTypeIdForeignKey()
    {
        return roleTypeIdForeignKey;
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private ForeignKey crSessIdForeignKey;
    private ParentForeignKey orgIdForeignKey;
    private ForeignKey recStatIdForeignKey;
    private ForeignKey roleTypeIdForeignKey;
    private Schema schema;
    private com.netspective.axiom.schema.table.BasicTable table;
    
    public final class Record
    {
        
        public Record(Row row)
        {
            if(row.getTable() != table) throw new ClassCastException("Attempting to assign row from table "+ row.getTable().getName() +" to "+ this.getClass().getName() +" (expecting a row from table "+ table.getName() +").");
            this.row = row;
            this.values = row.getColumnValues();
        }
        
        public final boolean dataChangedInStorage(ConnectionContext cc)
        throws NamingException, SQLException
        {
            return table.dataChangedInStorage(cc, row);
        }
        
        public final void delete(ConnectionContext cc, String whereCond, Object[] whereCondBindParams)
        throws NamingException, SQLException
        {
            table.delete(cc, row, whereCond, whereCondBindParams);
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            table.delete(cc, row);
        }
        
        public final TextColumn.TextColumnValue getCrSessId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CR_SESS_ID);
        }
        
        public final DateColumn.DateColumnValue getCrStamp()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_CR_STAMP);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getOrgId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ORG_ID);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRecStatId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REC_STAT_ID);
        }
        
        public final TextColumn.TextColumnValue getRoleName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ROLE_NAME);
        }
        
        public final TextColumn.TextColumnValue getRoleNameId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ROLE_NAME_ID);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRoleTypeId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_ROLE_TYPE_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final auto.dal.db.vo.OrgRoleDeclaration getValues()
        {
            return getValues(new auto.dal.db.vo.impl.OrgRoleDeclarationVO());
        }
        
        public final auto.dal.db.vo.OrgRoleDeclaration getValues(auto.dal.db.vo.OrgRoleDeclaration valueObject)
        {
            valueObject.setCrStamp((java.util.Date) values.getByColumnIndex(COLINDEX_CR_STAMP).getValue());
            valueObject.setCrSessId((java.lang.String) values.getByColumnIndex(COLINDEX_CR_SESS_ID).getValue());
            valueObject.setRecStatId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REC_STAT_ID).getValue());
            valueObject.setRoleNameId((java.lang.String) values.getByColumnIndex(COLINDEX_ROLE_NAME_ID).getValue());
            valueObject.setOrgId((java.lang.Long) values.getByColumnIndex(COLINDEX_ORG_ID).getValue());
            valueObject.setRoleTypeId((java.lang.Integer) values.getByColumnIndex(COLINDEX_ROLE_TYPE_ID).getValue());
            valueObject.setRoleName((java.lang.String) values.getByColumnIndex(COLINDEX_ROLE_NAME).getValue());
            return valueObject;
        }
        
        public final void insert(ConnectionContext cc)
        throws NamingException, SQLException
        {
            table.insert(cc, row);
        }
        
        public final void refresh(ConnectionContext cc)
        throws NamingException, SQLException
        {
            table.refreshData(cc, row);
        }
        
        public final void setCrSessId(com.netspective.commons.value.Value value)
        {
            getCrSessId().copyValueByReference(value);
        }
        
        public final void setCrStamp(com.netspective.commons.value.Value value)
        {
            getCrStamp().copyValueByReference(value);
        }
        
        public final void setOrgId(com.netspective.commons.value.Value value)
        {
            getOrgId().copyValueByReference(value);
        }
        
        public final void setRecStatId(com.netspective.commons.value.Value value)
        {
            getRecStatId().copyValueByReference(value);
        }
        
        public final void setRoleName(com.netspective.commons.value.Value value)
        {
            getRoleName().copyValueByReference(value);
        }
        
        public final void setRoleNameId(com.netspective.commons.value.Value value)
        {
            getRoleNameId().copyValueByReference(value);
        }
        
        public final void setRoleTypeId(com.netspective.commons.value.Value value)
        {
            getRoleTypeId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.OrgRoleDeclaration valueObject)
        {
            values.getByColumnIndex(COLINDEX_CR_STAMP).setValue(valueObject.getCrStamp());
            values.getByColumnIndex(COLINDEX_CR_SESS_ID).setValue(valueObject.getCrSessId());
            values.getByColumnIndex(COLINDEX_REC_STAT_ID).setValue(valueObject.getRecStatId());
            values.getByColumnIndex(COLINDEX_ROLE_NAME_ID).setValue(valueObject.getRoleNameId());
            values.getByColumnIndex(COLINDEX_ORG_ID).setValue(valueObject.getOrgId());
            values.getByColumnIndex(COLINDEX_ROLE_TYPE_ID).setValue(valueObject.getRoleTypeId());
            values.getByColumnIndex(COLINDEX_ROLE_NAME).setValue(valueObject.getRoleName());
        }
        
        public final String toString()
        {
            return row.toString();
        }
        
        public final void update(ConnectionContext cc, String whereCond, Object[] whereCondBindParams)
        throws NamingException, SQLException
        {
            table.update(cc, row, whereCond, whereCondBindParams);
        }
        
        public final void update(ConnectionContext cc)
        throws NamingException, SQLException
        {
            table.update(cc, row);
        }
        private Row row;
        private ColumnValues values;
    }
    
    public final class Records
    {
        
        public Records(Rows rows)
        {
            this.rows = rows;
            this.cache = new Record[rows.size()];
        }
        
        public Records(OrgTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final OrgRoleDeclarationTable.Record get(int i)
        {
            if(cache[i] == null) cache[i] = new Record(rows.getRow(i));
            return cache[i];
        }
        
        public final int size()
        {
            return rows.size();
        }
        
        public final String toString()
        {
            return rows.toString();
        }
        private Record[] cache;
        private OrgTable.Record parentRecord;
        private Rows rows;
    }
}