package com.example.myapplication.services;

import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.EventType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface EventTypeAPIService {

    @GET("event-types/active")
    Call<List<EventType>> getActiveEventTypes();

    @GET("event-types/deactivated")
    Call<List<EventType>> getInactiveEventTypes();

    @POST("event-types/save-all")
    Call<ApiResponse> saveAllEventTypes(@Body List<EventType> eventTypes);

    @PUT("event-types/activate")
    Call<Optional<EventType>> activateEventType(@Body String id);


    @PUT("event-types/deactivate")
    Call<Optional<EventType>> deaactivateEventType(@Body String id);
}
