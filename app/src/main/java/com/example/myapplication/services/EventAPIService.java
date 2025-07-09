package com.example.myapplication.services;
import com.example.myapplication.domain.ApiResponse;
import com.example.myapplication.domain.PagedResponse;
import com.example.myapplication.domain.dto.AgendaUpdateRequest;
import com.example.myapplication.domain.dto.CreateEventRequest;
import com.example.myapplication.domain.dto.EventCardResponse;
import com.example.myapplication.domain.dto.EventInfoResponse;
import com.example.myapplication.domain.dto.EventSignupRequest;
import com.example.myapplication.domain.dto.EventUpdateRequest;
import com.example.myapplication.domain.dto.GuestResponse;
import com.example.myapplication.domain.dto.GuestlistUpdateRequest;
import com.example.myapplication.domain.dto.InvitationUpdateRequest;
import com.example.myapplication.domain.dto.SearchEventsRequest;
import com.example.myapplication.domain.enumerations.EventSortParameter;

import java.util.List;

import javax.xml.parsers.SAXParser;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface EventAPIService {

    @POST("events")
    Call<ApiResponse> createEvent(@Body CreateEventRequest request);
    @GET("events/{id}")
    Call<EventInfoResponse> getEventById(@Path("id") String eventId);

    @GET("events/top5")
    Call<List<EventCardResponse>> getTop5Events();

    @GET("events/all")
    Call<PagedResponse<EventCardResponse>> getEvents(@Query("page") int page,
                                                     @Query("size") int size);

    @GET("events/filter")
    Call<PagedResponse<EventCardResponse>> filterEvents(
            @Query("page") int page,
            @Query("size") int size,
            @Query("name") String name,
            @Query("eventTypes") List<String> eventTypes,
            @Query("lowerCapacity") int lowerCapacity,
            @Query("upperCapacity") int upperCapacity,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("sortBy") String sortParameter,
            @Query("sortOrder") String ascending,
            @Query ("owner") String owner
    );

    @GET("events/fetch_users/{userId}")
    Call<List<EventInfoResponse>> fetchUserEvents(@Path("userId") String userId);

    @GET("events/fetch_guests/{eventId}")
    Call<List<GuestResponse>> fetchGuestList(@Path("eventId") String eventId);

    @GET("events/fetch_activities/{eventId}")
    Call<AgendaUpdateRequest> fetchActivities(@Path("eventId") String eventId);

    @GET("events/fetch_guestlist/{eventId}")
    @Streaming
    Call<ResponseBody> fetchGuestListPDF(@Path("eventId") String eventId);

    @GET("events/fetch_agenda/{eventId}")
    @Streaming
    Call<ResponseBody> fetchEventAgendaPDF(@Path("eventId") String id);


    @PUT("events/update")
    Call<String> updateEvent(@Body EventUpdateRequest eventUpdateRequest);

    @PUT("events/update_agenda/{eventId}")
    Call<ApiResponse> updateAgenda( @Path("eventId") String eventId,
                                    @Body AgendaUpdateRequest agendaUpdateRequest);

    @PUT("events/update_guestlist/{eventId}")
    Call<ApiResponse> updateGuestList(@Path("eventId") String eventId,
                                      @Body GuestlistUpdateRequest request);
    @PUT("events/update/invitations/{eventId}")
    Call<ApiResponse> updateInvitations (@Path("eventId") String eventId,
                                         @Body InvitationUpdateRequest request);

    @DELETE("events/delete/{id}")
    Call<String> deleteEvent(@Path("id") String id);

    @POST("events/{eventId}/review")
    Call<String> submitReview(@Path("eventId") String eventId, @Body RequestBody reviewData);

    @GET("events/check-asset")
    Call<Boolean> checkAssetInOrganizedEvents(@Query("userId") String userId, @Query("assetId") String assetId);

    @POST("events/already")
    Call<Boolean> isUserSignedUp(@Body EventSignupRequest eventSignupRequest);

    @PUT("events/signup")
    Call<String> signUserUpToEvent(@Body EventSignupRequest eventSignupRequest);

    @POST("events/leave")
    Call<String> leaveEvent(@Body EventSignupRequest eventSignupRequest);
}
