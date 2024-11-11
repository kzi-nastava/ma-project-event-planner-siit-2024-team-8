package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.domain.EventDTO;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offering_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(_eventCards.get(position).getName());
        holder.txtDate.setText(_eventCards.get(position).getStringDuration());
        Glide.with(context)
                .asBitmap()
                .load(_eventCards.get(position).getImageURL())
                .into(holder.imageViewEvent);
    }


    @Override
    public int getItemCount() {
        return _eventCards.size();
    }

    public void set_eventCards(ArrayList<EventDTO> _eventCards) {
        this._eventCards = _eventCards;
        notifyDataSetChanged();
    }
}
