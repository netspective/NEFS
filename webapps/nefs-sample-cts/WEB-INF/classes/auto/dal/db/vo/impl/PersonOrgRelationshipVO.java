package auto.dal.db.vo.impl;


public class PersonOrgRelationshipVO
implements auto.dal.db.vo.PersonOrgRelationship
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.Long getParentId()
    {
        return parentId;
    }
    
    public long getParentIdLong()
    {
        return getParentIdLong(-1);
    }
    
    public long getParentIdLong(long defaultValue)
    {
        return parentId != null ? parentId.longValue() : defaultValue;
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
    
    public java.util.Date getRelBegin()
    {
        return relBegin;
    }
    
    public java.lang.String getRelDescr()
    {
        return relDescr;
    }
    
    public java.util.Date getRelEnd()
    {
        return relEnd;
    }
    
    public java.lang.Long getRelEntityId()
    {
        return relEntityId;
    }
    
    public long getRelEntityIdLong()
    {
        return getRelEntityIdLong(-1);
    }
    
    public long getRelEntityIdLong(long defaultValue)
    {
        return relEntityId != null ? relEntityId.longValue() : defaultValue;
    }
    
    public java.lang.String getRelType()
    {
        return relType;
    }
    
    public java.lang.Integer getRelTypeId()
    {
        return relTypeId;
    }
    
    public int getRelTypeIdInt()
    {
        return getRelTypeIdInt(-1);
    }
    
    public int getRelTypeIdInt(int defaultValue)
    {
        return relTypeId != null ? relTypeId.intValue() : defaultValue;
    }
    
    public java.lang.String getRelationshipCode()
    {
        return relationshipCode;
    }
    
    public java.lang.String getRelationshipName()
    {
        return relationshipName;
    }
    
    public java.lang.Long getSystemId()
    {
        return systemId;
    }
    
    public long getSystemIdLong()
    {
        return getSystemIdLong(-1);
    }
    
    public long getSystemIdLong(long defaultValue)
    {
        return systemId != null ? systemId.longValue() : defaultValue;
    }
    
    public void setCrSessId(java.lang.String crSessId)
    {
        this.crSessId = crSessId;
    }
    
    public void setCrStamp(java.util.Date crStamp)
    {
        this.crStamp = crStamp;
    }
    
    public void setParentId(java.lang.Long parentId)
    {
        this.parentId = parentId;
    }
    
    public void setParentIdLong(long parentId)
    {
        this.parentId = new java.lang.Long(parentId);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setRelBegin(java.util.Date relBegin)
    {
        this.relBegin = relBegin;
    }
    
    public void setRelDescr(java.lang.String relDescr)
    {
        this.relDescr = relDescr;
    }
    
    public void setRelEnd(java.util.Date relEnd)
    {
        this.relEnd = relEnd;
    }
    
    public void setRelEntityId(java.lang.Long relEntityId)
    {
        this.relEntityId = relEntityId;
    }
    
    public void setRelEntityIdLong(long relEntityId)
    {
        this.relEntityId = new java.lang.Long(relEntityId);
    }
    
    public void setRelType(java.lang.String relType)
    {
        this.relType = relType;
    }
    
    public void setRelTypeId(java.lang.Integer relTypeId)
    {
        this.relTypeId = relTypeId;
    }
    
    public void setRelTypeIdInt(int relTypeId)
    {
        this.relTypeId = new java.lang.Integer(relTypeId);
    }
    
    public void setRelationshipCode(java.lang.String relationshipCode)
    {
        this.relationshipCode = relationshipCode;
    }
    
    public void setRelationshipName(java.lang.String relationshipName)
    {
        this.relationshipName = relationshipName;
    }
    
    public void setSystemId(java.lang.Long systemId)
    {
        this.systemId = systemId;
    }
    
    public void setSystemIdLong(long systemId)
    {
        this.systemId = new java.lang.Long(systemId);
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.Long parentId;
    private java.lang.Integer recStatId;
    private java.util.Date relBegin;
    private java.lang.String relDescr;
    private java.util.Date relEnd;
    private java.lang.Long relEntityId;
    private java.lang.String relType;
    private java.lang.Integer relTypeId;
    private java.lang.String relationshipCode;
    private java.lang.String relationshipName;
    private java.lang.Long systemId;
}