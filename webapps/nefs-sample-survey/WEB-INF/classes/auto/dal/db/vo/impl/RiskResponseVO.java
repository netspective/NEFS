package auto.dal.db.vo.impl;


public class RiskResponseVO
implements auto.dal.db.vo.RiskResponse
{
    
    public java.lang.Integer getFirmEff()
    {
        return firmEff;
    }
    
    public int getFirmEffInt()
    {
        return getFirmEffInt(-1);
    }
    
    public int getFirmEffInt(int defaultValue)
    {
        return firmEff != null ? firmEff.intValue() : defaultValue;
    }
    
    public java.lang.Integer getFirmSig()
    {
        return firmSig;
    }
    
    public int getFirmSigInt()
    {
        return getFirmSigInt(-1);
    }
    
    public int getFirmSigInt(int defaultValue)
    {
        return firmSig != null ? firmSig.intValue() : defaultValue;
    }
    
    public java.lang.Integer getIbuEff()
    {
        return ibuEff;
    }
    
    public int getIbuEffInt()
    {
        return getIbuEffInt(-1);
    }
    
    public int getIbuEffInt(int defaultValue)
    {
        return ibuEff != null ? ibuEff.intValue() : defaultValue;
    }
    
    public java.lang.Integer getIbuSig()
    {
        return ibuSig;
    }
    
    public int getIbuSigInt()
    {
        return getIbuSigInt(-1);
    }
    
    public int getIbuSigInt(int defaultValue)
    {
        return ibuSig != null ? ibuSig.intValue() : defaultValue;
    }
    
    public java.lang.Integer getLbgEff()
    {
        return lbgEff;
    }
    
    public int getLbgEffInt()
    {
        return getLbgEffInt(-1);
    }
    
    public int getLbgEffInt(int defaultValue)
    {
        return lbgEff != null ? lbgEff.intValue() : defaultValue;
    }
    
    public java.lang.Integer getLbgSig()
    {
        return lbgSig;
    }
    
    public int getLbgSigInt()
    {
        return getLbgSigInt(-1);
    }
    
    public int getLbgSigInt(int defaultValue)
    {
        return lbgSig != null ? lbgSig.intValue() : defaultValue;
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
    
    public java.lang.String getRisk()
    {
        return risk;
    }
    
    public java.lang.String getRiskGroup()
    {
        return riskGroup;
    }
    
    public java.lang.String getRiskId()
    {
        return riskId;
    }
    
    public java.lang.Long getSystemId()
    {
        return systemId;
    }
    
    public long getSystemIdLong()
    {
        return getSystemIdLong(-1);
    }
    
    public long getSystemIdLong(long defaultValue)
    {
        return systemId != null ? systemId.longValue() : defaultValue;
    }
    
    public void setFirmEff(java.lang.Integer firmEff)
    {
        this.firmEff = firmEff;
    }
    
    public void setFirmEffInt(int firmEff)
    {
        this.firmEff = new java.lang.Integer(firmEff);
    }
    
    public void setFirmSig(java.lang.Integer firmSig)
    {
        this.firmSig = firmSig;
    }
    
    public void setFirmSigInt(int firmSig)
    {
        this.firmSig = new java.lang.Integer(firmSig);
    }
    
    public void setIbuEff(java.lang.Integer ibuEff)
    {
        this.ibuEff = ibuEff;
    }
    
    public void setIbuEffInt(int ibuEff)
    {
        this.ibuEff = new java.lang.Integer(ibuEff);
    }
    
    public void setIbuSig(java.lang.Integer ibuSig)
    {
        this.ibuSig = ibuSig;
    }
    
    public void setIbuSigInt(int ibuSig)
    {
        this.ibuSig = new java.lang.Integer(ibuSig);
    }
    
    public void setLbgEff(java.lang.Integer lbgEff)
    {
        this.lbgEff = lbgEff;
    }
    
    public void setLbgEffInt(int lbgEff)
    {
        this.lbgEff = new java.lang.Integer(lbgEff);
    }
    
    public void setLbgSig(java.lang.Integer lbgSig)
    {
        this.lbgSig = lbgSig;
    }
    
    public void setLbgSigInt(int lbgSig)
    {
        this.lbgSig = new java.lang.Integer(lbgSig);
    }
    
    public void setPin(java.lang.Integer pin)
    {
        this.pin = pin;
    }
    
    public void setPinInt(int pin)
    {
        this.pin = new java.lang.Integer(pin);
    }
    
    public void setRisk(java.lang.String risk)
    {
        this.risk = risk;
    }
    
    public void setRiskGroup(java.lang.String riskGroup)
    {
        this.riskGroup = riskGroup;
    }
    
    public void setRiskId(java.lang.String riskId)
    {
        this.riskId = riskId;
    }
    
    public void setSystemId(java.lang.Long systemId)
    {
        this.systemId = systemId;
    }
    
    public void setSystemIdLong(long systemId)
    {
        this.systemId = new java.lang.Long(systemId);
    }
    private java.lang.Integer firmEff;
    private java.lang.Integer firmSig;
    private java.lang.Integer ibuEff;
    private java.lang.Integer ibuSig;
    private java.lang.Integer lbgEff;
    private java.lang.Integer lbgSig;
    private java.lang.Integer pin;
    private java.lang.String risk;
    private java.lang.String riskGroup;
    private java.lang.String riskId;
    private java.lang.Long systemId;
}