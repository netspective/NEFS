package auto.dal.db.vo;


public interface PersonSession
{
    
    public java.util.Date getFirstAccess();
    
    public java.util.Date getLastAccess();
    
    public java.lang.Long getOrgId();
    
    public java.lang.Long getPersonId();
    
    public java.lang.String getRemoteAddr();
    
    public java.lang.String getRemoteHost();
    
    public java.lang.String getSessionId();
    
    public void setFirstAccess(java.util.Date firstAccess);
    
    public void setLastAccess(java.util.Date lastAccess);
    
    public void setOrgId(java.lang.Long orgId);
    
    public void setPersonId(java.lang.Long personId);
    
    public void setRemoteAddr(java.lang.String remoteAddr);
    
    public void setRemoteHost(java.lang.String remoteHost);
    
    public void setSessionId(java.lang.String sessionId);
}