package auto.dal.db.vo;


public interface PersonFlag
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Integer getFlagId();
    
    public int getFlagIdInt();
    
    public int getFlagIdInt(int defaultValue);
    
    public java.lang.Long getParentId();
    
    public long getParentIdLong();
    
    public long getParentIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setFlagId(java.lang.Integer flagId);
    
    public void setFlagIdInt(int flagId);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setParentIdLong(long parentId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSystemId(java.lang.String systemId);
}