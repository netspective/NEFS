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
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.column.type.LongIntegerColumn;
import com.netspective.axiom.schema.column.type.IntegerColumn;
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;
import auto.dal.db.dao.PersonTable;

public final class PerEthnicityIdSetTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_SYSTEM_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_PARENT_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_ENUM_INDEX_EQUALITY = 4;
    public static final int ACCESSORID_BY_ENUM_VALUE_EQUALITY = 5;
    public static final int COLINDEX_SYSTEM_ID = 0;
    public static final int COLINDEX_PARENT_ID = 1;
    public static final int COLINDEX_ENUM_INDEX = 2;
    public static final int COLINDEX_ENUM_VALUE = 3;
    
    public PerEthnicityIdSetTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        parentIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_PARENT_ID).getForeignKey();
        enumValueForeignKey = table.getColumns().get(COLINDEX_ENUM_VALUE).getForeignKey();
    }
    
    public final PerEthnicityIdSetTable.Record createChildLinkedByParentId(PersonTable.Record parentRecord)
    {
        return new PerEthnicityIdSetTable.Record(table.createRow(parentIdForeignKey, parentRecord.getRow()));
    }
    
    public final PerEthnicityIdSetTable.Record createRecord()
    {
        return new PerEthnicityIdSetTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByEnumIndexEquality()
    {
        return accessors.get(ACCESSORID_BY_ENUM_INDEX_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByEnumValueEquality()
    {
        return accessors.get(ACCESSORID_BY_ENUM_VALUE_EQUALITY);
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
    
    public final QueryDefnSelect getAccessorBySystemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SYSTEM_ID_EQUALITY);
    }
    
    public final PerEthnicityIdSetTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final IntegerColumn getEnumIndexColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_ENUM_INDEX);
    }
    
    public final EnumerationIdRefColumn getEnumValueColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_ENUM_VALUE);
    }
    
    public final ForeignKey getEnumValueForeignKey()
    {
        return enumValueForeignKey;
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
     * Parent reference: ParentForeignKey Sources: Per_ethnicity_id_Set.parent_id;
     *  Referenced Columns: Person.person_id
     */
    public final PerEthnicityIdSetTable.Records getParentRecordsByParentId(PersonTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, parentIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final PerEthnicityIdSetTable.Record getRecord(Row row)
    {
        return new PerEthnicityIdSetTable.Record(row);
    }
    
    public final PerEthnicityIdSetTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long systemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { systemId }, null);
        Record result = row != null ? new PerEthnicityIdSetTable.Record(row) : null;
        return result;
    }
    
    public final PerEthnicityIdSetTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new PerEthnicityIdSetTable.Record(row) : null;
        return result;
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
    private ForeignKey enumValueForeignKey;
    private ParentForeignKey parentIdForeignKey;
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
        
        public final IntegerColumn.IntegerColumnValue getEnumIndex()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_ENUM_INDEX);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getEnumValue()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_ENUM_VALUE);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getParentId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_PARENT_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getSystemId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_SYSTEM_ID);
        }
        
        public final auto.dal.db.vo.PerEthnicityIdSet getValues()
        {
            return getValues(new auto.dal.db.vo.impl.PerEthnicityIdSetVO());
        }
        
        public final auto.dal.db.vo.PerEthnicityIdSet getValues(auto.dal.db.vo.PerEthnicityIdSet valueObject)
        {
            Object autoIncSystemIdValue = values.getByColumnIndex(COLINDEX_SYSTEM_ID).getValue();
            valueObject.setSystemId(autoIncSystemIdValue instanceof Integer ? new Long(((Integer) autoIncSystemIdValue).intValue()) : (Long) autoIncSystemIdValue);
            valueObject.setParentId((java.lang.Long) values.getByColumnIndex(COLINDEX_PARENT_ID).getValue());
            valueObject.setEnumIndex((java.lang.Integer) values.getByColumnIndex(COLINDEX_ENUM_INDEX).getValue());
            valueObject.setEnumValue((java.lang.Integer) values.getByColumnIndex(COLINDEX_ENUM_VALUE).getValue());
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
        
        public final void setEnumIndex(com.netspective.commons.value.Value value)
        {
            getEnumIndex().copyValueByReference(value);
        }
        
        public final void setEnumValue(com.netspective.commons.value.Value value)
        {
            getEnumValue().copyValueByReference(value);
        }
        
        public final void setParentId(com.netspective.commons.value.Value value)
        {
            getParentId().copyValueByReference(value);
        }
        
        public final void setSystemId(com.netspective.commons.value.Value value)
        {
            getSystemId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.PerEthnicityIdSet valueObject)
        {
            values.getByColumnIndex(COLINDEX_SYSTEM_ID).setValue(valueObject.getSystemId());
            values.getByColumnIndex(COLINDEX_PARENT_ID).setValue(valueObject.getParentId());
            values.getByColumnIndex(COLINDEX_ENUM_INDEX).setValue(valueObject.getEnumIndex());
            values.getByColumnIndex(COLINDEX_ENUM_VALUE).setValue(valueObject.getEnumValue());
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
        
        public final PerEthnicityIdSetTable.Record get(int i)
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