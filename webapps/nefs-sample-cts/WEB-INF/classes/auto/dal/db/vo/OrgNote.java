package auto.dal.db.vo;


public interface OrgNote
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getNoteType();
    
    public java.lang.Integer getNoteTypeId();
    
    public int getNoteTypeIdInt();
    
    public int getNoteTypeIdInt(int defaultValue);
    
    public java.lang.String getNotes();
    
    public java.lang.Long getParentId();
    
    public long getParentIdLong();
    
    public long getParentIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setNoteType(java.lang.String noteType);
    
    public void setNoteTypeId(java.lang.Integer noteTypeId);
    
    public void setNoteTypeIdInt(int noteTypeId);
    
    public void setNotes(java.lang.String notes);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setParentIdLong(long parentId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSystemId(java.lang.String systemId);
}