package com.example.myapplication.domain.dto;


import com.example.myapplication.domain.enumerations.AssetSortParameter;
import com.example.myapplication.domain.enumerations.AssetType;

import java.util.List;
import java.util.UUID;

public class SearchAssetRequest {
    private AssetType assetType;
    private List<String> assetCategories;
    private String name;
    private int priceLow;
    private int priceHigh;
    private int gradeLow;
    private int gradeHigh;
    private Boolean available;

    private String sortBy;

    private String sortOrder;
    private String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<String> getAssetCategories() {
        return assetCategories;
    }

    public void setAssetCategories(List<String> assetCategories) {
        this.assetCategories = assetCategories;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getGradeHigh() {
        return gradeHigh;
    }

    public void setGradeHigh(Integer gradeHigh) {
        this.gradeHigh = gradeHigh;
    }

    public Integer getGradeLow() {
        return gradeLow;
    }

    public void setGradeLow(Integer gradeLow) {
        this.gradeLow = gradeLow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriceHigh() {
        return priceHigh;
    }

    public void setPriceHigh(Integer priceHigh) {
        this.priceHigh = priceHigh;
    }

    public Integer getPriceLow() {
        return priceLow;
    }

    public void setPriceLow(Integer priceLow) {
        this.priceLow = priceLow;
    }
}
