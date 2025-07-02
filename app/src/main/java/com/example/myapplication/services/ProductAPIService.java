package com.example.myapplication.services;

import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Review;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ProductAPIService {

    @GET("products/all")
    Call<List<Product>> getAllProducts(@Header("Authorization") String token);

    @GET("product-versions/{id}")
    Call<Product> getProductById(@Header("Authorization") String token, @Path("id") String id);

    @Multipart
    @POST("products")
    Call<Product> createProduct(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("discount") RequestBody discount,
            @Part("visible") RequestBody visible,
            @Part("available") RequestBody available,
            @Part List<MultipartBody.Part> images,
            @Part("suggestedCategoryName") RequestBody suggestedCategoryName,
            @Part("suggestedCategoryDesc") RequestBody suggestedCategoryDesc,
            @Part("category") RequestBody category,
            @Part("provider") RequestBody providerId
    );

    @Multipart
    @PUT("products/{id}")
    Call<Object> updateProduct(@Header("Authorization") String token, @Path("id") String id,
                               @Part("name") RequestBody name,
                               @Part("description") RequestBody description,
                               @Part("price") RequestBody price,
                               @Part("discount") RequestBody discount,
                               @Part("visible") RequestBody visible,
                               @Part("available") RequestBody available,
                               @Part List<MultipartBody.Part> images);  // changed to accept multiple images

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Header("Authorization") String token, @Path("id") String id);

    @POST("products/{id}/favorite")
    Call<String> addProductToFavorites(@Header("Authorization") String token, @Path("id") String id);

    @DELETE("products/{id}/favorite")
    Call<String> removeProductFromFavorites(@Header("Authorization") String token, @Path("id") String id);

    @POST("products/{id}/review")
    Call<String> submitReview(@Header("Authorization") String token, @Path("id") String productId, @Body RequestBody reviewData);
}
