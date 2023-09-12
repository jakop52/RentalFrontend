package com.jakop52.apartmentrent.android.preferences;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class PreferencesManager {
    private static final String PREFERENCES_NAME = "encrypted_prefs";
    private static final String KEY_SESSION_ID = "JSESSIONID";

    private static PreferencesManager instance;
    private final SharedPreferences encryptedSharedPreferences;

    private PreferencesManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            encryptedSharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    PREFERENCES_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up encrypted shared preferences", e);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PreferencesManager must be initialized in Application class first.");
        }
        return instance;
    }

    public String getSessionId() {
        Log.d(TAG, "getSessionId: "+encryptedSharedPreferences.getString(KEY_SESSION_ID, null));
        return encryptedSharedPreferences.getString(KEY_SESSION_ID, null);
    }

    public void setSessionId(String sessionId) {
        Log.d(TAG, "setSessionId: "+KEY_SESSION_ID+sessionId);
        encryptedSharedPreferences.edit().putString(KEY_SESSION_ID, sessionId).apply();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context.getApplicationContext());
        }
    }

    public void clearSession() {
        Log.d(TAG, "clearSession: "+KEY_SESSION_ID);
        encryptedSharedPreferences.edit().remove(KEY_SESSION_ID).apply();
    }
}

