package auto.dal.db.vo;


public interface OrgNote
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getNoteType();
    
    public java.lang.Integer getNoteTypeId();
    
    public java.lang.String getNotes();
    
    public java.lang.Long getParentId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setNoteType(java.lang.String noteType);
    
    public void setNoteTypeId(java.lang.Integer noteTypeId);
    
    public void setNotes(java.lang.String notes);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSystemId(java.lang.String systemId);
}