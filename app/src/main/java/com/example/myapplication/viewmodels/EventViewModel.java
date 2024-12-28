package com.example.myapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.Activity;
import com.example.myapplication.domain.Event;
import com.example.myapplication.domain.Location;
import com.example.myapplication.domain.dto.CreateEventRequest;
import com.example.myapplication.domain.dto.EventCardResponse;
import com.example.myapplication.services.EventService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<CreateEventRequest> createEventRequest = new MutableLiveData<>();

    private final MutableLiveData<Location> createLocationRequest = new MutableLiveData<>();

    private final MutableLiveData<List<EventCardResponse>> top5Events = new MutableLiveData<>();
    public EventViewModel(){
        event.setValue(new Event());
        createEventRequest.setValue(new CreateEventRequest());
        createLocationRequest.setValue(new Location());
        top5Events.setValue(new ArrayList<>());
    }

    public LiveData<Event> getEvent(){
        return event;
    }
    public List<Activity> getActivities(){
        return this.event.getValue().getActivities();
    }

    public LiveData<CreateEventRequest> getCreateEventRequest() {return createEventRequest;}
    public List<Activity> getRequestActivities() {return createEventRequest.getValue().getAgenda();}

    public LiveData<Location> getCreateLocationRequest(){return createLocationRequest;}

    public LiveData<List<EventCardResponse>> getTop5(){
        return top5Events;
    }

    public void setActivities(List<Activity> activities){
        this.event.getValue().setActivities(activities);
    }

    public void saveEvent(){
        Event event = this.event.getValue();

    }

    public void fetchTop5Events(){
        EventService eventService = new EventService();
        eventService.getTop5Events(new Callback<List<EventCardResponse>>() {
            @Override
            public void onResponse(Call<List<EventCardResponse>> call, Response<List<EventCardResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    top5Events.setValue(response.body());
                    Log.d("Response",response.body().toString());
                    Log.d("SUCCESS", "Response received");
                } else {
                    Log.d("ERROR", "Error fetching top 5");
                }
            }

            @Override
            public void onFailure(Call<List<EventCardResponse>> call, Throwable t) {
                Log.d("ERROR", "Error", t);
            }
        });
    }


}