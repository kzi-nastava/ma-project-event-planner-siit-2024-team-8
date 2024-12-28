package com.example.myapplication.domain;

import com.example.myapplication.domain.dto.BudgetItemCreateRequest;

import java.util.UUID;

public class BudgetItem {


    private AssetCategory category;

    private Double plannedAmount;

    public BudgetItem(){
        plannedAmount = 0.0;
    }

    public BudgetItem(AssetCategory category){
        this.plannedAmount = 0.0;
        this.category = category;
    }

    public BudgetItem(String plannedAmmount,AssetCategory category){
        this.category = category;
    }
    public AssetCategory getCategory() {
        return category;
    }

    public void setCategory(AssetCategory category) {
        this.category = category;
    }

    public Double getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(Double plannedAmount) {
        this.plannedAmount = plannedAmount;
    }
}
