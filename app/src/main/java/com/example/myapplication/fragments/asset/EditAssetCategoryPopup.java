package com.example.myapplication.fragments.asset;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.domain.AssetCategory;

public class EditAssetCategoryPopup extends DialogFragment {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private AssetCategory category;
    private EditAssetCategoryListener listener;
    private boolean isAddMode;

    public interface EditAssetCategoryListener {
        void onSaveCategory(AssetCategory category);
        void onDeleteCategory(AssetCategory category);
        void onCancel();
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

    @NonNull
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

        if (isAddMode) {
            deleteButton.setVisibility(View.GONE);
            saveButton.setText("Add");
        } else {
            // In Edit mode, show delete button
            deleteButton.setVisibility(View.VISIBLE);
            saveButton.setText("Save");
            nameEditText.setText(category.getName());
            descriptionEditText.setText(category.getDescription());
        }

        saveButton.setOnClickListener(v -> {
            category.setName(nameEditText.getText().toString());
            category.setDescription(descriptionEditText.getText().toString());
            if (listener != null) {
                listener.onSaveCategory(category);
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteCategory(category);
            }
            dismiss();
        });

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }
}

