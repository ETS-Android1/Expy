package com.xdev.expy.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xdev.expy.R;
import com.xdev.expy.databinding.ActivityOnboardingBinding;
import com.xdev.expy.ui.auth.AuthActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOnboardingBinding binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        OnboardingAdapter adapter = new OnboardingAdapter();
        binding.viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager2(binding.viewPager);

        populateViewPager(adapter);

        // Disable overscroll animation
        View child = binding.viewPager.getChildAt(0);
        if (child instanceof RecyclerView) child.setOverScrollMode(View.OVER_SCROLL_NEVER);

        binding.btnStart.setOnClickListener(view -> {
            Intent intent = new Intent(OnboardingActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void populateViewPager(OnboardingAdapter adapter) {
        List<Onboarding> onboardingList = new ArrayList<>();

        Onboarding onboarding1 = new Onboarding(R.drawable.illustration_onboarding_1,
                "Pantau Tanggal Kedaluwarsa",
                "Catat tanggal kedaluwarsa biar kamu enggak lagi lupa");

        Onboarding onboarding2 = new Onboarding(R.drawable.illustration_onboarding_2,
                "Dapatkan Notifikasi",
                "Terima notifikasi berkala ketika mendekati tanggal kedaluwarsa");

        Onboarding onboarding3 = new Onboarding(R.drawable.illustration_onboarding_3,
                "Lebih Mudah dengan Widget",
                "Pasang widget di home screen untuk akses pantau yang lebih cepat");

        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);
        onboardingList.add(onboarding3);

        adapter.submitList(onboardingList);
    }
}