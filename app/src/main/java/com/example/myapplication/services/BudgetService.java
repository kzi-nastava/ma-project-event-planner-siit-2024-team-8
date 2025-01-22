package com.example.myapplication.services;

import com.example.myapplication.domain.Budget;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.dto.BudgetItemCreateRequest;
import com.example.myapplication.utilities.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetService {

    private final BudgetAPIService apiService;

    public BudgetService() {
        apiService = RetrofitClient.getRetrofitInstance().create(BudgetAPIService.class);
    }

    public void getBudgetByEventId(String budgetId, Callback<Budget> callback) {
        Call<Budget> call = apiService.getBudgetByEventId(budgetId);
        call.enqueue(callback);
    }

    public void updateBudgetItem(String budgetItemId, double plannedAmount, Callback<BudgetItem> callback) {
        Call<BudgetItem> call = apiService.updateBudgetItem(budgetItemId, String.valueOf(plannedAmount));
        call.enqueue(callback);
    }

    public void addBudgetItem(String budgetId, BudgetItemCreateRequest budgetItem, Callback<BudgetItem> callback) {
        Call<BudgetItem> call = apiService.addBudgetItem(budgetId, budgetItem);
        call.enqueue(callback);
    }

    public void deleteBudgetItem(String budgetItemId, Callback<Void> callback) {
        Call<Void> call = apiService.deleteBudgetItem(budgetItemId);
        call.enqueue(callback);
    }

    public void buyProduct(String eventId, String productId, Callback<BudgetItem> callback) {
        Call<BudgetItem> call = apiService.buyProduct(eventId, productId);
        call.enqueue(callback);
    }

    public void reserveUtility(String eventId, String utilityId, Callback<BudgetItem> callback) {
        Call<BudgetItem> call = apiService.reserveUtility(eventId, utilityId);
        call.enqueue(callback);
    }
}