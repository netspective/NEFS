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
    
    public java.lang.String getNotes()
    {
        return notes;
    }
    
    public java.lang.Long getParentId()
    {
        return parentId;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
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
    
    public void setNotes(java.lang.String notes)
    {
        this.notes = notes;
    }
    
    public void setParentId(java.lang.Long parentId)
    {
        this.parentId = parentId;
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
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