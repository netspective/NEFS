package auto.dal.db.vo;


public interface OrgIdentifier
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getIdentifier();
    
    public java.lang.String getIdentifierType();
    
    public java.lang.Integer getIdentifierTypeId();
    
    public int getIdentifierTypeIdInt();
    
    public int getIdentifierTypeIdInt(int defaultValue);
    
    public java.lang.Long getOrgId();
    
    public long getOrgIdLong();
    
    public long getOrgIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setIdentifier(java.lang.String identifier);
    
    public void setIdentifierType(java.lang.String identifierType);
    
    public void setIdentifierTypeId(java.lang.Integer identifierTypeId);
    
    public void setIdentifierTypeIdInt(int identifierTypeId);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setOrgIdLong(long orgId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSystemId(java.lang.String systemId);
}