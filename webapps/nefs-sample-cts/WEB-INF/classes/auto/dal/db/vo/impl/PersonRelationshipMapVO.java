package auto.dal.db.vo.impl;


public class PersonRelationshipMapVO
implements auto.dal.db.vo.PersonRelationshipMap
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
    
    public int getInvRelTypeIdInt()
    {
        return getInvRelTypeIdInt(-1);
    }
    
    public int getInvRelTypeIdInt(int defaultValue)
    {
        return invRelTypeId != null ? invRelTypeId.intValue() : defaultValue;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public int getRecStatIdInt()
    {
        return getRecStatIdInt(-1);
    }
    
    public int getRecStatIdInt(int defaultValue)
    {
        return recStatId != null ? recStatId.intValue() : defaultValue;
    }
    
    public java.lang.Integer getRelTypeId()
    {
        return relTypeId;
    }
    
    public int getRelTypeIdInt()
    {
        return getRelTypeIdInt(-1);
    }
    
    public int getRelTypeIdInt(int defaultValue)
    {
        return relTypeId != null ? relTypeId.intValue() : defaultValue;
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
    
    public void setInvRelTypeIdInt(int invRelTypeId)
    {
        this.invRelTypeId = new java.lang.Integer(invRelTypeId);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setRelTypeId(java.lang.Integer relTypeId)
    {
        this.relTypeId = relTypeId;
    }
    
    public void setRelTypeIdInt(int relTypeId)
    {
        this.relTypeId = new java.lang.Integer(relTypeId);
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