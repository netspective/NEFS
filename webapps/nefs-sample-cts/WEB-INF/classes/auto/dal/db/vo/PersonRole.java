package auto.dal.db.vo;


public interface PersonRole
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Long getPersonId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getRoleNameId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setPersonId(java.lang.Long personId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRoleNameId(java.lang.String roleNameId);
    
    public void setSystemId(java.lang.String systemId);
}