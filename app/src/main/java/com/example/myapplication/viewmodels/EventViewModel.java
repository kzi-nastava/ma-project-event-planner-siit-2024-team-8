package com.example.myapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.domain.Activity;
import com.example.myapplication.domain.Event;
import com.example.myapplication.domain.Location;
import com.example.myapplication.domain.PagedResponse;
import com.example.myapplication.domain.dto.CreateEventRequest;
import com.example.myapplication.domain.dto.EventCardResponse;
import com.example.myapplication.domain.dto.SearchEventsRequest;
import com.example.myapplication.services.ClientUtils;
import com.example.myapplication.services.EventService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<Event> event = new MutableLiveData<>();

    private final MutableLiveData<CreateEventRequest> createEventRequest = new MutableLiveData<>();

    private final MutableLiveData<Location> createLocationRequest = new MutableLiveData<>();

    private final MutableLiveData<List<EventCardResponse>> top5Events = new MutableLiveData<>();

    private final MutableLiveData<List<EventCardResponse>> currentEvents = new MutableLiveData<>();

    private final MutableLiveData<SearchEventsRequest> currentFilters = new MutableLiveData<>();
    private MutableLiveData<Long> totalElements = new MutableLiveData<>();

    private MutableLiveData<Integer> totalPages = new MutableLiveData<>();
    public EventViewModel(){
        event.setValue(new Event());
        createEventRequest.setValue(new CreateEventRequest());
        createLocationRequest.setValue(new Location());
        top5Events.setValue(new ArrayList<>());
        currentFilters.setValue(new SearchEventsRequest());
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

    public LiveData<SearchEventsRequest> getCurrentFilters() {return currentFilters;}

    public LiveData<List<EventCardResponse>> getCurrentEvents() {return currentEvents;}

    public LiveData<Long> getTotalElements() {return totalElements;}
    public LiveData<Integer> getTotalPages() {return totalPages;}

    public LiveData<List<EventCardResponse>> getTop5(){
        return top5Events;
    }

    public void setActivities(List<Activity> activities){
        this.event.getValue().setActivities(activities);
    }

    public void saveEvent(){
        Event event = this.event.getValue();

    }

    public void setCurrentFilters(SearchEventsRequest request){
        this.currentFilters.setValue(request);
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


    public void fetchEvents(int currentPage,int pageSize) {
        EventService eventService = new EventService();
        eventService.getEvents(pageSize, currentPage, new Callback<PagedResponse<EventCardResponse>>() {
            @Override
            public void onResponse(Call<PagedResponse<EventCardResponse>> call, Response<PagedResponse<EventCardResponse>> response) {
                assert response.body() != null;
                currentEvents.setValue(response.body().getContent());
                totalElements.setValue(response.body().getTotalElements());
            }

            @Override
            public void onFailure(Call<PagedResponse<EventCardResponse>> call, Throwable t) {

            }
        });
    }

    public void filterEvents(int currentPage,int pageSize){
        SearchEventsRequest request = currentFilters.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        String startDate = request.getStartDate() != null ? request.getStartDate().format(formatter) : null;
        String endDate = request.getEndDate() != null ? request.getEndDate().format(formatter) : null;

        Call<PagedResponse<EventCardResponse>> call = ClientUtils.eventAPIService.filterEvents(
                currentPage,
                pageSize,
                request.getName(),
                request.getEventTypes(),
                request.getLowerCapacity(),
                request.getUpperCapacity(),
                startDate,
                endDate,
                request.getSortParameter(),
                request.getAscending()
        );
       call.enqueue(new Callback<PagedResponse<EventCardResponse>>() {
            @Override
            public void onResponse(Call<PagedResponse<EventCardResponse>> call, Response<PagedResponse<EventCardResponse>> response) {
                assert response.body() != null;
                Log.d("body ",response.body().toString());
                currentEvents.setValue(response.body().getContent());
                totalElements.setValue(response.body().getTotalElements());
                totalPages.setValue(response.body().getTotalPages());
            }

            @Override
            public void onFailure(Call<PagedResponse<EventCardResponse>> call, Throwable t) {
                Log.d("error fetching events", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}