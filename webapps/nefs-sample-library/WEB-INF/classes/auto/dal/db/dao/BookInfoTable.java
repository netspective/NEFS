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
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;

public final class BookInfoTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_ID_EQUALITY = 1;
    public static final int ACCESSORID_BY_NAME_EQUALITY = 2;
    public static final int ACCESSORID_BY_AUTHOR_EQUALITY = 3;
    public static final int ACCESSORID_BY_GENRE_EQUALITY = 4;
    public static final int ACCESSORID_BY_ISBN_EQUALITY = 5;
    public static final int COLINDEX_ID = 0;
    public static final int COLINDEX_NAME = 1;
    public static final int COLINDEX_AUTHOR = 2;
    public static final int COLINDEX_GENRE = 3;
    public static final int COLINDEX_ISBN = 4;
    
    public BookInfoTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        genreForeignKey = table.getColumns().get(COLINDEX_GENRE).getForeignKey();
    }
    
    public final BookInfoTable.Record createRecord()
    {
        return new BookInfoTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByAuthorEquality()
    {
        return accessors.get(ACCESSORID_BY_AUTHOR_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByGenreEquality()
    {
        return accessors.get(ACCESSORID_BY_GENRE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIsbnEquality()
    {
        return accessors.get(ACCESSORID_BY_ISBN_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNameEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final BookInfoTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getAuthorColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_AUTHOR);
    }
    
    public final EnumerationIdRefColumn getGenreColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_GENRE);
    }
    
    public final ForeignKey getGenreForeignKey()
    {
        return genreForeignKey;
    }
    
    public final TextColumn getIdColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ID);
    }
    
    public final TextColumn getIsbnColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ISBN);
    }
    
    public final TextColumn getNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME);
    }
    
    public final BookInfoTable.Record getRecord(Row row)
    {
        return new BookInfoTable.Record(row);
    }
    
    public final BookInfoTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String id)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { id }, null);
        Record result = row != null ? new BookInfoTable.Record(row) : null;
        return result;
    }
    
    public final BookInfoTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new BookInfoTable.Record(row) : null;
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
    private ForeignKey genreForeignKey;
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
        
        public final TextColumn.TextColumnValue getAuthor()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_AUTHOR);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getGenre()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_GENRE);
        }
        
        public final TextColumn.TextColumnValue getId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ID);
        }
        
        public final TextColumn.TextColumnValue getIsbn()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ISBN);
        }
        
        public final TextColumn.TextColumnValue getName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final auto.dal.db.vo.BookInfo getValues()
        {
            return getValues(new auto.dal.db.vo.impl.BookInfoVO());
        }
        
        public final auto.dal.db.vo.BookInfo getValues(auto.dal.db.vo.BookInfo valueObject)
        {
            valueObject.setId((java.lang.String) values.getByColumnIndex(COLINDEX_ID).getValue());
            valueObject.setName((java.lang.String) values.getByColumnIndex(COLINDEX_NAME).getValue());
            valueObject.setAuthor((java.lang.String) values.getByColumnIndex(COLINDEX_AUTHOR).getValue());
            valueObject.setGenre((java.lang.Integer) values.getByColumnIndex(COLINDEX_GENRE).getValue());
            valueObject.setIsbn((java.lang.String) values.getByColumnIndex(COLINDEX_ISBN).getValue());
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
        
        public final void setAuthor(com.netspective.commons.value.Value value)
        {
            getAuthor().copyValueByReference(value);
        }
        
        public final void setGenre(com.netspective.commons.value.Value value)
        {
            getGenre().copyValueByReference(value);
        }
        
        public final void setId(com.netspective.commons.value.Value value)
        {
            getId().copyValueByReference(value);
        }
        
        public final void setIsbn(com.netspective.commons.value.Value value)
        {
            getIsbn().copyValueByReference(value);
        }
        
        public final void setName(com.netspective.commons.value.Value value)
        {
            getName().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.BookInfo valueObject)
        {
            values.getByColumnIndex(COLINDEX_ID).setValue(valueObject.getId());
            values.getByColumnIndex(COLINDEX_NAME).setValue(valueObject.getName());
            values.getByColumnIndex(COLINDEX_AUTHOR).setValue(valueObject.getAuthor());
            values.getByColumnIndex(COLINDEX_GENRE).setValue(valueObject.getGenre());
            values.getByColumnIndex(COLINDEX_ISBN).setValue(valueObject.getIsbn());
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
        
        public final BookInfoTable.Record get(int i)
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