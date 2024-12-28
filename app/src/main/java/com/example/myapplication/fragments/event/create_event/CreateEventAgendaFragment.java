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

        activityList = eventViewModel.getRequestActivities();
        activitiesAdapter = new ActivitiesAdapter(activityList,this);
        recyclerView.setAdapter(activitiesAdapter);

        Button next = binding.createEventNextButton3;
        next.setOnClickListener(v -> {
            onClickedNext();
        });

        return binding.getRoot();
    }

    public void onClickedNext(){
        if (activityList.isEmpty() || activityList.size() < 2){
            Toast.makeText(requireContext(),"You must have at least 1 activity!",Toast.LENGTH_SHORT).show();
            return;
        }
        activityList.remove(activityList.size()-1);
        eventViewModel.getCreateEventRequest().getValue().setAgenda(activityList);

        CreateEventFragment parentFragment = (CreateEventFragment) getParentFragment();
        assert parentFragment != null;
        CreateEventBudgetFragment fragment = new CreateEventBudgetFragment();
        parentFragment.getChildFragmentManager().beginTransaction()
                .setCustomAnimations( R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right )
                .replace(R.id.createEventLayout, fragment)
                .addToBackStack(null)
                .commit();
        parentFragment.changeTitle(4);
        parentFragment.animateProgressBar(3);
    }


    //When new activity is clicked we add new card for input
    @Override
    public void onCreateNewActivity() {
        Activity inputActivity = new Activity("", "", "", "","", true);
        activityList.add(activityList.size() - 1, inputActivity);
        activitiesAdapter.notifyItemInserted(activityList.size() - 2);
    }
}