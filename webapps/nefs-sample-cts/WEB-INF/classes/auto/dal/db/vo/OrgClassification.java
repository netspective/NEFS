package auto.dal.db.vo;


public interface OrgClassification
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getOrgType();
    
    public java.lang.Integer getOrgTypeId();
    
    public int getOrgTypeIdInt();
    
    public int getOrgTypeIdInt(int defaultValue);
    
    public java.lang.Long getParentId();
    
    public long getParentIdLong();
    
    public long getParentIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setOrgType(java.lang.String orgType);
    
    public void setOrgTypeId(java.lang.Integer orgTypeId);
    
    public void setOrgTypeIdInt(int orgTypeId);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setParentIdLong(long parentId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSystemId(java.lang.String systemId);
}