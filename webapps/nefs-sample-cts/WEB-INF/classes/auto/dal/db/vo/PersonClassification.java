package auto.dal.db.vo;


public interface PersonClassification
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Long getOrgId();
    
    public java.lang.Long getPersonId();
    
    public java.lang.String getPersonType();
    
    public java.lang.Integer getPersonTypeId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setPersonId(java.lang.Long personId);
    
    public void setPersonType(java.lang.String personType);
    
    public void setPersonTypeId(java.lang.Integer personTypeId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSystemId(java.lang.String systemId);
}