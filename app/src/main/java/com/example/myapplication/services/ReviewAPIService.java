package com.example.myapplication.services;

import com.example.myapplication.domain.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewAPIService {

    @GET("reviews/pending")
    Call<List<Review>> getPendingReviews();

    @PUT("reviews/{reviewId}/approve")
    Call<Void> approveReview(@Path("reviewId") String reviewId);

    @PUT("reviews/{reviewId}/deny")
    Call<Void> denyReview(@Path("reviewId") String reviewId);

    @GET("reviews/{eventId}/event")
    Call<List<Review>> getActiveReviewsForEvent(@Path("eventId") String eventId);

    @GET("reviews/{assetId}/asset")
    Call<List<Review>> getActiveReviewsForAsset(@Path("assetId") String assetId);
}