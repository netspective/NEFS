CREATE TABLE Lookup_Result_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Lookup_Result_Type on Lookup_Result_Type (id);

CREATE TABLE Record_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_RecStatus_abbrev on Record_Status (abbrev);
CREATE  INDEX PK_Record_Status on Record_Status (id);

CREATE TABLE Record_State
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Record_State on Record_State (id);

CREATE TABLE Contact_Method_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Contact_Method_Type on Contact_Method_Type (id);

CREATE TABLE Contact_Address_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Contact_Address_Type on Contact_Address_Type (id);

CREATE TABLE Contact_Telephone_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Contact_Telephone_Type on Contact_Telephone_Type (id);

CREATE TABLE Contact_Email_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Contact_Email_Type on Contact_Email_Type (id);

CREATE TABLE Action_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Action_Type on Action_Type (id);

CREATE TABLE Patient_Comm_Act_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Patient_Comm_Act_Type on Patient_Comm_Act_Type (id);

CREATE TABLE Patient_Comm_Status_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Patient_Comm_Status_Type on Patient_Comm_Status_Type (id);

CREATE TABLE Provider_Comm_Act_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Provider_Comm_Act_Type on Provider_Comm_Act_Type (id);

CREATE TABLE Provider_Comm_Status_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Provider_Comm_Status_Type on Provider_Comm_Status_Type (id);

CREATE TABLE Directive_Act_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Directive_Act_Type on Directive_Act_Type (id);

CREATE TABLE Diag_Term_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Diag_Term_Type on Diag_Term_Type (id);

CREATE TABLE Proc_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Proc_Type on Proc_Type (id);

CREATE TABLE Artifact_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Artifact_Type on Artifact_Type (id);

CREATE TABLE Artifact_Event_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Artifact_Event_Type on Artifact_Event_Type (id);

CREATE TABLE Artifact_Source_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Artifact_Source_Type on Artifact_Source_Type (id);

CREATE TABLE Artifact_Association_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Artifact_Association_Type on Artifact_Association_Type (id);

CREATE TABLE Claim_Table_Field_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Claim_Table_Field_Type on Claim_Table_Field_Type (id);

CREATE TABLE Claim_Status_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Claim_Status_Type on Claim_Status_Type (id);

CREATE TABLE Claim_Diagnosis_Code_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Claim_Diagnosis_Code_Type on Claim_Diagnosis_Code_Type (id);

CREATE TABLE Claim_Procedure_Code_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Claim_Procedure_Code_Type on Claim_Procedure_Code_Type (id);

CREATE TABLE Document_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Document_Type on Document_Type (id);

CREATE TABLE Document_Mime_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Document_Mime_Type on Document_Mime_Type (id);

CREATE TABLE HCFA1500_Service_Place_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_HCFA1500_Service_Place_Type on HCFA1500_Service_Place_Type (id);

CREATE TABLE Procedure_Code_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Procedure_Code_Type on Procedure_Code_Type (id);

CREATE TABLE Bill_Remittance_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Bill_Remittance_Type on Bill_Remittance_Type (id);

CREATE TABLE Bill_Sequence_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Bill_Sequence_Type on Bill_Sequence_Type (id);

CREATE TABLE Ins_Coverage_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Ins_Coverage_Type on Ins_Coverage_Type (id);

CREATE TABLE Ins_Product_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Ins_Product_Type on Ins_Product_Type (id);

CREATE TABLE Insured_Relationship
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Insured_Relationship on Insured_Relationship (id);

CREATE TABLE Invoice_Status_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Invoice_Status_Type on Invoice_Status_Type (id);

CREATE TABLE Invoice_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Invoice_Type on Invoice_Type (id);

CREATE TABLE Doctor_Visit_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Doctor_Visit_Type on Doctor_Visit_Type (id);

CREATE TABLE Lab_Order_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Lab_Order_Status on Lab_Order_Status (id);

CREATE TABLE Lab_Order_Priority
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Lab_Order_Priority on Lab_Order_Priority (id);

CREATE TABLE Lab_Order_Transmission
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Lab_Order_Transmission on Lab_Order_Transmission (id);

CREATE TABLE Medication_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Medication_Type on Medication_Type (id);

CREATE TABLE Medication_Record_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Medication_Record_Type on Medication_Record_Type (id);

CREATE TABLE Referral_Communication
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Referral_Communication on Referral_Communication (id);

CREATE TABLE Referral_Urgency
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Referral_Urgency on Referral_Urgency (id);

CREATE TABLE Referral_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Referral_Status on Referral_Status (id);

CREATE TABLE Referral_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Referral_Type on Referral_Type (id);

CREATE TABLE Message_Attachment_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Message_Attachment_Type on Message_Attachment_Type (id);

CREATE TABLE Message_Recipient_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Message_Recipient_Type on Message_Recipient_Type (id);

CREATE TABLE Message_Reception_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Message_Reception_Type on Message_Reception_Type (id);

CREATE TABLE Table_Activity_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Table_Activity_Type on Table_Activity_Type (id);

CREATE TABLE Field_Change_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Field_Change_Type on Field_Change_Type (id);

CREATE TABLE Measurement_Unit_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Measurement_Unit_Type on Measurement_Unit_Type (id);

CREATE TABLE Month_Of_Year
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Month_Of_Year on Month_Of_Year (id);

CREATE TABLE Session_Status_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Session_Status_Type on Session_Status_Type (id);

CREATE TABLE US_State_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_US_State_Type on US_State_Type (id);

CREATE TABLE Org_Note_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Org_Note_Type on Org_Note_Type (id);

CREATE TABLE Org_Identifier_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Org_Identifier_Type on Org_Identifier_Type (id);

CREATE TABLE Org_Industry_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Org_Industry_Type on Org_Industry_Type (id);

CREATE TABLE Org_Level_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Org_Level_Type on Org_Level_Type (id);

CREATE TABLE Org_Ownership_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Org_Ownership_Type on Org_Ownership_Type (id);

CREATE TABLE Org_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Org_Type on Org_Type (id);

CREATE TABLE Person_Note_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Person_Note_Type on Person_Note_Type (id);

CREATE TABLE Blood_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Blood_Type on Blood_Type (id);

CREATE TABLE Ethnicity_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Ethnicity_Type on Ethnicity_Type (id);

CREATE TABLE Gender_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Gender_Type on Gender_Type (id);

CREATE TABLE Language_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Language_Type on Language_Type (id);

CREATE TABLE Marital_Status_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Marital_Status_Type on Marital_Status_Type (id);

CREATE TABLE Name_Prefix_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Name_Prefix_Type on Name_Prefix_Type (id);

CREATE TABLE Person_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Person_Type on Person_Type (id);

CREATE TABLE Person_Indication_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Person_Indication_Type on Person_Indication_Type (id);

CREATE TABLE Person_License_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Person_License_Type on Person_License_Type (id);

CREATE TABLE Person_Relationship_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Person_Relationship_Type on Person_Relationship_Type (id);

CREATE TABLE PersonOrg_Rel_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_PersonOrg_Rel_Type on PersonOrg_Rel_Type (id);

CREATE TABLE Person_Role_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Person_Role_Type on Person_Role_Type (id);

CREATE TABLE Asset_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Asset_Type on Asset_Type (id);

CREATE TABLE Event_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Event_Type on Event_Type (id);

CREATE TABLE Event_Status_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Event_Status_Type on Event_Status_Type (id);

CREATE TABLE Event_Status_ChgRsn_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Event_Status_ChgRsn_Type on Event_Status_ChgRsn_Type (id);

CREATE TABLE Resource_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Resource_Type on Resource_Type (id);

CREATE TABLE Session_Activity_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Session_Activity_Type on Session_Activity_Type (id);

CREATE TABLE Session_Action_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Session_Action_Type on Session_Action_Type (id);

CREATE TABLE Staff_Benefit_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Staff_Benefit_Type on Staff_Benefit_Type (id);

CREATE TABLE Staff_Speciality_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Staff_Speciality_Type on Staff_Speciality_Type (id);

CREATE TABLE Transaction_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Transaction_Type on Transaction_Type (id);

CREATE TABLE Comm_Trns_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Comm_Trns_Type on Comm_Trns_Type (id);

CREATE TABLE PhyComm_Trns_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_PhyComm_Trns_Type on PhyComm_Trns_Type (id);

CREATE TABLE Directive_Trns_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Directive_Trns_Type on Directive_Trns_Type (id);

CREATE TABLE Person
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    person_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    name_prefix_id INTEGER, /* type.EnumerationIdRefColumn */
    name_prefix VARCHAR(16), /* type.TextColumn */
    name_first VARCHAR(32) NOT NULL, /* type.TextColumn */
    name_middle VARCHAR(32), /* type.TextColumn */
    name_last VARCHAR(32) NOT NULL, /* type.TextColumn */
    name_suffix VARCHAR(16), /* type.TextColumn */
    short_name VARCHAR(42) NOT NULL, /* type.TextColumn */
    simple_name VARCHAR(96) NOT NULL, /* type.TextColumn */
    complete_name VARCHAR(128) NOT NULL, /* type.TextColumn */
    short_sortable_name VARCHAR(42) NOT NULL, /* type.TextColumn */
    complete_sortable_name VARCHAR(128) NOT NULL, /* type.TextColumn */
    ssn VARCHAR(11), /* type.TextColumn */
    gender_id INTEGER, /* type.EnumerationIdRefColumn */
    ethnicity_id VARCHAR(255), /* type.EnumSetColumn */
    language_id VARCHAR(255), /* type.EnumSetColumn */
    marital_status_id INTEGER, /* type.EnumerationIdRefColumn */
    blood_type_id INTEGER, /* type.EnumerationIdRefColumn */
    birth_date DATETIME, /* type.DateColumn */
    death_date DATETIME, /* type.DateColumn */
    age INTEGER, /* type.IntegerColumn */

    CONSTRAINT FK_PER_BLOOD_TYPE_ID FOREIGN KEY (blood_type_id) REFERENCES Blood_Type (id),
    CONSTRAINT FK_PER_GENDER_ID FOREIGN KEY (gender_id) REFERENCES Gender_Type (id),
    CONSTRAINT FK_PER_MARITAL_STATUS_ID FOREIGN KEY (marital_status_id) REFERENCES Marital_Status_Type (id),
    CONSTRAINT FK_PER_NAME_PREFIX_ID FOREIGN KEY (name_prefix_id) REFERENCES Name_Prefix_Type (id),
    CONSTRAINT FK_PER_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_PER_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX Per_ssn on Person (ssn);
CREATE  INDEX PK_Person on Person (person_id);

CREATE TABLE Per_ethnicity_id_Set
(
    system_id INTEGER IDENTITY PRIMARY KEY, /* type.AutoIncColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    enum_index INTEGER, /* type.IntegerColumn */
    enum_value INTEGER, /* type.EnumerationIdRefColumn */

    CONSTRAINT FK_PERETHST_ENUM_VALUE FOREIGN KEY (enum_value) REFERENCES Ethnicity_Type (id),
    CONSTRAINT FK_PERETHST_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Person (person_id)
);
CREATE  INDEX PK_Per_ethnicity_id_Set on Per_ethnicity_id_Set (system_id);
CREATE  INDEX PR_PerethSt_parent_id on Per_ethnicity_id_Set (parent_id);

CREATE TABLE Per_language_id_Set
(
    system_id INTEGER IDENTITY PRIMARY KEY, /* type.AutoIncColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    enum_index INTEGER, /* type.IntegerColumn */
    enum_value INTEGER, /* type.EnumerationIdRefColumn */

    CONSTRAINT FK_PERLANGST_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERLANGST_ENUM_VALUE FOREIGN KEY (enum_value) REFERENCES Language_Type (id)
);
CREATE  INDEX PK_Per_language_id_Set on Per_language_id_Set (system_id);
CREATE  INDEX PR_PerlangSt_parent_id on Per_language_id_Set (parent_id);

CREATE TABLE Person_Address
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    address_name VARCHAR(128), /* type.TextColumn */
    mailing BIT, /* type.BooleanColumn */
    address_type_id INTEGER, /* type.EnumerationIdRefColumn */
    line1 VARCHAR(256), /* type.TextColumn */
    line2 VARCHAR(256), /* type.TextColumn */
    city VARCHAR(128) NOT NULL, /* type.TextColumn */
    county VARCHAR(128), /* type.TextColumn */
    state_id INTEGER, /* type.EnumerationIdRefColumn */
    state VARCHAR(128), /* type.TextColumn */
    zip VARCHAR(128), /* type.TextColumn */
    country VARCHAR(128), /* type.TextColumn */

    CONSTRAINT FK_PERADDR_STATE_ID FOREIGN KEY (state_id) REFERENCES US_State_Type (id),
    CONSTRAINT FK_PERADDR_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERADDR_ADDRESS_TYPE_ID FOREIGN KEY (address_type_id) REFERENCES Contact_Address_Type (id),
    CONSTRAINT FK_PERADDR_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_PERADDR_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX Person_Address_unq on Person_Address (parent_id, address_name);
CREATE  INDEX PerAddr_parent_id on Person_Address (parent_id);
CREATE  INDEX PerAddr_address_name on Person_Address (address_name);
CREATE  INDEX PK_Person_Address on Person_Address (system_id);
CREATE  INDEX PR_PerAddr_parent_id on Person_Address (parent_id);

CREATE TABLE Person_Note
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    note_type_id INTEGER, /* type.EnumerationIdRefColumn */
    note_type VARCHAR(32), /* type.TextColumn */
    notes VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_PERNOTE_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PERNOTE_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERNOTE_NOTE_TYPE_ID FOREIGN KEY (note_type_id) REFERENCES Person_Note_Type (id)

    /* DELAYED: CONSTRAINT FK_PERNOTE_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Person_Note on Person_Note (system_id);
CREATE  INDEX PR_PerNote_parent_id on Person_Note (parent_id);

CREATE TABLE Person_Classification
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    person_type_id INTEGER, /* type.EnumerationIdRefColumn */
    person_type VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_PERCLASS_PERSON_TYPE_ID FOREIGN KEY (person_type_id) REFERENCES Person_Type (id),
    CONSTRAINT FK_PERCLASS_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PERCLASS_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_PERCLASS_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
    /* DELAYED: CONSTRAINT FK_PERCLASS_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Person_Classification on Person_Classification (system_id);
CREATE  INDEX PR_PerClass_person_id on Person_Classification (person_id);

CREATE TABLE Person_Contact
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    method_type INTEGER, /* type.EnumerationIdRefColumn */
    method_name VARCHAR(128), /* type.TextColumn */
    method_value VARCHAR(255), /* type.TextColumn */
    phone_cc VARCHAR(16), /* type.TextColumn */
    phone_ac VARCHAR(32), /* type.TextColumn */
    phone_prefix VARCHAR(32), /* type.TextColumn */
    phone_suffix VARCHAR(32), /* type.TextColumn */

    CONSTRAINT FK_PERCONT_METHOD_TYPE FOREIGN KEY (method_type) REFERENCES Contact_Method_Type (id),
    CONSTRAINT FK_PERCONT_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERCONT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_PERCONT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX Person_Contact_unq on Person_Contact (parent_id, method_value);
CREATE  INDEX PerCont_parent_id on Person_Contact (parent_id);
CREATE  INDEX PerCont_method_type on Person_Contact (method_type);
CREATE  INDEX PerCont_method_name on Person_Contact (method_name);
CREATE  INDEX PerCont_method_value on Person_Contact (method_value);
CREATE  INDEX PerCont_phone_cc on Person_Contact (phone_cc);
CREATE  INDEX PerCont_phone_ac on Person_Contact (phone_ac);
CREATE  INDEX PerCont_phone_prefix on Person_Contact (phone_prefix);
CREATE  INDEX PerCont_phone_suffix on Person_Contact (phone_suffix);
CREATE  INDEX PK_Person_Contact on Person_Contact (system_id);
CREATE  INDEX PR_PerCont_parent_id on Person_Contact (parent_id);

CREATE TABLE Person_Ethnicity
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    ethnicity_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    ethnicity VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_PERETH_ETHNICITY_ID FOREIGN KEY (ethnicity_id) REFERENCES Ethnicity_Type (id),
    CONSTRAINT FK_PERETH_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERETH_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_PERETH_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Person_Ethnicity on Person_Ethnicity (system_id);
CREATE  INDEX PR_PerEth_person_id on Person_Ethnicity (person_id);

CREATE TABLE Person_Flag_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Person_Flag_Type on Person_Flag_Type (id);

CREATE TABLE Person_Flag_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_PerFlSt_abbrev on Person_Flag_Status (abbrev);
CREATE  INDEX PK_Person_Flag_Status on Person_Flag_Status (id);

CREATE TABLE Person_Flag
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    flag_id INTEGER, /* type.EnumerationIdRefColumn */

    CONSTRAINT FK_PERFLG_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERFLG_FLAG_ID FOREIGN KEY (flag_id) REFERENCES Person_Flag_Type (id),
    CONSTRAINT FK_PERFLG_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Person_Flag_Status (id)

    /* DELAYED: CONSTRAINT FK_PERFLG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Person_Flag on Person_Flag (system_id);
CREATE  INDEX PR_PerFlg_parent_id on Person_Flag (parent_id);

CREATE TABLE Person_Identifier
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    id_type_id VARCHAR(36), /* type.GuidTextColumn */
    id_type VARCHAR(64), /* type.TextColumn */
    identifier VARCHAR(64), /* type.TextColumn */
    source_type_id VARCHAR(36), /* type.GuidTextColumn */
    source_type VARCHAR(64), /* type.TextColumn */
    notes VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_PERID_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERID_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_PERID_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_PERID_SOURCE_TYPE_ID FOREIGN KEY (source_type_id) REFERENCES Org_PersonId_Src_Type (system_id) (Org_PersonId_Src_Type table not created yet) */
    /* DELAYED: CONSTRAINT FK_PERID_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
    /* DELAYED: CONSTRAINT FK_PERID_ID_TYPE_ID FOREIGN KEY (id_type_id) REFERENCES Org_PersonId_Src_Type (system_id) (Org_PersonId_Src_Type table not created yet) */
);
CREATE  INDEX PK_Person_Identifier on Person_Identifier (system_id);
CREATE  INDEX PR_PerID_person_id on Person_Identifier (person_id);

CREATE TABLE Person_Insurance
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    ins_rel_id VARCHAR(36), /* type.GuidTextColumn */
    guar_person_id VARCHAR(36), /* type.GuidTextColumn */
    guar_rel_id VARCHAR(36), /* type.GuidTextColumn */
    member_number VARCHAR(64), /* type.GuidColumn */
    policy_id VARCHAR(36), /* type.GuidTextColumn */

    CONSTRAINT FK_PERINS_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERINS_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PERINS_GUAR_PERSON_ID FOREIGN KEY (guar_person_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_PERINS_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_PERINS_INS_REL_ID FOREIGN KEY (ins_rel_id) REFERENCES Person_Relationship (system_id) (Person_Relationship table not created yet) */
    /* DELAYED: CONSTRAINT FK_PERINS_POLICY_ID FOREIGN KEY (policy_id) REFERENCES Ins_Policy (policy_id) (Ins_Policy table not created yet) */
    /* DELAYED: CONSTRAINT FK_PERINS_GUAR_REL_ID FOREIGN KEY (guar_rel_id) REFERENCES Person_Relationship (system_id) (Person_Relationship table not created yet) */
);
CREATE  INDEX PK_Person_Insurance on Person_Insurance (system_id);
CREATE  INDEX PR_PerIns_person_id on Person_Insurance (person_id);

CREATE TABLE Person_Language
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    language_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    language VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_PERLANG_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERLANG_LANGUAGE_ID FOREIGN KEY (language_id) REFERENCES Language_Type (id),
    CONSTRAINT FK_PERLANG_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_PERLANG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Person_Language on Person_Language (system_id);
CREATE  INDEX PR_PerLang_person_id on Person_Language (person_id);

CREATE TABLE Person_Login_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_PerLgSt_abbrev on Person_Login_Status (abbrev);
CREATE  INDEX PK_Person_Login_Status on Person_Login_Status (id);

CREATE TABLE Person_Login
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    user_id VARCHAR(32), /* type.TextColumn */
    password VARCHAR(16), /* type.TextColumn */
    login_status INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    quantity INTEGER, /* type.IntegerColumn */

    CONSTRAINT FK_PERLG_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERLG_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PERLG_LOGIN_STATUS FOREIGN KEY (login_status) REFERENCES Person_Login_Status (id)

    /* DELAYED: CONSTRAINT FK_PERLG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PerLg_person_id on Person_Login (person_id);
CREATE unique INDEX UNQ_PerLg_user_id on Person_Login (user_id);
CREATE  INDEX PK_Person_Login on Person_Login (system_id);
CREATE  INDEX PR_PerLg_person_id on Person_Login (person_id);

CREATE TABLE Person_Relationship_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_PerRelSt_abbrev on Person_Relationship_Status (abbrev);
CREATE  INDEX PK_Person_Relationship_Status on Person_Relationship_Status (id);

CREATE TABLE Person_Relationship
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    rel_entity_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    rel_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    rel_type VARCHAR(64), /* type.TextColumn */
    rel_begin DATETIME, /* type.DateColumn */
    rel_end DATETIME, /* type.DateColumn */
    rel_descr VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_PERREL_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERREL_REL_ENTITY_ID FOREIGN KEY (rel_entity_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERREL_REL_TYPE_ID FOREIGN KEY (rel_type_id) REFERENCES Person_Relationship_Type (id),
    CONSTRAINT FK_PERREL_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Person_Relationship_Status (id)

    /* DELAYED: CONSTRAINT FK_PERREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Person_Relationship on Person_Relationship (system_id);
CREATE  INDEX PR_PerRel_parent_id on Person_Relationship (parent_id);

CREATE TABLE Person_Relationship_Map
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    rel_type_id INTEGER, /* type.EnumerationIdRefColumn */
    inv_rel_type_id INTEGER, /* type.EnumerationIdRefColumn */

    CONSTRAINT FK_PERRELMP_INV_REL_TYPE_ID FOREIGN KEY (inv_rel_type_id) REFERENCES Person_Relationship_Type (id),
    CONSTRAINT FK_PERRELMP_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PERRELMP_REL_TYPE_ID FOREIGN KEY (rel_type_id) REFERENCES Person_Relationship_Type (id)

    /* DELAYED: CONSTRAINT FK_PERRELMP_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Person_Relationship_Map on Person_Relationship_Map (system_id);

CREATE TABLE Person_Role_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_PerRlSt_abbrev on Person_Role_Status (abbrev);
CREATE  INDEX PK_Person_Role_Status on Person_Role_Status (id);

CREATE TABLE Person_Role
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    role_name_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */

    CONSTRAINT FK_PERRL_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PERRL_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_PERRL_ROLE_NAME_ID FOREIGN KEY (role_name_id) REFERENCES Org_Role_Declaration (role_name_id) (Org_Role_Declaration table not created yet) */
    /* DELAYED: CONSTRAINT FK_PERRL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PerRl_person_id on Person_Role (person_id);
CREATE  INDEX PerRl_role_name_id on Person_Role (role_name_id);
CREATE  INDEX PK_Person_Role on Person_Role (system_id);
CREATE  INDEX PR_PerRl_person_id on Person_Role (person_id);

CREATE TABLE PersonOrg_Rel_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_PeORelSt_abbrev on PersonOrg_Rel_Status (abbrev);
CREATE  INDEX PK_PersonOrg_Rel_Status on PersonOrg_Rel_Status (id);

CREATE TABLE PersonOrg_Relationship
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    rel_entity_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    rel_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    rel_type VARCHAR(64), /* type.TextColumn */
    rel_begin DATETIME, /* type.DateColumn */
    rel_end DATETIME, /* type.DateColumn */
    rel_descr VARCHAR(1024), /* type.TextColumn */
    relationship_name VARCHAR(64), /* type.TextColumn */
    relationship_code VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_PEOREL_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES PersonOrg_Rel_Status (id),
    CONSTRAINT FK_PEOREL_REL_TYPE_ID FOREIGN KEY (rel_type_id) REFERENCES PersonOrg_Rel_Type (id),
    CONSTRAINT FK_PEOREL_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_PEOREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_PEOREL_REL_ENTITY_ID FOREIGN KEY (rel_entity_id) REFERENCES Org (org_id) (Org table not created yet) */
);
CREATE  INDEX PK_PersonOrg_Relationship on PersonOrg_Relationship (system_id);
CREATE  INDEX PR_PeORel_parent_id on PersonOrg_Relationship (parent_id);

