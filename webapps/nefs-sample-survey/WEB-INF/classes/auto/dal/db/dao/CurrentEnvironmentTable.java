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
import com.netspective.axiom.schema.column.type.TextColumn;

public final class CurrentEnvironmentTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_PIN_EQUALITY = 1;
    public static final int ACCESSORID_BY_APPROACH_EQUALITY = 2;
    public static final int ACCESSORID_BY_APPROACH_EXPL_EQUALITY = 3;
    public static final int ACCESSORID_BY_RISKS_PRJ_EQUALITY = 4;
    public static final int ACCESSORID_BY_RISKS_PRJ_EXPL_EQUALITY = 5;
    public static final int ACCESSORID_BY_RISKS_DEPT_EQUALITY = 6;
    public static final int ACCESSORID_BY_RISKS_DEPT_EXPL_EQUALITY = 7;
    public static final int ACCESSORID_BY_RISKS_DIV_EQUALITY = 8;
    public static final int ACCESSORID_BY_RISKS_DIV_EXPL_EQUALITY = 9;
    public static final int ACCESSORID_BY_RISKS_DELTA_EQUALITY = 10;
    public static final int ACCESSORID_BY_RISKS_DELTA_EXPL_EQUALITY = 11;
    public static final int COLINDEX_PIN = 0;
    public static final int COLINDEX_APPROACH = 1;
    public static final int COLINDEX_APPROACH_EXPL = 2;
    public static final int COLINDEX_RISKS_PRJ = 3;
    public static final int COLINDEX_RISKS_PRJ_EXPL = 4;
    public static final int COLINDEX_RISKS_DEPT = 5;
    public static final int COLINDEX_RISKS_DEPT_EXPL = 6;
    public static final int COLINDEX_RISKS_DIV = 7;
    public static final int COLINDEX_RISKS_DIV_EXPL = 8;
    public static final int COLINDEX_RISKS_DELTA = 9;
    public static final int COLINDEX_RISKS_DELTA_EXPL = 10;
    
    public CurrentEnvironmentTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
    }
    
    public final CurrentEnvironmentTable.Record createRecord()
    {
        return new CurrentEnvironmentTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByApproachEquality()
    {
        return accessors.get(ACCESSORID_BY_APPROACH_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByApproachExplEquality()
    {
        return accessors.get(ACCESSORID_BY_APPROACH_EXPL_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPinEquality()
    {
        return accessors.get(ACCESSORID_BY_PIN_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRisksDeltaEquality()
    {
        return accessors.get(ACCESSORID_BY_RISKS_DELTA_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRisksDeltaExplEquality()
    {
        return accessors.get(ACCESSORID_BY_RISKS_DELTA_EXPL_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRisksDeptEquality()
    {
        return accessors.get(ACCESSORID_BY_RISKS_DEPT_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRisksDeptExplEquality()
    {
        return accessors.get(ACCESSORID_BY_RISKS_DEPT_EXPL_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRisksDivEquality()
    {
        return accessors.get(ACCESSORID_BY_RISKS_DIV_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRisksDivExplEquality()
    {
        return accessors.get(ACCESSORID_BY_RISKS_DIV_EXPL_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRisksPrjEquality()
    {
        return accessors.get(ACCESSORID_BY_RISKS_PRJ_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRisksPrjExplEquality()
    {
        return accessors.get(ACCESSORID_BY_RISKS_PRJ_EXPL_EQUALITY);
    }
    
    public final CurrentEnvironmentTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TextColumn getApproachColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_APPROACH);
    }
    
    public final TextColumn getApproachExplColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_APPROACH_EXPL);
    }
    
    public final IntegerColumn getPinColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_PIN);
    }
    
    public final CurrentEnvironmentTable.Record getRecord(Row row)
    {
        return new CurrentEnvironmentTable.Record(row);
    }
    
    public final CurrentEnvironmentTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Integer pin)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { pin }, null);
        Record result = row != null ? new CurrentEnvironmentTable.Record(row) : null;
        return result;
    }
    
    public final CurrentEnvironmentTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new CurrentEnvironmentTable.Record(row) : null;
        return result;
    }
    
    public final TextColumn getRisksDeltaColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISKS_DELTA);
    }
    
    public final TextColumn getRisksDeltaExplColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISKS_DELTA_EXPL);
    }
    
    public final TextColumn getRisksDeptColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISKS_DEPT);
    }
    
    public final TextColumn getRisksDeptExplColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISKS_DEPT_EXPL);
    }
    
    public final TextColumn getRisksDivColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISKS_DIV);
    }
    
    public final TextColumn getRisksDivExplColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISKS_DIV_EXPL);
    }
    
    public final TextColumn getRisksPrjColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISKS_PRJ);
    }
    
    public final TextColumn getRisksPrjExplColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISKS_PRJ_EXPL);
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
        
        public final TextColumn.TextColumnValue getApproach()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_APPROACH);
        }
        
        public final TextColumn.TextColumnValue getApproachExpl()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_APPROACH_EXPL);
        }
        
        public final IntegerColumn.IntegerColumnValue getPin()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_PIN);
        }
        
        public final TextColumn.TextColumnValue getRisksDelta()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISKS_DELTA);
        }
        
        public final TextColumn.TextColumnValue getRisksDeltaExpl()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISKS_DELTA_EXPL);
        }
        
        public final TextColumn.TextColumnValue getRisksDept()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISKS_DEPT);
        }
        
        public final TextColumn.TextColumnValue getRisksDeptExpl()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISKS_DEPT_EXPL);
        }
        
        public final TextColumn.TextColumnValue getRisksDiv()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISKS_DIV);
        }
        
        public final TextColumn.TextColumnValue getRisksDivExpl()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISKS_DIV_EXPL);
        }
        
        public final TextColumn.TextColumnValue getRisksPrj()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISKS_PRJ);
        }
        
        public final TextColumn.TextColumnValue getRisksPrjExpl()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISKS_PRJ_EXPL);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final auto.dal.db.vo.CurrentEnvironment getValues()
        {
            return getValues(new auto.dal.db.vo.impl.CurrentEnvironmentVO());
        }
        
        public final auto.dal.db.vo.CurrentEnvironment getValues(auto.dal.db.vo.CurrentEnvironment valueObject)
        {
            valueObject.setPin((java.lang.Integer) values.getByColumnIndex(COLINDEX_PIN).getValue());
            valueObject.setApproach((java.lang.String) values.getByColumnIndex(COLINDEX_APPROACH).getValue());
            valueObject.setApproachExpl((java.lang.String) values.getByColumnIndex(COLINDEX_APPROACH_EXPL).getValue());
            valueObject.setRisksPrj((java.lang.String) values.getByColumnIndex(COLINDEX_RISKS_PRJ).getValue());
            valueObject.setRisksPrjExpl((java.lang.String) values.getByColumnIndex(COLINDEX_RISKS_PRJ_EXPL).getValue());
            valueObject.setRisksDept((java.lang.String) values.getByColumnIndex(COLINDEX_RISKS_DEPT).getValue());
            valueObject.setRisksDeptExpl((java.lang.String) values.getByColumnIndex(COLINDEX_RISKS_DEPT_EXPL).getValue());
            valueObject.setRisksDiv((java.lang.String) values.getByColumnIndex(COLINDEX_RISKS_DIV).getValue());
            valueObject.setRisksDivExpl((java.lang.String) values.getByColumnIndex(COLINDEX_RISKS_DIV_EXPL).getValue());
            valueObject.setRisksDelta((java.lang.String) values.getByColumnIndex(COLINDEX_RISKS_DELTA).getValue());
            valueObject.setRisksDeltaExpl((java.lang.String) values.getByColumnIndex(COLINDEX_RISKS_DELTA_EXPL).getValue());
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
        
        public final void setApproach(com.netspective.commons.value.Value value)
        {
            getApproach().copyValueByReference(value);
        }
        
        public final void setApproachExpl(com.netspective.commons.value.Value value)
        {
            getApproachExpl().copyValueByReference(value);
        }
        
        public final void setPin(com.netspective.commons.value.Value value)
        {
            getPin().copyValueByReference(value);
        }
        
        public final void setRisksDelta(com.netspective.commons.value.Value value)
        {
            getRisksDelta().copyValueByReference(value);
        }
        
        public final void setRisksDeltaExpl(com.netspective.commons.value.Value value)
        {
            getRisksDeltaExpl().copyValueByReference(value);
        }
        
        public final void setRisksDept(com.netspective.commons.value.Value value)
        {
            getRisksDept().copyValueByReference(value);
        }
        
        public final void setRisksDeptExpl(com.netspective.commons.value.Value value)
        {
            getRisksDeptExpl().copyValueByReference(value);
        }
        
        public final void setRisksDiv(com.netspective.commons.value.Value value)
        {
            getRisksDiv().copyValueByReference(value);
        }
        
        public final void setRisksDivExpl(com.netspective.commons.value.Value value)
        {
            getRisksDivExpl().copyValueByReference(value);
        }
        
        public final void setRisksPrj(com.netspective.commons.value.Value value)
        {
            getRisksPrj().copyValueByReference(value);
        }
        
        public final void setRisksPrjExpl(com.netspective.commons.value.Value value)
        {
            getRisksPrjExpl().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.CurrentEnvironment valueObject)
        {
            values.getByColumnIndex(COLINDEX_PIN).setValue(valueObject.getPin());
            values.getByColumnIndex(COLINDEX_APPROACH).setValue(valueObject.getApproach());
            values.getByColumnIndex(COLINDEX_APPROACH_EXPL).setValue(valueObject.getApproachExpl());
            values.getByColumnIndex(COLINDEX_RISKS_PRJ).setValue(valueObject.getRisksPrj());
            values.getByColumnIndex(COLINDEX_RISKS_PRJ_EXPL).setValue(valueObject.getRisksPrjExpl());
            values.getByColumnIndex(COLINDEX_RISKS_DEPT).setValue(valueObject.getRisksDept());
            values.getByColumnIndex(COLINDEX_RISKS_DEPT_EXPL).setValue(valueObject.getRisksDeptExpl());
            values.getByColumnIndex(COLINDEX_RISKS_DIV).setValue(valueObject.getRisksDiv());
            values.getByColumnIndex(COLINDEX_RISKS_DIV_EXPL).setValue(valueObject.getRisksDivExpl());
            values.getByColumnIndex(COLINDEX_RISKS_DELTA).setValue(valueObject.getRisksDelta());
            values.getByColumnIndex(COLINDEX_RISKS_DELTA_EXPL).setValue(valueObject.getRisksDeltaExpl());
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
        
        public final CurrentEnvironmentTable.Record get(int i)
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