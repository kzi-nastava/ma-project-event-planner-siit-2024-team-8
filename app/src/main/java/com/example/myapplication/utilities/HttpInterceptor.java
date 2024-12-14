package com.example.myapplication.utilities;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = JwtTokenUtil.getToken();
        Request request = chain.request();
        if (token!=null){
            Request intercepted = request.newBuilder()
                    .header("Authorization","Bearer " + JwtTokenUtil.getToken())
                    .build();
            return chain.proceed(intercepted);
        }else{
            return chain.proceed(request);
        }
    }
}
