package com.example.myapplication.domain.dto;

import com.example.myapplication.domain.enumerations.EventSortParameter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchEventsRequest {
    private String name; //searchByName
    private List<String> eventTypes; //searchByType
    private int lowerCapacity;
    private int upperCapacity;
    private LocalDate startDate;

    private LocalDate endDate;

    private EventSortParameter sortParameter;

    private Boolean ascending;

    public Boolean getAscending() {
        return ascending;
    }

    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }

    public EventSortParameter getSortParameter() {
        return sortParameter;
    }

    public void setSortParameter(EventSortParameter sortParameter) {
        this.sortParameter = sortParameter;
    }

    public SearchEventsRequest(){
        eventTypes = new ArrayList<>();
        name = "";
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