package auto.dal.db.vo;


public interface VisitedPage
{
    
    public java.lang.String getPageId();
    
    public java.lang.Integer getPin();
    
    public int getPinInt();
    
    public int getPinInt(int defaultValue);
    
    public java.lang.Long getSystemId();
    
    public long getSystemIdLong();
    
    public long getSystemIdLong(long defaultValue);
    
    public java.lang.Integer getVisitCount();
    
    public int getVisitCountInt();
    
    public int getVisitCountInt(int defaultValue);
    
    public void setPageId(java.lang.String pageId);
    
    public void setPin(java.lang.Integer pin);
    
    public void setPinInt(int pin);
    
    public void setSystemId(java.lang.Long systemId);
    
    public void setSystemIdLong(long systemId);
    
    public void setVisitCount(java.lang.Integer visitCount);
    
    public void setVisitCountInt(int visitCount);
}