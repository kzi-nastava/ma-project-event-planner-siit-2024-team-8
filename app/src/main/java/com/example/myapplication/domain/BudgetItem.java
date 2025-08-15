package com.example.myapplication.domain;

import java.util.ArrayList;
import java.util.List;

public class BudgetItem {

    private String id;
    private String assetCategoryId;
    private Double plannedAmount;
    private Double actualAmount;

    public BudgetItem(){
        plannedAmount = 0.0;
        actualAmount = 0.0;
    }

    public BudgetItem(AssetCategory category){
        this.plannedAmount = 0.0;
        this.assetCategoryId = category.getId();
    }

    public BudgetItem(String plannedAmmount, String category){
        this.assetCategoryId = category;
    }


    public String getCategory() {
        return assetCategoryId;
    }

    public void setCategory(String category) {
        this.assetCategoryId = category;
    }

    public Double getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(Double plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
