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
import com.netspective.axiom.schema.column.type.IntegerColumn;
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;

public final class RiskResponseTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_SYSTEM_ID_EQUALITY = 1;
    public static final int ACCESSORID_BY_PIN_EQUALITY = 2;
    public static final int ACCESSORID_BY_RISK_GROUP_EQUALITY = 3;
    public static final int ACCESSORID_BY_RISK_EQUALITY = 4;
    public static final int ACCESSORID_BY_IBU_SIG_EQUALITY = 5;
    public static final int ACCESSORID_BY_IBU_EFF_EQUALITY = 6;
    public static final int ACCESSORID_BY_LBG_SIG_EQUALITY = 7;
    public static final int ACCESSORID_BY_LBG_EFF_EQUALITY = 8;
    public static final int ACCESSORID_BY_FIRM_SIG_EQUALITY = 9;
    public static final int ACCESSORID_BY_FIRM_EFF_EQUALITY = 10;
    public static final int ACCESSORID_BY_INDEX_UNIQUE_RISK_EQUALITY = 11;
    public static final int COLINDEX_SYSTEM_ID = 0;
    public static final int COLINDEX_PIN = 1;
    public static final int COLINDEX_RISK_GROUP = 2;
    public static final int COLINDEX_RISK = 3;
    public static final int COLINDEX_IBU_SIG = 4;
    public static final int COLINDEX_IBU_EFF = 5;
    public static final int COLINDEX_LBG_SIG = 6;
    public static final int COLINDEX_LBG_EFF = 7;
    public static final int COLINDEX_FIRM_SIG = 8;
    public static final int COLINDEX_FIRM_EFF = 9;
    
    public RiskResponseTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        ibuSigForeignKey = table.getColumns().get(COLINDEX_IBU_SIG).getForeignKey();
        ibuEffForeignKey = table.getColumns().get(COLINDEX_IBU_EFF).getForeignKey();
        lbgSigForeignKey = table.getColumns().get(COLINDEX_LBG_SIG).getForeignKey();
        lbgEffForeignKey = table.getColumns().get(COLINDEX_LBG_EFF).getForeignKey();
        firmSigForeignKey = table.getColumns().get(COLINDEX_FIRM_SIG).getForeignKey();
        firmEffForeignKey = table.getColumns().get(COLINDEX_FIRM_EFF).getForeignKey();
    }
    
    public final RiskResponseTable.Record createRecord()
    {
        return new RiskResponseTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByFirmEffEquality()
    {
        return accessors.get(ACCESSORID_BY_FIRM_EFF_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByFirmSigEquality()
    {
        return accessors.get(ACCESSORID_BY_FIRM_SIG_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIbuEffEquality()
    {
        return accessors.get(ACCESSORID_BY_IBU_EFF_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIbuSigEquality()
    {
        return accessors.get(ACCESSORID_BY_IBU_SIG_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIndexUniqueRiskEquality()
    {
        return accessors.get(ACCESSORID_BY_INDEX_UNIQUE_RISK_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLbgEffEquality()
    {
        return accessors.get(ACCESSORID_BY_LBG_EFF_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLbgSigEquality()
    {
        return accessors.get(ACCESSORID_BY_LBG_SIG_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPinEquality()
    {
        return accessors.get(ACCESSORID_BY_PIN_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRiskEquality()
    {
        return accessors.get(ACCESSORID_BY_RISK_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRiskGroupEquality()
    {
        return accessors.get(ACCESSORID_BY_RISK_GROUP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySystemIdEquality()
    {
        return accessors.get(ACCESSORID_BY_SYSTEM_ID_EQUALITY);
    }
    
    public final RiskResponseTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final EnumerationIdRefColumn getFirmEffColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_FIRM_EFF);
    }
    
    public final ForeignKey getFirmEffForeignKey()
    {
        return firmEffForeignKey;
    }
    
    public final EnumerationIdRefColumn getFirmSigColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_FIRM_SIG);
    }
    
    public final ForeignKey getFirmSigForeignKey()
    {
        return firmSigForeignKey;
    }
    
    public final EnumerationIdRefColumn getIbuEffColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_IBU_EFF);
    }
    
    public final ForeignKey getIbuEffForeignKey()
    {
        return ibuEffForeignKey;
    }
    
    public final EnumerationIdRefColumn getIbuSigColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_IBU_SIG);
    }
    
    public final ForeignKey getIbuSigForeignKey()
    {
        return ibuSigForeignKey;
    }
    
    public final EnumerationIdRefColumn getLbgEffColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_LBG_EFF);
    }
    
    public final ForeignKey getLbgEffForeignKey()
    {
        return lbgEffForeignKey;
    }
    
    public final EnumerationIdRefColumn getLbgSigColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_LBG_SIG);
    }
    
    public final ForeignKey getLbgSigForeignKey()
    {
        return lbgSigForeignKey;
    }
    
    public final IntegerColumn getPinColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_PIN);
    }
    
    public final RiskResponseTable.Record getRecord(Row row)
    {
        return new RiskResponseTable.Record(row);
    }
    
    public final RiskResponseTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long systemId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { systemId }, null);
        Record result = row != null ? new RiskResponseTable.Record(row) : null;
        return result;
    }
    
    public final RiskResponseTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new RiskResponseTable.Record(row) : null;
        return result;
    }
    
    public final TextColumn getRiskColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISK);
    }
    
    public final TextColumn getRiskGroupColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_RISK_GROUP);
    }
    
    public final AutoIncColumn getSystemIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_SYSTEM_ID);
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
    private ForeignKey firmEffForeignKey;
    private ForeignKey firmSigForeignKey;
    private ForeignKey ibuEffForeignKey;
    private ForeignKey ibuSigForeignKey;
    private ForeignKey lbgEffForeignKey;
    private ForeignKey lbgSigForeignKey;
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
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getFirmEff()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_FIRM_EFF);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getFirmSig()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_FIRM_SIG);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getIbuEff()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_IBU_EFF);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getIbuSig()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_IBU_SIG);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getLbgEff()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_LBG_EFF);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getLbgSig()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_LBG_SIG);
        }
        
        public final IntegerColumn.IntegerColumnValue getPin()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_PIN);
        }
        
        public final TextColumn.TextColumnValue getRisk()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISK);
        }
        
        public final TextColumn.TextColumnValue getRiskGroup()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_RISK_GROUP);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getSystemId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_SYSTEM_ID);
        }
        
        public final auto.dal.db.vo.RiskResponse getValues()
        {
            return getValues(new auto.dal.db.vo.impl.RiskResponseVO());
        }
        
        public final auto.dal.db.vo.RiskResponse getValues(auto.dal.db.vo.RiskResponse valueObject)
        {
            Object autoIncSystemIdValue = values.getByColumnIndex(COLINDEX_SYSTEM_ID).getValue();
            valueObject.setSystemId(autoIncSystemIdValue instanceof Integer ? new Long(((Integer) autoIncSystemIdValue).intValue()) : (Long) autoIncSystemIdValue);
            valueObject.setPin((java.lang.Integer) values.getByColumnIndex(COLINDEX_PIN).getValue());
            valueObject.setRiskGroup((java.lang.String) values.getByColumnIndex(COLINDEX_RISK_GROUP).getValue());
            valueObject.setRisk((java.lang.String) values.getByColumnIndex(COLINDEX_RISK).getValue());
            valueObject.setIbuSig((java.lang.Integer) values.getByColumnIndex(COLINDEX_IBU_SIG).getValue());
            valueObject.setIbuEff((java.lang.Integer) values.getByColumnIndex(COLINDEX_IBU_EFF).getValue());
            valueObject.setLbgSig((java.lang.Integer) values.getByColumnIndex(COLINDEX_LBG_SIG).getValue());
            valueObject.setLbgEff((java.lang.Integer) values.getByColumnIndex(COLINDEX_LBG_EFF).getValue());
            valueObject.setFirmSig((java.lang.Integer) values.getByColumnIndex(COLINDEX_FIRM_SIG).getValue());
            valueObject.setFirmEff((java.lang.Integer) values.getByColumnIndex(COLINDEX_FIRM_EFF).getValue());
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
        
        public final void setFirmEff(com.netspective.commons.value.Value value)
        {
            getFirmEff().copyValueByReference(value);
        }
        
        public final void setFirmSig(com.netspective.commons.value.Value value)
        {
            getFirmSig().copyValueByReference(value);
        }
        
        public final void setIbuEff(com.netspective.commons.value.Value value)
        {
            getIbuEff().copyValueByReference(value);
        }
        
        public final void setIbuSig(com.netspective.commons.value.Value value)
        {
            getIbuSig().copyValueByReference(value);
        }
        
        public final void setLbgEff(com.netspective.commons.value.Value value)
        {
            getLbgEff().copyValueByReference(value);
        }
        
        public final void setLbgSig(com.netspective.commons.value.Value value)
        {
            getLbgSig().copyValueByReference(value);
        }
        
        public final void setPin(com.netspective.commons.value.Value value)
        {
            getPin().copyValueByReference(value);
        }
        
        public final void setRisk(com.netspective.commons.value.Value value)
        {
            getRisk().copyValueByReference(value);
        }
        
        public final void setRiskGroup(com.netspective.commons.value.Value value)
        {
            getRiskGroup().copyValueByReference(value);
        }
        
        public final void setSystemId(com.netspective.commons.value.Value value)
        {
            getSystemId().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.RiskResponse valueObject)
        {
            values.getByColumnIndex(COLINDEX_SYSTEM_ID).setValue(valueObject.getSystemId());
            values.getByColumnIndex(COLINDEX_PIN).setValue(valueObject.getPin());
            values.getByColumnIndex(COLINDEX_RISK_GROUP).setValue(valueObject.getRiskGroup());
            values.getByColumnIndex(COLINDEX_RISK).setValue(valueObject.getRisk());
            values.getByColumnIndex(COLINDEX_IBU_SIG).setValue(valueObject.getIbuSig());
            values.getByColumnIndex(COLINDEX_IBU_EFF).setValue(valueObject.getIbuEff());
            values.getByColumnIndex(COLINDEX_LBG_SIG).setValue(valueObject.getLbgSig());
            values.getByColumnIndex(COLINDEX_LBG_EFF).setValue(valueObject.getLbgEff());
            values.getByColumnIndex(COLINDEX_FIRM_SIG).setValue(valueObject.getFirmSig());
            values.getByColumnIndex(COLINDEX_FIRM_EFF).setValue(valueObject.getFirmEff());
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
        
        public final RiskResponseTable.Record get(int i)
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