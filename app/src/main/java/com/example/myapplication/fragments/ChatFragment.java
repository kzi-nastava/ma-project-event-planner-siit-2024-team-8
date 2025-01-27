package com.example.myapplication.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class ChatFragment extends Fragment {

    private LinearLayout messagesLayout;
    private EditText messageInput;
    private Button sendMessageButton;

    public ChatFragment() {
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        messagesLayout = view.findViewById(R.id.messagesLayout);
        messageInput = view.findViewById(R.id.messageInput);
        sendMessageButton = view.findViewById(R.id.sendMessageButton);

        sendMessageButton.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // User's message
            TextView userMessage = new TextView(getContext());
            userMessage.setText(messageText);
            userMessage.setBackgroundResource(R.drawable.rounded_message);
            userMessage.setPadding(10, 10, 10, 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            userMessage.setLayoutParams(params);
            userMessage.setGravity(Gravity.END);

            messagesLayout.addView(userMessage);
            messageInput.setText("");

            scrollToBottom();

            simulateProviderResponse();
        }
    }

    private void simulateProviderResponse() {
        new Handler().postDelayed(() -> {
            // Provider's message
            TextView providerMessage = new TextView(getContext());
            providerMessage.setText("This is a response from the provider.");
            providerMessage.setBackgroundResource(R.drawable.rounded_message_other);
            providerMessage.setPadding(10, 10, 10, 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            providerMessage.setLayoutParams(params);
            providerMessage.setGravity(Gravity.START);

            messagesLayout.addView(providerMessage);

            scrollToBottom();
        }, 1000);
    }

    private void scrollToBottom() {
        ScrollView scrollView = (ScrollView) messagesLayout.getParent();
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}