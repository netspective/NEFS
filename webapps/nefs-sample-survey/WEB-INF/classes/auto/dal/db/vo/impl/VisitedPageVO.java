package auto.dal.db.vo.impl;


public class VisitedPageVO
implements auto.dal.db.vo.VisitedPage
{
    
    public java.lang.String getPageId()
    {
        return pageId;
    }
    
    public java.lang.Integer getPin()
    {
        return pin;
    }
    
    public int getPinInt()
    {
        return getPinInt(-1);
    }
    
    public int getPinInt(int defaultValue)
    {
        return pin != null ? pin.intValue() : defaultValue;
    }
    
    public java.lang.Long getSystemId()
    {
        return systemId;
    }
    
    public long getSystemIdLong()
    {
        return getSystemIdLong(-1);
    }
    
    public long getSystemIdLong(long defaultValue)
    {
        return systemId != null ? systemId.longValue() : defaultValue;
    }
    
    public java.lang.Integer getVisitCount()
    {
        return visitCount;
    }
    
    public int getVisitCountInt()
    {
        return getVisitCountInt(-1);
    }
    
    public int getVisitCountInt(int defaultValue)
    {
        return visitCount != null ? visitCount.intValue() : defaultValue;
    }
    
    public void setPageId(java.lang.String pageId)
    {
        this.pageId = pageId;
    }
    
    public void setPin(java.lang.Integer pin)
    {
        this.pin = pin;
    }
    
    public void setPinInt(int pin)
    {
        this.pin = new java.lang.Integer(pin);
    }
    
    public void setSystemId(java.lang.Long systemId)
    {
        this.systemId = systemId;
    }
    
    public void setSystemIdLong(long systemId)
    {
        this.systemId = new java.lang.Long(systemId);
    }
    
    public void setVisitCount(java.lang.Integer visitCount)
    {
        this.visitCount = visitCount;
    }
    
    public void setVisitCountInt(int visitCount)
    {
        this.visitCount = new java.lang.Integer(visitCount);
    }
    private java.lang.String pageId;
    private java.lang.Integer pin;
    private java.lang.Long systemId;
    private java.lang.Integer visitCount;
}