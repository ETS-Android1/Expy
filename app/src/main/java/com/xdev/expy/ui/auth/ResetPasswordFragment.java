package com.xdev.expy.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdev.expy.R;
import com.xdev.expy.databinding.FragmentResetPasswordBinding;
import com.xdev.expy.textwatcher.EmailTextWatcher;
import com.xdev.expy.viewmodel.ViewModelFactory;

import org.jetbrains.annotations.Contract;

import static com.xdev.expy.utils.AppUtils.showToast;

public class ResetPasswordFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private AuthViewModel viewModel;
    private FragmentResetPasswordBinding binding;

    public ResetPasswordFragment() {
    }

    @NonNull
    @Contract(" -> new")
    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class);
        viewModel.isLoading().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnSend.setVisibility(View.INVISIBLE);
            } else {
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.btnSend.setVisibility(View.VISIBLE);
            }
        });

        binding.btnSend.setOnClickListener(v -> {
            if (binding.edtEmail.getText() != null)
                sendResetPassword(binding.edtEmail.getText().toString());
        });
        binding.edtEmail.addTextChangedListener(new EmailTextWatcher(getContext(), binding.tilEmail));
    }

    private void sendResetPassword(String email) {
        if (!isValidForm(email)) {
            showToast(getContext(), getResources().getString(R.string.toast_empty_fields));
            return;
        }
        Log.d(TAG, "sendResetPassword: " + email);
        viewModel.sendPasswordReset(email);
    }

    private boolean isValidForm(String email) {
        return !(email.isEmpty()) && binding.tilEmail.getError() == null;
    }
}