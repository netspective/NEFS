package auto.dal.db.vo.impl;


public class OrgPersonIdSrcTypeVO
implements auto.dal.db.vo.OrgPersonIdSrcType
{
    
    public java.lang.String getAbbrev()
    {
        return abbrev;
    }
    
    public java.lang.String getCaption()
    {
        return caption;
    }
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.Integer getItemId()
    {
        return itemId;
    }
    
    public int getItemIdInt()
    {
        return getItemIdInt(-1);
    }
    
    public int getItemIdInt(int defaultValue)
    {
        return itemId != null ? itemId.intValue() : defaultValue;
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
    
    public java.lang.String getSystemId()
    {
        return systemId;
    }
    
    public void setAbbrev(java.lang.String abbrev)
    {
        this.abbrev = abbrev;
    }
    
    public void setCaption(java.lang.String caption)
    {
        this.caption = caption;
    }
    
    public void setCrSessId(java.lang.String crSessId)
    {
        this.crSessId = crSessId;
    }
    
    public void setCrStamp(java.util.Date crStamp)
    {
        this.crStamp = crStamp;
    }
    
    public void setItemId(java.lang.Integer itemId)
    {
        this.itemId = itemId;
    }
    
    public void setItemIdInt(int itemId)
    {
        this.itemId = new java.lang.Integer(itemId);
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
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String abbrev;
    private java.lang.String caption;
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.Integer itemId;
    private java.lang.Long orgId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}