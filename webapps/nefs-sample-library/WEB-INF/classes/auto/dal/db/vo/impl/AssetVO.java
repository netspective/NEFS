package auto.dal.db.vo.impl;


public class AssetVO
implements auto.dal.db.vo.Asset
{
    
    public java.lang.Long getAssetId()
    {
        return assetId;
    }
    
    public long getAssetIdLong()
    {
        return getAssetIdLong(-1);
    }
    
    public long getAssetIdLong(long defaultValue)
    {
        return assetId != null ? assetId.longValue() : defaultValue;
    }
    
    public java.lang.String getName()
    {
        return name;
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
    
    public java.lang.Integer getType()
    {
        return type;
    }
    
    public int getTypeInt()
    {
        return getTypeInt(-1);
    }
    
    public int getTypeInt(int defaultValue)
    {
        return type != null ? type.intValue() : defaultValue;
    }
    
    public void setAssetId(java.lang.Long assetId)
    {
        this.assetId = assetId;
    }
    
    public void setAssetIdLong(long assetId)
    {
        this.assetId = new java.lang.Long(assetId);
    }
    
    public void setName(java.lang.String name)
    {
        this.name = name;
    }
    
    public void setQuantity(java.lang.Integer quantity)
    {
        this.quantity = quantity;
    }
    
    public void setQuantityInt(int quantity)
    {
        this.quantity = new java.lang.Integer(quantity);
    }
    
    public void setType(java.lang.Integer type)
    {
        this.type = type;
    }
    
    public void setTypeInt(int type)
    {
        this.type = new java.lang.Integer(type);
    }
    private java.lang.Long assetId;
    private java.lang.String name;
    private java.lang.Integer quantity;
    private java.lang.Integer type;
}