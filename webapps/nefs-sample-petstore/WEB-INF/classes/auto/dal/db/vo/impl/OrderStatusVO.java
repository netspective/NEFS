package auto.dal.db.vo.impl;


public class OrderStatusVO
implements auto.dal.db.vo.OrderStatus
{
    
    public java.lang.Integer getItemnum()
    {
        return itemnum;
    }
    
    public int getItemnumInt()
    {
        return getItemnumInt(-1);
    }
    
    public int getItemnumInt(int defaultValue)
    {
        return itemnum != null ? itemnum.intValue() : defaultValue;
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
    
    public java.lang.Long getOrderstatusId()
    {
        return orderstatusId;
    }
    
    public long getOrderstatusIdLong()
    {
        return getOrderstatusIdLong(-1);
    }
    
    public long getOrderstatusIdLong(long defaultValue)
    {
        return orderstatusId != null ? orderstatusId.longValue() : defaultValue;
    }
    
    public java.lang.String getStatus()
    {
        return status;
    }
    
    public java.util.Date getTs()
    {
        return ts;
    }
    
    public void setItemnum(java.lang.Integer itemnum)
    {
        this.itemnum = itemnum;
    }
    
    public void setItemnumInt(int itemnum)
    {
        this.itemnum = new java.lang.Integer(itemnum);
    }
    
    public void setOrdersId(java.lang.Long ordersId)
    {
        this.ordersId = ordersId;
    }
    
    public void setOrdersIdLong(long ordersId)
    {
        this.ordersId = new java.lang.Long(ordersId);
    }
    
    public void setOrderstatusId(java.lang.Long orderstatusId)
    {
        this.orderstatusId = orderstatusId;
    }
    
    public void setOrderstatusIdLong(long orderstatusId)
    {
        this.orderstatusId = new java.lang.Long(orderstatusId);
    }
    
    public void setStatus(java.lang.String status)
    {
        this.status = status;
    }
    
    public void setTs(java.util.Date ts)
    {
        this.ts = ts;
    }
    private java.lang.Integer itemnum;
    private java.lang.Long ordersId;
    private java.lang.Long orderstatusId;
    private java.lang.String status;
    private java.util.Date ts;
}