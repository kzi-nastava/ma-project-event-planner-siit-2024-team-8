package com.example.myapplication.callbacks;

public interface UserRegisterCallBack {
    void onSuccess();
    void onServerError(String errorMessage);
    void onNetworkError(Throwable t);
}
