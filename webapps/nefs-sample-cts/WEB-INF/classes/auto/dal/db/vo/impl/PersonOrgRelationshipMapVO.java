package auto.dal.db.vo.impl;


public class PersonOrgRelationshipMapVO
implements auto.dal.db.vo.PersonOrgRelationshipMap
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.Integer getInvRelTypeId()
    {
        return invRelTypeId;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public java.lang.Integer getRelTypeId()
    {
        return relTypeId;
    }
    
    public java.lang.String getSystemId()
    {
        return systemId;
    }
    
    public void setCrSessId(java.lang.String crSessId)
    {
        this.crSessId = crSessId;
    }
    
    public void setCrStamp(java.util.Date crStamp)
    {
        this.crStamp = crStamp;
    }
    
    public void setInvRelTypeId(java.lang.Integer invRelTypeId)
    {
        this.invRelTypeId = invRelTypeId;
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRelTypeId(java.lang.Integer relTypeId)
    {
        this.relTypeId = relTypeId;
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.Integer invRelTypeId;
    private java.lang.Integer recStatId;
    private java.lang.Integer relTypeId;
    private java.lang.String systemId;
}