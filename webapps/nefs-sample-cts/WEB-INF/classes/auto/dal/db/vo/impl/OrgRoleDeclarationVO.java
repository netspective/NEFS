package auto.dal.db.vo.impl;


public class OrgRoleDeclarationVO
implements auto.dal.db.vo.OrgRoleDeclaration
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
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public java.lang.String getRoleName()
    {
        return roleName;
    }
    
    public java.lang.String getRoleNameId()
    {
        return roleNameId;
    }
    
    public java.lang.Integer getRoleTypeId()
    {
        return roleTypeId;
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
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRoleName(java.lang.String roleName)
    {
        this.roleName = roleName;
    }
    
    public void setRoleNameId(java.lang.String roleNameId)
    {
        this.roleNameId = roleNameId;
    }
    
    public void setRoleTypeId(java.lang.Integer roleTypeId)
    {
        this.roleTypeId = roleTypeId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.Long orgId;
    private java.lang.Integer recStatId;
    private java.lang.String roleName;
    private java.lang.String roleNameId;
    private java.lang.Integer roleTypeId;
}