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
    
    public int getLoginStatusInt()
    {
        return getLoginStatusInt(-1);
    }
    
    public int getLoginStatusInt(int defaultValue)
    {
        return loginStatus != null ? loginStatus.intValue() : defaultValue;
    }
    
    public java.lang.String getPassword()
    {
        return password;
    }
    
    public java.lang.Long getPersonId()
    {
        return personId;
    }
    
    public long getPersonIdLong()
    {
        return getPersonIdLong(-1);
    }
    
    public long getPersonIdLong(long defaultValue)
    {
        return personId != null ? personId.longValue() : defaultValue;
    }
    
    public java.lang.Integer getQuantity()
    {
        return quantity;
    }
    
    public int getQuantityInt()
    {
        return getQuantityInt(-1);
    }
    
    public int getQuantityInt(int defaultValue)
    {
        return quantity != null ? quantity.intValue() : defaultValue;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public int getRecStatIdInt()
    {
        return getRecStatIdInt(-1);
    }
    
    public int getRecStatIdInt(int defaultValue)
    {
        return recStatId != null ? recStatId.intValue() : defaultValue;
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
    
    public void setLoginStatusInt(int loginStatus)
    {
        this.loginStatus = new java.lang.Integer(loginStatus);
    }
    
    public void setPassword(java.lang.String password)
    {
        this.password = password;
    }
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setPersonIdLong(long personId)
    {
        this.personId = new java.lang.Long(personId);
    }
    
    public void setQuantity(java.lang.Integer quantity)
    {
        this.quantity = quantity;
    }
    
    public void setQuantityInt(int quantity)
    {
        this.quantity = new java.lang.Integer(quantity);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
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