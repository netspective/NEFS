package auto.dal.db.vo;


public interface PersonAddress
{
    
    public java.lang.String getAddressName();
    
    public java.lang.Integer getAddressTypeId();
    
    public int getAddressTypeIdInt();
    
    public int getAddressTypeIdInt(int defaultValue);
    
    public java.lang.String getCity();
    
    public java.lang.String getCountry();
    
    public java.lang.String getCounty();
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getLine1();
    
    public java.lang.String getLine2();
    
    public java.lang.Boolean getMailing();
    
    public java.lang.Long getParentId();
    
    public long getParentIdLong();
    
    public long getParentIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getState();
    
    public java.lang.Integer getStateId();
    
    public int getStateIdInt();
    
    public int getStateIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public java.lang.String getZip();
    
    public void setAddressName(java.lang.String addressName);
    
    public void setAddressTypeId(java.lang.Integer addressTypeId);
    
    public void setAddressTypeIdInt(int addressTypeId);
    
    public void setCity(java.lang.String city);
    
    public void setCountry(java.lang.String country);
    
    public void setCounty(java.lang.String county);
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setLine1(java.lang.String line1);
    
    public void setLine2(java.lang.String line2);
    
    public void setMailing(java.lang.Boolean mailing);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setParentIdLong(long parentId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setState(java.lang.String state);
    
    public void setStateId(java.lang.Integer stateId);
    
    public void setStateIdInt(int stateId);
    
    public void setSystemId(java.lang.String systemId);
    
    public void setZip(java.lang.String zip);
}