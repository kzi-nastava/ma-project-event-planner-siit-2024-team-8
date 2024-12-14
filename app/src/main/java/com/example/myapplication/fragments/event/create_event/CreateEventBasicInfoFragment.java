package com.example.myapplication.fragments.event.create_event;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCreateEventFirstStepBinding;
import com.example.myapplication.databinding.FragmentCreateEventStepTwoBinding;
import com.example.myapplication.viewmodels.EventViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventBasicInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventBasicInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCreateEventFirstStepBinding binding;

    private EventViewModel eventViewModel;

    public CreateEventBasicInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventFirstStepFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventBasicInfoFragment newInstance(String param1, String param2) {
        CreateEventBasicInfoFragment fragment = new CreateEventBasicInfoFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateEventFirstStepBinding.inflate(inflater, container, false);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        Button next = binding.createEventNextButton;

        binding.setEventVM(eventViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        next.setOnClickListener( v -> {
            nextButtonClicked();
        });

        return binding.getRoot();
    }
    private void nextButtonClicked() {
        CreateEventFragment parentFragment = (CreateEventFragment) getParentFragment();
        Fragment fragment;
        if (eventViewModel.getEvent().getValue().isPrivate()){
            fragment = new CreateEventInvitationsFragment();
        }
        else{
            fragment = new CreateEventLocationFragment();
            parentFragment.changeTitle(2);
            parentFragment.animateProgressBar(33);
        }
        assert parentFragment != null;
        parentFragment.getChildFragmentManager().beginTransaction()
                .setCustomAnimations( R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right )
                .replace(R.id.createEventLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
}