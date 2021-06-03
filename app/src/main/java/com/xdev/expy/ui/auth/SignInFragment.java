package com.xdev.expy.ui.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdev.expy.R;
import com.xdev.expy.databinding.FragmentSignInBinding;
import com.xdev.expy.textwatcher.EmailTextWatcher;
import com.xdev.expy.textwatcher.PasswordTextWatcher;
import com.xdev.expy.viewmodel.ViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.xdev.expy.utils.AppUtils.showToast;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private AuthCallback callback;
    private AuthViewModel viewModel;
    private FragmentSignInBinding binding;
    private GoogleSignInClient googleSignInClient;

    public SignInFragment() {}

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) authWithGoogle(account);
                        } catch (ApiException e){
                            // Google Sign In failed or user press back button
                            Log.w(TAG, "Google sign in failed", e);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelFactory factory = ViewModelFactory.getInstance(requireActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class);
        viewModel.isLoading().observe(requireActivity(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.layoutButton.setVisibility(View.INVISIBLE);
            }
            else {
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.layoutButton.setVisibility(View.VISIBLE);
            }
        });

        if (getContext() != null){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getResources().getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        }

        binding.btnLogin.setOnClickListener(this);
        binding.btnGoogle.setOnClickListener(this);
        binding.tvResetPassword.setOnClickListener(this);
        binding.tvRegister.setOnClickListener(this);
        binding.edtEmail.addTextChangedListener(new EmailTextWatcher(getContext(), binding.tilEmail));
        binding.edtPassword.addTextChangedListener(new PasswordTextWatcher(getContext(), binding.tilPassword));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (AuthCallback) context;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == binding.btnLogin.getId()){
            if (binding.edtEmail.getText() != null && binding.edtPassword.getText() != null){
                loginWithEmail(binding.edtEmail.getText().toString(),
                        binding.edtPassword.getText().toString());
            }
        } else if (id == binding.btnGoogle.getId()){
            loginWithGoogle();
        } else if (id == binding.tvResetPassword.getId()){
            callback.moveTo(ResetPasswordFragment.newInstance());
        } else if (id == binding.tvRegister.getId()){
            callback.moveTo(SignUpFragment.newInstance());
        }
    }

    private void loginWithEmail(String email, String password){
        if (!isValidForm(email, password)){
            showToast(getContext(), getResources().getString(R.string.toast_empty_fields));
            return;
        }

        Log.d(TAG, "loginWithEmail: " + email);
        viewModel.loginWithEmail(email, password);
    }

    private void loginWithGoogle() {
        Intent intentGoogle = googleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(intentGoogle);
    }

    private void authWithGoogle(GoogleSignInAccount account){
        Log.d(TAG, "authWithGoogle: " + account.getId());
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        viewModel.authWithGoogle(authCredential);
    }

    private boolean isValidForm(String email, String password){
        return !(email.isEmpty() || password.isEmpty()) &&
                binding.tilEmail.getError() == null &&
                binding.tilPassword.getError() == null;
    }
}