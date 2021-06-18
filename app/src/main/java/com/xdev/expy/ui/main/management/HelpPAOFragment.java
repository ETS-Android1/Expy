package com.xdev.expy.ui.main.management;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdev.expy.databinding.FragmentHelpPaoBinding;
import com.xdev.expy.utils.MyBottomSheetDialogFragment;

import org.jetbrains.annotations.Contract;

public class HelpPAOFragment extends MyBottomSheetDialogFragment {

    public static final String TAG = HelpPAOFragment.class.getSimpleName();

    private FragmentHelpPaoBinding binding;

    public HelpPAOFragment() {}

    @NonNull
    @Contract(" -> new")
    public static HelpPAOFragment newInstance() {
        return new HelpPAOFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHelpPaoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(v -> dismiss());
    }
}