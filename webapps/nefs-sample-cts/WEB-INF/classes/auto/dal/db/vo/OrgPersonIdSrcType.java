package auto.dal.db.vo;


public interface OrgPersonIdSrcType
{
    
    public java.lang.String getAbbrev();
    
    public java.lang.String getCaption();
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Integer getItemId();
    
    public int getItemIdInt();
    
    public int getItemIdInt(int defaultValue);
    
    public java.lang.Long getOrgId();
    
    public long getOrgIdLong();
    
    public long getOrgIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setAbbrev(java.lang.String abbrev);
    
    public void setCaption(java.lang.String caption);
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setItemId(java.lang.Integer itemId);
    
    public void setItemIdInt(int itemId);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setOrgIdLong(long orgId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSystemId(java.lang.String systemId);
}