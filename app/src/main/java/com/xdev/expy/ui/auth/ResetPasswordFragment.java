package com.xdev.expy.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdev.expy.databinding.FragmentResetPasswordBinding;
import com.xdev.expy.viewmodel.ViewModelFactory;

import static com.xdev.expy.utils.AppUtils.showToast;

public class ResetPasswordFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private AuthViewModel viewModel;
    private FragmentResetPasswordBinding binding;

    public ResetPasswordFragment() {}

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

        if (getActivity() != null){
            binding.toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

            ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
            viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);
            viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
                if (isLoading) binding.btnSend.setVisibility(View.INVISIBLE);
                else binding.btnSend.setVisibility(View.VISIBLE);
            });
            viewModel.getToastText().observe(getViewLifecycleOwner(), toastText -> {
                String text = toastText.getContentIfNotHandled();
                if (text != null) showToast(getContext(), text);
            });

            // Initialize view
            binding.btnSend.setOnClickListener(v -> {
                if (binding.edtEmail.getText() != null)
                    sendResetPassword(binding.edtEmail.getText().toString());
            });

            binding.edtEmail.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override public void afterTextChanged(Editable editable) {}
                @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    validateEmail();
                }
            });
        }
    }

    private void sendResetPassword(String email){
        if (!isValidForm(email)) {
            showToast(getContext(), "Pastikan semua data lengkap");
            return;
        }
        Log.d(TAG, "sendResetPassword: " + email);
        viewModel.sendPasswordReset(email);
    }

    private boolean isValidForm(String email){
        return !(email.isEmpty()) && binding.tilEmail.getError() == null;
    }

    private void validateEmail() {
        if (binding.edtEmail.getText() == null) return;
        String email = binding.edtEmail.getText().toString();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.tilEmail.setError("Masukkan email yang valid");
        } else binding.tilEmail.setErrorEnabled(false);
    }
}