package com.xdev.expy.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.xdev.expy.R;
import com.xdev.expy.databinding.ActivityAuthBinding;
import com.xdev.expy.ui.main.MainActivity;
import com.xdev.expy.viewmodel.ViewModelFactory;

import static com.xdev.expy.utils.AppUtils.showToast;

public class AuthActivity extends AppCompatActivity implements AuthCallback {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        AuthViewModel viewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);
        viewModel.getUser().observe(this, user -> {
            if (user != null) launchMain();
        });
        viewModel.getToastText().observe(this, toastText -> {
            String text = toastText.getContentIfNotHandled();
            if (text != null) showToast(this, text);
        });

        populateFragment(SignInFragment.newInstance());
    }

    private void launchMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void populateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(binding.container.getId(), fragment, fragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void moveTo(Fragment fragment) {
        populateFragment(fragment);
    }
}