package com.example.myapplication.services;

import com.example.myapplication.domain.InboxUser;
import com.example.myapplication.domain.Message;
import com.example.myapplication.domain.dto.MarkSeenRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatAPIService {

    @GET("chat/history")
    Call<List<Message>> getChatHistory(@Query("senderId") String senderId, @Query("receiverId") String receiverId);

    @POST("chat/messages/mark-seen")
    Call<Void> markMessagesAsSeen(@Body MarkSeenRequest request);

    @GET("chat/inbox/{userId}")
    Call<List<InboxUser>> getInboxUsers(@Path("userId") String userId);
}
