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
import com.netspective.axiom.schema.column.type.TextColumn;
import auto.dal.db.dao.category.ProductTable;

public final class CategoryTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_CATEGORY_ID_EQUALITY = 1;
    public static final int ACCESSORID_BY_NAME_EQUALITY = 2;
    public static final int ACCESSORID_BY_DESCR_EQUALITY = 3;
    public static final int COLINDEX_CATEGORY_ID = 0;
    public static final int COLINDEX_NAME = 1;
    public static final int COLINDEX_DESCR = 2;
    
    public CategoryTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        productTable = new ProductTable(schema.getTables().getByName("Product"));
    }
    
    public final CategoryTable.Record createRecord()
    {
        return new CategoryTable.Record(table.createRow());
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
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final CategoryTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
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
    
    public final TextColumn getDescrColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_DESCR);
    }
    
    public final TextColumn getNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME);
    }
    
    public final ProductTable getProductTable()
    {
        return productTable;
    }
    
    public final CategoryTable.Record getRecord(Row row)
    {
        return new CategoryTable.Record(row);
    }
    
    public final CategoryTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String categoryId, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { categoryId }, null);
        Record result = row != null ? new CategoryTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc, true);
        return result;
    }
    
    public final CategoryTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new CategoryTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc, true);
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
    private ProductTable productTable;
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
        
        public final ProductTable.Record createProductTableRecord()
        {
            return productTable.createChildLinkedByCategoryId(this);
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
            productTableRecords.delete(cc);
        }
        
        public final TextColumn.TextColumnValue getCategoryId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CATEGORY_ID);
        }
        
        public final TextColumn.TextColumnValue getDescr()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_DESCR);
        }
        
        public final TextColumn.TextColumnValue getName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME);
        }
        
        public final ProductTable.Records getProductTableRecords(ConnectionContext cc, boolean retrieveChildren)
        throws NamingException, SQLException
        {
            if (productTableRecords != null) return productTableRecords;
            productTableRecords = productTable.getParentRecordsByCategoryId(this, cc, retrieveChildren);
            return productTableRecords;
        }
        
        public final ProductTable.Records getProductTableRecords()
        {
            return productTableRecords;
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final auto.dal.db.vo.Category getValues()
        {
            return getValues(new auto.dal.db.vo.impl.CategoryVO());
        }
        
        public final auto.dal.db.vo.Category getValues(auto.dal.db.vo.Category valueObject)
        {
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
        
        public final void retrieveChildren(ConnectionContext cc, boolean retrieveGrandchildren)
        throws NamingException, SQLException
        {
            productTableRecords = getProductTableRecords(cc, retrieveGrandchildren);
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
        
        public final void setValues(auto.dal.db.vo.Category valueObject)
        {
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
        private ProductTable.Records productTableRecords;
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
        
        public final CategoryTable.Record get(int i)
        {
            if(cache[i] == null) cache[i] = new Record(rows.getRow(i));
            return cache[i];
        }
        
        public final void retrieveChildren(ConnectionContext cc, boolean retrieveGrandchildren)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++) get(i).retrieveChildren(cc, retrieveGrandchildren);
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