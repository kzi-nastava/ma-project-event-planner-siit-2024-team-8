package com.example.myapplication.services;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.domain.Notification;
import com.example.myapplication.domain.dto.NotificationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;

public class NotificationService {

    private StompClient stompClient;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String TAG = "NotificationService";

    @SuppressLint("CheckResult")
    public void connect(String jwtToken, NotificationListener listener) {

        String url = "ws://" + BuildConfig.IP_ADDR + ":8080/chat?token=" + jwtToken;

        stompClient = Stomp.over(Stomp.ConnectionProvider.JWS, url);

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("token", jwtToken));

        stompClient.lifecycle()
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(TAG, "STOMP connection opened");
                            subscribeToUserQueue(listener);
                            break;
                        case ERROR:
                            Log.e(TAG, "STOMP connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.d(TAG, "STOMP connection closed");
                            break;
                    }
                });

        stompClient.connect(headers);
    }

    private void subscribeToUserQueue(NotificationListener listener) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                        return LocalDateTime.parse(json.getAsString());
                    }
                })
                .create();
        Disposable disp = stompClient.topic("/user/queue/notifications")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    String json = stompMessage.getPayload();
                    Notification notification = gson.fromJson(json, Notification.class);
                    listener.onNotificationReceived(notification);
                }, throwable -> Log.e(TAG, "Subscribe error", throwable));

        compositeDisposable.add(disp);
    }

    public void disconnect() {
        compositeDisposable.dispose();
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }

    public interface NotificationListener {
        void onNotificationReceived(Notification notification);
        void onNotificationClicked(NotificationResponse notification);
    }
}
