package com.example.myapplication.services;

import com.example.myapplication.domain.PriceListItem;
import com.example.myapplication.utilities.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListService {

    private final PriceListAPIService apiService;

    public PriceListService() {
        apiService = RetrofitClient.getRetrofitInstance().create(PriceListAPIService.class);
    }

    public void getPriceList(String providerId, Callback<List<PriceListItem>> callback) {
        Call<List<PriceListItem>> call = apiService.getPriceList(providerId);
        call.enqueue(callback);
    }

    public void updatePriceAndDiscount(String assetId, double newPrice, double newDiscount, Callback<Void> callback) {
        Call<Void> call = apiService.updatePriceAndDiscount(
                assetId,
                String.valueOf(newPrice),
                String.valueOf(newDiscount)
        );
        call.enqueue(callback);
    }
}