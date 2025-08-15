package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.dto.event.EventCardResponse;
import com.example.myapplication.domain.dto.user.AssetResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class AssetButtonAdapter extends RecyclerView.Adapter<AssetButtonAdapter.ViewHolder> {

    private List<AssetResponse> assets = new ArrayList<>();
    private OnAssetClickListener listener = null;

    public AssetButtonAdapter(OnAssetClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public AssetButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_button_item ,parent, false);
        return new AssetButtonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetButtonAdapter.ViewHolder holder, int position) {
        AssetResponse response = assets.get(holder.getAdapterPosition());
        holder.assetButton.setText(response.getName());
        holder.assetButton.setOnClickListener(v -> {
            listener.onAssetClick(response);
        });
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    public void updateData(List<AssetResponse> assetResponses) {
        this.assets = assetResponses;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialButton assetButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assetButton = itemView.findViewById(R.id.eventButton);
        }
    }

    public interface OnAssetClickListener{
        void onAssetClick(AssetResponse assetResponse);
    }
}
