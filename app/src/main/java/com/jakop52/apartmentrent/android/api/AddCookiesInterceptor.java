package com.jakop52.apartmentrent.android.api;

import android.util.Log;

import com.jakop52.apartmentrent.android.preferences.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    private final String sessionId;

    public AddCookiesInterceptor(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (sessionId != null) {
            builder.addHeader("Cookie", sessionId);
        }
        return chain.proceed(builder.build());
    }
}