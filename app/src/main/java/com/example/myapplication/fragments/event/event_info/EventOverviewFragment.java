package com.example.myapplication.fragments.event.event_info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.domain.dto.EventInfoResponse;
import com.example.myapplication.fragments.event.edit_event.EventEditFragment;
import com.example.myapplication.services.EventService;
import com.example.myapplication.utilities.JwtTokenUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventOverviewFragment extends Fragment {
    private String eventId = "";
    private EventInfoResponse eventInfo;

    private EventService eventService = new EventService();

    public EventOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString("eventId"); // Retrieve the eventId passed via arguments
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_overview, container, false);

        // Set up edit button
        Button edit = view.findViewById(R.id.editEventButton);
        edit.setOnClickListener(v -> onClickEdit());

        // Fetch event data using the eventId
        getEventById(eventId, view);

        // Set up map button to open location in Google Maps
        Button openInMapButton = view.findViewById(R.id.mapButton);
        openInMapButton.setOnClickListener(v -> {
            double longitude = 0;
            double latitude = 0;
            if (eventInfo != null && eventInfo.location != null) {
                latitude = eventInfo.location.latitude;
                longitude = eventInfo.location.longitude;
            }
            String uri = String.format("geo:%f,%f?q=%f,%f", latitude, longitude, latitude, longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps"); // Ensure it opens in Google Maps
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Google Maps is not installed", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void onClickEdit() {
        Fragment edit = EventEditFragment.newInstance(eventId);
        replaceFragment(edit);
    }

    // Make the API call to fetch event by eventId
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

    // Populate fields with the event data
    private void loadDataIntoFields(View view) {
        TextView eName = view.findViewById(R.id.eName);
        eName.setText(eventInfo.name);
        TextView eDesc = view.findViewById(R.id.eDesc);
        eDesc.setText(eventInfo.description);
        TextView eStart = view.findViewById(R.id.eStart);
        eStart.setText(eventInfo.startDate);
        TextView eEnd = view.findViewById(R.id.eEnd);
        eEnd.setText(eventInfo.endDate);
        TextView eOrg = view.findViewById(R.id.eOrg);
        eOrg.setText(eventInfo.organizerName);
        TextView ePriv = view.findViewById(R.id.ePriv);
        if (eventInfo.isPrivate) {
            ePriv.setText("PRIVATE");
        } else {
            ePriv.setText("PUBLIC");
        }
        TextView eCap = view.findViewById(R.id.eCap);
        eCap.setText(eventInfo.capacity.toString());
    }
}
