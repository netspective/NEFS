package auto.dal.db.vo;


public interface OrgIndustry
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getIndustryType();
    
    public java.lang.Integer getIndustryTypeId();
    
    public int getIndustryTypeIdInt();
    
    public int getIndustryTypeIdInt(int defaultValue);
    
    public java.lang.Long getOrgId();
    
    public long getOrgIdLong();
    
    public long getOrgIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setIndustryType(java.lang.String industryType);
    
    public void setIndustryTypeId(java.lang.Integer industryTypeId);
    
    public void setIndustryTypeIdInt(int industryTypeId);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setOrgIdLong(long orgId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSystemId(java.lang.String systemId);
}