CREATE TABLE PersonOrg_Relationship_Map
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    rel_type_id INTEGER, /* type.EnumerationIdRefColumn */
    inv_rel_type_id INTEGER, /* type.EnumerationIdRefColumn */

    CONSTRAINT FK_PERORELMP_REL_TYPE_ID FOREIGN KEY (rel_type_id) REFERENCES PersonOrg_Rel_Type (id),
    CONSTRAINT FK_PERORELMP_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PERORELMP_INV_REL_TYPE_ID FOREIGN KEY (inv_rel_type_id) REFERENCES PersonOrg_Rel_Type (id)

    /* DELAYED: CONSTRAINT FK_PERORELMP_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_PersonOrg_Relationship_Map on PersonOrg_Relationship_Map (system_id);

CREATE TABLE Staff_Benefit
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    benefit_type_id INTEGER, /* type.EnumerationIdRefColumn */
    benefit_type VARCHAR(64), /* type.TextColumn */
    benefit_detail VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_STFBEN_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_STFBEN_BENEFIT_TYPE_ID FOREIGN KEY (benefit_type_id) REFERENCES Staff_Benefit_Type (id),
    CONSTRAINT FK_STFBEN_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_STFBEN_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
    /* DELAYED: CONSTRAINT FK_STFBEN_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX StfBen_person_id on Staff_Benefit (person_id);
CREATE  INDEX PK_Staff_Benefit on Staff_Benefit (system_id);
CREATE  INDEX PR_StfBen_person_id on Staff_Benefit (person_id);

CREATE TABLE Staff_License
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    license_type_id INTEGER, /* type.EnumerationIdRefColumn */
    license_type VARCHAR(64), /* type.TextColumn */
    license_num VARCHAR(64), /* type.GuidColumn */
    expiration_date DATETIME, /* type.DateColumn */
    license_state_id INTEGER, /* type.EnumerationIdRefColumn */
    license_state VARCHAR(64), /* type.TextColumn */
    speciality_type_id INTEGER, /* type.EnumerationIdRefColumn */

    CONSTRAINT FK_STFLIC_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_STFLIC_LICENSE_STATE_ID FOREIGN KEY (license_state_id) REFERENCES US_State_Type (id),
    CONSTRAINT FK_STFLIC_LICENSE_TYPE_ID FOREIGN KEY (license_type_id) REFERENCES Person_License_Type (id),
    CONSTRAINT FK_STFLIC_SPECIALITY_TYPE_ID FOREIGN KEY (speciality_type_id) REFERENCES Staff_Speciality_Type (id),
    CONSTRAINT FK_STFLIC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_STFLIC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_STFLIC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
);
CREATE  INDEX StfLic_person_id on Staff_License (person_id);
CREATE  INDEX PK_Staff_License on Staff_License (system_id);
CREATE  INDEX PR_StfLic_person_id on Staff_License (person_id);

CREATE TABLE Org_Appt_Type
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    appt_type_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    caption VARCHAR(128) NOT NULL, /* type.TextColumn */
    duration INTEGER NOT NULL, /* type.IntegerColumn */
    lead_time INTEGER, /* type.IntegerColumn */
    lag_time INTEGER, /* type.IntegerColumn */
    back_to_back BIT, /* type.BooleanColumn */
    multiple BIT, /* type.BooleanColumn */
    num_sim INTEGER, /* type.IntegerColumn */
    am_limit INTEGER, /* type.IntegerColumn */
    pm_limit INTEGER, /* type.IntegerColumn */
    day_limit INTEGER, /* type.IntegerColumn */
    appt_width INTEGER, /* type.IntegerColumn */
    attribute VARCHAR(512), /* type.TextColumn */
    super_bill_ids VARCHAR(255), /* type.TextSetColumn */

    CONSTRAINT FK_ORGAPTY_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_ORGAPTY_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_ORGAPTY_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
);
CREATE  INDEX OrgApTy_org_id on Org_Appt_Type (org_id);
CREATE  INDEX OrgApTy_caption on Org_Appt_Type (caption);
CREATE  INDEX PK_Org_Appt_Type on Org_Appt_Type (appt_type_id);
CREATE  INDEX PR_OrgApTy_org_id on Org_Appt_Type (org_id);

CREATE TABLE OrgApTy_super_bill_ids_Set
(
    system_id INTEGER IDENTITY PRIMARY KEY, /* type.AutoIncColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    value_index INTEGER, /* type.IntegerColumn */
    value_text VARCHAR(32), /* type.TextColumn */

    CONSTRAINT FK_ORGAPTYSPRBL_IDST_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Org_Appt_Type (appt_type_id)
);
CREATE  INDEX PK_OrgApTy_super_bill_ids_Set on OrgApTy_super_bill_ids_Set (system_id);
CREATE  INDEX PR_OrgApTysprbl_idSt_parent_id on OrgApTy_super_bill_ids_Set (parent_id);

CREATE TABLE Appt_Type_Resource
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    resource_id VARCHAR(36), /* type.GuidColumn */
    resource_type_id INTEGER, /* type.EnumerationIdRefColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    asset_id VARCHAR(36), /* type.GuidTextColumn */
    other_id VARCHAR(36), /* type.TextColumn */
    appt_type_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */

    CONSTRAINT FK_APTYRSRC_APPT_TYPE_ID FOREIGN KEY (appt_type_id) REFERENCES Org_Appt_Type (appt_type_id),
    CONSTRAINT FK_APTYRSRC_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_APTYRSRC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_APTYRSRC_RESOURCE_TYPE_ID FOREIGN KEY (resource_type_id) REFERENCES Resource_Type (id)

    /* DELAYED: CONSTRAINT FK_APTYRSRC_ASSET_ID FOREIGN KEY (asset_id) REFERENCES Asset (asset_id) (Asset table not created yet) */
    /* DELAYED: CONSTRAINT FK_APTYRSRC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
    /* DELAYED: CONSTRAINT FK_APTYRSRC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX ApTyRsrc_appt_type_id on Appt_Type_Resource (appt_type_id);
CREATE  INDEX PR_ApTyRsrc_appt_type_id on Appt_Type_Resource (appt_type_id);

CREATE TABLE Org_Sch_Template
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    template_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    active BIT NOT NULL, /* type.BooleanColumn */
    caption VARCHAR(128) NOT NULL, /* type.TextColumn */
    start_time DATETIME NOT NULL, /* type.TimeColumn */
    end_time DATETIME NOT NULL, /* type.TimeColumn */
    available BIT NOT NULL, /* type.BooleanColumn */
    days_of_month VARCHAR(256), /* type.TextColumn */
    months VARCHAR(256), /* type.TextColumn */
    days_of_week VARCHAR(256), /* type.TextColumn */
    slot_width INTEGER, /* type.IntegerColumn */

    CONSTRAINT FK_SCTMPL_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_SCTMPL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_SCTMPL_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
);
CREATE  INDEX ScTmpl_org_id on Org_Sch_Template (org_id);
CREATE  INDEX ScTmpl_caption on Org_Sch_Template (caption);
CREATE  INDEX PK_Org_Sch_Template on Org_Sch_Template (template_id);
CREATE  INDEX PR_ScTmpl_org_id on Org_Sch_Template (org_id);

CREATE TABLE Template_Resource
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    resource_id VARCHAR(36), /* type.GuidColumn */
    resource_type_id INTEGER, /* type.EnumerationIdRefColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    asset_id VARCHAR(36), /* type.GuidTextColumn */
    other_id VARCHAR(36), /* type.TextColumn */
    template_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */

    CONSTRAINT FK_TMPLRSRC_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_TMPLRSRC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_TMPLRSRC_RESOURCE_TYPE_ID FOREIGN KEY (resource_type_id) REFERENCES Resource_Type (id),
    CONSTRAINT FK_TMPLRSRC_TEMPLATE_ID FOREIGN KEY (template_id) REFERENCES Org_Sch_Template (template_id)

    /* DELAYED: CONSTRAINT FK_TMPLRSRC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
    /* DELAYED: CONSTRAINT FK_TMPLRSRC_ASSET_ID FOREIGN KEY (asset_id) REFERENCES Asset (asset_id) (Asset table not created yet) */
    /* DELAYED: CONSTRAINT FK_TMPLRSRC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX TmplRsrc_template_id on Template_Resource (template_id);
CREATE  INDEX PR_TmplRsrc_template_id on Template_Resource (template_id);

CREATE TABLE Appt_Chain_Entry
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    entry_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    chain_id VARCHAR(36), /* type.GuidTextColumn */
    appt_type_id VARCHAR(36), /* type.GuidTextColumn */
    sequence INTEGER, /* type.IntegerColumn */
    org_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */

    CONSTRAINT FK_APCHAINE_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_APCHAINE_CHAIN_ID FOREIGN KEY (chain_id) REFERENCES Org_Appt_Type (appt_type_id),
    CONSTRAINT FK_APCHAINE_APPT_TYPE_ID FOREIGN KEY (appt_type_id) REFERENCES Org_Appt_Type (appt_type_id)

    /* DELAYED: CONSTRAINT FK_APCHAINE_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_APCHAINE_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
);
CREATE  INDEX ApChainE_org_id on Appt_Chain_Entry (org_id);
CREATE  INDEX PK_Appt_Chain_Entry on Appt_Chain_Entry (entry_id);

CREATE TABLE Event
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    event_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    event_type_id INTEGER, /* type.EnumerationIdRefColumn */
    event_status_id INTEGER, /* type.EnumerationIdRefColumn */
    status_chgrsn_id INTEGER, /* type.EnumerationIdRefColumn */
    remark VARCHAR(1024), /* type.TextColumn */
    start_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    duration INTEGER, /* type.IntegerColumn */
    scheduled_by_id VARCHAR(36), /* type.GuidTextColumn */
    scheduled_stamp DATETIME, /* type.DateTimeColumn */
    appt_type_id VARCHAR(36), /* type.GuidTextColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    waiting_list BIT, /* type.BooleanColumn */
    start_time VARCHAR(8), /* type.TextColumn */

    CONSTRAINT FK_EVENT_SCHEDULED_BY_ID FOREIGN KEY (scheduled_by_id) REFERENCES Person (person_id),
    CONSTRAINT FK_EVENT_EVENT_STATUS_ID FOREIGN KEY (event_status_id) REFERENCES Event_Status_Type (id),
    CONSTRAINT FK_EVENT_APPT_TYPE_ID FOREIGN KEY (appt_type_id) REFERENCES Org_Appt_Type (appt_type_id),
    CONSTRAINT FK_EVENT_STATUS_CHGRSN_ID FOREIGN KEY (status_chgrsn_id) REFERENCES Event_Status_ChgRsn_Type (id),
    CONSTRAINT FK_EVENT_EVENT_TYPE_ID FOREIGN KEY (event_type_id) REFERENCES Event_Type (id),
    CONSTRAINT FK_EVENT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_EVENT_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Event (event_id) (Event table not created yet) */
    /* DELAYED: CONSTRAINT FK_EVENT_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
    /* DELAYED: CONSTRAINT FK_EVENT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX Event_org_id on Event (org_id);
CREATE  INDEX Event_start_stamp on Event (start_stamp);
CREATE  INDEX PK_Event on Event (event_id);

CREATE TABLE Event_Resource
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    resource_id VARCHAR(36), /* type.GuidColumn */
    resource_type_id INTEGER, /* type.EnumerationIdRefColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    asset_id VARCHAR(36), /* type.GuidTextColumn */
    other_id VARCHAR(36), /* type.TextColumn */
    event_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */

    CONSTRAINT FK_EVRSRC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_EVRSRC_RESOURCE_TYPE_ID FOREIGN KEY (resource_type_id) REFERENCES Resource_Type (id),
    CONSTRAINT FK_EVRSRC_EVENT_ID FOREIGN KEY (event_id) REFERENCES Event (event_id),
    CONSTRAINT FK_EVRSRC_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_EVRSRC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id) (Org table not created yet) */
    /* DELAYED: CONSTRAINT FK_EVRSRC_ASSET_ID FOREIGN KEY (asset_id) REFERENCES Asset (asset_id) (Asset table not created yet) */
    /* DELAYED: CONSTRAINT FK_EVRSRC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX EvRsrc_event_id on Event_Resource (event_id);
CREATE  INDEX PR_EvRsrc_event_id on Event_Resource (event_id);

CREATE TABLE Event_Attendee
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    event_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    patient_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    reason VARCHAR(1024), /* type.TextColumn */
    checkin_by_id VARCHAR(36), /* type.GuidTextColumn */
    checkin_stamp DATETIME, /* type.DateTimeColumn */
    checkout_by_id VARCHAR(36), /* type.GuidTextColumn */
    checkout_stamp DATETIME, /* type.DateTimeColumn */
    discard_by_id VARCHAR(36), /* type.GuidTextColumn */
    discard_stamp DATETIME, /* type.DateTimeColumn */

    CONSTRAINT FK_EVATNDEE_CHECKOUT_BY_ID FOREIGN KEY (checkout_by_id) REFERENCES Person (person_id),
    CONSTRAINT FK_EVATNDEE_CHECKIN_BY_ID FOREIGN KEY (checkin_by_id) REFERENCES Person (person_id),
    CONSTRAINT FK_EVATNDEE_EVENT_ID FOREIGN KEY (event_id) REFERENCES Event (event_id),
    CONSTRAINT FK_EVATNDEE_PATIENT_ID FOREIGN KEY (patient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_EVATNDEE_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_EVATNDEE_DISCARD_BY_ID FOREIGN KEY (discard_by_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_EVATNDEE_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX EvAtndee_event_id on Event_Attendee (event_id);
CREATE  INDEX EvAtndee_patient_id on Event_Attendee (patient_id);
CREATE  INDEX PR_EvAtndee_event_id on Event_Attendee (event_id);

CREATE TABLE Org
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    org_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_code VARCHAR(64) NOT NULL, /* type.TextColumn */
    org_name VARCHAR(128) NOT NULL, /* type.TextColumn */
    org_abbrev VARCHAR(24), /* type.TextColumn */
    ownership_id INTEGER, /* type.EnumerationIdRefColumn */
    ticker_symbol VARCHAR(24), /* type.TextColumn */
    sic_code VARCHAR(24), /* type.TextColumn */
    employees INTEGER, /* type.IntegerColumn */
    fiscal_year_month_id INTEGER, /* type.EnumerationIdRefColumn */
    business_start_time DATETIME, /* type.TimeColumn */
    business_end_time DATETIME, /* type.TimeColumn */
    time_zone VARCHAR(10), /* type.TextColumn */
    org_level_id INTEGER, /* type.EnumerationIdRefColumn */
    hcfa_servplace_id INTEGER, /* type.EnumerationIdRefColumn */

    CONSTRAINT FK_ORG_ORG_LEVEL_ID FOREIGN KEY (org_level_id) REFERENCES Org_Level_Type (id),
    CONSTRAINT FK_ORG_FISCAL_YEAR_MONTH_ID FOREIGN KEY (fiscal_year_month_id) REFERENCES Month_Of_Year (id),
    CONSTRAINT FK_ORG_HCFA_SERVPLACE_ID FOREIGN KEY (hcfa_servplace_id) REFERENCES HCFA1500_Service_Place_Type (id),
    CONSTRAINT FK_ORG_OWNERSHIP_ID FOREIGN KEY (ownership_id) REFERENCES Org_Ownership_Type (id),
    CONSTRAINT FK_ORG_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_ORG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org on Org (org_id);

CREATE TABLE Org_Address
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    address_name VARCHAR(128), /* type.TextColumn */
    mailing BIT, /* type.BooleanColumn */
    address_type_id INTEGER, /* type.EnumerationIdRefColumn */
    line1 VARCHAR(256), /* type.TextColumn */
    line2 VARCHAR(256), /* type.TextColumn */
    city VARCHAR(128) NOT NULL, /* type.TextColumn */
    county VARCHAR(128), /* type.TextColumn */
    state_id INTEGER, /* type.EnumerationIdRefColumn */
    state VARCHAR(128), /* type.TextColumn */
    zip VARCHAR(128), /* type.TextColumn */
    country VARCHAR(128), /* type.TextColumn */

    CONSTRAINT FK_ORGADR_STATE_ID FOREIGN KEY (state_id) REFERENCES US_State_Type (id),
    CONSTRAINT FK_ORGADR_ADDRESS_TYPE_ID FOREIGN KEY (address_type_id) REFERENCES Contact_Address_Type (id),
    CONSTRAINT FK_ORGADR_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ORGADR_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_ORGADR_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX Org_Address_unq on Org_Address (parent_id, address_name);
CREATE  INDEX OrgAdr_parent_id on Org_Address (parent_id);
CREATE  INDEX OrgAdr_address_name on Org_Address (address_name);
CREATE  INDEX PK_Org_Address on Org_Address (system_id);
CREATE  INDEX PR_OrgAdr_parent_id on Org_Address (parent_id);

CREATE TABLE Org_Note
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    note_type_id INTEGER, /* type.EnumerationIdRefColumn */
    note_type VARCHAR(32), /* type.TextColumn */
    notes VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_ONOTE_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ONOTE_NOTE_TYPE_ID FOREIGN KEY (note_type_id) REFERENCES Org_Note_Type (id),
    CONSTRAINT FK_ONOTE_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_ONOTE_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org_Note on Org_Note (system_id);
CREATE  INDEX PR_ONote_parent_id on Org_Note (parent_id);

CREATE TABLE Org_Classification
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    org_type_id INTEGER, /* type.EnumerationIdRefColumn */
    org_type VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_ORGCLASS_ORG_TYPE_ID FOREIGN KEY (org_type_id) REFERENCES Org_Type (id),
    CONSTRAINT FK_ORGCLASS_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ORGCLASS_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id)

    /* DELAYED: CONSTRAINT FK_ORGCLASS_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org_Classification on Org_Classification (system_id);
CREATE  INDEX PR_OrgClass_org_id on Org_Classification (org_id);

CREATE TABLE Org_Contact
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    method_type INTEGER, /* type.EnumerationIdRefColumn */
    method_name VARCHAR(128), /* type.TextColumn */
    method_value VARCHAR(255), /* type.TextColumn */
    phone_cc VARCHAR(16), /* type.TextColumn */
    phone_ac VARCHAR(32), /* type.TextColumn */
    phone_prefix VARCHAR(32), /* type.TextColumn */
    phone_suffix VARCHAR(32), /* type.TextColumn */

    CONSTRAINT FK_ORGCNT_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ORGCNT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ORGCNT_METHOD_TYPE FOREIGN KEY (method_type) REFERENCES Contact_Method_Type (id)

    /* DELAYED: CONSTRAINT FK_ORGCNT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX Org_Contact_unq on Org_Contact (parent_id, method_value);
CREATE  INDEX OrgCnt_parent_id on Org_Contact (parent_id);
CREATE  INDEX OrgCnt_method_type on Org_Contact (method_type);
CREATE  INDEX OrgCnt_method_name on Org_Contact (method_name);
CREATE  INDEX OrgCnt_method_value on Org_Contact (method_value);
CREATE  INDEX OrgCnt_phone_cc on Org_Contact (phone_cc);
CREATE  INDEX OrgCnt_phone_ac on Org_Contact (phone_ac);
CREATE  INDEX OrgCnt_phone_prefix on Org_Contact (phone_prefix);
CREATE  INDEX OrgCnt_phone_suffix on Org_Contact (phone_suffix);
CREATE  INDEX PK_Org_Contact on Org_Contact (system_id);
CREATE  INDEX PR_OrgCnt_parent_id on Org_Contact (parent_id);

CREATE TABLE Org_Inv_Claim_Rel
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    invoice_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    claim_id VARCHAR(36), /* type.GuidTextColumn */
    inv_type_id INTEGER, /* type.EnumerationIdRefColumn */
    inv_type VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_ORGINVCLMREL_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ORGINVCLMREL_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ORGINVCLMREL_INV_TYPE_ID FOREIGN KEY (inv_type_id) REFERENCES Invoice_Type (id)

    /* DELAYED: CONSTRAINT FK_ORGINVCLMREL_CLAIM_ID FOREIGN KEY (claim_id) REFERENCES Claim (claim_id) (Claim table not created yet) */
    /* DELAYED: CONSTRAINT FK_ORGINVCLMREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_ORGINVCLMREL_INVOICE_ID FOREIGN KEY (invoice_id) REFERENCES Org_Inv_Visit (invoice_id) (Org_Inv_Visit table not created yet) */
);
CREATE  INDEX PK_Org_Inv_Claim_Rel on Org_Inv_Claim_Rel (system_id);
CREATE  INDEX PR_OrgInvClmRel_org_id on Org_Inv_Claim_Rel (org_id);

CREATE TABLE Org_Identifier
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    identifier_type_id INTEGER, /* type.EnumerationIdRefColumn */
    identifier_type VARCHAR(64), /* type.TextColumn */
    identifier VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_ORGID_IDENTIFIER_TYPE_ID FOREIGN KEY (identifier_type_id) REFERENCES Org_Identifier_Type (id),
    CONSTRAINT FK_ORGID_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ORGID_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_ORGID_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org_Identifier on Org_Identifier (system_id);
CREATE  INDEX PR_OrgID_org_id on Org_Identifier (org_id);

CREATE TABLE Org_Industry
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    industry_type_id INTEGER, /* type.EnumerationIdRefColumn */
    industry_type VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_OIND_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id),
    CONSTRAINT FK_OIND_INDUSTRY_TYPE_ID FOREIGN KEY (industry_type_id) REFERENCES Org_Industry_Type (id),
    CONSTRAINT FK_OIND_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_OIND_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org_Industry on Org_Industry (system_id);
CREATE  INDEX PR_OInd_org_id on Org_Industry (org_id);

