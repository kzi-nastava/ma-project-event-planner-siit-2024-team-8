package com.example.myapplication.domain.dto;

import com.example.myapplication.domain.enumerations.EventSortParameter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchEventsRequest {
    private String name; //searchByName
    private List<String> eventTypes; //searchByType
    private int lowerCapacity;
    private int upperCapacity;
    private LocalDate startDate;

    private LocalDate endDate;

    private String sortBy;

    private String sortOrder;

    private String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String  sortBy) {
        this.sortBy = sortBy;
    }

    public SearchEventsRequest(){
        eventTypes = new ArrayList<>();
        name = "";
        sortBy = null;
        sortOrder = null;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public int getLowerCapacity() {
        return lowerCapacity;
    }

    public void setLowerCapacity(int lowerCapacity) {
        this.lowerCapacity = lowerCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getUpperCapacity() {
        return upperCapacity;
    }

    public void setUpperCapacity(int upperCapacity) {
        this.upperCapacity = upperCapacity;
    }
}