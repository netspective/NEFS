package auto.dal.db.vo;


public interface RiskResponse
{
    
    public java.lang.Integer getFirmEff();
    
    public java.lang.Integer getFirmSig();
    
    public java.lang.Integer getIbuEff();
    
    public java.lang.Integer getIbuSig();
    
    public java.lang.Integer getLbgEff();
    
    public java.lang.Integer getLbgSig();
    
    public java.lang.Integer getPin();
    
    public java.lang.String getRisk();
    
    public java.lang.String getRiskGroup();
    
    public java.lang.String getRiskId();
    
    public java.lang.Long getSystemId();
    
    public void setFirmEff(java.lang.Integer firmEff);
    
    public void setFirmSig(java.lang.Integer firmSig);
    
    public void setIbuEff(java.lang.Integer ibuEff);
    
    public void setIbuSig(java.lang.Integer ibuSig);
    
    public void setLbgEff(java.lang.Integer lbgEff);
    
    public void setLbgSig(java.lang.Integer lbgSig);
    
    public void setPin(java.lang.Integer pin);
    
    public void setRisk(java.lang.String risk);
    
    public void setRiskGroup(java.lang.String riskGroup);
    
    public void setRiskId(java.lang.String riskId);
    
    public void setSystemId(java.lang.Long systemId);
}