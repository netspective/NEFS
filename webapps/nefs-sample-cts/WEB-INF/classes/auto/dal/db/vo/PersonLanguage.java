package auto.dal.db.vo;


public interface PersonLanguage
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getLanguage();
    
    public java.lang.Integer getLanguageId();
    
    public int getLanguageIdInt();
    
    public int getLanguageIdInt(int defaultValue);
    
    public java.lang.Long getPersonId();
    
    public long getPersonIdLong();
    
    public long getPersonIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setLanguage(java.lang.String language);
    
    public void setLanguageId(java.lang.Integer languageId);
    
    public void setLanguageIdInt(int languageId);
    
    public void setPersonId(java.lang.Long personId);
    
    public void setPersonIdLong(long personId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSystemId(java.lang.String systemId);
}