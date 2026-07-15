package com.example.travel.planner.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DayPlan implements Serializable {
    private int dayNumber;
    private String dayLabel;
    private String date;
    private List<ScheduleItem> items;

    public DayPlan() {
        items = new ArrayList<>();
    }

    public DayPlan(int dayNumber, String dayLabel, String date) {
        this.dayNumber = dayNumber;
        this.dayLabel  = dayLabel;
        this.date      = date;
        this.items     = new ArrayList<>();
    }

    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int n) { this.dayNumber = n; }
    public String getDayLabel() { return dayLabel; }
    public void setDayLabel(String l) { this.dayLabel = l; }
    public String getDate() { return date; }
    public void setDate(String d) { this.date = d; }
    public List<ScheduleItem> getItems() { return items; }
    public void setItems(List<ScheduleItem> i) { this.items = i; }
    public void addItem(ScheduleItem item) { items.add(item); }
}