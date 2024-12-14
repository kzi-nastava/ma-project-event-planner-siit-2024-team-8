package com.example.myapplication.services;

import com.example.myapplication.domain.AuthResponse;
import com.example.myapplication.domain.dto.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);
}
