package com.example.myapplication.fragments.asset;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;
import com.google.android.material.textfield.TextInputEditText;

public class EditAssetCategoryPopup extends DialogFragment {

    private TextInputEditText nameEditText;
    private TextInputEditText descriptionEditText;
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Button approveButton;
    private RadioGroup categoryTypeRadioGroup;
    private RadioButton productRadioButton;
    private RadioButton utilityRadioButton;
    private AssetCategory category;
    private EditAssetCategoryListener listener;
    private boolean isAddMode;

    public interface EditAssetCategoryListener {
        void onSaveCategory(AssetCategory category);
        void onDeleteCategory(AssetCategory category);
        void onCancel();
        void onApproveCategory(AssetCategory category);
        void onCategoryUpdated();
    }

    public static EditAssetCategoryPopup newInstance(AssetCategory category, boolean isAddMode) {
        EditAssetCategoryPopup fragment = new EditAssetCategoryPopup();
        Bundle args = new Bundle();
        args.putSerializable("category", category);
        args.putBoolean("isAddMode", isAddMode);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(EditAssetCategoryListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (getArguments() != null) {
            category = (AssetCategory) getArguments().getSerializable("category");
            isAddMode = getArguments().getBoolean("isAddMode", false);
        }

        View view = getLayoutInflater().inflate(R.layout.fragment_edit_asset_category_popup, null);

        nameEditText = view.findViewById(R.id.editCategoryName);
        descriptionEditText = view.findViewById(R.id.editCategoryDescription);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        approveButton = view.findViewById(R.id.approveButton);
        categoryTypeRadioGroup = view.findViewById(R.id.categoryTypeRadioGroup);
        productRadioButton = view.findViewById(R.id.productRadioButton);
        utilityRadioButton = view.findViewById(R.id.utilityRadioButton);

        if (isAddMode) {
            deleteButton.setVisibility(View.GONE);
            approveButton.setVisibility(View.GONE);
            saveButton.setText("Add");
            productRadioButton.setChecked(true);
            nameEditText.setEnabled(true);
            descriptionEditText.setEnabled(true);
            productRadioButton.setEnabled(true);
            utilityRadioButton.setEnabled(true);
        } else {
            if (category != null) {
                if (category.getActive()) {
                    approveButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);

                    nameEditText.setEnabled(true);
                    descriptionEditText.setEnabled(true);
                    productRadioButton.setEnabled(false);
                    utilityRadioButton.setEnabled(false);

                    nameEditText.setText(category.getName());
                    descriptionEditText.setText(category.getDescription());
                    if ("Product".equals(category.getType())) {
                        productRadioButton.setChecked(true);
                    } else {
                        utilityRadioButton.setChecked(true);
                    }
                } else {
                    approveButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);

                    nameEditText.setEnabled(false);
                    descriptionEditText.setEnabled(false);
                    productRadioButton.setEnabled(false);
                    utilityRadioButton.setEnabled(false);

                    nameEditText.setText(category.getName());
                    descriptionEditText.setText(category.getDescription());
                    if ("Product".equals(category.getType())) {
                        productRadioButton.setChecked(true);
                    } else {
                        utilityRadioButton.setChecked(true);
                    }
                }
            }
        }

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Cancel Button
        cancelButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        });

        // Delete Button
        deleteButton.setOnClickListener(v -> {
            if (category != null && listener != null) {
                listener.onDeleteCategory(category);
            }
            dismiss();
        });

        // Approve Button (only visible when in edit mode for pending categories)
        approveButton.setOnClickListener(v -> {
            if (category != null && listener != null) {
                listener.onApproveCategory(category);
            }
            dismiss();
        });

        // Save Button (Add or Update based on mode)
        // Inside the saveButton click listener in EditAssetCategoryPopup
        saveButton.setOnClickListener(v -> {
            if (nameEditText.getText().toString().isEmpty() || descriptionEditText.getText().toString().isEmpty()) {
                // Handle empty fields if needed
                return;
            }

            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String type = productRadioButton.isChecked() ? "Product" : "Utility";

            AssetCategory updatedCategory = new AssetCategory(category.getId(), name, description, type, category.getActive());

            if (isAddMode) {
                if (listener != null) {
                    listener.onSaveCategory(updatedCategory);  // Save new category
                }
            } else {
                if (listener != null) {
                    listener.onSaveCategory(updatedCategory);  // Update existing category
                }
            }

            // Notify the parent fragment to refresh the categories after saving
            if (listener != null) {
                listener.onCategoryUpdated();
            }

            dismiss();
        });


        return dialog;
    }
}
