package com.example.myapplication.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AssetCategory implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("type")
    private String type;
    @SerializedName("active")
    private boolean active;


    public AssetCategory(String id, String name, String description, String type, boolean active) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.type = type;
        this.active = active;
    }

    public AssetCategory() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AssetCategory{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
