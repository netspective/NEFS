package auto.dal.db.vo.impl;


public class RespondentVO
implements auto.dal.db.vo.Respondent
{
    
    public java.lang.Integer getBusinessUnit()
    {
        return businessUnit;
    }
    
    public int getBusinessUnitInt()
    {
        return getBusinessUnitInt(-1);
    }
    
    public int getBusinessUnitInt(int defaultValue)
    {
        return businessUnit != null ? businessUnit.intValue() : defaultValue;
    }
    
    public java.lang.Integer getDivision()
    {
        return division;
    }
    
    public int getDivisionInt()
    {
        return getDivisionInt(-1);
    }
    
    public int getDivisionInt(int defaultValue)
    {
        return division != null ? division.intValue() : defaultValue;
    }
    
    public java.lang.Integer getFunction()
    {
        return function;
    }
    
    public int getFunctionInt()
    {
        return getFunctionInt(-1);
    }
    
    public int getFunctionInt(int defaultValue)
    {
        return function != null ? function.intValue() : defaultValue;
    }
    
    public java.lang.Integer getLocation()
    {
        return location;
    }
    
    public int getLocationInt()
    {
        return getLocationInt(-1);
    }
    
    public int getLocationInt(int defaultValue)
    {
        return location != null ? location.intValue() : defaultValue;
    }
    
    public java.lang.Integer getLocked()
    {
        return locked;
    }
    
    public int getLockedInt()
    {
        return getLockedInt(-1);
    }
    
    public int getLockedInt(int defaultValue)
    {
        return locked != null ? locked.intValue() : defaultValue;
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
    
    public java.lang.Integer getTitle()
    {
        return title;
    }
    
    public int getTitleInt()
    {
        return getTitleInt(-1);
    }
    
    public int getTitleInt(int defaultValue)
    {
        return title != null ? title.intValue() : defaultValue;
    }
    
    public java.lang.Integer getYrsCurrentPos()
    {
        return yrsCurrentPos;
    }
    
    public int getYrsCurrentPosInt()
    {
        return getYrsCurrentPosInt(-1);
    }
    
    public int getYrsCurrentPosInt(int defaultValue)
    {
        return yrsCurrentPos != null ? yrsCurrentPos.intValue() : defaultValue;
    }
    
    public java.lang.Integer getYrsFirm()
    {
        return yrsFirm;
    }
    
    public int getYrsFirmInt()
    {
        return getYrsFirmInt(-1);
    }
    
    public int getYrsFirmInt(int defaultValue)
    {
        return yrsFirm != null ? yrsFirm.intValue() : defaultValue;
    }
    
    public void setBusinessUnit(java.lang.Integer businessUnit)
    {
        this.businessUnit = businessUnit;
    }
    
    public void setBusinessUnitInt(int businessUnit)
    {
        this.businessUnit = new java.lang.Integer(businessUnit);
    }
    
    public void setDivision(java.lang.Integer division)
    {
        this.division = division;
    }
    
    public void setDivisionInt(int division)
    {
        this.division = new java.lang.Integer(division);
    }
    
    public void setFunction(java.lang.Integer function)
    {
        this.function = function;
    }
    
    public void setFunctionInt(int function)
    {
        this.function = new java.lang.Integer(function);
    }
    
    public void setLocation(java.lang.Integer location)
    {
        this.location = location;
    }
    
    public void setLocationInt(int location)
    {
        this.location = new java.lang.Integer(location);
    }
    
    public void setLocked(java.lang.Integer locked)
    {
        this.locked = locked;
    }
    
    public void setLockedInt(int locked)
    {
        this.locked = new java.lang.Integer(locked);
    }
    
    public void setPin(java.lang.Integer pin)
    {
        this.pin = pin;
    }
    
    public void setPinInt(int pin)
    {
        this.pin = new java.lang.Integer(pin);
    }
    
    public void setTitle(java.lang.Integer title)
    {
        this.title = title;
    }
    
    public void setTitleInt(int title)
    {
        this.title = new java.lang.Integer(title);
    }
    
    public void setYrsCurrentPos(java.lang.Integer yrsCurrentPos)
    {
        this.yrsCurrentPos = yrsCurrentPos;
    }
    
    public void setYrsCurrentPosInt(int yrsCurrentPos)
    {
        this.yrsCurrentPos = new java.lang.Integer(yrsCurrentPos);
    }
    
    public void setYrsFirm(java.lang.Integer yrsFirm)
    {
        this.yrsFirm = yrsFirm;
    }
    
    public void setYrsFirmInt(int yrsFirm)
    {
        this.yrsFirm = new java.lang.Integer(yrsFirm);
    }
    private java.lang.Integer businessUnit;
    private java.lang.Integer division;
    private java.lang.Integer function;
    private java.lang.Integer location;
    private java.lang.Integer locked;
    private java.lang.Integer pin;
    private java.lang.Integer title;
    private java.lang.Integer yrsCurrentPos;
    private java.lang.Integer yrsFirm;
}