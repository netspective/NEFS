package auto.dal.db.dao.person;

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
import com.netspective.axiom.schema.column.type.GuidColumn;
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.axiom.schema.column.type.LongIntegerColumn;
import com.netspective.axiom.schema.column.type.DateTimeColumn;
import com.netspective.axiom.schema.column.type.DateColumn;
import auto.dal.db.dao.PersonTable;
import auto.dal.db.dao.person.person_session.PersonSessionActivityTable;
import auto.dal.db.dao.person.person_session.PersonSessionViewCountTable;

public final class PersonSessionTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_SESSION_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_PERSON_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_ORG_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_REMOTE_HOST_EQUALITY = 5;
    public static final int ACCESSORID_BY_REMOTE_ADDR_EQUALITY = 6;
    public static final int ACCESSORID_BY_FIRST_ACCESS_EQUALITY = 7;
    public static final int ACCESSORID_BY_LAST_ACCESS_EQUALITY = 8;
    public static final int COLINDEX_SESSION_ID = 0;
    public static final int COLINDEX_PERSON_ID = 1;
    public static final int COLINDEX_ORG_ID = 2;
    public static final int COLINDEX_REMOTE_HOST = 3;
    public static final int COLINDEX_REMOTE_ADDR = 4;
    public static final int COLINDEX_FIRST_ACCESS = 5;
    public static final int COLINDEX_LAST_ACCESS = 6;
    
    public PersonSessionTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        personIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_PERSON_ID).getForeignKey();
        orgIdForeignKey = table.getColumns().get(COLINDEX_ORG_ID).getForeignKey();
        personSessionActivityTable = new PersonSessionActivityTable(schema.getTables().getByName("PersonSession_Activity"));
        personSessionViewCountTable = new PersonSessionViewCountTable(schema.getTables().getByName("PersonSession_View_Count"));
    }
    
    public final PersonSessionTable.Record createChildLinkedByPersonId(PersonTable.Record parentRecord)
    {
        return new PersonSessionTable.Record(table.createRow(personIdForeignKey, parentRecord.getRow()));
    }
    
    public final PersonSessionTable.Record createRecord()
    {
        return new PersonSessionTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByFirstAccessEquality()
    {
        return accessors.get(ACCESSORID_BY_FIRST_ACCESS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLastAccessEquality()
    {
        return accessors.get(ACCESSORID_BY_LAST_ACCESS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrgIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ORG_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPersonIdEquality()
    {
        return accessors.get(ACCESSORID_BY_PERSON_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRemoteAddrEquality()
    {
        return accessors.get(ACCESSORID_BY_REMOTE_ADDR_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRemoteHostEquality()
    {
        return accessors.get(ACCESSORID_BY_REMOTE_HOST_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySessionIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SESSION_ID_EQUALITY);
    }
    
    public final PersonSessionTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final DateTimeColumn getFirstAccessColumn()
    {
        return (DateTimeColumn)table.getColumns().get(COLINDEX_FIRST_ACCESS);
    }
    
    public final DateTimeColumn getLastAccessColumn()
    {
        return (DateTimeColumn)table.getColumns().get(COLINDEX_LAST_ACCESS);
    }
    
    public final LongIntegerColumn getOrgIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_ORG_ID);
    }
    
    public final ForeignKey getOrgIdForeignKey()
    {
        return orgIdForeignKey;
    }
    
    /**
     * Parent reference: ParentForeignKey Sources: Person_Session.person_id;
     *  Referenced Columns: Person.person_id
     */
    public final PersonSessionTable.Records getParentRecordsByPersonId(PersonTable.Record parentRecord, ConnectionContext cc, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, personIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        if(retrieveChildren) result.retrieveChildren(cc);
        return result;
    }
    
    public final LongIntegerColumn getPersonIdColumn()
    {
        return (LongIntegerColumn)table.getColumns().get(COLINDEX_PERSON_ID);
    }
    
    public final ParentForeignKey getPersonIdForeignKey()
    {
        return personIdForeignKey;
    }
    
    public final PersonSessionActivityTable getPersonSessionActivityTable()
    {
        return personSessionActivityTable;
    }
    
    public final PersonSessionViewCountTable getPersonSessionViewCountTable()
    {
        return personSessionViewCountTable;
    }
    
    public final PersonSessionTable.Record getRecord(Row row)
    {
        return new PersonSessionTable.Record(row);
    }
    
    public final PersonSessionTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.String sessionId, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { sessionId }, null);
        Record result = row != null ? new PersonSessionTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final PersonSessionTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new PersonSessionTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final TextColumn getRemoteAddrColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_REMOTE_ADDR);
    }
    
    public final TextColumn getRemoteHostColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_REMOTE_HOST);
    }
    
    public final GuidColumn getSessionIdColumn()
    {
        return (GuidColumn)table.getColumns().get(COLINDEX_SESSION_ID);
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
    private ForeignKey orgIdForeignKey;
    private ParentForeignKey personIdForeignKey;
    private PersonSessionActivityTable personSessionActivityTable;
    private PersonSessionViewCountTable personSessionViewCountTable;
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
        
        public final PersonSessionActivityTable.Record createPersonSessionActivityTableRecord()
        {
            return personSessionActivityTable.createChildLinkedBySessionId(this);
        }
        
        public final PersonSessionViewCountTable.Record createPersonSessionViewCountTableRecord()
        {
            return personSessionViewCountTable.createChildLinkedBySessionId(this);
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
            personSessionActivityTableRecords.delete(cc);
            personSessionViewCountTableRecords.delete(cc);
        }
        
        public final DateColumn.DateColumnValue getFirstAccess()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_FIRST_ACCESS);
        }
        
        public final DateColumn.DateColumnValue getLastAccess()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_LAST_ACCESS);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getOrgId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ORG_ID);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getPersonId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_PERSON_ID);
        }
        
        public final PersonSessionActivityTable.Records getPersonSessionActivityTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personSessionActivityTableRecords != null) return personSessionActivityTableRecords;
            personSessionActivityTableRecords = personSessionActivityTable.getParentRecordsBySessionId(this, cc);
            return personSessionActivityTableRecords;
        }
        
        public final PersonSessionActivityTable.Records getPersonSessionActivityTableRecords()
        {
            return personSessionActivityTableRecords;
        }
        
        public final PersonSessionViewCountTable.Records getPersonSessionViewCountTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personSessionViewCountTableRecords != null) return personSessionViewCountTableRecords;
            personSessionViewCountTableRecords = personSessionViewCountTable.getParentRecordsBySessionId(this, cc);
            return personSessionViewCountTableRecords;
        }
        
        public final PersonSessionViewCountTable.Records getPersonSessionViewCountTableRecords()
        {
            return personSessionViewCountTableRecords;
        }
        
        public final TextColumn.TextColumnValue getRemoteAddr()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_REMOTE_ADDR);
        }
        
        public final TextColumn.TextColumnValue getRemoteHost()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_REMOTE_HOST);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getSessionId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SESSION_ID);
        }
        
        public final auto.dal.db.vo.PersonSession getValues()
        {
            return getValues(new auto.dal.db.vo.impl.PersonSessionVO());
        }
        
        public final auto.dal.db.vo.PersonSession getValues(auto.dal.db.vo.PersonSession valueObject)
        {
            valueObject.setSessionId((java.lang.String) values.getByColumnIndex(COLINDEX_SESSION_ID).getValue());
            valueObject.setPersonId((java.lang.Long) values.getByColumnIndex(COLINDEX_PERSON_ID).getValue());
            valueObject.setOrgId((java.lang.Long) values.getByColumnIndex(COLINDEX_ORG_ID).getValue());
            valueObject.setRemoteHost((java.lang.String) values.getByColumnIndex(COLINDEX_REMOTE_HOST).getValue());
            valueObject.setRemoteAddr((java.lang.String) values.getByColumnIndex(COLINDEX_REMOTE_ADDR).getValue());
            valueObject.setFirstAccess((java.util.Date) values.getByColumnIndex(COLINDEX_FIRST_ACCESS).getValue());
            valueObject.setLastAccess((java.util.Date) values.getByColumnIndex(COLINDEX_LAST_ACCESS).getValue());
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
            personSessionActivityTableRecords = getPersonSessionActivityTableRecords(cc);
            personSessionViewCountTableRecords = getPersonSessionViewCountTableRecords(cc);
        }
        
        public final void setFirstAccess(com.netspective.commons.value.Value value)
        {
            getFirstAccess().copyValueByReference(value);
        }
        
        public final void setLastAccess(com.netspective.commons.value.Value value)
        {
            getLastAccess().copyValueByReference(value);
        }
        
        public final void setOrgId(com.netspective.commons.value.Value value)
        {
            getOrgId().copyValueByReference(value);
        }
        
        public final void setPersonId(com.netspective.commons.value.Value value)
        {
            getPersonId().copyValueByReference(value);
        }
        
        public final void setRemoteAddr(com.netspective.commons.value.Value value)
        {
            getRemoteAddr().copyValueByReference(value);
        }
        
        public final void setRemoteHost(com.netspective.commons.value.Value value)
        {
            getRemoteHost().copyValueByReference(value);
        }
        
        public final void setSessionId(com.netspective.commons.value.Value value)
        {
            getSessionId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.PersonSession valueObject)
        {
            values.getByColumnIndex(COLINDEX_SESSION_ID).setValue(valueObject.getSessionId());
            values.getByColumnIndex(COLINDEX_PERSON_ID).setValue(valueObject.getPersonId());
            values.getByColumnIndex(COLINDEX_ORG_ID).setValue(valueObject.getOrgId());
            values.getByColumnIndex(COLINDEX_REMOTE_HOST).setValue(valueObject.getRemoteHost());
            values.getByColumnIndex(COLINDEX_REMOTE_ADDR).setValue(valueObject.getRemoteAddr());
            values.getByColumnIndex(COLINDEX_FIRST_ACCESS).setValue(valueObject.getFirstAccess());
            values.getByColumnIndex(COLINDEX_LAST_ACCESS).setValue(valueObject.getLastAccess());
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
        private PersonSessionActivityTable.Records personSessionActivityTableRecords;
        private PersonSessionViewCountTable.Records personSessionViewCountTableRecords;
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
        
        public Records(PersonTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final PersonSessionTable.Record get(int i)
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
        private PersonTable.Record parentRecord;
        private Rows rows;
    }
}