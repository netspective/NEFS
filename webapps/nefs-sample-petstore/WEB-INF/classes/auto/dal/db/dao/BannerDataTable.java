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

public final class BannerDataTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_BANNER_DATA_ID_EQUALITY = 1;
    public static final int ACCESSORID_BY_BANNER_NAME_EQUALITY = 2;
    public static final int COLINDEX_BANNER_DATA_ID = 0;
    public static final int COLINDEX_BANNER_NAME = 1;
    
    public BannerDataTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
    }
    
    public final BannerDataTable.Record createRecord()
    {
        return new BannerDataTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByBannerDataIdEquality()
    {
        return accessors.get(ACCESSORID_BY_BANNER_DATA_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBannerNameEquality()
    {
        return accessors.get(ACCESSORID_BY_BANNER_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final BannerDataTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getBannerDataIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_BANNER_DATA_ID);
    }
    
    public final TextColumn getBannerNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_BANNER_NAME);
    }
    
    public final BannerDataTable.Record getRecord(Row row)
    {
        return new BannerDataTable.Record(row);
    }
    
    public final BannerDataTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String bannerDataId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { bannerDataId }, null);
        Record result = row != null ? new BannerDataTable.Record(row) : null;
        return result;
    }
    
    public final BannerDataTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new BannerDataTable.Record(row) : null;
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
        
        public final TextColumn.TextColumnValue getBannerDataId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_BANNER_DATA_ID);
        }
        
        public final TextColumn.TextColumnValue getBannerName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_BANNER_NAME);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final auto.dal.db.vo.BannerData getValues()
        {
            return getValues(new auto.dal.db.vo.impl.BannerDataVO());
        }
        
        public final auto.dal.db.vo.BannerData getValues(auto.dal.db.vo.BannerData valueObject)
        {
            valueObject.setBannerDataId((java.lang.String) values.getByColumnIndex(COLINDEX_BANNER_DATA_ID).getValue());
            valueObject.setBannerName((java.lang.String) values.getByColumnIndex(COLINDEX_BANNER_NAME).getValue());
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
        
        public final void setBannerDataId(com.netspective.commons.value.Value value)
        {
            getBannerDataId().copyValueByReference(value);
        }
        
        public final void setBannerName(com.netspective.commons.value.Value value)
        {
            getBannerName().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.BannerData valueObject)
        {
            values.getByColumnIndex(COLINDEX_BANNER_DATA_ID).setValue(valueObject.getBannerDataId());
            values.getByColumnIndex(COLINDEX_BANNER_NAME).setValue(valueObject.getBannerName());
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
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final BannerDataTable.Record get(int i)
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