package auto.dal.db.vo.impl;


public class PersonLoginVO
implements auto.dal.db.vo.PersonLogin
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.Integer getLoginStatus()
    {
        return loginStatus;
    }
    
    public java.lang.String getPassword()
    {
        return password;
    }
    
    public java.lang.Long getPersonId()
    {
        return personId;
    }
    
    public java.lang.Integer getQuantity()
    {
        return quantity;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public java.lang.String getSystemId()
    {
        return systemId;
    }
    
    public java.lang.String getUserId()
    {
        return userId;
    }
    
    public void setCrSessId(java.lang.String crSessId)
    {
        this.crSessId = crSessId;
    }
    
    public void setCrStamp(java.util.Date crStamp)
    {
        this.crStamp = crStamp;
    }
    
    public void setLoginStatus(java.lang.Integer loginStatus)
    {
        this.loginStatus = loginStatus;
    }
    
    public void setPassword(java.lang.String password)
    {
        this.password = password;
    }
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setQuantity(java.lang.Integer quantity)
    {
        this.quantity = quantity;
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    
    public void setUserId(java.lang.String userId)
    {
        this.userId = userId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.Integer loginStatus;
    private java.lang.String password;
    private java.lang.Long personId;
    private java.lang.Integer quantity;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
    private java.lang.String userId;
}