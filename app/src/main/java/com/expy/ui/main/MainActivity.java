package com.expy.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.expy.R;
import com.expy.databinding.ActivityMainBinding;
import com.expy.ui.main.about.AboutFragment;
import com.expy.ui.main.profile.ProfileFragment;
import com.expy.viewmodel.ViewModelFactory;

import static com.expy.utils.AppUtils.loadImage;
import static com.expy.utils.DateUtils.getCurrentDate;
import static com.expy.utils.DateUtils.getFormattedDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.expy.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager());
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.setAdapter(pagerAdapter);

        binding.tvDate.setText(getFormattedDate(getCurrentDate(), false));

        binding.btnAbout.setOnClickListener(this);
        binding.civProfile.setOnClickListener(this);

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        MainViewModel viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
        viewModel.getUser().observe(this, user -> {
            if (user != null) {
                loadImage(this, binding.civProfile, user.getPhotoUrl());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_about){
            AboutFragment aboutFragment = new AboutFragment();
            aboutFragment.show(getSupportFragmentManager(), aboutFragment.getTag());
        } else if (id == R.id.civ_profile){
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.show(getSupportFragmentManager(), profileFragment.getTag());
        }
    }
}