package com.example.myapplication.domain.dto.event;

import com.example.myapplication.domain.Activity;
import com.example.myapplication.domain.EventType;
import com.example.myapplication.domain.Location;

import java.util.ArrayList;
import java.util.List;

public class CreateEventRequest {
    private String organizerID;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private Integer capacity;
    private Location location;
    private List<Activity> agenda;
    private List<String> guests;
    private EventType eventType;
    private boolean isPrivate;
    private List<BudgetItemCreateRequest> budgetItems;

    public CreateEventRequest(){
        guests = new ArrayList<>();
        agenda = new ArrayList<>();
        location = new Location();
    }

    public List<Activity> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<Activity> agenda) {
        this.agenda = agenda;
    }

    public List<BudgetItemCreateRequest> getBudgetItems() {
        return budgetItems;
    }

    public void setBudgetItems(List<BudgetItemCreateRequest> budgetItems) {
        this.budgetItems = budgetItems;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public List<String> getGuests() {
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
