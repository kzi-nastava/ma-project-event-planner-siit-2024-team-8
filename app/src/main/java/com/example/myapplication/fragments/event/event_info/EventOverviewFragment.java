package com.example.myapplication.fragments.event.event_info;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ReviewLiveAdapter;
import com.example.myapplication.domain.Review;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.example.myapplication.domain.dto.event.EventSignupRequest;
import com.example.myapplication.fragments.user.ChatFragment;
import com.example.myapplication.fragments.user.ProfileInfoFragment;
import com.example.myapplication.fragments.event.edit_event.EventEditFragment;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.ReviewService;
import com.example.myapplication.services.UserService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.EventViewModel;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventOverviewFragment extends Fragment {
    private String eventId = "";
    private EventInfoResponse eventInfo;

    private EventService eventService = new EventService();

    private ReviewService reviewService = new ReviewService();

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    private RecyclerView reviewsRecyclerView;

    private EventViewModel eventVM;

    private Boolean isMyEvent;

    private Boolean isPrivate;

    private Boolean isUserSignedUp;

    private Boolean hasInFavorites;
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
        View view = inflater.inflate(R.layout.fragment_event_overview, container, false);

        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button edit = view.findViewById(R.id.editEventButton);
        edit.setOnClickListener(v -> onClickEdit());

        Button loadReviews = view.findViewById(R.id.loadReviewsButton);
        loadReviews.setOnClickListener(v -> fetchReviews());

        Button submitComment = view.findViewById(R.id.submitCommentButton);
        submitComment.setOnClickListener(v -> submitComment());

        MaterialButton chatButton = view.findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> openChatFragment());

        getEventById(eventId, view);


        Button openInMapButton = view.findViewById(R.id.mapButton);
        openInMapButton.setOnClickListener(v -> {
            double longitude = 0;
            double latitude = 0;
            if (eventInfo != null && eventInfo.getLocation() != null) {
                latitude = eventInfo.getLocation().latitude;
                longitude = eventInfo.getLocation().longitude;
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

        eventVM = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        eventVM.fetchEvent(eventId);


        Button eOrg = view.findViewById(R.id.eOrg);
        eOrg.setOnClickListener(v -> organizerClicked());

        chatButton.setOnClickListener(v -> openChatFragment());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventVM.getEvent().observe(getViewLifecycleOwner(), eventInfoResponse -> {
            isMyEvent = Objects.equals(eventVM.getEvent().getValue().getOrganizerID(), JwtTokenUtil.getUserId());
            isPrivate = eventVM.getEvent().getValue().getPrivate();
            isUserSignedUp(view);
            hasInFavorites(view);
        });
    }

    private void hasInFavorites(View view) {
        String userId = jwtTokenUtil.getUserId();
        RequestBody body = RequestBody.create(
                MediaType.parse("text/plain"),
                eventId
        );
        ClientUtils.userService.checkFavorite(userId,body).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    hasInFavorites = response.body();
                    if (isUserSignedUp != null){
                        setupEventButtons(view);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void setupEventButtons(View view){
        MaterialButton editButton = view.findViewById(R.id.editEventButton);
        MaterialButton chatButton = view.findViewById(R.id.chatButton);
        MaterialButton favoriteButton = view.findViewById(R.id.favoriteEventsButton);
        if(isMyEvent){
            editButton.setVisibility(View.VISIBLE);
            chatButton.setVisibility(View.GONE);
        }
        MaterialButton signUpButton = view.findViewById(R.id.signUpButton);
        if (!isMyEvent && !isPrivate && !isEventOver()){signUpButton.setVisibility(View.VISIBLE);}
        if (!isMyEvent){favoriteButton.setVisibility(View.VISIBLE);}
        if(hasInFavorites){
            favoriteButton.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.baseline_favorite_24));
            favoriteButton.setText("Unfavorite Event");
        }
        if(isUserSignedUp){
            signUpButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.deactivate_color));
            signUpButton.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.leave_icon));
            signUpButton.setText("Leave Event");
        }
        signUpButton.setOnClickListener(v -> signUserUpToEvent(view));

        favoriteButton.setOnClickListener(v -> favoriteEvent());

    }


    private boolean isEventOver() {
        if (eventInfo == null || eventInfo.getEndDate() == null) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date endDate = sdf.parse(eventInfo.getEndDate());
            return endDate != null && endDate.before(new Date());
        } catch (ParseException e) {
            Log.e("EventTime", "Error parsing date", e);
            return false;
        }
    }

    private void signUserUpToEvent(View view) {
        String userId = JwtTokenUtil.getUserId();
        if(isUserSignedUp){
            showLeaveDialog(userId,view);
        }else{
            showJoinDialog(userId,view);
        }
    }

    private void showJoinDialog(String userId,View view) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure you want to join this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationsUtils.getInstance().showSuccessToast(getContext(),
                                String.format("Joined event %s", eventInfo.getName()));
                        isUserSignedUp = true;
                        setupJoinButton(getView());
                        assert getParentFragment() != null;
                        getParentFragment().getParentFragmentManager().popBackStack();                        eventService.signUserUpToEvent(new EventSignupRequest(userId, eventId), new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void setupJoinButton(View view) {
        MaterialButton signUpButton = view.findViewById(R.id.favoriteEventsButton);
        signUpButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.olive));
        signUpButton.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.baseline_supervised_user_circle_24));
        signUpButton.setText("Join Event");
    }

    private void showLeaveDialog(String userId,View view) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure you want to leave this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationsUtils.getInstance().showNotification(
                                requireContext(),
                                String.format("Event %s left.", eventInfo.getName())
                        );
                        isUserSignedUp = false;
                        setupLeaveButton(requireView());
                        assert getParentFragment() != null;
                        getParentFragment().getParentFragmentManager().popBackStack();
                        eventService.leaveEvent(new EventSignupRequest(userId, eventId), new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void setupLeaveButton(View view) {
        MaterialButton signUpButton = view.findViewById(R.id.favoriteEventsButton);
        signUpButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.deactivate_color));
        signUpButton.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.leave_icon));
        signUpButton.setText("Leave Event");
    }

    private void favoriteEvent() {
        UserService userService = new UserService();
        if (!hasInFavorites){
            userService.favoriteEvent(eventId,getContext());
            NotificationsUtils.getInstance().showSuccessToast(getContext(),"Event added to favorites!");
        }else{
            userService.unfavoriteEvent(eventId,getContext());
            NotificationsUtils.getInstance().showSuccessToast(getContext(),"Event removed from favorites!");
        }
        getParentFragment().getParentFragmentManager().popBackStack();
    }


    private void organizerClicked() {
        replaceFragment(new ProfileInfoFragment(UUID.fromString(eventInfo.getOrganizerID())));
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

    private void getEventById(String eventId, View view) {
        eventService.getEventById(eventId, new Callback<EventInfoResponse>() {
            @Override
            public void onResponse(Call<EventInfoResponse> call, Response<EventInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventInfo = response.body();
                    updateUI(view);
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

    private void updateUI(View view) {
        if (eventInfo != null) {
            loadDataIntoFields(view);
        }
    }

    private void loadDataIntoFields(View view) {
        TextView eName = view.findViewById(R.id.eName);
        eName.setText(eventInfo.getName());
        TextView eDesc = view.findViewById(R.id.eDesc);
        eDesc.setText(eventInfo.getDescription());
        TextView eStart = view.findViewById(R.id.eStart);
        eStart.setText(eventInfo.getStartDate());
        TextView eEnd = view.findViewById(R.id.eEnd);
        eEnd.setText(eventInfo.getEndDate());
        TextView eOrg = view.findViewById(R.id.eOrg);
        eOrg.setText(eventInfo.getOrganizerName());
        TextView ePriv = view.findViewById(R.id.ePriv);
        if (eventInfo.getPrivate()) {
            ePriv.setText("PRIVATE");
        } else {
            ePriv.setText("PUBLIC");
        }
        TextView eCap = view.findViewById(R.id.eCap);
        eCap.setText(eventInfo.getCapacity().toString());
    }

    private void fetchReviews() {
        String userId = jwtTokenUtil.getUserId();
        EventSignupRequest eventSignupRequest = new EventSignupRequest(userId, eventId);
        eventService.isUserSignedUp(eventSignupRequest, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean hasPurchased = response.body();
                    if (hasPurchased) {
                        showCommentInputFields();
                        View joinToCommentTextView = getView().findViewById(R.id.joinToCommentTextView);
                        joinToCommentTextView.setVisibility(View.GONE);
                    } else {
                        hideCommentInputFields();
                        View joinToCommentTextView = getView().findViewById(R.id.joinToCommentTextView);
                        joinToCommentTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("EventOverviewFragment", "Failed to check if joined: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("EventOverviewFragment", "Error checking join event status: " + t.getMessage(), t);
            }
        });

        reviewService.getActiveReviewsForEvent(eventId, new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> reviews = response.body();
                    reviewsRecyclerView.setAdapter(new ReviewLiveAdapter(reviews));
                    reviewsRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    Log.e("EventOverviewFragment", "Failed to fetch reviews: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Log.e("EventOverviewFragment", "Error fetching reviews: " + t.getMessage(), t);
            }
        });
    }

    private void isUserSignedUp(View view){
        String userId = jwtTokenUtil.getUserId();
        EventSignupRequest request = new EventSignupRequest(userId,eventId);
        eventService.isUserSignedUp(request, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    isUserSignedUp = response.body();
                    if(hasInFavorites != null){
                        setupEventButtons(view);
                    }

                }else{
                    NotificationsUtils.getInstance().showErrToast(getContext(),response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    private void showCommentInputFields() {
        View commentSection = getView().findViewById(R.id.commentSection);
        if (commentSection != null) {
            commentSection.setVisibility(View.VISIBLE);
        }
    }

    private void hideCommentInputFields() {
        View commentSection = getView().findViewById(R.id.commentSection);
        if (commentSection != null) {
            commentSection.setVisibility(View.GONE);
        }
    }

    private void submitComment() {
        EditText reviewCommentEditText = getView().findViewById(R.id.reviewCommentEditText);
        RatingBar reviewRatingBar = getView().findViewById(R.id.reviewRatingBar);

        String userComment = reviewCommentEditText.getText().toString();
        float userRating = reviewRatingBar.getRating();

        if (userComment.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a comment.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userRating == 0) {
            Toast.makeText(getContext(), "Please provide a rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = jwtTokenUtil.getUserId();
        RequestBody reviewData = createReviewRequestBody(this.eventId, userId, userComment, userRating);
        Log.e("id", "id is " + userId);
        eventService.submitReview(this.eventId, reviewData, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body();
                    if (message.equalsIgnoreCase("Review submitted successfully")) {
                        Toast.makeText(getContext(), "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                        reviewCommentEditText.setText("");
                        reviewRatingBar.setRating(0);
                    } else if (message.equalsIgnoreCase("You already reviewed this event")) {
                        Toast.makeText(getContext(), "You already reviewed this event.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("EventOverviewFragment", "Failed to submit review: " + response.code());
                    Toast.makeText(getContext(), "Failed to submit review. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("EventOverviewFragment", "Error submitting review", t);
                Toast.makeText(getContext(), "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RequestBody createReviewRequestBody(String eventId, String userId, String comment, float rating) {
        JSONObject reviewJson = new JSONObject();
        try {
            reviewJson.put("eventId", eventId);
            reviewJson.put("userId", userId);
            reviewJson.put("comment", comment);
            reviewJson.put("rating", rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(MediaType.parse("application/json"), reviewJson.toString());
    }

    private void openChatFragment() {
        if (eventInfo.getOrganizerID() == null) {
            Toast.makeText(getContext(), "Provider ID not available", Toast.LENGTH_SHORT).show();
            return;
        }
        ChatFragment chatFragment = ChatFragment.newInstance(eventInfo.getOrganizerID());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, chatFragment)
                .addToBackStack(null)
                .commit();
    }
}
