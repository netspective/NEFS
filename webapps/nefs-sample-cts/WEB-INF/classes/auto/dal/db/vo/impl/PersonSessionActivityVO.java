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
    
    public int getActionTypeInt()
    {
        return getActionTypeInt(-1);
    }
    
    public int getActionTypeInt(int defaultValue)
    {
        return actionType != null ? actionType.intValue() : defaultValue;
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
    
    public int getActivityTypeInt()
    {
        return getActivityTypeInt(-1);
    }
    
    public int getActivityTypeInt(int defaultValue)
    {
        return activityType != null ? activityType.intValue() : defaultValue;
    }
    
    public java.lang.Integer getDetailLevel()
    {
        return detailLevel;
    }
    
    public int getDetailLevelInt()
    {
        return getDetailLevelInt(-1);
    }
    
    public int getDetailLevelInt(int defaultValue)
    {
        return detailLevel != null ? detailLevel.intValue() : defaultValue;
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
    
    public void setActionTypeInt(int actionType)
    {
        this.actionType = new java.lang.Integer(actionType);
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
    
    public void setActivityTypeInt(int activityType)
    {
        this.activityType = new java.lang.Integer(activityType);
    }
    
    public void setDetailLevel(java.lang.Integer detailLevel)
    {
        this.detailLevel = detailLevel;
    }
    
    public void setDetailLevelInt(int detailLevel)
    {
        this.detailLevel = new java.lang.Integer(detailLevel);
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