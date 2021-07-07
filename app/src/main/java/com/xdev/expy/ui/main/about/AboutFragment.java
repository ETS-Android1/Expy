package com.xdev.expy.ui.main.about;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdev.expy.databinding.FragmentAboutBinding;
import com.xdev.expy.customview.MyBottomSheetDialogFragment;

import org.jetbrains.annotations.Contract;

public class AboutFragment extends MyBottomSheetDialogFragment {

    public static final String TAG = AboutFragment.class.getSimpleName();

    private FragmentAboutBinding binding;

    public AboutFragment() {
    }

    @NonNull
    @Contract(" -> new")
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(v -> dismiss());
    }
}