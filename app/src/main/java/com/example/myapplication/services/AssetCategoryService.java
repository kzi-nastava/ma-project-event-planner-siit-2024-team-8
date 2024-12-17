package com.example.myapplication.services;

import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.utilities.RetrofitClient;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetCategoryService {

    private final AssetCategoryAPIService apiService;

    public AssetCategoryService() {
        apiService = RetrofitClient.getRetrofitInstance().create(AssetCategoryAPIService.class);
    }

    public void getActiveCategories(String token, Callback<List<AssetCategory>> callback) {
        Call<List<AssetCategory>> call = apiService.getActiveCategories(token);
        call.enqueue(callback);
    }

    public void getPendingCategories(String token, Callback<List<AssetCategory>> callback) {
        Call<List<AssetCategory>> call = apiService.getPendingCategories(token);
        call.enqueue(callback);
    }

    public void getActiveUtilityCategories(String token, Callback<List<AssetCategory>> callback) {
        getActiveCategories(token, new Callback<List<AssetCategory>>() {
            @Override
            public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AssetCategory> utilityCategories = filterUtilityCategories(response.body());
                    callback.onResponse(call, Response.success(utilityCategories));
                } else {
                    callback.onFailure(call, new Throwable("Failed to fetch categories"));
                }
            }

            @Override
            public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void getActiveProductCategories(String token, Callback<List<AssetCategory>> callback) {
        getActiveCategories(token, new Callback<List<AssetCategory>>() {
            @Override
            public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AssetCategory> productCategories = filterProductCategories(response.body());
                    callback.onResponse(call, Response.success(productCategories));
                } else {
                    callback.onFailure(call, new Throwable("Failed to fetch categories"));
                }
            }

            @Override
            public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public void createCategory(String token, AssetCategory category, Callback<AssetCategory> callback) {
        Call<AssetCategory> call = apiService.createCategory(token, category);
        call.enqueue(callback);
    }

    public void updateCategory(String token, String categoryId, AssetCategory category, Callback<AssetCategory> callback) {
        Call<AssetCategory> call = apiService.updateCategory(token, categoryId, category);
        call.enqueue(callback);
    }

    public void deleteCategory(String token, String categoryId, Callback<Void> callback) {
        Call<Void> call = apiService.deleteCategory(token, categoryId);
        call.enqueue(callback);
    }

    public void approveCategory(String token, String categoryId, Callback<Void> callback) {
        Call<Void> call = apiService.approveCategory(token, categoryId);
        call.enqueue(callback);
    }

    private List<AssetCategory> filterUtilityCategories(List<AssetCategory> categories) {
        return categories.stream()
                .filter(category -> "UTILITY".equals(category.getType()))
                .collect(Collectors.toList());
    }

    private List<AssetCategory> filterProductCategories(List<AssetCategory> categories) {
        return categories.stream()
                .filter(category -> "PRODUCT".equals(category.getType()))
                .collect(Collectors.toList());
    }
}
