package com.example.myapplication.services;

import com.example.myapplication.domain.AuthResponse;
import com.example.myapplication.domain.dto.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LoginService {
    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @GET("login/expired")
    Call<Boolean> isExpired (@Query("token") String token);
}
