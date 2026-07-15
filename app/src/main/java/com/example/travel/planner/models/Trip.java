package com.example.travel.planner.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.example.travel.planner.models.DayPlan;

public class Trip implements Serializable {
    private String id, tripName, startDate, endDate, coverEmoji, notes;
    private List<Destination> destinations;
    private long createdAt;
    private List<DayPlan> schedule;


    public Trip() {
        destinations = new ArrayList<>();
        createdAt = System.currentTimeMillis();
    }

    public Trip(String tripName, String startDate, String endDate, String coverEmoji) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.tripName = tripName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coverEmoji = coverEmoji;
        this.destinations = new ArrayList<>();
        this.notes = "";
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTripName() { return tripName; }
    public void setTripName(String n) { this.tripName = n; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String d) { this.startDate = d; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String d) { this.endDate = d; }
    public String getCoverEmoji() { return coverEmoji; }
    public void setCoverEmoji(String e) { this.coverEmoji = e; }
    public List<Destination> getDestinations() { return destinations; }
    public void setDestinations(List<Destination> d) { this.destinations = d; }
    public String getNotes() { return notes; }
    public void setNotes(String n) { this.notes = n; }
    public long getCreatedAt() { return createdAt; }
    public List<DayPlan> getSchedule() { return schedule; }
    public void setSchedule(List<DayPlan> s) { this.schedule = s; }
    public void addDestination(Destination d) { destinations.add(d); }
}