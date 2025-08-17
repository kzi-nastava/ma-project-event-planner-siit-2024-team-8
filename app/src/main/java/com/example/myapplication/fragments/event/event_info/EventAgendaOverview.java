package com.example.myapplication.fragments.event.event_info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ActivitiesAdapter;
import com.example.myapplication.domain.Activity;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.dto.event.ActivityUpdateRequest;
import com.example.myapplication.domain.dto.event.AgendaUpdateRequest;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.utilities.HashUtils;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.EventViewModel;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventAgendaOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventAgendaOverview extends Fragment implements ActivitiesAdapter.OnNewActivityListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String initialHash;

    private RecyclerView recyclerView;
    private ActivitiesAdapter adapter;

    private List<Activity> activityList = new ArrayList<>();

    private EventViewModel eventViewModel;

    private Boolean isMyEvent;

    private int SAVE_PDF_REQUEST_CODE = 3001;

    private ResponseBody pendingPdfResponse;

    public EventAgendaOverview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventAgendaOverview.
     */
    // TODO: Rename and change types and number of parameters
    public static EventAgendaOverview newInstance(String param1, String param2) {
        EventAgendaOverview fragment = new EventAgendaOverview();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_agenda_overview, container, false);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventViewModel.getEvent().observe(getViewLifecycleOwner(),event ->{
            isMyEvent = Objects.equals(event.getOrganizerID(), JwtTokenUtil.getUserId());
            setupRecyclerView(view);
            fetchActivities();
            setupPdfClick(view);
            adapter.setIsMyEvent(isMyEvent);
        });
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isMyEvent == null || !isMyEvent){return;}
        String currentHash = HashUtils.hashActivityList(activityList);
        if (!currentHash.equals(initialHash)){
            activityList.remove(activityList.size()-1);
            List<ActivityUpdateRequest> list = activityList.stream().map(ActivityUpdateRequest::new).collect(Collectors.toList());
            AgendaUpdateRequest request = new AgendaUpdateRequest(list);
            ClientUtils.eventAPIService.updateAgenda(eventViewModel.getEvent().getValue().getId(),request).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (!response.isSuccessful()){
                        NotificationsUtils.getInstance().showErrToast(getContext(),"Error updating activites!");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
        }
    }

    private void setupPdfClick(View view) {
        MaterialButton pdfAgenda = view.findViewById(R.id.pdfAgendaButton);
        pdfAgenda.setOnClickListener(v -> {
            EventInfoResponse event = eventViewModel.getEvent().getValue();
            if (event == null) return;
            ClientUtils.eventAPIService.fetchEventAgendaPDF(event.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pendingPdfResponse = response.body(); // save for writing
                        String fileName = String.format("%s_%s_agenda.pdf", event.getName().split(" ")[0],event.getStartDate());

                        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("application/pdf");
                        intent.putExtra(Intent.EXTRA_TITLE, fileName);
                        startActivityForResult(intent, SAVE_PDF_REQUEST_CODE);
                    } else {
                        Toast.makeText(getContext(), "Failed to download PDF", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("PDF_DOWNLOAD_FAIL", t.getMessage());
                    Toast.makeText(getContext(), "Error downloading PDF", Toast.LENGTH_SHORT).show();
                }
            });
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

    private void fetchActivities() {
        ClientUtils.eventAPIService.fetchActivities(eventViewModel.getEvent().getValue().getId()).enqueue(new Callback<AgendaUpdateRequest>() {
            @Override
            public void onResponse(Call<AgendaUpdateRequest> call, Response<AgendaUpdateRequest> response) {
                if (response.isSuccessful()){
                    activityList = response.body().getActivityUpdates().stream().map(Activity::new).collect(Collectors.toList());
                    adapter.setActivities(activityList);
                    initialHash = HashUtils.hashActivityList(activityList);
                }
            }

            @Override
            public void onFailure(Call<AgendaUpdateRequest> call, Throwable t) {

            }
        });
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.agendaRecyclerView);
        adapter = new ActivitiesAdapter(new ArrayList<Activity>(),this,isMyEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateNewActivity() {
        if (isMyEvent){
            Activity inputActivity = new Activity("", "", "", "","", true);
            activityList.add(activityList.size() - 1, inputActivity);
            adapter.notifyItemInserted(activityList.size() - 2);
        }
    }
}