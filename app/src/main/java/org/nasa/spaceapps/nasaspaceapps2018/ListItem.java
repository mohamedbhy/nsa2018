package org.nasa.spaceapps.nasaspaceapps2018;

import java.util.Date;

public class ListItem {
    private String missionName,launchSite,rocketName;
    private int id;
    private Date launchDate;
    private boolean saved;

    public ListItem(int id, String missionName, String launchSite, String rocketName, Date launchDate, boolean saved) {
        this.missionName = missionName;
        this.launchSite = launchSite;
        this.rocketName = rocketName;
        this.id = id;
        this.launchDate = launchDate;
        this.saved = saved;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getLaunchSite() {
        return launchSite;
    }

    public String getRocketName() {
        return rocketName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
