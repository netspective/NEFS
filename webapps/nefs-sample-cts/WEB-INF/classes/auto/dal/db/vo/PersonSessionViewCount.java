package auto.dal.db.vo;


public interface PersonSessionViewCount
{
    
    public java.lang.String getSessionId();
    
    public java.lang.String getViewArl();
    
    public java.lang.String getViewCaption();
    
    public java.lang.Integer getViewCount();
    
    public int getViewCountInt();
    
    public int getViewCountInt(int defaultValue);
    
    public java.util.Date getViewInit();
    
    public java.lang.String getViewKey();
    
    public java.util.Date getViewLatest();
    
    public java.lang.String getViewScope();
    
    public void setSessionId(java.lang.String sessionId);
    
    public void setViewArl(java.lang.String viewArl);
    
    public void setViewCaption(java.lang.String viewCaption);
    
    public void setViewCount(java.lang.Integer viewCount);
    
    public void setViewCountInt(int viewCount);
    
    public void setViewInit(java.util.Date viewInit);
    
    public void setViewKey(java.lang.String viewKey);
    
    public void setViewLatest(java.util.Date viewLatest);
    
    public void setViewScope(java.lang.String viewScope);
}