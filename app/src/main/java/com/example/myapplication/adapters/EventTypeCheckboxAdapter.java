package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.EventType;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventTypeCheckboxAdapter extends RecyclerView.Adapter<EventTypeCheckboxAdapter.EventTypeViewHolder> {
    private List<EventType> eventTypes;
    private OnEventTypeCheckedChangeListener listener;

    private List<EventType> selectedItems = new ArrayList<>();

    public EventTypeCheckboxAdapter(List<EventType> eventTypes, OnEventTypeCheckedChangeListener listener) {
        this.eventTypes = eventTypes;
        this.listener = listener;
    }

    public EventTypeCheckboxAdapter(OnEventTypeCheckedChangeListener listener){
        this.listener = listener;
        this.eventTypes = new ArrayList<>();
    }

    @NonNull
    @Override
    public EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkbox_item, parent, false);
        return new EventTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeViewHolder holder, int position) {
        ((EventTypeViewHolder) holder).bind(eventTypes.get(position));

        EventType currentItem = eventTypes.get(position);
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedItems.contains(currentItem)) {
                    selectedItems.add(currentItem);
                }
            } else {
                selectedItems.removeIf(item -> Objects.equals(item.getId(), currentItem.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventTypes.size();
    }

    public List<EventType> getSelectedItems(){
        return this.selectedItems;
    }

    public void updateData(List<EventType> newData) {
        this.eventTypes.clear();
        this.eventTypes.addAll(newData);
        notifyDataSetChanged();
    }

    // View holder class for event type item
    public static class EventTypeViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox checkbox;
        TextView name;
        public EventTypeViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
            name = itemView.findViewById(R.id.checkboxNameTextView);
        }

        public void bind(EventType eventType) {
            name.setText(eventType.getName());
            checkbox.setChecked(false);
        }
    }

    // Interface to listen for checkbox changes
    public interface OnEventTypeCheckedChangeListener {
        void onEventTypeCheckedChange(EventType eventType, boolean isChecked);
    }
}
