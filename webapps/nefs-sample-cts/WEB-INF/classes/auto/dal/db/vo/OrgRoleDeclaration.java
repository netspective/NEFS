package auto.dal.db.vo;


public interface OrgRoleDeclaration
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Long getOrgId();
    
    public long getOrgIdLong();
    
    public long getOrgIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getRoleName();
    
    public java.lang.String getRoleNameId();
    
    public java.lang.Integer getRoleTypeId();
    
    public int getRoleTypeIdInt();
    
    public int getRoleTypeIdInt(int defaultValue);
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setOrgIdLong(long orgId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setRoleName(java.lang.String roleName);
    
    public void setRoleNameId(java.lang.String roleNameId);
    
    public void setRoleTypeId(java.lang.Integer roleTypeId);
    
    public void setRoleTypeIdInt(int roleTypeId);
}