package auto.dal.db.vo.impl;


public class CategoryVO
implements auto.dal.db.vo.Category
{
    
    public java.lang.String getCategoryId()
    {
        return categoryId;
    }
    
    public java.lang.String getDescr()
    {
        return descr;
    }
    
    public java.lang.String getName()
    {
        return name;
    }
    
    public void setCategoryId(java.lang.String categoryId)
    {
        this.categoryId = categoryId;
    }
    
    public void setDescr(java.lang.String descr)
    {
        this.descr = descr;
    }
    
    public void setName(java.lang.String name)
    {
        this.name = name;
    }
    private java.lang.String categoryId;
    private java.lang.String descr;
    private java.lang.String name;
}