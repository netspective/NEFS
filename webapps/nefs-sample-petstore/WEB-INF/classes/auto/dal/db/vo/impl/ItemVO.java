package auto.dal.db.vo.impl;


public class ItemVO
implements auto.dal.db.vo.Item
{
    
    public java.lang.String getAttr1()
    {
        return attr1;
    }
    
    public java.lang.String getAttr2()
    {
        return attr2;
    }
    
    public java.lang.String getAttr3()
    {
        return attr3;
    }
    
    public java.lang.String getDescr()
    {
        return descr;
    }
    
    public java.lang.String getImage()
    {
        return image;
    }
    
    public java.lang.String getItemId()
    {
        return itemId;
    }
    
    public java.lang.Float getListPrice()
    {
        return listPrice;
    }
    
    public float getListPriceFloat()
    {
        return getListPriceFloat(-1);
    }
    
    public float getListPriceFloat(float defaultValue)
    {
        return listPrice != null ? listPrice.floatValue() : defaultValue;
    }
    
    public java.lang.String getName()
    {
        return name;
    }
    
    public java.lang.String getProductId()
    {
        return productId;
    }
    
    public java.lang.String getStatus()
    {
        return status;
    }
    
    public java.lang.Long getSupplierId()
    {
        return supplierId;
    }
    
    public long getSupplierIdLong()
    {
        return getSupplierIdLong(-1);
    }
    
    public long getSupplierIdLong(long defaultValue)
    {
        return supplierId != null ? supplierId.longValue() : defaultValue;
    }
    
    public java.lang.Float getUnitCost()
    {
        return unitCost;
    }
    
    public float getUnitCostFloat()
    {
        return getUnitCostFloat(-1);
    }
    
    public float getUnitCostFloat(float defaultValue)
    {
        return unitCost != null ? unitCost.floatValue() : defaultValue;
    }
    
    public void setAttr1(java.lang.String attr1)
    {
        this.attr1 = attr1;
    }
    
    public void setAttr2(java.lang.String attr2)
    {
        this.attr2 = attr2;
    }
    
    public void setAttr3(java.lang.String attr3)
    {
        this.attr3 = attr3;
    }
    
    public void setDescr(java.lang.String descr)
    {
        this.descr = descr;
    }
    
    public void setImage(java.lang.String image)
    {
        this.image = image;
    }
    
    public void setItemId(java.lang.String itemId)
    {
        this.itemId = itemId;
    }
    
    public void setListPrice(java.lang.Float listPrice)
    {
        this.listPrice = listPrice;
    }
    
    public void setListPriceFloat(float listPrice)
    {
        this.listPrice = new java.lang.Float(listPrice);
    }
    
    public void setName(java.lang.String name)
    {
        this.name = name;
    }
    
    public void setProductId(java.lang.String productId)
    {
        this.productId = productId;
    }
    
    public void setStatus(java.lang.String status)
    {
        this.status = status;
    }
    
    public void setSupplierId(java.lang.Long supplierId)
    {
        this.supplierId = supplierId;
    }
    
    public void setSupplierIdLong(long supplierId)
    {
        this.supplierId = new java.lang.Long(supplierId);
    }
    
    public void setUnitCost(java.lang.Float unitCost)
    {
        this.unitCost = unitCost;
    }
    
    public void setUnitCostFloat(float unitCost)
    {
        this.unitCost = new java.lang.Float(unitCost);
    }
    private java.lang.String attr1;
    private java.lang.String attr2;
    private java.lang.String attr3;
    private java.lang.String descr;
    private java.lang.String image;
    private java.lang.String itemId;
    private java.lang.Float listPrice;
    private java.lang.String name;
    private java.lang.String productId;
    private java.lang.String status;
    private java.lang.Long supplierId;
    private java.lang.Float unitCost;
}