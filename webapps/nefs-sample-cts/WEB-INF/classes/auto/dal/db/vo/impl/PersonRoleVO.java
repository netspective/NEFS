package auto.dal.db.vo.impl;


public class PersonRoleVO
implements auto.dal.db.vo.PersonRole
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.Long getPersonId()
    {
        return personId;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public java.lang.String getRoleNameId()
    {
        return roleNameId;
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
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRoleNameId(java.lang.String roleNameId)
    {
        this.roleNameId = roleNameId;
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.Long personId;
    private java.lang.Integer recStatId;
    private java.lang.String roleNameId;
    private java.lang.String systemId;
}