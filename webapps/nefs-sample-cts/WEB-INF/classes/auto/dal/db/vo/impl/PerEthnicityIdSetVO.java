package auto.dal.db.vo.impl;


public class PerEthnicityIdSetVO
implements auto.dal.db.vo.PerEthnicityIdSet
{
    
    public java.lang.Integer getEnumIndex()
    {
        return enumIndex;
    }
    
    public int getEnumIndexInt()
    {
        return getEnumIndexInt(-1);
    }
    
    public int getEnumIndexInt(int defaultValue)
    {
        return enumIndex != null ? enumIndex.intValue() : defaultValue;
    }
    
    public java.lang.Integer getEnumValue()
    {
        return enumValue;
    }
    
    public int getEnumValueInt()
    {
        return getEnumValueInt(-1);
    }
    
    public int getEnumValueInt(int defaultValue)
    {
        return enumValue != null ? enumValue.intValue() : defaultValue;
    }
    
    public java.lang.Long getParentId()
    {
        return parentId;
    }
    
    public long getParentIdLong()
    {
        return getParentIdLong(-1);
    }
    
    public long getParentIdLong(long defaultValue)
    {
        return parentId != null ? parentId.longValue() : defaultValue;
    }
    
    public java.lang.Long getSystemId()
    {
        return systemId;
    }
    
    public long getSystemIdLong()
    {
        return getSystemIdLong(-1);
    }
    
    public long getSystemIdLong(long defaultValue)
    {
        return systemId != null ? systemId.longValue() : defaultValue;
    }
    
    public void setEnumIndex(java.lang.Integer enumIndex)
    {
        this.enumIndex = enumIndex;
    }
    
    public void setEnumIndexInt(int enumIndex)
    {
        this.enumIndex = new java.lang.Integer(enumIndex);
    }
    
    public void setEnumValue(java.lang.Integer enumValue)
    {
        this.enumValue = enumValue;
    }
    
    public void setEnumValueInt(int enumValue)
    {
        this.enumValue = new java.lang.Integer(enumValue);
    }
    
    public void setParentId(java.lang.Long parentId)
    {
        this.parentId = parentId;
    }
    
    public void setParentIdLong(long parentId)
    {
        this.parentId = new java.lang.Long(parentId);
    }
    
    public void setSystemId(java.lang.Long systemId)
    {
        this.systemId = systemId;
    }
    
    public void setSystemIdLong(long systemId)
    {
        this.systemId = new java.lang.Long(systemId);
    }
    private java.lang.Integer enumIndex;
    private java.lang.Integer enumValue;
    private java.lang.Long parentId;
    private java.lang.Long systemId;
}