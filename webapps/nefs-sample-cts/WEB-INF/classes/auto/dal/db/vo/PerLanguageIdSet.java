package auto.dal.db.vo;


public interface PerLanguageIdSet
{
    
    public java.lang.Integer getEnumIndex();
    
    public int getEnumIndexInt();
    
    public int getEnumIndexInt(int defaultValue);
    
    public java.lang.Integer getEnumValue();
    
    public int getEnumValueInt();
    
    public int getEnumValueInt(int defaultValue);
    
    public java.lang.Long getParentId();
    
    public long getParentIdLong();
    
    public long getParentIdLong(long defaultValue);
    
    public java.lang.Long getSystemId();
    
    public long getSystemIdLong();
    
    public long getSystemIdLong(long defaultValue);
    
    public void setEnumIndex(java.lang.Integer enumIndex);
    
    public void setEnumIndexInt(int enumIndex);
    
    public void setEnumValue(java.lang.Integer enumValue);
    
    public void setEnumValueInt(int enumValue);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setParentIdLong(long parentId);
    
    public void setSystemId(java.lang.Long systemId);
    
    public void setSystemIdLong(long systemId);
}