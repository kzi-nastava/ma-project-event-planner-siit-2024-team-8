package com.example.myapplication.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.adapters.InvitationsAdapter;
import com.example.myapplication.adapters.ReportsAdapter;
import com.example.myapplication.domain.Invitation;
import com.example.myapplication.domain.PagedResponse;
import com.example.myapplication.domain.dto.ReportResponse;
import com.example.myapplication.domain.enumerations.OfferingType;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportsFragment extends Fragment implements ReportsAdapter.OnSuspendUserListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private ReportsAdapter adapter;

    private int currentPage = 0;

    private int currentPageSize = 1;
    private int totalPages = 0;

    private UserViewModel userViewModel;

    private final ArrayList<String> pageSizes = new ArrayList<>(Arrays.asList("1","2","5","10","15","20"));




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportsFragment newInstance(String param1, String param2) {
        ReportsFragment fragment = new ReportsFragment();
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
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        setupRecyclerView(view);
        setupNextPreviousButton(view);
        setupPagingSpinner(view);
        fetchReports();
        view.findViewById(R.id.backButton).setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void fetchReports() {
        ClientUtils.reportAPIService.getReports(currentPage,currentPageSize).enqueue(new Callback<PagedResponse<ReportResponse>>() {
            @Override
            public void onResponse(Call<PagedResponse<ReportResponse>> call, Response<PagedResponse<ReportResponse>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    adapter.setReports(response.body().getContent());
                    totalPages = response.body().getTotalPages();
                }else{
                    Log.d("ERR","ERR");
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<ReportResponse>> call, Throwable t) {
                Log.d("ERR","ERR");

            }
        });
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.reportsRecyclerView);
        adapter = new ReportsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupPagingSpinner(View view) {
        AppCompatSpinner spinner = view.findViewById(R.id.pageSizeSpinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, pageSizes);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                currentPageSize = Integer.parseInt(selectedItem);
                currentPage = 0;
                fetchReports();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
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
                fetchReports();
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
                fetchReports();
                nextButton.setEnabled(true);
                if (currentPage == 0) {
                    previousButton.setEnabled(false);
                }
            }
        });

        previousButton.setEnabled(false);
    }


    @Override
    public void suspendUserClicked(String reportId) {
        suspendButtonClicked(reportId);
    }

    private void suspendButtonClicked(String reportId) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.suspend_user_dialog, null);
        dialog.setView(view);

        final AlertDialog alert = dialog.create();
        setUpDialog(alert); //setting up height and background
        Button cancel = (Button) view.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        Button ok = (Button) view.findViewById(R.id.ok_action);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.suspendUser(reportId,getContext());
                alert.dismiss();
            }
        });
        alert.show();
    }
    private void setUpDialog(AlertDialog alert) {
        Window window = alert.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Transparent background

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());

            // Convert 300dp to pixels
            float density = getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (300 * density);

            // Set dimensions
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = heightInPixels;

            window.setAttributes(layoutParams);
        }
    }


}