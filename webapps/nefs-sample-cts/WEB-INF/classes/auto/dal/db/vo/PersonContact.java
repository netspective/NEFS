package auto.dal.db.vo;


public interface PersonContact
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getMethodName();
    
    public java.lang.Integer getMethodType();
    
    public int getMethodTypeInt();
    
    public int getMethodTypeInt(int defaultValue);
    
    public java.lang.String getMethodValue();
    
    public java.lang.Long getParentId();
    
    public long getParentIdLong();
    
    public long getParentIdLong(long defaultValue);
    
    public java.lang.Integer getPhoneAc();
    
    public int getPhoneAcInt();
    
    public int getPhoneAcInt(int defaultValue);
    
    public java.lang.String getPhoneCc();
    
    public java.lang.Integer getPhonePrefix();
    
    public int getPhonePrefixInt();
    
    public int getPhonePrefixInt(int defaultValue);
    
    public java.lang.Integer getPhoneSuffix();
    
    public int getPhoneSuffixInt();
    
    public int getPhoneSuffixInt(int defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setMethodName(java.lang.String methodName);
    
    public void setMethodType(java.lang.Integer methodType);
    
    public void setMethodTypeInt(int methodType);
    
    public void setMethodValue(java.lang.String methodValue);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setParentIdLong(long parentId);
    
    public void setPhoneAc(java.lang.Integer phoneAc);
    
    public void setPhoneAcInt(int phoneAc);
    
    public void setPhoneCc(java.lang.String phoneCc);
    
    public void setPhonePrefix(java.lang.Integer phonePrefix);
    
    public void setPhonePrefixInt(int phonePrefix);
    
    public void setPhoneSuffix(java.lang.Integer phoneSuffix);
    
    public void setPhoneSuffixInt(int phoneSuffix);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setSystemId(java.lang.String systemId);
}