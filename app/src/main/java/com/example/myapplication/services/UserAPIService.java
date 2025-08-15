package com.example.myapplication.services;

import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.example.myapplication.domain.dto.user.BlockedUserResponse;
import com.example.myapplication.domain.dto.user.ProviderInfoResponse;
import com.example.myapplication.domain.dto.user.UserInfoResponse;

import java.util.List;
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

    @GET("users/blocked/{id}")
    Call<List<BlockedUserResponse>> getBlockedUsers(@Path("id") String id);

    @GET("users/fetch-favs/{userId}")
    Call<List<EventInfoResponse>> fetchFavoriteEvents(@Path("userId") String userId);
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

    @PUT("users/blocked/{id}")
    Call<ApiResponse> updateBlockedUsers(
            @Path("id") String id,
            @Body List<String> blockedUsers
    );

    @Multipart
    @POST("providers/register/mobile")
    Call<ApiResponse> registerProvider(
            @Part("user") RequestBody user,
            @Part MultipartBody.Part image,
            @Part List<MultipartBody.Part> companyImages
    );

    @POST("users/is-favorite/{userId}")
    Call<Boolean> checkFavorite(@Path("userId") String userId,
                                @Body RequestBody eventId);

    @PUT("users/favorite/{userId}")
    Call<String> favoriteEvent(@Path("userId") String userId,
                               @Body RequestBody eventId);

    @PUT("users/unfavorite/{userId}")
    Call<String> unfavoriteEvent(@Path("userId") String userId,
                                 @Body RequestBody eventId);

}