package auto.dal.db.vo;


public interface OrderStatus
{
    
    public java.lang.Integer getItemnum();
    
    public int getItemnumInt();
    
    public int getItemnumInt(int defaultValue);
    
    public java.lang.Long getOrdersId();
    
    public long getOrdersIdLong();
    
    public long getOrdersIdLong(long defaultValue);
    
    public java.lang.Long getOrderstatusId();
    
    public long getOrderstatusIdLong();
    
    public long getOrderstatusIdLong(long defaultValue);
    
    public java.lang.String getStatus();
    
    public java.util.Date getTs();
    
    public void setItemnum(java.lang.Integer itemnum);
    
    public void setItemnumInt(int itemnum);
    
    public void setOrdersId(java.lang.Long ordersId);
    
    public void setOrdersIdLong(long ordersId);
    
    public void setOrderstatusId(java.lang.Long orderstatusId);
    
    public void setOrderstatusIdLong(long orderstatusId);
    
    public void setStatus(java.lang.String status);
    
    public void setTs(java.util.Date ts);
}