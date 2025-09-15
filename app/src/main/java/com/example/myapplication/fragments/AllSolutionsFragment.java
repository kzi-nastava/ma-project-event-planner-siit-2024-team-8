package com.example.myapplication.fragments;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapters.AssetCardAdapter;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.dto.user.SearchAssetRequest;
import com.example.myapplication.domain.enumerations.OfferingType;
import com.example.myapplication.domain.dto.event.SearchEventsRequest;
import com.example.myapplication.fragments.asset.AssetsFilterFragmentBottomSheet;
import com.example.myapplication.fragments.event.FilterBottomSheetFragment;
import com.example.myapplication.viewmodels.AssetViewModel;
import com.example.myapplication.viewmodels.EventViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class AllSolutionsFragment extends Fragment implements FilterBottomSheetFragment.FilterListener, AssetsFilterFragmentBottomSheet.AssetsFilterListener {

    private EventViewModel eventViewModel;
    private AssetViewModel assetViewModel;
    private OfferingType type;

    private String owner = null;

    private int currentPage = 0;

    private int currentPageSize = 1;

    private int totalPages = 0;

    private int totalElements = 0;

    private final ArrayList<String> pageSizes = new ArrayList<>(Arrays.asList("1","2","5","10","15","20"));

    private boolean spinnerInitialized = false;

    public Fragment setType(OfferingType type) {
        this.type = type;
        return this;
    }

    public AllSolutionsFragment() {}

    public AllSolutionsFragment(OfferingType type) {
        this.type = type;
    }
    public AllSolutionsFragment(OfferingType type,String owner) {
        this.type = type;
        this.owner = owner;
    }

    public static AllSolutionsFragment newInstance(){
        return null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_solutions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViews(view);
        setupNextPreviousButton(view);
        setupFilterButton(view);
        setupPagingSpinner(view);
        setupSort(view);
        setupSearch(view);
        setupBackButton(view);
        if (this.owner != null){
            switch (type){
                case EVENT:
                    eventViewModel.getCurrentFilters().getValue().setOwner(owner);
                    break;
                case ASSET:
                    assetViewModel.getCurrentFilters().getValue().setOwner(owner);
                    break;
            }

        }

    }

    private void setupBackButton(View view) {
        ImageButton backButton = view.findViewById(R.id.imageButton);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupSearch(View view) {
        EditText search = view.findViewById(R.id.searchEditText);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Called as the text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (type == OfferingType.EVENT){
                    eventViewModel.getCurrentFilters().getValue().setName(search.getText().toString());
                    eventViewModel.filterEvents(0,10);
                }else{
                    assetViewModel.getCurrentFilters().getValue().setName(search.getText().toString());
                    assetViewModel.filterAssets(0,10);
                }

            }
        });;
    }

    private void setupSort(View view) {
        Spinner mySpinner = view.findViewById(R.id.mySpinner);
        String[] options = type == OfferingType.EVENT ? new String[]{"Name A-Z","Name Z-A","Start date oldest","Start date newest","End date oldest","End Date newest","Capacity lowest","Capacity highest"} :
                new String[]{"Name A-Z","Name Z-A","Price lowest", "Price highest", "Grade lowest", " Grade highest","Discount lowest", "Discount highest"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                options);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinnerInitialized){
                    spinnerInitialized = true;
                    return;
                }
                if (type == OfferingType.EVENT){
                    setEventSortParameter(position);
                }else{
                    setAssetSortParameter(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAssetSortParameter(int position){
        String attr = "";
        String sortOrder = "";
        switch (position) {
            case 0: case 1:
                attr = "name";
                break;
            case 2: case 3:
                attr = "price";
                break;
            case 4: case 5:
                attr = "grade";
                break;
            case 6: case 7:
                attr = "discount";
                break;
            default:
                attr = "";
        }
        sortOrder = position%2 == 0 && position != 0 ? "asc" : "desc";
        assetViewModel.getCurrentFilters().getValue().setSortBy(attr);
        assetViewModel.getCurrentFilters().getValue().setSortOrder(sortOrder);
        assetViewModel.filterAssets(currentPage,currentPageSize);
    }

    private void setEventSortParameter(int position){
        String attr = "";
        String sortOrder = "";
        switch (position) {
            case 0: case 1:
                attr = "name";
                break;
            case 2: case 3:
                attr = "startDate";
                break;
            case 4: case 5:
                attr = "endDate";
                break;
            case 6: case 7:
                attr = "capacity";
                break;
            default:
                attr = "";
        }
        sortOrder = position%2 == 0 ? "asc" : "desc";
        eventViewModel.getCurrentFilters().getValue().setSortBy(attr);
        eventViewModel.getCurrentFilters().getValue().setSortOrder(sortOrder);
        eventViewModel.filterEvents(currentPage,currentPageSize);
    }

    private void setupPagingSpinner(View view) {
        AppCompatSpinner spinner = view.findViewById(R.id.pageSizeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, pageSizes);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                currentPageSize = Integer.parseInt(selectedItem);
                currentPage = 0;
                if (type == OfferingType.EVENT) {
                    eventViewModel.filterEvents(currentPage, currentPageSize);
                }else{
                    assetViewModel.filterAssets(currentPage,currentPageSize);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupFilterButton(View view) {
        Button filterButton = view.findViewById(R.id.buttonFilter3);
        filterButton.setOnClickListener(v->onFilterClicked());

        Button resetFilterButton = view.findViewById(R.id.resetFilterButton);
        resetFilterButton.setOnClickListener(v -> {
            if (type == OfferingType.EVENT){
                eventViewModel.setCurrentFilters(new SearchEventsRequest());
                eventViewModel.filterEvents(currentPage,currentPageSize);
            }else{
                assetViewModel.setCurrentFilters(new SearchAssetRequest());
                assetViewModel.filterAssets(currentPage,currentPageSize);
            }
        });
    }

    private void setupNextPreviousButton(View view) {
        Button nextButton = view.findViewById(R.id.next_button);
        Button previousButton = view.findViewById(R.id.previous_button);

        // Handle Next Button click
        nextButton.setOnClickListener(v -> {
            if (currentPage+1 < totalPages) {
                currentPage++;
                if (type == OfferingType.EVENT) {
                    eventViewModel.filterEvents(currentPage, currentPageSize);
                }else{
                    assetViewModel.filterAssets(currentPage,currentPageSize);
                }
                previousButton.setEnabled(true);
                if (currentPage+1 == totalPages) {
                    nextButton.setEnabled(false);
                }
            }
        });

        // Handle Previous Button click
        previousButton.setOnClickListener(v -> {
            if (currentPage > 0) { // Check if we are not on the first page
                currentPage--;
                if (type == OfferingType.EVENT) {
                    eventViewModel.filterEvents(currentPage, currentPageSize);
                }else{
                    assetViewModel.filterAssets(currentPage,currentPageSize);
                }
                nextButton.setEnabled(true);
                if (currentPage == 0) {
                    previousButton.setEnabled(false);
                }
            }
        });

        previousButton.setEnabled(false);
    }

    private void setupRecyclerViews(View view) {
        // Initialize your ViewModel
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        assetViewModel = new ViewModelProvider(requireActivity()).get(AssetViewModel.class);
        RecyclerView solutionsRecyclerView = view.findViewById(R.id.soultionsRecyclerView);
        TextView header = view.findViewById(R.id.titleSolution);
        solutionsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        switch (type) {
            case EVENT:
                EventCardAdapter adapter = new EventCardAdapter(view.getContext());
                adapter.SetOnClick((MainActivity) getActivity(), getActivity().getSupportFragmentManager());
                solutionsRecyclerView.setAdapter(adapter);
                eventViewModel.filterEvents(currentPage,currentPageSize);
                eventViewModel.getTotalPages().observe(getViewLifecycleOwner(),pages -> {
                    this.totalPages = pages;
                    TextView totalPagesText = view.findViewById(R.id.totalPagesTextView);
                    totalPagesText.setText(String.format("%s of %s",currentPage+1,totalPages));
                });
                eventViewModel.getCurrentEvents().observe(getViewLifecycleOwner(),eventCardResponses -> {
                    if (eventCardResponses != null) {
                        adapter.setEvents(eventCardResponses);
                    }
                });
                break;
            case ASSET:
                AssetCardAdapter assetAdapter = new AssetCardAdapter(view.getContext());
                solutionsRecyclerView.setAdapter(assetAdapter);
                assetViewModel.getCurrentAssets().observe(getViewLifecycleOwner(), assets -> {
                    if (assets != null) {
                        Log.d("ASSET_DEBUG", "Assets received from backend: " + assets.size());
                        assetAdapter.setAssets(assets);
                    } else {
                        Log.d("ASSET_DEBUG", "No assets received or null response.");
                    }
                });
                assetViewModel.getTotalPages().observe(getViewLifecycleOwner(),pages -> {
                    this.totalPages = pages;
                    TextView totalPagesText = view.findViewById(R.id.totalPagesTextView);
                    totalPagesText.setText(String.format("%s of %s",currentPage+1,totalPages));
                });
                assetAdapter.SetOnClick(getActivity(), getActivity().getSupportFragmentManager());
                header.setText("Assets");
                break;
            default:
                break;

        }
    }

    private void onFilterClicked() {
        if (type == OfferingType.EVENT){
            FilterBottomSheetFragment filterBottomSheet = new FilterBottomSheetFragment();
            filterBottomSheet.show(getChildFragmentManager(), filterBottomSheet.getTag());
        }else{
            AssetsFilterFragmentBottomSheet assetsFilterFragmentBottomSheet = new AssetsFilterFragmentBottomSheet();
            assetsFilterFragmentBottomSheet.show(getChildFragmentManager(),assetsFilterFragmentBottomSheet.getTag());
        }
    }
    @Override
    public void onDataReceived() {
        currentPage = 0;
        eventViewModel.filterEvents(currentPage,currentPageSize);
    }

    @Override
    public void onAssetsDataReceived() {
        currentPage = 0;
        assetViewModel.filterAssets(currentPage,currentPageSize);
    }
}