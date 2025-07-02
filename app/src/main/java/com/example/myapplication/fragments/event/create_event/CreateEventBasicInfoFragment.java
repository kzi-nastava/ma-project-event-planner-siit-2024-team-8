package com.example.myapplication.fragments.event.create_event;

import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCreateEventFirstStepBinding;
import com.example.myapplication.databinding.FragmentCreateEventStepTwoBinding;
import com.example.myapplication.domain.dto.CreateEventRequest;
import com.example.myapplication.viewmodels.EventViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    private TextView dateRangeTextView;

    private LocalDate startDate;

    private LocalDate endDate;

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

        dateRangeTextView = binding.dateRangeTextView;

        // Build the Date Range Picker
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Date Range");

        final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

        // Show the picker
        binding.openDateRangePickerButton.setOnClickListener(v -> picker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER"));

        // Handle selection
        picker.addOnPositiveButtonClickListener(
                selection -> {
                    startDate = Instant.ofEpochMilli(selection.first)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                    endDate = Instant.ofEpochMilli(selection.second)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String startDateString = sdf.format(selection.first);
                    String endDateString = sdf.format(selection.second);
                    dateRangeTextView.setText("Start: " + startDateString + "\nEnd: " + endDateString);
                }
        );


        return binding.getRoot();
    }
    private void nextButtonClicked() {
        if (!isDataValid()){return;}
        CreateEventFragment parentFragment = (CreateEventFragment) getParentFragment();
        Fragment fragment;
        if (eventViewModel.getCreateEventRequest().getValue().isPrivate()){
            fragment = new CreateEventInvitationsFragment();
            parentFragment.changeTitle(2);
        }
        else{
            fragment = new CreateEventLocationFragment();
            parentFragment.changeTitle(2);
            parentFragment.animateProgressBar(1);
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

    public boolean isDataValid(){

        int count = 0 ;
        String dateMessage = "";
        CreateEventRequest request = eventViewModel.getCreateEventRequest().getValue();

        TextInputLayout name = binding.textInputEventName;
        TextInputLayout description = binding.textInputEventDescription;
        TextInputLayout capacity = binding.textInputMaxAttendants;
        TextInputLayout dateRange= binding.textInputDateRange;

        assert request != null;
        if (startDate == null || endDate == null){
            name.setError("Event start date and end date are required!");
        }else{
            request.setStartDate(formatDate(startDate));
            request.setEndDate(formatDate(endDate));
        }
        if (request.getName() == null|| request.getName().isEmpty()){
            name.setError("Event Name Is Required!");
            count++;
        }else if (request.getName().length() > 30){
            name.setError("Event Name Cannot be more than 30 letters long!");
            count++;
        }else if (request.getName().matches(".*\\d.*")){
            name.setError("Event Name cannot contain numbers!");
            count++;
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            description.setError("Event Description Is Required!\n");
            count++;
        }if (request.getCapacity() == null || request.getCapacity() == 0){
            capacity.setError("Event Capacity Is Required!");
            count++;
        }if (startDate == null){
            dateMessage += "Start date is required\n";;
            count++;
        }if (endDate == null){
            dateMessage += "End date is required\n";
            dateRange.setError(dateMessage);
            count++;
        }

        return count == 0;

    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}