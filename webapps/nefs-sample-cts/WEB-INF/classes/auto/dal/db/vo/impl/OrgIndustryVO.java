package auto.dal.db.vo.impl;


public class OrgIndustryVO
implements auto.dal.db.vo.OrgIndustry
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.String getIndustryType()
    {
        return industryType;
    }
    
    public java.lang.Integer getIndustryTypeId()
    {
        return industryTypeId;
    }
    
    public int getIndustryTypeIdInt()
    {
        return getIndustryTypeIdInt(-1);
    }
    
    public int getIndustryTypeIdInt(int defaultValue)
    {
        return industryTypeId != null ? industryTypeId.intValue() : defaultValue;
    }
    
    public java.lang.Long getOrgId()
    {
        return orgId;
    }
    
    public long getOrgIdLong()
    {
        return getOrgIdLong(-1);
    }
    
    public long getOrgIdLong(long defaultValue)
    {
        return orgId != null ? orgId.longValue() : defaultValue;
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
    
    public void setIndustryType(java.lang.String industryType)
    {
        this.industryType = industryType;
    }
    
    public void setIndustryTypeId(java.lang.Integer industryTypeId)
    {
        this.industryTypeId = industryTypeId;
    }
    
    public void setIndustryTypeIdInt(int industryTypeId)
    {
        this.industryTypeId = new java.lang.Integer(industryTypeId);
    }
    
    public void setOrgId(java.lang.Long orgId)
    {
        this.orgId = orgId;
    }
    
    public void setOrgIdLong(long orgId)
    {
        this.orgId = new java.lang.Long(orgId);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.String industryType;
    private java.lang.Integer industryTypeId;
    private java.lang.Long orgId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}