package auto.dal.db.vo.impl;


public class AssetLoanVO
implements auto.dal.db.vo.AssetLoan
{
    
    public java.lang.Long getAssetId()
    {
        return assetId;
    }
    
    public long getAssetIdLong()
    {
        return getAssetIdLong(-1);
    }
    
    public long getAssetIdLong(long defaultValue)
    {
        return assetId != null ? assetId.longValue() : defaultValue;
    }
    
    public java.lang.Long getAssetLoanId()
    {
        return assetLoanId;
    }
    
    public long getAssetLoanIdLong()
    {
        return getAssetLoanIdLong(-1);
    }
    
    public long getAssetLoanIdLong(long defaultValue)
    {
        return assetLoanId != null ? assetLoanId.longValue() : defaultValue;
    }
    
    public java.lang.Long getBorrowerId()
    {
        return borrowerId;
    }
    
    public long getBorrowerIdLong()
    {
        return getBorrowerIdLong(-1);
    }
    
    public long getBorrowerIdLong(long defaultValue)
    {
        return borrowerId != null ? borrowerId.longValue() : defaultValue;
    }
    
    public java.util.Date getLoanBeginDate()
    {
        return loanBeginDate;
    }
    
    public java.util.Date getLoanEndDate()
    {
        return loanEndDate;
    }
    
    public java.lang.Integer getLoanType()
    {
        return loanType;
    }
    
    public int getLoanTypeInt()
    {
        return getLoanTypeInt(-1);
    }
    
    public int getLoanTypeInt(int defaultValue)
    {
        return loanType != null ? loanType.intValue() : defaultValue;
    }
    
    public java.lang.Boolean getReturned()
    {
        return returned;
    }
    
    public void setAssetId(java.lang.Long assetId)
    {
        this.assetId = assetId;
    }
    
    public void setAssetIdLong(long assetId)
    {
        this.assetId = new java.lang.Long(assetId);
    }
    
    public void setAssetLoanId(java.lang.Long assetLoanId)
    {
        this.assetLoanId = assetLoanId;
    }
    
    public void setAssetLoanIdLong(long assetLoanId)
    {
        this.assetLoanId = new java.lang.Long(assetLoanId);
    }
    
    public void setBorrowerId(java.lang.Long borrowerId)
    {
        this.borrowerId = borrowerId;
    }
    
    public void setBorrowerIdLong(long borrowerId)
    {
        this.borrowerId = new java.lang.Long(borrowerId);
    }
    
    public void setLoanBeginDate(java.util.Date loanBeginDate)
    {
        this.loanBeginDate = loanBeginDate;
    }
    
    public void setLoanEndDate(java.util.Date loanEndDate)
    {
        this.loanEndDate = loanEndDate;
    }
    
    public void setLoanType(java.lang.Integer loanType)
    {
        this.loanType = loanType;
    }
    
    public void setLoanTypeInt(int loanType)
    {
        this.loanType = new java.lang.Integer(loanType);
    }
    
    public void setReturned(java.lang.Boolean returned)
    {
        this.returned = returned;
    }
    private java.lang.Long assetId;
    private java.lang.Long assetLoanId;
    private java.lang.Long borrowerId;
    private java.util.Date loanBeginDate;
    private java.util.Date loanEndDate;
    private java.lang.Integer loanType;
    private java.lang.Boolean returned;
}