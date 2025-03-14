package com.example.myapplication.fragments;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapters.AssetCardAdapter;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.enumerations.OfferingType;
import com.example.myapplication.domain.dto.SearchEventsRequest;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.viewmodels.AssetViewModel;
import com.example.myapplication.viewmodels.EventViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class AllSolutionsFragment extends Fragment implements FilterBottomSheetFragment.FilterListener {

    private EventViewModel eventViewModel;
    private AssetViewModel assetViewModel;
    private OfferingType type;

    private int currentPage = 0;

    private int currentPageSize = 5;

    private int totalPages = 0;

    private int totalElements = 0;

    private final ArrayList<String> pageSizes = new ArrayList<>(Arrays.asList("1","2","5","10","15","20"));

    public void setType(OfferingType type) {
        this.type = type;
    }

    public AllSolutionsFragment() {}

    public AllSolutionsFragment(OfferingType type) {
        this.type = type;
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
                assetAdapter.SetOnClick((MainActivity) getActivity(),getActivity().getSupportFragmentManager());
                assetViewModel.getAssetsLiveData().observe(getViewLifecycleOwner(), assets -> {
                    if (assets != null) {
                        assetAdapter.setAssets(new ArrayList<>(assets)); // Set all assets
                    }
                });
                String token = JwtTokenUtil.getToken();
                if (token != null) {
                    assetViewModel.fetchAssets("Bearer " + token);
                }
                assetAdapter.SetOnClick(getActivity(), getActivity().getSupportFragmentManager());
                header.setText("Assets");
                break;
            default:
                break;

        }

        Button nextButton = view.findViewById(R.id.next_button);
        Button previousButton = view.findViewById(R.id.previous_button);
        TextView pageNumberText = view.findViewById(R.id.page_number_text);

        // Handle Next Button click
        nextButton.setOnClickListener(v -> {
            if (currentPage+1 < totalPages) {
                currentPage++;
                pageNumberText.setText(String.format("Page %d",currentPage+1));
                if (type == OfferingType.EVENT) {
                    eventViewModel.filterEvents(currentPage, currentPageSize);
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
                pageNumberText.setText(String.format("Page %d",currentPage+1));
                if (type == OfferingType.EVENT) {
                    eventViewModel.filterEvents(currentPage, currentPageSize);
                }
                nextButton.setEnabled(true);
                if (currentPage == 0) {
                    previousButton.setEnabled(false);
                }
            }
        });


        pageNumberText.setText(String.format("Page %d",currentPage+1));
        previousButton.setEnabled(false);


        Button filterButton = view.findViewById(R.id.buttonFilter3);
        filterButton.setOnClickListener(v->onFilterClicked());

        Button resetFilterButton = view.findViewById(R.id.resetFilterButton);
        resetFilterButton.setOnClickListener(v -> {
            eventViewModel.setCurrentFilters(new SearchEventsRequest());
            eventViewModel.filterEvents(currentPage,currentPageSize);
        });
        



        AppCompatSpinner spinner = view.findViewById(R.id.pageSizeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, pageSizes);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                currentPageSize = Integer.parseInt(selectedItem);
                currentPage = 0;
                eventViewModel.filterEvents(currentPage,currentPageSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

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

    private void filterEvents(){}

    private void filterAssets(){}

    @Override
    public void onDataReceived() {
        currentPage = 0;
        eventViewModel.filterEvents(currentPage,currentPageSize);
    }
}