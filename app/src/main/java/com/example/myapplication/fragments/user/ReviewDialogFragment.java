package com.example.myapplication.fragments.user;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.domain.Review;
import com.example.myapplication.services.ReviewService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDialogFragment extends DialogFragment {

    private Review review;
    private ReviewService reviewService;
    private OnReviewActionListener listener;

    public interface OnReviewActionListener {
        void onReviewAction(boolean success);
    }

    public void setOnReviewActionListener(OnReviewActionListener listener) {
        this.listener = listener;
    }

    public static ReviewDialogFragment newInstance(Review review) {
        ReviewDialogFragment fragment = new ReviewDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("review", review);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        review = (Review) getArguments().getSerializable("review");
        reviewService = new ReviewService();

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_review);

        TextView commentTextView = dialog.findViewById(R.id.commentTextView);
        TextView ratingTextView = dialog.findViewById(R.id.ratingTextView);
        Button approveButton = dialog.findViewById(R.id.approveButton);
        Button denyButton = dialog.findViewById(R.id.denyButton);

        commentTextView.setText(review.getComment());
        ratingTextView.setText("Rating: " + review.getRating());

        approveButton.setOnClickListener(v -> {
            reviewService.approveReview(review.getId(), new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    listener.onReviewAction(true);
                    dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    dismiss();
                }
            });
        });

        denyButton.setOnClickListener(v -> {
            reviewService.denyReview(review.getId(), new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    listener.onReviewAction(false);
                    dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    dismiss();
                }
            });
        });

        return dialog;
    }
}