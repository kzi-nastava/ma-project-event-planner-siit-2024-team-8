package com.example.myapplication.adapters;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.example.myapplication.domain.UserType;
public class BindingAdapters {

    @BindingAdapter(value = {"userType","userTypeAttrChanged"}, requireAll = false)
    public static void bindProfileType(Spinner spinner, UserType selectedType, final InverseBindingListener listener) {
        if (spinner.getAdapter() == null) {
            ArrayAdapter<UserType> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, UserType.values());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        if (selectedType != null) {
            int position = ((ArrayAdapter<UserType>) spinner.getAdapter()).getPosition(selectedType);
            spinner.setSelection(position);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                listener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                listener.onChange();
            }
        });
    }
    @InverseBindingAdapter(attribute = "userType")
    public static UserType getUserType(Spinner spinner) {
        return (UserType) spinner.getSelectedItem();
    }
}
