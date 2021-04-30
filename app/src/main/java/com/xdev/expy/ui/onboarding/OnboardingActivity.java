package com.xdev.expy.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.xdev.expy.R;
import com.xdev.expy.data.Onboarding;
import com.xdev.expy.ui.auth.AuthActivity;
import com.xdev.expy.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private OnboardingAdapter adapter;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        setupItemOnboarding();

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        DotsIndicator dots = findViewById(R.id.dot_indicator);
        dots.setViewPager2(viewPager);

        btnStart = findViewById(R.id.button_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardingActivity.this, AuthActivity.class));
                finish();
            }
        });
    }

    private void setupItemOnboarding() {
        List<Onboarding> onboardingList = new ArrayList<>();

        Onboarding itemStar = new Onboarding();
        itemStar.setTitle("Pantau Tanggal Kedaluwarsa");
        itemStar.setDescription("Catat tanggal kedaluwarsa biar kamu enggak lagi lupa");
        itemStar.setImage(R.drawable.illustration_onboarding_1);

        Onboarding itemTarget = new Onboarding();
        itemTarget.setTitle("Dapatkan Notifikasi");
        itemTarget.setDescription("Terima notifikasi berkala ketika mendekati tanggal kedaluwarsa");
        itemTarget.setImage(R.drawable.illustration_onboarding_2);

        Onboarding itemNotification = new Onboarding();
        itemNotification.setTitle("Lebih Mudah dengan Widget");
        itemNotification.setDescription("Pasang widget di home screen untuk akses pantau yang lebih cepat");
        itemNotification.setImage(R.drawable.illustration_onboarding_3);

        onboardingList.add(itemStar);
        onboardingList.add(itemTarget);
        onboardingList.add(itemNotification);

        adapter = new OnboardingAdapter(onboardingList);

    }

}