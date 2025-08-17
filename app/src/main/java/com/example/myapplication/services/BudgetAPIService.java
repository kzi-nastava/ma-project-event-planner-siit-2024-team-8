package com.example.myapplication.services;

import com.example.myapplication.domain.Budget;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.dto.ReservationResponse;
import com.example.myapplication.domain.dto.asset.CreateReservationRequest;
import com.example.myapplication.domain.dto.event.BudgetItemCreateRequest;
import com.example.myapplication.domain.dto.event.BudgetItemResponse;
import com.example.myapplication.domain.dto.event.BudgetResponse;

import java.util.UUID;

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
    Call<BudgetResponse> getBudgetByEventId(@Path("id") String id);

    @PUT("event/budget/item/{budgetItemId}")
    Call<BudgetItemResponse> updateBudgetItem(@Path("budgetItemId") String budgetItemId, @Query("plannedAmount") String plannedAmount);

    @POST("event/budget/item/{budgetId}")
    Call<BudgetItemResponse> addBudgetItem(@Path("budgetId") String budgetId, @Body BudgetItemCreateRequest budgetItem);

    @DELETE("event/budget/item/{budgetItemId}")
    Call<Void> deleteBudgetItem(@Path("budgetItemId") String budgetItemId);

    @POST("event/budget/{eventId}/buy-product/{productId}")
    Call<BudgetItemResponse> buyProduct(@Path("eventId") String eventId, @Path("productId") String productId);

    @POST("event/budget/reserve-utility")
    Call<BudgetItemResponse> reserveUtility(@Body CreateReservationRequest request);

    @GET("event/budget/reservation/{eventId}/{utilityId}")
    Call<ReservationResponse> fetchReservation(@Path("eventId") String eventId,@Path("utilityId") String utilityId);

    @PUT("event/budget/accept-reservation/{reservationId}")
    Call<Void> acceptReservation(@Path("reservationId") UUID reservationId);

    @PUT("event/budget/deny-reservation/{reservationId}")
    Call<Void> denyReservation(@Path("reservationId") UUID reservationId);
    @PUT("event/budget/{eventId}/cancel-utility/{utilityVersionId}")
    Call<Void> cancelReservation(@Path("eventId")UUID eventId,@Path("utilityVersionId") UUID utilityVersionId);
}