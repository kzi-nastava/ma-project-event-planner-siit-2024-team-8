package com.example.myapplication.fragments.event.edit_event;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.example.myapplication.domain.dto.event.EventLocationDTO;
import com.example.myapplication.domain.dto.event.EventUpdateRequest;
import com.example.myapplication.fragments.HomePageFragment;
import com.example.myapplication.services.EventService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventEditFragment extends Fragment {

    private EventInfoResponse eventInfo;
    private EventUpdateRequest eventUpdate;
    private String eventId = "";

    private EventService eventService = new EventService();

    public EventEditFragment() {
        // Required empty public constructor
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static EventEditFragment newInstance(String id) {
        EventEditFragment fragment = new EventEditFragment();
        Bundle args = new Bundle();
        args.putString("eventId", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_edit, container, false);
        getEventById(eventId, view);
        Button submitButton = view.findViewById(R.id.saveEventButton);
        submitButton.setOnClickListener(v -> onClickSubmit());
        Button deleteButton = view.findViewById(R.id.deleteEventButton);
        deleteButton.setOnClickListener(v -> onClickDelete());
        return view;
    }

    private void getEventById(String eventId, View view) {
        eventService.getEventById(eventId, new Callback<EventInfoResponse>() {
            @Override
            public void onResponse(Call<EventInfoResponse> call, Response<EventInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Assign the response body to the private field
                    eventInfo = response.body();
                    updateUI(view); // Update the UI with event data
                    Log.d("EventFragment", "Event retrieved: " + eventInfo.toString());
                } else {
                    Log.e("EventFragment", "Failed to retrieve event: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<EventInfoResponse> call, Throwable t) {
                Log.e("EventFragment", "API call failed", t);
            }
        });
    }

    // Update UI with the retrieved event information
    private void updateUI(View view) {
        if (eventInfo != null) {
            loadDataIntoFields(view); // Populate the UI with event data
        }
    }

    private void loadDataIntoFields(View view) {
        EditText name = view.findViewById(R.id.eventNameEditText);
        name.setText(eventInfo.getName());
        EditText desc = view.findViewById(R.id.eventDescEditText);
        desc.setText(eventInfo.getDescription());
        EditText startDate = view.findViewById(R.id.eventStartDateEditText);
        startDate.setText(eventInfo.getStartDate());
        EditText endDate = view.findViewById(R.id.eventEndDateEditText);
        endDate.setText(eventInfo.getEndDate());
        EditText cap = view.findViewById(R.id.eventCapEditText);
        cap.setText(eventInfo.getCapacity().toString());
        EditText loc = view.findViewById(R.id.eventLocEditText);
        loc.setText(eventInfo.getLocation().city + "," + eventInfo.getLocation().street);
    }

    public void onClickSubmit() {
        eventUpdate = new EventUpdateRequest();
        loadDataFromFields(eventUpdate);
        eventService.updateEvent(eventUpdate, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle successful update (e.g., show a success message)
                    String successMessage = response.body();
                    Log.d("EventEditActivity", "Event updated successfully: " + successMessage);
                } else {
                    // Handle failure response
                    Log.e("EventEditActivity", "Failed to update event: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle failure, such as network errors
                Log.e("EventEditActivity", "API call failed", t);
            }
        });
        replaceFragment(new HomePageFragment());
    }

    public void onClickDelete() {
        eventService.deleteEvent(eventId, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {}

            @Override
            public void onFailure(Call<String> call, Throwable t) {}
        });
        replaceFragment(new HomePageFragment());
    }




    private void loadDataFromFields(EventUpdateRequest u) {

        u.id = eventId;

        EditText name = getView().findViewById(R.id.eventNameEditText);
        u.name = name.getText().toString();
        EditText desc = getView().findViewById(R.id.eventDescEditText);
        u.description = desc.getText().toString();
        EditText startDate = getView().findViewById(R.id.eventStartDateEditText);
        u.startDate = startDate.getText().toString();
        EditText endDate = getView().findViewById(R.id.eventEndDateEditText);
        u.endDate = endDate.getText().toString();
        EditText cap = getView().findViewById(R.id.eventCapEditText);
        u.capacity = Integer.valueOf(cap.getText().toString());

        EditText loc = getView().findViewById(R.id.eventLocEditText);
        getLocationFromAddress(loc.getText().toString(), u);
    }

    private void getLocationFromAddress(String strAddress, EventUpdateRequest u) {
        u.location = new EventLocationDTO();
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            // Geocode the address to get latitude and longitude
            addresses = geocoder.getFromLocationName(strAddress, 1);  // 1 means we are only interested in one result.

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                u.location.latitude = address.getLatitude();
                u.location.longitude = address.getLongitude();
                String[] cityStreet = strAddress.split(",");
                u.location.city = cityStreet[0];
                u.location.street = cityStreet[1];
            } else {
                u.location.latitude = 0.0;
                u.location.longitude = 0.0;
                String[] cityStreet = strAddress.split(",");
                u.location.city = cityStreet[0];
                u.location.street = cityStreet[1];
            }

        } catch (IOException e) {
            Toast toast = Toast.makeText(requireActivity(), "Cannot find location for given address!\n Try again!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }
}