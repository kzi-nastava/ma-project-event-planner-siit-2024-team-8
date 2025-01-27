package com.example.myapplication.fragments.asset;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ReviewLiveAdapter;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Review;
import com.example.myapplication.domain.Utility;
import com.example.myapplication.fragments.ChatFragment;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.ProductService;
import com.example.myapplication.services.ReviewService;
import com.example.myapplication.services.UtilityService;
import com.example.myapplication.utilities.JwtTokenUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetOverviewFragment extends Fragment {

    private String assetId;
    private String assetType;

    private TextView assetNameTextView;
    private TextView assetTypeTextView;
    private TextView assetCategoryTextView;
    private TextView assetDescriptionTextView;
    private TextView assetPriceTextView;
    private TextView assetDiscountTextView;
    private TextView assetActualPriceTextView;
    private LinearLayout utilityDetailsLayout;
    private TextView assetDurationTextView;

    private TextView assetBookingDeadlineTextView;

    private TextView assetCancellationDeadlineTextView;

    private Button loadReviewsButton;
    private RecyclerView reviewsRecyclerView;
    private Button submitCommentButton;
    private Button chatButton;

    private UtilityService utilityService;
    private ProductService productService;

    private ReviewService reviewService;

    private EventService eventService;

    private JwtTokenUtil jwtTokenUtil;


    public AssetOverviewFragment() {
        // Required empty public constructor
    }

    public static AssetOverviewFragment newInstance(String assetId, String assetType) {
        AssetOverviewFragment fragment = new AssetOverviewFragment();
        Bundle args = new Bundle();
        args.putString("asset_id", assetId);
        args.putString("asset_type", assetType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetId = getArguments().getString("asset_id");
            assetType = getArguments().getString("asset_type");
        }

        utilityService = new UtilityService();
        productService = new ProductService();
        reviewService = new ReviewService();
        eventService = new EventService();
        jwtTokenUtil = new JwtTokenUtil();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_overview, container, false);

        assetNameTextView = view.findViewById(R.id.assetNameTextView);
        assetTypeTextView = view.findViewById(R.id.assetTypeTextView);
        assetCategoryTextView = view.findViewById(R.id.assetCategoryTextView);
        assetDescriptionTextView = view.findViewById(R.id.assetDescriptionTextView);
        assetPriceTextView = view.findViewById(R.id.assetPriceTextView);
        assetDiscountTextView = view.findViewById(R.id.assetDiscountTextView);
        assetActualPriceTextView = view.findViewById(R.id.assetActualPriceTextView);
        utilityDetailsLayout = view.findViewById(R.id.utilityDetailsLayout);
        assetDurationTextView = view.findViewById(R.id.assetDurationTextView);
        assetBookingDeadlineTextView = view.findViewById(R.id.assetBookingDeadlineTextView);
        assetCancellationDeadlineTextView = view.findViewById(R.id.assetCancellationDeadlineTextView);
        chatButton = view.findViewById(R.id.chatButton);

        loadReviewsButton = view.findViewById(R.id.loadReviewsButton);
        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        submitCommentButton = view.findViewById(R.id.submitCommentButton);
        submitCommentButton.setOnClickListener(v -> submitComment());
        chatButton.setOnClickListener(v -> openChatFragment());

        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        Log.e("debug", assetId + " i ovo je type " + assetType);
        if ("UTILITY".equals(assetType)) {
            getUtilityById(authHeader, assetId);
        } else if ("PRODUCT".equals(assetType)) {
            getProductById(authHeader, assetId);
        }
        loadReviewsButton.setOnClickListener(v -> fetchReviews());

        return view;
    }

    private void openChatFragment() {
        ChatFragment chatFragment = ChatFragment.newInstance();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, chatFragment)
                .addToBackStack(null)
                .commit();
    }

    private void fetchReviews() {
        String userId = jwtTokenUtil.getUserId();
        eventService.checkAssetInOrganizedEvents(userId, assetId, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean hasPurchased = response.body();
                    if (hasPurchased) {
                        showCommentInputFields();
                        View buyToCommentTextView = getView().findViewById(R.id.buyToCommentTextView);
                        buyToCommentTextView.setVisibility(View.GONE);
                    } else {
                        hideCommentInputFields();
                        View buyToCommentTextView = getView().findViewById(R.id.buyToCommentTextView);
                        buyToCommentTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("AssetOverviewFragment", "Failed to check purchase: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("AssetOverviewFragment", "Error checking purchase status: " + t.getMessage(), t);
            }
        });

        reviewService.getActiveReviewsForAsset(assetId, new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> reviews = response.body();
                    reviewsRecyclerView.setAdapter(new ReviewLiveAdapter(reviews));
                    reviewsRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    Log.e("AssetOverviewFragment", "Failed to fetch reviews: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Log.e("AssetOverviewFragment", "Error fetching reviews: " + t.getMessage(), t);
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

    private void getUtilityById(String token, String id) {
        utilityService.getUtilityById(token, id, new Callback<Utility>() {
            @Override
            public void onResponse(Call<Utility> call, Response<Utility> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utility utility = response.body();
                    Log.d("AssetOverviewFragment", "Received utility: " + utility.getName());
                    populateUtilityData(utility);
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.d("AssetOverviewFragment", "Request URL: " + call.request().url());
                    } catch (IOException e) {
                        Log.e("AssetOverviewFragment", "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Utility> call, Throwable t) {
                Log.e("AssetOverviewFragment", "Request failed: " + t.getMessage(), t);
            }
        });
    }

    private void getProductById(String token, String id) {
        productService.getProductById(token, id, new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    Log.d("AssetOverviewFragment", "Received product: " + product.getName());
                    populateProductData(product);
                } else {
                    Log.e("error", response.errorBody() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
            }
        });
    }

    private void populateUtilityData(Utility utility) {
        assetNameTextView.setText(utility.getName());
        assetTypeTextView.setText("Utility");

        String categoryId = utility.getCategory();
        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        AssetCategoryService categoryService = new AssetCategoryService();
        categoryService.getCategoryById(authHeader, categoryId, new Callback<AssetCategory>() {
            @Override
            public void onResponse(Call<AssetCategory> call, Response<AssetCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AssetCategory category = response.body();
                    assetCategoryTextView.setText("Category: " + category.getName());  // Set category name
                } else {
                    Log.e("AssetOverviewFragment", "Failed to fetch category for utility");
                }
            }

            @Override
            public void onFailure(Call<AssetCategory> call, Throwable t) {
                Log.e("AssetOverviewFragment", "Category request failed: " + t.getMessage(), t);
            }
        });

        assetDescriptionTextView.setText(utility.getDescription());
        double price = utility.getPrice();
        double discount = utility.getDiscount();
        double actualPrice = price - (price * discount / 100);
        assetPriceTextView.setText("Price: $" + price);
        assetDiscountTextView.setText("Discount: " + discount + "%");
        assetActualPriceTextView.setText("Actual Price: $" + String.format("%.2f", actualPrice));
        utilityDetailsLayout.setVisibility(View.VISIBLE);
        assetDurationTextView.setText("Duration: " + utility.getDuration() + " hours");
        assetBookingDeadlineTextView.setText("Booking Deadline: " + utility.getReservationTerm());
        assetCancellationDeadlineTextView.setText("Cancellation Deadline: " + utility.getCancellationTerm());
    }

    private void populateProductData(Product product) {
        assetNameTextView.setText(product.getName());
        assetTypeTextView.setText("Product");

        String categoryId = product.getCategory();
        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        AssetCategoryService categoryService = new AssetCategoryService();
        categoryService.getCategoryById(authHeader, categoryId, new Callback<AssetCategory>() {
            @Override
            public void onResponse(Call<AssetCategory> call, Response<AssetCategory> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AssetCategory category = response.body();
                    assetCategoryTextView.setText("Category " + category.getName());
                } else {
                    Log.e("AssetOverviewFragment", "Failed to fetch category for product");
                }
            }

            @Override
            public void onFailure(Call<AssetCategory> call, Throwable t) {
                Log.e("AssetOverviewFragment", "Category request failed: " + t.getMessage(), t);
            }
        });

        assetDescriptionTextView.setText(product.getDescription());
        double price = product.getPrice();
        double discount = product.getDiscount();
        double actualPrice = price - (price * discount / 100);
        assetPriceTextView.setText("Price: $" + price);
        assetDiscountTextView.setText("Discount: " + discount + "%");
        assetActualPriceTextView.setText("Actual Price: $" + String.format("%.2f", actualPrice));
        utilityDetailsLayout.setVisibility(View.GONE);
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
        String assetId = this.assetId;

        RequestBody reviewData = createReviewRequestBody(assetId, userId, userComment, userRating);

        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        if ("UTILITY".equals(assetType)) {
            utilityService.submitReview(authHeader, assetId, reviewData, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("AssetOverviewFragment", "Failed to submit review: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("AssetOverviewFragment", "Error submitting review: " + t.getMessage(), t);
                }
            });
        } else if ("PRODUCT".equals(assetType)) {
            productService.submitReview(authHeader, assetId, reviewData, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("AssetOverviewFragment", "Failed to submit review: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("AssetOverviewFragment", "Error submitting review: " + t.getMessage(), t);
                    if (t.getMessage() == "404") {
                        Toast.makeText(getContext(), "Already submitted a review for this asset", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }

    private RequestBody createReviewRequestBody(String assetId, String userId, String comment, float rating) {
        JSONObject reviewJson = new JSONObject();
        try {
            reviewJson.put("assetId", assetId);
            reviewJson.put("userId", userId);
            reviewJson.put("comment", comment);
            reviewJson.put("rating", rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(MediaType.parse("application/json"), reviewJson.toString());
    }

}