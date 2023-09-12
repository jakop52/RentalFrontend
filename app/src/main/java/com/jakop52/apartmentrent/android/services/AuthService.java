package com.jakop52.apartmentrent.android.services;

import com.jakop52.apartmentrent.android.dto.LoginRequest;
import com.jakop52.apartmentrent.android.dto.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<Void> login(@Body LoginRequest loginRequest);

    @POST("auth/signup")
    Call<Void> register(@Body RegisterRequest registerRequest);
    @GET("http://217.182.78.197:8080/logout")
    Call<Void> logout();

}
