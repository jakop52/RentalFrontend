package com.jakop52.apartmentrent.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakop52.apartmentrent.android.api.adapters.ZonedDateTimeAdapter;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;


import java.io.IOException;
import java.time.ZonedDateTime;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://217.182.78.197:8080/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String sessionId = PreferencesManager.getInstance().getSessionId();

                if (sessionId != null) {
                    Request newRequest = originalRequest.newBuilder()
                            .header("Cookie", sessionId)
                            .build();
                    return chain.proceed(newRequest);
                }

                return chain.proceed(originalRequest);
            }
        });

        OkHttpClient client = httpClientBuilder.build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
