package auto.dal.db.vo.impl;


public class OrgNoteVO
implements auto.dal.db.vo.OrgNote
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.String getNoteType()
    {
        return noteType;
    }
    
    public java.lang.Integer getNoteTypeId()
    {
        return noteTypeId;
    }
    
    public int getNoteTypeIdInt()
    {
        return getNoteTypeIdInt(-1);
    }
    
    public int getNoteTypeIdInt(int defaultValue)
    {
        return noteTypeId != null ? noteTypeId.intValue() : defaultValue;
    }
    
    public java.lang.String getNotes()
    {
        return notes;
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
    
    public void setNoteType(java.lang.String noteType)
    {
        this.noteType = noteType;
    }
    
    public void setNoteTypeId(java.lang.Integer noteTypeId)
    {
        this.noteTypeId = noteTypeId;
    }
    
    public void setNoteTypeIdInt(int noteTypeId)
    {
        this.noteTypeId = new java.lang.Integer(noteTypeId);
    }
    
    public void setNotes(java.lang.String notes)
    {
        this.notes = notes;
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
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.String noteType;
    private java.lang.Integer noteTypeId;
    private java.lang.String notes;
    private java.lang.Long parentId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}