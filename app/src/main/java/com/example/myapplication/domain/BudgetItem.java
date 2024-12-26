package com.example.myapplication.domain;

import java.util.UUID;

public class BudgetItem {
    private String name;


    private AssetCategory category;

    //private Asset boughtAsset;


    private Budget budget;

    private Integer plannedAmount;

    public BudgetItem(String name,AssetCategory category){
        this.name = name;
        this.category = category;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public AssetCategory getCategory() {
        return category;
    }

    public void setCategory(AssetCategory category) {
        this.category = category;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlannedAmount() {
        return plannedAmount;
    }

    public void setPlannedAmount(Integer plannedAmount) {
        this.plannedAmount = plannedAmount;
    }
}
