package auto.dal.db.vo;


public interface PersonOrgRelationshipMap
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Integer getInvRelTypeId();
    
    public int getInvRelTypeIdInt();
    
    public int getInvRelTypeIdInt(int defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.Integer getRelTypeId();
    
    public int getRelTypeIdInt();
    
    public int getRelTypeIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setInvRelTypeId(java.lang.Integer invRelTypeId);
    
    public void setInvRelTypeIdInt(int invRelTypeId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setRelTypeId(java.lang.Integer relTypeId);
    
    public void setRelTypeIdInt(int relTypeId);
    
    public void setSystemId(java.lang.String systemId);
}