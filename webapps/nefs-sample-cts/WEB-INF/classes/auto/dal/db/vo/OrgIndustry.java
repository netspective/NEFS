package auto.dal.db.vo;


public interface OrgIndustry
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getIndustryType();
    
    public java.lang.Integer getIndustryTypeId();
    
    public java.lang.Long getOrgId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setIndustryType(java.lang.String industryType);
    
    public void setIndustryTypeId(java.lang.Integer industryTypeId);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSystemId(java.lang.String systemId);
}