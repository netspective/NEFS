package auto.dal.db.vo;


public interface Item
{
    
    public java.lang.String getAttr1();
    
    public java.lang.String getAttr2();
    
    public java.lang.String getAttr3();
    
    public java.lang.String getDescr();
    
    public java.lang.String getImage();
    
    public java.lang.String getItemId();
    
    public java.lang.Float getListPrice();
    
    public float getListPriceFloat();
    
    public float getListPriceFloat(float defaultValue);
    
    public java.lang.String getName();
    
    public java.lang.String getProductId();
    
    public java.lang.String getStatus();
    
    public java.lang.Long getSupplierId();
    
    public long getSupplierIdLong();
    
    public long getSupplierIdLong(long defaultValue);
    
    public java.lang.Float getUnitCost();
    
    public float getUnitCostFloat();
    
    public float getUnitCostFloat(float defaultValue);
    
    public void setAttr1(java.lang.String attr1);
    
    public void setAttr2(java.lang.String attr2);
    
    public void setAttr3(java.lang.String attr3);
    
    public void setDescr(java.lang.String descr);
    
    public void setImage(java.lang.String image);
    
    public void setItemId(java.lang.String itemId);
    
    public void setListPrice(java.lang.Float listPrice);
    
    public void setListPriceFloat(float listPrice);
    
    public void setName(java.lang.String name);
    
    public void setProductId(java.lang.String productId);
    
    public void setStatus(java.lang.String status);
    
    public void setSupplierId(java.lang.Long supplierId);
    
    public void setSupplierIdLong(long supplierId);
    
    public void setUnitCost(java.lang.Float unitCost);
    
    public void setUnitCostFloat(float unitCost);
}