package com.xdev.expy.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ListenerRegistration;
import com.xdev.expy.R;
import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.databinding.ActivityMainBinding;
import com.xdev.expy.ui.auth.AuthActivity;
import com.xdev.expy.ui.main.about.AboutFragment;
import com.xdev.expy.ui.main.management.AddUpdateFragment;
import com.xdev.expy.ui.main.profile.ProfileFragment;
import com.xdev.expy.viewmodel.ViewModelFactory;

import static com.xdev.expy.utils.AppUtils.loadImage;
import static com.xdev.expy.utils.AppUtils.showToast;
import static com.xdev.expy.utils.DateUtils.getCurrentDate;
import static com.xdev.expy.utils.DateUtils.getFormattedDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainCallback {

    private final String TAG = getClass().getSimpleName();

    private ActivityMainBinding binding;
    private FirebaseUser currentUser;
    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) ->
                tab.setText(pagerAdapter.TAB_TITLES[position])).attach();

        binding.tvDate.setText(getFormattedDate(getCurrentDate(), false));

        binding.btnAbout.setOnClickListener(this);
        binding.civProfile.setOnClickListener(this);

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        MainViewModel viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
        viewModel.getUser().observe(this, user -> {
            this.currentUser = user;
            if (user == null) {
                launchAuth();
            } else {
                loadImage(this, binding.civProfile, user.getPhotoUrl());
                viewModel.setProductsReference(user.getUid());
                if (registration == null) {
                    registration = viewModel.getProductsReference().addSnapshotListener((value, error) -> {
                        if (error != null) Log.w(TAG, "Listen failed", error);
                        else if (value != null){
                            Log.d(TAG, "Changes detected");
                            viewModel.fetchNow(true);
                        }
                    });
                }
            }
        });
        viewModel.getToastText().observe(this, toastText -> {
            String text = toastText.getContentIfNotHandled();
            if (text != null) showToast(this, text);
        });
    }

    private void launchAuth() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == binding.btnAbout.getId()){
            showAbout();
        } else if (id == binding.civProfile.getId()){
            showProfile();
        }
    }

    private void showAbout() {
        AboutFragment.newInstance().show(getSupportFragmentManager(), AboutFragment.TAG);
    }

    private void showProfile() {
        ProfileFragment.newInstance(currentUser.getDisplayName(),
                currentUser.getEmail(),
                currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : ""
        ).show(getSupportFragmentManager(), ProfileFragment.TAG);
    }

    @Override
    public void addUpdateProduct(ProductEntity product) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                .replace(binding.container.getId(), AddUpdateFragment.newInstance(product), AddUpdateFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backToHome(boolean isCancelEditing) {
        if (isCancelEditing) showCancelEditingConfirmDialog();
        else super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Fragment addUpdateFragment = getSupportFragmentManager().findFragmentByTag(AddUpdateFragment.TAG);
        boolean isAddUpdateFragmentVisible = addUpdateFragment != null && addUpdateFragment.isVisible();
        if (isAddUpdateFragmentVisible) showCancelEditingConfirmDialog();
        else super.onBackPressed();
    }

    private void showCancelEditingConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_cancel_editing)
                .setMessage(R.string.dialog_message_cancel_editing)
                .setNeutralButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, (dialogInterface, i) ->
                        super.onBackPressed())
                .create().show();
    }

    public void setContainerBackground(boolean isAddUpdateFragmentVisible){
        if (isAddUpdateFragmentVisible) {
            binding.container.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        } else {
            binding.container.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }
}