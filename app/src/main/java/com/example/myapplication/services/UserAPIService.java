package com.example.myapplication.services;

import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.dto.ProviderInfoResponse;
import com.example.myapplication.domain.dto.UpdateUserRequest;
import com.example.myapplication.domain.dto.UserCreateRequest;
import com.example.myapplication.domain.dto.UserInfoResponse;

import java.util.Optional;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserAPIService {
    @Multipart
    @POST("users/register/mobile")
    Call<ApiResponse> registerUser(
            @Part("user") RequestBody user,
            @Part MultipartBody.Part image);

    @GET("users/user")
    Call<UserInfoResponse> getUserInfo(
            @Header("Authorization") String token
    );

    @GET("users/{id}")
    Call<UserInfoResponse> getUserById(@Path("id") String id);

    @GET("users/provider/{id}")
    Call<ProviderInfoResponse> getProviderInfo(@Path("id") UUID id);

    @GET("users/{email}")
    Call<UUID> getUserIdByEmail(@Path("email") String email);
    @Multipart
    @PUT("users/update")
    Call<String> updateUser(
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("email") RequestBody email,
            @Part("address") RequestBody address,
            @Part("number") RequestBody number,
            @Part("companyName") RequestBody companyName,
            @Part("companyDescription") RequestBody companyDescription,
            @Part MultipartBody.Part image // Optional if you are uploading an image
    );

    @PUT("users/block/{id}")
    Call<ApiResponse> blockUser(
            @Path("id") UUID id);

}