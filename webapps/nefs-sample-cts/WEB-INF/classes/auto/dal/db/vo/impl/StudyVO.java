package auto.dal.db.vo.impl;


public class StudyVO
implements auto.dal.db.vo.Study
{
    
    public java.util.Date getActualEndDate()
    {
        return actualEndDate;
    }
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.util.Date getIrbApprovalDate()
    {
        return irbApprovalDate;
    }
    
    public java.util.Date getIrbExpirationDate()
    {
        return irbExpirationDate;
    }
    
    public java.lang.String getIrbName()
    {
        return irbName;
    }
    
    public java.lang.String getIrbNumber()
    {
        return irbNumber;
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
    
    public java.util.Date getStartDate()
    {
        return startDate;
    }
    
    public java.lang.String getStudyCode()
    {
        return studyCode;
    }
    
    public java.lang.String getStudyDescr()
    {
        return studyDescr;
    }
    
    public java.lang.Long getStudyId()
    {
        return studyId;
    }
    
    public long getStudyIdLong()
    {
        return getStudyIdLong(-1);
    }
    
    public long getStudyIdLong(long defaultValue)
    {
        return studyId != null ? studyId.longValue() : defaultValue;
    }
    
    public java.lang.String getStudyName()
    {
        return studyName;
    }
    
    public java.lang.Integer getStudyStage()
    {
        return studyStage;
    }
    
    public int getStudyStageInt()
    {
        return getStudyStageInt(-1);
    }
    
    public int getStudyStageInt(int defaultValue)
    {
        return studyStage != null ? studyStage.intValue() : defaultValue;
    }
    
    public java.lang.Integer getStudyStatus()
    {
        return studyStatus;
    }
    
    public int getStudyStatusInt()
    {
        return getStudyStatusInt(-1);
    }
    
    public int getStudyStatusInt(int defaultValue)
    {
        return studyStatus != null ? studyStatus.intValue() : defaultValue;
    }
    
    public java.util.Date getTargetEndDate()
    {
        return targetEndDate;
    }
    
    public void setActualEndDate(java.util.Date actualEndDate)
    {
        this.actualEndDate = actualEndDate;
    }
    
    public void setCrSessId(java.lang.String crSessId)
    {
        this.crSessId = crSessId;
    }
    
    public void setCrStamp(java.util.Date crStamp)
    {
        this.crStamp = crStamp;
    }
    
    public void setIrbApprovalDate(java.util.Date irbApprovalDate)
    {
        this.irbApprovalDate = irbApprovalDate;
    }
    
    public void setIrbExpirationDate(java.util.Date irbExpirationDate)
    {
        this.irbExpirationDate = irbExpirationDate;
    }
    
    public void setIrbName(java.lang.String irbName)
    {
        this.irbName = irbName;
    }
    
    public void setIrbNumber(java.lang.String irbNumber)
    {
        this.irbNumber = irbNumber;
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setStartDate(java.util.Date startDate)
    {
        this.startDate = startDate;
    }
    
    public void setStudyCode(java.lang.String studyCode)
    {
        this.studyCode = studyCode;
    }
    
    public void setStudyDescr(java.lang.String studyDescr)
    {
        this.studyDescr = studyDescr;
    }
    
    public void setStudyId(java.lang.Long studyId)
    {
        this.studyId = studyId;
    }
    
    public void setStudyIdLong(long studyId)
    {
        this.studyId = new java.lang.Long(studyId);
    }
    
    public void setStudyName(java.lang.String studyName)
    {
        this.studyName = studyName;
    }
    
    public void setStudyStage(java.lang.Integer studyStage)
    {
        this.studyStage = studyStage;
    }
    
    public void setStudyStageInt(int studyStage)
    {
        this.studyStage = new java.lang.Integer(studyStage);
    }
    
    public void setStudyStatus(java.lang.Integer studyStatus)
    {
        this.studyStatus = studyStatus;
    }
    
    public void setStudyStatusInt(int studyStatus)
    {
        this.studyStatus = new java.lang.Integer(studyStatus);
    }
    
    public void setTargetEndDate(java.util.Date targetEndDate)
    {
        this.targetEndDate = targetEndDate;
    }
    private java.util.Date actualEndDate;
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.util.Date irbApprovalDate;
    private java.util.Date irbExpirationDate;
    private java.lang.String irbName;
    private java.lang.String irbNumber;
    private java.lang.Integer recStatId;
    private java.util.Date startDate;
    private java.lang.String studyCode;
    private java.lang.String studyDescr;
    private java.lang.Long studyId;
    private java.lang.String studyName;
    private java.lang.Integer studyStage;
    private java.lang.Integer studyStatus;
    private java.util.Date targetEndDate;
}