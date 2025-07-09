package com.example.myapplication.domain;

import com.example.myapplication.domain.dto.ActivityUpdateRequest;

import java.time.format.DateTimeFormatter;

public class Activity {
    private String name;
    private String startTime;
    private String endTime;
    private String place;

    private String description;

    private boolean input;

    public Activity(String name,String location,String description, String startTime,String endTime,boolean isInput) {
        this.place = location;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.input = isInput;
    }

    public Activity( String name,String description, String location, String startTime,String endTime) {
        this.description = description;
        this.place = location;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Activity(ActivityUpdateRequest request){
        this.name = request.getName();
        this.place = request.getPlace();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.description = request.getDescription();
        this.input = false;
    }

    public Activity(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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
