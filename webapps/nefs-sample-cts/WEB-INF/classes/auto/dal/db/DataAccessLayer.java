package auto.dal.db;

import com.netspective.axiom.schema.Schema;
import auto.dal.db.dao.OrgTable;
import auto.dal.db.dao.PersonTable;
import auto.dal.db.dao.PersonRelationshipMapTable;
import auto.dal.db.dao.PersonOrgRelationshipMapTable;
import auto.dal.db.dao.StudyTable;

public final class DataAccessLayer
{
    private static final DataAccessLayer INSTANCE = new DataAccessLayer();
    
    public final OrgTable getOrgTable()
    {
        return orgTable;
    }
    
    public final PersonOrgRelationshipMapTable getPersonOrgRelationshipMapTable()
    {
        return personOrgRelationshipMapTable;
    }
    
    public final PersonRelationshipMapTable getPersonRelationshipMapTable()
    {
        return personRelationshipMapTable;
    }
    
    public final PersonTable getPersonTable()
    {
        return personTable;
    }
    
    public final Schema getSchema()
    {
        return this.schema;
    }
    
    public final StudyTable getStudyTable()
    {
        return studyTable;
    }
    
    public final void setSchema(Schema schema)
    {
        this.schema = schema;
        orgTable = new OrgTable(schema.getTables().getByName("Org"));
        personTable = new PersonTable(schema.getTables().getByName("Person"));
        personRelationshipMapTable = new PersonRelationshipMapTable(schema.getTables().getByName("Person_Relationship_Map"));
        personOrgRelationshipMapTable = new PersonOrgRelationshipMapTable(schema.getTables().getByName("PersonOrg_Relationship_Map"));
        studyTable = new StudyTable(schema.getTables().getByName("Study"));
    }
    
    public static final DataAccessLayer getInstance()
    {
        return INSTANCE;
    }
    private OrgTable orgTable;
    private PersonOrgRelationshipMapTable personOrgRelationshipMapTable;
    private PersonRelationshipMapTable personRelationshipMapTable;
    private PersonTable personTable;
    private Schema schema;
    private StudyTable studyTable;
}