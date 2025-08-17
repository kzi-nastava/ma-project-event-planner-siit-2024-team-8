package com.example.myapplication.fragments.asset;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EventButtonAdapter;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.dto.asset.CreateReservationRequest;
import com.example.myapplication.domain.dto.asset.UtilityResponse;
import com.example.myapplication.domain.dto.event.BudgetItemResponse;
import com.example.myapplication.domain.dto.event.EventCardResponse;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.example.myapplication.services.BudgetService;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.EventViewModel;
import com.example.myapplication.viewmodels.UserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyAssetFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BuyAssetFragment extends DialogFragment implements EventCardAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView eventsRecyclerView;

    private EventButtonAdapter adapter;
    private EventViewModel eventVM;

    private MaterialDatePicker<Long> materialDatePicker;

    private MaterialTimePicker materialTimePicker;

    private CreateReservationRequest request = new CreateReservationRequest();

    private LinearLayout dateTimeLayout;


    // TODO: Rename and change types of parameters
    private Integer reservationTerm = null;
    private String assetId;

    private boolean isUtility;

    private BudgetService budgetService;

    private Button reserveButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReserveUtilityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyAssetFragment newInstance(String param1, String param2) {
        BuyAssetFragment fragment = new BuyAssetFragment();
        Bundle args = new Bundle();
        args.putString("reservation_term", param1);
        args.putString("asset_id", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BuyAssetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (!Objects.equals(getArguments().getString("reservation_term"), "null")){
                reservationTerm =Integer.parseInt(getArguments().getString("reservation_term"));
                isUtility = true;
            }else{
                isUtility = false;
            }
            assetId = getArguments().getString("asset_id");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Make dialog width match parent
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            // Optional: remove default dialog background margins
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_asset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);
        eventVM = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventVM.getUserEvents().observe(getViewLifecycleOwner(), eventInfoResponses -> {
            if(eventInfoResponses.isEmpty()){ view.findViewById(R.id.noEventsTV).setVisibility(View.VISIBLE);}
            adapter.updateData(eventInfoResponses);
        });
        dateTimeLayout = view.findViewById(R.id.dateTimeLayout);
        eventVM.fetchOrganizerEvents(getContext());
        budgetService = new BudgetService();
        reserveButton = view.findViewById(R.id.reserveButton);
        reserveButton.setOnClickListener(v -> onReserveUtilityClicked());
        TextView assetTitle = view.findViewById(R.id.assetTitleTV);
        assetTitle.setText(isUtility ? "Reserve Utility" : "Buy Product");
    }


    private void setupDateTimePickers(View view, EventCardResponse event)  {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(event.getStartDate());
            Date endDate = sdf.parse(event.getEndDate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            endDate = cal.getTime();
            long startConstraint = startDate.getTime();
            long endConstraint = endDate.getTime();
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            List<CalendarConstraints.DateValidator> validators = new ArrayList<>();
            validators.add(DateValidatorPointForward.from(startConstraint)); // min
            validators.add(DateValidatorPointBackward.before(endConstraint));
            constraintsBuilder.setValidator(
                    CompositeDateValidator.allOf(validators) // both must be true
            );
            materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select a date")
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();
        }catch (ParseException ex){
            Log.d("Exception ","Parsing ex");
            return;
        }

        materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select time")
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(v-> {
            Instant instant = Instant.ofEpochMilli(v);
            request.setDate(instant.toString());
        });
        materialTimePicker.addOnPositiveButtonClickListener(v-> {
            request.setTime(formatTime());
        });
        view.findViewById(R.id.timeMaterialCard).setOnClickListener(v -> {
            materialTimePicker.show(getParentFragmentManager(),null);
        });
        view.findViewById(R.id.DateMaterialCard).setOnClickListener( v -> {
            materialDatePicker.show(getParentFragmentManager(),null);
        });
    }

    private String formatTime() {
        int hour = materialTimePicker.getHour();
        int minute = materialTimePicker.getMinute();

        String amPm = (hour >= 12) ? "PM" : "AM";
        int hour12 = (hour == 0) ? 12 : (hour > 12 ? hour - 12 : hour);

        String format = String.format("%d:%02d %s", hour12, minute, amPm);
        return format;
    }

    private void setupRecyclerView(View view) {
        eventsRecyclerView = view.findViewById(R.id.recyclerViewEvents);
        adapter = new EventButtonAdapter(this);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void onEventClicked(EventCardResponse event){
        if (isUtility && isAfterBookingDeadline(event.getStartDate())){// UTILITY
            NotificationsUtils.getInstance().showErrToast(getContext(),"Reservation deadline has passed for this event!");
            return;
        }else if (isUtility){
            request.setEventId(event.getId());
            request.setUtilityId(UUID.fromString(assetId));
            setupDateTimePickers(getView(),event);
            dateTimeLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (this.reservationTerm == null){ //PRODUCT
            showBuyDialog(event.getId());
        }
        dateTimeLayout.setVisibility(View.GONE);
    }

    private void showBuyDialog(UUID eventId) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Purchase")
                .setMessage("Do you want to buy this product?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    budgetService.buyProduct(eventId.toString(), assetId, new Callback<BudgetItemResponse>() {
                        @Override
                        public void onResponse(Call<BudgetItemResponse> call, Response<BudgetItemResponse> response) {
                            if(response.isSuccessful()){
                                NotificationsUtils.getInstance().showSuccessToast(getContext(),"Successfully bought product!");
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<BudgetItemResponse> call, Throwable t) {

                        }
                    });
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private boolean isAfterBookingDeadline(String startDateString) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(startDateString);

            Calendar lastBookingDate = Calendar.getInstance();
            lastBookingDate.setTime(startDate);
            lastBookingDate.add(Calendar.DATE, -reservationTerm);

            Date now = new Date();

            return now.after(lastBookingDate.getTime());
        }catch (ParseException e){
            Log.d("Err","parse Err");
        }
        return true;
    }

    private void onReserveUtilityClicked() {
        if (!isRequestValid()){
            return;
        }
        budgetService.reserveUtility(request, new Callback<BudgetItemResponse>() {
            @Override
            public void onResponse(Call<BudgetItemResponse> call, Response<BudgetItemResponse> response) {
                if (response.isSuccessful()){
                    NotificationsUtils.getInstance().showSuccessToast(getContext(),"Utility reserved successfuly!");
                    dismiss();
                }else if(response.code() == 400){
                    NotificationsUtils.getInstance().showErrToast(getContext(),"Utility already reserved for this event!");

                }else if(response.code() == 404){
                    NotificationsUtils.getInstance().showErrToast(getContext(),"This utility is currently unavailable!");

                }else if (response.code() == 500){
                    NotificationsUtils.getInstance().showErrToast(getContext(),"Server error!");
                }
            }

            @Override
            public void onFailure(Call<BudgetItemResponse> call, Throwable t) {

            }
        });
    }

    private boolean isRequestValid(){
        if (request.getEventId() == null) {
            NotificationsUtils.getInstance().showErrToast(getContext(), "Event not picked");
            return false;
        }
        if (request.getDate() == null || request.getDate().isEmpty()){
            NotificationsUtils.getInstance().showErrToast(getContext(), "Date is required!");
            return false;
        }
        if (request.getTime()==null || request.getTime().isEmpty()){
            NotificationsUtils.getInstance().showErrToast(getContext(), "Time is required!");
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(EventCardResponse event) {
        onEventClicked(event);
    }
}
