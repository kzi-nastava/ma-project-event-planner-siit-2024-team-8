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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.domain.EventDTO;
import com.example.myapplication.fragments.EventInfoFragment;

import java.util.ArrayList;

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.ViewHolder> {

    private ArrayList<EventDTO> _eventCards;

    private Context context;

    public Context getContext() {
        return context;
    }

    public EventCardAdapter(Context context) {
        this.context = context;
    }

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void SetOnClick(Activity activity, FragmentManager manager){
        this.setItemClickListener(event -> {
            Log.d("OfferingsFragment", "Clicked on asset: " + event.getName());
            EventInfoFragment eventInfoFragment = new EventInfoFragment();
            if (activity != null) {
                manager.beginTransaction()
                        .replace(R.id.main, eventInfoFragment)
                        .addToBackStack(null)  // Add to backstack so you can go back
                        .commit();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName;
        private TextView txtDate;
        private ImageView imageViewEvent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.nameTextView);
            txtDate = itemView.findViewById(R.id.secondTextView);
            imageViewEvent = itemView.findViewById(R.id.imageViewOffering);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_event_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(_eventCards.get(position).getName());
        holder.txtDate.setText(_eventCards.get(position).getStartDateString());
        Glide.with(context)
                .asBitmap()
                .load(_eventCards.get(position).getImageURL())
                .into(holder.imageViewEvent);
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(_eventCards.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return _eventCards.size();
    }

    public void set_eventCards(ArrayList<EventDTO> _eventCards) {
        this._eventCards = _eventCards;
    }

    public interface OnItemClickListener {
        void onItemClick(EventDTO event);
    }

    /*
    public void SetOnClick(Activity activity, FragmentManager manager){
        this.setItemClickListener(asset -> {
            Log.d("OfferingsFragment", "Clicked on asset: " + asset.getName());
            EventInfoFragment eventInfoFragment = new EventInfoFragment();
            if (activity != null) {
                manager.beginTransaction()
                        .replace(R.id.fragment_layout, eventInfoFragment)
                        .addToBackStack(null)  // Add to backstack so you can go back
                        .commit();
            }
        });
    }
     */
}
