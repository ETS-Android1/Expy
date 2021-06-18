package com.xdev.expy.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import com.google.firebase.firestore.CollectionReference;
import com.xdev.expy.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.data.AuthRepository;
import com.xdev.expy.data.MainRepository;
import com.xdev.expy.utils.Event;
import com.google.firebase.auth.FirebaseUser;
import com.xdev.expy.vo.Resource;

public class MainViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MainRepository mainRepository;

    private LiveData<Resource<PagedList<ProductWithReminders>>> monitoredProductList;
    private LiveData<Resource<PagedList<ProductWithReminders>>> expiredProductList;
    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Event<String>> toastText;

    private final MutableLiveData<Boolean> fetchNow = new MutableLiveData<>();
    public void fetch(boolean fetchNow) {
        this.fetchNow.setValue(fetchNow);
    }

    public MainViewModel(@NonNull Application application, AuthRepository authRepository, MainRepository mainRepository){
        super(application);
        this.authRepository = authRepository;
        this.mainRepository = mainRepository;
    }

    public LiveData<Resource<PagedList<ProductWithReminders>>> getMonitoredProducts(){
        if (monitoredProductList == null) monitoredProductList = Transformations.switchMap(fetchNow,
                input -> mainRepository.getProducts(false, input));
        return monitoredProductList;
    }

    public LiveData<Resource<PagedList<ProductWithReminders>>> getExpiredProducts(){
        if (expiredProductList == null) expiredProductList = Transformations.switchMap(fetchNow,
                input -> mainRepository.getProducts(true, input));
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

    public CollectionReference getProductsReference(){
        return mainRepository.getProductsReference();
    }

    public void setProductsReference(String userId){
        mainRepository.setProductsReference(userId);
    }

    public void sendPasswordReset(String email){
        authRepository.sendPasswordReset(email);
    }

    public void logout(){
        mainRepository.clearDatabase();
        authRepository.logout();
    }
}
