package com.jakop52.apartmentrent.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;

public class LauncherActivity extends AppCompatActivity {
    private boolean isAppRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (isAppRunning) {
            navigateTo(MainActivity.class);
        } else {
            setContentView(R.layout.activity_launcher);
            isAppRunning = true;
            new Handler().postDelayed(() -> {
                String jSessionID = PreferencesManager.getInstance().getSessionId();

                if (jSessionID != null && !jSessionID.isEmpty()) {
                    navigateTo(MainActivity.class);
                } else {
                    navigateTo(LoginActivity.class);
                }
            }, 2000);
        }
        setContentView(R.layout.activity_launcher);
    }
    @Override
    protected void onStart() {
        super.onStart();
        isAppRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAppRunning = false;
    }
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(LauncherActivity.this, targetActivity);
        startActivity(intent);
        finish();
    }
}
