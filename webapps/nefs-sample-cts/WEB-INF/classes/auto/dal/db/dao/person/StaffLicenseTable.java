package auto.dal.db.dao.person;

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
import auto.dal.db.dao.PersonTable;

public final class StaffLicenseTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_CR_STAMP_EQUALITY = 2;
    public static final int ACCESSORID_BY_CR_SESS_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_REC_STAT_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_SYSTEM_ID_EQUALITY = 5;
    public static final int ACCESSORID_BY_PERSON_ID_EQUALITY = 6;
    public static final int ACCESSORID_BY_ORG_ID_EQUALITY = 7;
    public static final int ACCESSORID_BY_LICENSE_TYPE_ID_EQUALITY = 8;
    public static final int ACCESSORID_BY_LICENSE_TYPE_EQUALITY = 9;
    public static final int ACCESSORID_BY_LICENSE_NUM_EQUALITY = 10;
    public static final int ACCESSORID_BY_EXPIRATION_DATE_EQUALITY = 11;
    public static final int ACCESSORID_BY_LICENSE_STATE_ID_EQUALITY = 12;
    public static final int ACCESSORID_BY_LICENSE_STATE_EQUALITY = 13;
    public static final int ACCESSORID_BY_SPECIALITY_TYPE_ID_EQUALITY = 14;
    public static final int COLINDEX_CR_STAMP = 0;
    public static final int COLINDEX_CR_SESS_ID = 1;
    public static final int COLINDEX_REC_STAT_ID = 2;
    public static final int COLINDEX_SYSTEM_ID = 3;
    public static final int COLINDEX_PERSON_ID = 4;
    public static final int COLINDEX_ORG_ID = 5;
    public static final int COLINDEX_LICENSE_TYPE_ID = 6;
    public static final int COLINDEX_LICENSE_TYPE = 7;
    public static final int COLINDEX_LICENSE_NUM = 8;
    public static final int COLINDEX_EXPIRATION_DATE = 9;
    public static final int COLINDEX_LICENSE_STATE_ID = 10;
    public static final int COLINDEX_LICENSE_STATE = 11;
    public static final int COLINDEX_SPECIALITY_TYPE_ID = 12;
    
    public StaffLicenseTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        crSessIdForeignKey = table.getColumns().get(COLINDEX_CR_SESS_ID).getForeignKey();
        recStatIdForeignKey = table.getColumns().get(COLINDEX_REC_STAT_ID).getForeignKey();
        personIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_PERSON_ID).getForeignKey();
        orgIdForeignKey = table.getColumns().get(COLINDEX_ORG_ID).getForeignKey();
        licenseTypeIdForeignKey = table.getColumns().get(COLINDEX_LICENSE_TYPE_ID).getForeignKey();
        licenseStateIdForeignKey = table.getColumns().get(COLINDEX_LICENSE_STATE_ID).getForeignKey();
        specialityTypeIdForeignKey = table.getColumns().get(COLINDEX_SPECIALITY_TYPE_ID).getForeignKey();
    }
    
    public final StaffLicenseTable.Record createChildLinkedByPersonId(PersonTable.Record parentRecord)
    {
        return new StaffLicenseTable.Record(table.createRow(personIdForeignKey, parentRecord.getRow()));
    }
    
    public final StaffLicenseTable.Record createRecord()
    {
        return new StaffLicenseTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByCrSessIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_SESS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrStampEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByExpirationDateEquality()
    {
        return accessors.get(ACCESSORID_BY_EXPIRATION_DATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLicenseNumEquality()
    {
        return accessors.get(ACCESSORID_BY_LICENSE_NUM_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLicenseStateEquality()
    {
        return accessors.get(ACCESSORID_BY_LICENSE_STATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLicenseStateIdEquality()
    {
        return accessors.get(ACCESSORID_BY_LICENSE_STATE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLicenseTypeEquality()
    {
        return accessors.get(ACCESSORID_BY_LICENSE_TYPE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLicenseTypeIdEquality()
    {
        return accessors.get(ACCESSORID_BY_LICENSE_TYPE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrgIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ORG_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPersonIdEquality()
    {
        return accessors.get(ACCESSORID_BY_PERSON_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRecStatIdEquality()
    {
        return accessors.get(ACCESSORID_BY_REC_STAT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySpecialityTypeIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SPECIALITY_TYPE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySystemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SYSTEM_ID_EQUALITY);
    }
    
    public final StaffLicenseTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
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
    
    public final DateColumn getExpirationDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_EXPIRATION_DATE);
    }
    
    public final TextColumn getLicenseNumColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_LICENSE_NUM);
    }
    
    public final TextColumn getLicenseStateColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_LICENSE_STATE);
    }
    
    public final EnumerationIdRefColumn getLicenseStateIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_LICENSE_STATE_ID);
    }
    
    public final ForeignKey getLicenseStateIdForeignKey()
    {
        return licenseStateIdForeignKey;
    }
    
    public final TextColumn getLicenseTypeColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_LICENSE_TYPE);
    }
    
    public final EnumerationIdRefColumn getLicenseTypeIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_LICENSE_TYPE_ID);
    }
    
    public final ForeignKey getLicenseTypeIdForeignKey()
    {
        return licenseTypeIdForeignKey;
    }
    
    public final LongIntegerColumn getOrgIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_ORG_ID);
    }
    
    public final ForeignKey getOrgIdForeignKey()
    {
        return orgIdForeignKey;
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: Staff_License.person_id;
     *  Referenced Columns: Person.person_id
     */
    public final StaffLicenseTable.Records getParentRecordsByPersonId(PersonTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, personIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final LongIntegerColumn getPersonIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_PERSON_ID);
    }
    
    public final ParentForeignKey getPersonIdForeignKey()
    {
        return personIdForeignKey;
    }
    
    public final RecordStatusIdColumn getRecStatIdColumn()
    {
        return (RecordStatusIdColumn)table.getColumns().get(COLINDEX_REC_STAT_ID);
    }
    
    public final ForeignKey getRecStatIdForeignKey()
    {
        return recStatIdForeignKey;
    }
    
    public final StaffLicenseTable.Record getRecord(Row row)
    {
        return new StaffLicenseTable.Record(row);
    }
    
    public final StaffLicenseTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String systemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { systemId }, null);
        Record result = row != null ? new StaffLicenseTable.Record(row) : null;
        return result;
    }
    
    public final StaffLicenseTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new StaffLicenseTable.Record(row) : null;
        return result;
    }
    
    public final EnumerationIdRefColumn getSpecialityTypeIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_SPECIALITY_TYPE_ID);
    }
    
    public final ForeignKey getSpecialityTypeIdForeignKey()
    {
        return specialityTypeIdForeignKey;
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
    private ForeignKey licenseStateIdForeignKey;
    private ForeignKey licenseTypeIdForeignKey;
    private ForeignKey orgIdForeignKey;
    private ParentForeignKey personIdForeignKey;
    private ForeignKey recStatIdForeignKey;
    private Schema schema;
    private ForeignKey specialityTypeIdForeignKey;
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
        
        public final DateColumn.DateColumnValue getExpirationDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_EXPIRATION_DATE);
        }
        
        public final TextColumn.TextColumnValue getLicenseNum()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_LICENSE_NUM);
        }
        
        public final TextColumn.TextColumnValue getLicenseState()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_LICENSE_STATE);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getLicenseStateId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_LICENSE_STATE_ID);
        }
        
        public final TextColumn.TextColumnValue getLicenseType()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_LICENSE_TYPE);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getLicenseTypeId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_LICENSE_TYPE_ID);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getOrgId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ORG_ID);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getPersonId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_PERSON_ID);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRecStatId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REC_STAT_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getSpecialityTypeId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_SPECIALITY_TYPE_ID);
        }
        
        public final TextColumn.TextColumnValue getSystemId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SYSTEM_ID);
        }
        
        public final auto.dal.db.vo.StaffLicense getValues()
        {
            return getValues(new auto.dal.db.vo.impl.StaffLicenseVO());
        }
        
        public final auto.dal.db.vo.StaffLicense getValues(auto.dal.db.vo.StaffLicense valueObject)
        {
            valueObject.setCrStamp((java.util.Date) values.getByColumnIndex(COLINDEX_CR_STAMP).getValue());
            valueObject.setCrSessId((java.lang.String) values.getByColumnIndex(COLINDEX_CR_SESS_ID).getValue());
            valueObject.setRecStatId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REC_STAT_ID).getValue());
            valueObject.setSystemId((java.lang.String) values.getByColumnIndex(COLINDEX_SYSTEM_ID).getValue());
            valueObject.setPersonId((java.lang.Long) values.getByColumnIndex(COLINDEX_PERSON_ID).getValue());
            valueObject.setOrgId((java.lang.Long) values.getByColumnIndex(COLINDEX_ORG_ID).getValue());
            valueObject.setLicenseTypeId((java.lang.Integer) values.getByColumnIndex(COLINDEX_LICENSE_TYPE_ID).getValue());
            valueObject.setLicenseType((java.lang.String) values.getByColumnIndex(COLINDEX_LICENSE_TYPE).getValue());
            valueObject.setLicenseNum((java.lang.String) values.getByColumnIndex(COLINDEX_LICENSE_NUM).getValue());
            valueObject.setExpirationDate((java.util.Date) values.getByColumnIndex(COLINDEX_EXPIRATION_DATE).getValue());
            valueObject.setLicenseStateId((java.lang.Integer) values.getByColumnIndex(COLINDEX_LICENSE_STATE_ID).getValue());
            valueObject.setLicenseState((java.lang.String) values.getByColumnIndex(COLINDEX_LICENSE_STATE).getValue());
            valueObject.setSpecialityTypeId((java.lang.Integer) values.getByColumnIndex(COLINDEX_SPECIALITY_TYPE_ID).getValue());
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
        
        public final void setExpirationDate(com.netspective.commons.value.Value value)
        {
            getExpirationDate().copyValueByReference(value);
        }
        
        public final void setLicenseNum(com.netspective.commons.value.Value value)
        {
            getLicenseNum().copyValueByReference(value);
        }
        
        public final void setLicenseState(com.netspective.commons.value.Value value)
        {
            getLicenseState().copyValueByReference(value);
        }
        
        public final void setLicenseStateId(com.netspective.commons.value.Value value)
        {
            getLicenseStateId().copyValueByReference(value);
        }
        
        public final void setLicenseType(com.netspective.commons.value.Value value)
        {
            getLicenseType().copyValueByReference(value);
        }
        
        public final void setLicenseTypeId(com.netspective.commons.value.Value value)
        {
            getLicenseTypeId().copyValueByReference(value);
        }
        
        public final void setOrgId(com.netspective.commons.value.Value value)
        {
            getOrgId().copyValueByReference(value);
        }
        
        public final void setPersonId(com.netspective.commons.value.Value value)
        {
            getPersonId().copyValueByReference(value);
        }
        
        public final void setRecStatId(com.netspective.commons.value.Value value)
        {
            getRecStatId().copyValueByReference(value);
        }
        
        public final void setSpecialityTypeId(com.netspective.commons.value.Value value)
        {
            getSpecialityTypeId().copyValueByReference(value);
        }
        
        public final void setSystemId(com.netspective.commons.value.Value value)
        {
            getSystemId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.StaffLicense valueObject)
        {
            values.getByColumnIndex(COLINDEX_CR_STAMP).setValue(valueObject.getCrStamp());
            values.getByColumnIndex(COLINDEX_CR_SESS_ID).setValue(valueObject.getCrSessId());
            values.getByColumnIndex(COLINDEX_REC_STAT_ID).setValue(valueObject.getRecStatId());
            values.getByColumnIndex(COLINDEX_SYSTEM_ID).setValue(valueObject.getSystemId());
            values.getByColumnIndex(COLINDEX_PERSON_ID).setValue(valueObject.getPersonId());
            values.getByColumnIndex(COLINDEX_ORG_ID).setValue(valueObject.getOrgId());
            values.getByColumnIndex(COLINDEX_LICENSE_TYPE_ID).setValue(valueObject.getLicenseTypeId());
            values.getByColumnIndex(COLINDEX_LICENSE_TYPE).setValue(valueObject.getLicenseType());
            values.getByColumnIndex(COLINDEX_LICENSE_NUM).setValue(valueObject.getLicenseNum());
            values.getByColumnIndex(COLINDEX_EXPIRATION_DATE).setValue(valueObject.getExpirationDate());
            values.getByColumnIndex(COLINDEX_LICENSE_STATE_ID).setValue(valueObject.getLicenseStateId());
            values.getByColumnIndex(COLINDEX_LICENSE_STATE).setValue(valueObject.getLicenseState());
            values.getByColumnIndex(COLINDEX_SPECIALITY_TYPE_ID).setValue(valueObject.getSpecialityTypeId());
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
        
        public Records(PersonTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final StaffLicenseTable.Record get(int i)
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
        private PersonTable.Record parentRecord;
        private Rows rows;
    }
}