package auto.dal.db.dao.account.orders;

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
import com.netspective.axiom.schema.column.type.DateColumn;
import com.netspective.axiom.schema.column.type.TextColumn;
import auto.dal.db.dao.account.OrdersTable;

public final class OrderStatusTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_ORDERSTATUS_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_ORDERS_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_ITEMNUM_EQUALITY = 4;
    public static final int ACCESSORID_BY_TS_EQUALITY = 5;
    public static final int ACCESSORID_BY_STATUS_EQUALITY = 6;
    public static final int COLINDEX_ORDERSTATUS_ID = 0;
    public static final int COLINDEX_ORDERS_ID = 1;
    public static final int COLINDEX_ITEMNUM = 2;
    public static final int COLINDEX_TS = 3;
    public static final int COLINDEX_STATUS = 4;
    
    public OrderStatusTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        ordersIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_ORDERS_ID).getForeignKey();
    }
    
    public final OrderStatusTable.Record createChildLinkedByOrdersId(OrdersTable.Record parentRecord)
    {
        return new OrderStatusTable.Record(table.createRow(ordersIdForeignKey, parentRecord.getRow()));
    }
    
    public final OrderStatusTable.Record createRecord()
    {
        return new OrderStatusTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByItemnumEquality()
    {
        return accessors.get(ACCESSORID_BY_ITEMNUM_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrdersIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ORDERS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrderstatusIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ORDERSTATUS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStatusEquality()
    {
        return accessors.get(ACCESSORID_BY_STATUS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByTsEquality()
    {
        return accessors.get(ACCESSORID_BY_TS_EQUALITY);
    }
    
    public final OrderStatusTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final IntegerColumn getItemnumColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_ITEMNUM);
    }
    
    public final LongIntegerColumn getOrdersIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_ORDERS_ID);
    }
    
    public final ParentForeignKey getOrdersIdForeignKey()
    {
        return ordersIdForeignKey;
    }
    
    public final AutoIncColumn getOrderstatusIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_ORDERSTATUS_ID);
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: OrderStatus.orders_id;
     *  Referenced Columns: Orders.orders_id
     */
    public final OrderStatusTable.Records getParentRecordsByOrdersId(OrdersTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, ordersIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final OrderStatusTable.Record getRecord(Row row)
    {
        return new OrderStatusTable.Record(row);
    }
    
    public final OrderStatusTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long orderstatusId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { orderstatusId }, null);
        Record result = row != null ? new OrderStatusTable.Record(row) : null;
        return result;
    }
    
    public final OrderStatusTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new OrderStatusTable.Record(row) : null;
        return result;
    }
    
    public final TextColumn getStatusColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_STATUS);
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final DateColumn getTsColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_TS);
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private ParentForeignKey ordersIdForeignKey;
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
        
        public final IntegerColumn.IntegerColumnValue getItemnum()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_ITEMNUM);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getOrdersId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ORDERS_ID);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getOrderstatusId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ORDERSTATUS_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getStatus()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_STATUS);
        }
        
        public final DateColumn.DateColumnValue getTs()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_TS);
        }
        
        public final auto.dal.db.vo.OrderStatus getValues()
        {
            return getValues(new auto.dal.db.vo.impl.OrderStatusVO());
        }
        
        public final auto.dal.db.vo.OrderStatus getValues(auto.dal.db.vo.OrderStatus valueObject)
        {
            Object autoIncOrderstatusIdValue = values.getByColumnIndex(COLINDEX_ORDERSTATUS_ID).getValue();
            valueObject.setOrderstatusId(autoIncOrderstatusIdValue instanceof Integer ? new Long(((Integer) autoIncOrderstatusIdValue).intValue()) : (Long) autoIncOrderstatusIdValue);
            valueObject.setOrdersId((java.lang.Long) values.getByColumnIndex(COLINDEX_ORDERS_ID).getValue());
            valueObject.setItemnum((java.lang.Integer) values.getByColumnIndex(COLINDEX_ITEMNUM).getValue());
            valueObject.setTs((java.util.Date) values.getByColumnIndex(COLINDEX_TS).getValue());
            valueObject.setStatus((java.lang.String) values.getByColumnIndex(COLINDEX_STATUS).getValue());
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
        
        public final void setItemnum(com.netspective.commons.value.Value value)
        {
            getItemnum().copyValueByReference(value);
        }
        
        public final void setOrdersId(com.netspective.commons.value.Value value)
        {
            getOrdersId().copyValueByReference(value);
        }
        
        public final void setOrderstatusId(com.netspective.commons.value.Value value)
        {
            getOrderstatusId().copyValueByReference(value);
        }
        
        public final void setStatus(com.netspective.commons.value.Value value)
        {
            getStatus().copyValueByReference(value);
        }
        
        public final void setTs(com.netspective.commons.value.Value value)
        {
            getTs().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.OrderStatus valueObject)
        {
            values.getByColumnIndex(COLINDEX_ORDERSTATUS_ID).setValue(valueObject.getOrderstatusId());
            values.getByColumnIndex(COLINDEX_ORDERS_ID).setValue(valueObject.getOrdersId());
            values.getByColumnIndex(COLINDEX_ITEMNUM).setValue(valueObject.getItemnum());
            values.getByColumnIndex(COLINDEX_TS).setValue(valueObject.getTs());
            values.getByColumnIndex(COLINDEX_STATUS).setValue(valueObject.getStatus());
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
        
        public Records(OrdersTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final OrderStatusTable.Record get(int i)
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
        private OrdersTable.Record parentRecord;
        private Rows rows;
    }
}