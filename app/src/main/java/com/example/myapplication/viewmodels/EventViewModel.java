package com.example.myapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.Activity;
import com.example.myapplication.domain.Event;

import java.util.List;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<Event> event = new MutableLiveData<>();

    public EventViewModel(){
        event.setValue(new Event());
    }

    public LiveData<Event> getEvent(){
        return event;
    }
    public List<Activity> getActivities(){
        return this.event.getValue().getActivities();
    }

    public void setActivities(List<Activity> activities){
        this.event.getValue().setActivities(activities);
    }

    public void saveEvent(){
        Event event = this.event.getValue();


    }


}