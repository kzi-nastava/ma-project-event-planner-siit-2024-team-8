package com.example.myapplication.domain.dto;

public class EventInfoResponse {
    public String id;
    public String name;
    public String description;
    public Integer capacity;
    public Boolean isPrivate;
    public EventLocationDTO location;
    public String organizerName;
    public String organizerID;
    public String startDate;
    public String endDate;

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }
}
