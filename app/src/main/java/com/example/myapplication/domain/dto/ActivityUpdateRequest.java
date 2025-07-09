package com.example.myapplication.domain.dto;

import com.example.myapplication.domain.Activity;

import java.time.LocalTime;

public class ActivityUpdateRequest {
    public String startTime;
    public String endTime;
    public String description;
    public String name;
    public String place;

    public ActivityUpdateRequest(LocalTime startTime, LocalTime endTime, String description, String name, String place) {
        this.startTime = String.valueOf(startTime);
        this.endTime = String.valueOf(endTime);
        this.description = description;
        this.name = name;
        this.place = place;
    }

    public ActivityUpdateRequest(Activity activity){
        this.name =activity.getName();
        this.place = activity.getPlace();
        this.startTime = activity.getStartTime();
        this.endTime = activity.getEndTime();
        this.description = activity.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public ActivityUpdateRequest() {}
}