package auto.dal.db.vo.impl;


public class BorrowerVO
implements auto.dal.db.vo.Borrower
{
    
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
    
    public java.lang.String getFirstName()
    {
        return firstName;
    }
    
    public java.lang.String getLastName()
    {
        return lastName;
    }
    
    public void setBorrowerId(java.lang.Long borrowerId)
    {
        this.borrowerId = borrowerId;
    }
    
    public void setBorrowerIdLong(long borrowerId)
    {
        this.borrowerId = new java.lang.Long(borrowerId);
    }
    
    public void setFirstName(java.lang.String firstName)
    {
        this.firstName = firstName;
    }
    
    public void setLastName(java.lang.String lastName)
    {
        this.lastName = lastName;
    }
    private java.lang.Long borrowerId;
    private java.lang.String firstName;
    private java.lang.String lastName;
}