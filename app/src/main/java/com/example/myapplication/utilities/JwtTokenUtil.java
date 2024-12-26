package com.example.myapplication.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.auth0.android.jwt.JWT;
import com.example.myapplication.domain.Role;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class JwtTokenUtil {

    private static SharedPreferences sharedPreferences;
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

    public static String getToken(){
        if (sharedPreferences != null){
            return sharedPreferences.getString("JWT_TOKEN", null);
        }else{
            return null;
        }
    }

    public static void removeToken(){
        sharedPreferences.edit().remove("JWT_TOKEN").apply();
    }
    public static Role getRole(){
        String jwtToken = sharedPreferences.getString("JWT_TOKEN",null);
        assert jwtToken != null;
        JWT token = new JWT(jwtToken);
        return Role.valueOf(token.getClaim("role").asString().toUpperCase());
    }
}
