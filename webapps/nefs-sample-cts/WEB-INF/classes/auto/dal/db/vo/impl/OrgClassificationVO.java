package auto.dal.db.vo.impl;


public class OrgClassificationVO
implements auto.dal.db.vo.OrgClassification
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.Long getOrgId()
    {
        return orgId;
    }
    
    public java.lang.String getOrgType()
    {
        return orgType;
    }
    
    public java.lang.Integer getOrgTypeId()
    {
        return orgTypeId;
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
    
    public void setOrgId(java.lang.Long orgId)
    {
        this.orgId = orgId;
    }
    
    public void setOrgType(java.lang.String orgType)
    {
        this.orgType = orgType;
    }
    
    public void setOrgTypeId(java.lang.Integer orgTypeId)
    {
        this.orgTypeId = orgTypeId;
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
    private java.lang.Long orgId;
    private java.lang.String orgType;
    private java.lang.Integer orgTypeId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}