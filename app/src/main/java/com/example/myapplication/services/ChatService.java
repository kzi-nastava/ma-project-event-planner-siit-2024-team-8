package com.example.myapplication.services;


import android.annotation.SuppressLint;
import android.util.Log;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.domain.Message;

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
import ua.naiksoftware.stomp.dto.StompMessage;

public class ChatService {

    private StompClient stompClient;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String TAG = "ChatService";

    @SuppressLint("CheckResult")
    public void connect(String jwtToken, MessageListener listener) {

        String url = "ws://" + BuildConfig.IP_ADDR + ":8080/chat?token=" + jwtToken;

        stompClient = Stomp.over(Stomp.ConnectionProvider.JWS, url);

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("token", jwtToken));

        stompClient.lifecycle()
                .subscribe(lifecycleEvent -> {
                    LifecycleEvent.Type type = lifecycleEvent.getType();
                    switch (type) {
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

    private void subscribeToUserQueue(MessageListener listener) {
        Disposable disp = stompClient.topic("/user/queue/messages")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    String json = stompMessage.getPayload();
                    Message message = Message.fromJson(json);
                    listener.onMessageReceived(message);
                }, throwable -> Log.e(TAG, "Subscribe error", throwable));

        compositeDisposable.add(disp);
    }

    @SuppressLint("CheckResult")
    public void sendMessage(Message message) {
        if (stompClient != null && stompClient.isConnected()) {
            String json = message.toJson();
            stompClient.send("/app/sendMessage", json).subscribe(() -> {
                Log.d(TAG, "Message sent");
            }, throwable -> {
                Log.e(TAG, "Error sending message", throwable);
            });
        }
    }

    public void disconnect() {
        compositeDisposable.dispose();
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }

    public interface MessageListener {
        void onMessageReceived(Message message);
    }
}