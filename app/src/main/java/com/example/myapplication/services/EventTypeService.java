package com.example.myapplication.services;

import android.graphics.Path;
import android.util.Log;

import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.EventType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class EventTypeService {

    public Future<List<EventType>> getActiveEventTypes(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> {
            Call<List<EventType>> call = ClientUtils.eventTypeService.getActiveEventTypes();
            Response<List<EventType>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to fetch event types");
            }
        });
    }

    public Future<List<EventType>> getInactiveEventTypes(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> {
            Call<List<EventType>> call = ClientUtils.eventTypeService.getInactiveEventTypes();
            Response<List<EventType>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to fetch event types");
            }
        });
    }

    public Future<ApiResponse> saveAllEventTypes(List<EventType> eventTypes){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> {
            Call<ApiResponse> call = ClientUtils.eventTypeService.saveAllEventTypes(eventTypes);
            Response<ApiResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to fetch event types");
            }
        });
    }

    public Future<Optional<EventType>> changeActiveStatus(UUID id, boolean active){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> {
            Call<Optional<EventType>> call = active ? ClientUtils.eventTypeService.deaactivateEventType(id.toString()):
                                            ClientUtils.eventTypeService.activateEventType(id.toString());
            Response<Optional<EventType>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                Log.d("API", "Response body: " + response.body());
                return response.body();
            } else {
                throw new RuntimeException("Failed to fetch event types");
            }
        });
    }
}
