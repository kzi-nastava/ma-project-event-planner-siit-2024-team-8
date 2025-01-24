package com.example.myapplication.services;

import com.example.myapplication.domain.Review;
import com.example.myapplication.utilities.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ReviewService {

    private final ReviewAPIService apiService;

    public ReviewService() {
        apiService = RetrofitClient.getRetrofitInstance().create(ReviewAPIService.class);
    }

    public void getPendingReviews(Callback<List<Review>> callback) {
        Call<List<Review>> call = apiService.getPendingReviews();
        call.enqueue(callback);
    }

    public void approveReview(String reviewId, Callback<Void> callback) {
        Call<Void> call = apiService.approveReview(reviewId);
        call.enqueue(callback);
    }

    public void denyReview(String reviewId, Callback<Void> callback) {
        Call<Void> call = apiService.denyReview(reviewId);
        call.enqueue(callback);
    }

    public void getActiveReviewsForEvent(String eventId, Callback<List<Review>> callback) {
        Call<List<Review>> call = apiService.getActiveReviewsForEvent(eventId);
        call.enqueue(callback);
    }

    public void getActiveReviewsForAsset(String assetId, Callback<List<Review>> callback) {
        Call<List<Review>> call = apiService.getActiveReviewsForAsset(assetId);
        call.enqueue(callback);
    }
}