package auto.dal.db.vo;


public interface StaffLicense
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.util.Date getExpirationDate();
    
    public java.lang.String getLicenseNum();
    
    public java.lang.String getLicenseState();
    
    public java.lang.Integer getLicenseStateId();
    
    public int getLicenseStateIdInt();
    
    public int getLicenseStateIdInt(int defaultValue);
    
    public java.lang.String getLicenseType();
    
    public java.lang.Integer getLicenseTypeId();
    
    public int getLicenseTypeIdInt();
    
    public int getLicenseTypeIdInt(int defaultValue);
    
    public java.lang.Long getOrgId();
    
    public long getOrgIdLong();
    
    public long getOrgIdLong(long defaultValue);
    
    public java.lang.Long getPersonId();
    
    public long getPersonIdLong();
    
    public long getPersonIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.Integer getSpecialityTypeId();
    
    public int getSpecialityTypeIdInt();
    
    public int getSpecialityTypeIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setExpirationDate(java.util.Date expirationDate);
    
    public void setLicenseNum(java.lang.String licenseNum);
    
    public void setLicenseState(java.lang.String licenseState);
    
    public void setLicenseStateId(java.lang.Integer licenseStateId);
    
    public void setLicenseStateIdInt(int licenseStateId);
    
    public void setLicenseType(java.lang.String licenseType);
    
    public void setLicenseTypeId(java.lang.Integer licenseTypeId);
    
    public void setLicenseTypeIdInt(int licenseTypeId);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setOrgIdLong(long orgId);
    
    public void setPersonId(java.lang.Long personId);
    
    public void setPersonIdLong(long personId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSpecialityTypeId(java.lang.Integer specialityTypeId);
    
    public void setSpecialityTypeIdInt(int specialityTypeId);
    
    public void setSystemId(java.lang.String systemId);
}