package com.xdev.expy.ui.management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xdev.expy.databinding.ActivityAddUpdateBinding;

public class AddUpdateActivity extends AppCompatActivity {

    private ActivityAddUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvHelpPao.setOnClickListener(view ->
                HelpPAOFragment.newInstance().show(getSupportFragmentManager(), HelpPAOFragment.TAG));
    }
}