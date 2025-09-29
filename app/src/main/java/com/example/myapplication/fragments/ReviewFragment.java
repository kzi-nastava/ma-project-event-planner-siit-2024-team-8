package com.example.myapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ReviewAdapter;
import com.example.myapplication.domain.Review;
import com.example.myapplication.fragments.user.ReviewDialogFragment;
import com.example.myapplication.services.ReviewService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {

    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> pendingReviews = new ArrayList<>();
    private ReviewService reviewService;
    private TextView noReviewsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reviewAdapter = new ReviewAdapter(pendingReviews, this::openReviewDialog);
        reviewRecyclerView.setAdapter(reviewAdapter);

        noReviewsTextView = view.findViewById(R.id.noReviewsTextView);

        reviewService = new ReviewService();
        loadPendingReviews();

        return view;
    }

    private void loadPendingReviews() {
        reviewService.getPendingReviews(new Callback<List<Review>>() {
            @Override
            public void onResponse(@NonNull Call<List<Review>> call, @NonNull Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pendingReviews.clear();
                    pendingReviews.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();

                    if (pendingReviews.isEmpty()) {
                        noReviewsTextView.setVisibility(View.VISIBLE);
                        reviewRecyclerView.setVisibility(View.GONE);
                    } else {
                        noReviewsTextView.setVisibility(View.GONE);
                        reviewRecyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("ReviewFragment", "Failed to load reviews. Response code: " + response.code() +
                            ", Error body: " + response.errorBody());

                    noReviewsTextView.setVisibility(View.VISIBLE);
                    reviewRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Review>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ReviewFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void openReviewDialog(Review review) {
        ReviewDialogFragment dialogFragment = ReviewDialogFragment.newInstance(review);
        dialogFragment.setOnReviewActionListener(success -> {
            if (success) {
                pendingReviews.remove(review);
                reviewAdapter.notifyDataSetChanged();
            }
        });
        dialogFragment.show(getParentFragmentManager(), "ReviewDialogFragment");
    }
}