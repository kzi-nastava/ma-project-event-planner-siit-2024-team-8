package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ActivitiesAdapter;
import com.example.myapplication.domain.Activity;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventAgendaFragment extends Fragment implements ActivitiesAdapter.OnNewActivityListener {

    private RecyclerView recyclerView;
    private ActivitiesAdapter activitiesAdapter;

    private List<Activity> activityList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event_agenda, container, false);

        // Recycler view for activities
        recyclerView = view.findViewById(R.id.activitiesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        activityList = new ArrayList<>();
        activitiesAdapter = new ActivitiesAdapter(activityList,this);
        recyclerView.setAdapter(activitiesAdapter);

        return view;
    }


    //When new activity is clicked we add new card for input
    @Override
    public void onCreateNewActivity() {
        Activity inputActivity = new Activity("", "", "", LocalTime.now(),LocalTime.now(), true);
        activityList.add(activityList.size() - 1, inputActivity);
        activitiesAdapter.notifyItemInserted(activityList.size() - 2);
    }
}