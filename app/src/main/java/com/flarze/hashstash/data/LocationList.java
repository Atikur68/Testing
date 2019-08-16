package com.flarze.hashstash.data;

public class LocationList {

    private int locationImage;
    private String locatonName;
    private String locatonLat;
    private String locatonLon;
    private String locationId;

    public LocationList(int locationImage, String locatonName,String locatonLat, String locatonLon,String locationId) {
        this.locationImage = locationImage;
        this.locatonName = locatonName;
        this.locatonLat=locatonLat;
        this.locatonLon=locatonLon;
        this.locationId=locationId;
    }

    public int getLocationImage() {
        return locationImage;
    }

    public String getLocatonName() {
        return locatonName;
    }

    public String getLocatonLat() {
        return locatonLat;
    }

    public String getLocatonLon() {
        return locatonLon;
    }

    public String getLocationId() {
        return locationId;
    }
}
