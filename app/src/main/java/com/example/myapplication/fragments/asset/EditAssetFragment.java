package com.example.myapplication.fragments.asset;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.Calendar;

public class EditAssetFragment extends Fragment {

    private EditText assetNameEditText, descriptionEditText, priceEditText, discountEditText, durationEditText;
    private Spinner categorySpinner;
    private Switch visibilitySwitch, availabilitySwitch;
    private RadioGroup confirmationMethodGroup;
    private TextView bookingDeadlineTextView, cancellationDeadlineTextView;
    private LinearLayout categorySuggestionLayout;

    public static EditAssetFragment newInstance(String param1, String param2) {
        EditAssetFragment fragment = new EditAssetFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_edit, container, false);

        assetNameEditText = view.findViewById(R.id.assetNameEditText);
        descriptionEditText = view.findViewById(R.id.assetDescriptionEditText);
        priceEditText = view.findViewById(R.id.assetPriceEditText);
        discountEditText = view.findViewById(R.id.assetDiscountEditText);
        durationEditText = view.findViewById(R.id.assetDurationEditText);

        visibilitySwitch = view.findViewById(R.id.visibilitySwitch);
        availabilitySwitch = view.findViewById(R.id.availabilitySwitch);

        confirmationMethodGroup = view.findViewById(R.id.confirmationMethodGroup);

        bookingDeadlineTextView = view.findViewById(R.id.bookingDateTextView);
        cancellationDeadlineTextView = view.findViewById(R.id.cancellationDateTextView);

        bookingDeadlineTextView.setOnClickListener(v -> showDatePickerDialog(bookingDeadlineTextView));
        cancellationDeadlineTextView.setOnClickListener(v -> showDatePickerDialog(cancellationDeadlineTextView));

        return view;
    }

    private void showDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    textView.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);

        datePickerDialog.show();
    }
}
