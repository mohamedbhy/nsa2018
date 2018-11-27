package org.nasa.spaceapps.nasaspaceapps2018;

import java.util.Date;

public class DetailsItem {
    private int id,coresCount,payloadsCount;
    private String missionName,rocketName,coresNames,payloadsIds,details,moreDetails,image,youtubeId,launchSite;
    private Date launchDate,staticFireDate;
    private boolean launchSuccess,saved;

    public DetailsItem(int id, String missionName, String rocketName, String coresNames, String payloadsIds, String details, String moreDetails, String image, String youtubeId, String launchSite, Date launchDate ,Date staticFireDate, boolean launchSuccess, boolean saved) {
        this.id = id;
        this.launchSite = launchSite;
        this.missionName = missionName;
        this.rocketName = rocketName;
        this.coresNames = coresNames;
        this.payloadsIds = payloadsIds;
        this.details = details;
        this.moreDetails = moreDetails;
        this.image = image;
        this.youtubeId = youtubeId;
        this.launchDate = launchDate;
        this.staticFireDate = staticFireDate;
        this.launchSuccess = launchSuccess;
        this.saved = saved;
        this.coresCount = this.coresNames!=null?this.coresNames.split(",").length:0;
        this.payloadsCount=this.payloadsIds.split(",").length;
    }

    public int getId() {
        return id;
    }

    public int getCoresCount() {
        return coresCount;
    }

    public int getPayloadsCount() {
        return payloadsCount;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getRocketName() {
        return rocketName;
    }

    public String getCoresNames() {
        return coresNames;
    }

    public String getPayloadsIds() {
        return payloadsIds;
    }

    public String getLaunchSite() {
        return launchSite;
    }

    public void setLaunchSite(String launchSite) {
        this.launchSite = launchSite;
    }

    public String getDetails() {
        return details;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public String getImage() {
        return image;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public Date getStaticFireDate() {
        return staticFireDate;
    }

    public boolean isLaunchSuccess() {
        return launchSuccess;
    }

    public boolean isSaved() {
        return saved;
    }
}
