package com.example.myapplication.services;

import android.util.Log;

import com.example.myapplication.domain.Budget;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.dto.ReservationResponse;
import com.example.myapplication.domain.dto.asset.CreateReservationRequest;
import com.example.myapplication.domain.dto.event.BudgetItemCreateRequest;
import com.example.myapplication.domain.dto.event.BudgetItemResponse;
import com.example.myapplication.domain.dto.event.BudgetResponse;
import com.example.myapplication.utilities.RetrofitClient;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetService {

    private final BudgetAPIService apiService;

    public BudgetService() {
        apiService = RetrofitClient.getRetrofitInstance().create(BudgetAPIService.class);
    }

    public void getBudgetByEventId(String budgetId, Callback<BudgetResponse> callback) {
        Call<BudgetResponse> call = apiService.getBudgetByEventId(budgetId);
        call.enqueue(callback);
    }

    public void updateBudgetItem(String budgetItemId, double plannedAmount, Callback<BudgetItemResponse> callback) {
        Call<BudgetItemResponse> call = apiService.updateBudgetItem(budgetItemId, String.valueOf(plannedAmount));
        call.enqueue(callback);
    }

    public void addBudgetItem(String budgetId, BudgetItemCreateRequest budgetItem, Callback<BudgetItemResponse> callback) {
        Call<BudgetItemResponse> call = apiService.addBudgetItem(budgetId, budgetItem);
        call.enqueue(callback);
    }

    public void deleteBudgetItem(String budgetItemId, Callback<Void> callback) {
        Call<Void> call = apiService.deleteBudgetItem(budgetItemId);
        call.enqueue(callback);
    }

    public void buyProduct(String eventId, String productId, Callback<BudgetItemResponse> callback) {
        Call<BudgetItemResponse> call = apiService.buyProduct(eventId, productId);
        call.enqueue(callback);
    }

    public void reserveUtility(CreateReservationRequest request, Callback<BudgetItemResponse> callback) {
        Call<BudgetItemResponse> call = apiService.reserveUtility(request);
        call.enqueue(callback);
    }

    public void fetchReservation(String eventId, String utilityId,Callback<ReservationResponse> callback) {
        Call<ReservationResponse> call = apiService.fetchReservation(eventId,utilityId);
        call.enqueue(callback);
    }

    public void acceptReservation(UUID reservationId) {
        Call<Void> call = apiService.acceptReservation(reservationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Log.d("err",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void denyReservation(UUID reservationId){
        Call<Void> call = apiService.denyReservation(reservationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Log.d("err",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void cancelReservation(String eventId,String utilityVersionId) {
        apiService.cancelReservation(UUID.fromString(eventId),UUID.fromString(utilityVersionId)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()){
                    Log.d("Err",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}