package com.expy.ui.management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.expy.databinding.ActivityAddUpdateBinding;

public class AddUpdateActivity extends AppCompatActivity {

    private ActivityAddUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvHelpPao.setOnClickListener(view -> {
            HelpPAOFragment helpPAOFragment = new HelpPAOFragment();
            helpPAOFragment.show(getSupportFragmentManager(), helpPAOFragment.getTag());
        });
    }
}