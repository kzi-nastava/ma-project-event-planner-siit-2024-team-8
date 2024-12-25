package com.example.myapplication.fragments;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EventTypesAdapter;
import com.example.myapplication.adapters.MultiSelectAdapter;
import com.example.myapplication.databinding.FragmentCreateEventTypeBinding;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.Event;
import com.example.myapplication.domain.EventType;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.EventTypeService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.viewmodels.AssetCategoryViewModel;
import com.google.android.material.materialswitch.MaterialSwitch;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventTypeFragment extends Fragment implements EventTypesAdapter.OnNewEventTypeListener, EventTypesAdapter.OnAssetCategoryRequestListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<EventType> eventTypes;

    private Dialog dialog;

    private EventTypeService eventTypeService;

    private List<AssetCategory> activeCategories;

    private FragmentCreateEventTypeBinding binding;

    private EventTypesAdapter adapter;

    //initial state of event Types
    private Integer initialHash;

    private MaterialSwitch active;

    public CreateEventTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventTypeFragment newInstance(String param1, String param2) {
        CreateEventTypeFragment fragment = new CreateEventTypeFragment();
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
        FragmentCreateEventTypeBinding binding = FragmentCreateEventTypeBinding.inflate(inflater, container, false);

        AssetCategoryViewModel viewModel = new ViewModelProvider(requireActivity()).get(AssetCategoryViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.event_type_edit_card);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(getContext(), R.drawable.custom_dialog_bg));
        dialog.setCancelable(true);

        Button saveBtn = dialog.findViewById(R.id.saveEventTypeButton2);
        saveBtn.setOnClickListener(v -> onSaveClicked());

        Button activationBtn = dialog.findViewById(R.id.activationButton);
        activationBtn.setOnClickListener(v-> onActivationClicked());

        Button createBtn = binding.createEventTypeButton;
        createBtn.setOnClickListener(v -> onCreateNewEventType());

        active = binding.activeSwitch;
        active.setOnCheckedChangeListener( (buttonView,checked)-> {
            if (eventTypes == null) {return;}
            Integer currentHash = deepHash(eventTypes);
            if (!initialHash.equals(currentHash)) {
                CompletableFuture<ApiResponse> future = saveEventTypes();
                future.thenAccept(response -> {
                    // Handle successful save
                    if (response != null && response.isSuccess()) {
                        // Optionally, show a success message
                        Log.d("Response", "Successfully saved event types");

                        // Continue with loading active/inactive event types
                        if (checked) {
                            loadActiveEventTypes(false);
                        } else {
                            loadInactiveEventTypes();
                        }
                    } else {
                        Log.d("Error", "Failed to save event types");
                    }
                }).exceptionally(e -> {
                    Log.d("Error", "Save failed: " + e.getMessage());
                    return null;  // Return null for continuation
                });
            } else {
                if (checked) {
                    loadActiveEventTypes(false);
                } else {
                    loadInactiveEventTypes();
                }
            }
        });


        //setting event types via recycler view and adapter
        RecyclerView recyclerView = binding.eventTypeRecyclerView;
        adapter = new EventTypesAdapter(null, CreateEventTypeFragment.this, dialog,this);
        recyclerView.setAdapter(adapter); // Attach an empty adapter first
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventTypeService = new EventTypeService();

        loadActiveEventTypes(true);

        return binding.getRoot();
    }

    private CompletableFuture<ApiResponse> saveEventTypes(){

        CompletableFuture<ApiResponse> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                Future<ApiResponse> response = eventTypeService.saveAllEventTypes(eventTypes);
                if (response.get().isSuccess()){
                    Log.d("Response", "Succesfully saved");
                    future.complete(response.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("error", "Failed to fetch event types: " + e.getMessage());
                });
            }
        }).start();

        return future;
    }
    private void loadActiveEventTypes(boolean initial){
        //loading event types
        new Thread(() -> {
            try {
                Future<List<EventType>> future = eventTypeService.getActiveEventTypes();
                eventTypes = future.get(); // Blocking, but in background thread
                Log.d("event types", eventTypes.toString());

                // Update UI on the main thread
                requireActivity().runOnUiThread(() -> {
                    if (!initial) {
                        adapter.updateData(true,eventTypes);
                    } else {
                        adapter.updateData(eventTypes);
                    }
                    initialHash = deepHash(eventTypes);
                    adapter.notifyDataSetChanged();
                });
            } catch (InterruptedException | ExecutionException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("error", "Failed to fetch event types: " + e.getMessage());
                });
            }
        }).start();
    }

    private void loadInactiveEventTypes(){
        //loading event types
        new Thread(() -> {
            try {
                Future<List<EventType>> future = eventTypeService.getInactiveEventTypes();
                eventTypes = future.get(); // Blocking, but in background thread

                // Update UI on the main thread
                requireActivity().runOnUiThread(() -> {
                    adapter.updateData(false,eventTypes);
                    initialHash = deepHash(eventTypes);
                    adapter.notifyDataSetChanged();
                });
            } catch (InterruptedException | ExecutionException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("error", "Failed to fetch event types: " + e.getMessage());
                });
            }
        }).start();
    }

    private void changeActivationStatus(EventType eventType){
        new Thread(() -> {
            try {
                Future<Optional<EventType>> response = eventTypeService.changeActiveStatus(eventType.getId(),eventType.getActive());;
                if (response.get().isPresent()){
                    Log.d("Response", "Succesfully saved");
                }
            } catch (InterruptedException | ExecutionException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.d("error", "Failed to fetch event types: " + e.getMessage());
                });
            }
        }).start();
    }

    private void setAssetCategories(List<AssetCategory> selected) {
        AssetCategoryService service = new AssetCategoryService();
        service.getActiveCategories(JwtTokenUtil.getToken(), new Callback<List<AssetCategory>>() {
            @Override
            public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                if (response.body() != null) {
                    activeCategories = response.body();

                    requireActivity().runOnUiThread(() -> {
                        MultiSelectAdapter checkboxAdapter = new MultiSelectAdapter(activeCategories,selected);
                        RecyclerView assetCategories = dialog.findViewById(R.id.assetCategoriesRecyclerView);
                        assetCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        assetCategories.setAdapter(checkboxAdapter);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<AssetCategory>> call, Throwable t) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onSaveClicked() {
        TextView name = dialog.findViewById(R.id.eventTypeTextView);
        boolean isNew = name.getVisibility() == View.GONE;

        EditText nameEditText = dialog.findViewById(R.id.eventTypeNameEditText);
        nameEditText.setVisibility(View.GONE);
        nameEditText.setText("");

        TextView id = dialog.findViewById(R.id.eventTypeId);
        UUID currentID = UUID.fromString(id.getText().toString());
        EditText description = dialog.findViewById(R.id.eventTypeDescriptionEditText);
        RecyclerView categories = dialog.findViewById(R.id.assetCategoriesRecyclerView);
        MultiSelectAdapter adapterMultiselect = (MultiSelectAdapter) categories.getAdapter();
        List<AssetCategory> selected = adapterMultiselect.getSelectedItems();

        EventType newEventType = new EventType();
        newEventType.setId(currentID);
        newEventType.setDescription(description.getText().toString());
        newEventType.setName(isNew ? nameEditText.getText().toString() : name.getText().toString());
        newEventType.setAssetCategories(selected);

        if (!isNew){
            Optional<EventType> oldEventType = eventTypes.stream()
                    .filter(et -> et.getId().equals(currentID))
                    .findFirst();
            eventTypes.set(eventTypes.indexOf(oldEventType.get()),newEventType);
            adapter.notifyDataSetChanged();
        }else{
            eventTypes.add(newEventType);
            adapter.notifyItemInserted(eventTypes.size() - 1);
        }


        dialog.dismiss();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onActivationClicked() {
        TextView id = dialog.findViewById(R.id.eventTypeId);


        Optional<EventType> oldEventType = eventTypes.stream()
                .filter(et -> et.getId().equals(UUID.fromString(id.getText().toString())))
                .findFirst();
        EventType newEventType = oldEventType.orElseThrow();
        newEventType.setActive(!newEventType.getActive());
        eventTypes.set(eventTypes.indexOf(newEventType),newEventType);
        adapter.updateData(active.isChecked(),eventTypes);
        adapter.notifyDataSetChanged();
        //changeActivationStatus(newEventType);

        dialog.dismiss();
    }



    @Override
    public void onCreateNewEventType() {
        onAssetCategoryRequested(new ArrayList<>());
        EditText name = dialog.findViewById(R.id.eventTypeNameEditText);
        name.setVisibility(View.VISIBLE);

        TextView nameTextView = dialog.findViewById(R.id.eventTypeTextView);
        nameTextView.setVisibility(View.GONE);

        TextView id = dialog.findViewById(R.id.eventTypeId);
        id.setText(UUID.randomUUID().toString());

        //Deactivation/activation button hide
        Button activationButton = dialog.findViewById(R.id.activationButton);
        activationButton.setVisibility(View.GONE);
        dialog.show();
    }

    @Override
    public void onAssetCategoryRequested(List<AssetCategory> selected) {
        setAssetCategories(selected);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Perform actions when fragment is stopped
        Integer currentHash = Objects.hash(eventTypes.toArray());
        if (!currentHash.equals(initialHash)){
            saveEventTypes();
        }
    }

    //helper
    public Integer deepHash(List<EventType> eventTypes) {
        return eventTypes.stream()
                .map(EventType::hashCode)
                .reduce(0, Integer::sum);
    }
}