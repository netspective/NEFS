package auto.dal.db.vo;


public interface OrgRoleDeclaration
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Long getOrgId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getRoleName();
    
    public java.lang.String getRoleNameId();
    
    public java.lang.Integer getRoleTypeId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRoleName(java.lang.String roleName);
    
    public void setRoleNameId(java.lang.String roleNameId);
    
    public void setRoleTypeId(java.lang.Integer roleTypeId);
}