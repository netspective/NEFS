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
    
    public long getOrgIdLong()
    {
        return getOrgIdLong(-1);
    }
    
    public long getOrgIdLong(long defaultValue)
    {
        return orgId != null ? orgId.longValue() : defaultValue;
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
    
    public int getRoleTypeIdInt()
    {
        return getRoleTypeIdInt(-1);
    }
    
    public int getRoleTypeIdInt(int defaultValue)
    {
        return roleTypeId != null ? roleTypeId.intValue() : defaultValue;
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
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
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
    
    public void setRoleTypeIdInt(int roleTypeId)
    {
        this.roleTypeId = new java.lang.Integer(roleTypeId);
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.Long orgId;
    private java.lang.Integer recStatId;
    private java.lang.String roleName;
    private java.lang.String roleNameId;
    private java.lang.Integer roleTypeId;
}