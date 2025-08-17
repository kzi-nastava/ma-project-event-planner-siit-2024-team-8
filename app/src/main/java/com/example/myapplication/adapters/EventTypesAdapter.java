package com.example.myapplication.adapters;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.EventType;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventTypesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EventType> eventTypes;

    private List<EventType> currentTypes;
    private OnNewEventTypeListener newEventTypeListener;

    private OnAssetCategoryRequestListener assetCategoryRequestListener;

    private Dialog dialog;

    public EventTypesAdapter(List<EventType> eventTypes,
                             OnNewEventTypeListener listener,
                             Dialog dialog,
                             OnAssetCategoryRequestListener assetCategoryRequestListener){
        this.eventTypes = eventTypes;
        this.newEventTypeListener = listener;
        this.dialog = dialog;
        this.assetCategoryRequestListener = assetCategoryRequestListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inputView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_type_card, parent, false);
        return new EventTypesAdapter.EventTypeViewHolder(inputView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((EventTypesAdapter.EventTypeViewHolder) holder).bind(currentTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return currentTypes != null ? currentTypes.size() : 0;
    }

    public void updateData(boolean activeEventTypes,List<EventType> eventTypes) {
        currentTypes = new ArrayList<>();
        for (EventType eventType : eventTypes){
            if (!(eventType.getActive() == activeEventTypes)){
                currentTypes.removeIf(eT -> eT.getId() == eventType.getId());
            }else{
                Optional<EventType> event = currentTypes.stream()
                        .filter(et -> et.getId().equals(eventType.getId()))
                        .findFirst();
                if(event.isEmpty()){
                    currentTypes.add(eventType);
                }
            }
        }
    }

    public void updateData(List<EventType> eventTypes){
        this.eventTypes = eventTypes;
        this.currentTypes = new ArrayList<>();
        this.currentTypes.addAll(eventTypes);
        notifyDataSetChanged();
    }


    public class EventTypeViewHolder extends RecyclerView.ViewHolder {

        private final TextView name,description,getIdTextView;

        private final EditText descriptionEditText;

        private final TextView nameTextView,idTextView;
        private final MaterialCardView materialCardView;
        private final TextInputLayout nameLayout;
        private List<AssetCategory> selected;

        boolean active;

        private Button activationButton;
        public EventTypeViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.eventTypeTitle);
            description = view.findViewById(R.id.eventTypeDescriptionTextView);
            getIdTextView = view.findViewById(R.id.eventTypeId);
            materialCardView = view.findViewById(R.id.eventTypeMaterialCard);
            descriptionEditText = dialog.findViewById(R.id.eventTypeDescriptionEditText);
            nameTextView = dialog.findViewById(R.id.eventTypeTextView);
            nameLayout = dialog.findViewById(R.id.nameLayout);
            idTextView = dialog.findViewById(R.id.eventTypeId);
            activationButton = dialog.findViewById(R.id.activationButton);

            materialCardView.setOnClickListener(v -> openDialog());
        }

        private void openDialog(){
            Optional<EventType> optionalEventType = currentTypes.stream()
                    .filter(et -> et.getId().equals(UUID.fromString(getIdTextView.getText().toString())))
                    .findFirst();
            EventType eventType = optionalEventType.orElseThrow();

            //setting button to Activate/Deactivate
            if(eventType.getActive()){
                activationButton.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.deactivate_color));
                activationButton.setText("Deactivate");
            }else{
                activationButton.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.olive));
                activationButton.setText("Activate");
            }
            activationButton.setVisibility(View.VISIBLE);

            dialog.show();
            assetCategoryRequestListener.onAssetCategoryRequested(selected);
            nameTextView.setText(name.getText());
            nameTextView.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.GONE);
            descriptionEditText.setText(description.getText());
            idTextView.setText(getIdTextView.getText());
        }

        public void bind(EventType eventType) {
            selected = eventType.getAssetCategories();
            active = eventType.getActive();
            name.setText(eventType.getName());
            description.setText(eventType.getDescription());
            getIdTextView.setText(eventType.getId().toString());
        }
    }

    public interface OnNewEventTypeListener {
        void onCreateNewEventType();
    }

    public interface OnAssetCategoryRequestListener{
        void onAssetCategoryRequested(List<AssetCategory> selected);
    }
}
