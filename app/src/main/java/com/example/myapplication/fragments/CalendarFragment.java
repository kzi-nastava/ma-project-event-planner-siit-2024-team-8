package com.example.myapplication.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapters.EventCardAdapter;
import com.example.myapplication.domain.Event;
import com.example.myapplication.domain.dto.EventCardResponse;
import com.example.myapplication.domain.dto.EventInfoResponse;
import com.example.myapplication.fragments.event.event_info.EventInfoFragment;
import com.example.myapplication.viewmodels.EventViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CalendarFragment extends Fragment {

    private MaterialCalendarView calendarView;

    private EventViewModel eventVM;

    private RecyclerView eventsRecyclerView;
    private EventCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        calendarView = view.findViewById(R.id.calendarView);
        setupRecyclerView(view);
        setupObserveUserEvents();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                updateEvents(date);
            }
        });
        fetchUserEvents();
    }

    private void updateEvents(CalendarDay date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<EventCardResponse> events = new ArrayList<>();
        try{
            for (EventInfoResponse e : eventVM.getUserEvents().getValue()){
                Date startDate = sdf.parse(e.getStartDate());
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startDate);

                CalendarDay day = CalendarDay.from(
                        startCal.get(Calendar.YEAR),
                        startCal.get(Calendar.MONTH) + 1,
                        startCal.get(Calendar.DAY_OF_MONTH)
                );
                if(day.equals(date)){
                    events.add(new EventCardResponse(e));
                }
            }
            adapter.setEvents(events);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupRecyclerView(View view) {
        eventsRecyclerView = view.findViewById(R.id.eventsRecylclerView);
        adapter = new EventCardAdapter(getContext());
        adapter.SetOnClick((MainActivity) getActivity(), getActivity().getSupportFragmentManager());
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupObserveUserEvents() {
        eventVM = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventVM.getUserEvents().observe(getViewLifecycleOwner(), eventInfoResponse -> {
            Set<CalendarDay> markedDates = new HashSet<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            for (EventInfoResponse event : eventInfoResponse) {
                try {
                    Date date = sdf.parse(event.getStartDate());
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(date);

                    CalendarDay day = CalendarDay.from(
                            startCal.get(Calendar.YEAR),
                            startCal.get(Calendar.MONTH) + 1,
                            startCal.get(Calendar.DAY_OF_MONTH)
                    );

                    markedDates.add(day);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Clear existing decorators first (optional but recommended)
            calendarView.removeDecorators();

            // Add decorator once with all dates
            calendarView.addDecorator(new EventDayDecorator(markedDates, Color.parseColor("#FFA364")));
        });
    }

    private void fetchUserEvents(){
            eventVM.fetchUserEvents(getContext());
    }
    public static class EventDayDecorator implements DayViewDecorator {

        private final HashSet<CalendarDay> dates;
        private final Drawable background;

        public EventDayDecorator(Set<CalendarDay> dates, int color) {
            this.dates = new HashSet<>(dates);
            this.background = new ColorDrawable(color);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(background);
        }
    }
}
