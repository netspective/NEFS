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
    
    public java.lang.Long getSystemId()
    {
        return systemId;
    }
    
    public java.lang.Integer getVisitCount()
    {
        return visitCount;
    }
    
    public void setPageId(java.lang.String pageId)
    {
        this.pageId = pageId;
    }
    
    public void setPin(java.lang.Integer pin)
    {
        this.pin = pin;
    }
    
    public void setSystemId(java.lang.Long systemId)
    {
        this.systemId = systemId;
    }
    
    public void setVisitCount(java.lang.Integer visitCount)
    {
        this.visitCount = visitCount;
    }
    private java.lang.String pageId;
    private java.lang.Integer pin;
    private java.lang.Long systemId;
    private java.lang.Integer visitCount;
}