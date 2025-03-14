package com.example.myapplication.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EventTypeCheckboxAdapter;
import com.example.myapplication.adapters.EventTypesAdapter;
import com.example.myapplication.databinding.FragmentFilterBottomSheetBinding;
import com.example.myapplication.domain.EventType;
import com.example.myapplication.domain.dto.SearchEventsRequest;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.EventTypeService;
import com.example.myapplication.viewmodels.EventViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FilterBottomSheetFragment extends BottomSheetDialogFragment implements EventTypeCheckboxAdapter.OnEventTypeCheckedChangeListener {

    private RangeSlider capacitySlider;
    private EditText lowerCapacity, upperCapacity;

    private LocalDate startDate,endDate;

    private TextView dateRangeTextView;

    private RecyclerView eventTypesRecyclerView;

    private EventTypeService eventTypeService;
    private MaterialButton applyButton;

    private EventViewModel eventViewModel;

    private EventTypeCheckboxAdapter adapter;

    private RecyclerView recyclerView;

    private List<EventType> eventTypes;

    public interface FilterListener {
        void onDataReceived();
    }

    private FilterListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof FilterListener) {
            listener = (FilterListener) parentFragment;
        } else {
            throw new ClassCastException("Parent fragment must implement FilterListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFilterBottomSheetBinding binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false);


// Initialize the views using binding
        capacitySlider = binding.capacitySlider;
        lowerCapacity = binding.lowerCapacity;
        upperCapacity = binding.upperCapacity;
        applyButton = binding.applyButton;
        eventTypesRecyclerView = binding.checkboxEventTypes;
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        binding.setEventVM(eventViewModel);

        capacitySlider.setValues(0f,100000f);
        capacitySlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.GRAY));  // Inactive track
        capacitySlider.setTrackActiveTintList(ContextCompat.getColorStateList(requireContext(),R.color.olive));    // Active track
        capacitySlider.setThumbTintList(ColorStateList.valueOf(Color.BLACK));           // Thumb color
        capacitySlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            lowerCapacity.setText(String.valueOf(values.get(0).intValue()));
            upperCapacity.setText(String.valueOf(values.get(1).intValue()));
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
                    eventViewModel.getCurrentFilters().getValue().setStartDate(startDate);
                    eventViewModel.getCurrentFilters().getValue().setEndDate(endDate);
                }
        );

// Apply button logic
        applyButton.setOnClickListener(v -> {
            // Apply the filters and close the BottomSheet
            applyFilters();
            dismiss();
        });
        eventTypeService = new EventTypeService();
        recyclerView = binding.checkboxEventTypes;
        adapter = new EventTypeCheckboxAdapter(new ArrayList<>(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadActiveEventTypes();



        return binding.getRoot();
    }
    private void loadActiveEventTypes() {

        new Thread(() -> {
            try {
                Future<List<EventType>> future = eventTypeService.getActiveEventTypes();
                eventTypes = future.get();

                requireActivity().runOnUiThread(() -> {
                    Log.d("Data",eventTypes.toString());
                    adapter.updateData(eventTypes);
                });
            } catch (InterruptedException | ExecutionException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("error", "Failed to fetch event types: " + e.getMessage());
                });
            }
        }).start();
    }

    private void showDatePickerDialog(EditText dateEditText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    dateEditText.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void applyFilters() {
        SearchEventsRequest searchEventsRequest = eventViewModel.getCurrentFilters().getValue();
        assert searchEventsRequest != null;
        searchEventsRequest.setEventTypes(adapter.getSelectedItems().stream().map(eventType -> eventType.getId().toString()).collect(Collectors.toList()));
        if (listener!=null){
            listener.onDataReceived();
        }
    }



    @Override
    public void onEventTypeCheckedChange(EventType eventType, boolean isChecked) {

    }
}