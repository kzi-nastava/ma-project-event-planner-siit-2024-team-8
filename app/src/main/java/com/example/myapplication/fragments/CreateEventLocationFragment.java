package com.example.myapplication.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.domain.Location;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventLocationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MapView mapView;

    private Marker marker;

    public CreateEventLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventStepTwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventLocationFragment newInstance(String param1, String param2) {
        CreateEventLocationFragment fragment = new CreateEventLocationFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_event_step_two, container, false);

        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

        mapView = view.findViewById(R.id.mapView3);
        mapView.setMultiTouchControls(true); // Enable zoom controls

        mapView.getController().setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(45.2517, 19.8369); // Latitude, Longitude
        mapView.getController().setCenter(startPoint);

        marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setTitle("Hello, OpenStreetMap!");
        mapView.getOverlays().add(marker);

        Button submitButton = view.findViewById(R.id.submitAddressButton);
        submitButton.setOnClickListener(v -> onSubmitClicked());

        Button next = view.findViewById(R.id.createEventNextButton2);

        next.setOnClickListener( v -> {
            nextButtonClicked();
        });

        return view;
    }

    private void nextButtonClicked() {
        CreateEventAgendaFragment stepTwoFragment = new CreateEventAgendaFragment();
        CreateEventFragment parentFragment = (CreateEventFragment) getParentFragment();
        assert parentFragment != null;
        parentFragment.getChildFragmentManager().beginTransaction()
                .setCustomAnimations( R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right )
                .replace(R.id.createEventLayout, stepTwoFragment)
                .addToBackStack(null)
                .commit();
        parentFragment.changeTitle(3);
        parentFragment.animateProgressBar(66);
    }

    private Location getLocationFromAddress(String strAddress) {
        Location location = new Location();
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            // Geocode the address to get latitude and longitude
            addresses = geocoder.getFromLocationName(strAddress, 1);  // 1 means we are only interested in one result.

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                location.setLatitude(address.getLatitude());
                location.setLongitude(address.getLongitude());
            }

        } catch (IOException e) {
            Toast toast = Toast.makeText(requireActivity(), "Cannot find location for given address!\n Try again!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }

        return location;
    }

    public void onSubmitClicked(){
        EditText city = requireActivity().findViewById(R.id.editTextEventStreet);
        EditText street = requireActivity().findViewById(R.id.editTextEventCity);

        String address = String.format("%s %s", street.getText(),city.getText());

        Location location = getLocationFromAddress(address);

        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapView.getController().setCenter(startPoint);

        removeMarker();
        marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setTitle("Hello, OpenStreetMap!");
        mapView.getOverlays().add(marker);

    }

    public void removeMarker() {
        if (marker != null) {
            mapView.getOverlays().remove(marker);
            mapView.invalidate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause(); // Prevent memory leaks
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume(); // Resume map updates
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDetach();
        }
    }
}