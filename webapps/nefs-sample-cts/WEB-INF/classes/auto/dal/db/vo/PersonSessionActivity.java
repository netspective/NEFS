package auto.dal.db.vo;


public interface PersonSessionActivity
{
    
    public java.lang.String getActionKey();
    
    public java.lang.String getActionScope();
    
    public java.lang.Integer getActionType();
    
    public int getActionTypeInt();
    
    public int getActionTypeInt(int defaultValue);
    
    public java.lang.String getActivityData();
    
    public java.util.Date getActivityStamp();
    
    public java.lang.Integer getActivityType();
    
    public int getActivityTypeInt();
    
    public int getActivityTypeInt(int defaultValue);
    
    public java.lang.Integer getDetailLevel();
    
    public int getDetailLevelInt();
    
    public int getDetailLevelInt(int defaultValue);
    
    public java.lang.String getSessionId();
    
    public void setActionKey(java.lang.String actionKey);
    
    public void setActionScope(java.lang.String actionScope);
    
    public void setActionType(java.lang.Integer actionType);
    
    public void setActionTypeInt(int actionType);
    
    public void setActivityData(java.lang.String activityData);
    
    public void setActivityStamp(java.util.Date activityStamp);
    
    public void setActivityType(java.lang.Integer activityType);
    
    public void setActivityTypeInt(int activityType);
    
    public void setDetailLevel(java.lang.Integer detailLevel);
    
    public void setDetailLevelInt(int detailLevel);
    
    public void setSessionId(java.lang.String sessionId);
}