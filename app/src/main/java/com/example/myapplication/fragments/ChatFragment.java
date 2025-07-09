package com.example.myapplication.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.domain.Message;
import com.example.myapplication.domain.dto.MarkSeenRequest;
import com.example.myapplication.domain.dto.UserInfoResponse;
import com.example.myapplication.services.ChatAPIService;
import com.example.myapplication.services.ChatService;
import com.example.myapplication.services.UserAPIService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private LinearLayout messagesLayout;
    private ScrollView scrollView;
    private EditText messageInput;
    private Button sendButton;

    private ChatService chatService;
    private ChatAPIService chatAPIService;

    private String userId;
    private String otherUserId;

    private List<Message> messages = new ArrayList<>();

    UserAPIService userAPIService = RetrofitClient.getRetrofitInstance().create(UserAPIService.class);

    public static ChatFragment newInstance(String otherUserId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("otherUserId", otherUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        messagesLayout = view.findViewById(R.id.messagesLayout);
        scrollView = view.findViewById(R.id.scrollViewMessages);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendMessageButton);
        ImageButton backButton = view.findViewById(R.id.backButton);
        TextView chatTitle = view.findViewById(R.id.chatTitle);
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        chatService = new ChatService();
        chatAPIService = RetrofitClient.getRetrofitInstance().create(ChatAPIService.class);

        userAPIService.getUserById(otherUserId).enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String fullName = response.body().firstName + " " + response.body().lastName;
                    chatTitle.setText(fullName);
                } else {
                    Log.e("ChatFragment", "Failed to get user info: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e("ChatFragment", "Error getting user info", t);
            }
        });

        setupListeners();
        connectWebSocket();
        loadChatHistory();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = JwtTokenUtil.getUserId();
        if (getArguments() != null) {
            otherUserId = getArguments().getString("otherUserId");
        }
    }

    private void setupListeners() {
        sendButton.setOnClickListener(v -> {
            String msgText = messageInput.getText().toString().trim();
            if (!msgText.isEmpty()) {
                Message message = new Message();
                message.setSenderId(userId);
                message.setReceiverId(otherUserId);
                message.setMessageContent(msgText);
                message.setSentAt(java.time.Instant.now().toString());
                message.setDeleted(false);

                chatService.sendMessage(message);
                messageInput.setText("");
            }
        });
    }

    private void connectWebSocket() {
        String token = JwtTokenUtil.getToken();
        chatService.connect(token, message -> {
            if ((message.getSenderId().equals(otherUserId) && message.getReceiverId().equals(userId)) ||
                    (message.getSenderId().equals(userId) && message.getReceiverId().equals(otherUserId))) {

                getActivity().runOnUiThread(() -> {
                    messages.add(message);
                    addMessageToView(message);
                    scrollToBottom();

                    if (message.getSenderId().equals(otherUserId)) {
                        markMessagesAsSeen();
                    }
                });
            }
        });
    }

    private void loadChatHistory() {
        Log.d("ChatFragment", "Loading chat history for: userId=" + userId + ", otherUserId=" + otherUserId);
        chatAPIService.getChatHistory(userId, otherUserId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ChatFragment", "Chat history loaded. Messages count: " + response.body().size());
                    messages.clear();
                    messages.addAll(response.body());
                    getActivity().runOnUiThread(() -> {
                        messagesLayout.removeAllViews();
                        for (Message m : messages) {
                            addMessageToView(m);
                        }
                        scrollToBottom();
                    });
                } else {
                    Log.e("ChatFragment", "Failed to load chat history: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e("ChatFragment", "Failed to load history", t);
            }
        });
    }

    private void markMessagesAsSeen() {
        MarkSeenRequest request = new MarkSeenRequest(userId, otherUserId);
        chatAPIService.markMessagesAsSeen(request).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) Log.e("ChatFragment", "Mark as seen failed");
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ChatFragment", "Mark as seen error", t);
            }
        });
    }

    private void addMessageToView(Message message) {
        LinearLayout wrapper = new LinearLayout(getContext());
        wrapper.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        wrapper.setOrientation(LinearLayout.HORIZONTAL);

        TextView tv = new TextView(getContext());
        tv.setText(message.getMessageContent());
        tv.setTextSize(16);
        int padding = (int) (10 * getResources().getDisplayMetrics().density);
        tv.setPadding(padding, padding, padding, padding);

        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        messageParams.setMargins(20, 10, 20, 10);
        tv.setLayoutParams(messageParams);

        if (message.getSenderId().equals(userId)) {
            tv.setBackgroundResource(R.drawable.rounded_message_right);
            wrapper.setGravity(Gravity.END);
        } else {
            tv.setBackgroundResource(R.drawable.rounded_message_left);
            wrapper.setGravity(Gravity.START);
        }

        wrapper.addView(tv);
        messagesLayout.addView(wrapper);
        scrollToBottom();
    }

    private void scrollToBottom() {
        scrollView.postDelayed(() -> scrollView.fullScroll(View.FOCUS_DOWN), 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        chatService.disconnect();
    }
}