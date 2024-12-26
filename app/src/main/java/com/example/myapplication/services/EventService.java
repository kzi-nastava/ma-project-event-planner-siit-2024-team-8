package com.example.myapplication.services;

import android.util.Log;

import com.example.myapplication.domain.dto.EventInfoResponse;
import com.example.myapplication.domain.dto.EventUpdateRequest;
import com.example.myapplication.utilities.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventService {

    private final EventAPIService apiService;

    public EventService() {
        apiService = RetrofitClient.getRetrofitInstance().create(EventAPIService.class);
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
}