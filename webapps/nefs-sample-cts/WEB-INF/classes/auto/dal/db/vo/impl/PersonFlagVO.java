package auto.dal.db.vo.impl;


public class PersonFlagVO
implements auto.dal.db.vo.PersonFlag
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.Integer getFlagId()
    {
        return flagId;
    }
    
    public java.lang.Long getParentId()
    {
        return parentId;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
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
    
    public void setFlagId(java.lang.Integer flagId)
    {
        this.flagId = flagId;
    }
    
    public void setParentId(java.lang.Long parentId)
    {
        this.parentId = parentId;
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.Integer flagId;
    private java.lang.Long parentId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}