package auto.dal.db.vo.impl;


public class StaffLicenseVO
implements auto.dal.db.vo.StaffLicense
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.util.Date getExpirationDate()
    {
        return expirationDate;
    }
    
    public java.lang.String getLicenseNum()
    {
        return licenseNum;
    }
    
    public java.lang.String getLicenseState()
    {
        return licenseState;
    }
    
    public java.lang.Integer getLicenseStateId()
    {
        return licenseStateId;
    }
    
    public int getLicenseStateIdInt()
    {
        return getLicenseStateIdInt(-1);
    }
    
    public int getLicenseStateIdInt(int defaultValue)
    {
        return licenseStateId != null ? licenseStateId.intValue() : defaultValue;
    }
    
    public java.lang.String getLicenseType()
    {
        return licenseType;
    }
    
    public java.lang.Integer getLicenseTypeId()
    {
        return licenseTypeId;
    }
    
    public int getLicenseTypeIdInt()
    {
        return getLicenseTypeIdInt(-1);
    }
    
    public int getLicenseTypeIdInt(int defaultValue)
    {
        return licenseTypeId != null ? licenseTypeId.intValue() : defaultValue;
    }
    
    public java.lang.Long getOrgId()
    {
        return orgId;
    }
    
    public long getOrgIdLong()
    {
        return getOrgIdLong(-1);
    }
    
    public long getOrgIdLong(long defaultValue)
    {
        return orgId != null ? orgId.longValue() : defaultValue;
    }
    
    public java.lang.Long getPersonId()
    {
        return personId;
    }
    
    public long getPersonIdLong()
    {
        return getPersonIdLong(-1);
    }
    
    public long getPersonIdLong(long defaultValue)
    {
        return personId != null ? personId.longValue() : defaultValue;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public int getRecStatIdInt()
    {
        return getRecStatIdInt(-1);
    }
    
    public int getRecStatIdInt(int defaultValue)
    {
        return recStatId != null ? recStatId.intValue() : defaultValue;
    }
    
    public java.lang.Integer getSpecialityTypeId()
    {
        return specialityTypeId;
    }
    
    public int getSpecialityTypeIdInt()
    {
        return getSpecialityTypeIdInt(-1);
    }
    
    public int getSpecialityTypeIdInt(int defaultValue)
    {
        return specialityTypeId != null ? specialityTypeId.intValue() : defaultValue;
    }
    
    public java.lang.String getSystemId()
    {
        return systemId;
    }
    
    public void setCrSessId(java.lang.String crSessId)
    {
        this.crSessId = crSessId;
    }
    
    public void setCrStamp(java.util.Date crStamp)
    {
        this.crStamp = crStamp;
    }
    
    public void setExpirationDate(java.util.Date expirationDate)
    {
        this.expirationDate = expirationDate;
    }
    
    public void setLicenseNum(java.lang.String licenseNum)
    {
        this.licenseNum = licenseNum;
    }
    
    public void setLicenseState(java.lang.String licenseState)
    {
        this.licenseState = licenseState;
    }
    
    public void setLicenseStateId(java.lang.Integer licenseStateId)
    {
        this.licenseStateId = licenseStateId;
    }
    
    public void setLicenseStateIdInt(int licenseStateId)
    {
        this.licenseStateId = new java.lang.Integer(licenseStateId);
    }
    
    public void setLicenseType(java.lang.String licenseType)
    {
        this.licenseType = licenseType;
    }
    
    public void setLicenseTypeId(java.lang.Integer licenseTypeId)
    {
        this.licenseTypeId = licenseTypeId;
    }
    
    public void setLicenseTypeIdInt(int licenseTypeId)
    {
        this.licenseTypeId = new java.lang.Integer(licenseTypeId);
    }
    
    public void setOrgId(java.lang.Long orgId)
    {
        this.orgId = orgId;
    }
    
    public void setOrgIdLong(long orgId)
    {
        this.orgId = new java.lang.Long(orgId);
    }
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setPersonIdLong(long personId)
    {
        this.personId = new java.lang.Long(personId);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setSpecialityTypeId(java.lang.Integer specialityTypeId)
    {
        this.specialityTypeId = specialityTypeId;
    }
    
    public void setSpecialityTypeIdInt(int specialityTypeId)
    {
        this.specialityTypeId = new java.lang.Integer(specialityTypeId);
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.util.Date expirationDate;
    private java.lang.String licenseNum;
    private java.lang.String licenseState;
    private java.lang.Integer licenseStateId;
    private java.lang.String licenseType;
    private java.lang.Integer licenseTypeId;
    private java.lang.Long orgId;
    private java.lang.Long personId;
    private java.lang.Integer recStatId;
    private java.lang.Integer specialityTypeId;
    private java.lang.String systemId;
}