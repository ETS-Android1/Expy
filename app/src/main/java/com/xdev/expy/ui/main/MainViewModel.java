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

    private LiveData<ApiResponse<List<ProductEntity>>> monitoredProductList;
    private LiveData<ApiResponse<List<ProductEntity>>> expiredProductList;
    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Event<String>> toastText;

    public MainViewModel(@NonNull Application application, AuthRepository authRepository, MainRepository mainRepository){
        super(application);
        this.authRepository = authRepository;
        this.mainRepository = mainRepository;
    }

    public LiveData<ApiResponse<List<ProductEntity>>> getMonitoredProducts(){
        if (monitoredProductList == null) monitoredProductList = mainRepository.queryMonitoredProducts();
        return monitoredProductList;
    }

    public LiveData<ApiResponse<List<ProductEntity>>> getExpiredProducts(){
        if (expiredProductList == null) expiredProductList = mainRepository.queryExpiredProducts();
        return expiredProductList;
    }

    public LiveData<FirebaseUser> getUser(){
        if (user == null) user = authRepository.getUser();
        return user;
    }

    public LiveData<Boolean> isLoading(){
        if (isLoading == null) isLoading = authRepository.isLoading();
        return isLoading;
    }

    public LiveData<Event<String>> getToastText() {
        if (toastText == null) toastText = authRepository.getToastText();
        return toastText;
    }

    public void insertProduct(ProductEntity product){
        mainRepository.insertProduct(product);
    }

    public void updateProduct(ProductEntity product){
        mainRepository.updateProduct(product);
    }

    public void deleteProduct(ProductEntity product){
        mainRepository.deleteProduct(product);
    }

    public void addProductsSnapshotListener(){
        mainRepository.addProductsSnapshotListener();
    }

    public void sendPasswordReset(String email){
        authRepository.sendPasswordReset(email);
    }

    public void logout(){
        authRepository.logout();
    }
}
