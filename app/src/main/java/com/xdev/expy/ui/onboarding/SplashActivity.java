package com.xdev.expy.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.xdev.expy.R;
import com.xdev.expy.ui.auth.AuthActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }
}