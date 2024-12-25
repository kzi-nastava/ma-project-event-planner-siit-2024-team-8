package com.example.myapplication.domain;

import com.example.myapplication.adapters.EventTypesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EventType {
    private UUID id;
    private String name;

    private String description;

    private Boolean active;

    private List<AssetCategory> assetCategories;

    public EventType(){
        this.id = UUID.randomUUID();
        this.name="";
        this.description = "";
        this.active = true;
        this.assetCategories = new ArrayList<>();
    }
    public EventType(EventType eventType){
        this.id = eventType.getId();
        this.name = eventType.getName();
        this.description = eventType.getDescription();
        this.active = eventType.getActive();
        this.assetCategories = eventType.getAssetCategories();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<AssetCategory> getAssetCategories() {
        return assetCategories;
    }

    public void setAssetCategories(List<AssetCategory> assetCategories) {
        this.assetCategories = assetCategories;
    }

    public void addAssetCategory(AssetCategory category){
        this.assetCategories.add(category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, active, assetCategories);
    }
}
