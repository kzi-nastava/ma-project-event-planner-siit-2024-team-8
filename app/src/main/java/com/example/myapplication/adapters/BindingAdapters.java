package com.example.myapplication.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.example.myapplication.domain.AssetCategory;
import com.example.myapplication.domain.EventType;
import com.example.myapplication.domain.Role;
import com.example.myapplication.services.AssetCategoryService;
import com.example.myapplication.services.EventTypeService;
import com.example.myapplication.utilities.JwtTokenUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindingAdapter("eventType")
    public static void setSelectedEventType(Spinner spinner, EventType selectedType) {
        if (spinner.getAdapter() == null) {
            EventTypeService service = new EventTypeService();
            try {
                List<EventType> eventTypes = service.getActiveEventTypes().get();

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        spinner.getContext(),
                        android.R.layout.simple_spinner_item,
                        getEventTypeNames(eventTypes)
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                if (selectedType != null) {
                    int position = getEventTypePosition(eventTypes, selectedType);
                    spinner.setSelection(position);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (selectedType != null) {
            int position = getEventTypePosition(
                    (List<EventType>) ((ArrayAdapter<String>) spinner.getAdapter()).getContext(),
                    selectedType
            );
            spinner.setSelection(position);
        }
    }

    @BindingAdapter("eventTypeAttrChanged")
    public static void setEventTypeChangeListener(Spinner spinner, final InverseBindingListener listener) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listener != null) {
                    listener.onChange(); // Notify the inverse binding system
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                if (listener != null) {
                    listener.onChange(); // Notify even when nothing is selected
                }
            }
        });
    }

    @InverseBindingAdapter(attribute = "eventType", event = "eventTypeAttrChanged")
    public static EventType getSelectedEventType(Spinner spinner) {
        int position = spinner.getSelectedItemPosition();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (adapter != null && position >= 0) {
            String selectedName = adapter.getItem(position);
            EventTypeService service = new EventTypeService();
            try {
                List<EventType> eventTypes = service.getActiveEventTypes().get();
                for (EventType type : eventTypes) {
                    if (type.getName().equals(selectedName)) {
                        return type;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * Utility: Get list of EventType names for the adapter.
     */
    private static List<String> getEventTypeNames(List<EventType> eventTypes) {
        List<String> names = new ArrayList<>();
        for (EventType type : eventTypes) {
            names.add(type.getName()); // Assuming EventType has a getName() method
        }
        return names;
    }

    private static int getEventTypePosition(List<EventType> eventTypes, EventType selectedType) {
        for (int i = 0; i < eventTypes.size(); i++) {
            if (eventTypes.get(i).equals(selectedType)) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }


    @BindingAdapter(value = {"assetCategory", "categoryType", "assetCategoryAttrChanged"}, requireAll = false)
    public static void bindAssetCategory(Spinner spinner, AssetCategory selectedCategory, String categoryType, final InverseBindingListener listener) {
        AssetCategoryService service = new AssetCategoryService();

        // Fetch active categories
        service.getActiveCategories(JwtTokenUtil.getToken(), new Callback<List<AssetCategory>>() {
            @Override
            public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                if (response.body() != null) {
                    // Filter categories based on the provided type
                    List<AssetCategory> filteredCategories = new ArrayList<>();
                    for (AssetCategory category : response.body()) {
                        if (categoryType == null || category.getType().equalsIgnoreCase(categoryType)) {
                            filteredCategories.add(category);
                        }
                    }

                    // Extract names for the spinner
                    List<String> categoryNames = getAssetCategoryNames(filteredCategories);

                    // Clear previous adapter
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
                    if (adapter != null) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }

                    // Create and set a new adapter
                    adapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, categoryNames) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Get the default view
                            View view = super.getView(position, convertView, parent);

                            // Set the tag for each item
                            AssetCategory assetCategory = filteredCategories.get(position);  // Get the AssetCategory for this position
                            view.setTag(assetCategory);  // Set the tag to the view

                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            // You can also set the tag in the dropdown view (if necessary)
                            View view = super.getDropDownView(position, convertView, parent);

                            AssetCategory assetCategory = filteredCategories.get(position);
                            view.setTag(assetCategory);

                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    // Set the selected category if it exists
                    if (selectedCategory != null) {
                        int position = getAssetCategoryPosition(filteredCategories, selectedCategory);
                        spinner.setSelection(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                Log.d("Error", "Failed to load categories: " + t.getMessage());
            }
        });

        // Listen for changes in selection and notify the InverseBindingListener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listener != null) {
                    listener.onChange(); // Notify the inverse binding system
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                if (listener != null) {
                    listener.onChange(); // Notify even when nothing is selected
                }
            }
        });
    }
    private static List<String> getAssetCategoryNames(List<AssetCategory> assetCategories) {
        List<String> names = new ArrayList<>();
        for (AssetCategory category : assetCategories) {
            names.add(category.getName()); // Assuming AssetCategory has a getName() method
        }
        return names;
    }

    // Utility: Get position of selected AssetCategory.
    private static int getAssetCategoryPosition(List<AssetCategory> assetCategories, AssetCategory selectedCategory) {
        for (int i = 0; i < assetCategories.size(); i++) {
            if (assetCategories.get(i).equals(selectedCategory)) {
                return i;
            }
        }
        return 0; // Default to the first item if not found
    }
    @BindingAdapter(value = {"assetCategoryType", "selectedAssetCategory", "assetCategoryAttrChanged"}, requireAll = false)
    public static void bindAssetCategoryByType(Spinner spinner, String assetCategoryType, AssetCategory selectedCategory, final InverseBindingListener listener) {
        AssetCategoryService service = new AssetCategoryService();

        // Fetch and filter Asset Categories based on type
        service.getActiveCategories(JwtTokenUtil.getToken(), new Callback<List<AssetCategory>>() {
            @Override
            public void onResponse(Call<List<AssetCategory>> call, Response<List<AssetCategory>> response) {
                if (response.body() != null) {
                    List<AssetCategory> allCategories = response.body();

                    List<AssetCategory> filteredCategories = new ArrayList<>();
                    for (AssetCategory category : allCategories) {
                        if (category.getType().equalsIgnoreCase(assetCategoryType)) {
                            filteredCategories.add(category);
                        }
                    }


                    List<String> categoryNames = getAssetCategoryNames(filteredCategories);


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            spinner.getContext(),
                            android.R.layout.simple_spinner_item,
                            categoryNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    if (selectedCategory != null) {
                        int position = getAssetCategoryPosition(filteredCategories, selectedCategory);
                        spinner.setSelection(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                Log.d("Error", "Failed to load categories: " + t.getMessage());
            }
        });

        // Listen for selection changes
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (listener != null) {
                    listener.onChange();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                if (listener != null) {
                    listener.onChange();
                }
            }
        });
    }


    //INTEGER TO EDIT TEXT TEXT AND REVERSE
    @BindingAdapter("android:text")
    public static void setIntText(EditText view, int value) {
        if (!view.getText().toString().equals(String.valueOf(value))) {
            view.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    public static int getIntText(EditText view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @BindingAdapter("android:textAttrChanged")
    public static void setTextWatcher(EditText view, final InverseBindingListener listener) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onChange();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

}
