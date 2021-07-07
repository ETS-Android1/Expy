package com.xdev.expy.ui.onboarding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class OnboardingPreference {

    private volatile static OnboardingPreference INSTANCE = null;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private static final String PREFERENCE_NAME = "onboarding_preference";
    private static final String IS_FIRST_TIME_LAUNCH = "is_first_time_launch";

    @SuppressLint("CommitPrefEdits")
    private OnboardingPreference(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static OnboardingPreference getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (OnboardingPreference.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OnboardingPreference(context);
                }
            }
        }
        return INSTANCE;
    }

    public void setIsFirstTimeLaunch(boolean isFirstTimeLaunch) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTimeLaunch);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
