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
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.axiom.schema.column.type.IntegerColumn;
import auto.dal.db.dao.asset.AssetLoanTable;

public final class AssetTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_ASSET_ID_EQUALITY = 1;
    public static final int ACCESSORID_BY_TYPE_EQUALITY = 2;
    public static final int ACCESSORID_BY_NAME_EQUALITY = 3;
    public static final int ACCESSORID_BY_QUANTITY_EQUALITY = 4;
    public static final int COLINDEX_ASSET_ID = 0;
    public static final int COLINDEX_TYPE = 1;
    public static final int COLINDEX_NAME = 2;
    public static final int COLINDEX_QUANTITY = 3;
    
    public AssetTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        typeForeignKey = table.getColumns().get(COLINDEX_TYPE).getForeignKey();
        assetLoanTable = new AssetLoanTable(schema.getTables().getByName("Asset_Loan"));
    }
    
    public final AssetTable.Record createRecord()
    {
        return new AssetTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByAssetIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ASSET_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNameEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByQuantityEquality()
    {
        return accessors.get(ACCESSORID_BY_QUANTITY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByTypeEquality()
    {
        return accessors.get(ACCESSORID_BY_TYPE_EQUALITY);
    }
    
    public final AssetTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final AutoIncColumn getAssetIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_ASSET_ID);
    }
    
    public final AssetLoanTable getAssetLoanTable()
    {
        return assetLoanTable;
    }
    
    public final TextColumn getNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME);
    }
    
    public final IntegerColumn getQuantityColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_QUANTITY);
    }
    
    public final AssetTable.Record getRecord(Row row)
    {
        return new AssetTable.Record(row);
    }
    
    public final AssetTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long assetId, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { assetId }, null);
        Record result = row != null ? new AssetTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final AssetTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new AssetTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final EnumerationIdRefColumn getTypeColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_TYPE);
    }
    
    public final ForeignKey getTypeForeignKey()
    {
        return typeForeignKey;
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private AssetLoanTable assetLoanTable;
    private Schema schema;
    private com.netspective.axiom.schema.table.BasicTable table;
    private ForeignKey typeForeignKey;
    
    public final class Record
    {
        
        public Record(Row row)
        {
            if(row.getTable() != table) throw new ClassCastException("Attempting to assign row from table "+ row.getTable().getName() +" to "+ this.getClass().getName() +" (expecting a row from table "+ table.getName() +").");
            this.row = row;
            this.values = row.getColumnValues();
        }
        
        public final AssetLoanTable.Record createAssetLoanTableRecord()
        {
            return assetLoanTable.createChildLinkedByAssetId(this);
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
            assetLoanTableRecords.delete(cc);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getAssetId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ASSET_ID);
        }
        
        public final AssetLoanTable.Records getAssetLoanTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (assetLoanTableRecords != null) return assetLoanTableRecords;
            assetLoanTableRecords = assetLoanTable.getParentRecordsByAssetId(this, cc);
            return assetLoanTableRecords;
        }
        
        public final AssetLoanTable.Records getAssetLoanTableRecords()
        {
            return assetLoanTableRecords;
        }
        
        public final TextColumn.TextColumnValue getName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME);
        }
        
        public final IntegerColumn.IntegerColumnValue getQuantity()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_QUANTITY);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getType()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_TYPE);
        }
        
        public final auto.dal.db.vo.Asset getValues()
        {
            return getValues(new auto.dal.db.vo.impl.AssetVO());
        }
        
        public final auto.dal.db.vo.Asset getValues(auto.dal.db.vo.Asset valueObject)
        {
            Object autoIncAssetIdValue = values.getByColumnIndex(COLINDEX_ASSET_ID).getValue();
            valueObject.setAssetId(autoIncAssetIdValue instanceof Integer ? new Long(((Integer) autoIncAssetIdValue).intValue()) : (Long) autoIncAssetIdValue);
            valueObject.setType((java.lang.Integer) values.getByColumnIndex(COLINDEX_TYPE).getValue());
            valueObject.setName((java.lang.String) values.getByColumnIndex(COLINDEX_NAME).getValue());
            valueObject.setQuantity((java.lang.Integer) values.getByColumnIndex(COLINDEX_QUANTITY).getValue());
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
            assetLoanTableRecords = getAssetLoanTableRecords(cc);
        }
        
        public final void setAssetId(com.netspective.commons.value.Value value)
        {
            getAssetId().copyValueByReference(value);
        }
        
        public final void setName(com.netspective.commons.value.Value value)
        {
            getName().copyValueByReference(value);
        }
        
        public final void setQuantity(com.netspective.commons.value.Value value)
        {
            getQuantity().copyValueByReference(value);
        }
        
        public final void setType(com.netspective.commons.value.Value value)
        {
            getType().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Asset valueObject)
        {
            values.getByColumnIndex(COLINDEX_ASSET_ID).setValue(valueObject.getAssetId());
            values.getByColumnIndex(COLINDEX_TYPE).setValue(valueObject.getType());
            values.getByColumnIndex(COLINDEX_NAME).setValue(valueObject.getName());
            values.getByColumnIndex(COLINDEX_QUANTITY).setValue(valueObject.getQuantity());
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
        private AssetLoanTable.Records assetLoanTableRecords;
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
        
        public final AssetTable.Record get(int i)
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
        private Rows rows;
    }
}