package com.xdev.expy.ui.auth;

import android.content.Intent;
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

import com.xdev.expy.databinding.FragmentSignUpBinding;
import com.xdev.expy.ui.main.MainActivity;
import com.xdev.expy.viewmodel.ViewModelFactory;

import java.util.regex.Pattern;

import static com.xdev.expy.utils.AppUtils.showToast;

public class SignUpFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private AuthViewModel viewModel;
    private FragmentSignUpBinding binding;

    public SignUpFragment() {}

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null){
            binding.toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

            ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
            viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);
            viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                if (user != null) launchMain();
            });
            viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
                if (isLoading) binding.btnRegister.setVisibility(View.INVISIBLE);
                else binding.btnRegister.setVisibility(View.VISIBLE);
            });
            viewModel.getToastText().observe(getViewLifecycleOwner(), toastText -> {
                String text = toastText.getContentIfNotHandled();
                if (text != null) showToast(getContext(), text);
            });

            // Initialize view
            binding.btnRegister.setOnClickListener(v -> {
                if (binding.edtName.getText() != null &&
                        binding.edtEmail.getText() != null &&
                        binding.edtPassword.getText() != null &&
                        binding.edtPasswordConfirm.getText() != null){
                    registerWithEmail(binding.edtName.getText().toString(),
                            binding.edtEmail.getText().toString(),
                            binding.edtPassword.getText().toString(),
                            binding.edtPasswordConfirm.getText().toString());
                }
            });

            setTextChangedListener();
        }
    }

    private void registerWithEmail(final String name, String email, String password, String passwordConfirm){
        if (!isValidForm(name, email, password, passwordConfirm)) {
            showToast(getContext(), "Pastikan semua data lengkap");
            return;
        }

        Log.d(TAG, "registerWithEmail: " + email);
        // Start create user with email
        viewModel.registerWithEmail(name, email, password);
    }

    private boolean isValidForm(String name, String email, String password, String passwordConfirm){
        return !(name.isEmpty() || email.isEmpty() ||
                password.isEmpty() || passwordConfirm.isEmpty()) &&
                binding.tilEmail.getError() == null &&
                binding.tilName.getError() == null &&
                binding.tilPassword.getError() == null &&
                binding.tilPasswordConfirm.getError() == null;
    }

    private void launchMain(){
        if (getActivity() != null) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void setTextChangedListener() {
        binding.edtName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateName();
            }
        });
        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateEmail();
            }
        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePassword();
                validatePasswordConfirm();
            }
        });
        binding.edtPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validatePasswordConfirm();
            }
        });
    }

    private void validateName() {
        if (binding.edtName.getText() == null) return;
        String name = binding.edtName.getText().toString();
        if (name.isEmpty()){
            binding.tilName.setError("Masukkan nama lengkap");
        } else binding.tilName.setErrorEnabled(false);
    }

    private void validateEmail() {
        if (binding.edtEmail.getText() == null) return;
        String email = binding.edtEmail.getText().toString();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.tilEmail.setError("Masukkan email yang valid");
        } else binding.tilEmail.setErrorEnabled(false);
    }

    private void validatePassword() {
        if (binding.edtPassword.getText() == null) return;
        String password = binding.edtPassword.getText().toString();
        if (password.isEmpty() ||
                !Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$").matcher(password).matches()) {
            binding.tilPassword.setError("Masukkan minimal 6 karakter dengan kombinasi 1 huruf besar, huruf kecil, angka, dan simbol");
        } else binding.tilPassword.setErrorEnabled(false);
    }

    private void validatePasswordConfirm() {
        if (binding.edtPasswordConfirm.getText() == null || binding.edtPassword.getText() == null) return;
        String passwordConfirm = binding.edtPasswordConfirm.getText().toString();
        String password = binding.edtPassword.getText().toString();
        if (!passwordConfirm.equals(password)) {
            binding.tilPasswordConfirm.setError("Kata sandi tidak sama");
        } else binding.tilPasswordConfirm.setErrorEnabled(false);
    }
}