package auto.dal.db.vo;


public interface Supplier
{
    
    public java.lang.String getAddr1();
    
    public java.lang.String getAddr2();
    
    public java.lang.String getCity();
    
    public java.lang.String getCountry();
    
    public java.lang.String getName();
    
    public java.lang.String getPhone();
    
    public java.lang.String getState();
    
    public java.lang.String getStatus();
    
    public java.lang.Long getSupplierId();
    
    public long getSupplierIdLong();
    
    public long getSupplierIdLong(long defaultValue);
    
    public void setAddr1(java.lang.String addr1);
    
    public void setAddr2(java.lang.String addr2);
    
    public void setCity(java.lang.String city);
    
    public void setCountry(java.lang.String country);
    
    public void setName(java.lang.String name);
    
    public void setPhone(java.lang.String phone);
    
    public void setState(java.lang.String state);
    
    public void setStatus(java.lang.String status);
    
    public void setSupplierId(java.lang.Long supplierId);
    
    public void setSupplierIdLong(long supplierId);
}