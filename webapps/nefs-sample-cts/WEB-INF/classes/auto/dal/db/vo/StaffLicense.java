package auto.dal.db.vo;


public interface StaffLicense
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.util.Date getExpirationDate();
    
    public java.lang.String getLicenseNum();
    
    public java.lang.String getLicenseState();
    
    public java.lang.Integer getLicenseStateId();
    
    public java.lang.String getLicenseType();
    
    public java.lang.Integer getLicenseTypeId();
    
    public java.lang.Long getOrgId();
    
    public java.lang.Long getPersonId();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.Integer getSpecialityTypeId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setExpirationDate(java.util.Date expirationDate);
    
    public void setLicenseNum(java.lang.String licenseNum);
    
    public void setLicenseState(java.lang.String licenseState);
    
    public void setLicenseStateId(java.lang.Integer licenseStateId);
    
    public void setLicenseType(java.lang.String licenseType);
    
    public void setLicenseTypeId(java.lang.Integer licenseTypeId);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setPersonId(java.lang.Long personId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSpecialityTypeId(java.lang.Integer specialityTypeId);
    
    public void setSystemId(java.lang.String systemId);
}