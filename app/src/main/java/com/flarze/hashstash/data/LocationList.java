package com.flarze.hashstash.data;

public class LocationList {

    private int locationImage;
    private String locatonName;

    public LocationList(int locationImage, String locatonName) {
        this.locationImage = locationImage;
        this.locatonName = locatonName;
    }

    public int getLocationImage() {
        return locationImage;
    }

    public String getLocatonName() {
        return locatonName;
    }
}
