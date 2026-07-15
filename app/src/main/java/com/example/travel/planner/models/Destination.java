package com.example.travel.planner.models;

import java.io.Serializable;

public class Destination implements Serializable {
    private String id, name, address, notes, dayLabel;
    private double latitude, longitude;  // ✅ lat/lng fields
    private int orderIndex;

    public Destination() {}


    public Destination(String name, String address) {
        this.id       = String.valueOf(System.currentTimeMillis());
        this.name     = name;
        this.address  = address;
        this.notes    = "";
        this.dayLabel = "Day 1";
        this.latitude = 0;
        this.longitude= 0;
    }


    public Destination(String name, String address,
                       double latitude, double longitude) {
        this.id        = String.valueOf(System.currentTimeMillis());
        this.name      = name;
        this.address   = address;
        this.notes     = "";
        this.dayLabel  = "Day 1";
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getDayLabel() { return dayLabel; }
    public void setDayLabel(String dayLabel) { this.dayLabel = dayLabel; }


    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
}