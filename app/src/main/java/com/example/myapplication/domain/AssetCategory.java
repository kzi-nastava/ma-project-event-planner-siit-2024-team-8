package com.example.myapplication.domain;

import java.io.Serializable;

public class AssetCategory implements Serializable {
    private String name;
    private String description;

    public AssetCategory(String name, String description) {
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return "AssetCategory{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
