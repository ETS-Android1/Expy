package com.expy.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.expy.R;
import com.expy.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity implements AuthCallback {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        populateFragment(SignInFragment.newInstance());
    }

    private void populateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(binding.container.getId(), fragment, fragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed(){
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