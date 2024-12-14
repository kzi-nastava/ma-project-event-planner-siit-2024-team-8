package com.example.myapplication.fragments.event.create_event;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ActivitiesAdapter;
import com.example.myapplication.databinding.FragmentCreateEventAgendaBinding;
import com.example.myapplication.databinding.FragmentCreateEventInvitationsBinding;
import com.example.myapplication.domain.Activity;
import com.example.myapplication.viewmodels.EventViewModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreateEventAgendaFragment extends Fragment implements ActivitiesAdapter.OnNewActivityListener {

    private RecyclerView recyclerView;
    private ActivitiesAdapter activitiesAdapter;
    private List<Activity> activityList;
    private FragmentCreateEventAgendaBinding binding;
    private EventViewModel eventViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateEventAgendaBinding.inflate(inflater, container, false);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // Recycler view for activities
        recyclerView = binding.activitiesRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        activityList = eventViewModel.getActivities();
        activitiesAdapter = new ActivitiesAdapter(activityList,this);
        recyclerView.setAdapter(activitiesAdapter);

        Button next = binding.createEventNextButton3;
        next.setOnClickListener(v -> {
            onClickedNext();
        });

        return binding.getRoot();
    }

    public void onClickedNext(){
        eventViewModel.getEvent().getValue().setActivities(activityList);
        Toast.makeText(getContext(), "Succesfully created an Event!", Toast.LENGTH_SHORT).show();
    }


    //When new activity is clicked we add new card for input
    @Override
    public void onCreateNewActivity() {
        Activity inputActivity = new Activity("", "", "", LocalTime.now(),LocalTime.now(), true);
        activityList.add(activityList.size() - 1, inputActivity);
        activitiesAdapter.notifyItemInserted(activityList.size() - 2);
    }
}