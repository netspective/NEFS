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
import com.netspective.axiom.schema.column.type.IntegerColumn;
import com.netspective.axiom.schema.column.type.TextColumn;

public final class VisitedPageTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_SYSTEM_ID_EQUALITY = 1;
    public static final int ACCESSORID_BY_PIN_EQUALITY = 2;
    public static final int ACCESSORID_BY_PAGE_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_VISIT_COUNT_EQUALITY = 4;
    public static final int ACCESSORID_BY_INDEX_UNIQUE_VISIT_EQUALITY = 5;
    public static final int COLINDEX_SYSTEM_ID = 0;
    public static final int COLINDEX_PIN = 1;
    public static final int COLINDEX_PAGE_ID = 2;
    public static final int COLINDEX_VISIT_COUNT = 3;
    
    public VisitedPageTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
    }
    
    public final VisitedPageTable.Record createRecord()
    {
        return new VisitedPageTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByIndexUniqueVisitEquality()
    {
        return accessors.get(ACCESSORID_BY_INDEX_UNIQUE_VISIT_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPageIdEquality()
    {
        return accessors.get(ACCESSORID_BY_PAGE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPinEquality()
    {
        return accessors.get(ACCESSORID_BY_PIN_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySystemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SYSTEM_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByVisitCountEquality()
    {
        return accessors.get(ACCESSORID_BY_VISIT_COUNT_EQUALITY);
    }
    
    public final VisitedPageTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getPageIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_PAGE_ID);
    }
    
    public final IntegerColumn getPinColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_PIN);
    }
    
    public final VisitedPageTable.Record getRecord(Row row)
    {
        return new VisitedPageTable.Record(row);
    }
    
    public final VisitedPageTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long systemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { systemId }, null);
        Record result = row != null ? new VisitedPageTable.Record(row) : null;
        return result;
    }
    
    public final VisitedPageTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new VisitedPageTable.Record(row) : null;
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
    
    public final IntegerColumn getVisitCountColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_VISIT_COUNT);
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
        
        public final TextColumn.TextColumnValue getPageId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_PAGE_ID);
        }
        
        public final IntegerColumn.IntegerColumnValue getPin()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_PIN);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getSystemId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_SYSTEM_ID);
        }
        
        public final auto.dal.db.vo.VisitedPage getValues()
        {
            return getValues(new auto.dal.db.vo.impl.VisitedPageVO());
        }
        
        public final auto.dal.db.vo.VisitedPage getValues(auto.dal.db.vo.VisitedPage valueObject)
        {
            Object autoIncSystemIdValue = values.getByColumnIndex(COLINDEX_SYSTEM_ID).getValue();
            valueObject.setSystemId(autoIncSystemIdValue instanceof Integer ? new Long(((Integer) autoIncSystemIdValue).intValue()) : (Long) autoIncSystemIdValue);
            valueObject.setPin((java.lang.Integer) values.getByColumnIndex(COLINDEX_PIN).getValue());
            valueObject.setPageId((java.lang.String) values.getByColumnIndex(COLINDEX_PAGE_ID).getValue());
            valueObject.setVisitCount((java.lang.Integer) values.getByColumnIndex(COLINDEX_VISIT_COUNT).getValue());
            return valueObject;
        }
        
        public final IntegerColumn.IntegerColumnValue getVisitCount()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_VISIT_COUNT);
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
        
        public final void setPageId(com.netspective.commons.value.Value value)
        {
            getPageId().copyValueByReference(value);
        }
        
        public final void setPin(com.netspective.commons.value.Value value)
        {
            getPin().copyValueByReference(value);
        }
        
        public final void setSystemId(com.netspective.commons.value.Value value)
        {
            getSystemId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.VisitedPage valueObject)
        {
            values.getByColumnIndex(COLINDEX_SYSTEM_ID).setValue(valueObject.getSystemId());
            values.getByColumnIndex(COLINDEX_PIN).setValue(valueObject.getPin());
            values.getByColumnIndex(COLINDEX_PAGE_ID).setValue(valueObject.getPageId());
            values.getByColumnIndex(COLINDEX_VISIT_COUNT).setValue(valueObject.getVisitCount());
        }
        
        public final void setVisitCount(com.netspective.commons.value.Value value)
        {
            getVisitCount().copyValueByReference(value);
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
        
        public final VisitedPageTable.Record get(int i)
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