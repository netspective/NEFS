package auto.dal.db.vo;


public interface Asset
{
    
    public java.lang.Long getAssetId();
    
    public long getAssetIdLong();
    
    public long getAssetIdLong(long defaultValue);
    
    public java.lang.String getName();
    
    public java.lang.Integer getQuantity();
    
    public int getQuantityInt();
    
    public int getQuantityInt(int defaultValue);
    
    public java.lang.Integer getType();
    
    public int getTypeInt();
    
    public int getTypeInt(int defaultValue);
    
    public void setAssetId(java.lang.Long assetId);
    
    public void setAssetIdLong(long assetId);
    
    public void setName(java.lang.String name);
    
    public void setQuantity(java.lang.Integer quantity);
    
    public void setQuantityInt(int quantity);
    
    public void setType(java.lang.Integer type);
    
    public void setTypeInt(int type);
}