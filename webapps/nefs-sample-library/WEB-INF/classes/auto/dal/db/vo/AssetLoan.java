package auto.dal.db.vo;


public interface AssetLoan
{
    
    public java.lang.Long getAssetId();
    
    public long getAssetIdLong();
    
    public long getAssetIdLong(long defaultValue);
    
    public java.lang.Long getAssetLoanId();
    
    public long getAssetLoanIdLong();
    
    public long getAssetLoanIdLong(long defaultValue);
    
    public java.lang.Long getBorrowerId();
    
    public long getBorrowerIdLong();
    
    public long getBorrowerIdLong(long defaultValue);
    
    public java.util.Date getLoanBeginDate();
    
    public java.util.Date getLoanEndDate();
    
    public java.lang.Integer getLoanType();
    
    public int getLoanTypeInt();
    
    public int getLoanTypeInt(int defaultValue);
    
    public java.lang.Boolean getReturned();
    
    public void setAssetId(java.lang.Long assetId);
    
    public void setAssetIdLong(long assetId);
    
    public void setAssetLoanId(java.lang.Long assetLoanId);
    
    public void setAssetLoanIdLong(long assetLoanId);
    
    public void setBorrowerId(java.lang.Long borrowerId);
    
    public void setBorrowerIdLong(long borrowerId);
    
    public void setLoanBeginDate(java.util.Date loanBeginDate);
    
    public void setLoanEndDate(java.util.Date loanEndDate);
    
    public void setLoanType(java.lang.Integer loanType);
    
    public void setLoanTypeInt(int loanType);
    
    public void setReturned(java.lang.Boolean returned);
}