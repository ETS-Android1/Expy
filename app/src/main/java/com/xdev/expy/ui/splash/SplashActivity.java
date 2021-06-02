package com.xdev.expy.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.xdev.expy.databinding.ActivitySplashBinding;
import com.xdev.expy.ui.main.MainActivity;
import com.xdev.expy.ui.onboarding.OnboardingActivity;
import com.xdev.expy.ui.onboarding.OnboardingPreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            boolean isFirstTimeLaunch = OnboardingPreference.getInstance(this).isFirstTimeLaunch();
            if (isFirstTimeLaunch) launchOnboarding();
            else launchMain();
        }, 1000);
    }

    private void launchOnboarding() {
        Intent intent = new Intent(this, OnboardingActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}