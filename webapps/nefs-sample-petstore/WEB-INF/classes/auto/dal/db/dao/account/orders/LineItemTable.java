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
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.axiom.schema.column.type.CurrencyColumn;
import com.netspective.axiom.schema.column.type.FloatColumn;
import auto.dal.db.dao.account.OrdersTable;

public final class LineItemTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_LINEITEM_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_ORDERS_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_ITEMNUM_EQUALITY = 4;
    public static final int ACCESSORID_BY_ITEM_ID_EQUALITY = 5;
    public static final int ACCESSORID_BY_QUANTITY_EQUALITY = 6;
    public static final int ACCESSORID_BY_UNIT_PRICE_EQUALITY = 7;
    public static final int COLINDEX_LINEITEM_ID = 0;
    public static final int COLINDEX_ORDERS_ID = 1;
    public static final int COLINDEX_ITEMNUM = 2;
    public static final int COLINDEX_ITEM_ID = 3;
    public static final int COLINDEX_QUANTITY = 4;
    public static final int COLINDEX_UNIT_PRICE = 5;
    
    public LineItemTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        ordersIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_ORDERS_ID).getForeignKey();
        itemIdForeignKey = table.getColumns().get(COLINDEX_ITEM_ID).getForeignKey();
    }
    
    public final LineItemTable.Record createChildLinkedByOrdersId(OrdersTable.Record parentRecord)
    {
        return new LineItemTable.Record(table.createRow(ordersIdForeignKey, parentRecord.getRow()));
    }
    
    public final LineItemTable.Record createRecord()
    {
        return new LineItemTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByItemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ITEM_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByItemnumEquality()
    {
        return accessors.get(ACCESSORID_BY_ITEMNUM_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLineitemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_LINEITEM_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrdersIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ORDERS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByQuantityEquality()
    {
        return accessors.get(ACCESSORID_BY_QUANTITY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByUnitPriceEquality()
    {
        return accessors.get(ACCESSORID_BY_UNIT_PRICE_EQUALITY);
    }
    
    public final LineItemTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getItemIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ITEM_ID);
    }
    
    public final ForeignKey getItemIdForeignKey()
    {
        return itemIdForeignKey;
    }
    
    public final IntegerColumn getItemnumColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_ITEMNUM);
    }
    
    public final AutoIncColumn getLineitemIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_LINEITEM_ID);
    }
    
    public final LongIntegerColumn getOrdersIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_ORDERS_ID);
    }
    
    public final ParentForeignKey getOrdersIdForeignKey()
    {
        return ordersIdForeignKey;
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: LineItem.orders_id; Referenced
     *  Columns: Orders.orders_id
     */
    public final LineItemTable.Records getParentRecordsByOrdersId(OrdersTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, ordersIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final IntegerColumn getQuantityColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_QUANTITY);
    }
    
    public final LineItemTable.Record getRecord(Row row)
    {
        return new LineItemTable.Record(row);
    }
    
    public final LineItemTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long lineitemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { lineitemId }, null);
        Record result = row != null ? new LineItemTable.Record(row) : null;
        return result;
    }
    
    public final LineItemTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new LineItemTable.Record(row) : null;
        return result;
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final CurrencyColumn getUnitPriceColumn()
    {
        return (CurrencyColumn)table.getColumns().get(COLINDEX_UNIT_PRICE);
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private ForeignKey itemIdForeignKey;
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
        
        public final TextColumn.TextColumnValue getItemId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ITEM_ID);
        }
        
        public final IntegerColumn.IntegerColumnValue getItemnum()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_ITEMNUM);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getLineitemId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_LINEITEM_ID);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getOrdersId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ORDERS_ID);
        }
        
        public final IntegerColumn.IntegerColumnValue getQuantity()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_QUANTITY);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final FloatColumn.FloatColumnValue getUnitPrice()
        {
            return (FloatColumn.FloatColumnValue)values.getByColumnIndex(COLINDEX_UNIT_PRICE);
        }
        
        public final auto.dal.db.vo.LineItem getValues()
        {
            return getValues(new auto.dal.db.vo.impl.LineItemVO());
        }
        
        public final auto.dal.db.vo.LineItem getValues(auto.dal.db.vo.LineItem valueObject)
        {
            Object autoIncLineitemIdValue = values.getByColumnIndex(COLINDEX_LINEITEM_ID).getValue();
            valueObject.setLineitemId(autoIncLineitemIdValue instanceof Integer ? new Long(((Integer) autoIncLineitemIdValue).intValue()) : (Long) autoIncLineitemIdValue);
            valueObject.setOrdersId((java.lang.Long) values.getByColumnIndex(COLINDEX_ORDERS_ID).getValue());
            valueObject.setItemnum((java.lang.Integer) values.getByColumnIndex(COLINDEX_ITEMNUM).getValue());
            valueObject.setItemId((java.lang.String) values.getByColumnIndex(COLINDEX_ITEM_ID).getValue());
            valueObject.setQuantity((java.lang.Integer) values.getByColumnIndex(COLINDEX_QUANTITY).getValue());
            valueObject.setUnitPrice((java.lang.Float) values.getByColumnIndex(COLINDEX_UNIT_PRICE).getValue());
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
        
        public final void setItemId(com.netspective.commons.value.Value value)
        {
            getItemId().copyValueByReference(value);
        }
        
        public final void setItemnum(com.netspective.commons.value.Value value)
        {
            getItemnum().copyValueByReference(value);
        }
        
        public final void setLineitemId(com.netspective.commons.value.Value value)
        {
            getLineitemId().copyValueByReference(value);
        }
        
        public final void setOrdersId(com.netspective.commons.value.Value value)
        {
            getOrdersId().copyValueByReference(value);
        }
        
        public final void setQuantity(com.netspective.commons.value.Value value)
        {
            getQuantity().copyValueByReference(value);
        }
        
        public final void setUnitPrice(com.netspective.commons.value.Value value)
        {
            getUnitPrice().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.LineItem valueObject)
        {
            values.getByColumnIndex(COLINDEX_LINEITEM_ID).setValue(valueObject.getLineitemId());
            values.getByColumnIndex(COLINDEX_ORDERS_ID).setValue(valueObject.getOrdersId());
            values.getByColumnIndex(COLINDEX_ITEMNUM).setValue(valueObject.getItemnum());
            values.getByColumnIndex(COLINDEX_ITEM_ID).setValue(valueObject.getItemId());
            values.getByColumnIndex(COLINDEX_QUANTITY).setValue(valueObject.getQuantity());
            values.getByColumnIndex(COLINDEX_UNIT_PRICE).setValue(valueObject.getUnitPrice());
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
        
        public final LineItemTable.Record get(int i)
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