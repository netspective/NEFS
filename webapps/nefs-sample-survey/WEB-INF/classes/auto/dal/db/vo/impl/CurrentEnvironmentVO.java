package auto.dal.db.vo.impl;


public class CurrentEnvironmentVO
implements auto.dal.db.vo.CurrentEnvironment
{
    
    public java.lang.String getApproach()
    {
        return approach;
    }
    
    public java.lang.String getApproachExpl()
    {
        return approachExpl;
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
    
    public java.lang.String getRisksDelta()
    {
        return risksDelta;
    }
    
    public java.lang.String getRisksDeltaExpl()
    {
        return risksDeltaExpl;
    }
    
    public java.lang.String getRisksDept()
    {
        return risksDept;
    }
    
    public java.lang.String getRisksDeptExpl()
    {
        return risksDeptExpl;
    }
    
    public java.lang.String getRisksDiv()
    {
        return risksDiv;
    }
    
    public java.lang.String getRisksDivExpl()
    {
        return risksDivExpl;
    }
    
    public java.lang.String getRisksPrj()
    {
        return risksPrj;
    }
    
    public java.lang.String getRisksPrjExpl()
    {
        return risksPrjExpl;
    }
    
    public void setApproach(java.lang.String approach)
    {
        this.approach = approach;
    }
    
    public void setApproachExpl(java.lang.String approachExpl)
    {
        this.approachExpl = approachExpl;
    }
    
    public void setPin(java.lang.Integer pin)
    {
        this.pin = pin;
    }
    
    public void setPinInt(int pin)
    {
        this.pin = new java.lang.Integer(pin);
    }
    
    public void setRisksDelta(java.lang.String risksDelta)
    {
        this.risksDelta = risksDelta;
    }
    
    public void setRisksDeltaExpl(java.lang.String risksDeltaExpl)
    {
        this.risksDeltaExpl = risksDeltaExpl;
    }
    
    public void setRisksDept(java.lang.String risksDept)
    {
        this.risksDept = risksDept;
    }
    
    public void setRisksDeptExpl(java.lang.String risksDeptExpl)
    {
        this.risksDeptExpl = risksDeptExpl;
    }
    
    public void setRisksDiv(java.lang.String risksDiv)
    {
        this.risksDiv = risksDiv;
    }
    
    public void setRisksDivExpl(java.lang.String risksDivExpl)
    {
        this.risksDivExpl = risksDivExpl;
    }
    
    public void setRisksPrj(java.lang.String risksPrj)
    {
        this.risksPrj = risksPrj;
    }
    
    public void setRisksPrjExpl(java.lang.String risksPrjExpl)
    {
        this.risksPrjExpl = risksPrjExpl;
    }
    private java.lang.String approach;
    private java.lang.String approachExpl;
    private java.lang.Integer pin;
    private java.lang.String risksDelta;
    private java.lang.String risksDeltaExpl;
    private java.lang.String risksDept;
    private java.lang.String risksDeptExpl;
    private java.lang.String risksDiv;
    private java.lang.String risksDivExpl;
    private java.lang.String risksPrj;
    private java.lang.String risksPrjExpl;
}