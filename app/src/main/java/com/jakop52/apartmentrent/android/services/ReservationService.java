package com.jakop52.apartmentrent.android.services;

import com.jakop52.apartmentrent.android.dto.PaymentDto;
import com.jakop52.apartmentrent.android.dto.ReservationDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ReservationService {
    @POST("reservations")
    Call<ReservationDto> addReservation(@Body ReservationDto reservationDto, @Header("Cookie") String cookie);

    @GET("reservations/me")
    Call<List<ReservationDto>> getUserReservations(@Header("Cookie") String cookie);

    @PUT("reservations/{id}/confirm")
    Call<ReservationDto> confirmReservation(@Path("id") Long reservationId);

    @DELETE("reservations/{id}")
    Call<Void> deleteReservation(@Path("id") Long id);

    @GET("reservations/{id}/payments")
    Call<List<PaymentDto>> getReservationPayments(@Path("id") Long reservationId);
}
