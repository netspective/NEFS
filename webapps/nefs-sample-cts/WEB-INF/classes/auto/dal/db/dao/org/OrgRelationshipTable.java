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
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.column.type.LongIntegerColumn;
import auto.dal.db.dao.OrgTable;

public final class OrgRelationshipTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_CR_STAMP_EQUALITY = 2;
    public static final int ACCESSORID_BY_CR_SESS_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_REC_STAT_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_SYSTEM_ID_EQUALITY = 5;
    public static final int ACCESSORID_BY_PARENT_ID_EQUALITY = 6;
    public static final int ACCESSORID_BY_REL_ENTITY_ID_EQUALITY = 7;
    public static final int ACCESSORID_BY_REL_TYPE_ID_EQUALITY = 8;
    public static final int ACCESSORID_BY_REL_TYPE_EQUALITY = 9;
    public static final int ACCESSORID_BY_REL_BEGIN_EQUALITY = 10;
    public static final int ACCESSORID_BY_REL_END_EQUALITY = 11;
    public static final int ACCESSORID_BY_REL_DESCR_EQUALITY = 12;
    public static final int COLINDEX_CR_STAMP = 0;
    public static final int COLINDEX_CR_SESS_ID = 1;
    public static final int COLINDEX_REC_STAT_ID = 2;
    public static final int COLINDEX_SYSTEM_ID = 3;
    public static final int COLINDEX_PARENT_ID = 4;
    public static final int COLINDEX_REL_ENTITY_ID = 5;
    public static final int COLINDEX_REL_TYPE_ID = 6;
    public static final int COLINDEX_REL_TYPE = 7;
    public static final int COLINDEX_REL_BEGIN = 8;
    public static final int COLINDEX_REL_END = 9;
    public static final int COLINDEX_REL_DESCR = 10;
    
    public OrgRelationshipTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        crSessIdForeignKey = table.getColumns().get(COLINDEX_CR_SESS_ID).getForeignKey();
        recStatIdForeignKey = table.getColumns().get(COLINDEX_REC_STAT_ID).getForeignKey();
        parentIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_PARENT_ID).getForeignKey();
        relEntityIdForeignKey = table.getColumns().get(COLINDEX_REL_ENTITY_ID).getForeignKey();
        relTypeIdForeignKey = table.getColumns().get(COLINDEX_REL_TYPE_ID).getForeignKey();
    }
    
    public final OrgRelationshipTable.Record createChildLinkedByParentId(OrgTable.Record parentRecord)
    {
        return new OrgRelationshipTable.Record(table.createRow(parentIdForeignKey, parentRecord.getRow()));
    }
    
    public final OrgRelationshipTable.Record createRecord()
    {
        return new OrgRelationshipTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByCrSessIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_SESS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrStampEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentIdEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_ID_EQUALITY);
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
    
    public final QueryDefnSelect getAccessorByRelBeginEquality()
    {
        return accessors.get(ACCESSORID_BY_REL_BEGIN_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRelDescrEquality()
    {
        return accessors.get(ACCESSORID_BY_REL_DESCR_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRelEndEquality()
    {
        return accessors.get(ACCESSORID_BY_REL_END_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRelEntityIdEquality()
    {
        return accessors.get(ACCESSORID_BY_REL_ENTITY_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRelTypeEquality()
    {
        return accessors.get(ACCESSORID_BY_REL_TYPE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRelTypeIdEquality()
    {
        return accessors.get(ACCESSORID_BY_REL_TYPE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySystemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SYSTEM_ID_EQUALITY);
    }
    
    public final OrgRelationshipTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
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
    
    public final LongIntegerColumn getParentIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_PARENT_ID);
    }
    
    public final ParentForeignKey getParentIdForeignKey()
    {
        return parentIdForeignKey;
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: Org_Relationship.parent_id;
     *  Referenced Columns: Org.org_id
     */
    public final OrgRelationshipTable.Records getParentRecordsByParentId(OrgTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, parentIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
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
    
    public final OrgRelationshipTable.Record getRecord(Row row)
    {
        return new OrgRelationshipTable.Record(row);
    }
    
    public final OrgRelationshipTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long systemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { systemId }, null);
        Record result = row != null ? new OrgRelationshipTable.Record(row) : null;
        return result;
    }
    
    public final OrgRelationshipTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new OrgRelationshipTable.Record(row) : null;
        return result;
    }
    
    public final DateColumn getRelBeginColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_REL_BEGIN);
    }
    
    public final TextColumn getRelDescrColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_REL_DESCR);
    }
    
    public final DateColumn getRelEndColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_REL_END);
    }
    
    public final LongIntegerColumn getRelEntityIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_REL_ENTITY_ID);
    }
    
    public final ForeignKey getRelEntityIdForeignKey()
    {
        return relEntityIdForeignKey;
    }
    
    public final TextColumn getRelTypeColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_REL_TYPE);
    }
    
    public final EnumerationIdRefColumn getRelTypeIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_REL_TYPE_ID);
    }
    
    public final ForeignKey getRelTypeIdForeignKey()
    {
        return relTypeIdForeignKey;
    }
    
    public final AutoIncColumn getSystemIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_SYSTEM_ID);
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
    private ParentForeignKey parentIdForeignKey;
    private ForeignKey recStatIdForeignKey;
    private ForeignKey relEntityIdForeignKey;
    private ForeignKey relTypeIdForeignKey;
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
        
        public final LongIntegerColumn.LongIntegerColumnValue getParentId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_PARENT_ID);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRecStatId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REC_STAT_ID);
        }
        
        public final DateColumn.DateColumnValue getRelBegin()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_REL_BEGIN);
        }
        
        public final TextColumn.TextColumnValue getRelDescr()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_REL_DESCR);
        }
        
        public final DateColumn.DateColumnValue getRelEnd()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_REL_END);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getRelEntityId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_REL_ENTITY_ID);
        }
        
        public final TextColumn.TextColumnValue getRelType()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_REL_TYPE);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRelTypeId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REL_TYPE_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getSystemId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_SYSTEM_ID);
        }
        
        public final auto.dal.db.vo.OrgRelationship getValues()
        {
            return getValues(new auto.dal.db.vo.impl.OrgRelationshipVO());
        }
        
        public final auto.dal.db.vo.OrgRelationship getValues(auto.dal.db.vo.OrgRelationship valueObject)
        {
            valueObject.setCrStamp((java.util.Date) values.getByColumnIndex(COLINDEX_CR_STAMP).getValue());
            valueObject.setCrSessId((java.lang.String) values.getByColumnIndex(COLINDEX_CR_SESS_ID).getValue());
            valueObject.setRecStatId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REC_STAT_ID).getValue());
            Object autoIncSystemIdValue = values.getByColumnIndex(COLINDEX_SYSTEM_ID).getValue();
            valueObject.setSystemId(autoIncSystemIdValue instanceof Integer ? new Long(((Integer) autoIncSystemIdValue).intValue()) : (Long) autoIncSystemIdValue);
            valueObject.setParentId((java.lang.Long) values.getByColumnIndex(COLINDEX_PARENT_ID).getValue());
            valueObject.setRelEntityId((java.lang.Long) values.getByColumnIndex(COLINDEX_REL_ENTITY_ID).getValue());
            valueObject.setRelTypeId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REL_TYPE_ID).getValue());
            valueObject.setRelType((java.lang.String) values.getByColumnIndex(COLINDEX_REL_TYPE).getValue());
            valueObject.setRelBegin((java.util.Date) values.getByColumnIndex(COLINDEX_REL_BEGIN).getValue());
            valueObject.setRelEnd((java.util.Date) values.getByColumnIndex(COLINDEX_REL_END).getValue());
            valueObject.setRelDescr((java.lang.String) values.getByColumnIndex(COLINDEX_REL_DESCR).getValue());
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
        
        public final void setParentId(com.netspective.commons.value.Value value)
        {
            getParentId().copyValueByReference(value);
        }
        
        public final void setRecStatId(com.netspective.commons.value.Value value)
        {
            getRecStatId().copyValueByReference(value);
        }
        
        public final void setRelBegin(com.netspective.commons.value.Value value)
        {
            getRelBegin().copyValueByReference(value);
        }
        
        public final void setRelDescr(com.netspective.commons.value.Value value)
        {
            getRelDescr().copyValueByReference(value);
        }
        
        public final void setRelEnd(com.netspective.commons.value.Value value)
        {
            getRelEnd().copyValueByReference(value);
        }
        
        public final void setRelEntityId(com.netspective.commons.value.Value value)
        {
            getRelEntityId().copyValueByReference(value);
        }
        
        public final void setRelType(com.netspective.commons.value.Value value)
        {
            getRelType().copyValueByReference(value);
        }
        
        public final void setRelTypeId(com.netspective.commons.value.Value value)
        {
            getRelTypeId().copyValueByReference(value);
        }
        
        public final void setSystemId(com.netspective.commons.value.Value value)
        {
            getSystemId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.OrgRelationship valueObject)
        {
            values.getByColumnIndex(COLINDEX_CR_STAMP).setValue(valueObject.getCrStamp());
            values.getByColumnIndex(COLINDEX_CR_SESS_ID).setValue(valueObject.getCrSessId());
            values.getByColumnIndex(COLINDEX_REC_STAT_ID).setValue(valueObject.getRecStatId());
            values.getByColumnIndex(COLINDEX_SYSTEM_ID).setValue(valueObject.getSystemId());
            values.getByColumnIndex(COLINDEX_PARENT_ID).setValue(valueObject.getParentId());
            values.getByColumnIndex(COLINDEX_REL_ENTITY_ID).setValue(valueObject.getRelEntityId());
            values.getByColumnIndex(COLINDEX_REL_TYPE_ID).setValue(valueObject.getRelTypeId());
            values.getByColumnIndex(COLINDEX_REL_TYPE).setValue(valueObject.getRelType());
            values.getByColumnIndex(COLINDEX_REL_BEGIN).setValue(valueObject.getRelBegin());
            values.getByColumnIndex(COLINDEX_REL_END).setValue(valueObject.getRelEnd());
            values.getByColumnIndex(COLINDEX_REL_DESCR).setValue(valueObject.getRelDescr());
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
        
        public final OrgRelationshipTable.Record get(int i)
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