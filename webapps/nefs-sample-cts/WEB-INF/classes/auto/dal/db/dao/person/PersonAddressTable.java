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
import com.netspective.axiom.schema.column.type.BooleanColumn;
import auto.dal.db.dao.PersonTable;

public final class PersonAddressTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_CR_STAMP_EQUALITY = 2;
    public static final int ACCESSORID_BY_CR_SESS_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_REC_STAT_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_SYSTEM_ID_EQUALITY = 5;
    public static final int ACCESSORID_BY_PARENT_ID_EQUALITY = 6;
    public static final int ACCESSORID_BY_ADDRESS_NAME_EQUALITY = 7;
    public static final int ACCESSORID_BY_MAILING_EQUALITY = 8;
    public static final int ACCESSORID_BY_ADDRESS_TYPE_ID_EQUALITY = 9;
    public static final int ACCESSORID_BY_LINE1_EQUALITY = 10;
    public static final int ACCESSORID_BY_LINE2_EQUALITY = 11;
    public static final int ACCESSORID_BY_CITY_EQUALITY = 12;
    public static final int ACCESSORID_BY_COUNTY_EQUALITY = 13;
    public static final int ACCESSORID_BY_STATE_ID_EQUALITY = 14;
    public static final int ACCESSORID_BY_STATE_EQUALITY = 15;
    public static final int ACCESSORID_BY_ZIP_EQUALITY = 16;
    public static final int ACCESSORID_BY_COUNTRY_EQUALITY = 17;
    public static final int ACCESSORID_BY_INDEX_PERSON_ADDRESS_UNQ_EQUALITY = 18;
    public static final int COLINDEX_CR_STAMP = 0;
    public static final int COLINDEX_CR_SESS_ID = 1;
    public static final int COLINDEX_REC_STAT_ID = 2;
    public static final int COLINDEX_SYSTEM_ID = 3;
    public static final int COLINDEX_PARENT_ID = 4;
    public static final int COLINDEX_ADDRESS_NAME = 5;
    public static final int COLINDEX_MAILING = 6;
    public static final int COLINDEX_ADDRESS_TYPE_ID = 7;
    public static final int COLINDEX_LINE1 = 8;
    public static final int COLINDEX_LINE2 = 9;
    public static final int COLINDEX_CITY = 10;
    public static final int COLINDEX_COUNTY = 11;
    public static final int COLINDEX_STATE_ID = 12;
    public static final int COLINDEX_STATE = 13;
    public static final int COLINDEX_ZIP = 14;
    public static final int COLINDEX_COUNTRY = 15;
    
    public PersonAddressTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        crSessIdForeignKey = table.getColumns().get(COLINDEX_CR_SESS_ID).getForeignKey();
        recStatIdForeignKey = table.getColumns().get(COLINDEX_REC_STAT_ID).getForeignKey();
        parentIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_PARENT_ID).getForeignKey();
        addressTypeIdForeignKey = table.getColumns().get(COLINDEX_ADDRESS_TYPE_ID).getForeignKey();
        stateIdForeignKey = table.getColumns().get(COLINDEX_STATE_ID).getForeignKey();
    }
    
    public final PersonAddressTable.Record createChildLinkedByParentId(PersonTable.Record parentRecord)
    {
        return new PersonAddressTable.Record(table.createRow(parentIdForeignKey, parentRecord.getRow()));
    }
    
    public final PersonAddressTable.Record createRecord()
    {
        return new PersonAddressTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByAddressNameEquality()
    {
        return accessors.get(ACCESSORID_BY_ADDRESS_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByAddressTypeIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ADDRESS_TYPE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCityEquality()
    {
        return accessors.get(ACCESSORID_BY_CITY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCountryEquality()
    {
        return accessors.get(ACCESSORID_BY_COUNTRY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCountyEquality()
    {
        return accessors.get(ACCESSORID_BY_COUNTY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrSessIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_SESS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrStampEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIndexPersonAddressUnqEquality()
    {
        return accessors.get(ACCESSORID_BY_INDEX_PERSON_ADDRESS_UNQ_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLine1Equality()
    {
        return accessors.get(ACCESSORID_BY_LINE1_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLine2Equality()
    {
        return accessors.get(ACCESSORID_BY_LINE2_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByMailingEquality()
    {
        return accessors.get(ACCESSORID_BY_MAILING_EQUALITY);
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
    
    public final QueryDefnSelect getAccessorByStateEquality()
    {
        return accessors.get(ACCESSORID_BY_STATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStateIdEquality()
    {
        return accessors.get(ACCESSORID_BY_STATE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySystemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SYSTEM_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByZipEquality()
    {
        return accessors.get(ACCESSORID_BY_ZIP_EQUALITY);
    }
    
    public final PersonAddressTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getAddressNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ADDRESS_NAME);
    }
    
    public final EnumerationIdRefColumn getAddressTypeIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_ADDRESS_TYPE_ID);
    }
    
    public final ForeignKey getAddressTypeIdForeignKey()
    {
        return addressTypeIdForeignKey;
    }
    
    public final TextColumn getCityColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_CITY);
    }
    
    public final TextColumn getCountryColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_COUNTRY);
    }
    
    public final TextColumn getCountyColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_COUNTY);
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
    
    public final TextColumn getLine1Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_LINE1);
    }
    
    public final TextColumn getLine2Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_LINE2);
    }
    
    public final BooleanColumn getMailingColumn()
    {
        return (BooleanColumn)table.getColumns().get(COLINDEX_MAILING);
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
     * Parent reference: ParentForeignKey Sources: Person_Address.parent_id;
     *  Referenced Columns: Person.person_id
     */
    public final PersonAddressTable.Records getParentRecordsByParentId(PersonTable.Record parentRecord, ConnectionContext cc)
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
    
    public final PersonAddressTable.Record getRecord(Row row)
    {
        return new PersonAddressTable.Record(row);
    }
    
    public final PersonAddressTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String systemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { systemId }, null);
        Record result = row != null ? new PersonAddressTable.Record(row) : null;
        return result;
    }
    
    public final PersonAddressTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new PersonAddressTable.Record(row) : null;
        return result;
    }
    
    public final TextColumn getStateColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_STATE);
    }
    
    public final EnumerationIdRefColumn getStateIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_STATE_ID);
    }
    
    public final ForeignKey getStateIdForeignKey()
    {
        return stateIdForeignKey;
    }
    
    public final GuidColumn getSystemIdColumn()
    {
        return (GuidColumn)table.getColumns().get(COLINDEX_SYSTEM_ID);
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final TextColumn getZipColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ZIP);
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private ForeignKey addressTypeIdForeignKey;
    private ForeignKey crSessIdForeignKey;
    private ParentForeignKey parentIdForeignKey;
    private ForeignKey recStatIdForeignKey;
    private Schema schema;
    private ForeignKey stateIdForeignKey;
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
        
        public final TextColumn.TextColumnValue getAddressName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ADDRESS_NAME);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getAddressTypeId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_ADDRESS_TYPE_ID);
        }
        
        public final TextColumn.TextColumnValue getCity()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CITY);
        }
        
        public final TextColumn.TextColumnValue getCountry()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_COUNTRY);
        }
        
        public final TextColumn.TextColumnValue getCounty()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_COUNTY);
        }
        
        public final TextColumn.TextColumnValue getCrSessId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CR_SESS_ID);
        }
        
        public final DateColumn.DateColumnValue getCrStamp()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_CR_STAMP);
        }
        
        public final TextColumn.TextColumnValue getLine1()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_LINE1);
        }
        
        public final TextColumn.TextColumnValue getLine2()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_LINE2);
        }
        
        public final BooleanColumn.BooleanColumnValue getMailing()
        {
            return (BooleanColumn.BooleanColumnValue)values.getByColumnIndex(COLINDEX_MAILING);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getParentId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_PARENT_ID);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRecStatId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REC_STAT_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getState()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_STATE);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getStateId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_STATE_ID);
        }
        
        public final TextColumn.TextColumnValue getSystemId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SYSTEM_ID);
        }
        
        public final auto.dal.db.vo.PersonAddress getValues()
        {
            return getValues(new auto.dal.db.vo.impl.PersonAddressVO());
        }
        
        public final auto.dal.db.vo.PersonAddress getValues(auto.dal.db.vo.PersonAddress valueObject)
        {
            valueObject.setCrStamp((java.util.Date) values.getByColumnIndex(COLINDEX_CR_STAMP).getValue());
            valueObject.setCrSessId((java.lang.String) values.getByColumnIndex(COLINDEX_CR_SESS_ID).getValue());
            valueObject.setRecStatId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REC_STAT_ID).getValue());
            valueObject.setSystemId((java.lang.String) values.getByColumnIndex(COLINDEX_SYSTEM_ID).getValue());
            valueObject.setParentId((java.lang.Long) values.getByColumnIndex(COLINDEX_PARENT_ID).getValue());
            valueObject.setAddressName((java.lang.String) values.getByColumnIndex(COLINDEX_ADDRESS_NAME).getValue());
            valueObject.setMailing((java.lang.Boolean) values.getByColumnIndex(COLINDEX_MAILING).getValue());
            valueObject.setAddressTypeId((java.lang.Integer) values.getByColumnIndex(COLINDEX_ADDRESS_TYPE_ID).getValue());
            valueObject.setLine1((java.lang.String) values.getByColumnIndex(COLINDEX_LINE1).getValue());
            valueObject.setLine2((java.lang.String) values.getByColumnIndex(COLINDEX_LINE2).getValue());
            valueObject.setCity((java.lang.String) values.getByColumnIndex(COLINDEX_CITY).getValue());
            valueObject.setCounty((java.lang.String) values.getByColumnIndex(COLINDEX_COUNTY).getValue());
            valueObject.setStateId((java.lang.Integer) values.getByColumnIndex(COLINDEX_STATE_ID).getValue());
            valueObject.setState((java.lang.String) values.getByColumnIndex(COLINDEX_STATE).getValue());
            valueObject.setZip((java.lang.String) values.getByColumnIndex(COLINDEX_ZIP).getValue());
            valueObject.setCountry((java.lang.String) values.getByColumnIndex(COLINDEX_COUNTRY).getValue());
            return valueObject;
        }
        
        public final TextColumn.TextColumnValue getZip()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ZIP);
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
        
        public final void setAddressName(com.netspective.commons.value.Value value)
        {
            getAddressName().copyValueByReference(value);
        }
        
        public final void setAddressTypeId(com.netspective.commons.value.Value value)
        {
            getAddressTypeId().copyValueByReference(value);
        }
        
        public final void setCity(com.netspective.commons.value.Value value)
        {
            getCity().copyValueByReference(value);
        }
        
        public final void setCountry(com.netspective.commons.value.Value value)
        {
            getCountry().copyValueByReference(value);
        }
        
        public final void setCounty(com.netspective.commons.value.Value value)
        {
            getCounty().copyValueByReference(value);
        }
        
        public final void setCrSessId(com.netspective.commons.value.Value value)
        {
            getCrSessId().copyValueByReference(value);
        }
        
        public final void setCrStamp(com.netspective.commons.value.Value value)
        {
            getCrStamp().copyValueByReference(value);
        }
        
        public final void setLine1(com.netspective.commons.value.Value value)
        {
            getLine1().copyValueByReference(value);
        }
        
        public final void setLine2(com.netspective.commons.value.Value value)
        {
            getLine2().copyValueByReference(value);
        }
        
        public final void setMailing(com.netspective.commons.value.Value value)
        {
            getMailing().copyValueByReference(value);
        }
        
        public final void setParentId(com.netspective.commons.value.Value value)
        {
            getParentId().copyValueByReference(value);
        }
        
        public final void setRecStatId(com.netspective.commons.value.Value value)
        {
            getRecStatId().copyValueByReference(value);
        }
        
        public final void setState(com.netspective.commons.value.Value value)
        {
            getState().copyValueByReference(value);
        }
        
        public final void setStateId(com.netspective.commons.value.Value value)
        {
            getStateId().copyValueByReference(value);
        }
        
        public final void setSystemId(com.netspective.commons.value.Value value)
        {
            getSystemId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.PersonAddress valueObject)
        {
            values.getByColumnIndex(COLINDEX_CR_STAMP).setValue(valueObject.getCrStamp());
            values.getByColumnIndex(COLINDEX_CR_SESS_ID).setValue(valueObject.getCrSessId());
            values.getByColumnIndex(COLINDEX_REC_STAT_ID).setValue(valueObject.getRecStatId());
            values.getByColumnIndex(COLINDEX_SYSTEM_ID).setValue(valueObject.getSystemId());
            values.getByColumnIndex(COLINDEX_PARENT_ID).setValue(valueObject.getParentId());
            values.getByColumnIndex(COLINDEX_ADDRESS_NAME).setValue(valueObject.getAddressName());
            values.getByColumnIndex(COLINDEX_MAILING).setValue(valueObject.getMailing());
            values.getByColumnIndex(COLINDEX_ADDRESS_TYPE_ID).setValue(valueObject.getAddressTypeId());
            values.getByColumnIndex(COLINDEX_LINE1).setValue(valueObject.getLine1());
            values.getByColumnIndex(COLINDEX_LINE2).setValue(valueObject.getLine2());
            values.getByColumnIndex(COLINDEX_CITY).setValue(valueObject.getCity());
            values.getByColumnIndex(COLINDEX_COUNTY).setValue(valueObject.getCounty());
            values.getByColumnIndex(COLINDEX_STATE_ID).setValue(valueObject.getStateId());
            values.getByColumnIndex(COLINDEX_STATE).setValue(valueObject.getState());
            values.getByColumnIndex(COLINDEX_ZIP).setValue(valueObject.getZip());
            values.getByColumnIndex(COLINDEX_COUNTRY).setValue(valueObject.getCountry());
        }
        
        public final void setZip(com.netspective.commons.value.Value value)
        {
            getZip().copyValueByReference(value);
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
        
        public final PersonAddressTable.Record get(int i)
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