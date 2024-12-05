package com.example.myapplication.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;

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

        CreateEventFirstStepFragment fragment = new CreateEventFirstStepFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.createEventLayout, fragment)
                .commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        ImageButton back = view.findViewById(R.id.backCreateEventButton);
        back.setOnClickListener( v -> {
            onBackButtonClick();
        });

        return view;
    }

    private void onBackButtonClick() {
        Fragment current = getChildFragmentManager().findFragmentById(R.id.createEventLayout);
        if (current instanceof CreateEventFirstStepFragment){
            getParentFragmentManager().popBackStack();
        }else if (current instanceof CreateEventStepTwoFragment){
            CreateEventFirstStepFragment stepOneFragment = new CreateEventFirstStepFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.createEventLayout, stepOneFragment)
                    .commit();
            animateProgressBar(0);
            changeTitle(1);
        }else if (current instanceof CreateEventAgendaFragment){
            CreateEventStepTwoFragment stepTwoFragment = new CreateEventStepTwoFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.createEventLayout, stepTwoFragment)
                    .commit();
            animateProgressBar(33);
            changeTitle(2);
        }
    }

    public void animateProgressBar(int progress) {
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);
        if (progressBar != null) {
            ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, progress);
            progressAnimator.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
            progressAnimator.setDuration(500); // Duration of 2 seconds
            progressAnimator.start();
        }
    }

    public void changeTitle(int step){
        TextView title = getView().findViewById(R.id.eventCreationTitle);
        if (step == 1){
            title.setText("Basic Info");
        }else if (step == 2){
            title.setText("Location");
        }else if(step == 3){
            title.setText("Agenda");
        }
    }
}