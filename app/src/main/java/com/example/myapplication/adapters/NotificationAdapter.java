package com.example.myapplication.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.Notification;
import com.example.myapplication.domain.dto.NotificationResponse;
import com.example.myapplication.services.NotificationService;
import com.google.android.material.card.MaterialCardView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    NotificationService.NotificationListener listener = null;

    List<NotificationResponse> notifications = new ArrayList<>();
    public NotificationAdapter(NotificationService.NotificationListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item ,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationResponse item = notifications.get(position);
        holder.titleTV.setText(item.getTitle());
        //holder.timestampTV.setText(getRelativeTime(item.getTimestamp()));
        holder.messageTV.setText(item.getBody());

        if (!item.isSeen()) {
            holder.container.setBackgroundColor(Color.parseColor("#EAF6FF"));
            holder.container.setBackgroundResource(R.drawable.unseen_background);
            holder.unseenDot.setVisibility(View.VISIBLE);
        } else {
            holder.container.setBackgroundColor(Color.WHITE);
            holder.unseenDot.setVisibility(View.GONE);
        }

        holder.notification.setOnClickListener(v -> {
            listener.onNotificationClicked(item);
            if(!item.isSeen()){
                item.setSeen(true);
                notifyDataSetChanged();
            }
        });
    }
    private String getRelativeTime(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "just now";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + "m ago";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + "h ago";
        } else if (seconds < 604800) {
            long days = seconds / 86400;
            return days + "d ago";
        } else {
            // Show actual date if more than a week ago
            return dateTime.toLocalDate().toString(); // or use formatter
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setData(List<NotificationResponse> notifications){
        notifications.sort((n1, n2) ->
                n2.getTimestamp().compareTo(n1.getTimestamp()));
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public void markAllRead() {
        notifications.forEach(notification -> notification.setSeen(true));
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTV,messageTV,timestampTV;

        LinearLayout container;

        View unseenDot;

        MaterialCardView notification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notificationCard);
            unseenDot = itemView.findViewById(R.id.unseenDot);
            container = itemView.findViewById(R.id.container);
            titleTV = itemView.findViewById(R.id.tvTitle);
            messageTV = itemView.findViewById(R.id.tvBody);
        }
    }
}
