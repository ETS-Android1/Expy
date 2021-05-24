package com.xdev.expy.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xdev.expy.data.AuthRepository;
import com.xdev.expy.utils.Event;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Event<String>> toastText;

    public AuthViewModel(@NonNull Application application, AuthRepository authRepository){
        super(application);
        this.authRepository = authRepository;
    }

    public LiveData<FirebaseUser> getUser(){
        if (user == null) user = authRepository.getUser();
        return user;
    }

    public LiveData<Boolean> isLoading(){
        if (isLoading == null) isLoading = authRepository.isLoading();
        return isLoading;
    }

    public MutableLiveData<Event<String>> getToastText() {
        if (toastText == null) toastText = authRepository.getToastText();
        return toastText;
    }

    public void authWithGoogle(AuthCredential authCredential){
        authRepository.authWithGoogle(authCredential);
    }

    public void registerWithEmail(String name, String email, String password){
        authRepository.registerWithEmail(name, email, password);
    }

    public void loginWithEmail(String email, String password){
        authRepository.loginWithEmail(email, password);
    }

    public void sendPasswordReset(String email){
        authRepository.sendPasswordReset(email);
    }
}
