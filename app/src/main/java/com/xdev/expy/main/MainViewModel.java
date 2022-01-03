package com.xdev.expy.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import com.google.firebase.firestore.CollectionReference;
import com.xdev.expy.core.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.core.data.source.local.entity.ProductEntity;
import com.xdev.expy.core.data.AuthRepository;
import com.xdev.expy.core.data.ProductRepository;
import com.xdev.expy.core.util.Event;
import com.google.firebase.auth.FirebaseUser;
import com.xdev.expy.core.vo.Resource;

public class MainViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final ProductRepository productRepository;

    public MainViewModel(@NonNull Application application, AuthRepository authRepository, ProductRepository productRepository) {
        super(application);
        this.authRepository = authRepository;
        this.productRepository = productRepository;
        fetchNow(true);
    }

    private final MutableLiveData<Boolean> _fetchNow = new MutableLiveData<>();

    public void fetchNow(boolean state) {
        _fetchNow.setValue(state);
    }

    public LiveData<Resource<PagedList<ProductWithReminders>>> getMonitoredProducts() {
        return Transformations.switchMap(_fetchNow, input ->
                productRepository.getProducts(false, input));
    }

    public LiveData<Resource<PagedList<ProductWithReminders>>> getExpiredProducts() {
        return Transformations.switchMap(_fetchNow, input ->
                productRepository.getProducts(true, input));
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

    public void insertProduct(ProductEntity product) {
        productRepository.insertProduct(product);
    }

    public void updateProduct(ProductEntity product) {
        productRepository.updateProduct(product);
    }

    public void deleteProduct(ProductEntity product) {
        productRepository.deleteProduct(product);
    }

    public CollectionReference getProductsReference() {
        return productRepository.getProductsReference();
    }

    public void setProductsReference(String userId) {
        productRepository.setProductsReference(userId);
    }

    public void sendPasswordReset(String email) {
        authRepository.sendPasswordReset(email);
    }

    public void logout() {
        productRepository.clearDatabase();
        authRepository.logout();
    }
}
