package auto.dal.db.dao.person.person_session;

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
import com.netspective.axiom.schema.column.type.GuidTextColumn;
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;
import com.netspective.axiom.schema.column.type.DateTimeColumn;
import com.netspective.axiom.schema.column.type.DateColumn;
import com.netspective.axiom.schema.column.type.IntegerColumn;
import auto.dal.db.dao.person.PersonSessionTable;

public final class PersonSessionActivityTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PARENT_KEY_EQUALITY = 1;
    public static final int ACCESSORID_BY_SESSION_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_ACTIVITY_TYPE_EQUALITY = 3;
    public static final int ACCESSORID_BY_ACTIVITY_STAMP_EQUALITY = 4;
    public static final int ACCESSORID_BY_ACTION_TYPE_EQUALITY = 5;
    public static final int ACCESSORID_BY_ACTION_SCOPE_EQUALITY = 6;
    public static final int ACCESSORID_BY_ACTION_KEY_EQUALITY = 7;
    public static final int ACCESSORID_BY_DETAIL_LEVEL_EQUALITY = 8;
    public static final int ACCESSORID_BY_ACTIVITY_DATA_EQUALITY = 9;
    public static final int COLINDEX_SESSION_ID = 0;
    public static final int COLINDEX_ACTIVITY_TYPE = 1;
    public static final int COLINDEX_ACTIVITY_STAMP = 2;
    public static final int COLINDEX_ACTION_TYPE = 3;
    public static final int COLINDEX_ACTION_SCOPE = 4;
    public static final int COLINDEX_ACTION_KEY = 5;
    public static final int COLINDEX_DETAIL_LEVEL = 6;
    public static final int COLINDEX_ACTIVITY_DATA = 7;
    
    public PersonSessionActivityTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        sessionIdForeignKey = (ParentForeignKey)table.getColumns().get(COLINDEX_SESSION_ID).getForeignKey();
        activityTypeForeignKey = table.getColumns().get(COLINDEX_ACTIVITY_TYPE).getForeignKey();
        actionTypeForeignKey = table.getColumns().get(COLINDEX_ACTION_TYPE).getForeignKey();
    }
    
    public final PersonSessionActivityTable.Record createChildLinkedBySessionId(PersonSessionTable.Record parentRecord)
    {
        return new PersonSessionActivityTable.Record(table.createRow(sessionIdForeignKey, parentRecord.getRow()));
    }
    
    public final PersonSessionActivityTable.Record createRecord()
    {
        return new PersonSessionActivityTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByActionKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_ACTION_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByActionScopeEquality()
    {
        return accessors.get(ACCESSORID_BY_ACTION_SCOPE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByActionTypeEquality()
    {
        return accessors.get(ACCESSORID_BY_ACTION_TYPE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByActivityDataEquality()
    {
        return accessors.get(ACCESSORID_BY_ACTIVITY_DATA_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByActivityStampEquality()
    {
        return accessors.get(ACCESSORID_BY_ACTIVITY_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByActivityTypeEquality()
    {
        return accessors.get(ACCESSORID_BY_ACTIVITY_TYPE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByDetailLevelEquality()
    {
        return accessors.get(ACCESSORID_BY_DETAIL_LEVEL_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByParentKeyEquality()
    {
        return accessors.get(ACCESSORID_BY_PARENT_KEY_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySessionIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SESSION_ID_EQUALITY);
    }
    
    public final PersonSessionActivityTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getActionKeyColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ACTION_KEY);
    }
    
    public final TextColumn getActionScopeColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ACTION_SCOPE);
    }
    
    public final EnumerationIdRefColumn getActionTypeColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_ACTION_TYPE);
    }
    
    public final ForeignKey getActionTypeForeignKey()
    {
        return actionTypeForeignKey;
    }
    
    public final TextColumn getActivityDataColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ACTIVITY_DATA);
    }
    
    public final DateTimeColumn getActivityStampColumn()
    {
        return (DateTimeColumn)table.getColumns().get(COLINDEX_ACTIVITY_STAMP);
    }
    
    public final EnumerationIdRefColumn getActivityTypeColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_ACTIVITY_TYPE);
    }
    
    public final ForeignKey getActivityTypeForeignKey()
    {
        return activityTypeForeignKey;
    }
    
    public final IntegerColumn getDetailLevelColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_DETAIL_LEVEL);
    }
    
    /**
     * Parent reference: ParentForeignKey Sources:
     *  PersonSession_Activity.session_id; Referenced Columns:
     *  Person_Session.session_id
     */
    public final PersonSessionActivityTable.Records getParentRecordsBySessionId(PersonSessionTable.Record parentRecord, ConnectionContext cc)
    throws NamingException, SQLException
    {
        Records result = new Records(parentRecord, sessionIdForeignKey.getChildRowsByParentRow(cc, parentRecord.getRow()));
        return result;
    }
    
    public final PersonSessionActivityTable.Record getRecord(Row row)
    {
        return new PersonSessionActivityTable.Record(row);
    }
    
    public final GuidTextColumn getSessionIdColumn()
    {
        return (GuidTextColumn)table.getColumns().get(COLINDEX_SESSION_ID);
    }
    
    public final ParentForeignKey getSessionIdForeignKey()
    {
        return sessionIdForeignKey;
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
    private ForeignKey actionTypeForeignKey;
    private ForeignKey activityTypeForeignKey;
    private Schema schema;
    private ParentForeignKey sessionIdForeignKey;
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
        
        public final TextColumn.TextColumnValue getActionKey()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ACTION_KEY);
        }
        
        public final TextColumn.TextColumnValue getActionScope()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ACTION_SCOPE);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getActionType()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_ACTION_TYPE);
        }
        
        public final TextColumn.TextColumnValue getActivityData()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ACTIVITY_DATA);
        }
        
        public final DateColumn.DateColumnValue getActivityStamp()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_ACTIVITY_STAMP);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getActivityType()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_ACTIVITY_TYPE);
        }
        
        public final IntegerColumn.IntegerColumnValue getDetailLevel()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_DETAIL_LEVEL);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getSessionId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SESSION_ID);
        }
        
        public final auto.dal.db.vo.PersonSessionActivity getValues()
        {
            return getValues(new auto.dal.db.vo.impl.PersonSessionActivityVO());
        }
        
        public final auto.dal.db.vo.PersonSessionActivity getValues(auto.dal.db.vo.PersonSessionActivity valueObject)
        {
            valueObject.setSessionId((java.lang.String) values.getByColumnIndex(COLINDEX_SESSION_ID).getValue());
            valueObject.setActivityType((java.lang.Integer) values.getByColumnIndex(COLINDEX_ACTIVITY_TYPE).getValue());
            valueObject.setActivityStamp((java.util.Date) values.getByColumnIndex(COLINDEX_ACTIVITY_STAMP).getValue());
            valueObject.setActionType((java.lang.Integer) values.getByColumnIndex(COLINDEX_ACTION_TYPE).getValue());
            valueObject.setActionScope((java.lang.String) values.getByColumnIndex(COLINDEX_ACTION_SCOPE).getValue());
            valueObject.setActionKey((java.lang.String) values.getByColumnIndex(COLINDEX_ACTION_KEY).getValue());
            valueObject.setDetailLevel((java.lang.Integer) values.getByColumnIndex(COLINDEX_DETAIL_LEVEL).getValue());
            valueObject.setActivityData((java.lang.String) values.getByColumnIndex(COLINDEX_ACTIVITY_DATA).getValue());
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
        
        public final void setActionKey(com.netspective.commons.value.Value value)
        {
            getActionKey().copyValueByReference(value);
        }
        
        public final void setActionScope(com.netspective.commons.value.Value value)
        {
            getActionScope().copyValueByReference(value);
        }
        
        public final void setActionType(com.netspective.commons.value.Value value)
        {
            getActionType().copyValueByReference(value);
        }
        
        public final void setActivityData(com.netspective.commons.value.Value value)
        {
            getActivityData().copyValueByReference(value);
        }
        
        public final void setActivityStamp(com.netspective.commons.value.Value value)
        {
            getActivityStamp().copyValueByReference(value);
        }
        
        public final void setActivityType(com.netspective.commons.value.Value value)
        {
            getActivityType().copyValueByReference(value);
        }
        
        public final void setDetailLevel(com.netspective.commons.value.Value value)
        {
            getDetailLevel().copyValueByReference(value);
        }
        
        public final void setSessionId(com.netspective.commons.value.Value value)
        {
            getSessionId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.PersonSessionActivity valueObject)
        {
            values.getByColumnIndex(COLINDEX_SESSION_ID).setValue(valueObject.getSessionId());
            values.getByColumnIndex(COLINDEX_ACTIVITY_TYPE).setValue(valueObject.getActivityType());
            values.getByColumnIndex(COLINDEX_ACTIVITY_STAMP).setValue(valueObject.getActivityStamp());
            values.getByColumnIndex(COLINDEX_ACTION_TYPE).setValue(valueObject.getActionType());
            values.getByColumnIndex(COLINDEX_ACTION_SCOPE).setValue(valueObject.getActionScope());
            values.getByColumnIndex(COLINDEX_ACTION_KEY).setValue(valueObject.getActionKey());
            values.getByColumnIndex(COLINDEX_DETAIL_LEVEL).setValue(valueObject.getDetailLevel());
            values.getByColumnIndex(COLINDEX_ACTIVITY_DATA).setValue(valueObject.getActivityData());
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
        
        public Records(PersonSessionTable.Record parentRecord, Rows rows)
        {
            this(rows);
            this.parentRecord = parentRecord;
        }
        
        public final PersonSessionActivityTable.Record get(int i)
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
        private PersonSessionTable.Record parentRecord;
        private Rows rows;
    }
}