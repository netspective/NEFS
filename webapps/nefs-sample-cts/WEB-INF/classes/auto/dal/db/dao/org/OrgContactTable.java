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
import com.netspective.axiom.schema.column.type.IntegerColumn;
import auto.dal.db.dao.OrgTable;

public final class OrgContactTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_CR_STAMP_EQUALITY = 2;
    public static final int ACCESSORID_BY_CR_SESS_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_REC_STAT_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_SYSTEM_ID_EQUALITY = 5;
    public static final int ACCESSORID_BY_PARENT_ID_EQUALITY = 6;
    public static final int ACCESSORID_BY_METHOD_TYPE_EQUALITY = 7;
    public static final int ACCESSORID_BY_METHOD_NAME_EQUALITY = 8;
    public static final int ACCESSORID_BY_METHOD_VALUE_EQUALITY = 9;
    public static final int ACCESSORID_BY_PHONE_CC_EQUALITY = 10;
    public static final int ACCESSORID_BY_PHONE_AC_EQUALITY = 11;
    public static final int ACCESSORID_BY_PHONE_PREFIX_EQUALITY = 12;
    public static final int ACCESSORID_BY_PHONE_SUFFIX_EQUALITY = 13;
    public static final int ACCESSORID_BY_INDEX_ORG_CONTACT_UNQ_EQUALITY = 14;
    public static final int COLINDEX_CR_STAMP = 0;
    public static final int COLINDEX_CR_SESS_ID = 1;
    public static final int COLINDEX_REC_STAT_ID = 2;
    public static final int COLINDEX_SYSTEM_ID = 3;
    public static final int COLINDEX_PARENT_ID = 4;
    public static final int COLINDEX_METHOD_TYPE = 5;
    public static final int COLINDEX_METHOD_NAME = 6;
    public static final int COLINDEX_METHOD_VALUE = 7;
    public static final int COLINDEX_PHONE_CC = 8;
    public static final int COLINDEX_PHONE_AC = 9;
    public static final int COLINDEX_PHONE_PREFIX = 10;
    public static final int COLINDEX_PHONE_SUFFIX = 11;
    
    public OrgContactTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        crSessIdForeignKey = table.getColumns().get(COLINDEX_CR_SESS_ID).getForeignKey();
        recStatIdForeignKey = table.getColumns().get(COLINDEX_REC_STAT_ID).getForeignKey();
        parentIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_PARENT_ID).getForeignKey();
        methodTypeForeignKey = table.getColumns().get(COLINDEX_METHOD_TYPE).getForeignKey();
    }
    
    public final OrgContactTable.Record createChildLinkedByParentId(OrgTable.Record parentRecord)
    {
        return new OrgContactTable.Record(table.createRow(parentIdForeignKey, parentRecord.getRow()));
    }
    
    public final OrgContactTable.Record createRecord()
    {
        return new OrgContactTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByCrSessIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_SESS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrStampEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIndexOrgContactUnqEquality()
    {
        return accessors.get(ACCESSORID_BY_INDEX_ORG_CONTACT_UNQ_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByMethodNameEquality()
    {
        return accessors.get(ACCESSORID_BY_METHOD_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByMethodTypeEquality()
    {
        return accessors.get(ACCESSORID_BY_METHOD_TYPE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByMethodValueEquality()
    {
        return accessors.get(ACCESSORID_BY_METHOD_VALUE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentIdEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPhoneAcEquality()
    {
        return accessors.get(ACCESSORID_BY_PHONE_AC_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPhoneCcEquality()
    {
        return accessors.get(ACCESSORID_BY_PHONE_CC_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPhonePrefixEquality()
    {
        return accessors.get(ACCESSORID_BY_PHONE_PREFIX_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPhoneSuffixEquality()
    {
        return accessors.get(ACCESSORID_BY_PHONE_SUFFIX_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRecStatIdEquality()
    {
        return accessors.get(ACCESSORID_BY_REC_STAT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySystemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SYSTEM_ID_EQUALITY);
    }
    
    public final OrgContactTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
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
    
    public final TextColumn getMethodNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_METHOD_NAME);
    }
    
    public final EnumerationIdRefColumn getMethodTypeColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_METHOD_TYPE);
    }
    
    public final ForeignKey getMethodTypeForeignKey()
    {
        return methodTypeForeignKey;
    }
    
    public final TextColumn getMethodValueColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_METHOD_VALUE);
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
     * Parent reference: ParentForeignKey Sources: Org_Contact.parent_id;
     *  Referenced Columns: Org.org_id
     */
    public final OrgContactTable.Records getParentRecordsByParentId(OrgTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, parentIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final IntegerColumn getPhoneAcColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_PHONE_AC);
    }
    
    public final TextColumn getPhoneCcColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_PHONE_CC);
    }
    
    public final IntegerColumn getPhonePrefixColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_PHONE_PREFIX);
    }
    
    public final IntegerColumn getPhoneSuffixColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_PHONE_SUFFIX);
    }
    
    public final RecordStatusIdColumn getRecStatIdColumn()
    {
        return (RecordStatusIdColumn)table.getColumns().get(COLINDEX_REC_STAT_ID);
    }
    
    public final ForeignKey getRecStatIdForeignKey()
    {
        return recStatIdForeignKey;
    }
    
    public final OrgContactTable.Record getRecord(Row row)
    {
        return new OrgContactTable.Record(row);
    }
    
    public final OrgContactTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String systemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { systemId }, null);
        Record result = row != null ? new OrgContactTable.Record(row) : null;
        return result;
    }
    
    public final OrgContactTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new OrgContactTable.Record(row) : null;
        return result;
    }
    
    public final GuidColumn getSystemIdColumn()
    {
        return (GuidColumn)table.getColumns().get(COLINDEX_SYSTEM_ID);
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
    private ForeignKey methodTypeForeignKey;
    private ParentForeignKey parentIdForeignKey;
    private ForeignKey recStatIdForeignKey;
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
        
        public final TextColumn.TextColumnValue getMethodName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_METHOD_NAME);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getMethodType()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_METHOD_TYPE);
        }
        
        public final TextColumn.TextColumnValue getMethodValue()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_METHOD_VALUE);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getParentId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_PARENT_ID);
        }
        
        public final IntegerColumn.IntegerColumnValue getPhoneAc()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_PHONE_AC);
        }
        
        public final TextColumn.TextColumnValue getPhoneCc()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_PHONE_CC);
        }
        
        public final IntegerColumn.IntegerColumnValue getPhonePrefix()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_PHONE_PREFIX);
        }
        
        public final IntegerColumn.IntegerColumnValue getPhoneSuffix()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_PHONE_SUFFIX);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRecStatId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REC_STAT_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getSystemId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SYSTEM_ID);
        }
        
        public final auto.dal.db.vo.OrgContact getValues()
        {
            return getValues(new auto.dal.db.vo.impl.OrgContactVO());
        }
        
        public final auto.dal.db.vo.OrgContact getValues(auto.dal.db.vo.OrgContact valueObject)
        {
            valueObject.setCrStamp((java.util.Date) values.getByColumnIndex(COLINDEX_CR_STAMP).getValue());
            valueObject.setCrSessId((java.lang.String) values.getByColumnIndex(COLINDEX_CR_SESS_ID).getValue());
            valueObject.setRecStatId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REC_STAT_ID).getValue());
            valueObject.setSystemId((java.lang.String) values.getByColumnIndex(COLINDEX_SYSTEM_ID).getValue());
            valueObject.setParentId((java.lang.Long) values.getByColumnIndex(COLINDEX_PARENT_ID).getValue());
            valueObject.setMethodType((java.lang.Integer) values.getByColumnIndex(COLINDEX_METHOD_TYPE).getValue());
            valueObject.setMethodName((java.lang.String) values.getByColumnIndex(COLINDEX_METHOD_NAME).getValue());
            valueObject.setMethodValue((java.lang.String) values.getByColumnIndex(COLINDEX_METHOD_VALUE).getValue());
            valueObject.setPhoneCc((java.lang.String) values.getByColumnIndex(COLINDEX_PHONE_CC).getValue());
            valueObject.setPhoneAc((java.lang.Integer) values.getByColumnIndex(COLINDEX_PHONE_AC).getValue());
            valueObject.setPhonePrefix((java.lang.Integer) values.getByColumnIndex(COLINDEX_PHONE_PREFIX).getValue());
            valueObject.setPhoneSuffix((java.lang.Integer) values.getByColumnIndex(COLINDEX_PHONE_SUFFIX).getValue());
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
        
        public final void setMethodName(com.netspective.commons.value.Value value)
        {
            getMethodName().copyValueByReference(value);
        }
        
        public final void setMethodType(com.netspective.commons.value.Value value)
        {
            getMethodType().copyValueByReference(value);
        }
        
        public final void setMethodValue(com.netspective.commons.value.Value value)
        {
            getMethodValue().copyValueByReference(value);
        }
        
        public final void setParentId(com.netspective.commons.value.Value value)
        {
            getParentId().copyValueByReference(value);
        }
        
        public final void setPhoneAc(com.netspective.commons.value.Value value)
        {
            getPhoneAc().copyValueByReference(value);
        }
        
        public final void setPhoneCc(com.netspective.commons.value.Value value)
        {
            getPhoneCc().copyValueByReference(value);
        }
        
        public final void setPhonePrefix(com.netspective.commons.value.Value value)
        {
            getPhonePrefix().copyValueByReference(value);
        }
        
        public final void setPhoneSuffix(com.netspective.commons.value.Value value)
        {
            getPhoneSuffix().copyValueByReference(value);
        }
        
        public final void setRecStatId(com.netspective.commons.value.Value value)
        {
            getRecStatId().copyValueByReference(value);
        }
        
        public final void setSystemId(com.netspective.commons.value.Value value)
        {
            getSystemId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.OrgContact valueObject)
        {
            values.getByColumnIndex(COLINDEX_CR_STAMP).setValue(valueObject.getCrStamp());
            values.getByColumnIndex(COLINDEX_CR_SESS_ID).setValue(valueObject.getCrSessId());
            values.getByColumnIndex(COLINDEX_REC_STAT_ID).setValue(valueObject.getRecStatId());
            values.getByColumnIndex(COLINDEX_SYSTEM_ID).setValue(valueObject.getSystemId());
            values.getByColumnIndex(COLINDEX_PARENT_ID).setValue(valueObject.getParentId());
            values.getByColumnIndex(COLINDEX_METHOD_TYPE).setValue(valueObject.getMethodType());
            values.getByColumnIndex(COLINDEX_METHOD_NAME).setValue(valueObject.getMethodName());
            values.getByColumnIndex(COLINDEX_METHOD_VALUE).setValue(valueObject.getMethodValue());
            values.getByColumnIndex(COLINDEX_PHONE_CC).setValue(valueObject.getPhoneCc());
            values.getByColumnIndex(COLINDEX_PHONE_AC).setValue(valueObject.getPhoneAc());
            values.getByColumnIndex(COLINDEX_PHONE_PREFIX).setValue(valueObject.getPhonePrefix());
            values.getByColumnIndex(COLINDEX_PHONE_SUFFIX).setValue(valueObject.getPhoneSuffix());
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
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final OrgContactTable.Record get(int i)
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