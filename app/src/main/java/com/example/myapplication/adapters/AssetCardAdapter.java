package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.domain.Asset;
import com.example.myapplication.fragments.asset.AssetInfoFragment;

import java.util.ArrayList;
import java.util.List;


public class AssetCardAdapter extends RecyclerView.Adapter<AssetCardAdapter.ViewHolder> {
    private List<Asset> assets = new ArrayList<>();
    private Context context;
    private OnItemClickListener itemClickListener;

    public AssetCardAdapter(Context context) {
        this.context = context;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void SetOnClick(Activity activity, FragmentManager manager){
        this.setItemClickListener(asset -> {
            AssetInfoFragment assetInfoFragment = new AssetInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("asset_id", asset.getId());
            bundle.putString("asset_type", asset.getType());
            assetInfoFragment.setArguments(bundle);
            if (activity != null) {
                manager.beginTransaction()
                        .replace(R.id.main, assetInfoFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Asset asset = assets.get(position);

        holder.txtName.setText(asset.getName());
        holder.txtAssetType.setText(asset.getType() != null ? asset.getType() : "Unknown");

        String imageUrl = !asset.getImages().isEmpty() ? asset.getImages().get(0) : null;
        if (imageUrl != null) {
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.profile_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(asset);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Asset asset);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtAssetType;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.nameTextView);
            txtAssetType = itemView.findViewById(R.id.secondTextView);
            imageView = itemView.findViewById(R.id.imageViewOffering);
        }
    }
}

