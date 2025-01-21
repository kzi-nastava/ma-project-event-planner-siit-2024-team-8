package com.example.myapplication.domain.dto;

import com.example.myapplication.domain.BudgetItem;

import java.util.UUID;

public class BudgetItemCreateRequest {
    private Double plannedAmount;
    private UUID assetCategoryId;

    public BudgetItemCreateRequest(BudgetItem item){
        this.plannedAmount = item.getPlannedAmount();
        this.assetCategoryId = UUID.fromString(item.getCategory().getId());
    }

    public UUID getAssetCategoryId() {
        return assetCategoryId;
    }

    public void setAssetCategoryId(UUID assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public Double getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(Double plannedAmount) {
        this.plannedAmount = plannedAmount;
    }
}