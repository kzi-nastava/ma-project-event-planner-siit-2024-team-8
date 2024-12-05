package com.example.myapplication.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Activity {
    private String name;
    private LocalTime startTime;

    private LocalTime endTime;
    private String location;

    private String description;

    private boolean input;

    public Activity(String name,String location,String description, LocalTime startTime,LocalTime endTime,boolean isInput) {
        this.location = location;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.input = isInput;
    }

    public Activity( String name,String description, String location, LocalTime startTime,LocalTime endTime) {
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

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getStartTimeString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        return startTime.format(formatter);
    }

    public String getEndTimeString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        return endTime.format(formatter);
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getTimeString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        return startTime.format(formatter) + "-" + endTime.format(formatter);
    }

    public boolean isInput() {
        return input;
    }
}
