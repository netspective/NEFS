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
import com.netspective.axiom.schema.column.type.DateTimeColumn;
import com.netspective.axiom.schema.column.type.DateColumn;
import com.netspective.axiom.schema.column.type.GuidTextColumn;
import com.netspective.axiom.schema.column.type.TextColumn;
import com.netspective.axiom.schema.column.type.RecordStatusIdColumn;
import com.netspective.axiom.schema.column.type.EnumerationIdRefColumn;
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.column.type.LongIntegerColumn;
import com.netspective.axiom.schema.column.type.IntegerColumn;
import com.netspective.axiom.schema.column.type.TimeColumn;
import auto.dal.db.dao.org.OrgAddressTable;
import auto.dal.db.dao.org.OrgClassificationTable;
import auto.dal.db.dao.org.OrgContactTable;
import auto.dal.db.dao.org.OrgIdentifierTable;
import auto.dal.db.dao.org.OrgIndustryTable;
import auto.dal.db.dao.org.OrgNoteTable;
import auto.dal.db.dao.org.OrgPersonIdSrcTypeTable;
import auto.dal.db.dao.org.OrgRelationshipTable;
import auto.dal.db.dao.org.OrgRoleDeclarationTable;

public final class OrgTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_CR_STAMP_EQUALITY = 1;
    public static final int ACCESSORID_BY_CR_SESS_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_REC_STAT_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_ORG_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_ORG_CODE_EQUALITY = 5;
    public static final int ACCESSORID_BY_ORG_NAME_EQUALITY = 6;
    public static final int ACCESSORID_BY_ORG_ABBREV_EQUALITY = 7;
    public static final int ACCESSORID_BY_EMPLOYEES_EQUALITY = 8;
    public static final int ACCESSORID_BY_BUSINESS_START_TIME_EQUALITY = 9;
    public static final int ACCESSORID_BY_BUSINESS_END_TIME_EQUALITY = 10;
    public static final int ACCESSORID_BY_TIME_ZONE_EQUALITY = 11;
    public static final int COLINDEX_CR_STAMP = 0;
    public static final int COLINDEX_CR_SESS_ID = 1;
    public static final int COLINDEX_REC_STAT_ID = 2;
    public static final int COLINDEX_ORG_ID = 3;
    public static final int COLINDEX_ORG_CODE = 4;
    public static final int COLINDEX_ORG_NAME = 5;
    public static final int COLINDEX_ORG_ABBREV = 6;
    public static final int COLINDEX_EMPLOYEES = 7;
    public static final int COLINDEX_BUSINESS_START_TIME = 8;
    public static final int COLINDEX_BUSINESS_END_TIME = 9;
    public static final int COLINDEX_TIME_ZONE = 10;
    
    public OrgTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        crSessIdForeignKey = table.getColumns().get(COLINDEX_CR_SESS_ID).getForeignKey();
        recStatIdForeignKey = table.getColumns().get(COLINDEX_REC_STAT_ID).getForeignKey();
        orgAddressTable = new OrgAddressTable(schema.getTables().getByName("Org_Address"));
        orgClassificationTable = new OrgClassificationTable(schema.getTables().getByName("Org_Classification"));
        orgContactTable = new OrgContactTable(schema.getTables().getByName("Org_Contact"));
        orgIdentifierTable = new OrgIdentifierTable(schema.getTables().getByName("Org_Identifier"));
        orgIndustryTable = new OrgIndustryTable(schema.getTables().getByName("Org_Industry"));
        orgNoteTable = new OrgNoteTable(schema.getTables().getByName("Org_Note"));
        orgPersonIdSrcTypeTable = new OrgPersonIdSrcTypeTable(schema.getTables().getByName("Org_PersonId_Src_Type"));
        orgRelationshipTable = new OrgRelationshipTable(schema.getTables().getByName("Org_Relationship"));
        orgRoleDeclarationTable = new OrgRoleDeclarationTable(schema.getTables().getByName("Org_Role_Declaration"));
    }
    
    public final OrgTable.Record createRecord()
    {
        return new OrgTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByBusinessEndTimeEquality()
    {
        return accessors.get(ACCESSORID_BY_BUSINESS_END_TIME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBusinessStartTimeEquality()
    {
        return accessors.get(ACCESSORID_BY_BUSINESS_START_TIME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrSessIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_SESS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrStampEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByEmployeesEquality()
    {
        return accessors.get(ACCESSORID_BY_EMPLOYEES_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrgAbbrevEquality()
    {
        return accessors.get(ACCESSORID_BY_ORG_ABBREV_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrgCodeEquality()
    {
        return accessors.get(ACCESSORID_BY_ORG_CODE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrgIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ORG_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByOrgNameEquality()
    {
        return accessors.get(ACCESSORID_BY_ORG_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRecStatIdEquality()
    {
        return accessors.get(ACCESSORID_BY_REC_STAT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByTimeZoneEquality()
    {
        return accessors.get(ACCESSORID_BY_TIME_ZONE_EQUALITY);
    }
    
    public final OrgTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final TimeColumn getBusinessEndTimeColumn()
    {
        return (TimeColumn)table.getColumns().get(COLINDEX_BUSINESS_END_TIME);
    }
    
    public final TimeColumn getBusinessStartTimeColumn()
    {
        return (TimeColumn)table.getColumns().get(COLINDEX_BUSINESS_START_TIME);
    }
    
    public final GuidTextColumn getCrSessIdColumn()
    {
        return (GuidTextColumn)table.getColumns().get(COLINDEX_CR_SESS_ID);
    }
    
    public final ForeignKey getCrSessIdForeignKey()
    {
        return crSessIdForeignKey;
    }
    
    public final DateTimeColumn getCrStampColumn()
    {
        return (DateTimeColumn)table.getColumns().get(COLINDEX_CR_STAMP);
    }
    
    public final IntegerColumn getEmployeesColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_EMPLOYEES);
    }
    
    public final TextColumn getOrgAbbrevColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ORG_ABBREV);
    }
    
    public final OrgAddressTable getOrgAddressTable()
    {
        return orgAddressTable;
    }
    
    public final OrgClassificationTable getOrgClassificationTable()
    {
        return orgClassificationTable;
    }
    
    public final TextColumn getOrgCodeColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ORG_CODE);
    }
    
    public final OrgContactTable getOrgContactTable()
    {
        return orgContactTable;
    }
    
    public final AutoIncColumn getOrgIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_ORG_ID);
    }
    
    public final OrgIdentifierTable getOrgIdentifierTable()
    {
        return orgIdentifierTable;
    }
    
    public final OrgIndustryTable getOrgIndustryTable()
    {
        return orgIndustryTable;
    }
    
    public final TextColumn getOrgNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_ORG_NAME);
    }
    
    public final OrgNoteTable getOrgNoteTable()
    {
        return orgNoteTable;
    }
    
    public final OrgPersonIdSrcTypeTable getOrgPersonIdSrcTypeTable()
    {
        return orgPersonIdSrcTypeTable;
    }
    
    public final OrgRelationshipTable getOrgRelationshipTable()
    {
        return orgRelationshipTable;
    }
    
    public final OrgRoleDeclarationTable getOrgRoleDeclarationTable()
    {
        return orgRoleDeclarationTable;
    }
    
    public final RecordStatusIdColumn getRecStatIdColumn()
    {
        return (RecordStatusIdColumn)table.getColumns().get(COLINDEX_REC_STAT_ID);
    }
    
    public final ForeignKey getRecStatIdForeignKey()
    {
        return recStatIdForeignKey;
    }
    
    public final OrgTable.Record getRecord(Row row)
    {
        return new OrgTable.Record(row);
    }
    
    public final OrgTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long orgId, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { orgId }, null);
        Record result = row != null ? new OrgTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final OrgTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new OrgTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final TextColumn getTimeZoneColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_TIME_ZONE);
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private ForeignKey crSessIdForeignKey;
    private OrgAddressTable orgAddressTable;
    private OrgClassificationTable orgClassificationTable;
    private OrgContactTable orgContactTable;
    private OrgIdentifierTable orgIdentifierTable;
    private OrgIndustryTable orgIndustryTable;
    private OrgNoteTable orgNoteTable;
    private OrgPersonIdSrcTypeTable orgPersonIdSrcTypeTable;
    private OrgRelationshipTable orgRelationshipTable;
    private OrgRoleDeclarationTable orgRoleDeclarationTable;
    private ForeignKey recStatIdForeignKey;
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
        
        public final OrgAddressTable.Record createOrgAddressTableRecord()
        {
            return orgAddressTable.createChildLinkedByParentId(this);
        }
        
        public final OrgClassificationTable.Record createOrgClassificationTableRecord()
        {
            return orgClassificationTable.createChildLinkedByParentId(this);
        }
        
        public final OrgContactTable.Record createOrgContactTableRecord()
        {
            return orgContactTable.createChildLinkedByParentId(this);
        }
        
        public final OrgIdentifierTable.Record createOrgIdentifierTableRecord()
        {
            return orgIdentifierTable.createChildLinkedByOrgId(this);
        }
        
        public final OrgIndustryTable.Record createOrgIndustryTableRecord()
        {
            return orgIndustryTable.createChildLinkedByOrgId(this);
        }
        
        public final OrgNoteTable.Record createOrgNoteTableRecord()
        {
            return orgNoteTable.createChildLinkedByParentId(this);
        }
        
        public final OrgPersonIdSrcTypeTable.Record createOrgPersonIdSrcTypeTableRecord()
        {
            return orgPersonIdSrcTypeTable.createChildLinkedByOrgId(this);
        }
        
        public final OrgRelationshipTable.Record createOrgRelationshipTableRecord()
        {
            return orgRelationshipTable.createChildLinkedByParentId(this);
        }
        
        public final OrgRoleDeclarationTable.Record createOrgRoleDeclarationTableRecord()
        {
            return orgRoleDeclarationTable.createChildLinkedByOrgId(this);
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
            orgAddressTableRecords.delete(cc);
            orgClassificationTableRecords.delete(cc);
            orgContactTableRecords.delete(cc);
            orgIdentifierTableRecords.delete(cc);
            orgIndustryTableRecords.delete(cc);
            orgNoteTableRecords.delete(cc);
            orgPersonIdSrcTypeTableRecords.delete(cc);
            orgRelationshipTableRecords.delete(cc);
            orgRoleDeclarationTableRecords.delete(cc);
        }
        
        public final DateColumn.DateColumnValue getBusinessEndTime()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_BUSINESS_END_TIME);
        }
        
        public final DateColumn.DateColumnValue getBusinessStartTime()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_BUSINESS_START_TIME);
        }
        
        public final TextColumn.TextColumnValue getCrSessId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CR_SESS_ID);
        }
        
        public final DateColumn.DateColumnValue getCrStamp()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_CR_STAMP);
        }
        
        public final IntegerColumn.IntegerColumnValue getEmployees()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_EMPLOYEES);
        }
        
        public final TextColumn.TextColumnValue getOrgAbbrev()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ORG_ABBREV);
        }
        
        public final OrgAddressTable.Records getOrgAddressTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgAddressTableRecords != null) return orgAddressTableRecords;
            orgAddressTableRecords = orgAddressTable.getParentRecordsByParentId(this, cc);
            return orgAddressTableRecords;
        }
        
        public final OrgAddressTable.Records getOrgAddressTableRecords()
        {
            return orgAddressTableRecords;
        }
        
        public final OrgClassificationTable.Records getOrgClassificationTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgClassificationTableRecords != null) return orgClassificationTableRecords;
            orgClassificationTableRecords = orgClassificationTable.getParentRecordsByParentId(this, cc);
            return orgClassificationTableRecords;
        }
        
        public final OrgClassificationTable.Records getOrgClassificationTableRecords()
        {
            return orgClassificationTableRecords;
        }
        
        public final TextColumn.TextColumnValue getOrgCode()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ORG_CODE);
        }
        
        public final OrgContactTable.Records getOrgContactTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgContactTableRecords != null) return orgContactTableRecords;
            orgContactTableRecords = orgContactTable.getParentRecordsByParentId(this, cc);
            return orgContactTableRecords;
        }
        
        public final OrgContactTable.Records getOrgContactTableRecords()
        {
            return orgContactTableRecords;
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getOrgId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_ORG_ID);
        }
        
        public final OrgIdentifierTable.Records getOrgIdentifierTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgIdentifierTableRecords != null) return orgIdentifierTableRecords;
            orgIdentifierTableRecords = orgIdentifierTable.getParentRecordsByOrgId(this, cc);
            return orgIdentifierTableRecords;
        }
        
        public final OrgIdentifierTable.Records getOrgIdentifierTableRecords()
        {
            return orgIdentifierTableRecords;
        }
        
        public final OrgIndustryTable.Records getOrgIndustryTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgIndustryTableRecords != null) return orgIndustryTableRecords;
            orgIndustryTableRecords = orgIndustryTable.getParentRecordsByOrgId(this, cc);
            return orgIndustryTableRecords;
        }
        
        public final OrgIndustryTable.Records getOrgIndustryTableRecords()
        {
            return orgIndustryTableRecords;
        }
        
        public final TextColumn.TextColumnValue getOrgName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ORG_NAME);
        }
        
        public final OrgNoteTable.Records getOrgNoteTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgNoteTableRecords != null) return orgNoteTableRecords;
            orgNoteTableRecords = orgNoteTable.getParentRecordsByParentId(this, cc);
            return orgNoteTableRecords;
        }
        
        public final OrgNoteTable.Records getOrgNoteTableRecords()
        {
            return orgNoteTableRecords;
        }
        
        public final OrgPersonIdSrcTypeTable.Records getOrgPersonIdSrcTypeTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgPersonIdSrcTypeTableRecords != null) return orgPersonIdSrcTypeTableRecords;
            orgPersonIdSrcTypeTableRecords = orgPersonIdSrcTypeTable.getParentRecordsByOrgId(this, cc);
            return orgPersonIdSrcTypeTableRecords;
        }
        
        public final OrgPersonIdSrcTypeTable.Records getOrgPersonIdSrcTypeTableRecords()
        {
            return orgPersonIdSrcTypeTableRecords;
        }
        
        public final OrgRelationshipTable.Records getOrgRelationshipTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgRelationshipTableRecords != null) return orgRelationshipTableRecords;
            orgRelationshipTableRecords = orgRelationshipTable.getParentRecordsByParentId(this, cc);
            return orgRelationshipTableRecords;
        }
        
        public final OrgRelationshipTable.Records getOrgRelationshipTableRecords()
        {
            return orgRelationshipTableRecords;
        }
        
        public final OrgRoleDeclarationTable.Records getOrgRoleDeclarationTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (orgRoleDeclarationTableRecords != null) return orgRoleDeclarationTableRecords;
            orgRoleDeclarationTableRecords = orgRoleDeclarationTable.getParentRecordsByOrgId(this, cc);
            return orgRoleDeclarationTableRecords;
        }
        
        public final OrgRoleDeclarationTable.Records getOrgRoleDeclarationTableRecords()
        {
            return orgRoleDeclarationTableRecords;
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRecStatId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REC_STAT_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getTimeZone()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_TIME_ZONE);
        }
        
        public final auto.dal.db.vo.Org getValues()
        {
            return getValues(new auto.dal.db.vo.impl.OrgVO());
        }
        
        public final auto.dal.db.vo.Org getValues(auto.dal.db.vo.Org valueObject)
        {
            valueObject.setCrStamp((java.util.Date) values.getByColumnIndex(COLINDEX_CR_STAMP).getValue());
            valueObject.setCrSessId((java.lang.String) values.getByColumnIndex(COLINDEX_CR_SESS_ID).getValue());
            valueObject.setRecStatId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REC_STAT_ID).getValue());
            Object autoIncOrgIdValue = values.getByColumnIndex(COLINDEX_ORG_ID).getValue();
            valueObject.setOrgId(autoIncOrgIdValue instanceof Integer ? new Long(((Integer) autoIncOrgIdValue).intValue()) : (Long) autoIncOrgIdValue);
            valueObject.setOrgCode((java.lang.String) values.getByColumnIndex(COLINDEX_ORG_CODE).getValue());
            valueObject.setOrgName((java.lang.String) values.getByColumnIndex(COLINDEX_ORG_NAME).getValue());
            valueObject.setOrgAbbrev((java.lang.String) values.getByColumnIndex(COLINDEX_ORG_ABBREV).getValue());
            valueObject.setEmployees((java.lang.Integer) values.getByColumnIndex(COLINDEX_EMPLOYEES).getValue());
            valueObject.setBusinessStartTime((java.util.Date) values.getByColumnIndex(COLINDEX_BUSINESS_START_TIME).getValue());
            valueObject.setBusinessEndTime((java.util.Date) values.getByColumnIndex(COLINDEX_BUSINESS_END_TIME).getValue());
            valueObject.setTimeZone((java.lang.String) values.getByColumnIndex(COLINDEX_TIME_ZONE).getValue());
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
            orgAddressTableRecords = getOrgAddressTableRecords(cc);
            orgClassificationTableRecords = getOrgClassificationTableRecords(cc);
            orgContactTableRecords = getOrgContactTableRecords(cc);
            orgIdentifierTableRecords = getOrgIdentifierTableRecords(cc);
            orgIndustryTableRecords = getOrgIndustryTableRecords(cc);
            orgNoteTableRecords = getOrgNoteTableRecords(cc);
            orgPersonIdSrcTypeTableRecords = getOrgPersonIdSrcTypeTableRecords(cc);
            orgRelationshipTableRecords = getOrgRelationshipTableRecords(cc);
            orgRoleDeclarationTableRecords = getOrgRoleDeclarationTableRecords(cc);
        }
        
        public final void setBusinessEndTime(com.netspective.commons.value.Value value)
        {
            getBusinessEndTime().copyValueByReference(value);
        }
        
        public final void setBusinessStartTime(com.netspective.commons.value.Value value)
        {
            getBusinessStartTime().copyValueByReference(value);
        }
        
        public final void setCrSessId(com.netspective.commons.value.Value value)
        {
            getCrSessId().copyValueByReference(value);
        }
        
        public final void setCrStamp(com.netspective.commons.value.Value value)
        {
            getCrStamp().copyValueByReference(value);
        }
        
        public final void setEmployees(com.netspective.commons.value.Value value)
        {
            getEmployees().copyValueByReference(value);
        }
        
        public final void setOrgAbbrev(com.netspective.commons.value.Value value)
        {
            getOrgAbbrev().copyValueByReference(value);
        }
        
        public final void setOrgCode(com.netspective.commons.value.Value value)
        {
            getOrgCode().copyValueByReference(value);
        }
        
        public final void setOrgId(com.netspective.commons.value.Value value)
        {
            getOrgId().copyValueByReference(value);
        }
        
        public final void setOrgName(com.netspective.commons.value.Value value)
        {
            getOrgName().copyValueByReference(value);
        }
        
        public final void setRecStatId(com.netspective.commons.value.Value value)
        {
            getRecStatId().copyValueByReference(value);
        }
        
        public final void setTimeZone(com.netspective.commons.value.Value value)
        {
            getTimeZone().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Org valueObject)
        {
            values.getByColumnIndex(COLINDEX_CR_STAMP).setValue(valueObject.getCrStamp());
            values.getByColumnIndex(COLINDEX_CR_SESS_ID).setValue(valueObject.getCrSessId());
            values.getByColumnIndex(COLINDEX_REC_STAT_ID).setValue(valueObject.getRecStatId());
            values.getByColumnIndex(COLINDEX_ORG_ID).setValue(valueObject.getOrgId());
            values.getByColumnIndex(COLINDEX_ORG_CODE).setValue(valueObject.getOrgCode());
            values.getByColumnIndex(COLINDEX_ORG_NAME).setValue(valueObject.getOrgName());
            values.getByColumnIndex(COLINDEX_ORG_ABBREV).setValue(valueObject.getOrgAbbrev());
            values.getByColumnIndex(COLINDEX_EMPLOYEES).setValue(valueObject.getEmployees());
            values.getByColumnIndex(COLINDEX_BUSINESS_START_TIME).setValue(valueObject.getBusinessStartTime());
            values.getByColumnIndex(COLINDEX_BUSINESS_END_TIME).setValue(valueObject.getBusinessEndTime());
            values.getByColumnIndex(COLINDEX_TIME_ZONE).setValue(valueObject.getTimeZone());
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
        private OrgAddressTable.Records orgAddressTableRecords;
        private OrgClassificationTable.Records orgClassificationTableRecords;
        private OrgContactTable.Records orgContactTableRecords;
        private OrgIdentifierTable.Records orgIdentifierTableRecords;
        private OrgIndustryTable.Records orgIndustryTableRecords;
        private OrgNoteTable.Records orgNoteTableRecords;
        private OrgPersonIdSrcTypeTable.Records orgPersonIdSrcTypeTableRecords;
        private OrgRelationshipTable.Records orgRelationshipTableRecords;
        private OrgRoleDeclarationTable.Records orgRoleDeclarationTableRecords;
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
        
        public final OrgTable.Record get(int i)
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