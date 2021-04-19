package com.expy.ui.main.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expy.R;
import com.expy.databinding.FragmentMonitoredBinding;
import com.expy.ui.management.AddUpdateActivity;

public class MonitoredFragment extends Fragment {

    private FragmentMonitoredBinding binding;

    public MonitoredFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMonitoredBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddUpdateActivity.class);
            startActivity(intent);
        });
    }
}