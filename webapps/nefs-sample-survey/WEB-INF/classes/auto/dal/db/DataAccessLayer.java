package auto.dal.db;

import com.netspective.axiom.schema.Schema;
import auto.dal.db.dao.CurrentEnvironmentTable;
import auto.dal.db.dao.RespondentTable;
import auto.dal.db.dao.RiskResponseTable;
import auto.dal.db.dao.VisitedPageTable;

public final class DataAccessLayer
{
    private static final DataAccessLayer INSTANCE = new DataAccessLayer();
    
    public final CurrentEnvironmentTable getCurrentEnvironmentTable()
    {
        return currentEnvironmentTable;
    }
    
    public final RespondentTable getRespondentTable()
    {
        return respondentTable;
    }
    
    public final RiskResponseTable getRiskResponseTable()
    {
        return riskResponseTable;
    }
    
    public final Schema getSchema()
    {
        return this.schema;
    }
    
    public final VisitedPageTable getVisitedPageTable()
    {
        return visitedPageTable;
    }
    
    public final void setSchema(Schema schema)
    {
        this.schema = schema;
        currentEnvironmentTable = new CurrentEnvironmentTable(schema.getTables().getByName("Current_Environment"));
        respondentTable = new RespondentTable(schema.getTables().getByName("Respondent"));
        riskResponseTable = new RiskResponseTable(schema.getTables().getByName("Risk_Response"));
        visitedPageTable = new VisitedPageTable(schema.getTables().getByName("Visited_Page"));
    }
    
    public static final DataAccessLayer getInstance()
    {
        return INSTANCE;
    }
    private CurrentEnvironmentTable currentEnvironmentTable;
    private RespondentTable respondentTable;
    private RiskResponseTable riskResponseTable;
    private Schema schema;
    private VisitedPageTable visitedPageTable;
}