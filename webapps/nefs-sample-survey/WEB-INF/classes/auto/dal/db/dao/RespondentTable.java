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
import com.netspective.axiom.schema.column.type.IntegerColumn;
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;

public final class RespondentTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PIN_EQUALITY = 1;
    public static final int ACCESSORID_BY_TITLE_EQUALITY = 2;
    public static final int ACCESSORID_BY_FUNCTION_EQUALITY = 3;
    public static final int ACCESSORID_BY_LOCATION_EQUALITY = 4;
    public static final int ACCESSORID_BY_BUSINESS_UNIT_EQUALITY = 5;
    public static final int ACCESSORID_BY_DIVISION_EQUALITY = 6;
    public static final int ACCESSORID_BY_YRS_FIRM_EQUALITY = 7;
    public static final int ACCESSORID_BY_YRS_CURRENT_POS_EQUALITY = 8;
    public static final int ACCESSORID_BY_LOCKED_EQUALITY = 9;
    public static final int COLINDEX_PIN = 0;
    public static final int COLINDEX_TITLE = 1;
    public static final int COLINDEX_FUNCTION = 2;
    public static final int COLINDEX_LOCATION = 3;
    public static final int COLINDEX_BUSINESS_UNIT = 4;
    public static final int COLINDEX_DIVISION = 5;
    public static final int COLINDEX_YRS_FIRM = 6;
    public static final int COLINDEX_YRS_CURRENT_POS = 7;
    public static final int COLINDEX_LOCKED = 8;
    
    public RespondentTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        titleForeignKey = table.getColumns().get(COLINDEX_TITLE).getForeignKey();
        functionForeignKey = table.getColumns().get(COLINDEX_FUNCTION).getForeignKey();
        locationForeignKey = table.getColumns().get(COLINDEX_LOCATION).getForeignKey();
        businessUnitForeignKey = table.getColumns().get(COLINDEX_BUSINESS_UNIT).getForeignKey();
        divisionForeignKey = table.getColumns().get(COLINDEX_DIVISION).getForeignKey();
    }
    
    public final RespondentTable.Record createRecord()
    {
        return new RespondentTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByBusinessUnitEquality()
    {
        return accessors.get(ACCESSORID_BY_BUSINESS_UNIT_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByDivisionEquality()
    {
        return accessors.get(ACCESSORID_BY_DIVISION_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByFunctionEquality()
    {
        return accessors.get(ACCESSORID_BY_FUNCTION_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLocationEquality()
    {
        return accessors.get(ACCESSORID_BY_LOCATION_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLockedEquality()
    {
        return accessors.get(ACCESSORID_BY_LOCKED_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPinEquality()
    {
        return accessors.get(ACCESSORID_BY_PIN_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByTitleEquality()
    {
        return accessors.get(ACCESSORID_BY_TITLE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByYrsCurrentPosEquality()
    {
        return accessors.get(ACCESSORID_BY_YRS_CURRENT_POS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByYrsFirmEquality()
    {
        return accessors.get(ACCESSORID_BY_YRS_FIRM_EQUALITY);
    }
    
    public final RespondentTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final EnumerationIdRefColumn getBusinessUnitColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_BUSINESS_UNIT);
    }
    
    public final ForeignKey getBusinessUnitForeignKey()
    {
        return businessUnitForeignKey;
    }
    
    public final EnumerationIdRefColumn getDivisionColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_DIVISION);
    }
    
    public final ForeignKey getDivisionForeignKey()
    {
        return divisionForeignKey;
    }
    
    public final EnumerationIdRefColumn getFunctionColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_FUNCTION);
    }
    
    public final ForeignKey getFunctionForeignKey()
    {
        return functionForeignKey;
    }
    
    public final EnumerationIdRefColumn getLocationColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_LOCATION);
    }
    
    public final ForeignKey getLocationForeignKey()
    {
        return locationForeignKey;
    }
    
    public final IntegerColumn getLockedColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_LOCKED);
    }
    
    public final IntegerColumn getPinColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_PIN);
    }
    
    public final RespondentTable.Record getRecord(Row row)
    {
        return new RespondentTable.Record(row);
    }
    
    public final RespondentTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Integer pin)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { pin }, null);
        Record result = row != null ? new RespondentTable.Record(row) : null;
        return result;
    }
    
    public final RespondentTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new RespondentTable.Record(row) : null;
        return result;
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final EnumerationIdRefColumn getTitleColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_TITLE);
    }
    
    public final ForeignKey getTitleForeignKey()
    {
        return titleForeignKey;
    }
    
    public final IntegerColumn getYrsCurrentPosColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_YRS_CURRENT_POS);
    }
    
    public final IntegerColumn getYrsFirmColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_YRS_FIRM);
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private ForeignKey businessUnitForeignKey;
    private ForeignKey divisionForeignKey;
    private ForeignKey functionForeignKey;
    private ForeignKey locationForeignKey;
    private Schema schema;
    private com.netspective.axiom.schema.table.BasicTable table;
    private ForeignKey titleForeignKey;
    
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
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getBusinessUnit()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_BUSINESS_UNIT);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getDivision()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_DIVISION);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getFunction()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_FUNCTION);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getLocation()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_LOCATION);
        }
        
        public final IntegerColumn.IntegerColumnValue getLocked()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_LOCKED);
        }
        
        public final IntegerColumn.IntegerColumnValue getPin()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_PIN);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getTitle()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_TITLE);
        }
        
        public final auto.dal.db.vo.Respondent getValues()
        {
            return getValues(new auto.dal.db.vo.impl.RespondentVO());
        }
        
        public final auto.dal.db.vo.Respondent getValues(auto.dal.db.vo.Respondent valueObject)
        {
            valueObject.setPin((java.lang.Integer) values.getByColumnIndex(COLINDEX_PIN).getValue());
            valueObject.setTitle((java.lang.Integer) values.getByColumnIndex(COLINDEX_TITLE).getValue());
            valueObject.setFunction((java.lang.Integer) values.getByColumnIndex(COLINDEX_FUNCTION).getValue());
            valueObject.setLocation((java.lang.Integer) values.getByColumnIndex(COLINDEX_LOCATION).getValue());
            valueObject.setBusinessUnit((java.lang.Integer) values.getByColumnIndex(COLINDEX_BUSINESS_UNIT).getValue());
            valueObject.setDivision((java.lang.Integer) values.getByColumnIndex(COLINDEX_DIVISION).getValue());
            valueObject.setYrsFirm((java.lang.Integer) values.getByColumnIndex(COLINDEX_YRS_FIRM).getValue());
            valueObject.setYrsCurrentPos((java.lang.Integer) values.getByColumnIndex(COLINDEX_YRS_CURRENT_POS).getValue());
            valueObject.setLocked((java.lang.Integer) values.getByColumnIndex(COLINDEX_LOCKED).getValue());
            return valueObject;
        }
        
        public final IntegerColumn.IntegerColumnValue getYrsCurrentPos()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_YRS_CURRENT_POS);
        }
        
        public final IntegerColumn.IntegerColumnValue getYrsFirm()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_YRS_FIRM);
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
        
        public final void setBusinessUnit(com.netspective.commons.value.Value value)
        {
            getBusinessUnit().copyValueByReference(value);
        }
        
        public final void setDivision(com.netspective.commons.value.Value value)
        {
            getDivision().copyValueByReference(value);
        }
        
        public final void setFunction(com.netspective.commons.value.Value value)
        {
            getFunction().copyValueByReference(value);
        }
        
        public final void setLocation(com.netspective.commons.value.Value value)
        {
            getLocation().copyValueByReference(value);
        }
        
        public final void setLocked(com.netspective.commons.value.Value value)
        {
            getLocked().copyValueByReference(value);
        }
        
        public final void setPin(com.netspective.commons.value.Value value)
        {
            getPin().copyValueByReference(value);
        }
        
        public final void setTitle(com.netspective.commons.value.Value value)
        {
            getTitle().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Respondent valueObject)
        {
            values.getByColumnIndex(COLINDEX_PIN).setValue(valueObject.getPin());
            values.getByColumnIndex(COLINDEX_TITLE).setValue(valueObject.getTitle());
            values.getByColumnIndex(COLINDEX_FUNCTION).setValue(valueObject.getFunction());
            values.getByColumnIndex(COLINDEX_LOCATION).setValue(valueObject.getLocation());
            values.getByColumnIndex(COLINDEX_BUSINESS_UNIT).setValue(valueObject.getBusinessUnit());
            values.getByColumnIndex(COLINDEX_DIVISION).setValue(valueObject.getDivision());
            values.getByColumnIndex(COLINDEX_YRS_FIRM).setValue(valueObject.getYrsFirm());
            values.getByColumnIndex(COLINDEX_YRS_CURRENT_POS).setValue(valueObject.getYrsCurrentPos());
            values.getByColumnIndex(COLINDEX_LOCKED).setValue(valueObject.getLocked());
        }
        
        public final void setYrsCurrentPos(com.netspective.commons.value.Value value)
        {
            getYrsCurrentPos().copyValueByReference(value);
        }
        
        public final void setYrsFirm(com.netspective.commons.value.Value value)
        {
            getYrsFirm().copyValueByReference(value);
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
        
        public final RespondentTable.Record get(int i)
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