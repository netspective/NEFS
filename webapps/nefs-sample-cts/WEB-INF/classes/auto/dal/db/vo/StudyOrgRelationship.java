package auto.dal.db.vo;


public interface StudyOrgRelationship
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Long getParentId();
    
    public java.lang.Integer getRecStatId();
    
    public java.util.Date getRelBegin();
    
    public java.lang.String getRelDescr();
    
    public java.util.Date getRelEnd();
    
    public java.lang.Long getRelEntityId();
    
    public java.lang.String getRelType();
    
    public java.lang.Integer getRelTypeId();
    
    public java.lang.Long getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRelBegin(java.util.Date relBegin);
    
    public void setRelDescr(java.lang.String relDescr);
    
    public void setRelEnd(java.util.Date relEnd);
    
    public void setRelEntityId(java.lang.Long relEntityId);
    
    public void setRelType(java.lang.String relType);
    
    public void setRelTypeId(java.lang.Integer relTypeId);
    
    public void setSystemId(java.lang.Long systemId);
}