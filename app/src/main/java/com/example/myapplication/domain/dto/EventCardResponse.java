package com.example.myapplication.domain.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class EventCardResponse {
    private UUID id;
    private String name;
    private String startDate;

    public EventCardResponse(UUID id, String name, String description, String startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }

    public EventCardResponse(EventInfoResponse response){
        this.id = UUID.fromString(response.getId());
        this.name = response.getName();
        this.startDate = response.getStartDate();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
