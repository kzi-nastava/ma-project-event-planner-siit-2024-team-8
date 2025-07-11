package com.example.myapplication.services;

import com.example.myapplication.domain.Review;
import com.example.myapplication.domain.Utility;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UtilityAPIService {

    @GET("utilities/all")
    Call<List<Utility>> getAllUtilities(@Header("Authorization") String token);

    @GET("utilities/{id}")
    Call<Utility> getUtilityById(@Header("Authorization") String token, @Path("id") String id);

    @Multipart
    @POST("utilities")
    Call<Utility> createUtility(@Header("Authorization") String token,
                                @Part("name") RequestBody name,
                                @Part("description") RequestBody description,
                                @Part("price") RequestBody price,
                                @Part("discount") RequestBody discount,
                                @Part("visible") RequestBody visible,
                                @Part("available") RequestBody available,
                                @Part("duration") RequestBody duration,
                                @Part("reservationTerm") RequestBody reservationTerm,
                                @Part("cancellationTerm") RequestBody cancellationTerm,
                                @Part("manuelConfirmation") RequestBody manuelConfirmation,
                                @Part List<MultipartBody.Part> images,
                                @Part("suggestedCategoryName") RequestBody suggestedCategoryName,
                                @Part("suggestedCategoryDesc") RequestBody suggestedCategoryDesc,
                                @Part("category") RequestBody category,
                                @Part("provider") RequestBody providerId);

    @PUT("utilities/{id}")
    Call<Utility> updateUtility(@Header("Authorization") String token, @Path("id") String id,
                                @Part("name") RequestBody name,
                                @Part("description") RequestBody description,
                                @Part("price") RequestBody price,
                                @Part("discount") RequestBody discount,
                                @Part("visible") RequestBody visible,
                                @Part("available") RequestBody available,
                                @Part("duration") RequestBody duration,
                                @Part("reservationTerm") RequestBody reservationTerm,
                                @Part("cancellationTerm") RequestBody cancellationTerm,
                                @Part("manuelConfirmation") RequestBody manuelConfirmation,
                                @Part List<MultipartBody.Part> images);

    @DELETE("utilities/{id}")
    Call<Void> deleteUtility(@Header("Authorization") String token, @Path("id") String id);

    @POST("utilities/{id}/favorite")
    Call<String> addUtilityToFavorites(@Header("Authorization") String token, @Path("id") String id);

    @DELETE("utilities/{id}/favorite")
    Call<String> removeUtilityFromFavorites(@Header("Authorization") String token, @Path("id") String id);

    @POST("utilities/{id}/review")
    Call<String> submitReview(@Header("Authorization") String token, @Path("id") String utilityId, @Body RequestBody reviewData);
}
