package com.example.myapplication.adapters;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.example.myapplication.domain.Role;

public class BindingAdapters {

    @BindingAdapter(value = {"userType","userTypeAttrChanged"}, requireAll = false)
    public static void bindProfileType(Spinner spinner, Role selectedType, final InverseBindingListener listener) {
        if (spinner.getAdapter() == null) {
            ArrayAdapter<Role> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, Role.values());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        if (selectedType != null) {
            int position = ((ArrayAdapter<Role>) spinner.getAdapter()).getPosition(selectedType);
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
    public static Role getUserType(Spinner spinner) {
        return (Role) spinner.getSelectedItem();
    }

    @BindingAdapter(value={"eventType","eventTypeAttrChanged"},requireAll = false)
    public static void bindEventType(Spinner spinner, Role selectedType, final InverseBindingListener listener) {
        if (spinner.getAdapter() == null) {
            ArrayAdapter<Role> adapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, Role.values());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        if (selectedType != null) {
            int position = ((ArrayAdapter<Role>) spinner.getAdapter()).getPosition(selectedType);
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

    @InverseBindingAdapter(attribute = "eventType")
    public static Role getEventType(Spinner spinner) {
        return (Role) spinner.getSelectedItem();
    }
}
