package auto.dal.db.vo;


public interface PersonContact
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.String getMethodName();
    
    public java.lang.Integer getMethodType();
    
    public java.lang.String getMethodValue();
    
    public java.lang.Long getParentId();
    
    public java.lang.Integer getPhoneAc();
    
    public java.lang.String getPhoneCc();
    
    public java.lang.Integer getPhonePrefix();
    
    public java.lang.Integer getPhoneSuffix();
    
    public java.lang.Integer getRecStatId();
    
    public java.lang.String getSystemId();
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setMethodName(java.lang.String methodName);
    
    public void setMethodType(java.lang.Integer methodType);
    
    public void setMethodValue(java.lang.String methodValue);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setPhoneAc(java.lang.Integer phoneAc);
    
    public void setPhoneCc(java.lang.String phoneCc);
    
    public void setPhonePrefix(java.lang.Integer phonePrefix);
    
    public void setPhoneSuffix(java.lang.Integer phoneSuffix);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setSystemId(java.lang.String systemId);
}