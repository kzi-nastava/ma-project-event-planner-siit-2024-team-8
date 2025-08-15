package com.example.myapplication.fragments.asset;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ReviewLiveAdapter;
import com.example.myapplication.domain.Asset;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Review;
import com.example.myapplication.domain.Utility;
import com.example.myapplication.domain.dto.asset.ProductResponse;
import com.example.myapplication.domain.dto.asset.UtilityResponse;
import com.example.myapplication.domain.dto.event.EventInfoResponse;
import com.example.myapplication.domain.dto.user.AssetResponse;
import com.example.myapplication.domain.dto.user.ProviderInfoResponse;
import com.example.myapplication.domain.enumerations.AssetType;
import com.example.myapplication.fragments.ChatFragment;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.BudgetService;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.ProductService;
import com.example.myapplication.services.ReviewService;
import com.example.myapplication.services.UserService;
import com.example.myapplication.services.UtilityService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.example.myapplication.utilities.NetworkUtils;
import com.example.myapplication.utilities.NotificationsUtils;
import com.example.myapplication.viewmodels.AssetViewModel;
import com.example.myapplication.viewmodels.EventViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
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

    private final List<ImageView> assetImages = new ArrayList<>();

    private MaterialButton loadReviewsButton;
    private RecyclerView reviewsRecyclerView;
    private Button submitCommentButton;
    private MaterialButton providerName;
    private MaterialButton chatButton;

    private MaterialButton buyProductButton;

    private MaterialButton reserveUtilityButton;

    private UtilityService utilityService;
    private ProductService productService;

    private ReviewService reviewService;

    private EventService eventService;

    private JwtTokenUtil jwtTokenUtil;

    private TextView availableTextView;

    private MaterialCardView reservationCard;

    private String providerId;

    private String eventId;

    private ImageButton backButton;

    private AssetViewModel assetVM;

    private EventViewModel eventVM;

    private String eventStartDate;

    private String reservationId = null;


    public AssetOverviewFragment() {
        // Required empty public constructor
    }

    public static AssetOverviewFragment newInstance(String assetId, String assetType,String eventId) {
        AssetOverviewFragment fragment = new AssetOverviewFragment();
        Bundle args = new Bundle();
        args.putString("asset_id", assetId);
        args.putString("asset_type", assetType);
        args.putString("event_id",eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetId = getArguments().getString("asset_id");
            assetType = getArguments().getString("asset_type");
            eventId = getArguments().getString("event_id");
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
        assetVM = new ViewModelProvider(requireActivity()).get(AssetViewModel.class);
        eventVM = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventVM.getEvent().observe(getViewLifecycleOwner(),eventInfoResponse -> {
            eventStartDate = eventInfoResponse.getStartDate();
        });
        eventVM.fetchEvent(eventId);
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
        availableTextView = view.findViewById(R.id.available);
        providerName = view.findViewById(R.id.providerNameButton);
        assetImages.add(view.findViewById(R.id.image1));
        assetImages.add(view.findViewById(R.id.image2));
        assetImages.add(view.findViewById(R.id.image3));
        chatButton = view.findViewById(R.id.chatButton);
        buyProductButton = view.findViewById(R.id.buyProductButton);
        reserveUtilityButton = view.findViewById(R.id.reserveUtilityButton);

        loadReviewsButton = view.findViewById(R.id.loadReviewsButton);
        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        submitCommentButton = view.findViewById(R.id.submitCommentButton);
        submitCommentButton.setOnClickListener(v -> submitComment());
        chatButton.setOnClickListener(v -> openChatFragment());

        reservationCard = view.findViewById(R.id.reservationCard);


        String authHeader = JwtTokenUtil.getToken() != null ? "Bearer " + JwtTokenUtil.getToken() : null;
        Log.e("debug", assetId + " i ovo je type " + assetType);
        if ("UTILITY".equals(assetType)) {
            getUtilityById(authHeader, assetId);
        } else if ("PRODUCT".equals(assetType)) {
            getProductById(authHeader, assetId);
        }
        loadReviewsButton.setOnClickListener(v -> fetchReviews());

        return view;
    }


    //SETTING UP RESERVATION (IF THERE IS ONE) WHEN ORGANIZER IS ACCESSING THIS FRAGMENT THROUGH BOUGHT ASSETS
    private void setupReservationData(View view) {
        TextView resDateTV = view.findViewById(R.id.resDateTV);
        TextView resTimeTV = view.findViewById(R.id.resTimeTV);
        assetVM.getCurrentReservation().observe(getViewLifecycleOwner(),reservationResponse -> {
            resDateTV.setText(reservationResponse.getDate());
            resTimeTV.setText(reservationResponse.getTime());
            reservationId = reservationResponse.getId().toString();
        });
        assetVM.fetchReservation(eventId,assetId);
    }

    private void setupCancelReservationButton(UtilityResponse response) {
        Button cancelReservationButton = getView().findViewById(R.id.cancelReservationButton);
        cancelReservationButton.setVisibility(isAfterCancellatioDeadline(response) ? View.GONE : View.VISIBLE);
        cancelReservationButton.setOnClickListener(v -> {
            showCancellationDialog();
        });
    }

    private void showCancellationDialog() {
        BudgetService budgetService = new BudgetService();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Reservation")
                .setMessage("Do you really want to cancel this reservation?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Accept reservation
                    budgetService.cancelReservation(eventId,assetId);
                    NotificationsUtils.getInstance().showNotification(getContext(),"Reservation cancelled.");
                    getParentFragment().getParentFragmentManager().popBackStack();
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Deny reservation
                    dialog.dismiss();
                })
                .setNeutralButton("Cancel", (dialog, which) -> {
                    Log.d("Reservation", "Dialog closed without action");
                    dialog.dismiss();
                })
                .show();
    }

    private boolean isAfterCancellatioDeadline(UtilityResponse response){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(eventStartDate);

            Calendar lastCancellationDate = Calendar.getInstance();
            lastCancellationDate.setTime(startDate);
            lastCancellationDate.add(Calendar.DATE, -response.getCancellationTerm());

            Date now = new Date();

            return now.after(lastCancellationDate.getTime());
        }catch (ParseException e){
            Log.d("Err","parse Err");
        }
        return true;
    }

    private void openChatFragment() {
        if (providerId == null) {
            Toast.makeText(getContext(), "Provider ID not available", Toast.LENGTH_SHORT).show();
            return;
        }
        ChatFragment chatFragment = ChatFragment.newInstance(providerId);
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
        utilityService.getUtilityVersionById( id, new Callback<UtilityResponse>() {
            @Override
            public void onResponse(Call<UtilityResponse> call, Response<UtilityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UtilityResponse utility = response.body();
                    Log.d("AssetOverviewFragment", "Received utility: " + utility.getName());
                    populateUtilityData(utility);
                    if(eventId != null){
                        reservationCard.setVisibility(View.VISIBLE);
                        setupReservationData(getView());
                        setupCancelReservationButton(response.body());
                    }
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
            public void onFailure(Call<UtilityResponse> call, Throwable t) {
                Log.e("AssetOverviewFragment", "Request failed: " + t.getMessage(), t);
            }
        });
    }

    private void getProductById(String token, String id) {
        productService.getProductById(token, id, new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse product = response.body();
                    Log.d("AssetOverviewFragment", "Received product: " + product.getName());
                    populateProductData(product);
                } else {
                    Log.e("error", response.errorBody() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("ERR", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void populateUtilityData(UtilityResponse utility) {
        assetNameTextView.setText(utility.getName());
        assetTypeTextView.setText("Utility");
        loadProviderInfo(utility.getProviderId().toString());
        this.providerId = utility.getProviderId().toString();

        String categoryName = utility.getCategory().getName();
        assetCategoryTextView.setText(categoryName);
        assetDescriptionTextView.setText(utility.getDescription());
        double price = utility.getPrice();
        double discount = utility.getDiscount();
        double actualPrice = price - (price * discount / 100);
        assetPriceTextView.setText(String.valueOf(price));
        assetDiscountTextView.setText(discount + "%");
        assetActualPriceTextView.setText(String.format("%.2f", actualPrice));
        utilityDetailsLayout.setVisibility(View.VISIBLE);
        assetDurationTextView.setText(utility.getDuration() + " minutes");
        assetBookingDeadlineTextView.setText(String.format("%s days before Event start", utility.getReservationTerm()));
        assetCancellationDeadlineTextView.setText(String.format("%s days before Event start", utility.getCancellationTerm()));
        availableTextView.setText(String.valueOf(utility.getAvailable()));
        fillImageViews(utility);
        setupButtonVisibility(utility);
    }

    private void fillImageViews(AssetResponse asset) {
        int i = 0;
        for(String image : asset.getImages()){
            ImageView currImg = assetImages.get(i);
            Glide.with(requireContext())
                    .load(NetworkUtils.replaceLocalhostWithDeviceIp(image))
                    .placeholder(R.drawable.placeholder_asset)
                    .error(R.drawable.placeholder_asset)// Optional placeholder while loading// Optional error image
                    .into(currImg);
            i++;
        }
        for (int j = i + 1; j<assetImages.size(); j++){
            assetImages.get(j).setVisibility(View.GONE);
        }
        if (i == 0){
            assetImages.get(i).setImageResource(R.drawable.placeholder_asset);
        }
    }

    private void loadProviderInfo(String providerId) {
        new UserService().loadProviderInfo(UUID.fromString(providerId), new Callback<ProviderInfoResponse>() {
            @Override
            public void onResponse(Call<ProviderInfoResponse> call, Response<ProviderInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProviderInfoResponse provider = response.body();
                    String fullName = provider.getFirstName() + " " + provider.getLastName();
                    providerName.setText(fullName);
                } else {
                    Log.e("ProviderInfo", "Failed to load provider info: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ProviderInfoResponse> call, Throwable t) {
                Log.e("ProviderInfo", "Error: " + t.getMessage(), t);
            }
        });
    }
    private void populateProductData(ProductResponse product) {
        assetNameTextView.setText(product.getName());
        assetTypeTextView.setText("Product");
        loadProviderInfo(product.getProviderId().toString());
        this.providerId = product.getProviderId().toString();
        assetCategoryTextView.setText(product.getCategory().getName());
        assetDescriptionTextView.setText(product.getDescription());
        double price = product.getPrice();
        double discount = product.getDiscount();
        double actualPrice = price - (price * discount / 100);
        assetPriceTextView.setText(String.valueOf(price));
        assetDiscountTextView.setText(discount + "%");
        assetActualPriceTextView.setText(String.format("%.2f", actualPrice));
        utilityDetailsLayout.setVisibility(View.GONE);
        availableTextView.setText(String.valueOf(product.getAvailable()));
        fillImageViews(product);
        setupButtonVisibility(product);

    }
    private void setupButtonVisibility(AssetResponse asset) {
        boolean isProvider = asset.getProviderId().toString().equals(JwtTokenUtil.getUserId());
        chatButton.setVisibility(isProvider ? View.GONE : View.VISIBLE);
        buyProductButton.setVisibility(asset instanceof ProductResponse && ((ProductResponse) asset).getAvailable()  && !isProvider  ? View.VISIBLE : View.GONE);
        reserveUtilityButton.setVisibility(asset instanceof UtilityResponse && !isProvider ? View.VISIBLE  : View.GONE);
        reserveUtilityButton.setOnClickListener(v-> {
            assert asset instanceof UtilityResponse;
            buyAsset((UtilityResponse) asset);
        });
        buyProductButton.setOnClickListener(v -> buyAsset(null));
    }

    private void buyAsset(UtilityResponse assetResponse) {
        BuyAssetFragment buyAssetFragment = BuyAssetFragment.newInstance(String.valueOf(assetResponse == null ? null : assetResponse.getReservationTerm()),assetId);
        buyAssetFragment.show(getParentFragmentManager(),null);
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