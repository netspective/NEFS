package auto.dal.db.vo;


public interface LineItem
{
    
    public java.lang.String getItemId();
    
    public java.lang.Integer getItemnum();
    
    public int getItemnumInt();
    
    public int getItemnumInt(int defaultValue);
    
    public java.lang.Long getLineitemId();
    
    public long getLineitemIdLong();
    
    public long getLineitemIdLong(long defaultValue);
    
    public java.lang.Long getOrdersId();
    
    public long getOrdersIdLong();
    
    public long getOrdersIdLong(long defaultValue);
    
    public java.lang.Integer getQuantity();
    
    public int getQuantityInt();
    
    public int getQuantityInt(int defaultValue);
    
    public java.lang.Float getUnitPrice();
    
    public float getUnitPriceFloat();
    
    public float getUnitPriceFloat(float defaultValue);
    
    public void setItemId(java.lang.String itemId);
    
    public void setItemnum(java.lang.Integer itemnum);
    
    public void setItemnumInt(int itemnum);
    
    public void setLineitemId(java.lang.Long lineitemId);
    
    public void setLineitemIdLong(long lineitemId);
    
    public void setOrdersId(java.lang.Long ordersId);
    
    public void setOrdersIdLong(long ordersId);
    
    public void setQuantity(java.lang.Integer quantity);
    
    public void setQuantityInt(int quantity);
    
    public void setUnitPrice(java.lang.Float unitPrice);
    
    public void setUnitPriceFloat(float unitPrice);
}