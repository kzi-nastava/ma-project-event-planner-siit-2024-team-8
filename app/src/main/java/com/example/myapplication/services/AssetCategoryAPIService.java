package com.example.myapplication.services;

import com.example.myapplication.domain.AssetCategory;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AssetCategoryAPIService {

    @GET("asset-categories/active")
    Call<List<AssetCategory>> getActiveCategories(@Header("Authorization") String token);

    @GET("asset-categories/pending")
    Call<List<AssetCategory>> getPendingCategories(@Header("Authorization") String token);

    @POST("asset-categories")
    Call<AssetCategory> createCategory(@Header("Authorization") String token, @Body AssetCategory category);

    @PUT("asset-categories/{id}")
    Call<AssetCategory> updateCategory(@Header("Authorization") String token, @Path("id") String categoryId, @Body AssetCategory category);

    @DELETE("asset-categories/{id}")
    Call<Void> deleteCategory(@Header("Authorization") String token, @Path("id") String categoryId);

    @PUT("asset-categories/pending/{id}")
    Call<Void> approveCategory(@Header("Authorization") String token, @Path("id") String categoryId);
}
