package com.example.travel.planner.models;

public class Recommendation {
    private String name;
    private String category;
    private String type;
    private double latitude;
    private double longitude;
    private double distance;
    private String address;

    public Recommendation() {}

    public Recommendation(String name, String category,
                          String type, double lat, double lng, String address) {
        this.name     = name;
        this.category = category;
        this.type     = type;
        this.latitude = lat;
        this.longitude= lng;
        this.address  = address;
    }

    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double lat) { this.latitude = lat; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double lng) { this.longitude = lng; }
    public double getDistance() { return distance; }
    public void setDistance(double d) { this.distance = d; }
    public String getAddress() { return address; }
    public void setAddress(String a) { this.address = a; }

    // Distance string for display
    public String getDistanceString() {
        if (distance < 1.0)
            return (int)(distance * 1000) + "m away";
        return String.format("%.1fkm away", distance);
    }
}