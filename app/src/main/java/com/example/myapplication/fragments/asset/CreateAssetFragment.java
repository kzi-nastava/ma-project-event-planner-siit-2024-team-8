package com.example.myapplication.fragments.asset;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.enumerations.OfferingType;
import com.example.myapplication.domain.Product;
import com.example.myapplication.domain.Utility;
import com.example.myapplication.fragments.AllSolutionsFragment;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.ProductService;
import com.example.myapplication.services.UtilityService;
import com.example.myapplication.utilities.FileUtils;
import com.example.myapplication.utilities.JwtTokenUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateAssetFragment extends Fragment {
    private static final int PICK_IMAGES_REQUEST = 1;
    private List<Uri> selectedImageUris = new ArrayList<>();
    private MaterialButton uploadImagesButton;
    private RadioGroup assetTypeGroup;
    private String selectedCategoryId;
    private View utilitySpecificFields;
    private TextView bookingDeadlineTextView, cancellationDeadlineTextView;
    private Spinner assetCategorySpinner;
    private UtilityService utilityService;
    private ProductService productService;
    private AssetCategoryService assetCategoryService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_asset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadImagesButton = view.findViewById(R.id.uploadImagesButton);
        uploadImagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, PICK_IMAGES_REQUEST);
        });

        assetTypeGroup = view.findViewById(R.id.assetTypeGroup);
        utilitySpecificFields = view.findViewById(R.id.utilitySpecificFieldsLayout);
        bookingDeadlineTextView = view.findViewById(R.id.bookingDateTextView);
        cancellationDeadlineTextView = view.findViewById(R.id.cancellationDateTextView);
        assetCategorySpinner = view.findViewById(R.id.assetCategorySpinner);
        MaterialButton cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> requireActivity().onBackPressed());

        MaterialButton saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                createAsset();
            }
        });

        utilityService = new UtilityService();
        productService = new ProductService();
        assetCategoryService = new AssetCategoryService();

        ((RadioButton) view.findViewById(R.id.assetTypeProduct)).setChecked(true);
        toggleUtilitySpecificFields(false);

        assetTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.assetTypeService) {
                toggleUtilitySpecificFields(true);
                loadCategories(true); // Load utility categories
            } else {
                toggleUtilitySpecificFields(false);
                loadCategories(false); // Load product categories
            }
        });
        bookingDeadlineTextView.setOnClickListener(v -> showDatePickerDialog(bookingDeadlineTextView));
        cancellationDeadlineTextView.setOnClickListener(v -> showDatePickerDialog(cancellationDeadlineTextView));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUris.clear();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                selectedImageUris.add(data.getData());
            }
        }
    }

    private void toggleUtilitySpecificFields(boolean show) {
        utilitySpecificFields.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean validateInputs() {
        String name = ((EditText) requireView().findViewById(R.id.assetNameEditText)).getText().toString();
        String description = ((EditText) requireView().findViewById(R.id.assetDescriptionEditText)).getText().toString();
        String priceStr = ((EditText) requireView().findViewById(R.id.assetPriceEditText)).getText().toString();
        String discountStr = ((EditText) requireView().findViewById(R.id.assetDiscountEditText)).getText().toString();
        String durationStr = ((EditText) requireView().findViewById(R.id.assetDurationEditText)).getText().toString();

        if (name.isEmpty() || name.length() > 30 || description.isEmpty() || priceStr.isEmpty() || discountStr.isEmpty()) {
            showToast("Please fill in all fields properly.");
            return false;
        }

        try {
            Double.parseDouble(priceStr);
            Double.parseDouble(discountStr);
        } catch (NumberFormatException e) {
            showToast("Invalid price or discount value.");
            return false;
        }

        if (Double.parseDouble(discountStr) < 0 || Double.parseDouble(discountStr) > 100) {
            showToast("Discount must be between 0 and 100.");
            return false;
        }

        if (selectedCategoryId == null || selectedCategoryId.isEmpty() || ("00000000-0000-0000-0000-000000000000".equals(selectedCategoryId) &&
                !validateCategoryFields())) {
            showToast("Please select a valid category.");
            return false;
        }

        if (assetTypeGroup.getCheckedRadioButtonId() == R.id.assetTypeService && (durationStr.isEmpty() || Integer.parseInt(durationStr) < 1 || Integer.parseInt(durationStr) > 999)) {
            showToast("Duration must be between 1 and 999.");
            return false;
        }

        return true;
    }

    private boolean validateCategoryFields() {
        String categoryName = ((EditText) requireView().findViewById(R.id.categoryNameEditText)).getText().toString();
        String categoryDesc = ((EditText) requireView().findViewById(R.id.categoryDescEditText)).getText().toString();
        return !categoryName.isEmpty() && !categoryDesc.isEmpty();
    }

    private void createAsset() {
        String name = getFieldText(R.id.assetNameEditText);
        String description = getFieldText(R.id.assetDescriptionEditText);
        double price = Double.parseDouble(getFieldText(R.id.assetPriceEditText));
        double discount = Double.parseDouble(getFieldText(R.id.assetDiscountEditText));
        boolean visible = ((MaterialSwitch) requireView().findViewById(R.id.visibilitySwitch)).isChecked();
        boolean available = ((MaterialSwitch) requireView().findViewById(R.id.availabilitySwitch)).isChecked();
        String suggestedCategoryName = "", suggestedCategoryDesc = "";

        if ("00000000-0000-0000-0000-000000000000".equals(selectedCategoryId)) {
            suggestedCategoryName = getFieldText(R.id.categoryNameEditText);
            suggestedCategoryDesc = getFieldText(R.id.categoryDescEditText);
        }

        String token = "Bearer " + JwtTokenUtil.getToken();
        List<MultipartBody.Part> images = FileUtils.convertUrisToMultipart(requireContext(), selectedImageUris);
        if (assetTypeGroup.getCheckedRadioButtonId() == R.id.assetTypeService) {
            createUtility(token, name, description, images, price, discount, visible, available, suggestedCategoryName, suggestedCategoryDesc);
        } else {
            createProduct(token, name, description, images, price, discount, visible, available, suggestedCategoryName, suggestedCategoryDesc);
        }
    }

    private String getFieldText(int id) {
        return ((EditText) requireView().findViewById(id)).getText().toString();
    }

    private void createUtility(String token, String name, String description, List<MultipartBody.Part> images, double price, double discount, boolean visible, boolean available, String suggestedCategoryName, String suggestedCategoryDesc) {
        int duration = Integer.parseInt(getFieldText(R.id.assetDurationEditText));
        String reservationTerm = bookingDeadlineTextView.getText().toString();
        String cancellationTerm = cancellationDeadlineTextView.getText().toString();
        boolean confirmationMethod = ((RadioButton) requireView().findViewById(R.id.confirmationMethodManual)).isChecked();

        utilityService.createUtility(token, name, description, selectedCategoryId, null, price, discount, visible, available, duration,
                reservationTerm, cancellationTerm, confirmationMethod, images, suggestedCategoryName, suggestedCategoryDesc, new Callback<Utility>() {
                    @Override
                    public void onResponse(Call<Utility> call, Response<Utility> response) {
                        if (response.isSuccessful()) {
                            showToast("Utility created successfully.");
                            navigateToAllSolutionsFragment();
                        } else {
                            showToast("Failed to create utility.");
                        }
                    }
                    @Override
                    public void onFailure(Call<Utility> call, Throwable t) {
                        showToast("Error: " + t.getMessage());
                        navigateToAllSolutionsFragment();
                    }
                });
    }

    private void createProduct(String token, String name, String description, List<MultipartBody.Part> images, double price, double discount, boolean visible, boolean available, String suggestedCategoryName, String suggestedCategoryDesc) {
        productService.createProduct(token, name, description, selectedCategoryId, null, price, discount, visible, available, images, suggestedCategoryName, suggestedCategoryDesc,
                new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            showToast("Product created successfully.");
                            requireActivity().onBackPressed();
                            navigateToAllSolutionsFragment();
                        }
                    }
                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.e("createasset",  "failure: " + t.getMessage());
                        navigateToAllSolutionsFragment();
                    }
                });
    }

    private void navigateToAllSolutionsFragment() {
        Bundle args = new Bundle();
        args.putSerializable("offeringType", OfferingType.ASSET);

        Fragment fragment = new AllSolutionsFragment();
        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadCategories(boolean isUtility) {
        if (isUtility) {
            loadUtilityCategories();
        } else {
            loadProductCategories();
        }
    }

    private void loadProductCategories() {
        String token = "Bearer " + JwtTokenUtil.getToken();
        assetCategoryService.getActiveProductCategories(token, new Callback<List<AssetCategory>>() {
            @Override
            public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AssetCategory> productCategories = response.body();
                    AssetCategory noneOfTheAbove = new AssetCategory();
                    noneOfTheAbove.setName("None of the Above");
                    productCategories.add(0, noneOfTheAbove);

                    ArrayAdapter<AssetCategory> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, productCategories) {
                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setText(productCategories.get(position).getName());
                            return view;
                        }
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setText(productCategories.get(position).getName());
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    assetCategorySpinner.setAdapter(adapter);

                    assetCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                            AssetCategory selectedCategory = (AssetCategory) parentView.getAdapter().getItem(position);
                            if (selectedCategory != null) {
                                if ("None of the Above".equals(selectedCategory.getName())) {
                                    toggleCategoryFields(true);
                                    selectedCategoryId = "00000000-0000-0000-0000-000000000000";
                                } else {
                                    toggleCategoryFields(false);
                                    selectedCategoryId = selectedCategory.getId().toString();
                                }
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Handle no selection
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void loadUtilityCategories() {
        String token = "Bearer " + JwtTokenUtil.getToken();
        assetCategoryService.getActiveUtilityCategories(token, new Callback<List<AssetCategory>>() {
            @Override
            public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AssetCategory> utilityCategories = response.body();

                    AssetCategory noneOfTheAbove = new AssetCategory();
                    noneOfTheAbove.setName("None of the Above");
                    utilityCategories.add(0, noneOfTheAbove);

                    ArrayAdapter<AssetCategory> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, utilityCategories) {
                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setText(utilityCategories.get(position).getName());
                            return view;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setText(utilityCategories.get(position).getName());
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    assetCategorySpinner.setAdapter(adapter);

                    assetCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                            AssetCategory selectedCategory = (AssetCategory) parentView.getAdapter().getItem(position);
                            if (selectedCategory != null) {
                                if ("None of the Above".equals(selectedCategory.getName())) {
                                    toggleCategoryFields(true);
                                    selectedCategoryId = "00000000-0000-0000-0000-000000000000";
                                } else {
                                    toggleCategoryFields(false);
                                    selectedCategoryId = selectedCategory.getId().toString();
                                }
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Handle no selection
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                // Handle failure
            }
        });
    }
    private void toggleCategoryFields(boolean show) {
        View categoryNameField = requireView().findViewById(R.id.categoryNameEditText);
        View categoryDescField = requireView().findViewById(R.id.categoryDescEditText);

        categoryNameField.setVisibility(show ? View.VISIBLE : View.GONE);
        categoryDescField.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    private void showDatePickerDialog(TextView targetTextView) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) ->
                targetTextView.setText(dayOfMonth + "/" + (month + 1) + "/" + year), calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}