package auto.dal.db.vo;


public interface PersonFlag
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Integer getFlagId();
    
    public java.lang.Long getParentId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setFlagId(java.lang.Integer flagId);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSystemId(java.lang.String systemId);
}