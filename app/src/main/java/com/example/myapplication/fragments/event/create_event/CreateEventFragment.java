package com.example.myapplication.fragments.event.create_event;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCreateEventBinding;
import com.example.myapplication.databinding.FragmentRegisterBinding;
import com.example.myapplication.domain.Event;
import com.example.myapplication.viewmodels.EventViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private EventViewModel eventViewModel;

    private int steps = 4;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventFragment newInstance(String param1, String param2) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        CreateEventBasicInfoFragment fragment = new CreateEventBasicInfoFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.createEventLayout, fragment)
                .commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCreateEventBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_event, container, false);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        binding.setEventVM(eventViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        ImageButton back = binding.backCreateEventButton;
        back.setOnClickListener( v -> {
            onBackButtonClick();
        });
        return binding.getRoot();
    }

    private void onBackButtonClick() {
        Fragment current = getChildFragmentManager().findFragmentById(R.id.createEventLayout);
        if (current instanceof CreateEventBasicInfoFragment){
            getParentFragmentManager().popBackStack();
        }else if ((current instanceof CreateEventLocationFragment && !eventViewModel.getEvent().getValue().isPrivate())
                    || current instanceof CreateEventInvitationsFragment){
            CreateEventBasicInfoFragment stepOneFragment = new CreateEventBasicInfoFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.createEventLayout, stepOneFragment)
                    .commit();
            animateProgressBar(1);
            changeTitle(1);
        }else if(current instanceof CreateEventLocationFragment && eventViewModel.getCreateEventRequest().getValue().isPrivate()){
            CreateEventInvitationsFragment invitationsFragment = new CreateEventInvitationsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.createEventLayout, invitationsFragment)
                    .commit();
            animateProgressBar(1);
            changeTitle(2);

        }else if (current instanceof CreateEventAgendaFragment){
            CreateEventLocationFragment stepTwoFragment = new CreateEventLocationFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.createEventLayout, stepTwoFragment)
                    .commit();
            animateProgressBar(2);
            changeTitle(2);
        }else if (current instanceof CreateEventBudgetFragment){
            CreateEventAgendaFragment agendaFragment = new CreateEventAgendaFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.createEventLayout,agendaFragment)
                    .commit();
            animateProgressBar(3);
            changeTitle(3);
        }
    }

    public void animateProgressBar(int step) {
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (progressBar != null) {
            int progress = (int) (((float) step / steps) * 100);
            ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, progress);
            progressAnimator.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
            progressAnimator.setDuration(500); // Duration of 2 seconds
            progressAnimator.start();
        }
    }

    public void changeTitle(int step){
        TextView title = getView().findViewById(R.id.eventCreationTitle);
        if (step == 1){
            title.setText("Create New Event");
        }else if (step == 2 && eventViewModel.getCreateEventRequest().getValue().isPrivate()){
            title.setText("Invitations");
        }else if (step == 2 && !eventViewModel.getCreateEventRequest().getValue().isPrivate()) {
            title.setText("Location");
        }
        else if(step == 3){
            title.setText("Agenda");
        }else if (step == 4) {
            title.setText("Budget");
        }
    }
}