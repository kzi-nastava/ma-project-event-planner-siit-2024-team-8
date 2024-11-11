package com.example.eventplanner.domain;

import java.sql.Date;

public class EventDTO {
    private String name;
    private Date startDate;
    private Date endDate;

    private String imageURL;

    public EventDTO(String name, Date startDate, Date endDate, String imageURL) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStringDuration(){
        return getStartDate().toString() + "-" + getEndDate().toString();
    }
}
