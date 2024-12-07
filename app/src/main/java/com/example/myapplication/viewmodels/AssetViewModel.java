package com.example.myapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.AssetDTO;

import java.util.List;

public class AssetViewModel extends ViewModel {
    private final MutableLiveData<List<AssetDTO>> asset = new MutableLiveData<>();



    // TODO: Implement the ViewModel
}