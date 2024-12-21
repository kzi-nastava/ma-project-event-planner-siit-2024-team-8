package com.example.myapplication.fragments.asset;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Utility;
import com.example.myapplication.services.ProductService;
import com.example.myapplication.services.UtilityService;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAssetFragment extends Fragment {
    private String assetId;
    private String assetType;
    private LinearLayout utilityDetailsLayout;

    private EditText assetNameEditText, descriptionEditText, priceEditText, discountEditText, durationEditText;
    private Switch visibilitySwitch, availabilitySwitch;
    private TextView bookingDeadlineTextView, cancellationDeadlineTextView;

    private RadioButton confirmationMethodAutomatic, confirmationMethodManual;

    private MaterialButton saveButton;

    private UtilityService utilityService;
    private ProductService productService;

    public static EditAssetFragment newInstance(String assetId, String assetType) {
        EditAssetFragment fragment = new EditAssetFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_edit, container, false);

        initializeViews(view);

        String authHeader = "Bearer " + JwtTokenUtil.getToken();
        if ("UTILITY".equals(assetType)) {
            getUtilityById(authHeader, assetId);
        } else if ("PRODUCT".equals(assetType)) {
            getProductById(authHeader, assetId);
        }

        saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveAsset(authHeader);
            }
        });

        return view;
    }

    private void initializeViews(View view) {
        utilityDetailsLayout = view.findViewById(R.id.utilityDetailsLayout);
        assetNameEditText = view.findViewById(R.id.assetNameEditText);
        descriptionEditText = view.findViewById(R.id.assetDescriptionEditText);
        priceEditText = view.findViewById(R.id.assetPriceEditText);
        discountEditText = view.findViewById(R.id.assetDiscountEditText);
        durationEditText = view.findViewById(R.id.assetDurationEditText);
        visibilitySwitch = view.findViewById(R.id.visibilitySwitch);
        availabilitySwitch = view.findViewById(R.id.availabilitySwitch);
        bookingDeadlineTextView = view.findViewById(R.id.bookingDateTextView);
        cancellationDeadlineTextView = view.findViewById(R.id.cancellationDateTextView);
        saveButton = view.findViewById(R.id.saveButton);
        bookingDeadlineTextView.setOnClickListener(v -> showDatePickerDialog(bookingDeadlineTextView));
        cancellationDeadlineTextView.setOnClickListener(v -> showDatePickerDialog(cancellationDeadlineTextView));
        confirmationMethodAutomatic = view.findViewById(R.id.confirmationMethodAutomatic);
        confirmationMethodManual = view.findViewById(R.id.confirmationMethodManual);
    }

    private boolean validateInputs() {
        String name = assetNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String discountStr = discountEditText.getText().toString().trim();
        String durationStr = durationEditText.getText().toString().trim();
        String bookingDeadline = bookingDeadlineTextView.getText().toString().trim();
        String cancellationDeadline = cancellationDeadlineTextView.getText().toString().trim();

        if (name.isEmpty() || name.length() > 20 || !name.matches("^[a-zA-Z\\s]+$")) {
            Toast.makeText(getContext(), "Name should contain only letters and spaces, and be less than 20 characters.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Description is required.", Toast.LENGTH_LONG).show();
            return false;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Price must be a valid number.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (price < 1 || price > 999999) {
            Toast.makeText(getContext(), "Price must be between 1 and 999999.", Toast.LENGTH_LONG).show();
            return false;
        }

        double discount;
        try {
            discount = Double.parseDouble(discountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Discount must be a valid number.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (discount < 0 || discount > 100) {
            Toast.makeText(getContext(), "Discount must be between 0 and 100.", Toast.LENGTH_LONG).show();
            return false;
        }

        if ("UTILITY".equals(assetType)) {
            int duration;
            try {
                duration = Integer.parseInt(durationStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Duration must be a valid number.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (duration < 1 || duration > 999) {
                Toast.makeText(getContext(), "Duration must be between 1 and 999.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (bookingDeadline.isEmpty() || cancellationDeadline.isEmpty()) {
                Toast.makeText(getContext(), "Please select both reservation and cancellation terms.", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void navigateToAssetDetailsFragment() {
        AssetDetailsFragment assetDetailsFragment = AssetDetailsFragment.newInstance(this.assetId, this.assetType);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.main, assetDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void saveAsset(String token) {
        String name = assetNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        double price = Double.parseDouble(priceEditText.getText().toString().trim());
        double discount = Double.parseDouble(discountEditText.getText().toString().trim());
        boolean visible = visibilitySwitch.isChecked();
        boolean available = availabilitySwitch.isChecked();

        if ("UTILITY".equals(assetType)) {
            int duration = Integer.parseInt(durationEditText.getText().toString().trim());
            String reservationTerm = bookingDeadlineTextView.getText().toString().trim();
            String cancellationTerm = cancellationDeadlineTextView.getText().toString().trim();
            boolean manualConfirmation = confirmationMethodManual.isChecked();

            utilityService.updateUtility(token, assetId, name, description, price, discount, visible, available, duration,
                    reservationTerm, cancellationTerm, manualConfirmation, null, new Callback<Utility>() {
                        @Override
                        public void onResponse(Call<Utility> call, Response<Utility> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Utility updated successfully.", Toast.LENGTH_SHORT).show();
                                navigateToAssetDetailsFragment();
                            } else {
                                Toast.makeText(getContext(), "Failed to update utility.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Utility> call, Throwable t) {
                            Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if ("PRODUCT".equals(assetType)) {
            productService.updateProduct(token, assetId, name, description, price, discount, visible, available, null, new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Product updated successfully.", Toast.LENGTH_SHORT).show();
                        navigateToAssetDetailsFragment();
                    } else {
                        Toast.makeText(getContext(), "Failed to update product.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getUtilityById(String token, String id) {
        utilityService.getUtilityById(token, id, new Callback<Utility>() {
            @Override
            public void onResponse(Call<Utility> call, Response<Utility> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateUtilityData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Utility> call, Throwable t) {
                Log.e("EditAssetFragment", "Error fetching utility: " + t.getMessage());
            }
        });
    }

    private void getProductById(String token, String id) {
        productService.getProductById(token, id, new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateProductData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("EditAssetFragment", "Error fetching product: " + t.getMessage());
            }
        });
    }

    private void populateUtilityData(Utility utility) {
        utilityDetailsLayout.setVisibility(View.VISIBLE);
        assetNameEditText.setText(utility.getName());
        descriptionEditText.setText(utility.getDescription());
        priceEditText.setText(String.valueOf(utility.getPrice()));
        discountEditText.setText(String.valueOf(utility.getDiscount()));
        durationEditText.setText(String.valueOf(utility.getDuration()));

        visibilitySwitch.setChecked(utility.isVisible());
        availabilitySwitch.setChecked(utility.isAvailable());

        bookingDeadlineTextView.setText(utility.getReservationTerm());
        cancellationDeadlineTextView.setText(utility.getCancellationTerm());

        if (utility.isManuelConfirmation()) {
            confirmationMethodManual.setChecked(true);
        } else {
            confirmationMethodAutomatic.setChecked(true);
        }
    }

    private void populateProductData(Product product) {
        assetNameEditText.setText(product.getName());
        descriptionEditText.setText(product.getDescription());
        priceEditText.setText(String.valueOf(product.getPrice()));
        discountEditText.setText(String.valueOf(product.getDiscount()));
        visibilitySwitch.setChecked(product.isVisible());
        availabilitySwitch.setChecked(product.isAvailable());
        utilityDetailsLayout.setVisibility(View.GONE);
    }

    private void showDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    textView.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);

        datePickerDialog.show();
    }
}
