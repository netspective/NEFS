package auto.dal.db.vo.impl;


public class PerLanguageIdSetVO
implements auto.dal.db.vo.PerLanguageIdSet
{
    
    public java.lang.Integer getEnumIndex()
    {
        return enumIndex;
    }
    
    public java.lang.Integer getEnumValue()
    {
        return enumValue;
    }
    
    public java.lang.Long getParentId()
    {
        return parentId;
    }
    
    public java.lang.Long getSystemId()
    {
        return systemId;
    }
    
    public void setEnumIndex(java.lang.Integer enumIndex)
    {
        this.enumIndex = enumIndex;
    }
    
    public void setEnumValue(java.lang.Integer enumValue)
    {
        this.enumValue = enumValue;
    }
    
    public void setParentId(java.lang.Long parentId)
    {
        this.parentId = parentId;
    }
    
    public void setSystemId(java.lang.Long systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.Integer enumIndex;
    private java.lang.Integer enumValue;
    private java.lang.Long parentId;
    private java.lang.Long systemId;
}