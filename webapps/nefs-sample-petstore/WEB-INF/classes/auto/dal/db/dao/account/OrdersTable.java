package auto.dal.db.dao.account;

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
import com.netspective.axiom.schema.column.type.DateColumn;
import auto.dal.db.dao.AccountTable;
import auto.dal.db.dao.account.orders.LineItemTable;
import auto.dal.db.dao.account.orders.OrderStatusTable;

public final class OrdersTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_ORDERS_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_ACCOUNT_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_ORDER_DATE_EQUALITY = 4;
    public static final int ACCESSORID_BY_SHIP_ADDR1_EQUALITY = 5;
    public static final int ACCESSORID_BY_SHIP_ADDR2_EQUALITY = 6;
    public static final int ACCESSORID_BY_SHIP_CITY_EQUALITY = 7;
    public static final int ACCESSORID_BY_SHIP_STATE_EQUALITY = 8;
    public static final int ACCESSORID_BY_SHIP_COUNTRY_EQUALITY = 9;
    public static final int ACCESSORID_BY_SHIP_PHONE_EQUALITY = 10;
    public static final int ACCESSORID_BY_BILL_ADDR1_EQUALITY = 11;
    public static final int ACCESSORID_BY_BILL_ADDR2_EQUALITY = 12;
    public static final int ACCESSORID_BY_BILL_CITY_EQUALITY = 13;
    public static final int ACCESSORID_BY_BILL_STATE_EQUALITY = 14;
    public static final int ACCESSORID_BY_BILL_COUNTRY_EQUALITY = 15;
    public static final int ACCESSORID_BY_BILL_PHONE_EQUALITY = 16;
    public static final int COLINDEX_ORDERS_ID = 0;
    public static final int COLINDEX_ACCOUNT_ID = 1;
    public static final int COLINDEX_ORDER_DATE = 2;
    public static final int COLINDEX_SHIP_ADDR1 = 3;
    public static final int COLINDEX_SHIP_ADDR2 = 4;
    public static final int COLINDEX_SHIP_CITY = 5;
    public static final int COLINDEX_SHIP_STATE = 6;
    public static final int COLINDEX_SHIP_COUNTRY = 7;
    public static final int COLINDEX_SHIP_PHONE = 8;
    public static final int COLINDEX_BILL_ADDR1 = 9;
    public static final int COLINDEX_BILL_ADDR2 = 10;
    public static final int COLINDEX_BILL_CITY = 11;
    public static final int COLINDEX_BILL_STATE = 12;
    public static final int COLINDEX_BILL_COUNTRY = 13;
    public static final int COLINDEX_BILL_PHONE = 14;
    
    public OrdersTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        accountIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_ACCOUNT_ID).getForeignKey();
        lineItemTable = new LineItemTable(schema.getTables().getByName("LineItem"));
        orderStatusTable = new OrderStatusTable(schema.getTables().getByName("OrderStatus"));
    }
    
    public final OrdersTable.Record createChildLinkedByAccountId(AccountTable.Record parentRecord)
    {
        return new OrdersTable.Record(table.createRow(accountIdForeignKey, parentRecord.getRow()));
    }
    
    public final OrdersTable.Record createRecord()
    {
        return new OrdersTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByAccountIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ACCOUNT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBillAddr1Equality()
    {
        return accessors.get(ACCESSORID_BY_BILL_ADDR1_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBillAddr2Equality()
    {
        return accessors.get(ACCESSORID_BY_BILL_ADDR2_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBillCityEquality()
    {
        return accessors.get(ACCESSORID_BY_BILL_CITY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBillCountryEquality()
    {
        return accessors.get(ACCESSORID_BY_BILL_COUNTRY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBillPhoneEquality()
    {
        return accessors.get(ACCESSORID_BY_BILL_PHONE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBillStateEquality()
    {
        return accessors.get(ACCESSORID_BY_BILL_STATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrderDateEquality()
    {
        return accessors.get(ACCESSORID_BY_ORDER_DATE_EQUALITY);
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
    
    public final QueryDefnSelect getAccessorByShipAddr1Equality()
    {
        return accessors.get(ACCESSORID_BY_SHIP_ADDR1_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByShipAddr2Equality()
    {
        return accessors.get(ACCESSORID_BY_SHIP_ADDR2_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByShipCityEquality()
    {
        return accessors.get(ACCESSORID_BY_SHIP_CITY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByShipCountryEquality()
    {
        return accessors.get(ACCESSORID_BY_SHIP_COUNTRY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByShipPhoneEquality()
    {
        return accessors.get(ACCESSORID_BY_SHIP_PHONE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByShipStateEquality()
    {
        return accessors.get(ACCESSORID_BY_SHIP_STATE_EQUALITY);
    }
    
    public final OrdersTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getAccountIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ACCOUNT_ID);
    }
    
    public final ParentForeignKey getAccountIdForeignKey()
    {
        return accountIdForeignKey;
    }
    
    public final TextColumn getBillAddr1Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_BILL_ADDR1);
    }
    
    public final TextColumn getBillAddr2Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_BILL_ADDR2);
    }
    
    public final TextColumn getBillCityColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_BILL_CITY);
    }
    
    public final TextColumn getBillCountryColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_BILL_COUNTRY);
    }
    
    public final TextColumn getBillPhoneColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_BILL_PHONE);
    }
    
    public final TextColumn getBillStateColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_BILL_STATE);
    }
    
    public final LineItemTable getLineItemTable()
    {
        return lineItemTable;
    }
    
    public final DateColumn getOrderDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_ORDER_DATE);
    }
    
    public final OrderStatusTable getOrderStatusTable()
    {
        return orderStatusTable;
    }
    
    public final AutoIncColumn getOrdersIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_ORDERS_ID);
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: Orders.account_id; Referenced
     *  Columns: Account.account_id
     */
    public final OrdersTable.Records getParentRecordsByAccountId(AccountTable.Record parentRecord, ConnectionContext cc, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, accountIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        if(retrieveChildren) result.retrieveChildren(cc);
        return result;
    }
    
    public final OrdersTable.Record getRecord(Row row)
    {
        return new OrdersTable.Record(row);
    }
    
    public final OrdersTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long ordersId, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { ordersId }, null);
        Record result = row != null ? new OrdersTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final OrdersTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new OrdersTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final TextColumn getShipAddr1Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SHIP_ADDR1);
    }
    
    public final TextColumn getShipAddr2Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SHIP_ADDR2);
    }
    
    public final TextColumn getShipCityColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SHIP_CITY);
    }
    
    public final TextColumn getShipCountryColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SHIP_COUNTRY);
    }
    
    public final TextColumn getShipPhoneColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SHIP_PHONE);
    }
    
    public final TextColumn getShipStateColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SHIP_STATE);
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
    private ParentForeignKey accountIdForeignKey;
    private LineItemTable lineItemTable;
    private OrderStatusTable orderStatusTable;
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
        
        public final LineItemTable.Record createLineItemTableRecord()
        {
            return lineItemTable.createChildLinkedByOrdersId(this);
        }
        
        public final OrderStatusTable.Record createOrderStatusTableRecord()
        {
            return orderStatusTable.createChildLinkedByOrdersId(this);
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
        
        public final void deleteChildren(ConnectionContext cc)
        throws NamingException, SQLException
        {
            lineItemTableRecords.delete(cc);
            orderStatusTableRecords.delete(cc);
        }
        
        public final TextColumn.TextColumnValue getAccountId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ACCOUNT_ID);
        }
        
        public final TextColumn.TextColumnValue getBillAddr1()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_BILL_ADDR1);
        }
        
        public final TextColumn.TextColumnValue getBillAddr2()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_BILL_ADDR2);
        }
        
        public final TextColumn.TextColumnValue getBillCity()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_BILL_CITY);
        }
        
        public final TextColumn.TextColumnValue getBillCountry()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_BILL_COUNTRY);
        }
        
        public final TextColumn.TextColumnValue getBillPhone()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_BILL_PHONE);
        }
        
        public final TextColumn.TextColumnValue getBillState()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_BILL_STATE);
        }
        
        public final LineItemTable.Records getLineItemTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (lineItemTableRecords != null) return lineItemTableRecords;
            lineItemTableRecords = lineItemTable.getParentRecordsByOrdersId(this, cc);
            return lineItemTableRecords;
        }
        
        public final LineItemTable.Records getLineItemTableRecords()
        {
            return lineItemTableRecords;
        }
        
        public final DateColumn.DateColumnValue getOrderDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_ORDER_DATE);
        }
        
        public final OrderStatusTable.Records getOrderStatusTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orderStatusTableRecords != null) return orderStatusTableRecords;
            orderStatusTableRecords = orderStatusTable.getParentRecordsByOrdersId(this, cc);
            return orderStatusTableRecords;
        }
        
        public final OrderStatusTable.Records getOrderStatusTableRecords()
        {
            return orderStatusTableRecords;
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getOrdersId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ORDERS_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getShipAddr1()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SHIP_ADDR1);
        }
        
        public final TextColumn.TextColumnValue getShipAddr2()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SHIP_ADDR2);
        }
        
        public final TextColumn.TextColumnValue getShipCity()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SHIP_CITY);
        }
        
        public final TextColumn.TextColumnValue getShipCountry()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SHIP_COUNTRY);
        }
        
        public final TextColumn.TextColumnValue getShipPhone()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SHIP_PHONE);
        }
        
        public final TextColumn.TextColumnValue getShipState()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SHIP_STATE);
        }
        
        public final auto.dal.db.vo.Orders getValues()
        {
            return getValues(new auto.dal.db.vo.impl.OrdersVO());
        }
        
        public final auto.dal.db.vo.Orders getValues(auto.dal.db.vo.Orders valueObject)
        {
            Object autoIncOrdersIdValue = values.getByColumnIndex(COLINDEX_ORDERS_ID).getValue();
            valueObject.setOrdersId(autoIncOrdersIdValue instanceof Integer ? new Long(((Integer) autoIncOrdersIdValue).intValue()) : (Long) autoIncOrdersIdValue);
            valueObject.setAccountId((java.lang.String) values.getByColumnIndex(COLINDEX_ACCOUNT_ID).getValue());
            valueObject.setOrderDate((java.util.Date) values.getByColumnIndex(COLINDEX_ORDER_DATE).getValue());
            valueObject.setShipAddr1((java.lang.String) values.getByColumnIndex(COLINDEX_SHIP_ADDR1).getValue());
            valueObject.setShipAddr2((java.lang.String) values.getByColumnIndex(COLINDEX_SHIP_ADDR2).getValue());
            valueObject.setShipCity((java.lang.String) values.getByColumnIndex(COLINDEX_SHIP_CITY).getValue());
            valueObject.setShipState((java.lang.String) values.getByColumnIndex(COLINDEX_SHIP_STATE).getValue());
            valueObject.setShipCountry((java.lang.String) values.getByColumnIndex(COLINDEX_SHIP_COUNTRY).getValue());
            valueObject.setShipPhone((java.lang.String) values.getByColumnIndex(COLINDEX_SHIP_PHONE).getValue());
            valueObject.setBillAddr1((java.lang.String) values.getByColumnIndex(COLINDEX_BILL_ADDR1).getValue());
            valueObject.setBillAddr2((java.lang.String) values.getByColumnIndex(COLINDEX_BILL_ADDR2).getValue());
            valueObject.setBillCity((java.lang.String) values.getByColumnIndex(COLINDEX_BILL_CITY).getValue());
            valueObject.setBillState((java.lang.String) values.getByColumnIndex(COLINDEX_BILL_STATE).getValue());
            valueObject.setBillCountry((java.lang.String) values.getByColumnIndex(COLINDEX_BILL_COUNTRY).getValue());
            valueObject.setBillPhone((java.lang.String) values.getByColumnIndex(COLINDEX_BILL_PHONE).getValue());
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
        
        public final void retrieveChildren(ConnectionContext cc)
        throws NamingException, SQLException
        {
            lineItemTableRecords = getLineItemTableRecords(cc);
            orderStatusTableRecords = getOrderStatusTableRecords(cc);
        }
        
        public final void setAccountId(com.netspective.commons.value.Value value)
        {
            getAccountId().copyValueByReference(value);
        }
        
        public final void setBillAddr1(com.netspective.commons.value.Value value)
        {
            getBillAddr1().copyValueByReference(value);
        }
        
        public final void setBillAddr2(com.netspective.commons.value.Value value)
        {
            getBillAddr2().copyValueByReference(value);
        }
        
        public final void setBillCity(com.netspective.commons.value.Value value)
        {
            getBillCity().copyValueByReference(value);
        }
        
        public final void setBillCountry(com.netspective.commons.value.Value value)
        {
            getBillCountry().copyValueByReference(value);
        }
        
        public final void setBillPhone(com.netspective.commons.value.Value value)
        {
            getBillPhone().copyValueByReference(value);
        }
        
        public final void setBillState(com.netspective.commons.value.Value value)
        {
            getBillState().copyValueByReference(value);
        }
        
        public final void setOrderDate(com.netspective.commons.value.Value value)
        {
            getOrderDate().copyValueByReference(value);
        }
        
        public final void setOrdersId(com.netspective.commons.value.Value value)
        {
            getOrdersId().copyValueByReference(value);
        }
        
        public final void setShipAddr1(com.netspective.commons.value.Value value)
        {
            getShipAddr1().copyValueByReference(value);
        }
        
        public final void setShipAddr2(com.netspective.commons.value.Value value)
        {
            getShipAddr2().copyValueByReference(value);
        }
        
        public final void setShipCity(com.netspective.commons.value.Value value)
        {
            getShipCity().copyValueByReference(value);
        }
        
        public final void setShipCountry(com.netspective.commons.value.Value value)
        {
            getShipCountry().copyValueByReference(value);
        }
        
        public final void setShipPhone(com.netspective.commons.value.Value value)
        {
            getShipPhone().copyValueByReference(value);
        }
        
        public final void setShipState(com.netspective.commons.value.Value value)
        {
            getShipState().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Orders valueObject)
        {
            values.getByColumnIndex(COLINDEX_ORDERS_ID).setValue(valueObject.getOrdersId());
            values.getByColumnIndex(COLINDEX_ACCOUNT_ID).setValue(valueObject.getAccountId());
            values.getByColumnIndex(COLINDEX_ORDER_DATE).setValue(valueObject.getOrderDate());
            values.getByColumnIndex(COLINDEX_SHIP_ADDR1).setValue(valueObject.getShipAddr1());
            values.getByColumnIndex(COLINDEX_SHIP_ADDR2).setValue(valueObject.getShipAddr2());
            values.getByColumnIndex(COLINDEX_SHIP_CITY).setValue(valueObject.getShipCity());
            values.getByColumnIndex(COLINDEX_SHIP_STATE).setValue(valueObject.getShipState());
            values.getByColumnIndex(COLINDEX_SHIP_COUNTRY).setValue(valueObject.getShipCountry());
            values.getByColumnIndex(COLINDEX_SHIP_PHONE).setValue(valueObject.getShipPhone());
            values.getByColumnIndex(COLINDEX_BILL_ADDR1).setValue(valueObject.getBillAddr1());
            values.getByColumnIndex(COLINDEX_BILL_ADDR2).setValue(valueObject.getBillAddr2());
            values.getByColumnIndex(COLINDEX_BILL_CITY).setValue(valueObject.getBillCity());
            values.getByColumnIndex(COLINDEX_BILL_STATE).setValue(valueObject.getBillState());
            values.getByColumnIndex(COLINDEX_BILL_COUNTRY).setValue(valueObject.getBillCountry());
            values.getByColumnIndex(COLINDEX_BILL_PHONE).setValue(valueObject.getBillPhone());
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
        private LineItemTable.Records lineItemTableRecords;
        private OrderStatusTable.Records orderStatusTableRecords;
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
        
        public Records(AccountTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final OrdersTable.Record get(int i)
        {
            if(cache[i] == null) cache[i] = new Record(rows.getRow(i));
            return cache[i];
        }
        
        public final void retrieveChildren(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++) get(i).retrieveChildren(cc);
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
        private AccountTable.Record parentRecord;
        private Rows rows;
    }
}