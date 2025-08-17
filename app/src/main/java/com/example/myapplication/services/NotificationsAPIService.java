package com.example.myapplication.services;

import com.example.myapplication.domain.dto.NotificationResponse;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationsAPIService {

    @POST("notifications/seen/{notificationId}")
    Call<Void> markAsSeen(@Path("notificationId")UUID notificationId);

    @POST("notifications/seen/all/{userId}")
    Call<Void> markAllAsSeen(@Path("userId") String userId);

    @GET("notifications/{userId}")
    Call<List<NotificationResponse>> getNotificationsForUser(@Path("userId") UUID userId);
}
