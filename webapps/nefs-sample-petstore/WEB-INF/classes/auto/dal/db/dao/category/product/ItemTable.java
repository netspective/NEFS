package auto.dal.db.dao.category.product;

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
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.axiom.schema.column.type.CurrencyColumn;
import com.netspective.axiom.schema.column.type.FloatColumn;
import com.netspective.axiom.schema.column.type.LongIntegerColumn;
import auto.dal.db.dao.category.ProductTable;

public final class ItemTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_ITEM_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_PRODUCT_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_LIST_PRICE_EQUALITY = 4;
    public static final int ACCESSORID_BY_UNIT_COST_EQUALITY = 5;
    public static final int ACCESSORID_BY_SUPPLIER_ID_EQUALITY = 6;
    public static final int ACCESSORID_BY_STATUS_EQUALITY = 7;
    public static final int ACCESSORID_BY_NAME_EQUALITY = 8;
    public static final int ACCESSORID_BY_DESCR_EQUALITY = 9;
    public static final int ACCESSORID_BY_IMAGE_EQUALITY = 10;
    public static final int ACCESSORID_BY_ATTR1_EQUALITY = 11;
    public static final int ACCESSORID_BY_ATTR2_EQUALITY = 12;
    public static final int ACCESSORID_BY_ATTR3_EQUALITY = 13;
    public static final int COLINDEX_ITEM_ID = 0;
    public static final int COLINDEX_PRODUCT_ID = 1;
    public static final int COLINDEX_LIST_PRICE = 2;
    public static final int COLINDEX_UNIT_COST = 3;
    public static final int COLINDEX_SUPPLIER_ID = 4;
    public static final int COLINDEX_STATUS = 5;
    public static final int COLINDEX_NAME = 6;
    public static final int COLINDEX_DESCR = 7;
    public static final int COLINDEX_IMAGE = 8;
    public static final int COLINDEX_ATTR1 = 9;
    public static final int COLINDEX_ATTR2 = 10;
    public static final int COLINDEX_ATTR3 = 11;
    
    public ItemTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        productIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_PRODUCT_ID).getForeignKey();
        supplierIdForeignKey = table.getColumns().get(COLINDEX_SUPPLIER_ID).getForeignKey();
    }
    
    public final ItemTable.Record createChildLinkedByProductId(ProductTable.Record parentRecord)
    {
        return new ItemTable.Record(table.createRow(productIdForeignKey, parentRecord.getRow()));
    }
    
    public final ItemTable.Record createRecord()
    {
        return new ItemTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByAttr1Equality()
    {
        return accessors.get(ACCESSORID_BY_ATTR1_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByAttr2Equality()
    {
        return accessors.get(ACCESSORID_BY_ATTR2_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByAttr3Equality()
    {
        return accessors.get(ACCESSORID_BY_ATTR3_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByDescrEquality()
    {
        return accessors.get(ACCESSORID_BY_DESCR_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByImageEquality()
    {
        return accessors.get(ACCESSORID_BY_IMAGE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByItemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ITEM_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByListPriceEquality()
    {
        return accessors.get(ACCESSORID_BY_LIST_PRICE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNameEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByProductIdEquality()
    {
        return accessors.get(ACCESSORID_BY_PRODUCT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStatusEquality()
    {
        return accessors.get(ACCESSORID_BY_STATUS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySupplierIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SUPPLIER_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByUnitCostEquality()
    {
        return accessors.get(ACCESSORID_BY_UNIT_COST_EQUALITY);
    }
    
    public final ItemTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getAttr1Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ATTR1);
    }
    
    public final TextColumn getAttr2Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ATTR2);
    }
    
    public final TextColumn getAttr3Column()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ATTR3);
    }
    
    public final TextColumn getDescrColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_DESCR);
    }
    
    public final TextColumn getImageColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_IMAGE);
    }
    
    public final TextColumn getItemIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ITEM_ID);
    }
    
    public final CurrencyColumn getListPriceColumn()
    {
        return (CurrencyColumn)table.getColumns().get(COLINDEX_LIST_PRICE);
    }
    
    public final TextColumn getNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME);
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: Item.product_id; Referenced
     *  Columns: Product.product_id
     */
    public final ItemTable.Records getParentRecordsByProductId(ProductTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, productIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final TextColumn getProductIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_PRODUCT_ID);
    }
    
    public final ParentForeignKey getProductIdForeignKey()
    {
        return productIdForeignKey;
    }
    
    public final ItemTable.Record getRecord(Row row)
    {
        return new ItemTable.Record(row);
    }
    
    public final ItemTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String itemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { itemId }, null);
        Record result = row != null ? new ItemTable.Record(row) : null;
        return result;
    }
    
    public final ItemTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new ItemTable.Record(row) : null;
        return result;
    }
    
    public final TextColumn getStatusColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_STATUS);
    }
    
    public final LongIntegerColumn getSupplierIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_SUPPLIER_ID);
    }
    
    public final ForeignKey getSupplierIdForeignKey()
    {
        return supplierIdForeignKey;
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final CurrencyColumn getUnitCostColumn()
    {
        return (CurrencyColumn)table.getColumns().get(COLINDEX_UNIT_COST);
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private ParentForeignKey productIdForeignKey;
    private Schema schema;
    private ForeignKey supplierIdForeignKey;
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
        
        public final TextColumn.TextColumnValue getAttr1()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ATTR1);
        }
        
        public final TextColumn.TextColumnValue getAttr2()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ATTR2);
        }
        
        public final TextColumn.TextColumnValue getAttr3()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ATTR3);
        }
        
        public final TextColumn.TextColumnValue getDescr()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_DESCR);
        }
        
        public final TextColumn.TextColumnValue getImage()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_IMAGE);
        }
        
        public final TextColumn.TextColumnValue getItemId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ITEM_ID);
        }
        
        public final FloatColumn.FloatColumnValue getListPrice()
        {
            return (FloatColumn.FloatColumnValue)values.getByColumnIndex(COLINDEX_LIST_PRICE);
        }
        
        public final TextColumn.TextColumnValue getName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME);
        }
        
        public final TextColumn.TextColumnValue getProductId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_PRODUCT_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getStatus()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_STATUS);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getSupplierId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_SUPPLIER_ID);
        }
        
        public final FloatColumn.FloatColumnValue getUnitCost()
        {
            return (FloatColumn.FloatColumnValue)values.getByColumnIndex(COLINDEX_UNIT_COST);
        }
        
        public final auto.dal.db.vo.Item getValues()
        {
            return getValues(new auto.dal.db.vo.impl.ItemVO());
        }
        
        public final auto.dal.db.vo.Item getValues(auto.dal.db.vo.Item valueObject)
        {
            valueObject.setItemId((java.lang.String) values.getByColumnIndex(COLINDEX_ITEM_ID).getValue());
            valueObject.setProductId((java.lang.String) values.getByColumnIndex(COLINDEX_PRODUCT_ID).getValue());
            valueObject.setListPrice((java.lang.Float) values.getByColumnIndex(COLINDEX_LIST_PRICE).getValue());
            valueObject.setUnitCost((java.lang.Float) values.getByColumnIndex(COLINDEX_UNIT_COST).getValue());
            valueObject.setSupplierId((java.lang.Long) values.getByColumnIndex(COLINDEX_SUPPLIER_ID).getValue());
            valueObject.setStatus((java.lang.String) values.getByColumnIndex(COLINDEX_STATUS).getValue());
            valueObject.setName((java.lang.String) values.getByColumnIndex(COLINDEX_NAME).getValue());
            valueObject.setDescr((java.lang.String) values.getByColumnIndex(COLINDEX_DESCR).getValue());
            valueObject.setImage((java.lang.String) values.getByColumnIndex(COLINDEX_IMAGE).getValue());
            valueObject.setAttr1((java.lang.String) values.getByColumnIndex(COLINDEX_ATTR1).getValue());
            valueObject.setAttr2((java.lang.String) values.getByColumnIndex(COLINDEX_ATTR2).getValue());
            valueObject.setAttr3((java.lang.String) values.getByColumnIndex(COLINDEX_ATTR3).getValue());
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
        
        public final void setAttr1(com.netspective.commons.value.Value value)
        {
            getAttr1().copyValueByReference(value);
        }
        
        public final void setAttr2(com.netspective.commons.value.Value value)
        {
            getAttr2().copyValueByReference(value);
        }
        
        public final void setAttr3(com.netspective.commons.value.Value value)
        {
            getAttr3().copyValueByReference(value);
        }
        
        public final void setDescr(com.netspective.commons.value.Value value)
        {
            getDescr().copyValueByReference(value);
        }
        
        public final void setImage(com.netspective.commons.value.Value value)
        {
            getImage().copyValueByReference(value);
        }
        
        public final void setItemId(com.netspective.commons.value.Value value)
        {
            getItemId().copyValueByReference(value);
        }
        
        public final void setListPrice(com.netspective.commons.value.Value value)
        {
            getListPrice().copyValueByReference(value);
        }
        
        public final void setName(com.netspective.commons.value.Value value)
        {
            getName().copyValueByReference(value);
        }
        
        public final void setProductId(com.netspective.commons.value.Value value)
        {
            getProductId().copyValueByReference(value);
        }
        
        public final void setStatus(com.netspective.commons.value.Value value)
        {
            getStatus().copyValueByReference(value);
        }
        
        public final void setSupplierId(com.netspective.commons.value.Value value)
        {
            getSupplierId().copyValueByReference(value);
        }
        
        public final void setUnitCost(com.netspective.commons.value.Value value)
        {
            getUnitCost().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Item valueObject)
        {
            values.getByColumnIndex(COLINDEX_ITEM_ID).setValue(valueObject.getItemId());
            values.getByColumnIndex(COLINDEX_PRODUCT_ID).setValue(valueObject.getProductId());
            values.getByColumnIndex(COLINDEX_LIST_PRICE).setValue(valueObject.getListPrice());
            values.getByColumnIndex(COLINDEX_UNIT_COST).setValue(valueObject.getUnitCost());
            values.getByColumnIndex(COLINDEX_SUPPLIER_ID).setValue(valueObject.getSupplierId());
            values.getByColumnIndex(COLINDEX_STATUS).setValue(valueObject.getStatus());
            values.getByColumnIndex(COLINDEX_NAME).setValue(valueObject.getName());
            values.getByColumnIndex(COLINDEX_DESCR).setValue(valueObject.getDescr());
            values.getByColumnIndex(COLINDEX_IMAGE).setValue(valueObject.getImage());
            values.getByColumnIndex(COLINDEX_ATTR1).setValue(valueObject.getAttr1());
            values.getByColumnIndex(COLINDEX_ATTR2).setValue(valueObject.getAttr2());
            values.getByColumnIndex(COLINDEX_ATTR3).setValue(valueObject.getAttr3());
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
        
        public Records(ProductTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final ItemTable.Record get(int i)
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
        private ProductTable.Record parentRecord;
        private Rows rows;
    }
}