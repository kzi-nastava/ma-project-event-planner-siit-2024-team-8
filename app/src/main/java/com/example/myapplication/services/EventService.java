package com.example.myapplication.services;

import android.util.Log;

import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.PagedResponse;
import com.example.myapplication.domain.dto.event.CreateEventRequest;
import com.example.myapplication.domain.dto.event.EventCardResponse;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.example.myapplication.domain.dto.event.EventSignupRequest;
import com.example.myapplication.domain.dto.event.EventUpdateRequest;
import com.example.myapplication.utilities.RetrofitClient;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventService {

    private final EventAPIService apiService;

    public EventService() {
        apiService = RetrofitClient.getRetrofitInstance().create(EventAPIService.class);
    }

    public void createEvent(CreateEventRequest request, Callback<ApiResponse> callback){
        Call<ApiResponse> call = apiService.createEvent(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    Log.e("EventService", "Failed to fetch event: " + response.message());
                    callback.onFailure(call, new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("EventService", "API call failed", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void getTop5Events(Callback<List<EventCardResponse>> callback){
        Call<List<EventCardResponse>> call = apiService.getTop5Events();
        call.enqueue(callback);
    }

    public void getEventById(String eventId, Callback<EventInfoResponse> callback) {
        Call<EventInfoResponse> call = apiService.getEventById(eventId);
        call.enqueue(new Callback<EventInfoResponse>() {
            @Override
            public void onResponse(Call<EventInfoResponse> call, Response<EventInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    Log.e("EventService", "Failed to fetch event: " + response.message());
                    callback.onFailure(call, new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<EventInfoResponse> call, Throwable t) {
                Log.e("EventService", "API call failed", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void updateEvent(EventUpdateRequest eventUpdateRequest, Callback<String> callback) {
        Call<String> call = apiService.updateEvent(eventUpdateRequest);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Assuming response body is a simple String message
                    Log.d("EventService", "Event updated successfully: " + response.body());
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    // Handle unsuccessful response
                    Log.e("EventService", "Failed to update event: " + response.message());
                    callback.onFailure(call, new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("EventService", "API call failed", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void deleteEvent(String eventId, Callback<String> callback) {
        Call<String> call = apiService.deleteEvent(eventId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("err", "err", t);
            }
        });
    }


    public void submitReview(String eventId, RequestBody reviewData, Callback<String> callback) {
        Call<String> call = apiService.submitReview(eventId, reviewData);
        call.enqueue(callback);
    }

    public void checkAssetInOrganizedEvents(String userId, String assetId, Callback<Boolean> callback) {
        Call<Boolean> call = apiService.checkAssetInOrganizedEvents(userId, assetId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    Log.e("EventService", "Failed to check asset in events: " + response.message());
                    callback.onFailure(call, new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("EventService", "API call failed", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void isUserSignedUp(EventSignupRequest eventSignupRequest, Callback<Boolean> callback) {
        Call<Boolean> call = apiService.isUserSignedUp(eventSignupRequest);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    Log.e("EventService", "Failed to check user signup: " + response.message());
                    callback.onFailure(call, new Throwable("Error: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("EventService", "API call failed", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void getEvents(int pageSize,int pageNumber, Callback<PagedResponse<EventCardResponse>> events){
        Call<PagedResponse<EventCardResponse>> call = apiService.getEvents(pageNumber,pageSize);
        call.enqueue(new Callback<PagedResponse<EventCardResponse>>() {
            @Override
            public void onResponse(Call<PagedResponse<EventCardResponse>> call, Response<PagedResponse<EventCardResponse>> response) {
                events.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<PagedResponse<EventCardResponse>> call, Throwable t) {

            }
        });
    }

    public void signUserUpToEvent(EventSignupRequest request, Callback<String> callback){
        apiService.signUserUpToEvent(request).enqueue(callback);
    }
    public void leaveEvent(EventSignupRequest request,Callback<String> callback){
        apiService.leaveEvent(request).enqueue(callback);
    }
}