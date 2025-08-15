package com.example.myapplication.domain.dto.event;

import java.util.List;
import java.util.UUID;

public class BudgetResponse {
    private UUID id;
    private Double plannedBudget;
    private Double actualBudget;
    private Boolean deleted;
    private List<BudgetItemResponse> items;

    public Double getActualBudget() {
        return actualBudget;
    }

    public void setActualBudget(Double actualBudget) {
        this.actualBudget = actualBudget;
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

    public List<BudgetItemResponse> getItems() {
        return items;
    }

    public void setItems(List<BudgetItemResponse> items) {
        this.items = items;
    }

    public Double getPlannedBudget() {
        return plannedBudget;
    }

    public void setPlannedBudget(Double plannedBudget) {
        this.plannedBudget = plannedBudget;
    }
}
