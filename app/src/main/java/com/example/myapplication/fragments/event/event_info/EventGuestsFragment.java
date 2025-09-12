package com.example.myapplication.fragments.event.event_info;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.InvitationsAdapter;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.Invitation;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.example.myapplication.domain.dto.GuestResponse;
import com.example.myapplication.domain.dto.event.InvitationUpdateRequest;
import com.example.myapplication.fragments.user.ProfileInfoFragment;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.utilities.HashUtils;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.EventViewModel;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventGuestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventGuestsFragment extends Fragment implements InvitationsAdapter.OnNewInvitationsListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int SAVE_PDF_REQUEST_CODE = 3001;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String initialHash;

    private RecyclerView recyclerView;
    private InvitationsAdapter adapter;

    private EventViewModel eventViewModel;

    private ResponseBody pendingPdfResponse;

    private List<Invitation> invitations = new ArrayList<>();

    private Boolean isMyEvent;

    private Boolean isPrivateEvent;

    public EventGuestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventGuestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventGuestsFragment newInstance(String param1, String param2) {
        EventGuestsFragment fragment = new EventGuestsFragment();
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
        return inflater.inflate(R.layout.fragment_event_guests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            isMyEvent = Objects.equals(event.getOrganizerID(), JwtTokenUtil.getUserId());
            isPrivateEvent = event.getPrivate();
            Button addGuest = view.findViewById(R.id.addGuestButton);
            if (isMyEvent && isPrivateEvent) {addGuest.setVisibility(View.VISIBLE);}
            addGuest.setOnClickListener( v -> {
                onCreateNewInvitation();
            });
            setupRecyclerView(view);
            fetchGuests();
            setupPdfClick(view);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isMyEvent == null || !isMyEvent){return;}
        List<GuestResponse> list = invitations.stream().map(GuestResponse::new).collect(Collectors.toList());
        String currentHash = HashUtils.hashGuestList(list);
        if (!currentHash.equals(initialHash)){
            List<String> emails = list.stream().map(GuestResponse::getEmail).collect(Collectors.toList());
            InvitationUpdateRequest request = new InvitationUpdateRequest(emails);
            ClientUtils.eventAPIService.updateInvitations(eventViewModel.getEvent().getValue().getId(),request).enqueue(new Callback<ApiResponse>() {
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
        MaterialButton pdfButton = view.findViewById(R.id.pdfGuestList);
        pdfButton.setOnClickListener(v -> {
            EventInfoResponse event = eventViewModel.getEvent().getValue();
            if (event == null) return;

            ClientUtils.eventAPIService.fetchGuestListPDF(event.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        pendingPdfResponse = response.body(); // save for writing
                        String fileName = String.format("%s_guestlist.pdf", event.getName().split(" ")[0]);

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

        if (requestCode == SAVE_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
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

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.guestsRecyclerView);
        adapter = new InvitationsAdapter(new ArrayList<Invitation>(),this,isMyEvent,isPrivateEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fetchGuests(){
        eventViewModel.fetchGuestList(new Callback<List<GuestResponse>>() {
            @Override
            public void onResponse(Call<List<GuestResponse>> call, Response<List<GuestResponse>> response) {
                if (response.isSuccessful()){
                    initialHash = HashUtils.hashGuestList(response.body());
                    invitations = response.body().stream().map(Invitation::new).collect(Collectors.toList());
                    adapter.setInvitations(invitations);
                }
            }

            @Override
            public void onFailure(Call<List<GuestResponse>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreateNewInvitation() {
        if (isMyEvent && isPrivateEvent){
            Invitation invitation = new Invitation(LocalDate.now(),"",true);
            invitations.add(invitation);
            adapter.notifyItemInserted(invitations.size() - 1);
        }
    }

    @Override
    public void onInvitationClickedUser(String userId) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main,new ProfileInfoFragment(UUID.fromString(userId))).addToBackStack(null).commit();
    }
}