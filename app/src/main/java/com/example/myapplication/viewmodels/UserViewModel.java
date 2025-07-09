package com.example.myapplication.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.callbacks.UserRegisterCallBack;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.dto.CreateReportRequest;
import com.example.myapplication.domain.dto.ProviderInfoResponse;
import com.example.myapplication.domain.dto.UserCreateRequest;
import com.example.myapplication.domain.dto.UserInfoResponse;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.fragments.ProfileEditFragment;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.services.UserService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NotificationsUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<UserCreateRequest> userLiveData = new MutableLiveData<>();

    private final MutableLiveData<UserInfoResponse> currentUserInfo = new MutableLiveData<>();

    private final MutableLiveData<ProviderInfoResponse> currentProviderInfo = new MutableLiveData<>();

    private UserService userService;

    public UserViewModel() {
        // Initialize with an empty user
        userLiveData.setValue(new UserCreateRequest());
        userService = new UserService();
    }

    public LiveData<UserCreateRequest> getUser() {
        return userLiveData;
    }

    public LiveData<UserInfoResponse> getUserInfo() {return currentUserInfo;}

    public LiveData<ProviderInfoResponse> getProviderInfo(){return currentProviderInfo;}

    public void onUserTypeChanged(){
    }

    public void saveUserData(File imageFile,UserRegisterCallBack callBack) {
        userService.createUser(userLiveData.getValue(), imageFile, new UserRegisterCallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();
            }

            @Override
            public void onServerError(String errorMessage) {
                callBack.onServerError(errorMessage);
            }

            @Override
            public void onNetworkError(Throwable t) {
                callBack.onNetworkError(t);
            }
        });
        userLiveData.setValue(new UserCreateRequest());
    }

    private boolean isValidUser(UserCreateRequest userCreateRequest){
        return (userCreateRequest !=null && !userCreateRequest.getFirstName().isEmpty() && !userCreateRequest.getLastName().isEmpty() && !userCreateRequest.getEmail().isEmpty()
        && !userCreateRequest.getPassword().isEmpty() && !userCreateRequest.getNumber().isEmpty() && !userCreateRequest.getAddress().isEmpty());
    }
    private boolean isValidProvider(UserCreateRequest userCreateRequest){
        return isValidUser(userCreateRequest) && !userCreateRequest.getCompanyName().isEmpty() && !userCreateRequest.getCompanyDescription().isEmpty() && !userCreateRequest.getCompanyImagesURL().isEmpty();
    }

    public void loadUserProfile(UUID userID){
        // Retrieve the token from storage
        String token = JwtTokenUtil.getToken();

        if (token != null && !token.isEmpty() && userID == null) {
            String authHeader = "Bearer " + token;
            userService.getApiService().getUserInfo(authHeader).enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentUserInfo.setValue(response.body());
                    } else {
                        Log.e("Profile", "Failed to load profile: " + response.code());
                        try {
                            // Log the error body to understand why the request failed
                            Log.e("Profile", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e("Profile", "Error reading error body: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                    Log.e("Profile", "Error: " + t.getMessage());
                }
            });
        } else {

            UserService userService = new UserService(); // Create an instance of UserService
            userService.getApiService().getUserById(userID.toString()).enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentUserInfo.setValue(response.body());
                    } else {
                        Log.e("Profile", "Failed to load profile: " + response.code());
                        try {
                            // Log the error body to understand why the request failed
                            Log.e("Profile", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e("Profile", "Error reading error body: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                    Log.e("Profile", "Error: " + t.getMessage());
                }
            });
        }
    }

    public void loadProviderInfo(UUID providerId){
        userService.loadProviderInfo(providerId, new Callback<ProviderInfoResponse>() {
            @Override
            public void onResponse(Call<ProviderInfoResponse> call, Response<ProviderInfoResponse> response) {
                if (response.isSuccessful()){
                    currentProviderInfo.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ProviderInfoResponse> call, Throwable t) {

            }
        });
    }

    public void editUser(RequestBody firstName, RequestBody lastName, RequestBody email, RequestBody address, RequestBody number, RequestBody companyName,RequestBody companyDesc, MultipartBody.Part imagePart, Context context) {
        // Send data to backend
        userService.getApiService().updateUser(firstName, lastName, email, address, number,companyName,companyDesc, imagePart).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    NotificationsUtils.getInstance().showSuccessToast(context,"Successfully updated user!");
                } else {
                    NotificationsUtils.getInstance().showErrToast(context,"Error loading user!");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void blockUser(UUID id,Context context){
        userService.blockUser(id, new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    NotificationsUtils.getInstance().showSuccessToast(context,"Succesully blocked user!");
                }else{
                    NotificationsUtils.getInstance().showErrToast(context,"Something went wrong.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public void reportUser(UUID userID, String reason,Context context) {
        String email = JwtTokenUtil.getRole() == Role.PROVIDER ? Objects.requireNonNull(currentProviderInfo.getValue()).email : Objects.requireNonNull(currentUserInfo.getValue()).email;
        CreateReportRequest request = new CreateReportRequest(reason, email, userID.toString());
        userService.reportUser(request, new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    NotificationsUtils.getInstance().showSuccessToast(context,String.format("Reported %s successfuly",email));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public void suspendUser(String reportId,Context context){
        Call<ApiResponse> call = ClientUtils.reportAPIService.suspendUser(reportId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    NotificationsUtils.getInstance().showSuccessToast(context,"User suspended.");
                }else{
                    NotificationsUtils.getInstance().showErrToast(context,response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
