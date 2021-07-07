package com.xdev.expy.ui.main.profile;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdev.expy.databinding.FragmentProfileBinding;
import com.xdev.expy.ui.main.MainViewModel;
import com.xdev.expy.customview.MyBottomSheetDialogFragment;
import com.xdev.expy.viewmodel.ViewModelFactory;

import static com.xdev.expy.utils.AppUtils.getAvatarFromResource;

public class ProfileFragment extends MyBottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = ProfileFragment.class.getSimpleName();

    private static final String ARG_USER_NAME = "user_name";
    private static final String ARG_USER_EMAIL = "user_email";
    private static final String ARG_USER_PROFILE = "user_profile";

    private FragmentProfileBinding binding;
    private MainViewModel viewModel;

    private String userName;
    private String userEmail;
    private String userProfile;

    public ProfileFragment() {
    }

    @NonNull
    public static ProfileFragment newInstance(String userName, String userEmail, String userProfile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, userName);
        args.putString(ARG_USER_EMAIL, userEmail);
        args.putString(ARG_USER_PROFILE, userProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_USER_NAME);
            userEmail = getArguments().getString(ARG_USER_EMAIL);
            userProfile = getArguments().getString(ARG_USER_PROFILE);
        }
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

        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MainViewModel.class);
        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.layoutButton.setVisibility(View.INVISIBLE);
            } else {
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.layoutButton.setVisibility(View.VISIBLE);
            }
        });

        binding.civProfile.setImageResource(getAvatarFromResource(Uri.parse(userProfile)));
        binding.tvName.setText(userName);
        binding.tvEmail.setText(userEmail);

        binding.btnResetPassword.setOnClickListener(this);
        binding.btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == binding.btnResetPassword.getId()) {
            viewModel.sendPasswordReset(userEmail);
        } else if (id == binding.btnLogout.getId()) {
            viewModel.logout();
        }
    }
}