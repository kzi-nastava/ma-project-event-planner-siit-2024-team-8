package com.example.myapplication.services;

import com.example.myapplication.domain.PagedResponse;
import com.example.myapplication.domain.dto.user.AssetResponse;
import com.example.myapplication.domain.enumerations.AssetType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AssetAPIService {
    @GET("assets/filter")
    Call<PagedResponse<AssetResponse>> filterAssets(
            @Query("page") int page,
            @Query("size") int size,
            @Query("name") String name,
            @Query("assetCategories") List<String> assetCategories,
            @Query("assetType") AssetType assetType,
            @Query("priceLow") Integer priceLow,
            @Query("priceHigh") Integer priceHigh,
            @Query("gradeLow") Integer gradeLow,
            @Query("gradeHigh") Integer gradeHigh,
            @Query("sortBy") String sortParameter,
            @Query("sortOrder") String sortOrder,
            @Query("isAvailable") Boolean isAvailable,
            @Query("owner") String owner
    );

    @GET("assets/top5")
    Call<List<AssetResponse>> getTop5Assets();

    @GET("assets/asset-version/bought")
    Call<List<AssetResponse>> getBoughtAssets(@Query("assetVersionIds") List<String> assetVersionIds);
}
