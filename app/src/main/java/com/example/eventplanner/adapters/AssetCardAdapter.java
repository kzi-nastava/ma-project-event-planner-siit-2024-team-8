package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.domain.AssetDTO;

import java.util.ArrayList;

public class AssetCardAdapter extends RecyclerView.Adapter<AssetCardAdapter.ViewHolder> {

    private ArrayList<AssetDTO> assets;

    private Context context;

    public ArrayList<AssetDTO> getAssets() {
        return assets;
    }

    public void setAssets(ArrayList<AssetDTO> assets) {
        this.assets = assets;
    }

    public AssetCardAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offering_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(assets.get(position).getName());
        holder.txtAssetType.setText(assets.get(position).getType().toString());
        Glide.with(context)
                .asBitmap()
                .load(assets.get(position).getImageURL())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private TextView txtAssetType;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.nameTextView);
            //for type of asset
            txtAssetType = itemView.findViewById(R.id.secondTextView);
            imageView = itemView.findViewById(R.id.imageViewOffering);
        }


    }
}
