package com.example.myapplication.domain.dto;

import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.enumerations.AssetType;

import java.util.List;
import java.util.UUID;

public class AssetResponse {
    private UUID id;
    private AssetType type;
    private String name;
    private List<String> images;
    private AssetCategory category;

    private UUID providerId;

    public AssetCategory getCategory() {
        return category;
    }

    public void setCategory(AssetCategory category) {
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public void setProviderId(UUID providerId) {
        this.providerId = providerId;
    }
}
