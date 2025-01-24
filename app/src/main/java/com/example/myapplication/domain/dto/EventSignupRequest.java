package com.example.myapplication.domain.dto;

public class EventSignupRequest {
    private String eventId;
    private String userId;

    public EventSignupRequest(String userId, String eventId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
