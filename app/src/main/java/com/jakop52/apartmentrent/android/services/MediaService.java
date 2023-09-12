package com.jakop52.apartmentrent.android.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MediaService {
    @GET("media/{id}/download")
    Call<ResponseBody> downloadMediaFile(@Path("id") Long mediaId);
}
