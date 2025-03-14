package com.example.myapplication.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAssetsFilterBottomSheetBinding;
import com.example.myapplication.domain.enumerations.AssetType;
import com.example.myapplication.viewmodels.AssetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetsFilterFragmentBottomSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetsFilterFragmentBottomSheet extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AssetViewModel assetViewModel;



    public AssetsFilterFragmentBottomSheet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssetsFilterFragmentBottomSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static AssetsFilterFragmentBottomSheet newInstance(String param1, String param2) {
        AssetsFilterFragmentBottomSheet fragment = new AssetsFilterFragmentBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       FragmentAssetsFilterBottomSheetBinding binding = FragmentAssetsFilterBottomSheetBinding.inflate(inflater, container, false);
       assetViewModel = new ViewModelProvider(requireActivity()).get(AssetViewModel.class);

       binding.setAssetVM(assetViewModel);

        RadioGroup assetType = binding.assetTypeRadioGroup;
        assetType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.UTILITY){
                    assetViewModel.getCurrentFilters().getValue().setAssetType(AssetType.SERVICE);
                }else{
                    assetViewModel.getCurrentFilters().getValue().setAssetType(AssetType.PRODUCT);
                }
            }
        });

        EditText lowPrice = binding.lowerPrice;
        EditText higherPrice = binding.highPrice;
        RangeSlider priceSlider = binding.priceSlider;

        priceSlider.setValues(100f,1000f);
        priceSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.GRAY));  // Inactive track
        priceSlider.setTrackActiveTintList(ContextCompat.getColorStateList(requireContext(),R.color.olive));    // Active track
        priceSlider.setThumbTintList(ColorStateList.valueOf(Color.BLACK));           // Thumb color
        priceSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            lowPrice.setText(String.valueOf(values.get(0).intValue()));
            higherPrice.setText(String.valueOf(values.get(1).intValue()));
        });

        RangeSlider gradeSlider = binding.gradeSlider;

        gradeSlider.setValues(1f,5f);
        gradeSlider.setTrackInactiveTintList(ColorStateList.valueOf(Color.GRAY));  // Inactive track
        gradeSlider.setTrackActiveTintList(ContextCompat.getColorStateList(requireContext(),R.color.olive));    // Active track
        gradeSlider.setThumbTintList(ColorStateList.valueOf(Color.BLACK));           // Thumb color
        gradeSlider.addOnChangeListener((slider,value,fromUser) -> {
            List<Float> values = slider.getValues();
            assetViewModel.getCurrentFilters().getValue().setGradeLow(values.get(0).intValue());
            assetViewModel.getCurrentFilters().getValue().setGradeHigh(values.get(1).intValue());
        });


        RecyclerView assetCategories = binding.assetCategories;



       return binding.getRoot();
    }
}