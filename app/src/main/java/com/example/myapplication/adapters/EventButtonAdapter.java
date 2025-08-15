package com.example.myapplication.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.dto.event.EventCardResponse;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventButtonAdapter extends RecyclerView.Adapter<EventButtonAdapter.ViewHolder> {

    private List<EventCardResponse> events = new ArrayList<>();
    private EventCardAdapter.OnItemClickListener listener = null;

    private int markedPos = -1;

    public EventButtonAdapter(EventCardAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_button_item ,parent, false);
        return new EventButtonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventButtonAdapter.ViewHolder holder,
                                 int position) {
        EventCardResponse item = events.get(holder.getAdapterPosition());
        holder.eventButton.setText(item.getName());
        if (position != this.markedPos){
            holder.eventButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.olive));
        }
        holder.eventButton.setOnClickListener(v -> {
            holder.eventButton.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black90));
            this.markedPos = holder.getAdapterPosition();
            notifyDataSetChanged();
            listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void updateData(List<EventInfoResponse> eventInfoResponses) {
        events = eventInfoResponses.stream().map(EventCardResponse::new).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialButton eventButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventButton = itemView.findViewById(R.id.eventButton);
        }
    }
}
