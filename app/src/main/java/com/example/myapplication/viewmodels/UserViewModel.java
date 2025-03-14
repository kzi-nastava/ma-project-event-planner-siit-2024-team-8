package com.example.myapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.dto.UserCreateRequest;
import com.example.myapplication.services.UserService;

import java.io.File;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<UserCreateRequest> userLiveData = new MutableLiveData<>();

    private UserService userService;

    public UserViewModel() {
        // Initialize with an empty user
        userLiveData.setValue(new UserCreateRequest());
        userService = new UserService();
    }

    public LiveData<UserCreateRequest> getUser() {
        return userLiveData;
    }

    public void onUserTypeChanged(){
    }

    public void saveUserData(File imageFile) {
        userService.createUser(userLiveData.getValue(),imageFile);
        userLiveData.setValue(new UserCreateRequest());
    }

    private boolean isValidUser(UserCreateRequest userCreateRequest){
        return (userCreateRequest !=null && !userCreateRequest.getFirstName().isEmpty() && !userCreateRequest.getLastName().isEmpty() && !userCreateRequest.getEmail().isEmpty()
        && !userCreateRequest.getPassword().isEmpty() && !userCreateRequest.getNumber().isEmpty() && !userCreateRequest.getAddress().isEmpty());
    }
    private boolean isValidProvider(UserCreateRequest userCreateRequest){
        return isValidUser(userCreateRequest) && !userCreateRequest.getCompanyName().isEmpty() && !userCreateRequest.getCompanyDescription().isEmpty() && !userCreateRequest.getCompanyImagesURL().isEmpty();
    }

}
