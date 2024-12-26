package com.example.myapplication.domain;

import java.util.ArrayList;
import java.util.UUID;

public class Budget {
    private UUID id;

    private Integer plannedBudget;

    private Double currentBudget;

    private ArrayList<BudgetItem> budgetItems;

    public ArrayList<BudgetItem> getBudgetItems() {
        return budgetItems;
    }

    public void setBudgetItems(ArrayList<BudgetItem> budgetItems) {
        this.budgetItems = budgetItems;
    }

    public Double getCurrentBudget() {
        return currentBudget;
    }

    public void setCurrentBudget(Double currentBudget) {
        this.currentBudget = currentBudget;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getPlannedBudget() {
        return plannedBudget;
    }

    public void setPlannedBudget(Integer plannedBudget) {
        this.plannedBudget = plannedBudget;
    }
}
