package com.example.myapplication.services;

import com.example.myapplication.domain.Budget;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.dto.BudgetItemCreateRequest;

import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BudgetAPIService {
    @GET("event/budget/{id}")
    Call<Budget> getBudgetByEventId(@Path("id") String id);

    @PUT("event/budget/item/{budgetItemId}")
    Call<BudgetItem> updateBudgetItem(@Path("budgetItemId") String budgetItemId, @Query("plannedAmount") String plannedAmount);

    @POST("event/budget/item/{budgetId}")
    Call<BudgetItem> addBudgetItem(@Path("budgetId") String budgetId, @Body BudgetItemCreateRequest budgetItem);

    @DELETE("event/budget/item/{budgetItemId}")
    Call<Void> deleteBudgetItem(@Path("budgetItemId") String budgetItemId);

    @POST("event/budget/{eventId}/buy-product/{productId}")
    Call<BudgetItem> buyProduct(@Path("eventId") String eventId, @Path("productId") String productId);

    @POST("event/budget/{eventId}/reserve-utility/{utilityId}")
    Call<BudgetItem> reserveUtility(@Path("eventId") String eventId, @Path("utilityId") String utilityId);
}