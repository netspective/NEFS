package auto.dal.db.vo;


public interface Orders
{
    
    public java.lang.String getAccountId();
    
    public java.lang.String getBillAddr1();
    
    public java.lang.String getBillAddr2();
    
    public java.lang.String getBillCity();
    
    public java.lang.String getBillCountry();
    
    public java.lang.String getBillPhone();
    
    public java.lang.String getBillState();
    
    public java.util.Date getOrderDate();
    
    public java.lang.Long getOrdersId();
    
    public long getOrdersIdLong();
    
    public long getOrdersIdLong(long defaultValue);
    
    public java.lang.String getShipAddr1();
    
    public java.lang.String getShipAddr2();
    
    public java.lang.String getShipCity();
    
    public java.lang.String getShipCountry();
    
    public java.lang.String getShipPhone();
    
    public java.lang.String getShipState();
    
    public void setAccountId(java.lang.String accountId);
    
    public void setBillAddr1(java.lang.String billAddr1);
    
    public void setBillAddr2(java.lang.String billAddr2);
    
    public void setBillCity(java.lang.String billCity);
    
    public void setBillCountry(java.lang.String billCountry);
    
    public void setBillPhone(java.lang.String billPhone);
    
    public void setBillState(java.lang.String billState);
    
    public void setOrderDate(java.util.Date orderDate);
    
    public void setOrdersId(java.lang.Long ordersId);
    
    public void setOrdersIdLong(long ordersId);
    
    public void setShipAddr1(java.lang.String shipAddr1);
    
    public void setShipAddr2(java.lang.String shipAddr2);
    
    public void setShipCity(java.lang.String shipCity);
    
    public void setShipCountry(java.lang.String shipCountry);
    
    public void setShipPhone(java.lang.String shipPhone);
    
    public void setShipState(java.lang.String shipState);
}