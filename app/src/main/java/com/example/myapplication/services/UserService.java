package com.example.myapplication.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.myapplication.callbacks.UserRegisterCallBack;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.dto.CreateReportRequest;
import com.example.myapplication.domain.dto.user.ProviderInfoResponse;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.domain.dto.user.UserCreateRequest;
import com.example.myapplication.utilities.FileUtils;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.utilities.RetrofitClient;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private final UserAPIService apiService;

    public UserService() {
        apiService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);
    }

    public UserAPIService getApiService() {
        return this.apiService;
    }

    public void createUser(UserCreateRequest user, File file, UserRegisterCallBack callback) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody userRequestBody = RequestBody.create(MediaType.parse("application/json"), json);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        if (user.getUserType() == Role.USER || user.getUserType() == Role.ORGANIZER) {
            ClientUtils.userService.registerUser(userRequestBody, imagePart).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        try {
                            String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown server error";
                            callback.onServerError(errorMessage);
                        } catch (IOException e) {
                            callback.onServerError("Server error occurred.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("RegisterUser", "Network error: ", t);
                    callback.onNetworkError(t);
                }
            });
        }
    }



    public void loadProviderInfo (UUID id,Callback<ProviderInfoResponse> callback){
        apiService.getProviderInfo(id).enqueue(new Callback<ProviderInfoResponse>() {
            @Override
            public void onResponse(Call<ProviderInfoResponse> call, Response<ProviderInfoResponse> response) {
                callback.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<ProviderInfoResponse> call, Throwable t) {
                callback.onFailure(call,t);
            }
        });
    }

    public void blockUser(UUID userId,Callback<ApiResponse> callback){
        apiService.blockUser(userId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                callback.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onFailure(call,t);
            }
        });
    }

    public void reportUser(CreateReportRequest request,Callback<ApiResponse> callback){
        ClientUtils.reportAPIService.createReport(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                callback.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onFailure(call,t);
            }
        });
    }

    public void updateBlockedUsers(List<String> blockedIds,Context context){
        ClientUtils.userService.updateBlockedUsers(JwtTokenUtil.getUserId(),blockedIds).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    NotificationsUtils.getInstance().showSuccessToast(context ,"Updated blocked list succesfully");
                }else{
                    NotificationsUtils.getInstance().showErrToast(context ,"Error in updating blocked list");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public void registerProvider(UserCreateRequest user, File profileImageFile, Callback<ApiResponse> callback, Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody userRequestBody = RequestBody.create(MediaType.parse("application/json"), json);

        RequestBody mainImageRequestBody = RequestBody.create(MediaType.parse("image/*"), profileImageFile);
        MultipartBody.Part mainImagePart = MultipartBody.Part.createFormData("image", profileImageFile.getName(), mainImageRequestBody);

        List<MultipartBody.Part> companyImageParts = FileUtils.convertUrisToMultipart(context,user.getCompanyImagesURL().stream().map(Uri::parse).collect(Collectors.toList()),"companyImages");
        ClientUtils.userService.registerProvider(userRequestBody,mainImagePart,companyImageParts).enqueue(callback);
    }

    public void favoriteEvent(String eventId,Context context){
        RequestBody body = RequestBody.create(
                MediaType.parse("text/plain"), // e.g. "12345"
                eventId
            );
        ClientUtils.userService.favoriteEvent(JwtTokenUtil.getUserId(),body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    NotificationsUtils.getInstance().showSuccessToast(context,response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void unfavoriteEvent(String eventId,Context context){
        RequestBody body = RequestBody.create(
                MediaType.parse("text/plain"),
                eventId
        );
        ClientUtils.userService.unfavoriteEvent(JwtTokenUtil.getUserId(),body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    NotificationsUtils.getInstance().showSuccessToast(context,response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
