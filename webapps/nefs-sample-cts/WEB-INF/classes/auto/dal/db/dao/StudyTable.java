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
import auto.dal.db.dao.study.StudyOrgRelationshipTable;
import auto.dal.db.dao.study.StudyPersonRelationshipTable;

public final class StudyTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_CR_STAMP_EQUALITY = 1;
    public static final int ACCESSORID_BY_CR_SESS_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_REC_STAT_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_STUDY_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_STUDY_CODE_EQUALITY = 5;
    public static final int ACCESSORID_BY_STUDY_NAME_EQUALITY = 6;
    public static final int ACCESSORID_BY_STUDY_DESCR_EQUALITY = 7;
    public static final int ACCESSORID_BY_STUDY_STATUS_EQUALITY = 8;
    public static final int ACCESSORID_BY_STUDY_STAGE_EQUALITY = 9;
    public static final int ACCESSORID_BY_START_DATE_EQUALITY = 10;
    public static final int ACCESSORID_BY_TARGET_END_DATE_EQUALITY = 11;
    public static final int ACCESSORID_BY_ACTUAL_END_DATE_EQUALITY = 12;
    public static final int ACCESSORID_BY_IRB_NAME_EQUALITY = 13;
    public static final int ACCESSORID_BY_IRB_NUMBER_EQUALITY = 14;
    public static final int ACCESSORID_BY_IRB_APPROVAL_DATE_EQUALITY = 15;
    public static final int ACCESSORID_BY_IRB_EXPIRATION_DATE_EQUALITY = 16;
    public static final int COLINDEX_CR_STAMP = 0;
    public static final int COLINDEX_CR_SESS_ID = 1;
    public static final int COLINDEX_REC_STAT_ID = 2;
    public static final int COLINDEX_STUDY_ID = 3;
    public static final int COLINDEX_STUDY_CODE = 4;
    public static final int COLINDEX_STUDY_NAME = 5;
    public static final int COLINDEX_STUDY_DESCR = 6;
    public static final int COLINDEX_STUDY_STATUS = 7;
    public static final int COLINDEX_STUDY_STAGE = 8;
    public static final int COLINDEX_START_DATE = 9;
    public static final int COLINDEX_TARGET_END_DATE = 10;
    public static final int COLINDEX_ACTUAL_END_DATE = 11;
    public static final int COLINDEX_IRB_NAME = 12;
    public static final int COLINDEX_IRB_NUMBER = 13;
    public static final int COLINDEX_IRB_APPROVAL_DATE = 14;
    public static final int COLINDEX_IRB_EXPIRATION_DATE = 15;
    
    public StudyTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        crSessIdForeignKey = table.getColumns().get(COLINDEX_CR_SESS_ID).getForeignKey();
        recStatIdForeignKey = table.getColumns().get(COLINDEX_REC_STAT_ID).getForeignKey();
        studyStatusForeignKey = table.getColumns().get(COLINDEX_STUDY_STATUS).getForeignKey();
        studyStageForeignKey = table.getColumns().get(COLINDEX_STUDY_STAGE).getForeignKey();
        studyOrgRelationshipTable = new StudyOrgRelationshipTable(schema.getTables().getByName("StudyOrg_Relationship"));
        studyPersonRelationshipTable = new StudyPersonRelationshipTable(schema.getTables().getByName("StudyPerson_Relationship"));
    }
    
    public final StudyTable.Record createRecord()
    {
        return new StudyTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByActualEndDateEquality()
    {
        return accessors.get(ACCESSORID_BY_ACTUAL_END_DATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrSessIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_SESS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrStampEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIrbApprovalDateEquality()
    {
        return accessors.get(ACCESSORID_BY_IRB_APPROVAL_DATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIrbExpirationDateEquality()
    {
        return accessors.get(ACCESSORID_BY_IRB_EXPIRATION_DATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIrbNameEquality()
    {
        return accessors.get(ACCESSORID_BY_IRB_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByIrbNumberEquality()
    {
        return accessors.get(ACCESSORID_BY_IRB_NUMBER_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRecStatIdEquality()
    {
        return accessors.get(ACCESSORID_BY_REC_STAT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStartDateEquality()
    {
        return accessors.get(ACCESSORID_BY_START_DATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStudyCodeEquality()
    {
        return accessors.get(ACCESSORID_BY_STUDY_CODE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStudyDescrEquality()
    {
        return accessors.get(ACCESSORID_BY_STUDY_DESCR_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStudyIdEquality()
    {
        return accessors.get(ACCESSORID_BY_STUDY_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStudyNameEquality()
    {
        return accessors.get(ACCESSORID_BY_STUDY_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStudyStageEquality()
    {
        return accessors.get(ACCESSORID_BY_STUDY_STAGE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByStudyStatusEquality()
    {
        return accessors.get(ACCESSORID_BY_STUDY_STATUS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByTargetEndDateEquality()
    {
        return accessors.get(ACCESSORID_BY_TARGET_END_DATE_EQUALITY);
    }
    
    public final StudyTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final DateColumn getActualEndDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_ACTUAL_END_DATE);
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
    
    public final DateColumn getIrbApprovalDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_IRB_APPROVAL_DATE);
    }
    
    public final DateColumn getIrbExpirationDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_IRB_EXPIRATION_DATE);
    }
    
    public final TextColumn getIrbNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_IRB_NAME);
    }
    
    public final TextColumn getIrbNumberColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_IRB_NUMBER);
    }
    
    public final RecordStatusIdColumn getRecStatIdColumn()
    {
        return (RecordStatusIdColumn)table.getColumns().get(COLINDEX_REC_STAT_ID);
    }
    
    public final ForeignKey getRecStatIdForeignKey()
    {
        return recStatIdForeignKey;
    }
    
    public final StudyTable.Record getRecord(Row row)
    {
        return new StudyTable.Record(row);
    }
    
    public final StudyTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long studyId, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { studyId }, null);
        Record result = row != null ? new StudyTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final StudyTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new StudyTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc);
        return result;
    }
    
    public final DateColumn getStartDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_START_DATE);
    }
    
    public final TextColumn getStudyCodeColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_STUDY_CODE);
    }
    
    public final TextColumn getStudyDescrColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_STUDY_DESCR);
    }
    
    public final AutoIncColumn getStudyIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_STUDY_ID);
    }
    
    public final TextColumn getStudyNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_STUDY_NAME);
    }
    
    public final StudyOrgRelationshipTable getStudyOrgRelationshipTable()
    {
        return studyOrgRelationshipTable;
    }
    
    public final StudyPersonRelationshipTable getStudyPersonRelationshipTable()
    {
        return studyPersonRelationshipTable;
    }
    
    public final EnumerationIdRefColumn getStudyStageColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_STUDY_STAGE);
    }
    
    public final ForeignKey getStudyStageForeignKey()
    {
        return studyStageForeignKey;
    }
    
    public final RecordStatusIdColumn getStudyStatusColumn()
    {
        return (RecordStatusIdColumn)table.getColumns().get(COLINDEX_STUDY_STATUS);
    }
    
    public final ForeignKey getStudyStatusForeignKey()
    {
        return studyStatusForeignKey;
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final DateColumn getTargetEndDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_TARGET_END_DATE);
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private ForeignKey crSessIdForeignKey;
    private ForeignKey recStatIdForeignKey;
    private Schema schema;
    private StudyOrgRelationshipTable studyOrgRelationshipTable;
    private StudyPersonRelationshipTable studyPersonRelationshipTable;
    private ForeignKey studyStageForeignKey;
    private ForeignKey studyStatusForeignKey;
    private com.netspective.axiom.schema.table.BasicTable table;
    
    public final class Record
    {
        
        public Record(Row row)
        {
            if(row.getTable() != table) throw new ClassCastException("Attempting to assign row from table "+ row.getTable().getName() +" to "+ this.getClass().getName() +" (expecting a row from table "+ table.getName() +").");
            this.row = row;
            this.values = row.getColumnValues();
        }
        
        public final StudyOrgRelationshipTable.Record createStudyOrgRelationshipTableRecord()
        {
            return studyOrgRelationshipTable.createChildLinkedByParentId(this);
        }
        
        public final StudyPersonRelationshipTable.Record createStudyPersonRelationshipTableRecord()
        {
            return studyPersonRelationshipTable.createChildLinkedByParentId(this);
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
        
        public final DateColumn.DateColumnValue getActualEndDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_ACTUAL_END_DATE);
        }
        
        public final TextColumn.TextColumnValue getCrSessId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CR_SESS_ID);
        }
        
        public final DateColumn.DateColumnValue getCrStamp()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_CR_STAMP);
        }
        
        public final DateColumn.DateColumnValue getIrbApprovalDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_IRB_APPROVAL_DATE);
        }
        
        public final DateColumn.DateColumnValue getIrbExpirationDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_IRB_EXPIRATION_DATE);
        }
        
        public final TextColumn.TextColumnValue getIrbName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_IRB_NAME);
        }
        
        public final TextColumn.TextColumnValue getIrbNumber()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_IRB_NUMBER);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRecStatId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REC_STAT_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final DateColumn.DateColumnValue getStartDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_START_DATE);
        }
        
        public final TextColumn.TextColumnValue getStudyCode()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_STUDY_CODE);
        }
        
        public final TextColumn.TextColumnValue getStudyDescr()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_STUDY_DESCR);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getStudyId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_STUDY_ID);
        }
        
        public final TextColumn.TextColumnValue getStudyName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_STUDY_NAME);
        }
        
        public final StudyOrgRelationshipTable.Records getStudyOrgRelationshipTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (studyOrgRelationshipTableRecords != null) return studyOrgRelationshipTableRecords;
            studyOrgRelationshipTableRecords = studyOrgRelationshipTable.getParentRecordsByParentId(this, cc);
            return studyOrgRelationshipTableRecords;
        }
        
        public final StudyOrgRelationshipTable.Records getStudyOrgRelationshipTableRecords()
        {
            return studyOrgRelationshipTableRecords;
        }
        
        public final StudyPersonRelationshipTable.Records getStudyPersonRelationshipTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (studyPersonRelationshipTableRecords != null) return studyPersonRelationshipTableRecords;
            studyPersonRelationshipTableRecords = studyPersonRelationshipTable.getParentRecordsByParentId(this, cc);
            return studyPersonRelationshipTableRecords;
        }
        
        public final StudyPersonRelationshipTable.Records getStudyPersonRelationshipTableRecords()
        {
            return studyPersonRelationshipTableRecords;
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getStudyStage()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_STUDY_STAGE);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getStudyStatus()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_STUDY_STATUS);
        }
        
        public final DateColumn.DateColumnValue getTargetEndDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_TARGET_END_DATE);
        }
        
        public final auto.dal.db.vo.Study getValues()
        {
            return getValues(new auto.dal.db.vo.impl.StudyVO());
        }
        
        public final auto.dal.db.vo.Study getValues(auto.dal.db.vo.Study valueObject)
        {
            valueObject.setCrStamp((java.util.Date) values.getByColumnIndex(COLINDEX_CR_STAMP).getValue());
            valueObject.setCrSessId((java.lang.String) values.getByColumnIndex(COLINDEX_CR_SESS_ID).getValue());
            valueObject.setRecStatId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REC_STAT_ID).getValue());
            Object autoIncStudyIdValue = values.getByColumnIndex(COLINDEX_STUDY_ID).getValue();
            valueObject.setStudyId(autoIncStudyIdValue instanceof Integer ? new Long(((Integer) autoIncStudyIdValue).intValue()) : (Long) autoIncStudyIdValue);
            valueObject.setStudyCode((java.lang.String) values.getByColumnIndex(COLINDEX_STUDY_CODE).getValue());
            valueObject.setStudyName((java.lang.String) values.getByColumnIndex(COLINDEX_STUDY_NAME).getValue());
            valueObject.setStudyDescr((java.lang.String) values.getByColumnIndex(COLINDEX_STUDY_DESCR).getValue());
            valueObject.setStudyStatus((java.lang.Integer) values.getByColumnIndex(COLINDEX_STUDY_STATUS).getValue());
            valueObject.setStudyStage((java.lang.Integer) values.getByColumnIndex(COLINDEX_STUDY_STAGE).getValue());
            valueObject.setStartDate((java.util.Date) values.getByColumnIndex(COLINDEX_START_DATE).getValue());
            valueObject.setTargetEndDate((java.util.Date) values.getByColumnIndex(COLINDEX_TARGET_END_DATE).getValue());
            valueObject.setActualEndDate((java.util.Date) values.getByColumnIndex(COLINDEX_ACTUAL_END_DATE).getValue());
            valueObject.setIrbName((java.lang.String) values.getByColumnIndex(COLINDEX_IRB_NAME).getValue());
            valueObject.setIrbNumber((java.lang.String) values.getByColumnIndex(COLINDEX_IRB_NUMBER).getValue());
            valueObject.setIrbApprovalDate((java.util.Date) values.getByColumnIndex(COLINDEX_IRB_APPROVAL_DATE).getValue());
            valueObject.setIrbExpirationDate((java.util.Date) values.getByColumnIndex(COLINDEX_IRB_EXPIRATION_DATE).getValue());
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
            studyOrgRelationshipTableRecords = getStudyOrgRelationshipTableRecords(cc);
            studyPersonRelationshipTableRecords = getStudyPersonRelationshipTableRecords(cc);
        }
        
        public final void setActualEndDate(com.netspective.commons.value.Value value)
        {
            getActualEndDate().copyValueByReference(value);
        }
        
        public final void setCrSessId(com.netspective.commons.value.Value value)
        {
            getCrSessId().copyValueByReference(value);
        }
        
        public final void setCrStamp(com.netspective.commons.value.Value value)
        {
            getCrStamp().copyValueByReference(value);
        }
        
        public final void setIrbApprovalDate(com.netspective.commons.value.Value value)
        {
            getIrbApprovalDate().copyValueByReference(value);
        }
        
        public final void setIrbExpirationDate(com.netspective.commons.value.Value value)
        {
            getIrbExpirationDate().copyValueByReference(value);
        }
        
        public final void setIrbName(com.netspective.commons.value.Value value)
        {
            getIrbName().copyValueByReference(value);
        }
        
        public final void setIrbNumber(com.netspective.commons.value.Value value)
        {
            getIrbNumber().copyValueByReference(value);
        }
        
        public final void setRecStatId(com.netspective.commons.value.Value value)
        {
            getRecStatId().copyValueByReference(value);
        }
        
        public final void setStartDate(com.netspective.commons.value.Value value)
        {
            getStartDate().copyValueByReference(value);
        }
        
        public final void setStudyCode(com.netspective.commons.value.Value value)
        {
            getStudyCode().copyValueByReference(value);
        }
        
        public final void setStudyDescr(com.netspective.commons.value.Value value)
        {
            getStudyDescr().copyValueByReference(value);
        }
        
        public final void setStudyId(com.netspective.commons.value.Value value)
        {
            getStudyId().copyValueByReference(value);
        }
        
        public final void setStudyName(com.netspective.commons.value.Value value)
        {
            getStudyName().copyValueByReference(value);
        }
        
        public final void setStudyStage(com.netspective.commons.value.Value value)
        {
            getStudyStage().copyValueByReference(value);
        }
        
        public final void setStudyStatus(com.netspective.commons.value.Value value)
        {
            getStudyStatus().copyValueByReference(value);
        }
        
        public final void setTargetEndDate(com.netspective.commons.value.Value value)
        {
            getTargetEndDate().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Study valueObject)
        {
            values.getByColumnIndex(COLINDEX_CR_STAMP).setValue(valueObject.getCrStamp());
            values.getByColumnIndex(COLINDEX_CR_SESS_ID).setValue(valueObject.getCrSessId());
            values.getByColumnIndex(COLINDEX_REC_STAT_ID).setValue(valueObject.getRecStatId());
            values.getByColumnIndex(COLINDEX_STUDY_ID).setValue(valueObject.getStudyId());
            values.getByColumnIndex(COLINDEX_STUDY_CODE).setValue(valueObject.getStudyCode());
            values.getByColumnIndex(COLINDEX_STUDY_NAME).setValue(valueObject.getStudyName());
            values.getByColumnIndex(COLINDEX_STUDY_DESCR).setValue(valueObject.getStudyDescr());
            values.getByColumnIndex(COLINDEX_STUDY_STATUS).setValue(valueObject.getStudyStatus());
            values.getByColumnIndex(COLINDEX_STUDY_STAGE).setValue(valueObject.getStudyStage());
            values.getByColumnIndex(COLINDEX_START_DATE).setValue(valueObject.getStartDate());
            values.getByColumnIndex(COLINDEX_TARGET_END_DATE).setValue(valueObject.getTargetEndDate());
            values.getByColumnIndex(COLINDEX_ACTUAL_END_DATE).setValue(valueObject.getActualEndDate());
            values.getByColumnIndex(COLINDEX_IRB_NAME).setValue(valueObject.getIrbName());
            values.getByColumnIndex(COLINDEX_IRB_NUMBER).setValue(valueObject.getIrbNumber());
            values.getByColumnIndex(COLINDEX_IRB_APPROVAL_DATE).setValue(valueObject.getIrbApprovalDate());
            values.getByColumnIndex(COLINDEX_IRB_EXPIRATION_DATE).setValue(valueObject.getIrbExpirationDate());
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
        private StudyOrgRelationshipTable.Records studyOrgRelationshipTableRecords;
        private StudyPersonRelationshipTable.Records studyPersonRelationshipTableRecords;
        private ColumnValues values;
    }
    
    public final class Records
    {
        
        public Records(Rows rows)
        {
            this.rows = rows;
            this.cache = new Record[rows.size()];
        }
        
        public final StudyTable.Record get(int i)
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