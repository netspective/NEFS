package auto.dal.db.vo.impl;


public class OrdersVO
implements auto.dal.db.vo.Orders
{
    
    public java.lang.String getAccountId()
    {
        return accountId;
    }
    
    public java.lang.String getBillAddr1()
    {
        return billAddr1;
    }
    
    public java.lang.String getBillAddr2()
    {
        return billAddr2;
    }
    
    public java.lang.String getBillCity()
    {
        return billCity;
    }
    
    public java.lang.String getBillCountry()
    {
        return billCountry;
    }
    
    public java.lang.String getBillPhone()
    {
        return billPhone;
    }
    
    public java.lang.String getBillState()
    {
        return billState;
    }
    
    public java.util.Date getOrderDate()
    {
        return orderDate;
    }
    
    public java.lang.Long getOrdersId()
    {
        return ordersId;
    }
    
    public long getOrdersIdLong()
    {
        return getOrdersIdLong(-1);
    }
    
    public long getOrdersIdLong(long defaultValue)
    {
        return ordersId != null ? ordersId.longValue() : defaultValue;
    }
    
    public java.lang.String getShipAddr1()
    {
        return shipAddr1;
    }
    
    public java.lang.String getShipAddr2()
    {
        return shipAddr2;
    }
    
    public java.lang.String getShipCity()
    {
        return shipCity;
    }
    
    public java.lang.String getShipCountry()
    {
        return shipCountry;
    }
    
    public java.lang.String getShipPhone()
    {
        return shipPhone;
    }
    
    public java.lang.String getShipState()
    {
        return shipState;
    }
    
    public void setAccountId(java.lang.String accountId)
    {
        this.accountId = accountId;
    }
    
    public void setBillAddr1(java.lang.String billAddr1)
    {
        this.billAddr1 = billAddr1;
    }
    
    public void setBillAddr2(java.lang.String billAddr2)
    {
        this.billAddr2 = billAddr2;
    }
    
    public void setBillCity(java.lang.String billCity)
    {
        this.billCity = billCity;
    }
    
    public void setBillCountry(java.lang.String billCountry)
    {
        this.billCountry = billCountry;
    }
    
    public void setBillPhone(java.lang.String billPhone)
    {
        this.billPhone = billPhone;
    }
    
    public void setBillState(java.lang.String billState)
    {
        this.billState = billState;
    }
    
    public void setOrderDate(java.util.Date orderDate)
    {
        this.orderDate = orderDate;
    }
    
    public void setOrdersId(java.lang.Long ordersId)
    {
        this.ordersId = ordersId;
    }
    
    public void setOrdersIdLong(long ordersId)
    {
        this.ordersId = new java.lang.Long(ordersId);
    }
    
    public void setShipAddr1(java.lang.String shipAddr1)
    {
        this.shipAddr1 = shipAddr1;
    }
    
    public void setShipAddr2(java.lang.String shipAddr2)
    {
        this.shipAddr2 = shipAddr2;
    }
    
    public void setShipCity(java.lang.String shipCity)
    {
        this.shipCity = shipCity;
    }
    
    public void setShipCountry(java.lang.String shipCountry)
    {
        this.shipCountry = shipCountry;
    }
    
    public void setShipPhone(java.lang.String shipPhone)
    {
        this.shipPhone = shipPhone;
    }
    
    public void setShipState(java.lang.String shipState)
    {
        this.shipState = shipState;
    }
    private java.lang.String accountId;
    private java.lang.String billAddr1;
    private java.lang.String billAddr2;
    private java.lang.String billCity;
    private java.lang.String billCountry;
    private java.lang.String billPhone;
    private java.lang.String billState;
    private java.util.Date orderDate;
    private java.lang.Long ordersId;
    private java.lang.String shipAddr1;
    private java.lang.String shipAddr2;
    private java.lang.String shipCity;
    private java.lang.String shipCountry;
    private java.lang.String shipPhone;
    private java.lang.String shipState;
}