package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.ViewHolder> {
    private List<AssetCategory> items;
    private List<AssetCategory> selectedItems = new ArrayList<>();

    public MultiSelectAdapter(List<AssetCategory> items) {
        this.items = items;
    }
    public MultiSelectAdapter(List<AssetCategory> items, List<AssetCategory> selectedItems){
        this.items = items;
        this.selectedItems = selectedItems;
    }

    public void updateData(List<AssetCategory> categories){
        this.items = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AssetCategory currentItem = items.get(position);
        holder.text.setText(currentItem.getName());
        boolean isSelected = selectedItems.stream()
                .anyMatch(category -> category.getId().equals(currentItem.getId()));
        holder.checkBox.setChecked(isSelected);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
        return items.size();
    }

    public List<AssetCategory> getSelectedItems() {
        return selectedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            text = itemView.findViewById(R.id.checkboxNameTextView);
        }
    }
}
