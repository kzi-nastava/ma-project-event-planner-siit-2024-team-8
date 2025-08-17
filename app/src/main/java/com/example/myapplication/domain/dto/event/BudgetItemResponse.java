package com.example.myapplication.domain.dto.event;

import java.util.List;
import java.util.UUID;

public class BudgetItemResponse {
    private UUID id;
    private UUID assetCategoryId;
    private Double plannedAmount;
    private Double actualAmount;
    private Boolean deleted;
    private List<UUID> assetVersionIds;

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public UUID getAssetCategoryId() {
        return assetCategoryId;
    }

    public void setAssetCategoryId(UUID assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public List<UUID> getAssetVersionIds() {
        return assetVersionIds;
    }

    public void setAssetVersionIds(List<UUID> assetVersionIds) {
        this.assetVersionIds = assetVersionIds;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(Double plannedAmount) {
        this.plannedAmount = plannedAmount;
    }
}
