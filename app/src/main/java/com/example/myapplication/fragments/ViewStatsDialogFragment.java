package com.example.myapplication.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EventButtonAdapter;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.dto.event.EventCardResponse;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.EventViewModel;
import com.example.myapplication.viewmodels.UserViewModel;

import java.io.IOException;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewStatsDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewStatsDialogFragment extends DialogFragment implements EventCardAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView eventsRecyclerView;

    private EventButtonAdapter adapter;
    private EventViewModel eventVM;

    private ResponseBody pendingPdfResponse;

    private int SAVE_PDF_REQUEST_CODE = 3001;

    public ViewStatsDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewStatsDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewStatsDialogFragment newInstance(String param1, String param2) {
        ViewStatsDialogFragment fragment = new ViewStatsDialogFragment();
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
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.white);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        eventVM.resetUserEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_stats_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);
        eventVM = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventVM.getUserEvents().observe(getViewLifecycleOwner(), eventInfoResponses -> {
            adapter.updateData(eventInfoResponses);
        });
        eventVM.fetchAllPublicEvents();
    }

    private void setupRecyclerView(View view) {
        eventsRecyclerView = view.findViewById(R.id.favEventsRecyclerView);
        adapter = new EventButtonAdapter(this);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(EventCardResponse event) {
        ClientUtils.eventAPIService.getChartReviews(event.getId().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pendingPdfResponse = response.body();
                String fileName = String.format("%s_%s_stats.pdf", event.getName().split(" ")[0],event.getStartDate());

                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE, fileName);
                startActivityForResult(intent, SAVE_PDF_REQUEST_CODE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SAVE_PDF_REQUEST_CODE && resultCode == android.app.Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null && pendingPdfResponse != null) {

                new Thread(() -> {
                    try (OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri)) {
                        if (outputStream != null) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = pendingPdfResponse.byteStream().read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            outputStream.flush();

                            requireActivity().runOnUiThread(() ->
                                    NotificationsUtils.getInstance().showSuccessToast(getContext(), "PDF saved successfully"));

                        } else {
                            requireActivity().runOnUiThread(() ->
                                    NotificationsUtils.getInstance().showErrToast(getContext(), "Unable to open output stream!"));
                        }
                    } catch (IOException e) {
                        Log.e("PDF_SAVE_ERROR", "Error writing PDF", e);
                        requireActivity().runOnUiThread(() ->
                                NotificationsUtils.getInstance().showErrToast(getContext(), "Failed to save PDF."));
                    } finally {
                        pendingPdfResponse.close();
                    }
                }).start();
            }
        }
    }
}