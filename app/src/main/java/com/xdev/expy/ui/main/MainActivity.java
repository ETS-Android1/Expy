package com.xdev.expy.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayoutMediator;
import com.xdev.expy.databinding.ActivityMainBinding;
import com.xdev.expy.ui.main.about.AboutFragment;
import com.xdev.expy.ui.main.profile.ProfileFragment;
import com.xdev.expy.viewmodel.ViewModelFactory;

import static com.xdev.expy.utils.AppUtils.loadImage;
import static com.xdev.expy.utils.DateUtils.getCurrentDate;
import static com.xdev.expy.utils.DateUtils.getFormattedDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) ->
                tab.setText(pagerAdapter.TAB_TITLES[position])).attach();

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
        if (id == binding.btnAbout.getId()){
            AboutFragment.newInstance().show(getSupportFragmentManager(), AboutFragment.TAG);
        } else if (id == binding.civProfile.getId()){
            ProfileFragment.newInstance().show(getSupportFragmentManager(), ProfileFragment.TAG);
        }
    }
}