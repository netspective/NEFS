package auto.dal.db.vo;


public interface PersonIdentifier
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getIdType();
    
    public java.lang.String getIdTypeId();
    
    public java.lang.String getIdentifier();
    
    public java.lang.String getNotes();
    
    public java.lang.Long getOrgId();
    
    public java.lang.Long getPersonId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getSourceType();
    
    public java.lang.String getSourceTypeId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setIdType(java.lang.String idType);
    
    public void setIdTypeId(java.lang.String idTypeId);
    
    public void setIdentifier(java.lang.String identifier);
    
    public void setNotes(java.lang.String notes);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setPersonId(java.lang.Long personId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSourceType(java.lang.String sourceType);
    
    public void setSourceTypeId(java.lang.String sourceTypeId);
    
    public void setSystemId(java.lang.String systemId);
}