package auto.dal.db.vo;


public interface OrgClassification
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Long getOrgId();
    
    public java.lang.String getOrgType();
    
    public java.lang.Integer getOrgTypeId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setOrgType(java.lang.String orgType);
    
    public void setOrgTypeId(java.lang.Integer orgTypeId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSystemId(java.lang.String systemId);
}