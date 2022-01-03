package com.xdev.expy.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.xdev.expy.R;
import com.xdev.expy.databinding.ActivityOnboardingBinding;
import com.xdev.expy.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private ViewPager2.OnPageChangeCallback onPageChangeCallback;

    private Handler handler;
    private Runnable runnable;
    private int delay, page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        OnboardingAdapter adapter = new OnboardingAdapter();
        binding.viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager2(binding.viewPager);

        populateViewPager(adapter);
        setAutoScrollViewPager(binding.viewPager, adapter);
        disableViewPagerOverscrollAnimation(binding.viewPager);

        binding.btnStart.setOnClickListener(view -> launchMain());
    }

    private void launchMain() {
        OnboardingPreference.getInstance(this).setIsFirstTimeLaunch(false);
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void populateViewPager(@NonNull OnboardingAdapter adapter) {
        List<Onboarding> onboardingList = new ArrayList<>();

        Onboarding onboarding1 = new Onboarding(R.drawable.illustration_onboarding_1,
                getResources().getString(R.string.title_onboarding_1),
                getResources().getString(R.string.desc_onboarding_1));
        Onboarding onboarding2 = new Onboarding(R.drawable.illustration_onboarding_2,
                getResources().getString(R.string.title_onboarding_2),
                getResources().getString(R.string.desc_onboarding_2));
        Onboarding onboarding3 = new Onboarding(R.drawable.illustration_onboarding_3,
                getResources().getString(R.string.title_onboarding_3),
                getResources().getString(R.string.desc_onboarding_3));

        onboardingList.add(onboarding1);
        onboardingList.add(onboarding2);
        onboardingList.add(onboarding3);

        adapter.submitList(onboardingList);
    }

    private void setAutoScrollViewPager(@NonNull ViewPager2 viewPager, OnboardingAdapter adapter) {
        handler = new Handler();
        delay = 5000; // Milliseconds
        page = 0;
        runnable = new Runnable() {
            public void run() {
                if (adapter.getItemCount() == page) page = 0;
                else page++;
                viewPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
        };

        onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                page = position;
            }
        };
        viewPager.registerOnPageChangeCallback(onPageChangeCallback);
    }

    private void disableViewPagerOverscrollAnimation(ViewPager2 viewPager) {
        View child = viewPager.getChildAt(0);
        if (child instanceof RecyclerView) child.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
    }
}