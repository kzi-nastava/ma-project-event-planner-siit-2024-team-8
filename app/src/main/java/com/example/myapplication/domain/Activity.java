package com.example.myapplication.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Activity {
    private String name;
    private String startTime;

    private String endTime;
    private String location;

    private String description;

    private boolean input;

    public Activity(String name,String location,String description, String startTime,String endTime,boolean isInput) {
        this.location = location;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.input = isInput;
    }

    public Activity( String name,String description, String location, String startTime,String endTime) {
        this.description = description;
        this.location = location;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Activity(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTimeString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        return startTime + "-" + endTime;
    }

    public boolean isInput() {
        return input;
    }
}
