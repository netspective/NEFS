package auto.dal.db.vo.impl;


public class PersonSessionViewCountVO
implements auto.dal.db.vo.PersonSessionViewCount
{
    
    public java.lang.String getSessionId()
    {
        return sessionId;
    }
    
    public java.lang.String getViewArl()
    {
        return viewArl;
    }
    
    public java.lang.String getViewCaption()
    {
        return viewCaption;
    }
    
    public java.lang.Integer getViewCount()
    {
        return viewCount;
    }
    
    public int getViewCountInt()
    {
        return getViewCountInt(-1);
    }
    
    public int getViewCountInt(int defaultValue)
    {
        return viewCount != null ? viewCount.intValue() : defaultValue;
    }
    
    public java.util.Date getViewInit()
    {
        return viewInit;
    }
    
    public java.lang.String getViewKey()
    {
        return viewKey;
    }
    
    public java.util.Date getViewLatest()
    {
        return viewLatest;
    }
    
    public java.lang.String getViewScope()
    {
        return viewScope;
    }
    
    public void setSessionId(java.lang.String sessionId)
    {
        this.sessionId = sessionId;
    }
    
    public void setViewArl(java.lang.String viewArl)
    {
        this.viewArl = viewArl;
    }
    
    public void setViewCaption(java.lang.String viewCaption)
    {
        this.viewCaption = viewCaption;
    }
    
    public void setViewCount(java.lang.Integer viewCount)
    {
        this.viewCount = viewCount;
    }
    
    public void setViewCountInt(int viewCount)
    {
        this.viewCount = new java.lang.Integer(viewCount);
    }
    
    public void setViewInit(java.util.Date viewInit)
    {
        this.viewInit = viewInit;
    }
    
    public void setViewKey(java.lang.String viewKey)
    {
        this.viewKey = viewKey;
    }
    
    public void setViewLatest(java.util.Date viewLatest)
    {
        this.viewLatest = viewLatest;
    }
    
    public void setViewScope(java.lang.String viewScope)
    {
        this.viewScope = viewScope;
    }
    private java.lang.String sessionId;
    private java.lang.String viewArl;
    private java.lang.String viewCaption;
    private java.lang.Integer viewCount;
    private java.util.Date viewInit;
    private java.lang.String viewKey;
    private java.util.Date viewLatest;
    private java.lang.String viewScope;
}