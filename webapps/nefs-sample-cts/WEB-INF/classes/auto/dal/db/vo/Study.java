package auto.dal.db.vo;


public interface Study
{
    
    public java.util.Date getActualEndDate();
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.util.Date getIrbApprovalDate();
    
    public java.util.Date getIrbExpirationDate();
    
    public java.lang.String getIrbName();
    
    public java.lang.String getIrbNumber();
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.util.Date getStartDate();
    
    public java.lang.String getStudyCode();
    
    public java.lang.String getStudyDescr();
    
    public java.lang.Long getStudyId();
    
    public long getStudyIdLong();
    
    public long getStudyIdLong(long defaultValue);
    
    public java.lang.String getStudyName();
    
    public java.lang.Integer getStudyStage();
    
    public int getStudyStageInt();
    
    public int getStudyStageInt(int defaultValue);
    
    public java.lang.Integer getStudyStatus();
    
    public int getStudyStatusInt();
    
    public int getStudyStatusInt(int defaultValue);
    
    public java.util.Date getTargetEndDate();
    
    public void setActualEndDate(java.util.Date actualEndDate);
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setIrbApprovalDate(java.util.Date irbApprovalDate);
    
    public void setIrbExpirationDate(java.util.Date irbExpirationDate);
    
    public void setIrbName(java.lang.String irbName);
    
    public void setIrbNumber(java.lang.String irbNumber);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setStartDate(java.util.Date startDate);
    
    public void setStudyCode(java.lang.String studyCode);
    
    public void setStudyDescr(java.lang.String studyDescr);
    
    public void setStudyId(java.lang.Long studyId);
    
    public void setStudyIdLong(long studyId);
    
    public void setStudyName(java.lang.String studyName);
    
    public void setStudyStage(java.lang.Integer studyStage);
    
    public void setStudyStageInt(int studyStage);
    
    public void setStudyStatus(java.lang.Integer studyStatus);
    
    public void setStudyStatusInt(int studyStatus);
    
    public void setTargetEndDate(java.util.Date targetEndDate);
}