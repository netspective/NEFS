package auto.dal.db.vo;


public interface Borrower
{
    
    public java.lang.Long getBorrowerId();
    
    public long getBorrowerIdLong();
    
    public long getBorrowerIdLong(long defaultValue);
    
    public java.lang.String getFirstName();
    
    public java.lang.String getLastName();
    
    public void setBorrowerId(java.lang.Long borrowerId);
    
    public void setBorrowerIdLong(long borrowerId);
    
    public void setFirstName(java.lang.String firstName);
    
    public void setLastName(java.lang.String lastName);
}