package com.example.myapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.EventDTO;

import java.util.List;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<List<EventDTO>> event = new MutableLiveData<>();



    // TODO: Implement the ViewModel
}