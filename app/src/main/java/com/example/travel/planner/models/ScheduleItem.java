package com.example.travel.planner.models;

import java.io.Serializable;

public class ScheduleItem implements Serializable {
    private String id;
    private String placeName;
    private String address;
    private String time;       // e.g. "09:00 AM"
    private String notes;
    private String category;   // e.g. "Sightseeing", "Food", "Hotel"
    private double latitude;
    private double longitude;

    public ScheduleItem() {}

    public ScheduleItem(String placeName, String address,
                        String time, String category) {
        this.id        = String.valueOf(System.currentTimeMillis());
        this.placeName = placeName;
        this.address   = address;
        this.time      = time;
        this.category  = category;
        this.notes     = "";
    }

    public String getId() { return id; }
    public String getPlaceName() { return placeName; }
    public void setPlaceName(String n) { this.placeName = n; }
    public String getAddress() { return address; }
    public void setAddress(String a) { this.address = a; }
    public String getTime() { return time; }
    public void setTime(String t) { this.time = t; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { this.notes = n; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double lat) { this.latitude = lat; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double lng) { this.longitude = lng; }
}