CREATE TABLE Org_PersonId_Src_Type
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    item_id INTEGER NOT NULL, /* type.IntegerColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32), /* type.TextColumn */

    CONSTRAINT FK_OPERSRCIDTY_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_OPERSRCIDTY_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id)

    /* DELAYED: CONSTRAINT FK_OPERSRCIDTY_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX Org_PersonId_Src_Type_unq on Org_PersonId_Src_Type (org_id, item_id);
CREATE unique INDEX UNQ_OPerSrcIdTy_abbrev on Org_PersonId_Src_Type (abbrev);
CREATE  INDEX PK_Org_PersonId_Src_Type on Org_PersonId_Src_Type (system_id);
CREATE  INDEX PR_OPerSrcIdTy_org_id on Org_PersonId_Src_Type (org_id);

CREATE TABLE Org_Product
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    product_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    product_name VARCHAR(256), /* type.TextColumn */
    measurement_unit_id INTEGER, /* type.EnumerationIdRefColumn */
    measurement_unit VARCHAR(16), /* type.TextColumn */
    unit_measure INTEGER, /* type.IntegerColumn */

    CONSTRAINT FK_OPRD_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_OPRD_MEASUREMENT_UNIT_ID FOREIGN KEY (measurement_unit_id) REFERENCES Measurement_Unit_Type (id),
    CONSTRAINT FK_OPRD_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id)

    /* DELAYED: CONSTRAINT FK_OPRD_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org_Product on Org_Product (product_id);
CREATE  INDEX PR_OPrd_org_id on Org_Product (org_id);

CREATE TABLE Org_Relationship_Type
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE  INDEX PK_Org_Relationship_Type on Org_Relationship_Type (id);

CREATE TABLE Org_Relationship_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_ORelSt_abbrev on Org_Relationship_Status (abbrev);
CREATE  INDEX PK_Org_Relationship_Status on Org_Relationship_Status (id);

CREATE TABLE Org_Relationship
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    rel_entity_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    rel_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    rel_type VARCHAR(64), /* type.TextColumn */
    rel_begin DATETIME, /* type.DateColumn */
    rel_end DATETIME, /* type.DateColumn */
    rel_descr VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_ORGREL_REL_ENTITY_ID FOREIGN KEY (rel_entity_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ORGREL_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ORGREL_REL_TYPE_ID FOREIGN KEY (rel_type_id) REFERENCES Org_Relationship_Type (id),
    CONSTRAINT FK_ORGREL_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Org_Relationship_Status (id)

    /* DELAYED: CONSTRAINT FK_ORGREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org_Relationship on Org_Relationship (system_id);
CREATE  INDEX PR_OrgRel_parent_id on Org_Relationship (parent_id);

CREATE TABLE Org_Relationship_Map
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    rel_type_id INTEGER, /* type.EnumerationIdRefColumn */
    inv_rel_type_id INTEGER, /* type.EnumerationIdRefColumn */

    CONSTRAINT FK_ORGRELMP_INV_REL_TYPE_ID FOREIGN KEY (inv_rel_type_id) REFERENCES Org_Relationship_Type (id),
    CONSTRAINT FK_ORGRELMP_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ORGRELMP_REL_TYPE_ID FOREIGN KEY (rel_type_id) REFERENCES Org_Relationship_Type (id)

    /* DELAYED: CONSTRAINT FK_ORGRELMP_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org_Relationship_Map on Org_Relationship_Map (system_id);

CREATE TABLE Org_Role_Declaration
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    role_name_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    role_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    role_name VARCHAR(255) NOT NULL, /* type.TextColumn */

    CONSTRAINT FK_ORGRLDC_ROLE_TYPE_ID FOREIGN KEY (role_type_id) REFERENCES Person_Role_Type (id),
    CONSTRAINT FK_ORGRLDC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ORGRLDC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id)

    /* DELAYED: CONSTRAINT FK_ORGRLDC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX Org_Role_Declaration_unq on Org_Role_Declaration (role_type_id, role_name);
CREATE  INDEX OrgRlDc_role_type_id on Org_Role_Declaration (role_type_id);
CREATE  INDEX PK_Org_Role_Declaration on Org_Role_Declaration (role_name_id);
CREATE  INDEX PR_OrgRlDc_org_id on Org_Role_Declaration (org_id);

CREATE TABLE Org_Service
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    service_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    service_name VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_OSVC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id),
    CONSTRAINT FK_OSVC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_OSVC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Org_Service on Org_Service (service_id);
CREATE  INDEX PR_OSvc_org_id on Org_Service (org_id);

CREATE TABLE Ins_Plan
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    plan_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    product_id VARCHAR(36), /* type.GuidTextColumn */
    plan_name VARCHAR(64), /* type.TextColumn */
    remit_payer_id VARCHAR(64), /* type.TextColumn */
    remit_type_id INTEGER, /* type.EnumerationIdRefColumn */
    remit_type VARCHAR(32), /* type.TextColumn */
    remit_payer_name VARCHAR(256), /* type.TextColumn */
    medigap_id VARCHAR(64), /* type.TextColumn */
    initiation_date DATETIME, /* type.DateColumn */
    expiration_date DATETIME, /* type.DateColumn */

    CONSTRAINT FK_INSPLN_REMIT_TYPE_ID FOREIGN KEY (remit_type_id) REFERENCES Bill_Remittance_Type (id),
    CONSTRAINT FK_INSPLN_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_INSPLN_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_INSPLN_PRODUCT_ID FOREIGN KEY (product_id) REFERENCES Ins_Product (product_id) (Ins_Product table not created yet) */
);
CREATE  INDEX PK_Ins_Plan on Ins_Plan (plan_id);
CREATE  INDEX PR_InsPln_product_id on Ins_Plan (product_id);

CREATE TABLE Ins_Policy
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    policy_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    plan_id VARCHAR(36), /* type.GuidTextColumn */
    policy_number VARCHAR(32), /* type.GuidColumn */
    group_number VARCHAR(32), /* type.GuidColumn */
    group_name VARCHAR(32), /* type.TextColumn */
    bill_sequence_id INTEGER, /* type.EnumerationIdRefColumn */
    coverage_begin_date DATETIME, /* type.DateColumn */
    coverage_end_date DATETIME, /* type.DateColumn */

    CONSTRAINT FK_INSPLCY_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_INSPLCY_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_INSPLCY_BILL_SEQUENCE_ID FOREIGN KEY (bill_sequence_id) REFERENCES Bill_Sequence_Type (id),
    CONSTRAINT FK_INSPLCY_PLAN_ID FOREIGN KEY (plan_id) REFERENCES Ins_Plan (plan_id)

    /* DELAYED: CONSTRAINT FK_INSPLCY_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Ins_Policy on Ins_Policy (policy_id);
CREATE  INDEX PR_InsPlcy_plan_id on Ins_Policy (plan_id);

CREATE TABLE Ins_Product
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    product_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    prd_type_id INTEGER, /* type.EnumerationIdRefColumn */
    prd_type VARCHAR(32), /* type.TextColumn */
    product_name VARCHAR(64), /* type.TextColumn */
    remit_payer_id VARCHAR(64), /* type.TextColumn */
    remit_type_id INTEGER, /* type.EnumerationIdRefColumn */
    remit_type VARCHAR(32), /* type.TextColumn */
    remit_payer_name VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_INSPRD_REMIT_TYPE_ID FOREIGN KEY (remit_type_id) REFERENCES Bill_Remittance_Type (id),
    CONSTRAINT FK_INSPRD_PRD_TYPE_ID FOREIGN KEY (prd_type_id) REFERENCES Ins_Product_Type (id),
    CONSTRAINT FK_INSPRD_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_INSPRD_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id)

    /* DELAYED: CONSTRAINT FK_INSPRD_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Ins_Product on Ins_Product (product_id);
CREATE  INDEX PR_InsPrd_org_id on Ins_Product (org_id);

CREATE TABLE InsPlan_Address
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    address_name VARCHAR(128), /* type.TextColumn */
    mailing BIT, /* type.BooleanColumn */
    address_type_id INTEGER, /* type.EnumerationIdRefColumn */
    line1 VARCHAR(256), /* type.TextColumn */
    line2 VARCHAR(256), /* type.TextColumn */
    city VARCHAR(128) NOT NULL, /* type.TextColumn */
    county VARCHAR(128), /* type.TextColumn */
    state_id INTEGER, /* type.EnumerationIdRefColumn */
    state VARCHAR(128), /* type.TextColumn */
    zip VARCHAR(128), /* type.TextColumn */
    country VARCHAR(128), /* type.TextColumn */

    CONSTRAINT FK_INSPLNADDR_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_INSPLNADDR_ADDRESS_TYPE_ID FOREIGN KEY (address_type_id) REFERENCES Contact_Address_Type (id),
    CONSTRAINT FK_INSPLNADDR_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Ins_Plan (plan_id),
    CONSTRAINT FK_INSPLNADDR_STATE_ID FOREIGN KEY (state_id) REFERENCES US_State_Type (id)

    /* DELAYED: CONSTRAINT FK_INSPLNADDR_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX InsPlan_Address_unq on InsPlan_Address (parent_id, address_name);
CREATE  INDEX InsPlnAddr_parent_id on InsPlan_Address (parent_id);
CREATE  INDEX InsPlnAddr_address_name on InsPlan_Address (address_name);
CREATE  INDEX PK_InsPlan_Address on InsPlan_Address (system_id);
CREATE  INDEX PR_InsPlnAddr_parent_id on InsPlan_Address (parent_id);

CREATE TABLE InsPlan_Contact
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    method_type INTEGER, /* type.EnumerationIdRefColumn */
    method_name VARCHAR(128), /* type.TextColumn */
    method_value VARCHAR(255), /* type.TextColumn */
    phone_cc VARCHAR(16), /* type.TextColumn */
    phone_ac VARCHAR(32), /* type.TextColumn */
    phone_prefix VARCHAR(32), /* type.TextColumn */
    phone_suffix VARCHAR(32), /* type.TextColumn */

    CONSTRAINT FK_INSPLNCONT_METHOD_TYPE FOREIGN KEY (method_type) REFERENCES Contact_Method_Type (id),
    CONSTRAINT FK_INSPLNCONT_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Ins_Plan (plan_id),
    CONSTRAINT FK_INSPLNCONT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_INSPLNCONT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX InsPlan_Contact_unq on InsPlan_Contact (parent_id, method_value);
CREATE  INDEX InsPlnCont_parent_id on InsPlan_Contact (parent_id);
CREATE  INDEX InsPlnCont_method_type on InsPlan_Contact (method_type);
CREATE  INDEX InsPlnCont_method_name on InsPlan_Contact (method_name);
CREATE  INDEX InsPlnCont_method_value on InsPlan_Contact (method_value);
CREATE  INDEX InsPlnCont_phone_cc on InsPlan_Contact (phone_cc);
CREATE  INDEX InsPlnCont_phone_ac on InsPlan_Contact (phone_ac);
CREATE  INDEX InsPlnCont_phone_prefix on InsPlan_Contact (phone_prefix);
CREATE  INDEX InsPlnCont_phone_suffix on InsPlan_Contact (phone_suffix);
CREATE  INDEX PK_InsPlan_Contact on InsPlan_Contact (system_id);
CREATE  INDEX PR_InsPlnCont_parent_id on InsPlan_Contact (parent_id);

CREATE TABLE InsPlan_Coverage
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    cvg_type_id INTEGER, /* type.EnumerationIdRefColumn */
    cvg_type VARCHAR(32), /* type.TextColumn */
    ind_deduct_amt MONEY, /* type.CurrencyColumn */
    fam_deduct_amt MONEY, /* type.CurrencyColumn */
    percent_pmt FLOAT, /* type.FloatColumn */
    threshold_amt MONEY, /* type.CurrencyColumn */
    copay_amt MONEY, /* type.CurrencyColumn */

    CONSTRAINT FK_INSPLNCOV_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_INSPLNCOV_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Ins_Plan (plan_id),
    CONSTRAINT FK_INSPLNCOV_CVG_TYPE_ID FOREIGN KEY (cvg_type_id) REFERENCES Ins_Coverage_Type (id)

    /* DELAYED: CONSTRAINT FK_INSPLNCOV_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX InsPlnCov_parent_id on InsPlan_Coverage (parent_id);
CREATE  INDEX PK_InsPlan_Coverage on InsPlan_Coverage (system_id);
CREATE  INDEX PR_InsPlnCov_parent_id on InsPlan_Coverage (parent_id);

CREATE TABLE InsPolicy_Coverage
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    cvg_type_id INTEGER, /* type.EnumerationIdRefColumn */
    cvg_type VARCHAR(32), /* type.TextColumn */
    ind_deduct_amt MONEY, /* type.CurrencyColumn */
    fam_deduct_amt MONEY, /* type.CurrencyColumn */
    percent_pmt FLOAT, /* type.FloatColumn */
    threshold_amt MONEY, /* type.CurrencyColumn */
    copay_amt MONEY, /* type.CurrencyColumn */

    CONSTRAINT FK_INSPOLCOV_CVG_TYPE_ID FOREIGN KEY (cvg_type_id) REFERENCES Ins_Coverage_Type (id),
    CONSTRAINT FK_INSPOLCOV_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_INSPOLCOV_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Ins_Policy (policy_id)

    /* DELAYED: CONSTRAINT FK_INSPOLCOV_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX InsPolCov_parent_id on InsPolicy_Coverage (parent_id);
CREATE  INDEX PK_InsPolicy_Coverage on InsPolicy_Coverage (system_id);
CREATE  INDEX PR_InsPolCov_parent_id on InsPolicy_Coverage (parent_id);

CREATE TABLE InsProduct_Address
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    address_name VARCHAR(128), /* type.TextColumn */
    mailing BIT, /* type.BooleanColumn */
    address_type_id INTEGER, /* type.EnumerationIdRefColumn */
    line1 VARCHAR(256), /* type.TextColumn */
    line2 VARCHAR(256), /* type.TextColumn */
    city VARCHAR(128) NOT NULL, /* type.TextColumn */
    county VARCHAR(128), /* type.TextColumn */
    state_id INTEGER, /* type.EnumerationIdRefColumn */
    state VARCHAR(128), /* type.TextColumn */
    zip VARCHAR(128), /* type.TextColumn */
    country VARCHAR(128), /* type.TextColumn */

    CONSTRAINT FK_INSPRDADDR_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_INSPRDADDR_STATE_ID FOREIGN KEY (state_id) REFERENCES US_State_Type (id),
    CONSTRAINT FK_INSPRDADDR_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Ins_Product (product_id),
    CONSTRAINT FK_INSPRDADDR_ADDRESS_TYPE_ID FOREIGN KEY (address_type_id) REFERENCES Contact_Address_Type (id)

    /* DELAYED: CONSTRAINT FK_INSPRDADDR_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX InsProduct_Address_unq on InsProduct_Address (parent_id, address_name);
CREATE  INDEX InsPrdAddr_parent_id on InsProduct_Address (parent_id);
CREATE  INDEX InsPrdAddr_address_name on InsProduct_Address (address_name);
CREATE  INDEX PK_InsProduct_Address on InsProduct_Address (system_id);
CREATE  INDEX PR_InsPrdAddr_parent_id on InsProduct_Address (parent_id);

CREATE TABLE InsProduct_Contact
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    method_type INTEGER, /* type.EnumerationIdRefColumn */
    method_name VARCHAR(128), /* type.TextColumn */
    method_value VARCHAR(255), /* type.TextColumn */
    phone_cc VARCHAR(16), /* type.TextColumn */
    phone_ac VARCHAR(32), /* type.TextColumn */
    phone_prefix VARCHAR(32), /* type.TextColumn */
    phone_suffix VARCHAR(32), /* type.TextColumn */

    CONSTRAINT FK_INSPRDCONT_METHOD_TYPE FOREIGN KEY (method_type) REFERENCES Contact_Method_Type (id),
    CONSTRAINT FK_INSPRDCONT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_INSPRDCONT_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Ins_Product (product_id)

    /* DELAYED: CONSTRAINT FK_INSPRDCONT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE unique INDEX InsProduct_Contact_unq on InsProduct_Contact (parent_id, method_value);
CREATE  INDEX InsPrdCont_parent_id on InsProduct_Contact (parent_id);
CREATE  INDEX InsPrdCont_method_type on InsProduct_Contact (method_type);
CREATE  INDEX InsPrdCont_method_name on InsProduct_Contact (method_name);
CREATE  INDEX InsPrdCont_method_value on InsProduct_Contact (method_value);
CREATE  INDEX InsPrdCont_phone_cc on InsProduct_Contact (phone_cc);
CREATE  INDEX InsPrdCont_phone_ac on InsProduct_Contact (phone_ac);
CREATE  INDEX InsPrdCont_phone_prefix on InsProduct_Contact (phone_prefix);
CREATE  INDEX InsPrdCont_phone_suffix on InsProduct_Contact (phone_suffix);
CREATE  INDEX PK_InsProduct_Contact on InsProduct_Contact (system_id);
CREATE  INDEX PR_InsPrdCont_parent_id on InsProduct_Contact (parent_id);

CREATE TABLE InsProduct_Coverage
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    cvg_type_id INTEGER, /* type.EnumerationIdRefColumn */
    cvg_type VARCHAR(32), /* type.TextColumn */
    ind_deduct_amt MONEY, /* type.CurrencyColumn */
    fam_deduct_amt MONEY, /* type.CurrencyColumn */
    percent_pmt FLOAT, /* type.FloatColumn */
    threshold_amt MONEY, /* type.CurrencyColumn */
    copay_amt MONEY, /* type.CurrencyColumn */

    CONSTRAINT FK_INSPRDCOV_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Ins_Product (product_id),
    CONSTRAINT FK_INSPRDCOV_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_INSPRDCOV_CVG_TYPE_ID FOREIGN KEY (cvg_type_id) REFERENCES Ins_Coverage_Type (id)

    /* DELAYED: CONSTRAINT FK_INSPRDCOV_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX InsPrdCov_parent_id on InsProduct_Coverage (parent_id);
CREATE  INDEX PK_InsProduct_Coverage on InsProduct_Coverage (system_id);
CREATE  INDEX PR_InsPrdCov_parent_id on InsProduct_Coverage (parent_id);

CREATE TABLE Asset
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    asset_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    asset_type_id INTEGER, /* type.EnumerationIdRefColumn */
    asset_type VARCHAR(256), /* type.TextColumn */
    name VARCHAR(256) NOT NULL, /* type.TextColumn */
    concurrency INTEGER NOT NULL, /* type.IntegerColumn */
    location VARCHAR(256), /* type.TextColumn */
    serial_num VARCHAR(512), /* type.GuidColumn */

    CONSTRAINT FK_AST_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_AST_ASSET_TYPE_ID FOREIGN KEY (asset_type_id) REFERENCES Asset_Type (id)

    /* DELAYED: CONSTRAINT FK_AST_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Asset on Asset (asset_id);

CREATE TABLE Asset_Maint
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    asset_id VARCHAR(36), /* type.GuidTextColumn */
    scheduled BIT, /* type.BooleanColumn */
    breakdown_date DATETIME, /* type.DateColumn */
    maintenance_date DATETIME, /* type.DateColumn */

    CONSTRAINT FK_ASTMAINT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ASTMAINT_ASSET_ID FOREIGN KEY (asset_id) REFERENCES Asset (asset_id)

    /* DELAYED: CONSTRAINT FK_ASTMAINT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Asset_Maint on Asset_Maint (system_id);
CREATE  INDEX PR_AstMaint_asset_id on Asset_Maint (asset_id);

CREATE TABLE Org_Inv_Visit
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    invoice_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    act_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    submitter_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    target_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    invoice_num VARCHAR(64), /* type.TextColumn */
    invoice_stat_id INTEGER, /* type.EnumerationIdRefColumn */
    invoice_date DATETIME NOT NULL, /* type.DateColumn */
    submit_date DATETIME, /* type.DateColumn */

    CONSTRAINT FK_ORGINVVIS_SUBMITTER_ID FOREIGN KEY (submitter_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ORGINVVIS_INVOICE_STAT_ID FOREIGN KEY (invoice_stat_id) REFERENCES Invoice_Status_Type (id),
    CONSTRAINT FK_ORGINVVIS_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ORGINVVIS_TARGET_ID FOREIGN KEY (target_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ORGINVVIS_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id)

    /* DELAYED: CONSTRAINT FK_ORGINVVIS_ACT_ID FOREIGN KEY (act_id) REFERENCES Action_Visit (act_id) (Action_Visit table not created yet) */
    /* DELAYED: CONSTRAINT FK_ORGINVVIS_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX OrgInvVis_invoice_stat_id on Org_Inv_Visit (invoice_stat_id);
CREATE  INDEX PK_Org_Inv_Visit on Org_Inv_Visit (invoice_id);
CREATE  INDEX PR_OrgInvVis_org_id on Org_Inv_Visit (org_id);

CREATE TABLE Visit_Line_Item
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    invoice_id VARCHAR(36), /* type.GuidTextColumn */

    CONSTRAINT FK_VISLNITM_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_VISLNITM_INVOICE_ID FOREIGN KEY (invoice_id) REFERENCES Org_Inv_Visit (invoice_id)

    /* DELAYED: CONSTRAINT FK_VISLNITM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Visit_Line_Item on Visit_Line_Item (system_id);
CREATE  INDEX PR_VisLnItm_invoice_id on Visit_Line_Item (invoice_id);

CREATE TABLE Org_Medication
(
    med_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    med_type_id INTEGER, /* type.EnumerationIdRefColumn */
    med_type VARCHAR(64), /* type.TextColumn */
    med_name VARCHAR(64), /* type.TextColumn */
    generic_name VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_MED_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id),
    CONSTRAINT FK_MED_MED_TYPE_ID FOREIGN KEY (med_type_id) REFERENCES Medication_Type (id)
);
CREATE unique INDEX Org_Medication_unq on Org_Medication (org_id, med_id);
CREATE  INDEX PK_Org_Medication on Org_Medication (med_id);
CREATE  INDEX PR_Med_org_id on Org_Medication (org_id);

CREATE TABLE Patient_Indication
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    indication_type_id INTEGER, /* type.EnumerationIdRefColumn */
    indication_type VARCHAR(64), /* type.TextColumn */
    indication VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_PATINDIC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PATINDIC_INDICATION_TYPE_ID FOREIGN KEY (indication_type_id) REFERENCES Person_Indication_Type (id),
    CONSTRAINT FK_PATINDIC_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_PATINDIC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Patient_Indication on Patient_Indication (system_id);
CREATE  INDEX PR_PatIndic_person_id on Patient_Indication (person_id);

