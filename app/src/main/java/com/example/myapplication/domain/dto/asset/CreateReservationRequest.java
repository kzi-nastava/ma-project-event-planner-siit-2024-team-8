package com.example.myapplication.domain.dto.asset;

import java.util.UUID;

public class CreateReservationRequest {
    private String date;
    private String time;
    private UUID utilityId;
    private UUID eventId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UUID getUtilityId() {
        return utilityId;
    }

    public void setUtilityId(UUID utilityId) {
        this.utilityId = utilityId;
    }
}
