package com.example.myapplication.services;

import com.example.myapplication.domain.PriceListItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PriceListAPIService {

    @GET("price-list")
    Call<List<PriceListItem>> getPriceList(@Query("providerId") String providerId);

    @PUT("price-list/{assetId}/price")
    Call<Void> updatePriceAndDiscount(
            @Path("assetId") String assetId,
            @Query("newPrice") String newPrice,
            @Query("newDiscount") String newDiscount
    );
}