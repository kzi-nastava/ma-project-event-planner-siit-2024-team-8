package com.example.myapplication.services;

import com.example.myapplication.utilities.HttpInterceptor;
import com.example.myapplication.utilities.RetrofitClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ClientUtils {

    public static OkHttpClient getClient(){
        HttpInterceptor interceptor = new HttpInterceptor();

        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
    }
    public static UserAPIService userService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);
    public static EventTypeAPIService eventTypeService = RetrofitClient.getRetrofitInstance().create(EventTypeAPIService.class);
    public static LoginService loginService = RetrofitClient.getRetrofitInstance().create(LoginService.class);

    public static EventAPIService eventAPIService = RetrofitClient.getRetrofitInstance().create(EventAPIService.class);
}