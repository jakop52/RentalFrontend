package com.jakop52.apartmentrent.android.api;

import com.jakop52.apartmentrent.android.preferences.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            for (String header : originalResponse.headers("Set-Cookie")) {
                if (header.contains("JSESSIONID")) {
                    String sessionId = header.split(";")[0];
                    PreferencesManager.getInstance().setSessionId(sessionId);
                    break;
                }
            }
        }
        return originalResponse;
    }
}
