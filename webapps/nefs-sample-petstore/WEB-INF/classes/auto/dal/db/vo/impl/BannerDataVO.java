package auto.dal.db.vo.impl;


public class BannerDataVO
implements auto.dal.db.vo.BannerData
{
    
    public java.lang.String getBannerDataId()
    {
        return bannerDataId;
    }
    
    public java.lang.String getBannerName()
    {
        return bannerName;
    }
    
    public void setBannerDataId(java.lang.String bannerDataId)
    {
        this.bannerDataId = bannerDataId;
    }
    
    public void setBannerName(java.lang.String bannerName)
    {
        this.bannerName = bannerName;
    }
    private java.lang.String bannerDataId;
    private java.lang.String bannerName;
}