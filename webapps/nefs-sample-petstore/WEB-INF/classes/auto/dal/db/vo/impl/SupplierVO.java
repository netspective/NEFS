package auto.dal.db.vo.impl;


public class SupplierVO
implements auto.dal.db.vo.Supplier
{
    
    public java.lang.String getAddr1()
    {
        return addr1;
    }
    
    public java.lang.String getAddr2()
    {
        return addr2;
    }
    
    public java.lang.String getCity()
    {
        return city;
    }
    
    public java.lang.String getCountry()
    {
        return country;
    }
    
    public java.lang.String getName()
    {
        return name;
    }
    
    public java.lang.String getPhone()
    {
        return phone;
    }
    
    public java.lang.String getState()
    {
        return state;
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
    
    public void setAddr1(java.lang.String addr1)
    {
        this.addr1 = addr1;
    }
    
    public void setAddr2(java.lang.String addr2)
    {
        this.addr2 = addr2;
    }
    
    public void setCity(java.lang.String city)
    {
        this.city = city;
    }
    
    public void setCountry(java.lang.String country)
    {
        this.country = country;
    }
    
    public void setName(java.lang.String name)
    {
        this.name = name;
    }
    
    public void setPhone(java.lang.String phone)
    {
        this.phone = phone;
    }
    
    public void setState(java.lang.String state)
    {
        this.state = state;
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
    private java.lang.String addr1;
    private java.lang.String addr2;
    private java.lang.String city;
    private java.lang.String country;
    private java.lang.String name;
    private java.lang.String phone;
    private java.lang.String state;
    private java.lang.String status;
    private java.lang.Long supplierId;
}