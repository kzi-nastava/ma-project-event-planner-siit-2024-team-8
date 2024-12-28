package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.Activity;
import com.example.myapplication.domain.BudgetItem;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View types constants
    public static final int VIEW_TYPE_NORMAL = 0; // For normal activity card
    public static final int VIEW_TYPE_INPUT = 1;  // For input form (editing/creating activity)
    public static final int VIEW_TYPE_CREATE_NEW = 2; // For the "Create New Activity" card

    private List<Activity> activitiesList;
    private OnNewActivityListener newActivityListener;

    // Constructor
    public ActivitiesAdapter(List<Activity> activitiesList, OnNewActivityListener newActivityListener) {
        this.activitiesList = activitiesList;
        this.newActivityListener = newActivityListener;
        // Add "Create New Activity" as the last item
        this.activitiesList.add(new Activity("Create New Activity", "", "", "", ""));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == activitiesList.size() - 1) {
            return VIEW_TYPE_CREATE_NEW;
        }

        Activity activity = activitiesList.get(position);
        if (activity.isInput()) {
            return VIEW_TYPE_INPUT;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_CREATE_NEW:
                View createNewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_activity_card, parent, false);
                return new CreateNewBudgetItemViewHolder(createNewView);

            case VIEW_TYPE_INPUT:
                View inputView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_activity_extended, parent, false);
                return new InputActivityViewHolder(inputView);

            case VIEW_TYPE_NORMAL:
            default:
                View normalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_activity_card, parent, false);
                return new ActivityViewHolder(normalView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CreateNewBudgetItemViewHolder) {
            ((CreateNewBudgetItemViewHolder) holder).bind();
        } else if (holder instanceof InputActivityViewHolder) {
            ((InputActivityViewHolder) holder).bind(activitiesList.get(position));
        } else if (holder instanceof ActivityViewHolder) {
            Activity activity = activitiesList.get(position);
            ((ActivityViewHolder) holder).bind(activity);
        }
    }

    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    // ViewHolder for "Create New Activity" card
    public class CreateNewBudgetItemViewHolder extends RecyclerView.ViewHolder {

        public CreateNewBudgetItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                newActivityListener.onCreateNewActivity();
            });
        }

        public void bind() {
        }
    }

    public void removeActivity(Activity activity){
        int position = activitiesList.indexOf(activity);
        if (position != -1) {
            activitiesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    // ViewHolder for input form (editing/creating activity)
    public class InputActivityViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText titleEditText, locationEditText, descriptionEditText, startTimeEditText,endTimeEditText;
        private final TimePicker startTimePicker,endTimePicker;

        private  Activity activity;

        public InputActivityViewHolder(View itemView) {
            super(itemView);
            titleEditText = itemView.findViewById(R.id.activityTitleEditText);
            locationEditText = itemView.findViewById(R.id.activityLocationEditText);
            descriptionEditText = itemView.findViewById(R.id.activityDescriptionEditText);
            startTimeEditText = itemView.findViewById(R.id.activityStartTimeEditText);
            endTimeEditText = itemView.findViewById(R.id.activityEndTimeEditText);


            startTimePicker = itemView.findViewById(R.id.startTimePicker);
            endTimePicker = itemView.findViewById(R.id.endTimePicker);
            Button saveButton = itemView.findViewById(R.id.saveActivityButton);
            saveButton.setOnClickListener(v -> {

                saveActivity();
            });

            Button deleteButton = itemView.findViewById(R.id.deleteActivityButton);
            deleteButton.setOnClickListener(v -> onRemoveClicked());

            startTimeEditText.setOnClickListener(v -> {
                onEditStartTimeClicked();
            });

            endTimeEditText.setOnClickListener(v -> {
                onEditEndTimeClicked();
            });

            descriptionEditText.setOnClickListener(v -> {
                endTimePicker.setVisibility(View.GONE);
            });
        }

        private void onRemoveClicked() {
            removeActivity(this.activity);
        }

        public void onEditStartTimeClicked(){
            startTimePicker.setIs24HourView(true);
            startTimePicker.setVisibility(View.VISIBLE);
            startTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                startTimeEditText.setText(time);
            });
        }

        public void onEditEndTimeClicked(){
            startTimePicker.setVisibility(View.GONE);

            endTimePicker.setIs24HourView(true);
            endTimePicker.setVisibility(View.VISIBLE);
            endTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                endTimeEditText.setText(time);
            });
        }

        public void bind(Activity activity) {
            this.activity = activity;
            titleEditText.setText(activity.getName());
            locationEditText.setText(activity.getLocation());
            descriptionEditText.setText(activity.getDescription());
            startTimeEditText.setText(activity.getStartTime());
            endTimeEditText.setText(activity.getEndTime());
        }

        private void saveActivity() {
            // Get values from the input fields
            String title = titleEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String time = startTimeEditText.getText().toString();
            String endTime = endTimeEditText.getText().toString();

            int position = getAdapterPosition();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            String start = LocalTime.parse(time).format(formatter);
            String end = LocalTime.parse(endTime).format(formatter);

            Activity updatedActivity = new Activity(title, location, description, start,end, false);

            activitiesList.set(position, updatedActivity);

            notifyItemChanged(position);
        }
    }

    // ViewHolder for normal activity card
    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        private final TextView activityTitle, activityLocation, activityTime,activityDescription;

        private final MaterialCardView activity;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            activityTitle = itemView.findViewById(R.id.activityTitle);
            activityLocation = itemView.findViewById(R.id.activityLocationTextView);
            activityTime = itemView.findViewById(R.id.timeTextView);
            activityDescription = itemView.findViewById(R.id.activityDescriptionTextView);
            activity = itemView.findViewById(R.id.activityMaterialCard);

            activity.setOnClickListener(v -> {
                onActivityClicked();
            });

        }

        public void bind(Activity activity) {
            activityTitle.setText(activity.getName());
            activityLocation.setText(activity.getLocation());
            activityTime.setText(activity.getTimeString());
            activityDescription.setText(activity.getDescription());
        }

        public void onActivityClicked(){
            String title = activityTitle.getText().toString();
            String location = activityLocation.getText().toString();
            String time = activityTime.getText().toString();
            String startTime = time.split("-")[0];
            String endTime = time.split("-")[1];
            String description = activityDescription.getText().toString();

            int position = getAdapterPosition();

            Activity updatedActivity = new Activity(title, location, description, "","", true);

            activitiesList.set(position, updatedActivity);

            notifyItemChanged(position);
        }
    }

    // Interface for handling "Create New Activity" card clicks
    public interface OnNewActivityListener {
        void onCreateNewActivity();
    }
}
