package auto.dal.db.dao;

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
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.column.type.LongIntegerColumn;
import com.netspective.axiom.schema.column.type.TextColumn;

public final class SupplierTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_SUPPLIER_ID_EQUALITY = 1;
    public static final int ACCESSORID_BY_NAME_EQUALITY = 2;
    public static final int ACCESSORID_BY_STATUS_EQUALITY = 3;
    public static final int ACCESSORID_BY_ADDR1_EQUALITY = 4;
    public static final int ACCESSORID_BY_ADDR2_EQUALITY = 5;
    public static final int ACCESSORID_BY_CITY_EQUALITY = 6;
    public static final int ACCESSORID_BY_STATE_EQUALITY = 7;
    public static final int ACCESSORID_BY_COUNTRY_EQUALITY = 8;
    public static final int ACCESSORID_BY_PHONE_EQUALITY = 9;
    public static final int COLINDEX_SUPPLIER_ID = 0;
    public static final int COLINDEX_NAME = 1;
    public static final int COLINDEX_STATUS = 2;
    public static final int COLINDEX_ADDR1 = 3;
    public static final int COLINDEX_ADDR2 = 4;
    public static final int COLINDEX_CITY = 5;
    public static final int COLINDEX_STATE = 6;
    public static final int COLINDEX_COUNTRY = 7;
    public static final int COLINDEX_PHONE = 8;
    
    public SupplierTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
    }
    
    public final SupplierTable.Record createRecord()
    {
        return new SupplierTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByAddr1Equality()
    {
        return accessors.get(ACCESSORID_BY_ADDR1_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByAddr2Equality()
    {
        return accessors.get(ACCESSORID_BY_ADDR2_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCityEquality()
    {
        return accessors.get(ACCESSORID_BY_CITY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCountryEquality()
    {
        return accessors.get(ACCESSORID_BY_COUNTRY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNameEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPhoneEquality()
    {
        return accessors.get(ACCESSORID_BY_PHONE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStateEquality()
    {
        return accessors.get(ACCESSORID_BY_STATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStatusEquality()
    {
        return accessors.get(ACCESSORID_BY_STATUS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySupplierIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SUPPLIER_ID_EQUALITY);
    }
    
    public final SupplierTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getAddr1Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ADDR1);
    }
    
    public final TextColumn getAddr2Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ADDR2);
    }
    
    public final TextColumn getCityColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_CITY);
    }
    
    public final TextColumn getCountryColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_COUNTRY);
    }
    
    public final TextColumn getNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME);
    }
    
    public final TextColumn getPhoneColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_PHONE);
    }
    
    public final SupplierTable.Record getRecord(Row row)
    {
        return new SupplierTable.Record(row);
    }
    
    public final SupplierTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long supplierId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { supplierId }, null);
        Record result = row != null ? new SupplierTable.Record(row) : null;
        return result;
    }
    
    public final SupplierTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new SupplierTable.Record(row) : null;
        return result;
    }
    
    public final TextColumn getStateColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_STATE);
    }
    
    public final TextColumn getStatusColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_STATUS);
    }
    
    public final AutoIncColumn getSupplierIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_SUPPLIER_ID);
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
        
        public final TextColumn.TextColumnValue getAddr1()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ADDR1);
        }
        
        public final TextColumn.TextColumnValue getAddr2()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ADDR2);
        }
        
        public final TextColumn.TextColumnValue getCity()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CITY);
        }
        
        public final TextColumn.TextColumnValue getCountry()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_COUNTRY);
        }
        
        public final TextColumn.TextColumnValue getName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME);
        }
        
        public final TextColumn.TextColumnValue getPhone()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_PHONE);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getState()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_STATE);
        }
        
        public final TextColumn.TextColumnValue getStatus()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_STATUS);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getSupplierId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_SUPPLIER_ID);
        }
        
        public final auto.dal.db.vo.Supplier getValues()
        {
            return getValues(new auto.dal.db.vo.impl.SupplierVO());
        }
        
        public final auto.dal.db.vo.Supplier getValues(auto.dal.db.vo.Supplier valueObject)
        {
            Object autoIncSupplierIdValue = values.getByColumnIndex(COLINDEX_SUPPLIER_ID).getValue();
            valueObject.setSupplierId(autoIncSupplierIdValue instanceof Integer ? new Long(((Integer) autoIncSupplierIdValue).intValue()) : (Long) autoIncSupplierIdValue);
            valueObject.setName((java.lang.String) values.getByColumnIndex(COLINDEX_NAME).getValue());
            valueObject.setStatus((java.lang.String) values.getByColumnIndex(COLINDEX_STATUS).getValue());
            valueObject.setAddr1((java.lang.String) values.getByColumnIndex(COLINDEX_ADDR1).getValue());
            valueObject.setAddr2((java.lang.String) values.getByColumnIndex(COLINDEX_ADDR2).getValue());
            valueObject.setCity((java.lang.String) values.getByColumnIndex(COLINDEX_CITY).getValue());
            valueObject.setState((java.lang.String) values.getByColumnIndex(COLINDEX_STATE).getValue());
            valueObject.setCountry((java.lang.String) values.getByColumnIndex(COLINDEX_COUNTRY).getValue());
            valueObject.setPhone((java.lang.String) values.getByColumnIndex(COLINDEX_PHONE).getValue());
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
        
        public final void setAddr1(com.netspective.commons.value.Value value)
        {
            getAddr1().copyValueByReference(value);
        }
        
        public final void setAddr2(com.netspective.commons.value.Value value)
        {
            getAddr2().copyValueByReference(value);
        }
        
        public final void setCity(com.netspective.commons.value.Value value)
        {
            getCity().copyValueByReference(value);
        }
        
        public final void setCountry(com.netspective.commons.value.Value value)
        {
            getCountry().copyValueByReference(value);
        }
        
        public final void setName(com.netspective.commons.value.Value value)
        {
            getName().copyValueByReference(value);
        }
        
        public final void setPhone(com.netspective.commons.value.Value value)
        {
            getPhone().copyValueByReference(value);
        }
        
        public final void setState(com.netspective.commons.value.Value value)
        {
            getState().copyValueByReference(value);
        }
        
        public final void setStatus(com.netspective.commons.value.Value value)
        {
            getStatus().copyValueByReference(value);
        }
        
        public final void setSupplierId(com.netspective.commons.value.Value value)
        {
            getSupplierId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Supplier valueObject)
        {
            values.getByColumnIndex(COLINDEX_SUPPLIER_ID).setValue(valueObject.getSupplierId());
            values.getByColumnIndex(COLINDEX_NAME).setValue(valueObject.getName());
            values.getByColumnIndex(COLINDEX_STATUS).setValue(valueObject.getStatus());
            values.getByColumnIndex(COLINDEX_ADDR1).setValue(valueObject.getAddr1());
            values.getByColumnIndex(COLINDEX_ADDR2).setValue(valueObject.getAddr2());
            values.getByColumnIndex(COLINDEX_CITY).setValue(valueObject.getCity());
            values.getByColumnIndex(COLINDEX_STATE).setValue(valueObject.getState());
            values.getByColumnIndex(COLINDEX_COUNTRY).setValue(valueObject.getCountry());
            values.getByColumnIndex(COLINDEX_PHONE).setValue(valueObject.getPhone());
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
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final SupplierTable.Record get(int i)
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
        private Rows rows;
    }
}