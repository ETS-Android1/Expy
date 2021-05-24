package com.xdev.expy.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xdev.expy.data.source.remote.ApiResponse;
import com.xdev.expy.data.source.remote.entity.ProductEntity;
import com.xdev.expy.data.AuthRepository;
import com.xdev.expy.data.MainRepository;
import com.xdev.expy.utils.Event;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MainRepository mainRepository;

    private LiveData<ApiResponse<List<ProductEntity>>> monitoredProduct;
    private LiveData<ApiResponse<List<ProductEntity>>> expiredProduct;
    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Event<String>> toastText;

    public MainViewModel(@NonNull Application application, AuthRepository authRepository, MainRepository mainRepository){
        super(application);
        this.authRepository = authRepository;
        this.mainRepository = mainRepository;
    }

    public LiveData<ApiResponse<List<ProductEntity>>> getMonitoredProduct(){
        if (monitoredProduct == null) monitoredProduct = mainRepository.queryMonitoredProducts();
        return monitoredProduct;
    }

    public LiveData<ApiResponse<List<ProductEntity>>> getExpiredProduct(){
        if (expiredProduct == null) expiredProduct = mainRepository.queryExpiredProducts();
        return expiredProduct;
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