CREATE TABLE Patient_Medication
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    patient_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    record_type INTEGER, /* type.EnumerationIdRefColumn */
    med_id VARCHAR(36), /* type.GuidTextColumn */
    prescriber_id VARCHAR(36), /* type.GuidTextColumn */
    pharmacy_id VARCHAR(36), /* type.GuidTextColumn */
    pharmacy_name VARCHAR(256), /* type.TextColumn */
    first_dose VARCHAR(64), /* type.TextColumn */
    dose FLOAT, /* type.FloatColumn */
    dose_units VARCHAR(32), /* type.TextColumn */
    sale_units VARCHAR(32), /* type.TextColumn */
    route VARCHAR(32), /* type.TextColumn */
    frequency VARCHAR(32), /* type.TextColumn */
    prn VARCHAR(32), /* type.TextColumn */
    start_date DATETIME, /* type.DateColumn */
    end_date DATETIME, /* type.DateColumn */
    ongoing BIT, /* type.BooleanColumn */
    duration INTEGER, /* type.IntegerColumn */
    duration_units VARCHAR(32), /* type.TextColumn */
    quantity INTEGER, /* type.IntegerColumn */
    num_refills INTEGER, /* type.IntegerColumn */
    allow_generic BIT, /* type.BooleanColumn */
    allow_substitutions BIT, /* type.BooleanColumn */
    notes VARCHAR(1024), /* type.TextColumn */
    sig VARCHAR(1024), /* type.TextColumn */
    status INTEGER, /* type.IntegerColumn */
    label BIT, /* type.BooleanColumn */
    label_language INTEGER, /* type.EnumerationIdRefColumn */
    signed BIT, /* type.BooleanColumn */

    CONSTRAINT FK_PATMED_PHARMACY_ID FOREIGN KEY (pharmacy_id) REFERENCES Org (org_id),
    CONSTRAINT FK_PATMED_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PATMED_MED_ID FOREIGN KEY (med_id) REFERENCES Org_Medication (med_id),
    CONSTRAINT FK_PATMED_PATIENT_ID FOREIGN KEY (patient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PATMED_LABEL_LANGUAGE FOREIGN KEY (label_language) REFERENCES Language_Type (id),
    CONSTRAINT FK_PATMED_PRESCRIBER_ID FOREIGN KEY (prescriber_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PATMED_RECORD_TYPE FOREIGN KEY (record_type) REFERENCES Medication_Record_Type (id)

    /* DELAYED: CONSTRAINT FK_PATMED_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Patient_Medication on Patient_Medication (system_id);
CREATE  INDEX PR_PatMed_patient_id on Patient_Medication (patient_id);

CREATE TABLE Patient_Referral
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    referral_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    patient_id VARCHAR(36), /* type.GuidTextColumn */
    referrer_id VARCHAR(36), /* type.GuidTextColumn */
    referee_id VARCHAR(36), /* type.GuidTextColumn */
    user_id VARCHAR(36), /* type.GuidTextColumn */
    request_date DATETIME, /* type.DateColumn */
    referral_urgency INTEGER, /* type.EnumerationIdRefColumn */
    code VARCHAR(60), /* type.TextColumn */
    speciality VARCHAR(512), /* type.TextColumn */
    referral_type_id INTEGER, /* type.EnumerationIdRefColumn */
    allowed_visits INTEGER, /* type.IntegerColumn */
    auth_number VARCHAR(64), /* type.TextColumn */
    referral_begin_date DATETIME, /* type.DateColumn */
    referral_end_date DATETIME, /* type.DateColumn */
    comm_type_id INTEGER, /* type.EnumerationIdRefColumn */
    completion_date DATETIME, /* type.DateTimeColumn */
    referral_status_id INTEGER, /* type.EnumerationIdRefColumn */
    referral_status_date DATETIME, /* type.DateColumn */
    referral_reason VARCHAR(512), /* type.TextColumn */
    comments VARCHAR(512), /* type.TextColumn */

    CONSTRAINT FK_PATREF_PATIENT_ID FOREIGN KEY (patient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PATREF_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PATREF_REFERRAL_URGENCY FOREIGN KEY (referral_urgency) REFERENCES Referral_Urgency (id),
    CONSTRAINT FK_PATREF_REFERRER_ID FOREIGN KEY (referrer_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PATREF_REFERRAL_STATUS_ID FOREIGN KEY (referral_status_id) REFERENCES Referral_Status (id),
    CONSTRAINT FK_PATREF_REFEREE_ID FOREIGN KEY (referee_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PATREF_USER_ID FOREIGN KEY (user_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PATREF_REFERRAL_TYPE_ID FOREIGN KEY (referral_type_id) REFERENCES Referral_Type (id),
    CONSTRAINT FK_PATREF_COMM_TYPE_ID FOREIGN KEY (comm_type_id) REFERENCES Referral_Communication (id)

    /* DELAYED: CONSTRAINT FK_PATREF_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Patient_Referral on Patient_Referral (referral_id);
CREATE  INDEX PR_PatRef_patient_id on Patient_Referral (patient_id);
CREATE  INDEX PR_PatRef_referrer_id on Patient_Referral (referrer_id);
CREATE  INDEX PR_PatRef_referee_id on Patient_Referral (referee_id);
CREATE  INDEX PR_PatRef_user_id on Patient_Referral (user_id);

CREATE TABLE Claim
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    claim_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    batch_id INTEGER, /* type.IntegerColumn */
    batch_date DATETIME, /* type.DateColumn */
    visit_type_id INTEGER, /* type.EnumerationIdRefColumn */
    claim_status_id INTEGER, /* type.EnumerationIdRefColumn */
    patient_id VARCHAR(36), /* type.GuidTextColumn */
    referral_id VARCHAR(36), /* type.GuidTextColumn */
    service_provider_id VARCHAR(36), /* type.GuidTextColumn */
    billing_provider_id VARCHAR(36), /* type.GuidTextColumn */
    service_facility_id VARCHAR(36), /* type.GuidTextColumn */
    billing_facility_id VARCHAR(36), /* type.GuidTextColumn */
    accident BIT, /* type.BooleanColumn */
    accident_state_id INTEGER, /* type.EnumerationIdRefColumn */
    accident_state VARCHAR(32), /* type.TextColumn */
    authorization_number VARCHAR(64), /* type.TextColumn */

    CONSTRAINT FK_CLM_ACCIDENT_STATE_ID FOREIGN KEY (accident_state_id) REFERENCES US_State_Type (id),
    CONSTRAINT FK_CLM_VISIT_TYPE_ID FOREIGN KEY (visit_type_id) REFERENCES Doctor_Visit_Type (id),
    CONSTRAINT FK_CLM_SERVICE_PROVIDER_ID FOREIGN KEY (service_provider_id) REFERENCES Person (person_id),
    CONSTRAINT FK_CLM_BILLING_PROVIDER_ID FOREIGN KEY (billing_provider_id) REFERENCES Person (person_id),
    CONSTRAINT FK_CLM_SERVICE_FACILITY_ID FOREIGN KEY (service_facility_id) REFERENCES Org (org_id),
    CONSTRAINT FK_CLM_PATIENT_ID FOREIGN KEY (patient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_CLM_REFERRAL_ID FOREIGN KEY (referral_id) REFERENCES Patient_Referral (referral_id),
    CONSTRAINT FK_CLM_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_CLM_BILLING_FACILITY_ID FOREIGN KEY (billing_facility_id) REFERENCES Org (org_id),
    CONSTRAINT FK_CLM_CLAIM_STATUS_ID FOREIGN KEY (claim_status_id) REFERENCES Claim_Status_Type (id)

    /* DELAYED: CONSTRAINT FK_CLM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Claim on Claim (claim_id);

CREATE TABLE Claim_Diagnosis
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    claim_id VARCHAR(36), /* type.GuidTextColumn */
    diag_code_type_id INTEGER, /* type.EnumerationIdRefColumn */
    diag_code VARCHAR(32), /* type.TextColumn */
    modifier VARCHAR(32), /* type.TextColumn */

    CONSTRAINT FK_CLMDIAG_CLAIM_ID FOREIGN KEY (claim_id) REFERENCES Claim (claim_id),
    CONSTRAINT FK_CLMDIAG_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_CLMDIAG_DIAG_CODE_TYPE_ID FOREIGN KEY (diag_code_type_id) REFERENCES Claim_Diagnosis_Code_Type (id)

    /* DELAYED: CONSTRAINT FK_CLMDIAG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Claim_Diagnosis on Claim_Diagnosis (system_id);
CREATE  INDEX PR_ClmDiag_claim_id on Claim_Diagnosis (claim_id);

CREATE TABLE Claim_DiagProc_Rel
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    diagnosis_id VARCHAR(36), /* type.GuidTextColumn */
    procedure_id VARCHAR(36), /* type.GuidTextColumn */
    comments VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_CLMDPREL_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_CLMDPREL_DIAGNOSIS_ID FOREIGN KEY (diagnosis_id) REFERENCES Claim_Diagnosis (system_id)

    /* DELAYED: CONSTRAINT FK_CLMDPREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_CLMDPREL_PROCEDURE_ID FOREIGN KEY (procedure_id) REFERENCES Claim_Procedure (system_id) (Claim_Procedure table not created yet) */
);
CREATE  INDEX PK_Claim_DiagProc_Rel on Claim_DiagProc_Rel (system_id);
CREATE  INDEX PR_ClmDPRel_diagnosis_id on Claim_DiagProc_Rel (diagnosis_id);
CREATE  INDEX PR_ClmDPRel_procedure_id on Claim_DiagProc_Rel (procedure_id);

CREATE TABLE Claim_Data_History
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    claim_id VARCHAR(36), /* type.GuidTextColumn */
    change_time DATETIME, /* type.DateTimeColumn */
    change_type_id INTEGER, /* type.EnumerationIdRefColumn */
    changed_field_id INTEGER, /* type.EnumerationIdRefColumn */
    field_old_value VARCHAR(256), /* type.TextColumn */
    field_new_value VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_CLMHIST_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_CLMHIST_CHANGED_FIELD_ID FOREIGN KEY (changed_field_id) REFERENCES Claim_Table_Field_Type (id),
    CONSTRAINT FK_CLMHIST_CHANGE_TYPE_ID FOREIGN KEY (change_type_id) REFERENCES Field_Change_Type (id),
    CONSTRAINT FK_CLMHIST_CLAIM_ID FOREIGN KEY (claim_id) REFERENCES Claim (claim_id)

    /* DELAYED: CONSTRAINT FK_CLMHIST_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Claim_Data_History on Claim_Data_History (system_id);
CREATE  INDEX PR_ClmHist_claim_id on Claim_Data_History (claim_id);

CREATE TABLE Claim_Procedure
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    claim_id VARCHAR(36), /* type.GuidTextColumn */
    proc_code_type_id INTEGER, /* type.EnumerationIdRefColumn */
    proc_code VARCHAR(32), /* type.TextColumn */
    modifier VARCHAR(32), /* type.TextColumn */
    date_begin_date DATETIME, /* type.DateColumn */
    date_end_date DATETIME, /* type.DateColumn */

    CONSTRAINT FK_CLMPROC_CLAIM_ID FOREIGN KEY (claim_id) REFERENCES Claim (claim_id),
    CONSTRAINT FK_CLMPROC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_CLMPROC_PROC_CODE_TYPE_ID FOREIGN KEY (proc_code_type_id) REFERENCES Claim_Procedure_Code_Type (id)

    /* DELAYED: CONSTRAINT FK_CLMPROC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Claim_Procedure on Claim_Procedure (system_id);
CREATE  INDEX PR_ClmProc_claim_id on Claim_Procedure (claim_id);

CREATE TABLE Claim_Data_Archive
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    claim_id VARCHAR(36), /* type.GuidTextColumn */
    x12_xml_data VARCHAR(2048), /* type.TextColumn */

    CONSTRAINT FK_CLMDATAARC_CLAIM_ID FOREIGN KEY (claim_id) REFERENCES Claim (claim_id),
    CONSTRAINT FK_CLMDATAARC_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_CLMDATAARC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Claim_Data_Archive on Claim_Data_Archive (system_id);
CREATE  INDEX PR_ClmDataArc_claim_id on Claim_Data_Archive (claim_id);

CREATE TABLE Action
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */

    CONSTRAINT FK_ACT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_ACT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);

CREATE TABLE Action_Patient_Comm
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    act_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    act_type_id INTEGER, /* type.EnumerationIdRefColumn */
    act_type VARCHAR(64), /* type.TextColumn */
    start_stamp DATETIME, /* type.DateTimeColumn */
    end_stamp DATETIME, /* type.DateTimeColumn */
    comments VARCHAR(1024), /* type.TextColumn */
    act_status_id INTEGER, /* type.EnumerationIdRefColumn */
    act_status VARCHAR(64), /* type.TextColumn */
    subject VARCHAR(256), /* type.TextColumn */
    initiator_id VARCHAR(36), /* type.GuidTextColumn */
    initiator VARCHAR(128), /* type.TextColumn */
    receptor_id VARCHAR(36), /* type.GuidTextColumn */
    receptor VARCHAR(128), /* type.TextColumn */
    rcpt_cont VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_ACTPATCOMM_ACT_STATUS_ID FOREIGN KEY (act_status_id) REFERENCES Patient_Comm_Status_Type (id),
    CONSTRAINT FK_ACTPATCOMM_RECEPTOR_ID FOREIGN KEY (receptor_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTPATCOMM_INITIATOR_ID FOREIGN KEY (initiator_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTPATCOMM_ACT_TYPE_ID FOREIGN KEY (act_type_id) REFERENCES Patient_Comm_Act_Type (id),
    CONSTRAINT FK_ACTPATCOMM_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_ACTPATCOMM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Action_Patient_Comm on Action_Patient_Comm (act_id);

CREATE TABLE Action_Diagnosis
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    act_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    act_type_id INTEGER, /* type.EnumerationIdRefColumn */
    act_type VARCHAR(64), /* type.TextColumn */
    start_stamp DATETIME, /* type.DateTimeColumn */
    end_stamp DATETIME, /* type.DateTimeColumn */
    comments VARCHAR(1024), /* type.TextColumn */
    patient_id VARCHAR(36), /* type.GuidTextColumn */
    patient VARCHAR(128), /* type.TextColumn */
    physician_id VARCHAR(36), /* type.GuidTextColumn */
    physician VARCHAR(128), /* type.TextColumn */
    diag_codetype_id INTEGER, /* type.EnumerationIdRefColumn */
    diag_code VARCHAR(32), /* type.TextColumn */
    diagnosis VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_ACTDIAG_DIAG_CODETYPE_ID FOREIGN KEY (diag_codetype_id) REFERENCES Claim_Diagnosis_Code_Type (id),
    CONSTRAINT FK_ACTDIAG_PHYSICIAN_ID FOREIGN KEY (physician_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTDIAG_PATIENT_ID FOREIGN KEY (patient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTDIAG_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ACTDIAG_ACT_TYPE_ID FOREIGN KEY (act_type_id) REFERENCES Diag_Term_Type (id)

    /* DELAYED: CONSTRAINT FK_ACTDIAG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Action_Diagnosis on Action_Diagnosis (act_id);

CREATE TABLE Action_Directive
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    act_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    act_type_id INTEGER, /* type.EnumerationIdRefColumn */
    act_type VARCHAR(64), /* type.TextColumn */
    start_stamp DATETIME, /* type.DateTimeColumn */
    end_stamp DATETIME, /* type.DateTimeColumn */
    comments VARCHAR(1024), /* type.TextColumn */
    patient_id VARCHAR(36), /* type.GuidTextColumn */
    patient VARCHAR(128), /* type.TextColumn */
    physician_id VARCHAR(36), /* type.GuidTextColumn */
    physician VARCHAR(128), /* type.TextColumn */
    issuer VARCHAR(64), /* type.TextColumn */
    reason VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_ACTDCTV_PHYSICIAN_ID FOREIGN KEY (physician_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTDCTV_PATIENT_ID FOREIGN KEY (patient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTDCTV_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ACTDCTV_ACT_TYPE_ID FOREIGN KEY (act_type_id) REFERENCES Directive_Act_Type (id)

    /* DELAYED: CONSTRAINT FK_ACTDCTV_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Action_Directive on Action_Directive (act_id);

CREATE TABLE Action_Ins_Verify
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    act_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    act_type_id INTEGER, /* type.EnumerationIdRefColumn */
    act_type VARCHAR(64), /* type.TextColumn */
    start_stamp DATETIME, /* type.DateTimeColumn */
    end_stamp DATETIME, /* type.DateTimeColumn */
    comments VARCHAR(1024), /* type.TextColumn */
    event_id VARCHAR(36), /* type.GuidTextColumn */
    person_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    effective_begin_date DATETIME, /* type.DateColumn */
    deductible MONEY, /* type.CurrencyColumn */
    deductible_met MONEY, /* type.CurrencyColumn */
    ovcopay MONEY, /* type.CurrencyColumn */
    labcopay MONEY, /* type.CurrencyColumn */
    xraycopay MONEY, /* type.CurrencyColumn */
    referral_required BIT, /* type.BooleanColumn */
    sep_copay_xray BIT, /* type.BooleanColumn */
    lab VARCHAR(128), /* type.TextColumn */
    provider_id VARCHAR(36), /* type.GuidTextColumn */
    coverage_req VARCHAR(128), /* type.TextColumn */
    coverage_on VARCHAR(128), /* type.TextColumn */
    referral_or_precert BIT, /* type.BooleanColumn */
    precert_phone VARCHAR(20), /* type.TextColumn */
    annual_pe_ww VARCHAR(128), /* type.TextColumn */
    gyn_exam VARCHAR(128), /* type.TextColumn */
    thin_prep_pap VARCHAR(128), /* type.TextColumn */
    depo_inj VARCHAR(128), /* type.TextColumn */
    iud VARCHAR(128), /* type.TextColumn */
    tubal_lig VARCHAR(128), /* type.TextColumn */
    surgery VARCHAR(128), /* type.TextColumn */
    flex_sig VARCHAR(128), /* type.TextColumn */
    output_xray VARCHAR(128), /* type.TextColumn */
    mammogram VARCHAR(128), /* type.TextColumn */
    amniocenteses VARCHAR(128), /* type.TextColumn */
    pelvic_ultrasound VARCHAR(128), /* type.TextColumn */
    fertility_test VARCHAR(128), /* type.TextColumn */
    circumcision VARCHAR(128), /* type.TextColumn */
    ins_rep_name VARCHAR(128), /* type.TextColumn */
    ins_verified_by VARCHAR(36), /* type.GuidTextColumn */
    ins_verify_date DATETIME, /* type.DateColumn */
    app_verified_by VARCHAR(36), /* type.GuidTextColumn */
    app_verify_date DATETIME, /* type.DateColumn */
    med_verified_by VARCHAR(36), /* type.GuidTextColumn */
    med_verify_date DATETIME, /* type.DateColumn */
    per_verified_by VARCHAR(36), /* type.GuidTextColumn */
    per_verify_date DATETIME, /* type.DateColumn */
    verify_action VARCHAR(64), /* type.TextColumn */
    owner_org_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */

    CONSTRAINT FK_ACTINSVFY_EVENT_ID FOREIGN KEY (event_id) REFERENCES Event (event_id),
    CONSTRAINT FK_ACTINSVFY_OWNER_ORG_ID FOREIGN KEY (owner_org_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ACTINSVFY_MED_VERIFIED_BY FOREIGN KEY (med_verified_by) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTINSVFY_PROVIDER_ID FOREIGN KEY (provider_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTINSVFY_ACT_TYPE_ID FOREIGN KEY (act_type_id) REFERENCES Action_Type (id),
    CONSTRAINT FK_ACTINSVFY_APP_VERIFIED_BY FOREIGN KEY (app_verified_by) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTINSVFY_PER_VERIFIED_BY FOREIGN KEY (per_verified_by) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTINSVFY_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ACTINSVFY_INS_VERIFIED_BY FOREIGN KEY (ins_verified_by) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTINSVFY_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id)

    /* DELAYED: CONSTRAINT FK_ACTINSVFY_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX ActInsVfy_person_id on Action_Ins_Verify (person_id);
CREATE  INDEX ActInsVfy_owner_org_id on Action_Ins_Verify (owner_org_id);
CREATE  INDEX PK_Action_Ins_Verify on Action_Ins_Verify (act_id);

CREATE TABLE Action_Provider_Comm
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    act_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    act_type_id INTEGER, /* type.EnumerationIdRefColumn */
    act_type VARCHAR(64), /* type.TextColumn */
    start_stamp DATETIME, /* type.DateTimeColumn */
    end_stamp DATETIME, /* type.DateTimeColumn */
    comments VARCHAR(1024), /* type.TextColumn */
    act_status_id INTEGER, /* type.EnumerationIdRefColumn */
    act_status VARCHAR(64), /* type.TextColumn */
    subject VARCHAR(256), /* type.TextColumn */
    initiator_id VARCHAR(36), /* type.GuidTextColumn */
    initiator VARCHAR(128), /* type.TextColumn */
    receptor_id VARCHAR(36), /* type.GuidTextColumn */
    receptor VARCHAR(128), /* type.TextColumn */
    rcpt_cont VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_ACTPRVCOMM_RECEPTOR_ID FOREIGN KEY (receptor_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTPRVCOMM_ACT_STATUS_ID FOREIGN KEY (act_status_id) REFERENCES Provider_Comm_Status_Type (id),
    CONSTRAINT FK_ACTPRVCOMM_ACT_TYPE_ID FOREIGN KEY (act_type_id) REFERENCES Provider_Comm_Act_Type (id),
    CONSTRAINT FK_ACTPRVCOMM_INITIATOR_ID FOREIGN KEY (initiator_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ACTPRVCOMM_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_ACTPRVCOMM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Action_Provider_Comm on Action_Provider_Comm (act_id);

CREATE TABLE Action_Procedure
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    act_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    act_type_id INTEGER, /* type.EnumerationIdRefColumn */
    act_type VARCHAR(64), /* type.TextColumn */
    start_stamp DATETIME, /* type.DateTimeColumn */
    end_stamp DATETIME, /* type.DateTimeColumn */
    comments VARCHAR(1024), /* type.TextColumn */
    patient_id VARCHAR(36), /* type.GuidTextColumn */
    patient VARCHAR(128), /* type.TextColumn */
    physician_id VARCHAR(36), /* type.GuidTextColumn */
    physician VARCHAR(128), /* type.TextColumn */
    proc_codetype_id INTEGER, /* type.EnumerationIdRefColumn */
    proc_code VARCHAR(32), /* type.TextColumn */
    procedure VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_PROCACT_PHYSICIAN_ID FOREIGN KEY (physician_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PROCACT_ACT_TYPE_ID FOREIGN KEY (act_type_id) REFERENCES Proc_Type (id),
    CONSTRAINT FK_PROCACT_PROC_CODETYPE_ID FOREIGN KEY (proc_codetype_id) REFERENCES Claim_Procedure_Code_Type (id),
    CONSTRAINT FK_PROCACT_PATIENT_ID FOREIGN KEY (patient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PROCACT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_PROCACT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Action_Procedure on Action_Procedure (act_id);

CREATE TABLE Action_Visit
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    act_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    act_type_id INTEGER, /* type.EnumerationIdRefColumn */
    act_type VARCHAR(64), /* type.TextColumn */
    start_stamp DATETIME, /* type.DateTimeColumn */
    end_stamp DATETIME, /* type.DateTimeColumn */
    comments VARCHAR(1024), /* type.TextColumn */
    patient_id VARCHAR(36), /* type.GuidTextColumn */
    patient VARCHAR(128), /* type.TextColumn */
    physician_id VARCHAR(36), /* type.GuidTextColumn */
    physician VARCHAR(128), /* type.TextColumn */
    reason VARCHAR(256), /* type.TextColumn */

    CONSTRAINT FK_VISITACT_PHYSICIAN_ID FOREIGN KEY (physician_id) REFERENCES Person (person_id),
    CONSTRAINT FK_VISITACT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_VISITACT_PATIENT_ID FOREIGN KEY (patient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_VISITACT_ACT_TYPE_ID FOREIGN KEY (act_type_id) REFERENCES Doctor_Visit_Type (id)

    /* DELAYED: CONSTRAINT FK_VISITACT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Action_Visit on Action_Visit (act_id);

CREATE TABLE Artifact_Association_Status
(
    id INTEGER PRIMARY KEY, /* type.EnumerationIdColumn */
    caption VARCHAR(96) NOT NULL, /* type.TextColumn */
    abbrev VARCHAR(32) /* type.TextColumn */
);
CREATE unique INDEX UNQ_ArfAsnSt_abbrev on Artifact_Association_Status (abbrev);
CREATE  INDEX PK_Artifact_Association_Status on Artifact_Association_Status (id);

CREATE TABLE Artifact
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    artifact_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    artifact_id_alias VARCHAR(64), /* type.TextColumn */
    message_digest VARCHAR(32), /* type.TextColumn */
    mime_type VARCHAR(128), /* type.TextColumn */
    header VARCHAR(4000), /* type.TextColumn */
    spec_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    spec_subtype VARCHAR(128), /* type.TextColumn */
    source_id VARCHAR(255), /* type.TextColumn */
    source_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    source_subtype VARCHAR(255), /* type.TextColumn */
    source_system VARCHAR(255), /* type.TextColumn */
    name VARCHAR(1024) NOT NULL, /* type.TextColumn */
    description VARCHAR(4000), /* type.TextColumn */
    orig_stamp DATETIME, /* type.DateTimeColumn */
    recv_stamp DATETIME, /* type.DateTimeColumn */
    content_uri VARCHAR(512), /* type.TextColumn */
    content_small VARCHAR(4000), /* type.TextColumn */
    content_large TEXT, /* type.DataBlockColumn */

    CONSTRAINT FK_ARTF_SOURCE_TYPE_ID FOREIGN KEY (source_type_id) REFERENCES Artifact_Source_Type (id),
    CONSTRAINT FK_ARTF_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ARTF_SPEC_TYPE_ID FOREIGN KEY (spec_type_id) REFERENCES Artifact_Type (id)

    /* DELAYED: CONSTRAINT FK_ARTF_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX Artf_spec_type_id on Artifact (spec_type_id);
CREATE  INDEX Artf_spec_subtype on Artifact (spec_subtype);
CREATE  INDEX Artf_source_id on Artifact (source_id);
CREATE  INDEX Artf_source_type_id on Artifact (source_type_id);
CREATE  INDEX Artf_source_subtype on Artifact (source_subtype);
CREATE  INDEX Artf_source_system on Artifact (source_system);
CREATE  INDEX PK_Artifact on Artifact (artifact_id);

CREATE TABLE Artifact_State
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    upd_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    upd_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_state_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    artifact_id VARCHAR(36), /* type.GuidTextColumn */
    artifact_id_alias VARCHAR(64), /* type.TextColumn */
    message_digest VARCHAR(32), /* type.TextColumn */
    mime_type VARCHAR(128), /* type.TextColumn */
    header VARCHAR(4000), /* type.TextColumn */
    spec_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    spec_subtype VARCHAR(128), /* type.TextColumn */
    source_id VARCHAR(255), /* type.TextColumn */
    source_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    source_subtype VARCHAR(255), /* type.TextColumn */
    source_system VARCHAR(255), /* type.TextColumn */
    name VARCHAR(1024) NOT NULL, /* type.TextColumn */
    description VARCHAR(4000), /* type.TextColumn */
    orig_stamp DATETIME, /* type.DateTimeColumn */
    recv_stamp DATETIME, /* type.DateTimeColumn */
    content_uri VARCHAR(512), /* type.TextColumn */
    content_small VARCHAR(4000), /* type.TextColumn */
    content_large TEXT, /* type.DataBlockColumn */

    CONSTRAINT FK_ARTFST_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ARTFST_SPEC_TYPE_ID FOREIGN KEY (spec_type_id) REFERENCES Artifact_Type (id),
    CONSTRAINT FK_ARTFST_ARTIFACT_ID FOREIGN KEY (artifact_id) REFERENCES Artifact (artifact_id),
    CONSTRAINT FK_ARTFST_REC_STATE_ID FOREIGN KEY (rec_state_id) REFERENCES Record_State (id),
    CONSTRAINT FK_ARTFST_SOURCE_TYPE_ID FOREIGN KEY (source_type_id) REFERENCES Artifact_Source_Type (id)

    /* DELAYED: CONSTRAINT FK_ARTFST_UPD_SESS_ID FOREIGN KEY (upd_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
    /* DELAYED: CONSTRAINT FK_ARTFST_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX ArtfSt_spec_type_id on Artifact_State (spec_type_id);
CREATE  INDEX ArtfSt_spec_subtype on Artifact_State (spec_subtype);
CREATE  INDEX ArtfSt_source_id on Artifact_State (source_id);
CREATE  INDEX ArtfSt_source_type_id on Artifact_State (source_type_id);
CREATE  INDEX ArtfSt_source_subtype on Artifact_State (source_subtype);
CREATE  INDEX ArtfSt_source_system on Artifact_State (source_system);
CREATE  INDEX PK_Artifact_State on Artifact_State (system_id);
CREATE  INDEX PR_ArtfSt_artifact_id on Artifact_State (artifact_id);

CREATE TABLE Artifact_Association
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    assn_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    assn_status_id INTEGER, /* type.RecordStatusIdColumn */
    assn_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    assn_sequence INTEGER, /* type.IntegerColumn */
    artifact_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    assoc_artifact_id VARCHAR(36), /* type.GuidTextColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    assn_data_a VARCHAR(1024), /* type.TextColumn */
    assn_data_b VARCHAR(1024), /* type.TextColumn */
    assn_data_c VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_ARFASSN_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ARFASSN_ASSN_TYPE_ID FOREIGN KEY (assn_type_id) REFERENCES Artifact_Association_Type (id),
    CONSTRAINT FK_ARFASSN_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ARFASSN_ASSN_STATUS_ID FOREIGN KEY (assn_status_id) REFERENCES Artifact_Association_Status (id),
    CONSTRAINT FK_ARFASSN_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ARFASSN_ASSOC_ARTIFACT_ID FOREIGN KEY (assoc_artifact_id) REFERENCES Artifact (artifact_id),
    CONSTRAINT FK_ARFASSN_ARTIFACT_ID FOREIGN KEY (artifact_id) REFERENCES Artifact (artifact_id)

    /* DELAYED: CONSTRAINT FK_ARFASSN_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX ArfAssn_assn_status_id on Artifact_Association (assn_status_id);
CREATE  INDEX ArfAssn_assn_type_id on Artifact_Association (assn_type_id);
CREATE  INDEX ArfAssn_assn_sequence on Artifact_Association (assn_sequence);
CREATE  INDEX ArfAssn_artifact_id on Artifact_Association (artifact_id);
CREATE  INDEX ArfAssn_assoc_artifact_id on Artifact_Association (assoc_artifact_id);
CREATE  INDEX ArfAssn_person_id on Artifact_Association (person_id);
CREATE  INDEX ArfAssn_org_id on Artifact_Association (org_id);
CREATE  INDEX PK_Artifact_Association on Artifact_Association (assn_id);
CREATE  INDEX PR_ArfAssn_artifact_id on Artifact_Association (artifact_id);

CREATE TABLE Artifact_Keyword
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    keyword_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    artifact_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    keyword VARCHAR(512), /* type.TextColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */

    CONSTRAINT FK_ARFKEYW_ARTIFACT_ID FOREIGN KEY (artifact_id) REFERENCES Artifact (artifact_id),
    CONSTRAINT FK_ARFKEYW_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ARFKEYW_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ARFKEYW_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id)

    /* DELAYED: CONSTRAINT FK_ARFKEYW_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX ArfKeyw_keyword on Artifact_Keyword (keyword);
CREATE  INDEX ArfKeyw_person_id on Artifact_Keyword (person_id);
CREATE  INDEX ArfKeyw_org_id on Artifact_Keyword (org_id);
CREATE  INDEX PK_Artifact_Keyword on Artifact_Keyword (keyword_id);
CREATE  INDEX PR_ArfKeyw_artifact_id on Artifact_Keyword (artifact_id);

CREATE TABLE Artifact_Event
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    event_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    event_type_id INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    event_status VARCHAR(1024), /* type.TextColumn */
    artifact_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    rel_artifact_id VARCHAR(36), /* type.GuidTextColumn */
    person_id VARCHAR(36), /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    event_info VARCHAR(1024), /* type.TextColumn */
    event_info_extra VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_ARFEVENT_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id),
    CONSTRAINT FK_ARFEVENT_EVENT_TYPE_ID FOREIGN KEY (event_type_id) REFERENCES Artifact_Event_Type (id),
    CONSTRAINT FK_ARFEVENT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_ARFEVENT_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_ARFEVENT_REL_ARTIFACT_ID FOREIGN KEY (rel_artifact_id) REFERENCES Artifact (artifact_id),
    CONSTRAINT FK_ARFEVENT_ARTIFACT_ID FOREIGN KEY (artifact_id) REFERENCES Artifact (artifact_id)

    /* DELAYED: CONSTRAINT FK_ARFEVENT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX ArfEvent_event_type_id on Artifact_Event (event_type_id);
CREATE  INDEX ArfEvent_person_id on Artifact_Event (person_id);
CREATE  INDEX ArfEvent_org_id on Artifact_Event (org_id);
CREATE  INDEX PK_Artifact_Event on Artifact_Event (event_id);
CREATE  INDEX PR_ArfEvent_artifact_id on Artifact_Event (artifact_id);

CREATE TABLE Catalog
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */

    CONSTRAINT FK_CATALOG_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_CATALOG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);

CREATE TABLE Service_Catalog
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    catalog_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    catalog_name VARCHAR(128), /* type.TextColumn */
    ins_plan_id VARCHAR(36), /* type.GuidTextColumn */

    CONSTRAINT FK_SVCCAT_INS_PLAN_ID FOREIGN KEY (ins_plan_id) REFERENCES Ins_Plan (plan_id),
    CONSTRAINT FK_SVCCAT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_SVCCAT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Service_Catalog on Service_Catalog (catalog_id);

CREATE TABLE Service_Catalog_Item
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    catalog_id VARCHAR(36), /* type.GuidTextColumn */
    item_id VARCHAR(36), /* type.GuidTextColumn */
    item VARCHAR(256), /* type.TextColumn */
    cost MONEY, /* type.CurrencyColumn */

    CONSTRAINT FK_SVCCATITM_ITEM_ID FOREIGN KEY (item_id) REFERENCES Org_Service (service_id),
    CONSTRAINT FK_SVCCATITM_CATALOG_ID FOREIGN KEY (catalog_id) REFERENCES Service_Catalog (catalog_id),
    CONSTRAINT FK_SVCCATITM_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id)

    /* DELAYED: CONSTRAINT FK_SVCCATITM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Service_Catalog_Item on Service_Catalog_Item (system_id);
CREATE  INDEX PR_SvcCatItm_catalog_id on Service_Catalog_Item (catalog_id);

CREATE TABLE Product_Catalog
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    catalog_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    catalog_name VARCHAR(128), /* type.TextColumn */
    ins_plan_id VARCHAR(36), /* type.GuidTextColumn */

    CONSTRAINT FK_PRDCAT_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PRDCAT_INS_PLAN_ID FOREIGN KEY (ins_plan_id) REFERENCES Ins_Plan (plan_id)

    /* DELAYED: CONSTRAINT FK_PRDCAT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Product_Catalog on Product_Catalog (catalog_id);

CREATE TABLE Product_Catalog_Item
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    catalog_id VARCHAR(36), /* type.GuidTextColumn */
    item_id VARCHAR(36), /* type.GuidTextColumn */
    item VARCHAR(256), /* type.TextColumn */
    cost MONEY, /* type.CurrencyColumn */

    CONSTRAINT FK_PRDCATITM_CATALOG_ID FOREIGN KEY (catalog_id) REFERENCES Product_Catalog (catalog_id),
    CONSTRAINT FK_PRDCATITM_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_PRDCATITM_ITEM_ID FOREIGN KEY (item_id) REFERENCES Org_Product (product_id)

    /* DELAYED: CONSTRAINT FK_PRDCATITM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Product_Catalog_Item on Product_Catalog_Item (system_id);
CREATE  INDEX PR_PrdCatItm_catalog_id on Product_Catalog_Item (catalog_id);

CREATE TABLE Message
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    message_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    parent_id VARCHAR(36), /* type.GuidTextColumn */
    subject VARCHAR(512), /* type.TextColumn */
    sender_id VARCHAR(36), /* type.GuidTextColumn */
    content VARCHAR(2048), /* type.TextColumn */

    CONSTRAINT FK_MESG_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_MESG_SENDER_ID FOREIGN KEY (sender_id) REFERENCES Person (person_id),
    CONSTRAINT FK_MESG_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Artifact (artifact_id)

    /* DELAYED: CONSTRAINT FK_MESG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Message on Message (message_id);
CREATE  INDEX PR_Mesg_parent_id on Message (parent_id);

CREATE TABLE Message_Recipient
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    message_id VARCHAR(36), /* type.GuidTextColumn */
    recipient_type_id INTEGER, /* type.EnumerationIdRefColumn */
    reception_type_id INTEGER, /* type.EnumerationIdRefColumn */
    recipient_id VARCHAR(36), /* type.GuidTextColumn */

    CONSTRAINT FK_MESGRECP_RECIPIENT_ID FOREIGN KEY (recipient_id) REFERENCES Person (person_id),
    CONSTRAINT FK_MESGRECP_RECIPIENT_TYPE_ID FOREIGN KEY (recipient_type_id) REFERENCES Message_Recipient_Type (id),
    CONSTRAINT FK_MESGRECP_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_MESGRECP_MESSAGE_ID FOREIGN KEY (message_id) REFERENCES Message (message_id),
    CONSTRAINT FK_MESGRECP_RECEPTION_TYPE_ID FOREIGN KEY (reception_type_id) REFERENCES Message_Reception_Type (id)

    /* DELAYED: CONSTRAINT FK_MESGRECP_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Message_Recipient on Message_Recipient (system_id);
CREATE  INDEX PR_MesgRecp_message_id on Message_Recipient (message_id);

CREATE TABLE Message_Attach
(
    cr_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    cr_sess_id VARCHAR(36), /* type.GuidTextColumn */
    rec_stat_id INTEGER NOT NULL, /* type.RecordStatusIdColumn */
    system_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    message_id VARCHAR(36), /* type.GuidTextColumn */
    attachment_type_id INTEGER, /* type.EnumerationIdRefColumn */
    document_id VARCHAR(36), /* type.GuidTextColumn */
    forwarded_id VARCHAR(36), /* type.GuidTextColumn */

    CONSTRAINT FK_MESGATCH_ATTACHMENT_TYPE_ID FOREIGN KEY (attachment_type_id) REFERENCES Message_Attachment_Type (id),
    CONSTRAINT FK_MESGATCH_REC_STAT_ID FOREIGN KEY (rec_stat_id) REFERENCES Record_Status (id),
    CONSTRAINT FK_MESGATCH_DOCUMENT_ID FOREIGN KEY (document_id) REFERENCES Artifact (artifact_id),
    CONSTRAINT FK_MESGATCH_FORWARDED_ID FOREIGN KEY (forwarded_id) REFERENCES Message (message_id),
    CONSTRAINT FK_MESGATCH_MESSAGE_ID FOREIGN KEY (message_id) REFERENCES Message (message_id)

    /* DELAYED: CONSTRAINT FK_MESGATCH_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id) (Person_Session table not created yet) */
);
CREATE  INDEX PK_Message_Attach on Message_Attach (system_id);
CREATE  INDEX PR_MesgAtch_message_id on Message_Attach (message_id);

CREATE TABLE Person_Session
(
    session_id VARCHAR(36) PRIMARY KEY, /* type.GuidColumn */
    person_id VARCHAR(36) NOT NULL, /* type.GuidTextColumn */
    org_id VARCHAR(36), /* type.GuidTextColumn */
    remote_host VARCHAR(128), /* type.TextColumn */
    remote_addr VARCHAR(32) NOT NULL, /* type.TextColumn */
    first_access DATETIME NOT NULL, /* type.DateTimeColumn */
    last_access DATETIME NOT NULL, /* type.DateTimeColumn */

    CONSTRAINT FK_PERSESS_PERSON_ID FOREIGN KEY (person_id) REFERENCES Person (person_id),
    CONSTRAINT FK_PERSESS_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id)
);
CREATE  INDEX PerSess_person_id on Person_Session (person_id);
CREATE  INDEX PK_Person_Session on Person_Session (session_id);
CREATE  INDEX PR_PerSess_person_id on Person_Session (person_id);

CREATE TABLE PersonSession_Activity
(
    session_id VARCHAR(36), /* type.GuidTextColumn */
    activity_type INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    activity_stamp DATETIME NOT NULL, /* type.DateTimeColumn */
    action_type INTEGER NOT NULL, /* type.EnumerationIdRefColumn */
    action_scope VARCHAR(48), /* type.TextColumn */
    action_key VARCHAR(48), /* type.TextColumn */
    detail_level INTEGER, /* type.IntegerColumn */
    activity_data VARCHAR(1024), /* type.TextColumn */

    CONSTRAINT FK_PERSESSACT_ACTION_TYPE FOREIGN KEY (action_type) REFERENCES Session_Action_Type (id),
    CONSTRAINT FK_PERSESSACT_ACTIVITY_TYPE FOREIGN KEY (activity_type) REFERENCES Session_Activity_Type (id),
    CONSTRAINT FK_PERSESSACT_SESSION_ID FOREIGN KEY (session_id) REFERENCES Person_Session (session_id)
);
CREATE  INDEX PerSessAct_session_id on PersonSession_Activity (session_id);
CREATE  INDEX PerSessAct_activity_stamp on PersonSession_Activity (activity_stamp);
CREATE  INDEX PerSessAct_detail_level on PersonSession_Activity (detail_level);
CREATE  INDEX PR_PerSessAct_session_id on PersonSession_Activity (session_id);

CREATE TABLE PersonSession_View_Count
(
    session_id VARCHAR(36), /* type.GuidTextColumn */
    view_init DATETIME NOT NULL, /* type.DateTimeColumn */
    view_latest DATETIME NOT NULL, /* type.DateTimeColumn */
    view_scope VARCHAR(48), /* type.TextColumn */
    view_key VARCHAR(48), /* type.TextColumn */
    view_count INTEGER, /* type.IntegerColumn */
    view_caption VARCHAR(255), /* type.TextColumn */
    view_arl VARCHAR(255), /* type.TextColumn */

    CONSTRAINT FK_PERSESSCNT_SESSION_ID FOREIGN KEY (session_id) REFERENCES Person_Session (session_id)
);
CREATE  INDEX PerSessCnt_session_id on PersonSession_View_Count (session_id);
CREATE  INDEX PerSessCnt_view_init on PersonSession_View_Count (view_init);
CREATE  INDEX PR_PerSessCnt_session_id on PersonSession_View_Count (session_id);

ALTER TABLE Action_Provider_Comm ADD CONSTRAINT FK_ACTPRVCOMM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Artifact_State ADD CONSTRAINT FK_ARTFST_UPD_SESS_ID FOREIGN KEY (upd_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Asset ADD CONSTRAINT FK_AST_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE InsProduct_Address ADD CONSTRAINT FK_INSPRDADDR_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Classification ADD CONSTRAINT FK_ORGCLASS_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Asset_Maint ADD CONSTRAINT FK_ASTMAINT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Product_Catalog ADD CONSTRAINT FK_PRDCAT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Classification ADD CONSTRAINT FK_PERCLASS_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Artifact ADD CONSTRAINT FK_ARTF_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Event_Resource ADD CONSTRAINT FK_EVRSRC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Org_Relationship ADD CONSTRAINT FK_ORGREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Product ADD CONSTRAINT FK_OPRD_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Artifact_Association ADD CONSTRAINT FK_ARFASSN_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Identifier ADD CONSTRAINT FK_PERID_ID_TYPE_ID FOREIGN KEY (id_type_id) REFERENCES Org_PersonId_Src_Type (system_id);
ALTER TABLE Visit_Line_Item ADD CONSTRAINT FK_VISLNITM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Event ADD CONSTRAINT FK_EVENT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Relationship_Map ADD CONSTRAINT FK_PERRELMP_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Inv_Visit ADD CONSTRAINT FK_ORGINVVIS_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Message ADD CONSTRAINT FK_MESG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Sch_Template ADD CONSTRAINT FK_SCTMPL_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE PersonOrg_Relationship ADD CONSTRAINT FK_PEOREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Action ADD CONSTRAINT FK_ACT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Template_Resource ADD CONSTRAINT FK_TMPLRSRC_ASSET_ID FOREIGN KEY (asset_id) REFERENCES Asset (asset_id);
ALTER TABLE Org_Address ADD CONSTRAINT FK_ORGADR_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE InsProduct_Contact ADD CONSTRAINT FK_INSPRDCONT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Event_Resource ADD CONSTRAINT FK_EVRSRC_ASSET_ID FOREIGN KEY (asset_id) REFERENCES Asset (asset_id);
ALTER TABLE Catalog ADD CONSTRAINT FK_CATALOG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Patient_Referral ADD CONSTRAINT FK_PATREF_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Service ADD CONSTRAINT FK_OSVC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Message_Attach ADD CONSTRAINT FK_MESGATCH_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE InsPlan_Contact ADD CONSTRAINT FK_INSPLNCONT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE InsPolicy_Coverage ADD CONSTRAINT FK_INSPOLCOV_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Product_Catalog_Item ADD CONSTRAINT FK_PRDCATITM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Industry ADD CONSTRAINT FK_OIND_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Insurance ADD CONSTRAINT FK_PERINS_GUAR_REL_ID FOREIGN KEY (guar_rel_id) REFERENCES Person_Relationship (system_id);
ALTER TABLE Ins_Plan ADD CONSTRAINT FK_INSPLN_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Appt_Type_Resource ADD CONSTRAINT FK_APTYRSRC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Role_Declaration ADD CONSTRAINT FK_ORGRLDC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Message_Recipient ADD CONSTRAINT FK_MESGRECP_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Artifact_State ADD CONSTRAINT FK_ARTFST_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Claim ADD CONSTRAINT FK_CLM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Inv_Claim_Rel ADD CONSTRAINT FK_ORGINVCLMREL_INVOICE_ID FOREIGN KEY (invoice_id) REFERENCES Org_Inv_Visit (invoice_id);
ALTER TABLE Action_Procedure ADD CONSTRAINT FK_PROCACT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Staff_License ADD CONSTRAINT FK_STFLIC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Org_PersonId_Src_Type ADD CONSTRAINT FK_OPERSRCIDTY_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Claim_DiagProc_Rel ADD CONSTRAINT FK_CLMDPREL_PROCEDURE_ID FOREIGN KEY (procedure_id) REFERENCES Claim_Procedure (system_id);
ALTER TABLE Ins_Product ADD CONSTRAINT FK_INSPRD_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Template_Resource ADD CONSTRAINT FK_TMPLRSRC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Identifier ADD CONSTRAINT FK_PERID_SOURCE_TYPE_ID FOREIGN KEY (source_type_id) REFERENCES Org_PersonId_Src_Type (system_id);
ALTER TABLE InsPlan_Address ADD CONSTRAINT FK_INSPLNADDR_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Role ADD CONSTRAINT FK_PERRL_ROLE_NAME_ID FOREIGN KEY (role_name_id) REFERENCES Org_Role_Declaration (role_name_id);
ALTER TABLE Appt_Chain_Entry ADD CONSTRAINT FK_APCHAINE_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Service_Catalog ADD CONSTRAINT FK_SVCCAT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE PersonOrg_Relationship ADD CONSTRAINT FK_PEOREL_REL_ENTITY_ID FOREIGN KEY (rel_entity_id) REFERENCES Org (org_id);
ALTER TABLE Action_Visit ADD CONSTRAINT FK_VISITACT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Artifact_Event ADD CONSTRAINT FK_ARFEVENT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Staff_License ADD CONSTRAINT FK_STFLIC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Insurance ADD CONSTRAINT FK_PERINS_INS_REL_ID FOREIGN KEY (ins_rel_id) REFERENCES Person_Relationship (system_id);
ALTER TABLE Person_Contact ADD CONSTRAINT FK_PERCONT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Claim_DiagProc_Rel ADD CONSTRAINT FK_CLMDPREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Action_Directive ADD CONSTRAINT FK_ACTDCTV_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Login ADD CONSTRAINT FK_PERLG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE InsPlan_Coverage ADD CONSTRAINT FK_INSPLNCOV_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org ADD CONSTRAINT FK_ORG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Template_Resource ADD CONSTRAINT FK_TMPLRSRC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Claim_Data_Archive ADD CONSTRAINT FK_CLMDATAARC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Event ADD CONSTRAINT FK_EVENT_PARENT_ID FOREIGN KEY (parent_id) REFERENCES Event (event_id);
ALTER TABLE Event ADD CONSTRAINT FK_EVENT_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Org_Appt_Type ADD CONSTRAINT FK_ORGAPTY_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Appt_Type ADD CONSTRAINT FK_ORGAPTY_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Staff_Benefit ADD CONSTRAINT FK_STFBEN_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Relationship ADD CONSTRAINT FK_PERREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Patient_Indication ADD CONSTRAINT FK_PATINDIC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Appt_Chain_Entry ADD CONSTRAINT FK_APCHAINE_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person ADD CONSTRAINT FK_PER_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Ins_Plan ADD CONSTRAINT FK_INSPLN_PRODUCT_ID FOREIGN KEY (product_id) REFERENCES Ins_Product (product_id);
ALTER TABLE Org_Sch_Template ADD CONSTRAINT FK_SCTMPL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Contact ADD CONSTRAINT FK_ORGCNT_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Event_Attendee ADD CONSTRAINT FK_EVATNDEE_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Note ADD CONSTRAINT FK_PERNOTE_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Identifier ADD CONSTRAINT FK_ORGID_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Ins_Policy ADD CONSTRAINT FK_INSPLCY_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Staff_Benefit ADD CONSTRAINT FK_STFBEN_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Claim_Diagnosis ADD CONSTRAINT FK_CLMDIAG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Action_Diagnosis ADD CONSTRAINT FK_ACTDIAG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Classification ADD CONSTRAINT FK_PERCLASS_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Org_Relationship_Map ADD CONSTRAINT FK_ORGRELMP_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Inv_Visit ADD CONSTRAINT FK_ORGINVVIS_ACT_ID FOREIGN KEY (act_id) REFERENCES Action_Visit (act_id);
ALTER TABLE Org_Note ADD CONSTRAINT FK_ONOTE_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Insurance ADD CONSTRAINT FK_PERINS_POLICY_ID FOREIGN KEY (policy_id) REFERENCES Ins_Policy (policy_id);
ALTER TABLE Org_Inv_Claim_Rel ADD CONSTRAINT FK_ORGINVCLMREL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Flag ADD CONSTRAINT FK_PERFLG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE InsProduct_Coverage ADD CONSTRAINT FK_INSPRDCOV_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Language ADD CONSTRAINT FK_PERLANG_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Identifier ADD CONSTRAINT FK_PERID_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Insurance ADD CONSTRAINT FK_PERINS_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Identifier ADD CONSTRAINT FK_PERID_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Service_Catalog_Item ADD CONSTRAINT FK_SVCCATITM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Appt_Type_Resource ADD CONSTRAINT FK_APTYRSRC_ASSET_ID FOREIGN KEY (asset_id) REFERENCES Asset (asset_id);
ALTER TABLE Artifact_Keyword ADD CONSTRAINT FK_ARFKEYW_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Appt_Type_Resource ADD CONSTRAINT FK_APTYRSRC_ORG_ID FOREIGN KEY (org_id) REFERENCES Org (org_id);
ALTER TABLE Action_Ins_Verify ADD CONSTRAINT FK_ACTINSVFY_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Action_Patient_Comm ADD CONSTRAINT FK_ACTPATCOMM_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Ethnicity ADD CONSTRAINT FK_PERETH_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Patient_Medication ADD CONSTRAINT FK_PATMED_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Address ADD CONSTRAINT FK_PERADDR_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Claim_Data_History ADD CONSTRAINT FK_CLMHIST_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Org_Inv_Claim_Rel ADD CONSTRAINT FK_ORGINVCLMREL_CLAIM_ID FOREIGN KEY (claim_id) REFERENCES Claim (claim_id);
ALTER TABLE Claim_Procedure ADD CONSTRAINT FK_CLMPROC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Event_Resource ADD CONSTRAINT FK_EVRSRC_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE PersonOrg_Relationship_Map ADD CONSTRAINT FK_PERORELMP_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);
ALTER TABLE Person_Role ADD CONSTRAINT FK_PERRL_CR_SESS_ID FOREIGN KEY (cr_sess_id) REFERENCES Person_Session (session_id);

insert into Lookup_Result_Type (id, caption, abbrev) values (0, 'ID', NULL);
insert into Lookup_Result_Type (id, caption, abbrev) values (1, 'Caption', NULL);
insert into Lookup_Result_Type (id, caption, abbrev) values (2, 'Abbreviation', NULL);

insert into Record_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Record_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Record_Status (id, caption, abbrev) values (99, 'Unknown', 'U');

insert into Record_State (id, caption, abbrev) values (0, 'Past', 'P');
insert into Record_State (id, caption, abbrev) values (1, 'Current', 'C');

insert into Contact_Method_Type (id, caption, abbrev) values (0, 'Telephone: Assistant', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (1, 'Telephone: Business', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (2, 'Telephone: Callback', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (3, 'Telephone: Car', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (4, 'Telephone: Company', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (5, 'Telephone: Home', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (6, 'Telephone: Home 2', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (7, 'Telephone: ISDN', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (8, 'Telephone: Mobile', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (9, 'Telephone: Other', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (10, 'Telephone: Work', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (11, 'Fax: Business', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (12, 'Fax: Home', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (13, 'Fax: Other', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (14, 'Pager', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (15, 'Radio', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (16, 'Telex', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (17, 'TTY/TDD', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (18, 'Email: Business', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (19, 'Email: Home', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (20, 'Email: Other', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (21, 'Email: Primary', NULL);
insert into Contact_Method_Type (id, caption, abbrev) values (22, 'URL', NULL);

insert into Contact_Address_Type (id, caption, abbrev) values (0, 'Primary', NULL);
insert into Contact_Address_Type (id, caption, abbrev) values (1, 'Secondary', NULL);
insert into Contact_Address_Type (id, caption, abbrev) values (2, 'Alternate', NULL);
insert into Contact_Address_Type (id, caption, abbrev) values (3, 'Billing', NULL);
insert into Contact_Address_Type (id, caption, abbrev) values (4, 'Other', NULL);

insert into Contact_Telephone_Type (id, caption, abbrev) values (0, 'Assistant', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (1, 'Business', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (2, 'Business 2', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (3, 'Business Fax', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (4, 'Callback', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (5, 'Car', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (6, 'Company', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (7, 'Home', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (8, 'Home 2', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (9, 'Home Fax', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (10, 'ISDN', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (11, 'Mobile', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (12, 'Other Fax', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (13, 'Pager', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (14, 'Primary', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (15, 'Radio', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (16, 'Telex', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (17, 'TTY/TDD', NULL);
insert into Contact_Telephone_Type (id, caption, abbrev) values (18, 'Other', NULL);

insert into Contact_Email_Type (id, caption, abbrev) values (0, 'Business', NULL);
insert into Contact_Email_Type (id, caption, abbrev) values (1, 'Home', NULL);
insert into Contact_Email_Type (id, caption, abbrev) values (2, 'Other', NULL);
insert into Contact_Email_Type (id, caption, abbrev) values (3, 'Primary', NULL);

insert into Action_Type (id, caption, abbrev) values (0, 'Billing', NULL);
insert into Action_Type (id, caption, abbrev) values (1, 'Clinical', NULL);
insert into Action_Type (id, caption, abbrev) values (2, 'Document', NULL);
insert into Action_Type (id, caption, abbrev) values (3, 'Financial', NULL);
insert into Action_Type (id, caption, abbrev) values (4, 'Other', NULL);

insert into Patient_Comm_Act_Type (id, caption, abbrev) values (0, 'Telephone', NULL);
insert into Patient_Comm_Act_Type (id, caption, abbrev) values (1, 'Fax', NULL);
insert into Patient_Comm_Act_Type (id, caption, abbrev) values (2, 'Page', NULL);
insert into Patient_Comm_Act_Type (id, caption, abbrev) values (3, 'E-Mail', NULL);
insert into Patient_Comm_Act_Type (id, caption, abbrev) values (4, 'Delivery', NULL);

insert into Patient_Comm_Status_Type (id, caption, abbrev) values (0, 'Successful', NULL);
insert into Patient_Comm_Status_Type (id, caption, abbrev) values (1, 'Voicemail', NULL);
insert into Patient_Comm_Status_Type (id, caption, abbrev) values (2, 'Left a Message', NULL);
insert into Patient_Comm_Status_Type (id, caption, abbrev) values (3, 'Other', NULL);

insert into Provider_Comm_Act_Type (id, caption, abbrev) values (0, 'Telephone', NULL);
insert into Provider_Comm_Act_Type (id, caption, abbrev) values (1, 'Fax', NULL);
insert into Provider_Comm_Act_Type (id, caption, abbrev) values (2, 'Page', NULL);
insert into Provider_Comm_Act_Type (id, caption, abbrev) values (3, 'E-Mail', NULL);
insert into Provider_Comm_Act_Type (id, caption, abbrev) values (4, 'Delivery', NULL);
insert into Provider_Comm_Act_Type (id, caption, abbrev) values (5, 'Mail', NULL);

insert into Provider_Comm_Status_Type (id, caption, abbrev) values (0, 'Successful', NULL);
insert into Provider_Comm_Status_Type (id, caption, abbrev) values (1, 'Voicemail', NULL);
insert into Provider_Comm_Status_Type (id, caption, abbrev) values (2, 'Left a Message', NULL);
insert into Provider_Comm_Status_Type (id, caption, abbrev) values (3, 'Returned Mail', NULL);
insert into Provider_Comm_Status_Type (id, caption, abbrev) values (4, 'Other', NULL);

insert into Directive_Act_Type (id, caption, abbrev) values (0, 'Patient Directive', NULL);
insert into Directive_Act_Type (id, caption, abbrev) values (1, 'Physician Directive', NULL);
insert into Directive_Act_Type (id, caption, abbrev) values (2, 'Other', NULL);

insert into Diag_Term_Type (id, caption, abbrev) values (0, 'Temporary', NULL);
insert into Diag_Term_Type (id, caption, abbrev) values (1, 'Permanent', NULL);

insert into Proc_Type (id, caption, abbrev) values (0, 'Regular', NULL);
insert into Proc_Type (id, caption, abbrev) values (1, 'Immunization', NULL);
insert into Proc_Type (id, caption, abbrev) values (2, 'Health Maintenance', NULL);
insert into Proc_Type (id, caption, abbrev) values (3, 'Outpatient', NULL);
insert into Proc_Type (id, caption, abbrev) values (4, 'Inpatient', NULL);
insert into Proc_Type (id, caption, abbrev) values (5, 'Surgical', NULL);

insert into Artifact_Type (id, caption, abbrev) values (0, 'Folder/Container', NULL);
insert into Artifact_Type (id, caption, abbrev) values (1000, 'MIME Artifact', NULL);
insert into Artifact_Type (id, caption, abbrev) values (2000, 'Internal Message (within Physia system)', NULL);
insert into Artifact_Type (id, caption, abbrev) values (2100, 'E-mail Message', NULL);
insert into Artifact_Type (id, caption, abbrev) values (3000, 'HL7 Message (Originating)', NULL);
insert into Artifact_Type (id, caption, abbrev) values (4000, 'HL7 Message (Translated to XML/MDL)', NULL);
insert into Artifact_Type (id, caption, abbrev) values (5000, 'Fax', NULL);

insert into Artifact_Event_Type (id, caption, abbrev) values (0, 'Arrived', NULL);
insert into Artifact_Event_Type (id, caption, abbrev) values (1, 'Reviewed', NULL);
insert into Artifact_Event_Type (id, caption, abbrev) values (2, 'Filed', NULL);
insert into Artifact_Event_Type (id, caption, abbrev) values (3, 'On-hold', NULL);
insert into Artifact_Event_Type (id, caption, abbrev) values (4, 'Routed', NULL);
insert into Artifact_Event_Type (id, caption, abbrev) values (5, 'Signed', NULL);

insert into Artifact_Source_Type (id, caption, abbrev) values (0, 'Our firm', NULL);
insert into Artifact_Source_Type (id, caption, abbrev) values (100, 'Person', NULL);
insert into Artifact_Source_Type (id, caption, abbrev) values (200, 'Org', NULL);

insert into Artifact_Association_Type (id, caption, abbrev) values (0, 'None', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (1, 'Parent', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (2, 'Child', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (3, 'Sibling', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (10, 'Translated from', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (1000, 'Owned by Person', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (1010, 'Owned by Organization', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (1020, 'Owned by Person in Organization', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (1030, 'Owned by Project', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (1040, 'Owned by Task', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (2000, 'Requested by Person', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (2010, 'Requested by Organization', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (2020, 'Requested by Person in Organization', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (3000, 'Requires review by Person', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (3010, 'Requires review by Organization', NULL);
insert into Artifact_Association_Type (id, caption, abbrev) values (3020, 'Requires review by Person in Organization', NULL);

insert into Claim_Table_Field_Type (id, caption, abbrev) values (0, 'Claim ID', 'claim_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (1, 'Invoice ID', 'invoice_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (2, 'Batch ID', 'batch_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (3, 'Claim Status', 'claim_status_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (4, 'Patient', 'patient_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (5, 'Referral Information', 'referral_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (6, 'Service Provider', 'service_provider_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (7, 'Billing Provider', 'billing_provider_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (8, 'Service Facility', 'service_facility_id');
insert into Claim_Table_Field_Type (id, caption, abbrev) values (9, 'Billing Facility', 'billing_facility_id');

insert into Claim_Status_Type (id, caption, abbrev) values (0, 'Created', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (1, 'Incomplete', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (2, 'Pending', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (3, 'On Hold', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (4, 'Submitted', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (5, 'Transferred', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (6, 'Approved Internal', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (7, 'Rejected Internal', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (8, 'Electronically Transmitted to Carrier', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (9, 'Transmitted to Carrier via Paper', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (10, 'Approved External', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (11, 'Rejected External', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (12, 'Awaiting Insurance Payment', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (13, 'Payments Applied', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (14, 'Appealed', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (15, 'Closed', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (16, 'Void', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (17, 'Paper Claim Printed', NULL);
insert into Claim_Status_Type (id, caption, abbrev) values (18, 'Awaiting Client Payment', NULL);

insert into Claim_Diagnosis_Code_Type (id, caption, abbrev) values (0, 'ICD-9', NULL);
insert into Claim_Diagnosis_Code_Type (id, caption, abbrev) values (1, 'ICD-10', NULL);

insert into Claim_Procedure_Code_Type (id, caption, abbrev) values (0, 'HCPCS', NULL);
insert into Claim_Procedure_Code_Type (id, caption, abbrev) values (1, 'CPT-4', NULL);
insert into Claim_Procedure_Code_Type (id, caption, abbrev) values (2, 'CPT-5', NULL);

insert into Document_Type (id, caption, abbrev) values (0, 'MIME Document', NULL);
insert into Document_Type (id, caption, abbrev) values (1, 'Internal Message (within Physia system)', NULL);
insert into Document_Type (id, caption, abbrev) values (2, 'E-mail Message', NULL);
insert into Document_Type (id, caption, abbrev) values (3, 'HL7 Message (Originating)', NULL);
insert into Document_Type (id, caption, abbrev) values (4, 'HL7 Message (Translated to XML/MDL)', NULL);
insert into Document_Type (id, caption, abbrev) values (5, 'Fax', NULL);
insert into Document_Type (id, caption, abbrev) values (6, 'Folder/Container', NULL);

insert into Document_Mime_Type (id, caption, abbrev) values (0, 'Plain Text', 'text/plain');
insert into Document_Mime_Type (id, caption, abbrev) values (1, 'HTML (Web) Pages', 'text/html');

insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (0, 'Office', '11');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (1, 'Home', '12');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (2, 'Inpatient Hospital', '21');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (3, 'Outpatient Hospital', '22');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (4, 'Emergency Room - Hospital', '23');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (5, 'Ambulatory Surgical Center', '24');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (6, 'Birthing Center', '25');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (7, 'Military Treatment Facility', '26');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (8, 'Skilled Nursing Facility', '31');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (9, 'Nursing Facility', '32');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (10, 'Custodial Care Facility', '33');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (11, 'Hospice', '34');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (12, 'Ambulance - Land', '41');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (13, 'Ambulance - Air or Water', '42');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (14, 'Inpatient Psyciatric Facility', '51');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (15, 'Psyciatric Facility Partial Hospitalization', '52');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (16, 'Community Mental Health Center', '53');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (17, 'Intermediate Care Facility/Mentally Retarted', '54');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (18, 'Residential Substance Abuse Treatment Facility', '55');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (19, 'Psyciatric Residential Treatment Center', '56');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (20, 'Comprehensive Inpatient Rehabilitation Facility', '61');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (21, 'Comprehensive Outpatient Rehabilitation Facility', '62');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (22, 'End Stage Renal Disease Treatment Facility', '65');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (23, 'State or Local Public Health Clinic', '71');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (24, 'Rural Health Clinic', '72');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (25, 'Independent Laboratory', '81');
insert into HCFA1500_Service_Place_Type (id, caption, abbrev) values (26, 'Other Unlisted Facility', '99');

insert into Procedure_Code_Type (id, caption, abbrev) values (0, 'ICD-9', NULL);
insert into Procedure_Code_Type (id, caption, abbrev) values (1, 'CPT', NULL);

insert into Bill_Remittance_Type (id, caption, abbrev) values (0, 'Paper', NULL);
insert into Bill_Remittance_Type (id, caption, abbrev) values (1, 'Electronic', NULL);

insert into Bill_Sequence_Type (id, caption, abbrev) values (0, 'Primary', NULL);
insert into Bill_Sequence_Type (id, caption, abbrev) values (1, 'Secondary', NULL);
insert into Bill_Sequence_Type (id, caption, abbrev) values (2, 'Tertiary', NULL);
insert into Bill_Sequence_Type (id, caption, abbrev) values (3, 'Quarternary', NULL);
insert into Bill_Sequence_Type (id, caption, abbrev) values (4, 'Workers Comp', NULL);
insert into Bill_Sequence_Type (id, caption, abbrev) values (5, 'Terminated', NULL);
insert into Bill_Sequence_Type (id, caption, abbrev) values (6, 'Inactive', NULL);

insert into Ins_Coverage_Type (id, caption, abbrev) values (0, 'Regular Visits', NULL);
insert into Ins_Coverage_Type (id, caption, abbrev) values (1, 'Emergency', NULL);
insert into Ins_Coverage_Type (id, caption, abbrev) values (2, 'Other', NULL);

insert into Ins_Product_Type (id, caption, abbrev) values (0, 'Self-Pay', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (1, 'Insurance', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (2, 'HMO(cap)', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (3, 'HMO(non-cap)', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (4, 'PPO', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (5, 'Medicare', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (6, 'Medicaid', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (7, 'Workers Compensation', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (8, 'Third-Party Payer', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (9, 'Champus', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (10, 'ChampVA', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (11, 'FECA Blk Lung', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (12, 'BCBS', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (13, 'MC(managed care choice)', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (14, 'POS(point of service)', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (15, 'Railroad Medicare', NULL);
insert into Ins_Product_Type (id, caption, abbrev) values (16, 'Other', NULL);

insert into Insured_Relationship (id, caption, abbrev) values (0, 'Self', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (1, 'Spouse', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (2, 'Natural/Adopted Child (Insured has Financial Responsibility)', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (3, 'Natural/Adopted Child (Insured does not have Financial Resp.)', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (4, 'Step Child', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (5, 'Foster Child', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (6, 'Ward of the Court', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (7, 'Employee', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (8, 'Unknown/Other', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (9, 'Handicapped Dependent', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (10, 'Organ Donor', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (11, 'Cadaver Donor', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (12, 'Grandchild', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (13, 'Niece/Nephew', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (14, 'Injured Plaintiff', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (15, 'Sponsored Dependent', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (16, 'Minor Dependent of a Minor Dependent', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (17, 'Parent', NULL);
insert into Insured_Relationship (id, caption, abbrev) values (18, 'Grandparent', NULL);

insert into Invoice_Status_Type (id, caption, abbrev) values (0, 'Created', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (1, 'Incomplete', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (2, 'Pending', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (3, 'On Hold', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (4, 'Submitted', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (5, 'Transferred', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (6, 'Approved Internal', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (7, 'Rejected Internal', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (8, 'Electronically Transmitted to Carrier', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (9, 'Transmitted to Carrier via Paper', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (10, 'Approved External', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (11, 'Rejected External', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (12, 'Awaiting Insurance Payment', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (13, 'Payments Applied', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (14, 'Appealed', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (15, 'Closed', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (16, 'Void', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (17, 'Paper Claim Printed', NULL);
insert into Invoice_Status_Type (id, caption, abbrev) values (18, 'Awaiting Client Payment', NULL);

insert into Invoice_Type (id, caption, abbrev) values (0, 'Visit', NULL);
insert into Invoice_Type (id, caption, abbrev) values (1, 'Product', NULL);
insert into Invoice_Type (id, caption, abbrev) values (2, 'Service', NULL);
insert into Invoice_Type (id, caption, abbrev) values (3, 'Other', NULL);

insert into Doctor_Visit_Type (id, caption, abbrev) values (0, 'Office', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (1, 'Clinic', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (2, 'Hospital', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (3, 'Facility', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (4, 'Physical Exam', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (5, 'Executive Physical', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (6, 'Sports/School Physical', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (7, 'Regular Visit', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (8, 'Complicated Visit', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (9, 'Well Women Exam', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (10, 'Consultation', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (11, 'Injection Only', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (12, 'Procedure Visit', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (13, 'Workers Compensation', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (14, 'Radiology Visit', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (15, 'Lab Visit', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (16, 'Counseling Visit', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (17, 'Physical Therapy', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (18, 'Special Visit', NULL);
insert into Doctor_Visit_Type (id, caption, abbrev) values (19, 'Other', NULL);

insert into Lab_Order_Status (id, caption, abbrev) values (0, 'Approved', NULL);
insert into Lab_Order_Status (id, caption, abbrev) values (1, 'Pending', NULL);
insert into Lab_Order_Status (id, caption, abbrev) values (2, 'Faxed to Lab', NULL);
insert into Lab_Order_Status (id, caption, abbrev) values (3, 'E-Mailed to Lab', NULL);
insert into Lab_Order_Status (id, caption, abbrev) values (4, 'Transmitted to Lab', NULL);

insert into Lab_Order_Priority (id, caption, abbrev) values (0, 'Normal', NULL);
insert into Lab_Order_Priority (id, caption, abbrev) values (1, 'STAT', NULL);
insert into Lab_Order_Priority (id, caption, abbrev) values (2, 'ASAP', NULL);
insert into Lab_Order_Priority (id, caption, abbrev) values (3, 'Today', NULL);

insert into Lab_Order_Transmission (id, caption, abbrev) values (0, 'Print', NULL);
insert into Lab_Order_Transmission (id, caption, abbrev) values (1, 'E-Mail', NULL);
insert into Lab_Order_Transmission (id, caption, abbrev) values (2, 'Fax', NULL);

insert into Medication_Type (id, caption, abbrev) values (0, 'Pain Reliever', NULL);
insert into Medication_Type (id, caption, abbrev) values (1, 'Carcinogen', NULL);
insert into Medication_Type (id, caption, abbrev) values (2, 'Addictive Drugs', NULL);

insert into Medication_Record_Type (id, caption, abbrev) values (0, 'Existing', NULL);
insert into Medication_Record_Type (id, caption, abbrev) values (1, 'Prescribe', NULL);
insert into Medication_Record_Type (id, caption, abbrev) values (2, 'Refill', NULL);

insert into Referral_Communication (id, caption, abbrev) values (0, 'Verbal', NULL);
insert into Referral_Communication (id, caption, abbrev) values (1, 'Fax', NULL);
insert into Referral_Communication (id, caption, abbrev) values (2, 'E-Mail', NULL);
insert into Referral_Communication (id, caption, abbrev) values (3, 'Mail', NULL);
insert into Referral_Communication (id, caption, abbrev) values (4, 'Left Message', NULL);

insert into Referral_Urgency (id, caption, abbrev) values (0, 'Stat', NULL);
insert into Referral_Urgency (id, caption, abbrev) values (1, 'Regular', NULL);
insert into Referral_Urgency (id, caption, abbrev) values (2, 'Follow Up', NULL);

insert into Referral_Status (id, caption, abbrev) values (0, 'Active', NULL);
insert into Referral_Status (id, caption, abbrev) values (1, 'Inactive', NULL);

insert into Referral_Type (id, caption, abbrev) values (0, 'Test', NULL);
insert into Referral_Type (id, caption, abbrev) values (1, 'Consultation', NULL);
insert into Referral_Type (id, caption, abbrev) values (2, 'Evaluation', NULL);
insert into Referral_Type (id, caption, abbrev) values (3, 'Treatment', NULL);
insert into Referral_Type (id, caption, abbrev) values (4, 'Surgery', NULL);
insert into Referral_Type (id, caption, abbrev) values (5, 'Procedure', NULL);

insert into Message_Attachment_Type (id, caption, abbrev) values (0, 'File', NULL);
insert into Message_Attachment_Type (id, caption, abbrev) values (1, 'Message', NULL);

insert into Message_Recipient_Type (id, caption, abbrev) values (0, 'Application', NULL);
insert into Message_Recipient_Type (id, caption, abbrev) values (1, 'Internet', NULL);

insert into Message_Reception_Type (id, caption, abbrev) values (0, 'Direct', 'To');
insert into Message_Reception_Type (id, caption, abbrev) values (1, 'Carbon-copy', 'Cc');
insert into Message_Reception_Type (id, caption, abbrev) values (2, 'Blind carbon-copy', 'Bcc');

insert into Table_Activity_Type (id, caption, abbrev) values (0, 'Unknown', NULL);
insert into Table_Activity_Type (id, caption, abbrev) values (1, 'Add', NULL);
insert into Table_Activity_Type (id, caption, abbrev) values (2, 'Update', NULL);
insert into Table_Activity_Type (id, caption, abbrev) values (3, 'Delete', NULL);
insert into Table_Activity_Type (id, caption, abbrev) values (4, 'Other', NULL);

insert into Field_Change_Type (id, caption, abbrev) values (0, 'Creation', NULL);
insert into Field_Change_Type (id, caption, abbrev) values (1, 'Addition', NULL);
insert into Field_Change_Type (id, caption, abbrev) values (2, 'Modification', NULL);
insert into Field_Change_Type (id, caption, abbrev) values (3, 'Deletion', NULL);

insert into Measurement_Unit_Type (id, caption, abbrev) values (0, 'Unspecified', NULL);
insert into Measurement_Unit_Type (id, caption, abbrev) values (1, 'milligrams', NULL);
insert into Measurement_Unit_Type (id, caption, abbrev) values (2, 'milliliters', NULL);
insert into Measurement_Unit_Type (id, caption, abbrev) values (3, 'units', NULL);

insert into Month_Of_Year (id, caption, abbrev) values (0, 'January', 'Jan');
insert into Month_Of_Year (id, caption, abbrev) values (1, 'February', 'Feb');
insert into Month_Of_Year (id, caption, abbrev) values (2, 'March', 'Mar');
insert into Month_Of_Year (id, caption, abbrev) values (3, 'April', 'Apr');
insert into Month_Of_Year (id, caption, abbrev) values (4, 'May', 'May');
insert into Month_Of_Year (id, caption, abbrev) values (5, 'June', 'Jun');
insert into Month_Of_Year (id, caption, abbrev) values (6, 'July', 'Jul');
insert into Month_Of_Year (id, caption, abbrev) values (7, 'August', 'Aug');
insert into Month_Of_Year (id, caption, abbrev) values (8, 'September', 'Sep');
insert into Month_Of_Year (id, caption, abbrev) values (9, 'October', 'Oct');
insert into Month_Of_Year (id, caption, abbrev) values (10, 'November', 'Nov');
insert into Month_Of_Year (id, caption, abbrev) values (11, 'December', 'Dec');

insert into Session_Status_Type (id, caption, abbrev) values (0, 'Active', NULL);
insert into Session_Status_Type (id, caption, abbrev) values (1, 'Inactive (User Normal Logout)', NULL);
insert into Session_Status_Type (id, caption, abbrev) values (2, 'Inactive (User Forced Logout)', NULL);
insert into Session_Status_Type (id, caption, abbrev) values (3, 'Inactive (System Forced Logout)', NULL);
insert into Session_Status_Type (id, caption, abbrev) values (4, 'Inactive (Timeout)', NULL);

insert into US_State_Type (id, caption, abbrev) values (0, 'Alabama', 'AL');
insert into US_State_Type (id, caption, abbrev) values (1, 'Alaska', 'AK');
insert into US_State_Type (id, caption, abbrev) values (2, 'Arizona', 'AZ');
insert into US_State_Type (id, caption, abbrev) values (3, 'Arkansas', 'AR');
insert into US_State_Type (id, caption, abbrev) values (4, 'California', 'CA');
insert into US_State_Type (id, caption, abbrev) values (5, 'Colorado', 'CO');
insert into US_State_Type (id, caption, abbrev) values (6, 'Connecticut', 'CT');
insert into US_State_Type (id, caption, abbrev) values (7, 'Delaware', 'DE');
insert into US_State_Type (id, caption, abbrev) values (8, 'District of Columbia', 'DC');
insert into US_State_Type (id, caption, abbrev) values (9, 'Florida', 'FL');
insert into US_State_Type (id, caption, abbrev) values (10, 'Georgia', 'GA');
insert into US_State_Type (id, caption, abbrev) values (11, 'Hawaii', 'HI');
insert into US_State_Type (id, caption, abbrev) values (12, 'Idaho', 'ID');
insert into US_State_Type (id, caption, abbrev) values (13, 'Illinois', 'IL');
insert into US_State_Type (id, caption, abbrev) values (14, 'Indiana', 'IN');
insert into US_State_Type (id, caption, abbrev) values (15, 'Iowa', 'IA');
insert into US_State_Type (id, caption, abbrev) values (16, 'Kansas', 'KS');
insert into US_State_Type (id, caption, abbrev) values (17, 'Kentucky', 'KY');
insert into US_State_Type (id, caption, abbrev) values (18, 'Louisiana', 'LA');
insert into US_State_Type (id, caption, abbrev) values (19, 'Maine', 'ME');
insert into US_State_Type (id, caption, abbrev) values (20, 'Maryland', 'MD');
insert into US_State_Type (id, caption, abbrev) values (21, 'Massacusetts', 'MA');
insert into US_State_Type (id, caption, abbrev) values (22, 'Michigan', 'MI');
insert into US_State_Type (id, caption, abbrev) values (23, 'Minnesota', 'MN');
insert into US_State_Type (id, caption, abbrev) values (24, 'Mississippi', 'MS');
insert into US_State_Type (id, caption, abbrev) values (25, 'Missouri', 'MO');
insert into US_State_Type (id, caption, abbrev) values (26, 'Montana', 'MT');
insert into US_State_Type (id, caption, abbrev) values (27, 'Nebraska', 'NE');
insert into US_State_Type (id, caption, abbrev) values (28, 'Nevada', 'NV');
insert into US_State_Type (id, caption, abbrev) values (29, 'New Hampshire', 'NH');
insert into US_State_Type (id, caption, abbrev) values (30, 'New Jersey', 'NJ');
insert into US_State_Type (id, caption, abbrev) values (31, 'New Mexico', 'NM');
insert into US_State_Type (id, caption, abbrev) values (32, 'New York', 'NY');
insert into US_State_Type (id, caption, abbrev) values (33, 'North Carolina', 'NC');
insert into US_State_Type (id, caption, abbrev) values (34, 'North Dakota', 'ND');
insert into US_State_Type (id, caption, abbrev) values (35, 'Ohio', 'OH');
insert into US_State_Type (id, caption, abbrev) values (36, 'Oklahoma', 'OK');
insert into US_State_Type (id, caption, abbrev) values (37, 'Oregon', 'OR');
insert into US_State_Type (id, caption, abbrev) values (38, 'Pennsylvania', 'PA');
insert into US_State_Type (id, caption, abbrev) values (39, 'Rhode Island', 'RI');
insert into US_State_Type (id, caption, abbrev) values (40, 'South Carolina', 'SC');
insert into US_State_Type (id, caption, abbrev) values (41, 'South Dakota', 'SD');
insert into US_State_Type (id, caption, abbrev) values (42, 'Tennessee', 'TN');
insert into US_State_Type (id, caption, abbrev) values (43, 'Texas', 'TX');
insert into US_State_Type (id, caption, abbrev) values (44, 'Utah', 'UT');
insert into US_State_Type (id, caption, abbrev) values (45, 'Vermont', 'VT');
insert into US_State_Type (id, caption, abbrev) values (46, 'Virginia', 'VA');
insert into US_State_Type (id, caption, abbrev) values (47, 'Washington', 'WA');
insert into US_State_Type (id, caption, abbrev) values (48, 'West Virginia', 'WV');
insert into US_State_Type (id, caption, abbrev) values (49, 'Wisconsin', 'WI');
insert into US_State_Type (id, caption, abbrev) values (50, 'Wyoming', 'WY');
insert into US_State_Type (id, caption, abbrev) values (51, 'American Samoa', 'A_S');
insert into US_State_Type (id, caption, abbrev) values (52, 'Federated States of Micronesia', 'F_S_M');
insert into US_State_Type (id, caption, abbrev) values (53, 'Guam', 'GUAM');
insert into US_State_Type (id, caption, abbrev) values (54, 'Marshall Islands', 'M_I');
insert into US_State_Type (id, caption, abbrev) values (55, 'Northern Mariana Islands', 'N_M_I');
insert into US_State_Type (id, caption, abbrev) values (56, 'Palau', 'PALAU');
insert into US_State_Type (id, caption, abbrev) values (57, 'Puerto Rico', 'P_R');
insert into US_State_Type (id, caption, abbrev) values (58, 'Virgin Islands', 'V_I');
insert into US_State_Type (id, caption, abbrev) values (59, 'Other', 'XX');

insert into Org_Identifier_Type (id, caption, abbrev) values (0, 'Tax ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (1, 'Employer ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (2, 'State ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (3, 'Medicaid ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (4, 'Medicare ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (5, 'Workers Comp ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (6, 'BlueCross/BlueShield ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (7, 'CLIA ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (8, 'Insurance Company Provider ID', NULL);
insert into Org_Identifier_Type (id, caption, abbrev) values (9, 'Other ID', NULL);

insert into Org_Industry_Type (id, caption, abbrev) values (0, 'Agriculture', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (1, 'Apparel', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (2, 'Banking', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (3, 'Biotechnology', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (4, 'Communications', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (5, 'Construction', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (6, 'Consulting', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (7, 'Education', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (8, 'Electronics', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (9, 'Energy', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (10, 'Engineering', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (11, 'Entertainment', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (12, 'Environmental', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (13, 'Finance', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (14, 'Food and Beverage', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (15, 'Government', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (16, 'Healthcare', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (17, 'Hospitality', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (18, 'Insurance', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (19, 'Machinery', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (20, 'Manufacturing', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (21, 'Media', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (22, 'Not for Profit', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (23, 'Recreation', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (24, 'Retail', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (25, 'Shipping', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (26, 'Technology', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (27, 'Telecommunications', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (28, 'Transportation', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (29, 'Utilities', NULL);
insert into Org_Industry_Type (id, caption, abbrev) values (30, 'Other', NULL);

insert into Org_Level_Type (id, caption, abbrev) values (0, 'Company', NULL);
insert into Org_Level_Type (id, caption, abbrev) values (1, 'Branch', NULL);
insert into Org_Level_Type (id, caption, abbrev) values (2, 'Department', NULL);

insert into Org_Ownership_Type (id, caption, abbrev) values (0, 'Public', NULL);
insert into Org_Ownership_Type (id, caption, abbrev) values (1, 'Private', NULL);
insert into Org_Ownership_Type (id, caption, abbrev) values (2, 'Subsidiary', NULL);
insert into Org_Ownership_Type (id, caption, abbrev) values (3, 'Other', NULL);

insert into Org_Type (id, caption, abbrev) values (0, 'Trial Sponsor', NULL);
insert into Org_Type (id, caption, abbrev) values (1, 'Trial Site', NULL);
insert into Org_Type (id, caption, abbrev) values (2, 'Practice', NULL);
insert into Org_Type (id, caption, abbrev) values (3, 'Clinic', NULL);
insert into Org_Type (id, caption, abbrev) values (4, 'Facility/Site', NULL);
insert into Org_Type (id, caption, abbrev) values (5, 'Diagnostic Services', NULL);
insert into Org_Type (id, caption, abbrev) values (6, 'Hospital', NULL);
insert into Org_Type (id, caption, abbrev) values (7, 'Therapeutic Services', NULL);
insert into Org_Type (id, caption, abbrev) values (8, 'Insurance', NULL);
insert into Org_Type (id, caption, abbrev) values (9, 'Employer', NULL);
insert into Org_Type (id, caption, abbrev) values (10, 'IPA', NULL);
insert into Org_Type (id, caption, abbrev) values (11, 'Ancilliary Service', NULL);
insert into Org_Type (id, caption, abbrev) values (12, 'Pharmacy', NULL);
insert into Org_Type (id, caption, abbrev) values (13, 'Other', NULL);

insert into Person_Note_Type (id, caption, abbrev) values (0, 'Unknown', NULL);
insert into Person_Note_Type (id, caption, abbrev) values (1, 'Medical', NULL);
insert into Person_Note_Type (id, caption, abbrev) values (2, 'Non-Medical', NULL);

insert into Blood_Type (id, caption, abbrev) values (0, 'Unknown', NULL);
insert into Blood_Type (id, caption, abbrev) values (1, 'A+', NULL);
insert into Blood_Type (id, caption, abbrev) values (2, 'A-', NULL);
insert into Blood_Type (id, caption, abbrev) values (3, 'B+', NULL);
insert into Blood_Type (id, caption, abbrev) values (4, 'B-', NULL);
insert into Blood_Type (id, caption, abbrev) values (5, 'AB+', NULL);
insert into Blood_Type (id, caption, abbrev) values (6, 'AB-', NULL);
insert into Blood_Type (id, caption, abbrev) values (7, 'O+', NULL);
insert into Blood_Type (id, caption, abbrev) values (8, 'O-', NULL);

insert into Ethnicity_Type (id, caption, abbrev) values (0, 'Unknown', NULL);
insert into Ethnicity_Type (id, caption, abbrev) values (1, 'American Indian/Alaskan Native', NULL);
insert into Ethnicity_Type (id, caption, abbrev) values (2, 'Caucasian', NULL);
insert into Ethnicity_Type (id, caption, abbrev) values (3, 'African-American', NULL);
insert into Ethnicity_Type (id, caption, abbrev) values (4, 'Asian/Pacific Islander', NULL);
insert into Ethnicity_Type (id, caption, abbrev) values (5, 'Hispanic', NULL);
insert into Ethnicity_Type (id, caption, abbrev) values (6, 'Other', NULL);

insert into Gender_Type (id, caption, abbrev) values (0, 'Male', NULL);
insert into Gender_Type (id, caption, abbrev) values (1, 'Female', NULL);
insert into Gender_Type (id, caption, abbrev) values (2, 'Not applicable', NULL);

insert into Language_Type (id, caption, abbrev) values (0, 'English', NULL);
insert into Language_Type (id, caption, abbrev) values (1, 'Spanish', NULL);
insert into Language_Type (id, caption, abbrev) values (2, 'French', NULL);
insert into Language_Type (id, caption, abbrev) values (3, 'German', NULL);
insert into Language_Type (id, caption, abbrev) values (4, 'Italian', NULL);
insert into Language_Type (id, caption, abbrev) values (5, 'Chinese', NULL);
insert into Language_Type (id, caption, abbrev) values (6, 'Japanese', NULL);
insert into Language_Type (id, caption, abbrev) values (7, 'Korean', NULL);
insert into Language_Type (id, caption, abbrev) values (8, 'Vietnamese', NULL);
insert into Language_Type (id, caption, abbrev) values (9, 'Other', NULL);

insert into Marital_Status_Type (id, caption, abbrev) values (0, 'Unknown', NULL);
insert into Marital_Status_Type (id, caption, abbrev) values (1, 'Single', NULL);
insert into Marital_Status_Type (id, caption, abbrev) values (2, 'Married', NULL);
insert into Marital_Status_Type (id, caption, abbrev) values (3, 'Partner', NULL);
insert into Marital_Status_Type (id, caption, abbrev) values (4, 'Legally Separated', NULL);
insert into Marital_Status_Type (id, caption, abbrev) values (5, 'Divorced', NULL);
insert into Marital_Status_Type (id, caption, abbrev) values (6, 'Widowed', NULL);
insert into Marital_Status_Type (id, caption, abbrev) values (7, 'Not applicable', NULL);

insert into Name_Prefix_Type (id, caption, abbrev) values (0, 'Mr.', NULL);
insert into Name_Prefix_Type (id, caption, abbrev) values (1, 'Ms.', NULL);
insert into Name_Prefix_Type (id, caption, abbrev) values (2, 'Mrs.', NULL);

insert into Person_Type (id, caption, abbrev) values (0, 'Principal Investigator', 'PI');
insert into Person_Type (id, caption, abbrev) values (1, 'Clinical Program Manager', 'CPM');
insert into Person_Type (id, caption, abbrev) values (2, 'Certified Clinician Research Associate', 'CCRA');
insert into Person_Type (id, caption, abbrev) values (3, 'Certified Clinical Research Coordinator', 'CCRC');
insert into Person_Type (id, caption, abbrev) values (4, 'Certified Clinical Research Professional', 'CCRP');
insert into Person_Type (id, caption, abbrev) values (5, 'Patient', NULL);
insert into Person_Type (id, caption, abbrev) values (6, 'Physician', NULL);
insert into Person_Type (id, caption, abbrev) values (7, 'Physician Extender (direct billing)', NULL);
insert into Person_Type (id, caption, abbrev) values (8, 'Other Clinical Service Provider (direct billing)', NULL);
insert into Person_Type (id, caption, abbrev) values (9, 'Other Clinical Service Provider (alternate billing)', NULL);
insert into Person_Type (id, caption, abbrev) values (10, 'LVN/LPN', NULL);
insert into Person_Type (id, caption, abbrev) values (11, 'RN', NULL);
insert into Person_Type (id, caption, abbrev) values (12, 'Other Nurse', NULL);
insert into Person_Type (id, caption, abbrev) values (13, 'Staff', NULL);
insert into Person_Type (id, caption, abbrev) values (14, 'Other', NULL);

insert into Person_Indication_Type (id, caption, abbrev) values (0, 'Preventive Care Protocol', NULL);
insert into Person_Indication_Type (id, caption, abbrev) values (1, 'Medication Allergy', NULL);
insert into Person_Indication_Type (id, caption, abbrev) values (2, 'Environmental Allergy', NULL);
insert into Person_Indication_Type (id, caption, abbrev) values (3, 'Medication Intolerance', NULL);
insert into Person_Indication_Type (id, caption, abbrev) values (4, 'Advance Directive (Patient)', NULL);
insert into Person_Indication_Type (id, caption, abbrev) values (5, 'Advance Directive (Physician)', NULL);

insert into Person_License_Type (id, caption, abbrev) values (0, 'DEA', NULL);
insert into Person_License_Type (id, caption, abbrev) values (1, 'DPS', NULL);
insert into Person_License_Type (id, caption, abbrev) values (2, 'IRS', NULL);
insert into Person_License_Type (id, caption, abbrev) values (3, 'Board Certification', NULL);
insert into Person_License_Type (id, caption, abbrev) values (4, 'BCBS', NULL);
insert into Person_License_Type (id, caption, abbrev) values (5, 'Nursing License', NULL);
insert into Person_License_Type (id, caption, abbrev) values (6, 'Memorial Sisters Charity', NULL);
insert into Person_License_Type (id, caption, abbrev) values (7, 'EPSDT', NULL);
insert into Person_License_Type (id, caption, abbrev) values (8, 'Medicare', NULL);
insert into Person_License_Type (id, caption, abbrev) values (9, 'Medicaid', NULL);
insert into Person_License_Type (id, caption, abbrev) values (10, 'UPIN', NULL);
insert into Person_License_Type (id, caption, abbrev) values (11, 'Tax ID', NULL);
insert into Person_License_Type (id, caption, abbrev) values (12, 'Railroad Medicare', NULL);
insert into Person_License_Type (id, caption, abbrev) values (13, 'Champus', NULL);
insert into Person_License_Type (id, caption, abbrev) values (14, 'National Provider ID', NULL);
insert into Person_License_Type (id, caption, abbrev) values (15, 'Other', NULL);

insert into Person_Relationship_Type (id, caption, abbrev) values (0, 'Self', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (1, 'Spouse', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (2, 'Mother', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (3, 'Father', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (4, 'Natural/Adopted Child (Insured has Financial Responsibility)', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (5, 'Natural/Adopted Child (Insured does not have Financial Resp.)', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (6, 'Step Child', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (7, 'Foster Child', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (8, 'Ward of the Court', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (9, 'Sister', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (10, 'Brother', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (11, 'Employee', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (12, 'Unknown/Other', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (13, 'Handicapped Dependent', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (14, 'Organ Donor', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (15, 'Cadaver Donor', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (16, 'Grandchild', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (17, 'Niece/Nephew', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (18, 'Injured Plaintiff', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (19, 'Sponsored Dependent', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (20, 'Minor Dependent of a Minor Dependent', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (21, 'Parent', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (22, 'Grandparent', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (23, 'Cousin', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (24, 'Emergency Contact', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (25, 'Care Provider', NULL);
insert into Person_Relationship_Type (id, caption, abbrev) values (26, 'Other', NULL);

insert into PersonOrg_Rel_Type (id, caption, abbrev) values (0, 'Unknown', NULL);
insert into PersonOrg_Rel_Type (id, caption, abbrev) values (1, 'Employed (Full-time)', NULL);
insert into PersonOrg_Rel_Type (id, caption, abbrev) values (2, 'Employed (Part-time)', NULL);
insert into PersonOrg_Rel_Type (id, caption, abbrev) values (3, 'Self-Employed', NULL);
insert into PersonOrg_Rel_Type (id, caption, abbrev) values (4, 'Retired', NULL);
insert into PersonOrg_Rel_Type (id, caption, abbrev) values (5, 'Student (Full-time)', NULL);
insert into PersonOrg_Rel_Type (id, caption, abbrev) values (6, 'Student (Part-time)', NULL);
insert into PersonOrg_Rel_Type (id, caption, abbrev) values (7, 'Unemployed', NULL);

insert into Person_Role_Type (id, caption, abbrev) values (0, 'Security Role (used for authorization)', NULL);
insert into Person_Role_Type (id, caption, abbrev) values (1, 'Functional Role (used for business rules processing)', NULL);

insert into Asset_Type (id, caption, abbrev) values (0, 'Heavy Machinery', NULL);
insert into Asset_Type (id, caption, abbrev) values (1, 'Medical Diagnostic Equipment', NULL);
insert into Asset_Type (id, caption, abbrev) values (2, 'Surgical Equipment', NULL);
insert into Asset_Type (id, caption, abbrev) values (3, 'Office Machinery', NULL);
insert into Asset_Type (id, caption, abbrev) values (4, 'Room', NULL);
insert into Asset_Type (id, caption, abbrev) values (5, 'Other', NULL);

insert into Event_Type (id, caption, abbrev) values (0, 'Appointment', NULL);
insert into Event_Type (id, caption, abbrev) values (1, 'Well Examination', NULL);
insert into Event_Type (id, caption, abbrev) values (2, 'Consultation', NULL);
insert into Event_Type (id, caption, abbrev) values (3, 'Sick', NULL);
insert into Event_Type (id, caption, abbrev) values (4, 'Re-Check', NULL);
insert into Event_Type (id, caption, abbrev) values (5, 'Allergy Injection', NULL);
insert into Event_Type (id, caption, abbrev) values (6, 'Immunization', NULL);
insert into Event_Type (id, caption, abbrev) values (7, 'Physical Exam', NULL);
insert into Event_Type (id, caption, abbrev) values (8, 'Seminar', NULL);
insert into Event_Type (id, caption, abbrev) values (9, 'Group Therapy', NULL);
insert into Event_Type (id, caption, abbrev) values (10, 'Hospital Rounds', NULL);
insert into Event_Type (id, caption, abbrev) values (11, 'Surgery', NULL);
insert into Event_Type (id, caption, abbrev) values (12, 'Meeting', NULL);
insert into Event_Type (id, caption, abbrev) values (13, 'Staff Meeting', NULL);
insert into Event_Type (id, caption, abbrev) values (14, 'Vacation', NULL);
insert into Event_Type (id, caption, abbrev) values (15, 'Other', NULL);

insert into Event_Status_Type (id, caption, abbrev) values (0, 'Scheduled', NULL);
insert into Event_Status_Type (id, caption, abbrev) values (1, 'In Progress', NULL);
insert into Event_Status_Type (id, caption, abbrev) values (2, 'Complete', NULL);
insert into Event_Status_Type (id, caption, abbrev) values (3, 'Discard', NULL);

insert into Event_Status_ChgRsn_Type (id, caption, abbrev) values (0, 'Natural Progression of Events', NULL);
insert into Event_Status_ChgRsn_Type (id, caption, abbrev) values (1, 'Event Cancelled', NULL);
insert into Event_Status_ChgRsn_Type (id, caption, abbrev) values (2, 'No Show', NULL);
insert into Event_Status_ChgRsn_Type (id, caption, abbrev) values (3, 'Patient Rescheduled', NULL);
insert into Event_Status_ChgRsn_Type (id, caption, abbrev) values (4, 'Org Rescheduled', NULL);

insert into Resource_Type (id, caption, abbrev) values (0, 'Organiztion', NULL);
insert into Resource_Type (id, caption, abbrev) values (1, 'People', NULL);
insert into Resource_Type (id, caption, abbrev) values (2, 'Asset', NULL);
insert into Resource_Type (id, caption, abbrev) values (3, 'Other', NULL);

insert into Staff_Benefit_Type (id, caption, abbrev) values (0, 'Attendance', NULL);
insert into Staff_Benefit_Type (id, caption, abbrev) values (1, 'Insurance', NULL);
insert into Staff_Benefit_Type (id, caption, abbrev) values (2, 'Retirement', NULL);
insert into Staff_Benefit_Type (id, caption, abbrev) values (3, 'Other', NULL);

insert into Staff_Speciality_Type (id, caption, abbrev) values (0, 'Addiction Medicine', '079');
insert into Staff_Speciality_Type (id, caption, abbrev) values (1, 'Allergy/Immunology', '003');
insert into Staff_Speciality_Type (id, caption, abbrev) values (2, 'Anesthesiology', '005');
insert into Staff_Speciality_Type (id, caption, abbrev) values (3, 'Cardiac Surgery', '078');
insert into Staff_Speciality_Type (id, caption, abbrev) values (4, 'Cardiology', '006');
insert into Staff_Speciality_Type (id, caption, abbrev) values (5, 'Chiropractic', '035');
insert into Staff_Speciality_Type (id, caption, abbrev) values (6, 'Critical Care (Intensivists)', '081');
insert into Staff_Speciality_Type (id, caption, abbrev) values (7, 'Colorectal Surgery', '028');
insert into Staff_Speciality_Type (id, caption, abbrev) values (8, 'Dermatology', '007');
insert into Staff_Speciality_Type (id, caption, abbrev) values (9, 'Diagnostic Radiology', '030');
insert into Staff_Speciality_Type (id, caption, abbrev) values (10, 'Emergency Medicine', '093');
insert into Staff_Speciality_Type (id, caption, abbrev) values (11, 'Endocrinology', '046');
insert into Staff_Speciality_Type (id, caption, abbrev) values (12, 'Family Practice', '008');
insert into Staff_Speciality_Type (id, caption, abbrev) values (13, 'Gastroenterology', '010');
insert into Staff_Speciality_Type (id, caption, abbrev) values (14, 'General Practice', '001');
insert into Staff_Speciality_Type (id, caption, abbrev) values (15, 'General Surgery', '002');
insert into Staff_Speciality_Type (id, caption, abbrev) values (16, 'Geriatric Medicine', '038');
insert into Staff_Speciality_Type (id, caption, abbrev) values (17, 'Gynecology (Osteopaths only)', '009');
insert into Staff_Speciality_Type (id, caption, abbrev) values (18, 'Hand Surgery', '040');
insert into Staff_Speciality_Type (id, caption, abbrev) values (19, 'Hematology', '082');
insert into Staff_Speciality_Type (id, caption, abbrev) values (20, 'Hematology/Oncology', '083');
insert into Staff_Speciality_Type (id, caption, abbrev) values (21, 'Infectious Disease', '044');
insert into Staff_Speciality_Type (id, caption, abbrev) values (22, 'Internal Medicine', '011');
insert into Staff_Speciality_Type (id, caption, abbrev) values (23, 'Interventional Radiology', '094');
insert into Staff_Speciality_Type (id, caption, abbrev) values (24, 'Maxillofacial Surgery', '085');
insert into Staff_Speciality_Type (id, caption, abbrev) values (25, 'Medical Oncology', '090');
insert into Staff_Speciality_Type (id, caption, abbrev) values (26, 'Multispecialty Clinic or Group Practice', '070');
insert into Staff_Speciality_Type (id, caption, abbrev) values (27, 'Nephrology', '039');
insert into Staff_Speciality_Type (id, caption, abbrev) values (28, 'Neurology', '013');
insert into Staff_Speciality_Type (id, caption, abbrev) values (29, 'Neuropsychiatry', '086');
insert into Staff_Speciality_Type (id, caption, abbrev) values (30, 'Neurosurgery', '014');
insert into Staff_Speciality_Type (id, caption, abbrev) values (31, 'Nuclear Medicine', '036');
insert into Staff_Speciality_Type (id, caption, abbrev) values (32, 'Obstetrics (Osteopaths only)', '015');
insert into Staff_Speciality_Type (id, caption, abbrev) values (33, 'Obstetrics/Gynecology', '016');
insert into Staff_Speciality_Type (id, caption, abbrev) values (34, 'Ophthalmology', '018');
insert into Staff_Speciality_Type (id, caption, abbrev) values (35, 'Ophthalmology/Otolaryngology (Osteopaths only)', '017');
insert into Staff_Speciality_Type (id, caption, abbrev) values (36, 'Oral Surgery (Dentists only)', '019');
insert into Staff_Speciality_Type (id, caption, abbrev) values (37, 'Orthopedic Surgery', '020');
insert into Staff_Speciality_Type (id, caption, abbrev) values (38, 'Osteopathic Manipulative Therapy', '012');
insert into Staff_Speciality_Type (id, caption, abbrev) values (39, 'Otolaryngology', '004');
insert into Staff_Speciality_Type (id, caption, abbrev) values (40, 'Pathologic Anatomy; Clinical Pathology (Osteopaths only)', '021');
insert into Staff_Speciality_Type (id, caption, abbrev) values (41, 'Pathology', '022');
insert into Staff_Speciality_Type (id, caption, abbrev) values (42, 'Pediatric Medicine', '037');
insert into Staff_Speciality_Type (id, caption, abbrev) values (43, 'Peripheral Vascular Disease', '076');
insert into Staff_Speciality_Type (id, caption, abbrev) values (44, 'Peripheral Vascular Disease - Med or Surg (Osteopaths only)', '023');
insert into Staff_Speciality_Type (id, caption, abbrev) values (45, 'Physical Medicine and Rehabilitation', '025');
insert into Staff_Speciality_Type (id, caption, abbrev) values (46, 'Plastic and Reconstruction Surgery', '024');
insert into Staff_Speciality_Type (id, caption, abbrev) values (47, 'Preventive Medicine', '084');
insert into Staff_Speciality_Type (id, caption, abbrev) values (48, 'Psychiatry', '026');
insert into Staff_Speciality_Type (id, caption, abbrev) values (49, 'Psychiatry/Neurology (Osteopaths only)', '027');
insert into Staff_Speciality_Type (id, caption, abbrev) values (50, 'Pulmonary Diseases', '029');
insert into Staff_Speciality_Type (id, caption, abbrev) values (51, 'Radiation Oncology', '092');
insert into Staff_Speciality_Type (id, caption, abbrev) values (52, 'Radiation Therapy (Osteopaths only)', '032');
insert into Staff_Speciality_Type (id, caption, abbrev) values (53, 'Rheumatology', '066');
insert into Staff_Speciality_Type (id, caption, abbrev) values (54, 'Roentgenology/Radiology (Osteopaths only)', '031');
insert into Staff_Speciality_Type (id, caption, abbrev) values (55, 'Surgical Oncology', '091');
insert into Staff_Speciality_Type (id, caption, abbrev) values (56, 'Thoracic Surgery', '033');
insert into Staff_Speciality_Type (id, caption, abbrev) values (57, 'Unknown Physician Specialty', '099');
insert into Staff_Speciality_Type (id, caption, abbrev) values (58, 'Urology', '034');
insert into Staff_Speciality_Type (id, caption, abbrev) values (59, 'Vascular Surgery', '077');
insert into Staff_Speciality_Type (id, caption, abbrev) values (60, 'Certified Clinical Nurse Specialist', '089');
insert into Staff_Speciality_Type (id, caption, abbrev) values (61, 'Certified Nurse Midwife', '042');
insert into Staff_Speciality_Type (id, caption, abbrev) values (62, 'Certified Registered Nurse Anesthetist', '043');
insert into Staff_Speciality_Type (id, caption, abbrev) values (63, 'Clinical Psychologist', '068');
insert into Staff_Speciality_Type (id, caption, abbrev) values (64, 'Independently-Billing Audiologist', '064');
insert into Staff_Speciality_Type (id, caption, abbrev) values (65, 'Independently-Billing Psychologist', '062');
insert into Staff_Speciality_Type (id, caption, abbrev) values (66, 'Independently-Practicing Occupational Therapist', '067');
insert into Staff_Speciality_Type (id, caption, abbrev) values (67, 'Independently-Practicing Physical Therapist', '065');
insert into Staff_Speciality_Type (id, caption, abbrev) values (68, 'Licensed Clinical Social Worker', '080');
insert into Staff_Speciality_Type (id, caption, abbrev) values (69, 'Nurse Practitioner', '050');
insert into Staff_Speciality_Type (id, caption, abbrev) values (70, 'Optometry', '041');
insert into Staff_Speciality_Type (id, caption, abbrev) values (71, 'Physician Assistant', '097');
insert into Staff_Speciality_Type (id, caption, abbrev) values (72, 'Podiatry', '048');
insert into Staff_Speciality_Type (id, caption, abbrev) values (73, 'Diagnostic Laboratory, GPPP', '072');
insert into Staff_Speciality_Type (id, caption, abbrev) values (74, 'Diagnostic X-Ray, GPPP', '071');
insert into Staff_Speciality_Type (id, caption, abbrev) values (75, 'Occupational Therapy, GPPP', '074');
insert into Staff_Speciality_Type (id, caption, abbrev) values (76, 'Other Medical Care, GPPP', '075');
insert into Staff_Speciality_Type (id, caption, abbrev) values (77, 'Physiotherapy, GPPP', '073');
insert into Staff_Speciality_Type (id, caption, abbrev) values (78, 'All Other (ex. Drug and Dept Stores)', '087');
insert into Staff_Speciality_Type (id, caption, abbrev) values (79, 'Ambulance Service Supplier', '059');
insert into Staff_Speciality_Type (id, caption, abbrev) values (80, 'Ambulatory Surgical Center', '049');
insert into Staff_Speciality_Type (id, caption, abbrev) values (81, 'Independent Physiological Laboratory', '095');
insert into Staff_Speciality_Type (id, caption, abbrev) values (82, 'Independently Billing Clinical Laboratory', '069');
insert into Staff_Speciality_Type (id, caption, abbrev) values (83, 'Individual Certified Orthotist', '055');
insert into Staff_Speciality_Type (id, caption, abbrev) values (84, 'Individual Certified Prosthetist', '056');
insert into Staff_Speciality_Type (id, caption, abbrev) values (85, 'Individual Certified Prosthetist-Orthotist', '057');
insert into Staff_Speciality_Type (id, caption, abbrev) values (86, 'Individual Certified (Other)', '058');
insert into Staff_Speciality_Type (id, caption, abbrev) values (87, 'Mammography Screening Center', '045');
insert into Staff_Speciality_Type (id, caption, abbrev) values (88, 'Medical Supply Co. (with Orthotist Cert. by ABCPO)', '051');
insert into Staff_Speciality_Type (id, caption, abbrev) values (89, 'Medical Supply Co. (with Prosthetist Cert. by ABCPO)', '052');
insert into Staff_Speciality_Type (id, caption, abbrev) values (90, 'Medical Supply Co. (with Prosthetist-Orthotist Cert. by ABCPO)', '053');
insert into Staff_Speciality_Type (id, caption, abbrev) values (91, 'Medical Supply Company (Other)', '054');
insert into Staff_Speciality_Type (id, caption, abbrev) values (92, 'Portable X-Ray Supplier', '063');
insert into Staff_Speciality_Type (id, caption, abbrev) values (93, 'Public Health or Welfare Agencies', '060');
insert into Staff_Speciality_Type (id, caption, abbrev) values (94, 'Unknown Supplier/Provider', '088');
insert into Staff_Speciality_Type (id, caption, abbrev) values (95, 'Volun. Health or Charitable Agencies', '061');
insert into Staff_Speciality_Type (id, caption, abbrev) values (96, 'Acupuncturist', 'N04');
insert into Staff_Speciality_Type (id, caption, abbrev) values (97, 'Independently Billing Radiology Grp', 'N06');
insert into Staff_Speciality_Type (id, caption, abbrev) values (98, 'Licensed Massage Therapist', 'N07');
insert into Staff_Speciality_Type (id, caption, abbrev) values (99, 'Licensed Practical Nurse', 'N02');
insert into Staff_Speciality_Type (id, caption, abbrev) values (100, 'Opthamology, Cataracts', 'N03');
insert into Staff_Speciality_Type (id, caption, abbrev) values (101, 'Obstetrics/Neo Natal', 'N05');
insert into Staff_Speciality_Type (id, caption, abbrev) values (102, 'Registered Nurse', 'N01');
insert into Staff_Speciality_Type (id, caption, abbrev) values (103, 'Dental General Practice', '301');
insert into Staff_Speciality_Type (id, caption, abbrev) values (104, 'Denturist', '308');
insert into Staff_Speciality_Type (id, caption, abbrev) values (105, 'Endodontics', '303');
insert into Staff_Speciality_Type (id, caption, abbrev) values (106, 'Independent Hygienist', '302');
insert into Staff_Speciality_Type (id, caption, abbrev) values (107, 'Orthodontics', '307');
insert into Staff_Speciality_Type (id, caption, abbrev) values (108, 'Pedodontics', '304');
insert into Staff_Speciality_Type (id, caption, abbrev) values (109, 'Periodontics', '305');
insert into Staff_Speciality_Type (id, caption, abbrev) values (110, 'Prosthodontics', '306');

insert into Transaction_Type (id, caption, abbrev) values (0, 'Billing', NULL);
insert into Transaction_Type (id, caption, abbrev) values (1, 'Clinical', NULL);
insert into Transaction_Type (id, caption, abbrev) values (2, 'Document', NULL);
insert into Transaction_Type (id, caption, abbrev) values (3, 'Financial', NULL);
insert into Transaction_Type (id, caption, abbrev) values (4, 'Other', NULL);

insert into Comm_Trns_Type (id, caption, abbrev) values (0, 'Telephone', NULL);
insert into Comm_Trns_Type (id, caption, abbrev) values (1, 'Fax', NULL);
insert into Comm_Trns_Type (id, caption, abbrev) values (2, 'Page', NULL);
insert into Comm_Trns_Type (id, caption, abbrev) values (3, 'E-Mail', NULL);
insert into Comm_Trns_Type (id, caption, abbrev) values (4, 'Delivery', NULL);

insert into PhyComm_Trns_Type (id, caption, abbrev) values (0, 'Telephone', NULL);
insert into PhyComm_Trns_Type (id, caption, abbrev) values (1, 'Fax', NULL);
insert into PhyComm_Trns_Type (id, caption, abbrev) values (2, 'Page', NULL);
insert into PhyComm_Trns_Type (id, caption, abbrev) values (3, 'E-Mail', NULL);
insert into PhyComm_Trns_Type (id, caption, abbrev) values (4, 'Delivery', NULL);
insert into PhyComm_Trns_Type (id, caption, abbrev) values (5, 'Mail', NULL);

insert into Directive_Trns_Type (id, caption, abbrev) values (0, 'Patient Directive', NULL);
insert into Directive_Trns_Type (id, caption, abbrev) values (1, 'Physician Directive', NULL);
insert into Directive_Trns_Type (id, caption, abbrev) values (2, 'Other', NULL);

insert into Person_Flag_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Person_Flag_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Person_Flag_Status (id, caption, abbrev) values (99, 'Unknown', 'U');

insert into Person_Login_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Person_Login_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Person_Login_Status (id, caption, abbrev) values (99, 'Unknown', 'U');

insert into Person_Relationship_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Person_Relationship_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Person_Relationship_Status (id, caption, abbrev) values (99, 'Unknown', 'U');

insert into Person_Role_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Person_Role_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Person_Role_Status (id, caption, abbrev) values (99, 'Unknown', 'U');

insert into PersonOrg_Rel_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into PersonOrg_Rel_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into PersonOrg_Rel_Status (id, caption, abbrev) values (99, 'Unknown', 'U');

insert into Org_Relationship_Type (id, caption, abbrev) values (0, 'Our Firm', NULL);
insert into Org_Relationship_Type (id, caption, abbrev) values (1, 'Client', NULL);
insert into Org_Relationship_Type (id, caption, abbrev) values (2, 'Vendor', NULL);
insert into Org_Relationship_Type (id, caption, abbrev) values (3, 'Partner', NULL);
insert into Org_Relationship_Type (id, caption, abbrev) values (1000, 'Ancestor of Org', NULL);
insert into Org_Relationship_Type (id, caption, abbrev) values (1010, 'Parent of Org', NULL);
insert into Org_Relationship_Type (id, caption, abbrev) values (1020, 'Sibling of Org', NULL);
insert into Org_Relationship_Type (id, caption, abbrev) values (1030, 'Child of Org', NULL);
insert into Org_Relationship_Type (id, caption, abbrev) values (1040, 'Descendent of Org', NULL);

insert into Org_Relationship_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Org_Relationship_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Org_Relationship_Status (id, caption, abbrev) values (99, 'Unknown', 'U');

insert into Artifact_Association_Status (id, caption, abbrev) values (0, 'Inactive', 'I');
insert into Artifact_Association_Status (id, caption, abbrev) values (1, 'Active', 'A');
insert into Artifact_Association_Status (id, caption, abbrev) values (99, 'Unknown', 'U');
