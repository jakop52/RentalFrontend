package com.jakop52.apartmentrent.android.services;

import com.jakop52.apartmentrent.android.dto.ApartmentDto;
import com.jakop52.apartmentrent.android.dto.ReservationDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApartmentService {
    @GET("apartments/my")
    Call<List<ApartmentDto>> getMyApartments(@Header("Cookie") String cookie);
    @GET("apartments/my/rented")
    Call<List<ApartmentDto>> getRentedByMe(@Header("Cookie") String cookie);


    @GET("apartments")
    Call<List<ApartmentDto>> getAllApartments(@Header("Cookie") String cookie);


    @GET("apartments/{id}/is-rentable")
    Call<Boolean> isApartmentRentable(@Path("id") long apartmentId, @Header("Cookie") String cookie);

    @GET("apartments/{id}/is-owner")
    Call<Boolean> isApartmentOwner(@Path("id") long apartmentId, @Header("Cookie") String cookie);

    @GET("apartments/{id}/reservations")
    Call<List<ReservationDto>> getApartmentReservations(@Path("id") long apartmentId);

    @POST("apartments")
    Call<ApartmentDto> addApartment(@Body ApartmentDto apartmentDto);

    @GET("apartments/{id}")
    Call<ApartmentDto> getApartmentDetails(@Path("id") long apartmentId);

    @DELETE("apartments/{id}")
    Call<Void> deleteOwnApartment(@Path("id") long apartmentId, @Header("Cookie") String cookie);
}
