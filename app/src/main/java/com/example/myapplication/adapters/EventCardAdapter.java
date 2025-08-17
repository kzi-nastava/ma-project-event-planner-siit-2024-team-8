package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.domain.dto.event.EventCardResponse;
import com.example.myapplication.fragments.event.event_info.EventInfoFragment;

import java.util.ArrayList;
import java.util.List;

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.ViewHolder> {

    private List<EventCardResponse> events = new ArrayList<>();

    //TODO MAKE THIS HARDCODING NIGHTMARE END
    public String eventId = "b6183bde-11f7-4052-aa16-15150003f2bd";

    private Context context;

    public Context getContext() {
        return context;
    }

    public EventCardAdapter(Context context) {
        this.context = context;
    }

    private OnItemClickListener itemClickListener;

    private boolean isFragmentTransactionInProgress = false;

    public void setItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void SetOnClick(MainActivity activity, FragmentManager manager){
        this.setItemClickListener(event -> {
            if (isFragmentTransactionInProgress) return;

            isFragmentTransactionInProgress = true;

            EventInfoFragment eventInfoFragment = EventInfoFragment.newInstance(event.getId().toString()); // Pass eventId to the fragment
            if (activity != null) {
                manager.beginTransaction()
                        .replace(R.id.main, eventInfoFragment)
                        .addToBackStack(null)  // Add to backstack so you can go back
                        .commit();
            }
            assert activity != null;
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName;
        private TextView txtDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.nameTextView);
            txtDate = itemView.findViewById(R.id.secondTextView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_home_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(events.get(position).getName());
        holder.txtDate.setText(events.get(position).getStartDate());
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(events.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<EventCardResponse> _eventCards) {
        this.events = _eventCards;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(EventCardResponse event);
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
