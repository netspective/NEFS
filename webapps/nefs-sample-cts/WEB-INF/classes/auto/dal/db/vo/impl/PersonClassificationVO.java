package auto.dal.db.vo.impl;


public class PersonClassificationVO
implements auto.dal.db.vo.PersonClassification
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
    
    public long getOrgIdLong()
    {
        return getOrgIdLong(-1);
    }
    
    public long getOrgIdLong(long defaultValue)
    {
        return orgId != null ? orgId.longValue() : defaultValue;
    }
    
    public java.lang.Long getPersonId()
    {
        return personId;
    }
    
    public long getPersonIdLong()
    {
        return getPersonIdLong(-1);
    }
    
    public long getPersonIdLong(long defaultValue)
    {
        return personId != null ? personId.longValue() : defaultValue;
    }
    
    public java.lang.String getPersonType()
    {
        return personType;
    }
    
    public java.lang.Integer getPersonTypeId()
    {
        return personTypeId;
    }
    
    public int getPersonTypeIdInt()
    {
        return getPersonTypeIdInt(-1);
    }
    
    public int getPersonTypeIdInt(int defaultValue)
    {
        return personTypeId != null ? personTypeId.intValue() : defaultValue;
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
    
    public void setOrgId(java.lang.Long orgId)
    {
        this.orgId = orgId;
    }
    
    public void setOrgIdLong(long orgId)
    {
        this.orgId = new java.lang.Long(orgId);
    }
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setPersonIdLong(long personId)
    {
        this.personId = new java.lang.Long(personId);
    }
    
    public void setPersonType(java.lang.String personType)
    {
        this.personType = personType;
    }
    
    public void setPersonTypeId(java.lang.Integer personTypeId)
    {
        this.personTypeId = personTypeId;
    }
    
    public void setPersonTypeIdInt(int personTypeId)
    {
        this.personTypeId = new java.lang.Integer(personTypeId);
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
    private java.lang.Long orgId;
    private java.lang.Long personId;
    private java.lang.String personType;
    private java.lang.Integer personTypeId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}