package com.xdev.expy.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xdev.expy.data.source.AuthRepository;
import com.xdev.expy.utils.Event;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Event<String>> toastText;

    public MainViewModel(@NonNull Application application, AuthRepository authRepository){
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

    public void sendPasswordReset(String email){
        authRepository.sendPasswordReset(email);
    }

    public void logout(){
        authRepository.logout();
    }
}
