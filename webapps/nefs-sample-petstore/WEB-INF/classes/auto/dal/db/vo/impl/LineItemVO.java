package auto.dal.db.vo.impl;


public class LineItemVO
implements auto.dal.db.vo.LineItem
{
    
    public java.lang.String getItemId()
    {
        return itemId;
    }
    
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
    
    public java.lang.Long getLineitemId()
    {
        return lineitemId;
    }
    
    public long getLineitemIdLong()
    {
        return getLineitemIdLong(-1);
    }
    
    public long getLineitemIdLong(long defaultValue)
    {
        return lineitemId != null ? lineitemId.longValue() : defaultValue;
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
    
    public java.lang.Float getUnitPrice()
    {
        return unitPrice;
    }
    
    public float getUnitPriceFloat()
    {
        return getUnitPriceFloat(-1);
    }
    
    public float getUnitPriceFloat(float defaultValue)
    {
        return unitPrice != null ? unitPrice.floatValue() : defaultValue;
    }
    
    public void setItemId(java.lang.String itemId)
    {
        this.itemId = itemId;
    }
    
    public void setItemnum(java.lang.Integer itemnum)
    {
        this.itemnum = itemnum;
    }
    
    public void setItemnumInt(int itemnum)
    {
        this.itemnum = new java.lang.Integer(itemnum);
    }
    
    public void setLineitemId(java.lang.Long lineitemId)
    {
        this.lineitemId = lineitemId;
    }
    
    public void setLineitemIdLong(long lineitemId)
    {
        this.lineitemId = new java.lang.Long(lineitemId);
    }
    
    public void setOrdersId(java.lang.Long ordersId)
    {
        this.ordersId = ordersId;
    }
    
    public void setOrdersIdLong(long ordersId)
    {
        this.ordersId = new java.lang.Long(ordersId);
    }
    
    public void setQuantity(java.lang.Integer quantity)
    {
        this.quantity = quantity;
    }
    
    public void setQuantityInt(int quantity)
    {
        this.quantity = new java.lang.Integer(quantity);
    }
    
    public void setUnitPrice(java.lang.Float unitPrice)
    {
        this.unitPrice = unitPrice;
    }
    
    public void setUnitPriceFloat(float unitPrice)
    {
        this.unitPrice = new java.lang.Float(unitPrice);
    }
    private java.lang.String itemId;
    private java.lang.Integer itemnum;
    private java.lang.Long lineitemId;
    private java.lang.Long ordersId;
    private java.lang.Integer quantity;
    private java.lang.Float unitPrice;
}