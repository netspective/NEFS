package auto.dal.db.vo.impl;


public class PersonSessionVO
implements auto.dal.db.vo.PersonSession
{
    
    public java.util.Date getFirstAccess()
    {
        return firstAccess;
    }
    
    public java.util.Date getLastAccess()
    {
        return lastAccess;
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
    
    public java.lang.String getRemoteAddr()
    {
        return remoteAddr;
    }
    
    public java.lang.String getRemoteHost()
    {
        return remoteHost;
    }
    
    public java.lang.String getSessionId()
    {
        return sessionId;
    }
    
    public void setFirstAccess(java.util.Date firstAccess)
    {
        this.firstAccess = firstAccess;
    }
    
    public void setLastAccess(java.util.Date lastAccess)
    {
        this.lastAccess = lastAccess;
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
    
    public void setRemoteAddr(java.lang.String remoteAddr)
    {
        this.remoteAddr = remoteAddr;
    }
    
    public void setRemoteHost(java.lang.String remoteHost)
    {
        this.remoteHost = remoteHost;
    }
    
    public void setSessionId(java.lang.String sessionId)
    {
        this.sessionId = sessionId;
    }
    private java.util.Date firstAccess;
    private java.util.Date lastAccess;
    private java.lang.Long orgId;
    private java.lang.Long personId;
    private java.lang.String remoteAddr;
    private java.lang.String remoteHost;
    private java.lang.String sessionId;
}