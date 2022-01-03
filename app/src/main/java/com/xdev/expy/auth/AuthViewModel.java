package com.xdev.expy.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.xdev.expy.core.data.AuthRepository;
import com.xdev.expy.core.util.Event;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application, AuthRepository authRepository) {
        super(application);
        this.authRepository = authRepository;
    }

    public LiveData<FirebaseUser> getUser() {
        return authRepository.getUser();
    }

    public LiveData<Boolean> isLoading() {
        return authRepository.isLoading();
    }

    public LiveData<Event<String>> getToastText() {
        return authRepository.getToastText();
    }

    public void authWithGoogle(AuthCredential authCredential) {
        authRepository.authWithGoogle(authCredential);
    }

    public void registerWithEmail(String name, String email, String password) {
        authRepository.registerWithEmail(name, email, password);
    }

    public void loginWithEmail(String email, String password) {
        authRepository.loginWithEmail(email, password);
    }

    public void sendPasswordReset(String email) {
        authRepository.sendPasswordReset(email);
    }
}
