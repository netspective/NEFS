package auto.dal.db.vo.impl;


public class PersonSessionActivityVO
implements auto.dal.db.vo.PersonSessionActivity
{
    
    public java.lang.String getActionKey()
    {
        return actionKey;
    }
    
    public java.lang.String getActionScope()
    {
        return actionScope;
    }
    
    public java.lang.Integer getActionType()
    {
        return actionType;
    }
    
    public java.lang.String getActivityData()
    {
        return activityData;
    }
    
    public java.util.Date getActivityStamp()
    {
        return activityStamp;
    }
    
    public java.lang.Integer getActivityType()
    {
        return activityType;
    }
    
    public java.lang.Integer getDetailLevel()
    {
        return detailLevel;
    }
    
    public java.lang.String getSessionId()
    {
        return sessionId;
    }
    
    public void setActionKey(java.lang.String actionKey)
    {
        this.actionKey = actionKey;
    }
    
    public void setActionScope(java.lang.String actionScope)
    {
        this.actionScope = actionScope;
    }
    
    public void setActionType(java.lang.Integer actionType)
    {
        this.actionType = actionType;
    }
    
    public void setActivityData(java.lang.String activityData)
    {
        this.activityData = activityData;
    }
    
    public void setActivityStamp(java.util.Date activityStamp)
    {
        this.activityStamp = activityStamp;
    }
    
    public void setActivityType(java.lang.Integer activityType)
    {
        this.activityType = activityType;
    }
    
    public void setDetailLevel(java.lang.Integer detailLevel)
    {
        this.detailLevel = detailLevel;
    }
    
    public void setSessionId(java.lang.String sessionId)
    {
        this.sessionId = sessionId;
    }
    private java.lang.String actionKey;
    private java.lang.String actionScope;
    private java.lang.Integer actionType;
    private java.lang.String activityData;
    private java.util.Date activityStamp;
    private java.lang.Integer activityType;
    private java.lang.Integer detailLevel;
    private java.lang.String sessionId;
}