package auto.dal.db.vo.impl;


public class PersonEthnicityVO
implements auto.dal.db.vo.PersonEthnicity
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.String getEthnicity()
    {
        return ethnicity;
    }
    
    public java.lang.Integer getEthnicityId()
    {
        return ethnicityId;
    }
    
    public int getEthnicityIdInt()
    {
        return getEthnicityIdInt(-1);
    }
    
    public int getEthnicityIdInt(int defaultValue)
    {
        return ethnicityId != null ? ethnicityId.intValue() : defaultValue;
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
    
    public void setEthnicity(java.lang.String ethnicity)
    {
        this.ethnicity = ethnicity;
    }
    
    public void setEthnicityId(java.lang.Integer ethnicityId)
    {
        this.ethnicityId = ethnicityId;
    }
    
    public void setEthnicityIdInt(int ethnicityId)
    {
        this.ethnicityId = new java.lang.Integer(ethnicityId);
    }
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setPersonIdLong(long personId)
    {
        this.personId = new java.lang.Long(personId);
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
    private java.lang.String ethnicity;
    private java.lang.Integer ethnicityId;
    private java.lang.Long personId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}