package auto.dal.db.dao.asset;

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
import com.netspective.axiom.schema.column.type.DateColumn;
import com.netspective.axiom.schema.column.type.BooleanColumn;
import auto.dal.db.dao.AssetTable;

public final class AssetLoanTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_ASSET_LOAN_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_ASSET_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_BORROWER_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_LOAN_TYPE_EQUALITY = 5;
    public static final int ACCESSORID_BY_LOAN_BEGIN_DATE_EQUALITY = 6;
    public static final int ACCESSORID_BY_LOAN_END_DATE_EQUALITY = 7;
    public static final int ACCESSORID_BY_RETURNED_EQUALITY = 8;
    public static final int COLINDEX_ASSET_LOAN_ID = 0;
    public static final int COLINDEX_ASSET_ID = 1;
    public static final int COLINDEX_BORROWER_ID = 2;
    public static final int COLINDEX_LOAN_TYPE = 3;
    public static final int COLINDEX_LOAN_BEGIN_DATE = 4;
    public static final int COLINDEX_LOAN_END_DATE = 5;
    public static final int COLINDEX_RETURNED = 6;
    
    public AssetLoanTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        assetIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_ASSET_ID).getForeignKey();
        borrowerIdForeignKey = table.getColumns().get(COLINDEX_BORROWER_ID).getForeignKey();
        loanTypeForeignKey = table.getColumns().get(COLINDEX_LOAN_TYPE).getForeignKey();
    }
    
    public final AssetLoanTable.Record createChildLinkedByAssetId(AssetTable.Record parentRecord)
    {
        return new AssetLoanTable.Record(table.createRow(assetIdForeignKey, parentRecord.getRow()));
    }
    
    public final AssetLoanTable.Record createRecord()
    {
        return new AssetLoanTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByAssetIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ASSET_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByAssetLoanIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ASSET_LOAN_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBorrowerIdEquality()
    {
        return accessors.get(ACCESSORID_BY_BORROWER_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLoanBeginDateEquality()
    {
        return accessors.get(ACCESSORID_BY_LOAN_BEGIN_DATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLoanEndDateEquality()
    {
        return accessors.get(ACCESSORID_BY_LOAN_END_DATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLoanTypeEquality()
    {
        return accessors.get(ACCESSORID_BY_LOAN_TYPE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByReturnedEquality()
    {
        return accessors.get(ACCESSORID_BY_RETURNED_EQUALITY);
    }
    
    public final AssetLoanTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final LongIntegerColumn getAssetIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_ASSET_ID);
    }
    
    public final ParentForeignKey getAssetIdForeignKey()
    {
        return assetIdForeignKey;
    }
    
    public final AutoIncColumn getAssetLoanIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_ASSET_LOAN_ID);
    }
    
    public final LongIntegerColumn getBorrowerIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_BORROWER_ID);
    }
    
    public final ForeignKey getBorrowerIdForeignKey()
    {
        return borrowerIdForeignKey;
    }
    
    public final DateColumn getLoanBeginDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_LOAN_BEGIN_DATE);
    }
    
    public final DateColumn getLoanEndDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_LOAN_END_DATE);
    }
    
    public final EnumerationIdRefColumn getLoanTypeColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_LOAN_TYPE);
    }
    
    public final ForeignKey getLoanTypeForeignKey()
    {
        return loanTypeForeignKey;
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: Asset_Loan.asset_id; Referenced
     *  Columns: Asset.asset_id
     */
    public final AssetLoanTable.Records getParentRecordsByAssetId(AssetTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, assetIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final AssetLoanTable.Record getRecord(Row row)
    {
        return new AssetLoanTable.Record(row);
    }
    
    public final AssetLoanTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long assetLoanId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { assetLoanId }, null);
        Record result = row != null ? new AssetLoanTable.Record(row) : null;
        return result;
    }
    
    public final AssetLoanTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new AssetLoanTable.Record(row) : null;
        return result;
    }
    
    public final BooleanColumn getReturnedColumn()
    {
        return (BooleanColumn)table.getColumns().get(COLINDEX_RETURNED);
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
    private ParentForeignKey assetIdForeignKey;
    private ForeignKey borrowerIdForeignKey;
    private ForeignKey loanTypeForeignKey;
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
        
        public final LongIntegerColumn.LongIntegerColumnValue getAssetId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ASSET_ID);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getAssetLoanId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ASSET_LOAN_ID);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getBorrowerId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_BORROWER_ID);
        }
        
        public final DateColumn.DateColumnValue getLoanBeginDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_LOAN_BEGIN_DATE);
        }
        
        public final DateColumn.DateColumnValue getLoanEndDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_LOAN_END_DATE);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getLoanType()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_LOAN_TYPE);
        }
        
        public final BooleanColumn.BooleanColumnValue getReturned()
        {
            return (BooleanColumn.BooleanColumnValue)values.getByColumnIndex(COLINDEX_RETURNED);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final auto.dal.db.vo.AssetLoan getValues()
        {
            return getValues(new auto.dal.db.vo.impl.AssetLoanVO());
        }
        
        public final auto.dal.db.vo.AssetLoan getValues(auto.dal.db.vo.AssetLoan valueObject)
        {
            Object autoIncAssetLoanIdValue = values.getByColumnIndex(COLINDEX_ASSET_LOAN_ID).getValue();
            valueObject.setAssetLoanId(autoIncAssetLoanIdValue instanceof Integer ? new Long(((Integer) autoIncAssetLoanIdValue).intValue()) : (Long) autoIncAssetLoanIdValue);
            valueObject.setAssetId((java.lang.Long) values.getByColumnIndex(COLINDEX_ASSET_ID).getValue());
            valueObject.setBorrowerId((java.lang.Long) values.getByColumnIndex(COLINDEX_BORROWER_ID).getValue());
            valueObject.setLoanType((java.lang.Integer) values.getByColumnIndex(COLINDEX_LOAN_TYPE).getValue());
            valueObject.setLoanBeginDate((java.util.Date) values.getByColumnIndex(COLINDEX_LOAN_BEGIN_DATE).getValue());
            valueObject.setLoanEndDate((java.util.Date) values.getByColumnIndex(COLINDEX_LOAN_END_DATE).getValue());
            valueObject.setReturned((java.lang.Boolean) values.getByColumnIndex(COLINDEX_RETURNED).getValue());
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
        
        public final void setAssetId(com.netspective.commons.value.Value value)
        {
            getAssetId().copyValueByReference(value);
        }
        
        public final void setAssetLoanId(com.netspective.commons.value.Value value)
        {
            getAssetLoanId().copyValueByReference(value);
        }
        
        public final void setBorrowerId(com.netspective.commons.value.Value value)
        {
            getBorrowerId().copyValueByReference(value);
        }
        
        public final void setLoanBeginDate(com.netspective.commons.value.Value value)
        {
            getLoanBeginDate().copyValueByReference(value);
        }
        
        public final void setLoanEndDate(com.netspective.commons.value.Value value)
        {
            getLoanEndDate().copyValueByReference(value);
        }
        
        public final void setLoanType(com.netspective.commons.value.Value value)
        {
            getLoanType().copyValueByReference(value);
        }
        
        public final void setReturned(com.netspective.commons.value.Value value)
        {
            getReturned().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.AssetLoan valueObject)
        {
            values.getByColumnIndex(COLINDEX_ASSET_LOAN_ID).setValue(valueObject.getAssetLoanId());
            values.getByColumnIndex(COLINDEX_ASSET_ID).setValue(valueObject.getAssetId());
            values.getByColumnIndex(COLINDEX_BORROWER_ID).setValue(valueObject.getBorrowerId());
            values.getByColumnIndex(COLINDEX_LOAN_TYPE).setValue(valueObject.getLoanType());
            values.getByColumnIndex(COLINDEX_LOAN_BEGIN_DATE).setValue(valueObject.getLoanBeginDate());
            values.getByColumnIndex(COLINDEX_LOAN_END_DATE).setValue(valueObject.getLoanEndDate());
            values.getByColumnIndex(COLINDEX_RETURNED).setValue(valueObject.getReturned());
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
        
        public Records(AssetTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final AssetLoanTable.Record get(int i)
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
        private AssetTable.Record parentRecord;
        private Rows rows;
    }
}