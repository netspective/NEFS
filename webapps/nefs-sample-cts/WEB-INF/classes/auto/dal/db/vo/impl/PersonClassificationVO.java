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
    
    public java.lang.Long getPersonId()
    {
        return personId;
    }
    
    public java.lang.String getPersonType()
    {
        return personType;
    }
    
    public java.lang.Integer getPersonTypeId()
    {
        return personTypeId;
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
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setPersonType(java.lang.String personType)
    {
        this.personType = personType;
    }
    
    public void setPersonTypeId(java.lang.Integer personTypeId)
    {
        this.personTypeId = personTypeId;
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
    private java.lang.Long personId;
    private java.lang.String personType;
    private java.lang.Integer personTypeId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}