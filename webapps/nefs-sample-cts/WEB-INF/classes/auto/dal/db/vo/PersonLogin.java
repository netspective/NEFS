package auto.dal.db.vo;


public interface PersonLogin
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Integer getLoginStatus();
    
    public java.lang.String getPassword();
    
    public java.lang.Long getPersonId();
    
    public java.lang.Integer getQuantity();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getSystemId();
    
    public java.lang.String getUserId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setLoginStatus(java.lang.Integer loginStatus);
    
    public void setPassword(java.lang.String password);
    
    public void setPersonId(java.lang.Long personId);
    
    public void setQuantity(java.lang.Integer quantity);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSystemId(java.lang.String systemId);
    
    public void setUserId(java.lang.String userId);
}