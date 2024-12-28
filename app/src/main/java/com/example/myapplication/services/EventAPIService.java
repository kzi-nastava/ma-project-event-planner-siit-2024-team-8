package com.example.myapplication.services;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.dto.CreateEventRequest;
import com.example.myapplication.domain.dto.EventCardResponse;
import com.example.myapplication.domain.dto.EventInfoResponse;
import com.example.myapplication.domain.dto.EventUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventAPIService {

    @POST("events")
    Call<ApiResponse> createEvent(@Body CreateEventRequest request);
    @GET("events/{id}")
    Call<EventInfoResponse> getEventById(@Path("id") String eventId);

    @GET("events/top5")
    Call<List<EventCardResponse>> getTop5Events();
    @PUT("events/update")
    Call<String> updateEvent(@Body EventUpdateRequest eventUpdateRequest);

    @DELETE("events/delete/{id}")
    Call<String> deleteEvent(@Path("id") String id);
}
