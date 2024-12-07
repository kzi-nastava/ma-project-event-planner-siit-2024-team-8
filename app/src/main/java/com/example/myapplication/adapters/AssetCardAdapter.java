package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import com.example.myapplication.domain.AssetDTO;
import com.example.myapplication.fragments.AssetInfoFragment;

import java.util.ArrayList;

public class AssetCardAdapter extends RecyclerView.Adapter<AssetCardAdapter.ViewHolder> {

    private ArrayList<AssetDTO> assets;
    private Context context;
    private OnItemClickListener itemClickListener;

    public AssetCardAdapter(Context context) {
        this.context = context;
    }

    public void setAssets(ArrayList<AssetDTO> assets) {
        this.assets = assets;
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_event_card, parent, false);
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

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(assets.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    public interface OnItemClickListener {
        void onItemClick(AssetDTO asset);
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

    public void SetOnClick(Activity activity, FragmentManager manager){
        this.setItemClickListener(asset -> {
            Log.d("OfferingsFragment", "Clicked on asset: " + asset.getName());
            AssetInfoFragment fragment = new AssetInfoFragment();
            if (activity != null) {
                manager.beginTransaction()
                        .replace(R.id.main, fragment)
                        .addToBackStack(null)  // Add to backstack so you can go back
                        .commit();
            }
        });
    }

    /*
    public void SetOnClick(Activity activity,FragmentManager manager){
        this.setItemClickListener(asset -> {
            Log.d("OfferingsFragment", "Clicked on asset: " + asset.getName());
            AssetFragment assetFragment = AssetFragment.newInstance(asset.getName(), asset.getType().toString());
            if (activity != null) {
                manager.beginTransaction()
                        .replace(R.id.fragment_layout, assetFragment)
                        .addToBackStack(null)  // Add to backstack so you can go back
                        .commit();
            }
        });
    }
     */
}
