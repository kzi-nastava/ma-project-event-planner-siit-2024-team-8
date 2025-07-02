package com.example.myapplication.fragments.event.event_info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.Event;
import com.example.myapplication.domain.Review;
import com.example.myapplication.domain.dto.EventInfoResponse;
import com.example.myapplication.domain.dto.EventSignupRequest;
import com.example.myapplication.fragments.ProfileInfoFragment;
import com.example.myapplication.domain.dto.EventSignupRequest;
import com.example.myapplication.fragments.event.edit_event.EventEditFragment;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.ReviewService;
import com.example.myapplication.utilities.JwtTokenUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

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

        getEventById(eventId, view);

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


        Button eOrg = view.findViewById(R.id.eOrg);
        eOrg.setOnClickListener(v -> organizerClicked());

        return view;
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
}
