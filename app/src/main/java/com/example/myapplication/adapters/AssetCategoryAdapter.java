package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;

import java.util.List;

public class AssetCategoryAdapter extends RecyclerView.Adapter<AssetCategoryAdapter.ViewHolder> {

    private final List<AssetCategory> categories;
    private final Context context;
    private final OnItemClickListener onItemClickListener;

    public AssetCategoryAdapter(List<AssetCategory> categories, Context context, OnItemClickListener onItemClickListener) {
        this.categories = categories;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.asset_category_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssetCategory category = categories.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryDescription.setText(category.getDescription());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(category)); // Handle click
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView categoryDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryDescription = itemView.findViewById(R.id.categoryDescription);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AssetCategory category);
    }
}

