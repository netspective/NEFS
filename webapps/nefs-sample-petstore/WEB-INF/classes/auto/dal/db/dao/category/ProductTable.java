package auto.dal.db.dao.category;

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
import auto.dal.db.dao.CategoryTable;
import auto.dal.db.dao.category.product.ItemTable;

public final class ProductTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_PRODUCT_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_CATEGORY_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_NAME_EQUALITY = 4;
    public static final int ACCESSORID_BY_DESCR_EQUALITY = 5;
    public static final int COLINDEX_PRODUCT_ID = 0;
    public static final int COLINDEX_CATEGORY_ID = 1;
    public static final int COLINDEX_NAME = 2;
    public static final int COLINDEX_DESCR = 3;
    
    public ProductTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        categoryIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_CATEGORY_ID).getForeignKey();
        itemTable = new ItemTable(schema.getTables().getByName("Item"));
    }
    
    public final ProductTable.Record createChildLinkedByCategoryId(CategoryTable.Record parentRecord)
    {
        return new ProductTable.Record(table.createRow(categoryIdForeignKey, parentRecord.getRow()));
    }
    
    public final ProductTable.Record createRecord()
    {
        return new ProductTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByCategoryIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CATEGORY_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByDescrEquality()
    {
        return accessors.get(ACCESSORID_BY_DESCR_EQUALITY);
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
    
    public final ProductTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getCategoryIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_CATEGORY_ID);
    }
    
    public final ParentForeignKey getCategoryIdForeignKey()
    {
        return categoryIdForeignKey;
    }
    
    public final TextColumn getDescrColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_DESCR);
    }
    
    public final ItemTable getItemTable()
    {
        return itemTable;
    }
    
    public final TextColumn getNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME);
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: Product.category_id; Referenced
     *  Columns: Category.category_id
     */
    public final ProductTable.Records getParentRecordsByCategoryId(CategoryTable.Record parentRecord, ConnectionContext cc, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, categoryIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        if(retrieveChildren) result.retrieveChildren(cc);
        return result;
    }
    
    public final TextColumn getProductIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_PRODUCT_ID);
    }
    
    public final ProductTable.Record getRecord(Row row)
    {
        return new ProductTable.Record(row);
    }
    
    public final ProductTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String productId, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { productId }, null);
        Record result = row != null ? new ProductTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final ProductTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new ProductTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
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
    private ParentForeignKey categoryIdForeignKey;
    private ItemTable itemTable;
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
        
        public final ItemTable.Record createItemTableRecord()
        {
            return itemTable.createChildLinkedByProductId(this);
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
            itemTableRecords.delete(cc);
        }
        
        public final TextColumn.TextColumnValue getCategoryId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CATEGORY_ID);
        }
        
        public final TextColumn.TextColumnValue getDescr()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_DESCR);
        }
        
        public final ItemTable.Records getItemTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (itemTableRecords != null) return itemTableRecords;
            itemTableRecords = itemTable.getParentRecordsByProductId(this, cc);
            return itemTableRecords;
        }
        
        public final ItemTable.Records getItemTableRecords()
        {
            return itemTableRecords;
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
        
        public final auto.dal.db.vo.Product getValues()
        {
            return getValues(new auto.dal.db.vo.impl.ProductVO());
        }
        
        public final auto.dal.db.vo.Product getValues(auto.dal.db.vo.Product valueObject)
        {
            valueObject.setProductId((java.lang.String) values.getByColumnIndex(COLINDEX_PRODUCT_ID).getValue());
            valueObject.setCategoryId((java.lang.String) values.getByColumnIndex(COLINDEX_CATEGORY_ID).getValue());
            valueObject.setName((java.lang.String) values.getByColumnIndex(COLINDEX_NAME).getValue());
            valueObject.setDescr((java.lang.String) values.getByColumnIndex(COLINDEX_DESCR).getValue());
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
            itemTableRecords = getItemTableRecords(cc);
        }
        
        public final void setCategoryId(com.netspective.commons.value.Value value)
        {
            getCategoryId().copyValueByReference(value);
        }
        
        public final void setDescr(com.netspective.commons.value.Value value)
        {
            getDescr().copyValueByReference(value);
        }
        
        public final void setName(com.netspective.commons.value.Value value)
        {
            getName().copyValueByReference(value);
        }
        
        public final void setProductId(com.netspective.commons.value.Value value)
        {
            getProductId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Product valueObject)
        {
            values.getByColumnIndex(COLINDEX_PRODUCT_ID).setValue(valueObject.getProductId());
            values.getByColumnIndex(COLINDEX_CATEGORY_ID).setValue(valueObject.getCategoryId());
            values.getByColumnIndex(COLINDEX_NAME).setValue(valueObject.getName());
            values.getByColumnIndex(COLINDEX_DESCR).setValue(valueObject.getDescr());
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
        private ItemTable.Records itemTableRecords;
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
        
        public Records(CategoryTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final ProductTable.Record get(int i)
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
        private CategoryTable.Record parentRecord;
        private Rows rows;
    }
}