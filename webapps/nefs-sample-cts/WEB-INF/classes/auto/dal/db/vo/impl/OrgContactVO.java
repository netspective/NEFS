package auto.dal.db.vo.impl;


public class OrgContactVO
implements auto.dal.db.vo.OrgContact
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.String getMethodName()
    {
        return methodName;
    }
    
    public java.lang.Integer getMethodType()
    {
        return methodType;
    }
    
    public int getMethodTypeInt()
    {
        return getMethodTypeInt(-1);
    }
    
    public int getMethodTypeInt(int defaultValue)
    {
        return methodType != null ? methodType.intValue() : defaultValue;
    }
    
    public java.lang.String getMethodValue()
    {
        return methodValue;
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
    
    public java.lang.Integer getPhoneAc()
    {
        return phoneAc;
    }
    
    public int getPhoneAcInt()
    {
        return getPhoneAcInt(-1);
    }
    
    public int getPhoneAcInt(int defaultValue)
    {
        return phoneAc != null ? phoneAc.intValue() : defaultValue;
    }
    
    public java.lang.String getPhoneCc()
    {
        return phoneCc;
    }
    
    public java.lang.Integer getPhonePrefix()
    {
        return phonePrefix;
    }
    
    public int getPhonePrefixInt()
    {
        return getPhonePrefixInt(-1);
    }
    
    public int getPhonePrefixInt(int defaultValue)
    {
        return phonePrefix != null ? phonePrefix.intValue() : defaultValue;
    }
    
    public java.lang.Integer getPhoneSuffix()
    {
        return phoneSuffix;
    }
    
    public int getPhoneSuffixInt()
    {
        return getPhoneSuffixInt(-1);
    }
    
    public int getPhoneSuffixInt(int defaultValue)
    {
        return phoneSuffix != null ? phoneSuffix.intValue() : defaultValue;
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
    
    public void setMethodName(java.lang.String methodName)
    {
        this.methodName = methodName;
    }
    
    public void setMethodType(java.lang.Integer methodType)
    {
        this.methodType = methodType;
    }
    
    public void setMethodTypeInt(int methodType)
    {
        this.methodType = new java.lang.Integer(methodType);
    }
    
    public void setMethodValue(java.lang.String methodValue)
    {
        this.methodValue = methodValue;
    }
    
    public void setParentId(java.lang.Long parentId)
    {
        this.parentId = parentId;
    }
    
    public void setParentIdLong(long parentId)
    {
        this.parentId = new java.lang.Long(parentId);
    }
    
    public void setPhoneAc(java.lang.Integer phoneAc)
    {
        this.phoneAc = phoneAc;
    }
    
    public void setPhoneAcInt(int phoneAc)
    {
        this.phoneAc = new java.lang.Integer(phoneAc);
    }
    
    public void setPhoneCc(java.lang.String phoneCc)
    {
        this.phoneCc = phoneCc;
    }
    
    public void setPhonePrefix(java.lang.Integer phonePrefix)
    {
        this.phonePrefix = phonePrefix;
    }
    
    public void setPhonePrefixInt(int phonePrefix)
    {
        this.phonePrefix = new java.lang.Integer(phonePrefix);
    }
    
    public void setPhoneSuffix(java.lang.Integer phoneSuffix)
    {
        this.phoneSuffix = phoneSuffix;
    }
    
    public void setPhoneSuffixInt(int phoneSuffix)
    {
        this.phoneSuffix = new java.lang.Integer(phoneSuffix);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.String methodName;
    private java.lang.Integer methodType;
    private java.lang.String methodValue;
    private java.lang.Long parentId;
    private java.lang.Integer phoneAc;
    private java.lang.String phoneCc;
    private java.lang.Integer phonePrefix;
    private java.lang.Integer phoneSuffix;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}