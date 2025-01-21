package com.example.myapplication.domain;

import java.util.ArrayList;
import java.util.UUID;

public class Budget {
    private UUID id;

    private Integer plannedBudget;

    private Double actualBudget;

    private ArrayList<BudgetItem> items;

    public ArrayList<BudgetItem> getBudgetItems() {
        return items;
    }

    public void setBudgetItems(ArrayList<BudgetItem> budgetItems) {
        this.items = budgetItems;
    }

    public Double getActualBudget() {
        return actualBudget;
    }

    public void setActualBudget(Double actualBudget) {
        this.actualBudget = actualBudget;
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
