package auto.dal.db.vo.impl;


public class ProductVO
implements auto.dal.db.vo.Product
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
    
    public java.lang.String getProductId()
    {
        return productId;
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
    
    public void setProductId(java.lang.String productId)
    {
        this.productId = productId;
    }
    private java.lang.String categoryId;
    private java.lang.String descr;
    private java.lang.String name;
    private java.lang.String productId;
}