package com.pig.original.Addfriend;

import java.io.Serializable;

public class Friend implements Serializable {

    private int id;
    private String name;
    private String logintime;
    private String lastloginRegion;
    private double latitude;
    private double longitude;

    public Friend(int id, String name, String logintime, String lastloginRegion, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.logintime = logintime;
        this.lastloginRegion = lastloginRegion;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Friend) obj).id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String phoneNo) {
        this.logintime = logintime;
    }

    public String getLastloginRegion() {
        return lastloginRegion;
    }

    public void setLastloginRegion(String address) {
        this.lastloginRegion = lastloginRegion;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
