package com.example.myapplication.domain;

public class PriceListItem {
    private String assetId;
    private String name;
    private Double price;
    private Double discount;
    private Double discountedPrice;
    private boolean editMode;

    public Double getDiscount() {
        return discount;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public Double getPrice() {
        return price;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getName() {
        return name;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
