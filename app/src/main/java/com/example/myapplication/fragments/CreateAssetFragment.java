package com.example.myapplication.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class CreateAssetFragment extends Fragment {

    private RadioGroup assetTypeGroup;
    private View serviceSpecificFields;
    private TextView bookingDeadlineTextView, cancellationDeadlineTextView;
    private Spinner assetCategorySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_asset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assetTypeGroup = view.findViewById(R.id.assetTypeGroup);
        serviceSpecificFields = view.findViewById(R.id.serviceSpecificFieldsLayout);
        bookingDeadlineTextView = view.findViewById(R.id.bookingDateTextView);
        cancellationDeadlineTextView = view.findViewById(R.id.cancellationDateTextView);
        assetCategorySpinner = view.findViewById(R.id.assetCategorySpinner);
        MaterialButton cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        MaterialButton saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.asset_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        assetCategorySpinner.setAdapter(adapter);

        assetCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ((RadioButton) view.findViewById(R.id.assetTypeProduct)).setChecked(true);
        toggleServiceFields(false);

        assetTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.assetTypeService) {
                toggleServiceFields(true);
            } else {
                toggleServiceFields(false);
            }
        });

        bookingDeadlineTextView.setOnClickListener(v -> showDatePickerDialog(bookingDeadlineTextView));
        cancellationDeadlineTextView.setOnClickListener(v -> showDatePickerDialog(cancellationDeadlineTextView));
    }

    private void toggleServiceFields(boolean show) {
        serviceSpecificFields.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> textView.setText(
                        selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear),
                year, month, day);
        datePickerDialog.show();
    }
}
