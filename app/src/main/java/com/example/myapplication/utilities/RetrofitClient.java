package com.example.myapplication.utilities;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.services.ClientUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://" + BuildConfig.IP_ADDR + ":8080/api/";
//BuildConfig.IP_ADDR
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Gson for JSON parsing
                    .client(ClientUtils.getClient())
                    .build();
        }
        return retrofit;
    }
}
