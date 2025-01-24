package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.Review;

import java.util.List;

public class ReviewLiveAdapter extends RecyclerView.Adapter<ReviewLiveAdapter.ReviewViewHolder> {

    private final List<Review> reviews;

    public ReviewLiveAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.reviewUserName.setText(review.getUserName());
        holder.reviewComment.setText(review.getComment());

        double rating = review.getRating();
        for (int i = 0; i < 5; i++) {
            holder.stars[i].setImageResource(i < rating ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewUserName, reviewComment;
        ImageView[] stars = new ImageView[5];

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewUserName = itemView.findViewById(R.id.reviewUserName);
            reviewComment = itemView.findViewById(R.id.reviewComment);
            stars[0] = itemView.findViewById(R.id.star1);
            stars[1] = itemView.findViewById(R.id.star2);
            stars[2] = itemView.findViewById(R.id.star3);
            stars[3] = itemView.findViewById(R.id.star4);
            stars[4] = itemView.findViewById(R.id.star5);
        }
    }
}
