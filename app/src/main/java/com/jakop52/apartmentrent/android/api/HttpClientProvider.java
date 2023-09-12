package com.jakop52.apartmentrent.android.api;

import com.jakop52.apartmentrent.android.preferences.PreferencesManager;

import okhttp3.OkHttpClient;

public class HttpClientProvider {
    private static OkHttpClient httpClient;

    public static OkHttpClient getClient() {
        if (httpClient == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            PreferencesManager preferencesManager = PreferencesManager.getInstance();
            if (preferencesManager.getSessionId() != null) {
                ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor();
                httpClientBuilder.addInterceptor(receivedCookiesInterceptor);
            }

            httpClient = httpClientBuilder.build();
        }
        return httpClient;
    }
}

