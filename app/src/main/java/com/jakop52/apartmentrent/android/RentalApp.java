package com.jakop52.apartmentrent.android;

import android.app.Application;

import com.jakop52.apartmentrent.android.preferences.PreferencesManager;

public class RentalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.init(this);
    }
}
