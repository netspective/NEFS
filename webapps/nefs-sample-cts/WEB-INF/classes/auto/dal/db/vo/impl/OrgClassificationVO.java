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
    
    public java.lang.String getOrgType()
    {
        return orgType;
    }
    
    public java.lang.Integer getOrgTypeId()
    {
        return orgTypeId;
    }
    
    public int getOrgTypeIdInt()
    {
        return getOrgTypeIdInt(-1);
    }
    
    public int getOrgTypeIdInt(int defaultValue)
    {
        return orgTypeId != null ? orgTypeId.intValue() : defaultValue;
    }
    
    public java.lang.Long getParentId()
    {
        return parentId;
    }
    
    public long getParentIdLong()
    {
        return getParentIdLong(-1);
    }
    
    public long getParentIdLong(long defaultValue)
    {
        return parentId != null ? parentId.longValue() : defaultValue;
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
    
    public void setOrgType(java.lang.String orgType)
    {
        this.orgType = orgType;
    }
    
    public void setOrgTypeId(java.lang.Integer orgTypeId)
    {
        this.orgTypeId = orgTypeId;
    }
    
    public void setOrgTypeIdInt(int orgTypeId)
    {
        this.orgTypeId = new java.lang.Integer(orgTypeId);
    }
    
    public void setParentId(java.lang.Long parentId)
    {
        this.parentId = parentId;
    }
    
    public void setParentIdLong(long parentId)
    {
        this.parentId = new java.lang.Long(parentId);
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
    private java.lang.String orgType;
    private java.lang.Integer orgTypeId;
    private java.lang.Long parentId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}