package com.example.myapplication.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.auth0.android.jwt.JWT;
import com.example.myapplication.domain.enumerations.Role;
import com.example.myapplication.services.ClientUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JwtTokenUtil {

    private static SharedPreferences sharedPreferences;
    public Context context;
    private Role role;
    public static void saveToken(String jwtToken, Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );

        sharedPreferences.edit().putString("JWT_TOKEN", jwtToken).apply();
    }

    public static void saveUserId(String id, Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        sharedPreferences.edit().putString("ID", id).apply();
    }

    public static void setSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        isExpired();

    }

    private static void isExpired() {
        if (getToken() == null) {return;}
        ClientUtils.loginService.isExpired(getToken()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (Boolean.TRUE.equals(response.body())){
                    removeToken();
                    removeID();
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public static String getToken(){
        if (sharedPreferences != null){
            return sharedPreferences.getString("JWT_TOKEN", null);
        }else{
            return null;
        }
    }

    public static String getUserId(){
        if (sharedPreferences != null){
            return sharedPreferences.getString("ID", null);
        }else{
            return null;
        }
    }

    public static void logOut(){
        removeToken();
        removeID();
    }

    public static void removeToken(){
        sharedPreferences.edit().remove("JWT_TOKEN").apply();
    }
    public static void removeID(){
        sharedPreferences.edit().remove("ID").apply();
    }
    public static Role getRole(){
        String jwtToken = sharedPreferences.getString("JWT_TOKEN",null);
        if (jwtToken == null) return null;
        JWT token = new JWT(jwtToken);
        return Role.valueOf(token.getClaim("role").asString().toUpperCase());
    }

    public static boolean isUserLoggedIn() {
        try {
            if (getToken() == null) {
                return false;
            }

            String jwtToken = getToken();
            if (jwtToken != null) {
                JWT token = new JWT(jwtToken);
                return !token.isExpired(60*60*10);
            }
            return false;
        } catch (Exception e) {
            Log.e("JwtTokenUtil", "Error checking login state", e);
            return false;
        }
    }
}
