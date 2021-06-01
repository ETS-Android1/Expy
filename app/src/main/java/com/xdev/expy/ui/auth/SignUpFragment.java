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

import com.xdev.expy.databinding.FragmentSignUpBinding;
import com.xdev.expy.textwatcher.CreatePasswordTextWatcher;
import com.xdev.expy.textwatcher.EmailTextWatcher;
import com.xdev.expy.textwatcher.PasswordConfirmationTextWatcher;
import com.xdev.expy.textwatcher.PersonNameTextWatcher;
import com.xdev.expy.viewmodel.ViewModelFactory;

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

        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class);
        viewModel.isLoading().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnRegister.setVisibility(View.INVISIBLE);
            }
            else {
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.btnRegister.setVisibility(View.VISIBLE);
            }
        });

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

        binding.edtName.addTextChangedListener(new PersonNameTextWatcher(binding.tilName));
        binding.edtEmail.addTextChangedListener(new EmailTextWatcher(binding.tilEmail));
        binding.edtPassword.addTextChangedListener(new CreatePasswordTextWatcher(binding.tilPassword, binding.tilPasswordConfirm, binding.edtPasswordConfirm));
        binding.edtPasswordConfirm.addTextChangedListener(new PasswordConfirmationTextWatcher(binding.tilPasswordConfirm, binding.edtPassword));
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
}