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
import com.netspective.axiom.schema.column.type.EnumSetColumn;
import com.netspective.axiom.schema.column.type.IntegerColumn;
import auto.dal.db.dao.person.PerEthnicityIdSetTable;
import auto.dal.db.dao.person.PerLanguageIdSetTable;
import auto.dal.db.dao.person.PersonAddressTable;
import auto.dal.db.dao.person.PersonClassificationTable;
import auto.dal.db.dao.person.PersonContactTable;
import auto.dal.db.dao.person.PersonEthnicityTable;
import auto.dal.db.dao.person.PersonFlagTable;
import auto.dal.db.dao.person.PersonIdentifierTable;
import auto.dal.db.dao.person.PersonLanguageTable;
import auto.dal.db.dao.person.PersonLoginTable;
import auto.dal.db.dao.person.PersonNoteTable;
import auto.dal.db.dao.person.PersonRelationshipTable;
import auto.dal.db.dao.person.PersonRoleTable;
import auto.dal.db.dao.person.PersonSessionTable;
import auto.dal.db.dao.person.PersonOrgRelationshipTable;
import auto.dal.db.dao.person.StaffLicenseTable;

public final class PersonTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_CR_STAMP_EQUALITY = 1;
    public static final int ACCESSORID_BY_CR_SESS_ID_EQUALITY = 2;
    public static final int ACCESSORID_BY_REC_STAT_ID_EQUALITY = 3;
    public static final int ACCESSORID_BY_PERSON_ID_EQUALITY = 4;
    public static final int ACCESSORID_BY_NAME_PREFIX_ID_EQUALITY = 5;
    public static final int ACCESSORID_BY_NAME_PREFIX_EQUALITY = 6;
    public static final int ACCESSORID_BY_NAME_FIRST_EQUALITY = 7;
    public static final int ACCESSORID_BY_NAME_MIDDLE_EQUALITY = 8;
    public static final int ACCESSORID_BY_NAME_LAST_EQUALITY = 9;
    public static final int ACCESSORID_BY_NAME_SUFFIX_EQUALITY = 10;
    public static final int ACCESSORID_BY_SHORT_NAME_EQUALITY = 11;
    public static final int ACCESSORID_BY_SIMPLE_NAME_EQUALITY = 12;
    public static final int ACCESSORID_BY_COMPLETE_NAME_EQUALITY = 13;
    public static final int ACCESSORID_BY_SHORT_SORTABLE_NAME_EQUALITY = 14;
    public static final int ACCESSORID_BY_COMPLETE_SORTABLE_NAME_EQUALITY = 15;
    public static final int ACCESSORID_BY_SSN_EQUALITY = 16;
    public static final int ACCESSORID_BY_GENDER_ID_EQUALITY = 17;
    public static final int ACCESSORID_BY_ETHNICITY_ID_EQUALITY = 18;
    public static final int ACCESSORID_BY_LANGUAGE_ID_EQUALITY = 19;
    public static final int ACCESSORID_BY_MARITAL_STATUS_ID_EQUALITY = 20;
    public static final int ACCESSORID_BY_BLOOD_TYPE_ID_EQUALITY = 21;
    public static final int ACCESSORID_BY_BIRTH_DATE_EQUALITY = 22;
    public static final int ACCESSORID_BY_AGE_EQUALITY = 23;
    public static final int COLINDEX_CR_STAMP = 0;
    public static final int COLINDEX_CR_SESS_ID = 1;
    public static final int COLINDEX_REC_STAT_ID = 2;
    public static final int COLINDEX_PERSON_ID = 3;
    public static final int COLINDEX_NAME_PREFIX_ID = 4;
    public static final int COLINDEX_NAME_PREFIX = 5;
    public static final int COLINDEX_NAME_FIRST = 6;
    public static final int COLINDEX_NAME_MIDDLE = 7;
    public static final int COLINDEX_NAME_LAST = 8;
    public static final int COLINDEX_NAME_SUFFIX = 9;
    public static final int COLINDEX_SHORT_NAME = 10;
    public static final int COLINDEX_SIMPLE_NAME = 11;
    public static final int COLINDEX_COMPLETE_NAME = 12;
    public static final int COLINDEX_SHORT_SORTABLE_NAME = 13;
    public static final int COLINDEX_COMPLETE_SORTABLE_NAME = 14;
    public static final int COLINDEX_SSN = 15;
    public static final int COLINDEX_GENDER_ID = 16;
    public static final int COLINDEX_ETHNICITY_ID = 17;
    public static final int COLINDEX_LANGUAGE_ID = 18;
    public static final int COLINDEX_MARITAL_STATUS_ID = 19;
    public static final int COLINDEX_BLOOD_TYPE_ID = 20;
    public static final int COLINDEX_BIRTH_DATE = 21;
    public static final int COLINDEX_AGE = 22;
    
    public PersonTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
        crSessIdForeignKey = table.getColumns().get(COLINDEX_CR_SESS_ID).getForeignKey();
        recStatIdForeignKey = table.getColumns().get(COLINDEX_REC_STAT_ID).getForeignKey();
        namePrefixIdForeignKey = table.getColumns().get(COLINDEX_NAME_PREFIX_ID).getForeignKey();
        genderIdForeignKey = table.getColumns().get(COLINDEX_GENDER_ID).getForeignKey();
        maritalStatusIdForeignKey = table.getColumns().get(COLINDEX_MARITAL_STATUS_ID).getForeignKey();
        bloodTypeIdForeignKey = table.getColumns().get(COLINDEX_BLOOD_TYPE_ID).getForeignKey();
        perEthnicityIdSetTable = new PerEthnicityIdSetTable(schema.getTables().getByName("Per_ethnicity_id_Set"));
        perLanguageIdSetTable = new PerLanguageIdSetTable(schema.getTables().getByName("Per_language_id_Set"));
        personAddressTable = new PersonAddressTable(schema.getTables().getByName("Person_Address"));
        personClassificationTable = new PersonClassificationTable(schema.getTables().getByName("Person_Classification"));
        personContactTable = new PersonContactTable(schema.getTables().getByName("Person_Contact"));
        personEthnicityTable = new PersonEthnicityTable(schema.getTables().getByName("Person_Ethnicity"));
        personFlagTable = new PersonFlagTable(schema.getTables().getByName("Person_Flag"));
        personIdentifierTable = new PersonIdentifierTable(schema.getTables().getByName("Person_Identifier"));
        personLanguageTable = new PersonLanguageTable(schema.getTables().getByName("Person_Language"));
        personLoginTable = new PersonLoginTable(schema.getTables().getByName("Person_Login"));
        personNoteTable = new PersonNoteTable(schema.getTables().getByName("Person_Note"));
        personRelationshipTable = new PersonRelationshipTable(schema.getTables().getByName("Person_Relationship"));
        personRoleTable = new PersonRoleTable(schema.getTables().getByName("Person_Role"));
        personSessionTable = new PersonSessionTable(schema.getTables().getByName("Person_Session"));
        personOrgRelationshipTable = new PersonOrgRelationshipTable(schema.getTables().getByName("PersonOrg_Relationship"));
        staffLicenseTable = new StaffLicenseTable(schema.getTables().getByName("Staff_License"));
    }
    
    public final PersonTable.Record createRecord()
    {
        return new PersonTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByAgeEquality()
    {
        return accessors.get(ACCESSORID_BY_AGE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBirthDateEquality()
    {
        return accessors.get(ACCESSORID_BY_BIRTH_DATE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByBloodTypeIdEquality()
    {
        return accessors.get(ACCESSORID_BY_BLOOD_TYPE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCompleteNameEquality()
    {
        return accessors.get(ACCESSORID_BY_COMPLETE_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCompleteSortableNameEquality()
    {
        return accessors.get(ACCESSORID_BY_COMPLETE_SORTABLE_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrSessIdEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_SESS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByCrStampEquality()
    {
        return accessors.get(ACCESSORID_BY_CR_STAMP_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByEthnicityIdEquality()
    {
        return accessors.get(ACCESSORID_BY_ETHNICITY_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByGenderIdEquality()
    {
        return accessors.get(ACCESSORID_BY_GENDER_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLanguageIdEquality()
    {
        return accessors.get(ACCESSORID_BY_LANGUAGE_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByMaritalStatusIdEquality()
    {
        return accessors.get(ACCESSORID_BY_MARITAL_STATUS_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNameFirstEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_FIRST_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNameLastEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_LAST_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNameMiddleEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_MIDDLE_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNamePrefixEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_PREFIX_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNamePrefixIdEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_PREFIX_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByNameSuffixEquality()
    {
        return accessors.get(ACCESSORID_BY_NAME_SUFFIX_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPersonIdEquality()
    {
        return accessors.get(ACCESSORID_BY_PERSON_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByRecStatIdEquality()
    {
        return accessors.get(ACCESSORID_BY_REC_STAT_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByShortNameEquality()
    {
        return accessors.get(ACCESSORID_BY_SHORT_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByShortSortableNameEquality()
    {
        return accessors.get(ACCESSORID_BY_SHORT_SORTABLE_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySimpleNameEquality()
    {
        return accessors.get(ACCESSORID_BY_SIMPLE_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorBySsnEquality()
    {
        return accessors.get(ACCESSORID_BY_SSN_EQUALITY);
    }
    
    public final PersonTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final IntegerColumn getAgeColumn()
    {
        return (IntegerColumn)table.getColumns().get(COLINDEX_AGE);
    }
    
    public final DateColumn getBirthDateColumn()
    {
        return (DateColumn)table.getColumns().get(COLINDEX_BIRTH_DATE);
    }
    
    public final EnumerationIdRefColumn getBloodTypeIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_BLOOD_TYPE_ID);
    }
    
    public final ForeignKey getBloodTypeIdForeignKey()
    {
        return bloodTypeIdForeignKey;
    }
    
    public final TextColumn getCompleteNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_COMPLETE_NAME);
    }
    
    public final TextColumn getCompleteSortableNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_COMPLETE_SORTABLE_NAME);
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
    
    public final EnumSetColumn getEthnicityIdColumn()
    {
        return (EnumSetColumn)table.getColumns().get(COLINDEX_ETHNICITY_ID);
    }
    
    public final EnumerationIdRefColumn getGenderIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_GENDER_ID);
    }
    
    public final ForeignKey getGenderIdForeignKey()
    {
        return genderIdForeignKey;
    }
    
    public final EnumSetColumn getLanguageIdColumn()
    {
        return (EnumSetColumn)table.getColumns().get(COLINDEX_LANGUAGE_ID);
    }
    
    public final EnumerationIdRefColumn getMaritalStatusIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_MARITAL_STATUS_ID);
    }
    
    public final ForeignKey getMaritalStatusIdForeignKey()
    {
        return maritalStatusIdForeignKey;
    }
    
    public final TextColumn getNameFirstColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME_FIRST);
    }
    
    public final TextColumn getNameLastColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME_LAST);
    }
    
    public final TextColumn getNameMiddleColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME_MIDDLE);
    }
    
    public final TextColumn getNamePrefixColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME_PREFIX);
    }
    
    public final EnumerationIdRefColumn getNamePrefixIdColumn()
    {
        return (EnumerationIdRefColumn)table.getColumns().get(COLINDEX_NAME_PREFIX_ID);
    }
    
    public final ForeignKey getNamePrefixIdForeignKey()
    {
        return namePrefixIdForeignKey;
    }
    
    public final TextColumn getNameSuffixColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_NAME_SUFFIX);
    }
    
    public final PerEthnicityIdSetTable getPerEthnicityIdSetTable()
    {
        return perEthnicityIdSetTable;
    }
    
    public final PerLanguageIdSetTable getPerLanguageIdSetTable()
    {
        return perLanguageIdSetTable;
    }
    
    public final PersonAddressTable getPersonAddressTable()
    {
        return personAddressTable;
    }
    
    public final PersonClassificationTable getPersonClassificationTable()
    {
        return personClassificationTable;
    }
    
    public final PersonContactTable getPersonContactTable()
    {
        return personContactTable;
    }
    
    public final PersonEthnicityTable getPersonEthnicityTable()
    {
        return personEthnicityTable;
    }
    
    public final PersonFlagTable getPersonFlagTable()
    {
        return personFlagTable;
    }
    
    public final AutoIncColumn getPersonIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_PERSON_ID);
    }
    
    public final PersonIdentifierTable getPersonIdentifierTable()
    {
        return personIdentifierTable;
    }
    
    public final PersonLanguageTable getPersonLanguageTable()
    {
        return personLanguageTable;
    }
    
    public final PersonLoginTable getPersonLoginTable()
    {
        return personLoginTable;
    }
    
    public final PersonNoteTable getPersonNoteTable()
    {
        return personNoteTable;
    }
    
    public final PersonOrgRelationshipTable getPersonOrgRelationshipTable()
    {
        return personOrgRelationshipTable;
    }
    
    public final PersonRelationshipTable getPersonRelationshipTable()
    {
        return personRelationshipTable;
    }
    
    public final PersonRoleTable getPersonRoleTable()
    {
        return personRoleTable;
    }
    
    public final PersonSessionTable getPersonSessionTable()
    {
        return personSessionTable;
    }
    
    public final RecordStatusIdColumn getRecStatIdColumn()
    {
        return (RecordStatusIdColumn)table.getColumns().get(COLINDEX_REC_STAT_ID);
    }
    
    public final ForeignKey getRecStatIdForeignKey()
    {
        return recStatIdForeignKey;
    }
    
    public final PersonTable.Record getRecord(Row row)
    {
        return new PersonTable.Record(row);
    }
    
    public final PersonTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long personId, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { personId }, null);
        Record result = row != null ? new PersonTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc, true);
        return result;
    }
    
    public final PersonTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues, boolean retrieveChildren)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new PersonTable.Record(row) : null;
        if(retrieveChildren && result != null) result.retrieveChildren(cc, true);
        return result;
    }
    
    public final TextColumn getShortNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SHORT_NAME);
    }
    
    public final TextColumn getShortSortableNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SHORT_SORTABLE_NAME);
    }
    
    public final TextColumn getSimpleNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SIMPLE_NAME);
    }
    
    public final TextColumn getSsnColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_SSN);
    }
    
    public final StaffLicenseTable getStaffLicenseTable()
    {
        return staffLicenseTable;
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
    private ForeignKey bloodTypeIdForeignKey;
    private ForeignKey crSessIdForeignKey;
    private ForeignKey genderIdForeignKey;
    private ForeignKey maritalStatusIdForeignKey;
    private ForeignKey namePrefixIdForeignKey;
    private PerEthnicityIdSetTable perEthnicityIdSetTable;
    private PerLanguageIdSetTable perLanguageIdSetTable;
    private PersonAddressTable personAddressTable;
    private PersonClassificationTable personClassificationTable;
    private PersonContactTable personContactTable;
    private PersonEthnicityTable personEthnicityTable;
    private PersonFlagTable personFlagTable;
    private PersonIdentifierTable personIdentifierTable;
    private PersonLanguageTable personLanguageTable;
    private PersonLoginTable personLoginTable;
    private PersonNoteTable personNoteTable;
    private PersonOrgRelationshipTable personOrgRelationshipTable;
    private PersonRelationshipTable personRelationshipTable;
    private PersonRoleTable personRoleTable;
    private PersonSessionTable personSessionTable;
    private ForeignKey recStatIdForeignKey;
    private Schema schema;
    private StaffLicenseTable staffLicenseTable;
    private com.netspective.axiom.schema.table.BasicTable table;
    
    public final class Record
    {
        
        public Record(Row row)
        {
            if(row.getTable() != table) throw new ClassCastException("Attempting to assign row from table "+ row.getTable().getName() +" to "+ this.getClass().getName() +" (expecting a row from table "+ table.getName() +").");
            this.row = row;
            this.values = row.getColumnValues();
        }
        
        public final PerEthnicityIdSetTable.Record createPerEthnicityIdSetTableRecord()
        {
            return perEthnicityIdSetTable.createChildLinkedByParentId(this);
        }
        
        public final PerLanguageIdSetTable.Record createPerLanguageIdSetTableRecord()
        {
            return perLanguageIdSetTable.createChildLinkedByParentId(this);
        }
        
        public final PersonAddressTable.Record createPersonAddressTableRecord()
        {
            return personAddressTable.createChildLinkedByParentId(this);
        }
        
        public final PersonClassificationTable.Record createPersonClassificationTableRecord()
        {
            return personClassificationTable.createChildLinkedByPersonId(this);
        }
        
        public final PersonContactTable.Record createPersonContactTableRecord()
        {
            return personContactTable.createChildLinkedByParentId(this);
        }
        
        public final PersonEthnicityTable.Record createPersonEthnicityTableRecord()
        {
            return personEthnicityTable.createChildLinkedByPersonId(this);
        }
        
        public final PersonFlagTable.Record createPersonFlagTableRecord()
        {
            return personFlagTable.createChildLinkedByParentId(this);
        }
        
        public final PersonIdentifierTable.Record createPersonIdentifierTableRecord()
        {
            return personIdentifierTable.createChildLinkedByPersonId(this);
        }
        
        public final PersonLanguageTable.Record createPersonLanguageTableRecord()
        {
            return personLanguageTable.createChildLinkedByPersonId(this);
        }
        
        public final PersonLoginTable.Record createPersonLoginTableRecord()
        {
            return personLoginTable.createChildLinkedByPersonId(this);
        }
        
        public final PersonNoteTable.Record createPersonNoteTableRecord()
        {
            return personNoteTable.createChildLinkedByParentId(this);
        }
        
        public final PersonOrgRelationshipTable.Record createPersonOrgRelationshipTableRecord()
        {
            return personOrgRelationshipTable.createChildLinkedByParentId(this);
        }
        
        public final PersonRelationshipTable.Record createPersonRelationshipTableRecord()
        {
            return personRelationshipTable.createChildLinkedByParentId(this);
        }
        
        public final PersonRoleTable.Record createPersonRoleTableRecord()
        {
            return personRoleTable.createChildLinkedByPersonId(this);
        }
        
        public final PersonSessionTable.Record createPersonSessionTableRecord()
        {
            return personSessionTable.createChildLinkedByPersonId(this);
        }
        
        public final StaffLicenseTable.Record createStaffLicenseTableRecord()
        {
            return staffLicenseTable.createChildLinkedByPersonId(this);
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
        
        public final IntegerColumn.IntegerColumnValue getAge()
        {
            return (IntegerColumn.IntegerColumnValue)values.getByColumnIndex(COLINDEX_AGE);
        }
        
        public final DateColumn.DateColumnValue getBirthDate()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_BIRTH_DATE);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getBloodTypeId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_BLOOD_TYPE_ID);
        }
        
        public final TextColumn.TextColumnValue getCompleteName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_COMPLETE_NAME);
        }
        
        public final TextColumn.TextColumnValue getCompleteSortableName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_COMPLETE_SORTABLE_NAME);
        }
        
        public final TextColumn.TextColumnValue getCrSessId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_CR_SESS_ID);
        }
        
        public final DateColumn.DateColumnValue getCrStamp()
        {
            return (DateColumn.DateColumnValue)values.getByColumnIndex(COLINDEX_CR_STAMP);
        }
        
        public final TextColumn.TextColumnValue getEthnicityId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_ETHNICITY_ID);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getGenderId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_GENDER_ID);
        }
        
        public final TextColumn.TextColumnValue getLanguageId()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_LANGUAGE_ID);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getMaritalStatusId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_MARITAL_STATUS_ID);
        }
        
        public final TextColumn.TextColumnValue getNameFirst()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME_FIRST);
        }
        
        public final TextColumn.TextColumnValue getNameLast()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME_LAST);
        }
        
        public final TextColumn.TextColumnValue getNameMiddle()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME_MIDDLE);
        }
        
        public final TextColumn.TextColumnValue getNamePrefix()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME_PREFIX);
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getNamePrefixId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_NAME_PREFIX_ID);
        }
        
        public final TextColumn.TextColumnValue getNameSuffix()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_NAME_SUFFIX);
        }
        
        public final PerEthnicityIdSetTable.Records getPerEthnicityIdSetTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (perEthnicityIdSetTableRecords != null) return perEthnicityIdSetTableRecords;
            perEthnicityIdSetTableRecords = perEthnicityIdSetTable.getParentRecordsByParentId(this, cc);
            return perEthnicityIdSetTableRecords;
        }
        
        public final PerEthnicityIdSetTable.Records getPerEthnicityIdSetTableRecords()
        {
            return perEthnicityIdSetTableRecords;
        }
        
        public final PerLanguageIdSetTable.Records getPerLanguageIdSetTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (perLanguageIdSetTableRecords != null) return perLanguageIdSetTableRecords;
            perLanguageIdSetTableRecords = perLanguageIdSetTable.getParentRecordsByParentId(this, cc);
            return perLanguageIdSetTableRecords;
        }
        
        public final PerLanguageIdSetTable.Records getPerLanguageIdSetTableRecords()
        {
            return perLanguageIdSetTableRecords;
        }
        
        public final PersonAddressTable.Records getPersonAddressTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personAddressTableRecords != null) return personAddressTableRecords;
            personAddressTableRecords = personAddressTable.getParentRecordsByParentId(this, cc);
            return personAddressTableRecords;
        }
        
        public final PersonAddressTable.Records getPersonAddressTableRecords()
        {
            return personAddressTableRecords;
        }
        
        public final PersonClassificationTable.Records getPersonClassificationTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personClassificationTableRecords != null) return personClassificationTableRecords;
            personClassificationTableRecords = personClassificationTable.getParentRecordsByPersonId(this, cc);
            return personClassificationTableRecords;
        }
        
        public final PersonClassificationTable.Records getPersonClassificationTableRecords()
        {
            return personClassificationTableRecords;
        }
        
        public final PersonContactTable.Records getPersonContactTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personContactTableRecords != null) return personContactTableRecords;
            personContactTableRecords = personContactTable.getParentRecordsByParentId(this, cc);
            return personContactTableRecords;
        }
        
        public final PersonContactTable.Records getPersonContactTableRecords()
        {
            return personContactTableRecords;
        }
        
        public final PersonEthnicityTable.Records getPersonEthnicityTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personEthnicityTableRecords != null) return personEthnicityTableRecords;
            personEthnicityTableRecords = personEthnicityTable.getParentRecordsByPersonId(this, cc);
            return personEthnicityTableRecords;
        }
        
        public final PersonEthnicityTable.Records getPersonEthnicityTableRecords()
        {
            return personEthnicityTableRecords;
        }
        
        public final PersonFlagTable.Records getPersonFlagTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personFlagTableRecords != null) return personFlagTableRecords;
            personFlagTableRecords = personFlagTable.getParentRecordsByParentId(this, cc);
            return personFlagTableRecords;
        }
        
        public final PersonFlagTable.Records getPersonFlagTableRecords()
        {
            return personFlagTableRecords;
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getPersonId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_PERSON_ID);
        }
        
        public final PersonIdentifierTable.Records getPersonIdentifierTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personIdentifierTableRecords != null) return personIdentifierTableRecords;
            personIdentifierTableRecords = personIdentifierTable.getParentRecordsByPersonId(this, cc);
            return personIdentifierTableRecords;
        }
        
        public final PersonIdentifierTable.Records getPersonIdentifierTableRecords()
        {
            return personIdentifierTableRecords;
        }
        
        public final PersonLanguageTable.Records getPersonLanguageTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personLanguageTableRecords != null) return personLanguageTableRecords;
            personLanguageTableRecords = personLanguageTable.getParentRecordsByPersonId(this, cc);
            return personLanguageTableRecords;
        }
        
        public final PersonLanguageTable.Records getPersonLanguageTableRecords()
        {
            return personLanguageTableRecords;
        }
        
        public final PersonLoginTable.Records getPersonLoginTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personLoginTableRecords != null) return personLoginTableRecords;
            personLoginTableRecords = personLoginTable.getParentRecordsByPersonId(this, cc);
            return personLoginTableRecords;
        }
        
        public final PersonLoginTable.Records getPersonLoginTableRecords()
        {
            return personLoginTableRecords;
        }
        
        public final PersonNoteTable.Records getPersonNoteTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personNoteTableRecords != null) return personNoteTableRecords;
            personNoteTableRecords = personNoteTable.getParentRecordsByParentId(this, cc);
            return personNoteTableRecords;
        }
        
        public final PersonNoteTable.Records getPersonNoteTableRecords()
        {
            return personNoteTableRecords;
        }
        
        public final PersonOrgRelationshipTable.Records getPersonOrgRelationshipTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personOrgRelationshipTableRecords != null) return personOrgRelationshipTableRecords;
            personOrgRelationshipTableRecords = personOrgRelationshipTable.getParentRecordsByParentId(this, cc);
            return personOrgRelationshipTableRecords;
        }
        
        public final PersonOrgRelationshipTable.Records getPersonOrgRelationshipTableRecords()
        {
            return personOrgRelationshipTableRecords;
        }
        
        public final PersonRelationshipTable.Records getPersonRelationshipTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personRelationshipTableRecords != null) return personRelationshipTableRecords;
            personRelationshipTableRecords = personRelationshipTable.getParentRecordsByParentId(this, cc);
            return personRelationshipTableRecords;
        }
        
        public final PersonRelationshipTable.Records getPersonRelationshipTableRecords()
        {
            return personRelationshipTableRecords;
        }
        
        public final PersonRoleTable.Records getPersonRoleTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (personRoleTableRecords != null) return personRoleTableRecords;
            personRoleTableRecords = personRoleTable.getParentRecordsByPersonId(this, cc);
            return personRoleTableRecords;
        }
        
        public final PersonRoleTable.Records getPersonRoleTableRecords()
        {
            return personRoleTableRecords;
        }
        
        public final PersonSessionTable.Records getPersonSessionTableRecords(ConnectionContext cc, boolean retrieveChildren)
        throws NamingException, SQLException
        {
            if (personSessionTableRecords != null) return personSessionTableRecords;
            personSessionTableRecords = personSessionTable.getParentRecordsByPersonId(this, cc, retrieveChildren);
            return personSessionTableRecords;
        }
        
        public final PersonSessionTable.Records getPersonSessionTableRecords()
        {
            return personSessionTableRecords;
        }
        
        public final EnumerationIdRefColumn.EnumerationIdRefValue getRecStatId()
        {
            return (EnumerationIdRefColumn.EnumerationIdRefValue)values.getByColumnIndex(COLINDEX_REC_STAT_ID);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final TextColumn.TextColumnValue getShortName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SHORT_NAME);
        }
        
        public final TextColumn.TextColumnValue getShortSortableName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SHORT_SORTABLE_NAME);
        }
        
        public final TextColumn.TextColumnValue getSimpleName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SIMPLE_NAME);
        }
        
        public final TextColumn.TextColumnValue getSsn()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_SSN);
        }
        
        public final StaffLicenseTable.Records getStaffLicenseTableRecords(ConnectionContext cc)
        throws NamingException, SQLException
        {
            if (staffLicenseTableRecords != null) return staffLicenseTableRecords;
            staffLicenseTableRecords = staffLicenseTable.getParentRecordsByPersonId(this, cc);
            return staffLicenseTableRecords;
        }
        
        public final StaffLicenseTable.Records getStaffLicenseTableRecords()
        {
            return staffLicenseTableRecords;
        }
        
        public final auto.dal.db.vo.Person getValues()
        {
            return getValues(new auto.dal.db.vo.impl.PersonVO());
        }
        
        public final auto.dal.db.vo.Person getValues(auto.dal.db.vo.Person valueObject)
        {
            valueObject.setCrStamp((java.util.Date) values.getByColumnIndex(COLINDEX_CR_STAMP).getValue());
            valueObject.setCrSessId((java.lang.String) values.getByColumnIndex(COLINDEX_CR_SESS_ID).getValue());
            valueObject.setRecStatId((java.lang.Integer) values.getByColumnIndex(COLINDEX_REC_STAT_ID).getValue());
            Object autoIncPersonIdValue = values.getByColumnIndex(COLINDEX_PERSON_ID).getValue();
            valueObject.setPersonId(autoIncPersonIdValue instanceof Integer ? new Long(((Integer) autoIncPersonIdValue).intValue()) : (Long) autoIncPersonIdValue);
            valueObject.setNamePrefixId((java.lang.Integer) values.getByColumnIndex(COLINDEX_NAME_PREFIX_ID).getValue());
            valueObject.setNamePrefix((java.lang.String) values.getByColumnIndex(COLINDEX_NAME_PREFIX).getValue());
            valueObject.setNameFirst((java.lang.String) values.getByColumnIndex(COLINDEX_NAME_FIRST).getValue());
            valueObject.setNameMiddle((java.lang.String) values.getByColumnIndex(COLINDEX_NAME_MIDDLE).getValue());
            valueObject.setNameLast((java.lang.String) values.getByColumnIndex(COLINDEX_NAME_LAST).getValue());
            valueObject.setNameSuffix((java.lang.String) values.getByColumnIndex(COLINDEX_NAME_SUFFIX).getValue());
            valueObject.setShortName((java.lang.String) values.getByColumnIndex(COLINDEX_SHORT_NAME).getValue());
            valueObject.setSimpleName((java.lang.String) values.getByColumnIndex(COLINDEX_SIMPLE_NAME).getValue());
            valueObject.setCompleteName((java.lang.String) values.getByColumnIndex(COLINDEX_COMPLETE_NAME).getValue());
            valueObject.setShortSortableName((java.lang.String) values.getByColumnIndex(COLINDEX_SHORT_SORTABLE_NAME).getValue());
            valueObject.setCompleteSortableName((java.lang.String) values.getByColumnIndex(COLINDEX_COMPLETE_SORTABLE_NAME).getValue());
            valueObject.setSsn((java.lang.String) values.getByColumnIndex(COLINDEX_SSN).getValue());
            valueObject.setGenderId((java.lang.Integer) values.getByColumnIndex(COLINDEX_GENDER_ID).getValue());
            valueObject.setEthnicityId((java.lang.String) values.getByColumnIndex(COLINDEX_ETHNICITY_ID).getValue());
            valueObject.setLanguageId((java.lang.String) values.getByColumnIndex(COLINDEX_LANGUAGE_ID).getValue());
            valueObject.setMaritalStatusId((java.lang.Integer) values.getByColumnIndex(COLINDEX_MARITAL_STATUS_ID).getValue());
            valueObject.setBloodTypeId((java.lang.Integer) values.getByColumnIndex(COLINDEX_BLOOD_TYPE_ID).getValue());
            valueObject.setBirthDate((java.util.Date) values.getByColumnIndex(COLINDEX_BIRTH_DATE).getValue());
            valueObject.setAge((java.lang.Integer) values.getByColumnIndex(COLINDEX_AGE).getValue());
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
        
        public final void retrieveChildren(ConnectionContext cc, boolean retrieveGrandchildren)
        throws NamingException, SQLException
        {
            perEthnicityIdSetTableRecords = getPerEthnicityIdSetTableRecords(cc);
            perLanguageIdSetTableRecords = getPerLanguageIdSetTableRecords(cc);
            personAddressTableRecords = getPersonAddressTableRecords(cc);
            personClassificationTableRecords = getPersonClassificationTableRecords(cc);
            personContactTableRecords = getPersonContactTableRecords(cc);
            personEthnicityTableRecords = getPersonEthnicityTableRecords(cc);
            personFlagTableRecords = getPersonFlagTableRecords(cc);
            personIdentifierTableRecords = getPersonIdentifierTableRecords(cc);
            personLanguageTableRecords = getPersonLanguageTableRecords(cc);
            personLoginTableRecords = getPersonLoginTableRecords(cc);
            personNoteTableRecords = getPersonNoteTableRecords(cc);
            personRelationshipTableRecords = getPersonRelationshipTableRecords(cc);
            personRoleTableRecords = getPersonRoleTableRecords(cc);
            personSessionTableRecords = getPersonSessionTableRecords(cc, retrieveGrandchildren);
            personOrgRelationshipTableRecords = getPersonOrgRelationshipTableRecords(cc);
            staffLicenseTableRecords = getStaffLicenseTableRecords(cc);
        }
        
        public final void setAge(com.netspective.commons.value.Value value)
        {
            getAge().copyValueByReference(value);
        }
        
        public final void setBirthDate(com.netspective.commons.value.Value value)
        {
            getBirthDate().copyValueByReference(value);
        }
        
        public final void setBloodTypeId(com.netspective.commons.value.Value value)
        {
            getBloodTypeId().copyValueByReference(value);
        }
        
        public final void setCompleteName(com.netspective.commons.value.Value value)
        {
            getCompleteName().copyValueByReference(value);
        }
        
        public final void setCompleteSortableName(com.netspective.commons.value.Value value)
        {
            getCompleteSortableName().copyValueByReference(value);
        }
        
        public final void setCrSessId(com.netspective.commons.value.Value value)
        {
            getCrSessId().copyValueByReference(value);
        }
        
        public final void setCrStamp(com.netspective.commons.value.Value value)
        {
            getCrStamp().copyValueByReference(value);
        }
        
        public final void setEthnicityId(com.netspective.commons.value.Value value)
        {
            getEthnicityId().copyValueByReference(value);
        }
        
        public final void setGenderId(com.netspective.commons.value.Value value)
        {
            getGenderId().copyValueByReference(value);
        }
        
        public final void setLanguageId(com.netspective.commons.value.Value value)
        {
            getLanguageId().copyValueByReference(value);
        }
        
        public final void setMaritalStatusId(com.netspective.commons.value.Value value)
        {
            getMaritalStatusId().copyValueByReference(value);
        }
        
        public final void setNameFirst(com.netspective.commons.value.Value value)
        {
            getNameFirst().copyValueByReference(value);
        }
        
        public final void setNameLast(com.netspective.commons.value.Value value)
        {
            getNameLast().copyValueByReference(value);
        }
        
        public final void setNameMiddle(com.netspective.commons.value.Value value)
        {
            getNameMiddle().copyValueByReference(value);
        }
        
        public final void setNamePrefix(com.netspective.commons.value.Value value)
        {
            getNamePrefix().copyValueByReference(value);
        }
        
        public final void setNamePrefixId(com.netspective.commons.value.Value value)
        {
            getNamePrefixId().copyValueByReference(value);
        }
        
        public final void setNameSuffix(com.netspective.commons.value.Value value)
        {
            getNameSuffix().copyValueByReference(value);
        }
        
        public final void setPersonId(com.netspective.commons.value.Value value)
        {
            getPersonId().copyValueByReference(value);
        }
        
        public final void setRecStatId(com.netspective.commons.value.Value value)
        {
            getRecStatId().copyValueByReference(value);
        }
        
        public final void setShortName(com.netspective.commons.value.Value value)
        {
            getShortName().copyValueByReference(value);
        }
        
        public final void setShortSortableName(com.netspective.commons.value.Value value)
        {
            getShortSortableName().copyValueByReference(value);
        }
        
        public final void setSimpleName(com.netspective.commons.value.Value value)
        {
            getSimpleName().copyValueByReference(value);
        }
        
        public final void setSsn(com.netspective.commons.value.Value value)
        {
            getSsn().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Person valueObject)
        {
            values.getByColumnIndex(COLINDEX_CR_STAMP).setValue(valueObject.getCrStamp());
            values.getByColumnIndex(COLINDEX_CR_SESS_ID).setValue(valueObject.getCrSessId());
            values.getByColumnIndex(COLINDEX_REC_STAT_ID).setValue(valueObject.getRecStatId());
            values.getByColumnIndex(COLINDEX_PERSON_ID).setValue(valueObject.getPersonId());
            values.getByColumnIndex(COLINDEX_NAME_PREFIX_ID).setValue(valueObject.getNamePrefixId());
            values.getByColumnIndex(COLINDEX_NAME_PREFIX).setValue(valueObject.getNamePrefix());
            values.getByColumnIndex(COLINDEX_NAME_FIRST).setValue(valueObject.getNameFirst());
            values.getByColumnIndex(COLINDEX_NAME_MIDDLE).setValue(valueObject.getNameMiddle());
            values.getByColumnIndex(COLINDEX_NAME_LAST).setValue(valueObject.getNameLast());
            values.getByColumnIndex(COLINDEX_NAME_SUFFIX).setValue(valueObject.getNameSuffix());
            values.getByColumnIndex(COLINDEX_SHORT_NAME).setValue(valueObject.getShortName());
            values.getByColumnIndex(COLINDEX_SIMPLE_NAME).setValue(valueObject.getSimpleName());
            values.getByColumnIndex(COLINDEX_COMPLETE_NAME).setValue(valueObject.getCompleteName());
            values.getByColumnIndex(COLINDEX_SHORT_SORTABLE_NAME).setValue(valueObject.getShortSortableName());
            values.getByColumnIndex(COLINDEX_COMPLETE_SORTABLE_NAME).setValue(valueObject.getCompleteSortableName());
            values.getByColumnIndex(COLINDEX_SSN).setValue(valueObject.getSsn());
            values.getByColumnIndex(COLINDEX_GENDER_ID).setValue(valueObject.getGenderId());
            values.getByColumnIndex(COLINDEX_ETHNICITY_ID).setValue(valueObject.getEthnicityId());
            values.getByColumnIndex(COLINDEX_LANGUAGE_ID).setValue(valueObject.getLanguageId());
            values.getByColumnIndex(COLINDEX_MARITAL_STATUS_ID).setValue(valueObject.getMaritalStatusId());
            values.getByColumnIndex(COLINDEX_BLOOD_TYPE_ID).setValue(valueObject.getBloodTypeId());
            values.getByColumnIndex(COLINDEX_BIRTH_DATE).setValue(valueObject.getBirthDate());
            values.getByColumnIndex(COLINDEX_AGE).setValue(valueObject.getAge());
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
        private PerEthnicityIdSetTable.Records perEthnicityIdSetTableRecords;
        private PerLanguageIdSetTable.Records perLanguageIdSetTableRecords;
        private PersonAddressTable.Records personAddressTableRecords;
        private PersonClassificationTable.Records personClassificationTableRecords;
        private PersonContactTable.Records personContactTableRecords;
        private PersonEthnicityTable.Records personEthnicityTableRecords;
        private PersonFlagTable.Records personFlagTableRecords;
        private PersonIdentifierTable.Records personIdentifierTableRecords;
        private PersonLanguageTable.Records personLanguageTableRecords;
        private PersonLoginTable.Records personLoginTableRecords;
        private PersonNoteTable.Records personNoteTableRecords;
        private PersonOrgRelationshipTable.Records personOrgRelationshipTableRecords;
        private PersonRelationshipTable.Records personRelationshipTableRecords;
        private PersonRoleTable.Records personRoleTableRecords;
        private PersonSessionTable.Records personSessionTableRecords;
        private Row row;
        private StaffLicenseTable.Records staffLicenseTableRecords;
        private ColumnValues values;
    }
    
    public final class Records
    {
        
        public Records(Rows rows)
        {
            this.rows = rows;
            this.cache = new Record[rows.size()];
        }
        
        public final PersonTable.Record get(int i)
        {
            if(cache[i] == null) cache[i] = new Record(rows.getRow(i));
            return cache[i];
        }
        
        public final void retrieveChildren(ConnectionContext cc, boolean retrieveGrandchildren)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++) get(i).retrieveChildren(cc, retrieveGrandchildren);
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