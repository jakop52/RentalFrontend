package com.jakop52.apartmentrent.android.services;

import com.jakop52.apartmentrent.android.dto.UserDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserRepository {
    @GET("users/me")
    Call<UserDto> getUserData(@Header("Cookie") String cookie);

}
