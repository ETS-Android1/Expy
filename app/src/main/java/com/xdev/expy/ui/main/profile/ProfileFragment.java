package com.xdev.expy.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdev.expy.data.source.remote.RemoteDataSource;
import com.xdev.expy.databinding.FragmentProfileBinding;
import com.xdev.expy.ui.main.MainViewModel;
import com.xdev.expy.ui.onboarding.SplashActivity;
import com.xdev.expy.utils.MyBottomSheetDialogFragment;
import com.xdev.expy.viewmodel.ViewModelFactory;
import com.google.firebase.auth.FirebaseUser;

import static com.xdev.expy.utils.AppUtils.loadImage;
import static com.xdev.expy.utils.AppUtils.showToast;

public class ProfileFragment extends MyBottomSheetDialogFragment implements View.OnClickListener{

    public static final String TAG = ProfileFragment.class.getSimpleName();

    private FirebaseUser firebaseUser;
    private FragmentProfileBinding binding;
    private MainViewModel viewModel;

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(v -> dismiss());

        if (getActivity() != null){
            ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication(), RemoteDataSource.getInstance());
            viewModel = new ViewModelProvider(requireActivity(), factory).get(MainViewModel.class);
            viewModel.getUser().observe(this, user -> {
                firebaseUser = user;
                if (user == null) restartApp();
                else {
                    loadImage(getContext(), binding.civProfile, user.getPhotoUrl());
                    binding.tvName.setText(user.getDisplayName());
                    binding.tvEmail.setText(user.getEmail());
                }
            });
            viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
                if (isLoading) binding.btnResetPassword.setVisibility(View.INVISIBLE);
                else binding.btnResetPassword.setVisibility(View.VISIBLE);
            });
            viewModel.getToastText().observe(getViewLifecycleOwner(), toastText -> {
                String text = toastText.getContentIfNotHandled();
                if (text != null) showToast(getContext(), text);
            });

            binding.btnResetPassword.setOnClickListener(this);
            binding.btnLogout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == binding.btnResetPassword.getId()){
            if (firebaseUser != null){
                viewModel.sendPasswordReset(firebaseUser.getEmail());
            } else {
                showToast(getContext(),
                        "Tidak bisa mengirim tautan ganti kata sandi ke email, coba lagi");
            }
        } else if (id == binding.btnLogout.getId()){
            viewModel.logout();
        }
    }

    private void restartApp() {
        if (getActivity() != null){
            Intent intent = new Intent(getContext(), SplashActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}